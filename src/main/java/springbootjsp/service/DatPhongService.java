package springbootjsp.service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import jakarta.transaction.Transactional;
import springbootjsp.model.DatPhong;
import springbootjsp.model.HoaDonChiTiet;
import springbootjsp.model.KhachHang;
import springbootjsp.model.KhachHang.TinhTrangKhach;
import springbootjsp.model.Phong;
import springbootjsp.model.Phong.TinhTrangPhong;
import springbootjsp.model.ThanhToan;
import springbootjsp.repository.DatPhongRepository;
import springbootjsp.repository.HoaDonChiTietRepository;
import springbootjsp.repository.KhachHangRepository;
import springbootjsp.repository.NhanVienRepository;
import springbootjsp.repository.PhongRepository;

@Service
@Transactional
public class DatPhongService {

    private final DatPhongRepository repository;
    private final PhongRepository phongRepository;
    private final KhachHangRepository khachHangRepository;
    private final NhanVienRepository nhanVienRepository;
    private final HoaDonChiTietRepository hdctRepository;

    public DatPhongService(KhachHangRepository khachHangRepository, NhanVienRepository nhanVienRepository, PhongRepository phongRepository, DatPhongRepository repository,
                           HoaDonChiTietRepository hdctRepository) {
        this.khachHangRepository = khachHangRepository;
        this.nhanVienRepository = nhanVienRepository;
        this.phongRepository = phongRepository;
        this.repository = repository;
        this.hdctRepository = hdctRepository;
    }

    public NhanVienRepository getNhanVienRepository() { return nhanVienRepository; }
    public KhachHangRepository getKhachHangRepository() { return khachHangRepository; }
    public PhongRepository getPhongRepository() { return phongRepository; }
    public DatPhongRepository getRepository() { return repository; }

    private static BigDecimal z(BigDecimal v) { return v == null ? BigDecimal.ZERO : v; }

    /** Chặn sửa/xoá nếu đơn đã ĐÃ THANH TOÁN */
    private void assertMutable(DatPhong dp) {
        if (dp != null && dp.getThanhToan() == ThanhToan.DA_THANH_TOAN) {
            throw new IllegalStateException("Đặt phòng " + dp.getMaDatPhong() + " đã ĐÃ THANH TOÁN - không được sửa/xoá.");
        }
    }

    // ===== CRUD =====

