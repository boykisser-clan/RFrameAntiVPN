package dev.rflame.antivpn.velocity;

import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.event.connection.LoginEvent;
import com.velocitypowered.api.proxy.Player;
import dev.rflame.antivpn.common.VPNResult;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;

import java.util.UUID;

public class VelocityLoginListener {

    private final RFlameAntiVPNVelocity plugin;

    public VelocityLoginListener(RFlameAntiVPNVelocity plugin) {
        this.plugin = plugin;
    }

    @Subscribe
    public void onLogin(LoginEvent event) {
        Player player = event.getPlayer();
        if (player.isActive()) {
            return;
        }

        String ip = player.getRemoteAddress().getAddress().getHostAddress();
        String name = player.getUsername();
        UUID uuid = player.getUniqueId();

        if (plugin.getConfig().isConsoleLog()) {
            plugin.getLogger().info("[RFlameAntiVPN] Checking: " + name + " (" + ip + ")");
        }

        if (plugin.getWhitelist().isWhitelisted(ip, uuid, name)) {
            return;
        }

        if (player.hasPermission(plugin.getConfig().getBypassPermission())) {
            return;
        }

        VPNResult result = plugin.getChecker().checkIP(ip).join();
        plugin.incrementChecked();

        if (shouldBlock(result)) {
            plugin.incrementBlocked();

            String kickMessage = plugin.buildKickMessage(result, name);
            event.setResult(
                LoginEvent.ColumnarResult.forbidden(
                    LegacyComponentSerializer.legacySection().deserialize(kickMessage)));

            String blockType = result.isVPN() ? "VPN" : result.isProxy() ? "PROXY" : "TOR";
            plugin.getLogger().warn("[RFlameAntiVPN] BLOCKED: " + name + " (" + ip + ") - " + blockType);

            plugin.getWebhook().sendAlert(name, ip, result);
        }
    }

    private boolean shouldBlock(VPNResult result) {
        if (result.isVPN() && plugin.getConfig().isBlockVpn()) return true;
        if (result.isProxy() && plugin.getConfig().isBlockProxy()) return true;
        if (result.isTor() && plugin.getConfig().isBlockTor()) return true;
        return false;
    }
}