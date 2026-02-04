<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<html>
<head>
    <title>In hóa đơn</title>
    <style>
        body { font-family: Arial, sans-serif; margin: 40px; color: #222; }
        h2 { text-align: center; margin-bottom: 10px; }
        h3 { text-align: center; margin-top: 0; color: #555; }
        table { width: 100%; border-collapse: collapse; margin-top: 20px; }
        th, td { border: 1px solid #ccc; padding: 8px; text-align: center; }
        th { background-color: #f2f2f2; }
        .info { margin: 10px 0; }
        .info strong { display: inline-block; width: 150px; }
        .total { text-align: right; font-size: 18px; font-weight: bold; margin-top: 15px; }
        .print-btn { text-align: right; margin-top: 20px; }
        .print-btn button { background: #5A3E2B; color: white; border: none; padding: 8px 16px; border-radius: 5px; cursor: pointer; }
        .print-btn button:hover { opacity:0.7 }
    </style>
</head>
<body>

    <h2>HÓA ĐƠN THANH TOÁN</h2>
    <h3>Mã hóa đơn: ${hoaDon.maHoaDon}</h3>

    <div class="info">
        <p><strong>Nhân viên:</strong> ${hoaDon.maNhanVien.tenNhanVien}</p>
        <p><strong>Khách hàng:</strong> ${hoaDon.khachHang.tenKhachHang}</p>
        <p><strong>Ngày lập:</strong> ${hoaDon.ngay}</p>
        <p><strong>Trạng thái:</strong> ${hoaDon.thanhToan}</p>
    </div>

    <table>
        <thead>
            <tr>
                <th>STT</th>
                <th>Mã Đặt Phòng</th>
                <th>Mã Sử Dụng DV</th>
                <th>Mã Kiểm Tra</th>
                <th>Tiền Đặt Phòng</th>
                <th>Tiền Dịch Vụ</th>
                <th>Tiền Kiểm Tra</th>
                <th>Thành Tiền</th>
            </tr>
        </thead>
        <tbody>
            <c:forEach var="ct" items="${chiTiet}" varStatus="st">
                <tr>
                    <td>${st.index + 1}</td>

                    <!-- Các quan hệ: DatPhong, SuDungDichVu, KiemTraPhong -->
                    <td><c:out value="${ct.datPhong != null ? ct.datPhong.maDatPhong : '-'}"/></td>
                    <td><c:out value="${ct.suDungDichVu != null ? ct.suDungDichVu.maSDDV : '-'}"/></td>
                    <td><c:out value="${ct.kiemTraPhong != null ? ct.kiemTraPhong.maKiemTra : '-'}"/></td>

                    <!-- Các trường số (theo entity) -->
                    <td><fmt:formatNumber value="${ct.tienDatPhong}" type="number" minFractionDigits="2"/> ₫</td>
                    <td><fmt:formatNumber value="${ct.tienSddv}" type="number" minFractionDigits="2"/> ₫</td>
                    <td><fmt:formatNumber value="${ct.tienKiemTra}" type="number" minFractionDigits="2"/> ₫</td>
                    <td><fmt:formatNumber value="${ct.thanhTien}" type="number" minFractionDigits="2"/> ₫</td>
                </tr>
            </c:forEach>
        </tbody>
    </table>

    <p class="total">
        Tổng cộng: <fmt:formatNumber value="${hoaDon.tongTien}" type="number" minFractionDigits="2"/> ₫
    </p>

    <div class="print-btn">
        <button  onclick="window.print()">🖨 In hóa đơn</button>
    </div>

</body>
</html>
