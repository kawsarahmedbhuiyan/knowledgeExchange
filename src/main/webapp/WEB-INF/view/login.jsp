<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<!--
@author kawsar.bhuiyan
@since 10/14/22
-->
<html>
<head>
    <title>Login</title>
</head>
<body>
<c:if test="${not empty SESSION_USER}">
    <c:redirect url="/"/>
</c:if>

<div class="card">
    <div class="card-body">
        <h1><fmt:message key="title.login"/></h1>
        <form:form class="form-group" method="POST" action="/auth/login" modelAttribute="credential">
            <form:errors path="" cssStyle="color: red"/><br/>
            <form:label path="username"><fmt:message key="label.username"/></form:label>
            <form:input class="form-control" path="username"/>
            <form:errors path="username" cssStyle="color: red"/>
            <br/><br/>
            <form:label path="password"><fmt:message key="label.password"/></form:label>
            <form:input type="password" class="form-control" path="password"/>
            <form:errors path="password" cssStyle="color: red"/>
            <br/><br/>
            <button class="btn btn-primary" value="login"><fmt:message key="btn.login"/></button>
        </form:form>
        <h2><fmt:message key="title.dontHaveAnAccount"/></h2>
        <br/>
        <c:url var="register" value="/auth/register"/>
        <a href="${register}">
            <button type="button" class="btn btn-primary"><fmt:message key="btn.register"/></button>
        </a>
    </div>
</div>
</body>
</html>