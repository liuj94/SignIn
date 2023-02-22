package com.example.signin.base

import android.app.ProgressDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.viewbinding.ViewBinding
import com.dylanc.mmkv.MMKVOwner
import com.dylanc.viewbinding.base.FragmentBinding
import com.dylanc.viewbinding.base.FragmentBindingDelegate
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
         mProgressDialog = ProgressDialog(requireActivity())
        mViewModel.mContext = requireActivity()
        initData()
        initListener()
    }
    abstract fun initData()
    fun initListener(){}


}