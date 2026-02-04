<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<div class="form-container">
    <h2>Hóa Đơn Chi Tiết</h2>
    <c:if test="${not empty error}"><div class="error">${error}</div></c:if>

    <form method="post" action="/hdct/save">
        <c:if test="${hdct.id != null}">
            <input type="hidden" name="id" value="${hdct.id}"/>
        </c:if>

        <div class="form-group">
            <label>Mã Hóa Đơn:</label>
            <select id="maHoaDonSelect" name="hoaDon.maHoaDon" required>
                <c:forEach var="h" items="${hoaDonList}">
                <option value="${h.maHoaDon}"
                    <c:if test="${hdct.hoaDon != null && hdct.hoaDon.maHoaDon == h.maHoaDon}">selected</c:if>>
                    ${h.maHoaDon}
                </option>
                </c:forEach>
            </select>
        </div>

        <c:set var="hasEligible" value="${not empty eligible and not empty eligible.maKhachHang}" />

        <div class="form-group">
            <label>Mã Đặt Phòng:</label>
            <select id="maDatPhong" name="datPhong.maDatPhong" <c:if test="${not hasEligible}">disabled</c:if>>
                <option value="">-- Không chọn --</option>
                <c:forEach var="dp" items="${datPhongList}">
                <option value="${dp.maDatPhong}" data-kt="${dp.maKiemTra}"
                    <c:if test="${hdct.datPhong != null && hdct.datPhong.maDatPhong == dp.maDatPhong}">selected</c:if>>
                    ${dp.moTa}
                </option>
                </c:forEach>
            </select>
            <small>KT tương ứng: <span id="ktView">—</span></small>
        </div>

        <div class="form-group">
            <label>Mã SDDV:</label>
            <select id="maSDDV" name="suDungDichVu.maSDDV" <c:if test="${not hasEligible}">disabled</c:if>>
                <option value="">-- Không chọn --</option>
                <c:forEach var="s" items="${sddvList}">
                <option value="${s.value}"
                    <c:if test="${hdct.suDungDichVu != null && hdct.suDungDichVu.maSDDV == s.value}">selected</c:if>>
                    ${s.label}
                </option>
                </c:forEach>
            </select>
        </div>

        <!-- Ẩn chọn KT, tự “ứng” theo ĐP -->
        <input type="hidden" id="maKiemTra" name="kiemTraPhong.maKiemTra"
            value="${hdct.kiemTraPhong != null ? hdct.kiemTraPhong.maKiemTra : ''}" />



        <%-- <div class="form-group">
            <label>Mã Kiểm Tra:</label>
            <input type="hidden" name="kiemTraPhong.maKiemTra" id="maKiemTra"
                value="${hdct.kiemTraPhong != null ? hdct.kiemTraPhong.maKiemTra : ''}"/>
            <span id="ktDisplay">${hdct.kiemTraPhong != null ? hdct.kiemTraPhong.maKiemTra : ''}</span>
        </div> --%>

        <div class="form-group">
            <label>Trạng thái thanh toán:</label>
            <!-- HDCT được phép đổi trạng thái -->
            <select name="thanhToan">
            <option value="">-- Không chọn --</option>
            <c:forEach var="tt" items="${thanhToanOptions}">
                <option value="${tt}" <c:if test="${hdct.thanhToan == tt}">selected</c:if>>${tt}</option>
            </c:forEach>
            </select>
        </div>


        <fieldset>
            <legend>Thông tin tính tiền (tự động)</legend>
            <div class="form-group">
                <label>Tiền đặt phòng:</label>
                <span id="tienDatPhongSpan">${hdct.tienDatPhong != null ? hdct.tienDatPhong : '0'}</span>
            </div>
            <div class="form-group">
                <label>Tiền SDDV:</label>
                <span id="tienSddvSpan">${hdct.tienSddv != null ? hdct.tienSddv : '0'}</span>
            </div>
            <div class="form-group">
                <label>Tiền kiểm tra:</label>
                <span id="tienKiemTraSpan">${hdct.tienKiemTra != null ? hdct.tienKiemTra : '0'}</span>
            </div>
             <div class="form-group">
                <label>Thành tiền:</label>
                <span id="thanhTienSpan">${hdct.thanhTien != null ? hdct.thanhTien : '0'}</span>
            </div>
        </fieldset>

        <div class="form-actions">
            <a href="/hdct" class="btn-secondary">Hủy</a>
            <button type="submit" class="btn-primary">Lưu</button>
        </div>
    </form>
