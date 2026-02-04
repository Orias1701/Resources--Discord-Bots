package springbootjsp.model;

import java.math.BigDecimal;
import java.time.LocalDate;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "b2_sddv")
public class SuDungDichVu {

    @Id
    @Column(name = "MaSDDV", length = 50)
    private String maSDDV;

    @ManyToOne
    @JoinColumn(name = "MaKhachHang", referencedColumnName = "MaKhachHang")
    private KhachHang khachHang;

    @ManyToOne
    @JoinColumn(name = "MaNhanVien", referencedColumnName = "MaNhanVien")
    private NhanVien maNhanVien;

    @Column(name = "NgaySDDV")
    private LocalDate ngaySDDV;

    @Enumerated(EnumType.STRING)
    @Column(name = "ThanhToan")
    private ThanhToan thanhToan =ThanhToan.CHUA_THANH_TOAN;

    @Column(name="TongTien")
    private BigDecimal tongTien;
    
    public ThanhToan getThanhToan() {
        return thanhToan;
    }

    public void setThanhToan(ThanhToan thanhToan) {
        this.thanhToan = thanhToan;
    }

    public BigDecimal getTongTien() {
        return tongTien;
    }

    public void setTongTien(BigDecimal tongTien) {
        this.tongTien = tongTien;
    }



    // Getters and Setters
    public String getMaSDDV() {
        return maSDDV;
    }

    public void setMaSDDV(String maSDDV) {
        this.maSDDV = maSDDV;
    }

    public KhachHang getKhachHang() {
        return khachHang;
    }

    public void setKhachHang(KhachHang khachHang) {
        this.khachHang = khachHang;
    }


    public LocalDate getNgaySDDV() {
        return ngaySDDV;
    }

    public void setNgaySDDV(LocalDate ngaySDDV) {
        this.ngaySDDV = ngaySDDV;
    }

    public NhanVien getMaNhanVien() {
        return maNhanVien;
    }

    public void setMaNhanVien(NhanVien maNhanVien) {
        this.maNhanVien = maNhanVien;
    }
}
