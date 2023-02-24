package com.example.signin

import android.Manifest
import android.content.pm.PackageManager
import cn.bingoogolapple.qrcode.core.QRCodeView
import com.afollestad.materialdialogs.MaterialDialog
import com.example.signin.base.BaseBindingActivity
import com.example.signin.base.BaseViewModel
import com.example.signin.databinding.ActScanBinding

class ScanActivity : BaseBindingActivity<ActScanBinding, BaseViewModel>() , QRCodeView.Delegate{


    override fun getViewModel(): Class<BaseViewModel> = BaseViewModel::class.java


    override fun initData() {

        binding.mZXingView.setDelegate(this)
        OnResultManager.requestPermissions(this, arrayOf(Manifest.permission.CAMERA), 200) { requestCode: Int, _: Array<String>, grantResults: IntArray ->
            if (requestCode == 200 && grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
//                initScan()
//                Log.e("QRCode", "startSpotAndShowRect=")
                binding.mZXingView.startSpotAndShowRect() // 显示扫描框，并开始识别
            } else {
                showPermission()
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
//        if (isCallBack==1) {
//            val intent = Intent()
//            intent.putExtra("QrCodeScanned", result)
//            setResult(111, intent)
//        } else {
//            AppManager.getAppManager().startScanResultWeb(PageRoutes.SCAN, result)
//        }
        finish()
    }

    override fun onCameraAmbientBrightnessChanged(isDark: Boolean) {

    }

    override fun onScanQRCodeOpenCameraError() {
        binding.mZXingView.startCamera()
        binding.mZXingView.startSpotAndShowRect()
    }
    override fun onStart() {
        super.onStart()
        binding.mZXingView.startCamera() // 打开后置摄像头开始预览，但是并未开始识别

    }

    override fun onStop() {
        binding.mZXingView.stopCamera() // 关闭摄像头预览，并且隐藏扫描框
        super.onStop()
    }

    override fun onDestroy() {
        binding.mZXingView.onDestroy() // 销毁二维码扫描控件
        super.onDestroy()
    }
}