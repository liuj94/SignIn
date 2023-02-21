package com.example.signin.fragment


import android.os.Build
import androidx.annotation.RequiresApi
import com.example.signin.base.BaseBindingFragment


import com.example.signin.base.BaseViewModel
import com.example.signin.databinding.FragHomeBinding
import com.example.signin.databinding.FragMyBinding

/**
 *   author : LiuJie
 *   date   : 2021/2/2513:36
 */
class   MyFragment : BaseBindingFragment<FragMyBinding, BaseViewModel>() {

    companion object {
        fun newInstance(): MyFragment {
            return MyFragment()
        }
    }

    override fun getViewModel(): Class<BaseViewModel> = BaseViewModel::class.java



    private var isVisibleFirst: Boolean = true


    @RequiresApi(Build.VERSION_CODES.M)
    override fun initData() {
        if ( isVisibleFirst) {
//            setStatusBarHeight(toolbarView)
            isVisibleFirst = false
            //更新界面数据，如果数据还在下载中，就显示加载框

    }}

}