    /** Thêm hoặc cập nhật đặt phòng. 
     *  - Nếu bản hiện tại đã DA_THANH_TOAN => KHÔNG cho sửa.
     *  - Nếu sau khi lưu mà thanhToan == DA_THANH_TOAN => tự chuyển tình trạng về DA_TRA và đồng bộ phòng + khách.
     *  - Không cố sync tiền sang HDCT khi đã chốt (tránh ném lỗi).
     */
    public DatPhong save(DatPhong dp) {
        // Phát sinh mã nếu chưa có
        if (dp.getMaDatPhong() == null || dp.getMaDatPhong().isEmpty()) {
            dp.setMaDatPhong(generateNextMaDatPhong());
        }

        // Lấy bản hiện tại (để biết phòng cũ & kiểm tra quyền sửa)
        Phong oldPhong = null;
        DatPhong current = null;
        if (dp.getMaDatPhong() != null) {
            current = repository.findById(dp.getMaDatPhong()).orElse(null);
            if (current != null) {
                oldPhong = current.getMaPhong();
                // 🔒 Khoá sửa nếu đơn đã thanh toán
                assertMutable(current);
            }
        }

        // Chuẩn hoá/attach entity liên quan
        if (dp.getMaKhachHang() != null && dp.getMaKhachHang().getMaKhachHang() != null) {
            String maKhach = dp.getMaKhachHang().getMaKhachHang();
            dp.setMaKhachHang(khachHangRepository.findById(maKhach).orElse(null));
        }
        if (dp.getMaNhanVien() != null && dp.getMaNhanVien().getMaNhanVien() != null) {
            String maNV = dp.getMaNhanVien().getMaNhanVien();
            dp.setMaNhanVien(nhanVienRepository.findById(maNV).orElse(null));
        }
        if (dp.getMaPhong() != null && dp.getMaPhong().getMaPhong() != null) {
            String maPhong = dp.getMaPhong().getMaPhong();
            dp.setMaPhong(phongRepository.findById(maPhong).orElse(dp.getMaPhong()));
        }

        // Validate cơ bản
        if (!checkBookingValidity(dp)) {
            throw new IllegalArgumentException("Dữ liệu đặt phòng không hợp lệ.");
        }
        String excludeId = (dp.getMaDatPhong() != null && !dp.getMaDatPhong().isBlank()) ? dp.getMaDatPhong() : null;

        if (dp.getMaKhachHang() != null && dp.getMaKhachHang().getMaKhachHang() != null) {
            String maKH = dp.getMaKhachHang().getMaKhachHang();

            long unpaid = repository.countUnpaidByCustomer(maKH, excludeId);

            // Nếu đã có >= 2 booking CHUA_THANH_TOAN thì chặn tạo/sửa thêm bản thứ 3
            if (unpaid >= 2) {
                throw new IllegalStateException(
                    "Khách hàng đã có 2 đặt phòng CHƯA THANH TOÁN. Vui lòng thanh toán hoặc trả phòng trước khi đặt thêm."
                );
            }
        }
        // CHẶN XUNG ĐỘT
        if (hasConflict(dp)) {
            throw new IllegalStateException("Phòng đã có đơn trùng thời gian. Vui lòng chọn phòng hoặc thời gian khác.");
        }

        // Tính phạt & tổng tiền
        calculatePenalty(dp);
        getTotalAmount(dp);

        // Lưu đơn
        DatPhong saved = repository.save(dp);

        // Chỉ sync tiền sang HDCT nếu đơn chưa chốt
        if (saved.getThanhToan() != ThanhToan.DA_THANH_TOAN) {
            try {
                syncTongTien(saved.getMaDatPhong());
            } catch (IllegalStateException ignore) {
                // bỏ qua nếu HDCT chốt
            }
        }

        // Đồng bộ trạng thái KHÁCH & PHÒNG theo đơn (cho UI realtime)
        syncWithCustomer(saved);
        syncWithRoom(saved);

        // Nếu ĐỔI PHÒNG: ÉP phòng cũ về TRỐNG ngay
        if (oldPhong != null && saved.getMaPhong() != null) {
            String oldId = oldPhong.getMaPhong();
            String newId = saved.getMaPhong().getMaPhong();
            if (!Objects.equals(oldId, newId)) {
                forceReleaseRoom(oldPhong);
            }
        }

        // ✅ Nếu đánh dấu thanh toán ngay trên Đặt phòng: tự chốt đơn → DA_TRA + đồng bộ
        if (saved.getThanhToan() == ThanhToan.DA_THANH_TOAN) {
            danhDauDaTra(saved.getMaDatPhong());
            saved = repository.findById(saved.getMaDatPhong()).orElse(saved);
        }

        return saved;
    }

    /** Xóa đặt phòng theo mã — bị khoá nếu đã thanh toán */
    public void deleteById(String maDatPhong) {
        DatPhong dp = repository.findById(maDatPhong).orElse(null);
        if (dp == null) return;

        // ❌ Không cho xoá nếu đã thanh toán
        if (dp.getThanhToan() == ThanhToan.DA_THANH_TOAN) {
            throw new IllegalStateException("Không thể xoá vì đơn đã thanh toán.");
        }

        // ❌ Không cho xoá nếu còn hóa đơn chi tiết tham chiếu
        List<HoaDonChiTiet> hdcts = hdctRepository.findByDatPhong_MaDatPhong(maDatPhong);
        if (hdcts != null && !hdcts.isEmpty()) {
            throw new IllegalStateException("Không thể xoá vì còn hóa đơn chi tiết tham chiếu!");
        }

        // ✅ Cho phép xoá
        String maPhong = (dp.getMaPhong() != null) ? dp.getMaPhong().getMaPhong() : null;
        repository.deleteById(maDatPhong);

        if (maPhong != null) {
            recalcRoomState(maPhong);
        }
    }



