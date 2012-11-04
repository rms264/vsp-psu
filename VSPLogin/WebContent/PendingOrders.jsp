<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<%@ page session="true"%>

<%@ page import="vsp.*"%>
<%@ page import="vsp.dal.requests.*"%>
<%@ page import="vsp.dataObject.*"%>
<%@ page import="vsp.utils.*"%>
<%@ page import="vsp.utils.Enumeration.*"%>
<%@ page import="java.text.*" %>
<%@ page import="java.util.*" %>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<style type="text/css">@import url("css/main.css")</style>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>VSP - Pending Orders</title>
</head>
<body>
 <%@ include file="headers/LoggedInHeader.jsp" %>
 <div id="pendingOrders">
 <h3>Pending Orders</h3>
 
<%
	try
	{
		String userName = request.getRemoteUser();
		VspServiceProvider vsp = new VspServiceProvider();
		
	    // If process is true, attempt to validate and process the form
	    if ("true".equals(request.getParameter("process"))
	    		&& !request.getParameter("id").isEmpty())
	    {
	    	try
	    	{
	    		// throws on error
	    		vsp.cancelOrder(userName, request.getParameter("id"));
	    		out.println("<p><b><i>Order cancelled successfully.</i></b></p>");
	    	}
	    	catch(Exception ex)
	    	{
	    		out.println("<p><b><i>Unable to cancel order:</i></b><br><font color=red>" + ex.getLocalizedMessage() + "</font>");
	    	}
	    }
		
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
		List<Order> pendingOrders = vsp.getPendingOrders(userName);
		
		/**** DEBUG ONLY (BEGIN) ****/
		/*if (pendingOrders.size() == 0)
		{
			Stock stockCif = new Stock("CIF", "MFS Intermediate Income");
			Order order = new Order(Order.CreateId(), userName, stockCif, OrderAction.BUY, 100f, OrderType.MARKET, 0.0, 0.0, TimeInForce.DAY, OrderState.PENDING, new Date(), new Date());
			Orders.addOrder(order);
			
			Stock stockMfv = new Stock("MFV", "MFS Special Value Trust");
			order = new Order(Order.CreateId(), userName, stockMfv, OrderAction.BUY, 500f, OrderType.LIMIT, 8.00, 0.0, TimeInForce.GOODUNTILCANCELED, OrderState.PENDING, new Date(), new Date());
			Orders.addOrder(order);
			
			pendingOrders = vsp.getPendingOrders(userName);
		}*/
		/**** DEBUG ONLY (END) ****/
		
		if (pendingOrders != null && pendingOrders.size() > 0)
		{
			// build stock symbol list
			List<String> symbols = new ArrayList<String>();
			for (Order pendingOrder : pendingOrders)
			{
				symbols.add(pendingOrder.getStock().getStockSymbol());
			}
			
			// try to get latest stock information for all symbols
			Map<String,StockInfo> stockInfoMap = vsp.getLatestStockInfo(symbols);
			
			DecimalFormat df = new DecimalFormat("0.00");
			SimpleDateFormat sd = new SimpleDateFormat("yyyy-MM-dd");
			
			out.println("<table border=1 cellpadding=4 cellspacing=0>");
			out.println("<tr>");
			out.println("<td align=center>&nbsp;</td>");
			out.println("<td align=center><b>Submitted</b></td>");
			out.println("<td align=center><b>Action</b></td>");
			out.println("<td align=center><b>Type</b></td>");
			out.println("<td align=center><b>Time in Force</b></td>");
			out.println("<td align=center><b>Symbol</b></td>");
			out.println("<td align=center><b>Name</b></td>");
			out.println("<td align=center><b>Quantity</b></td>");
			out.println("<td align=center><b>Limit</b></td>");
			out.println("<td align=center><b>Stop</b></td>");
			out.println("<td align=center><b>Estimated Value</b></td>");
			out.println("<td align=center><b>Id</b></td>");
			out.println("</tr>");
			
			OrderType type = OrderType.DEFAULT;
			Order order = null;
			String symbol = null;
			for (int i = 0; i < pendingOrders.size(); ++i)
			{
				order = pendingOrders.get(i);
				type = order.getType();
				symbol = order.getStock().getStockSymbol();
				
				out.println("<tr>");
				
				out.println("<td><form name='cancelOrder" + i + "' action='" + request.getRequestURI() + "' method='POST'>");
				out.println("<input type='hidden' name='process' value='true' />");
				out.println("<input type='hidden' name='id' value='" + order.getId() + "' />");
				out.println("<input type='submit' value='Attempt Cancel' />");
				out.println("</form></td>");
				
				out.println("<td>" + sd.format(order.getDateSubmitted()) + "</td>");
				out.println("<td>" + order.getAction().toString() + "</td>");
				out.println("<td>" + type.toString() + "</td>");
				out.println("<td>" + order.getTimeInForce().toString() + "</td>");
				out.println("<td>" + symbol + "</td>");
				out.println("<td>" + order.getStock().getStockDescription() + "</td>");
				out.println("<td>" + order.getQuantity() + "</td>");
				
				if (type == OrderType.LIMIT || type == OrderType.STOPLIMIT)
				{
					out.println("<td>" + df.format(order.getLimitPrice()) + "</td>");
				}
				else
				{
					out.println("<td>N/A</td>");
				}
				
				if (type == OrderType.STOP || type == OrderType.STOPLIMIT)
				{
					out.println("<td>" + df.format(order.getStopPrice()) + "</td>");
				}
				else
				{
					out.println("<td>N/A</td>");
				}
				
				if (stockInfoMap.containsKey(symbol))
				{
					out.println("<td>" + VSPUtils.formatColor(order.getLatestEstimatedValue(stockInfoMap.get(symbol)), df) + "</td>");	
				}
				else
				{
					out.println("<td>Unavailable</td>");
				}
								
				out.println("<td>" + order.getId() + "</td>");
				out.println("</tr>");
			}
			out.println("</table><br>");
		}
		else
		{
			out.println("<p><b><i>No pending orders.</i></b></p>");
			out.println("<p>&nbsp;</p>");
		}
	}
	catch(Exception ex)
	{
		out.println("<p><b><i>Unable to retrieve pending orders:</i></b><br><font color=red>" + ex.toString() + "</font>");
		out.println("<p>&nbsp;</p>");
		out.println("<p>&nbsp;</p>");
	}
%>
  
 </div>
<ul>
<li><a href="viewPortfolio">Portfolio</a></li>
<li><a href="stockSearch">Stock Search</a></li>
<li><a href="order">Order Stock</a></li>
<li><a href="transactionHistory">Transaction History</a></li>
<li><a href="viewUserInfo">User Info</a></li>
</ul>
</body>
</html>