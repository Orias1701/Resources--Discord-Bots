<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<script src="https://ajax.googleapis.com/ajax/libs/jquery/3.5.1/jquery.min.js"></script>
<script>
$(function () {
    function updateTien() {
        var selectedMaHoaDon = $('select[name="hoaDon.maHoaDon"]').val();
        if (selectedMaHoaDon) {
            $.ajax({
                url: '/hdct/getTienByMaHoaDon',
                data: {maHoaDon: selectedMaHoaDon},
                success: function (data) {
                    $('#tienDatPhongSpan').text(data.tienDatPhong);
                    $('#tienSddvSpan').text(data.tienSddv);
                    $('#tienKiemTraSpan').text(data.tienKiemTra);
                    $('#thanhTienSpan').text(data.thanhTien);
                }
            });
        }
    }
    $('select[name="hoaDon.maHoaDon"]').change(updateTien);
    updateTien();
});
</script>