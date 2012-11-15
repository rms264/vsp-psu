<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<style type="text/css">@import url("css/main.css")</style>
<title>VSP - Reset Password</title>
</head>
<%@ include file="headers/CancelHeader.jsp" %>
<body>

  <div id="resetPassword">
    <h3>Reset Password</h3>
    <form name='actionForm' action='submitResetPassword' method='POST'>
    <table>
      <tr>
        <td>Enter New Password: </td><td>
        <input type='password' name='password' size=46 /></td>
      </tr>
      <tr>
        <td>Repeat New Password: </td>
        <td><input type='password' name='verifyPassword' size=46 /></td>
      </tr>
      <tr>
        <td>&nbsp;</td>
        <td><input type='submit' value='Submit'></td>
      </tr>
    </table>
    </form>
  </div>

</body>
</html>