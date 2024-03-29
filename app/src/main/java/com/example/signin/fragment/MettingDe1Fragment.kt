package com.example.signin.fragment


import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.LinearLayoutManager
import com.alibaba.fastjson.JSON
import com.example.signin.PageRoutes
import com.example.signin.base.BaseBindingFragment
import com.example.signin.base.BaseViewModel
import com.example.signin.bean.MeetingStatisticsData
import com.example.signin.bean.SiginUpListData
import com.example.signin.databinding.FragMeetingde1Binding
import com.example.signin.adapter.FMeetingDeListAdapter
import com.example.signin.bean.SiginUpListModel
import com.example.signin.bean.User
import com.example.signin.net.RequestCallback
import com.jeremyliao.liveeventbus.LiveEventBus
import com.lzy.okgo.OkGo
import com.lzy.okgo.model.Response
import java.math.BigDecimal
import java.text.DecimalFormat

/**
 *   author : LiuJie
 *   date   : 2021/2/2513:36
 */
class MettingDe1Fragment : BaseBindingFragment<FragMeetingde1Binding, BaseViewModel>() {
//    companion object {
//        fun newInstance(meetingid: String, meetingName: String): MettingDe1Fragment {
//            val args = Bundle()
//            args.putString("meetingid", meetingid)
//            args.putString("meetingName", meetingName)
//            val fragment = MettingDe1Fragment()
//            fragment.arguments = args
//            return fragment
//        }
//    }

    override fun getViewModel(): Class<BaseViewModel> = BaseViewModel::class.java


    private var adapter: FMeetingDeListAdapter? = null
    private var list: MutableList<SiginUpListData> = ArrayList()
    var meetingid: String? = ""
    var meetingName: String? = ""

    var isShow: Boolean = false
    override fun onDestroyView() {
        super.onDestroyView()
        isShow = false
        Log.d("MettingDe2Fragment","MettingDe1Fragmenton==onDestroyView()")
    }
    @RequiresApi(Build.VERSION_CODES.M)
    override fun initData() {
        Log.d("hhhhhhhhhhhhhhhhhhh", "gotogotogotogoto结束")
        isShow = true
//        meetingid = arguments?.getString("meetingid", "")
        meetingid =kv.getString("meetingid", "")
//        meetingName = arguments?.getString("meetingName", "")
        meetingName = kv.getString("meetingName", "")
        binding.recyclerview.layoutManager = LinearLayoutManager(activity)
        adapter = FMeetingDeListAdapter().apply {
            submitList(list)
        }
        binding.recyclerview.adapter = adapter
        binding.name.text = meetingName
        mViewModel.isShowLoading.value = true

        binding.refresh.setEnableLoadMore(false)
        binding.refresh.setOnRefreshListener {
            getData()
            getList()
        }

        getData()
        getList()
        LiveEventBus
            .get<String>("JWebSocketClientlocation", String::class.java)
            .observe(this) {
//        LiveDataBus.get().with("JWebSocketClientlocation", String::class.java)
//            .observeForever {
                try {
                    if (isShow) {
                        if (!kv.getString("userData", "").isNullOrEmpty()) {
                            var userData = JSON.parseObject(kv.getString("userData", ""), User::class.java)
                            userData?.let {
                                if (it.userType.equals("01")||it.userType.equals("04")){
                                    getData()
                                    getList()
                                }
                            }}

                    }
                } catch (e: java.lang.Exception) {
                }


            }

    }

