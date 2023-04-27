package com.example.signin

import cn.bingoogolapple.qrcode.core.QRCodeView
import com.alibaba.fastjson.JSON
import com.dylanc.longan.startActivity
import com.dylanc.longan.toast
import com.example.signin.base.BaseBindingActivity
import com.example.signin.base.BaseViewModel
import com.example.signin.bean.MeetingUserDeData
import com.example.signin.bean.SignUpUser
import com.example.signin.databinding.ActScanBinding
import com.example.signin.net.RequestCallback
import com.example.signin.scan.KeyEventResolver
import com.hello.scan.ScanCallBack
import com.lzy.okgo.OkGo
import com.lzy.okgo.model.Response
import sigin

class ScanActivity : BaseBindingActivity<ActScanBinding, BaseViewModel>() , QRCodeView.Delegate,
    ScanCallBack, KeyEventResolver.OnScanSuccessListener {


    override fun getViewModel(): Class<BaseViewModel> = BaseViewModel::class.java
    var id = ""
    var signUpId = ""
    var meetingid = ""
    var autoStatus = ""
    var timeLong = 3
    var showType = 0
    var name = ""
    var signUpStatus = ""
    var nameMobile: String? = ""
    var params: String? = ""
    var failedMsg:String = "签到失败"
    var okMsg:String = "签到成功"
    var repeatMsg:String = "重复签到"
    var voiceStatus:String = "2"


    override fun initData() {
//        if(SystemUtil.getInternalModel().equals("P8")){
//            openHardreader()
//        }else{
//            ScanTool.GET.initSerial(this, "/dev/ttyACM0", 115200, this@ScanActivity)
//        }
//        ScanTool.GET.initSerial(this, "/dev/ttyACM0", 115200, this@ScanActivity)
//        openHardreader()
        intent.getStringExtra("id")?.let { id = it }
        intent.getStringExtra("name")?.let { name = it }
        intent.getStringExtra("params")?.let { params = it }
        intent.getStringExtra("meetingid")?.let { meetingid = it }
        intent.getStringExtra("signUpId")?.let { signUpId = it }
        intent.getStringExtra("autoStatus")?.let { autoStatus = it }
        intent.getStringExtra("okMsg")?.let { okMsg = it }
        intent.getStringExtra("failedMsg")?.let { failedMsg = it }
        intent.getStringExtra("repeatMsg")?.let { repeatMsg = it }
        intent.getStringExtra("voiceStatus")?.let { voiceStatus = it }
        intent.getIntExtra("timeLong",3)?.let { timeLong = it }
        intent.getIntExtra("showType",0)?.let { showType = it }
        binding.mZXingView.setDelegate(this)

    }



    override fun onScanQRCodeSuccess(result: String?) {

        result?.let {param->
            var signUpUser = JSON.parseObject(param, SignUpUser::class.java)
            if(signUpUser.id.isNullOrEmpty()){
                signUpUser.signUpLocationId = id
                signUpUser.meetingName = name
                signUpUser.signUpId = signUpId
                signUpUser.userMeetingId = ""
                signUpUser.meetingId = ""
                signUpUser.userMeetingTypeName = ""
                signUpUser.autoStatus =  autoStatus
                signUpUser.timeLong =  timeLong
                signUpUser.okMsg = okMsg
                signUpUser.failedMsg = failedMsg
                signUpUser.repeatMsg = repeatMsg
                signUpUser.voiceStatus = voiceStatus
                signUpUser.success = "500"
                com.dylanc.longan.startActivity<SiginReAutoActivity>(
                    "type" to showType,
                    "data" to signUpUser
                )
                return
            }
            getData(signUpUser.id)

//            if(autoStatus.equals("1")){
//
//            }else{
//                com.dylanc.longan.startActivity<SiginReActivity>(
//                    "type" to showType,
//                    "data" to signUpUser
//                )
//            }
        }

    }
    private fun getData(userid:String) {
        mViewModel.isShowLoading.value = true
        OkGo.get<MeetingUserDeData>(PageRoutes.Api_meetinguser_data + userid + "?id=" + userid)
            .tag(PageRoutes.Api_meetinguser_data + userid + "?id=" + userid)
            .headers("Authorization", kv.getString("token", ""))
            .execute(object : RequestCallback<MeetingUserDeData>() {
                override fun onSuccessNullData() {
                    super.onSuccessNullData()

                }

                override fun onMySuccess(data: MeetingUserDeData) {
                    super.onMySuccess(data)
                    var signUpUser = SignUpUser()
                    signUpUser.signUpLocationId = id
                    signUpUser.meetingName = name
                    signUpUser.signUpId = signUpId
                    signUpUser.userMeetingId = userid
                    signUpUser.meetingId = ""+data.meetingId
                    signUpUser.userMeetingTypeName = ""+data.userMeetingTypeName
                    signUpUser.autoStatus =  autoStatus
                    signUpUser.timeLong =  timeLong
                    signUpUser.okMsg = okMsg
                    signUpUser.failedMsg = failedMsg
                    signUpUser.repeatMsg = repeatMsg
                    signUpUser.voiceStatus = voiceStatus
                    if(showType==3){
                        com.dylanc.longan.startActivity<SiginReActivity>(
                            "type" to showType,
                            "data" to signUpUser
                        )
                    }else{
                        var avatar = ""
                        data.avatar?.let { avatar = it }
                        var params = java.util.HashMap<String, String>()
                        params["meetingId"] = signUpUser.meetingId//会议id
                        params["signUpLocationId"] = signUpUser.signUpLocationId//签到点id
                        params["signUpId"] = signUpUser.signUpId//签到站id
                        params["userMeetingId"] = signUpUser.userMeetingId//用户参与会议id
                        params["status"] = "2"//用户参与会议id
                        sigin(JSON.toJSONString(params),{ success->
                            signUpUser.success = success
                            com.dylanc.longan.startActivity<SiginReAutoActivity>(
                                "type" to showType,
                                "data" to signUpUser,
                                "avatar" to avatar
                            )
                        },{  signUpUser.success = "500"
                            com.dylanc.longan.startActivity<SiginReAutoActivity>(
                                "type" to showType,
                                "data" to signUpUser,
                                "avatar" to avatar
                            )},{})}


                }

                override fun onError(response: Response<MeetingUserDeData>) {
                    super.onError(response)

                }

                override fun onFinish() {
                    super.onFinish()
                    mViewModel.isShowLoading.value = false
                }


            })
    }
    override fun onCameraAmbientBrightnessChanged(isDark: Boolean) {

    }
var scanQRCodeOpenCameraError = false
    override fun onScanQRCodeOpenCameraError() {
        scanQRCodeOpenCameraError = true
    }
    override fun onStart() {
        super.onStart()
        if(!scanQRCodeOpenCameraError){ binding.mZXingView.startCamera() }
        // 打开后置摄像头开始预览，但是并未开始识别}



    }

    override fun onResume() {
        super.onResume()
        isPause = false
        if(!scanQRCodeOpenCameraError){  binding.mZXingView.startSpotAndShowRect() }
        // 显示扫描框，并开始识别}
    }

    override fun onPause() {
        super.onPause()
        isPause = true
    }
    override fun onStop() {
        if(!scanQRCodeOpenCameraError) {
            binding.mZXingView.stopSpotAndHiddenRect()
        }
        super.onStop()
    }
var isPause =true
    override fun onScanCallBack(data: String?) {
        try {
        goRe(data)
        } catch (e: Exception) {
        }
    }

    private fun goRe(data: String?) {
        if (isPause) {
            return
        }
        try {
            var signUpUser = JSON.parseObject(data, SignUpUser::class.java)

            signUpUser.signUpLocationId = id
            signUpUser.meetingName = name
            signUpUser.signUpId = signUpId
            signUpUser.userMeetingId = signUpUser.id
            signUpUser.meetingId = signUpUser.meetingId
            signUpUser.userMeetingTypeName = signUpUser.supplement
            signUpUser.autoStatus = autoStatus
            signUpUser.timeLong = timeLong
            signUpUser.okMsg = okMsg
            signUpUser.failedMsg = failedMsg
            signUpUser.repeatMsg = repeatMsg
            signUpUser.voiceStatus = voiceStatus
            if (autoStatus.equals("1")) {
                if (showType == 3) {
                    com.dylanc.longan.startActivity<SiginReActivity>(
                        "type" to showType,
                        "data" to signUpUser
                    )
                } else {
                    var params = HashMap<String, String>()
                    params["meetingId"] = signUpUser.meetingId//会议id
                    params["signUpLocationId"] = signUpUser.signUpLocationId//签到点id
                    params["signUpId"] = signUpUser.signUpId//签到站id
                    params["userMeetingId"] = signUpUser.userMeetingId//用户参与会议id
                    params["status"] = "2"//用户参与会议id
                    sigin(JSON.toJSONString(params), { success ->
                        signUpUser.success = success
                        com.dylanc.longan.startActivity<SiginReAutoActivity>(
                            "type" to showType,
                            "data" to signUpUser
                        )
                    }, {
                        signUpUser.success = "500"
                        startActivity<SiginReAutoActivity>(
                            "type" to showType,
                            "data" to signUpUser
                        )
                    }, {})
                }
            } else {
                com.dylanc.longan.startActivity<SiginReActivity>(
                    "type" to showType,
                    "data" to signUpUser
                )
            }
        } catch (e: Exception) {
            toast("二维码解析失败")
        }
    }

    override fun onDestroy() {
        if(!scanQRCodeOpenCameraError){
        binding.mZXingView.onDestroy() }
        // 销毁二维码扫描控件
        super.onDestroy()
    }

    override fun onScanSuccess(barcode: String?) {
        try {
            goRe(barcode)
        } catch (e: Exception) {
        }
    }
}