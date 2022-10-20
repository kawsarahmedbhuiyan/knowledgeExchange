<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!--
@author kawsar.bhuiyan
@since 10/14/22
-->
<nav class="navbar navbar-dark navbar-expand-lg bg-primary">
    <div class="container-fluid">
        <c:url var="home" value="/">
        </c:url>
        <a href="${home}" class="navbar-brand"><fmt:message key="title.knowledgeExchange"/></a>
        <c:if test="${not empty SESSION_USER}">
            <div class="collapse navbar-collapse">
                <ul class="navbar-nav ms-auto mb-2 mb-lg-0">
                    <li class="nav-item">
                        <c:url var="profile" value="/user/view">
                            <c:param name="userId" value="${SESSION_USER.id}"/>
                        </c:url>
                        <a href="${profile}">
                            <button type="button" class="btn btn-primary">
                                <c:out value="${SESSION_USER.username}"/>
                            </button>
                        </a>
                    </li>
                    &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
                    <li class="nav-item">
                        <c:url var="logout" value="/auth/logout">
                        </c:url>
                        <a href="${logout}">
                            <button type="button" class="btn btn-outline-light"><fmt:message key="btn.logout"/></button>
                        </a>
                    </li>
                </ul>
            </div>
        </c:if>
    </div>
</nav>
<br/><br/>