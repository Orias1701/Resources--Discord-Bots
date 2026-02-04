package springbootjsp.model;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;


@Entity
@Table(name = "a5_phong")
public class Phong {

    @Id
    @Column(name = "MaPhong", length = 50)
    private String maPhong;

    @Column(name = "TenPhong", length = 100)
    private String tenPhong;

    @ManyToOne
    @JoinColumn(name = "MaLoai", referencedColumnName = "MaLoai")
    private LoaiPhong maLoai;

    @Column(name = "MoTa", length = 50)
    private String moTa;

    @Enumerated(EnumType.STRING)
    @Column(name = "TinhTrangPhong")
    private TinhTrangPhong tinhTrangPhong;

    public enum TinhTrangPhong {
        TRONG, DANG_SU_DUNG, DA_DAT
    }

    public String getMaPhong() {
        return maPhong;
    }

    public void setMaPhong(String maPhong) {
        this.maPhong = maPhong;
    }

    public String getTenPhong() {
        return tenPhong;
    }

    public void setTenPhong(String tenPhong) {
        this.tenPhong = tenPhong;
    }

    public LoaiPhong getMaLoai() {
        return maLoai;
    }

    public void setMaLoai(LoaiPhong maLoai) {
        this.maLoai = maLoai;
    }

    public String getMoTa() {
        return moTa;
    }

    public void setMoTa(String moTa) {
        this.moTa = moTa;
    }

    public TinhTrangPhong getTinhTrangPhong() {
        return tinhTrangPhong;
    }

    public void setTinhTrangPhong(TinhTrangPhong tinhTrangPhong) {
        this.tinhTrangPhong = tinhTrangPhong;
    }
}