</div>
<script>
(function () {
  const $hd  = document.getElementById('maHoaDonSelect');
  const $dp  = document.getElementById('maDatPhong');
  const $dv  = document.getElementById('maSDDV');
  const $ktHidden = document.getElementById('maKiemTra');
  const $ktView = document.getElementById('ktView');

  const $tDP = document.getElementById('tienDatPhongSpan');
  const $tDV = document.getElementById('tienSddvSpan');
  const $tKT = document.getElementById('tienKiemTraSpan');
  const $sum = document.getElementById('thanhTienSpan');

  function fmt(x){ try{ const n=Number(x); return Number.isFinite(n)? n.toFixed(2): String(x??''); }catch{ return String(x??''); } }

  function applyKiemTraFromDatPhong() {
    if (!$dp) return;
    const opt = $dp.options[$dp.selectedIndex];
    const maKt = opt ? (opt.getAttribute('data-kt') || '') : '';
    if ($ktHidden) $ktHidden.value = maKt;
    if ($ktView)   $ktView.textContent = maKt && maKt.length ? maKt : '—';
  }

  async function refreshTotals() {
    const qs = new URLSearchParams();
    if ($dp && !$dp.disabled && $dp.value) qs.set('maDatPhong', $dp.value);
    if ($dv && !$dv.disabled && $dv.value) qs.set('maSDDV', $dv.value);
    if ($ktHidden && $ktHidden.value)      qs.set('maKiemTra', $ktHidden.value);

    const res = await fetch('/hdct/api/preview?' + qs.toString(), { headers: { 'X-Requested-With': 'XMLHttpRequest' } });
    if (!res.ok) return;
    const data = await res.json();

    if ($tDP) $tDP.textContent = fmt(data.tienDatPhong);
    if ($tDV) $tDV.textContent = fmt(data.tienSddv);
    if ($tKT) $tKT.textContent = fmt(data.tienKiemTra);
    if ($sum) $sum.textContent = fmt(data.thanhTien);
  }

  function clearSelect(el) {
    if (!el) return;
    el.options.length = 0;
    el.add(new Option('-- Không chọn --', ''));
  }

  function rebuildSelect(el, list, getVal, getLabel, prev) {
    clearSelect(el);
    if (!Array.isArray(list) || list.length === 0) {
      el.disabled = true;
      el.value = '';
      return '';
    }
    el.disabled = false;

    const values = [];
    for (const item of list) {
      const v = String(getVal(item) ?? '');
      const label = String(getLabel(item) ?? v);
      el.add(new Option(label, v));
      values.push(v);
    }

    if (prev && values.includes(String(prev))) {
      el.value = String(prev);
      return String(prev);
    } else if (list.length === 1) {
      const only = String(getVal(list[0]) ?? '');
      el.value = only;
      return only;
    } else {
      el.value = '';
      return '';
    }
  }

  async function reloadEligible() {
    // Nếu chưa chọn HĐ -> clear + disable con
    if (!$hd || !$hd.value) {
      clearSelect($dp); if ($dp) $dp.disabled = true;
      clearSelect($dv); if ($dv) $dv.disabled = true;
      applyKiemTraFromDatPhong();
      refreshTotals();
      return;
    }

    const url = '/hdct/api/eligible-children?maHoaDon=' + encodeURIComponent($hd.value);
    const res = await fetch(url, { headers: { 'X-Requested-With': 'XMLHttpRequest' } });
    if (!res.ok) return;

    // Một số server trả text/plain -> fallback parse text
    let eg;
    try { eg = await res.json(); }
    catch { eg = JSON.parse(await res.text()); }

    // --- Đổ ĐẶT PHÒNG ---
    const prevDp = $dp ? $dp.value : '';
    const dpList = Array.isArray(eg.datPhongOptions) ? eg.datPhongOptions : [];
    let pickedDp = '';
    if ($dp) {
      pickedDp = rebuildSelect(
        $dp,
        dpList,
        x => x.maDatPhong,
        x => (x.moTa ?? x.maDatPhong),
        prevDp
      );

      // Gắn data-kt lên từng option theo map maDatPhong -> maKiemTra
      const ktMap = new Map(dpList.map(x => [String(x.maDatPhong), String(x.maKiemTra || '')]));
      for (let i = 0; i < $dp.options.length; i++) {
        const opt = $dp.options[i];
        const v = opt.value;
        if (v) opt.setAttribute('data-kt', ktMap.get(v) || '');
      }
    }

    // --- Đổ SDDV ---
    const prevDv = $dv ? $dv.value : '';
    const dvList = Array.isArray(eg.sddvOptions) ? eg.sddvOptions : [];
    let pickedDv = '';
    if ($dv) {
      pickedDv = rebuildSelect(
        $dv,
        dvList,
        x => x.value,
        x => (x.label ?? x.value),
        prevDv
      );
    }

    applyKiemTraFromDatPhong();
    refreshTotals();
  }

  // Events
  if ($hd) $hd.addEventListener('change', reloadEligible);
  if ($dp) $dp.addEventListener('change', () => { applyKiemTraFromDatPhong(); refreshTotals(); });
  if ($dv) $dv.addEventListener('change', refreshTotals);

  // Init
  applyKiemTraFromDatPhong();
  refreshTotals();
  if ($hd && $hd.value) reloadEligible(); // có sẵn HĐ thì đổ DS con ngay
})();
</script>


