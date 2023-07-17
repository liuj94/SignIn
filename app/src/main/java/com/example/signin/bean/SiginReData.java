package com.example.signin.bean;

import java.io.Serializable;

public class SiginReData implements Serializable {

    private String code;
    private SocketData data;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public SocketData getData() {
        return data;
    }

    public void setData(SocketData data) {
        this.data = data;
    }
}
