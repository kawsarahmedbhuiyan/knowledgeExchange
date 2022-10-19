<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
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
    <title><fmt:message key="title.user"/></title>
</head>
<body>
<div class="container">
    <jsp:include page='../common/navbar.jsp'/>
    <button type="button" class="btn btn-dark" onclick="history.back()"><fmt:message key="btn.back"/></button>
    <br/><br/>
    <c:if test="${SESSION_USER == forum.manager}">
        <c:url var="pendingUserListLink" value="/user/list">
            <c:param name="forumId" value="${forum.id}"/>
            <c:param name="status" value="PENDING"/>
        </c:url>
        <c:url var="declinedUserListLink" value="/user/list">
            <c:param name="forumId" value="${forum.id}"/>
            <c:param name="status" value="DECLINED"/>
        </c:url>
        <a href="${pendingUserListLink}"><fmt:message key="title.viewPendingMembers"/></a>
        <a href="${declinedUserListLink}"><fmt:message key="title.viewDeclinedMembers"/></a>
        <br/>
    </c:if>
    <c:if test="${not empty message}">
        <div class="my-3 p-2 alert alert-success">
            <div><c:out value="${message}"/></div>
        </div>
    </c:if>
    <c:url var="forumViewLink" value="/forum/view">
        <c:param name="forumId" value="${forum.id}"/>
    </c:url>
    <c:if test="${PENDING}">
        <h2>
            <fmt:message key="title.pendingMembersOf"/>
            <a href="${forumViewLink}"><c:out value=" ${forum.name} "/></a>
        </h2>
    </c:if>
    <c:if test="${APPROVED}">
        <h2>
            <fmt:message key="title.membersOf"/>
            <a href="${forumViewLink}"><c:out value=" ${forum.name} "/></a>
        </h2>
    </c:if>
    <c:if test="${DECLINED}">
        <h2>
            <fmt:message key="title.declinedMembersOf"/>
            <a href="${forumViewLink}"><c:out value=" ${forum.name} "/></a>
        </h2>
    </c:if>
    <div class="row">
        <div class="col-12">
            <table class="table table-bordered">
                <thead>
                <tr>
                    <th colspan="2"><fmt:message key="user.username"/></th>
                </tr>
                </thead>
                <tbody>
                <c:forEach var="user" items="${users}">
                    <c:url var="userViewLink" value="/user/view">
                        <c:param name="userId" value="${user.id}"/>
                    </c:url>
                    <tr>
                        <td>
                            <c:out value="${user.username}"/>
                        </td>
                        <c:if test="${SESSION_USER == forum.manager}">
                            <td>
                                <c:url var="enrollmentApproveLink" value="/enrollment/approve">
                                    <c:param name="forumId" value="${forum.id}"/>
                                    <c:param name="userId" value="${user.id}"/>
                                </c:url>
                                <c:url var="enrollmentDeclineLink" value="/enrollment/decline">
                                    <c:param name="forumId" value="${forum.id}"/>
                                    <c:param name="userId" value="${user.id}"/>
                                </c:url>
                                <c:url var="enrollmentDeleteLink" value="/enrollment/delete">
                                    <c:param name="forumId" value="${forum.id}"/>
                                    <c:param name="userId" value="${user.id}"/>
                                </c:url>
                                <c:choose>
                                    <c:when test="${PENDING}">
                                        <form action="${enrollmentApproveLink}" method="post">
                                            <button class="btn btn-success"><fmt:message key="btn.approve"/></button>
                                        </form>
                                        <form action="${enrollmentDeclineLink}" method="post">
                                            <button class="btn btn-danger"><fmt:message key="btn.decline"/></button>
                                        </form>
                                    </c:when>
                                    <c:when test="${APPROVED}">
                                        <form action="${enrollmentDeleteLink}" method="post"
                                              onSubmit="return confirm('<fmt:message key="memberDeleteConfirmationMessage"/>');">
                                            <button class="btn btn-danger"><fmt:message key="btn.remove"/></button>
                                        </form>
                                    </c:when>
                                    <c:when test="${DECLINED}">
                                        <form action="${enrollmentApproveLink}" method="post">
                                            <button class="btn btn-success"><fmt:message key="btn.approve"/></button>
                                        </form>
                                    </c:when>
                                </c:choose>
                            </td>
                        </c:if>
                    </tr>
                </c:forEach>
                </tbody>
            </table>
        </div>
    </div>
    <br/><br/>
    <jsp:include page='../common/footer.jsp'/>
</div>
</body>
</html>