package com.example.signin

import com.alibaba.fastjson.JSON
import com.example.signin.base.BaseBindingActivity
import com.example.signin.base.BaseViewModel
import com.example.signin.databinding.ActKpBinding
import com.example.signin.net.RequestCallback
import com.lzy.okgo.OkGo
import com.lzy.okgo.model.Response
import java.util.HashMap


class ExamineKPActivity : BaseBindingActivity<ActKpBinding, BaseViewModel>() {
    override fun getViewModel(): Class<BaseViewModel> = BaseViewModel::class.java

    var orderId : String = ""
    var params = HashMap<String, Any>()
    override fun initData() {

        intent.getStringExtra("orderId")?.let { orderId = it }


    }

    private fun examine() {

        mViewModel.isShowLoading.value = true
        OkGo.post<String>(PageRoutes.Api_examine)
            .tag(PageRoutes.Api_examine)
            .upJson(JSON.toJSONString(params))
            .headers("Authorization", kv.getString("token", ""))
            .execute(object : RequestCallback<String>() {

                override fun onSuccess(response: Response<String>?) {
                    super.onSuccess(response)

                }

                override fun onFinish() {
                    super.onFinish()

                }

                override fun onError(response: Response<String>?) {
                    super.onError(response)
                    mViewModel.isShowLoading.value = false
                }


            })
    }

}