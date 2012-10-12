<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<%@ page session="true"%>

<%@ page import="vsp.*"%>
<%@ page import="vsp.dataObject.*"%>
<%@ page import="vsp.orders.*"%>
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
		// throws on error
		List<Order> pendingOrders = vsp.getPendingOrders(userName);
		SimpleDateFormat sd = new SimpleDateFormat("yyyy-MM-dd");
		
		if (pendingOrders != null && pendingOrders.size() > 0)
		{
			out.println("<table border=1 cellpadding=4 cellspacing=0>");
			out.println("<tr>");
			out.println("<td><b>Submitted</b></td>");
			out.println("<td><b>Action</b></td>");
			out.println("<td><b>Type</b></td>");
			out.println("<td><b>Time in Force</b></td>");
			out.println("<td><b>Symbol</b></td>");
			out.println("<td><b>Name</b></td>");
			out.println("<td><b>Quantity</b></td>");
			out.println("<td><b>Limit Price</b></td>");
			out.println("<td><b>Stop Price</b></td>");
			out.println("<td><b>Id</b></td>");
			out.println("</tr>");
			
			OrderType type = OrderType.DEFAULT;
			Order order = null;
			for (int i = 0; i < pendingOrders.size(); ++i)
			{
				order = pendingOrders.get(i);
				type = order.getType();
				
				out.println("<tr>");
				out.println("<td>" + sd.format(order.getDateSubmitted()) + "</td>");
				out.println("<td>" + order.getAction().toString() + "</td>");
				out.println("<td>" + type.toString() + "</td>");
				out.println("<td>" + order.getTimeInForce().toString() + "</td>");
				out.println("<td>" + order.getStock().getStockSymbol() + "</td>");
				out.println("<td>" + order.getStock().getStockDescription() + "</td>");
				out.println("<td>" + order.getQuantity() + "</td>");
				
				if (type == OrderType.LIMIT || type == OrderType.STOPLIMIT)
				{
					out.println("<td>" + order.getLimitPrice() + "</td>");
				}
				else
				{
					out.println("<td>N/A</td>");
				}
				
				if (type == OrderType.STOP || type == OrderType.STOPLIMIT)
				{
					out.println("<td>" + order.getStopPrice() + "</td>");
				}
				else
				{
					out.println("<td>N/A</td>");
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
<li><a href="TransactionHistory.jsp">Transaction History</a></li>
<li><a href="UserInfo.jsp">User Info</a></li>
<li><a href="Logout.jsp">Logout</a></li>
</ul>
</body>
</html>