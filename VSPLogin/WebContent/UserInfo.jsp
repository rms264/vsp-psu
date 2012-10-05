<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page session="true"%>

<%@ page import="vsp.*"%>
<%@ page import="vsp.dataObject.*"%>
<%@ page import="java.text.*" %>

<html>
  <head>
    <title>VSP - User Information</title>
  </head>
  <body>
  
    <h2>Virtual Stock Portfolio (VSP) System</h2>

<%
	try
	{
		String userName = request.getRemoteUser();
		VspServiceProvider vsp = new VspServiceProvider();
		// throws on error
		AccountData data = vsp.getAccountInfo(userName);
		SimpleDateFormat sd = new SimpleDateFormat("yyyy-MM-dd");
		
		out.println("<table>");
		out.println("<tr><td align=center colspan=2><b>User Info:</b></td></tr>");
		out.println("<tr><td>Email Address: &nbsp;&nbsp;</td><td>" + data.getEmail() + "</td></tr>");
		out.println("<tr><td>Sign-up Date: </td><td>" + sd.format(data.getSignup()) + "</td></tr>");
		out.println("</table>");
	}
	catch(Exception ex)
	{
		out.println("<p><font color=red>" + ex.toString() + "</font>");
		out.println("<table>");
		out.println("<tr><td align=center colspan=2><b>User Info:</b></td></tr>");
		out.println("<tr><td>Email Address: &nbsp;&nbsp;</td><td>[Unable to connect]</td></tr>");
		out.println("<tr><td>Sign-up Date: </td><td>[Unable to connect]</td></tr>");
		out.println("</table>");
	}
%>
    
  <ul>
<li><a href="Portfolio.jsp">Portfolio</a></li>
<li><a href="Logout.jsp">Logout</a></li>
</ul>
  
  </body>
</html>