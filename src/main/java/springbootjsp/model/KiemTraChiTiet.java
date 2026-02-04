package springbootjsp.model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Objects;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "b0_kiemtrachitiet")
public class KiemTraChiTiet implements Serializable {

    @EmbeddedId
    private KiemTraChiTietId id;

    @ManyToOne
    @JoinColumn(name = "MaKiemTra", insertable = false, updatable = false)
    private KiemTraPhong kiemTraPhong;

    @Column(name = "MaPhong", length = 50)
    private String maPhong;

    @Column(name = "MaThietBi", insertable = false, updatable = false)
    private String maThietBi;

    @Enumerated(EnumType.STRING)
    @Column(name = "TinhTrang")
    private TinhTrang tinhTrang;

    @Column(name = "DenBu")
    private BigDecimal denBu;

    @Column(name = "GhiChu", length = 255)
    private String ghiChu;

    @Column(name="SoLuongHong")
    private Integer soLuongHong;

    public Integer getSoLuongHong() {
        return soLuongHong;
    }
    public void setSoLuongHong(Integer soLuongHong) {
        this.soLuongHong = soLuongHong;
    }
    public enum TinhTrang {
        TOT, HONG
    }

    @Embeddable
    public static class KiemTraChiTietId implements Serializable {
        @Column(name = "MaKiemTra", length = 50)
        private String maKiemTra;

        @Column(name = "MaThietBi", length = 50)
        private String maThietBi;

        public KiemTraChiTietId() {}

        // ĐÃ SỬA: Hàm tạo giờ đã gán giá trị
        public KiemTraChiTietId(String maKiemTra, String maThietBi) {
            this.maKiemTra = maKiemTra;
            this.maThietBi = maThietBi;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            KiemTraChiTietId that = (KiemTraChiTietId) o;
            return Objects.equals(maKiemTra, that.maKiemTra) &&
                    Objects.equals(maThietBi, that.maThietBi);
        }

        @Override
        public int hashCode() {
            return Objects.hash(maKiemTra, maThietBi);
        }

        // Getters and Setters
        public String getMaKiemTra() { return maKiemTra; }
        public void setMaKiemTra(String maKiemTra) { this.maKiemTra = maKiemTra; }
        public String getMaThietBi() { return maThietBi; }
        public void setMaThietBi(String maThietBi) { this.maThietBi = maThietBi; }
    }

    // Getters and Setters
    public KiemTraChiTietId getId() { return id; }
    public void setId(KiemTraChiTietId id) { this.id = id; }
    public KiemTraPhong getKiemTraPhong() { return kiemTraPhong; }
    public void setKiemTraPhong(KiemTraPhong kiemTraPhong) { this.kiemTraPhong = kiemTraPhong; }
    public String getMaPhong() { return maPhong; }
    public void setMaPhong(String maPhong) { this.maPhong = maPhong; }
    public String getMaThietBi() { return maThietBi; }
    public void setMaThietBi(String maThietBi) { this.maThietBi = maThietBi; }
    public TinhTrang getTinhTrang() { return tinhTrang; }
    public void setTinhTrang(TinhTrang tinhTrang) { this.tinhTrang = tinhTrang; }
    public BigDecimal getDenBu() { return denBu; }
    public void setDenBu(BigDecimal denBu) { this.denBu = denBu; }
    public String getGhiChu() { return ghiChu; }
    public void setGhiChu(String ghiChu) { this.ghiChu = ghiChu; }
}