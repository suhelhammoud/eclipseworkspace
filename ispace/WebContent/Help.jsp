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
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1">
<title>Help</title>
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
.style1 {font-size: 18px}
-->
</style></head>

<body>
<p align="right"><a href="Upload.jsp">Home Page</a> <a href="<%= MM_Logout %>">Log out</a></p>
<p align="left">&nbsp;</p>
<p align="left" class="style1">Uploading a file:</p>
<p align="left">In the <a href="Upload.jsp">Upload</a> page, click Browse... to choose the file you want to upload from your computer hard disk, then click the Upload button to upload the file and you'll see it in the table of files on your page, otherwise if you want to cancel the operation click Clear button to clear the file field.</p>
<p align="left">&nbsp;</p>
<p align="left" class="style1">Downloading a file:</p>
<p align="left">To download a file previously uploaded, go to the <a href="Download.jsp">Download</a> page and choose the file(s) you want to download by ticking the check boxes associated then click the Download File(s) button, and choose where to save them on you computer hard disk.</p>
<p align="left">&nbsp;</p>
<p align="left" class="style1">Delete a file:</p>
<p align="left">To delete a file or a list of files, go to the <a href="Delete.jsp">Delete</a> Page and choose the file(s) you want to delete by ticking the check boxes associated then click the Delete File(s) button and you can see that the file(s) are cleared from the list.</p>
<p align="left">&nbsp;</p>
<p align="left" class="style1">Log out:</p>
<p align="left">You can log out from the system any time by clicking the Log out hyperlink on any of the pages.</p>
<p align="left">&nbsp;</p>
<p align="left" class="style1">Contact us:</p>
<p align="left">For any more information don't hesitate to contact us:</p>
<p align="left">Email:  </p>
</body>
</html>