    private fun getList() {
        if (!kv.getString("SiginUpListModelmeetingId"+meetingid, "").isNullOrEmpty()) {
            var data =
                JSON.parseObject(kv.getString("SiginUpListModelmeetingId"+meetingid, ""), SiginUpListModel::class.java)
            try {
                list.clear()
                list.addAll(data.list)
                adapter?.notifyDataSetChanged()
            } catch (e: java.lang.Exception) {
            }
        }else{
        Log.d("getList","接口开始调用===up/app/list")
        mViewModel.isShowLoading.value = true
        OkGo.get<List<SiginUpListData>>(PageRoutes.Api_meeting_sign_up_app_list + meetingid)
            .tag(PageRoutes.Api_meeting_sign_up_app_list + meetingid+"f1"+System.currentTimeMillis())
            .headers("Authorization", kv.getString("token", ""))
            .execute(object : RequestCallback<List<SiginUpListData>>() {
                override fun onSuccessNullData() {
                    super.onSuccessNullData()

                }

                override fun onMySuccess(data: List<SiginUpListData>) {
                    super.onMySuccess(data)
                    var allList = SiginUpListModel()
                    allList.list = data
                    kv.putString("SiginUpListModelmeetingId"+meetingid, JSON.toJSONString(allList))
                    if(!isShow){
                        return
                    }
                    try {

                        list.clear()
                        list.addAll(data)
                        adapter?.notifyDataSetChanged()
                    } catch (e: java.lang.Exception) {
                    }

                }

                override fun onError(response: Response<List<SiginUpListData>>) {
                    super.onError(response)


                }

                override fun onFinish() {
                    super.onFinish()
                    if(!isShow){
                        return
                    }
                    try {
                        mViewModel.isShowLoading.value = false
                        binding.refresh.finishRefresh()
                    }catch (e:Exception){}


                }


            })

         }

    }

     fun getData() {

        OkGo.get<MeetingStatisticsData>(PageRoutes.Api_meeting_statistics + meetingid + "?id=" + meetingid)
            .tag(PageRoutes.Api_meeting_statistics + meetingid + "?id=" + meetingid)
            .headers("Authorization", kv.getString("token", ""))
            .execute(object : RequestCallback<MeetingStatisticsData>() {
                override fun onSuccessNullData() {
                    super.onSuccessNullData()

                }

                override fun onMySuccess(data: MeetingStatisticsData) {
                    super.onMySuccess(data)
                    if(!isShow){
                        return
                    }
                    try {

                        binding.num1.text = toNum("" + data.browseCount)
                        binding.num2.text = toNum("" + data.userMeetingCount)
                        binding.num3.text = toNum("" + data.totalAmount)
                        binding.num4.text = toNum("" + data.todayInsertUserCount)
                        binding.num5.text = toNum("" + data.todayBeReviewedCount)
                        binding.num6.text = toNum("" + data.leaveCount)
                        setNumData(
                            data.todayInsertUserCount,
                            data.yesterdayInsertUserCount,
                            binding.num7
                        )
                        setNumData(
                            data.todayBeReviewedCount,
                            data.yesterdayBeReviewedCount,
                            binding.num8
                        )
                        setNumData(data.leaveCount, data.yesterdayLeaveCount, binding.num9)
                    } catch (e: java.lang.Exception) {
                    }

                }

                override fun onError(response: Response<MeetingStatisticsData>) {
                    super.onError(response)

                }

                override fun onFinish() {
                    super.onFinish()
                    if(!isShow){
                        return
                    }
                    try {
                    mViewModel.isShowLoading.value = false
                    } catch (e: java.lang.Exception) {
                    }
                }


            })
    }

    fun toNum(data: String?): String {
        if (data.isNullOrEmpty()) {
            return "0"
        }
        var d = "0"
        var amount = data.toDouble()
        var df = DecimalFormat("0.00");
        var s = df.format(amount);
        if (amount >= 10000) {
            var b1 = BigDecimal(amount);
            var b2 = BigDecimal(10000);
//            d = ""+b1.divide(b2)+"万"
            d = "" + b1.divide(b2, 2, BigDecimal.ROUND_DOWN) + "万"
        } else {
            d = data
        }
        return d
    }


    fun setNumData(todayInsertUserCount: Int?, yesterdayInsertUserCount: Int?, tv: TextView) {
        if(todayInsertUserCount==null){
            tv.setTextColor(Color.parseColor("#ff7d8592"))
            tv.text = " /"
            return
        }
        if(yesterdayInsertUserCount==null){
            tv.setTextColor(Color.parseColor("#ff7d8592"))
            tv.text = " /"
            return
        }
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
