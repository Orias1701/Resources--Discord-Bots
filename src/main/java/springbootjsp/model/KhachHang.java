package springbootjsp.model;


import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;

@Entity
@Table(name = "a1_khachhang")
public class KhachHang {

    @Id
    @Column(name = "MaKhachHang", length = 50)
    private String maKhachHang;

    @Column(name = "TenKhachHang", length = 100)
    private String tenKhachHang;

    @Enumerated(EnumType.STRING)
    @Column(name = "GioiTinh")
    private GioiTinh gioiTinh;

    @Column(name = "Sdt", length = 10)
    private String sdt;

    @Column(name = "TinhTrangKhach", length = 20)
    @Enumerated(EnumType.STRING)
    private TinhTrangKhach tinhTrangKhach;

    @PrePersist
    public void prePersistDefaultStatus() {
        // Chỉ bảo vệ lúc INSERT (tạo mới)
        if (tinhTrangKhach == null
                || (tinhTrangKhach != TinhTrangKhach.TRONG && tinhTrangKhach != TinhTrangKhach.DA_DAT)) {
            tinhTrangKhach = TinhTrangKhach.TRONG;
        }
    }
    
    public enum GioiTinh {
        NAM, NU
    }

    public enum TinhTrangKhach {
        DANG_O,DA_ROI,DA_DAT,TRONG
    }

    // Getters and Setters
    public String getMaKhachHang() {
        return maKhachHang;
    }

    public void setMaKhachHang(String maKhachHang) {
        this.maKhachHang = maKhachHang;
    }

    public String getTenKhachHang() {
        return tenKhachHang;
    }

    public void setTenKhachHang(String tenKhachHang) {
        this.tenKhachHang = tenKhachHang;
    }

    public GioiTinh getGioiTinh() {
        return gioiTinh;
    }

    public void setGioiTinh(GioiTinh gioiTinh) {
        this.gioiTinh = gioiTinh;
    }

    public String getSdt() {
        return sdt;
    }

    public void setSdt(String sdt) {
        this.sdt = sdt;
    }

    public TinhTrangKhach getTinhTrangKhach() {
        return tinhTrangKhach;
    }

    public void setTinhTrangKhach(TinhTrangKhach tinhTrangKhach) {
        this.tinhTrangKhach = tinhTrangKhach;
    }
}