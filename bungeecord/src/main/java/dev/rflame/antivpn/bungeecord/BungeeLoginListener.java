package dev.rflame.antivpn.bungeecord;

import dev.rflame.antivpn.common.VPNResult;
import net.md_5.bungee.api.event.PostLoginEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.event.EventHandler;

import java.net.InetSocketAddress;
import java.util.UUID;

public class BungeeLoginListener implements Listener {

    private final RFlameAntiVPNBungee plugin;

    public BungeeLoginListener(RFlameAntiVPNBungee plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onLogin(PostLoginEvent event) {
        ProxiedPlayer player = event.getPlayer();
        String ip = getPlayerIp(player);
        String name = player.getName();
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

        plugin.getChecker().checkIP(ip).thenAccept(result -> {
            plugin.incrementChecked();

            if (shouldBlock(result)) {
                plugin.incrementBlocked();

                plugin.getProxy().getScheduler().runAsync(plugin, () -> {
                    player.disconnect(new net.md_5.bungee.api.chat.TextComponent(
                        plugin.buildKickMessage(result, name)));

                    String blockType = result.isVPN() ? "VPN" : result.isProxy() ? "PROXY" : "TOR";
                    plugin.getLogger().warning("[RFlameAntiVPN] BLOCKED: " + name + " (" + ip + ") — " + blockType);

                    plugin.getWebhook().sendAlert(name, ip, result);
                });
            }
        });
    }

    private String getPlayerIp(ProxiedPlayer player) {
        try {
            java.lang.reflect.Field field = ProxiedPlayer.class.getDeclaredField("socketAddress");
            field.setAccessible(true);
            InetSocketAddress address = (InetSocketAddress) field.get(player);
            if (address != null) {
                return address.getAddress().getHostAddress();
            }
        } catch (Exception e) {
        }
        return player.getAddress().getAddress().getHostAddress();
    }

    private boolean shouldBlock(VPNResult result) {
        if (result.isVPN() && plugin.getConfig().isBlockVpn()) return true;
        if (result.isProxy() && plugin.getConfig().isBlockProxy()) return true;
        if (result.isTor() && plugin.getConfig().isBlockTor()) return true;
        return false;
    }
}