    public DatPhong findById(String maDatPhong) { return repository.findById(maDatPhong).orElse(null); }
    public Iterable<DatPhong> findAll() { return repository.findAll(); }

    public String generateNextMaDatPhong() {
        String maxMa = repository.findMaxMaDatPhong();
        if (maxMa == null || maxMa.length() < 3) return "DP001";
        String numberPart = maxMa.substring(2); // DP001 -> 001
        int num = Integer.parseInt(numberPart);
        num++;
        return String.format("DP%03d", num); // DP002, DP003, ...
    }

    // ===== Nghiệp vụ =====

    public boolean checkBookingValidity(DatPhong dp) {
        LocalDateTime nhanPhong = dp.getNgayNhanPhong();
        LocalDateTime traPhong = dp.getNgayTraPhong();
        LocalDateTime henTra = dp.getNgayHenTra();
        if (nhanPhong == null || henTra == null) return false;
        if (traPhong != null && traPhong.isBefore(nhanPhong)) return false;
        if (traPhong != null && traPhong.isBefore(henTra)) return false;
        if (nhanPhong.isAfter(henTra)) return false;
        if (nhanPhong.isAfter(LocalDateTime.now())) return false;
        return true;
    }

    public void calculatePenalty(DatPhong dp) {
        if (dp == null) return;

        LocalDateTime henTra   = dp.getNgayHenTra();
        LocalDateTime traPhong = dp.getNgayTraPhong();

        if (henTra == null || traPhong == null || dp.getMaPhong() == null) {
            dp.setTienPhat(BigDecimal.ZERO);
            return;
        }

        LocalDateTime muon = henTra.plusMinutes(30);
        BigDecimal penalty = BigDecimal.ZERO;

        String maPhong = dp.getMaPhong().getMaPhong();
        if (maPhong != null) {
            BigDecimal donGia = phongRepository.findGiaLoaiByMaPhong(maPhong);
            if (donGia != null && traPhong.isAfter(muon)) {
                double hoursLate = Duration.between(muon, traPhong).toMinutes() / 60.0;
                penalty = donGia.multiply(BigDecimal.valueOf(hoursLate))
                                .multiply(BigDecimal.valueOf(0.3));
            }
        }
        dp.setTienPhat(penalty);
    }

    /** Tính tổng tiền đặt phòng: làm tròn theo ngày (ceil), tối thiểu 1 ngày. */
    public void getTotalAmount(DatPhong dp){
        if (dp == null || dp.getMaPhong() == null || dp.getMaPhong().getMaPhong() == null) {
            return;
        }
        BigDecimal donGia = phongRepository.findGiaLoaiByMaPhong(dp.getMaPhong().getMaPhong());
        if (donGia == null) donGia = BigDecimal.ZERO;

        LocalDateTime start = dp.getNgayNhanPhong();
        if (start == null) {
            // Chưa nhận phòng: chỉ tính tiền phạt nếu có
            dp.setTongTien(dp.getTienPhat() != null ? dp.getTienPhat() : BigDecimal.ZERO);
            return;
        }

        LocalDateTime end = (dp.getNgayTraPhong() != null) ? dp.getNgayTraPhong() : LocalDateTime.now();

        // --- NEW: làm tròn ngày ---
        long minutes = Math.max(0, Duration.between(start, end).toMinutes());
        long chargedDays = (minutes <= 0) ? 0 : (long) Math.ceil(minutes / (24.0 * 60.0));
        if (chargedDays == 0) chargedDays = 1; // ở > 0 phút vẫn tính tối thiểu 1 ngày

        BigDecimal tienDaO = donGia.multiply(BigDecimal.valueOf(chargedDays));

        BigDecimal tienPhat = dp.getTienPhat() != null ? dp.getTienPhat() : BigDecimal.ZERO;
        dp.setTongTien(tienDaO.add(tienPhat).setScale(2, RoundingMode.HALF_UP));
    }


