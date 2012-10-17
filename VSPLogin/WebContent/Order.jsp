<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<%@ page session="true"%>
<%@ page import="vsp.*"%>
<%@ page import="vsp.dataObject.*"%>
<%@ page import="java.text.*" %>
<%@ page import="java.util.*" %>
<%@page import="vsp.utils.Enumeration.*"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>VSP - Order Stock</title>
</head>
<body>
 <h2>Virtual Stock Portfolio (VSP) System</h2>
 
 <h3>Order Stock</h3>

<p><i>Put order stuff here</i></p>
<p>&nbsp;</p>

<%
	Boolean showForm = true;
	String userName = request.getRemoteUser();
	VspServiceProvider vsp = new VspServiceProvider();

    // If process is true, attempt to validate and process the form
    if ("true".equals(request.getParameter("process"))
    		&& !request.getParameter("action").isEmpty()
    		&& !request.getParameter("stock").isEmpty()
    		&& !request.getParameter("quantity").isEmpty()
    		&& !request.getParameter("type").isEmpty()
    		&& !request.getParameter("timeInForce").isEmpty())
    {
    	try
    	{
    		// throws on error
    		vsp.createOrder(userName, 
    				request.getParameter("action"), 
    				request.getParameter("stock"), 
    				request.getParameter("quantity"), 
    				request.getParameter("type"), 
    				request.getParameter("timeInForce"));


    		// TODO: show order submitted text and offer link to this page for another order
    		
   			showForm = false;
   			return;
    	}
    	catch(Exception ex)
    	{
    		out.println("<p><font color=red>" + ex.getLocalizedMessage() + "</font>");
    	}
    }

	if (showForm)
    {
		List<PortfolioData> portfolioEntries = null;
		try
		{
			portfolioEntries = vsp.getPortfolioEntries(userName);
		}
		catch (Exception ex)
		{
			// ignore
		}
		
		out.println("<form name='actionForm' action='" + request.getRequestURI() + "' method='POST'>");
    	out.println("<input type='hidden' name='process' value='true' />");
    	out.println("<table>");
    	
    	out.println("<tr><td>Action: </td><td><select name='action' width=50>");
    	out.println("<option value='" + OrderAction.BUY.getValue() + "' selected>" + OrderAction.BUY.toString() + "</option>");
    	if (portfolioEntries != null && portfolioEntries.size() > 0)
    	{
    		out.println("<option value='" + OrderAction.SELL.getValue() + "'>" + OrderAction.SELL.toString() + "</option>");
    	}
  	
    	out.println("</td></tr>");
    	   	    	
    	out.println("<tr><td>Stock Symbol: </td><td><input type='text' name='stock' /></td></tr>");
    	out.println("<tr><td>Quantity: </td><td><input type='text' name='quantity' /></td></tr>");
    	
    	out.println("<tr><td>Order Type: </td><td><select name='type' width=50>");
    	out.println("<option value='" + OrderType.MARKET.getValue() + "' selected>" + OrderType.MARKET.toString() + "</option>");
    	out.println("<option value='" + OrderType.LIMIT.getValue() + "'>" + OrderType.LIMIT.toString() + "</option>");
    	out.println("<option value='" + OrderType.STOP.getValue() + "'>" + OrderType.STOP.toString() + "</option>");
    	out.println("<option value='" + OrderType.STOPLIMIT.getValue() + "'>" + OrderType.STOPLIMIT.toString() + "</option>");
    	out.println("</td></tr>");
    	
    	out.println("<tr><td>Time in Force: </td><td><select name='timeInForce' width=50>");
    	out.println("<option value='" + TimeInForce.DAY.getValue() + "' selected>" + TimeInForce.DAY.toString() + "</option>");
    	out.println("<option value='" + TimeInForce.GOODUNTILCANCELED.getValue() + "'>" + TimeInForce.GOODUNTILCANCELED.toString() + "</option>");
    	out.println("<option value='" + TimeInForce.FILLORKILL.getValue() + "'>" + TimeInForce.FILLORKILL.toString() + "</option>");
    	out.println("<option value='" + TimeInForce.IMMEDIATEORCANCEL.getValue() + "'>" + TimeInForce.IMMEDIATEORCANCEL.toString() + "</option>");
    	out.println("</td></tr>");
    	out.println("<tr><td>&nbsp;</td><td><input type='submit' value='Submit'></td></tr>");
    	
    	out.println("</table>");
    	out.println("</form>");
    }
%>

<ul>
<li><a href="Portfolio.jsp">Portfolio</a></li>
<li><a href="PendingOrders.jsp">Pending Orders</a></li>
<li><a href="TransactionHistory.jsp">Transaction History</a></li>
<li><a href="UserInfo.jsp">User Info</a></li>
<li><a href="Logout.jsp">Logout</a></li>
</ul>
</body>
</html>