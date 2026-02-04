package springbootjsp.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import springbootjsp.model.NhanVien;
import springbootjsp.repository.NhanVienRepository;
// import springbootjsp.repository.UserRepository;

@Service
public class NhanVienService {

    @Autowired
    private NhanVienRepository repository;
    @Autowired
    // private UserRepository userRepository;

    public List<NhanVien> getAllNhanVien() {
        return repository.findAll();
    }

    public void saveNhanVien(NhanVien nv) {
        repository.save(nv);
    }

    public NhanVien getNhanVienById(String maNV) {
        Optional<NhanVien> optional = repository.findById(maNV);
        return optional.orElse(null);
    }

    public void deleteNhanVien(String maNV) {
        repository.deleteById(maNV);
    }

    public String generateNextMaNhanVien() {
        String maxMa = repository.findMaxMaNhanVien();

        // Nếu chưa có dữ liệu hoặc maxMa quá ngắn
        if (maxMa == null || maxMa.length() < 3) {
            return "NV001";
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
        return String.format("NV%03d", num);
    }
    public List<NhanVien> getNhanVienDonDep() {
        return repository.findByChucVu("Dọn dẹp");
    }

    
    public List<NhanVien> getAll() {
        return repository.findAll();
    }

    public NhanVien findByUsername(String username) {
        return repository.findByTenNhanVien(username);
    }




}
