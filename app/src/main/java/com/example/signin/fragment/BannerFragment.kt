package com.example.signin.fragment


import android.os.Build
import android.os.Bundle
import androidx.annotation.RequiresApi
import com.alibaba.fastjson.JSON
import com.example.signin.base.BaseBindingFragment
import com.example.signin.base.BaseViewModel
import com.example.signin.bean.MeetingData
import com.example.signin.databinding.ItemBannnerBinding


/**
 *   author : LiuJie
 *   date   : 2021/2/2513:36
 */
class BannerFragment : BaseBindingFragment<ItemBannnerBinding, BaseViewModel>() {

    companion object {
        fun newInstance(pageIcons: MeetingData): BannerFragment {


            val args = Bundle()
            args.putString("data", JSON.toJSONString(pageIcons))
            val fragment = BannerFragment()
            fragment.arguments = args
            return fragment
        }
    }

    override fun getViewModel(): Class<BaseViewModel> = BaseViewModel::class.java


    @RequiresApi(Build.VERSION_CODES.M)
    override fun initData() {
        val adListDataString: String? = requireArguments().getString("data")
        var data = JSON.parseObject(adListDataString, MeetingData::class.java)
//    //totalAmount enterCount totalSignUpCount
        binding.name.text = data.name
        binding.num1.text = ""+data.browseCount
        binding.num2.text = ""+data.totalSignUpCount
//        binding.num2.text = ""+data.userMeetingCount
        binding.num3.text = ""+data.totalAmount


    }


}
