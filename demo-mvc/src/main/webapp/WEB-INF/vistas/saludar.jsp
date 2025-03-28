<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<!DOCTYPE html>
<html>
<head>
<meta charset="ISO-8859-1">
<title>Curso</title>
</head>
<body>
	<h1>Hola ${nombre}</h1>
	<c:if test="${language == 'en' }">
	<h1>Hello ${nombre}</h1>
	</c:if>
</body>
</html>