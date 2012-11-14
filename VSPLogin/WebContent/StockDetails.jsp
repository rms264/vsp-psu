<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ page import="vsp.*"%>
<%@ page import="vsp.dal.requests.*"%>
<%@ page import="vsp.statistics.*"%>
<%@ page import="vsp.dataObject.*"%>
<%@ page import="vsp.utils.*"%>
<%@ page import="vsp.utils.Enumeration.*"%>
<%@ page import="java.text.*" %>
<%@ page import="java.util.*" %>
<%@ taglib uri="/WEB-INF/vsp_tags.tld" prefix="vspTag"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<style type="text/css">@import url("css/main.css")</style>
<title>VSP - Stock Details</title>
</head>

<body>
  <%@ include file="headers/LoggedInHeader.jsp" %>
  
  <h2>
    ${stockInfo.stock.stockDescription}
  </h2>

  <table border="0" cellpadding="0" cellspacing="0" width="100%" bgcolor="#ffffff">
  <tr>
    <td align="center">
      <c:choose>
         <c:when test="${performance != null }">
           <img src="getStockChart?symbol=${stockInfo.stock.stockSymbol}">
         </c:when>
        <c:otherwise>
         <img src="getStockChart?symbol=${stockInfo.stock.stockSymbol}&year=1">
        </c:otherwise>
      </c:choose>
      <c:if test="${performance != null }">
        
      </c:if>
    </td>
  </tr>
  </table>
  
<c:if test="${stockInfo != null }">  
<div id="transHistory">
  <h3>Current Stock Information</h3>
  <table border=1 cellpadding=4 cellspacing=0>
    <tr>
      <td align=center><b>Last</b></td>
      <td align=center><b>Day High</b></td>
      <td align=center><b>Day Low</b></td>
      <td align=center><b>Open</b></td>
      <td align=center><b>Ask</b></td>
      <td align=center><b>Prev Close</b></td>
      <td align=center><b>Bid</b></td>
      <td align=center><b>Change ($)</b></td>
      <td align=center><b>Change (%)</b></td>
      <td align=center><b>Volume</b></td>
    </tr>
    <tr>
      <vspTag:decimalFormatTag value="${stockInfo.lastTradePrice }"/>
      <vspTag:decimalFormatTag value="${stockInfo.dayHigh }"/>
      <vspTag:decimalFormatTag value="${stockInfo.dayLow }"/>
      <vspTag:decimalFormatTag value="${stockInfo.open }"/>
       <vspTag:decimalFormatTag value="${stockInfo.ask }"/>
       <vspTag:decimalFormatTag value="${stockInfo.close }"/>
       <vspTag:decimalFormatTag value="${stockInfo.bid }"/>
      <vspTag:decimalFormatTag value="${stockInfo.priceChangeSinceOpen }" color="true"/>
      <vspTag:decimalFormatTag value="${stockInfo.changeSinceClosePercent }" color="true" percent="true"/>
      <vspTag:decimalFormatTag value="${stockInfo.volume }"/>
    </table>
      
</div>
</c:if>


 <div id="transHistory">
 <h3>Volatility</h3>
  <table border=1 cellpadding=4 cellspacing=0>
    <tr>
      <td colspan=2 align=center><b>Stock</b></td>
      <td colspan=4 align=center><b>Volatility</b></td>
    </tr>
    <tr>
      <td align=center><b>Symbol</b></td>
      <td align=center><b>Name</b></td>
      <td align=center><b>Day</b></td>
      <td align=center><b>Week</b></td>
      <td align=center><b>Month</b></td>
      <td align=center><b>Year</b></td>
    </tr>
    <tr>
      <td>${stockInfo.stock.stockSymbol }</td>
      <td>${stockInfo.stock.stockDescription }</td>
      <vspTag:decimalFormatTag value="${volData.day }" percent="true"/>
      <vspTag:decimalFormatTag value="${volData.week }" percent="true"/>
      <vspTag:decimalFormatTag value="${volData.month }" percent="true"/>
      <vspTag:decimalFormatTag value="${volData.year }"  percent="true"/>
    </tr>
  </table>
  <br> 
