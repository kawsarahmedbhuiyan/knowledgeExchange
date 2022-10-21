<!--
@author kawsar.bhuiyan
@since 10/20/22
-->
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<html>
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1, shrink-to-fit=no">
    <meta name="description" content="">
    <meta name="author" content="">
    <meta name="keywords" content="">
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.2.0/dist/css/bootstrap.min.css" rel="stylesheet"
          integrity="sha384-gH2yIJqKdNHPEq0n4Mqa/HGKIhSkIHeL5AyhkYV8i59U5AR6csBvApHHNl/vI1Bx" crossorigin="anonymous">
    <link rel="stylesheet"
          href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.1.2/css/all.min.css"
          integrity="sha512-1sCRPdkRXhBV2PBLUdRb4tMg1w2YPf37qatUFeS7zlBy7jJI8Lf4VHwWfZZfpXtYSLy85pkm9GaYVYMfw5BC1A=="
          crossorigin="anonymous"
          referrerpolicy="no-referrer"
    />
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.2.0/dist/js/bootstrap.bundle.min.js"
            integrity="sha384-A3rJD856KowSb7dwlZdYEkO39Gagi7vIsF0jrRAoQmDKKtQBHUuLZ9AsSv4jD4Xa"
            crossorigin="anonymous"></script>
    <title></title>
</head>
<body>
<div class="container">
    <jsp:include page='../common/navbar.jsp'/>
    <button type="button" class="btn btn-dark" onclick="history.back()"><fmt:message key="btn.back"/></button>
    <br/><br/>
    <c:if test="${not empty message}">
        <div class="my-3 p-2 alert alert-success">
            <div><c:out value="${message}"/></div>
        </div>
    </c:if>
    <h1><fmt:message key="title.userDetails"/></h1>
    <b><fmt:message key="label.username"/></b><c:out value=" ${user.username}"/><br/><br/>
    <b><fmt:message key="label.name"/></b><c:out value=" ${user.name}"/><br/><br/>
    <h2>
        <fmt:message key="title.listOfForums"/><c:out value=" ${user.username} "/><fmt:message key="title.manages"/>
    </h2>
    <c:set var="listType" scope="request" value="managedForumsList"/>
    <c:set var="forums" scope="request" value="${managedForums}"/>
    <jsp:include page="../forum/list.jsp"/>
    <br/>
    <h2>
        <fmt:message key="title.listOfForums"/><c:out value=" ${user.username} "/><fmt:message key="title.hasJoined"/>
    </h2>
    <c:set var="listType" scope="request" value="joinedForumsList"/>
    <c:set var="forums" scope="request" value="${joinedForums}"/>
    <jsp:include page="../forum/list.jsp"/>
    <br/>
    <c:if test="${ADMIN || user == SESSION_USER}">
        <c:url var="pendingForumCreationRequestListLink" value="/forum/creationRequestList">
            <c:param name="status" value="PENDING"/>
        </c:url>
        <c:url var="pendingForumJoinRequestListLink" value="/forum/joinRequestList">
            <c:param name="status" value="PENDING"/>
        </c:url>
        <c:url var="userEditLink" value="/user/update">
            <c:param name="userId" value="${user.id}"/>
        </c:url>
        <a href="${pendingForumCreationRequestListLink}">
            <fmt:message key="title.viewPendingForumCreationRequestList"/>
        </a>
        <br/>
        <a href="${pendingForumJoinRequestListLink}">
            <fmt:message key="title.viewPendingForumJoinRequestList"/>
        </a>
        <br/><br/>
        <a href="${userEditLink}">
            <button type="button" class="btn btn-primary"><fmt:message key="btn.editUser"/></button>
        </a>
    </c:if>
    <br/><br/>
    <jsp:include page='../common/footer.jsp'/>
</div>
</body>
</html>