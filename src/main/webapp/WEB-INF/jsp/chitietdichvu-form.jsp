<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
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
    <title>Form chi tiết dịch vụ</title>
    <style>
        body { font-family: Arial, sans-serif; margin: 20px; }
        form { max-width: 560px; }
        label { display:block; margin-top:12px; font-weight:600; }
        input[type="number"], select, input[type="text"] { width:100%; padding:8px; box-sizing:border-box; }
        .row { display:flex; gap:12px; }
        .row > div { flex:1; }
        .actions { margin-top:18px; display:flex; gap:10px; }
        .error { color:#b00020; margin-bottom:12px; }
    </style>
    <script>
        function updateThanhTien() {
            const selectDV = document.getElementById("dichVuSelect");
            const soLuongInput = document.getElementById("soLuongInput");
            const giaInput = document.getElementById("giaDichVuInput");
            const thanhTienInput = document.getElementById("thanhTienInput");
            if (!selectDV || !soLuongInput || !giaInput || !thanhTienInput) return;

            var opt = selectDV.options[selectDV.selectedIndex];
            var gia = parseFloat(opt ? (opt.getAttribute("data-gia") || "0") : "0") || 0;
            var sl  = parseInt(soLuongInput.value || "0", 10) || 0;

            giaInput.value = gia.toFixed(2);
            thanhTienInput.value = (gia * sl).toFixed(2);
        }
        window.addEventListener("DOMContentLoaded", updateThanhTien);
    </script>
</head>
<body>

<h2>${ct != null && ct.id != null ? "Sửa chi tiết sử dụng dịch vụ" : "Thêm chi tiết sử dụng dịch vụ"}</h2>

<c:if test="${not empty error}">
    <div class="error">${error}</div>
</c:if>

<form method="post" action="${pageContext.request.contextPath}/chitietdichvu/save">
    <c:if test="${ct ne null and ct.id ne null}">
        <input type="hidden" name="id" value="${ct.id}" />
    </c:if>

    <!-- MaSDDV: tên PHẢI là maSDDV (phẳng) -->
    <label>MaSDDV</label>
    <select name="maSDDV"
            <c:if test="${ct ne null and ct.id ne null}">disabled</c:if>>
        <c:forEach var="sddv" items="${listSDDV}">
            <option value="${sddv.maSDDV}"
                    <c:if test="${ct ne null and ct.suDungDichVu ne null and ct.suDungDichVu.maSDDV eq sddv.maSDDV}">
                        selected
                    </c:if>>
                    ${sddv.maSDDV}
            </option>
        </c:forEach>
    </select>
    <!-- mirror hidden để submit khi select bị disabled -->
    <c:if test="${ct ne null and ct.id ne null}">
        <input type="hidden" name="maSDDV" value="${ct.suDungDichVu.maSDDV}" />
    </c:if>

    <!-- MaDichVu: tên PHẢI là maDichVu (phẳng) -->
    <label>Mã dịch vụ</label>
    <select id="dichVuSelect" name="maDichVu" onchange="updateThanhTien()"
            <c:if test="${ct ne null and ct.id ne null}">disabled</c:if>>
        <c:forEach var="dv" items="${listDV}">
            <option value="${dv.maDichVu}" data-gia="${dv.giaDichVu}"
                <c:if test="${ct ne null and ct.dichVu ne null and ct.dichVu.maDichVu eq dv.maDichVu}">
                    selected
                </c:if>>
                ${dv.maDichVu} - ${dv.tenDichVu}
            </option>
        </c:forEach>
    </select>
    <!-- mirror hidden để submit khi select bị disabled -->
    <c:if test="${ct ne null and ct.id ne null}">
        <input type="hidden" name="maDichVu" value="${ct.dichVu.maDichVu}" />
    </c:if>

    <div class="row">
        <div>
            <label>Số lượng</label>
            <input type="number" id="soLuongInput" name="soLuong"
                   value="${ct.soLuong != null ? ct.soLuong : 1}"
                   min="1" required oninput="updateThanhTien()" />
        </div>
        <div>
            <label>Giá dịch vụ (tham khảo)</label>
            <input type="text" id="giaDichVuInput" value="" readonly />
        </div>
    </div>

    <div class="row">
        <div>
            <label>Thành tiền (tự tính)</label>
            <input type="text" id="thanhTienInput" value="" readonly />
        </div>
    </div>

    <div class="actions">
        <button type="submit">Lưu</button>
        <a href="${pageContext.request.contextPath}/chitietdichvu">Hủy</a>
    </div>
</form>

</body>
</html>
