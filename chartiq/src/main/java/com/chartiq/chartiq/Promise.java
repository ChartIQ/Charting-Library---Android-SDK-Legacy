package com.chartiq.chartiq;

public class Promise<T> {

    private T result;
    private Callback<T> callback;

    public void setResult(T object) {
        if (callback != null) {
            callback.call(object);
        } else {
            this.result = object;
        }

    }

    public void than(Callback<T> callback) {
        if (result != null) {
            callback.call(result);
        } else {
            this.callback = callback;
        }
    }

    public interface Callback<T> {
        void call(T object);
    }
}