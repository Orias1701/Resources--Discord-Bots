package springbootjsp.model;
import java.math.BigDecimal;
import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;

@Entity
@Table(name = "a6_datphong")
public class DatPhong {

    @Id
    @Column(name = "MaDatPhong", length = 50)
    private String maDatPhong;

    @ManyToOne
    @JoinColumn(name = "MaPhong", referencedColumnName = "MaPhong")
    private Phong maPhong;

    @ManyToOne
    @JoinColumn(name = "MaNhanVien", referencedColumnName = "MaNhanVien")
    private NhanVien maNhanVien;

    @ManyToOne
    @JoinColumn(name = "MaKhachHang", referencedColumnName = "MaKhachHang")
    private KhachHang maKhachHang;

    @Column(name = "NgayNhanPhong")
    private LocalDateTime ngayNhanPhong;

    @Column(name = "NgayTraPhong")
    private LocalDateTime ngayTraPhong;

    @Enumerated(EnumType.STRING)
    @Column(name = "CachDat")
    private CachDat cachDat;

    @Enumerated(EnumType.STRING)
    @Column(name = "TinhTrang")
    private TinhTrang tinhTrang;

    @Column(name = "TienPhat")
    private BigDecimal tienPhat;

    @Column(name = "NgayHenTra")
    private LocalDateTime ngayHenTra;
    
    @Column(name = "TongTien")
    private BigDecimal tongTien;

    @Enumerated(EnumType.STRING)
    @Column(name = "ThanhToan")
    private ThanhToan thanhToan =ThanhToan.CHUA_THANH_TOAN;

    @PrePersist
    public void onCreate() {
        if (this.thanhToan == null) this.thanhToan = ThanhToan.CHUA_THANH_TOAN;
    }
    
    public ThanhToan getThanhToan() {
        return thanhToan;
    }

    public void setThanhToan(ThanhToan daThanhToan) {
        this.thanhToan = daThanhToan;
    }

    public BigDecimal getTongTien() {
        return tongTien;
    }

    public void setTongTien(BigDecimal tongTien) {
        this.tongTien = tongTien;
    }

    public Phong getMaPhong() {
        return maPhong;
    }

    public void setMaPhong(Phong maPhong) {
        this.maPhong = maPhong;
    }

    public KhachHang getMaKhachHang() {
        return maKhachHang;
    }

    public void setMaKhachHang(KhachHang maKhachHang) {
        this.maKhachHang = maKhachHang;
    }

    public NhanVien getMaNhanVien() {
        return maNhanVien;
    }

    public void setMaNhanVien(NhanVien maNhanVien) {
        this.maNhanVien = maNhanVien;
    }
    
    public LocalDateTime getNgayHenTra() {
        return ngayHenTra;
    }
    public void setNgayHenTra(LocalDateTime ngayHenTra) {
        this.ngayHenTra = ngayHenTra;
    }

    public enum CachDat {
        DAT_ONLINE, DAT_TRUC_TIEP
    }

    public enum TinhTrang {
        DANG_SU_DUNG, QUA_HAN, DANG_DOI, DA_TRA
    }

    // Getters and Setters
    public String getMaDatPhong() {
        return maDatPhong;
    }

    public void setMaDatPhong(String maDatPhong) {
        this.maDatPhong = maDatPhong;
    }

    public LocalDateTime getNgayNhanPhong() {
        return ngayNhanPhong;
    }

    public void setNgayNhanPhong(LocalDateTime ngayNhanPhong) {
        this.ngayNhanPhong = ngayNhanPhong;
    }

    public LocalDateTime getNgayTraPhong() {
        return ngayTraPhong;
    }

    public void setNgayTraPhong(LocalDateTime ngayTraPhong) {
        this.ngayTraPhong = ngayTraPhong;
    }

    public CachDat getCachDat() {
        return cachDat;
    }

    public void setCachDat(CachDat cachDat) {
        this.cachDat = cachDat;
    }

    public TinhTrang getTinhTrang() {
        return tinhTrang;
    }

    public void setTinhTrang(TinhTrang tinhTrang) {
        this.tinhTrang = tinhTrang;
    }

    public BigDecimal getTienPhat() {
        return tienPhat;
    }

    public void setTienPhat(BigDecimal tienPhat) {
        this.tienPhat = tienPhat;
    }

    public DatPhong orElseThrow(Object object) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'orElseThrow'");
    }
}