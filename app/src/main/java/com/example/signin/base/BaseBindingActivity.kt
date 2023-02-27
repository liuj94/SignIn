package com.example.signin.base

import android.graphics.Color

import android.view.View
import androidx.viewbinding.ViewBinding
import com.example.signin.ProgressDialog

import com.example.signin.R


abstract class BaseBindingActivity<VB : ViewBinding,VM : BaseViewModel> : BaseActivity<VB, VM>() {
    override fun initRootTitleBar() {
        super.initRootTitleBar()


        findViewById<View>(R.id.goback)?.let { it.setOnClickListener { onBackPressed() } }
    }
    override fun startPendingTransition() {
        overridePendingTransition(R.anim.left_in, R.anim.left_out)
    }
    override fun initTranslucentStatus() {
        StatusBarUtil.setTranslucentStatus(this,Color.WHITE)
        //设置状态栏字体颜色
        StatusBarUtil.setAndroidNativeLightStatusBar(this,true)

    }
    private var mProgressDialog: ProgressDialog? = null
    override fun initProgressDialog() {
        super.initProgressDialog()
        mProgressDialog = ProgressDialog(this)
        mViewModel.isShowLoadingLiveData().observe(this) {
            if (it) {
                showLoading()
            } else {
                hideLoading()
            }
        }
    }
    override fun finishPendingTransition() {
        overridePendingTransition(R.anim.right_in, R.anim.right_out)

    }

        override fun onPause() {
        super.onPause()



    }
    fun showLoading() {
        if (mProgressDialog?.isShowing == false)
            mProgressDialog?.show()
    }

    fun hideLoading() {
        if (mProgressDialog?.isShowing == true)
            mProgressDialog?.dismiss()
    }
}