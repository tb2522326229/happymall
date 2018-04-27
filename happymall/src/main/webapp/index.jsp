<%@ page language="java" contentType="text/html; charset=utf-8"
    pageEncoding="utf-8"%>
<!DOCTYPE html>
<%
	pageContext.setAttribute("myurl",request.getContextPath());
 %>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<title>Insert title here</title>
</head>
<body>
	<form action="/user/login.do" method="post">
		<p><input type="text" name="username"/></p>
		<p><input type="password" name="password"></p>
		<input type="submit">
	</form>
</body>
</html>