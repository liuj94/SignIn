package com.example.signin.bean;

import java.io.Serializable;

 
public class SiginUpListData implements Serializable {

    /**
     * searchValue : null
     * createBy : null
     * createTime : null
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
     * id : 99
     * name : 注册签到
     * type : 1
     * address : null
     * addressStatus : 1
     * startTime : null
     * endTime : null
     * signUpType : null
     * signUpLimitStatus : 1
     * limitNum : 1
     * signUpString : null
     * cardStatus : 2
     * avatarStatus : 2
     * meetingSignUpFormList : null
     * meetingId : 21
     * meetingName : 码上互动
     * createUserId : null
     * select : null
     * status : 1
     * modelType : 1
     * personChargeId : 353
     * personChargeName : 签到站EZIL9I
     * personChargeMobile : 
     * personChargeUsername : null
     * delTf : 0
     * userMeetingSignUp : null
     * userMeetingTrip : null
     * backUserMeetingTrip : null
     * userMeetingAccommodation : null
     * meetingSignUpLocation : null
     * userType : null
     * userId : null
     * beSignInCount : 0
     * signInCount : 0
     * unSignInCount : 0
     * locationCount : 1
     * localPersonChargeId : null
     * localPersonCharge : null
     * localPersonChargeMobile : null
     */

    private String searchValue= "";
    private boolean myselect;
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
   
    private String id= "";
    private String name= "";
    private int type;
    private String address= "";
    private int addressStatus;
    private String startTime= "";
    private String endTime= "";
    private String signUpType= "";
    private int signUpLimitStatus;
    private int limitNum;
    private String signUpString= "";
    private int cardStatus;
    private int avatarStatus;
    private String meetingSignUpFormList= "";
    private int meetingId;
    private String meetingName= "";
    private String createUserId= "";
    private String select= "";
    private int status;
    private String modelType= "";
    private int personChargeId;
    private String personChargeName= "";
    private String personChargeMobile= "";
    private String personChargeUsername= "";
    private int delTf;
    private String userMeetingSignUp= "";
    private String userMeetingTrip= "";
    private String backUserMeetingTrip= "";
    private String userMeetingAccommodation= "";
    private String meetingSignUpLocation= "";
    private String userType= "";
    private String userId= "";
    private int beSignInCount;
    private int signInCount;
    private int unSignInCount;
    private int locationCount;
    private String localPersonChargeId= "";
    private String localPersonCharge= "";
    private String localPersonChargeMobile= "";

    public String getSearchValue() {
        return searchValue;
    }

    public boolean isMyselect() {
        return myselect;
    }

    public void setMyselect(boolean myselect) {
        this.myselect = myselect;
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

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public int getAddressStatus() {
        return addressStatus;
    }

    public void setAddressStatus(int addressStatus) {
        this.addressStatus = addressStatus;
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

    public String getSignUpType() {
        return signUpType;
    }

    public void setSignUpType(String signUpType) {
        this.signUpType = signUpType;
    }

    public int getSignUpLimitStatus() {
        return signUpLimitStatus;
    }

    public void setSignUpLimitStatus(int signUpLimitStatus) {
        this.signUpLimitStatus = signUpLimitStatus;
    }

    public int getLimitNum() {
        return limitNum;
    }

    public void setLimitNum(int limitNum) {
        this.limitNum = limitNum;
    }

    public String getSignUpString() {
        return signUpString;
    }

    public void setSignUpString(String signUpString) {
        this.signUpString = signUpString;
    }

    public int getCardStatus() {
        return cardStatus;
    }

    public void setCardStatus(int cardStatus) {
        this.cardStatus = cardStatus;
    }

    public int getAvatarStatus() {
        return avatarStatus;
    }

    public void setAvatarStatus(int avatarStatus) {
        this.avatarStatus = avatarStatus;
    }

    public String getMeetingSignUpFormList() {
        return meetingSignUpFormList;
    }

    public void setMeetingSignUpFormList(String meetingSignUpFormList) {
        this.meetingSignUpFormList = meetingSignUpFormList;
    }

    public int getMeetingId() {
        return meetingId;
    }

    public void setMeetingId(int meetingId) {
        this.meetingId = meetingId;
    }

    public String getMeetingName() {
        return meetingName;
    }

    public void setMeetingName(String meetingName) {
        this.meetingName = meetingName;
    }

    public String getCreateUserId() {
        return createUserId;
    }

    public void setCreateUserId(String createUserId) {
        this.createUserId = createUserId;
    }

    public String getSelect() {
        return select;
    }

    public void setSelect(String select) {
        this.select = select;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getModelType() {
        return modelType;
    }

    public void setModelType(String modelType) {
        this.modelType = modelType;
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

    public String getPersonChargeUsername() {
        return personChargeUsername;
    }

    public void setPersonChargeUsername(String personChargeUsername) {
        this.personChargeUsername = personChargeUsername;
    }

    public int getDelTf() {
        return delTf;
    }

    public void setDelTf(int delTf) {
        this.delTf = delTf;
    }

    public String getUserMeetingSignUp() {
        return userMeetingSignUp;
    }

    public void setUserMeetingSignUp(String userMeetingSignUp) {
        this.userMeetingSignUp = userMeetingSignUp;
    }

    public String getUserMeetingTrip() {
        return userMeetingTrip;
    }

    public void setUserMeetingTrip(String userMeetingTrip) {
        this.userMeetingTrip = userMeetingTrip;
    }

    public String getBackUserMeetingTrip() {
        return backUserMeetingTrip;
    }

    public void setBackUserMeetingTrip(String backUserMeetingTrip) {
        this.backUserMeetingTrip = backUserMeetingTrip;
    }

    public String getUserMeetingAccommodation() {
        return userMeetingAccommodation;
    }

    public void setUserMeetingAccommodation(String userMeetingAccommodation) {
        this.userMeetingAccommodation = userMeetingAccommodation;
    }

    public String getMeetingSignUpLocation() {
        return meetingSignUpLocation;
    }

    public void setMeetingSignUpLocation(String meetingSignUpLocation) {
        this.meetingSignUpLocation = meetingSignUpLocation;
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

    public int getBeSignInCount() {
        return beSignInCount;
    }

    public void setBeSignInCount(int beSignInCount) {
        this.beSignInCount = beSignInCount;
    }

    public int getSignInCount() {
        return signInCount;
    }

    public void setSignInCount(int signInCount) {
        this.signInCount = signInCount;
    }

    public int getUnSignInCount() {
        return unSignInCount;
    }

    public void setUnSignInCount(int unSignInCount) {
        this.unSignInCount = unSignInCount;
    }

    public int getLocationCount() {
        return locationCount;
    }

    public void setLocationCount(int locationCount) {
        this.locationCount = locationCount;
    }

    public String getLocalPersonChargeId() {
        return localPersonChargeId;
    }

    public void setLocalPersonChargeId(String localPersonChargeId) {
        this.localPersonChargeId = localPersonChargeId;
    }

    public String getLocalPersonCharge() {
        return localPersonCharge;
    }

    public void setLocalPersonCharge(String localPersonCharge) {
        this.localPersonCharge = localPersonCharge;
    }

    public String getLocalPersonChargeMobile() {
        return localPersonChargeMobile;
    }

    public void setLocalPersonChargeMobile(String localPersonChargeMobile) {
        this.localPersonChargeMobile = localPersonChargeMobile;
    }
}
