<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<style type="text/css">@import url("css/main.css")</style>
<title>VSP - Reset Email</title>
</head>
<body>
<%@ include file="headers/LoggedInHeader.jsp" %>

    <table width=100%>
  <tr><td colspan=2>Welcome, ${pageContext.request.remoteUser}</td></tr>
  <tr><td>&nbsp;</td><td align=center><a href="viewPortfolio">Portfolio</a> | 
  <a href="stockSearch">Stock Search</a> | <a href="order">Order Stock</a> | 
  <a href="pendingOrders">Pending Orders</a> | <a href="transactionHistory">Transaction History</a> | 
  <b><a href="viewUserInfo">User Info</a></b></td></tr>
  <tr><td colspan=2>&nbsp;</td></tr>
  <tr><td>&nbsp;</td><td align=center><a href="updatePassword">Change Password</a> | 
  									  <b>Change Email</b> | 
  			                          <a href="updateSecurityQuestion">Change Security Question</a></td></tr>
  </table>

  <div id="updateEmail">
    <h3>Update Email</h3>
    <form name='submitUpdateEmail' action='submitUpdateEmail' method='POST'>
    <c:if test="${emailUpdate != null}">
      <p id="success">
        ${emailUpdate }
      </p>
    </c:if>
    <c:if test="${requestScope.errors != null}">
        <p id="errors">
        Error(s)!
        <ul>
        <c:forEach var="error" items="${requestScope.errors}">
            <li>${error}</li>
        </c:forEach>
        </ul>
    </c:if>
    <table>      
      <tr>
        <td>Enter New Email: </td><td>
        <input type='text' name='email' size=46 /></td>
      </tr>
      <tr>
        <td>Repeat New Email: </td>
        <td><input type='text' name='verifyemail' size=46 /></td>
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