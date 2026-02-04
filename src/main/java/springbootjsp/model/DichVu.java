package springbootjsp.model;

import java.math.BigDecimal;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "b1_dichvu")
public class DichVu {

    @Id
    @Column(name = "MaDichVu", length = 50)
    private String maDichVu;

    @Column(name = "TenDichVu", length = 255)
    private String tenDichVu;

    @Column(name = "LoaiDichVu", length = 100)
    private String loaiDichVu;

    @Column(name = "GiaDichVu")
    private BigDecimal giaDichVu;

    // Getters and Setters
    public String getMaDichVu() {
        return maDichVu;
    }

    public void setMaDichVu(String maDichVu) {
        this.maDichVu = maDichVu;
    }

    public String getTenDichVu() {
        return tenDichVu;
    }

    public void setTenDichVu(String tenDichVu) {
        this.tenDichVu = tenDichVu;
    }

    public String getLoaiDichVu() {
        return loaiDichVu;
    }

    public void setLoaiDichVu(String loaiDichVu) {
        this.loaiDichVu = loaiDichVu;
    }

    public BigDecimal getGiaDichVu() {
        return giaDichVu;
    }

    public void setGiaDichVu(BigDecimal giaDichVu) {
        this.giaDichVu = giaDichVu;
    }
}
