package dev.rflame.antivpn.bungeecord;

import dev.rflame.antivpn.common.*;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.plugin.Plugin;
import net.md_5.bungee.api.plugin.PluginManager;
import net.md_5.bungee.config.Configuration;

import java.io.File;
import java.util.concurrent.atomic.AtomicLong;

public class RFlameAntiVPNBungee extends Plugin {

    private RFlameAPIChecker checker;
    private ConfigManager config;
    private WhitelistManager whitelist;
    private RFlameWebhook webhook;
    private AtomicLong checkedCount = new AtomicLong(0);
    private AtomicLong blockedCount = new AtomicLong(0);

    @Override
    public void onEnable() {
        if (!getDataFolder().exists()) {
            getDataFolder().mkdirs();
        }

        config = new ConfigManager(new File(getDataFolder(), "config.yml"));
        whitelist = new WhitelistManager(new File(getDataFolder(), "whitelist.yml"));

        checker = new RFlameAPIChecker(
            config.getApiUrl(),
            config.getApiKey(),
            config.getCacheMinutes());

        webhook = new RFlameWebhook(
            config.getWebhookUrl(),
            msg -> getLogger().warning(msg));

        PluginManager pm = getProxy().getPluginManager();
        pm.registerListener(this, new BungeeLoginListener(this));

        registerCommands();

        getLogger().info(ChatColor.GREEN + "[RFlameAntiVPN] Enabled on BungeeCord!");
    }

    @Override
    public void onDisable() {
        getLogger().info(ChatColor.RED + "[RFlameAntiVPN] Disabled.");
    }

    private void registerCommands() {
        getProxy().getPluginManager().registerCommand(this, new RFlameCommand(this));
    }

    public RFlameAPIChecker getChecker() {
        return checker;
    }

    public ConfigManager getConfig() {
        return config;
    }

    public WhitelistManager getWhitelist() {
        return whitelist;
    }

    public RFlameWebhook getWebhook() {
        return webhook;
    }

    public String buildKickMessage(VPNResult result, String playerName) {
        return MessageBuilder.buildKickMessage(result, playerName, config.getServerName(), config.getDiscord());
    }

    public void incrementChecked() {
        checkedCount.incrementAndGet();
    }

    public void incrementBlocked() {
        blockedCount.incrementAndGet();
    }

    public long getCheckedCount() {
        return checkedCount.get();
    }

    public long getBlockedCount() {
        return blockedCount.get();
    }

    public void reload() {
        config.reload();
        whitelist.load();
        checker.clearCache();
    }
}