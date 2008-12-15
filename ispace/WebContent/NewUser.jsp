<%@ page contentType="text/html; charset=iso-8859-1" language="java" import="java.sql.*" errorPage="" %>
<%@ include file="Connections/conn.jsp" %>

<%
Driver DriverAddUser = (Driver)Class.forName(MM_conn_DRIVER).newInstance();
Connection ConnAddUser = DriverManager.getConnection(MM_conn_STRING,MM_conn_USERNAME,MM_conn_PASSWORD);
Statement AddUser = ConnAddUser.createStatement();

if(request.getParameter("action") != null){
	String uname=null;
	String pass =null;
	String pass2 =null;
	String email =null;
	
	uname = request.getParameter("username");
	pass = request.getParameter("password");
	pass2 = request.getParameter("password2");
	email = request.getParameter("email");
	
if (uname.length()<6 || uname.length()>15) { 
	%> <script language="javascript">	alert("Invalid Username");	</script> <%
	  } else if (pass.length()<6 || pass.length()>12 || pass.equals(pass2)==false) { 
	%> <script language="javascript">	alert("Invalid Password");	</script> <%
	  } else if (email.length()==0 || email.length()>35) { 
	%> <script language="javascript">	alert("Invalid Email");	</script> <%
	  } else {
	  	try {
			AddUser.executeUpdate("INSERT INTO users VALUES ('"+uname+"','"+pass+"','"+email+"') ");
			AddUser.close();
			%><jsp:forward page="Successful.jsp"></jsp:forward><%
			} catch (Exception e)
			{	%> <script language="javascript">	alert("Choose another Username!");	</script> <% }
			}
		} %>
	
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1">
<title>Register</title>
<style type="text/css">
<!--
body,td,th {
	font-family: Verdana, Arial, Helvetica, sans-serif;
	font-size: 16px;
	color: #000000;
	font-style: italic;
}
body {
	background-color: #CCCCCC;
}
.style2 {font-size: 16px}
.style3 {
	font-size: 18px;
	font-weight: bold;
}
-->
</style></head>

<body>
<p>&nbsp;</p>
<p>&nbsp;</p>
<p align="center">&nbsp; </p>

<form name="form1" method="post" action="NewUser.jsp">
<input type="hidden" name="action">

<table align="center" border="1" width="45%"><tr><td>
<p>&nbsp;</p>
<table border="0" align="center">
  <tr>
    <td colspan="2"><p class="style3">New User:</p>
      </td>
  </tr>
  <tr>
    <td width="32%"><span class="style2">Username: </span></td>
    <td width="68%"><span class="style2">
      <input name="username" type="text" size="23">
    </span></td>
  </tr>
  <tr>
    <td ><span class="style2">Password: </span></td>
    <td><span class="style2">
      <input name="password" type="password" size="25">
    </span></td>
  </tr>
  <tr>
    <td><span class="style2">Confirm Password: </span></td>
    <td><span class="style2">
      <input name="password2" type="password" size="25">
    </span></td>
  </tr>
  <tr>
    <td><span class="style2">Email: </span></td>
    <td><span class="style2">
      <input name="email" type="text" size="40">
    </span></td>
  </tr>
  <tr>
    <td colspan="2" align="center"><input type="submit" name="Submit" value="Submit">
      <input type="reset" name="reset" value="Clear"></td>
    </tr>
</table>
<p>&nbsp;</p>
</td></tr></table>
</form>
<p>&nbsp;</p>
</body>
</html>

<% if (ConnAddUser!=null) ConnAddUser.close(); %>