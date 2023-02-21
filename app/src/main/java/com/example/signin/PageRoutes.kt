package com.example.signin


interface PageRoutes {

    companion object {
        val BaseUrl = "https://meeting.nbqichen.com:20882/prod-api"


        //登录
        val Api_login = "$BaseUrl/login"
        val Api_editUser = "$BaseUrl/system/user"
        val Api_getUserInfo = "$BaseUrl/getInfo"
        //appStatus	-1,全部;1,进行中;2,过期
        val Api_meetingList = "$BaseUrl/manager/meeting/list"
        val Api_meeting_statistics = "$BaseUrl/manager/meeting/meeting/statistics/{id}"
        val Api_meeting_business = "$BaseUrl/manager/meeting/business/{id}"

        //type 1 注册签到2 来程签到3 入住签到4 会场签到5 餐饮签到6 礼品签到7 返程签到
        val Api_meetingSignUpLocationList = "$BaseUrl/manager/meetingSignUpLocation/list"
        val Api_meetingSignUpLocation = "$BaseUrl/manager/meetingSignUpLocation/{id}"

    }

}
