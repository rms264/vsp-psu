<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri ="/WEB-INF/vsp_tags.tld" prefix ="vspTag"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>VSP - Errors</title>
<style type="text/css">@import url("css/main.css")</style>
</head>
<body>
<%@ include file="headers/HomeHeader.jsp" %>
<div id="errorBlock">
  <c:if test="${requestScope.errors != null}">
    <p id="errors">
      Error(s)!
      <ul>
        <c:forEach var="error" items="${errors}">
          <li class="li-error">${error}</li>
        </c:forEach>
      </ul>
      </p>
   </c:if>
</div>
</body>
</html>