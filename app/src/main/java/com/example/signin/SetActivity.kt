package com.example.signin

import com.example.signin.base.BaseBindingActivity
import com.example.signin.base.BaseViewModel
import com.example.signin.databinding.ActivitySetBinding

class SetActivity : BaseBindingActivity<ActivitySetBinding, BaseViewModel>() {
    override fun getViewModel(): Class<BaseViewModel> = BaseViewModel::class.java
    var startApp = "1"
    override fun initData() {
        if (kv.getString("startApp", "2").equals("1")) {
            startApp = "1"
            binding.kg.setImageResource(R.mipmap.kaiguank)
        } else {
            startApp = "2"
            binding.kg.setImageResource(R.mipmap.kaiguanguan)
        }




        binding.kg.setOnClickListener {
            if(startApp.equals("1")){
                startApp = "2"
                binding.kg.setImageResource(R.mipmap.kaiguanguan)
            }else{
                startApp = "1"
                binding.kg.setImageResource(R.mipmap.kaiguank)
                PermissionPageManager(this@SetActivity).jumpPermissionPage()

            }
            kv.putString("startApp",startApp)

        }
    }



}