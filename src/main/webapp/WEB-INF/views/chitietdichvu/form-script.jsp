<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<script>
    function updateThanhTien() {
        const selectDV = document.getElementById("dichVuSelect");
        const soLuongInput = document.getElementById("soLuongInput");
        const giaInput = document.getElementById("giaDichVuInput");
        const thanhTienInput = document.getElementById("thanhTienInput");

        if (!selectDV || !soLuongInput || !giaInput || !thanhTienInput) return;

        const selectedOption = selectDV.options[selectDV.selectedIndex];
        const gia = parseFloat(selectedOption.dataset.gia) || 0;
        const soLuong = parseInt(soLuongInput.value) || 0;

        giaInput.value = gia;
        thanhTienInput.value = gia * soLuong;
    }

    document.addEventListener("DOMContentLoaded", function() {
        const selectDV = document.getElementById("dichVuSelect");
        if(selectDV) {
            selectDV.addEventListener("change", updateThanhTien);
            updateThanhTien();
        }
    });
</script>