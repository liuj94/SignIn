package com.example.signin

import com.dylanc.longan.toast
import com.example.signin.base.BaseBindingActivity
import com.example.signin.base.BaseViewModel
import com.example.signin.databinding.ActLoginBinding


class LoginActivity : BaseBindingActivity<ActLoginBinding, BaseViewModel>(){

    override fun getViewModel(): Class<BaseViewModel> = BaseViewModel::class.java

    override fun initData() {

    }
    private var exitTime: Long = 0
    override fun onBackPressed() {
        if (System.currentTimeMillis() - exitTime > 2000) {
            toast( "再按一次返回退出程序")
            exitTime = System.currentTimeMillis()
        } else {

            System.exit(0);
        }
    }

}


