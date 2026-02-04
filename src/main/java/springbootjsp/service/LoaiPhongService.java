package springbootjsp.service;

import org.springframework.stereotype.Service;
import springbootjsp.model.LoaiPhong;
import springbootjsp.repository.LoaiPhongRepository;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Service
public class LoaiPhongService {

    private final LoaiPhongRepository repository;
    private final DatPhongService datPhongService;

    public LoaiPhongService(LoaiPhongRepository loaiPhongRepository,
                            DatPhongService datPhongService) {
        this.repository = loaiPhongRepository;
        this.datPhongService = datPhongService;
    }

    // Lay tat ca loai phong
    public List<LoaiPhong> getAll() {
        return repository.findAll();
    }

    // Tim kiem theo keyword
    public List<LoaiPhong> search(String keyword) {
        return repository.searchByKeyword(keyword);
    }

    // Loc theo khoang gia
    public List<LoaiPhong> filterByPrice(BigDecimal minPrice, BigDecimal maxPrice) {
        return repository.filterByPrice(minPrice, maxPrice);
    }

    // Lay theo ma
    public Optional<LoaiPhong> getById(String maLoai) {
        return repository.findById(maLoai);
    }

    // Them hoac cap nhat
    public LoaiPhong save(LoaiPhong lp) {
        // nếu có thay đổi giá thì cập nhật lại tất cả booking đang hoạt động
        LoaiPhong old = repository.findById(lp.getMaLoai()).orElse(null);
        boolean giaThayDoi = old != null && lp.getGiaLoai() != null
                             && !lp.getGiaLoai().equals(old.getGiaLoai());

        LoaiPhong saved = repository.save(lp);

        if (giaThayDoi) {
            int affected = datPhongService.recalcTotalsForActiveBookingsByRoomType(lp.getMaLoai());
            System.out.println("Đã cập nhật tổng tiền cho " + affected + " booking của loại phòng " + lp.getMaLoai());
        }
        return saved;
    }

    // Xoa theo ma
    public void deleteById(String maLoai) {
        repository.deleteById(maLoai);
    }
    public String generateNextMaLoaiPhong() {
        String maxMa = repository.findMaxMaLoaiPhong();

        // Nếu chưa có dữ liệu hoặc maxMa quá ngắn
        if (maxMa == null || maxMa.length() < 3) {
            return "LP001";
        }

        // Lấy số cuối
        String numberPart = maxMa.substring(2); // NV001 -> 001
        int num = 0;

        try {
            num = Integer.parseInt(numberPart);
        } catch (NumberFormatException e) {
            // nếu không parse được thì reset về 0
            num = 0;
        }

        num++;
        return String.format("LP%03d", num);
    }
}
