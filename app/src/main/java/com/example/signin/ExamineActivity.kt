package com.example.signin

import com.afollestad.materialdialogs.LayoutMode
import com.afollestad.materialdialogs.MaterialDialog
import com.afollestad.materialdialogs.bottomsheets.BottomSheet
import com.afollestad.materialdialogs.list.ItemListener
import com.afollestad.materialdialogs.list.listItems
import com.alibaba.fastjson.JSON
import com.example.signin.base.BaseBindingActivity
import com.example.signin.base.BaseViewModel
import com.example.signin.bean.TypeModel
import com.example.signin.databinding.ActInformationReviewBinding
import com.example.signin.net.RequestCallback
import com.lzy.okgo.OkGo
import com.lzy.okgo.model.Response
import java.util.HashMap


class ExamineActivity : BaseBindingActivity<ActInformationReviewBinding, BaseViewModel>() {
    override fun getViewModel(): Class<BaseViewModel> = BaseViewModel::class.java

    var orderId: String = ""
    var failRemark: String = ""
    var params = HashMap<String, Any>()
    override fun initData() {

        intent.getStringExtra("orderId")?.let { orderId = it }
        binding.btn1.setOnClickListener {

            select()
        }
        binding.btn2.setOnClickListener {
            params["orderId"] = orderId
            params["examine"] = true
            examine()
        }

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


    fun select() {
        var model = JSON.parseObject(kv.getString("TypeModel", ""), TypeModel::class.java)
        var list :MutableList<String> = ArrayList<String>()
        for (item in model.sys_examine_reason) {
            list.add(item.dictLabel)
        }


        MaterialDialog(this, BottomSheet(LayoutMode.WRAP_CONTENT)).show {
            listItems(items = list, selection = object : ItemListener {
                override fun invoke(dialog: MaterialDialog, index: Int, text: CharSequence) {
                    failRemark = list[index]
                    params["failRemark"] = failRemark
                    params["examine"] = false
                    examine()
                }
            })

        }
    }

}