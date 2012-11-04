package vsp.servlet.tag;

import java.io.IOException;

import javax.servlet.jsp.JspContext;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.tagext.SimpleTagSupport;

public class RefreshStockTag extends SimpleTagSupport {
  private String uri;
  private String stockSymbol;
  
  public void setUri(String uri){
    this.uri = uri;
  }
  public void setStockSymbol(String stockSymbol){
    this.stockSymbol = stockSymbol;
  }
  
  public void doTag() throws IOException, JspException {
    JspContext jspContext = getJspContext();
    JspWriter out = jspContext.getOut();
   
    out.print("<a href='" + uri + "?stockSymbol=" + stockSymbol + "'>Refresh</a>");
  }
}
