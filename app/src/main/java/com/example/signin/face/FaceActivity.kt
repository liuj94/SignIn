package com.example.signin.face

import android.graphics.Bitmap
import android.hardware.Camera
import android.media.FaceDetector
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.TextView
import android.widget.Toast
import cn.bingoogolapple.qrcode.core.QRCodeView
import com.alibaba.fastjson.JSON
import com.bifan.detectlib.FaceDetectTextureView
import com.dylanc.longan.mainThread
import com.dylanc.longan.toast
import com.example.signin.R
import com.example.signin.SiginReActivity
import com.example.signin.SiginReAutoActivity
import com.example.signin.base.BaseBindingActivity
import com.example.signin.base.BaseViewModel
import com.example.signin.bean.SignUpUser
import com.example.signin.databinding.ActivityFaceBinding
import search

import sigin2
import upFile
import java.io.*
import java.util.*
import java.util.concurrent.Executors


class FaceActivity : BaseBindingActivity<ActivityFaceBinding, BaseViewModel>(), QRCodeView.Delegate {
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
    var moshi = 1
    var showType = 0
    override fun initData() {
        binding.mZXingView.setDelegate(this)
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
        intent.getIntExtra("showType", 3)?.let { showType = it }
        binding.test.setOnClickListener { startDetect() }
        binding.moshi.setOnClickListener {
            if(moshi==1){
                moshi = 2
                binding.faceDetectView.stopCameraPreview()
                binding.faceDetectView.visibility = View.GONE
                binding.moshi.setText("切换人脸模式")
                binding.mZXingView.startCamera() // 关闭摄像头预览，并且隐藏扫描框
                binding.mZXingView.visibility = View.VISIBLE
                binding.mZXingView.startSpot()

            }else{
                moshi = 1
                binding.moshi.setText("切换二维码模式")
                binding.mZXingView.stopCamera() // 关闭摄像头预览，并且隐藏扫描框
                binding.mZXingView.visibility = View.GONE
                binding.mZXingView.stopSpot()
                binding.faceDetectView.startCameraPreview()
                binding.faceDetectView.visibility = View.VISIBLE
            }
        }
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
                    Log.i("FaceActivity", "isSavingPic==：" + isSavingPic)
                    if (!isSavingPic) {

                        if (faces.size > 0) {
                            preFrame?.let {
                                preFrameList.add(it)
                            }
                            if (preFrameList.size > 5) {
                                Log.i("FaceActivity", "preFrameList.size ==：" + preFrameList.size )
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
//        LiveDataBus.get().with("onScanCallBack", String::class.java)
//            .observeForever {
//                var signUpUser = JSON.parseObject(it, SignUpUser::class.java)
//                sigin(signUpUser.id)
//            }
    }

    private fun showToast() {
        var toast = Toast(this)
        var view: View = LayoutInflater.from(this).inflate(R.layout.tost_sb, null)
//        when (state) {
//            "1" -> {
//                view = LayoutInflater.from(this).inflate(R.layout.tost_cg, null)
//            }
//            "2" -> {
//                view = LayoutInflater.from(this).inflate(R.layout.tost_cf, null)
//            }
//        }
        toast.setView(view)
        toast.setDuration(Toast.LENGTH_LONG)
        toast.setGravity(0, 0, 0);
        toast.show();
    }
    private fun showToast2(string: String) {
        var toast = Toast(this)
        var view: View = LayoutInflater.from(this).inflate(R.layout.tost_sb, null)
        var str = view.findViewById<TextView>(R.id.str)
        str.setText(string)
//        when (state) {
//            "1" -> {
//                view = LayoutInflater.from(this).inflate(R.layout.tost_cg, null)
//            }
//            "2" -> {
//                view = LayoutInflater.from(this).inflate(R.layout.tost_cf, null)
//            }
//        }

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
            binding.faceDetectView.detectConfig.MinDetectTime = 1000
            binding.faceDetectView.detectConfig.Simple = 0.2f //图片检测时的压缩取样率，0~1，越小检测越流畅
            binding.faceDetectView.detectConfig.MaxDetectTime = 2000 //进入智能休眠检测，以2秒一次的这个速度检测
            binding.faceDetectView.detectConfig.EnableIdleSleepOption = true //启用智能休眠检测机制
            binding.faceDetectView.detectConfig.IdleSleepOptionJudgeTime =
                1000 * 60 //1分钟内没有检测到人脸，进入智能休眠检测
        }
        binding.faceDetectView.startCameraPreview()
    }

    fun endDetect() {
        binding.faceDetectView.stopCameraPreview()
        binding.faceDetectView.faceRectView.clearBorder()
    }

    private fun saveFacePicToLocal(bitmap: Bitmap) {
        Log.e("FaceActivity", "saveFacePicToLocal=isSavingPic="+isSavingPic)
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
            mainThread {
                var faceFile : File= File(picPath)
                var flag: Boolean = faceFile.exists()
                Log.e("FaceActivity", "flag,$flag")
                if (flag){

                    upImgFile(picPath)
                }
            }

        } catch (e: Exception) {
            isSavingPic = false
            Log.e("FaceActivity", "Exception,"+e.toString())
        }


    }

    fun upImgFile(picPath: String) {
        mViewModel.isShowLoading.value = true
        upFile(File(picPath), {

//            var urlString = "https://img-blog.csdnimg.cn/20190618124505345.png?x-oss-process=image/watermark,type_ZmFuZ3poZW5naGVpdGk,shadow_10,text_aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L2EzMTI4NjMwNjM=,size_16,color_FFFFFF,t_70"

            var urlString = it.url
//            detect(urlString,{
//                var urlString = "https://meeting.nbqichen.com:20882/profile/upload/2023/03/09/ac479084-cff7-40a1-a7fe-480e6c8f4523.jpg"
                search(urlString,{

                    it.result?.let {result->

                        if(!result.user_list.isNullOrEmpty()){
                            for (item in result.user_list){
                                sigin(item.user_id)
//                                sigin("129")
                            }
                        }


                    }

                },{
                    showToast()
                    isSavingPic = false
                    },{
                    mViewModel.isShowLoading.value = false
                })
//            },{isSavingPic = false})
        }, {
            toast("图片上传失败")
            isSavingPic = false
            mViewModel.isShowLoading.value = false
        }, {
            isSavingPic = false
            try {
                mainThread {
                    File(picPath).delete()
                }

            } catch (e: Exception) {


            }

        })

    }

    private fun sigin(userMeetingId:String,userMeetingTypeName :String?="") {
                var signUpUser = SignUpUser()
                signUpUser.signUpLocationId = signUpLocationId
                signUpUser.signUpId = signUpId
                signUpUser.userMeetingId = userMeetingId
                signUpUser.meetingId = meetingid
                signUpUser.userMeetingTypeName = userMeetingTypeName
                signUpUser.autoStatus =  autoStatus
                signUpUser.timeLong =  timeLong
                signUpUser.okMsg = okMsg
                signUpUser.failedMsg = failedMsg
                signUpUser.repeatMsg = repeatMsg
                signUpUser.voiceStatus = voiceStatus
                if(autoStatus.equals("2")){
                    if(showType==3){
                        com.dylanc.longan.startActivity<SiginReActivity>(
                            "type" to showType,
                            "data" to signUpUser
                        )
                        finish()
                    }else{
                        var params = HashMap<String, String>()
                        params["meetingId"] = signUpUser.meetingId//会议id
                        params["signUpLocationId"] = signUpUser.signUpLocationId//签到点id
                        params["signUpId"] = signUpUser.signUpId//签到站id
                        params["userMeetingId"] = signUpUser.userMeetingId//用户参与会议id
                        params["status"] = "2"//用户参与会议id
                        sigin2(JSON.toJSONString(params),{success->
                            signUpUser.success = success
                            finish()
                            com.dylanc.longan.startActivity<SiginReAutoActivity>(
                                "type" to showType,
                                "data" to signUpUser
                            )
                        },{
                          showToast2(it)
                            if(moshi==2){
                                binding.mZXingView.startSpot()
                            }

                        },{

                        })}
                }else{
                    finish()
                    com.dylanc.longan.startActivity<SiginReActivity>(
                        "type" to showType,
                        "data" to signUpUser
                    )
                }




//        var params = HashMap<String, String>()
//        params["meetingId"] = meetingid//会议id
//        params["signUpLocationId"] = signUpLocationId//签到点id
//        params["signUpId"] =signUpId//签到站id
//        params["userMeetingId"] = userMeetingId//用户参与会议id
//        params["status"] = "2"//用户参与会议id
//        sigin(JSON.toJSONString(params), { success ->
//            if (voiceStatus.equals("1")) {
//                LiveDataBus.get().with("voiceStatus").postValue(success)
//            }
//            showToast(success)
//        }, {
//            showToast("0")
//        }, {
//            isSavingPic = false
//        })
    }

    override fun onDestroy() {
        super.onDestroy()
        endDetect()
        binding.mZXingView.onDestroy()
    }
    override fun onScanQRCodeSuccess(result: String?) {
//        result
        result?.let {

            try {
                binding.mZXingView.stopSpot()
                var signUpUser = JSON.parseObject(it, SignUpUser::class.java)
//            sigin(signUpUser.id,signUpUser.userMeetingTypeName)
                sigin("129",signUpUser.userMeetingTypeName)
            }catch (e :java.lang.Exception){
                toast("二维码信息错误")
            }
        }

    }

    override fun onCameraAmbientBrightnessChanged(isDark: Boolean) {

    }

    override fun onScanQRCodeOpenCameraError() {
        binding.mZXingView.startCamera()
        binding.mZXingView.startSpotAndShowRect()
    }
    override fun onStart() {
        super.onStart()
//        binding.mZXingView.startCamera() // 打开后置摄像头开始预览，但是并未开始识别

    }

    override fun onStop() {
        binding.mZXingView.stopCamera() // 关闭摄像头预览，并且隐藏扫描框
        super.onStop()
    }


}