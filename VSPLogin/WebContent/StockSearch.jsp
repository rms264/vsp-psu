<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<%@ page session="true"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="/WEB-INF/vsp_tags.tld" prefix="vspTag"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<style type="text/css">@import url("css/main.css")</style>
<title>VSP - Stock Search</title>
</head>
<body>
  <%@ include file="headers/LoggedInHeader.jsp" %>
  
    <table width=100%>
  <tr><td colspan=2>Welcome, ${pageContext.request.remoteUser}</td></tr>
  <tr><td>&nbsp;</td><td align=center><a href="viewPortfolio">Portfolio</a> | 
  <b>Stock Search</b> | <a href="order">Order Stock</a> | <a href="pendingOrders">Pending Orders</a> | 
  <a href="transactionHistory">Transaction History</a> | <a href="viewUserInfo">User Info</a></td></tr>
  </table>
  
  <div id="stockSearch">
    <h3>Stock Search</h3> 
    <c:if test="${queryString != null}">
      <p>Search results for <b>${queryString }</b>:</p>
      <c:choose>
        <c:when test="${stockList != null && not empty stockList}">
          <table border=1 cellpadding=4 cellspacing=0>
          <tr>
            <td align=center><b>Symbol</b></td>
            <td align=center><b>Description</b></td>
            <td align=center>&nbsp;</td>
            <td align=center>&nbsp;</td>
          </tr>
          <c:forEach var="stock" items="${stockList}">
            <tr>
              <td>${stock.stockSymbol }</td>
              <td>${stock.stockDescription }</td>
              <td align=center><a href="order?action=0&symbol=${stock.stockSymbol}">Buy</a></td>
              <td align=center><a href="viewStockDetails?symbol=${stock.stockSymbol}">Details</a></td>
            </tr>
          </c:forEach>
        </table>
        </c:when>
        <c:otherwise>
          <p><b><i>There were no search results.</i></b></p>
        </c:otherwise>
      </c:choose>
    </c:if>
    <p><form name='stockSearch' action="stockSearch" method='POST'>
    <table>
      <tr>
        <td>Search for: </td>
        <td><input type='text' name="queryString" /></td>
      </tr>
      <tr>
        <td>&nbsp;</td>
        <td><input type='submit' value='Search'></td>
      </tr>
    </table>
    </form>
  </div>  
</body>
</html>