<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<div class="form-container">
    <h2>Thiết Bị Trong Phòng</h2>
    <form method="post" action="thietbiphong/save">
        <input type="hidden" name="oldMaPhong" value="${oldMaPhong}"/>
        <input type="hidden" name="oldMaThietBi" value="${oldMaThietBi}"/>

        <div class="form-group">
            <label>Phòng</label>
            <select name="maPhong" required>
                <option value="">-- Chọn phòng --</option>
                <c:forEach var="p" items="${listPhong}">
                    <option value="${p.maPhong}" <c:if test="${tbp.phong.maPhong eq p.maPhong}">selected</c:if>>
                        ${p.maPhong} - ${p.tenPhong}
                    </option>
                </c:forEach>
            </select>
        </div>

        <div class="form-group">
            <label>Thiết bị</label>
            <select name="maThietBi" required>
                <option value="">-- Chọn thiết bị --</option>
                <c:forEach var="t" items="${listThietBi}">
                    <option value="${t.maThietBi}" <c:if test="${tbp.thietBi.maThietBi eq t.maThietBi}">selected</c:if>>
                        ${t.maThietBi} - ${t.tenThietBi}
                    </option>
                </c:forEach>
            </select>
        </div>
        <div class="form-group">
            <label>Trạng thái:</label>
            <select name="trangThai" required>
            <c:forEach var="st" items="${allTrangThais}">
                <option value="${st}" <c:if test="${tbp.trangThai eq st}">selected</c:if>>${st}</option>
            </c:forEach>
            </select>
        </div>

        <div class="form-group">
            <label>Số lượng</label>
            <input type="number" min="0" name="soLuong" value="${tbp.soLuong != null ? tbp.soLuong : 0}" required/>
        </div>

        <div class="form-actions">
             <a href="/thietbiphong" class="btn-secondary">Hủy</a>
            <button type="submit" class="btn-primary">Lưu</button>
        </div>
    </form>
</div>