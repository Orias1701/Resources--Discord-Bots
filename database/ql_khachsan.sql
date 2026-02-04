-- MySQL dump 10.13  Distrib 8.0.43, for Win64 (x86_64)
--
-- Host: 127.0.0.1    Database: ql_khachsan
-- ------------------------------------------------------
-- Server version	8.0.43

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!50503 SET NAMES utf8 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `b0_khachhang`
--

DROP DATABASE IF EXISTS ql_khachsan;
CREATE DATABASE IF NOT EXISTS ql_khachsan;

USE ql_khachsan;

DROP TABLE IF EXISTS `b0_khachhang`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `b0_khachhang` (
  `MaKhachHang` varchar(50) NOT NULL COMMENT 'Mã khách hàng',
  `TenKhachHang` varchar(100) DEFAULT NULL COMMENT 'Tên khách hàng',
  `GioiTinh` enum('NAM','NU') DEFAULT NULL,
  `Sdt` varchar(10) DEFAULT NULL,
  `TinhTrangKhach` enum('DANG_O','DA_DAT','DA_ROI','TRONG') DEFAULT NULL,
  PRIMARY KEY (`MaKhachHang`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='Khách hàng';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `b0_khachhang`
--

LOCK TABLES `b0_khachhang` WRITE;
/*!40000 ALTER TABLE `b0_khachhang` DISABLE KEYS */;
INSERT INTO `b0_khachhang` VALUES ('KH001','123','NU','123','DA_ROI'),('KH004','test','NAM','1111','DA_ROI');
/*!40000 ALTER TABLE `b0_khachhang` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `c0_hoadon`
--

DROP TABLE IF EXISTS `c0_hoadon`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `c0_hoadon` (
  `MaHoaDon` varchar(50) NOT NULL COMMENT 'Mã hóa đơn',
  `MaNhanVien` varchar(50) DEFAULT NULL COMMENT 'Mã nhân viên',
  `MaKhachHang` varchar(50) DEFAULT NULL COMMENT 'Mã khách hàng',
  `Ngay` date DEFAULT NULL COMMENT 'Ngày nhập',
  `TongTien` decimal(38,2) DEFAULT NULL,
  `ThanhToan` enum('DA_THANH_TOAN','CHUA_THANH_TOAN') DEFAULT 'CHUA_THANH_TOAN',
  PRIMARY KEY (`MaHoaDon`),
  KEY `MaNhanVien` (`MaNhanVien`),
  KEY `MaKhachHang` (`MaKhachHang`),
  CONSTRAINT `c0_hoadon_ibfk_1` FOREIGN KEY (`MaNhanVien`) REFERENCES `a0_nhanvien` (`MaNhanVien`),
  CONSTRAINT `c0_hoadon_ibfk_2` FOREIGN KEY (`MaKhachHang`) REFERENCES `b0_khachhang` (`MaKhachHang`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='Hóa đơn';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `c0_hoadon`
--

LOCK TABLES `c0_hoadon` WRITE;
/*!40000 ALTER TABLE `c0_hoadon` DISABLE KEYS */;
INSERT INTO `c0_hoadon` VALUES ('HD001',NULL,'KH001',NULL,3.00,'DA_THANH_TOAN');
/*!40000 ALTER TABLE `c0_hoadon` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `c1_hoadonchitiet`
--

DROP TABLE IF EXISTS `c1_hoadonchitiet`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `c1_hoadonchitiet` (
  `Id` bigint unsigned NOT NULL AUTO_INCREMENT,
  `MaHoaDon` varchar(50) NOT NULL COMMENT 'Mã hóa đơn',
  `MaDatPhong` varchar(50) DEFAULT NULL,
  `MaSDDV` varchar(50) DEFAULT NULL,
  `MaKiemTra` varchar(50) DEFAULT NULL,
  `TienDatPhong` decimal(38,2) DEFAULT NULL,
  `TienSDDV` decimal(38,2) DEFAULT NULL,
  `TienKiemTra` decimal(38,2) DEFAULT NULL,
  `ThanhTien` decimal(38,2) DEFAULT NULL,
  `ThanhToan` enum('CHUA_THANH_TOAN','DA_THANH_TOAN') DEFAULT 'CHUA_THANH_TOAN',
  PRIMARY KEY (`Id`),
  UNIQUE KEY `uk_a3_hd_dp_sddv_kt` (`MaHoaDon`,`MaDatPhong`,`MaSDDV`,`MaKiemTra`),
  KEY `fk_a3_dp` (`MaDatPhong`),
  KEY `fk_a3_sddv` (`MaSDDV`),
  KEY `fk_a3_kt` (`MaKiemTra`),
  CONSTRAINT `c1_hoadonchitiet_ibfk_1` FOREIGN KEY (`MaHoaDon`) REFERENCES `c0_hoadon` (`MaHoaDon`),
  CONSTRAINT `fk_a3_dp` FOREIGN KEY (`MaDatPhong`) REFERENCES `d2_datphong` (`MaDatPhong`),
  CONSTRAINT `fk_a3_hd` FOREIGN KEY (`MaHoaDon`) REFERENCES `c0_hoadon` (`MaHoaDon`),
  CONSTRAINT `fk_a3_kt` FOREIGN KEY (`MaKiemTra`) REFERENCES `f2_kiemtraphong` (`MaKiemTra`),
  CONSTRAINT `fk_a3_sddv` FOREIGN KEY (`MaSDDV`) REFERENCES `e1_sddv` (`MaSDDV`)
) ENGINE=InnoDB AUTO_INCREMENT=124 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='Hóa đơn chi tiết';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `c1_hoadonchitiet`
--

LOCK TABLES `c1_hoadonchitiet` WRITE;
/*!40000 ALTER TABLE `c1_hoadonchitiet` DISABLE KEYS */;
INSERT INTO `c1_hoadonchitiet` VALUES (123,'HD001','DP004',NULL,NULL,3.00,0.00,0.00,3.00,'DA_THANH_TOAN');
/*!40000 ALTER TABLE `c1_hoadonchitiet` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `d0_loaiphong`
--

DROP TABLE IF EXISTS `d0_loaiphong`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `d0_loaiphong` (
  `MaLoai` varchar(50) NOT NULL COMMENT 'Mã loại phòng',
  `TenLoai` varchar(100) DEFAULT NULL COMMENT 'Tên loại',
  `SoGiuong` varchar(100) DEFAULT NULL COMMENT 'Số giường',
  `GiaLoai` decimal(38,2) DEFAULT NULL,
  PRIMARY KEY (`MaLoai`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='Loại phòng';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `d0_loaiphong`
--

LOCK TABLES `d0_loaiphong` WRITE;
/*!40000 ALTER TABLE `d0_loaiphong` DISABLE KEYS */;
INSERT INTO `d0_loaiphong` VALUES ('LP001','1','1',1.00),('NV002','2','2',2.00);
/*!40000 ALTER TABLE `d0_loaiphong` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `d1_phong`
--

DROP TABLE IF EXISTS `d1_phong`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `d1_phong` (
  `MaPhong` varchar(50) NOT NULL COMMENT 'Mã phòng',
  `TenPhong` varchar(100) DEFAULT NULL COMMENT 'Tên phòng',
  `MaLoai` varchar(50) DEFAULT NULL COMMENT 'Loại phòng',
  `MoTa` varchar(50) DEFAULT NULL COMMENT 'Mô tả',
  `TinhTrangPhong` enum('TRONG','DANG_SU_DUNG','DA_DAT') DEFAULT NULL,
  PRIMARY KEY (`MaPhong`),
  KEY `MaLoai` (`MaLoai`),
  CONSTRAINT `d1_phong_ibfk_1` FOREIGN KEY (`MaLoai`) REFERENCES `d0_loaiphong` (`MaLoai`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='Phòng';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `d1_phong`
--

LOCK TABLES `d1_phong` WRITE;
/*!40000 ALTER TABLE `d1_phong` DISABLE KEYS */;
INSERT INTO `d1_phong` VALUES ('P001','1','LP001','1','DANG_SU_DUNG'),('P002','2','LP001','','TRONG'),('P003','3','LP001','','TRONG');
/*!40000 ALTER TABLE `d1_phong` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `d2_datphong`
--

DROP TABLE IF EXISTS `d2_datphong`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `d2_datphong` (
  `MaDatPhong` varchar(50) NOT NULL COMMENT 'Mã đặt phòng',
  `MaPhong` varchar(50) DEFAULT NULL COMMENT 'Mã phòng',
  `MaNhanVien` varchar(50) DEFAULT NULL COMMENT 'Mã nhân viên',
  `MaKhachHang` varchar(50) DEFAULT NULL COMMENT 'Mã khách hàng',
  `NgayNhanPhong` datetime DEFAULT NULL COMMENT 'Ngày nhận phòng',
  `NgayTraPhong` datetime DEFAULT NULL COMMENT 'Ngày trả phòng',
  `NgayHenTra` datetime DEFAULT NULL,
  `CachDat` enum('DAT_ONLINE','DAT_TRUC_TIEP') DEFAULT NULL,
  `TinhTrang` enum('DANG_SU_DUNG','QUA_HAN','DANG_DOI','DA_TRA') DEFAULT NULL,
  `TienPhat` decimal(38,2) DEFAULT NULL,
  `ThanhToan` enum('DA_THANH_TOAN','CHUA_THANH_TOAN') DEFAULT 'CHUA_THANH_TOAN' COMMENT 'Trạng thái thanh toán',
  `TongTien` decimal(38,2) DEFAULT NULL,
  PRIMARY KEY (`MaDatPhong`),
  KEY `MaPhong` (`MaPhong`),
  KEY `MaNhanVien` (`MaNhanVien`),
  KEY `MaKhachHang` (`MaKhachHang`),
  CONSTRAINT `d2_datphong_ibfk_1` FOREIGN KEY (`MaPhong`) REFERENCES `d1_phong` (`MaPhong`),
  CONSTRAINT `d2_datphong_ibfk_2` FOREIGN KEY (`MaNhanVien`) REFERENCES `a0_nhanvien` (`MaNhanVien`),
  CONSTRAINT `d2_datphong_ibfk_3` FOREIGN KEY (`MaKhachHang`) REFERENCES `b0_khachhang` (`MaKhachHang`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='Đặt phòng';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `d2_datphong`
--

LOCK TABLES `d2_datphong` WRITE;
/*!40000 ALTER TABLE `d2_datphong` DISABLE KEYS */;
INSERT INTO `d2_datphong` VALUES ('DP001','P001','NV001','KH001','2025-09-01 00:23:00','2025-09-01 00:25:16','2025-09-01 04:23:00','DAT_TRUC_TIEP','DA_TRA',0.00,'DA_THANH_TOAN',0.00),('DP002','P001','NV001','KH001','2025-08-31 02:04:00','2025-09-01 02:10:23','2025-09-01 09:04:00','DAT_TRUC_TIEP','DA_TRA',0.00,'DA_THANH_TOAN',2.00),('DP004','P002','NV001','KH001','2025-08-30 04:22:00','2025-09-01 16:26:10','2025-09-01 04:22:00','DAT_ONLINE','DA_TRA',3.47,'DA_THANH_TOAN',6.47),('DP005','P001','NV001','KH001','2025-08-22 16:24:00',NULL,'2025-09-01 16:24:00','DAT_TRUC_TIEP','DANG_SU_DUNG',0.00,'CHUA_THANH_TOAN',10.00);
/*!40000 ALTER TABLE `d2_datphong` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `f0_thietbi`
--

DROP TABLE IF EXISTS `f0_thietbi`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `f0_thietbi` (
  `MaThietBi` varchar(50) NOT NULL COMMENT 'Mã thiết bị',
  `TenThietBi` varchar(100) DEFAULT NULL COMMENT 'Tên thiết bị',
  `DonGia` decimal(38,2) DEFAULT NULL,
  `DenBu` decimal(38,2) DEFAULT NULL,
  PRIMARY KEY (`MaThietBi`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='Thiết bị';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `f0_thietbi`
--

LOCK TABLES `f0_thietbi` WRITE;
/*!40000 ALTER TABLE `f0_thietbi` DISABLE KEYS */;
INSERT INTO `f0_thietbi` VALUES ('NV002','2',2.00,2.00),('TP001','1',1.00,1.00);
/*!40000 ALTER TABLE `f0_thietbi` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `f1_thietbiphong`
--

DROP TABLE IF EXISTS `f1_thietbiphong`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `f1_thietbiphong` (
  `Id` bigint unsigned NOT NULL AUTO_INCREMENT,
  `MaPhong` varchar(50) NOT NULL COMMENT 'Mã phòng',
  `MaThietBi` varchar(50) NOT NULL COMMENT 'Mã thiết bị',
  `SoLuong` int DEFAULT NULL COMMENT 'Số lượng thiết bị',
  `TrangThai` enum('TOT','HU_HONG') DEFAULT NULL,
  PRIMARY KEY (`Id`),
  UNIQUE KEY `uk_a8_maphong_mathietbi` (`MaPhong`,`MaThietBi`),
  KEY `MaThietBi` (`MaThietBi`),
  CONSTRAINT `f1_thietbiphong_ibfk_1` FOREIGN KEY (`MaPhong`) REFERENCES `d1_phong` (`MaPhong`),
  CONSTRAINT `f1_thietbiphong_ibfk_2` FOREIGN KEY (`MaThietBi`) REFERENCES `f0_thietbi` (`MaThietBi`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='Thiết bị phòng';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `f1_thietbiphong`
--

LOCK TABLES `f1_thietbiphong` WRITE;
/*!40000 ALTER TABLE `f1_thietbiphong` DISABLE KEYS */;
INSERT INTO `f1_thietbiphong` VALUES (1,'P001','NV002',3,'TOT'),(2,'P001','TP001',5,'TOT');
/*!40000 ALTER TABLE `f1_thietbiphong` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `f2_kiemtraphong`
--

DROP TABLE IF EXISTS `f2_kiemtraphong`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `f2_kiemtraphong` (
  `MaKiemTra` varchar(50) NOT NULL COMMENT 'Mã kiểm tra phòng',
  `MaPhong` varchar(50) DEFAULT NULL COMMENT 'Mã phòng',
  `MaNhanVien` varchar(50) DEFAULT NULL COMMENT 'Mã nhân viên',
  `NgayKiemTra` datetime DEFAULT NULL COMMENT 'Ngày kiểm tra',
  `GhiChu` varchar(255) DEFAULT NULL COMMENT 'Ghi chú',
  `TrangThai` enum('TOT','CAN_DON','HONG') DEFAULT NULL,
  `ThanhToan` enum('DA_THANH_TOAN','CHUA_THANH_TOAN') DEFAULT 'CHUA_THANH_TOAN' COMMENT 'Trạng thái thanh toán',
  `TienKiemTra` decimal(38,2) DEFAULT NULL,
  `MaDatPhong` varchar(50) DEFAULT NULL,
  PRIMARY KEY (`MaKiemTra`),
  KEY `MaPhong` (`MaPhong`),
  KEY `MaNhanVien` (`MaNhanVien`),
  KEY `fk_a9_dp` (`MaDatPhong`),
  CONSTRAINT `f2_kiemtraphong_ibfk_1` FOREIGN KEY (`MaPhong`) REFERENCES `d1_phong` (`MaPhong`),
  CONSTRAINT `f2_kiemtraphong_ibfk_2` FOREIGN KEY (`MaNhanVien`) REFERENCES `a0_nhanvien` (`MaNhanVien`),
  CONSTRAINT `fk_a9_dp` FOREIGN KEY (`MaDatPhong`) REFERENCES `d2_datphong` (`MaDatPhong`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='Kiểm tra phòng';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `f2_kiemtraphong`
--

LOCK TABLES `f2_kiemtraphong` WRITE;
/*!40000 ALTER TABLE `f2_kiemtraphong` DISABLE KEYS */;
INSERT INTO `f2_kiemtraphong` VALUES ('KT001','P002','DP004','NV001','2025-09-01 14:56:16','','TOT','CHUA_THANH_TOAN',0.00);
/*!40000 ALTER TABLE `f2_kiemtraphong` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `f3_kiemtrachitiet`
--

DROP TABLE IF EXISTS `f3_kiemtrachitiet`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `f3_kiemtrachitiet` (
  `Id` bigint unsigned NOT NULL AUTO_INCREMENT,
  `MaKiemTra` varchar(50) NOT NULL COMMENT 'Mã kiểm tra',
  `MaPhong` varchar(50) DEFAULT NULL COMMENT 'Mã phòng',
  `MaThietBi` varchar(50) NOT NULL COMMENT 'Mã thiết bị',
  `TinhTrang` enum('TOT','HONG') DEFAULT NULL,
  `SoLuongHong` int unsigned NOT NULL DEFAULT '0',
  `GhiChu` varchar(255) DEFAULT NULL COMMENT 'Ghi chú',
  `DenBu` decimal(38,2) DEFAULT NULL,
  PRIMARY KEY (`Id`),
  UNIQUE KEY `uk_b0_maktra_mathietbi` (`MaKiemTra`,`MaThietBi`),
  KEY `MaThietBi` (`MaThietBi`),
  KEY `fk_kiemtra_thietbiphong` (`MaPhong`,`MaThietBi`),
  CONSTRAINT `f3_kiemtrachitiet_ibfk_1` FOREIGN KEY (`MaKiemTra`) REFERENCES `f2_kiemtraphong` (`MaKiemTra`),
  CONSTRAINT `fk_kiemtra_thietbiphong` FOREIGN KEY (`MaPhong`, `MaThietBi`) REFERENCES `f1_thietbiphong` (`MaPhong`, `MaThietBi`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='Kiểm tra chi tiết';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `f3_kiemtrachitiet`
--

LOCK TABLES `f3_kiemtrachitiet` WRITE;
/*!40000 ALTER TABLE `f3_kiemtrachitiet` DISABLE KEYS */;
/*!40000 ALTER TABLE `f3_kiemtrachitiet` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `e0_dichvu`
--

DROP TABLE IF EXISTS `e0_dichvu`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `e0_dichvu` (
  `MaDichVu` varchar(50) NOT NULL COMMENT 'Mã dịch vụ',
  `TenDichVu` varchar(255) DEFAULT NULL COMMENT 'Tên dịch vụ',
  `LoaiDichVu` varchar(100) DEFAULT NULL COMMENT 'Loại dịch vụ',
  `GiaDichVu` decimal(38,2) DEFAULT NULL,
  PRIMARY KEY (`MaDichVu`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='Dịch vụ';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `e0_dichvu`
--

LOCK TABLES `e0_dichvu` WRITE;
/*!40000 ALTER TABLE `e0_dichvu` DISABLE KEYS */;
INSERT INTO `e0_dichvu` VALUES ('DV001','1','1',1.00),('DV002','2','2',2.00),('DV003','3','3',3.00),('DV004','4','4',4.00);
/*!40000 ALTER TABLE `e0_dichvu` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `e1_sddv`
--

DROP TABLE IF EXISTS `e1_sddv`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `e1_sddv` (
  `MaSDDV` varchar(50) NOT NULL COMMENT 'Mã sử dụng dịch vụ',
  `MaKhachHang` varchar(50) DEFAULT NULL COMMENT 'Mã khách hàng',
  `MaNhanVien` varchar(50) DEFAULT NULL COMMENT 'Mã nhân viên',
  `NgaySDDV` date DEFAULT NULL COMMENT 'Ngày sử dụng dịch vụ',
  `ThanhToan` enum('DA_THANH_TOAN','CHUA_THANH_TOAN') DEFAULT 'CHUA_THANH_TOAN' COMMENT 'Trạng thái thanh toán',
  `TongTien` decimal(38,2) DEFAULT NULL,
  PRIMARY KEY (`MaSDDV`),
  KEY `MaKhachHang` (`MaKhachHang`),
  KEY `MaNhanVien` (`MaNhanVien`),
  CONSTRAINT `e1_sddv_ibfk_1` FOREIGN KEY (`MaKhachHang`) REFERENCES `b0_khachhang` (`MaKhachHang`),
  CONSTRAINT `e1_sddv_ibfk_2` FOREIGN KEY (`MaNhanVien`) REFERENCES `a0_nhanvien` (`MaNhanVien`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='Sử dụng dịch vụ';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `e1_sddv`
--

LOCK TABLES `e1_sddv` WRITE;
/*!40000 ALTER TABLE `e1_sddv` DISABLE KEYS */;
INSERT INTO `e1_sddv` VALUES ('SD002','KH001','NV001','2025-08-26','DA_THANH_TOAN',9.00),('SD003','KH004',NULL,'2025-08-27','CHUA_THANH_TOAN',5.00);
/*!40000 ALTER TABLE `e1_sddv` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `e2_chitietsddv`
--

DROP TABLE IF EXISTS `e2_chitietsddv`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `e2_chitietsddv` (
  `Id` bigint unsigned NOT NULL AUTO_INCREMENT,
  `MaSDDV` varchar(50) NOT NULL COMMENT 'Mã sử dụng dịch vụ',
  `MaDichVu` varchar(50) NOT NULL COMMENT 'Mã dịch vụ',
  `SoLuong` int DEFAULT NULL,
  `ThanhTien` decimal(38,2) DEFAULT NULL,
  PRIMARY KEY (`Id`),
  UNIQUE KEY `uk_b3_masddv_madv` (`MaSDDV`,`MaDichVu`),
  KEY `MaDichVu` (`MaDichVu`),
  CONSTRAINT `e2_chitietsddv_ibfk_1` FOREIGN KEY (`MaSDDV`) REFERENCES `e1_sddv` (`MaSDDV`),
  CONSTRAINT `e2_chitietsddv_ibfk_2` FOREIGN KEY (`MaDichVu`) REFERENCES `e0_dichvu` (`MaDichVu`)
) ENGINE=InnoDB AUTO_INCREMENT=12 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='Sử dụng dịch vụ chi tiết';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `e2_chitietsddv`
--

LOCK TABLES `e2_chitietsddv` WRITE;
/*!40000 ALTER TABLE `e2_chitietsddv` DISABLE KEYS */;
INSERT INTO `e2_chitietsddv` VALUES (8,'SD002','DV001',3,3.00),(9,'SD002','DV002',3,6.00),(11,'SD003','DV001',5,5.00);
/*!40000 ALTER TABLE `e2_chitietsddv` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `a0_nhanvien`
--

DROP TABLE IF EXISTS `a0_nhanvien`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `a0_nhanvien` (
  `MaNhanVien` varchar(50) NOT NULL COMMENT 'Mã nhân viên',
  `TenNhanVien` varchar(100) DEFAULT NULL COMMENT 'Tên nhân viên',
  `Sdt` varchar(15) DEFAULT NULL COMMENT 'Số điện thoại',
  `GioiTinh` enum('NAM','NU') DEFAULT NULL,
  `ChucVu` varchar(50) DEFAULT NULL COMMENT 'Chức vụ',
  PRIMARY KEY (`MaNhanVien`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='Nhân viên';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `a0_nhanvien`
--

LOCK TABLES `a0_nhanvien` WRITE;
/*!40000 ALTER TABLE `a0_nhanvien` DISABLE KEYS */;
INSERT INTO `a0_nhanvien` VALUES ('NV001','1','44','NAM','1'),('NV002','2','2','NAM','2');
/*!40000 ALTER TABLE `a0_nhanvien` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `a1_vaitro`
--

DROP TABLE IF EXISTS `a1_vaitro`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `a1_vaitro` (
  `role_id` int NOT NULL AUTO_INCREMENT,
  `role_name` varchar(255) DEFAULT NULL,
  `description` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`role_id`),
  UNIQUE KEY `name` (`role_name`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='Vai trò';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `a1_vaitro`
--

LOCK TABLES `a1_vaitro` WRITE;
/*!40000 ALTER TABLE `a1_vaitro` DISABLE KEYS */;
INSERT INTO `a1_vaitro` VALUES (0,'ADMIN','Nguoi quan ly'), (1, 'USER', 'user');
/*!40000 ALTER TABLE `a1_vaitro` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `a2_taikhoan`
--

DROP TABLE IF EXISTS `a2_taikhoan`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `a2_taikhoan` (
  `id` int NOT NULL AUTO_INCREMENT,
  `username` varchar(50) NOT NULL,
  `password` varchar(255) NOT NULL,
  `nhanvien_id` varchar(50) NOT NULL,
  `role_id` int DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `username` (`username`),
  KEY `fk_a2_taikhoan_nhanvien` (`nhanvien_id`),
  KEY `fk_a2_taikhoan_role` (`role_id`),
  CONSTRAINT `fk_a2_taikhoan_nhanvien` FOREIGN KEY (`nhanvien_id`) REFERENCES `a0_nhanvien` (`MaNhanVien`) ON DELETE RESTRICT ON UPDATE CASCADE,
  CONSTRAINT `fk_a2_taikhoan_role` FOREIGN KEY (`role_id`) REFERENCES `a1_vaitro` (`role_id`) ON DELETE RESTRICT ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='Tài khoản';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `a2_taikhoan`
--

LOCK TABLES `a2_taikhoan` WRITE;
/*!40000 ALTER TABLE `a2_taikhoan` DISABLE KEYS */;
INSERT INTO `a2_taikhoan` VALUES (1,'ADMIN','ADMIN','NV001',0),(2,'NV002','NV002','NV002',1);
/*!40000 ALTER TABLE `a2_taikhoan` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2025-11-02 17:09:39
