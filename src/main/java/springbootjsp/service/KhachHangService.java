package springbootjsp.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import springbootjsp.model.KhachHang;
import springbootjsp.repository.KhachHangRepository;

@Service
public class KhachHangService {

    @Autowired
    private KhachHangRepository khachHangRepository;

    public List<KhachHang> getAllKhachHang() {
        return khachHangRepository.findAll();
    }

    public Optional<KhachHang> getKhachHangById(String maKhachHang) {
        return khachHangRepository.findById(maKhachHang);
    }

    public KhachHang saveKhachHang(KhachHang khachHang) {
        String id = khachHang.getMaKhachHang();

        // === NẾU KHÁCH HÀNG ĐÃ CÓ TRONG DB => CẬP NHẬT ===
        if (id != null && khachHangRepository.existsById(id)) {
            KhachHang current = khachHangRepository.findById(id).get();
            current.setTenKhachHang(khachHang.getTenKhachHang());
            current.setGioiTinh(khachHang.getGioiTinh());
            current.setSdt(khachHang.getSdt());
            current.setTinhTrangKhach(khachHang.getTinhTrangKhach());
            return khachHangRepository.save(current);
        }

        // === NẾU CHƯA CÓ TRONG DB => THÊM MỚI ===
        if (id == null || id.isBlank()) {
            khachHang.setMaKhachHang(generateNextMaKhachHang());
        }

        var st = khachHang.getTinhTrangKhach();
        if (st == null || (st != KhachHang.TinhTrangKhach.TRONG && st != KhachHang.TinhTrangKhach.DA_DAT)) {
            khachHang.setTinhTrangKhach(KhachHang.TinhTrangKhach.TRONG);
        }

        return khachHangRepository.save(khachHang);
    }


    public void deleteKhachHang(String maKhachHang) {
        khachHangRepository.deleteById(maKhachHang);
    }

    public String generateNextMaKhachHang() {
        String maxMa = khachHangRepository.findMaxMaKhachHang();
        if (maxMa == null || maxMa.length() < 3) {
            return "KH001";
        }
        String numberPart = maxMa.replaceAll("\\D+", "");
        int num;
        try {
            num = numberPart.isEmpty() ? 0 : Integer.parseInt(numberPart);
        } catch (NumberFormatException e) {
            num = 0;
        }
        return String.format("KH%03d", num + 1); // SỬA: KH thay vì NV
    }

    public List<KhachHang> getAll() {
        return khachHangRepository.findAll();
    }
}

