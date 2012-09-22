<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page session="true"%>

<%--  <%@ page import="vsp.*"%>
<%@ page import="vsp.VspWebServiceImplStub.*" %>
<%@ page import="org.apache.axis2.*" %>

<%
	VspWebServiceImplStub stub = new VspWebServiceImplStub();
	GetAccountInfo getAccountInfo0 = new GetAccountInfo();
	getAccountInfosetUserName();
	GetAccountInfoResponse resp = stub.getAccountInfo(getAccountInfo0);
	vsp.VspWebServiceImplStub.AccountData data = resp.get_return();
%>--%>


<html>
  <head>
    <title>Login Success</title>
  </head>
  <body><h2>User Logged In Successfully</h2>
  
  <p><b>User Info:</b>
  <table>
  <tr><td><b>Email Address:</b></td><td><%  %></td></tr>
  <tr><td><b>Sign-up Date:</b></td><td><%  %></td></tr>
  </table>
  
  <p><a href="Logout.jsp">Logout</a></body>
</html>