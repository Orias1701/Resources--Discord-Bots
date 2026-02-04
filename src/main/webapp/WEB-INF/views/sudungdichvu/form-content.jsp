<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<div class="form-container">
    <h2>Sử Dụng Dịch Vụ</h2>
    <form method="post" action="/sddv/save">
        <div class="form-group">
            <label>Mã SDDV</label>
            <input type="text" name="maSDDV" value="${sddv.maSDDV}" readonly />
        </div>
        <div class="form-group">
            <label>Khách Hàng</label>
            <select name="khachHang.maKhachHang">
                <c:forEach var="kh" items="${khachHangList}">
                    <option value="${kh.maKhachHang}" ${sddv.khachHang.maKhachHang == kh.maKhachHang ? "selected" : ""}>
                        ${kh.maKhachHang} - ${kh.tenKhachHang}
                    </option>
                </c:forEach>
            </select>
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
            <label>Ngày Sử Dụng</label>
            <input type="date" name="ngaySDDV" value="${sddv.ngaySDDV}" />
        </div>
        <div class="form-group">
            <label>Tổng tiền</label>
            <input type="text" name="tongTien" value="${sddv.tongTien}" readonly/>
        </div>
        <div class="form-group">
            <label for="thanhToan">Thanh Toán:</label>
            <select name="thanhToan" id="thanhToan">
                <option value="CHUA_THANH_TOAN" ${sddv.thanhToan == 'CHUA_THANH_TOAN' ? "selected" : ""}>
                    CHUA_THANH_TOAN
                </option>
                <option value="DA_THANH_TOAN" ${sddv.thanhToan == 'DA_THANH_TOAN' ? "selected" : ""}>
                    DA_THANH_TOAN
                </option>
            </select>
        </div>
        <div class="form-actions">
             <a href="/sddv" class="btn-secondary">Hủy</a>
            <button type="submit" class="btn-primary">Lưu</button>
        </div>
    </form>
</div>