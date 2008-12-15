<%@ page contentType="text/html; charset=iso-8859-1" language="java" import="java.sql.*" errorPage="" %>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1">
<title>Upload File</title>
</head>

<body>
<div align="center">
<form action="UploadServlet" method="post" enctype="multipart/form-data">

<p>fileId:<input type="text" name="fileId"></p>
<p>file:<input type="file" name="file1"></p>
<input type="submit" value="Upload File">
</form>
</div>
</body>
</html>
