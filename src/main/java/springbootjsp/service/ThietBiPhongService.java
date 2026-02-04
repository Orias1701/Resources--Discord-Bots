package springbootjsp.service;

import org.springframework.stereotype.Service;
import springbootjsp.model.ThietBiPhong;
import springbootjsp.repository.ThietBiPhongRepository;

import java.util.List;
import java.util.Optional;

@Service
public class ThietBiPhongService {

    private final ThietBiPhongRepository repository;

    public ThietBiPhongService(ThietBiPhongRepository repository) {
        this.repository = repository;
    }

    public List<ThietBiPhong> getAll() {
        return repository.findAll();
    }

    public List<ThietBiPhong> search(String keyword, ThietBiPhong.TrangThai trangThai) {
        String k = (keyword == null || keyword.isBlank()) ? null : keyword.trim();
        return repository.search(k, trangThai);
    }

    // Giữ nguyên chữ ký: lấy theo cặp (MaPhong, MaThietBi)
    public Optional<ThietBiPhong> getById(String maPhong, String maThietBi) {
        if (maPhong == null || maPhong.isBlank() || maThietBi == null || maThietBi.isBlank()) {
            return Optional.empty();
        }
        return repository.findByPhong_MaPhongAndThietBi_MaThietBi(maPhong, maThietBi);
    }

    @SuppressWarnings("null")
    public void save(ThietBiPhong tbp) {
        // Nếu chưa có Id nhưng cặp (MaPhong, MaThietBi) đã tồn tại → set Id để update (tránh UNIQUE lỗi)
        if (tbp != null
                && tbp.getId() == null
                && tbp.getPhong() != null
                && tbp.getPhong().getMaPhong() != null
                && tbp.getThietBi() != null
                && tbp.getThietBi().getMaThietBi() != null) {

            repository.findByPhong_MaPhongAndThietBi_MaThietBi(
                    tbp.getPhong().getMaPhong(),
                    tbp.getThietBi().getMaThietBi()
            ).ifPresent(existing -> tbp.setId(existing.getId()));
        }
        repository.save(tbp);
    }

    // Giữ nguyên chữ ký: xóa theo cặp (MaPhong, MaThietBi)
    public void delete(String maPhong, String maThietBi) {
        repository.findByPhong_MaPhongAndThietBi_MaThietBi(maPhong, maThietBi)
                  .ifPresent(existing -> repository.deleteById(existing.getId()));
    }
}
