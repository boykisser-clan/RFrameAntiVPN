package dev.rflame.antivpn.common;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Instant;
import java.util.concurrent.CompletableFuture;
import java.util.function.Consumer;

public class RFlameWebhook {

    private final String webhookUrl;
    private final Consumer<String> logger;

    public RFlameWebhook(String webhookUrl, Consumer<String> logger) {
        this.webhookUrl = webhookUrl;
        this.logger = logger;
    }

    public void sendAlert(String playerName, String ip, VPNResult result) {
        if (webhookUrl == null || webhookUrl.isEmpty()) {
            return;
        }

        String type = result.isVPN() ? "VPN" : result.isProxy() ? "Proxy" : result.isTor() ? "Tor" : "Unknown";

        JsonObject payload = new JsonObject();
        JsonArray embeds = new JsonArray();
        JsonObject embed = new JsonObject();

        embed.addProperty("title", "🚫 VPN/Proxy Blocked");
        embed.addProperty("color", 0xff4444);

        JsonArray fields = new JsonArray();
        addField(fields, "Player", playerName, true);
        addField(fields, "IP", ip, true);
        addField(fields, "Type", type, true);
        addField(fields, "Country", result.getCountry(), true);
        addField(fields, "ISP", result.getIsp(), true);
        addField(fields, "Time", getTime(), true);
        embed.add("fields", fields);

        JsonObject footer = new JsonObject();
        footer.addProperty("text", "RFrameAntiVPN • Protection System");
        embed.add("footer", footer);
        embed.addProperty("timestamp", Instant.now().toString());

        embeds.add(embed);
        payload.add("embeds", embeds);

        CompletableFuture.runAsync(() -> {
            try {
                HttpClient client = HttpClient.newHttpClient();
                HttpRequest request = HttpRequest.newBuilder()
                        .uri(URI.create(webhookUrl))
                        .header("Content-Type", "application/json")
                        .POST(HttpRequest.BodyPublishers.ofString(payload.toString()))
                        .build();
                client.send(request, HttpResponse.BodyHandlers.ofString());
            } catch (Exception e) {
                if (logger != null) {
                    logger.accept("[RFlameAntiVPN] Webhook failed: " + e.getMessage());
                }
            }
        });
    }

    private void addField(JsonArray fields, String name, String value, boolean inline) {
        JsonObject field = new JsonObject();
        field.addProperty("name", name);
        field.addProperty("value", value);
        field.addProperty("inline", inline);
        fields.add(field);
    }

    private String getTime() {
        java.time.LocalDateTime now = java.time.LocalDateTime.now();
        return now.toString();
    }
}