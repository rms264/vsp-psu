<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<style type="text/css">@import url("css/main.css")</style>
<title>VSP - Stock Details</title>
</head>

<body>
  <%@ include file="headers/LoggedInHeader.jsp" %>

  <img src="getStockChart?symbol=${param.symbol }">
</body>
</html>