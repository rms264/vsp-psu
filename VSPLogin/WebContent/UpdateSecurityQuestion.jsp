<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@page import="vsp.utils.Enumeration.SecurityQuestion"%>
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
  									  <a href="updateEmail">Change Email</a> | 
  			                          <b>Change Security Question</b></td></tr>
  </table>

  <div id="updateSecurityQuestion">
    <h3>Update Security Question</h3>
    <form name='submitUpdateSecurityQuestion' action='submitUpdateSecurityQuestion' method='POST'>
    <c:if test="${securityUpdate != null}">
      <p id="success">
        ${securityUpdate }
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
      <% 
        out.println("<tr><td>Select your Security Question: </td><td><select name='question' width=50>");
      
        out.println("<option value='-1' selected>(Please select one:)</option>");
        for(SecurityQuestion question : SecurityQuestion.values())
        {
          if(question == SecurityQuestion.DEFAULT)
            continue;
        
          out.println("<option value='" + Integer.toString(question.getValue()) + "'>" + question + "</option>");
        }
        out.println("</select></td></tr>");
      %>
      <tr>
        <td>Enter your Security Answer: &nbsp;</td>
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