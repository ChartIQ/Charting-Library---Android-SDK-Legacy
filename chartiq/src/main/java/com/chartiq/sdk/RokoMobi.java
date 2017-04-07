package com.chartiq.sdk;

import android.content.Context;
import android.content.ContextWrapper;
import android.content.SharedPreferences;

import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

class RokoMobi extends ContextWrapper {

    private static final String TAG = "RokoMobi";
    public static boolean appActive = false;
    private static RokoMobi instance;
    private static SharedPreferences sharedPreferences;
    private static LoginData mLoginData;
    private Settings settings;

    public RokoMobi(Context base) {
        super(base);
    }

    public static void start(Context context, String apiToken, RokoLogger.CallbackStart callbackStart) {
        if (instance == null) {
            instance = new RokoMobi(context.getApplicationContext());
            sharedPreferences = context.getSharedPreferences("RokoMobi", Context.MODE_PRIVATE);
            instance.settings = new Settings(instance, apiToken);

            RokoLogger.start();
            RokoLogger.sendStartEvent(callbackStart);
            AppLifecycle.init();
        }
    }

    public static RokoMobi getInstance() {
        return instance;
    }

    public static void setUser(String userName) {
        setUser(userName, null);
    }

    public static void setUser(String userName, final ResponseCallback callback) {
        String url = getInstance().getSettings().getApiUrl() + "/usersession/setUserCmd";
        JSONObject params = new JSONObject();
        try {
            params.put("username", userName);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        RokoHttp.POST(url, new HashMap<String, String>(), params.toString(), new ResponseCallback() {
            @Override
            public void success(Response response) {
                LoginData result = new Gson().fromJson(response.body, LoginData.class);
                if (result != null) {
                    mLoginData = result;
                    RokoMobi.getInstance().getApplicationContext().getSharedPreferences("RokoMobi", 0).edit().putString("loginData", new Gson().toJson(result)).apply();
                    RokoMobi.getInstance().getSettings().authSession = result.data.sessionKey;
                    bindApplicationSession(callback);
                }
            }

            @Override
            public void failure(Response response) {
                if(callback != null) {
                    callback.failure(response);
                }
            }
        });
    }

    public static void bindApplicationSession() {
        bindApplicationSession(null);
    }

    public static void bindApplicationSession(final ResponseCallback callback) {
        String url = getInstance().getSettings().getApiUrl() + "/usersession/actions/bindApplicationSession";
        JSONObject params = new JSONObject();
//        if (lastName != null && !lastName.equals(getLastUserName())) {
//            getInstance().getSettings().setSessionId();
//        }
        try {
            params.put("applicationSessionUUID", getInstance().getSettings().getSessionId());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        RokoHttp.POST(url, new HashMap<String, String>(), params.toString(), callback);
    }

    public static User getLoginUser() {
        if (mLoginData != null) {
            return mLoginData.data.user;
        }
        String loginDataAsString = RokoMobi.getInstance().getApplicationContext().getSharedPreferences("RokoMobi", Context.MODE_PRIVATE).getString("loginData", null);
        if (loginDataAsString == null) {
            return null;
        }
        LoginData data = new Gson().fromJson(loginDataAsString, LoginData.class);
        if (data != null && data.data != null) {
            return data.data.user;
        }
        return null;
    }

    public static void setUserCustomProperty(String propertyName, String propertyValue) {
        setUserCustomProperty(propertyName, propertyValue, null);
    }

    public static void setUserCustomProperty(String propertyName, String propertyValue, ResponseCallback callback) {
        setUserCustomProperties(Collections.singletonMap(propertyName, propertyValue), callback);
    }

    public static void setUserCustomProperties(Map<String, String> properties, ResponseCallback callback) {
        User user = getLoginUser();
        if (user == null || user.customProperties == null || properties == null) {
            return;
        }

        for (Map.Entry<String, String> property : properties.entrySet()) {
            String value = property.getValue() != null ? property.getValue() : "";
            user.customProperties.put(property.getKey(), value);
        }

        String url = RokoMobi.getInstance().getSettings().getApiUrl() + "/usersession/user";
        final String userJson = new Gson().toJson(user);
        RokoHttp.PUT(url, new HashMap<String, String>(), userJson, callback);
    }

    public SharedPreferences getSharedPreferences() {
        return sharedPreferences;
    }

    public Settings getSettings() {
        return settings;
    }
}
