package com.example.signin

import android.graphics.Bitmap
import android.graphics.Color
import android.graphics.Rect
import android.media.MediaPlayer
import android.os.CountDownTimer
import android.util.Log
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.alibaba.fastjson.JSON
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.load.resource.bitmap.CircleCrop
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.RequestOptions
import com.bumptech.glide.request.target.Target
import com.ctaiot.ctprinter.ctpl.CTPL
import com.ctaiot.ctprinter.ctpl.param.PaperType
import com.dylanc.longan.activity
import com.dylanc.longan.toast
import com.example.signin.PageRoutes.Companion.BaseUrl
import com.example.signin.adapter.ReAdapter
import com.example.signin.base.BaseBindingActivity
import com.example.signin.base.BaseViewModel
import com.example.signin.base.StatusBarUtil
import com.example.signin.bean.MeetingFormData
import com.example.signin.bean.MeetingFormList
import com.example.signin.bean.SignUpUser
import com.example.signin.bean.SocketData
import com.example.signin.databinding.ActSigninStateBinding
import getprintImg


class SiginReAutoActivity : BaseBindingActivity<ActSigninStateBinding, BaseViewModel>() {
    override fun initTranslucentStatus() {
        StatusBarUtil.setTranslucentStatus(this, Color.TRANSPARENT)
        //设置状态栏字体颜色
        StatusBarUtil.setAndroidNativeLightStatusBar(this, true)
    }

