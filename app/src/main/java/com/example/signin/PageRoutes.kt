package com.example.signin


interface PageRoutes {

    companion object {
        val BaseUrlApi = "https://meeting.nbqichen.com:20882/prod-api"
        val BaseUrl = "https://meeting.nbqichen.com:20882"


        //登录
        val Api_login = "$BaseUrlApi/login"
        val Api_editUser = "$BaseUrlApi/manager/system/user"
//        val Api_getUserInfo = "$BaseUrlApi/getInfo"
        val Api_getUserInfo = "$BaseUrlApi/manager/system/user/getInfo"
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
        val Api_voice = "$BaseUrlApi/manager/voice"
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
//        val Api_examine = "$BaseUrlApi/exe/meeting/user/examine"
        val Api_examine = "$BaseUrlApi/manager/meeting/order/examine"
        //签到
//        val Api_sigin = "$BaseUrlApi/exe/meeting/user"
        val Api_sigin = "$BaseUrlApi/manager/meeting/user"

//        val Api_sigin = "$BaseUrlApi/manager/meeting/order/{id}"
        val Api_order = "$BaseUrlApi/manager/meeting/order/"


        val Api_upload = "$BaseUrlApi/common/upload"

        val Api_billfinish = "$BaseUrlApi/manager/meeting/order/bill/finish"
//        val Api_bill = "$BaseUrlApi/manager/meeting/order/bill/{orderId}"
        val Api_bill = "$BaseUrlApi/manager/meeting/order/bill/"

        val Api_detect = "$BaseUrlApi/common/baidu/aip/detect?img="
        val Api_search = "$BaseUrlApi/common/baidu/aip/search?img="
        val Api_balance = "$BaseUrlApi/manager/business/current/balance?aBusinessId="

        val baiduToken = "https://aip.baidubce.com/oauth/2.0/token?grant_type=client_credentials&client_id=Va5yQRHlA4Fq5eR3LT0vuXV4&client_secret=0rDSjzQ20XUj5itV6WRtznPQSzr5pVw2&"



    }

}
