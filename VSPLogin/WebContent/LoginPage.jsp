<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=windows-1256">
<title>VSP - Login Page</title>
<style type="text/css">
  input {
      font-family: sans-serif;                
  }
</style>
</head>
<body>
<form name="actionForm" action="j_security_check" method ="POST">

<h2>Virtual Stock Portfolio (VSP) System</h2>

<%
	Object signupObj = session.getAttribute("signup");
	if (signupObj != null)
	{
		session.removeAttribute("signup");
		String signup = signupObj.toString();
		if (!signup.isEmpty())
		{
			out.println("<p>Account created successfully!  Please login.");
		}
	}
%>

<table>
<tr><td>Enter your User Name: &nbsp;</td><td><input type="text" name="j_username" /></td></tr>
<tr><td>Enter your Password: </td><td><input type="password" name="j_password" /></td></tr>
<tr><td>&nbsp;</td><td><input type="submit" value="Login">&nbsp;&nbsp;</td></tr>
<tr><td><a href="Signup.jsp"><font size=2>Create Account</font></a></td><td>&nbsp;</td></tr>
</table>
</form>
</body>
</html>