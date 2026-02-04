<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<div class="page-header">
    <h2>Danh sách Hóa Đơn</h2>
</div>

<div id = "sw_layout">
    <span id = "sw_table">Bảng</span>
    <span id = "sw_card">Thẻ</span>
</div>

<div class="page-controls">
    <div class="control-group filters">
        <form method="get" action="/hoadon">
            <input type="text" name="keyword" placeholder="Tìm kiếm..." value="${keyword}"/>
            <button type="submit"><i class="fa-regular fa-circle"></i></button>
            <a href="/hoadon" class="btn-secondary"><i class="fa-solid fa-arrow-rotate-left"></i></a>
        </form>
    </div>
    <div class="control-group actions">
        <a href="javascript:void(0)" onclick="loadForm('/hoadon/form')" class="btn-primary"><i class="fa-solid fa-plus"></i></a>
    </div>
</div>

<div class="data-layout">
    <div class="data-container table-lyo">
        <c:forEach var="h" items="${list}">
            <div class="data-item">
                <div class="data-cell" data-label="Mã HĐ">${h.maHoaDon}</div>
                <div class="data-cell" data-label="Nhân viên">${h.maNhanVien.maNhanVien}</div>
                <div class="data-cell" data-label="Khách hàng">${h.khachHang.maKhachHang}</div>
                <div class="data-cell" data-label="Ngày">${h.ngay}</div>
                <div class="data-cell" data-label="Tổng tiền">${h.tongTien}</div>
                <div class="data-cell" data-label="Trạng thái">${h.thanhToan}</div>
                <div class="item-actions">
                                    <button type="button"
                                        title="In hóa đơn"
                                        style="background-color:#F5E5C0;color:#654321;border:none;padding:5px 8px;border-radius:5px;cursor:pointer;"
                                        onclick="inHoaDon('${h.maHoaDon}')">
                                        🖨
                                     </button>


                    <button type="submit"><i class="fa-solid fa-check"></i></button>
                    <c:if test="${h.thanhToan eq 'CHUA_THANH_TOAN'}">
                        <form class="inline" method="post" action="/hoadon/mark-paid">
                            <input type="hidden" name="maHoaDon" value="${h.maHoaDon}"/>
                            <button type="submit"><i class="fa-solid fa-check"></i></button>
                        </form>
                    </c:if>
                </div>
            </div>
        </c:forEach>
    </div>
    <div class="data-container grid-lyo">
        <c:forEach var="h" items="${list}">
            <div class="data-item">
                <div class="data-cell" data-label="Mã HĐ">${h.maHoaDon}</div>
                <div class="data-cell" data-label="Nhân viên">${h.maNhanVien.maNhanVien}</div>
                <div class="data-cell" data-label="Khách hàng">${h.khachHang.maKhachHang}</div>
                <div class="data-cell" data-label="Ngày">${h.ngay}</div>
                <div class="data-cell" data-label="Tổng tiền">${h.tongTien}</div>
                <div class="data-cell" data-label="Trạng thái">${h.thanhToan}</div>
                <div class="item-actions">
                    <button type="submit">Đã Th. Toán <i class="fa-solid fa-check"></i></button>
                    <c:if test="${h.thanhToan eq 'CHUA_THANH_TOAN'}">
                        <form class="inline" method="post" action="/hoadon/mark-paid">
                            <input type="hidden" name="maHoaDon" value="${h.maHoaDon}"/>
                            <button type="submit">Thanh Toán <i class="fa-solid fa-check"></i></button>
                        </form>
                    </c:if>
                </div>
            </div>
        </c:forEach>
    </div>
</div>

<script>
function inHoaDon(maHoaDon) {
    // Mở trang in ra tab mới để xem trước hoặc tải PDF
    window.open("/hoadon/in/" + maHoaDon, "_blank");
}

</script>
