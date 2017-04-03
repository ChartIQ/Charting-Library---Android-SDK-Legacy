package com.chartiq.chartiq;


import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.telephony.TelephonyManager;

import com.chartiq.chartiq.Request;

import java.util.TimeZone;


class RokoHeadersApplier extends RokoHttp.DefaultHeadersApplier {
    @Override
    public Request apply(Request request) {
        addDefaultHeaders(request);
        return request;
    }

    protected void addDefaultHeaders(Request request) {
        if (request != null) {
            RokoMobi rokoMobi = RokoMobi.getInstance();
//            request.addHeader("Accept-Encoding", "gzip");
            request.addHeader("X-ROKO-Mobi-Api-Key", rokoMobi.getSettings().getApiToken());
            request.addHeader("X-ROKO-Mobi-Partner-Key", "chiq");
            request.addHeader("X-ROKO-Mobi-Device-Type", "Android");
            request.addHeader("X-ROKO-Mobi-Device-Model", rokoMobi.getSettings().deviceModel);
            request.addHeader("X-ROKO-Mobi-OS", "Android");
            request.addHeader("X-ROKO-Mobi-OS-Version", "Android " + rokoMobi.getSettings().operatingSystem);
            request.addHeader("X-ROKO-Mobi-Application-Version", rokoMobi.getSettings().applicationVersion);
            request.addHeader("X-ROKO-Mobi-Timezone", TimeZone.getDefault().getDisplayName(false, TimeZone.SHORT));

            try {
                ConnectivityManager connectivityManager = (ConnectivityManager) rokoMobi.getSystemService(Context.CONNECTIVITY_SERVICE);
                NetworkInfo netInfo = connectivityManager.getActiveNetworkInfo();
                if (netInfo != null) {
                    request.addHeader("X-ROKO-Mobi-Network-Type", netInfo.getTypeName());
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

            TelephonyManager tm = (TelephonyManager) rokoMobi.getSystemService(Context.TELEPHONY_SERVICE);
            if (tm != null) {
                request.addHeader("X-ROKO-Mobi-Carrier", tm.getNetworkOperatorName());
            }

            if (rokoMobi.getSettings().authSession != null) {
                request.addHeader("Authorization", "X-ROKO-Mobi-User-Session" + " " + rokoMobi.getSettings().authSession);
            }
        }
    }
}