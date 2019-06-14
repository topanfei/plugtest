<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%
String path = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+request.getContextPath()+"/";
%>    
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>下载页面</title>
<base href="<%=path%>">
</head>
<body>
	<a href="test/downFile?fileRealPath=${filePath }">下载文件</a>
</body>
</html>