package com.example.signin

import android.content.Intent
import com.example.signin.base.BaseBindingActivity
import com.example.signin.base.BaseViewModel
import com.example.signin.databinding.ActSplashBinding


class SplashActivity : BaseBindingActivity<ActSplashBinding, BaseViewModel>() {


    override fun getViewModel(): Class<BaseViewModel> = BaseViewModel::class.java


    override fun initData() {
        val hideNavIntent = Intent()
        hideNavIntent.action = "android.intent.action.systemui"
        hideNavIntent.putExtra("navigation_bar", "dismiss")
        sendBroadcast(hideNavIntent)
        if (kv.getString("userData", "").isNullOrEmpty()) {
            com.dylanc.longan.startActivity<LoginActivity>()
        } else {
            com.dylanc.longan.startActivity<MainHomeActivity>()
        }
        finish()
    }

}