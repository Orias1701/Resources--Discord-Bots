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
    <title>Form Loai Phong</title>
</head>
<body>
<h2>Form Loai Phong</h2>

<form method="post" action="/loaiphong/save">
    Ma Loai: <input type="text" name="maLoai" value="${loaiphong.maLoai}" ${loaiphong.maLoai != null ? "readonly" : ""} /> <br/>
    Ten Loai: <input type="text" name="tenLoai" value="${loaiphong.tenLoai}" /> <br/>
    So Giuong: <input type="text" name="soGiuong" value="${loaiphong.soGiuong}" /> <br/>
    Gia Loai: <input type="text" name="giaLoai" value="${loaiphong.giaLoai}" /> <br/>
    <button type="submit">Luu</button>
    <a href="/loaiphong">Huy</a>
</form>

</body>
</html>
