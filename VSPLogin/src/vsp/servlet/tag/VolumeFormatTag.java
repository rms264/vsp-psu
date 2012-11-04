package vsp.servlet.tag;

import java.io.IOException;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;

import javax.servlet.jsp.JspContext;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.tagext.SimpleTagSupport;

public class VolumeFormatTag extends SimpleTagSupport {
  
  private DecimalFormat volumeDf = new DecimalFormat();
  private DecimalFormatSymbols dfs = new DecimalFormatSymbols();
  private int value;
  
  public VolumeFormatTag(){
    super();
    dfs.setGroupingSeparator(',');
    volumeDf.setDecimalFormatSymbols(dfs);
  }
  
  public void setValue(int value){
    this.value = value;
  }
  
  public void doTag() throws IOException, JspException {
    JspContext jspContext = getJspContext();
    JspWriter out = jspContext.getOut();
   
    out.print("<td align=right>" + volumeDf.format(value) + "</td>");
  }
}
