<%@ page language="java" contentType="text/html; charset=windows-1256" pageEncoding="windows-1256" %>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=windows-1256">
<title>Login Page</title> </head>
<body>
<!--  <form name="actionForm" action="LoginServlet" method ="GET"> -->
<form name="actionForm" action="j_security_check" method ="POST">
<table>
<tr><td>Enter your User Name: </td><td><input type="text" name="j_username"/></td></tr>
<tr><td>Enter your Password: </td><td><input type="password" name="j_password"/></td></tr>
<tr><td colspan="2" align="center"><input type="submit" value="submit"> </td></tr>
</table>
</form>
</body>
</html>