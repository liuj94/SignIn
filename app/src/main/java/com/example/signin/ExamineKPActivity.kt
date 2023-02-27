package com.example.signin

import android.graphics.Color
import android.text.Html
import com.alibaba.fastjson.JSON
import com.dylanc.longan.startActivity
import com.example.signin.base.BaseBindingActivity
import com.example.signin.base.BaseViewModel
import com.example.signin.base.StatusBarUtil
import com.example.signin.bean.MeetingUserDeData
import com.example.signin.bean.TypeModel
import com.example.signin.databinding.ActKpBinding
import com.example.signin.net.RequestCallback
import com.lzy.okgo.OkGo
import com.lzy.okgo.model.Response
import java.util.HashMap


class ExamineKPActivity : BaseBindingActivity<ActKpBinding, BaseViewModel>() {
    override fun initTranslucentStatus() {
        StatusBarUtil.setTranslucentStatus(this, Color.TRANSPARENT)
        //设置状态栏字体颜色
        StatusBarUtil.setAndroidNativeLightStatusBar(this, true)
    }

    override fun getViewModel(): Class<BaseViewModel> = BaseViewModel::class.java
    var order: MeetingUserDeData.UserOrderBean = MeetingUserDeData.UserOrderBean()

    var params = HashMap<String, Any>()
    override fun initData() {

        intent.getSerializableExtra("order")?.let { order = it as MeetingUserDeData.UserOrderBean }
        binding.name.text = order.meetingName
        binding.userName.text = order.userName
        binding.companyName.text = order.corporateName
        var p = "开票金额<font color='#D43030'>" + order.amount + "</font>元"
        binding.amount.text = Html.fromHtml(p)
        //增值税专用发票、普通销售发票
        if (order.invoiceType.equals("1")) {
            binding.invoiceType.text = "普通销售发票"
        } else {
            binding.invoiceType.text = "增值税专用发票"
        }
        var model =
            JSON.parseObject(kv.getString("TypeModel", ""), TypeModel::class.java)
        for (item in model.sys_invoice_type) {
            if (order.invoiceType.equals(item.dictValue)) {
                binding.invoiceType.text =
                    item.dictLabel
            }
        }

//        binding.stateIv
//        binding.invoiceNumber
//        binding.invoiceNo
        binding.btn.setOnClickListener { startActivity<ExamineActivity>("order" to order) }
        binding.submit.setOnClickListener {
            //        binding.invoiceNumber
//        binding.invoiceNo
            examine()
        }
    }

    private fun examine() {
        var params = HashMap<String, Any>()
        params["id"] = order.id
        mViewModel.isShowLoading.value = true
        OkGo.post<String>(PageRoutes.Api_billfinish)
            .tag(PageRoutes.Api_billfinish)
            .upJson(JSON.toJSONString(params))
            .headers("Authorization", kv.getString("token", ""))
            .execute(object : RequestCallback<String>() {

                override fun onSuccess(response: Response<String>?) {
                    super.onSuccess(response)
                    finish()
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