package com.example.signin.fragment


import android.os.Build
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.databinding.DataBindingUtil
import com.afollestad.materialdialogs.MaterialDialog
import com.afollestad.materialdialogs.customview.customView
import com.alibaba.fastjson.JSON
import com.bumptech.glide.Glide
import com.dylanc.longan.toast
import com.example.signin.AboutActivity
import com.example.signin.LoginActivity
import com.example.signin.PageRoutes
import com.example.signin.R
import com.example.signin.base.BaseBindingFragment
import com.example.signin.base.BaseViewModel
import com.example.signin.bean.MeetingData
import com.example.signin.bean.User
import com.example.signin.databinding.FragMyBinding
import com.example.signin.net.RequestCallback
import com.lzy.okgo.OkGo
import com.lzy.okgo.model.Response
import com.youth.banner.holder.BannerImageHolder
import com.youth.banner.indicator.RectangleIndicator
import com.youth.banner.listener.OnBannerListener


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


    @RequiresApi(Build.VERSION_CODES.M)
    override fun initData() {

        if (!kv.getString("userData", "").isNullOrEmpty()) {
            var data = JSON.parseObject(kv.getString("userData", ""), User::class.java)
            activity?.let {
                Glide.with(it).load(PageRoutes.BaseUrl + data.avatar).into(binding.img)
            }
            binding.name.text = data.nickName
            binding.phone.text = data.phonenumber
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


    }

    private fun initView() {

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


                }

                override fun onError(response: Response<List<MeetingData>>) {
                    super.onError(response)
                    toast(response.message())
                }

                override fun onFinish() {
                    super.onFinish()

                }


            })
    }

}
