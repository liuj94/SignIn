package com.example.signin.bean;

import java.io.Serializable;


public class UploadData implements Serializable {

    /**
     * fileName :
     * url :
     */

    private String fileName= "";;
    private String url= "";;

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
