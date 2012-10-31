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
<title>VSP - Stock Search</title>
</head>
<body>
<h2>Virtual Stock Portfolio (VSP) System</h2>
 
  <h3>Stock Search</h3>
  <%
  	Boolean showForm = true;
	StockInfoServiceProvider sp = new StockInfoServiceProvider();
	// If process is true, attempt to validate and process the form
    if ("true".equals(request.getParameter("process"))
    		&& !request.getParameter("stock").isEmpty())
    {
    	try
    	{    		
    		List<Stock> stockLists = sp.searchForStocks(request.getParameter("stock"));    		
    		
    		out.println("<table border=1 cellpadding=4 cellspacing=0>");
    		
    		out.println("<tr>");
    		out.println("<td align=center><u>Symbol</u></td>");
    		out.println("<td align=center><u>Description</u></td>");
    		out.println("</tr>");
    		
    		for (Stock stock : stockLists)
    		{    		   	
    	    	out.println("<tr><td>" + stock.getStockSymbol() + "</td><td>" + stock.getStockDescription() + "</td></tr>");	
    		}    		
    		
	    	out.println("</table>");
    		
   			showForm = false;
    	}
    	catch(Exception ex)
    	{
    		out.println("<p><font color=red>" + ex.getLocalizedMessage() + "</font>");
    	}
    }
	if (showForm)
    {
		out.println("<form name='actionForm' action='" + request.getRequestURI() + "' method='POST'>");
    	out.println("<input type='hidden' name='process' value='true' />");
    	out.println("<table>");
    	
    	String symbol = request.getParameter("symbol");
    	if (symbol == null)
    	{
    		symbol = "";
    	}
    	
    	String action = request.getParameter("action");
    	if (action == null)
    	{
    		action = "";
    	}
    	    	   	    
    	out.println("<tr><td>Stock Symbol: </td><td><input type='text' value='" + symbol + "' name='stock' /></td></tr>");
    	out.println("<tr><td>&nbsp;</td><td><input type='submit' value='Search'></td></tr>");
    	
    	out.println("</table>");
    	out.println("</form>");
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