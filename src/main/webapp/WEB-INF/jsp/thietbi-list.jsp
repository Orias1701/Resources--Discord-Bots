<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:if test="${not empty error}">
  <div class="alert alert-danger">${error}</div>
</c:if>
<c:if test="${not empty message}">
  <div class="alert alert-success">${message}</div>
</c:if>

<html>
<head>
    <meta charset="UTF-8">
    <title>Danh sach thiet bi</title>
</head>
<body>
<h2>Danh sach thiet bi</h2>

<form method="get" action="/thietbi">
    Tim kiem: <input type="text" name="keyword" value="${keyword}" />
    <button type="submit">Tim</button>
</form>

<a href="/thietbi/form">Them moi thiet bi</a>

<table border="1" cellpadding="5">
    <tr>
        <th>MaThietBi</th>
        <th>TenThietBi</th>
        <th>DonGia</th>
        <th>DenBu</th>
        <th>Hanh dong</th>
    </tr>
    <c:forEach var="tb" items="${list}">
        <tr>
            <td>${tb.maThietBi}</td>
            <td>${tb.tenThietBi}</td>
            <td>${tb.donGia}</td>
            <td>${tb.denBu}</td>
            <td>
                <a href="/thietbi/form?maThietBi=${tb.maThietBi}">Sua</a>
                <a href="/thietbi/delete?maThietBi=${tb.maThietBi}"
                   onclick="return confirm('Ban co muon xoa?');">Xoa</a>
            </td>
        </tr>
    </c:forEach>
</table>
</body>
</html>
