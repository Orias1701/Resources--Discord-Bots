### IMPORTANT:  Structure

```
resources-discord-bot/
│
├── assets/
│
├── data/
│
├── img/
│
├── public/
│
├── src/
│   ├── index.js            		# File chạy chính
│   ├── deploy-commands.js  		# Đăng ký lệnh Slash
│   ├── config.js           		# Cấu hình chung
│   ├── commands/           		# Thư mục chứa lệnh
│   │   ├── moderation/     		# Chứa kick.js, ban.js...
│   │   ├── fun/            		# Chứa dice.js, coinflip.js...
│   │   └── utility/        		# Chứa ping.js, userinfo.js...
│   └── events/             		# Thư mục chứa sự kiện
│       ├── ready.js        		# Sự kiện khi bot bật lên
│       └── interactionCreate.js 	# Sự kiện khi người dùng gõ lệnh
│
├── test/
│
├── .dockerignore
├── .env
├── .gitignore
├── Dockerfile
└── package.json
```

### NOTE
