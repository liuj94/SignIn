package com.example.signin.base

import android.app.Activity

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.viewbinding.ViewBinding
import com.dylanc.mmkv.MMKVOwner
import com.dylanc.viewbinding.base.FragmentBinding
import com.dylanc.viewbinding.base.FragmentBindingDelegate
import com.example.signin.ProgressDialog
import com.example.signin.obtainViewModel
import com.tencent.mmkv.MMKV

abstract class BaseBindingFragment<VB : ViewBinding,T : BaseViewModel> : Fragment(),
    FragmentBinding<VB> by FragmentBindingDelegate(), MMKVOwner {
        val mViewModel : T by lazy {
        obtainViewModel(this,getViewModel())
    }
    abstract fun getViewModel(): Class<T>
    override val kv = MMKV.mmkvWithID("MyDataMMKV")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {

        return createViewWithBinding(inflater, container)
    }
    private var mProgressDialog: ProgressDialog? = null
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
//         mProgressDialog = ProgressDialog(requireActivity())
//        mViewModel.mContext = requireActivity()
//        initProgressDialog()
//        initData()
//        initListener()
    }
    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        mProgressDialog = ProgressDialog(requireActivity())
        mViewModel.mContext = requireActivity()
        initProgressDialog()
        initData()
        initListener()
    }
    fun initProgressDialog() {


        mViewModel.isShowLoadingLiveData().observe(requireActivity()) {
            if (it) {
                showLoading()
            } else {
                hideLoading()
            }
        }
    }
    fun showLoading() {
        if (mProgressDialog?.isShowing == false)
            mProgressDialog?.show()
    }

    fun hideLoading() {
        if (mProgressDialog?.isShowing == true)
            mProgressDialog?.dismiss()
    }
    abstract fun initData()
    fun initListener(){}
    fun Activity.hideSoftInput() = currentFocus?.let { (getSystemService(AppCompatActivity.INPUT_METHOD_SERVICE) as InputMethodManager).hideSoftInputFromWindow(it.windowToken, 0) }


}