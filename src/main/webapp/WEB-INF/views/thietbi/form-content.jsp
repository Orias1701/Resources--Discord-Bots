<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<div class="form-container">
    <h2>Thiết Bị</h2>
    <form method="post" action="/thietbi/save">
        <div class="form-group">
            <label>Mã Thiết Bị</label>
            <input type="text" name="maThietBi" value="${tb.maThietBi}" ${tb.maThietBi != null ? "readonly" : ""} />
        </div>
        <div class="form-group">
            <label>Tên Thiết Bị</label>
            <input type="text" name="tenThietBi" value="${tb.tenThietBi}" />
        </div>
        <div class="form-group">
            <label>Đơn Giá</label>
            <input type="number" name="donGia" value="${tb.donGia}" />
        </div>
        <div class="form-group">
            <label>Giá Đền Bù</label>
            <input type="number" name="denBu" value="${tb.denBu}" />
        </div>
        <div class="form-actions">
            <a href="/thietbi" class="btn-secondary">Hủy</a>
            <button type="submit" class="btn-primary">Lưu</button>
        </div>
    </form>
</div>