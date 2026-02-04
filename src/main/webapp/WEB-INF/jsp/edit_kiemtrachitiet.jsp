<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:if test="${not empty error}">
  <div class="alert alert-danger">${error}</div>
</c:if>
<c:if test="${not empty message}">
  <div class="alert alert-success">${message}</div>
</c:if>

<!DOCTYPE html>
<html>
<head>
    <title>Sửa Chi Tiết Kiểm Tra</title>
    <script src="https://ajax.googleapis.com/ajax/libs/jquery/3.5.1/jquery.min.js"></script>
</head>
<body>
<h1>Sửa Chi Tiết Kiểm Tra</h1>

<form action="/kiemtrachitiet/update" method="post">
    <label for="maKiemTra">Mã Kiểm Tra:</label>
    <input type="text" id="maKiemTra" name="maKiemTra" value="${kiemTraChiTiet.id.maKiemTra}" readonly/><br><br>

    <label for="maThietBi">Mã Thiết Bị:</label>
    <input type="text" id="maThietBi" name="maThietBi" value="${kiemTraChiTiet.id.maThietBi}" readonly/><br><br>

    <label for="maPhong">Mã Phòng:</label>
    <input type="text" id="maPhong" name="maPhong" value="${kiemTraChiTiet.maPhong}" readonly/><br><br>

    <label>Tình Trạng:</label>
    <input type="radio" id="tinhTrangTot" name="tinhTrang" value="TOT" ${kiemTraChiTiet.tinhTrang.name() == 'TOT' ? 'checked' : ''}>
    <label for="tinhTrangTot">Tốt</label>
    <input type="radio" id="tinhTrangHong" name="tinhTrang" value="HONG" ${kiemTraChiTiet.tinhTrang.name() == 'HONG' ? 'checked' : ''}>
    <label for="tinhTrangHong">Hỏng</label><br><br>

    <label for="soLuongHong">Số Lượng Hỏng:</label>
    <input type="number" id="soLuongHong" name="soLuongHong" min="0" value="${kiemTraChiTiet.soLuongHong}" required><br><br>
    <c:if test="${not empty errorMessage}">
        <div style="color:red; margin-bottom: 10px;">
                ${errorMessage}
        </div>
    </c:if>


    <label for="denBu">Đền Bù:</label>
    <input type="number" id="denBu" name="denBu" value="${kiemTraChiTiet.denBu}" readonly><br><br>

    <label for="ghiChu">Ghi Chú:</label>
    <textarea id="ghiChu" name="ghiChu">${kiemTraChiTiet.ghiChu}</textarea><br><br>

    <button type="submit">Lưu Cập Nhật</button>
    <a href="/kiemtrachitiet/list">Hủy</a>
</form>

<script>
    $(document).ready(function() {
        let giaDenBuCoBan = 0;

        // Hàm cập nhật tổng tiền
        function capNhatTongTien() {
            const soLuongHong = $('#soLuongHong').val();
            const tongDenBu = giaDenBuCoBan * parseInt(soLuongHong);
            $('#denBu').val(tongDenBu);
        }

        // Sự kiện khi Tình Trạng thay đổi
        $('input[name="tinhTrang"]').change(function() {
            const tinhTrang = $('input[name="tinhTrang"]:checked').val();
            const maThietBi = $('#maThietBi').val();

            if (tinhTrang === 'HONG' && maThietBi) {
                $.get('/kiemtrachitiet/api/get-denbu', { maThietBi: maThietBi }, function(data) {
                    giaDenBuCoBan = parseFloat(data);
                    capNhatTongTien();
                });
            } else {
                giaDenBuCoBan = 0;
                capNhatTongTien();
            }
        });

        // Sự kiện khi Số Lượng Hỏng thay đổi
        $('#soLuongHong').on('input', function() {
            capNhatTongTien();
        });

        // LƯU Ý QUAN TRỌNG: Gọi hàm này một lần khi trang được tải để tính toán ban đầu
        // cho trường hợp Sửa với dữ liệu có sẵn.
        $('input[name="tinhTrang"]').trigger('change');
    });
</script>
</body>
</html>