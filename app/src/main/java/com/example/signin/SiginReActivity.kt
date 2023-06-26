package com.example.signin

import android.graphics.Color
import android.media.MediaPlayer
import android.os.CountDownTimer
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.alibaba.fastjson.JSON
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CircleCrop
import com.bumptech.glide.request.RequestOptions
import com.dylanc.longan.activity
import com.dylanc.longan.toast
import com.example.signin.adapter.ReAdapter
import com.example.signin.base.BaseBindingActivity
import com.example.signin.base.BaseViewModel
import com.example.signin.base.StatusBarUtil
import com.example.signin.bean.MeetingFormData
import com.example.signin.bean.MeetingFormList
import com.example.signin.bean.SignUpUser
import com.example.signin.databinding.ActSigninStateBinding
import com.example.signin.net.RequestCallback
import com.lzy.okgo.OkGo
import com.lzy.okgo.model.Response
import java.util.HashMap


class SiginReActivity : BaseBindingActivity<ActSigninStateBinding, BaseViewModel>() {
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
    var voiceStatus: String = "2"
    var autoStatus: String = "1"
    var mRingPlayer: MediaPlayer? = null
    var avatar: String = ""
    var signUpStatus: String = "1"
    var ruzhustatus: String = "1"
    var isShowAvatar: Boolean = false
    var meetingFormList: MutableList<MeetingFormList> = ArrayList()
    override fun initData() {
        var d = kv.getString("MeetingFormData", "")
        var isPrint = kv.getBoolean("printkaiguan", true)
        if(isPrint){
            binding.print.visibility = View.VISIBLE
        }else{
            binding.print.visibility = View.GONE
        }
        binding.print.setOnClickListener {
            LiveDataBus.get().with("JWebSocketClientlocationPrint").postValue("JWebSocketClientlocationPrint")
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
                            }
                        }
                    }
                }
            } catch (e: Exception) {
            }

        }
        intent.getIntExtra("type", 0)?.let { type = it }
        intent.getStringExtra("avatar")?.let { avatar = it }
        intent.getStringExtra("ruzhustatus")?.let { ruzhustatus = it }
        intent.getSerializableExtra("data")?.let {
            signUpUser = it as SignUpUser
        }
        signUpUser?.let {
            params["meetingId"] = it.meetingId//会议id
            params["signUpLocationId"] = it.signUpLocationId//签到点id
            params["signUpId"] = it.signUpId//签到站id
            params["userMeetingId"] = it.userMeetingId//用户参与会议id
            params["status"] = "2"//用户参与会议id
            params["signUpStatus"] = it.signUpStatus
            it.meetingName?.let { meetingName -> binding.name.text = meetingName }
//            it.name?.let { name-> binding.userName.text = encode(name)
//                binding.userName.visibility = View.VISIBLE}
//            it.corporateName?.let {companyName-> binding.companyName.text = encode(companyName)
//                binding.companyName.visibility = View.VISIBLE}
//            it.userMeetingTypeName?.let {userMeetingTypeName->binding.type.text = encode(userMeetingTypeName)
//                binding.type.visibility = View.VISIBLE}
            it.timeLong?.let { t -> timeLong = t }
            it.voiceStatus?.let { t -> voiceStatus = t }
            it.failedMsg?.let { it -> failedMsg = it }
            it.okMsg?.let { it -> okMsg = it }
            it.repeatMsg?.let { it -> repeatMsg = it }

        }
