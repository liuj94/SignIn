package com.example.signin


import android.util.Log
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import com.example.signin.adapter.MainViewPagerAdapter
import com.example.signin.base.BaseBindingActivity
import com.example.signin.base.BaseViewModel
import com.example.signin.databinding.ActMeetdaBinding
import com.example.signin.fragment.MettingDe1Fragment
import com.example.signin.fragment.MettingDe2Fragment
import com.example.signin.fragment.MettingDe3Fragment
import com.example.signin.fragment.MettingDe4Fragment


class MeetingDe2Activity : BaseBindingActivity<ActMeetdaBinding, BaseViewModel>() {

    override fun getViewModel(): Class<BaseViewModel> = BaseViewModel::class.java


    var meetingId = ""
    var meetingName = ""
    var businessId = ""
    var userType = "00"
    override fun initData() {
        Log.d("ActivityBinding", " MeetingDeActivity")
//        binding.titlell.addStatusBarHeightToMarginTop()
        intent.getStringExtra("meetingId")?.let {
            meetingId = it
        }
        intent.getStringExtra("meetingName")?.let {
            meetingName = it
        }
        intent.getStringExtra("businessId")?.let {
            businessId = it
        }
        intent.getStringExtra("userType")?.let {
            userType = it
        }

//        getFragmentLists()
        LiveDataBus.get().with("selectLlVISIBLE", String::class.java)
            .observeForever {
                binding.zhez.visibility = View.VISIBLE
            }
        LiveDataBus.get().with("selectLlGONE", String::class.java)
            .observeForever {
                binding.zhez.visibility = View.GONE
            }
        binding.zhez.setOnClickListener {

        }
        if (userType.equals("01") || userType.equals("04")) {
            binding.mRb1.visibility = View.VISIBLE
             binding.title.text = "数据统计"
            binding.mRb1.isChecked = true
//            checkFragment4()
//            checkFragment3()
//            checkFragment2()
            checkFragment1()
        }else {
            binding.mRb1.visibility = View.GONE
                        binding.title.text = "选签到点"
            binding.mRb2.isChecked = true
//            checkFragment4()
//            checkFragment3()
            checkFragment2()
        }

    }
    //第一个fragment
    private var mettingDe1Fragment: MettingDe1Fragment? = null
    //第二个fragment
    private var mettingDe3Fragment: MettingDe3Fragment? = null
    private var mettingDe2Fragment: MettingDe2Fragment? = null
    private var mettingDe4Fragment: MettingDe4Fragment? = null

