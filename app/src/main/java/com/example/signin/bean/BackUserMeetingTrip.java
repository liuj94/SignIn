package com.example.signin.bean;

import java.io.Serializable;

/**
 * author : LiuJie
 * date   : 2023/2/2421:17
 */

public class BackUserMeetingTrip implements Serializable {

    /**
     * searchValue : null
     * createBy : null
     * createTime : 2023-02-18 15:38:24
     * updateBy : null
     * groupBy : null
     * dateFormat : null
     * dateType : null
     * updateTime : null
     * beginCreateTime : null
     * endCreateTime : null
     * beginCreateDate : null
     * endCreateDate : null
     * remark : T1231
     * params : {}
     * id : 8
     * userId : 1905
     * userMeetingId : 33
     * type : 2
     * startTime : 00:00
     * endTime : 05:04
     * startDate : 2023-02-19
     * endDate : 2023-02-19
     * startCity : 宁波市
     * startAddress : 栎社机场
     * endCity : 杭州东
     * endAddress : 萧山国际机场
     * transport : 03
     * signUpId : null
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
    private String remark="";

    private int id;
    private int userId;
    private int userMeetingId;
    private int type;
    private String startTime;
    private String endTime;
    private String startDate;
    private String endDate;
    private String startCity="";
    private String startAddress="";
    private String endCity="";
    private String endAddress ="";
    private String transport;
    private String signUpId;

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

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getUserMeetingId() {
        return userMeetingId;
    }

    public void setUserMeetingId(int userMeetingId) {
        this.userMeetingId = userMeetingId;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
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

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    public String getStartCity() {
        return startCity;
    }

    public void setStartCity(String startCity) {
        this.startCity = startCity;
    }

    public String getStartAddress() {
        return startAddress;
    }

    public void setStartAddress(String startAddress) {
        this.startAddress = startAddress;
    }

    public String getEndCity() {
        return endCity;
    }

    public void setEndCity(String endCity) {
        this.endCity = endCity;
    }

    public String getEndAddress() {
        return endAddress;
    }

    public void setEndAddress(String endAddress) {
        this.endAddress = endAddress;
    }

    public String getTransport() {
        return transport;
    }

    public void setTransport(String transport) {
        this.transport = transport;
    }

    public String getSignUpId() {
        return signUpId;
    }

    public void setSignUpId(String signUpId) {
        this.signUpId = signUpId;
    }
}
