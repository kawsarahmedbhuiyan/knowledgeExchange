<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!--
@author kawsar.bhuiyan
@since 10/15/22
-->
<html>
<head>
    <title>Forum Form</title>
</head>
<body>
<button type="button" class="btn btn-dark" onclick="history.back()"><fmt:message key="btn.back"/></button>
<br/><br/>
<h1><fmt:message key="title.${action}Forum"/></h1>
<br/>
<form:form method="POST" action="/forum/index" modelAttribute="forum">
    <form:label path="name"><fmt:message key="label.name"/></form:label>
    <form:input class="form-control" path="name"/><br/>
    <form:errors path="name" cssStyle="color: red"/>
    <br/><br/>
    <form:label path="description"><fmt:message key="label.description"/></form:label>
    <form:textarea class="form-control" rows="8" path="description"/><br/>
    <form:errors path="description" cssStyle="color: red"/>
    <br/><br/>
    <button name="_action_${action}" class="btn btn-success"><fmt:message key="btn.${action}"/></button>
</form:form>
</body>
</html>