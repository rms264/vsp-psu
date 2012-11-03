<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<style type="text/css">@import url("css/main.css")</style>
<title>VSP Logout</title>
</head>
<%@ include file="headers/LogoutHeader.jsp" %>
<body background="images/background.png">

  <div id=login>
    <p>User: <i>${userName }</i> has been logged out.</p>
  </div>
</body>
</html>