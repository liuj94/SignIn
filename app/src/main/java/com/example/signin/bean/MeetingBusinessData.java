package com.example.signin.bean;

import java.io.Serializable;

/**
 * author : LiuJie
 * date   : 2023/2/226:40
 */
@lombok.Data
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
}
