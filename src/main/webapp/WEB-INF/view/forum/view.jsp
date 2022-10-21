<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
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
    <link href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.1.2/css/all.min.css" rel="stylesheet"
          integrity="sha512-1sCRPdkRXhBV2PBLUdRb4tMg1w2YPf37qatUFeS7zlBy7jJI8Lf4VHwWfZZfpXtYSLy85pkm9GaYVYMfw5BC1A=="
          crossorigin="anonymous"
          referrerpolicy="no-referrer"
    />
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
            <h1><c:out value="${forum.name}"/></h1><br/>
        </div>
        <c:url var="approvedUserListLink" value="/user/list">
            <c:param name="forumId" value="${forum.id}"/>
            <c:param name="status" value="APPROVED"/>
        </c:url>
        <div class="d-flex justify-content-center">
            <small><a href="${approvedUserListLink}"><fmt:message key="title.viewMembers"/></a></small>
            <br/><br/>
        </div>
        <c:url var="enrollLink" value="/enrollment/enroll">
            <c:param name="forumId" value="${forum.id}"/>
        </c:url>
        <c:url var="deleteEnrollmentLink" value="/enrollment/delete">
            <c:param name="forumId" value="${forum.id}"/>
        </c:url>
        <div class="d-flex justify-content-center">
            <c:if test="${SESSION_USER != forum.manager}">
                <c:choose>
                    <c:when test="${PENDING}">
                        <form action="${deleteEnrollmentLink}" method="post">
                            <button class="btn btn-danger"><fmt:message key="btn.cancelJoinRequest"/></button>
                        </form>
                    </c:when>
                    <c:when test="${APPROVED}">
                        <form action="${deleteEnrollmentLink}" method="post">
                            <button class="btn btn-danger"><fmt:message key="btn.leave"/></button>
                        </form>
                    </c:when>
                    <c:otherwise>
                        <form action="${enrollLink}" method="post">
                            <button class="btn btn-success"><fmt:message key="btn.join"/></button>
                        </form>
                    </c:otherwise>
                </c:choose>
            </c:if>
        </div>
    </div>
    <c:if test="${not empty message}">
        <div class="my-3 p-2 alert alert-success">
            <div><c:out value="${message}"/></div>
        </div>
    </c:if>
    <c:url var="pendingPostListLink" value="/post/list">
        <c:param name="forumId" value="${forum.id}"/>
        <c:param name="status" value="PENDING"/>
    </c:url>
    <c:url var="declinedPostListLink" value="/post/list">
        <c:param name="forumId" value="${forum.id}"/>
        <c:param name="status" value="DECLINED"/>
    </c:url>
    <a href="${pendingPostListLink}"><fmt:message key="label.viewPendingPostList"/></a>
    <a href="${declinedPostListLink}"><fmt:message key="label.viewDeclinedPostList"/></a>
    <c:if test="${SESSION_USER != forum.manager}">
        <c:url var="approvedPostListLink" value="/post/list">
            <c:param name="forumId" value="${forum.id}"/>
            <c:param name="status" value="APPROVED"/>
        </c:url>
        <a href="${approvedPostListLink}"><fmt:message key="label.viewApprovedPostList"/></a>
    </c:if>
    <br/><br/>
    <c:url var="postSaveLink" value="/post/save">
        <c:param name="forumId" value="${forum.id}"/>
    </c:url>
    <a href="${postSaveLink}">
        <button class="btn btn-primary"><fmt:message key="btn.addNewPost"/></button>
    </a>
    <br/><br/>
    <c:set var="posts" scope="request" value="${approvedPosts}"/>
    <jsp:include page="../post/list.jsp"/>
    <br/><br/>
    <jsp:include page='../common/footer.jsp'/>
</div>
</body>
</html>