    override fun getViewModel(): Class<BaseViewModel> = BaseViewModel::class.java
    var type: Int = 0
    var timeLong: Int = 3
    var signUpUser: SignUpUser? = null
    var params = HashMap<String, String>()
    var failedMsg: String = "签到失败"
    var okMsg: String = "签到成功"
    var repeatMsg: String = "重复签到"
    var success: String = ""
    var voiceStatus: String = "1"
    var autoStatus: String = "1"
    var avatar: String = ""
    var mRingPlayer: MediaPlayer? = null
    var isShowAvatar: Boolean = false
    var isShowFrist: Boolean = true
    var meetingFormList: MutableList<MeetingFormList> = ArrayList()
    override fun initData() {
        CTPL.getInstance().clean()
        var d = kv.getString("MeetingFormData", "")
        var isPrint = kv.getBoolean("printStatus", true)
        if (isPrint) {
            binding.print.visibility = View.VISIBLE
        } else {
            binding.print.visibility = View.GONE
        }
        var printZd = kv.getBoolean("printZd", true)
        if (printZd) {
//                            App.getInstance().toast("自动打印开启")
            var message = kv.getString("printData", "")
            Log.d("JWebSocketClient", "message=" + message)
            if (!CTPL.getInstance().isConnected) {
//                                if (false) {
                App.getInstance().toast("打印机未连接")
                Log.d("JWebSocketClient", "打印机未连接=")
            } else {
                var message = kv.getString("printData", "")
                if (!message.isNullOrEmpty()) {
                    try {
                        var data =
                            JSON.parseObject(message, SocketData::class.java)
                        printImg(data)
                    } catch (e: Exception) {
                        App.getInstance().toast("数据解析异常")
                        Log.d(
                            "JWebSocketClient",
                            "ExceptionionPrint=" + e.message
                        )
                    }

                } else {
                    App.getInstance().toast("打印参数为空")
                }
            }
//                            binding.print.performClick()
        } else {
            App.getInstance().toast("自动打印未开启")
            Log.d("JWebSocketClient", "自动打印未开启=")
        }
//        LiveEventBus
//            .get<String>("PrintJWebSocketPrint", String::class.java)
//            .observe(this) {
//                if (!isShowFrist){
//                    return@observe
//                }
//                isShowFrist = false
//                    if (AppManager.getAppManager()
//                            .activityInstanceIsLive(this@SiginReAutoActivity)
//                    ) {
//                        Log.d("JWebSocketClient", "JWebSocketClientlocationPrint=")
//                        try {
//                            App.getInstance().toast("接收到打印通知")
//                            var printZd = kv.getBoolean("printZd", true)
//                            if (printZd) {
////                            App.getInstance().toast("自动打印开启")
//                                var message = kv.getString("printData", "")
//                                Log.d("JWebSocketClient", "message=" + message)
//                            if (!CTPL.getInstance().isConnected) {
////                                if (false) {
//                                    App.getInstance().toast("打印机未连接")
//                                    Log.d("JWebSocketClient", "打印机未连接=")
//                                } else {
//                                    var message = kv.getString("printData", "")
//                                    if (!message.isNullOrEmpty()) {
//                                        try {
//                                            var data =
//                                                JSON.parseObject(message, SocketData::class.java)
//                                            printImg(data)
//                                        } catch (e: Exception) {
//                                            App.getInstance().toast("数据解析异常")
//                                            Log.d(
//                                                "JWebSocketClient",
//                                                "ExceptionionPrint=" + e.message
//                                            )
//                                        }
//
//                                    } else {
//                                        App.getInstance().toast("打印参数为空")
//                                    }
//                                }
////                            binding.print.performClick()
//                            } else {
//                                App.getInstance().toast("自动打印未开启")
//                                Log.d("JWebSocketClient", "自动打印未开启=")
//                            }
//
//                        } catch (e: Exception) {
//                            App.getInstance().toast("线程异常")
//                            Log.d("JWebSocketClient", "线程异常=")
//
//                        }
//                    }
//
//
//            }
        binding.print.setOnClickListener {
            if (!CTPL.getInstance().isConnected) {
                toast("打印机未连接")
                Log.d("JWebSocketClient", "打印机未连接=")
            } else {
                var message = kv.getString("printData", "")
                if (!message.isNullOrEmpty()) {
                    try {
                        var data = JSON.parseObject(message, SocketData::class.java)
                        printImg(data)
                    } catch (e: Exception) {
                        App.getInstance().toast("数据解析异常")
                        Log.d("JWebSocketClient", "ExceptionionPrint=" + e.message)
                    }

                } else {
                    toast("打印参数为空")
                }
//                LiveDataBus.get().with("JWebSocketClientlocationPrint").postValue("JWebSocketClientlocationPrint")
            }
        }
        if (!d.isNullOrEmpty()) {
            try {
                var meetingFormData = JSON.parseObject(d, MeetingFormData::class.java)
                meetingFormData?.let {
                    for (list in it.meetingFormList) {
                        if (list.type.equals("avatar")) {
                            isShowAvatar = list.showStatus == 1
                        } else {
                            if (list.showStatus == 1) {
                                meetingFormList.add(list)
                                Log.d("meetingFormList", list.toString())
                            }
                        }
                    }
                }
            } catch (e: Exception) {
            }

        }
//setInfo()
        intent.getIntExtra("type", 0)?.let { type = it }
        intent.getStringExtra("avatar")?.let { avatar = it }
        intent.getSerializableExtra("data")?.let {
            signUpUser = it as SignUpUser
        }
        signUpUser?.let {

            params["meetingId"] = it.meetingId//会议id
            params["signUpLocationId"] = it.signUpLocationId//签到点id
            params["signUpId"] = it.signUpId//签到站id
            params["userMeetingId"] = it.userMeetingId//用户参与会议id
            params["status"] = "2"//用户参与会议id
            it.meetingName?.let { meetingName -> binding.name.text = meetingName }
            it.failedMsg?.let { it -> failedMsg = it }
            it.okMsg?.let { it -> okMsg = it }
            it.repeatMsg?.let { it -> repeatMsg = it }
//            it.name?.let { name-> binding.userName.text = encode(name)
//                binding.userName.visibility = View.VISIBLE}
//            it.corporateName?.let {companyName-> binding.companyName.text = encode(companyName)
//                binding.companyName.visibility = View.VISIBLE}
//            it.userMeetingTypeName?.let {userMeetingTypeName->binding.type.text = encode(userMeetingTypeName)
//                binding.type.visibility = View.VISIBLE}
            it.timeLong?.let { t -> timeLong = t }
            it.success?.let { t -> success = t }
            it.voiceStatus?.let { t -> voiceStatus = t }
            it.autoStatus?.let { t -> autoStatus = t }
            if (success.equals("1")) {
                getprintImg(it.userMeetingId)
            }


//            if(it.name.isNullOrEmpty()){
//                if(it.corporateName.isNullOrEmpty()){
//                    if(it.userMeetingTypeName.isNullOrEmpty()){
//                        binding.userName.text = ""
//                        binding.companyName.text = ""
//                        binding.type.text = ""
//                    }else{
//                        binding.userName.text = it.userMeetingTypeName
//                    }
//
//                }else{
//                    binding.userName.text = it.corporateName
//                    if(it.userMeetingTypeName.isNullOrEmpty()){
//                        binding.companyName.text = ""
//                        binding.type.text = ""
//                    }else{
//                        binding.companyName.text = it.userMeetingTypeName
//                    }
//                }
//
//            }else{
//                binding.userName.text = it.name
//                if(it.corporateName.isNullOrEmpty()){
//                    if(it.userMeetingTypeName.isNullOrEmpty()){
//                        binding.companyName.text = ""
//                        binding.type.text = ""
//                    }else{
//                        binding.companyName.text = it.userMeetingTypeName
//                    }
//
//                }else{
//                    binding.companyName.text = it.corporateName
//                    if(it.userMeetingTypeName.isNullOrEmpty()){
//                        binding.type.text = ""
//                    }else{
//                        binding.type.text = it.userMeetingTypeName
//
//                    }
//                }
//            }

        }
//        1 注册签到2 来程签到3 入住签到4 会场签到5 餐饮签到6 礼品签到7 返程签到
        binding.numEt.visibility = View.INVISIBLE
        when (type) {
            1 -> binding.title.text = "注册签到"
            2 -> binding.title.text = "来程签到"
            3 -> {
                binding.title.text = "入住签到"
//                binding.numEt.visibility = View.VISIBLE
                binding.stateTv.text = "入住办理"
                binding.submit.text = "确认入住"
            }

            4 -> {
                binding.title.text = "会场签到"
//                binding.numEt.visibility = View.VISIBLE
                binding.numEt.hint = "请输入座位号"
            }

            5 -> {
                binding.title.text = "餐饮签到"
//                binding.numEt.visibility = View.VISIBLE
                binding.numEt.hint = "请输入座位号"
            }

            6 -> binding.title.text = "礼品签到"
            7 -> binding.title.text = "返程签到"
            8 -> binding.title.text = "发票签到"
        }

        if (success.equals("1")) {
            setInfo()
            if (voiceStatus.equals("1")) {
//                LiveDataBus.get().with("voiceStatus").postValue(okMsg)
//                if(SpeechUtils.getInstance(this@SiginReAutoActivity).isSpeech){
//                    SpeechUtils.getInstance(this@SiginReAutoActivity).speakText(okMsg);
//                }else{
//
//                }
                if (okMsg.contains("签出")) {
                    mRingPlayer = MediaPlayer.create(this@SiginReAutoActivity, R.raw.dccg);
                } else {
                    mRingPlayer = MediaPlayer.create(this@SiginReAutoActivity, R.raw.cg);
                }

                mRingPlayer?.start();
            }
            binding.stateTv.text = okMsg
            binding.stateTv.setTextColor(Color.parseColor("#3974F6"))
            if (avatar.isNullOrEmpty()) {
                binding.stateIv.setImageResource(R.mipmap.qd2)
            } else {
                Glide.with(this@SiginReAutoActivity).load(PageRoutes.BaseUrl + avatar).apply(
                    RequestOptions.bitmapTransform(
                        CircleCrop()
                    )
                )
                    .error(R.mipmap.qd2).into(binding.stateIv)
            }
            if (!isShowAvatar) {
                binding.stateIv.setImageResource(R.mipmap.qd2)
            }
        } else if (success.equals("2")) {
            setInfo()
            if (voiceStatus.equals("1")) {
//                LiveDataBus.get().with("voiceStatus").postValue(repeatMsg)
//                if(SpeechUtils.getInstance(this@SiginReAutoActivity).isSpeech){
//                    SpeechUtils.getInstance(this@SiginReAutoActivity).speakText(repeatMsg);
//                }else{
//
//                }
                if (repeatMsg.contains("签出")) {
                    mRingPlayer = MediaPlayer.create(this@SiginReAutoActivity, R.raw.dccf);
                } else {
                    mRingPlayer = MediaPlayer.create(this@SiginReAutoActivity, R.raw.cf);
                }
                mRingPlayer?.start();
            }
            binding.stateTv.text = repeatMsg
            binding.stateTv.setTextColor(Color.parseColor("#FFC300"))
            binding.stateIv.setImageResource(R.mipmap.qd3)
        } else if (success.equals("501")) {
//            binding.userName.text = ""
//            binding.companyName.text = ""
//            binding.type.text = ""
//            binding.userName.visibility = View.GONE
//            binding.companyName.visibility = View.GONE
//            binding.type.visibility = View.GONE
            binding.stateTv.text = failedMsg
            binding.stateTv.setTextColor(Color.parseColor("#D43030"))
            binding.stateIv.setImageResource(R.mipmap.cw_h)
            if (voiceStatus.equals("1")) {
//                LiveDataBus.get().with("voiceStatus").postValue(failedMsg)
//                if(SpeechUtils.getInstance(this@SiginReAutoActivity).isSpeech){
//                    SpeechUtils.getInstance(this@SiginReAutoActivity).speakText(failedMsg);
//                }else{
//
//                }
                if (failedMsg.contains("签出")) {
                    mRingPlayer = MediaPlayer.create(this@SiginReAutoActivity, R.raw.dcsb);
                } else {
                    mRingPlayer = MediaPlayer.create(this@SiginReAutoActivity, R.raw.qdsb);
                }
                mRingPlayer?.start();
            }
        } else {
//            binding.userName.visibility = View.GONE
//            binding.companyName.visibility = View.GONE
//            binding.type.visibility = View.GONE
//            binding.userName.text = ""
//            binding.companyName.text = ""
//            binding.type.text = ""
            binding.stateTv.text = failedMsg
            binding.stateTv.setTextColor(Color.parseColor("#D43030"))
            binding.stateIv.setImageResource(R.mipmap.cw_h)
            if (voiceStatus.equals("1")) {
//                LiveDataBus.get().with("voiceStatus").postValue(failedMsg)
//                if(SpeechUtils.getInstance(this@SiginReAutoActivity).isSpeech){
//                    SpeechUtils.getInstance(this@SiginReAutoActivity).speakText(failedMsg);
//                }else{
//
//                }
                if (failedMsg.contains("签出")) {
                    mRingPlayer = MediaPlayer.create(this@SiginReAutoActivity, R.raw.dcsb);
                } else {
                    mRingPlayer = MediaPlayer.create(this@SiginReAutoActivity, R.raw.qdsb);
                }

                mRingPlayer?.start();
            }
        }
        if (autoStatus.equals("1")) {
            timer()
            binding.submit.text = "返回（" + timeLong + "）"
            timer?.start()
        } else {
            binding.submit.text = "返回"
        }
        binding.submit.setOnClickListener { finish() }

    }


