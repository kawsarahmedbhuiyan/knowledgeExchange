<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!--
@author kawsar.bhuiyan
@since 10/16/22
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
    <h1><fmt:message key="title.${list}"/></h1>
    <c:if test="${not empty message}">
        <div class="my-3 p-2 alert alert-success">
            <div><c:out value="${message}"/></div>
        </div>
    </c:if>
    <c:url var="pendingForumListLink" value="/forum/${list}">
        <c:param name="status" value="PENDING"/>
    </c:url>
    <c:url var="approvedForumListLink" value="/forum/${list}">
        <c:param name="status" value="APPROVED"/>
    </c:url>
    <c:url var="declinedForumListLink" value="/forum/${list}">
        <c:param name="status" value="DECLINED"/>
    </c:url>
    <a href="${pendingForumListLink}"><fmt:message key="title.pending"/></a>
    <a href="${approvedForumListLink}"><fmt:message key="title.approved"/></a>
    <a href="${declinedForumListLink}"><fmt:message key="title.declined"/></a>
    <br/><br/>
    <c:if test="${PENDING}">
        <h2><fmt:message key="title.pending"/></h2>
    </c:if>
    <c:if test="${APPROVED}">
        <h2><fmt:message key="title.approved"/></h2>
    </c:if>
    <c:if test="${DECLINED}">
        <h2><fmt:message key="title.declined"/></h2>
    </c:if>
    <div class="row">
        <div class="col-12">
            <table class="table table-bordered">
                <thead>
                <tr>
                    <th colspan="2"><fmt:message key="forum.name"/></th>
                </tr>
                </thead>
                <tbody>
                <c:forEach var="forum" items="${forums}">
                    <c:url var="forumViewLink" value="/forum/view">
                        <c:param name="forumId" value="${forum.id}"/>
                    </c:url>
                    <tr>
                        <td>
                            <c:choose>
                                <c:when test="${APPROVED}">
                                    <a href="${forumViewLink}"><c:out value="${forum.name}"/></a>
                                </c:when>
                                <c:otherwise>
                                    <c:out value="${forum.name}"/>
                                </c:otherwise>
                            </c:choose>
                        </td>
                        <c:if test="${ADMIN}">
                            <td>
                                <c:url var="forumApproveLink" value="/forum/approve">
                                    <c:param name="forumId" value="${forum.id}"/>
                                </c:url>
                                <c:url var="forumDeclineLink" value="/forum/decline">
                                    <c:param name="forumId" value="${forum.id}"/>
                                </c:url>
                                <c:url var="forumDeleteLink" value="/forum/delete">
                                    <c:param name="forumId" value="${forum.id}"/>
                                </c:url>

                                <c:choose>
                                    <c:when test="${PENDING}">
                                        <form action="${forumApproveLink}" method="post">
                                            <button class="btn btn-success"><fmt:message key="btn.approve"/></button>
                                        </form>
                                        <form action="${forumDeclineLink}" method="post">
                                            <button class="btn btn-danger"><fmt:message key="btn.decline"/></button>
                                        </form>
                                    </c:when>
                                    <c:when test="${APPROVED}">
                                        <form action="${forumDeleteLink}" method="post"
                                              onSubmit="return confirm('<fmt:message key="forumDeleteConfirmationMessage"/>');">
                                            <button class="btn btn-danger"><fmt:message key="btn.delete"/></button>
                                        </form>
                                    </c:when>
                                    <c:when test="${DECLINED}">
                                        <form action="${forumApproveLink}" method="post">
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
    <a href="/forum/save">
        <button class="btn btn-primary"><fmt:message key="btn.createNewForum"/></button>
    </a>
    <br/><br/>
    <jsp:include page='../common/footer.jsp'/>
</div>
</body>
</html>