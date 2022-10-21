<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
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
    <title><fmt:message key="title.post"/></title>
</head>
<body>
<div class="container">
    <c:if test="${listType == 'requestList'}">
        <jsp:include page='../common/navbar.jsp'/>
        <button type="button" class="btn btn-dark" onclick="history.back()"><fmt:message key="btn.back"/></button>
        <br/><br/>
    </c:if>
    <div class="row">
        <div class="col-12">
            <c:forEach var="post" items="${posts}">
                <c:url var="postViewLink" value="/post/view">
                    <c:param name="postId" value="${post.id}"/>
                </c:url>
                <c:url var="userViewLink" value="/user/view">
                    <c:param name="userId" value="${post.user.id}"/>
                </c:url>
                <c:url var="forumViewLink" value="/forum/view">
                    <c:param name="forumId" value="${post.forum.id}"/>
                </c:url>
                <c:set var="shortText" value="${fn:substring(post.body, 0, 200)}"/>
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
                        <p class="card-text"><c:out value="${shortText}..."/></p>
                        <a href="${postViewLink}"><fmt:message key="label.readMore"/></a>
                    </div>
                    <c:if test="${post.status == 'APPROVED'}">
                        <div class="card-footer">
                            <i class="fa-regular fa-thumbs-up"><c:out value=" ${post.totalLikes}"/></i>
                            &nbsp;&nbsp;<i class="fa-regular fa-comment"><c:out value=" ${post.totalComments}"/></i>
                        </div>
                    </c:if>
                </div>
                <br/>
            </c:forEach>
        </div>
    </div>
    <c:if test="${listType == 'requestList'}">
        <br/><br/>
        <jsp:include page='../common/footer.jsp'/>
    </c:if>
</div>
</body>
</html>