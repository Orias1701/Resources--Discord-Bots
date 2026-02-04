package springbootjsp.model;

import jakarta.persistence.*;
import java.math.BigDecimal;


@Entity
@Table(name = "a4_loaiphong")
public class LoaiPhong {

    @Id
    @Column(name = "MaLoai", length = 50)
    private String maLoai;

    @Column(name = "TenLoai", length = 100)
    private String tenLoai;

    @Column(name = "SoGiuong", length = 100)
    private String soGiuong;

    @Column(name = "GiaLoai")
    private BigDecimal giaLoai;

    // Getters and Setters
    public String getMaLoai() {
        return maLoai;
    }

    public void setMaLoai(String maLoai) {
        this.maLoai = maLoai;
    }

    public String getTenLoai() {
        return tenLoai;
    }

    public void setTenLoai(String tenLoai) {
        this.tenLoai = tenLoai;
    }

    public String getSoGiuong() {
        return soGiuong;
    }

    public void setSoGiuong(String soGiuong) {
        this.soGiuong = soGiuong;
    }

    public BigDecimal getGiaLoai() {
        return giaLoai;
    }

    public void setGiaLoai(BigDecimal giaLoai) {
        this.giaLoai = giaLoai;
    }
}