package com.chartiq.chartiq;

interface Callback<SUCCESS, FAILURE> {
    void success(SUCCESS response);

    void failure(FAILURE response);
}
