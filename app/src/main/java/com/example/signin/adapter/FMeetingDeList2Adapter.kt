package com.example.signin.adapter


import android.widget.Toast
import com.alibaba.fastjson.JSON
import com.dylanc.longan.startActivity
import com.dylanc.longan.toast
import com.example.signin.*

import com.example.signin.base.BaseBindingAdapter
import com.example.signin.bean.SiginData
import com.example.signin.bean.User
import com.example.signin.databinding.ListMeetingde2Binding
import com.example.signin.net.RequestCallback
import com.lzy.okgo.OkGo
import com.lzy.okgo.model.Response
import com.tencent.mmkv.MMKV


class FMeetingDeList2Adapter: BaseBindingAdapter<SiginData, ListMeetingde2Binding>() {

    override fun onBindViewHolder(holder: ListMeetingde2Binding, item: SiginData, position: Int) {

        holder.name.text =  item.name
//1 注册签到2 来程签到3 入住签到4 会场签到5 餐饮签到6 礼品签到7 返程签到 8发票签到
        var num = ""
        if(item.type == 1){
            num = "注册签到"+item.currentUserCount+"/"+item.totalUserCount
        }else if(item.type == 2){
            num = "来程签到"+item.currentUserCount+"/"+item.totalUserCount
        }else if(item.type == 3){
            num = "入住签到"+item.currentUserCount+"/"+item.totalUserCount
        }else if(item.type == 4){
            num = "会场签到"+item.currentUserCount+"/"+item.totalUserCount
        }else if(item.type == 5){
            num = "餐饮签到"+item.currentUserCount+"/"+item.totalUserCount
        }else if(item.type == 6){
            num = "礼品签到"+item.currentUserCount+"/"+item.totalUserCount
        }else if(item.type == 7){
            num = "返程签到"+item.currentUserCount+"/"+item.totalUserCount
        }else if(item.type == 8){
            num = "发票签到"+item.currentUserCount+"/"+item.totalUserCount
        }
        holder.num.text = num
        //"status": 2,
        //            "voiceStatus": 1,
        if(item.status == 2){
            holder.kg1.setImageResource(R.mipmap.kaiguan2)
        }else{
            holder.kg1.setImageResource(R.mipmap.kaiguan1)
        }
        if(item.voiceStatus == 2){
            holder.kg2.setImageResource(R.mipmap.kaiguan2)
        }else{
            holder.kg2.setImageResource(R.mipmap.kaiguan1)
        }
        holder.kg1.setOnClickListener {
            var status = 1

            if(item.status==1){
                status = 2
            }
            setState(item.id,status,item.voiceStatus,item)
//            if (!kv.getString("userData", "").isNullOrEmpty()) {
//                var userData = JSON.parseObject(kv.getString("userData", ""), User::class.java)
//                userData?.let {
//                    if (it.userType.equals("01")||it.userType.equals("04")){
//                        var status = 1
//
//                        if(item.status==1){
//                            status = 2
//                        }
//                        setState(item.id,status,item.voiceStatus,item)
//
//                    }else{
//                        Toast.makeText(context, "账号没有修改权限", Toast.LENGTH_SHORT).show()
//
//                    }
//
//
//                }
//
//
//            }

        }
        holder.kg2.setOnClickListener {
            if (!kv.getString("userData", "").isNullOrEmpty()) {
                var userData = JSON.parseObject(kv.getString("userData", ""), User::class.java)
                userData?.let {
                    if (it.userType.equals("01")||it.userType.equals("04")){
                        var voiceStatus = 1

                        if(item.voiceStatus==1){
                            voiceStatus = 2
                        }else{
                            voiceStatus = 1
                        }
                        setState(item.id,item.status,voiceStatus,item)

                    }else{
                        Toast.makeText(context, "账号没有修改权限", Toast.LENGTH_SHORT).show()

                    }


                }


            }

        }
        holder.sz.setOnClickListener {
           startActivity<SigninSetActivity>("id" to ""+item.id)
        }
//        holder.qd.setOnClickListener {
//
//        }

    }


    fun setState(id: Int, status: Int, voiceStatus: Int,item: SiginData){
//        {id: 66, status: 1, voiceStatus: 1}
        val params = HashMap<String, Int>()
        params["id"] = id
        params["status"] = status
        params["voiceStatus"] = voiceStatus

        OkGo.put<String>(PageRoutes.Api_ed_meetingSignUpLocation)
            .tag(PageRoutes.Api_ed_meetingSignUpLocation)
            .upJson(JSON.toJSONString(params))
            .headers("Authorization", MMKV.mmkvWithID("MyDataMMKV").getString("token",""))
            .execute(object : RequestCallback<String>() {

                override fun onSuccess(response: Response<String>?) {
                    super.onSuccess(response)
                    item.voiceStatus = voiceStatus
                    item.status = status
                    notifyDataSetChanged()
                }

                override fun onError(response: Response<String>) {
                    super.onError(response)
                    context.toast("")
                }

                override fun onFinish() {
                    super.onFinish()

                }


            })
    }
    }
