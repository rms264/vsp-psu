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
<title>VSP - Transaction History</title>
</head>
<body>
 <h2>Virtual Stock Portfolio (VSP) System</h2>
 
 <h3>Transaction History</h3>
 
<%
	try
	{
		String userName = request.getRemoteUser();
		VspServiceProvider vsp = new VspServiceProvider();
		// throws on error
		List<StockTransaction> transactions = vsp.getTransactionHistory(userName);
		
		if (transactions != null && transactions.size() > 0)
		{
			DecimalFormat df = new DecimalFormat("0.00");
			SimpleDateFormat sd = new SimpleDateFormat("yyyy-MM-dd");
			
			out.println("<table border=1 cellpadding=4 cellspacing=0>");
			out.println("<tr>");
			out.println("<td align=center><b>Date/Time</b></td>");
			out.println("<td align=center><b>Type</b></td>");
			out.println("<td align=center><b>Symbol</b></td>");
			out.println("<td align=center><b>Name</b></td>");
			out.println("<td align=center><b>Quantity</b></td>");
			out.println("<td align=center><b>Per Share</b></td>");
			out.println("<td align=center><b>Value</b></td>");
			//out.println("<td><b>Order Id</b></td>");
			out.println("<td align=center><b>Id</b></td>");
			out.println("</tr>");
			
			Order order = null;
			TransactionType type = TransactionType.DEFAULT;
			StockTransaction transaction = null;
			for (int i = 0; i < transactions.size(); ++i)
			{
				transaction = transactions.get(i);
				type = transaction.getType();
				order = transaction.getOrder();
				
				out.println("<tr>");
				out.println("<td>" + sd.format(transaction.getDateTime()) + "</td>");
				out.println("<td>" + type.toString() + "</td>");
				out.println("<td>" + transaction.getStock().getStockSymbol() + "</td>");
				out.println("<td>" + transaction.getStock().getStockDescription() + "</td>");
				
				if (type == TransactionType.CANCELLATION)
				{
					out.println("<td>N/A</td>");
					out.println("<td>N/A</td>");
					out.println("<td>N/A</td>");
				}
				else
				{
					out.println("<td>" + transaction.getQuantity() + "</td>");
					out.println("<td>" + df.format(transaction.getPricePerShare()) + "</td>");
					out.println("<td>" + df.format(transaction.getValue()) + "</td>");
				}
				
				/*if (order != null)
				{
					out.println("<td>" + order.getId() + "</td>");
				}
				else
				{
					out.println("<td>N/A</td>");
				}*/

				out.println("<td>" + transaction.getId() + "</td>");
				out.println("</tr>");
			}
			out.println("</table><br>");
		}
		else
		{
			out.println("<p><b><i>No transactions.</i></b></p>");
			out.println("<p>&nbsp;</p>");
		}
	}
	catch(Exception ex)
	{
		out.println("<p><b><i>Unable to retrieve transaction history:</i></b><br><font color=red>" + ex.toString() + "</font>");
		out.println("<p>&nbsp;</p>");
		out.println("<p>&nbsp;</p>");
	}
%>
 
<ul>
<li><a href="Portfolio.jsp">Portfolio</a></li>
<li><a href="PendingOrders.jsp">Pending Orders</a></li>
<li><a href="UserInfo.jsp">User Info</a></li>
<li><a href="Logout.jsp">Logout</a></li>
</ul>
</body>
</html>