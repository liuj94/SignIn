package com.example.signin.net;

public class RequestException extends RuntimeException {
    private String Msg;
    private int mStatusCode;

    public RequestException(int Status, String msg) {
        super(msg);
        this.mStatusCode = Status;
        this.Msg = msg;
    }

    public String getMsg() {
        return Msg;
    }

    public int getStatusCode() {
        return mStatusCode;
    }

}
