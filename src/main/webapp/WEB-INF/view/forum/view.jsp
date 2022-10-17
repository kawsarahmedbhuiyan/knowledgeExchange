<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!--
@author kawsar.bhuiyan
@since 10/17/22
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
    <div class="d-flex flex-column">
        <div class="d-flex justify-content-center">
            <h1><c:out value="${forum.name}"/></h1>
        </div>
        <c:url var="forumJoinLink" value="/forum/join">
            <c:param name="forumId" value="${forum.id}"/>
        </c:url>
        <c:url var="forumLeaveLink" value="/forum/leave">
            <c:param name="forumId" value="${forum.id}"/>
        </c:url>
        <c:choose>
            <c:when test="${JOINED}">
                <form action="${forumLeaveLink}" method="post">
                    <button class="btn btn-danger"><fmt:message key="btn.leave"/></button>
                </form>
            </c:when>
            <c:otherwise>
                <form action="${forumJoinLink}" method="post">
                    <button class="btn btn-success"><fmt:message key="btn.join"/></button>
                </form>
            </c:otherwise>
        </c:choose>
    </div>
    <br/>
    <jsp:include page='../common/footer.jsp'/>
</div>
</body>
</html>