</div>
 
 
<c:if test="${performance != null }">

<div id="transHistory">
 <h3>Geometric Average Rate of Return</h3>
 <%
	try
	{
		String symbol = request.getParameter("symbol");
		String userName = request.getRemoteUser();
		AccountData account = Users.requestAccountData(userName);
		Stock stock = Stocks.getStock(symbol);
		
		GeometricAverageRateOfReturn gror = new GeometricAverageRateOfReturn(account);
	    double gror_day = gror.getAverageRateOfReturn(stock, TimeType.DAY);
	    double gror_week = gror.getAverageRateOfReturn(stock, TimeType.WEEK);
	    double gror_month = gror.getAverageRateOfReturn(stock, TimeType.MONTH);
	    double gror_year = gror.getAverageRateOfReturn(stock, TimeType.YEAR);
	      			   	   	    	
	    DecimalFormat df = new DecimalFormat("0.0000");		
		
	    out.println("<table border=1 cellpadding=4 cellspacing=0>");
		
		out.println("<tr>");
		out.println("<td colspan=2 align=center><b>Stock</b></td>");
		out.println("<td colspan=4 align=center><b>Geometric Average Rate of Return</b></td>");
		out.println("</tr>");
		
		out.println("<tr>");
		out.println("<td align=center><b>Symbol</b></td>");
		out.println("<td align=center><b>Name</b></td>");
		out.println("<td align=center><b>Day</b></td>");
		out.println("<td align=center><b>Week</b></td>");
		out.println("<td align=center><b>Month</b></td>");
		out.println("<td align=center><b>Year</b></td>");
		out.println("</tr>");
			
		out.println("<tr>");
		out.println("<td>" + symbol + "</td>");
		out.println("<td>" + stock.getStockDescription() + "</td>");
		out.println("<td align=right>" + VSPUtils.formatColor(gror_day*100, df, true) + "</td>");
		out.println("<td align=right>" + VSPUtils.formatColor(gror_week*100, df, true) + "</td>");
		out.println("<td align=right>" + VSPUtils.formatColor(gror_month*100, df, true) + "</td>");
		out.println("<td align=right>" + VSPUtils.formatColor(gror_year*100, df, true) + "</td>");
		out.println("</tr>");
			
		out.println("</table><br>");	
	}
	catch(Exception ex)
	{
		out.println("<p><b><i>Unable to retrieve Geometric Average Rate of Return:</i></b><br><font color=red>" + ex.toString() + "</font>");
		out.println("<p>&nbsp;</p>");
		out.println("<p>&nbsp;</p>");
	}
%>
</div>

 

 <div id="transHistory">
 <h3>Compound Annual Growth Rate and Return On Investment</h3>
 <%
	try
	{
		String symbol = request.getParameter("symbol");
		String userName = request.getRemoteUser();
		AccountData account = Users.requestAccountData(userName);
		Stock stock = Stocks.getStock(symbol);
		
		CompoundAnualGrowthRate cagr = new CompoundAnualGrowthRate(account);
	    double cagr_value = cagr.calculate(symbol);
	      
	    ReturnOnInvestment roi = new ReturnOnInvestment(account);	    
	    double roi_value = roi.getReturnOnInvestment(symbol);

	    DecimalFormat df = new DecimalFormat("0.0000");
	    
		out.println("<table border=1 cellpadding=4 cellspacing=0>");
		out.println("<tr>");
		out.println("<td align=center><b>Symbol</b></td>");
		out.println("<td align=center><b>Name</b></td>");
		out.println("<td align=center><b>Compound Annual Growth Rate</b></td>");
		out.println("<td align=center><b>Return On Investment</b></td>");
		out.println("</tr>");
			
		out.println("<tr>");
		out.println("<td>" + symbol + "</td>");
		out.println("<td>" + stock.getStockDescription() + "</td>");
		out.println("<td align=right>" + VSPUtils.formatColor(cagr_value*100, df, true) + "</td>");
		out.println("<td align=right>" + VSPUtils.formatColor(roi_value*100, df, true) + "</td>");
		out.println("</tr>");
			
		out.println("</table><br>");	
	}
	catch(Exception ex)
	{
		out.println("<p><b><i>Unable to retrieve Compound Annual Growth Rate and Return On Investment:</i></b><br><font color=red>" + ex.toString() + "</font>");
		out.println("<p>&nbsp;</p>");
		out.println("<p>&nbsp;</p>");
	}
