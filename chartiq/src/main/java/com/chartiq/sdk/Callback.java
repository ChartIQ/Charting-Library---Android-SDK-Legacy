package com.chartiq.sdk;

interface Callback<SUCCESS, FAILURE> {
    void success(SUCCESS response);

    void failure(FAILURE response);
}
