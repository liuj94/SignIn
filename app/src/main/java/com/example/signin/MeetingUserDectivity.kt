package com.example.signin

import android.view.View
import android.widget.EditText
import android.widget.TextView
import com.afollestad.materialdialogs.MaterialDialog
import com.afollestad.materialdialogs.customview.customView
import com.alibaba.fastjson.JSON
import com.bumptech.glide.Glide
import com.dylanc.longan.startActivity
import com.example.signin.base.BaseBindingActivity
import com.example.signin.base.BaseViewModel
import com.example.signin.bean.MeetingUserDeData
import com.example.signin.bean.TypeData
import com.example.signin.bean.TypeModel
import com.example.signin.databinding.ActMeetingUserInfoBinding
import com.example.signin.net.RequestCallback
import com.lzy.okgo.OkGo
import com.lzy.okgo.model.Response
import java.text.SimpleDateFormat
import java.util.*

class MeetingUserDectivity : BaseBindingActivity<ActMeetingUserInfoBinding, BaseViewModel>() {
    override fun getViewModel(): Class<BaseViewModel> = BaseViewModel::class.java

    //var data : MeetingUserDeData
    var id = ""
    var state_dingdan = "1"
    var state_laicheng = "1"
    var state_zhuche = "1"
    var state_ruzhu = "1"
    var state_huichang = "1"
    var state_chanyin = "1"
    var state_fancheng = "1"
    var state_liwu = "1"
    override fun initData() {
        intent.getStringExtra("id")?.let { id = it }

        getData()
        binding.itemDdxx.ddBtn.setOnClickListener {
//            invoiceStatus 1未开发票 2代开发票 3已开发票
            //examineStatus	0初始状态 1待审核 2审核成功 3审核失败
//            {
//                "orderId": 141,
//                "examine": true,
//                "failRemark": false
//            }
        }
        binding.itemLcxx.lcBtn.setOnClickListener {
            showSigin(1)
        }
        binding.itemZcbd.zcBtn.setOnClickListener {
            showSigin(1)
        }
        binding.itemRzxx.ddBtn.setOnClickListener {
            if (state_ruzhu.equals("2")) {
                //请输入房间号
                MaterialDialog(this@MeetingUserDectivity).show {
                    customView(	//自定义弹窗
                        viewRes = R.layout.tc_qiandao2,//自定义文件
                        dialogWrapContent = true,	//让自定义宽度生效
                        scrollable = true,			//让自定义宽高生效
                        noVerticalPadding = true    //让自定义高度生效
                    ).apply {
                        findViewById<EditText>(R.id.qx).hint = "请输入房间号"
                        findViewById<TextView>(R.id.qx).setOnClickListener {
                            dismiss()
                        }
                        findViewById<TextView>(R.id.qd).setOnClickListener {
                            dismiss()
                            findViewById<EditText>(R.id.qx).text
                            sigin()
                        }
                    }

                    cancelOnTouchOutside(false)	//点击外部不消失
                }
            }else{
                showSigin(1)
            }
        }
        binding.itemHcqd.zcBtn.setOnClickListener {
            if (state_huichang.equals("2")) {
                //请输入座位号
                MaterialDialog(this@MeetingUserDectivity).show {
                    customView(	//自定义弹窗
                        viewRes = R.layout.tc_qiandao2,//自定义文件
                        dialogWrapContent = true,	//让自定义宽度生效
                        scrollable = true,			//让自定义宽高生效
                        noVerticalPadding = true    //让自定义高度生效
                    ).apply {

                        findViewById<TextView>(R.id.qx).setOnClickListener {
                            dismiss()
                        }
                        findViewById<TextView>(R.id.qd).setOnClickListener {
                            dismiss()
                            findViewById<EditText>(R.id.qx).text
                            sigin()
                        }
                    }

                    cancelOnTouchOutside(false)	//点击外部不消失
                }
            }else{
                showSigin(1)
            }
        }
        binding.itemCyxi.zcBtn.setOnClickListener {
            if (state_chanyin.equals("2")) {
    //请输入座位号
                MaterialDialog(this@MeetingUserDectivity).show {
                    customView(	//自定义弹窗
                        viewRes = R.layout.tc_qiandao2,//自定义文件
                        dialogWrapContent = true,	//让自定义宽度生效
                        scrollable = true,			//让自定义宽高生效
                        noVerticalPadding = true    //让自定义高度生效
                    ).apply {

                        findViewById<TextView>(R.id.qx).setOnClickListener {
                            dismiss()
                        }
                        findViewById<TextView>(R.id.qd).setOnClickListener {
                            dismiss()
                            findViewById<EditText>(R.id.qx).text
                            sigin()
                        }
                    }

                    cancelOnTouchOutside(false)	//点击外部不消失
                }
            }else{
                showSigin(1)
            }
        }
        binding.itemFcxx.lcBtn.setOnClickListener {
            showSigin(1)
        }
        binding.itemLpff.lpBtn.setOnClickListener {
            showSigin(1)
        }
    }

