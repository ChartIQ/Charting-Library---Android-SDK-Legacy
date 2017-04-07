package com.chartiq.sdk.model;

import java.util.Date;

public class OHLCChart {

    public Date DT;
    public double Open;
    public double High;
    public double Low;
    public double Close;
    public double Volume;
    public double AdjClose;

    public OHLCChart(Date date, double open, double high, double low, double close, double volume, double adjClose) {
        this.DT = date;
        this.Open = open;
        this.High = high;
        this.Low = low;
        this.Close = close;
        this.Volume = volume;
        this.AdjClose = adjClose;
    }

    @Override
    public String toString() {
        return "{" +
                "\"DT\":\"" + DT +
                "\",\"Open\":" + Open +
                ",\"High\":" + High +
                ",\"Low\":" + Low +
                ",\"Close\":" + Close +
                ",\"Volume\":" + Volume +
                ",\"Adj_Close\":" + AdjClose +
                "}";
    }
}
