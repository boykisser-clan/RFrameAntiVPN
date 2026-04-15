package dev.rflame.antivpn.common;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;

public class RFlameAPIChecker {

    private final String apiUrl;
    private final String apiKey;
    private final Cache<String, VPNResult> sessionCache;

    public RFlameAPIChecker(String apiUrl, String apiKey, int cacheMinutes) {
        this.apiUrl = apiUrl;
        this.apiKey = apiKey;
        this.sessionCache = Caffeine.newBuilder()
                .expireAfterWrite(cacheMinutes, TimeUnit.MINUTES)
                .maximumSize(10000)
                .build();
    }

    public CompletableFuture<VPNResult> checkIP(String ip) {
        VPNResult cached = sessionCache.getIfPresent(ip);
        if (cached != null) {
            return CompletableFuture.completedFuture(cached.withIp(ip));
        }

        return CompletableFuture.supplyAsync(() -> {
            try {
                String url = apiUrl + "?ip=" + ip + "&key=" + apiKey;

                HttpClient client = HttpClient.newBuilder()
                        .connectTimeout(Duration.ofSeconds(5))
                        .build();

                HttpRequest request = HttpRequest.newBuilder()
                        .uri(URI.create(url))
                        .timeout(Duration.ofSeconds(5))
                        .GET()
                        .build();

                HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

                if (response.statusCode() == 200) {
                    VPNResult result = parseResponse(response.body(), ip);
                    sessionCache.put(ip, result);
                    return result;
                }

                return VPNResult.CLEAN.withIp(ip);

            } catch (Exception e) {
                return VPNResult.CLEAN.withIp(ip);
            }
        });
    }

    private VPNResult parseResponse(String json, String ip) {
        try {
            JsonObject obj = JsonParser.parseString(json).getAsJsonObject();

            boolean isVPN = obj.has("vpn") && obj.get("vpn").getAsBoolean();
            boolean isProxy = obj.has("proxy") && obj.get("proxy").getAsBoolean();
            boolean isTor = obj.has("tor") && obj.get("tor").getAsBoolean();
            String country = obj.has("country") ? obj.get("country").getAsString() : "?";
            String isp = obj.has("isp") ? obj.get("isp").getAsString() : "?";

            return new VPNResult(isVPN || isProxy || isTor, isVPN, isProxy, isTor, country, isp, ip);

        } catch (Exception e) {
            return VPNResult.CLEAN.withIp(ip);
        }
    }

    public void invalidate(String ip) {
        sessionCache.invalidate(ip);
    }

    public void clearCache() {
        sessionCache.invalidateAll();
    }

    public long getCacheSize() {
        return sessionCache.estimatedSize();
    }
}