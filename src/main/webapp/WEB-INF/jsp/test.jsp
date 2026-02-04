<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
    <title>Trang chinh</title>
    <style>
        body {
            font-family: Arial, sans-serif;
        }
        .header {
            background-color: #f2f2f2;
            padding: 15px;
        }
        .content {
            margin: 20px;
        }
        .logout {
            float: right;
        }
    </style>
</head>
<body>
<div class="header">
    <c:if test="${not empty sessionScope.loggedInUser}">
        Xin chao, <b>${sessionScope.loggedInUser.username}</b>!
        <a href="${pageContext.request.contextPath}/logout" class="logout">Sign Out</a>
    </c:if>
</div>

<div class="content">
    <h2>Trang chinh</h2>
    <p>Chao mung ban den voi he thong!</p>

    <c:if test="${not empty sessionScope.loggedInUser}">
        <p>Thong tin dang nhap:</p>
        <ul>
            <li>ID: ${sessionScope.loggedInUser.id}</li>
            <li>Username: ${sessionScope.loggedInUser.username}</li>
            <li>Vai tro: ${sessionScope.loggedInUser.role.name}</li>
        </ul>
    </c:if>
</div>
</body>
</html>
