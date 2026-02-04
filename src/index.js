// src/index.js

require('dotenv').config(); // Nạp biến môi trường
const { Client, GatewayIntentBits, Collection } = require('discord.js');
const fs = require('node:fs');
const path = require('node:path');

// Khởi tạo Client với các quyền (Intents) cần thiết
// Guilds: Quyền cơ bản để bot hoạt động trong server
// GuildMessages: Quyền đọc tin nhắn (cần nếu dùng prefix cũ, slash command thì ít cần hơn)
// MessageContent: Quyền đọc nội dung tin nhắn (cần bật trong Developer Portal)
const client = new Client({
    intents: [
        GatewayIntentBits.Guilds,
        GatewayIntentBits.GuildMessages,
        GatewayIntentBits.MessageContent,
        GatewayIntentBits.GuildMembers // Cần cho tính năng Welcome
    ]
});

// Tạo một Collection để chứa các lệnh
client.commands = new Collection();

// --- ĐOẠN CODE NÀY SẼ ĐƯỢC MỞ RỘNG Ở BƯỚC SAU ---
// Chúng ta sẽ viết logic để tự động đọc file trong folder 'commands'
// và 'events' tại đây. Tạm thời để trống để đảm bảo bot chạy được đã.

client.once('ready', () => {
    console.log(`✅ Bot đã online! Đăng nhập dưới tên: ${client.user.tag}`);
    client.user.setActivity('đang phục vụ Server riêng');
});

client.on('interactionCreate', async interaction => {
    if (!interaction.isChatInputCommand()) return;

    // Xử lý phản hồi tạm thời để test bot
    if (interaction.commandName === 'ping') {
        await interaction.reply('Pong! Bot đang hoạt động tốt.');
    }
});

// Đăng nhập bot
client.login(process.env.DISCORD_TOKEN);