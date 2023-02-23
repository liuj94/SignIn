package com.example.signin

import com.alibaba.fastjson.JSON
import com.dylanc.longan.toast
import com.example.signin.base.BaseBindingActivity
import com.example.signin.base.BaseViewModel
import com.example.signin.databinding.ActivitySiginSetBinding
import com.example.signin.net.RequestCallback
import com.lzy.okgo.OkGo
import com.lzy.okgo.model.Response


class SigninSetActivity : BaseBindingActivity<ActivitySiginSetBinding, BaseViewModel>() {

    override fun getViewModel(): Class<BaseViewModel> = BaseViewModel::class.java

    override fun initData() {
        kv.getString("user","")

    }

    fun add( phonenumber:String,userName:String,userData :String){
        val params = HashMap<String, String>()
        params["phonenumber"] = phonenumber
        params["userName"] = userName

        OkGo.put<String>(PageRoutes.Api_editUser)
            .tag(PageRoutes.Api_editUser)
            .upJson(JSON.toJSONString(params))
            .headers("Authorization",kv.getString("token",""))
            .execute(object : RequestCallback<String>() {

                override fun onSuccess(response: Response<String>?) {
                    super.onSuccess(response)

                }

                override fun onError(response: Response<String>) {
                    super.onError(response)
                    toast(response.message())
                    mViewModel.isShowLoading.value = false
                }

                override fun onFinish() {
                    super.onFinish()

                }


            })
    }



}


