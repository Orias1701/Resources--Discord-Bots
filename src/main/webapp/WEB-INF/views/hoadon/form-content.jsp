<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<div class="form-container">
    <h2>Hóa Đơn</h2>
    <c:if test="${not empty error}"><div class="error">${error}</div></c:if>

    <form method="post" action="/hoadon/save">
        <div class="form-group">
            <label>Mã Hóa Đơn</label>
            <input type="text" name="maHoaDon" value="${hd.maHoaDon}" ${hd.maHoaDon != null ? "readonly" : ""}/>
        </div>

        <div class="form-group">
            <label>Nhân viên</label>
            <!-- Hiển thị nhân viên đang đăng nhập -->
            <select class="form-control" disabled>
                <option selected>
                    ${sessionScope.loggedInUser.nhanVien.maNhanVien} - ${sessionScope.loggedInUser.nhanVien.tenNhanVien}
                </option>
            </select>

            <!-- input hidden để gửi giá trị thật -->
            <input type="hidden" name="maNhanVien.maNhanVien"
                   value="${sessionScope.loggedInUser.nhanVien.maNhanVien}" />
        </div>


        <div class="form-group">
            <label>Khách hàng</label>
            <select name="khachHang.maKhachHang">
                <option value="">-- Không chọn --</option>
                <c:forEach var="kh" items="${khachHangList}">
                    <option value="${kh.maKhachHang}" <c:if test="${hd.khachHang.maKhachHang == kh.maKhachHang}">selected</c:if>>
                        ${kh.maKhachHang} - ${kh.tenKhachHang}
                    </option>
                </c:forEach>
            </select>
        </div>

        <div class="form-group">
            <label>Ngày Lập</label>
            <input type="date" name="ngay" value="${hd.ngay}"/>
        </div>

        <div class="form-group">
            <label>Trạng thái thanh toán</label>
            <select name="thanhToan">
                <option value="CHUA_THANH_TOAN" <c:if test="${hd.thanhToan == 'CHUA_THANH_TOAN'}">selected</c:if>>CHƯA THANH TOÁN</option>
                <option value="DA_THANH_TOAN"   <c:if test="${hd.thanhToan == 'DA_THANH_TOAN'}">selected</c:if>>ĐÃ THANH TOÁN</option>
            </select>
        </div>

        <div class="form-actions">
            <a href="/hoadon" class="btn-secondary">Hủy</a>
            <button type="submit" class="btn-primary">Lưu</button>
        </div>
    </form>
</div>