package com.example.signin.bean;

import java.util.List;

public class TypeModel {
    /**
     * status
     * 注册报到 sys_zhuce
     住宿安排 sys_ruzhu
     会场签到 sys_huichang
     来程接机 sys_laicheng
     礼品发放 sys_liping
     返程送客 sys_fancheng
     餐饮安排 sys_canyin
     这7个 是用status

     userMeetingSignUpStatus
     user_meeting_sign_up_status


     userMeetingType
     user_meeting_type

     getDataType("sys_invoice_status")
     getDataType("sys_invoice_type")
     getDataType("transport_type")
     getDataType("sys_examine_reason")
     */
    public List<TypeData> sys_invoice_status;
    public List<TypeData> sys_invoice_type;
    public List<TypeData> transport_type;
    public List<TypeData> sys_examine_reason;



    public List<TypeData> sys_zhuce;
    public List<TypeData> pay_status;

    public List<TypeData> getPay_status() {
        return pay_status;
    }

    public void setPay_status(List<TypeData> pay_status) {
        this.pay_status = pay_status;
    }

    public List<TypeData> sys_ruzhu;
    public List<TypeData> sys_huichang;
    public List<TypeData> sys_canyin;
    public List<TypeData> sys_laicheng;
    public List<TypeData> sys_liping;
    public List<TypeData> sys_fancheng;
    public List<TypeData> user_meeting_sign_up_status;
    public List<TypeData> user_meeting_type;
    public List<TypeData> getSys_invoice_status() {
        return sys_invoice_status;
    }

    public void setSys_invoice_status(List<TypeData> sys_invoice_status) {
        this.sys_invoice_status = sys_invoice_status;
    }

    public List<TypeData> getSys_invoice_type() {
        return sys_invoice_type;
    }

    public void setSys_invoice_type(List<TypeData> sys_invoice_type) {
        this.sys_invoice_type = sys_invoice_type;
    }

    public List<TypeData> getTransport_type() {
        return transport_type;
    }

    public void setTransport_type(List<TypeData> transport_type) {
        this.transport_type = transport_type;
    }

    public List<TypeData> getSys_examine_reason() {
        return sys_examine_reason;
    }

    public void setSys_examine_reason(List<TypeData> sys_examine_reason) {
        this.sys_examine_reason = sys_examine_reason;
    }
    public List<TypeData> getSys_zhuce() {
        return sys_zhuce;
    }

    public void setSys_zhuce(List<TypeData> sys_zhuce) {
        this.sys_zhuce = sys_zhuce;
    }

    public List<TypeData> getSys_ruzhu() {
        return sys_ruzhu;
    }

    public void setSys_ruzhu(List<TypeData> sys_ruzhu) {
        this.sys_ruzhu = sys_ruzhu;
    }

    public List<TypeData> getSys_huichang() {
        return sys_huichang;
    }

    public void setSys_huichang(List<TypeData> sys_huichang) {
        this.sys_huichang = sys_huichang;
    }

    public List<TypeData> getSys_canyin() {
        return sys_canyin;
    }

    public void setSys_canyin(List<TypeData> sys_canyin) {
        this.sys_canyin = sys_canyin;
    }

    public List<TypeData> getSys_laicheng() {
        return sys_laicheng;
    }

    public void setSys_laicheng(List<TypeData> sys_laicheng) {
        this.sys_laicheng = sys_laicheng;
    }

    public List<TypeData> getSys_liping() {
        return sys_liping;
    }

    public void setSys_liping(List<TypeData> sys_liping) {
        this.sys_liping = sys_liping;
    }

    public List<TypeData> getSys_fancheng() {
        return sys_fancheng;
    }

    public void setSys_fancheng(List<TypeData> sys_fancheng) {
        this.sys_fancheng = sys_fancheng;
    }

    public List<TypeData> getUser_meeting_sign_up_status() {
        return user_meeting_sign_up_status;
    }

    public void setUser_meeting_sign_up_status(List<TypeData> user_meeting_sign_up_status) {
        this.user_meeting_sign_up_status = user_meeting_sign_up_status;
    }

    public List<TypeData> getUser_meeting_type() {
        return user_meeting_type;
    }

    public void setUser_meeting_type(List<TypeData> user_meeting_type) {
        this.user_meeting_type = user_meeting_type;
    }
}
