# Lộ Trình Xây Dựng Discord Bot (Docker + Render)

Đây là lộ trình từng bước để xây dựng bot từ con số 0 đến khi chạy ổn định trên Render.

## Giai đoạn 1: Khởi tạo & Cấu hình (Hiện tại)

* [X] Tạo cấu trúc thư mục dự án.
* [X] Thiết lập `package.json` và cài đặt thư viện (`discord.js`, `dotenv`).
* [X] Viết `Dockerfile` để đóng gói ứng dụng.
* [X] Tạo Bot trên Discord Developer Portal để lấy Token.

## Giai đoạn 2: Bộ não của Bot (Core)

* [ ] Viết `src/index.js`: Khởi tạo Client, đăng nhập Bot.
* [ ] Thiết lập  **Command Handler** : Giúp tự động đọc các file lệnh trong thư mục `commands` (thay vì viết hết vào một file).
* [ ] Thiết lập  **Event Handler** : Quản lý các sự kiện (khi bot online, khi có tin nhắn đến).

## Giai đoạn 3: Phát triển Tính năng (Feature Implementation)

*Sau khi Core chạy ổn, ta sẽ code từng file lệnh nhỏ:*

* [ ] **Nhóm System:** Lệnh `/ping`, `/help`.
* [ ] **Nhóm Moderation:** Lệnh `/kick`, `/ban`, `/clear`, `/timeout`.
* [ ] **Nhóm Utility:** Lệnh `/userinfo`, `/avatar`.
* [ ] **Nhóm Fun:** Lệnh `/dice`, `/coinflip`.
* [ ] **Sự kiện:** Tự động chào mừng thành viên mới (GuildMemberAdd).

## Giai đoạn 4: Deployment (Render)

* [ ] Đẩy code lên GitHub (Private Repository).
* [ ] Kết nối Render với GitHub.
* [ ] Cấu hình biến môi trường (Environment Variables) trên Render.
* [ ] Deploy và tận hưởng.

---

## 1. Chuẩn bị (Prerequisites)

Trước khi bắt đầu, bạn cần cài đặt:

* [Node.js](https://nodejs.org/ "null") (Phiên bản 16.9.0 trở lên, khuyến nghị v18+).
* [Visual Studio Code](https://code.visualstudio.com/ "null").
* Tài khoản [Discord Developer Portal](https://www.google.com/search?q=https://discord.com/developers/applications "null") để tạo Bot và lấy **Token** &  **Client ID** .

## 2. Cài đặt Local (Chạy thử trên máy)

1. **Clone hoặc tải code về máy.**
2. **Cài đặt thư viện:**
   Mở terminal tại thư mục dự án và chạy:
   ```
   npm install
   ```
3. **Cấu hình môi trường:**
   * Đổi tên file `.env.example` thành `.env`.
   * Điền `DISCORD_TOKEN` và `CLIENT_ID` vào file `.env`.
4. **Đăng ký lệnh (Quan trọng):**
   Mỗi khi bạn tạo một file lệnh mới (ví dụ `/ping`), bạn cần chạy lệnh này để cập nhật cho Discord biết:
   ```
   npm run deploy
   ```
5. **Chạy Bot:**
   ```
   npm start
   ```

## 3. Cấu hình Docker (Dành cho Render)

File `Dockerfile` đã được cấu hình tối ưu cho Node.js.

1. Đảm bảo file `.dockerignore` đã loại bỏ `node_modules` và `.env`.
2. Khi chạy trên Render, Render sẽ tự động đọc `Dockerfile` để build.

## 4. Deploy lên Render

1. Đẩy code lên **GitHub** (Khuyên dùng Private Repo để bảo mật Token nếu bạn lỡ commit file .env, nhưng tốt nhất là thêm .env vào .gitignore).
2. Vào [Dashboard Render](https://dashboard.render.com/ "null"), chọn **New +** -> **Background Worker** (Dùng Background Worker vì Bot cần chạy liên tục, không phải Web Server).
   * *Lưu ý:* Nếu dùng gói Free của Render, Bot có thể bị tắt sau 15 phút nếu không dùng gói Web Service có ping giữ nhịp. Tuy nhiên, chuẩn nhất là dùng Background Worker.
3. Kết nối với Repo GitHub của bạn.
4. **Runtime:** Docker.
5. **Environment Variables (Quan trọng):**
   Vào tab "Environment" trên Render, thêm các biến sau (lấy từ file .env của bạn):
   * `DISCORD_TOKEN`: token_cua_ban
   * `CLIENT_ID`: id_cua_bot

## Lưu ý về tính năng

* Bot sử dụng **Slash Commands** (`/`) hiện đại.
* Cấu trúc code dạng Module, mỗi lệnh là 1 file riêng biệt.
