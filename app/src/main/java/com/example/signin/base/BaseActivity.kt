package com.example.signin.base

import android.app.Activity
import android.content.res.Configuration
import android.content.res.Resources
import android.os.Bundle

import android.view.MotionEvent
import android.view.WindowManager

import android.view.inputmethod.InputMethodManager

import androidx.appcompat.app.AppCompatActivity

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelStoreOwner
import androidx.viewbinding.ViewBinding

import com.dylanc.mmkv.MMKVOwner
import com.dylanc.viewbinding.base.ActivityBinding
import com.dylanc.viewbinding.base.ActivityBindingDelegate
import com.example.signin.AppManager
import com.tencent.mmkv.MMKV

abstract class BaseActivity<VB : ViewBinding,VM : BaseViewModel> : AppCompatActivity(),
    ActivityBinding<VB> by ActivityBindingDelegate(), MMKVOwner {
        val mViewModel: VM by lazy {
        obtainViewModel(this, getViewModel())
    }
    abstract fun getViewModel(): Class<VM>
    override val kv = MMKV.mmkvWithID("MyDataMMKV")


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        startPendingTransition()
        initTranslucentStatus()
        setContentViewWithBinding()
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
        mViewModel.mContext = this
        getWindow().setSoftInputMode( WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        AppManager.getAppManager().addActivity(this)

    }
var isFrist = true;
    override fun onEnterAnimationComplete() {
        super.onEnterAnimationComplete()
        if(isFrist){
            isFrist = false
            initProgressDialog()
            initIntentStringExtra()
            initTitle()
            initData()
            initListener()
            initRootTitleBar()
        }

    }
    override fun onDestroy() {
        AppManager.getAppManager().removeActivity(this)
        super.onDestroy()
    }

    abstract fun initData()
    open fun initListener() {}
    open fun initProgressDialog() {}
    open fun initRootTitleBar() {

    }

    open fun initTitle() {

    }
    open fun startPendingTransition() {

    }
    open fun initTranslucentStatus() {

    }
    open fun finishPendingTransition() {

    }
    open fun initIntentStringExtra() {

    }

    override fun finish() {
        super.finish()
        finishPendingTransition()
    }



    //隐藏软键盘
    override fun dispatchTouchEvent(ev: MotionEvent): Boolean {
        if (ev.action == MotionEvent.ACTION_DOWN) {
            hideSoftInput()
            return super.dispatchTouchEvent(ev)
        }
        return if (window.superDispatchTouchEvent(ev)) {
            true
        } else onTouchEvent(ev)
    }
    fun Activity.hideSoftInput() = currentFocus?.let { (getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager).hideSoftInputFromWindow(it.windowToken, 0) }

    override fun getResources(): Resources? {
        val res: Resources = super.getResources()
        val config = Configuration()
        config.setToDefaults()
        res.updateConfiguration(config, res.displayMetrics)
        return res
    }


    fun <T : ViewModel> obtainViewModel(owner: ViewModelStoreOwner, viewModelClass: Class<T>) =
        ViewModelProvider(owner, ViewModelProvider.NewInstanceFactory()).get(viewModelClass)

}
