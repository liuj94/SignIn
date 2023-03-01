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
            binding.name.text = it.meetingName
            binding.userName.text = encode(it.name)
            binding.companyName.text = encode(it.corporateName)
            binding.type.text = encode(it.userMeetingTypeName)
            timeLong = it.timeLong
            success = it.success
            voiceStatus= it.voiceStatus
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
//        binding.submit.setOnClickListener {
//            if(binding.submit.text.contains("返回")){
//                finish()
//            }else{
//                sigin()
//            }
//
//
//        }
        timer()
        if (success.equals("1")) {
            binding.stateTv.text = okMsg
            binding.stateTv.setTextColor(Color.parseColor("#3974F6"))
            binding.stateIv.setImageResource(R.mipmap.qd2)
        } else if (success.equals("2")) {
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
        if(voiceStatus.equals("1")){
            LiveDataBus.get().with("voiceStatus").postValue("1")
        }
    }

//    private fun sigin() {
//        if (type == 3 || type == 4 || type == 5) {
//            if (binding.numEt.text.toString().trim().equals("")) {
//                if (type == 3) {
//                    toast("请输入房号")
//                } else {
//                    toast("请输入座位号")
//                }
//
//                return
//            }
//            params["location"] = binding.numEt.text.toString().trim()
//        }
//
//        mViewModel.isShowLoading.value = true
//        OkGo.post<String>(PageRoutes.Api_sigin)
//            .tag(PageRoutes.Api_sigin)
//            .upJson(JSON.toJSONString(params))
//            .headers("Authorization", kv.getString("token", ""))
//            .execute(object : RequestCallback<String>() {
//
//                override fun onMySuccess(data: String) {
//                    super.onMySuccess(data)
//                    //1成功 2重复
//                    if(data.equals("1")){
//                        binding.stateTv.text = okMsg
//                        binding.stateTv.setTextColor(Color.parseColor("#3974F6"))
//                        binding.stateIv.setImageResource(R.mipmap.qd2)
//                    }else if(data.equals("2")){
//                        binding.stateTv.text = repeatMsg
//                        binding.stateTv.setTextColor(Color.parseColor("#FFC300"))
//                        binding.stateIv.setImageResource(R.mipmap.qd3)
//                    }else{
//                        binding.stateTv.text = failedMsg
//                        binding.stateTv.setTextColor(Color.parseColor("#D43030"))
//                        binding.stateIv.setImageResource(R.mipmap.cf_h)
//                    }
////                    signUpUser?.let {
////                        if(it.autoStatus.equals("2")){
////                            binding.submit.text = "返回（3）"
////                            timer?.start()
////                        } else {
////                            binding.submit.text = "返回"
////                        }
////                    }
//                    binding.submit.text = "返回"
//
//                }
//
//                override fun onError(response: Response<String>) {
//                    super.onError(response)
//                    mViewModel.isShowLoading.value = false
//                }
//
//                override fun onFinish() {
//                    super.onFinish()
//                    mViewModel.isShowLoading.value = false
//                }
//
//
//            })
//    }

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