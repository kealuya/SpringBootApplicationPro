<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="shiro" uri="http://shiro.apache.org/tags"%>

<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort()
			+ path + "/";
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<base href="<%=basePath%>" />
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Insert title here</title>
</head>
<body>
	Hello jsp 3333333333 ${msg}
	<br>${my2}
	<br>
    <shiro:hasPermission name="power1">
        <span>只有power1用户可以看到我</span>
    </shiro:hasPermission>
	<br>
    <shiro:hasPermission name="power2">
        <span>只有power2用户可以看到我</span>
    </shiro:hasPermission>
    <br>
	<script type="text/javascript" src="static/js/common.js"></script>
</body>
</html>