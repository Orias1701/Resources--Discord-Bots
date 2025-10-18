// Cloudflare Worker: ping Render every 10 minutes
const renderURL = "https://your-app-name.onrender.com";

export default {
  async scheduled(controller, env, ctx) {
    try {
      const res = await fetch(renderURL);
      console.log("Ping OK:", await res.text());
    } catch (e) {
      console.error("Ping failed:", e);
    }
  },
};
