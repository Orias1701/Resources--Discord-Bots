<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>${param.pageTitle} | Quản Lý Khách Sạn</title>

    <%-- Các file CSS chung và file CSS chuyên cho form --%>
    <link rel="stylesheet" href="<c.url value='/static/css/main-style.css' />">
    <link rel="stylesheet" href="<c:url value='/static/css/data-style.css' />">
    <link rel="stylesheet" href="<c.url value='/static/css/form-style.css' />">
</head>
<body>
    <div class="popup-container">
        <main class="app-main-content">
            <jsp:include page="${param.mainContent}" />
        </main>
    </div>

    <script src="https://code.jquery.com/jquery-3.7.0.min.js"></script>
    <script src="<c:url value='/static/js/main-script.js' />"></script>

    <c:if test="${not empty param.pageScript}">
        <jsp:include page="${param.pageScript}" />
    </c:if>
</body>
</html>