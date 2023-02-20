package com.example.signin.net;

import android.text.TextUtils;

public class BaseJson<T> {
    private String message;
    private String msg;
    private T result;
    private int status;

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

    public T getResult() {
        return result;
    }

    public void setResult(T result) {
        this.result = result;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public boolean isSuccess() {
        return this.status == 0;
    }

    @Override
    public String toString() {
        return "BaseJson{" +
                "message='" + message + '\'' +
                ", msg='" + msg + '\'' +
                ", result=" + result +
                ", status=" + status +
                '}';
    }
}
