<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!--
@author kawsar.bhuiyan
@since 10/19/22
-->
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
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
    <title>Comment View</title>
</head>
<body>
<c:url var="userViewLink" value="/user/view">
    <c:param name="userId" value="${comment.user.id}"/>
</c:url>
<div class="card">
    <div class="card-header">
        <small>
            <a href="${userViewLink}"><c:out value=" ${comment.user.username} "/></a>
            <fmt:message key="label.commentedOn"/>
            <fmt:formatDate pattern="MM/dd/yyyy" value="${comment.created}"/>
            <c:out value=" | "/>
            <fmt:message key="label.modifiedOn"/>
            <fmt:formatDate pattern="MM/dd/yyyy" value="${comment.updated}"/>
        </small>
    </div>
    <div class="card-body">
        <p class="card-text"><c:out value="${comment.body}"/></p>
    </div>
    <div class="d-inline-flex">
        <c:if test="${ADMIN || SESSION_USER == comment.user}">
            <c:url var="commentUpdateLink" value="/comment/update">
                <c:param name="commentId" value="${comment.id}"/>
            </c:url>
            <a href="${commentUpdateLink}">
                <button type="button" class="btn btn-primary mx-1"><fmt:message key="btn.edit"/></button>
            </a>
        </c:if>
        <c:if test="${ADMIN || SESSION_USER == forum.manager|| SESSION_USER == post.user}">
            <c:url var="commentDeleteLink" value="/comment/delete">
                <c:param name="commentId" value="${comment.id}"/>
            </c:url>
            <form action="${commentDeleteLink}" method="post"
                  onSubmit="return confirm('<fmt:message key="commentDeleteConfirmationMessage"/>');">
                <button class="btn btn-danger d-inline mx-1"><fmt:message key="btn.delete"/></button>
            </form>
        </c:if>
    </div>
</div>
</body>
</html>