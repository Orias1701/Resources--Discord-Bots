<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
    <title>User List</title>
    <style>
        table {
            border-collapse: collapse;
            width: 60%;
        }
        th, td {
            padding: 8px;
            border: 1px solid #333;
            text-align: center;
        }
        a.btn {
            padding: 5px 10px;
            border-radius: 4px;
            text-decoration: none;
            margin: 2px;
        }
        .add { background: green; color: white; }
        .edit { background: orange; color: white; }
        .delete { background: red; color: white; }
    </style>
</head>
<body>
<h2>User List</h2>

<a class="btn add" href="${pageContext.request.contextPath}/users/add">+ Add User</a>
<br/><br/>

<table>
    <tr>
        <th>ID</th>
        <th>Username</th>
        <th>Password</th>
        <th>Actions</th>
    </tr>
    <c:forEach var="user" items="${users}">
        <tr>
            <td>${user.id}</td>
            <td>${user.username}</td>
            <td>${user.password}</td>
            <td>
                <a class="btn edit" href="${pageContext.request.contextPath}/users/edit/${user.id}">Edit</a>
                <a class="btn delete" href="${pageContext.request.contextPath}/users/delete/${user.id}"
                   onclick="return confirm('Are you sure to delete this user?');">Delete</a>
            </td>
        </tr>
    </c:forEach>
</table>

</body>
</html>
