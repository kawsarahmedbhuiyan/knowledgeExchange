<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!--
@author kawsar.bhuiyan
@since 10/13/22
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
    <title><fmt:message key="title.home"/></title>
</head>
<body>
<div class="container">
    <jsp:include page='common/navbar.jsp'/>
    <h1><fmt:message key="title.welcome"/></h1>
    <br/>
    <c:set var="forums" scope="request" value="${forums}"/>
    <jsp:include page="./forum/list.jsp"/>
    <br/>
    <c:if test="${ADMIN}">
    <c:url var="pendingForumCreationRequestListLink" value="/forum/creationRequestList">
        <c:param name="status" value="PENDING"/>
    </c:url>
    <a href="${pendingForumCreationRequestListLink}">
        <fmt:message key="title.viewPendingForumCreationRequestList"/>
    </a>
    <br/><br/>
    </c:if>
    <a href="/forum/save">
        <button class="btn btn-primary"><fmt:message key="btn.createNewForum"/></button>
    </a>
    <br/><br/>
    <jsp:include page='common/footer.jsp'/>
</body>
</html>