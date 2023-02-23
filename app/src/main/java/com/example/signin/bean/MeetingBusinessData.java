package com.example.signin.bean;

import java.io.Serializable;

/**
 * author : LiuJie
 * date   : 2023/2/226:40
 */

class MeetingBusinessData implements Serializable {

    /**
     * enterCount : 0
     * id : 0
     * name :
     * signUpCount : 0
     * signUpPercent : 0
     * unSignUpCount : 0
     * unSignUpPercent : 0
     */

    private String enterCount;//参会总数
    private int id;
    private String name;
    private String signUpCount;//已签到数
    private String signUpPercent;//已签到比率
    private String unSignUpCount;//未签到数量
    private String unSignUpPercent;//未签到比率

    public String getEnterCount() {
        return enterCount;
    }

    public void setEnterCount(String enterCount) {
        this.enterCount = enterCount;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSignUpCount() {
        return signUpCount;
    }

    public void setSignUpCount(String signUpCount) {
        this.signUpCount = signUpCount;
    }

    public String getSignUpPercent() {
        return signUpPercent;
    }

    public void setSignUpPercent(String signUpPercent) {
        this.signUpPercent = signUpPercent;
    }

    public String getUnSignUpCount() {
        return unSignUpCount;
    }

    public void setUnSignUpCount(String unSignUpCount) {
        this.unSignUpCount = unSignUpCount;
    }

    public String getUnSignUpPercent() {
        return unSignUpPercent;
    }

    public void setUnSignUpPercent(String unSignUpPercent) {
        this.unSignUpPercent = unSignUpPercent;
    }
}
