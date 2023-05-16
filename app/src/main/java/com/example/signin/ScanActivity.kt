package com.example.signin

import android.media.MediaPlayer
import cn.bingoogolapple.qrcode.core.QRCodeView
import com.alibaba.fastjson.JSON
import com.example.signin.base.BaseBindingActivity
import com.example.signin.base.BaseViewModel
import com.example.signin.bean.MeetingFormData
import com.example.signin.bean.MeetingUserDeData
import com.example.signin.bean.SignUpUser
import com.example.signin.databinding.ActScanBinding
import com.example.signin.net.RequestCallback
import com.lzy.okgo.OkGo
import com.lzy.okgo.model.Response
import sigin

class ScanActivity : BaseBindingActivity<ActScanBinding, BaseViewModel>(), QRCodeView.Delegate {


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
    var failedMsg: String = "签到失败"
    var okMsg: String = "签到成功"
    var repeatMsg: String = "重复签到"
    var voiceStatus: String = "2"


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
        intent.getIntExtra("timeLong", 3)?.let { timeLong = it }
        intent.getIntExtra("showType", 0)?.let { showType = it }
        binding.mZXingView.setDelegate(this)

    }


    override fun onScanQRCodeSuccess(result: String?) {
        var mRingPlayer =
            MediaPlayer.create(this@ScanActivity, R.raw.ddd)
        mRingPlayer?.start()
        if (result.isNullOrEmpty()) {
            var signUpUser = SignUpUser()
            signUpUser.signUpLocationId = id
            signUpUser.meetingName = name
            signUpUser.signUpId = signUpId
            signUpUser.userMeetingId = ""
            signUpUser.meetingId = ""
            signUpUser.userMeetingTypeName = ""
            signUpUser.autoStatus = autoStatus
            signUpUser.timeLong = timeLong
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
        result?.let { param ->
            var signUpUser = JSON.parseObject(param, SignUpUser::class.java)
            if (signUpUser.id.isNullOrEmpty()) {
                signUpUser.signUpLocationId = id
                signUpUser.meetingName = name
                signUpUser.signUpId = signUpId
                signUpUser.userMeetingId = ""
                signUpUser.meetingId = ""
                signUpUser.userMeetingTypeName = ""
                signUpUser.autoStatus = autoStatus
                signUpUser.timeLong = timeLong
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

    private fun getData(userid: String) {
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
                    var d = kv.getString("MeetingFormData", "")
                    if (!d.isNullOrEmpty()) {
                        try {
                            var meetingFormData = JSON.parseObject(d, MeetingFormData::class.java)
                            meetingFormData?.let {
                                for (list in it.meetingFormList) {
                                    for (listFrom in data.userMeetingForms) {
                                        if (list.name.equals(listFrom.name)) {
                                            if (listFrom.selectCheckboxParam != null) {
                                                list.value = listFrom.selectCheckboxParam.boxValue
                                            } else {
                                                list.value = listFrom.value
                                            }

                                        }
                                    }
                                }
                                kv.putString("MeetingFormData", JSON.toJSONString(meetingFormData))
                            }
                        } catch (e: Exception) {
                        }

                    }

                    var signUpUser = SignUpUser()
                    signUpUser.signUpLocationId = id
                    signUpUser.meetingName = name
                    signUpUser.signUpId = signUpId
                    signUpUser.userMeetingId = userid
                    signUpUser.meetingId = "" + data.meetingId
                    signUpUser.userMeetingTypeName = "" + data.userMeetingTypeName
                    signUpUser.autoStatus = autoStatus
                    signUpUser.timeLong = timeLong
                    signUpUser.okMsg = okMsg
                    signUpUser.failedMsg = failedMsg
                    signUpUser.repeatMsg = repeatMsg
                    signUpUser.voiceStatus = voiceStatus
                    if (showType == 3) {
                        com.dylanc.longan.startActivity<SiginReActivity>(
                            "type" to showType,
                            "data" to signUpUser
                        )
                    } else {
                        var avatar = ""
                        data.avatar?.let { avatar = it }
                        var params = java.util.HashMap<String, String>()
                        params["meetingId"] = signUpUser.meetingId//会议id
                        params["signUpLocationId"] = signUpUser.signUpLocationId//签到点id
                        params["signUpId"] = signUpUser.signUpId//签到站id
                        params["userMeetingId"] = signUpUser.userMeetingId//用户参与会议id
                        params["status"] = "2"//用户参与会议id
                        sigin(JSON.toJSONString(params), { success ->
                            signUpUser.success = success
                            com.dylanc.longan.startActivity<SiginReAutoActivity>(
                                "type" to showType,
                                "data" to signUpUser,
                                "avatar" to avatar
                            )
                        }, {
                            signUpUser.success = "500"
                            com.dylanc.longan.startActivity<SiginReAutoActivity>(
                                "type" to showType,
                                "data" to signUpUser,
                                "avatar" to avatar
                            )
                        }, {})
                    }


                }

                override fun onError(response: Response<MeetingUserDeData>) {
                    super.onError(response)
                    var signUpUser = SignUpUser()
                    signUpUser.signUpLocationId = id
                    signUpUser.meetingName = name
                    signUpUser.signUpId = signUpId
                    signUpUser.userMeetingId = ""
                    signUpUser.meetingId = ""
                    signUpUser.userMeetingTypeName = ""
                    signUpUser.autoStatus = autoStatus
                    signUpUser.timeLong = timeLong
                    signUpUser.okMsg = okMsg
                    signUpUser.failedMsg = failedMsg
                    signUpUser.repeatMsg = repeatMsg
                    signUpUser.voiceStatus = voiceStatus
                    signUpUser.success = "500"
                    com.dylanc.longan.startActivity<SiginReAutoActivity>(
                        "type" to showType,
                        "data" to signUpUser
                    )
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
        if (!scanQRCodeOpenCameraError) {
            binding.mZXingView.startCamera()
        }
        // 打开后置摄像头开始预览，但是并未开始识别}


    }

    override fun onResume() {
        super.onResume()
        isPause = false
        if (!scanQRCodeOpenCameraError) {
            binding.mZXingView.startSpotAndShowRect()
        }
        // 显示扫描框，并开始识别}
    }

    override fun onPause() {
        super.onPause()
        isPause = true
    }

    override fun onStop() {
        if (!scanQRCodeOpenCameraError) {
            binding.mZXingView.stopSpotAndHiddenRect()
        }
        super.onStop()
    }

    var isPause = true
//    override fun onScanCallBack(data: String?) {
//        try {
//        goRe(data)
//        } catch (e: Exception) {
//        }
//    }

    override fun onDestroy() {
        if (!scanQRCodeOpenCameraError) {
            binding.mZXingView.onDestroy()
        }
        // 销毁二维码扫描控件
        super.onDestroy()
    }

//    override fun onScanSuccess(barcode: String?) {
//        try {
//            goRe(barcode)
//        } catch (e: Exception) {
//        }
//    }
}