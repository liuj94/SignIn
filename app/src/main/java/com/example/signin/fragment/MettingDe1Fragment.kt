package com.example.signin.fragment


import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.LinearLayoutManager
import com.alibaba.fastjson.JSON
import com.dylanc.longan.toast
import com.example.signin.PageRoutes
import com.example.signin.base.BaseBindingFragment
import com.example.signin.base.BaseViewModel
import com.example.signin.bean.MeetingStatisticsData
import com.example.signin.bean.SiginUpListData
import com.example.signin.databinding.FragMeetingde1Binding
import com.example.signin.adapter.FMeetingDeListAdapter
import com.example.signin.bean.SiginUpListModel
import com.example.signin.net.RequestCallback
import com.lzy.okgo.OkGo
import com.lzy.okgo.model.Response

/**
 *   author : LiuJie
 *   date   : 2021/2/2513:36
 */
class MettingDe1Fragment : BaseBindingFragment<FragMeetingde1Binding, BaseViewModel>() {
    companion object {
        fun newInstance(meetingid: String,meetingName: String): MettingDe1Fragment {
            val args = Bundle()
            args.putString("meetingid", meetingid)
            args.putString("meetingName", meetingName)
            val fragment = MettingDe1Fragment()
            fragment.arguments = args
            return fragment
        }
    }

    override fun getViewModel(): Class<BaseViewModel> = BaseViewModel::class.java


    private var isVisibleFirst: Boolean = true
    private var adapter: FMeetingDeListAdapter? = null
    private var list: MutableList<SiginUpListData> = ArrayList()
    var meetingid: String? = ""
    var meetingName: String? = ""

    @RequiresApi(Build.VERSION_CODES.M)
    override fun initData() {
        meetingid = arguments?.getString("meetingid", "")
        meetingName = arguments?.getString("meetingName", "")

//            setStatusBarHeight(toolbarView)
            isVisibleFirst = false

            binding.recyclerview.layoutManager = LinearLayoutManager(activity)
            adapter = FMeetingDeListAdapter().apply {
                submitList(list)
            }
            binding.recyclerview.adapter = adapter
            binding.name.text = meetingName
            getData()
            getList()


    }

    private fun getList() {
        if(!kv.getString("SiginUpListModel","").isNullOrEmpty()){
           var data = JSON.parseObject(kv.getString("SiginUpListModel",""), SiginUpListModel::class.java)
            list.clear()
            list.addAll(data.list)
            adapter?.notifyDataSetChanged()
        }
//        OkGo.get<List<SiginUpListData>>(PageRoutes.Api_meeting_sign_up_app_list + meetingid)
//            .tag(PageRoutes.Api_meeting_sign_up_app_list + meetingid)
//            .headers("Authorization", kv.getString("token", ""))
//            .execute(object : RequestCallback<List<SiginUpListData>>() {
//                override fun onSuccessNullData() {
//                    super.onSuccessNullData()
//
//                }
//
//                override fun onMySuccess(data: List<SiginUpListData>) {
//                    super.onMySuccess(data)
//                    list.clear()
//                    list.addAll(data)
//                    adapter?.notifyDataSetChanged()
//
//                }
//
//                override fun onError(response: Response<List<SiginUpListData>>) {
//                    super.onError(response)
//                    toast(response.message())
//                }
//
//                override fun onFinish() {
//                    super.onFinish()
//
//                }
//
//
//            })
    }

    private fun getData() {

        OkGo.get<MeetingStatisticsData>(PageRoutes.Api_meeting_statistics + meetingid + "?id=" + meetingid)
            .tag(PageRoutes.Api_meeting_statistics + meetingid + "?id=" + meetingid)
            .headers("Authorization", kv.getString("token", ""))
            .execute(object : RequestCallback<MeetingStatisticsData>() {
                override fun onSuccessNullData() {
                    super.onSuccessNullData()

                }

                override fun onMySuccess(data: MeetingStatisticsData) {
                    super.onMySuccess(data)
                    binding.num1.text = data.browseCount
                    binding.num2.text = data.userMeetingCount
                    binding.num3.text = data.totalAmount
                    binding.num4.text = "" + data.todayInsertUserCount
                    binding.num5.text = "" + data.todayBeReviewedCount
                    binding.num6.text = "" + data.leaveCount
                    setNumData(
                        data.todayInsertUserCount,
                        data.yesterdayInsertUserCount,
                        binding.num7
                    )
                    setNumData(data.todayBeReviewedCount, data.todayBeReviewedCount, binding.num8)
                    setNumData(data.leaveCount, data.yesterdayLeaveCount, binding.num9)


                }

                override fun onError(response: Response<MeetingStatisticsData>) {
                    super.onError(response)
                    toast(response.message())
                }

                override fun onFinish() {
                    super.onFinish()

                }


            })
    }

    fun setNumData(todayInsertUserCount: Int, yesterdayInsertUserCount: Int, tv: TextView) {
        if (todayInsertUserCount > yesterdayInsertUserCount) {
            tv.setTextColor(Color.parseColor("#ff43cf7c"))
        } else if (todayInsertUserCount < yesterdayInsertUserCount) {
            tv.setTextColor(Color.parseColor("#ffd43030"))
        } else {
            tv.setTextColor(Color.parseColor("#ff7d8592"))
        }
        var num = todayInsertUserCount - yesterdayInsertUserCount
        tv.text = " /"
        if (num > 0) {
            tv.text = " " + num + "↑"
        } else if (num < 0) {
            tv.text = " " + num + "↓"
        }
    }

}
