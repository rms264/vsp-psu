package vsp.servlet.tag;

import java.io.IOException;
import java.text.DecimalFormat;

import javax.servlet.jsp.JspContext;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.tagext.SimpleTagSupport;

import vsp.utils.VSPUtils;

public class DecimalFormatTag extends SimpleTagSupport {
  private final DecimalFormat df = new DecimalFormat("###,###,##0.0000");
  private double value;
  private boolean color = false;
  private boolean percent = false;
  
  public void setValue(double value){
    this.value = value;
  }
  
  public void setColor(boolean color){
    this.color = color;
  }
  
  public void setPercent(boolean percent){
    this.percent = percent;
  }
  
  public void doTag() throws IOException, JspException {
    JspContext jspContext = getJspContext();
    JspWriter out = jspContext.getOut();
    if(color && percent){
      out.print("<td align=right>" + VSPUtils.formatColor(value, df, percent) +"</td>");
    }else if(!color && percent){
      out.print("<td align=right>" + VSPUtils.format(value, df, percent) +"</td>");
    }
    else{
      out.print("<td align=right>" + df.format(value) +"</td>");
    }
  }
}
