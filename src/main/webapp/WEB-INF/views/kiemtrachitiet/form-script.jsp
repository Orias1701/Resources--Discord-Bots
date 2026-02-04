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
