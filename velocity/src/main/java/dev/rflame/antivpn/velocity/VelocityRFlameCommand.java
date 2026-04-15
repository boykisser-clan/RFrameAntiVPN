package dev.rflame.antivpn.velocity;

import com.velocitypowered.api.command.SimpleCommand;
import com.velocitypowered.api.proxy.Player;
import dev.rflame.antivpn.common.MessageBuilder;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;

public class VelocityRFlameCommand extends SimpleCommand {

    private final RFlameAntiVPNVelocity plugin;

    public VelocityRFlameCommand(RFlameAntiVPNVelocity plugin) {
        this.plugin = plugin;
    }

    @Override
    public void execute(Invocation invocation) {
        com.velocitypowered.api.command.CommandSource source = invocation.source();
        String[] args = invocation.arguments();

        if (!source.hasPermission(plugin.getConfig().getAdminPermission())) {
            source.sendMessage(Component.text("No permission!").color(NamedTextColor.RED));
            return;
        }

        if (args.length == 0) {
            sendHelp(source);
            return;
        }

        switch (args[0].toLowerCase()) {
            case "check":
                if (args.length < 2) {
                    source.sendMessage(Component.text("Usage: /rvpn check <player|ip>").color(NamedTextColor.RED));
                    return;
                }
                String target = args[1];
                handleCheck(source, target);
                break;
            case "whitelist":
                if (args.length < 3) {
                    source.sendMessage(Component.text("Usage: /rvpn whitelist add|remove <player|ip|cidr>").color(NamedTextColor.RED));
                    return;
                }
                String action = args[1];
                String type = args[2];
                handleWhitelist(source, action, type, args.length > 3 ? args[3] : "");
                break;
            case "list":
                handleWhitelistList(source);
                break;
            case "cache":
                if (args.length < 2) {
                    source.sendMessage(Component.text("Usage: /rvpn cache clear|stats").color(NamedTextColor.RED));
                    return;
                }
                String cacheAction = args[1];
                handleCache(source, cacheAction);
                break;
            case "reload":
                plugin.reload();
                source.sendMessage(Component.text(plugin.getConfig().getMessage("reloaded")).color(NamedTextColor.GREEN));
                break;
            case "stats":
                handleStats(source);
                break;
            default:
                sendHelp(source);
        }
    }

    private void sendHelp(com.velocitypowered.api.command.CommandSource source) {
        source.sendMessage(Component.text("[RFrameAntiVPN] Help").color(NamedTextColor.GOLD));
        source.sendMessage(Component.text("/rvpn check <player|ip> - Check player IP"));
        source.sendMessage(Component.text("/rvpn whitelist add|remove <target> - Manage whitelist"));
        source.sendMessage(Component.text("/rvpn whitelist list - Show whitelist"));
        source.sendMessage(Component.text("/rvpn cache clear|stats - Cache management"));
        source.sendMessage(Component.text("/rvpn reload - Reload config"));
        source.sendMessage(Component.text("/rvpn stats - Show statistics"));
    }

    private void handleCheck(com.velocitypowered.api.command.CommandSource source, String target) {
        String ip = target;
        if (!target.matches("^\\d{1,3}(\\.\\d{1,3}){3}$")) {
            Player player = plugin.getServer().getPlayer(target).orElse(null);
            if (player != null) {
                ip = player.getRemoteAddress().getAddress().getHostAddress();
            } else {
                source.sendMessage(Component.text("Player not found!").color(NamedTextColor.RED));
                return;
            }
        }

        plugin.getChecker().checkIP(ip).thenAcceptAsync(result -> {
            String prefix = plugin.getConfig().getPrefix();
            source.sendMessage(Component.text(MessageBuilder.buildCheckResult(prefix, result, target).replace("§", "")));
        });
    }

    private void handleWhitelist(com.velocitypowered.api.command.CommandSource source, String action, String type, String target) {
        if (target.isEmpty()) {
            source.sendMessage(Component.text("Usage: /rvpn whitelist add|remove <player|ip|cidr>").color(NamedTextColor.RED));
            return;
        }

        if (action.equalsIgnoreCase("add")) {
            if (type.equalsIgnoreCase("player")) {
                plugin.getWhitelist().addPlayer(target);
            } else if (type.equalsIgnoreCase("ip")) {
                plugin.getWhitelist().addIp(target);
            } else if (type.equalsIgnoreCase("cidr")) {
                plugin.getWhitelist().addIpRange(target);
            } else {
                source.sendMessage(Component.text("Invalid type. Use: player, ip, or cidr").color(NamedTextColor.RED));
                return;
            }
            source.sendMessage(Component.text(plugin.getConfig().getMessage("whitelisted").replace("{target}", target)).color(NamedTextColor.GREEN));
        } else if (action.equalsIgnoreCase("remove")) {
            if (type.equalsIgnoreCase("player")) {
                plugin.getWhitelist().removePlayer(target);
            } else if (type.equalsIgnoreCase("ip")) {
                plugin.getWhitelist().removeIp(target);
            } else if (type.equalsIgnoreCase("cidr")) {
                plugin.getWhitelist().removeIpRange(target);
            } else {
                source.sendMessage(Component.text("Invalid type. Use: player, ip, or cidr").color(NamedTextColor.RED));
                return;
            }
            source.sendMessage(Component.text(plugin.getConfig().getMessage("unwhitelisted").replace("{target}", target)).color(NamedTextColor.RED));
        }
    }

    private void handleWhitelistList(com.velocitypowered.api.command.CommandSource source) {
        String prefix = plugin.getConfig().getPrefix();
        source.sendMessage(Component.text(MessageBuilder.buildWhitelistList(prefix, plugin.getWhitelist()).replace("§", "")));
    }

    private void handleCache(com.velocitypowered.api.command.CommandSource source, String action) {
        if (action.equalsIgnoreCase("clear")) {
            plugin.getChecker().clearCache();
            source.sendMessage(Component.text(plugin.getConfig().getMessage("cache-cleared")).color(NamedTextColor.GREEN));
        } else if (action.equalsIgnoreCase("stats")) {
            source.sendMessage(Component.text("Cache size: " + plugin.getChecker().getCacheSize()));
        }
    }

    private void handleStats(com.velocitypowered.api.command.CommandSource source) {
        String prefix = plugin.getConfig().getPrefix();
        source.sendMessage(Component.text(MessageBuilder.buildStats(prefix, plugin.getCheckedCount(), plugin.getBlockedCount(), plugin.getChecker().getCacheSize()).replace("§", "")));
    }
}