//        1 注册签到2 来程签到3 入住签到4 会场签到5 餐饮签到6 礼品签到7 返程签到
        binding.numEt.visibility = View.INVISIBLE
        when (type) {
            1 -> binding.title.text = "注册签到"
            2 -> binding.title.text = "来程签到"
            3 -> {
                binding.title.text = "入住签到"
                binding.numEt.visibility = View.VISIBLE
                binding.stateTv.text = "入住办理"
                binding.submit.text = "确认入住"
                setInfo()
            }
            4 -> {
                binding.title.text = "会场签到"
//                binding.numEt.visibility = View.VISIBLE
//                binding.numEt.hint = "请输入座位号"
            }
            5 -> {
                binding.title.text = "餐饮签到"
//                binding.numEt.visibility = View.VISIBLE
//                binding.numEt.hint = "请输入桌号"
            }
            6 -> binding.title.text = "礼品签到"
            7 -> binding.title.text = "返程签到"
            8 -> binding.title.text = "发票签到"
        }
        binding.submit.setOnClickListener {
            if (binding.submit.text.contains("返回")) {
                finish()
            } else {
                sigin()
            }


        }
        if (autoStatus.equals("1")) {
            timer()
        }
        if (!ruzhustatus.equals("1")) {
            binding.numEt.visibility = View.INVISIBLE
            setInfo()
            binding.stateTv.text = repeatMsg
            binding.stateTv.setTextColor(Color.parseColor("#FFC300"))
            binding.stateIv.setImageResource(R.mipmap.qd3)
            signUpUser?.let {
                if (it.autoStatus.equals("1")) {
                    binding.submit.text = "返回（3）"
                    timer?.start()
                } else {
                    binding.submit.text = "返回"
                }
                if (voiceStatus.equals("1")) {

                    binding.stateTv.text = repeatMsg
                    binding.stateTv.setTextColor(Color.parseColor("#FFC300"))
                    binding.stateIv.setImageResource(R.mipmap.qd3)
                    if (SpeechUtils.getInstance(this@SiginReActivity).isSpeech) {
                        SpeechUtils.getInstance(this@SiginReActivity)
                            .speakText(repeatMsg);
                    } else {
                        if (repeatMsg.contains("签出")) {
                            mRingPlayer =
                                MediaPlayer.create(this@SiginReActivity, R.raw.dccf);
                        } else {
                            mRingPlayer =
                                MediaPlayer.create(this@SiginReActivity, R.raw.cf);
                        }
                        mRingPlayer?.start();
                    }


                }
            }
        }

    }

    private fun setInfo() {
        binding.recyclerView.layoutManager = LinearLayoutManager(activity)
        var adapter = ReAdapter().apply {
            submitList(meetingFormList)
        }
        binding.recyclerView.adapter = adapter
    }

    private fun sigin() {
//        if (type == 3 || type == 4 || type == 5) {
        if (type == 3) {
            if (binding.numEt.text.toString().trim().equals("")) {
                if (type == 3) {
                    toast("请输入房号")
                }
                return
            }
            params["location"] = binding.numEt.text.toString().trim()
        }

        mViewModel.isShowLoading.value = true
        OkGo.post<String>(PageRoutes.Api_sigin)
            .tag(PageRoutes.Api_sigin)
            .upJson(JSON.toJSONString(params))
            .headers("Authorization", kv.getString("token", ""))
            .execute(object : RequestCallback<String>() {
                override fun onSuccessNullData() {
                    super.onSuccessNullData()
                }

                override fun onMySuccess(data: String) {
                    super.onMySuccess(data)
                    if (autoStatus.equals("1")) {
                        timer()
                    }
                    //1成功 2重复
                    if (data.equals("1")) {
                        setInfo()
                        binding.stateTv.text = okMsg
                        binding.stateTv.setTextColor(Color.parseColor("#3974F6"))
//                        binding.stateIv.setImageResource(R.mipmap.qd2)
                        if (avatar.isNullOrEmpty()) {
                            binding.stateIv.setImageResource(R.mipmap.qd2)
                        } else {
                            Glide.with(this@SiginReActivity).load(PageRoutes.BaseUrl + avatar)
                                .apply(
                                    RequestOptions.bitmapTransform(
                                        CircleCrop()
                                    )
                                )
                                .error(R.mipmap.qd2).into(binding.stateIv)
                        }
                        if (!isShowAvatar) {
                            binding.stateIv.setImageResource(R.mipmap.qd2)
                        }
                    } else if (data.equals("2")) {
                        setInfo()
                        binding.stateTv.text = repeatMsg
                        binding.stateTv.setTextColor(Color.parseColor("#FFC300"))
                        binding.stateIv.setImageResource(R.mipmap.qd3)
                    } else {
                        binding.stateTv.text = failedMsg
                        binding.stateTv.setTextColor(Color.parseColor("#D43030"))
                        binding.stateIv.setImageResource(R.mipmap.cw_h)
                    }
                    signUpUser?.let {
                        if (it.autoStatus.equals("1")) {
                            binding.submit.text = "返回（3）"
                            timer?.start()
                        } else {
                            binding.submit.text = "返回"
                        }
                        if (voiceStatus.equals("1")) {
                            if (data.equals("1")) {
                                binding.stateTv.text = okMsg
                                binding.stateTv.setTextColor(Color.parseColor("#3974F6"))
//                                binding.stateIv.setImageResource(R.mipmap.qd2)
//                                LiveDataBus.get().with("voiceStatus").postValue(okMsg)
                                if (SpeechUtils.getInstance(this@SiginReActivity).isSpeech) {
                                    SpeechUtils.getInstance(this@SiginReActivity).speakText(okMsg);
                                } else {
                                    if (okMsg.contains("签出")) {
                                        mRingPlayer =
                                            MediaPlayer.create(this@SiginReActivity, R.raw.dccg);
                                    } else {
                                        mRingPlayer =
                                            MediaPlayer.create(this@SiginReActivity, R.raw.cg);
                                    }
                                    mRingPlayer?.start();
                                }
                            } else if (data.equals("2")) {
                                binding.stateTv.text = repeatMsg
                                binding.stateTv.setTextColor(Color.parseColor("#FFC300"))
                                binding.stateIv.setImageResource(R.mipmap.qd3)
//                                LiveDataBus.get().with("voiceStatus").postValue(repeatMsg)
                                if (SpeechUtils.getInstance(this@SiginReActivity).isSpeech) {
                                    SpeechUtils.getInstance(this@SiginReActivity)
                                        .speakText(repeatMsg);
                                } else {
                                    if (repeatMsg.contains("签出")) {
                                        mRingPlayer =
                                            MediaPlayer.create(this@SiginReActivity, R.raw.dccf);
                                    } else {
                                        mRingPlayer =
                                            MediaPlayer.create(this@SiginReActivity, R.raw.cf);
                                    }
                                    mRingPlayer?.start();
                                }
                            } else {
//                                binding.userName.visibility = View.GONE
//                                binding.companyName.visibility = View.GONE
//                                binding.type.visibility = View.GONE
//                                binding.userName.text = ""
//                                binding.companyName.text = ""
//                                binding.type.text = ""
                                binding.stateTv.text = failedMsg
                                binding.stateTv.setTextColor(Color.parseColor("#D43030"))
                                binding.stateIv.setImageResource(R.mipmap.cw_h)
//                                LiveDataBus.get().with("voiceStatus").postValue(failedMsg)
                                if (SpeechUtils.getInstance(this@SiginReActivity).isSpeech) {
                                    SpeechUtils.getInstance(this@SiginReActivity)
                                        .speakText(failedMsg);
                                } else {

                                    if (failedMsg.contains("签出")) {
                                        mRingPlayer =
                                            MediaPlayer.create(this@SiginReActivity, R.raw.dcsb);
                                    } else {
                                        mRingPlayer =
                                            MediaPlayer.create(this@SiginReActivity, R.raw.qdsb);
                                    }
                                    mRingPlayer?.start();
                                }
                            }

                        }
                    }


                }

                override fun onError(response: Response<String>) {
                    super.onError(response)
                    mViewModel.isShowLoading.value = false
                    binding.stateTv.text = failedMsg
                    binding.stateTv.setTextColor(Color.parseColor("#D43030"))
                    binding.stateIv.setImageResource(R.mipmap.cw_h)
//                    LiveDataBus.get().with("voiceStatus").postValue(failedMsg)
                    if (SpeechUtils.getInstance(this@SiginReActivity).isSpeech) {
                        SpeechUtils.getInstance(this@SiginReActivity)
                            .speakText(failedMsg);
                    } else {

                        if (failedMsg.contains("签出")) {
                            mRingPlayer = MediaPlayer.create(this@SiginReActivity, R.raw.dcsb);
                        } else {
                            mRingPlayer = MediaPlayer.create(this@SiginReActivity, R.raw.qdsb);
                        }
                        mRingPlayer?.start();
                    }
                }

                override fun onFinish() {
                    super.onFinish()
                    binding.numEt.visibility = View.GONE
                    mViewModel.isShowLoading.value = false
                }


            })
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

}