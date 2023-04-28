package com.example.signin

import com.alibaba.fastjson.JSON
import com.dylanc.longan.startActivity
import com.example.signin.base.BaseBindingActivity
import com.example.signin.base.BaseViewModel
import com.example.signin.bean.XCData
import com.example.signin.databinding.ActEditXcBinding
import com.example.signin.net.RequestCallback
import com.lzy.okgo.OkGo
import com.lzy.okgo.model.Response

class XCActivity  : BaseBindingActivity<ActEditXcBinding, BaseViewModel>() {
    override fun getViewModel(): Class<BaseViewModel> = BaseViewModel::class.java
    var id = ""
    var type = 0
    override fun initData() {
        intent.getStringExtra("id")?.let { id = it }
        intent.getIntExtra("type", 0)?.let { type = it }
        //1来程2返程
        if(type==1){
            binding.title.text ="修改来程信息"
        }else{
            binding.title.text ="修改返程信息"
        }

        getData()
    }
var xcData :XCData? = null
    private fun getData() {
        mViewModel.isShowLoading.value = true
        OkGo.get<XCData>(PageRoutes.Api_get_trip + id + "?type=" + type+"&userMeetingId="+id)
            .tag(PageRoutes.Api_get_trip)
            .headers("Authorization", kv.getString("token", ""))
            .execute(object : RequestCallback<XCData>() {
                override fun onSuccessNullData() {
                    super.onSuccessNullData()

                }

                override fun onMySuccess(data: XCData) {
                    super.onMySuccess(data)
                    xcData = data


                }

                override fun onError(response: Response<XCData>) {
                    super.onError(response)

                }

                override fun onFinish() {
                    super.onFinish()
                    mViewModel.isShowLoading.value = false
                }


            })
    }

    fun putTrip( type: Int){

        OkGo.put<String>(PageRoutes.Api_trip)
            .tag(PageRoutes.Api_trip)
            .upJson(JSON.toJSONString(xcData))
            .headers("Authorization",kv.getString("token",""))
            .execute(object : RequestCallback<String>() {

                override fun onSuccess(response: Response<String>?) {
                    super.onSuccess(response)
                    startActivity<MainHomeActivity>()
                    finish()
                }

                override fun onError(response: Response<String>) {
                    super.onError(response)

                    mViewModel.isShowLoading.value = false
                }

                override fun onFinish() {
                    super.onFinish()

                }


            })
    }
}