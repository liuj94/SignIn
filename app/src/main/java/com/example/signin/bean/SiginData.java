package com.example.signin.bean;

import java.io.Serializable;

 
public class SiginData implements Serializable {

    /**
     * searchValue : null
     * createBy : null
     * createTime : 2022-12-27 18:11:08
     * updateBy : null
     * groupBy : null
     * dateFormat : null
     * dateType : null
     * updateTime : 2023-02-10 14:58:03
     * beginCreateTime : null
     * endCreateTime : null
     * beginCreateDate : null
     * endCreateDate : null
     * remark : null
     * params : {}
     * id : 24
     * signUpId : 65
     * name : 礼物签到
     * delTf : 0
     * personChargeId : 313
     * personChargeName : 
     * personChargeMobile : null
     * status : 1
     * voiceStatus : 1
     * location : 6号卓
     * startTime : 2023-01-03 10:12
     * endTime : 2023-03-03 10:12
     * address : 酒店大堂
     * addressStatus : 2
     * signUpType : 1
     * userMeetingTypes : null
     * leveStatus : null
     * meetingId : 16
     * loginTimeStatus : null
     * insertUserInfoStatus : null
     * signUpNumStatus : 1
     * signUpNum : 1
     * useAllStatus : null
     * totalUserCount : 5
     * currentUserCount : 2
     * signUpStatus : 2
     * ticketIds : null
     * shockStatus : 2
     * autoStatus : 2
     * okMsg : 成功签到
     * repeatMsg : 重复签到
     * failedMsg : 识别失败
     * timeLong : 1
     * type : 6
     * businessId : null
     * percent : null
     * signUpCount : null
     * levelCount : null
     * siteName : null
     * personChargeUsername : null
     * userType : null
     * userId : null
     */

    private String searchValue= "";
    private boolean kuan= false;

    public boolean isKuan() {
        return kuan;
    }

    public void setKuan(boolean kuan) {
        this.kuan = kuan;
    }

    private String meetingSignUpCount= "";
    private String modelType= "";

    private String signUpNeedCount= "";
    private boolean isMyselect;

    public String getMeetingSignUpCount() {
        return meetingSignUpCount;
    }

    public void setMeetingSignUpCount(String meetingSignUpCount) {
        this.meetingSignUpCount = meetingSignUpCount;
    }

    public String getModelType() {
        return modelType;
    }

    public void setModelType(String modelType) {
        this.modelType = modelType;
    }

    public String getSignUpNeedCount() {
        return signUpNeedCount;
    }

    public void setSignUpNeedCount(String signUpNeedCount) {
        this.signUpNeedCount = signUpNeedCount;
    }

    public boolean isMyselect() {
        return isMyselect;
    }

    public void setMyselect(boolean myselect) {
        isMyselect = myselect;
    }

    private String createBy= "";
    private String createTime= "";
    private String updateBy= "";
    private String groupBy= "";
    private String dateFormat= "";
    private String dateType= "";
    private String updateTime= "";
    private String beginCreateTime= "";
    private String endCreateTime= "";
    private String beginCreateDate= "";
    private String endCreateDate= "";
    private String remark= "";

    private int id;
    private int signUpId;
    private String name= "";
    private int delTf;
    private int personChargeId;
    private String personChargeName= "";
    private String personChargeMobile= "";
    private int status;
    private int voiceStatus;
    private String speechStatus = "2";
    private String location= "";
    private String startTime= "";
    private String endTime= "";
    private String address= "";
    private String addressStatus= "";
    private int signUpType;
    private String userMeetingTypes= "";
    private String leveStatus= "";
    private int meetingId;
    private String loginTimeStatus= "";
    private String insertUserInfoStatus= "";
    private int signUpNumStatus;
    private int signUpNum;
    private String useAllStatus= "";
    private int totalUserCount;
    private int currentUserCount;
    private int localSignUpCount;
    private int signUpStatus;
    private String ticketIds= "";
    private int shockStatus;
    private int autoStatus;
    private String failedMsg = "签到失败";
    private String okMsg = "签到成功";
    private String repeatMsg = "重复签到";
    private int timeLong;
    private int type;
    private String businessId= "";
    private String percent= "";
    private String signUpCount = "0";
    private String beUserCount= "0";
    private String levelCount= "";
    private String siteName= "";
    private String personChargeUsername= "";
    private String userType= "";
    private String userId= "";

    public String getSpeechStatus() {
        return speechStatus;
    }

    public void setSpeechStatus(String speechStatus) {
        this.speechStatus = speechStatus;
    }

    public String getBeUserCount() {
        return beUserCount;
    }

    public void setBeUserCount(String beUserCount) {
        this.beUserCount = beUserCount;
    }

    public int getLocalSignUpCount() {
        return localSignUpCount;
    }

    public void setLocalSignUpCount(int localSignUpCount) {
        this.localSignUpCount = localSignUpCount;
    }

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

    public int getSignUpId() {
        return signUpId;
    }

