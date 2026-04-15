# 🇻ᴴ🇳 🇫 🇱 🇦 🇲 🇪 🇦 🇳 🇹 🇮 🇻 🇵 🇳
### VPN/Proxy Detection System for Minecraft Servers

---

<p align="center">
  <img src="https://img.shields.io/badge/Java-17%2B-brightgreen?style=for-the-badge&logo=java" alt="Java">
  <img src="https://img.shields.io/badge/Version-1.0.0-blue?style=for-the-badge" alt="Version">
  <img src="https://img.shields.io/badge/Author-boykisser%20clan%20and%20RIN-red?style=for-the-badge" alt="Author">
  <br>
  <img src="https://img.shields.io/badge/Supports-BungeeCord%20%7C%20Velocity%20%7C%20Paper-brightgreen?style=for-the-badge" alt="Supported Platforms">
</p>

---

## ꜱᴍᴀʟʟ ᴄᴀᴘꜱ ꜰᴏɴᴛ ꜱᴛʏʟᴇ!

This plugin uses **small caps unicode font** throughout for a unique aesthetic:

```
ʀꜰʟᴀᴍᴇᴀɴᴛɪᴠᴘɴ
ᴠᴘɴ/ᴘʀᴏxʏ ᴅᴇᴛᴇᴄᴛɪᴏɴ
```

---

## ꜰᴇᴀᴛᴜʀᴇꜱ

| Feature | Description |
|---------|------------|
| 🔍 **API Check** | Real-time VPN/Proxy/Tor detection via rinantivpn-premium.vercel.app API |
| 💾 **Session Cache** | Caffeine cache with configurable TTL (default 30 min) |
| 📋 **Whitelist** | Player name, UUID, IP, and CIDR range support |
| 🚫 **Auto-Kick** | Disconnect detected players with custom message |
| 💬 **Discord Webhook** | Get alerts when players are blocked |
| ⚡ **Async** | Non-blocking IP checks - zero lag on join |

---

## ꜱᴜᴘᴘᴏʀᴛᴇᴅ ᴘʟᴀᴛꜰᴏʀᴍꜱ

- ✅ **BungeeCord** - Put in `plugins/` folder
- ✅ **Velocity** - Put in `plugins/` folder  
- ✅ **Paper/Spigot/Purpur** - Works via proxy

---

## 🛠️ ꜱᴛᴀʀᴛᴜᴘ

1. Download `RFrameAntiVPN-BungeeCord-1.0.0.jar`
2. Place in your proxy's `plugins/` folder
3. Restart proxy
4. Edit `config.yml` with your API key

---

## ⚙️ ꜱᴏɴꜰɪɢᴜʀᴀᴛɪᴏɴ

```yaml
api:
  url: "https://rinantivpn-premium.vercel.app/api/check"
  key: "YOUR-API-KEY-HERE"  # Change this!
  timeout: 5
  fail-open: true

session:
  cache-minutes: 30
  max-size: 10000

detection:
  block-vpn: true
  block-proxy: true
  block-tor: true

permissions:
  bypass: "rflameantivpn.bypass"
  admin: "rflameantivpn.admin"
```

---

## 📋 ꜰᴇʀᴍɪꜱꜱɪᴏɴꜱ

| Permission | Description |
|------------|-------------|
| `rflameantivpn.bypass` | Bypass VPN check |
| `rflameantivpn.admin` | Access all commands |

---

## 💬 ꜱᴏᴍᴍᴀɴᴅꜱ

| Command | Description |
|---------|-------------|
| `/rvpn` | Show plugin info |
| `/rvpn check <player\|ip>` | Check an IP |
| `/rvpn whitelist add <player\|ip\|cidr>` | Add to whitelist |
| `/rvpn whitelist remove <player\|ip\|cidr>` | Remove from whitelist |
| `/rvpn whitelist list` | Show whitelist |
| `/rvpn cache clear` | Clear session cache |
| `/rvpn cache stats` | Show cache stats |
| `/rvpn reload` | Reload config |

---

## 🚫 Kick Message (Small Caps)

```
§c§lʀꜰʟᴀᴍᴇ§r§cᴀɴᴛɪᴠᴘɴ §8— §cᴄᴏɴɴᴇᴄᴛɪᴏɴ §cʙʟᴏᴄᴋᴇᴅ

§7ʏᴏᴜʀ ɪᴘ ᴡᴀꜱ ᴅᴇᴛᴇᴄᴛᴇᴅ ᴀꜱ ᴀ §cVPN §7/ §cᴘʀᴏxʏ

§fᴛᴏ ᴊᴏɪɴ ᴛʜɪꜱ ꜱᴇʀᴠᴇʀ§8:
§7① §fᴅɪꜱᴀʙʟᴇ ʏᴏᴜʀ §cVPN §f/ §cᴘʀᴏxʏ
§7② §fʀᴇᴄᴏɴɴᴇᴄᴛ ᴡɪᴛʜ ʏᴏᴜʀ §aʀᴇᴀʟ ɪᴘ

§7ɪꜰ ʏᴏᴜ ᴅᴏ ɴᴏᴛ ʜᴀᴠᴇ ᴀ §cVPN §7ᴇɴᴀʙʟᴇᴅ§8,
§7ᴄᴏɴᴛᴀᴄᴛ ᴀɴ §bᴀᴅᴍɪɴ §7ꜰᴏʀ ᴀ §aᴡʜɪᴛᴇʟɪꜱᴛ
```

---

## 🏗️ Build

```bash
gradle :bungeecord:build
```

Output: `bungeecord/build/libs/RFrameAntiVPN-1.0.0.jar`

---

## 📝 License

MIT License - Feel free to modify and distribute!

---

**Made with 🩷 by boykisser clan and RIN my team boykisser clan my NAME RIN**