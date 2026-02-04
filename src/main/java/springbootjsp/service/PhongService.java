package springbootjsp.service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import springbootjsp.model.Phong;
import springbootjsp.repository.PhongRepository;

@Service
public class PhongService {

    private final PhongRepository repository;

    public PhongService(PhongRepository repository) {
        this.repository = repository;
    }

    public List<Phong> getAll() {
        return repository.findAll();
    }

    public List<Phong> search(String keyword) {
        return repository.searchByKeyword(keyword);
    }

    public List<Phong> filterByTinhTrang(Phong.TinhTrangPhong tinhTrang) {
        return repository.filterByTinhTrang(tinhTrang);
    }

    public Optional<Phong> getById(String maPhong) {
        return repository.findById(maPhong);
    }

    public Phong save(Phong phong) {
        return repository.save(phong);
    }

    public void deleteById(String maPhong) {
        repository.deleteById(maPhong);
    }

    public String generateNextMaPhong() {
        String maxMa = repository.findMaxMaPhong();
        if (maxMa == null || maxMa.length() < 3) {
            return "P001";
        }
        String numberPart = maxMa.substring(1); // P001 -> 001
        int num = 0;
        try {
            num = Integer.parseInt(numberPart);
        } catch (NumberFormatException e) {
            num = 0;
        }
        num++;
        return String.format("P%03d", num);
    }

    public List< Phong >getPhongDangSuDung() {
        return repository.findByTinhTrangPhong(Phong.TinhTrangPhong.DANG_SU_DUNG);
    }

    public List<Phong> getPhongTrong() {
        return repository.findByTinhTrangPhong(Phong.TinhTrangPhong.TRONG);
    }

    public List<Phong> getPhongDaDat() {
        return repository.findByTinhTrangPhong(Phong.TinhTrangPhong.DA_DAT);
    }

    public List<Phong> getALL(){
        return repository.findAll();
    }

}
