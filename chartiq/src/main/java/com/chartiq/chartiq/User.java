package com.chartiq.chartiq;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Map;

public class User implements Serializable {
    private static final long serialVersionUID = 1L;
    public int objectId;
    public String username;
    public String email;
    public String phone;
    public PhotoFile photoFile;
    public String firstLoginTime;
    public String lastLoginTime;
    public String name;
    public String userType;
    public String referralCode;
    public String createDate;
    public String updateDate;
    public ArrayList influences;
    public Map systemProperties;
    public Map customProperties;

    //"photoFile": {
    //"url": "https://roko-mobi.s3.amazonaws.com/ea2a3eb1-63e4-4f0c-bd18-ffeb324a359e/ed69b4a0-911d-4a7a-9199-f0b0d825637f?AWSAccessKeyId=AKIAIVQZYRABA7F6TXYQ&Expires=1476016319&Signature=7C4ewZ2piRSImLKAJrLP6zEvw4g%3D",
    // "urlExpiresAt": "2016-10-09T12:31:59.0602924Z",
    // "objectId": 1146727
    // }

    public class PhotoFile implements Serializable {
        public long objectId;
        public String url;
        public String urlExpiresAt;
    }
}
