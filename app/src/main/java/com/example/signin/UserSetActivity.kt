package com.example.signin

import com.alibaba.fastjson.JSON
import com.bumptech.glide.Glide
import com.dylanc.longan.activity
import com.example.signin.base.BaseBindingActivity
import com.example.signin.base.BaseViewModel
import com.example.signin.bean.User
import com.example.signin.databinding.ActivityUserSetBinding

class UserSetActivity  : BaseBindingActivity<ActivityUserSetBinding, BaseViewModel>() {
    override fun getViewModel(): Class<BaseViewModel> = BaseViewModel::class.java

    override fun initData() {
        if (!kv.getString("userData", "").isNullOrEmpty()) {
            var data = JSON.parseObject(kv.getString("userData", ""), User::class.java)
            activity?.let {
                Glide.with(it).load(PageRoutes.BaseUrl + data.avatar).error(R.drawable.ov_999).into(binding.img)
            }
            binding.password.text = data.password
            binding.name.text = data.name
            binding.uname.text = data.nickName
            binding.phone.text = data.phonenumber
        }
    }
}