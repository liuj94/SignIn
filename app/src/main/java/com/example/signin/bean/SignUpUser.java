package com.example.signin.bean;

import java.io.Serializable;


public class SignUpUser implements Serializable {



    /**
     * location :
     * meetingId : 0
     * signUpId : 0
     * signUpLocationId : 0
     * status : 0
     * userMeetingId : 0
     * {"id":33,"name":"%u5446%u6D4B%u8BD5",
     * "corporateName":"%u4F01%u5BB8%u8F6F%u4EF6",
     * "supplement":"%u5609%u5BBE",
     * "meetingId":20,"nowTime":1677296122085}
     */

//1开启 2关闭 自动
    private String autoStatus = "2";
    private String voiceStatus = "2";
    private String name= "";
    private int timeLong = 3;
    private String id= "";
    private String corporateName = "";
    private String supplement= "";
    private String location = "";
    private String meetingId= "";
    private String meetingName= "";
    private String signUpId= "";
    private String signUpLocationId= "";
    private String status = "1";
    private String userMeetingId= "";
    private String userMeetingTypeName = "";
    private String failedMsg = "签到失败";
    private String okMsg = "签到成功";
    private String repeatMsg = "重复签到";
    private String success = "";

    public String getVoiceStatus() {
        return voiceStatus;
    }

    public void setVoiceStatus(String voiceStatus) {
        this.voiceStatus = voiceStatus;
    }

    public String getSuccess() {
        return success;
    }

    public void setSuccess(String success) {
        this.success = success;
    }

    public String getAutoStatus() {
        return autoStatus;
    }

    public int getTimeLong() {
        return timeLong;
    }

    public void setTimeLong(int timeLong) {
        this.timeLong = timeLong;
    }

    public String getFailedMsg() {
        return failedMsg;
    }

    public void setFailedMsg(String failedMsg) {
        this.failedMsg = failedMsg;
    }

    public String getOkMsg() {
        return okMsg;
    }

    public void setOkMsg(String okMsg) {
        this.okMsg = okMsg;
    }

    public String getRepeatMsg() {
        return repeatMsg;
    }

    public void setRepeatMsg(String repeatMsg) {
        this.repeatMsg = repeatMsg;
    }

    public void setAutoStatus(String autoStatus) {
        this.autoStatus = autoStatus;
    }
    public String getUserMeetingTypeName() {
        return userMeetingTypeName;
    }

    public void setUserMeetingTypeName(String userMeetingTypeName) {
        this.userMeetingTypeName = userMeetingTypeName;
    }

    public String getMeetingName() {
        return meetingName;
    }

    public void setMeetingName(String meetingName) {
        this.meetingName = meetingName;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getMeetingId() {
        return meetingId;
    }

    public void setMeetingId(String meetingId) {
        this.meetingId = meetingId;
    }

    public String getSignUpId() {
        return signUpId;
    }

    public void setSignUpId(String signUpId) {
        this.signUpId = signUpId;
    }

    public String getSignUpLocationId() {
        return signUpLocationId;
    }

    public void setSignUpLocationId(String signUpLocationId) {
        this.signUpLocationId = signUpLocationId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getUserMeetingId() {
        return userMeetingId;
    }

    public void setUserMeetingId(String userMeetingId) {
        this.userMeetingId = userMeetingId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCorporateName() {
        return corporateName;
    }

    public void setCorporateName(String corporateName) {
        this.corporateName = corporateName;
    }

    public String getSupplement() {
        return supplement;
    }

    public void setSupplement(String supplement) {
        this.supplement = supplement;
    }

    @Override
    public String toString() {
        return "SignUpUser{" +
                "name='" + name + '\'' +
                ", id='" + id + '\'' +
                ", corporateName='" + corporateName + '\'' +
                ", supplement='" + supplement + '\'' +
                ", location='" + location + '\'' +
                ", meetingId='" + meetingId + '\'' +
                ", signUpId='" + signUpId + '\'' +
                ", signUpLocationId='" + signUpLocationId + '\'' +
                ", status='" + status + '\'' +
                ", userMeetingId='" + userMeetingId + '\'' +
                '}';
    }
}
