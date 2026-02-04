<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<script>
    $(document).ready(function() {
        $('.tinhTrangSelect').change(function() {
            var maPhong = $(this).data('maphong');
            var tinhTrangMoi = $(this).val();
            $.ajax({
                url: '/phong/updateTinhTrang',
                method: 'POST',
                data: { maPhong: maPhong, tinhTrangPhong: tinhTrangMoi },
                success: function() {
                    console.log('Cập nhật thành công!');
                },
                error: function() {
                    alert('Cập nhật thất bại!');
                }
            });
        });
    });
</script>