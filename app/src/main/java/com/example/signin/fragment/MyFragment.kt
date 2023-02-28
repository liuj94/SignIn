package com.example.signin.fragment


import add
import android.os.Build
import android.view.View
import android.widget.TextView
import androidx.annotation.RequiresApi
import com.afollestad.materialdialogs.MaterialDialog
import com.afollestad.materialdialogs.customview.customView
import com.alibaba.fastjson.JSON
import com.bumptech.glide.Glide
import com.dylanc.longan.startActivity
import com.dylanc.longan.toast
import com.example.signin.*
import com.example.signin.adapter.IconFristPagerAdapter
import com.example.signin.adapter.MyCommonNavigatorAdapter2
import com.example.signin.base.BaseBindingFragment
import com.example.signin.base.BaseViewModel
import com.example.signin.bean.MeetingData
import com.example.signin.bean.User
import com.example.signin.databinding.FragMyBinding
import com.example.signin.net.RequestCallback
import com.lzy.okgo.OkGo
import com.lzy.okgo.model.Response
import net.lucode.hackware.magicindicator.ViewPagerHelper
import net.lucode.hackware.magicindicator.buildins.commonnavigator.CommonNavigator
import upFile
import java.io.File
import java.util.ArrayList


/**
 *   author : LiuJie
 *   date   : 2021/2/2513:36
 */
class MyFragment : BaseBindingFragment<FragMyBinding, BaseViewModel>() {

    companion object {
        fun newInstance(): MyFragment {
            return MyFragment()
        }
    }

    override fun getViewModel(): Class<BaseViewModel> = BaseViewModel::class.java
    var userData: User? = null

    @RequiresApi(Build.VERSION_CODES.M)
    override fun initData() {

        if (!kv.getString("userData", "").isNullOrEmpty()) {
            userData = JSON.parseObject(kv.getString("userData", ""), User::class.java)
            activity?.let {
                Glide.with(it).load(PageRoutes.BaseUrl + userData?.avatar).error(R.drawable.ov_999)
                    .into(binding.img)
            }
            binding.name.text = userData?.nickName
            binding.phone.text = userData?.phonenumber
//            00系统用户01举办方用户02分享商用户03小程序主账号04会议账号05站点账号06签到账号
            when(userData?.userType){
                "00"->{binding.type.text = "系统用户"}
                "01"->{binding.type.text = "举办方用户"}
                "02"->{binding.type.text = "分享商用户"}
                "03"->{binding.type.text = "主账号"}
                "04"->{binding.type.text = "会议账号"}
                "05"->{binding.type.text = "站点账号"}
                "06"->{binding.type.text = "签到账号"}
            }

        }
        binding.infoll.setOnClickListener {
           startActivity<UserSetActivity>()
        }
        binding.gjgy.setOnClickListener {
            com.dylanc.longan.startActivity<AboutActivity>()
        }
        binding.gjrlsz.setOnClickListener {
//            com.dylanc.longan.startActivity<AboutActivity>()
        }
        binding.gjzjms.setOnClickListener {
//            com.dylanc.longan.startActivity<AboutActivity>()
        }
        binding.gjtc.setOnClickListener {
            activity?.let { it1 ->
                MaterialDialog(it1).show {
                    customView(    //自定义弹窗
                        viewRes = R.layout.tc_logout,//自定义文件
                        dialogWrapContent = true,    //让自定义宽度生效
                        scrollable = true,            //让自定义宽高生效
                        noVerticalPadding = true    //让自定义高度生效
                    ).apply {
                        findViewById<TextView>(R.id.qr).setOnClickListener {
                            dismiss()
                            kv.remove("token")
                            kv.remove("userData")
                            com.dylanc.longan.startActivity<LoginActivity>()
                        }
                        findViewById<TextView>(R.id.qx).setOnClickListener {
                            dismiss()
                        }
                    }

                    cancelOnTouchOutside(false)    //点击外部不消失
                }
            }
        }

        getData()
        binding.img.setOnClickListener {
            activity?.let { it1 -> takePhotoDialog(it1) { submitUserAvatar(it) } }
        }

    }

    private fun initFistToolAdapter(pageIcon: List<MeetingData>) {

        val recommends: MutableList<String> = ArrayList()

        for (data in pageIcon) {
            recommends.add("")
        }
        if(pageIcon.size>0){
            binding.banner.visibility = View.VISIBLE
            binding.viewPager.adapter = IconFristPagerAdapter(childFragmentManager, pageIcon)
            val commonNavigator = CommonNavigator(activity)
            commonNavigator.isAdjustMode = false
            commonNavigator.adapter =
                MyCommonNavigatorAdapter2(activity, recommends, binding.viewPager, 15)
            binding.toolFristMagicIndicator.navigator = commonNavigator
            ViewPagerHelper.bind(binding.toolFristMagicIndicator, binding.viewPager)
            binding.viewPager.pageMargin = 30
        }else{
            binding.banner.visibility = View.GONE
        }



    }


    //totalAmount enterCount totalSignUpCount
    private fun getData() {

        OkGo.get<List<MeetingData>>(PageRoutes.Api_meetingList + "status=2")
            .tag(PageRoutes.Api_meetingList + "status=2")
            .headers("Authorization", kv.getString("token", ""))
            .execute(object : RequestCallback<List<MeetingData>>() {
                override fun onSuccessNullData() {
                    super.onSuccessNullData()

                }

                override fun onMySuccess(data: List<MeetingData>) {
                    super.onMySuccess(data)
                    initFistToolAdapter(data)

                }

                override fun onError(response: Response<List<MeetingData>>) {
                    super.onError(response)

                }

                override fun onFinish() {
                    super.onFinish()

                }


            })
    }


    var avatar = ""
    fun submitUserAvatar(file: File) {
        mViewModel.isShowLoading.value = true
        upFile(file, {
            avatar = it.fileName
            add(avatar, userData!!.name,
                {
                    userData?.avatar = avatar
                    kv.putString("userData", JSON.toJSONString(userData))
                    activity?.let {
                        Glide.with(it).load(PageRoutes.BaseUrl + avatar).error(R.drawable.ov_999)
                            .into(binding.img)
                    }
                },
                { mViewModel.isShowLoading.value = false
                    toast("头像修改失败")},
                { mViewModel.isShowLoading.value = false })
        }, {
            toast("图片上传失败")
            mViewModel.isShowLoading.value = false
        }, {})

    }

}
