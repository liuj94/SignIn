package com.example.signin

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.signin.base.BaseBindingActivity
import com.example.signin.base.BaseViewModel
import com.example.signin.databinding.ActivityAboutBinding

class AboutActivity  : BaseBindingActivity<ActivityAboutBinding, BaseViewModel>() {
    override fun getViewModel(): Class<BaseViewModel> = BaseViewModel::class.java

    override fun initData() {

    }
}