// src/deploy-commands.js

require('dotenv').config();
const { REST, Routes } = require('discord.js');

// Danh sách lệnh cơ bản để test trước
const commands = [
    {
        name: 'ping',
        description: 'Kiểm tra xem bot có phản hồi không',
    },
];

// Chuẩn bị REST module
const rest = new REST({ version: '10' }).setToken(process.env.DISCORD_TOKEN);

// Hàm đăng ký lệnh
(async () => {
    try {
        console.log('Đang bắt đầu làm mới các lệnh ứng dụng (/).');

        // Đăng ký cho một Server cụ thể (Cập nhật tức thì - Khuyên dùng cho Dev/Server riêng)
        if (process.env.GUILD_ID) {
             await rest.put(
                Routes.applicationGuildCommands(process.env.CLIENT_ID, process.env.GUILD_ID),
                { body: commands },
            );
            console.log(`Đã đăng ký thành công lệnh cho Server ID: ${process.env.GUILD_ID}`);
        } else {
            // Đăng ký toàn cục (Global) - Mất tới 1 giờ để cập nhật, dùng cho Public Bot
            await rest.put(
                Routes.applicationCommands(process.env.CLIENT_ID),
                { body: commands },
            );
            console.log('Đã đăng ký thành công lệnh toàn cục.');
        }

    } catch (error) {
        console.error(error);
    }
})();