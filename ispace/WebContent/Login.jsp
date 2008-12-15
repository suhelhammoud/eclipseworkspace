<%@ page language="java" import="java.sql.*" %>
<%@ include file="Connections/conn.jsp" %>
<%
// *** Validate request to log in to this site.
String MM_LoginAction = request.getRequestURI();
if (request.getQueryString() != null && request.getQueryString().length() > 0) {
  String queryString = request.getQueryString();
  String tempStr = "";
  for (int i=0; i < queryString.length(); i++) {
    if (queryString.charAt(i) == '<') tempStr = tempStr + "&lt;";
    else if (queryString.charAt(i) == '>') tempStr = tempStr + "&gt;";
    else if (queryString.charAt(i) == '"') tempStr = tempStr +  "&quot;";
    else tempStr = tempStr + queryString.charAt(i);
  }
  MM_LoginAction += "?" + tempStr;
}
String MM_valUsername=request.getParameter("username");
if (MM_valUsername != null) {
  String MM_fldUserAuthorization="";
  String MM_redirectLoginSuccess="Upload.jsp";
  String MM_redirectLoginFailed="Error.jsp";
  String MM_redirectLogin=MM_redirectLoginFailed;
  Driver MM_driverUser = (Driver)Class.forName(MM_conn_DRIVER).newInstance();
  Connection MM_connUser = DriverManager.getConnection(MM_conn_STRING,MM_conn_USERNAME,MM_conn_PASSWORD);
  String MM_pSQL = "SELECT username, password";
  if (!MM_fldUserAuthorization.equals("")) MM_pSQL += "," + MM_fldUserAuthorization;
  MM_pSQL += " FROM mydatabase.users WHERE username=\'" + MM_valUsername.replace('\'', ' ') + "\' AND password=\'" + request.getParameter("password").toString().replace('\'', ' ') + "\'";
  PreparedStatement MM_statementUser = MM_connUser.prepareStatement(MM_pSQL);
  ResultSet MM_rsUser = MM_statementUser.executeQuery();
  boolean MM_rsUser_isNotEmpty = MM_rsUser.next();
  if (MM_rsUser_isNotEmpty) {
    // username and password match - this is a valid user
    session.putValue("MM_Username", MM_valUsername);
    if (!MM_fldUserAuthorization.equals("")) {
      session.putValue("MM_UserAuthorization", MM_rsUser.getString(MM_fldUserAuthorization).trim());
    } else {
      session.putValue("MM_UserAuthorization", "");
    }
    if ((request.getParameter("accessdenied") != null) && false) {
      MM_redirectLoginSuccess = request.getParameter("accessdenied");
    }
    MM_redirectLogin=MM_redirectLoginSuccess;
  }
  MM_rsUser.close();
  MM_connUser.close();
  response.sendRedirect(response.encodeRedirectURL(MM_redirectLogin));
  return;
}
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1">
<title>iSpace Login</title>
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
.style2 {font-size: 24px}
-->
</style>
<p>&nbsp;</p>
<p align="center"><span class="style2">Welcome To iSpace</span></p>
<p>&nbsp;</p>
<form action="<%=MM_LoginAction%>" method="POST" name="Login" id="Login">
<table width="40%" align="center" border="1"><tr><td>
  <p align="center">&nbsp;</p>
  <p align="center">Please enter your username and password to log in:</p>
  <p align="center">Username:
      <input type="text" name="username">
  </p>
  <p align="center">Password:
      <input type="password" name="password">
  </p>
  <p align="center">
    <input type="submit" name="login" value="Log in">
    <input type="reset" name="Clear" value="Clear">
</p>
  <p align="center">&nbsp;</p></td></tr></table>
</form>
<p align="center">New User? <a href="NewUser.jsp">Register</a></p>
<p align="center">&nbsp;</p>

</body>
</html>