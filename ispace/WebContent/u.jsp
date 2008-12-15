<%@ page contentType="text/html; charset=iso-8859-1" language="java" import="java.sql.*" errorPage="" %>
<%@ page import = "java.net.*" %>
<%@ page import = "java.io.*" %>
<%@ page import = "server.*" %>
<%@ page import="java.util.*" %>
<% 	int fileId;
	if(request.getContentType()!=null){
	fileId=Integer.parseInt(request.getParameter("fileId"));
	String contentType = request.getContentType();
	DataInputStream in= new DataInputStream(request.getInputStream());
	int formDataLength = request.getContentLength();
	//Socket sock = new Socket("localhost",4445);
	byte [] dataBytes  = new byte [formDataLength];
	int byteRead = 0;
	int totalBytesRead = 0;
	    
	while (totalBytesRead < formDataLength){
	byteRead = in.read(dataBytes, totalBytesRead, formDataLength);
	totalBytesRead += byteRead;
	}
	
	String file = new String(dataBytes);
	
	//for saving the file name
	
	String saveFile = file.substring(file.indexOf("filename=\"") + 10);
	saveFile = saveFile.substring(0, saveFile.indexOf("\n"));
	saveFile = saveFile.substring(saveFile.lastIndexOf("\\") + 1,saveFile.indexOf("\""));
	//Statement AddFile= ConnGet_User_Files.createStatement();
	//AddFile.executeUpdate("INSERT INTO files VALUES ('"+0+"','"+saveFile+"','"+formDataLength+"','"+(String)session.getValue("MM_Username")+"','"+0+"') ");
	int lastIndex = contentType.lastIndexOf("=");
	String boundary = contentType.substring(lastIndex + 1,contentType.length());
	int pos;
	
	//extracting the index of file
	
	pos = file.indexOf("filename=\"");
	pos = file.indexOf("\n", pos) + 1;
	pos = file.indexOf("\n", pos) + 1;
	pos = file.indexOf("\n", pos) + 1;
	int boundaryLocation = file.indexOf(boundary, pos) - 4;
	int startPos = ((file.substring(0, pos)).getBytes()).length;
	int endPos = ((file.substring(0, boundaryLocation)).getBytes()).length;
	int datasize= (endPos - startPos);
	
	byte[] dataToSend=new byte[datasize];
	System.arraycopy(dataBytes, startPos, dataToSend, 0, datasize);
	//OutputStream os = sock.getOutputStream();
	System.out.println("\n\n\ntest print\n\n\n");
	//MyFile mf=new MyFile(7676);
	MyFile myFile=new MyFile(fileId, 0, saveFile, dataToSend);
	ISpace.uploadMyFile(myFile);

	
	}
%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">


<%@page import="org.apache.jasper.tagplugins.jstl.core.ForEach"%><html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1">
<title>Upload</title>
<style type="text/css">
<!--
body,td,th {
	font-family: Verdana, Arial, Helvetica, sans-serif;
	color: #000000;
}
body {
	background-color: #CCCCCC;
}
.style3 {
	font-size: 16px;
	font-style: italic;
}
.style11 {font-size: 24px; font-style: italic; }
-->
</style></head>

<body>
<p align="right" class="style3"><a href="Help.jsp">Help</a> Log out</p>
<p align="center" class="style3">&nbsp;</p>
<p align="center" class="style11">Welcome </p>
<p align="center" class="style3">&nbsp;</p>
  
<div align="center" class="style3">
  <table width="99%"  border="1">
    <tr>
      <td width="33%"><div align="center"><a href="Upload.jsp">Upload</a></div></td>
      <td width="33%"><div align="center"><a href="Download.jsp">Download</a></div></td>
      <td width="33%"><div align="center"><a href="Delete.jsp">Delete</a></div></td>
    </tr>
  </table>
</div>
<form action="u.jsp" method="post" enctype="multipart/form-data" name="form1">
  <p align="center">&nbsp;</p>
  <p align="center"><em>File Id:</em>
  <input name="fileId" id="fileId" type="text" size="40"><br/>  
  </p>
  <p align="center"><em>File:</em>  
    <input name="file" type="file" size="40">
  </p>
  <p align="center">
    <input type="submit" name="Upload" value="Upload">
    <input type="reset" name="reset" value="Clear">
  </p>
</form>
<p align="center" class="style3">&nbsp;</p>
  

<div align="center" class="style3">
  <table width="50%" border="1" align="center">
    <tr>
	  <td width="4%"><div align="center"><strong>&nbsp;</strong></div></td>
      <td width="26%"><div align="center"><strong>File Name</strong></div></td>
      <td width="20%"><div align="center"><strong>Size</strong></div></td>
    </tr>
    
    <tr>
      <td width="4%"><div align="center"></div></td>
      <td width="26%"><div align="center"></div></td>
      <td width="20%"><div align="center"></div></td>
    </tr>
  </table>
</div>
</body>
</html>
