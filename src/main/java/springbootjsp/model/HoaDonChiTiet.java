package springbootjsp.model;

import java.math.BigDecimal;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;

@Entity
@Table(
  name = "a3_hoadonchitiet",
  uniqueConstraints = @UniqueConstraint(
    name = "uk_a3_hd_dp_sddv_kt",
    columnNames = {"MaHoaDon", "MaDatPhong", "MaSDDV", "MaKiemTra"}
  )
)

public class HoaDonChiTiet {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "Id", nullable = false, updatable = false)
    private Long id;

    // FK -> a2_hoadon.MaHoaDon (NOT NULL)
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "MaHoaDon", referencedColumnName = "MaHoaDon", nullable = false)
    @JsonIgnoreProperties("chiTietList") // ⬅️ tránh Jackson đệ quy

    private HoaDon hoaDon;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "MaDatPhong", referencedColumnName = "MaDatPhong")
    private DatPhong datPhong;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "MaSDDV", referencedColumnName = "MaSDDV")
    private SuDungDichVu suDungDichVu;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "MaKiemTra", referencedColumnName = "MaKiemTra")
    private KiemTraPhong kiemTraPhong;

    @Column(name = "TienDatPhong")
    private BigDecimal tienDatPhong;
    @Column(name = "TienSDDV")
    private BigDecimal tienSddv;

    @Enumerated(EnumType.STRING)
    @Column(name = "ThanhToan")
    private ThanhToan thanhToan;

    public BigDecimal getTienSddv() {
        return tienSddv;
    }

    public void setTienSddv(BigDecimal tienSddv) {
        this.tienSddv = tienSddv;
    }
    @Column(name = "TienKiemTra")
    private BigDecimal tienKiemTra;
    public BigDecimal getTienDatPhong() {
        return tienDatPhong;
    }

    public void setTienDatPhong(BigDecimal tienDatPhong) {
        this.tienDatPhong = tienDatPhong;
    }



    public BigDecimal getTienKiemTra() {
        return tienKiemTra;
    }

    public void setTienKiemTra(BigDecimal tienKiemTra) {
        this.tienKiemTra = tienKiemTra;
    }

    public BigDecimal getThanhTien() {
        return thanhTien;
    }

    public void setThanhTien(BigDecimal thanhTien) {
        this.thanhTien = thanhTien;
    }
    @Column(name= "ThanhTien")
    private BigDecimal thanhTien;
    // ===== Getters/Setters =====
    public Long getId() { return id; }

    public HoaDon getHoaDon() { return hoaDon; }
    public void setHoaDon(HoaDon hoaDon) { this.hoaDon = hoaDon; }

    public DatPhong getDatPhong() { return datPhong; }
    public void setDatPhong(DatPhong datPhong) { this.datPhong = datPhong; }

    public SuDungDichVu getSuDungDichVu() { return suDungDichVu; }
    public void setSuDungDichVu(SuDungDichVu suDungDichVu) { this.suDungDichVu = suDungDichVu; }

    public KiemTraPhong getKiemTraPhong() { return kiemTraPhong; }
    public void setKiemTraPhong(KiemTraPhong kiemTraPhong) { this.kiemTraPhong = kiemTraPhong; }

    public ThanhToan getThanhToan() {
        return thanhToan;
    }

    public void setThanhToan(ThanhToan thanhToan) {
        this.thanhToan = thanhToan;
    }


}
