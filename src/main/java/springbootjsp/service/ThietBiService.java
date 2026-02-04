package springbootjsp.service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import springbootjsp.model.ThietBi;
import springbootjsp.repository.ThietBiRepository;

@Service
public class ThietBiService {

    private final ThietBiRepository repository;

    public ThietBiService(ThietBiRepository repository) {
        this.repository = repository;
    }

    public List<ThietBi> getAll() {
        return repository.findAll();
    }

    public Optional<ThietBi> getById(String maThietBi) {
        return repository.findById(maThietBi);
    }

    public ThietBi save(ThietBi tb) {
        return repository.save(tb);
    }

    public void delete(String maThietBi) {
        repository.deleteById(maThietBi);
    }

    public List<ThietBi> searchByKeyword(String keyword) {
        return repository.searchByKeyword(keyword);
    }

    public String generateNextMaThietBi() {
        String maxMa = repository.findMaxMaThietBi();

        // Nếu chưa có dữ liệu hoặc maxMa quá ngắn
        if (maxMa == null || maxMa.length() < 3) {
            return "TP001";
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
        return String.format("TP%03d", num);
    }

}
