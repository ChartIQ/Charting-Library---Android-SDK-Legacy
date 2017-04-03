package com.chartiq.chartiq;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

class RokoLogger {

    private final static List<Event> pendingEvents = new ArrayList<>();
    private static Analytics analytics;
    private static Thread bgThread;
    private static Runnable loggerTask = new Runnable() {
        @Override
        public void run() {
            while (true) {
                sendEvents();
//                synchronized (mMutex) {
//                    try {
//                        mMutex.wait(TRACKING_INTERVAL);
//                    } catch (InterruptedException e) {
//                        e.printStackTrace();
//                    }
//                }
            }
        }
    };

    public static void addEvent(Event event) {
        if (event != null) {
            event.set("ChartIQ SDK Version", RokoMobi.getInstance().getSettings().chartiqSdkVersion);
            synchronized (pendingEvents) {
                pendingEvents.add(event);
//                if (pendingEvents.size() > MAX_EVENTS_COUNT) {
//                    synchronized (mMutex) {
//                        mMutex.notify();
//                    }
//                }
            }
        }
    }

    public static void start() {
        analytics = new Analytics();
        if (bgThread == null) {
            (bgThread = new Thread(loggerTask)).start();
        }
    }

    private static void sendEvents() {
        if (pendingEvents.size() == 0) {
            return;
        }

        synchronized (pendingEvents) {
            List<Event> events = new ArrayList<>(pendingEvents);
            pendingEvents.clear();
            analytics.events = events;
        }

        analytics.applicationSessionUUID = RokoMobi.getInstance().getSettings().getSessionId();
        String url = RokoMobi.getInstance().getSettings().getApiUrl() + "/events";

        RokoHttp.POST(url, new HashMap<String, String>(), new Gson().toJson(analytics), new ResponseCallback() {
            @Override
            public void success(Response response) {
                if (response.code == 200) {
//                    String body = new Gson().toJson(analytics);
                    analytics.events.clear();
                }
            }

            @Override
            public void failure(Response response) {
                if (response.code >= 400 && response.code < 500) {
                    analytics.events.clear();
                }
                synchronized (pendingEvents) {
                    pendingEvents.addAll(0, analytics.events);
                }
//                cache();
            }
        });
    }

    static void sendStartEvent(final CallbackStart callback) {
        Analytics a = new Analytics();
        a.applicationSessionUUID = RokoMobi.getInstance().getSettings().getSessionId();
        a.events.add(new Event("_ChartIQ.SDK.Init"));
        String url = RokoMobi.getInstance().getSettings().getApiUrl() + "/events";
        RokoHttp.POST(url, new HashMap<String, String>(), new Gson().toJson(a), new ResponseCallback() {
            @Override
            public void success(Response response) {
                callback.load();
            }

            @Override
            public void failure(Response response) {
                if (response.code != 400 && response.code != 401) {
                    callback.load();
                }
            }
        });
    }

    interface CallbackStart {
        void load();
    }
}
