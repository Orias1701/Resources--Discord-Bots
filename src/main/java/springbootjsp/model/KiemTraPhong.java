package springbootjsp.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;


@Entity
@Table(name = "a9_kiemtraphong")
public class KiemTraPhong {

    @Id
    @Column(name = "MaKiemTra", length = 50)
    private String maKiemTra;

    @ManyToOne
    @JoinColumn(name = "MaPhong", referencedColumnName = "MaPhong")
    private Phong phong;

    @ManyToOne
    @JoinColumn(name = "MaNhanVien", referencedColumnName = "MaNhanVien")
    private NhanVien maNhanVien;

    @ManyToOne
    @JoinColumn(name = "MaDatPhong", referencedColumnName = "MaDatPhong")
    private DatPhong datPhong;

    @Column(name = "NgayKiemTra")
    private LocalDateTime ngayKiemTra;

    @Column(name = "GhiChu", length = 255)
    private String ghiChu;

    @Column(name="TienKiemTra")
    private BigDecimal tienKiemTra;
    
    public BigDecimal getTienKiemTra() {
        return tienKiemTra;
    }
    public void setTienKiemTra(BigDecimal tienKiemTra) {
        this.tienKiemTra = tienKiemTra;
    }

    @Enumerated(EnumType.STRING)
    @Column(name = "ThanhToan")
    private ThanhToan thanhToan=ThanhToan.CHUA_THANH_TOAN;;

    public KiemTraPhong() {
    }

    public ThanhToan getThanhToan() {
        return thanhToan;
    }

    public void setThanhToan(ThanhToan daThanhToan) {
        this.thanhToan = daThanhToan;
    }
    @Enumerated(EnumType.STRING)
    @Column(name = "TrangThai")
    private TrangThai trangThai;

    public NhanVien getMaNhanVien() {
        return maNhanVien;
    }

    public void setMaNhanVien(NhanVien maNhanVien) {
        this.maNhanVien = maNhanVien;
    }

    public DatPhong getDatPhong() {
        return datPhong;
    }

    public void setDatPhong(DatPhong datPhong) {
        this.datPhong = datPhong;
    }

    public enum TrangThai {
        TOT, CAN_DON, HONG
    }

    // Getters and Setters
    public String getMaKiemTra() {
        return maKiemTra;
    }

    public void setMaKiemTra(String maKiemTra) {
        this.maKiemTra = maKiemTra;
    }

    public Phong getPhong() {
        return phong;
    }

    public void setPhong(Phong phong) {
        this.phong = phong;
    }

    

    public LocalDateTime getNgayKiemTra() {
        return ngayKiemTra;
    }

    public void setNgayKiemTra(LocalDateTime ngayKiemTra) {
        this.ngayKiemTra = ngayKiemTra;
    }

    public String getGhiChu() {
        return ghiChu;
    }

    public void setGhiChu(String ghiChu) {
        this.ghiChu = ghiChu;
    }

    public TrangThai getTrangThai() {
        return trangThai;
    }

    public void setTrangThai(TrangThai trangThai) {
        this.trangThai = trangThai;
    }


    public String getNgayKiemTraString() {
        if (ngayKiemTra == null) {
            return "";
        }
        return ngayKiemTra.format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm"));
    }




}