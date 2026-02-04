<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<div class="form-container">
<c:if test="${not empty error}">
  <div class="alert alert-danger">${error}</div>
 </c:if>
    <h2>Đặt phòng</h2>

    <form method="post" action="datphong/save">
        <input type="hidden" name="maDatPhong" value="${datPhong.maDatPhong}"/>

        <div class="form-group">
            <label>Khách hàng</label>
            <select name="maKhachHang.maKhachHang" required>
                <c:forEach var="kh" items="${khachHangList}">
                    <option value="${kh.maKhachHang}" <c:if test="${datPhong.maKhachHang.maKhachHang == kh.maKhachHang}">selected</c:if>>
                        ${kh.maKhachHang} - ${kh.tenKhachHang}
                    </option>
                </c:forEach>
            </select>
        </div>

        <div class="form-group">
            <label>Nhân viên</label>
            <select name="nhanVien.maNhanVien" class="form-control" disabled>
                <option value="${sessionScope.loggedInUser.nhanVien.maNhanVien}" selected>
                    ${sessionScope.loggedInUser.nhanVien.maNhanVien} - ${sessionScope.loggedInUser.nhanVien.tenNhanVien}
                </option>
            </select>
            <!-- input hidden để gửi giá trị thật về server -->
            <input type="hidden" name="nhanVien.maNhanVien" value="${sessionScope.loggedInUser.nhanVien.maNhanVien}" />
        </div>


        <div class="form-group">
            <label>Phòng</label>
            <select name="maPhong.maPhong" required>
                <c:forEach var="p" items="${phongList}">
                    <option value="${p.maPhong}" <c:if test="${datPhong.maPhong.maPhong == p.maPhong}">selected</c:if>>
                        ${p.maPhong} - ${p.tenPhong}
                    </option>
                </c:forEach>
            </select>
        </div>

        <div class="form-group">
            <label>Giờ nhận (dự kiến)</label>
            <input type="datetime-local" name="ngayNhanPhong" value="${datPhong.ngayNhanPhong != null ? datPhong.ngayNhanPhong.toString().substring(0,16) : ''}" required/>
        </div>

        <div class="form-group">
            <label>Giờ trả (dự kiến)</label>
            <input type="datetime-local" name="ngayHenTra" value="${datPhong.ngayHenTra != null ? datPhong.ngayHenTra.toString().substring(0,16) : ''}" required/>
        </div>

        <div class="form-group">
            <label>Giờ trả (thực tế)</label>
            <input type="datetime-local" name="ngayTraPhong" value="${datPhong.ngayTraPhong != null ? datPhong.ngayTraPhong.toString().substring(0,16) : ''}"/>
        </div>
        <div class="form-group">
            <label>Cách đặt</label>
            <select name="cachDat" class="form-control" disabled>
                <option value="DAT_TRUC_TIEP" selected>DAT_TRUC_TIEP</option>
            </select>
            <!-- Hidden để gửi giá trị thật -->
            <input type="hidden" name="cachDat" value="DAT_TRUC_TIEP" />
        </div>

        <div class="form-group">
            <label>Tình trạng</label>
            <select name="tinhTrang">
                <c:forEach items="${tinhTrangEnums}" var="tt">
                    <option value="${tt}" <c:if test="${datPhong.tinhTrang == tt}">selected</c:if>>${tt}</option>
                </c:forEach>
            </select>
        </div>
        
        <div class="form-actions">
            <a href="/datphong" class="btn-secondary">Quay lại</a>
            <button type="submit" class="btn-primary">Lưu</button>
        </div>
    </form>
</div>