    private fun showSigin(type:Int) {
        MaterialDialog(this@MeetingUserDectivity).show {
            customView(    //自定义弹窗
                viewRes = R.layout.tc_qiandao,//自定义文件
                dialogWrapContent = true,    //让自定义宽度生效
                scrollable = true,            //让自定义宽高生效
                noVerticalPadding = true    //让自定义高度生效
            ).apply {

                findViewById<TextView>(R.id.qx).setOnClickListener {
                    dismiss()
                }
                findViewById<TextView>(R.id.qd).setOnClickListener {
                    dismiss()
                    val params = HashMap<String, String>()
                    params["meetingId"] = meetingId//会议id
                    params["signUpLocationId"] = signUpLocationId//签到点id
                    params["signUpId"] = signUpId//签到站id
                    params["userMeetingId"] = userMeetingId//用户参与会议id
                    sigin(JSON.toJSONString(params))
                }
            }

            cancelOnTouchOutside(false)    //点击外部不消失
        }
    }

    private fun sigin(params :String) {

        OkGo.post<String>(PageRoutes.Api_sigin)
            .tag(PageRoutes.Api_sigin)
            .upJson(params)
            .headers("Authorization",kv.getString("token",""))
            .execute(object : RequestCallback<String>() {

                override fun onSuccess(response: Response<String>?) {
                    super.onSuccess(response)

                }

                override fun onFinish() {
                    super.onFinish()

                }


            })
    }