    private static boolean before(LocalDateTime a, LocalDateTime b) { return a != null && b != null && a.isBefore(b); }
    private static boolean after(LocalDateTime a, LocalDateTime b)  { return a != null && b != null && a.isAfter(b); }

    public void syncWithCustomer(DatPhong dp) {
        if (dp == null || dp.getMaKhachHang() == null) return;

        KhachHang kh = dp.getMaKhachHang();

        final LocalDateTime nhanPhong = dp.getNgayNhanPhong();
        final LocalDateTime henTra    = dp.getNgayHenTra();
        final LocalDateTime traPhong  = dp.getNgayTraPhong();
        final LocalDateTime thoiGianHuy = (nhanPhong == null) ? null : nhanPhong.minusHours(5);

        if (dp.getCachDat() == DatPhong.CachDat.DAT_TRUC_TIEP) {
            switch (dp.getTinhTrang()) {
                case DANG_SU_DUNG -> kh.setTinhTrangKhach(TinhTrangKhach.DANG_O);
                case QUA_HAN      -> kh.setTinhTrangKhach(TinhTrangKhach.DANG_O);
                case DANG_DOI     -> kh.setTinhTrangKhach(TinhTrangKhach.DA_DAT);
                case DA_TRA       -> kh.setTinhTrangKhach(TinhTrangKhach.DA_ROI);
            }
            return;
        }

        // ĐẶT ONLINE
        switch (dp.getTinhTrang()) {
            case DANG_DOI -> {
                if (before(traPhong, thoiGianHuy)) {
                    kh.setTinhTrangKhach(TinhTrangKhach.DA_ROI);
                } else if (before(traPhong, nhanPhong)) {
                    kh.setTinhTrangKhach(TinhTrangKhach.DA_DAT);
                } else if (after(traPhong, nhanPhong) && before(traPhong, henTra)) {
                    dp.setTinhTrang(DatPhong.TinhTrang.DANG_SU_DUNG);
                    kh.setTinhTrangKhach(TinhTrangKhach.DANG_O);
                } else if (after(traPhong, henTra)) {
                    dp.setTinhTrang(DatPhong.TinhTrang.QUA_HAN);
                    kh.setTinhTrangKhach(TinhTrangKhach.DA_ROI);
                } else {
                    kh.setTinhTrangKhach(TinhTrangKhach.DA_DAT);
                }
            }

            case DANG_SU_DUNG -> {
                if (after(traPhong, henTra)) {
                    dp.setTinhTrang(DatPhong.TinhTrang.QUA_HAN);
                    kh.setTinhTrangKhach(TinhTrangKhach.DA_ROI);
                    calculatePenalty(dp);
                } else {
                    kh.setTinhTrangKhach(TinhTrangKhach.DANG_O);
                }
            }

            case QUA_HAN -> {
                kh.setTinhTrangKhach(TinhTrangKhach.DA_ROI);
                calculatePenalty(dp);
            }

            case DA_TRA -> {
                kh.setTinhTrangKhach(TinhTrangKhach.DA_ROI);
            }
        }
    }