    public void setSignUpId(int signUpId) {
        this.signUpId = signUpId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getDelTf() {
        return delTf;
    }

    public void setDelTf(int delTf) {
        this.delTf = delTf;
    }

    public int getPersonChargeId() {
        return personChargeId;
    }

    public void setPersonChargeId(int personChargeId) {
        this.personChargeId = personChargeId;
    }

    public String getPersonChargeName() {
        return personChargeName;
    }

    public void setPersonChargeName(String personChargeName) {
        this.personChargeName = personChargeName;
    }

    public String getPersonChargeMobile() {
        return personChargeMobile;
    }

    public void setPersonChargeMobile(String personChargeMobile) {
        this.personChargeMobile = personChargeMobile;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public int getVoiceStatus() {
        return voiceStatus;
    }

    public void setVoiceStatus(int voiceStatus) {
        this.voiceStatus = voiceStatus;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
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

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getAddressStatus() {
        return addressStatus;
    }

    public void setAddressStatus(String addressStatus) {
        this.addressStatus = addressStatus;
    }

    public int getSignUpType() {
        return signUpType;
    }

    public void setSignUpType(int signUpType) {
        this.signUpType = signUpType;
    }

    public String getUserMeetingTypes() {
        return userMeetingTypes;
    }

    public void setUserMeetingTypes(String userMeetingTypes) {
        this.userMeetingTypes = userMeetingTypes;
    }

    public String getLeveStatus() {
        return leveStatus;
    }

    public void setLeveStatus(String leveStatus) {
        this.leveStatus = leveStatus;
    }

    public int getMeetingId() {
        return meetingId;
    }

    public void setMeetingId(int meetingId) {
        this.meetingId = meetingId;
    }

    public String getLoginTimeStatus() {
        return loginTimeStatus;
    }

    public void setLoginTimeStatus(String loginTimeStatus) {
        this.loginTimeStatus = loginTimeStatus;
    }

    public String getInsertUserInfoStatus() {
        return insertUserInfoStatus;
    }

    public void setInsertUserInfoStatus(String insertUserInfoStatus) {
        this.insertUserInfoStatus = insertUserInfoStatus;
    }

    public int getSignUpNumStatus() {
        return signUpNumStatus;
    }

    public void setSignUpNumStatus(int signUpNumStatus) {
        this.signUpNumStatus = signUpNumStatus;
    }

    public int getSignUpNum() {
        return signUpNum;
    }

    public void setSignUpNum(int signUpNum) {
        this.signUpNum = signUpNum;
    }

    public String getUseAllStatus() {
        return useAllStatus;
    }

    public void setUseAllStatus(String useAllStatus) {
        this.useAllStatus = useAllStatus;
    }

    public int getTotalUserCount() {
        return totalUserCount;
    }

    public void setTotalUserCount(int totalUserCount) {
        this.totalUserCount = totalUserCount;
    }

    public int getCurrentUserCount() {
        return currentUserCount;
    }

    public void setCurrentUserCount(int currentUserCount) {
        this.currentUserCount = currentUserCount;
    }

    public int getSignUpStatus() {
        return signUpStatus;
    }

    public void setSignUpStatus(int signUpStatus) {
        this.signUpStatus = signUpStatus;
    }

    public String getTicketIds() {
        return ticketIds;
    }

    public void setTicketIds(String ticketIds) {
        this.ticketIds = ticketIds;
    }

    public int getShockStatus() {
        return shockStatus;
    }

    public void setShockStatus(int shockStatus) {
        this.shockStatus = shockStatus;
    }

    public int getAutoStatus() {
        return autoStatus;
    }

    public void setAutoStatus(int autoStatus) {
        this.autoStatus = autoStatus;
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

    public String getFailedMsg() {
        return failedMsg;
    }

    public void setFailedMsg(String failedMsg) {
        this.failedMsg = failedMsg;
    }

    public int getTimeLong() {
        return timeLong;
    }

    public void setTimeLong(int timeLong) {
        this.timeLong = timeLong;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getBusinessId() {
        return businessId;
    }

    public void setBusinessId(String businessId) {
        this.businessId = businessId;
    }

    public String getPercent() {
        return percent;
    }

    public void setPercent(String percent) {
        this.percent = percent;
    }

    public String getSignUpCount() {
        return signUpCount;
    }

    public void setSignUpCount(String signUpCount) {
        this.signUpCount = signUpCount;
    }

    public String getLevelCount() {
        return levelCount;
    }

    public void setLevelCount(String levelCount) {
        this.levelCount = levelCount;
    }

    public String getSiteName() {
        return siteName;
    }

    public void setSiteName(String siteName) {
        this.siteName = siteName;
    }

    public String getPersonChargeUsername() {
        return personChargeUsername;
    }

    public void setPersonChargeUsername(String personChargeUsername) {
        this.personChargeUsername = personChargeUsername;
    }

    public String getUserType() {
        return userType;
    }

    public void setUserType(String userType) {
        this.userType = userType;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
}
