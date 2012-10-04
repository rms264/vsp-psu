<%@page import="vsp.utils.Enumeration.SecurityQuestion"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<%@ page import="vsp.*"%>
<%@ page import="java.text.*" %>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>VSP - Signup for an Account</title>
<style type="text/css">
  input {
      font-family: sans-serif;                
  }
</style>
</head>
<body>

<h2>Virtual Stock Portfolio (VSP) System</h2>

<%
	Boolean showForm = true;
    // If process is true, attempt to validate and process the form
    if ("true".equals(request.getParameter("process"))
    		&& !request.getParameter("email").isEmpty()
    		&& !request.getParameter("username").isEmpty()
    		&& !request.getParameter("password1").isEmpty()
    		&& !request.getParameter("password2").isEmpty()
    		&& !request.getParameter("question").isEmpty()
    		&& !request.getParameter("answer").isEmpty())
    {
    	try
    	{
    		VspServiceProvider vsp = new VspServiceProvider();
    		// throws on error
    		vsp.createAccount(request.getParameter("username"), request.getParameter("password1"), 
    				request.getParameter("password2"), request.getParameter("email"), request.getParameter("question"),
    				request.getParameter("answer"));

    		session.setAttribute("signup","true");
    		response.sendRedirect("Portfolio.jsp");
   			showForm = false;
   			return;
    	}
    	catch(Exception ex)
    	{
    		out.println("<p><font color=red>" + ex.getLocalizedMessage() + "</font>");
    	}
    }

	if (showForm)
    {
    	out.println("<form name='actionForm' action='" + request.getRequestURI() + "' method='POST'>");
    	out.println("<input type='hidden' name='process' value='true' />");
    	out.println("<table>");
    	out.println("<tr><td>Enter your User Name: </td><td><input type='text' name='username' size=46 /></td></tr>");
    	out.println("<tr><td>Enter your Email: </td><td><input type='text' name='email' size=46 /></td></tr>");
    	out.println("<tr><td>Enter your Security Question: </td><td><select name='question' width=50>");
    	
//     	String[] securityQuestions = SecurityQuestions.getAllQuestions();
    	out.println("<option value='-1' selected>(Please select one:)</option>");
//     	for (int i = 0; i < securityQuestions.length; ++i)
//     	{
//     		out.println("<option value='" + Integer.toString(i) + "'>" + securityQuestions[i] + "</option>");
//     	}
    	for(SecurityQuestion question : SecurityQuestion.values()){
    		if(question == SecurityQuestion.DEFAULT)
    			continue;
    		out.println("<option value='" + Integer.toString(question.getVal()) + "'>" + question + "</option>");
    	}
    	
    	out.println("</select></td></tr>");
    	out.println("<tr><td>Select your Security Answer: &nbsp;</td><td><input type='text' name='answer' size=46 /></td></tr>");
    	out.println("<tr><td>Enter your Password: </td><td><input type='password' name='password1' size=46 /></td></tr>");
    	out.println("<tr><td>Confirm your Password: </td><td><input type='password' name='password2' size=46 /></td></tr>");
    	out.println("<tr><td>&nbsp;</td><td><input type='submit' value='Signup'></td></tr>");
    	out.println("</table>");
    	out.println("</form>");
    }
%>

</body>
</html>