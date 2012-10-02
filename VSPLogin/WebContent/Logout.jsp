<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<%@ page session="true"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head><title>VSP - Logout</title></head>
<body>

<%
	Boolean admin = request.isUserInRole("admin");
%>

<h2>Virtual Stock Portfolio (VSP) System</h2>

<p><i>User '<%=request.getRemoteUser()%>' has been logged out.</i>
<% session.invalidate(); %>

<br/><br/>
<%
	if (admin)
	{
		out.println("<p><a href='admin/Admin.jsp'>Login</a>");	
	}
	else
	{
		out.println("<p><a href='Portfolio.jsp'>Login</a>");
	}
%>
</body>
</html>