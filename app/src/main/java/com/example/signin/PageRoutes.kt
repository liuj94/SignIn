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

    }

}
