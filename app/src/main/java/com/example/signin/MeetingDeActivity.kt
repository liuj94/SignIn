package com.example.signin


import android.util.Log
import android.view.View
import androidx.fragment.app.Fragment
import com.example.signin.adapter.MainViewPagerAdapter
import com.example.signin.base.BaseBindingActivity
import com.example.signin.base.BaseViewModel
import com.example.signin.databinding.ActMeetdaBinding
import com.example.signin.fragment.MettingDe1Fragment
import com.example.signin.fragment.MettingDe2Fragment
import com.example.signin.fragment.MettingDe3Fragment
import com.example.signin.fragment.MettingDe4Fragment


class MeetingDeActivity : BaseBindingActivity<ActMeetdaBinding, BaseViewModel>() {

    override fun getViewModel(): Class<BaseViewModel> = BaseViewModel::class.java


    var meetingId = ""
    var meetingName = ""
    var businessId = ""
    var userType = "00"
    override fun initData() {
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

        getFragmentLists()
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

    }

    fun getFragmentLists() {
        binding.mRb1.visibility = View.GONE
        if (userType.equals("01") || userType.equals("04")) {
            binding.mRb1.visibility = View.VISIBLE
            Log.d("ActivityBinding", " binding.mRb1.visibility = View.VISIBLE")
            fragments.add(MettingDe1Fragment())
            fragments.add(MettingDe2Fragment())
            fragments.add(MettingDe3Fragment())
            fragments.add(MettingDe4Fragment())
        } else {
            binding.mRb1.visibility = View.GONE
            fragments.add(MettingDe2Fragment())
            fragments.add(MettingDe3Fragment())
            fragments.add(MettingDe4Fragment())
        }

        initAdapter()


    }

    var fragments: MutableList<Fragment> = ArrayList<Fragment>()
    private fun initAdapter() {
        val mAdapter = MainViewPagerAdapter(supportFragmentManager, fragments)
        binding.mViewPager.adapter = mAdapter
        binding.mViewPager.offscreenPageLimit = 1
        initListener()
        binding.mViewPager.currentItem = 0
        if (fragments.size == 4) {
            binding.title.text = "数据统计"
            binding.mRb1.isChecked = true
        } else {
            binding.title.text = "选签到点"
            binding.mRb2.isChecked = true
        }

    }

    override fun initListener() {
        binding.mRg.setOnCheckedChangeListener { _, checkedId ->
            when (checkedId) {
                R.id.mRb1 -> {
                    binding.title.text = "数据统计"
                    binding.mViewPager?.currentItem = 0

                }

                R.id.mRb2 -> {
                    binding.title.text = "选签到点"
                    if (fragments.size == 3) {
                        binding.mViewPager?.currentItem = 0
                    } else {
                        binding.mViewPager?.currentItem = 1
                    }


                }

                R.id.mRb3 -> {
                    binding.title.text = "参会名单"
                    if (fragments.size == 3) {
                        binding.mViewPager?.currentItem = 1
                    } else {
                        binding.mViewPager?.currentItem = 2
                    }


                }

                R.id.mRb4 -> {
                    binding.title.text = "实时对讲"
                    if (fragments.size == 3) {
                        binding.mViewPager?.currentItem = 2
                    } else {
                        binding.mViewPager?.currentItem = 3
                    }


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