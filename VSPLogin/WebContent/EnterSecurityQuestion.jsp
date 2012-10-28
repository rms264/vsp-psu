<%@page import="vsp.utils.Validate"%>
<%@page import="java.util.logging.Logger"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<%@ page import="vsp.*"%>
<%@page import="vsp.utils.Enumeration.SecurityQuestion"%>
<%@ page import="java.text.*" %>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>VSP - Reset Account Password</title>
<style type="text/css">
  input {
      font-family: sans-serif;                
  }
</style>
</head>
<body>

<h2>Virtual Stock Portfolio (VSP) System</h2>

<%

  String resultInfo = "";
  VspServiceProvider vsp = new VspServiceProvider();
    // If process is true, attempt to validate and process the form
    if ("true".equals(request.getParameter("resetPassword"))
    		&& !request.getParameter("username").isEmpty())
    {
    	try
    	{
    	  String userName = request.getParameter("username");
    	  if(Validate.validateUserName(userName)){
    	    String question = vsp.getUserSecurityQuestion(userName).toString();
    	    
    	    out.println("<form name='actionForm' action='" + request.getRequestURI() + "' method='POST'>");
    	    out.println("<input type='hidden' name='updatePassword' value='true' />");
    	    out.println("<input type='hidden' name='username' value='" + userName+"' />");
          
          out.println("<table>");
          out.println("<p><font color=black>" + question + "</font>");
          out.println("<tr><td>Enter Security Question Answer: </td><td><input type='text' name='answer' size=46 /></td></tr>");
          out.println("<tr><td>&nbsp;</td><td><input type='submit' value='Submit'></td></tr>");
          out.println("</table>");
          out.println("</form>");
    	    
    	  }
    	}
    	catch(Exception ex)
    	{
    	  resultInfo = "<p><font color=red>" + ex.getLocalizedMessage() + "</font>";
    	}
    }
    else if("true".equals(request.getParameter("updatePassword"))
        && !request.getParameter("answer").isEmpty()
        && !request.getParameter("username").isEmpty())
    {
      try{
        if(vsp.checkUserSecurityQuestion(request.getParameter("username"),request.getParameter("answer"))){
          pageContext.forward("ResetUserPassword.jsp");
        }
        else{
          resultInfo = "<p><font color=red>Invalid Response</font>";
        }
      }
      catch(Exception ex){
        resultInfo = "<p><font color=red>" + ex.getLocalizedMessage() + "</font>";
      }
    }
    out.print(resultInfo);
%>

<ul>
<li><a href="Portfolio.jsp">Return to Login</a></li>
</ul>

</body>
</html>