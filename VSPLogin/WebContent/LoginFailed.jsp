<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
  <head>
    <title>Invalid Login </title>
  </head>
  <body><h3>Authentication Failed</h3>
  
<form name="actionForm" action="j_security_check" method ="POST">
<table>
<tr><td>Enter your User Name: </td><td><input type="text" name="j_username"/></td></tr>
<tr><td>Enter your Password: </td><td><input type="password" name="j_password"/></td></tr>
<tr><td colspan="2" align="center"><input type="submit" value="submit"> </td></tr>
</table>
</form>
  
  </body>
</html>