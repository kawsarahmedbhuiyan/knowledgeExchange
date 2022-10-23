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
    <title>Post Form</title>
</head>
<body>
<div class="container">
    <jsp:include page='../common/navbar.jsp'/>
    <button type="button" class="btn btn-dark" onclick="history.back()"><fmt:message key="btn.back"/></button>
    <br/><br/>
    <h1><fmt:message key="title.${action}Post"/></h1>
    <br/>
    <form:form method="POST" action="/post/${action}" modelAttribute="post">
        <form:label path="title"><fmt:message key="label.title"/></form:label>
        <form:input class="form-control" path="title"/><br/>
        <form:errors path="title" cssStyle="color: red"/>
        <br/>
        <form:label path="body"><fmt:message key="label.body"/></form:label>
        <form:textarea class="form-control" rows="10" path="body"/><br/>
        <form:errors path="body" cssStyle="color: red"/>
        <br/>
        <button class="btn btn-success"><fmt:message key="btn.${action}"/></button>
    </form:form>
    <br/>
    <jsp:include page='../common/footer.jsp'/>
</div>
</body>
</html>