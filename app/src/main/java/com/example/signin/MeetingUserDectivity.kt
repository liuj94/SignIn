package com.example.signin

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.dylanc.longan.toast
import com.example.signin.base.BaseBindingActivity
import com.example.signin.base.BaseViewModel
import com.example.signin.bean.MeetingData
import com.example.signin.bean.MeetingUserDeData
import com.example.signin.databinding.ActMeetingUserInfoBinding
import com.example.signin.databinding.ActivityAboutBinding
import com.example.signin.net.RequestCallback
import com.lzy.okgo.OkGo
import com.lzy.okgo.model.Response

class MeetingUserDectivity  : BaseBindingActivity<ActMeetingUserInfoBinding, BaseViewModel>() {
    override fun getViewModel(): Class<BaseViewModel> = BaseViewModel::class.java
//var data : MeetingUserDeData
    var id = ""
    override fun initData() {
        intent.getStringExtra("id")?.let {  id = it }

    getData()
    }
    private fun getData() {

        OkGo.get<MeetingUserDeData>(PageRoutes.Api_meetinguser_data+id+"?id="+id)
            .tag(PageRoutes.Api_meetinguser_data+id+"?id="+id)
            .headers("Authorization",kv.getString("token",""))
            .execute(object : RequestCallback<MeetingUserDeData>() {
                override fun onSuccessNullData() {
                    super.onSuccessNullData()

                }

                override fun onMySuccess(data: MeetingUserDeData) {
                    super.onMySuccess(data)

                  binding.itemYhxx.name.text = data.name
                  binding.itemYhxx.gongshiName.text = data.corporateName

                }

                override fun onError(response: Response<MeetingUserDeData>) {
                    super.onError(response)
                    toast(response.message())
                }

                override fun onFinish() {
                    super.onFinish()

                }


            })
    }

}