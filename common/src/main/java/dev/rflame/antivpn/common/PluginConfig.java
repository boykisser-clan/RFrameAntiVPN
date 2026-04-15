package dev.rflame.antivpn.common;

public class PluginConfig {

    private String apiUrl = "https://rinantivpn-premium.vercel.app/api/check";
    private String apiKey = "PREMIUM-KEY113";
    private int apiTimeout = 5;
    private boolean failOpen = true;

    private int cacheMinutes = 30;
    private int maxCacheSize = 10000;

    private boolean blockVpn = true;
    private boolean blockProxy = true;
    private boolean blockTor = true;

    private boolean useDefaultKickMessage = true;
    private String customKickMessage = "";
    private String serverName = "KRUD SMP";
    private String discord = "discord.gg/krudsmp";

    private boolean webhookEnabled = false;
    private String webhookUrl = "";

    private boolean consoleLog = true;
    private boolean fileLog = true;
    private String logFile = "logs/rflameantivpn.log";

    private String bypassPermission = "rflameantivpn.bypass";
    private String adminPermission = "rflameantivpn.admin";

    private String prefix = "§8[§cʀꜰʟᴀᴍᴇ§7ᴀɴᴛɪᴠᴘɴ§8]";
    private String msgReloaded = "§aᴄᴏɴꜱɪɢ ʀᴇʟᴏᴀᴅᴇᴅ!";
    private String msgNoPermission = "§cɴᴏ ᴘᴇʀᴍɪꜱꜱɪᴏɴ!";
    private String msgPlayerNotFound = "§cᴘʟᴀʏᴇʀ ɴᴏᴛ ꜰᴏᴜɴᴅ!";
    private String msgWhitelisted = "§a{target} ᴀᴅᴅᴇᴅ ᴛᴏ ᴡʜɪᴛᴇʟɪꜱᴛ!";
    private String msgUnwhitelisted = "§c{target} ʀᴇᴍᴏᴠᴇᴅ ꜰʀᴏᴅ ᴡʜɪᴛᴇʟɪꜱᴛ!";
    private String msgCacheCleared = "§aꜱᴇꜱꜱɪᴏɴ ᴄᴀᴄʜᴇ ᴄʟᴇᴀʀᴇᴅ!";

    public String getApiUrl() {
        return apiUrl;
    }

    public void setApiUrl(String apiUrl) {
        this.apiUrl = apiUrl;
    }

    public String getApiKey() {
        return apiKey;
    }

    public void setApiKey(String apiKey) {
        this.apiKey = apiKey;
    }

    public int getApiTimeout() {
        return apiTimeout;
    }

    public void setApiTimeout(int apiTimeout) {
        this.apiTimeout = apiTimeout;
    }

    public boolean isFailOpen() {
        return failOpen;
    }

    public void setFailOpen(boolean failOpen) {
        this.failOpen = failOpen;
    }

    public int getCacheMinutes() {
        return cacheMinutes;
    }

    public void setCacheMinutes(int cacheMinutes) {
        this.cacheMinutes = cacheMinutes;
    }

    public int getMaxCacheSize() {
        return maxCacheSize;
    }

    public void setMaxCacheSize(int maxCacheSize) {
        this.maxCacheSize = maxCacheSize;
    }

    public boolean isBlockVpn() {
        return blockVpn;
    }

    public boolean isBlockProxy() {
        return blockProxy;
    }

    public boolean isBlockTor() {
        return blockTor;
    }

    public boolean isUseDefaultKickMessage() {
        return useDefaultKickMessage;
    }

    public void setUseDefaultKickMessage(boolean useDefaultKickMessage) {
        this.useDefaultKickMessage = useDefaultKickMessage;
    }

    public String getCustomKickMessage() {
        return customKickMessage;
    }

    public String getServerName() {
        return serverName;
    }

    public String getDiscord() {
        return discord;
    }

    public boolean isWebhookEnabled() {
        return webhookEnabled;
    }

    public String getWebhookUrl() {
        return webhookUrl;
    }

    public boolean isConsoleLog() {
        return consoleLog;
    }

    public boolean isFileLog() {
        return fileLog;
    }

    public String getLogFile() {
        return logFile;
    }

    public String getBypassPermission() {
        return bypassPermission;
    }

    public String getAdminPermission() {
        return adminPermission;
    }

    public String getPrefix() {
        return prefix;
    }

    public String getMsgReloaded() {
        return msgReloaded;
    }

    public String getMsgNoPermission() {
        return msgNoPermission;
    }

    public String getMsgPlayerNotFound() {
        return msgPlayerNotFound;
    }

    public String getMsgWhitelisted() {
        return msgWhitelisted;
    }

    public String getMsgUnwhitelisted() {
        return msgUnwhitelisted;
    }

    public String getMsgCacheCleared() {
        return msgCacheCleared;
    }
}