    public void syncWithRoom(DatPhong dp) {
        if (dp == null || dp.getMaPhong() == null) return;

        Phong phong = dp.getMaPhong();

        final LocalDateTime nhanPhong = dp.getNgayNhanPhong();
        final LocalDateTime henTra    = dp.getNgayHenTra();
        final LocalDateTime traPhong  = dp.getNgayTraPhong();
        final LocalDateTime thoiGianHuy = (nhanPhong == null) ? null : nhanPhong.minusHours(5);

        if (dp.getCachDat() == DatPhong.CachDat.DAT_TRUC_TIEP) {
            switch (dp.getTinhTrang()) {
                case DANG_SU_DUNG -> phong.setTinhTrangPhong(TinhTrangPhong.DANG_SU_DUNG);
                case QUA_HAN      -> phong.setTinhTrangPhong(TinhTrangPhong.DANG_SU_DUNG);
                case DANG_DOI     -> phong.setTinhTrangPhong(TinhTrangPhong.DA_DAT);
                case DA_TRA       -> phong.setTinhTrangPhong(TinhTrangPhong.TRONG);
            }
            return;
        }

        // ĐẶT ONLINE
        switch (dp.getTinhTrang()) {
            case DANG_DOI -> {
                if (before(traPhong, thoiGianHuy)) {
                    phong.setTinhTrangPhong(TinhTrangPhong.TRONG);
                } else if (before(traPhong, nhanPhong)) {
                    phong.setTinhTrangPhong(TinhTrangPhong.DA_DAT);
                } else if (after(traPhong, nhanPhong) && before(traPhong, henTra)) {
                    dp.setTinhTrang(DatPhong.TinhTrang.DANG_SU_DUNG);
                    phong.setTinhTrangPhong(TinhTrangPhong.DANG_SU_DUNG);
                } else if (after(traPhong, henTra)) {
                    dp.setTinhTrang(DatPhong.TinhTrang.QUA_HAN);
                    phong.setTinhTrangPhong(TinhTrangPhong.TRONG);
                    calculatePenalty(dp);
                } else {
                    phong.setTinhTrangPhong(TinhTrangPhong.DA_DAT);
                }
            }

            case DANG_SU_DUNG -> {
                if (after(traPhong, henTra)) {
                    dp.setTinhTrang(DatPhong.TinhTrang.QUA_HAN);
                    phong.setTinhTrangPhong(TinhTrangPhong.TRONG);
                    calculatePenalty(dp);
                } else {
                    phong.setTinhTrangPhong(TinhTrangPhong.DANG_SU_DUNG);
                }
            }

            case QUA_HAN, DA_TRA -> {
                phong.setTinhTrangPhong(TinhTrangPhong.TRONG);
            }
        }
    }

    // Tính & trả về Tổng tiền
    public BigDecimal tinhTongTien(DatPhong dp) {
        if (dp == null) return BigDecimal.ZERO;
        try { calculatePenalty(dp); } catch (Exception ignore) { dp.setTienPhat(BigDecimal.ZERO); }
        getTotalAmount(dp);
        return dp.getTongTien() != null ? dp.getTongTien().setScale(2, RoundingMode.HALF_UP) : BigDecimal.ZERO;
    }

    // Overload: nhận mã đặt phòng
    public BigDecimal tinhTongTienById(String maDatPhong) {
        DatPhong dp = findById(maDatPhong);
        return tinhTongTien(dp);
    }

    @Transactional(Transactional.TxType.SUPPORTS)
    public List<DatPhong> search(
            String keyword,
            String maNhanVien,
            String maKhachHang,
            String maPhong,
            String tinhTrang,
            BigDecimal minTienPhat,
            BigDecimal maxTienPhat,
            LocalDateTime fromDate,
            LocalDateTime toDate
    ) {
        String kw = (keyword != null && !keyword.isBlank()) ? keyword.trim() : null;
        String nv = (maNhanVien != null && !maNhanVien.isBlank()) ? maNhanVien.trim() : null;
        String kh = (maKhachHang != null && !maKhachHang.isBlank()) ? maKhachHang.trim() : null;
        String mp = (maPhong != null && !maPhong.isBlank()) ? maPhong.trim() : null;

        List<DatPhong.TinhTrang> statuses = new ArrayList<>();
        if (tinhTrang != null && !tinhTrang.isBlank()) {
            try { statuses.add(DatPhong.TinhTrang.valueOf(tinhTrang.trim())); } catch (IllegalArgumentException ignored) { }
        }
        boolean hasStatuses = !statuses.isEmpty();

        boolean onlyOverdue = false;
        List<DatPhong> base = repository.search(kw, statuses, hasStatuses, onlyOverdue);

        return base.stream()
                .filter(dp -> nv == null || (dp.getMaNhanVien() != null && dp.getMaNhanVien().equals(nv)))
                .filter(dp -> kh == null || (dp.getMaKhachHang() != null
                        && Objects.equals(dp.getMaKhachHang().getMaKhachHang(), kh)))
                .filter(dp -> mp == null || (dp.getMaPhong() != null
                        && Objects.equals(dp.getMaPhong().getMaPhong(), mp)))
                .filter(dp -> fromDate == null || (dp.getNgayNhanPhong() != null && !dp.getNgayNhanPhong().isBefore(fromDate)))
                .filter(dp -> toDate   == null || (dp.getNgayNhanPhong() != null && !dp.getNgayNhanPhong().isAfter(toDate)))
                .filter(dp -> {
                    BigDecimal phat = dp.getTienPhat();
                    if (minTienPhat != null && (phat == null || phat.compareTo(minTienPhat) < 0)) return false;
                    if (maxTienPhat != null && (phat != null && phat.compareTo(maxTienPhat) > 0)) return false;
                    return true;
                })
                .collect(Collectors.toList());
    }

