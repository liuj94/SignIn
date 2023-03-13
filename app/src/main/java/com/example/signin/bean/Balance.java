package com.example.signin.bean;

import java.io.Serializable;

public class Balance implements Serializable {

    /**
     * searchValue : null
     * createBy : null
     * createTime : 2022-11-17 16:18:50
     * updateBy : null
     * groupBy : null
     * dateFormat : null
     * dateType : null
     * updateTime : null
     * beginCreateTime : null
     * endCreateTime : null
     * beginCreateDate : null
     * endCreateDate : null
     * remark : null
     * params : {}
     * businessId : 1
     * balance : 0.01
     * smsNum : 20
     * peopleNum : 0
     * talkTime : 0
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

    private String businessId;
    private String balance;
    private String smsNum;
    private String peopleNum;
    private int talkTime;

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

    public String getBusinessId() {
        return businessId;
    }

    public void setBusinessId(String businessId) {
        this.businessId = businessId;
    }

    public String getBalance() {
        return balance;
    }

    public void setBalance(String balance) {
        this.balance = balance;
    }

    public String getSmsNum() {
        return smsNum;
    }

    public void setSmsNum(String smsNum) {
        this.smsNum = smsNum;
    }

    public String getPeopleNum() {
        return peopleNum;
    }

    public void setPeopleNum(String peopleNum) {
        this.peopleNum = peopleNum;
    }

    public int getTalkTime() {
        return talkTime;
    }

    public void setTalkTime(int talkTime) {
        this.talkTime = talkTime;
    }
}
