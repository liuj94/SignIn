package com.example.signin

import android.util.Log
import cn.bingoogolapple.qrcode.core.QRCodeView
import com.afollestad.materialdialogs.MaterialDialog
import com.alibaba.fastjson.JSON
import com.example.signin.base.BaseBindingActivity
import com.example.signin.base.BaseViewModel
import com.example.signin.bean.SignUpUser
import com.example.signin.databinding.ActScanBinding
import sigin

class ScanActivity : BaseBindingActivity<ActScanBinding, BaseViewModel>() , QRCodeView.Delegate{


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
        Log.d("mZXingView","name=="+name)
                LiveDataBus.get().with("onScanCallBack", String::class.java)
            .observeForever {
                it?.let {param->
                    var signUpUser = JSON.parseObject(param, SignUpUser::class.java)

                    signUpUser.signUpLocationId = id
                    signUpUser.meetingName = name
                    signUpUser.signUpId = signUpId
                    signUpUser.userMeetingId = signUpUser.id
                    signUpUser.meetingId = signUpUser.meetingId
                    signUpUser.userMeetingTypeName = signUpUser.supplement
                    signUpUser.autoStatus =  autoStatus
                    signUpUser.timeLong =  timeLong
                    signUpUser.okMsg = okMsg
                    signUpUser.failedMsg = failedMsg
                    signUpUser.repeatMsg = repeatMsg
                    signUpUser.voiceStatus = voiceStatus
                    if(autoStatus.equals("1")){
                        if(showType==3){
                            com.dylanc.longan.startActivity<SiginReActivity>(
                                "type" to showType,
                                "data" to signUpUser
                            )
                        }else{
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
                                    "data" to signUpUser
                                )
                            },{},{})}
                    }else{
                        com.dylanc.longan.startActivity<SiginReActivity>(
                            "type" to showType,
                            "data" to signUpUser
                        )
                    }
                }
            }
    }
    private fun showPermission() {
        MaterialDialog(this).show {
            title(text ="提示")
            message(text ="无相机权限，无法使用扫一扫功能")
            positiveButton(text = "确定"){
                finish()
            }

        }

    }
    override fun onScanQRCodeSuccess(result: String?) {

        result?.let {param->
            var signUpUser = JSON.parseObject(param, SignUpUser::class.java)

            signUpUser.signUpLocationId = id
            signUpUser.meetingName = name
            signUpUser.signUpId = signUpId
            signUpUser.userMeetingId = signUpUser.id
            signUpUser.meetingId = signUpUser.meetingId
            signUpUser.userMeetingTypeName = signUpUser.supplement
            signUpUser.autoStatus =  autoStatus
            signUpUser.timeLong =  timeLong
            signUpUser.okMsg = okMsg
            signUpUser.failedMsg = failedMsg
            signUpUser.repeatMsg = repeatMsg
            signUpUser.voiceStatus = voiceStatus
            if(autoStatus.equals("1")){
                if(showType==3){
                    com.dylanc.longan.startActivity<SiginReActivity>(
                        "type" to showType,
                        "data" to signUpUser
                    )
                }else{
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
                            "data" to signUpUser
                        )
                    },{},{})}
            }else{
                com.dylanc.longan.startActivity<SiginReActivity>(
                    "type" to showType,
                    "data" to signUpUser
                )
            }
        }

    }

    override fun onCameraAmbientBrightnessChanged(isDark: Boolean) {

    }
var scanQRCodeOpenCameraError = false
    override fun onScanQRCodeOpenCameraError() {
//        binding.mZXingView.startCamera()
//        binding.mZXingView.startSpotAndShowRect()
        scanQRCodeOpenCameraError = true
    }
    override fun onStart() {
        super.onStart()
        if(!scanQRCodeOpenCameraError){ binding.mZXingView.startCamera() }
        // 打开后置摄像头开始预览，但是并未开始识别}



    }

    override fun onResume() {
        super.onResume()
        if(!scanQRCodeOpenCameraError){  binding.mZXingView.startSpotAndShowRect() }
        // 显示扫描框，并开始识别}
    }

    override fun onStop() {
        if(!scanQRCodeOpenCameraError) {
            binding.mZXingView.stopSpotAndHiddenRect()
        }
        super.onStop()
    }

    override fun onDestroy() {
        if(!scanQRCodeOpenCameraError){
        binding.mZXingView.onDestroy() }
        // 销毁二维码扫描控件
        super.onDestroy()
    }
}