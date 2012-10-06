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
	String delete = request.getParameter("delete");
	if (delete != null && !delete.isEmpty())
	{
		try
		{
			VspServiceProvider vsp = new VspServiceProvider();
			// throws on error
			vsp.deleteTraderAccount(delete);
			out.println("<p><i>User deleted.</i>");
		}
		catch (Exception ex)
		{
			out.println("<p><i>Unable to delete user.</i>");
		}
	}

	try
	{
		VspServiceProvider vsp = new VspServiceProvider();
		// throws on error
		List<String> traders = vsp.getTraders();
		if (traders.size() > 0)
		{
			out.println("<table border=1 cellpadding=4 cellspacing=0 width=500>");
			out.println("<tr><td colspan=4 align=center><b>Trader Accounts:</b></td></tr>");
			for (int i = 0; i < traders.size(); ++i)
			{
				out.println("<tr><td width=225>" + traders.get(i) + "</td><td><font size=2>Set Email</a></font></td><td><font size=2>Set Password</font></td><td colspan=2 align=right><font size=2>&nbsp;&nbsp;<a href='Admin.jsp?delete=" + traders.get(i) + "'>Delete</a></font></td></tr>");
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