package com.chartiq.chartiq;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;

class Response {

    public String body = "";
    public int code;

    public Response(HttpURLConnection connection) {
        try {
            this.code = connection.getResponseCode();
            InputStream is;
            if (this.code >= 200 && this.code < 400) {
                is = connection.getInputStream();
            } else {
                is = connection.getErrorStream();
            }
            if (is != null) {
                BufferedReader reader = new BufferedReader(new InputStreamReader(is));
                String line;
                StringBuilder builder = new StringBuilder();
                while ((line = reader.readLine()) != null) {
                    builder.append(line);
                }
                this.body = builder.toString();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public String toString() {
        return "body: " + body + "\ncode: " + code;
    }
}
