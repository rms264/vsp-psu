<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<style type="text/css">@import url("css/main.css")</style>
<title>VSP - Answer Security Question</title>
</head>
<%@ include file="headers/CancelHeader.jsp" %>
<body>

  
  <div id="enterAnswer">
    <h3>Reset Password</h3>
    <form name='submitAnswer' action="submitSecurityAnswer" method='POST'>
    <input type='hidden' name='userName' value=${userAccount.userName } />
    <table>
      <p><font color=black >${question }</font></p>
      <tr>
        <td>Enter Answer: </td>
        <td><input type='text' name='answer' size=46 /></td>
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