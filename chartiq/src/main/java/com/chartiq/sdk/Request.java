package com.chartiq.sdk;

import java.util.HashMap;
import java.util.Map;

class Request {
    public String url;
    public RequestMethod method;
    public Map<String, String> headers;

    public Request(String url, RequestMethod method, Map<String, String> headers) {
        this(url, method);
        this.headers = headers;
        if (headers == null) {
            this.headers = new HashMap<>();
        }
    }

    public Request(String url, RequestMethod method) {
        this();
        this.url = url;
        this.method = method;
    }

    public Request() {
        headers = new HashMap<>();
        method = RequestMethod.GET;
    }

    public void addHeader(String headerName, String headerValue) {
        if (headers == null) {
            headers = new HashMap<>();
        }
        if (!headers.containsKey(headerName)) {
            headers.put(headerName, headerValue);
        }
    }


}
