package com.example.signin.face

import android.graphics.Bitmap
import android.hardware.Camera
import android.media.FaceDetector
import android.util.Log
import com.bifan.detectlib.FaceDetectTextureView
import com.dylanc.longan.mainThread
import com.example.signin.base.BaseBindingActivity
import com.example.signin.base.BaseViewModel
import com.example.signin.databinding.ActivityFaceBinding
import java.io.*
import java.util.*
import java.util.concurrent.Executors

class FaceActivity : BaseBindingActivity<ActivityFaceBinding, BaseViewModel>() {
    override fun getViewModel(): Class<BaseViewModel> = BaseViewModel::class.java

    override fun initData() {

       binding.test.setOnClickListener { startDetect() }

        binding.faceDetectView.framePreViewListener = object : FaceDetectTextureView.IFramePreViewListener {
            override fun onFrame(preFrame: Bitmap?): Boolean {
                //每一帧回调
                //这个这帧preFrame处理了就是进行了回收，返回true
                //否则返回false，内部进行回收处理
                return false
            }

            override fun onFaceFrame(preFrame: Bitmap?, faces: Array<FaceDetector.Face?>): Boolean {
                //faces是检测出来的人脸参数
                //检测到人脸的回调,保存人脸图片到本地
                if (isSavingPic === false) {
                    isSavingPic = true
                    preFrame?.let {
//                        executorService.submit(SavePicRunnable(it))
                        executorService.submit {  saveFacePicToLocal(preFrame)
                            isSavingPic = false }
                    }

                }
                Log.i("FaceActivity", "当前图片人脸个数：" + faces.size)
                //这个这帧preFrame处理了就是进行了回收，返回true
                //否则返回false，内部进行回收处理
                return true
            }
        }
        mViewModel.isShowLoading.value = true
        val task: TimerTask = object : TimerTask() {
            override fun run() {
                mainThread {
                    binding.test.performClick()
                    mViewModel.isShowLoading.value = false
                }

            }
        }
        val timer = Timer()
        timer.schedule(task, 1500)

    }
    private var isSavingPic = false
    private val executorService = Executors.newSingleThreadExecutor()

    fun startDetect() {
        if (!binding.faceDetectView.isHasInit) {
            //必须是在view可见后进行初始化
            binding.faceDetectView.initView()
            binding.faceDetectView.initCamera()
            binding.faceDetectView.detectConfig.CameraType = Camera.CameraInfo.CAMERA_FACING_FRONT
            binding.faceDetectView.detectConfig.EnableFaceDetect = true
            binding.faceDetectView.detectConfig.MinDetectTime = 100
            binding.faceDetectView.detectConfig.Simple = 0.2f //图片检测时的压缩取样率，0~1，越小检测越流畅
            binding.faceDetectView.detectConfig.MaxDetectTime = 2000 //进入智能休眠检测，以2秒一次的这个速度检测
            binding.faceDetectView.detectConfig.EnableIdleSleepOption = true //启用智能休眠检测机制
            binding.faceDetectView.detectConfig.IdleSleepOptionJudgeTime =
                1000 * 10 //1分钟内没有检测到人脸，进入智能休眠检测
        }
        binding.faceDetectView.startCameraPreview()
    }
    fun endDetect() {
        binding.faceDetectView.stopCameraPreview()
        binding.faceDetectView.faceRectView.clearBorder()
    }
    private fun saveFacePicToLocal(bitmap: Bitmap) {
        val picPath =this.externalCacheDir.toString() + File.separator +  "face.jpg"
//            File((this.externalCacheDir + File.separator +  "face.jpg"))
//        val picPath = Environment.getExternalStorageDirectory().toString() + "/face.jpg"
        var fileOutputStream: FileOutputStream? = null
        val facePicFile = File(picPath)
        try {
            facePicFile.createNewFile()
        } catch (e: IOException) {
            Log.e("FaceActivity", "保存失败$e,$picPath")
            e.printStackTrace()
        }
        try {
            fileOutputStream = FileOutputStream(facePicFile)
        } catch (e: FileNotFoundException) {
            e.printStackTrace()
        }
        if (fileOutputStream != null) {
            val bos = BufferedOutputStream(fileOutputStream)
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bos)
            try {
                bos.flush()
                bos.close()
            } catch (e: IOException) {
                e.printStackTrace()
                Log.e("FaceActivity", e.toString())
            }
        }
        bitmap.recycle()
        Log.e("FaceActivity", "保存完成,$picPath")
    }
    override fun onDestroy() {
        super.onDestroy()
        endDetect()
    }
}