package dev.rflame.antivpn.bungeecord;

import dev.rflame.antivpn.common.MessageBuilder;
import dev.rflame.antivpn.common.VPNResult;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;
import net.md_5.bungee.api.plugin.TabExecutor;

import java.util.UUID;

public class RFlameCommand extends Command implements TabExecutor {

    private final RFlameAntiVPNBungee plugin;

    public RFlameCommand(RFlameAntiVPNBungee plugin) {
        super("rflameantivpn", null, "rvpn", "antivpn");
        this.plugin = plugin;
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        if (!sender.hasPermission(plugin.getConfig().getAdminPermission())) {
            sender.sendMessage(ChatColor.translateAlternateColorCodes('&', plugin.getConfig().getMessage("no-permission")));
            return;
        }

        if (args.length == 0) {
            sendHelp(sender);
            return;
        }

        switch (args[0].toLowerCase()) {
            case "check" -> {
                if (args.length < 2) {
                    sender.sendMessage(ChatColor.RED + "Usage: /rvpn check <player|ip>");
                    return;
                }
                String target = args[1];
                handleCheck(sender, target);
            }
            case "whitelist" -> {
                if (args.length < 3) {
                    sender.sendMessage(ChatColor.RED + "Usage: /rvpn whitelist add|remove <player|ip|cidr>");
                    return;
                }
                String action = args[1];
                String type = args[2];
                handleWhitelist(sender, action, type, args.length > 3 ? args[3] : "");
            }
            case "list" -> {
                if (args.length == 2 && args[1].equalsIgnoreCase("list")) {
                    handleWhitelistList(sender);
                } else {
                    sender.sendMessage(ChatColor.RED + "Usage: /rvpn whitelist list");
                }
            }
            case "cache" -> {
                if (args.length < 2) {
                    sender.sendMessage(ChatColor.RED + "Usage: /rvpn cache clear|stats");
                    return;
                }
                String action = args[1];
                handleCache(sender, action);
            }
            case "reload" -> {
                plugin.reload();
                sender.sendMessage(ChatColor.translateAlternateColorCodes('&', plugin.getConfig().getMessage("reloaded")));
            }
            case "stats" -> {
                handleStats(sender);
            }
            default -> {
                sendHelp(sender);
            }
        }
    }

    private void sendHelp(CommandSender sender) {
        String prefix = plugin.getConfig().getPrefix();
        sender.sendMessage(ChatColor.translateAlternateColorCodes('&', prefix + " §7ᴠɪꜱᴘɴ"));
        sender.sendMessage(ChatColor.GRAY + "/rvpn check <player|ip> - Check player IP");
        sender.sendMessage(ChatColor.GRAY + "/rvpn whitelist add|remove <target> - Manage whitelist");
        sender.sendMessage(ChatColor.GRAY + "/rvpn whitelist list - Show whitelist");
        sender.sendMessage(ChatColor.GRAY + "/rvpn cache clear|stats - Cache management");
        sender.sendMessage(ChatColor.GRAY + "/rvpn reload - Reload config");
        sender.sendMessage(ChatColor.GRAY + "/rvpn stats - Show statistics");
    }

    private void handleCheck(CommandSender sender, String target) {
        String ip = target;
        if (!target.matches("^\\d{1,3}(\\.\\d{1,3}){3}$")) {
            ProxiedPlayer player = plugin.getProxy().getPlayer(target);
            if (player != null) {
                try {
                    java.lang.reflect.Field field = net.md_5.bungee.api.connection.ProxiedPlayer.class.getDeclaredField("socketAddress");
                    field.setAccessible(true);
                    java.net.InetSocketAddress address = (java.net.InetSocketAddress) field.get(player);
                    ip = address.getAddress().getHostAddress();
                } catch (Exception e) {
                    ip = player.getAddress().getAddress().getHostAddress();
                }
            } else {
                sender.sendMessage(ChatColor.translateAlternateColorCodes('&', plugin.getConfig().getMessage("player-not-found")));
                return;
            }
        }

        plugin.getChecker().checkIP(ip).thenAccept(result -> {
            String prefix = plugin.getConfig().getPrefix();
            sender.sendMessage(ChatColor.translateAlternateColorCodes('&',
                MessageBuilder.buildCheckResult(prefix, result, target)));
        });
    }

    private void handleWhitelist(CommandSender sender, String action, String type, String target) {
        if (target.isEmpty()) {
            sender.sendMessage(ChatColor.RED + "Usage: /rvpn whitelist add|remove <player|ip|cidr>");
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
                sender.sendMessage(ChatColor.RED + "Invalid type. Use: player, ip, or cidr");
                return;
            }
            sender.sendMessage(ChatColor.translateAlternateColorCodes('&',
                plugin.getConfig().getMessage("whitelisted").replace("{target}", target)));
        } else if (action.equalsIgnoreCase("remove")) {
            if (type.equalsIgnoreCase("player")) {
                plugin.getWhitelist().removePlayer(target);
            } else if (type.equalsIgnoreCase("ip")) {
                plugin.getWhitelist().removeIp(target);
            } else if (type.equalsIgnoreCase("cidr")) {
                plugin.getWhitelist().removeIpRange(target);
            } else {
                sender.sendMessage(ChatColor.RED + "Invalid type. Use: player, ip, or cidr");
                return;
            }
            sender.sendMessage(ChatColor.translateAlternateColorCodes('&',
                plugin.getConfig().getMessage("unwhitelisted").replace("{target}", target)));
        }
    }

    private void handleWhitelistList(CommandSender sender) {
        String prefix = plugin.getConfig().getPrefix();
        sender.sendMessage(ChatColor.translateAlternateColorCodes('&',
            MessageBuilder.buildWhitelistList(prefix, plugin.getWhitelist())));
    }

    private void handleCache(CommandSender sender, String action) {
        if (action.equalsIgnoreCase("clear")) {
            plugin.getChecker().clearCache();
            sender.sendMessage(ChatColor.translateAlternateColorCodes('&',
                plugin.getConfig().getMessage("cache-cleared")));
        } else if (action.equalsIgnoreCase("stats")) {
            sender.sendMessage(ChatColor.GRAY + "Cache size: " + plugin.getChecker().getCacheSize());
        }
    }

    private void handleStats(CommandSender sender) {
        String prefix = plugin.getConfig().getPrefix();
        sender.sendMessage(ChatColor.translateAlternateColorCodes('&',
            MessageBuilder.buildStats(prefix, plugin.getCheckedCount(), plugin.getBlockedCount(),
                plugin.getChecker().getCacheSize())));
    }

    @Override
    public Iterable<String> onTabComplete(CommandSender sender, String[] args) {
        return java.util.Collections.emptyList();
    }
}