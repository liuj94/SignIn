package com.example.signin.bean;

public class TypeData {
//    "dictLabel": "待填写",
//            "dictValue": "0",
//            "dictType": "user_meeting_sign_up_status",
    public boolean myselect;
    public String dictLabel= "";;
    public String dictValue= "";;
    public String dictType;

    public boolean isMyselect() {
        return myselect;
    }

    public void setMyselect(boolean myselect) {
        this.myselect = myselect;
    }

    public String getDictLabel() {
        return dictLabel;
    }

    public void setDictLabel(String dictLabel) {
        this.dictLabel = dictLabel;
    }

    public String getDictValue() {
        return dictValue;
    }

    public void setDictValue(String dictValue) {
        this.dictValue = dictValue;
    }

    public String getDictType() {
        return dictType;
    }

    public void setDictType(String dictType) {
        this.dictType = dictType;
    }
}
