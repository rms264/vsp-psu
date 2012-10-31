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
	StockInfoServiceProvider sp = new StockInfoServiceProvider();
	// If process is true, attempt to validate and process the form
    if ("true".equals(request.getParameter("process"))
    		&& !request.getParameter("searchTerm").isEmpty())
    {
    	try
    	{    		
    		String searchTerm = request.getParameter("searchTerm");
    		
    		out.println("<p>Search results for <b>" + searchTerm + "</b>:");
    		
    		List<Stock> stockLists = sp.searchForStocks(searchTerm);
    		if (stockLists != null && stockLists.size() > 0)
    		{
	    		out.println("<table border=1 cellpadding=4 cellspacing=0>");
	    		
	    		out.println("<tr>");
	    		out.println("<td align=center><b>Symbol</b></td>");
	    		out.println("<td align=center><b>Description</b></td>");
	    		out.println("<td align=center>&nbsp;</td>");
	    		out.println("</tr>");
	    		
	    		for (Stock stock : stockLists)
	    		{    		   	
	    	    	out.println("<tr>");
	    	    	out.println("<td>" + stock.getStockSymbol() + "</td>");
	    	    	out.println("<td>" + stock.getStockDescription() + "</td>");
	    	    	out.println("<td align=center><a href='Order.jsp?action=0&symbol=" + stock.getStockSymbol() + "'>Buy</a></td>");
	    	    	out.println("</tr>");	
	    		}
	    		
		    	out.println("</table>");
    		}
    		else
    		{
    			out.println("<p><b><i>There were no search results.</i></b>");
    		}
    	}
    	catch(Exception ex)
    	{
    		out.println("<p><font color=red>" + ex.getLocalizedMessage() + "</font>");
    	}
    	
    	out.println("<br>");
    }
	
  	// always show search form
	out.println("<p><form name='actionForm' action='" + request.getRequestURI() + "' method='POST'>");
 	out.println("<input type='hidden' name='process' value='true' />");
 	out.println("<table>");   	    	   	    
 	out.println("<tr><td>Search for: </td><td><input type='text' name='searchTerm' /></td></tr>");
 	out.println("<tr><td>&nbsp;</td><td><input type='submit' value='Search'></td></tr>");
 	out.println("</table>");
 	out.println("</form>"); 
  %>
<ul>
<li><a href="Portfolio.jsp">Portfolio</a></li>
<li><a href="Order.jsp">Order Stock</a></li>
<li><a href="PendingOrders.jsp">Pending Orders</a></li>
<li><a href="TransactionHistory.jsp">Transaction History</a></li>
<li><a href="UserInfo.jsp">User Info</a></li>
<li><a href="Logout.jsp">Logout</a></li>
</ul>
  
</body>
</html>