    private boolean hasConflict(DatPhong dp) {
        if (dp == null || dp.getMaPhong() == null || dp.getMaPhong().getMaPhong() == null) return false;
        LocalDateTime start = dp.getNgayNhanPhong();
        LocalDateTime end   = dp.getNgayTraPhong() != null ? dp.getNgayTraPhong()
                : (dp.getNgayHenTra() != null ? dp.getNgayHenTra() : LocalDateTime.now());
        if (start == null || end == null) return false;

        List<DatPhong.TinhTrang> active = java.util.Arrays.asList(
                DatPhong.TinhTrang.DANG_DOI, DatPhong.TinhTrang.DANG_SU_DUNG
        );
        String excludeId = dp.getMaDatPhong();
        String maPhong   = dp.getMaPhong().getMaPhong();

        List<DatPhong> conflicts = repository.findConflictsForRoom(maPhong, start, end, excludeId, active);
        return !conflicts.isEmpty();
    }

    private void forceReleaseRoom(Phong room) {
        if (room == null) return;
        room.setTinhTrangPhong(TinhTrangPhong.TRONG);
        phongRepository.save(room);
    }

    /** Đổi trạng thái đơn đặt phòng & đồng bộ hiển thị phụ */
    public void updateStatus(String maDatPhong, DatPhong.TinhTrang newStatus) {
        DatPhong dp = findById(maDatPhong);
        if (dp == null) return;

        // Không cho sửa trạng thái nếu đơn đã thanh toán
        assertMutable(dp);

        dp.setTinhTrang(newStatus);
        if (newStatus == DatPhong.TinhTrang.DA_TRA && dp.getNgayTraPhong() == null) {
            dp.setNgayTraPhong(LocalDateTime.now());
        }

        calculatePenalty(dp);
        getTotalAmount(dp);

        dp = repository.save(dp);

        if (newStatus == DatPhong.TinhTrang.DA_TRA) {
            forceReleaseRoom(dp.getMaPhong());
        }

        syncWithCustomer(dp);
        syncWithRoom(dp);
        repository.save(dp);
    }

    /** Đồng bộ tiền đặt phòng → HDCT (bỏ qua bản HDCT đã chốt) */
    @Transactional
    public void syncTongTien(String maDatPhong) {
        if (maDatPhong == null || maDatPhong.isBlank()) return;

        var dp = repository.findById(maDatPhong).orElse(null);
        if (dp == null) return;

        BigDecimal tong = z(dp.getTongTien());

        List<HoaDonChiTiet> hdcts = hdctRepository.findByDatPhong_MaDatPhong(maDatPhong);
        if (hdcts == null || hdcts.isEmpty()) return;

        for (HoaDonChiTiet hdct : hdcts) {
            if (hdct.getThanhToan() == ThanhToan.DA_THANH_TOAN) {
                // Rule hiện tại: chặn cập nhật nếu HĐCT đã chốt
                throw new IllegalStateException("HDCT đã ĐÃ THANH TOÁN - không được cập nhật tiền Đặt phòng.");
                // hoặc: continue; // nếu bạn muốn bỏ qua thay vì chặn toàn bộ
            }

            hdct.setDatPhong(dp);
            hdct.setTienDatPhong(tong);

            BigDecimal sum = z(hdct.getTienDatPhong())
                    .add(z(hdct.getTienKiemTra()))
                    .add(z(hdct.getTienSddv()));
            hdct.setThanhTien(sum);
        }

        hdctRepository.saveAll(hdcts);
    }

