<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
  <head>
    <title>Invalid Login </title>
    <style type="text/css">
	  input {
	      font-family: sans-serif;                
	  }
	</style>
  </head>
  <body>
  <h2>Virtual Stock Portfolio (VSP) System</h2>
  
<form name="actionForm" action="j_security_check" method ="POST">
<table>
<tr><td align=center colspan=2><b><i><font color="red">Authentication Failed<br>&nbsp;</font></i></b></td></tr>
<tr><td>Enter your User Name: &nbsp;</td><td><input type="text" name="j_username" /></td></tr>
<tr><td>Enter your Password: </td><td><input type="password" name="j_password" /></td></tr>
<tr><td>&nbsp;</td><td><input type="submit" value="Login"></td></tr>
</table>
</form>
  
  </body>
</html>