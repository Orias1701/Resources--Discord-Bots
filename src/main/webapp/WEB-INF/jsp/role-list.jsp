<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:if test="${not empty error}">
  <div class="alert alert-danger">${error}</div>
</c:if>
<c:if test="${not empty message}">
  <div class="alert alert-success">${message}</div>
</c:if>

<html>
<head>
    <title>Danh sach Role</title>
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
<h2>Danh sach role</h2>

<a class="btn add" href="${pageContext.request.contextPath}/roles/add">+ Them Role</a>
<br/><br/>

<table>
    <tr>
        <th>ID</th>
        <th>Ten role</th>
        <th>Mo ta</th>
        <th>Hanh dong</th>
    </tr>
    <c:forEach var="role" items="${roles}">
        <tr>
            <td>${role.id}</td>
            <td>${role.name}</td>
            <td>${role.description}</td>
            <td>
                <a class="btn edit" href="${pageContext.request.contextPath}/roles/edit/${role.id}">Sua</a>
                <a class="btn delete" href="${pageContext.request.contextPath}/roles/delete/${role.id}"
                   onclick="return confirm('Chac chan xoa khong');">Xoa</a>
            </td>
        </tr>
    </c:forEach>
</table>

</body>
</html>