    var examineStatus: Int = 0
    private fun getData() {

        OkGo.get<MeetingUserDeData>(PageRoutes.Api_meetinguser_data + id + "?id=" + id)
            .tag(PageRoutes.Api_meetinguser_data + id + "?id=" + id)
            .headers("Authorization", kv.getString("token", ""))
            .execute(object : RequestCallback<MeetingUserDeData>() {
                override fun onSuccessNullData() {
                    super.onSuccessNullData()

                }

                override fun onMySuccess(data: MeetingUserDeData) {
                    super.onMySuccess(data)

                    binding.itemYhxx.name.text = data.name
                    binding.itemYhxx.gongshiName.text = data.corporateName
                    binding.itemYhxx.zhengjiangType.text = data.userMeetingTypeName
                    Glide.with(this@MeetingUserDectivity).load(PageRoutes.BaseUrl + data.avatar)
                        .error(R.mipmap.touxiang).into(binding.itemYhxx.tx)
                    //订单
                    binding.itemDdxx.kong.visibility = View.VISIBLE
                    binding.itemDdxx.ll.visibility = View.GONE
                    data.userOrder?.let {
                        binding.itemDdxx.kong.visibility = View.GONE
                        binding.itemDdxx.ll.visibility = View.VISIBLE
                        binding.itemDdxx.ddName.text = it.ticketName
                        binding.itemDdxx.ddPrice.text = "¥" + it.amount
                        //invoiceType	1 普票 2专票
                        binding.itemDdxx.ddType.text =
                            "开票类型:" + if (it.invoiceType.equals("1")) "普票" else "专票"

                        binding.itemDdxx.ddTime.text = parseTime(it.createTime)
                        binding.itemDdxx.ddNum.text = it.invoiceNo
                        //0初始状态 1待审核 2审核成功 3审核失败
                        examineStatus = it.examineStatus
                        when (it.examineStatus) {
                            0 -> {
                                binding.itemDdxx.ddBtn.text = "初始状态"
                            }
                            1 -> {
                                binding.itemDdxx.ddBtn.text = "待审核"
                            }
                            2 -> {
                                binding.itemDdxx.ddBtn.text = "审核成功"
                            }
                            3 -> {
                                binding.itemDdxx.ddBtn.text = "审核失败"
                            }

                        }
                    }
                    var model =
                        JSON.parseObject(kv.getString("TypeModel", ""), TypeModel::class.java)

                    for (item in data.meetingSignUps) {
                        when (item.type) {
                            1 -> {
                                //注册报到
                                binding.itemZcbd.kong.visibility = View.GONE
                                binding.itemZcbd.ll.visibility = View.VISIBLE
                                binding.itemZcbd.name.text = item.meetingSignUpLocation.name
                                binding.itemZcbd.data.text =
                                    getDateStr("MM月dd", item.meetingSignUpLocation.startTime)
                                binding.itemZcbd.address.text = item.meetingSignUpLocation.address
                                binding.itemZcbd.time.text = getDateStr(
                                    "HH:mm",
                                    item.meetingSignUpLocation.startTime
                                ) + "-" + getDateStr("HH:mm", item.meetingSignUpLocation.endTime)
                                setStateColor(model.sys_zhuce, item.status, binding.itemZcbd.zcBtn)


                            }
                            6 -> {
                                //礼品发放

                                binding.itemLpff.kong.visibility = View.GONE
                                binding.itemLpff.ll.visibility = View.VISIBLE
                                binding.itemLpff.name.text = item.meetingSignUpLocation.name
                                binding.itemLpff.date.text =
                                    getDateStr("MM月dd", item.meetingSignUpLocation.startTime)
                                binding.itemLpff.address.text = item.meetingSignUpLocation.address
//                                binding.itemLpff.time.text =
//                                    item.meetingSignUpLocation.startTime + item.meetingSignUpLocation.endTime
                                binding.itemLpff.time.text = getDateStr(
                                    "HH:mm",
                                    item.meetingSignUpLocation.startTime
                                ) + "-" + getDateStr("HH:mm", item.meetingSignUpLocation.endTime)

                                //
                                setStateColor(model.sys_liping, item.status, binding.itemLpff.lpBtn)

                            }
                            3 -> {
                                //入住签到
                                binding.itemRzxx.kong.visibility = View.GONE
                                binding.itemRzxx.ll.visibility = View.VISIBLE
                                binding.itemRzxx.name.text = item.meetingSignUpLocation.name
                                binding.itemRzxx.date1.text = getDateStr(
                                    "MM月dd",
                                    item.meetingSignUpLocation.startTime
                                ).toString()
                                binding.itemRzxx.date2.text = getDateStr(
                                    "MM月dd",
                                    item.meetingSignUpLocation.endTime
                                ).toString()
                                binding.itemRzxx.address.text = item.meetingSignUpLocation.address
                                binding.itemRzxx.location.text =
                                    "房间号：" + item.meetingSignUpLocation.location
                                binding.itemRzxx.day.text = "1天"
                                setStateColor(model.sys_ruzhu, item.status, binding.itemRzxx.ddBtn)


                            }
                            4 -> {
                                //会场签到
                                binding.itemHcqd.kong.visibility = View.GONE
                                binding.itemHcqd.ll.visibility = View.VISIBLE
                                binding.itemHcqd.name.text = item.meetingSignUpLocation.name
                                binding.itemHcqd.data.text = getDateStr(
                                    "MM月dd",
                                    item.meetingSignUpLocation.startTime
                                ).toString()
//                                binding.itemHcqd.time.text =
//                                    item.meetingSignUpLocation.startTime + item.meetingSignUpLocation.endTime
                                binding.itemHcqd.time.text = getDateStr(
                                    "HH:mm",
                                    item.meetingSignUpLocation.startTime
                                ).toString() + "-" + getDateStr(
                                    "HH:mm",
                                    item.meetingSignUpLocation.endTime
                                ).toString()

                                binding.itemHcqd.address.text = item.meetingSignUpLocation.address
                                binding.itemHcqd.location.text =
                                    "座位号：" + item.meetingSignUpLocation.location
//                                binding.itemHcqd.location.text = "请到"+item.meetingSignUpLocation.location+"号桌"
                                setStateColor(
                                    model.sys_huichang,
                                    item.status,
                                    binding.itemHcqd.zcBtn
                                )


                            }
                            5 -> {
                                //餐饮签到
                                binding.itemCyxi.kong.visibility = View.GONE
                                binding.itemCyxi.ll.visibility = View.VISIBLE
                                binding.itemCyxi.name.text = item.meetingSignUpLocation.name
                                binding.itemCyxi.date.text = getDateStr(
                                    "MM月dd",
                                    item.meetingSignUpLocation.startTime
                                ).toString()

                                binding.itemCyxi.time.text = getDateStr(
                                    "HH:mm",
                                    item.meetingSignUpLocation.startTime
                                ).toString() + "-" + getDateStr(
                                    "HH:mm",
                                    item.meetingSignUpLocation.endTime
                                ).toString()

                                binding.itemCyxi.address.text = item.meetingSignUpLocation.address
                                binding.itemCyxi.location.text =
                                    "请到" + item.meetingSignUpLocation.location + "号桌"
                                setStateColor(model.sys_canyin, item.status, binding.itemCyxi.zcBtn)


                            }
                            7 -> {
                                //返程签到
                                item.backUserMeetingTrip?.let {
                                    binding.itemFcxx.kong.visibility = View.GONE
                                    binding.itemFcxx.ll.visibility = View.VISIBLE
                                    binding.itemFcxx.name.text = it.remark
                                    binding.itemFcxx.lcData1.text =
                                        getDateStr("MM月dd", it.startDate).toString()
                                    binding.itemFcxx.lcDidian1.text = it.startCity
                                    binding.itemFcxx.lcJichang1.text = it.startAddress
                                    binding.itemFcxx.lcData2.text =
                                        getDateStr("MM月dd", it.endDate).toString()
                                    binding.itemFcxx.lcDidian2.text = it.endCity
                                    binding.itemFcxx.lcJichang1.text = it.endAddress
                                    binding.itemFcxx.jiedai.text = "接待员：" + item.personChargeName
                                    binding.itemFcxx.time.text = getDateStr(
                                        "HH:mm",
                                        it.startTime
                                    ).toString() + "-" + getDateStr("HH:mm", it.endTime).toString()
                                    //personChargeMobile status
                                    setStateColor(
                                        model.sys_fancheng,
                                        item.status,
                                        binding.itemFcxx.lcBtn
                                    )
                                }
                            }
                            2 -> {
                                //来程签到
                                item.userMeetingTrip?.let {
                                    binding.itemLcxx.kong.visibility = View.GONE
                                    binding.itemLcxx.ll.visibility = View.VISIBLE
                                    binding.itemLcxx.name.text = it.remark
                                    binding.itemLcxx.lcData1.text =
                                        getDateStr("MM月dd", it.startDate).toString()
                                    binding.itemLcxx.lcDidian1.text = it.startCity
                                    binding.itemLcxx.lcJichang1.text = it.startAddress
                                    binding.itemLcxx.lcData2.text =
                                        getDateStr("MM月dd", it.endDate).toString()
                                    binding.itemLcxx.lcDidian2.text = it.endCity
                                    binding.itemLcxx.lcJichang1.text = it.endAddress
                                    binding.itemLcxx.jiedai.text = "接待员：" + item.personChargeName
                                    binding.itemLcxx.time.text = getDateStr(
                                        "HH:mm",
                                        it.startTime
                                    ).toString() + "-" + getDateStr("HH:mm", it.endTime).toString()

                                    //personChargeMobile status
                                    setStateColor(
                                        model.sys_laicheng,
                                        item.status,
                                        binding.itemLcxx.lcBtn
                                    )

                                }
                            }
                        }
                    }


                }

                override fun onError(response: Response<MeetingUserDeData>) {
                    super.onError(response)

                }

                override fun onFinish() {
                    super.onFinish()

                }


            })
    }

