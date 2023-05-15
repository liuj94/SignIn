package com.example.signin.bean;

import java.io.Serializable;

public class SocketData implements Serializable {

    /**
     * code : 200
     * count : 1
     * type : refresh
     */

    private String code;
    private int count;
    private String type;

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
