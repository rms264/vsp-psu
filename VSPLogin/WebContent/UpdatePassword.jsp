<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<style type="text/css">@import url("css/main.css")</style>
<title>VSP - Reset Password</title>
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
  <tr><td>&nbsp;</td><td align=center><b>Change Password</b> | 
  									  <a href="updateEmail">Change Email</a> | 
  			                          <a href="updateSecurityQuestion">Change Security Question</a></td></tr>
  </table>

  <div id="resetPassword">
    <h3>Update Password</h3>
    <form name='actionForm' action='submitUpdatePassword' method='POST'>
    <c:if test="${passwordUpdate != null}">
      <p id="success">
        ${passwordUpdate }
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
        <td>Enter Current Password: </td>
          <td><input type='password' name='current_password' size=46 /></td>
        </tr>
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