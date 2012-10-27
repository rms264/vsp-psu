<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<%@ page import="vsp.*"%>
<%@page import="vsp.utils.Enumeration.SecurityQuestion"%>
<%@ page import="java.text.*" %>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>VSP - Change Account Password</title>
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
    // If process is true, attempt to validate and process the form
    if ("true".equals(request.getParameter("resetPassword"))
        && !request.getParameter("username").isEmpty()
    		&& !request.getParameter("password_update1").isEmpty()
    		&& !request.getParameter("password_update2").isEmpty())
    {
      boolean passwordUpdated = false;
      String message = "";
    	try
    	{
    		VspServiceProvider vsp = new VspServiceProvider();
    		// throws on error
    		  passwordUpdated = vsp.updateUserPassword( 
    		      request.getParameter("username"),
    		      request.getParameter("password_update1"),
              request.getParameter("password_update2"));
    	}
    	catch(Exception ex)
    	{
    	  message = ex.getLocalizedMessage();
    	}
    	if(passwordUpdated){
    	  resultInfo = "<p><font color=green> Password Changed Successfully </font>";
    	}
    	else{
    	  resultInfo = "<p><font color=red>" + message + "</font>";
    	}
    	session.setAttribute("passwordReset",resultInfo);
      response.sendRedirect("Portfolio.jsp");
    }
    else{
	  	out.println("<form name='actionForm' action='ResetUserPassword.jsp' method='POST'>");
	  	out.println("<input type='hidden' name='resetPassword' value='true' />");
	  	out.println("<input type='hidden' name='username' value='" + request.getParameter("username")+"'/>");
	   	out.println("<table>");
	   	out.println("<tr><td>Enter your New Password: </td><td><input type='password' name='password_update1' size=46 /></td></tr>");
	   	out.println("<tr><td>Enter your Repeat New Password: </td><td><input type='password' name='password_update2' size=46 /></td></tr>");
	   	
	   	out.println("<tr><td>&nbsp;</td><td><input type='submit' value='Submit'></td></tr>");
	   	out.println("</table>");
	   	out.println("</form>");
    }
%>

<ul>
<li><a href="LoginPage.jsp">Return to Login</a></li>
</ul>

</body>
</html>