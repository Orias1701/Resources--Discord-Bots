<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<div class="form-container">
    <h2>Dịch Vụ</h2>
    <form method="post" action="/dichvu/save">
        <div class="form-group">
            <label>Mã Dịch Vụ</label>
            <input type="text" name="maDichVu" value="${dichvu.maDichVu}" ${dichvu.maDichVu != null ? "readonly" : ""} />
        </div>
        <div class="form-group">
            <label>Tên Dịch Vụ</label>
            <input type="text" name="tenDichVu" value="${dichvu.tenDichVu}" />
        </div>
        <div class="form-group">
            <label>Loại Dịch Vụ</label>
            <input type="text" name="loaiDichVu" value="${dichvu.loaiDichVu}" />
        </div>
        <div class="form-group">
            <label>Giá Dịch Vụ</label>
            <input type="number" name="giaDichVu" value="${dichvu.giaDichVu}" />
        </div>
        <div class="form-actions">
            <a href="/dichvu" class="btn-secondary">Hủy</a>
            <button type="submit" class="btn-primary">Lưu</button>
        </div>
    </form>
</div>