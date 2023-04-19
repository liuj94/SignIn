package com.example.signin

import android.view.KeyEvent
import cn.bingoogolapple.qrcode.core.QRCodeView
import com.afollestad.materialdialogs.MaterialDialog
import com.alibaba.fastjson.JSON
import com.common.apiutil.decode.DecodeReader
import com.dylanc.longan.startActivity
import com.example.signin.base.BaseBindingActivity
import com.example.signin.base.BaseViewModel
import com.example.signin.bean.SignUpUser
import com.example.signin.databinding.ActScanBinding
import com.example.signin.scan.KeyEventResolver
import com.hello.scan.ScanCallBack
import com.hello.scan.ScanTool
import sigin
import java.io.UnsupportedEncodingException
import java.nio.charset.StandardCharsets

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
    private var mDecodeReader: DecodeReader? = null
    private var mKeyEventResolver: KeyEventResolver? = null
    override fun initData() {
        ScanTool.GET.initSerial(this, "/dev/ttyACM0", 115200, this@ScanActivity)
        openHardreader()
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
    private fun openHardreader() {
        try {
        if (mDecodeReader == null) {
            mDecodeReader = DecodeReader(this) //初始化
        }
//        mKeyEventResolver = KeyEventResolver(this)
        mDecodeReader?.setDecodeReaderListener { data ->
            mDecodeReader?.close()
            try {
                val str = String(data, StandardCharsets.UTF_8)
                runOnUiThread {
                   goRe(str)
                }
            } catch (e: UnsupportedEncodingException) {
                e.printStackTrace()
            }
        }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        // val ret = mDecodeReader!!.open(115200)
    }

    /**
     * 截获按键事件.发给ScanGunKeyEventHelper
     */
    override fun dispatchKeyEvent(event: KeyEvent): Boolean {
        //要是重虚拟键盘输入怎不拦截
        if ("Virtual" == event.device.name) {
            return super.dispatchKeyEvent(event)
        }
        mDecodeReader?.open(115200)
//        mKeyEventResolver?.analysisKeyEvent(event)
        return true
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
                    },{  signUpUser.success = "500"
                        com.dylanc.longan.startActivity<SiginReAutoActivity>(
                            "type" to showType,
                            "data" to signUpUser
                        )},{})}
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
        mDecodeReader?.close()
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
        }
    }

    override fun onDestroy() {
        MainHomeActivity.isScan = false
        if(!scanQRCodeOpenCameraError){
        binding.mZXingView.onDestroy() }
        // 销毁二维码扫描控件
        super.onDestroy()
        ScanTool.GET.release()
        mDecodeReader?.close()
//        mKeyEventResolver?.onDestroy()
    }

    override fun onScanSuccess(barcode: String?) {
        try {
            goRe(barcode)
        } catch (e: Exception) {
        }
    }
}