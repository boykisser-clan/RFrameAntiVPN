package dev.rflame.antivpn.common;

public class VPNResult {
    public static final VPNResult CLEAN = new VPNResult(false, false, false, false, "?", "?");

    private final boolean detected;
    private final boolean isVPN;
    private final boolean isProxy;
    private final boolean isTor;
    private final String country;
    private final String isp;
    private final String ip;

    public VPNResult(boolean detected, boolean isVPN, boolean isProxy, boolean isTor, String country, String isp) {
        this.detected = detected;
        this.isVPN = isVPN;
        this.isProxy = isProxy;
        this.isTor = isTor;
        this.country = country;
        this.isp = isp;
        this.ip = "unknown";
    }

    public VPNResult(boolean detected, boolean isVPN, boolean isProxy, boolean isTor, String country, String isp, String ip) {
        this.detected = detected;
        this.isVPN = isVPN;
        this.isProxy = isProxy;
        this.isTor = isTor;
        this.country = country;
        this.isp = isp;
        this.ip = ip;
    }

    public boolean isDetected() {
        return detected;
    }

    public boolean isVPN() {
        return isVPN;
    }

    public boolean isProxy() {
        return isProxy;
    }

    public boolean isTor() {
        return isTor;
    }

    public String getCountry() {
        return country;
    }

    public String getIsp() {
        return isp;
    }

    public String getIp() {
        return ip;
    }

    public VPNResult withIp(String ip) {
        return new VPNResult(detected, isVPN, isProxy, isTor, country, isp, ip);
    }
}