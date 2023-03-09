package com.example.signin.face

import android.graphics.Bitmap
import android.hardware.Camera
import android.media.FaceDetector
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.Toast
import com.alibaba.fastjson.JSON
import com.bifan.detectlib.FaceDetectTextureView
import com.dylanc.longan.mainThread
import com.dylanc.longan.toast
import com.example.signin.LiveDataBus
import com.example.signin.R
import com.example.signin.base.BaseBindingActivity
import com.example.signin.base.BaseViewModel
import com.example.signin.bean.SignUpUser
import com.example.signin.databinding.ActivityFaceBinding
import detect
import search
import sigin
import upFile
import java.io.*
import java.util.*
import java.util.concurrent.Executors


class FaceActivity : BaseBindingActivity<ActivityFaceBinding, BaseViewModel>() {
    override fun getViewModel(): Class<BaseViewModel> = BaseViewModel::class.java
    var preFrameList: MutableList<Bitmap> = ArrayList<Bitmap>()
    var failedMsg: String = "签到失败"
    var okMsg: String = "签到成功"
    var repeatMsg: String = "重复签到"
    var voiceStatus: String = "2"
    var id = ""
    var signUpId = ""
    var meetingid = ""
    var signUpLocationId = ""
    var autoStatus = ""
    var timeLong = 3
    override fun initData() {
        intent.getStringExtra("id")?.let { id = it }
        intent.getStringExtra("meetingid")?.let { meetingid = it }
        intent.getStringExtra("signUpId")?.let { signUpId = it }
        intent.getStringExtra("signUpLocationId")?.let { signUpLocationId = it }
        intent.getStringExtra("autoStatus")?.let { autoStatus = it }
        intent.getStringExtra("okMsg")?.let { okMsg = it }
        intent.getStringExtra("failedMsg")?.let { failedMsg = it }
        intent.getStringExtra("repeatMsg")?.let { repeatMsg = it }
        intent.getStringExtra("voiceStatus")?.let { voiceStatus = it }
        intent.getIntExtra("timeLong", 3)?.let { timeLong = it }
        binding.test.setOnClickListener { startDetect() }

        binding.faceDetectView.framePreViewListener =
            object : FaceDetectTextureView.IFramePreViewListener {
                override fun onFrame(preFrame: Bitmap?): Boolean {
                    //每一帧回调
                    //这个这帧preFrame处理了就是进行了回收，返回true
                    //否则返回false，内部进行回收处理
                    return false
                }

                override fun onFaceFrame(
                    preFrame: Bitmap?,
                    faces: Array<FaceDetector.Face?>
                ): Boolean {
                    //faces是检测出来的人脸参数
                    //检测到人脸的回调,保存人脸图片到本地
                    Log.i("FaceActivity", "当前图片人脸个数：" + faces.size)
                    if (!isSavingPic) {
                        if (faces.size > 0) {
                            preFrame?.let {
                                preFrameList.add(it)
                            }
                            if (preFrameList.size > 5) {

                                isSavingPic = true
                                preFrame?.let {
//                        executorService.submit(SavePicRunnable(it))
                                    executorService.submit {
                                        saveFacePicToLocal(preFrame)
                                        preFrameList.clear()
                                    }
                                }



                            }

                        }
                    }

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
        LiveDataBus.get().with("onScanCallBack", String::class.java)
            .observeForever {
                var signUpUser = JSON.parseObject(it, SignUpUser::class.java)
                sigin(signUpUser.id)
            }
    }

    private fun showToast(state: String) {
        var toast = Toast(this)
        var view: View = LayoutInflater.from(this).inflate(R.layout.tost_sb, null)
        when (state) {
            "1" -> {
                view = LayoutInflater.from(this).inflate(R.layout.tost_cg, null)
            }
            "2" -> {
                view = LayoutInflater.from(this).inflate(R.layout.tost_cf, null)
            }
        }
        toast.setView(view)
        toast.setDuration(Toast.LENGTH_LONG)
        toast.setGravity(0, 0, 0);
        toast.show();
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
            binding.faceDetectView.detectConfig.MinDetectTime = 1500
            binding.faceDetectView.detectConfig.Simple = 0.2f //图片检测时的压缩取样率，0~1，越小检测越流畅
            binding.faceDetectView.detectConfig.MaxDetectTime = 3000 //进入智能休眠检测，以2秒一次的这个速度检测
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
        val picPath = this.externalCacheDir.toString() + File.separator + "face.jpg"
        var fileOutputStream: FileOutputStream? = null
        val facePicFile = File(picPath)
        try {
            facePicFile.createNewFile()
        } catch (e: IOException) {
            isSavingPic = false
            Log.e("FaceActivity", "保存失败$e,$picPath")
            e.printStackTrace()
        }
        try {
            fileOutputStream = FileOutputStream(facePicFile)
        } catch (e: FileNotFoundException) {
            isSavingPic = false
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
                isSavingPic = false
            }
        }else{
            isSavingPic = false
        }
        bitmap.recycle()
        Log.e("FaceActivity", "保存完成,$picPath")
        try {
            var faceFile : File= File(picPath)
            var flag: Boolean = faceFile.exists()
            if (flag){
                upImgFile(picPath)
            }
        } catch (e: Exception) {
            isSavingPic = false
        }


    }

    fun upImgFile(picPath: String) {
        mViewModel.isShowLoading.value = true
        upFile(File(picPath), {
            var urlString = "https://img-blog.csdnimg.cn/20190618124505345.png"
//            var urlString = it.url
            detect(urlString,{
                search(urlString,{
                    it.result?.let {result->
                        if(!result.user_list.isNullOrEmpty()){
                            for (item in result.user_list){
                                sigin(item.user_id)
                            }
                        }


                    }

                },{isSavingPic = false})
            },{isSavingPic = false})
        }, {
            toast("图片上传失败")
            isSavingPic = false
            mViewModel.isShowLoading.value = false
        }, {

            File(picPath).delete()
        })

    }

    private fun sigin(userMeetingId:String,) {

        var params = HashMap<String, String>()
        params["meetingId"] = meetingid//会议id
        params["signUpLocationId"] = signUpLocationId//签到点id
        params["signUpId"] =signUpId//签到站id
        params["userMeetingId"] = userMeetingId//用户参与会议id
        params["status"] = "2"//用户参与会议id
        sigin(JSON.toJSONString(params), { success ->
            if (voiceStatus.equals("1")) {
                LiveDataBus.get().with("voiceStatus").postValue(success)
            }
            showToast(success)
        }, {
            showToast("0")
        }, {
            isSavingPic = false
        })
    }

    override fun onDestroy() {
        super.onDestroy()
        endDetect()
    }
}