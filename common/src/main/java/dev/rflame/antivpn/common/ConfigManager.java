package dev.rflame.antivpn.common;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;

public class ConfigManager {

    private final File file;
    private final Gson gson = new GsonBuilder().setPrettyPrinting().create();
    private Map<String, Object> config;

    public ConfigManager(File file) {
        this.file = file;
        this.config = load();
    }

    @SuppressWarnings("unchecked")
    private Map<String, Object> load() {
        try {
            if (file.exists()) {
                try (FileReader reader = new FileReader(file)) {
                    Type type = new com.google.gson.reflect.TypeToken<Map<String, Object>>() {}.getType();
                    Map<String, Object> loaded = gson.fromJson(reader, type);
                    if (loaded != null) {
                        return loaded;
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return createDefault();
    }

    private Map<String, Object> createDefault() {
        Map<String, Object> config = new HashMap<>();

        Map<String, Object> api = new HashMap<>();
        api.put("url", "https://rinantivpn-premium.vercel.app/api/check");
        api.put("key", "PREMIUM-KEY113");
        api.put("timeout", 5);
        api.put("fail-open", true);
        config.put("api", api);

        Map<String, Object> session = new HashMap<>();
        session.put("cache-minutes", 30);
        session.put("max-size", 10000);
        config.put("session", session);

        Map<String, Object> detection = new HashMap<>();
        detection.put("block-vpn", true);
        detection.put("block-proxy", true);
        detection.put("block-tor", true);
        config.put("detection", detection);

        Map<String, Object> kickMessage = new HashMap<>();
        kickMessage.put("use-default", true);
        kickMessage.put("custom", "");
        kickMessage.put("server-name", "KRUD SMP");
        kickMessage.put("discord", "discord.gg/krudsmp");
        config.put("kick-message", kickMessage);

        Map<String, Object> discord = new HashMap<>();
        discord.put("enabled", false);
        discord.put("webhook-url", "");
        config.put("discord", discord);

        Map<String, Object> logging = new HashMap<>();
        logging.put("console", true);
        logging.put("file", true);
        logging.put("log-file", "logs/rflameantivpn.log");
        config.put("logging", logging);

        Map<String, Object> permissions = new HashMap<>();
        permissions.put("bypass", "rflameantivpn.bypass");
        permissions.put("admin", "rflameantivpn.admin");
        config.put("permissions", permissions);

        Map<String, Object> messages = new HashMap<>();
        messages.put("prefix", "§8[§cʀꜰʟᴀᴍᴇ§7ᴀɴᴛɪᴠᴘɴ§8]");
        messages.put("reloaded", "§aᴄᴏɴꜱɪɢ ʀᴇʟᴏᴀᴅᴇᴅ!");
        messages.put("no-permission", "§cɴᴏ ᴘᴇʀᴍɪꜱꜱɪᴏɴ!");
        messages.put("player-not-found", "§cᴘʟᴀʏᴇʀ ɴᴏᴛ ꜰᴏᴜɴᴅ!");
        messages.put("whitelisted", "§a{target} ᴀᴅᴅᴇᴅ ᴛᴏ ᴡʜɪᴛᴇʟɪꜱᴛ!");
        messages.put("unwhitelisted", "§c{target} ʀᴇᴍᴏᴠᴇᴅ ꜰʀᴏᴅ ᴡʜɪᴛᴇʟɪꜱᴛ!");
        messages.put("cache-cleared", "§aꜱᴇꜱꜱɪᴏɴ ᴄᴀᴄʜᴇ ᴄʟᴇᴀʀᴇᴅ!");
        config.put("messages", messages);

        save(config);
        return config;
    }

    public void save(Map<String, Object> config) {
        try (FileWriter writer = new FileWriter(file)) {
            gson.toJson(config, writer);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void reload() {
        this.config = load();
    }

    @SuppressWarnings("unchecked")
    public String getApiUrl() {
        Map<String, Object> api = (Map<String, Object>) config.get("api");
        return api != null ? (String) api.get("url") : "https://rinantivpn-premium.vercel.app/api/check";
    }

    @SuppressWarnings("unchecked")
    public String getApiKey() {
        Map<String, Object> api = (Map<String, Object>) config.get("api");
        return api != null ? (String) api.get("key") : "PREMIUM-KEY113";
    }

    @SuppressWarnings("unchecked")
    public int getApiTimeout() {
        Map<String, Object> api = (Map<String, Object>) config.get("api");
        return api != null ? (int) api.getOrDefault("timeout", 5) : 5;
    }

    @SuppressWarnings("unchecked")
    public boolean isFailOpen() {
        Map<String, Object> api = (Map<String, Object>) config.get("api");
        return api != null && (boolean) api.getOrDefault("fail-open", true);
    }

    @SuppressWarnings("unchecked")
    public int getCacheMinutes() {
        Map<String, Object> session = (Map<String, Object>) config.get("session");
        return session != null ? (int) session.getOrDefault("cache-minutes", 30) : 30;
    }

    @SuppressWarnings("unchecked")
    public int getMaxCacheSize() {
        Map<String, Object> session = (Map<String, Object>) config.get("session");
        return session != null ? (int) session.getOrDefault("max-size", 10000) : 10000;
    }

    @SuppressWarnings("unchecked")
    public boolean isBlockVpn() {
        Map<String, Object> detection = (Map<String, Object>) config.get("detection");
        return detection != null && (boolean) detection.getOrDefault("block-vpn", true);
    }

    @SuppressWarnings("unchecked")
    public boolean isBlockProxy() {
        Map<String, Object> detection = (Map<String, Object>) config.get("detection");
        return detection != null && (boolean) detection.getOrDefault("block-proxy", true);
    }

    @SuppressWarnings("unchecked")
    public boolean isBlockTor() {
        Map<String, Object> detection = (Map<String, Object>) config.get("detection");
        return detection != null && (boolean) detection.getOrDefault("block-tor", true);
    }

    @SuppressWarnings("unchecked")
    public boolean isUseDefaultKickMessage() {
        Map<String, Object> kickMessage = (Map<String, Object>) config.get("kick-message");
        return kickMessage != null && (boolean) kickMessage.getOrDefault("use-default", true);
    }

    @SuppressWarnings("unchecked")
    public String getCustomKickMessage() {
        Map<String, Object> kickMessage = (Map<String, Object>) config.get("kick-message");
        return kickMessage != null ? (String) kickMessage.getOrDefault("custom", "") : "";
    }

    @SuppressWarnings("unchecked")
    public String getServerName() {
        Map<String, Object> kickMessage = (Map<String, Object>) config.get("kick-message");
        return kickMessage != null ? (String) kickMessage.getOrDefault("server-name", "KRUD SMP") : "KRUD SMP";
    }

    @SuppressWarnings("unchecked")
    public String getDiscord() {
        Map<String, Object> kickMessage = (Map<String, Object>) config.get("kick-message");
        return kickMessage != null ? (String) kickMessage.getOrDefault("discord", "discord.gg/krudsmp") : "discord.gg/krudsmp";
    }

    @SuppressWarnings("unchecked")
    public boolean isWebhookEnabled() {
        Map<String, Object> discord = (Map<String, Object>) config.get("discord");
        return discord != null && (boolean) discord.getOrDefault("enabled", false);
    }

    @SuppressWarnings("unchecked")
    public String getWebhookUrl() {
        Map<String, Object> discord = (Map<String, Object>) config.get("discord");
        return discord != null ? (String) discord.getOrDefault("webhook-url", "") : "";
    }

    @SuppressWarnings("unchecked")
    public boolean isConsoleLog() {
        Map<String, Object> logging = (Map<String, Object>) config.get("logging");
        return logging != null && (boolean) logging.getOrDefault("console", true);
    }

    @SuppressWarnings("unchecked")
    public boolean isFileLog() {
        Map<String, Object> logging = (Map<String, Object>) config.get("logging");
        return logging != null && (boolean) logging.getOrDefault("file", true);
    }

    @SuppressWarnings("unchecked")
    public String getLogFile() {
        Map<String, Object> logging = (Map<String, Object>) config.get("logging");
        return logging != null ? (String) logging.getOrDefault("log-file", "logs/rflameantivpn.log") : "logs/rflameantivpn.log";
    }

    @SuppressWarnings("unchecked")
    public String getBypassPermission() {
        Map<String, Object> permissions = (Map<String, Object>) config.get("permissions");
        return permissions != null ? (String) permissions.getOrDefault("bypass", "rflameantivpn.bypass") : "rflameantivpn.bypass";
    }

    @SuppressWarnings("unchecked")
    public String getAdminPermission() {
        Map<String, Object> permissions = (Map<String, Object>) config.get("permissions");
        return permissions != null ? (String) permissions.getOrDefault("admin", "rflameantivpn.admin") : "rflameantivpn.admin";
    }

    @SuppressWarnings("unchecked")
    public String getPrefix() {
        Map<String, Object> messages = (Map<String, Object>) config.get("messages");
        return messages != null ? (String) messages.getOrDefault("prefix", "§8[§cʀꜰʟᴀᴍᴇ§7ᴀɴᴛɪᴠᴘɴ§8]") : "§8[§cʀꜰʟᴀᴍᴇ§7ᴀɴᴛɪᴠᴘɴ§8]";
    }

    @SuppressWarnings("unchecked")
    public String getMessage(String key) {
        Map<String, Object> messages = (Map<String, Object>) config.get("messages");
        if (messages != null && messages.containsKey(key)) {
            return (String) messages.get(key);
        }
        return "";
    }
}