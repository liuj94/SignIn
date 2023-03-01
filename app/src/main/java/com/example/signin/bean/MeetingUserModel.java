package com.example.signin.bean;

import java.util.List;

public class MeetingUserModel {

    private String message= "";
    private String msg;
    private int total;
    private List<MeetingUserData> data;
    private int code;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public List<MeetingUserData> getData() {
        return data;
    }

    public void setData(List<MeetingUserData> data) {
        this.data = data;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }
}
