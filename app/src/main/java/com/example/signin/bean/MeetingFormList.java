package com.example.signin.bean;

public class MeetingFormList {
    public String 	name;
    public int 	showStatus;
    public String 	value;
    public String 	type;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getShowStatus() {
        return showStatus;
    }

    public void setShowStatus(int showStatus) {
        this.showStatus = showStatus;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return "MeetingFormList{" +
                "name='" + name + '\'' +
                ", showStatus=" + showStatus +
                ", value='" + value + '\'' +
                ", type='" + type + '\'' +
                '}';
    }
}
