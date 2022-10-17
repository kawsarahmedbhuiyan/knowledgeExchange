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
    <title><fmt:message key="title.forum"/></title>
</head>
<body>
<div class="container">
    <jsp:include page='../common/navbar.jsp'/>
    <button type="button" class="btn btn-dark" onclick="history.back()"><fmt:message key="btn.back"/></button>
    <br/><br/>
    <c:if test="${SAVE}">
        <h1><fmt:message key="title.addForum"/></h1>
    </c:if>
    <c:if test="${UPDATE}">
        <h1><fmt:message key="title.editForum"/></h1>
    </c:if>
    <br/>
    <form:form method="POST" action="/forum/save" modelAttribute="forum">
        <form:label path="name"><fmt:message key="label.name"/> </form:label>
        <form:input path="name"/><br/>
        <form:errors path="name" cssStyle="color: red"/>
        <br/><br/>
        <form:label path="description"><fmt:message key="label.description"/> </form:label><br/><br/>
        <form:textarea class="form-control" rows="8" path="description"/><br/>
        <form:errors path="description" cssStyle="color: red"/>
        <br/><br/>
        <c:if test="${SAVE}">
            <button class="btn btn-success"><fmt:message key="btn.save"/></button>
        </c:if>
        <c:if test="${UPDATE}">
            <button class="btn btn-success"><fmt:message key="btn.update"/></button>
        </c:if>
    </form:form>
    <br/>
    <jsp:include page='../common/footer.jsp'/>
</div>
</body>
</html>
