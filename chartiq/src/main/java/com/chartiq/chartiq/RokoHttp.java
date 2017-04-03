package com.chartiq.chartiq;

import android.util.Log;

import com.chartiq.chartiq.PostRequest;
import com.chartiq.chartiq.Request;
import com.chartiq.chartiq.RequestMethod;
import com.chartiq.chartiq.Response;
import com.chartiq.chartiq.ResponseCallback;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Map;
import java.util.concurrent.Executors;


class RokoHttp {
    private static final String TAG = "RokoHttp";
    private static final int REDIRECT = 3;
    private static DefaultRokoHttpConnectionFactory connectionFactory = new DefaultRokoHttpConnectionFactory();
    private static DefaultHeadersApplier headersApplier = new RokoHeadersApplier();

    static void send(final PostRequest request, final ResponseCallback callback) {
        final Runnable task = new Runnable() {
            @Override
            public void run() {
                try {
                    if (request == null) {
                        throw new RuntimeException();
                    }
                    headersApplier.apply(request);
                    HttpURLConnection connection = connectionFactory.createConnection(request);
                    connection.setRequestMethod(request.method.getMethodStringName());
                    if (request.contentType != null) {
                        connection.setRequestProperty("Content-Type", request.contentType);
                    }
                    for (Map.Entry<String, String> item : request.headers.entrySet()) {
                        connection.setRequestProperty(item.getKey(), item.getValue());
                    }
                    if (!RequestMethod.DELETE.equals(request.method) && !RequestMethod.GET.equals(request.method)) {
                        OutputStream out = connection.getOutputStream();
                        if (request.body != null) {
                            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(out));
                            writer.write(request.body);
                            writer.flush();
                        }
                    }

                    connection.connect();
                    Response resp = new Response(connection);
                    if (resp.code == -1) {
//                        Log.e("RokoHttp", "send: error in connection");
                    } else if (resp.code == HttpURLConnection.HTTP_OK || resp.code == HttpURLConnection.HTTP_CREATED) {
                        if (callback != null) {
                            callback.success(resp);
                        }
                    } else {
                        int firstCodeNumber = resp.code / 100;
                        if (firstCodeNumber == REDIRECT) {
//                            Log.e("RokoHttp", "send: Redirect");
                        } else {
                            if (callback != null) {
                                callback.failure(resp);
                            }
                        }
                    }
                } catch (MalformedURLException ex) {
//                    Log.e("RokoHttp", "send: incorrect url");
                } catch (IOException ex) {
//                    Log.e("RokoHttp", "send: error in connection");
                }
            }
        };

        Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors()).execute(task);

    }

    public static void GET(String url, Map<String, String> headers, final ResponseCallback callback) {
        final PostRequest request = new PostRequest(url, RequestMethod.GET, headers);
        send(request, callback);
    }

    public static void GET(String url, final ResponseCallback callback) {
        final PostRequest request = new PostRequest(url, RequestMethod.GET, null);
        send(request, callback);
    }

    public static void POST(String url, Map<String, String> headers, String body, final ResponseCallback callback) {
        final PostRequest request = new PostRequest(url, RequestMethod.POST, headers, "application/json", body == null ? "" : body);
        send(request, callback);
    }

    public static void POST(String url, String body, final ResponseCallback callback) {
        final PostRequest request = new PostRequest(url, RequestMethod.POST, null, "application/json", body == null ? "" : body);
        send(request, callback);
    }

    public static void DELETE(String url, Map<String, String> headers, final ResponseCallback callback) {
        final PostRequest request = new PostRequest(url, RequestMethod.DELETE, headers);
        send(request, callback);
    }

    public static void DELETE(String url, final ResponseCallback callback) {
        final PostRequest request = new PostRequest(url, RequestMethod.DELETE, null);
        send(request, callback);
    }

    public static void PUT(String url, Map<String, String> headers, String body, final ResponseCallback callback) {
        final PostRequest request = new PostRequest(url, RequestMethod.PUT, headers, "application/json", body == null ? "" : body);
        send(request, callback);
    }

    public static void PUT(String url, String body, final ResponseCallback callback) {
        final PostRequest request = new PostRequest(url, RequestMethod.PUT, null, "application/json", body == null ? "" : body);
        send(request, callback);
    }

    static class DefaultRokoHttpConnectionFactory {
        public HttpURLConnection createConnection(Request request) throws IOException {
            URL connectionUrl = new URL(request.url);
            HttpURLConnection connection = (HttpURLConnection) connectionUrl.openConnection();
            return connection;
        }
    }

    static class DefaultHeadersApplier {
        public Request apply(Request request) {
            return request;
        }
    }

}