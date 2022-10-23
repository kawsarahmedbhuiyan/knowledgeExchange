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
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1, shrink-to-fit=no">
    <meta name="description" content="">
    <meta name="author" content="">
    <meta name="keywords" content="">
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.2.0/dist/css/bootstrap.min.css" rel="stylesheet"
          integrity="sha384-gH2yIJqKdNHPEq0n4Mqa/HGKIhSkIHeL5AyhkYV8i59U5AR6csBvApHHNl/vI1Bx" crossorigin="anonymous">
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.2.0/dist/js/bootstrap.bundle.min.js"
            integrity="sha384-A3rJD856KowSb7dwlZdYEkO39Gagi7vIsF0jrRAoQmDKKtQBHUuLZ9AsSv4jD4Xa"
            crossorigin="anonymous"></script>
    <title>User Form</title>
</head>
<body>
<div class="container">
    <jsp:include page='../common/navbar.jsp'/>
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
    <jsp:include page='../common/footer.jsp'/>
</div>
</body>
</html>