    // ====== NEW: phục vụ HDCT — đánh dấu đơn đã trả khi HDCT thanh toán xong ======
    public void danhDauDaTra(String maDatPhong) {
        DatPhong dp = findById(maDatPhong);
        if (dp == null) return;

        // Đã trả rồi thì bỏ qua
        if (dp.getTinhTrang() == DatPhong.TinhTrang.DA_TRA) {
            // đảm bảo cờ thanh toán cũng đúng
            if (dp.getThanhToan() != ThanhToan.DA_THANH_TOAN) {
                dp.setThanhToan(ThanhToan.DA_THANH_TOAN);
                repository.save(dp);
            }
            return;
        }

        // set thời điểm trả nếu thiếu
        if (dp.getNgayTraPhong() == null) {
            dp.setNgayTraPhong(LocalDateTime.now());
        }

        // chuyển tình trạng & gắn cờ thanh toán
        dp.setTinhTrang(DatPhong.TinhTrang.DA_TRA);
        dp.setThanhToan(ThanhToan.DA_THANH_TOAN);

        // tính tiền cuối cùng rồi lưu
        calculatePenalty(dp);
        getTotalAmount(dp);
        dp = repository.save(dp);

        // trả phòng + đồng bộ các bảng liên quan
        forceReleaseRoom(dp.getMaPhong());
        syncWithCustomer(dp);
        syncWithRoom(dp);
        repository.save(dp);
    }

    @Transactional
    public int recalcTotalsForActiveBookingsByRoomType(String maLoai) {
        List<DatPhong> bookings = repository.findAll().stream()
                .filter(dp -> dp.getMaPhong() != null
                        && dp.getMaPhong().getMaLoai() != null
                        && maLoai.equals(dp.getMaPhong().getMaLoai().getMaLoai()))
                .filter(dp -> dp.getTinhTrang() == DatPhong.TinhTrang.DANG_DOI
                        || dp.getTinhTrang() == DatPhong.TinhTrang.DANG_SU_DUNG
                        || dp.getTinhTrang() == DatPhong.TinhTrang.QUA_HAN)
                .toList();

        for (DatPhong dp : bookings) {
            calculatePenalty(dp);
            getTotalAmount(dp);
            repository.save(dp);
        }
        return bookings.size();
    }
    @Transactional
    private void recalcRoomState(String maPhong) {
        if (maPhong == null || maPhong.isBlank()) return;
    
        Phong room = phongRepository.findById(maPhong).orElse(null);
        if (room == null) return;
    
        // Lấy mọi booking còn “giữ” phòng
        // Gợi ý: tạo query repo: findByMaPhong_MaPhongAndTinhTrangIn(...)
        List<DatPhong.TinhTrang> active = java.util.Arrays.asList(
            DatPhong.TinhTrang.DANG_DOI,
            DatPhong.TinhTrang.DANG_SU_DUNG,
            DatPhong.TinhTrang.QUA_HAN
        );
        List<DatPhong> keepers = repository
            .findByMaPhong_MaPhongAndTinhTrangIn(maPhong, active);
    
        if (keepers == null || keepers.isEmpty()) {
            room.setTinhTrangPhong(TinhTrangPhong.TRONG);
        } else if (keepers.stream().anyMatch(dp ->
                   dp.getTinhTrang() == DatPhong.TinhTrang.DANG_SU_DUNG
                || dp.getTinhTrang() == DatPhong.TinhTrang.QUA_HAN)) {
            room.setTinhTrangPhong(TinhTrangPhong.DANG_SU_DUNG);
        } else {
            // chỉ còn DANG_DOI → coi là đã đặt, chưa ở
            room.setTinhTrangPhong(TinhTrangPhong.DA_DAT);
        }
        phongRepository.save(room);
    }

    public List<DatPhong> getAll() {
        return repository.findAll();
    }
    public List<DatPhong> getAllUnpaid() {
        return repository.findByThanhToan(ThanhToan.CHUA_THANH_TOAN);
    }
}
