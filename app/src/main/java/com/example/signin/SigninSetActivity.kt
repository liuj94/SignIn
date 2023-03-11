package com.example.signin

import android.graphics.Color
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.TextView
import com.alibaba.fastjson.JSON
import com.dylanc.longan.toast
import com.example.signin.base.BaseBindingActivity
import com.example.signin.base.BaseViewModel
import com.example.signin.bean.SiginData
import com.example.signin.databinding.ActivitySiginSetBinding
import com.example.signin.net.RequestCallback
import com.lzy.okgo.OkGo
import com.lzy.okgo.model.Response
import com.tencent.mmkv.MMKV


class SigninSetActivity : BaseBindingActivity<ActivitySiginSetBinding, BaseViewModel>() {

    override fun getViewModel(): Class<BaseViewModel> = BaseViewModel::class.java

    override fun initData() {
        mViewModel.isShowLoading.value = true
        intent.getStringExtra("id")?.let {
            id = it
        }

//        if (!kv.getString("userData", "").isNullOrEmpty()) {
//            var data = JSON.parseObject(kv.getString("userData", ""), User::class.java)
//            activity?.let {
//                Glide.with(it).load(PageRoutes.BaseUrl + data.avatar).error(R.drawable.ov_ccc).into(binding.img)
//            }
//            binding.name.text = data.bame
//            binding.password.text = data.phonenumber
//        }
        binding.kg.setOnClickListener {
            if(autoStatus.equals("1")){
                autoStatus = "2"
//                binding.kg.setImageResource(R.mipmap.kaiguan2)
//                binding.kgtv.text = "(自动)"
//                binding.settime.visibility = View.VISIBLE
                timeLong = "3"
            }else{
                autoStatus = "1"
//                binding.kg.setImageResource(R.mipmap.kaiguan1)
//                binding.kgtv.text = "(手动)"
//                binding.settime.visibility = View.GONE
//                setTimeState(binding.time3)
                timeState = 3
                timeLong = "3"
            }

            setState()
        }
        binding.time2.setOnClickListener {
            binding.time4.setText("" )
            setTimeState(binding.time2)
            timeState = 2
            timeLong = "2"
            setState()
        }
        binding.time1.setOnClickListener {
            binding.time4.setText("" )
            setTimeState(binding.time1)
            timeState = 1
            timeLong = "1"
            setState()
        }
        binding.time3.setOnClickListener {
            binding.time4.setText("" )
            setTimeState(binding.time3)
            timeState = 3
            timeLong = "3"
            setState()
        }
        binding.time3.setOnClickListener {
            binding.time4.setText("" )
            setTimeState(binding.time3)
            timeState = 3
            timeLong = "3"
            setState()
        }
        binding.time5.setOnClickListener {
            binding.time4.setText("" )
            setTimeState(binding.time5)
            timeState = 5
            timeLong = "5"
            setState()
        }
        binding.time10.setOnClickListener {
            binding.time4.setText("" )
            setTimeState(binding.time10)
            timeState = 10
            timeLong = "10"
            setState()
        }
        binding.time4.setOnClickListener {
            binding.time4.setText("" )
            setTimeState(binding.time4)
            timeState = 4
        }
        binding.kg1.setOnClickListener {

            if(speechStatus.equals("2")){
                speechStatus = "1"
                binding.kg1.setImageResource(R.mipmap.kaiguan1)
//                binding.yuying.visibility = View.VISIBLE
            }else{
                speechStatus = "2"
                binding.kg1.setImageResource(R.mipmap.kaiguan2)
//                binding.yuying.visibility = View.GONE
            }
            setState(false)
        }
        binding.kg2.setOnClickListener {
            if(shockStatus.equals("2")){
                shockStatus = "1"
                binding.kg2.setImageResource(R.mipmap.kaiguan1)
            }else{
                shockStatus = "2"
                binding.kg2.setImageResource(R.mipmap.kaiguan2)
            }

            setState()
        }

        binding.okMsg.addTextChangedListener(object : TextWatcher{
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

            }

            override fun afterTextChanged(s: Editable?) {

                if(!okMsg.equals(s.toString().trim())){
                    okMsg = s.toString().trim()
                    setState()
                }
            }

        })
        binding.repeatMsg.addTextChangedListener(object : TextWatcher{
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

            }

            override fun afterTextChanged(s: Editable?) {
                if(!repeatMsg.equals(s.toString().trim())){
                    repeatMsg = s.toString().trim()
                    setState()
                }

            }

        })
        binding.failedMsg.addTextChangedListener(object : TextWatcher{
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

            }

            override fun afterTextChanged(s: Editable?) {

                if(!failedMsg.equals(s.toString().trim())){
                    failedMsg = s.toString().trim()
                    setState()
                }
            }

        })

        getDate()
    }

    var id:String = ""
    var failedMsg:String = "签到失败"
    var okMsg:String = "签到成功"
    var repeatMsg:String = "重复签到"
    //1开启 2关闭 自动
    var autoStatus:String = "2"
    //shockStatus	1开启 2关闭 震动
    var shockStatus:String = "2"

    var timeLong:String = "3"
    var timeState:Int = 1
    var speechStatus:String = "2"

    fun getDate(){

        OkGo.get<SiginData>(PageRoutes.Api_meetingSignUpLocationDe+id)
            .tag(PageRoutes.Api_meetingSignUpLocationDe+id)
            .headers("Authorization", MMKV.mmkvWithID("MyDataMMKV").getString("token",""))
            .execute(object : RequestCallback<SiginData>() {
                override fun onMySuccess(data: SiginData) {
                    super.onMySuccess(data)
                    binding.name.text = data.name
                    binding.password.text = data.personChargeUsername
                    failedMsg = ""+data.failedMsg
                    okMsg = ""+data.okMsg
                    repeatMsg = ""+data.repeatMsg
                    //1开启 2关闭 自动
                    autoStatus = ""+data.autoStatus
                    //shockStatus	1开启 2关闭 震动
                    shockStatus = ""+data.shockStatus
                    speechStatus =  ""+data.speechStatus

                    timeLong = ""+data.timeLong

                    binding.failedMsg.setText(failedMsg)
                    binding.failedMsg.setSelection(failedMsg.length)
                    binding.okMsg.setText( okMsg)
                    binding.okMsg.setSelection(okMsg.length)
                    binding.repeatMsg.setText( repeatMsg)
                    binding.repeatMsg.setSelection(repeatMsg.length)

                    if(autoStatus.equals("1")){
                        binding.kg.setImageResource(R.mipmap.kaiguan2)
                        binding.kgtv.text = "(手动)"
                        binding.settime.visibility = View.GONE


                    }else{
                        binding.kg.setImageResource(R.mipmap.kaiguan1)
                        binding.kgtv.text = "(自动)"
                        binding.settime.visibility = View.VISIBLE
                        if(timeLong.equals("1")){
                            setTimeState(binding.time1)
                            timeState = 1
                        }else if(timeLong.equals("2")){
                            setTimeState(binding.time2)
                            timeState = 2
                        }else if(timeLong.equals("3")){
                            setTimeState(binding.time3)
                            timeState = 3
                        }else if(timeLong.equals("5")){
                            setTimeState(binding.time5)
                            timeState = 5
                        }else if(timeLong.equals("10")){
                            setTimeState(binding.time10)
                            timeState = 10
                        }else{
                            timeState = 4
                            setTimeState(binding.time4)
                        }
                    }
                    if(shockStatus.equals("1")){
                        binding.kg2.setImageResource(R.mipmap.kaiguan1)
                    }else{
                        binding.kg2.setImageResource(R.mipmap.kaiguan2)
                    }
                    if(speechStatus.equals("1")){
                        binding.kg1.setImageResource(R.mipmap.kaiguan1)
//                        binding.yuying.visibility = View.VISIBLE
                    }else{
                        binding.kg1.setImageResource(R.mipmap.kaiguan2)
//                        binding.yuying.visibility = View.GONE
                    }
                }


                override fun onError(response: Response<SiginData>) {
                    super.onError(response)
                    toast("")
                }

                override fun onFinish() {
                    super.onFinish()
                    mViewModel.isShowLoading.value = false
                }


            })
    }

    fun setTimeState(tv:TextView){
        binding.time1.setTextColor(Color.parseColor("#666666"))
        binding.time2.setTextColor(Color.parseColor("#666666"))
        binding.time3.setTextColor(Color.parseColor("#666666"))
        binding.time4.setTextColor(Color.parseColor("#666666"))
        binding.time5.setTextColor(Color.parseColor("#666666"))
        binding.time10.setTextColor(Color.parseColor("#666666"))
        binding.time1.setBackgroundResource(R.drawable.shape_bg_9999_fff_17)
        binding.time2.setBackgroundResource(R.drawable.shape_bg_9999_fff_17)
        binding.time3.setBackgroundResource(R.drawable.shape_bg_9999_fff_17)
        binding.time4.setBackgroundResource(R.drawable.shape_bg_9999_fff_17)
        binding.time5.setBackgroundResource(R.drawable.shape_bg_9999_fff_17)
        binding.time10.setBackgroundResource(R.drawable.shape_bg_9999_fff_17)

        tv.setTextColor(Color.parseColor("#ffffff"))
        tv.setBackgroundResource(R.drawable.shape_bg_3974f6_17)
    }
    fun setState(re:Boolean?=true){

        val params = HashMap<String, String>()
        params["id"] = id
        params["autoStatus"] = autoStatus
        params["speechStatus"] = speechStatus
        params["timeLong"] = timeLong
        params["shockStatus"] = shockStatus
        params["repeatMsg"] = repeatMsg
        params["okMsg"] = okMsg
        params["failedMsg"] = failedMsg

        OkGo.put<String>(PageRoutes.Api_ed_meetingSignUpLocation)
            .tag(PageRoutes.Api_ed_meetingSignUpLocation)
            .upJson(JSON.toJSONString(params))
            .headers("Authorization", MMKV.mmkvWithID("MyDataMMKV")
                .getString("token",""))
            .execute(object : RequestCallback<String>() {

                override fun onSuccess(response: Response<String>?) {
                    super.onSuccess(response)
                    re?.let {
                        if (it){
                            getDate()
                        }
                    }
                    if(re==null){
                        getDate()
                    }

                }

                override fun onError(response: Response<String>) {
                    super.onError(response)
                    toast("")
                }

                override fun onFinish() {
                    super.onFinish()

                }


            })
    }

}


