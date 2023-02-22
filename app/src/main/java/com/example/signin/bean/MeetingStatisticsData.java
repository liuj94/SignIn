package com.example.signin.bean;

import java.io.Serializable;

/**
 * author : LiuJie
 * date   : 2023/2/226:36
 */
@lombok.Data
class MeetingStatisticsData implements Serializable {

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

    private String browseCount;
    private String leaveCount;//请假
    private String todayBeReviewedCount;//待审核
    private String todayInsertUserCount;//报名
    private String totalAmount;//金额
    private String userMeetingCount;//会议报名
    private String yesterdayBeReviewedCount;//昨天待审核
    private String yesterdayInsertUserCount;
    private String yesterdayLeaveCount;//昨日请假

    public String getBrowseCount() {
        return browseCount;
    }

    public void setBrowseCount(String browseCount) {
        this.browseCount = browseCount;
    }

    public String getLeaveCount() {
        return leaveCount;
    }

    public void setLeaveCount(String leaveCount) {
        this.leaveCount = leaveCount;
    }

    public String getTodayBeReviewedCount() {
        return todayBeReviewedCount;
    }

    public void setTodayBeReviewedCount(String todayBeReviewedCount) {
        this.todayBeReviewedCount = todayBeReviewedCount;
    }

    public String getTodayInsertUserCount() {
        return todayInsertUserCount;
    }

    public void setTodayInsertUserCount(String todayInsertUserCount) {
        this.todayInsertUserCount = todayInsertUserCount;
    }

    public String getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(String totalAmount) {
        this.totalAmount = totalAmount;
    }

    public String getUserMeetingCount() {
        return userMeetingCount;
    }

    public void setUserMeetingCount(String userMeetingCount) {
        this.userMeetingCount = userMeetingCount;
    }

    public String getYesterdayBeReviewedCount() {
        return yesterdayBeReviewedCount;
    }

    public void setYesterdayBeReviewedCount(String yesterdayBeReviewedCount) {
        this.yesterdayBeReviewedCount = yesterdayBeReviewedCount;
    }

    public String getYesterdayInsertUserCount() {
        return yesterdayInsertUserCount;
    }

    public void setYesterdayInsertUserCount(String yesterdayInsertUserCount) {
        this.yesterdayInsertUserCount = yesterdayInsertUserCount;
    }

    public String getYesterdayLeaveCount() {
        return yesterdayLeaveCount;
    }

    public void setYesterdayLeaveCount(String yesterdayLeaveCount) {
        this.yesterdayLeaveCount = yesterdayLeaveCount;
    }
}
