package springbootjsp.model;

import jakarta.persistence.*;
import java.io.Serializable;

@Entity
@Table(
    name = "a8_thietbiphong",
    uniqueConstraints = @UniqueConstraint(
        name = "uk_a8_maphong_mathietbi",
        columnNames = {"MaPhong", "MaThietBi"}
    )
)
public class ThietBiPhong implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "Id")
    private Long id; 

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "MaPhong", nullable = false)
    private Phong phong;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "MaThietBi", nullable = false)
    private ThietBi thietBi;

    @Column(name = "SoLuong")
    private Integer soLuong;

    @Enumerated(EnumType.STRING)
    @Column(name = "TrangThai")
    private TrangThai trangThai;

    public enum TrangThai { TOT, HU_HONG }

    // ========== Getters / Setters ==========
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Phong getPhong() { return phong; }
    public void setPhong(Phong phong) { this.phong = phong; }

    public ThietBi getThietBi() { return thietBi; }
    public void setThietBi(ThietBi thietBi) { this.thietBi = thietBi; }

    public Integer getSoLuong() { return soLuong; }
    public void setSoLuong(Integer soLuong) { this.soLuong = soLuong; }

    public TrangThai getTrangThai() { return trangThai; }
    public void setTrangThai(TrangThai trangThai) { this.trangThai = trangThai; }
}
