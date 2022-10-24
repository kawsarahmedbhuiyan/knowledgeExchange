<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!--
@author kawsar.bhuiyan
@since 10/20/22
-->
<html>
<head>
    <title>User Form</title>
</head>
<body>
<button type="button" class="btn btn-dark" onclick="history.back()"><fmt:message key="btn.back"/></button>
<h1><fmt:message key="title.updateUser"/></h1>

<form:form method="POST" action="/user/update" modelAttribute="user">
    <form:label path="username"><fmt:message key="label.username"/></form:label>
    <form:input class="form-control" path="username"/>
    <form:errors path="username" cssStyle="color: red"/>
    <br/><br/>
    <form:label path="name"><fmt:message key="label.name"/></form:label>
    <form:input class="form-control" path="name"/>
    <form:errors path="name" cssStyle="color: red"/>
    <br/><br/>
    <c:if test="${ADMIN}">
        <form:label path="roles"><fmt:message key="label.roles"/></form:label>
        <form:checkboxes path="roles" itemValue="id" items="${roleList}" delimiter=" "/>
        <form:errors path="roles" cssStyle="color: red"/>
        <br/><br/>
    </c:if>
    <button class="btn btn-success"><fmt:message key="btn.update"/></button>
</form:form>
</body>
</html>