    private fun setStateColor(
        lists: List<TypeData>,
        status: String,
        tv: TextView
    ) {
        for (list in lists) {
            if (list.dictValue.equals(status)) {
                tv.text = list.dictLabel
                if (!list.dictValue.equals("1")) {
                    tv.setBackgroundResource(R.drawable.shape_bg_999999_15)
                } else {
                    tv.setBackgroundResource(R.drawable.shape_bg_ff3974f6_15)
                }
            }

        }
    }


    fun getDateStr(format: String, dateStr: String): String {
        var date = parseServerTime(dateStr)
        var format = format
        if (format == null || format.isEmpty()) {
            format = "MM-dd"
        }
        val formatter = SimpleDateFormat(format)
        return formatter.format(date)
    }

    fun parseServerTime(serverTime: String): Date {
        var format = "yyyy-MM-dd HH:mm"
        val sdf = SimpleDateFormat(format, Locale.CHINESE)
        sdf.timeZone = TimeZone.getTimeZone("GMT+8:00")
        var date: Date = Date()
        try {
            date = sdf.parse(serverTime)
        } catch (e: Exception) {

        }
        return date
    }

    fun parseTime(serverTime: String): String {
        var format = "yyyy-MM-dd HH:mm:ss"
        val sdf = SimpleDateFormat(format, Locale.CHINESE)
        sdf.timeZone = TimeZone.getTimeZone("GMT+8:00")
        var date: Date = Date()
        try {
            date = sdf.parse(serverTime)
        } catch (e: Exception) {

        }
        val formatter = SimpleDateFormat("MM月dd日 HH:mm")
        return formatter.format(date)

    }

}