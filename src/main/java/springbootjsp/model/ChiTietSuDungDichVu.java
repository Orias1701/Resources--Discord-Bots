package springbootjsp.model;

import java.math.BigDecimal;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;

@Entity
@Table(
    name = "b3_chitietsddv",
    uniqueConstraints = @UniqueConstraint(
        name = "uk_b3_masddv_madv",
        columnNames = {"MaSDDV", "MaDichVu"}
    )
)
public class ChiTietSuDungDichVu {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "Id", nullable = false, updatable = false)
    private Long id;

    // Nếu bạn có entity SuDungDichVu và DichVu thì dùng ManyToOne như dưới:
    @ManyToOne(optional = false)
    @JoinColumn(name = "MaSDDV", referencedColumnName = "MaSDDV", nullable = false)
    private SuDungDichVu suDungDichVu;

    @ManyToOne(optional = false)
    @JoinColumn(name = "MaDichVu", referencedColumnName = "MaDichVu", nullable = false)
    private DichVu dichVu;

    // Các cột dữ liệu khác (tuỳ schema thực tế của bạn):
    @Column(name = "SoLuong")
    private Integer soLuong;

    @Column(name = "ThanhTien")
    private BigDecimal thanhTien;

    public BigDecimal getThanhTien() {
        return thanhTien;
    }
    public void setThanhTien(BigDecimal thanhTien) {
        this.thanhTien = thanhTien;
    }
    // ===== Getters/Setters =====
    public Long getId() { return id; }
    public SuDungDichVu getSuDungDichVu() { return suDungDichVu; }
    public void setSuDungDichVu(SuDungDichVu suDungDichVu) { this.suDungDichVu = suDungDichVu; }
    public DichVu getDichVu() { return dichVu; }
    public void setDichVu(DichVu dichVu) { this.dichVu = dichVu; }
    public Integer getSoLuong() { return soLuong; }
    public void setSoLuong(Integer soLuong) { this.soLuong = soLuong; }
}
