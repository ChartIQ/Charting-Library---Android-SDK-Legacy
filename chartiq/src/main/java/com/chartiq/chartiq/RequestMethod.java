package com.chartiq.chartiq;

/**
 * Created by ilya.uglov on 08.06.16.
 */
enum RequestMethod {
    GET("GET"),
    POST("POST"),
    DELETE("DELETE"),
    PUT("PUT");

    private String methodStringName;

    RequestMethod(String methodStringName) {
        this.methodStringName = methodStringName;
    }

    public String getMethodStringName() {
        return methodStringName;
    }
}
