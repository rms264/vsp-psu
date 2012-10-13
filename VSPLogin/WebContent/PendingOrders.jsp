<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<%@ page session="true"%>

<%@ page import="vsp.*"%>
<%@ page import="vsp.dal.requests.*"%>
<%@ page import="vsp.dataObject.*"%>
<%@ page import="vsp.orders.*"%>
<%@ page import="vsp.utils.*"%>
<%@ page import="vsp.utils.Enumeration.*"%>
<%@ page import="java.text.*" %>
<%@ page import="java.util.*" %>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>VSP - Pending Orders</title>
</head>
<body>
 <h2>Virtual Stock Portfolio (VSP) System</h2>
 
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
		
		// throws on error
		List<Order> pendingOrders = vsp.getPendingOrders(userName);
		
		/**** DEBUG ONLY (BEGIN) ****/
		if (pendingOrders.size() == 0)
		{
			Stock stockCif = new Stock("CIF", "MFS Intermediate Income");
			Order order = new Order(Order.CreateId(), userName, stockCif, OrderAction.BUY, 100f, OrderType.MARKET, 0.0, 0.0, TimeInForce.DAY, OrderState.PENDING, new Date());
			Orders.addOrder(order);
			
			Stock stockMfv = new Stock("MFV", "MFS Special Value Trust");
			order = new Order(Order.CreateId(), userName, stockMfv, OrderAction.BUY, 500f, OrderType.LIMIT, 8.00, 0.0, TimeInForce.GOODUNTILCANCELED, OrderState.PENDING, new Date());
			Orders.addOrder(order);
			
			pendingOrders = vsp.getPendingOrders(userName);
		}
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
			List<StockInfo> stockInfos = vsp.getLatestStockInfo(symbols);
			Map<String,StockInfo> stockInfoMap = new HashMap<String,StockInfo>(stockInfos.size());
			if (stockInfos != null)
			{
				for (StockInfo info : stockInfos)
				{
					stockInfoMap.put(info.getSymbol(), info);
				}
			}
			
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
  
<ul>
<li><a href="Portfolio.jsp">Portfolio</a></li>
<li><a href="Order.jsp">Order Stock</a></li>
<li><a href="TransactionHistory.jsp">Transaction History</a></li>
<li><a href="UserInfo.jsp">User Info</a></li>
<li><a href="Logout.jsp">Logout</a></li>
</ul>
</body>
</html>