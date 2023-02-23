package com.example.signin.net;

import android.text.TextUtils;

public class BaseJson<T> {
    private String message;
    private String msg;
    private int total;
    private T data;
    private int code;

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public String getMsg() {
        if (TextUtils.isEmpty(msg)) {
            return "";
        }
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getMessage() {
        if (TextUtils.isEmpty(message)) {
            return "";
        }
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public boolean isSuccess() {
        return this.code == 200;
    }

    @Override
    public String toString() {
        return "BaseJson{" +
                "message='" + message + '\'' +
                ", msg='" + msg + '\'' +
                ", result=" + data +
                ", status=" + code +
                '}';
    }
}
