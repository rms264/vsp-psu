package vsp.servlet.tag;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.servlet.jsp.JspContext;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.tagext.SimpleTagSupport;

public class DateFormatTag extends SimpleTagSupport {
  private final SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
  private Date date = null;
  private String formattedDate = "";
  
  public void setDate(Date date){
    this.date = date;
  }
  
  public void doTag() throws IOException, JspException {
    JspContext jspContext = getJspContext();
    JspWriter out = jspContext.getOut();
   
    if(date != null){
      formattedDate = formatter.format(date);
    }
    out.print("<td>"+formattedDate+"</td>");
  }
}
