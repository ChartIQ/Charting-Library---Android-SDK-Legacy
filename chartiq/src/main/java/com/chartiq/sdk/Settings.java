package com.chartiq.sdk;

import java.util.UUID;

class Settings {

    public String chartiqSdkVersion = "1.0.8";
    public String applicationVersion = "1.0.8";
    String authSession = null; // Only for autorized user. If user logout set null
    String deviceModel;
    String operatingSystem;
    private String apiUrl = "https://api.roko.mobi/v1";
    private String apiToken = "";
    private String sessionId; // for url applicationSessionUuid
    private RokoMobi rokoMobi;

    public Settings(RokoMobi rm, String token) {
        rokoMobi = rm;
        apiToken = token;

        sessionId = rokoMobi.getSharedPreferences().getString("sessionId", null);
        if (sessionId == null) {
            setSessionId();
        }

        deviceModel = android.os.Build.MANUFACTURER + " " + android.os.Build.MODEL;
        operatingSystem = android.os.Build.VERSION.RELEASE;
    }

    public void setSessionId() {
        sessionId = UUID.randomUUID().toString();
        rokoMobi.getSharedPreferences().edit().putString("sessionId", sessionId).apply();
    }

    public String getSessionId() {
        return sessionId;
    }

    public String getApiUrl() {
        return apiUrl;
    }

    public String getApiToken() {
        return apiToken;
    }
}
