package com.chartiq.chartiq;

class RokoLifecycle {

    private static final String TAG = "RokoLifecycle";
    private Callback cb;


    private RokoLifecycle(Callback callback) {
        this.cb = callback;
        AppLifecycle.addListener(cb);
    }

    public static RokoLifecycle add(Callback callback) {
        RokoLifecycle obj = new RokoLifecycle(callback);
        return obj;
    }

    public void delCallback() {
        AppLifecycle.removeListener(cb);
    }

    public interface Callback {

        void appForeground();

        void appBackground();
    }

}
