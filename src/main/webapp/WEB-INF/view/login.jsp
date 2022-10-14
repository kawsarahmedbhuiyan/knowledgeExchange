<!--
@author kawsar.bhuiyan
@since 10/14/22
-->
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
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
    <title><fmt:message key="title.login"/></title>
</head>
<body>
<c:if test="${not empty SESSION_USER}">
    <c:redirect url="/"/>
</c:if>
<div class="container">
    <jsp:include page='common/navbar.jsp'/>
    <div class="card">
        <div class="card-body">
            <h1><fmt:message key="title.login"/></h1>
            <form:form method="POST" action="/auth/login" modelAttribute="credential">
                <form:errors path="" cssStyle="color: red"/><br/>
                <form:label path="username"><fmt:message key="label.username"/> </form:label>
                <form:input path="username"/>
                <form:errors path="username" cssStyle="color: red"/>
                <br/><br/>
                <form:label path="password"><fmt:message key="label.password"/> </form:label>
                <form:input type="password" path="password"/>
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
    <br/>
    <jsp:include page='common/footer.jsp'/>
</div>
</body>
</html>