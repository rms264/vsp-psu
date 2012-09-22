<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<%@ page session="true"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head></head>
<body>

<h2>Virtual Stock Portfolio (VSP) System</h2>

<p><i>User '<%=request.getRemoteUser()%>' has been logged out.</i>
<% session.invalidate(); %>

<br/><br/>
<p><a href="LoginSuccess.jsp">Login</a>
</body>
</html>