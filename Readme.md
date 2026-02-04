# Hướng dẫn dự án Spring Boot + JSP 4 tầng (MRSC)

## Giới thiệu

Đây là dự án backend sử dụng Spring Boot, kết hợp JSP để xây dựng giao diện web. Cấu trúc dự án được chia thành 4 tầng theo mô hình MRSC: Model – Repository – Service – Controller. Mỗi tầng đảm nhận một nhiệm vụ riêng để đảm bảo tính rõ ràng, dễ mở rộng và dễ bảo trì.

## Yêu cầu hệ thống

* JDK 18 hoặc cao hơn
* Maven 3.8+
* MySQL 8+ (hoặc H2 Database cho môi trường thử nghiệm)
* IDE (IntelliJ IDEA, Eclipse hoặc VS Code)

## Cấu trúc dự án

1. **Model (modal/)** : Chứa các class Entity ánh xạ với bảng trong cơ sở dữ liệu.
2. **Repository (repository/)** : Kế thừa `JpaRepository` để thao tác CRUD với database.
3. **Service (service/)** : Xử lý logic nghiệp vụ, gọi Repository và trả dữ liệu cho Controller.
4. **Controller (controller/)** : Xử lý request từ người dùng, gọi Service, trả kết quả về JSP.
5. **Resources (src/main/resources/)** : Chứa cấu hình `application.properties`.
6. **Webapp (src/main/webapp/WEB-INF/jsp/)** : Chứa file JSP cho giao diện.

```
\spring-boot-jsp
│
├─ .idea/
│
├─ src/
│  └─ main/
│     ├─ java/
|     |   └─ springbootjsp/
│     |	      ├─ controller/
│     |	      ├─ model/
│     |	      ├─ repository/
│     |	      ├─ service/
│     |       └─ TestApplication.java
│     |   
│     ├─ resources/
|     |   └─ application.properties
|     |
│     └─ webapp/
|         └─ WEB-INF/
│      	      |─ jsp/ # Frontend cũ
│      	      |   ├─ user-list.jsp
│      	      |   ├─ nhanvien-list.jsp
│      	      |   ├─ phong-list.jsp
│      	      |   ├─ hoadon-list.jsp
│      	      |   └─ .....
│      	      |
│      	      └─ views/ # Frontend mới
│      	          ├─ common/
│      	          |   └─list.jsp
│      	          ├─ user/
│      	          |   └─list-wrapper.jsp
│      	          ├─ nhanvien/
│      	          |   └─list-wrapper.jsp
│      	          ├─ phong/
│      	          |   └─list-wrapper.jsp
│      	          ├─ hoadon/
│      	          |   └─list-wrapper.jsp
│      	          └─ .....
│
├─ target/
├─ .gitignore
├─ nb-configuration.xml
├─ nbactions.xml
├─ pom.xml
└─ README.md
```

## Các dependency chính trong Maven

* Spring Boot Starter Data JPA: làm việc với database.
* Spring Boot Starter Web: hỗ trợ REST API và MVC.
* Spring Boot Starter Validation: kiểm tra dữ liệu đầu vào.
* MySQL Connector J: kết nối MySQL.
* Tomcat Embed Jasper + JSTL: hỗ trợ JSP.
* H2 Database: database in-memory để test nhanh.

## Cấu hình kết nối CSDL (application.properties)

* `spring.datasource.url`: URL kết nối MySQL.
* `spring.datasource.username`: Tài khoản MySQL.
* `spring.datasource.password`: Mật khẩu.
* `spring.datasource.driver-class-name`: Driver JDBC.
* `spring.jpa.hibernate.naming.physical-strategy`: Giữ tên bảng, cột như trong Entity.
* `spring.jpa.show-sql`: In ra câu lệnh SQL khi chạy.
* `spring.jpa.properties.hibernate.dialect`: Dialect của MySQL 8.
* `spring.mvc.view.prefix`: Thư mục chứa JSP.
* `spring.mvc.view.suffix`: Định dạng file giao diện JSP.

## Cách chạy dự án

1. Clone dự án từ repository.
2. Import dự án vào IDE dưới dạng Maven project.
3. Cấu hình MySQL, tạo database `ql_khachsan`.
4. Chỉnh sửa thông tin trong `application.properties` nếu cần.
5. Chạy dự án bằng lệnh:
   * Trong IDE: chạy class có annotation `@SpringBootApplication`.
   * Trong terminal: `mvn spring-boot:run`.
6. Truy cập ứng dụng tại: `http://localhost:8080/`.

## Ghi chú

* Nếu muốn dùng H2 thay cho MySQL, chỉ cần bỏ comment cấu hình H2 trong `pom.xml` và cấu hình `application.properties`.
* Tầng Service là nơi đặt logic nghiệp vụ, nên tránh viết trực tiếp trong Controller.
* JSP chỉ để hiển thị, không xử lý logic.
