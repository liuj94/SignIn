package com.example.signin.bean;

import java.util.ArrayList;
import java.util.List;

public class MeetingFormData {
    private List<MeetingFormList> meetingFormList= new ArrayList<>();

    public List<MeetingFormList> getMeetingFormList() {
        return meetingFormList;
    }

    public void setMeetingFormList(List<MeetingFormList> meetingFormList) {
        this.meetingFormList = meetingFormList;
    }
}
