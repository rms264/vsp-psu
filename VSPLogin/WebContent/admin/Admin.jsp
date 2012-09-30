<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<%@ page session="true"%>
<%@ page import="vsp.*"%>
<%@ page import="java.util.*"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>VSP - Administration</title>
</head>
<body>
<h2>Virtual Stock Portfolio (VSP) System</h2>

<%
	try
	{
		VspServiceProvider vsp = new VspServiceProvider();
		// throws on error
		List<String> traders = vsp.getTraders();
		if (traders.size() > 0)
		{
			out.println("<table>");
			out.println("<tr><td align=center><b>Trader Accounts:</b></td></tr>");
			for (int i = 0; i < traders.size(); ++i)
			{
				out.println("<tr><td align=center>" + traders.get(i) + "</td></tr>");
			}
			
			out.println("</table>");
		}
		else
		{
			out.println("<p><i>No traders exist in the database.</i>");
		}
	}
	catch(Exception ex)
	{
		out.println("<p><font color=red>" + ex.toString() + "</font>");
		out.println("<p><i>Unable to pull traders from the database.</i>");
	}
%>

<p><a href="..\Logout.jsp">Logout</a>

</body>
</html>