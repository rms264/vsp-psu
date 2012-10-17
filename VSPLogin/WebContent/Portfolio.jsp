<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<%@ page session="true"%>
<%@ page import="vsp.*"%>
<%@ page import="vsp.dataObject.*"%>
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
 	/*DecimalFormat df = new DecimalFormat("0.00");
	SimpleDateFormat sd = new SimpleDateFormat("yyyy-MM-dd");
 	Date date = new Date();
 	date.setMonth(7);
 
 	StockInfoServiceProvider sisp = new StockInfoServiceProvider();
 	List<HistoricalStockInfo> infos = sisp.requestDailyHistoricalStockData("CIF", date);
 	
 	out.println("<table>");
 	for (HistoricalStockInfo hsi : infos)
 	{
 	 	out.println("<tr>");
 	 	out.println("<td>" + sd.format(hsi.getDate()) + "</td>");
 	 	out.println("<td>" + df.format(hsi.getOpen()) + "</td>");
 	 	out.println("<td>" + df.format(hsi.getDayLow()) + "</td>");
 	 	out.println("<td>" + df.format(hsi.getDayHigh()) + "</td>");
 	 	out.println("<td>" + df.format(hsi.getClose()) + "</td>");
 	 	out.println("<td>" + df.format(hsi.getVolume()) + "</td>");
 	 	out.println("<td>" + df.format(hsi.getAdjustedClose()) + "</td>");
 	 	out.println("</tr>");
 	}
 	out.println("</table>");*/
 %>
 
 <p>&nbsp;</p>
 <p>&nbsp;</p> 
 
<ul>
<li><a href="Order.jsp">Order Stock</a></li>
<li><a href="PendingOrders.jsp">Pending Orders</a></li>
<li><a href="TransactionHistory.jsp">Transaction History</a></li>
<li><a href="UserInfo.jsp">User Info</a></li>
<li><a href="Logout.jsp">Logout</a></li>
</ul>
</body>
</html>