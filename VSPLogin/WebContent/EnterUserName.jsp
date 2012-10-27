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
<title>VSP - Reset User Password</title>
<style type="text/css">
  input {
      font-family: sans-serif;                
  }
</style>
</head>
<body>

<h2>Virtual Stock Portfolio (VSP) System</h2>
<%
    // If process is true, attempt to validate and process the form
    if ("true".equals(request.getParameter("resetPassword"))
        && !request.getParameter("username").isEmpty())
    {
      try
      {
        VspServiceProvider vsp = new VspServiceProvider();
        // throws on error
        if(Validate.userNameExistsInDb(request.getParameter("username"))){
          pageContext.forward("EnterSecurityQuestion.jsp");

        }
        else{
          out.println("<p><font color=red> Unknown User Name</font>");
        }
      }
      catch(Exception ex)
      {
        out.println("<p><font color=red>" + ex.getLocalizedMessage() + "</font>");
      }
    }

    {
      out.println("<form name='actionForm' action='" + request.getRequestURI() + "' method='POST'>");
      out.println("<input type='hidden' name='resetPassword' value='true' />");
      out.println("<table>");
      out.println("<tr><td>Enter your User Name: </td><td><input type='text' name='username' size=46 /></td></tr>");
      out.println("<tr><td>&nbsp;</td><td><input type='submit' value='Submit'></td></tr>");
      out.println("</table>");
      out.println("</form>");
    }
%>
<li><a href="Portfolio.jsp">Return to Login</a></li>
</body>
</html>