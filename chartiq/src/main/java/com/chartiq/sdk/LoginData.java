package com.chartiq.sdk;

class LoginData {

    public String apiStatusCode;
    public String apiStatusMessage;

    public Data data;

    public class Data {
        public User user;
        public String sessionKey;
        public String sessionExpirationDate;
    }

}
