package com.example.signin.bean;

import java.io.Serializable;

/**
 * author : LiuJie
 * date   : 2023/2/226:36
 */

public class MeetingStatisticsData implements Serializable {

    /**
     * browseCount : 0
     * leaveCount : 0
     * todayBeReviewedCount : 0
     * todayInsertUserCount : 0
     * totalAmount : 0
     * userMeetingCount : 0
     * yesterdayBeReviewedCount : 0
     * yesterdayInsertUserCount : 0
     * yesterdayLeaveCount : 0
     */

    private int browseCount= 0;
    private int leaveCount= 0;//请假
    private int todayBeReviewedCount= 0;//待审核
    private int todayInsertUserCount= 0;//报名
    private String totalAmount = "0";//金额
    private int userMeetingCount= 0;//会议报名
    private int yesterdayBeReviewedCount= 0;//昨天待审核
    private int yesterdayInsertUserCount= 0;
    private int yesterdayLeaveCount= 0;//昨日请假

    public int getUserMeetingCount() {
        return userMeetingCount;
    }

    public void setUserMeetingCount(int userMeetingCount) {
        this.userMeetingCount = userMeetingCount;
    }

    public int getBrowseCount() {
        return browseCount;
    }

    public void setBrowseCount(int browseCount) {
        this.browseCount = browseCount;
    }

    public int getLeaveCount() {
        return leaveCount;
    }

    public void setLeaveCount(int leaveCount) {
        this.leaveCount = leaveCount;
    }

    public int getTodayBeReviewedCount() {
        return todayBeReviewedCount;
    }

    public void setTodayBeReviewedCount(int todayBeReviewedCount) {
        this.todayBeReviewedCount = todayBeReviewedCount;
    }

    public int getTodayInsertUserCount() {
        return todayInsertUserCount;
    }

    public void setTodayInsertUserCount(int todayInsertUserCount) {
        this.todayInsertUserCount = todayInsertUserCount;
    }

    public String getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(String totalAmount) {
        this.totalAmount = totalAmount;
    }



    public int getYesterdayBeReviewedCount() {
        return yesterdayBeReviewedCount;
    }

    public void setYesterdayBeReviewedCount(int yesterdayBeReviewedCount) {
        this.yesterdayBeReviewedCount = yesterdayBeReviewedCount;
    }

    public int getYesterdayInsertUserCount() {
        return yesterdayInsertUserCount;
    }

    public void setYesterdayInsertUserCount(int yesterdayInsertUserCount) {
        this.yesterdayInsertUserCount = yesterdayInsertUserCount;
    }

    public int getYesterdayLeaveCount() {
        return yesterdayLeaveCount;
    }

    public void setYesterdayLeaveCount(int yesterdayLeaveCount) {
        this.yesterdayLeaveCount = yesterdayLeaveCount;
    }
}
