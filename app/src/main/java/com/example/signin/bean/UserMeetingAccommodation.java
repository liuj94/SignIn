package com.example.signin.bean;

import java.io.Serializable;

 
public class UserMeetingAccommodation implements Serializable {

    /**
     * searchValue : null
     * createBy : null
     * createTime : 2023-03-31 08:48:19
     * updateBy : null
     * groupBy : null
     * dateFormat : null
     * dateType : null
     * updateTime : 2023-03-31 09:00:15
     * beginCreateTime : null
     * endCreateTime : null
     * beginCreateDate : null
     * endCreateDate : null
     * remark : null
     * params : {}
     * id : 12
     * startTime : 2023-03-31
     * endTime : 2023-04-01
     * accommodation : 某某某酒店
     * roomNo : 123456
     * num : 2
     * userMeetingId : 163
     * meetingId : null
     * signLoginId : null
     * signUpId : 281
     * signUpLocationId : 220
     * status : 1
     */

    private String searchValue;
    private String createBy;
    private String createTime;
    private String updateBy;
    private String groupBy;
    private String dateFormat;
    private String dateType;
    private String updateTime;
    private String beginCreateTime;
    private String endCreateTime;
    private String beginCreateDate;
    private String endCreateDate;
    private String remark;

    private int id;
    private String startTime;
    private String endTime;
    private String accommodation;
    private String roomNo;
    private int num;
    private int userMeetingId;
    private String meetingId;
    private String signLoginId;
    private int signUpId;
    private int signUpLocationId;
    private int status;

    public String getSearchValue() {
        return searchValue;
    }

    public void setSearchValue(String searchValue) {
        this.searchValue = searchValue;
    }

    public String getCreateBy() {
        return createBy;
    }

    public void setCreateBy(String createBy) {
        this.createBy = createBy;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getUpdateBy() {
        return updateBy;
    }

    public void setUpdateBy(String updateBy) {
        this.updateBy = updateBy;
    }

    public String getGroupBy() {
        return groupBy;
    }

    public void setGroupBy(String groupBy) {
        this.groupBy = groupBy;
    }

    public String getDateFormat() {
        return dateFormat;
    }

    public void setDateFormat(String dateFormat) {
        this.dateFormat = dateFormat;
    }

    public String getDateType() {
        return dateType;
    }

    public void setDateType(String dateType) {
        this.dateType = dateType;
    }

    public String getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(String updateTime) {
        this.updateTime = updateTime;
    }

    public String getBeginCreateTime() {
        return beginCreateTime;
    }

    public void setBeginCreateTime(String beginCreateTime) {
        this.beginCreateTime = beginCreateTime;
    }

    public String getEndCreateTime() {
        return endCreateTime;
    }

    public void setEndCreateTime(String endCreateTime) {
        this.endCreateTime = endCreateTime;
    }

    public String getBeginCreateDate() {
        return beginCreateDate;
    }

    public void setBeginCreateDate(String beginCreateDate) {
        this.beginCreateDate = beginCreateDate;
    }

    public String getEndCreateDate() {
        return endCreateDate;
    }

    public void setEndCreateDate(String endCreateDate) {
        this.endCreateDate = endCreateDate;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public String getAccommodation() {
        return accommodation;
    }

    public void setAccommodation(String accommodation) {
        this.accommodation = accommodation;
    }

    public String getRoomNo() {
        return roomNo;
    }

    public void setRoomNo(String roomNo) {
        this.roomNo = roomNo;
    }

    public int getNum() {
        return num;
    }

    public void setNum(int num) {
        this.num = num;
    }

    public int getUserMeetingId() {
        return userMeetingId;
    }

    public void setUserMeetingId(int userMeetingId) {
        this.userMeetingId = userMeetingId;
    }

    public String getMeetingId() {
        return meetingId;
    }

    public void setMeetingId(String meetingId) {
        this.meetingId = meetingId;
    }

    public String getSignLoginId() {
        return signLoginId;
    }

    public void setSignLoginId(String signLoginId) {
        this.signLoginId = signLoginId;
    }

    public int getSignUpId() {
        return signUpId;
    }

    public void setSignUpId(int signUpId) {
        this.signUpId = signUpId;
    }

    public int getSignUpLocationId() {
        return signUpLocationId;
    }

    public void setSignUpLocationId(int signUpLocationId) {
        this.signUpLocationId = signUpLocationId;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }
}
