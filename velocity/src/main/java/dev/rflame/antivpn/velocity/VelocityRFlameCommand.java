package dev.rflame.antivpn.velocity;

import com.velocitypowered.api.command.Command;
import com.velocitypowered.api.command.CommandSource;
import com.velocitypowered.api.proxy.Player;
import dev.rflame.antivpn.common.MessageBuilder;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;

public class VelocityRFlameCommand implements Command {

    private final RFlameAntiVPNVelocity plugin;

    public VelocityRFlameCommand(RFlameAntiVPNVelocity plugin) {
        this.plugin = plugin;
    }

    @Override
    public void execute(CommandSource source, String[] args) {
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
                handleCheck(source, args[1]);
                break;
            case "whitelist":
                if (args.length < 3) {
                    source.sendMessage(Component.text("Usage: /rvpn whitelist add|remove <player|ip|cidr>").color(NamedTextColor.RED));
                    return;
                }
                handleWhitelist(source, args[1], args[2], args.length > 3 ? args[3] : "");
                break;
            case "list":
                handleWhitelistList(source);
                break;
            case "cache":
                if (args.length < 2) {
                    source.sendMessage(Component.text("Usage: /rvpn cache clear|stats").color(NamedTextColor.RED));
                    return;
                }
                handleCache(source, args[1]);
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

    private void sendHelp(CommandSource source) {
        source.sendMessage(Component.text("[RFrameAntiVPN] Help").color(NamedTextColor.GOLD));
        source.sendMessage(Component.text("/rvpn check <player|ip>"));
        source.sendMessage(Component.text("/rvpn whitelist add|remove <target>"));
        source.sendMessage(Component.text("/rvpn whitelist list"));
        source.sendMessage(Component.text("/rvpn cache clear|stats"));
        source.sendMessage(Component.text("/rvpn reload"));
        source.sendMessage(Component.text("/rvpn stats"));
    }

    private void handleCheck(CommandSource source, String target) {
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

    private void handleWhitelist(CommandSource source, String action, String type, String target) {
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
                source.sendMessage(Component.text("Invalid type").color(NamedTextColor.RED));
                return;
            }
            source.sendMessage(Component.text("Added: " + target).color(NamedTextColor.GREEN));
        } else if (action.equalsIgnoreCase("remove")) {
            if (type.equalsIgnoreCase("player")) {
                plugin.getWhitelist().removePlayer(target);
            } else if (type.equalsIgnoreCase("ip")) {
                plugin.getWhitelist().removeIp(target);
            } else if (type.equalsIgnoreCase("cidr")) {
                plugin.getWhitelist().removeIpRange(target);
            } else {
                source.sendMessage(Component.text("Invalid type").color(NamedTextColor.RED));
                return;
            }
            source.sendMessage(Component.text("Removed: " + target).color(NamedTextColor.RED));
        }
    }

    private void handleWhitelistList(CommandSource source) {
        String prefix = plugin.getConfig().getPrefix();
        source.sendMessage(Component.text(MessageBuilder.buildWhitelistList(prefix, plugin.getWhitelist()).replace("§", "")));
    }

    private void handleCache(CommandSource source, String action) {
        if (action.equalsIgnoreCase("clear")) {
            plugin.getChecker().clearCache();
            source.sendMessage(Component.text("Cache cleared").color(NamedTextColor.GREEN));
        } else if (action.equalsIgnoreCase("stats")) {
            source.sendMessage(Component.text("Cache size: " + plugin.getChecker().getCacheSize()));
        }
    }

    private void handleStats(CommandSource source) {
        String prefix = plugin.getConfig().getPrefix();
        source.sendMessage(Component.text(MessageBuilder.buildStats(prefix, plugin.getCheckedCount(), plugin.getBlockedCount(), plugin.getChecker().getCacheSize()).replace("§", "")));
    }
}