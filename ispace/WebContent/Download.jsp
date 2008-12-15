<%@ page contentType="text/html; charset=iso-8859-1" language="java" import="java.sql.*" errorPage="" %>
<%
// *** Logout the current user.
String MM_Logout = request.getRequestURI() + "?MM_Logoutnow=1";
if (request.getParameter("MM_Logoutnow") != null && request.getParameter("MM_Logoutnow").equals("1")) {
  session.putValue("MM_Username", "");
  session.putValue("MM_UserAuthorization", "");
  String MM_logoutRedirectPage = "Login.jsp";
  // redirect with URL parameters (remove the "MM_Logoutnow" query param).
  if (MM_logoutRedirectPage.equals("")) MM_logoutRedirectPage = request.getRequestURI();
  if (MM_logoutRedirectPage.indexOf("?") == -1 && request.getQueryString() != null) {
    String MM_newQS = request.getQueryString();
    String URsearch = "MM_Logoutnow=1";
    int URStart = MM_newQS.indexOf(URsearch);
    if (URStart >= 0) {
      MM_newQS = MM_newQS.substring(0,URStart) + MM_newQS.substring(URStart + URsearch.length());
    }
    if (MM_newQS.length() > 0) MM_logoutRedirectPage += "?" + MM_newQS;
  }
  response.sendRedirect(response.encodeRedirectURL(MM_logoutRedirectPage));
  return;
}
%>
<%
// *** Restrict Access To Page: Grant or deny access to this page
String MM_authorizedUsers="";
String MM_authFailedURL="Login.jsp";
boolean MM_grantAccess=false;
if (session.getValue("MM_Username") != null && !session.getValue("MM_Username").equals("")) {
  if (true || (session.getValue("MM_UserAuthorization")=="") || 
          (MM_authorizedUsers.indexOf((String)session.getValue("MM_UserAuthorization")) >=0)) {
    MM_grantAccess = true;
  }
}
if (!MM_grantAccess) {
  String MM_qsChar = "?";
  if (MM_authFailedURL.indexOf("?") >= 0) MM_qsChar = "&";
  String MM_referrer = request.getRequestURI();
  if (request.getQueryString() != null) MM_referrer = MM_referrer + "?" + request.getQueryString();
  MM_authFailedURL = MM_authFailedURL + MM_qsChar + "accessdenied=" + java.net.URLEncoder.encode(MM_referrer);
  response.sendRedirect(response.encodeRedirectURL(MM_authFailedURL));
  return;
}
%>
<%@ include file="Connections/conn.jsp" %>
<%
String Get_User_Files__MMColParam = "1";
if (session.getValue("MM_Username") !=null) {Get_User_Files__MMColParam = (String)session.getValue("MM_Username");}
%>
<%
Driver DriverGet_User_Files = (Driver)Class.forName(MM_conn_DRIVER).newInstance();
Connection ConnGet_User_Files = DriverManager.getConnection(MM_conn_STRING,MM_conn_USERNAME,MM_conn_PASSWORD);
PreparedStatement StatementGet_User_Files = ConnGet_User_Files.prepareStatement("SELECT fileid, filename, filesize  FROM mydatabase.files  WHERE username = '" + Get_User_Files__MMColParam + "' AND flag=0");
ResultSet Get_User_Files = StatementGet_User_Files.executeQuery();
boolean Get_User_Files_isEmpty = !Get_User_Files.next();
boolean Get_User_Files_hasData = !Get_User_Files_isEmpty;
Object Get_User_Files_data;
int Get_User_Files_numRows = 0;
%>
<%
int Repeat1__numRows = -1;
int Repeat1__index = 0;
Get_User_Files_numRows += Repeat1__numRows;
%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1">
<title>Download</title>
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
.style1 {font-size: 24px}
-->
</style></head>

<body>
<p align="right"><a href="Help.jsp">Help</a> <a href="<%= MM_Logout %>">Log out</a>
  </p>
<p align="center">&nbsp;</p>
<p align="center" class="style1">Welcome <%= (String)session.getValue("MM_Username") %></p>
<p align="center">&nbsp;</p>
  
<div align="center">
  <table width="99%"  border="1">
    <tr>
      <td width="33%"><div align="center"><a href="Upload.jsp">Upload</a></div></td>
      <td width="33%"><div align="center"><a href="Download.jsp">Download</a></div></td>
      <td width="33%"><div align="center"><a href="Delete.jsp">Delete</a></div></td>
    </tr>
                  </table>
</div>
<p align="center">&nbsp;</p>
<form name="form1" method="post" action="">
  <p align="center">
    <input type="submit" name="download" value="Download">
  </p>
  
  <div align="center">
    <table width="50%" border="1" align="center">
      <tr>
        <td width="4%"><div align="center"><strong>&nbsp;</strong></div></td>
        <td width="26%"><div align="center"><strong>File Name</strong></div></td>
        <td width="20%"><div align="center"><strong>Size</strong></div></td>
      </tr>
      <% while ((Get_User_Files_hasData)&&(Repeat1__numRows-- != 0)) { %>
      <tr>
        <td width="4%"><div align="center"><input name="cb" type="checkbox" id="cb" value="<%=(((Get_User_Files_data = Get_User_Files.getObject("fileid"))==null || Get_User_Files.wasNull())?"":Get_User_Files_data)%>"></div></td>
        <td width="26%"><div align="center"><%=(((Get_User_Files_data = Get_User_Files.getObject("filename"))==null || Get_User_Files.wasNull())?"":Get_User_Files_data)%></div></td>
        <td width="20%"><div align="center"><%=(((Get_User_Files_data = Get_User_Files.getObject("filesize"))==null || Get_User_Files.wasNull())?"":Get_User_Files_data)%></div></td>
      </tr>
      <%
  Repeat1__index++;
  Get_User_Files_hasData = Get_User_Files.next();
}
%>
                      </table>  
  </div>
  <p align="center">&nbsp;</p>
</form>
<p align="center">&nbsp;</p>
</body>
</html>
<%
Get_User_Files.close();
StatementGet_User_Files.close();
ConnGet_User_Files.close();
%>
