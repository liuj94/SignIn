package com.example.signin


import android.content.Intent
import android.view.View
import androidx.fragment.app.Fragment
import com.alibaba.fastjson.JSON
import com.dylanc.longan.addStatusBarHeightToMarginTop
import com.example.signin.adapter.MainViewPagerAdapter
import com.example.signin.base.BaseBindingActivity
import com.example.signin.base.BaseViewModel
import com.example.signin.bean.CustomUpdateParser
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
import com.xuexiang.xupdate.XUpdate


class MeetingDeActivity : BaseBindingActivity<ActMeetdaBinding, BaseViewModel>() {

//    override fun initTranslucentStatus() {
//        super.initTranslucentStatus()
//        immerseStatusBar()
//
//    }

    override fun getViewModel(): Class<BaseViewModel> = BaseViewModel::class.java


var meetingId = ""
var meetingName = ""
var businessId = ""
    override fun initData() {
        binding.titlell.addStatusBarHeightToMarginTop()
        intent.getStringExtra("meetingId")?.let {
            meetingId = it
        }
        intent.getStringExtra("meetingName")?.let {
            meetingName = it
        }
        intent.getStringExtra("businessId")?.let {
            businessId = it
        }
//        mViewModel.isShowLoading.value = true
//        getData()
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
        LiveDataBus.get().with("JWebSocketClientlocation", String::class.java)
            .observeForever {
               try {
                   if(AppManager.getAppManager().activityInstanceIsLive(MeetingDeActivity@this)){
                       getData2()
                   }
               }catch (e:java.lang.Exception){}


            }
    }
    private fun getData() {

        OkGo.get<List<SiginUpListData>>(PageRoutes.Api_meeting_sign_up_app_list + meetingId)
            .tag(PageRoutes.Api_meeting_sign_up_app_list + meetingId+"d1")
            .headers("Authorization", kv.getString("token", ""))
            .execute(object : RequestCallback<List<SiginUpListData>>() {
                override fun onSuccessNullData() {
                    super.onSuccessNullData()

                }

                override fun onMySuccess(data: List<SiginUpListData>) {
                    super.onMySuccess(data)
                   var list = SiginUpListModel()
                    list.list = data
                    kv.putString("SiginUpListModel", JSON.toJSONString(list))

                }

                override fun onError(response: Response<List<SiginUpListData>>) {
                    super.onError(response)


                }

                override fun onFinish() {
                    super.onFinish()
                    mViewModel.isShowLoading.value = false
                    getFragmentLists()
                }


            })
    }
    private fun getData2() {

        OkGo.get<List<SiginUpListData>>(PageRoutes.Api_meeting_sign_up_app_list + meetingId)
            .tag(PageRoutes.Api_meeting_sign_up_app_list + meetingId)
            .headers("Authorization", kv.getString("token", ""))
            .execute(object : RequestCallback<List<SiginUpListData>>() {
                override fun onSuccessNullData() {
                    super.onSuccessNullData()

                }

                override fun onMySuccess(data: List<SiginUpListData>) {
                    super.onMySuccess(data)
                    var list = SiginUpListModel()
                    list.list = data
                    kv.putString("SiginUpListModel", JSON.toJSONString(list))

                }

                override fun onError(response: Response<List<SiginUpListData>>) {
                    super.onError(response)


                }

                override fun onFinish() {
                    super.onFinish()


                }


            })
    }
    fun getFragmentLists() {
//        val fragmentLists: MutableList<Fragment> = ArrayList<Fragment>()
        binding.mRb1.visibility = View.GONE

        if (!kv.getString("userData", "").isNullOrEmpty()) {
           var userData = JSON.parseObject(kv.getString("userData", ""), User::class.java)
            userData?.let {
                if (it.userType.equals("01")||it.userType.equals("04")){
//                if (it.userType.equals("00")||it.userType.equals("00")){
                    binding.mRb1.visibility = View.VISIBLE
                    fragments.add(MettingDe1Fragment.newInstance(meetingId,meetingName))
                    fragments.add(MettingDe2Fragment.newInstance(meetingId))
                    fragments.add(MettingDe3Fragment.newInstance(meetingId))
                    fragments.add(MettingDe4Fragment.newInstance(meetingId,businessId))
                    initAdapter()
                }else{
                    binding.mRb1.visibility = View.GONE
                    fragments.add(MettingDe2Fragment.newInstance(meetingId))
                    fragments.add(MettingDe3Fragment.newInstance(meetingId))
                    fragments.add(MettingDe4Fragment.newInstance(meetingId,businessId))
                    initAdapter()
                }


            }


        }




    }

var fragments: MutableList<Fragment> = ArrayList<Fragment>()
    private fun initAdapter() {
        val mAdapter = MainViewPagerAdapter(supportFragmentManager, fragments)
        binding.mViewPager.adapter = mAdapter
        binding.mViewPager.offscreenPageLimit = 1
        initListener()
        binding.mViewPager.currentItem = 0
//        if(index==0){
//            binding.title.text = "数据统计"
//            binding.mRb1.isChecked = true
//        }else{
//            binding.title.text = "选签到点"
//            binding.mRb2.isChecked = true
//        }

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
                    if(fragments.size==3){
                        binding.mViewPager?.currentItem = 0
                    }else{
                        binding.mViewPager?.currentItem = 1
                    }



                }
                R.id.mRb3 -> {
                    binding.title.text = "参会名单"
                    if(fragments.size==3){
                        binding.mViewPager?.currentItem = 1
                    }else{
                        binding.mViewPager?.currentItem = 2
                    }


                }
                R.id.mRb4 -> {
                    binding.title.text = "实时对讲"
                    if(fragments.size==3){
                        binding.mViewPager?.currentItem = 2
                    }else{
                        binding.mViewPager?.currentItem = 3
                    }


                }


            }
        }



    }


    override fun onResume() {
        super.onResume()
        XUpdate.newBuild(this)
            .updateUrl(PageRoutes.Api_appVersion)
            .updateParser( CustomUpdateParser(this))
            .update();
    }

}