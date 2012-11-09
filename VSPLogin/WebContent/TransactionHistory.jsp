<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<%@ page session="true"%>

<%@ page import="vsp.*"%>
<%@ page import="vsp.dataObject.*"%>
<%@ page import="vsp.utils.Enumeration.*"%>
<%@ page import="java.text.*" %>
<%@ page import="java.util.*" %>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<style type="text/css">@import url("css/main.css")</style>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>VSP - Transaction History</title>
</head>
<body>
<%@ include file="headers/LoggedInHeader.jsp" %>

  <table width=100%>
  <tr><td colspan=2>Welcome, ${pageContext.request.remoteUser}</td></tr>
  <tr><td>&nbsp;</td><td align=center><a href="viewPortfolio">Portfolio</a> | 
  <a href="stockSearch">Stock Search</a> | <a href="order">Order Stock</a> | 
  <a href="pendingOrders">Pending Orders</a> | <b>Transaction History</b> | <a href="viewUserInfo">User Info</a></td></tr>
  </table>

<div id="transHistory">
 <h3>Transaction History</h3>
 
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
		
		// throws on error
		List<StockTransaction> transactions = vsp.getTransactionHistory(userName);
		
		if (transactions != null && transactions.size() > 0)
		{
			DecimalFormat df = new DecimalFormat("0.00");
			SimpleDateFormat sd = new SimpleDateFormat("yyyy-MM-dd");
			
			out.println("<table border=1 cellpadding=4 cellspacing=0>");
			out.println("<tr>");
			out.println("<td align=center><b>Date</b></td>");
			out.println("<td align=center><b>Type</b></td>");
			out.println("<td align=center><b>Action</b></td>");
			out.println("<td align=center><b>Symbol</b></td>");
			out.println("<td align=center><b>Name</b></td>");
			out.println("<td align=center><b>Quantity</b></td>");
			out.println("<td align=center><b>Per Share</b></td>");
			out.println("<td align=center><b>Value</b></td>");
			out.println("<td align=center><b>Note</b></td>");
			out.println("<td align=center><b>Id</b></td>");
			out.println("</tr>");
			
			Order order = null;
			TransactionType type = TransactionType.DEFAULT;
			StockTransaction transaction = null;
			String note;
			for (int i = 0; i < transactions.size(); ++i)
			{
				transaction = transactions.get(i);
				type = transaction.getType();
				order = transaction.getOrder();
				
				out.println("<tr>");
				out.println("<td>" + sd.format(transaction.getDateTime()) + "</td>");
				out.println("<td>" + type.toString() + "</td>");
				
				if (type == TransactionType.DIVIDEND)
				{
					out.println("<td> -- </td>");
				}
				else
				{
					out.println("<td>" + order.getAction().toString() + "</td>");
				}
				
				out.println("<td>" + transaction.getStock().getStockSymbol() + "</td>");
				out.println("<td>" + transaction.getStock().getStockDescription() + "</td>");
				
				if (type == TransactionType.CANCELLATION)
				{
					out.println("<td align=center>" + transaction.getOrder().getQuantity() + "</td>");
					out.println("<td align=center>N/A</td>");
					out.println("<td align=center>N/A</td>");
				}
				else
				{
					out.println("<td align=center>" + transaction.getQuantity() + "</td>");
					out.println("<td align=center>" + df.format(transaction.getPricePerShare()) + "</td>");
					out.println("<td align=center>" + df.format(transaction.getValue()) + "</td>");
				}
				
				note = transaction.getNote();
				if (note != null && !note.isEmpty())
				{
					out.println("<td>" + note + "</td>");
				}
				else
				{
					out.println("<td>&nbsp;</td>");
				}

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
 </div>
</body>
</html>