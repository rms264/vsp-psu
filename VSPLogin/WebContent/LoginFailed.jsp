<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<style type="text/css">@import url("css/main.css")</style>
<title>VSP Login</title>
</head>
<%@ include file="headers/CancelHeader.jsp" %>
<body>
  
  <form name="actionForm" action="j_security_check" method ="POST">

  <div id="login">
    <table>
      <tr>
        <td align=center colspan=2><b><i><font color="red">Authentication Failed<br>&nbsp;</font></i></b></td>
      </tr>
      <tr>
        <td>Enter your User Name: &nbsp;</td>
        <td><input type="text" name="j_username" /></td>
      </tr>
      <tr>
      <td>Enter your Password: </td>
      <td><input type="password" name="j_password" /></td>
    </tr>
    <tr>
      <td>&nbsp;</td>
      <td><input type="submit" value="Login">&nbsp;&nbsp;</td>
    </tr>
    <tr>
      <td><a href="enterUserName"><font size=2>Forgot Password?</font></a></td>
      <td>&nbsp;</td>
    </tr>
  </table>
  </form>
 </div>
  
  
  </body>
</html>