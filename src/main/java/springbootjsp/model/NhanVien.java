package springbootjsp.model;

import jakarta.persistence.*;

@Entity
@Table(name = "b4_nhanvien")
public class NhanVien {

    @Id
    @Column(name = "MaNhanVien", length = 50, unique = true)
    private String maNhanVien;

    @Column(name = "TenNhanVien", length = 100)
    private String tenNhanVien;

    @Column(name = "Sdt", length = 15)
    private String sdt;

    @Enumerated(EnumType.STRING)
    @Column(name = "GioiTinh")
    private GioiTinh gioiTinh;

    @Column(name = "ChucVu", length = 50)
    private String chucVu;

    // Thêm quan hệ 1-1 với User
    @OneToOne(mappedBy = "nhanVien", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private User user;

    // Getters & Setters
    public String getMaNhanVien() {
        return maNhanVien;
    }

    public void setMaNhanVien(String maNhanVien) {
        this.maNhanVien = maNhanVien;
    }

    public String getTenNhanVien() {
        return tenNhanVien;
    }

    public void setTenNhanVien(String tenNhanVien) {
        this.tenNhanVien = tenNhanVien;
    }

    public String getSdt() {
        return sdt;
    }

    public void setSdt(String sdt) {
        this.sdt = sdt;
    }

    public GioiTinh getGioiTinh() {
        return gioiTinh;
    }

    public void setGioiTinh(GioiTinh gioiTinh) {
        this.gioiTinh = gioiTinh;
    }

    public String getChucVu() {
        return chucVu;
    }

    public void setChucVu(String chucVu) {
        this.chucVu = chucVu;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    // enum giới tính
    public enum GioiTinh {
        NAM, NU
    }
}
