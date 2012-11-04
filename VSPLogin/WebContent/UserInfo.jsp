<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page session="true"%>

<%@ page import="vsp.*"%>
<%@ page import="vsp.dataObject.*"%>
<%@ page import="java.text.*" %>
<%@ taglib uri="/WEB-INF/vsp_tags.tld" prefix="vspTag"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<html>
<head>
<style type="text/css">@import url("css/main.css")</style>
<title>VSP - User Info</title>
<body>
  <%@ include file="headers/LoggedInHeader.jsp" %>
  
  <c:if test="${requestScope.errors != null}">
    <p id="errors">
      Error(s)!
      <ul>
      <c:forEach var="error" items="${requestScope.errors}">
        <li>${error}</li>
      </c:forEach>
      </ul>
    </p>
  </c:if>
    <div id="userInfo">
      <table>
        <tr>
          <td colspan=2><h3><b>User Info:</b></h3></td>
        </tr>
        <tr>
          <td>Email Address: &nbsp;&nbsp;</td>
          <td>${userAccount.email }</td>
        </tr>
        <tr>
          <td>Sign-up Date: </td>
          <vspTag:dateFormatter date="${userAccount.signup }"/>
        </tr>
      </table>
    </div>
    
<ul>
<li><a href="updatePassword">Change Password</a></li>
<li><a href="viewPortfolio">Return</a></li>
</ul>
  
  </body>
</html>