package com.chartiq.sdk;

import android.app.Activity;
import android.app.Application;
import android.os.Bundle;
import android.os.Handler;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Created by mist on 17.10.16.
 */

class AppLifecycle implements Application.ActivityLifecycleCallbacks {
    private static final String TAG = AppLifecycle.class.getName();
    private static final long CHECK_DELAY = 200;
    private static AppLifecycle instance;
    private static long sessionTime = 0;
    private AtomicBoolean foreground = new AtomicBoolean(), paused = new AtomicBoolean();
    private Handler handler = new Handler();
    private Task check;
//    private List<RokoLifecycle.Callback> listeners = new CopyOnWriteArrayList<RokoLifecycle.Callback>();

    private AppLifecycle() {
    }

    public static void init() {
        if (instance == null) instance = new AppLifecycle();
    }

    @Override
    public void onActivityCreated(Activity activity, Bundle bundle) {

    }

    @Override
    public void onActivityStarted(Activity activity) {

    }

    @Override
    public void onActivityResumed(Activity activity) {
        paused.set(false);
        boolean wasBackground = check == null;

        if (check != null) {
            handler.removeCallbacks(check);
            wasBackground = check.executed;
        }

        if (!foreground.get() && wasBackground) {
            sessionTime = System.currentTimeMillis();
        }
        foreground.set(true);
    }

    @Override
    public void onActivityPaused(Activity activity) {
        paused.set(true);
    }

    @Override
    public void onActivityStopped(Activity activity) {


        if (check != null)
            handler.removeCallbacks(check);

        handler.postDelayed(check = new Task() {
            @Override
            public void execute() {
                if (foreground.get() && paused.get()) {
                    foreground.set(false);
                    if (sessionTime > 0) {
                        long timeSpent = (System.currentTimeMillis() - sessionTime) / 1000;
                        sessionTime = 0;
                    }
                }
            }
        }, CHECK_DELAY);
    }

    @Override
    public void onActivitySaveInstanceState(Activity activity, Bundle bundle) {

    }

    @Override
    public void onActivityDestroyed(Activity activity) {

    }

    private abstract class Task implements Runnable {
        public volatile boolean executed;

        @Override
        public final void run() {
            if (!executed) {
                execute();
            }
            executed = true;
        }

        protected abstract void execute();
    }
}