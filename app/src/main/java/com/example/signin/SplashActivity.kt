package com.example.signin

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.databinding.DataBindingUtil.setContentView
import com.example.signin.base.BaseBindingActivity
import com.example.signin.base.BaseViewModel
import com.example.signin.databinding.ActSplashBinding
import com.example.signin.databinding.ActivityMainBinding
import com.example.signin.mvvm.vm.MainHomeAVM

class SplashActivity : BaseBindingActivity<ActSplashBinding, BaseViewModel>() {


    override fun getViewModel(): Class<BaseViewModel> = BaseViewModel::class.java


    override fun initData() {

        if (kv.getString("userData", "").isNullOrEmpty()) {
            com.dylanc.longan.startActivity<LoginActivity>()
        } else {
            com.dylanc.longan.startActivity<MainHomeActivity>()
        }
    }

}