    var timer: CountDownTimer? = null
    fun timer() {
        timer = object : CountDownTimer((timeLong * 1000).toLong(), 1000) {
            override fun onTick(millisUntilFinished: Long) {
                binding.submit.text = "返回（" + millisUntilFinished / 1000 + "）"
            }

            /**
             * 倒计时结束后调用的
             */
            override fun onFinish() {
                finish()
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        if (mRingPlayer != null) {
            mRingPlayer?.release()
            mRingPlayer = null;
        }

        kv.putString("printData", "")
    }

    private fun setInfo() {
        binding.recyclerView.layoutManager = LinearLayoutManager(activity)
        var adapter = ReAdapter().apply {
            submitList(meetingFormList)
        }
        binding.recyclerView.adapter = adapter
    }

    private fun printImg(data: SocketData) {
        var printkaiguan = kv.getBoolean("printkaiguan", true)
        if (!printkaiguan) {
            App.getInstance().toast("请前往设置开启打印机")
            return
        }
       var speed = kv.getString("printshudu", "1")?.toDouble()?.toInt()
       var density = kv.getString("printnongdu", "15")?.toDouble()?.toInt()
       var cardW = kv.getString("printkuangdu", "0")?.toDouble()?.toInt()
       var cardH = kv.getString("printgaodu", "0")?.toDouble()?.toInt()
        App.getInstance().toast("正在下载打印图片请等待")
        for (url in data.urls) {
            Glide.with(this@SiginReAutoActivity).asBitmap()
                .load(BaseUrl + url)
//                .load(url)
//                .apply(options)
                .listener(object : RequestListener<Bitmap> {
                    override fun onLoadFailed(
                        e: GlideException?,
                        model: Any?,
                        target: Target<Bitmap>?,
                        isFirstResource: Boolean
                    ): Boolean {
//                        App.getInstance().toast("图片下载失败")
                        Log.d("JWebSocketClient", "图片下载失败=")
                        return false
                    }

                    override fun onResourceReady(
                        resource: Bitmap?,
                        model: Any?,
                        target: Target<Bitmap>?,
                        dataSource: DataSource?,
                        isFirstResource: Boolean
                    ): Boolean {
                        resource?.let { b ->
                            Log.d("JWebSocketClient", "打印机打印=")
//                            if (!CTPL.getInstance().isConnected) {
                            if (false) {
//                                App.getInstance().toast("打印机未连接")
                            } else {
//                                App.getInstance().toast("正在打印请等待")
                                Log.d("JWebSocketClient", "打印机打印=")
//                                data.cardW.intValueExact(),120
//                                data.cardH.intValueExact()80
//                                var w:Int = 120
//                                if(data.cardW.toDouble().toInt()<120){
//                                    w = data.cardW.toDouble().toInt()
//                                }
//                                var h:Int = 80
//                                if(data.cardH.toDouble().toInt()<80){
//                                    h = data.cardH.toDouble().toInt()
//                                }
                                var w: Int = 80
                                if (data.cardW.toDouble().toInt() < 80) {
                                    w = data.cardW.toDouble().toInt()
                                }
                                var h: Int = 50
                                if (data.cardH.toDouble().toInt() < 50) {
                                    h = data.cardH.toDouble().toInt()
                                }
                                if(cardW!=0){
                                    cardW?.let { w =  it }

                                }
                                if(cardH!=0){
                                    cardH?.let { h =  it }
                                }
                                CTPL.getInstance().setPaperType(PaperType.Label).setPrintSpeed(speed).setPrintDensity(density)
                                    .setSize(
                                        w,
                                        h
                                    ) //设置纸张尺寸,单位:毫米
                                    .drawBitmap(
                                        Rect(
                                            0,
                                            0,
                                            w * 12,
                                            h * 12
                                        ), b, true, null
                                    ) //绘制图像, 单位:像素
                                    .print(1)
                                    .execute() //执行打印
                            }
                            Log.d("JWebSocketClient", "打印机打印=rrrr")

                        }
                        return false
                    }
                }
                ).submit()

        }


    }

}