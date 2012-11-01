<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<%@ page session="true"%>
<%@ page import="vsp.*"%>
<%@ page import="vsp.dataObject.*"%>
<%@ page import="vsp.utils.*"%>
<%@ page import="java.util.*"%>
<%@ page import="java.text.*"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>VSP - Stock Info</title>
</head>
<body>
  <% 
  	boolean infoLoaded = false;
	StockInfoServiceProvider sp = new StockInfoServiceProvider();
	// If process is true, attempt to validate and process the form
	String stockSymbol = request.getParameter("stockSymbol");
    if (stockSymbol != null && !stockSymbol.isEmpty())
    {
    	try
    	{    		
    		StockInfo stockInfo = sp.requestCurrentStockData(stockSymbol);
    		if (stockInfo != null)
    		{
    			infoLoaded = true;
    			DecimalFormat df = new DecimalFormat("0.0000");
    			DecimalFormat volumeDf = new DecimalFormat();
    			DecimalFormatSymbols dfs = new DecimalFormatSymbols();
    			dfs.setGroupingSeparator(',');
    			volumeDf.setDecimalFormatSymbols(dfs);
    			
    			out.println("<font size=1 face=Arial><b>Real-Time Quote</b><br><table border=1 cellpadding=4 cellspacing=0 width=240>");
    			out.println("<tr>");
    			out.println("<td colspan=2><table width=240 cellpadding=0 cellspacing=0><tr><td><font size=4><b>" + stockInfo.getSymbol() + "</b></font><br><font size=2>" + stockInfo.getStock().getStockDescription() +"</font></td>");
    			out.println("<td align=right valign=top><font size=1 face=Arial><a href='" + request.getRequestURI() + "?stockSymbol=" + stockInfo.getSymbol() + "'>Refresh</a></font></td></tr></table></td>");
    			out.println("</tr>");
    			
    			out.println("<tr>");
    			out.println("<td><table width=120 cellpadding=0 cellspacing=0><tr><td>Last</td><td align=right>" + df.format(stockInfo.getLastTradePrice()) +"</td></tr></table></td>");
    			out.println("<td><table width=120 cellpadding=0 cellspacing=0><tr><td>Volume</td><td align=right>" + volumeDf.format(stockInfo.getVolume()) + "</td></tr></table></td>");
    			out.println("</tr>");
    			
    			out.println("<tr>");
    			out.println("<td><table width=120 cellpadding=0 cellspacing=0><tr><td>Change $</td><td align=right>" + VSPUtils.formatColor(stockInfo.getPriceChanceSinceOpen(), df) +"</td></tr></table></td>");
    			out.println("<td><table width=120 cellpadding=0 cellspacing=0><tr><td>Day High</td><td align=right>" + df.format(stockInfo.getDayHigh()) + "</td></tr></table></td>");
    			out.println("</tr>");
    			
       			out.println("<tr>");
    			out.println("<td><table width=120 cellpadding=0 cellspacing=0><tr><td>Change %</td><td align=right>" + VSPUtils.formatColor(stockInfo.getChangeSinceClosePercent(), df, true) +"</td></tr></table></td>");
    			out.println("<td><table width=120 cellpadding=0 cellspacing=0><tr><td>Day Low</td><td align=right>" + df.format(stockInfo.getDayLow()) + "</td></tr></table></td>");
    			out.println("</tr>");
    			
       			out.println("<tr>");
    			out.println("<td><table width=120 cellpadding=0 cellspacing=0><tr><td>Bid</td><td align=right>" + df.format(stockInfo.getBid()) +"</td></tr></table></td>");
    			out.println("<td><table width=120 cellpadding=0 cellspacing=0><tr><td>Open</td><td align=right>" + df.format(stockInfo.getOpen()) + "</td></tr></table></td>");
    			out.println("</tr>");
    			
       			out.println("<tr>");
    			out.println("<td><table width=120 cellpadding=0 cellspacing=0><tr><td>Ask</td><td align=right>" + df.format(stockInfo.getAsk()) +"</td></tr></table></td>");
    			out.println("<td><table width=120 cellpadding=0 cellspacing=0><tr><td>Prev Close</td><td align=right>" + df.format(stockInfo.getClose()) + "</td></tr></table></td>");
    			out.println("</tr>");
    			
    			out.println("</table></font>");
    		}
    	}
    	catch(Exception ex)
    	{
    		out.println("<p><font color=red>" + ex.getLocalizedMessage() + "</font>");
    	}
    }
	
	
	if (!infoLoaded)
	{
	  	out.println("<font size=1 face=Arial><b>Real-Time Quote</b><br><br><i>Please enter a stock symbol.</i></font>");
	}
  %>
  
</body>
</html>