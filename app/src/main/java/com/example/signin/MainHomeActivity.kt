package com.example.signin.mvvm.ui.activity

import androidx.fragment.app.Fragment
import com.dylanc.longan.toast
import com.example.signin.R

import com.example.signin.base.BaseBindingActivity


import com.example.signin.databinding.ActivityMainHomeBinding
import com.example.signin.mvvm.ui.adapter.MainViewPagerAdapter
import com.example.signin.mvvm.vm.MainHomeAVM
import com.example.signin.setTvGradientColor


class MainHomeActivity : BaseBindingActivity<ActivityMainHomeBinding, MainHomeAVM>() {


    override fun getViewModel(): Class<MainHomeAVM> = MainHomeAVM::class.java

//    override fun initView(savedInstanceState: Bundle?) = R.layout.activity_main_home


    override fun initData() {

    }




    private fun initAdapter(fragments: MutableList<Fragment>) {
        val mAdapter = MainViewPagerAdapter(supportFragmentManager, fragments)
        binding.mViewPager.adapter = mAdapter
        binding.mViewPager.offscreenPageLimit = 4
        initListener()
        setTvGradientColor( binding.mMainHomeRb, "#FF4c93fd", "#FF4ca6fc")
        setTvGradientColor( binding.mMainMineRb, "#FF999999", "#FF999999")


    }

    override fun initListener() {
        binding.mMainBottomRg.setOnCheckedChangeListener { _, checkedId ->
            when (checkedId) {
                R.id.mMainHomeRb -> {
                    binding.mMainHomeRb
                    binding.mMainMineRb
                    binding.mViewPager?.currentItem = 0

                }
                R.id.mMainMineRb -> {
                    binding.mMainHomeRb
                    binding.mMainMineRb
                    binding.mViewPager?.currentItem = 1


                }


            }
        }



    }



    private var exitTime: Long = 0
    override fun onBackPressed() {
        if (System.currentTimeMillis() - exitTime > 2000) {
            toast("再按一次返回退出程序")
            exitTime = System.currentTimeMillis()
        } else {
            System.exit(0);
        }
    }




}