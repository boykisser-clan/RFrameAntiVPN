package dev.rflame.antivpn.velocity;

import com.google.inject.Inject;
import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.event.proxy.ProxyInitializeEvent;
import com.velocitypowered.api.event.proxy.ProxyShutdownEvent;
import com.velocitypowered.api.plugin.Plugin;
import com.velocitypowered.api.plugin.annotation.DataDirectory;
import com.velocitypowered.api.proxy.ProxyServer;
import dev.rflame.antivpn.common.*;
import net.kyori.adventure.text.Component;
import org.slf4j.Logger;

import java.io.File;
import java.nio.file.Path;
import java.util.concurrent.atomic.AtomicLong;

@Plugin(
    id = "rflameantivpn",
    name = "RFrameAntiVPN",
    version = "1.0.0",
    authors = {"RFlame Studio"},
    description = "VPN/Proxy detection for Velocity"
)
public class RFlameAntiVPNVelocity {

    private final ProxyServer server;
    private final Logger logger;
    private final Path dataDirectory;

    private RFlameAPIChecker checker;
    private ConfigManager config;
    private WhitelistManager whitelist;
    private RFlameWebhook webhook;
    private AtomicLong checkedCount = new AtomicLong(0);
    private AtomicLong blockedCount = new AtomicLong(0);

    @Inject
    public RFlameAntiVPNVelocity(
            ProxyServer server,
            Logger logger,
            @DataDirectory Path dataDirectory) {
        this.server = server;
        this.logger = logger;
        this.dataDirectory = dataDirectory;
    }

    @Subscribe
    public void onProxyInit(ProxyInitializeEvent event) {
        try {
            java.nio.file.Files.createDirectories(dataDirectory);
        } catch (java.io.IOException e) {
            logger.error("Failed to create data directory");
        }

        File configFile = dataDirectory.resolve("config.yml").toFile();
        File whitelistFile = dataDirectory.resolve("whitelist.yml").toFile();

        config = new ConfigManager(configFile);
        whitelist = new WhitelistManager(whitelistFile);

        checker = new RFlameAPIChecker(
            config.getApiUrl(),
            config.getApiKey(),
            config.getCacheMinutes());

        webhook = new RFlameWebhook(
            config.getWebhookUrl(),
            msg -> logger.warn(msg));

        server.getEventManager().register(this, new VelocityLoginListener(this));

        registerCommands();

        logger.info("[RFlameAntiVPN] Enabled on Velocity!");
    }

    @Subscribe
    public void onProxyShutdown(ProxyShutdownEvent event) {
        logger.info("[RFlameAntiVPN] Disabled.");
    }

    private void registerCommands() {
        server.getCommandManager().register(
            server.getCommandManager().metaBuilder("rflameantivpn")
                .aliases("rvpn", "antivpn")
                .build(),
            new VelocityRFlameCommand(this));
    }

    public ProxyServer getServer() {
        return server;
    }

    public Logger getLogger() {
        return logger;
    }

    public Path getDataDirectory() {
        return dataDirectory;
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