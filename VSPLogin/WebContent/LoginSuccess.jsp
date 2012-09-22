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
  <body>
  
    <h2>Virtual Stock Portfolio (VSP) System</h2>

  <table>
  <tr><td align=center colspan=2><b>User Info:</b></td></tr>
  <tr><td>Email Address: &nbsp;&nbsp;</td><td>fake@fake.com</td></tr>
  <tr><td>Sign-up Date: </td><td>2012-09-20</td></tr>
  </table>
  
  <p><a href="Logout.jsp">Logout</a></body>
</html>