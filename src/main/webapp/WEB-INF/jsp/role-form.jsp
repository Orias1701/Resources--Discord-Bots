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
    <title>Form Role</title>
</head>
<body>
<h2>
    <c:if test="${role.id == null}">Them Role</c:if>
    <c:if test="${role.id != null}">Sua Role</c:if>
</h2>

<form action="${pageContext.request.contextPath}/roles/save" method="post">
    <input type="hidden" name="id" value="${role.id}" />

    <label>Ten Role:</label>
    <input type="text" name="name" value="${role.name}" required/><br/><br/>

    <label>Mo ta:</label>
    <input type="text" name="description" value="${role.description}" /><br/><br/>

    <input type="submit" value="Luu" />
</form>

<br/>
<a href="${pageContext.request.contextPath}/roles"> Quay lai</a>
</body>
</html>
