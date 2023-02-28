package com.example.signin

import add
import com.alibaba.fastjson.JSON
import com.bumptech.glide.Glide
import com.dylanc.longan.activity
import com.dylanc.longan.toast
import com.example.signin.base.BaseBindingActivity
import com.example.signin.base.BaseViewModel
import com.example.signin.bean.User
import com.example.signin.databinding.ActivityUserSetBinding
import upFile
import java.io.File

class UserSetActivity : BaseBindingActivity<ActivityUserSetBinding, BaseViewModel>() {
    override fun getViewModel(): Class<BaseViewModel> = BaseViewModel::class.java
    var userData: User? = null
    override fun initData() {
        if (!kv.getString("userData", "").isNullOrEmpty()) {
            userData = JSON.parseObject(kv.getString("userData", ""), User::class.java)
            activity?.let {
                Glide.with(it).load(PageRoutes.BaseUrl + userData?.avatar).error(R.drawable.ov_999)
                    .into(binding.img)
            }
            binding.password.text = userData?.password
            binding.name.text = userData?.name
            binding.uname.text = userData?.nickName
            binding.phone.text = userData?.phonenumber
        }
        binding.img.setOnClickListener {
            takePhotoDialog(this@UserSetActivity) { submitUserAvatar(it) }
        }
    }



    var avatar = ""
    fun submitUserAvatar(file: File) {
        mViewModel.isShowLoading.value = true
        upFile(file, {
            avatar = it.fileName
            add(avatar,

                {
                    userData?.avatar = avatar
                    kv.putString("userData", JSON.toJSONString(userData))
                    activity?.let {
                        Glide.with(it).load(PageRoutes.BaseUrl + avatar).error(R.drawable.ov_999)
                            .into(binding.img)
                    }
                    LiveDataBus.get().with("Avatar").postValue("1")
                },
                { mViewModel.isShowLoading.value = false
                    toast("头像修改失败")},
                { mViewModel.isShowLoading.value = false })
        }, {
            toast("图片上传失败")
            mViewModel.isShowLoading.value = false
        }, {})

    }


}