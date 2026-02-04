<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<div class="form-container">
    <h2>Chi Tiết Kiểm Tra</h2>

    <!-- Xác định URL action -->
    <c:choose>
        <c:when test="${kiemTraChiTiet.id.maKiemTra == null}">
            <c:url var="formAction" value="/kiemtrachitiet/add"/>
        </c:when>
        <c:otherwise>
            <c:url var="formAction" value="/kiemtrachitiet/update"/>
        </c:otherwise>
    </c:choose>

    <form action="${formAction}" method="post">
        
        <!-- Nếu đang sửa thì hiển thị readonly -->
        <c:if test="${kiemTraChiTiet.id.maKiemTra != null}">
            <div class="form-group">
                <label>Mã Kiểm Tra</label>
                <input type="text" name="maKiemTra" value="${kiemTraChiTiet.id.maKiemTra}" readonly/>
            </div>
            <div class="form-group">
                <label>Mã Thiết Bị</label>
                <input type="text" id="maThietBi" name="maThietBi" value="${kiemTraChiTiet.id.maThietBi}" readonly/>
            </div>
            <div class="form-group">
                <label>Mã Phòng</label>
                <input type="text" name="maPhong" value="${kiemTraChiTiet.maPhong}" readonly/>
            </div>
        </c:if>

        <!-- Nếu đang thêm mới -->
        <c:if test="${kiemTraChiTiet.id.maKiemTra == null}">
            <div class="form-group">
                <label>Mã Kiểm Tra</label>
                <select id="maKiemTra" name="maKiemTra" required>
                    <option value="">-- Chọn Mã Kiểm Tra --</option>
                    <c:forEach var="kt" items="${kiemTraPhongs}">
                        <option value="${kt.maKiemTra}">${kt.maKiemTra}</option>
                    </c:forEach>
                </select>
            </div>
            <div class="form-group">
                <label>Mã Phòng (tự động)</label>
                <input type="text" id="maPhong" name="maPhong" readonly>
            </div>
            <div class="form-group">
                <label>Mã Thiết Bị</label>
                <select id="maThietBi" name="maThietBi" required>
                    <option value="">-- Chọn Thiết Bị --</option>
                </select>
            </div>
        </c:if>

        <div class="form-group">
            <label>Tình Trạng</label>
            <input type="radio" name="tinhTrang" value="TOT" ${kiemTraChiTiet.tinhTrang != null && kiemTraChiTiet.tinhTrang.name() == 'TOT' ? 'checked' : ''}> Tốt
            <input type="radio" name="tinhTrang" value="HONG" ${kiemTraChiTiet.tinhTrang != null && kiemTraChiTiet.tinhTrang.name() == 'HONG' ? 'checked' : ''}> Hỏng
        </div>

        <div class="form-group">
            <label>Số Lượng Hỏng</label>
            <input type="number" id="soLuongHong" name="soLuongHong" value="${kiemTraChiTiet.soLuongHong != null ? kiemTraChiTiet.soLuongHong : 0}" min="0" required>
        </div>

        <div class="form-group">
            <label>Tổng Đền Bù</label>
            <input type="text" id="denBu" name="denBu" value="${kiemTraChiTiet.denBu}" readonly>
        </div>

        <div class="form-group">
            <label>Ghi Chú</label>
            <textarea name="ghiChu">${kiemTraChiTiet.ghiChu}</textarea>
        </div>

        <div class="form-actions">
            <a href="<c:url value='/kiemtrachitiet' />" class="btn-secondary">Hủy</a>
            <button type="submit" class="btn-primary">Lưu</button>
        </div>
    </form>
</div>

<script>
    $(document).ready(function() {
        let giaDenBuCoBan = 0;

        // Khi chọn Mã Kiểm Tra
        $('#maKiemTra').change(function() {
            const maKiemTra = $(this).val();
            if (maKiemTra) {
                $.get('<c:url value="/kiemtrachitiet/api/get-thong-tin-theo-kiem-tra"/>', { maKiemTra: maKiemTra }, function(data) {
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

        // Khi chọn Tình Trạng hoặc Mã Thiết Bị
        $('#maThietBi, input[name="tinhTrang"]').change(function() {
            const tinhTrang = $('input[name="tinhTrang"]:checked').val();
            const maThietBi = $('#maThietBi').val();

            if (tinhTrang === 'HONG' && maThietBi) {
                $.get('<c:url value="/kiemtrachitiet/api/get-denbu"/>', { maThietBi: maThietBi }, function(data) {
                    giaDenBuCoBan = parseFloat(data);
                    capNhatTongTien();
                });
            } else {
                giaDenBuCoBan = 0;
                capNhatTongTien();
            }
        });

        // Khi thay đổi số lượng hỏng
        $('#soLuongHong').on('input', function() {
            capNhatTongTien();
        });

        function capNhatTongTien() {
            const soLuongHong = $('#soLuongHong').val();
            const tongDenBu = giaDenBuCoBan * parseInt(soLuongHong || 0);
            $('#denBu').val(tongDenBu);
        }
    });
</script>
