<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
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
    <link href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.1.2/css/all.min.css" rel="stylesheet"
          integrity="sha512-1sCRPdkRXhBV2PBLUdRb4tMg1w2YPf37qatUFeS7zlBy7jJI8Lf4VHwWfZZfpXtYSLy85pkm9GaYVYMfw5BC1A=="
          crossorigin="anonymous"
          referrerpolicy="no-referrer"
    />
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.2.0/dist/css/bootstrap.min.css" rel="stylesheet"
          integrity="sha384-gH2yIJqKdNHPEq0n4Mqa/HGKIhSkIHeL5AyhkYV8i59U5AR6csBvApHHNl/vI1Bx"
          crossorigin="anonymous">
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.2.0/dist/js/bootstrap.bundle.min.js"
            integrity="sha384-A3rJD856KowSb7dwlZdYEkO39Gagi7vIsF0jrRAoQmDKKtQBHUuLZ9AsSv4jD4Xa"
            crossorigin="anonymous"></script>
    <script src="//ajax.googleapis.com/ajax/libs/jquery/1.9.1/jquery.min.js"></script>
    <script type="text/javascript">
        $(document).ready(function () {
            $('#likeHandlerForm').submit(function (event) {
                $.ajax({
                    type: "POST",
                    url: $("#likeHandlerForm").attr("action"),
                    dataType: 'json',
                    success: function (data) {
                        $("#like-btn").html(data.likeButtonText);
                        $("#like-counter").html(data.totalLikes);
                    }
                });
                event.preventDefault();
            });
        });
    </script>
    <title><fmt:message key="title.post"/></title>
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
    <c:url var="forumViewLink" value="/forum/view">
        <c:param name="forumId" value="${post.forum.id}"/>
    </c:url>
    <c:url var="userViewLink" value="/user/view">
        <c:param name="userId" value="${post.user.id}"/>
    </c:url>
    <div class="card">
        <div class="card-header">
            <h5>
                <c:out value="${post.title}"/>
            </h5>
            <small>
                <fmt:message key="label.postedBy"/>
                <a href="${userViewLink}"><c:out value=" ${post.user.username} "/></a>
                <fmt:message key="label.in"/>
                <a href="${forumViewLink}"><c:out value=" ${post.forum.name} "/></a>
                <fmt:message key="label.on"/>
                <fmt:formatDate pattern="MM/dd/yyyy" value="${post.created}"/>
                <c:out value=" | "/>
                <fmt:message key="label.modifiedOn"/>
                <fmt:formatDate pattern="MM/dd/yyyy" value="${post.updated}"/>
            </small>
        </div>
        <div class="card-body">
            <p class="card-text"><c:out value="${post.body}"/></p>
            <c:if test="${SESSION_USER == forum.manager}">
                <c:url var="postApproveLink" value="/post/approve">
                    <c:param name="postId" value="${post.id}"/>
                </c:url>
                <c:url var="postDeclineLink" value="/post/decline">
                    <c:param name="postId" value="${post.id}"/>
                </c:url>
                <c:if test="${PENDING || DECLINED}">
                    <form action="${postApproveLink}" method="post">
                        <button class="btn btn-success"><fmt:message key="btn.approve"/></button>
                    </form>
                </c:if>
                <c:if test="${PENDING}">
                    <form action="${postDeclineLink}" method="post">
                        <button class="btn btn-danger"><fmt:message key="btn.decline"/></button>
                    </form>
                </c:if>
            </c:if>
            <c:if test="${SESSION_USER == forum.manager || SESSION_USER == post.user}">
                <c:url var="postUpdateLink" value="/post/update">
                    <c:param name="postId" value="${post.id}"/>
                </c:url>
                <a href="${postUpdateLink}">
                    <button type="button" class="btn btn-primary"><fmt:message key="btn.edit"/></button>
                </a>
                <br/><br/>
                <c:url var="postDeleteLink" value="/post/delete">
                    <c:param name="postId" value="${post.id}"/>
                </c:url>
                <form action="${postDeleteLink}" method="post"
                      onSubmit="return confirm('<fmt:message key="postDeleteConfirmationMessage"/>');">
                    <button class="btn btn-danger"><fmt:message key="btn.delete"/></button>
                </form>
            </c:if>
        </div>
        <c:if test="${post.status == 'APPROVED'}">
            <div class="card-footer">
                <c:url var="likeLink" value="/post/like">
                    <c:param name="postId" value="${post.id}"/>
                </c:url>
                <form id="likeHandlerForm" action="${likeLink}" method="post" class="input-group mb-3">
                 <span class="input-group-text">
                     <button id="like-btn" class="btn btn-small btn-link">
                     <c:choose>
                         <c:when test="${LIKED}">
                             <fmt:message key="btn.unlike"/>
                         </c:when>
                         <c:otherwise>
                             <fmt:message key="btn.like"/>
                         </c:otherwise>
                     </c:choose>
                     </button>
                     <i id="like-counter" class="fa-regular fa-thumbs-up"><c:out value="${post.totalLikes}"/></i>
                     &nbsp;&nbsp;<i class="fa-regular fa-comment"><c:out value="${post.totalComments}"/></i>
                 </span>
                </form>
            </div>
        </c:if>
    </div>
    <br/>
    <c:url var="commentSaveLink" value="/comment/save">
        <c:param name="postId" value="${post.id}"/>
    </c:url>
    <a href="${commentSaveLink}">
        <button type="button" class="btn btn-primary"><fmt:message key="btn.addComment"/></button>
    </a>
    <br/><br/>
    <c:set var="comments" scope="request" value="${post.comments}"/>
    <jsp:include page="../comment/list.jsp"/>
    <br/><br/>
    <jsp:include page='../common/footer.jsp'/>
</div>
</body>
</html>