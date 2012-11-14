<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<%@ page session="true"%>
<%@ page import="vsp.*"%>
<%@ page import="vsp.dataObject.*"%>
<%@ page import="vsp.utils.*"%>
<%@ page import="java.util.*"%>
<%@ page import="java.text.*"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<style type="text/css">@import url("css/main.css")</style>
<title>VSP - Portfolio</title>
<body>
  <%@ include file="headers/LoggedInHeader.jsp" %>
 
  <table width=100%>
  <tr><td colspan=2>Welcome, ${pageContext.request.remoteUser}</td></tr>
  <tr><td>&nbsp;</td><td align=center><b>Portfolio</b> | <a href="stockSearch">Stock Search</a> | 
  <a href="order">Order Stock</a> | <a href="pendingOrders">Pending Orders</a> | 
  <a href="transactionHistory">Transaction History</a> | <a href="viewUserInfo">User Info</a></td></tr>
  </table>
 
 
<%
	try
	{
		String userName = request.getRemoteUser();
		VspServiceProvider vsp = new VspServiceProvider();
		
	    // try to execute any pending orders
	    try
	    {
	    	vsp.processPendingOrders(userName);
	    }
	    catch (Exception ex)
	    {
	    	// ignore
	    }
	    
	    // try to credit any owed dividends
	    try
	    {
	    	vsp.processDividends(userName);
	    }
	    catch (Exception ex)
	    {
	    	// ignore
	    }
	    
	    DecimalFormat df = new DecimalFormat("###,###,##0.00");
		DecimalFormatSymbols dfs = new DecimalFormatSymbols();
		dfs.setGroupingSeparator(',');
		df.setDecimalFormatSymbols(dfs);
		
		AccountData data = vsp.getAccountInfo(userName);
	    
		double result;
		double totalValue = data.getBalance();
		double totalChangeSincePurchase = 0;
		double totalChangeSinceClose = 0;
		double totalCostBasis = 0;
		double totalCloseValue = 0;
	    
		out.println("<div id='portfolio'>");
		out.println("<h3 align=center>Portfolio</h3>");
		out.println("<table align=center border=1 cellpadding=4 cellspacing=0>");
		
		out.println("<tr>");
		out.println("<td colspan=3 align=center>Stock Holding</td>");
		out.println("<td colspan=3 align=center>Most Recent</td>");
		out.println("<td colspan=2 align=center>Change Since Close</td>");
		out.println("<td colspan=2 align=center>Change Since Purchase</td>");
		out.println("<td colspan=2 align=center>Cost Basis</td>");
		out.println("<td colspan=1 align=center>&nbsp;</td>");
		out.println("<td colspan=1 align=center>&nbsp;</td>");
		out.println("</tr>");
		
		out.println("<tr>");
		out.println("<td align=center><u>Symbol</u></td>");
		out.println("<td align=center><u>Name</u></td>");
		out.println("<td align=center><u>Quantity</u></td>");
		out.println("<td align=center><u>Price</u></td>");
		out.println("<td align=center><u>Change</u></td>");
		out.println("<td align=center><u>Value</u></td>");
		out.println("<td align=center><u>Dollar</u></td>");
		out.println("<td align=center><u>Percent</u></td>");
		out.println("<td align=center><u>Dollar</u></td>");
		out.println("<td align=center><u>Percent</u></td>");
		out.println("<td align=center><u>Share</u></td>");
		out.println("<td align=center><u>Total</u></td>");
		out.println("<td align=center><u>Action</u></td>");
		out.println("<td align=center><u>Details</u></td>");
		out.println("</tr>");
		
		out.println("<tr>");
		out.println("<td>Cash</td>");
		out.println("<td>Trading Reserves</td>");
		out.println("<td align=right>" + df.format(data.getBalance()) + "</td>");
		out.println("<td align=right>$1.00</td>");
		out.println("<td align=right>$0.00</td>");
		out.println("<td align=right>$" + df.format(data.getBalance()) + "</td>");
		out.println("<td align=center> -- </td>");
		out.println("<td align=center> -- </td>");
		out.println("<td align=center> -- </td>");
		out.println("<td align=center> -- </td>");
		out.println("<td align=center> -- </td>");
		out.println("<td align=center> -- </td>");
		out.println("<td align=center>&nbsp</td>");
		out.println("<td align=center>&nbsp</td>");
		out.println("</tr>");
		
		// throws on error
		List<PortfolioData> portfolioItems = vsp.getPortfolioEntries(userName);
		if (portfolioItems != null && portfolioItems.size() > 0)
		{			
			// build stock symbol list
			List<String> symbols = new ArrayList<String>();
			for (PortfolioData item : portfolioItems)
			{
				symbols.add(item.getStock().getStockSymbol());
			}
			
			// try to get latest stock information for all symbols
			Map<String,StockInfo> stockInfoMap = vsp.getLatestStockInfo(symbols);
			
			// format and print items to screen			
			String symbol;
			StockInfo latestInfo;
			for (PortfolioData item : portfolioItems)
			{
				symbol = item.getStock().getStockSymbol();
				
				out.println("<tr>");
								
				out.println("<td>" + symbol + "</td>");
				out.println("<td>" + item.getStock().getStockDescription() + "</td>");
				out.println("<td align=right>" + df.format(item.getQuantity()) + "</td>");
				
				// information based on latest stock data
				if (stockInfoMap.containsKey(symbol))
				{
					latestInfo = stockInfoMap.get(symbol);
					
					// latest price
					out.println("<td align=right>$" + df.format(latestInfo.getLastTradePrice()) + "</td>");
					// change in price
					out.println("<td align=right>" + VSPUtils.formatColor(latestInfo.getPriceChangeSinceOpen(), df) + "</td>");
					// latest value
					result = latestInfo.getLastTradePrice() * item.getQuantity();
					totalValue += result;
					out.println("<td align=right>$" + df.format(result) + "</td>");
					
					// change since close (value)
					totalCloseValue += latestInfo.getClose() * item.getQuantity();
					result = latestInfo.getChangeSinceClosePrice() * item.getQuantity();
					totalChangeSinceClose += result;
					out.println("<td align=right>" + VSPUtils.formatColor(result, df) + "</td>");
					// change since close (percent)
					out.println("<td align=right>" + VSPUtils.formatColor(latestInfo.getChangeSinceClosePercent(), df, true) + "</td>");
					
					// change since purchase (value)
					result = (latestInfo.getLastTradePrice() * item.getQuantity()) - item.getCostBasis();
					totalChangeSincePurchase += result;
					out.println("<td align=right>" + VSPUtils.formatColor(result, df) + "</td>");
					// change since purchase (percent)
					result = (result / item.getCostBasis()) * 100.0;
					out.println("<td align=right>" + VSPUtils.formatColor(result, df, true) + "</td>");
				}
				else
				{ // data not available
					out.println("<td>N/A</td>");
					out.println("<td>N/A</td>");
					out.println("<td>N/A</td>");
					out.println("<td>N/A</td>");
					out.println("<td>N/A</td>");
					out.println("<td>N/A</td>");
					out.println("<td>N/A</td>");
				}
				
				// cost basis
				out.println("<td align=right>$" + df.format(item.getCostBasisPerShare()) + "</td>");
				result = item.getCostBasis();
				totalCostBasis += result;
				out.println("<td align=right>$" + df.format(result) + "</td>");
				
				// sell link
				out.println("<td align=center><a href='order?action=1&symbol=" + symbol + "'>Sell</a></td>");
				
				//details link
				out.println("<td align=center><a href='viewStockDetails?perf=1&symbol=" + symbol + "'>Details</a></td>");
				
				out.println("</tr>");
			}
			
			double totalChangeSinceClosePercent = (totalChangeSinceClose / totalCloseValue) * 100.0;
			double totalChangeSincePurchasePercent = (totalChangeSincePurchase / totalCostBasis) * 100.0;
			
			out.println("<tr>");
			out.println("<td colspan=4 align=center>&nbsp;</td>");
			out.println("<td colspan=1 align=center><b>Total:</b></td>");
			out.println("<td colspan=1 align=right>$" + df.format(totalValue) + "</td>");
			out.println("<td colspan=1 align=right>" + VSPUtils.formatColor(totalChangeSinceClose, df) + "</td>");
			out.println("<td colspan=1 align=right>" + VSPUtils.formatColor(totalChangeSinceClosePercent, df, true) + "</td>");
			out.println("<td colspan=1 align=right>" + VSPUtils.formatColor(totalChangeSincePurchase, df) + "</td>");
			out.println("<td colspan=1 align=right>" + VSPUtils.formatColor(totalChangeSincePurchasePercent, df, true) + "</td>");
			out.println("<td colspan=1 align=center> -- </td>");
			out.println("<td colspan=1 align=right>$" + df.format(totalCostBasis) + "</td>");
			out.println("<td colspan=1 align=center>&nbsp</td>");
			out.println("<td colspan=1 align=center>&nbsp</td>");
			out.println("</tr>");
		}
		else
		{
			out.println("<tr>");
			out.println("<td colspan=4 align=center>&nbsp;</td>");
			out.println("<td colspan=1 align=center><b>Total:</b></td>");
			out.println("<td colspan=1 align=right>$" + df.format(totalValue) + "</td>");
			out.println("<td colspan=1 align=center> -- </td>");
			out.println("<td colspan=1 align=center> -- </td>");
			out.println("<td colspan=1 align=center> -- </td>");
			out.println("<td colspan=1 align=center> -- </td>");
			out.println("<td colspan=1 align=center> -- </td>");
			out.println("<td colspan=1 align=center> -- </td>");
			out.println("<td colspan=1 align=center> -- </td>");
			out.println("</tr>");
		}
		
		out.println("</table>");
		out.println("</div>");		
	}
	catch(Exception ex)
	{
	  ex.printStackTrace();
		out.println("<p><b><i>Unable to retrieve Portfolio:</i></b><br><font color=red>" + ex.toString() + "</font>");
		out.println("<p>&nbsp;</p>");
		out.println("<p>&nbsp;</p>");
	}
%>

</body>
</html>