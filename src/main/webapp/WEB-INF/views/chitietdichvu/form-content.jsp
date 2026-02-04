<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<div class="form-container">
    <h2>Chi tiết SDDV</h2>

    <form id="ctForm" action="<c:url value='/chitietdichvu/save'/>" method="post">
        <c:if test="${ct != null && ct.id != null}">
            <input type="hidden" name="id" value="${ct.id}"/>
            <input type="hidden" name="maSDDV" value="${ct.suDungDichVu.maSDDV}" />
            <input type="hidden" name="maDichVu" value="${ct.dichVu.maDichVu}" />
        </c:if>

        <div class="form-group">
            <label>Mã sử dụng dịch vụ (SDDV)</label>
            <select name="maSDDV" <c:if test="${ct != null && ct.id != null}">disabled</c:if>>
                <c:forEach var="sddv" items="${listSDDV}">
                    <option value="${sddv.maSDDV}"
                        <c:if test="${ct.suDungDichVu.maSDDV eq sddv.maSDDV}">selected</c:if>>
                        ${sddv.maSDDV}
                    </option>
                </c:forEach>
            </select>
        </div>

        <div class="form-group">
            <label>Dịch vụ</label>
            <select id="dichVuSelect" name="maDichVu" <c:if test="${ct != null && ct.id != null}">disabled</c:if>>
                <c:forEach var="dv" items="${listDV}">
                    <option value="${dv.maDichVu}" data-gia="${dv.giaDichVu}"
                        <c:if test="${ct.dichVu.maDichVu eq dv.maDichVu}">selected</c:if>>
                        ${dv.maDichVu} - ${dv.tenDichVu}
                    </option>
                </c:forEach>
            </select>
        </div>

        <div class="form-group">
            <label>Số lượng</label>
            <input type="number" id="soLuong" name="soLuong" value="${ct.soLuong}" min="1" required />
        </div>

        <div class="form-group">
            <label>Thành tiền</label>
            <input type="text" id="thanhTien" name="thanhTien" value="${ct.thanhTien}" readonly />
        </div>

        <div class="form-actions">
            <a href="<c:url value='/chitietdichvu'/>" class="btn btn-secondary">Quay lại</a>
            <button type="submit" class="btn-primary">Lưu</button>
        </div>
    </form>
</div>

<script>
    function tinhThanhTien() {
        const select = document.getElementById("dichVuSelect");
        const gia = parseFloat(select.options[select.selectedIndex].getAttribute("data-gia")) || 0;
        const soLuong = parseInt(document.getElementById("soLuong").value) || 0;
        document.getElementById("thanhTien").value = gia * soLuong;
    }

    document.getElementById("dichVuSelect").addEventListener("change", tinhThanhTien);
    document.getElementById("soLuong").addEventListener("input", tinhThanhTien);

    // Tính ngay khi load nếu có dữ liệu sẵn
    window.addEventListener("load", tinhThanhTien);
</script>
