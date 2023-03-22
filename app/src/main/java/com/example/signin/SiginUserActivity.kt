package com.example.signin

import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.alibaba.fastjson.JSON
import com.dylanc.longan.activity

import com.example.signin.adapter.FMeetingDeList3Adapter
import com.example.signin.base.BaseBindingActivity
import com.example.signin.base.BaseViewModel
import com.example.signin.bean.*

import com.example.signin.databinding.ActSiginUserBinding
import com.example.signin.net.JsonCallback

import com.lzy.okgo.OkGo
import com.lzy.okgo.model.Response


class SiginUserActivity : BaseBindingActivity<ActSiginUserBinding, BaseViewModel>() {
    override fun getViewModel(): Class<BaseViewModel> = BaseViewModel::class.java

    var url = ""
    var name = ""
    var showType = 0

    private var list: MutableList<MeetingUserData> = ArrayList()
    private var adapter: FMeetingDeList3Adapter? = null
    override fun initData() {
        mViewModel.isShowLoading.value = true
        intent.getStringExtra("name")?.let { name = it }
        intent.getStringExtra("url")?.let { url = it }
        intent.getIntExtra("showType",0)?.let { showType = it }
        binding.title.text = name
        binding.recyclerview.layoutManager = LinearLayoutManager(activity)
        adapter = FMeetingDeList3Adapter().apply {
            submitList(list)
            setOnItemClickListener { _, _, position ->
                com.dylanc.longan.startActivity<MeetingUserDectivity>("id" to list[position].id.toString(),
                    "showType" to showType)
            }
        }
        setSelect2Data(showType)
        binding.recyclerview.adapter = adapter

        getList()
    }
    private var siginUp2List: MutableList<TypeData> = ArrayList()
    private fun setSelect2Data(type: Int) {
        //1 注册签到2 来程签到3 入住签到4 会场签到5 餐饮签到6 礼品签到7 返程签到
        var model: TypeModel
        if (!kv.getString("TypeModel", "").isNullOrEmpty()) {
            model = JSON.parseObject(kv.getString("TypeModel", ""), TypeModel::class.java)
            siginUp2List.clear()
            when (type) {
                1 -> {
                    siginUp2List.addAll(model.sys_zhuce)
                }
                2 -> {
                    siginUp2List.addAll(model.sys_laicheng)
                }
                3 -> {
                    siginUp2List.addAll(model.sys_ruzhu)
                }
                4 -> {
                    siginUp2List.addAll(model.sys_huichang)
                }
                5 -> {
                    siginUp2List.addAll(model.sys_canyin)
                }
                6 -> {
                    siginUp2List.addAll(model.sys_liping)
                }
                7 -> {
                    siginUp2List.addAll(model.sys_fancheng)
                }
            }
        }

        adapter?.setSiginUp2List(siginUp2List)
    }

    private fun getList() {

        OkGo.get<MeetingUserModel>(url)
            .tag(url)
            .headers("Authorization", kv.getString("token", ""))
            .execute(object : JsonCallback<MeetingUserModel>(MeetingUserModel::class.java) {

                override fun onSuccess(response: Response<MeetingUserModel>) {

                    response?.let {
                        list.addAll(response.body().data)
                        adapter?.notifyDataSetChanged()
                        binding.num.text = "名单列表（" + response.body().total + "）"
                        if ( list.size <= 0) {
                            binding.recyclerview.visibility = View.GONE
                            binding.kong.visibility = View.VISIBLE
                        } else {
                            binding.recyclerview.visibility = View.VISIBLE
                            binding.kong.visibility = View.GONE
                        }
                    }
                }

                override fun onError(response: Response<MeetingUserModel>?) {
                    super.onError(response)

                }

                override fun onFinish() {

                    mViewModel.isShowLoading.value = false
                }
            })

    }

}