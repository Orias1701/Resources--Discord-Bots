<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:if test="${not empty error}">
  <div class="alert alert-danger">${error}</div>
</c:if>
<c:if test="${not empty message}">
  <div class="alert alert-success">${message}</div>
</c:if>

<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html>
<head>
    <title>Thêm Chi Tiết Kiểm Tra</title>
    <script src="https://ajax.googleapis.com/ajax/libs/jquery/3.5.1/jquery.min.js"></script>
</head>
<body>
<h1>Thêm Chi Tiết Kiểm Tra</h1>

<form action="/kiemtrachitiet/add" method="post">

    <label for="maKiemTra">Mã Kiểm Tra:</label>
    <select id="maKiemTra" name="maKiemTra" required>
        <option value="">-- Chọn Mã Kiểm Tra --</option>
        <c:forEach var="kt" items="${kiemTraPhongs}">
            <option value="${kt.maKiemTra}">${kt.maKiemTra}</option>
        </c:forEach>
    </select><br><br>

    <label for="maPhong">Mã Phòng:</label>
    <input type="text" id="maPhong" name="maPhong" readonly><br><br>

    <label for="maThietBi">Mã Thiết Bị:</label>
    <select id="maThietBi" name="maThietBi" required>
        <option value="">-- Chọn Thiết Bị --</option>
    </select><br><br>

    <label>Tình Trạng:</label>
    <input type="radio" id="tinhTrangTot" name="tinhTrang" value="TOT" checked>
    <label for="tinhTrangTot">Tốt</label>
    <input type="radio" id="tinhTrangHong" name="tinhTrang" value="HONG">
    <label for="tinhTrangHong">Hỏng</label><br><br>

    <label for="soLuongHong">Số Lượng Hỏng:</label>
    <input type="number" id="soLuongHong" name="soLuongHong" min="0" value="0" required><br><br>
    <c:if test="${not empty errorMessage}">
        <div style="color:red; margin-bottom: 10px;">
                ${errorMessage}
        </div>
    </c:if>


    <label for="denBu">Đền Bù:</label>
    <input type="number" id="denBu" name="denBu" value="0" readonly><br><br>

    <label for="ghiChu">Ghi Chú:</label>
    <textarea id="ghiChu" name="ghiChu"></textarea><br><br>

    <button type="submit">Lưu</button>
</form>

<script>
    $(document).ready(function() {
        // Biến để lưu trữ giá đền bù cơ bản, giúp tính toán tức thì mà không cần gọi API lại
        let giaDenBuCoBan = 0;

        // Sự kiện khi Mã Kiểm Tra thay đổi
        $('#maKiemTra').change(function() {
            const maKiemTra = $(this).val();
            if (maKiemTra) {
                $.get('/kiemtrachitiet/api/get-thong-tin-theo-kiem-tra', { maKiemTra: maKiemTra }, function(data) {
                    $('#maPhong').val(data.maPhong);
                    const $maThietBiDropdown = $('#maThietBi');
                    $maThietBiDropdown.empty().append('<option value="">-- Chọn Thiết Bị --</option>');
                    $.each(data.maThietBiList, function(index, maThietBi) {
                        $maThietBiDropdown.append('<option value="' + maThietBi + '">' + maThietBi + '</option>');
                    });
                });
            } else {
                $('#maPhong').val('');
                $('#maThietBi').empty().append('<option value="">-- Chọn Thiết Bị --</option>');
            }
        });

        // Sự kiện khi Tình Trạng hoặc Mã Thiết Bị thay đổi
        $('#maThietBi, input[name="tinhTrang"]').change(function() {
            const tinhTrang = $('input[name="tinhTrang"]:checked').val();
            const maThietBi = $('#maThietBi').val();

            if (tinhTrang === 'HONG' && maThietBi) {
                // Chỉ gọi API một lần duy nhất khi thiết bị được chọn và tình trạng là hỏng
                $.get('/kiemtrachitiet/api/get-denbu', { maThietBi: maThietBi }, function(data) {
                    giaDenBuCoBan = parseFloat(data);
                    // Sau khi có giá, gọi hàm cập nhật tổng tiền
                    capNhatTongTien();
                });
            } else {
                giaDenBuCoBan = 0;
                capNhatTongTien();
            }
        });

        // Sự kiện khi Số Lượng Hỏng thay đổi (Sử dụng 'input' để cập nhật tức thì)
        $('#soLuongHong').on('input', function() {
            capNhatTongTien();
        });

        // Hàm riêng để tính toán và cập nhật tổng tiền, giúp mã rõ ràng hơn
        function capNhatTongTien() {
            const soLuongHong = $('#soLuongHong').val();
            const tongDenBu = giaDenBuCoBan * parseInt(soLuongHong);
            $('#denBu').val(tongDenBu);
        }
    });
</script>
</body>
</html>