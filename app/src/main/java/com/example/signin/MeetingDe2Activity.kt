package com.example.signin


import android.graphics.Color
import android.util.Log
import android.view.View
import android.widget.TextView
import androidx.fragment.app.FragmentTransaction
import androidx.recyclerview.widget.LinearLayoutManager
import com.alibaba.fastjson.JSON
import com.example.signin.adapter.FMeetingDeListAdapter
import com.example.signin.base.BaseBindingActivity
import com.example.signin.base.BaseViewModel
import com.example.signin.bean.MeetingStatisticsData
import com.example.signin.bean.SiginUpListData
import com.example.signin.bean.SiginUpListModel
import com.example.signin.databinding.ActMeetda2Binding
import com.example.signin.fragment.MettingDe2Fragment
import com.example.signin.fragment.MettingDe3Fragment
import com.example.signin.fragment.MettingDe4Fragment
import com.example.signin.net.RequestCallback
import com.jeremyliao.liveeventbus.LiveEventBus
import com.lzy.okgo.OkGo
import com.lzy.okgo.model.Response
import java.math.BigDecimal
import java.text.DecimalFormat


class MeetingDe2Activity : BaseBindingActivity<ActMeetda2Binding, BaseViewModel>() {

    override fun getViewModel(): Class<BaseViewModel> = BaseViewModel::class.java


    var meetingId = ""
    var meetingName = ""
    var businessId = ""
    var userType = "00"
    private var adapter: FMeetingDeListAdapter? = null

    //    private var myAdapter: SimpleAdapter? = null
    private var list: MutableList<SiginUpListData> = ArrayList()

    //    private var list: MutableList<String> = ArrayList()
    override fun initCARData() {
        super.initCARData()
        isCreateShow = false
    }

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
        LiveEventBus
            .get<String>("selectLlVISIBLE", String::class.java)
            .observe(this) {
//        LiveDataBus.get().with("selectLlVISIBLE", String::class.java)
//            .observeForever {
                binding.zhez.visibility = View.VISIBLE
            }
        LiveEventBus
            .get<String>("selectLlGONE", String::class.java)
            .observe(this) {
//        LiveDataBus.get().with("selectLlGONE", String::class.java)
//            .observeForever {
                binding.zhez.visibility = View.GONE
            }
        binding.zhez.setOnClickListener {

        }
        binding.name.text = meetingName
        if (userType.equals("01") || userType.equals("04")) {
            binding.mRb1.visibility = View.VISIBLE
            binding.fragmentLL.visibility = View.VISIBLE
            binding.title.text = "数据统计"
            binding.mRb1.isChecked = true
            getinitGata()

            binding.recyclerview.layoutManager = LinearLayoutManager(this)
            adapter = FMeetingDeListAdapter()
            adapter?.submitList(list)
            binding.recyclerview.adapter = adapter

            getinitList()
        } else {
            binding.mRb1.visibility = View.GONE
            binding.fragmentLL.visibility = View.GONE
            binding.title.text = "选签到点"
            binding.mRb2.isChecked = true
            checkFragment2()
        }
        LiveEventBus
            .get<String>("JWebSocketClientlocation", String::class.java)
            .observe(this) {
//        LiveDataBus.get().with("JWebSocketClientlocation", String::class.java)
//            .observeForever {
                try {
                    if (AppManager.getAppManager()
                            .activityInstanceIsLive(this@MeetingDe2Activity)
                    ) {
                        if (userType.equals("01") || userType.equals("04")) {
                            getData()
                            getList()
                        }

                    }
                } catch (e: java.lang.Exception) {
                }


            }


    }

    //第一个fragment
//    private var mettingDe1Fragment: MettingDe1Fragment? = null
    //第二个fragment
    private var mettingDe3Fragment: MettingDe3Fragment? = null
    private var mettingDe2Fragment: MettingDe2Fragment? = null
    private var mettingDe4Fragment: MettingDe4Fragment? = null

    //    private val mettingDe1FragmentTag = "MettingDe1Fragment"
    private val mettingDe2FragmentTag = "MettingDe2Fragment"
    private val mettingDe3FragmentTag = "MettingDe2Fragment"
    private val mettingDe4FragmentTag = "MettingDe2Fragment"

    /**
     * 显示 现场检查 fragment
     */
