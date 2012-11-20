<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<%@ page session="true"%>
<%@ page import="vsp.*"%>
<%@ page import="java.util.*"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<style type="text/css">@import url("css/main.css")</style>
<title>VSP - Administration</title>
</head>
<body>
  <%@ include file="../headers/AdminHeader.jsp" %>

  <div id="admin">
  <h3>Account Administration</h3>
    <c:if test="${deleteSuccess != null}">
    <p id="success">
      ${deleteSuccess }
    </p>
  </c:if>
  <c:if test="${passwordUpdate != null}">
      <p id="success">
        ${passwordUpdate }
      </p>
    </c:if>
   <c:choose>
    <c:when test="${traders != null}">
      <table border=1 cellpadding=4 cellspacing=0 width=500 align=center>
      <tr>
        <td colspan=4 align=center><b>Trader Accounts:</b></td>
      </tr>
      <c:forEach var="trader" items="${traders}">
        <tr>
          <td width=225>${trader}</td>
          <td colspan=2 align=right><font size=2>&nbsp;&nbsp;<a href="admin?password=${trader}">Set Password</a></font></td>
          <td colspan=2 align=right><font size=2>&nbsp;&nbsp;<a href="admin?delete=${trader}">Delete</a></font></td>
        </tr>
      </c:forEach>
      </table>
   </c:when>
   <c:otherwise>
    <p><i>No traders exist in the database.</i>
   </c:otherwise>
   </c:choose>
  </div>
</body>
</html>