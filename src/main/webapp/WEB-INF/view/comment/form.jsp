<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!--
@author kawsar.bhuiyan
@since 10/19/22
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
    <title>Comment Form</title>
</head>
<body>
<div class="container">
    <c:if test="${action == 'update'}">
        <jsp:include page='../common/navbar.jsp'/>
        <button type="button" class="btn btn-dark" onclick="history.back()"><fmt:message key="btn.back"/></button>
        <br/><br/>
        <h1><fmt:message key="title.editComment"/></h1>
        <br/>
    </c:if>
    <fmt:message key="label.writeComment" var="writeComment"/>
    <form:form method="POST" action="/comment/${action}" modelAttribute="comment">
        <form:textarea class="form-control" placeholder="${writeComment}" rows="5" path="body"/><br/>
        <form:errors path="body" cssStyle="color: red"/>
        <br/>
        <button class="btn btn-success"><fmt:message key="btn.${action}"/></button>
    </form:form>
    <c:if test="${action == 'update'}">
        <br/>
        <jsp:include page='../common/footer.jsp'/>
    </c:if>
</div>
</body>
</html>