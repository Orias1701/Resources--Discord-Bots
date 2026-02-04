<<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:if test="${not empty error}">
  <div class="alert alert-danger">${error}</div>
</c:if>
<c:if test="${not empty message}">
  <div class="alert alert-success">${message}</div>
</c:if>

<c:if test="${not empty error}">
  <div class="alert alert-danger">${error}</div>
</c:if>
<c:if test="${not empty message}">
  <div class="alert alert-success">${message}</div>
</c:if>

<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <title>Form Kiểm Tra Phòng</title>
</head>
<body>
<h2>Form Kiểm Tra Phòng</h2>

<form method="post" action="/kiemtraphong/save">
    Mã Kiểm Tra: <input type="text" name="maKiemTra" value="${kiemtraphong.maKiemTra}" readonly/> <br/>
    Phòng:
    <select name="phong.maPhong">
        <c:forEach var="p" items="${listPhong}">
            <option value="${p.maPhong}" ${kiemtraphong.phong != null && kiemtraphong.phong.maPhong == p.maPhong ? "selected" : ""}>
                    ${p.tenPhong}
            </option>
        </c:forEach>
    </select> <br/>
    Mã Nhân Viên:
    <select name="maNhanVien">
        <c:forEach var="nv" items="${listNV}">
            <option value="${nv.maNhanVien}"
                ${kiemtraphong.maNhanVien != null && kiemtraphong.maNhanVien == nv.maNhanVien ? "selected" : ""}>
                    ${nv.tenNhanVien} - ${nv.chucVu}
            </option>
        </c:forEach>
    </select>
    <br/>



    Ngày Kiểm Tra: <input type="datetime-local" name="ngayKiemTra" value="${kiemtraphong.ngayKiemTra}"/> <br/>
    Ghi Chú: <input type="text" name="ghiChu" value="${kiemtraphong.ghiChu}"/> <br/>
    Trạng Thái:
    <select name="trangThai">
        <c:forEach var="tt" items="${trangThaiOptions}">
            <option value="${tt}" ${kiemtraphong.trangThai == tt ? "selected" : ""}>${tt}</option>
        </c:forEach>
    </select>
    <br/>
    <button type="submit">Lưu</button>
    <a href="/kiemtraphong">Hủy</a>
</form>

</body>
</html>