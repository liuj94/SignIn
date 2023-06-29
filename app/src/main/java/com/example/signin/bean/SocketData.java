package com.example.signin.bean;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class SocketData implements Serializable {

    /**
     * {"cardH":120.0,"cardW":80.0,"code":"200","count":1,"type":"print","urls":["/profile/upload/2023/05/22/ef4765ff-7f84-45cf-a0eb-5451f129fed3.png","/profile/upload/2023/05/22/7e203438-dc37-42ba-afde-927657009c85.png"]}
     * code : 200
     * count : 1
     * type : refresh
     */

    private String code;
    private String cardH = "80";
    private String cardW = "120";
    private int count;
    private String type;
    private List<String> urls = new ArrayList<>();


    public String getCardH() {
        return cardH;
    }

    public void setCardH(String cardH) {
        this.cardH = cardH;
    }

    public String getCardW() {
        return cardW;
    }

    public void setCardW(String cardW) {
        this.cardW = cardW;
    }

    public List<String> getUrls() {
        return urls;
    }

    public void setUrls(List<String> urls) {
        this.urls = urls;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
