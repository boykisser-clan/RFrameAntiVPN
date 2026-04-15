package dev.rflame.antivpn.common;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class WhitelistManager {

    private List<String> players = new ArrayList<>();
    private List<String> uuids = new ArrayList<>();
    private List<String> ips = new ArrayList<>();
    private List<String> ipRanges = new ArrayList<>();
    private final File file;
    private final Gson gson = new GsonBuilder().setPrettyPrinting().create();

    public WhitelistManager(File file) {
        this.file = file;
        load();
    }

    public void load() {
        try {
            if (file.exists()) {
                try (FileReader reader = new FileReader(file)) {
                    Type type = new TypeToken<WhitelistData>() {}.getType();
                    WhitelistData data = gson.fromJson(reader, type);
                    if (data != null) {
                        players = data.players != null ? data.players : new ArrayList<>();
                        uuids = data.uuids != null ? data.uuids : new ArrayList<>();
                        ips = data.ips != null ? data.ips : new ArrayList<>();
                        ipRanges = data.ipRanges != null ? data.ipRanges : new ArrayList<>();
                    }
                }
            } else {
                save();
            }
        } catch (Exception e) {
            save();
        }
    }

    public void save() {
        try (FileWriter writer = new FileWriter(file)) {
            WhitelistData data = new WhitelistData(players, uuids, ips, ipRanges);
            gson.toJson(data, writer);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public boolean isWhitelisted(String ip, UUID uuid, String name) {
        if (players.stream().anyMatch(p -> p.equalsIgnoreCase(name))) {
            return true;
        }

        if (uuids.contains(uuid.toString().replace("-", ""))) {
            return true;
        }

        if (ips.contains(ip)) {
            return true;
        }

        for (String range : ipRanges) {
            if (ipInCIDR(ip, range)) {
                return true;
            }
        }

        return false;
    }

    private boolean ipInCIDR(String ip, String cidr) {
        try {
            String[] parts = cidr.split("/");
            int prefix = Integer.parseInt(parts[1]);
            long ipLong = ipToLong(ip);
            long cidrLong = ipToLong(parts[0]);
            long mask = prefix == 0 ? 0 : (-1L << (32 - prefix));
            return (ipLong & mask) == (cidrLong & mask);
        } catch (Exception e) {
            return false;
        }
    }

    private long ipToLong(String ip) {
        String[] parts = ip.split("\\.");
        long result = 0;
        for (String part : parts) {
            result = result * 256 + Long.parseLong(part);
        }
        return result;
    }

    public List<String> getPlayers() {
        return players;
    }

    public void addPlayer(String player) {
        if (!players.contains(player)) {
            players.add(player);
            save();
        }
    }

    public void removePlayer(String player) {
        players.remove(player);
        save();
    }

    public List<String> getUuids() {
        return uuids;
    }

    public void addUuid(String uuid) {
        if (!uuids.contains(uuid)) {
            uuids.add(uuid);
            save();
        }
    }

    public void removeUuid(String uuid) {
        uuids.remove(uuid);
        save();
    }

    public List<String> getIps() {
        return ips;
    }

    public void addIp(String ip) {
        if (!ips.contains(ip)) {
            ips.add(ip);
            save();
        }
    }

    public void removeIp(String ip) {
        ips.remove(ip);
        save();
    }

    public List<String> getIpRanges() {
        return ipRanges;
    }

    public void addIpRange(String range) {
        if (!ipRanges.contains(range)) {
            ipRanges.add(range);
            save();
        }
    }

    public void removeIpRange(String range) {
        ipRanges.remove(range);
        save();
    }

    private static class WhitelistData {
        List<String> players;
        List<String> uuids;
        List<String> ips;
        List<String> ipRanges;

        WhitelistData(List<String> players, List<String> uuids, List<String> ips, List<String> ipRanges) {
            this.players = players;
            this.uuids = uuids;
            this.ips = ips;
            this.ipRanges = ipRanges;
        }
    }
}