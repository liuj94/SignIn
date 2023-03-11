package com.example.signin


import android.content.Intent
import android.view.View
import androidx.fragment.app.Fragment
import com.alibaba.fastjson.JSON
import com.dylanc.longan.addStatusBarHeightToMarginTop
import com.example.signin.adapter.MainViewPagerAdapter
import com.example.signin.base.BaseBindingActivity
import com.example.signin.base.BaseViewModel
import com.example.signin.bean.SiginUpListData
import com.example.signin.bean.SiginUpListModel
import com.example.signin.bean.User
import com.example.signin.databinding.ActMeetdaBinding
import com.example.signin.fragment.MettingDe1Fragment
import com.example.signin.fragment.MettingDe2Fragment
import com.example.signin.fragment.MettingDe3Fragment
import com.example.signin.fragment.MettingDe4Fragment

import com.example.signin.net.RequestCallback
import com.lzy.okgo.OkGo
import com.lzy.okgo.model.Response


class MeetingDeActivity : BaseBindingActivity<ActMeetdaBinding, BaseViewModel>() {

//    override fun initTranslucentStatus() {
//        super.initTranslucentStatus()
//        immerseStatusBar()
//
//    }

    override fun getViewModel(): Class<BaseViewModel> = BaseViewModel::class.java


var meetingId = ""
var meetingName = ""
    override fun initData() {
        binding.titlell.addStatusBarHeightToMarginTop()
        intent.getStringExtra("meetingId")?.let {
            meetingId = it
        }
        intent.getStringExtra("meetingName")?.let {
            meetingName = it
        }
        mViewModel.isShowLoading.value = true
        getData()
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
    private fun getData() {

        OkGo.get<List<SiginUpListData>>(PageRoutes.Api_meeting_sign_up_app_list + meetingId)
            .tag(PageRoutes.Api_meeting_sign_up_app_list + meetingId)
            .headers("Authorization", kv.getString("token", ""))
            .execute(object : RequestCallback<List<SiginUpListData>>() {
                override fun onSuccessNullData() {
                    super.onSuccessNullData()
                    getFragmentLists()
                }

                override fun onMySuccess(data: List<SiginUpListData>) {
                    super.onMySuccess(data)
                   var list = SiginUpListModel()
                    list.list = data
                    kv.putString("SiginUpListModel", JSON.toJSONString(list))
                    getFragmentLists()
                }

                override fun onError(response: Response<List<SiginUpListData>>) {
                    super.onError(response)

                    getFragmentLists()
                }

                override fun onFinish() {
                    super.onFinish()
                    mViewModel.isShowLoading.value = false
                }


            })
    }

    fun getFragmentLists() {
        val fragmentLists: MutableList<Fragment> = ArrayList<Fragment>()
        binding.mRb1.visibility = View.GONE
        if (!kv.getString("userData", "").isNullOrEmpty()) {
           var userData = JSON.parseObject(kv.getString("userData", ""), User::class.java)
            userData?.let {
                if (it.userType.equals("01")||it.userType.equals("04")){
                    binding.mRb1.visibility = View.VISIBLE
                    fragmentLists.add(MettingDe1Fragment.newInstance(meetingId,meetingName))
                }
            }


        }

        fragmentLists.add(MettingDe2Fragment.newInstance(meetingId))
        fragmentLists.add(MettingDe3Fragment.newInstance(meetingId))
        fragmentLists.add(MettingDe4Fragment.newInstance(meetingId))

        initAdapter(fragmentLists)

    }


    private fun initAdapter(fragments: MutableList<Fragment>) {
        val mAdapter = MainViewPagerAdapter(supportFragmentManager, fragments)
        binding.mViewPager.adapter = mAdapter
        binding.mViewPager.offscreenPageLimit = 4
        initListener()



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
                    binding.mViewPager?.currentItem = 1


                }
                R.id.mRb3 -> {
                    binding.title.text = "参会名单"
                    binding.mViewPager?.currentItem = 2


                }
                R.id.mRb4 -> {
                    binding.title.text = "实时对讲"
                    binding.mViewPager?.currentItem = 3


                }


            }
        }



    }
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
    }

}