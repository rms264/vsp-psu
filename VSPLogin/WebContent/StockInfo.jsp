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
<title>VSP Stock Info</title>
</head>
<body>
    <c:if test="${stockInfo != null }">
    <font size=1 face=Arial><b>Real-Time Quote</b><br>
    <table border=1 cellpadding=4 cellspacing=0 width=240 style="background-color: #fdf8e6">
      <tr>
        <td colspan=2>
          <table width=240 cellpadding=0 cellspacing=0>
            <tr>
              <td><font size=4><b>${stockInfo.symbol }</b></font>
                <br><font size=2>${stockInfo.description }</font>
              </td>
              <td align=right valign=top>
                <font size=1 face=Arial>
                  <vspTag:refreshStockTag uri ="${request.requestURI }" stockSymbol="${stockInfo.symbol }"/>
                </font>
              </td>
            </tr>
          </table>
        </td>
      </tr>
      <tr>
        <td>
          <table width=120 cellpadding=0 cellspacing=0>
            <tr>
              <td>Last</td>
              <vspTag:decimalFormatTag value="${stockInfo.lastTradePrice }"/>
            </tr>
          </table>
        </td>
        <td>
          <table width=120 cellpadding=0 cellspacing=0>
            <tr>
              <td>Volume</td>
              <vspTag:decimalFormatTag value="${stockInfo.volume }"/>
            </tr>
          </table>
        </td>
      </tr> 
      <tr>
        <td>
          <table width=120 cellpadding=0 cellspacing=0>
            <tr>
              <td>Change $</td>
              <vspTag:decimalFormatTag value="${stockInfo.priceChangeSinceOpen }" color="true"/>
            </tr>
          </table>
        </td>
        <td>
          <table width=120 cellpadding=0 cellspacing=0>
            <tr>
              <td>Day High</td>
              <vspTag:decimalFormatTag value="${stockInfo.dayHigh }"/>
            </tr>
          </table>
        </td>
      </tr>
      <tr>
        <td>
          <table width=120 cellpadding=0 cellspacing=0>
            <tr>
              <td>Change %</td>
              <vspTag:decimalFormatTag value="${stockInfo.changeSinceClosePercent }" color="true" percent="true"/>
            </tr>
          </table>
        </td>
        <td>
          <table width=120 cellpadding=0 cellspacing=0>
            <tr>
              <td>Day Low</td>
              <vspTag:decimalFormatTag value="${stockInfo.dayLow }"/>
            </tr>
          </table>
        </td>
      </tr>
      <tr>
        <td>
          <table width=120 cellpadding=0 cellspacing=0>
            <tr>
              <td>Bid</td>
              <vspTag:decimalFormatTag value="${stockInfo.bid }"/>
            </tr>
          </table>
        </td>
        <td>
          <table width=120 cellpadding=0 cellspacing=0>
            <tr>
            <td>Open</td>
            <vspTag:decimalFormatTag value="${stockInfo.open }"/>
          </tr>
        </table>
      </td>
    </tr>
    <tr>
      <td>
        <table width=120 cellpadding=0 cellspacing=0>
          <tr>
            <td>Ask</td>
            <vspTag:decimalFormatTag value="${stockInfo.ask }"/>
          </tr>
        </table>
      </td>
      <td>
        <table width=120 cellpadding=0 cellspacing=0>
          <tr>
            <td>Prev Close</td>
            <vspTag:decimalFormatTag value="${stockInfo.close }"/>
          </tr>
        </table>
      </td>
    </tr>
  </table></font>
  </c:if>
  <c:if test="${noData != null }">
    <font size=1 face=Arial>
      <b>Real-Time Quote</b>
        <br><br>
        <i>Please enter a stock symbol.</i>
    </font>
  </c:if>
  
  
</body>
</html>