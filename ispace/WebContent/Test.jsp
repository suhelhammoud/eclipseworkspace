<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Insert title here</title>
</head>
<body>
<%@ page import="server.Suhel" %>
<%@ page import="server.MyFile" %>
<%
	MyFile mf=new MyFile(999);
%>
<%= "mf"+mf.toString() %>
<p>e
<%=new Suhel().say()+"  1"%>
</body>
</html>