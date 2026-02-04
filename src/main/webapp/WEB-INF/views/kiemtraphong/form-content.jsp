<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<link rel="stylesheet" href="<spring:url value='/css/main-style.css'/>" />

<div class="form-container">
    <h2>Kiểm Tra Phòng</h2>
    <form method="post" action="/kiemtraphong/save">
        <div class="form-group">
            <label>Mã Kiểm Tra</label>
            <input type="text" name="maKiemTra" value="${kiemtraphong.maKiemTra}" readonly/>
        </div>
        <div class="form-group">
            <label>Phòng</label>
            <select id="phongSelect" disabled>
                <c:forEach var="p" items="${listPhong}">
                    <option value="${p.maPhong}"
                        ${kiemtraphong.phong != null && kiemtraphong.phong.maPhong == p.maPhong ? "selected" : ""}>
                        ${p.maPhong} - ${p.tenPhong}
                    </option>
                </c:forEach>
            </select>
            <!-- Hidden để submit về server -->
            <input type="hidden" name="phong.maPhong" id="phongHidden"
                   value="${kiemtraphong.phong != null ? kiemtraphong.phong.maPhong : ''}"/>
        </div>
        <div class="form-group">
            <label>Đặt Phòng</label>
            <select name="datPhong.maDatPhong" id="datPhongSelect">
                <c:forEach var="dp" items="${listDatPhong}">
                    <option
                        value="${dp.maDatPhong}"
                        data-phong="${dp.maPhong != null ? dp.maPhong.maPhong : ''}"
                        ${ (not empty kiemtraphong.datPhong
                            and kiemtraphong.datPhong.maDatPhong == dp.maDatPhong) ? 'selected' : '' }>
                        ${dp.maDatPhong}
                        <c:if test="${not empty dp.maKhachHang}"> - ${dp.maKhachHang.tenKhachHang}</c:if>
                    </option>
                </c:forEach>
            </select>
        </div>

        <div class="form-group">
            <label>Nhân Viên</label>
            <input type="text" value="${nhanVienDangNhap.tenNhanVien}" readonly />
            <input type="hidden" name="maNhanVien.maNhanVien" value="${nhanVienDangNhap.maNhanVien}" />
        </div>

        <div class="form-group">
            <label>Ngày Kiểm Tra</label>
            <input type="datetime-local" name="ngayKiemTra" value="${kiemtraphong.ngayKiemTra}"/>
        </div>
        <div class="form-group">
            <label>Ghi Chú</label>
            <input type="text" name="ghiChu" value="${kiemtraphong.ghiChu}"/>
        </div>
        <div class="form-group">
            <label>Trạng Thái</label>
            <select name="trangThai">
                <c:forEach var="tt" items="${trangThaiOptions}">
                    <option value="${tt}" ${kiemtraphong.trangThai == tt ? "selected" : ""}>${tt}</option>
                </c:forEach>
            </select>
        </div>
        <div class="form-group">
            <label>Tiền Kiểm Tra</label>
            <input type="text" name="tienKiemTra" value="${kiemtraphong.tienKiemTra}" readonly/>
        </div>
        <div class="form-group">
            <label for="thanhToan">Thanh Toán:</label>
            <select name="thanhToan" id="thanhToan">
                <option value="CHUA_THANH_TOAN" ${kiemtraphong.thanhToan == 'CHUA_THANH_TOAN' ? "selected" : ""}>
                    CHUA_THANH_TOAN
                </option>
                <option value="DA_THANH_TOAN" ${kiemtraphong.thanhToan == 'DA_THANH_TOAN' ? "selected" : ""}>
                    DA_THANH_TOAN
                </option>
            </select>
        </div>
        <div class="form-actions">
             <a href="/kiemtraphong" class="btn-secondary">Hủy</a>
            <button type="submit" class="btn-primary">Lưu</button>
        </div>
    </form>
</div>
<script>
  (function() {
    var dpSel = document.getElementById('datPhongSelect');
    var phongSel = document.getElementById('phongSelect');
    var phongHidden = document.getElementById('phongHidden');

    function syncRoomFromBooking() {
      var opt = dpSel.options[dpSel.selectedIndex];
      if (!opt) return;
      var maPhong = opt.getAttribute('data-phong') || '';
      if (maPhong) {
        // set cho select hiển thị (disabled)
        phongSel.value = maPhong;
        // set cho input hidden để submit
        phongHidden.value = maPhong;
      } else {
        // nếu không có phòng gắn với mã đặt phòng
        phongSel.value = '';
        phongHidden.value = '';
      }
    }

    // Đồng bộ khi thay đổi mã đặt phòng
    dpSel.addEventListener('change', syncRoomFromBooking);

    // Đồng bộ lần đầu khi vào form
    syncRoomFromBooking();
  })();
</script>