package com.example.signin


import androidx.fragment.app.Fragment
import com.dylanc.longan.toast
import com.example.signin.adapter.MainViewPagerAdapter

import com.example.signin.base.BaseBindingActivity
import com.example.signin.databinding.ActivityMainBinding
import com.example.signin.fragment.HomeMainFragment
import com.example.signin.fragment.MyFragment


import com.example.signin.mvvm.vm.MainHomeAVM



class MainHomeActivity : BaseBindingActivity<ActivityMainBinding, MainHomeAVM>() {


    override fun getViewModel(): Class<MainHomeAVM> = MainHomeAVM::class.java



    override fun initData() {
        getFragmentLists()

    }

    var homeFragment: HomeMainFragment? = null
    var myFragment: MyFragment? = null
    fun getFragmentLists() {
        val fragmentLists: MutableList<Fragment> = ArrayList<Fragment>()
        homeFragment = HomeMainFragment()
        fragmentLists.add(homeFragment!!)
        myFragment = MyFragment()
        fragmentLists.add(myFragment!!)

        initAdapter(fragmentLists)

    }


    private fun initAdapter(fragments: MutableList<Fragment>) {
        val mAdapter = MainViewPagerAdapter(supportFragmentManager, fragments)
        binding.mViewPager.adapter = mAdapter
        binding.mViewPager.offscreenPageLimit = 4
        initListener()



    }

    override fun initListener() {
        binding.mMainBottomRg.setOnCheckedChangeListener { _, checkedId ->
            when (checkedId) {
                R.id.mMainHomeRb -> {

                    binding.mViewPager?.currentItem = 0

                }
                R.id.mMainMineRb -> {
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