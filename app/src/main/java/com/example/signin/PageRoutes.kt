package com.example.signin


interface PageRoutes {

    companion object {
        val BaseUrlApi = "https://meeting.nbqichen.com:20882/prod-api"
        val BaseUrl = "https://meeting.nbqichen.com:20882"


        //登录
        val Api_login = "$BaseUrlApi/login"
        val Api_editUser = "$BaseUrlApi/system/user"
        val Api_getUserInfo = "$BaseUrlApi/getInfo"
        //appStatus	-1,全部;1,进行中;2,过期
        val Api_meetingList = "$BaseUrlApi/manager/meeting/list?"

//        val Api_meeting_statistics = "$BaseUrl/manager/meeting/meeting/statistics/{id}"
        val Api_meeting_statistics = "$BaseUrlApi/manager/meeting/meeting/statistics/"
//        val Api_meeting_business = "$BaseUrl/manager/meeting/business/{id}"
        val Api_meeting_business = "$BaseUrlApi/manager/meeting/business/"

        val Api_ed_meetingSignUpLocation = "$BaseUrlApi/manager/meetingSignUpLocation"

        val Api_meeting_sign_up_app_list = "$BaseUrlApi/manager/meeting/sign/up/app/list?meetingId="


        //manager/meetingSignUpLocation/list?meetingId=20&signUpId=
        val Api_meetingSignUpLocation = "$BaseUrlApi/manager/meetingSignUpLocation/list?meetingId="
        val Api_meetinguser = "$BaseUrlApi/manager/meeting/user/list?meetingId="
//        val Api_meetinguser_data = "$BaseUrlApi/manager/meeting/user/35?id=35"
        val Api_meetinguser_data = "$BaseUrlApi/manager/meeting/user/"

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
         */
        val Api_datatype = "$BaseUrlApi/common/dict/data/type/"

//        val Api_datatype = "$BaseUrlApi/manager/meeting/sign/up/{id}"
        val Api_signdata = "$BaseUrlApi/manager/meeting/sign/up/"

//        val Api_signdata = "$BaseUrlApi/manager/meetingSignUpLocation/{id}"
        val Api_meetingSignUpLocationDe = "$BaseUrlApi/manager/meetingSignUpLocation/"

       //审核订单
        val Api_examine = "$BaseUrlApi/exe/meeting/user/examine"
        //签到
        val Api_sigin = "$BaseUrlApi/exe/meeting/user"




    }

}
