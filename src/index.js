// ================================
// Discord Music Bot for Render
// ================================
import express from "express";
import dotenv from "dotenv";
import { Client, GatewayIntentBits } from "discord.js";
import { joinVoiceChannel, createAudioPlayer, createAudioResource, AudioPlayerStatus, getVoiceConnection } from "@discordjs/voice";
import play from "play-dl";

dotenv.config();

const app = express();
app.get("/", (_, res) => res.send("🎵 Bot đang hoạt động!"));
app.listen(10000, () => console.log("🌐 Web server on port 10000"));

// -------------------------------
// Discord Bot Init
// -------------------------------
const client = new Client({
  intents: [
    GatewayIntentBits.Guilds,
    GatewayIntentBits.GuildMessages,
    GatewayIntentBits.MessageContent,
    GatewayIntentBits.GuildVoiceStates,
  ],
});

let player = createAudioPlayer();
let currentConnection = null;
let currentResource = null;

// -------------------------------
// Helper: Play music
// -------------------------------
async function playMusic(message, url) {
  const channel = message.member?.voice?.channel;
  if (!channel) return message.reply("⚠️ Bạn cần vào kênh thoại trước!");

  try {
    const yt_info = await play.video_info(url);
    const stream = await play.stream_from_info(yt_info);
    currentResource = createAudioResource(stream.stream, { inputType: stream.type });

    // Join VC nếu chưa có
    currentConnection = joinVoiceChannel({
      channelId: channel.id,
      guildId: message.guild.id,
      adapterCreator: message.guild.voiceAdapterCreator,
    });

    player.play(currentResource);
    currentConnection.subscribe(player);

    message.reply(`🎶 Đang phát: **${yt_info.video_details.title}**`);
  } catch (err) {
    console.error(err);
    message.reply("❌ Không thể phát link này!");
  }
}

// -------------------------------
// Commands
// -------------------------------
client.on("messageCreate", async (msg) => {
  if (!msg.content.startsWith("!")) return;
  const [cmd, ...args] = msg.content.trim().split(" ");

  switch (cmd) {
    case "!play":
      if (!args.length) return msg.reply("❗ Dán link YouTube sau `!play`");
      return await playMusic(msg, args[0]);

    case "!pause":
      player.pause();
      return msg.reply("⏸️ Đã tạm dừng nhạc!");

    case "!resume":
      player.unpause();
      return msg.reply("▶️ Tiếp tục phát!");

    case "!stop":
      player.stop();
      if (currentConnection) currentConnection.destroy();
      return msg.reply("🛑 Đã dừng và rời kênh thoại!");
  }
});

// -------------------------------
// Discord Login
// -------------------------------
client.once("ready", () => console.log(`✅ Logged in as ${client.user.tag}`));
client.login(process.env.TOKEN);
