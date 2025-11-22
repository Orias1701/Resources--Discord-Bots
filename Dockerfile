# Sử dụng Node.js v18 phiên bản nhẹ (alpine)
FROM node:18-alpine

# Tạo thư mục làm việc trong container
WORKDIR /usr/src/app

# Copy file package.json và package-lock.json trước (để tận dụng Docker cache)
COPY package*.json ./

# Cài đặt các thư viện (dependencies)
RUN npm install --production

# Copy toàn bộ mã nguồn vào container
COPY . .

# Bot không cần expose port như web server, nhưng nếu Render yêu cầu, có thể để (tùy chọn)
# EXPOSE 3000 

# Lệnh chạy bot khi container khởi động
CMD ["npm", "start"]