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
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>VSP - Portfolio</title>
</head>
<body>
 <h2>Virtual Stock Portfolio (VSP) System</h2>
 
  <h3>Portfolio</h3>
 
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
	    
		// throws on error
		List<PortfolioData> portfolioItems = vsp.getPortfolioEntries(userName);
		if (portfolioItems != null && portfolioItems.size() > 0)
		{
			DecimalFormat df = new DecimalFormat("0.00");
			
			// build stock symbol list
			List<String> symbols = new ArrayList<String>();
			for (PortfolioData item : portfolioItems)
			{
				symbols.add(item.getStock().getStockSymbol());
			}
			
			// try to get latest stock information for all symbols
			List<StockInfo> stockInfos = vsp.getLatestStockInfo(symbols);
			Map<String,StockInfo> stockInfoMap = new HashMap<String,StockInfo>(stockInfos.size());
			if (stockInfos != null)
			{
				for (StockInfo info : stockInfos)
				{
					stockInfoMap.put(info.getSymbol(), info);
				}
			}
			
			// format and print items to screen
			out.println("<table border=1 cellpadding=4 cellspacing=0>");
			
			out.println("<tr>");
			out.println("<td colspan=3 align=center>Stock Holding</td>");
			out.println("<td colspan=3 align=center>Most Recent</td>");
			out.println("<td colspan=2 align=center>Change Since Close</td>");
			out.println("<td colspan=2 align=center>Change Since Purchase</td>");
			out.println("<td colspan=2 align=center>Cost Basis</td>");
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
			out.println("</tr>");
			
			String symbol;
			StockInfo latestInfo;
			double changeSince;
			for (PortfolioData item : portfolioItems)
			{
				symbol = item.getStock().getStockSymbol();
				
				out.println("<tr>");
								
				out.println("<td>" + symbol + "</td>");
				out.println("<td>" + item.getStock().getStockDescription() + "</td>");
				out.println("<td align=center>" + item.getQuantity() + "</td>");
				
				// information based on latest stock data
				if (stockInfoMap.containsKey(symbol))
				{
					latestInfo = stockInfoMap.get(symbol);
					
					// latest price
					out.println("<td align=center>$" + df.format(latestInfo.getLastTradePrice()) + "</td>");
					// change in price
					out.println("<td align=center>" + VSPUtils.formatColor(latestInfo.getPriceChanceSinceOpen(), df) + "</td>");
					// latest value
					out.println("<td align=center>$" + df.format(latestInfo.getLastTradePrice() * item.getQuantity()) + "</td>");
					
					// change since close (value)
					changeSince = latestInfo.getChangeSinceClosePrice() * item.getQuantity();
					out.println("<td align=center>" + VSPUtils.formatColor(changeSince, df) + "</td>");
					// change since close (percent)
					out.println("<td align=center>" + VSPUtils.formatColor(latestInfo.getChangeSinceClosePercent(), df, true) + "</td>");
					
					// change since purchase (value)
					changeSince = (latestInfo.getLastTradePrice() * item.getQuantity()) - item.getCostBasis();
					out.println("<td align=center>" + VSPUtils.formatColor(changeSince, df) + "</td>");
					// change since purchase (percent)
					changeSince = (changeSince / item.getCostBasis()) * 100.0;
					out.println("<td align=center>" + VSPUtils.formatColor(changeSince, df, true) + "</td>");
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
				out.println("<td align=center>$" + df.format(item.getCostBasisPerShare()) + "</td>");
				out.println("<td align=center>$" + df.format(item.getCostBasis()) + "</td>");
				
				// sell link
				out.println("<td align=center><a href='Order.jsp?action=1&symbol=" + symbol + "'>Sell</a></td>");
				
				out.println("</tr>");
			}
			
			out.println("</table>");
		}
		else
		{
			out.println("<p><b><i>You do not own any stocks.</i></b></p>");
			out.println("<p>&nbsp;</p>");
		}
	}
	catch(Exception ex)
	{
		out.println("<p><b><i>Unable to retrieve Portfolio:</i></b><br><font color=red>" + ex.toString() + "</font>");
		out.println("<p>&nbsp;</p>");
		out.println("<p>&nbsp;</p>");
	}
%>
 
<ul>
<li><a href="Order.jsp">Order Stock</a></li>
<li><a href="PendingOrders.jsp">Pending Orders</a></li>
<li><a href="TransactionHistory.jsp">Transaction History</a></li>
<li><a href="UserInfo.jsp">User Info</a></li>
<li><a href="Logout.jsp">Logout</a></li>
</ul>
</body>
</html>