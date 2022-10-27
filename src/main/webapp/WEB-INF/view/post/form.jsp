<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!--
@author kawsar.bhuiyan
@since 10/18/22
-->
<html>
<head>
    <title>Post Form</title>
</head>
<body>
<button type="button" class="btn btn-dark" onclick="history.back()"><fmt:message key="btn.back"/></button>
<br/><br/>
<h1><fmt:message key="title.${action}Post"/></h1>
<br/>
<form:form method="POST" action="/post/index" modelAttribute="post">
    <form:label path="title"><fmt:message key="label.title"/></form:label>
    <form:input class="form-control" path="title"/><br/>
    <form:errors path="title" cssStyle="color: red"/>
    <br/>
    <form:label path="body"><fmt:message key="label.body"/></form:label>
    <form:textarea class="form-control" rows="10" path="body"/><br/>
    <form:errors path="body" cssStyle="color: red"/>
    <br/>
    <button name="_action_${action}" class="btn btn-success"><fmt:message key="btn.${action}"/></button>
</form:form>
</body>
</html>