//    fun checkFragment1() {
//        val transaction: FragmentTransaction = supportFragmentManager.beginTransaction()
//        //add,初始化fragment并添加到事务中，如果为null就new一个
//        if (mettingDe1Fragment == null) {
//            mettingDe1Fragment = MettingDe1Fragment()
//            transaction.add(R.id.navi_home, mettingDe1Fragment!!, mettingDe1FragmentTag)
//        }
//        hideFragment(transaction)
//        transaction.show(mettingDe1Fragment!!)
//        //提交事务
//        transaction.commit()
//    }
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

    override fun onResume() {
        super.onResume()
        if (mettingDe2Fragment != null) {
            mettingDe2Fragment?.shuaxin()
        }
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
//        if (mettingDe1Fragment != null) {
//            transaction.hide(mettingDe1Fragment!!)
//        }

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


    override fun initListener() {
        binding.mRg.setOnCheckedChangeListener { _, checkedId ->
            when (checkedId) {
                R.id.mRb1 -> {
                    binding.title.text = "数据统计"
//                    binding.mViewPager?.currentItem = 0
//                    checkFragment1()
                    binding.fragmentLL.visibility = View.VISIBLE
                }

                R.id.mRb2 -> {
                    binding.title.text = "选签到点"
//                    if (fragments.size == 3) {
//                        binding.mViewPager?.currentItem = 0
//                    } else {
//                        binding.mViewPager?.currentItem = 1
//                    }
                    binding.fragmentLL.visibility = View.GONE
                    checkFragment2()

                }

                R.id.mRb3 -> {
                    binding.title.text = "参会名单"
//                    if (fragments.size == 3) {
//                        binding.mViewPager?.currentItem = 1
//                    } else {
//                        binding.mViewPager?.currentItem = 2
//                    }
                    binding.fragmentLL.visibility = View.GONE
                    checkFragment3()

                }

                R.id.mRb4 -> {
                    binding.title.text = "实时对讲"
//                    if (fragments.size == 3) {
//                        binding.mViewPager?.currentItem = 2
//                    } else {
//                        binding.mViewPager?.currentItem = 3
//                    }
                    binding.fragmentLL.visibility = View.GONE
                    checkFragment4()

                }


            }
        }


    }

    private fun getinitList() {
        if (!kv.getString("SiginUpListModelmeetingId" + meetingId, "").isNullOrEmpty()) {
            Log.d("aaaaprintUnitXXPermissions", "获取本地===up/app/list")
            var data =
                JSON.parseObject(
                    kv.getString("SiginUpListModelmeetingId" + meetingId, ""),
                    SiginUpListModel::class.java
                )
            try {
                list.clear()
                list.addAll(data.list)
                adapter?.notifyDataSetChanged()
            } catch (e: java.lang.Exception) {
            }
        } else {
            Log.d("getList", "接口开始调用===up/app/list")
            mViewModel.isShowLoading.value = true
            getList()

        }

    }

    private fun getList() {

        Log.d("getList", "接口开始调用===up/app/list")
        mViewModel.isShowLoading.value = true
        OkGo.get<List<SiginUpListData>>(PageRoutes.Api_meeting_sign_up_app_list + meetingId)
            .tag(PageRoutes.Api_meeting_sign_up_app_list + meetingId + "f1" + System.currentTimeMillis())
            .headers("Authorization", kv.getString("token", ""))
            .execute(object : RequestCallback<List<SiginUpListData>>() {
                override fun onSuccessNullData() {
                    super.onSuccessNullData()

                }

                override fun onMySuccess(data: List<SiginUpListData>) {
                    super.onMySuccess(data)
                    var allList = SiginUpListModel()
                    allList.list = data
                    kv.putString(
                        "SiginUpListModelmeetingId" + meetingId,
                        JSON.toJSONString(allList)
                    )
                    if (!AppManager.getAppManager()
                            .activityInstanceIsLive(this@MeetingDe2Activity)
                    ) {
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
                    if (!AppManager.getAppManager()
                            .activityInstanceIsLive(this@MeetingDe2Activity)
                    ) {
                        return
                    }
                    try {
                        mViewModel.isShowLoading.value = false

                    } catch (e: Exception) {
                    }


                }


            })


    }

    fun getinitGata() {
        if (!kv.getString("MeetingStatistics" + meetingId, "").isNullOrEmpty()) {
            Log.d("aaaaprintUnitXXPermissions", "获取本地===MeetingStatistics")
            var data =
                JSON.parseObject(
                    kv.getString("MeetingStatistics" + meetingId, ""),
                    MeetingStatisticsData::class.java
                )
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
        } else {
            getData()
        }
    }

    fun getData() {

        OkGo.get<MeetingStatisticsData>(PageRoutes.Api_meeting_statistics + meetingId + "?id=" + meetingId)
            .tag(PageRoutes.Api_meeting_statistics + meetingId + "?id=" + meetingId)
            .headers("Authorization", kv.getString("token", ""))
            .execute(object : RequestCallback<MeetingStatisticsData>() {
                override fun onSuccessNullData() {
                    super.onSuccessNullData()

                }

                override fun onMySuccess(data: MeetingStatisticsData) {
                    super.onMySuccess(data)
                    if (!AppManager.getAppManager()
                            .activityInstanceIsLive(this@MeetingDe2Activity)
                    ) {
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
                    if (!AppManager.getAppManager()
                            .activityInstanceIsLive(this@MeetingDe2Activity)
                    ) {
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
        if (todayInsertUserCount == null) {
            tv.setTextColor(Color.parseColor("#ff7d8592"))
            tv.text = " /"
            return
        }
        if (yesterdayInsertUserCount == null) {
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