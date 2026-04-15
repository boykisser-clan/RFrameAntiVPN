package dev.rflame.antivpn.common;

public class MessageBuilder {

    public static String buildKickMessage(VPNResult result, String playerName, String serverName, String discord) {
        return
            "§r\n" +
            "§c§lʀꜰʟᴀᴍᴇ§r§cᴀɴᴛɪᴠᴘɴ §8— §cᴄᴏɴɴᴇᴄᴛɪᴏɴ §cʙʟᴏᴄᴋᴇᴅ\n" +
            "\n" +
            "§7ʏᴏᴜʀ ɪᴘ ᴡᴀꜱ ᴅᴇᴛᴇᴄᴛᴇᴅ ᴀꜱ ᴀ §cVPN §7/ §cᴘʀᴏxʏ\n" +
            "\n" +
            "§fᴛᴏ ᴊᴏɪɴ ᴛʜɪꜱ ꜱᴇʀᴠᴇʀ§8:\n" +
            "§7① §fᴅɪꜱᴀʙʟᴇ ʏᴏᴜʀ §cVPN §f/ §cᴘʀᴏxʏ\n" +
            "§7② §fʀᴇᴄᴏɴɴᴇᴄᴛ ᴡɪᴛʜ ʏᴏᴜʀ §aʀᴇᴀʟ ɪᴘ\n" +
            "\n" +
            "§7ɪꜰ ʏᴏᴜ ᴅᴏ ɴᴏᴛ ʜᴀᴠᴇ ᴀ §cVPN §7ᴇɴᴀʙʟᴇᴅ§8,\n" +
            "§7ᴄᴏɴᴛᴀᴄᴛ ᴀɴ §bᴀᴅᴍɪɴ §7ꜰᴏʀ ᴀ §aᴡʜɪᴛᴇʟɪꜱᴛ\n" +
            "\n" +
            "§8» §7ɪᴘ§8: §c" + result.getIp() + "\n" +
            "§8» §7ᴄᴏᴜɴᴛʀʏ§8: §f" + result.getCountry() + "§r\n" +
            "§8» §7ɪꜱᴘ§8: §f" + result.getIsp() + "§r\n";
    }

    public static String buildCheckResult(String prefix, VPNResult result, String target) {
        String status = result.isDetected() ? "§cʙʟᴏᴄᴋᴇᴅ" : "§aᴄʟᴇᴀɴ";
        return prefix + " ʀᴇꜱᴜʟᴛ ꜰᴏʀ §f" + target + "\n" +
            "§8» §7ɪᴘ§8: §c" + result.getIp() + "\n" +
            "§8» §7ᴠᴘɴ§8: §c" + result.isVPN() + "\n" +
            "§8» §7ᴘʀᴏxʏ§8: §c" + result.isProxy() + "\n" +
            "§8» §7ᴛᴏʀ§8: §c" + result.isTor() + "\n" +
            "§8» §7ᴄᴏᴜɴᴛʀʏ§8: §f" + result.getCountry() + "\n" +
            "§8» §7ɪꜱᴘ§8: §f" + result.getIsp() + "\n" +
            "§8» §7ꜱᴛᴀᴛᴜꜱ§8: " + status;
    }

    public static String buildStats(String prefix, long checked, long blocked, long cacheSize) {
        return prefix + " §7ꜱᴛᴀᴛɪꜱᴛɪᴄꜱ\n" +
            "§8» §7ᴛᴏᴛᴀʟ ᴄʜᴇᴄᴋᴇᴅ§8: §f" + checked + "\n" +
            "§8» §7ʙʟᴏᴄᴋᴇᴅ§8: §c" + blocked + "\n" +
            "§8» §7ᴄᴀᴄʜᴇ ꜱɪᴢᴇ§8: §f" + cacheSize;
    }

    public static String buildWhitelistList(String prefix, WhitelistManager whitelist) {
        StringBuilder sb = new StringBuilder();
        sb.append(prefix).append(" §7ᴡʜɪᴛᴇʟɪꜱᴛ\n");

        if (!whitelist.getPlayers().isEmpty()) {
            sb.append("§8» §7ᴘʟᴀʏᴇʀꜱ§8: §f").append(String.join(", ", whitelist.getPlayers())).append("\n");
        }
        if (!whitelist.getIps().isEmpty()) {
            sb.append("§8» §7ɪᴘꜱ§8: §f").append(String.join(", ", whitelist.getIps())).append("\n");
        }
        if (!whitelist.getIpRanges().isEmpty()) {
            sb.append("§8» §7ɪᴘ ʀᴀɴɢᴇꜱ§8: §f").append(String.join(", ", whitelist.getIpRanges()));
        }

        return sb.toString();
    }

    public static String createLine() {
        return "§8§m───────────────────────────────────────";
    }
}