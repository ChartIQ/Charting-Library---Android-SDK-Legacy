package com.chartiq.chartiq;

import java.util.Map;

class PostRequest extends Request {

    public String contentType;
    public String body;

    public PostRequest(String url, RequestMethod method, Map<String, String> headers) {
        super(url, method, headers);
    }

    public PostRequest(String url, RequestMethod method) {
        super(url, method);
    }


    public PostRequest(String url, RequestMethod method, Map<String, String> headers, String contentType, String body) {
        super(url, method, headers);
        this.contentType = contentType;
        this.body = body;
    }
}
