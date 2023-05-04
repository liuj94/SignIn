package com.example.signin

import android.content.Intent
import cn.bingoogolapple.qrcode.core.QRCodeView
import com.dylanc.longan.toast

import com.example.signin.base.BaseBindingActivity
import com.example.signin.base.BaseViewModel

import com.example.signin.databinding.ActScanBinding



class ScanKPActivity : BaseBindingActivity<ActScanBinding, BaseViewModel>() , QRCodeView.Delegate {


    override fun getViewModel(): Class<BaseViewModel> = BaseViewModel::class.java



    override fun initData() {

        binding.mZXingView.setDelegate(this)

    }



    override fun onScanQRCodeSuccess(result: String?) {

        result?.let {param->
//        044031801104（发票代码）,
//        40021227（发票号码）,
            try {
                var temp = param.split(",")
                var dm = temp[2]
                var hm = temp[3]
                val intent = Intent()
                intent.putExtra("dm", dm)
                intent.putExtra("hm", hm)
                setResult(1111, intent)
            }catch (e:java.lang.Exception){
                toast("请提供正确发票码")
            }

        }

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