%>
</div>

<div id="transHistory">
<h3>Transaction History</h3>
<%
  try
  {
    String symbol = request.getParameter("symbol");
    String userName = request.getRemoteUser();    
         
    // throws on error
    List<StockTransaction> transactions = Transactions.getAllExecutedTransactionsForUserAndStock(userName,symbol);
    
    if (transactions != null && transactions.size() > 0)
    {
      DecimalFormat df = new DecimalFormat("0.00");
      SimpleDateFormat sd = new SimpleDateFormat("yyyy-MM-dd");
      
      out.println("<table border=1 cellpadding=4 cellspacing=0>");
      out.println("<tr>");
      out.println("<td align=center><b>Symbol</b></td>");
      out.println("<td align=center><b>Name</b></td>");     
      out.println("<td align=center><b>Date</b></td>");
      out.println("<td align=center><b>Type</b></td>");
      out.println("<td align=center><b>Action</b></td>");
      out.println("<td align=center><b>Quantity</b></td>");
      out.println("<td align=center><b>Per Share</b></td>");
      out.println("<td align=center><b>Value</b></td>");
      out.println("<td align=center><b>Note</b></td>");
      out.println("<td align=center><b>Id</b></td>");
      out.println("</tr>");
      
      
      
      
      Order order = null;
      TransactionType type = TransactionType.DEFAULT;
      StockTransaction transaction = null;
      String note;
      for (int i = 0; i < transactions.size(); ++i)
      {
        transaction = transactions.get(i);
        type = transaction.getType();
        order = transaction.getOrder();
        
        out.println("<tr>");
        
        out.println("<td>" + transaction.getStock().getStockSymbol() + "</td>");
        out.println("<td>" + transaction.getStock().getStockDescription() + "</td>");
        
        out.println("<td>" + sd.format(transaction.getDateTime()) + "</td>");
        out.println("<td>" + type.toString() + "</td>");
        
        if (type == TransactionType.DIVIDEND)
        {
          out.println("<td> -- </td>");
        }
        else
        {
          out.println("<td>" + order.getAction().toString() + "</td>");
        }             
        
        if (type == TransactionType.CANCELLATION)
        {
          out.println("<td align=center>" + transaction.getOrder().getQuantity() + "</td>");
          out.println("<td align=center>N/A</td>");
          out.println("<td align=center>N/A</td>");
        }
        else
        {
          out.println("<td align=center>" + transaction.getQuantity() + "</td>");
          out.println("<td align=center>" + df.format(transaction.getPricePerShare()) + "</td>");
          out.println("<td align=center>" + df.format(transaction.getValue()) + "</td>");
        }
        
        note = transaction.getNote();
        if (note != null && !note.isEmpty())
        {
          out.println("<td>" + note + "</td>");
        }
        else
        {
          out.println("<td>&nbsp;</td>");
        }

        out.println("<td>" + transaction.getId() + "</td>");
        out.println("</tr>");
      }
      out.println("</table><br>");
    }
    else
    {
      out.println("<p><b><i>No transactions.</i></b></p>");
      out.println("<p>&nbsp;</p>");
    }
  }
  catch(Exception ex)
  {
    out.println("<p><b><i>Unable to retrieve transaction history:</i></b><br><font color=red>" + ex.toString() + "</font>");
    out.println("<p>&nbsp;</p>");
    out.println("<p>&nbsp;</p>"); 
  }
%>
 </div>
 </c:if>
 <a href="javascript:history.go(-1)">
    Go Back
 </a>
</body>
</html>
