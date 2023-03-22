package com.example.signin

import android.graphics.Color
import android.os.CountDownTimer
import android.view.View
import com.example.signin.base.BaseBindingActivity
import com.example.signin.base.BaseViewModel
import com.example.signin.base.StatusBarUtil
import com.example.signin.bean.SignUpUser
import com.example.signin.databinding.ActSigninStateBinding
import java.util.HashMap


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
    var voiceStatus: String = "2"
    override fun initData() {
        intent.getIntExtra("type", 0)?.let { type = it }
        intent.getSerializableExtra("data")?.let {
            signUpUser = it as SignUpUser
        }
        signUpUser?.let {
            params["meetingId"] = it.meetingId//会议id
            params["signUpLocationId"] = it.signUpLocationId//签到点id
            params["signUpId"] = it.signUpId//签到站id
            params["userMeetingId"] = it.userMeetingId//用户参与会议id
            params["status"] = "2"//用户参与会议id
            it.meetingName?.let {meetingName-> binding.name.text = meetingName }
            it.name?.let { name-> binding.userName.text = encode(name) }
            it.corporateName?.let {companyName-> binding.companyName.text = encode(companyName) }
            it.userMeetingTypeName?.let {userMeetingTypeName->binding.type.text = encode(userMeetingTypeName)  }
            it.timeLong?.let {t-> timeLong=t }
            it.success?.let {t-> success=t }
            it.voiceStatus?.let {t-> voiceStatus=t }

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
        }

        timer()
        if (success.equals("1")) {
            if(voiceStatus.equals("1")){
                LiveDataBus.get().with("voiceStatus").postValue("1")
            }
            binding.stateTv.text = okMsg
            binding.stateTv.setTextColor(Color.parseColor("#3974F6"))
            binding.stateIv.setImageResource(R.mipmap.qd2)
        } else if (success.equals("2")) {
            if(voiceStatus.equals("1")){
                LiveDataBus.get().with("voiceStatus").postValue("2")
            }
            binding.stateTv.text = repeatMsg
            binding.stateTv.setTextColor(Color.parseColor("#FFC300"))
            binding.stateIv.setImageResource(R.mipmap.qd3)
        } else {
            binding.stateTv.text = failedMsg
            binding.stateTv.setTextColor(Color.parseColor("#D43030"))
            binding.stateIv.setImageResource(R.mipmap.cf_h)
        }
        binding.submit.text = "返回（"+timeLong+"）"
        timer?.start()

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