    private val mettingDe1FragmentTag = "MettingDe1Fragment"
    private val mettingDe2FragmentTag = "MettingDe2Fragment"
    private val mettingDe3FragmentTag = "MettingDe2Fragment"
    private val mettingDe4FragmentTag = "MettingDe2Fragment"
    /**
     * 显示 现场检查 fragment
     */
    fun checkFragment1() {
        val transaction: FragmentTransaction = supportFragmentManager.beginTransaction()
        //add,初始化fragment并添加到事务中，如果为null就new一个
        if (mettingDe1Fragment == null) {
            mettingDe1Fragment = MettingDe1Fragment()
            transaction.add(R.id.navi_home, mettingDe1Fragment!!, mettingDe1FragmentTag)
        }
        hideFragment(transaction)
        transaction.show(mettingDe1Fragment!!)
        //提交事务
        transaction.commit()
    }
    fun checkFragment2() {
        val transaction: FragmentTransaction = supportFragmentManager.beginTransaction()
        //add,初始化fragment并添加到事务中，如果为null就new一个
        if (mettingDe2Fragment == null) {
            mettingDe2Fragment = MettingDe2Fragment()
            transaction.add(R.id.navi_home, mettingDe2Fragment!!, mettingDe2FragmentTag)
        }
        hideFragment(transaction)
        transaction.show(mettingDe2Fragment!!)
        //提交事务
        transaction.commit()
    }
    fun checkFragment3() {
        val transaction: FragmentTransaction = supportFragmentManager.beginTransaction()
        //add,初始化fragment并添加到事务中，如果为null就new一个
        if (mettingDe3Fragment == null) {
            mettingDe3Fragment = MettingDe3Fragment()
            transaction.add(R.id.navi_home, mettingDe3Fragment!!, mettingDe3FragmentTag)
        }
        hideFragment(transaction)
        transaction.show(mettingDe3Fragment!!)
        //提交事务
        transaction.commit()
    }
    fun checkFragment4() {
        val transaction: FragmentTransaction = supportFragmentManager.beginTransaction()
        //add,初始化fragment并添加到事务中，如果为null就new一个
        if (mettingDe4Fragment == null) {
            mettingDe4Fragment = MettingDe4Fragment()
            transaction.add(R.id.navi_home, mettingDe4Fragment!!, mettingDe4FragmentTag)
        }
        hideFragment(transaction)
        transaction.show(mettingDe4Fragment!!)
        //提交事务
        transaction.commit()
    }
    /**
     * 隐藏所有的fragment
     * @param transaction FragmentTransaction
     */
    private fun hideFragment(transaction: FragmentTransaction) {
        if (mettingDe1Fragment != null) {
            transaction.hide(mettingDe1Fragment!!)
        }

        if (mettingDe2Fragment != null) {
            transaction.hide(mettingDe2Fragment!!)
        }
        if (mettingDe3Fragment != null) {
            transaction.hide(mettingDe3Fragment!!)
        }
        if (mettingDe4Fragment != null) {
            transaction.hide(mettingDe4Fragment!!)
        }

    }

//    fun getFragmentLists() {
//        binding.mRb1.visibility = View.GONE
//        if (userType.equals("01") || userType.equals("04")) {
//            binding.mRb1.visibility = View.VISIBLE
//            Log.d("ActivityBinding", " binding.mRb1.visibility = View.VISIBLE")
//            fragments.add(MettingDe1Fragment())
//            fragments.add(MettingDe2Fragment())
//            fragments.add(MettingDe3Fragment())
//            fragments.add(MettingDe4Fragment())
//        } else {
//            binding.mRb1.visibility = View.GONE
//            fragments.add(MettingDe2Fragment())
//            fragments.add(MettingDe3Fragment())
//            fragments.add(MettingDe4Fragment())
//        }
//
//        initAdapter()
//
//
//    }

//    var fragments: MutableList<Fragment> = ArrayList<Fragment>()
//    private fun initAdapter() {
//        val mAdapter = MainViewPagerAdapter(supportFragmentManager, fragments)
//        binding.mViewPager.adapter = mAdapter
//        binding.mViewPager.offscreenPageLimit = 1
//        initListener()
//        binding.mViewPager.currentItem = 0
//        if (fragments.size == 4) {
//            binding.title.text = "数据统计"
//            binding.mRb1.isChecked = true
//        } else {
//            binding.title.text = "选签到点"
//            binding.mRb2.isChecked = true
//        }
//
//    }

    override fun initListener() {
        binding.mRg.setOnCheckedChangeListener { _, checkedId ->
            when (checkedId) {
                R.id.mRb1 -> {
                    binding.title.text = "数据统计"
//                    binding.mViewPager?.currentItem = 0
                    checkFragment1()
                }

                R.id.mRb2 -> {
                    binding.title.text = "选签到点"
//                    if (fragments.size == 3) {
//                        binding.mViewPager?.currentItem = 0
//                    } else {
//                        binding.mViewPager?.currentItem = 1
//                    }
                    checkFragment2()

                }

                R.id.mRb3 -> {
                    binding.title.text = "参会名单"
//                    if (fragments.size == 3) {
//                        binding.mViewPager?.currentItem = 1
//                    } else {
//                        binding.mViewPager?.currentItem = 2
//                    }
                    checkFragment3()

                }

                R.id.mRb4 -> {
                    binding.title.text = "实时对讲"
//                    if (fragments.size == 3) {
//                        binding.mViewPager?.currentItem = 2
//                    } else {
//                        binding.mViewPager?.currentItem = 3
//                    }
                    checkFragment4()

                }


            }
        }


    }


    override fun onResume() {
        super.onResume()
//        XUpdate.newBuild(this)
//            .updateUrl(PageRoutes.Api_appVersion)
//            .updateParser( CustomUpdateParser(this))
//            .update();
    }

}