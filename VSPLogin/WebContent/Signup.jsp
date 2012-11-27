<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<%@ page import="vsp.*"%>
<%@page import="vsp.utils.Enumeration.SecurityQuestion"%>
<%@ page import="java.text.*" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<style type="text/css">@import url("css/main.css")</style>
<title>VSP - Signup for an Account</title>
<body>
  <%@ include file="headers/SignUpHeader.jsp" %>
<div id="register">
  <c:if test="${errors != null}">
        <p id="errors">
        Error(s)!
        <ul>
        <c:forEach var="error" items="${errors}">
            <li>${error}</li>
        </c:forEach>
        </ul>
  </c:if>
  <c:if test="${loginSuccessMessage != null}">
    <p id="success">
      ${loginSuccessMessage }
    </p>
  </c:if>
    <form name='signupForm' action='register' method='POST'>
    <table>
      <tr>
        <td>Enter your User Name: </td>
        <td><input type='text' name='userName' size=46 /></td>
      </tr>
      <tr>
        <td>Enter your Email: </td>
        <td><input type='text' name='email' size=46 />
        </td>
      </tr>
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
        <td>Enter your Password: </td>
        <td><input type='password' name='password' size=46 /></td>
      </tr>
      <tr>
        <td>Confirm your Password: </td>
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