package springbootjsp.model;

import jakarta.persistence.*;

import java.math.BigDecimal;

@Entity
@Table(name = "a7_thietbi")
public class ThietBi {

    @Id
    @Column(name = "MaThietBi", length = 50)
    private String maThietBi;

    @Column(name = "TenThietBi", length = 100)
    private String tenThietBi;

    @Column(name = "DonGia")
    private BigDecimal donGia;

    @Column(name = "DenBu")
    private BigDecimal denBu;

    // Getters and Setters
    public String getMaThietBi() {
        return maThietBi;
    }

    public void setMaThietBi(String maThietBi) {
        this.maThietBi = maThietBi;
    }

    public String getTenThietBi() {
        return tenThietBi;
    }

    public void setTenThietBi(String tenThietBi) {
        this.tenThietBi = tenThietBi;
    }

    public BigDecimal getDonGia() {
        return donGia;
    }

    public void setDonGia(BigDecimal donGia) {
        this.donGia = donGia;
    }

    public BigDecimal getDenBu() {
        return denBu;
    }

    public void setDenBu(BigDecimal denBu) {
        this.denBu = denBu;
    }
}
