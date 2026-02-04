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
    <title>Form Phong</title>
</head>
<body>
<h2>Form Phong</h2>

<form method="post" action="/phong/save">
    Ma Phong: <input type="text" name="maPhong" value="${phong.maPhong}" ${phong.maPhong != null ? "readonly" : ""}/> <br/>
    Ten Phong: <input type="text" name="tenPhong" value="${phong.tenPhong}"/> <br/>
    Loai Phong:
    <select name="maLoai.maLoai">
        <c:forEach var="lp" items="${listLoaiPhong}">
            <option value="${lp.maLoai}" ${phong.maLoai != null && phong.maLoai.maLoai == lp.maLoai ? "selected" : ""}>
                    ${lp.tenLoai}
            </option>
        </c:forEach>
    </select> <br/>
    Mo Ta: <input type="text" name="moTa" value="${phong.moTa}"/> <br/>
    Tinh Trang Phong:
    <select name="tinhTrangPhong">
        <c:forEach var="tt" items="${tinhTrangOptions}">
            <option value="${tt}" ${phong.tinhTrangPhong == tt ? "selected" : ""}>${tt}</option>
        </c:forEach>
    </select>
    <br/>
    <button type="submit">Luu</button>
    <a href="/phong">Huy</a>
</form>

</body>
</html>
