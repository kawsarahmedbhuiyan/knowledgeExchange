<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!--
@author kawsar.bhuiyan
@since 10/14/22
-->
<html>
<head>
    <title>Registration</title>
</head>
<body>
<c:if test="${not empty SESSION_USER}">
    <c:redirect url="/"/>
</c:if>

<button type="button" class="btn btn-dark" onclick="history.back()"><fmt:message key="btn.back"/></button>
<h1><fmt:message key="title.register"/></h1>

<form:form method="POST" action="/auth/register" modelAttribute="user">
    <form:label path="username"><fmt:message key="label.username"/></form:label>
    <form:input class="form-control" path="username"/>
    <form:errors path="username" cssStyle="color: red"/>
    <br/><br/>
    <form:label path="password"><fmt:message key="label.password"/></form:label>
    <form:input type="password" class="form-control" path="password"/>
    <form:errors path="password" cssStyle="color: red"/>
    <br/><br/>
    <form:label path="name"><fmt:message key="label.name"/></form:label>
    <form:input class="form-control" path="name"/>
    <form:errors path="name" cssStyle="color: red"/>
    <br/><br/>
    <button class="btn btn-success"><fmt:message key="btn.register"/></button>
</form:form>
</body>
</html>