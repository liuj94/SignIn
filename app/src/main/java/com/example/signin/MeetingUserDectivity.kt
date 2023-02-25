package com.example.signin

import android.content.Intent
import android.net.Uri
import android.view.View
import android.widget.TextView
import com.alibaba.fastjson.JSON
import com.bumptech.glide.Glide
import com.dylanc.longan.startActivity
import com.example.signin.base.BaseBindingActivity
import com.example.signin.base.BaseViewModel
import com.example.signin.bean.MeetingUserDeData
import com.example.signin.bean.SignUpUser
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
    var orderId = ""
    var failRemark = ""
    var state_laicheng: SignUpUser = SignUpUser()
    var state_zhuche: SignUpUser = SignUpUser()
    var state_ruzhu: SignUpUser = SignUpUser()
    var state_huichang: SignUpUser = SignUpUser()
    var state_chanyin: SignUpUser = SignUpUser()
    var state_fancheng: SignUpUser = SignUpUser()
    var state_liwu: SignUpUser = SignUpUser()
    var examineStatus: String = "0"
    var invoiceStatus: String = "0"
    override fun initData() {
        intent.getStringExtra("id")?.let { id = it }
        mViewModel.isShowLoading.value = true

        binding.itemDdxx.ddBtn.setOnClickListener {
            if (examineStatus.equals("2")) {
                if (invoiceStatus.equals("1")) {
//                    startActivity<ExamineKPActivity>("orderId" to orderId)
                }
            } else if (examineStatus.equals("1")) {
                startActivity<ExamineActivity>("orderId" to orderId)
            }


        }
        binding.itemLcxx.lcBtn.setOnClickListener {
            if(state_laicheng.status.equals("1")){
                startActivity<SiginReActivity>("type" to 2, "data" to state_laicheng)
            }

        }
        binding.itemZcbd.zcBtn.setOnClickListener {
            if(state_zhuche.status.equals("1")){
            startActivity<SiginReActivity>("type" to 1, "data" to state_zhuche)}
        }
        binding.itemRzxx.ddBtn.setOnClickListener {
            if(state_ruzhu.status.equals("1")){
            startActivity<SiginReActivity>("type" to 3, "data" to state_ruzhu)}
        }
        binding.itemHcqd.zcBtn.setOnClickListener {
            if(state_huichang.status.equals("1")){
            startActivity<SiginReActivity>("type" to 4, "data" to state_huichang)}
        }
        binding.itemCyxi.zcBtn.setOnClickListener {
            if(state_chanyin.status.equals("1")){
            startActivity<SiginReActivity>("type" to 5, "data" to state_chanyin)}
        }
        binding.itemFcxx.lcBtn.setOnClickListener {
            if(state_fancheng.status.equals("1")){
            startActivity<SiginReActivity>("type" to 7, "data" to state_fancheng)}
        }

        binding.itemLpff.lpBtn.setOnClickListener {
            if(state_liwu.status.equals("1")){
            startActivity<SiginReActivity>("type" to 6, "data" to state_liwu)}
        }
        binding.itemFcxx.call1.setOnClickListener {
            takePhone(mobile3)
        }
        binding.itemLcxx.call1.setOnClickListener {
            takePhone(mobile2)
        }
        binding.itemYhxx.call1.setOnClickListener {
            takePhone(mobile1)
        }
        binding.itemYhxx.call.setOnClickListener {
            takePhone(mobile)
        }
    }
    fun takePhone(phone: String) {
        val uri = Uri.parse("tel:$phone") //设置要操作的路径
        val it = Intent()
        it.action = Intent.ACTION_DIAL
        it.data = uri
        startActivity(it)


    }

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

                    setDate(data)


                }

                override fun onError(response: Response<MeetingUserDeData>) {
                    super.onError(response)

                }

                override fun onFinish() {
                    super.onFinish()
                    mViewModel.isShowLoading.value = false
                }


            })
    }

    var mobile = ""
    var mobile1 = ""
    var mobile2 = ""
    var mobile3 = ""
    private fun setDate(data: MeetingUserDeData) {
        binding.itemYhxx.name.text = data.name
        binding.itemYhxx.gongshiName.text = data.corporateName
        binding.itemYhxx.zhengjiangType.text = data.userMeetingTypeName
//        binding.itemYhxx.jiedaidName.text = "接待员："+data.meetingSignUps.personChargeName
//        personChargeMobile
        mobile1 = data.mobile
        Glide.with(this@MeetingUserDectivity).load(PageRoutes.BaseUrl + data.avatar)
            .error(R.mipmap.touxiang).into(binding.itemYhxx.tx)
        var model =
            JSON.parseObject(kv.getString("TypeModel", ""), TypeModel::class.java)
        state_zhuche.name = data.name
        state_zhuche.corporateName = data.corporateName
        state_zhuche.userMeetingTypeName = data.userMeetingTypeName

        state_laicheng.name = data.name
        state_laicheng.corporateName = data.corporateName
        state_laicheng.userMeetingTypeName = data.userMeetingTypeName

        state_chanyin.name = data.name
        state_chanyin.corporateName = data.corporateName
        state_chanyin.userMeetingTypeName = data.userMeetingTypeName

        state_ruzhu.name = data.name
        state_ruzhu.corporateName = data.corporateName
        state_ruzhu.userMeetingTypeName = data.userMeetingTypeName

        state_huichang.name = data.name
        state_huichang.corporateName = data.corporateName
        state_huichang.userMeetingTypeName = data.userMeetingTypeName

        state_fancheng.name = data.name
        state_fancheng.corporateName = data.corporateName
        state_fancheng.userMeetingTypeName = data.userMeetingTypeName

        state_liwu.name = data.name
        state_liwu.corporateName = data.corporateName
        state_liwu.userMeetingTypeName = data.userMeetingTypeName


        //订单
        binding.itemDdxx.kong.visibility = View.VISIBLE
        binding.itemDdxx.ll.visibility = View.GONE
        data.userOrder?.let {
            orderId = it.orderNo
            binding.itemDdxx.kong.visibility = View.GONE
            binding.itemDdxx.ll.visibility = View.VISIBLE
            binding.itemDdxx.ddName.text = it.ticketName
            binding.itemDdxx.ddPrice.text = "¥" + it.amount
            //invoiceType	1 普票 2专票
            for (item in model.sys_invoice_type) {
                if (it.invoiceType.equals(item.dictValue)) {
                    binding.itemDdxx.ddType.text =
                        "开票类型:" + item.dictLabel
                }
            }

            binding.itemDdxx.ddTime.text = parseTime(it.createTime)
            binding.itemDdxx.ddNum.text = it.invoiceNo
            //0初始状态 1待审核 2审核成功 3审核失败
            examineStatus = "" + it.examineStatus
            invoiceStatus = "" + it.invoiceStatus
            binding.itemDdxx.ddBtn.setBackgroundResource(R.drawable.shape_bg_ff3974f6_15)
            binding.infoLl.visibility = View.GONE
            for (item in model.sys_invoice_status) {
                if (examineStatus.equals(item.dictValue)) {

                    binding.itemDdxx.ddBtn.text = item.dictLabel
                    if (item.dictValue.equals("2")) {
                        binding.infoLl.visibility = View.VISIBLE
                        if (invoiceStatus.equals("1")) {
                            binding.itemDdxx.ddBtn.text = "审核开票"
                            binding.itemDdxx.ddBtn.setBackgroundResource(R.drawable.shape_bg_ff3974f6_15)
                        } else {
                            binding.itemDdxx.ddBtn.setBackgroundResource(R.drawable.shape_bg_999999_15)
                        }

                    } else {

                        binding.itemDdxx.ddBtn.setBackgroundResource(R.drawable.shape_bg_999999_15)
                    }
                }
            }

        }

        for (item in data.meetingSignUps) {
            when (item.type) {
                1 -> {

                    //注册报到
                    binding.itemZcbd.kong.visibility = View.GONE
                    binding.itemZcbd.ll.visibility = View.VISIBLE
                    setStateColor(model.sys_zhuce, "1", binding.itemZcbd.zcBtn)

                    item.userMeetingSignUp?.let {
                        state_zhuche.status = ""+item.userMeetingSignUp.status
                        setStateColor(model.sys_zhuce, ""+item.userMeetingSignUp.status, binding.itemZcbd.zcBtn)
                    }
                    item.meetingSignUpLocation?.let {

                        state_zhuche.meetingName = item.meetingSignUpLocation.name
                        state_zhuche.autoStatus = item.meetingSignUpLocation.autoStatus
                        state_zhuche.timeLong = item.meetingSignUpLocation.timeLong
                        binding.itemZcbd.name.text = item.meetingSignUpLocation.name
                        binding.itemZcbd.data.text =
                            getDateStr("MM月dd", item.meetingSignUpLocation.startTime)
                        binding.itemZcbd.address.text = item.meetingSignUpLocation.address
                        binding.itemZcbd.time.text = getDateStr(
                            "HH:mm",
                            item.meetingSignUpLocation.startTime
                        ) + "-" + getDateStr("HH:mm", item.meetingSignUpLocation.endTime)
                        }
                    state_zhuche.meetingId = item.meetingSignUpLocation.meetingId
                    state_zhuche.signUpId = item.meetingSignUpLocation.signUpId
                    state_zhuche.signUpLocationId = item.meetingSignUpLocation.id
                    state_zhuche.userMeetingId = data.id



                }
                6 -> {
                    //礼品发放
                    setStateColor(model.sys_liping, "1", binding.itemLpff.lpBtn)

                    item.userMeetingSignUp?.let {
                        state_liwu.status = ""+item.userMeetingSignUp.status
                        setStateColor(model.sys_liping, ""+item.userMeetingSignUp.status, binding.itemLpff.lpBtn)

                    }
                    binding.itemLpff.kong.visibility = View.GONE
                    binding.itemLpff.ll.visibility = View.VISIBLE
                    state_liwu.meetingName = item.meetingSignUpLocation.name
                    state_liwu.autoStatus = item.meetingSignUpLocation.autoStatus
                    state_liwu.timeLong = item.meetingSignUpLocation.timeLong
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

                    state_liwu.meetingId = item.meetingSignUpLocation.meetingId
                    state_liwu.signUpId = item.meetingSignUpLocation.signUpId
                    state_liwu.signUpLocationId = item.meetingSignUpLocation.id
                    state_liwu.userMeetingId = data.id

                }
                3 -> {
                    //入住签到
                    setStateColor(model.sys_ruzhu, "1", binding.itemRzxx.ddBtn)

                    item.userMeetingSignUp?.let {

                        setStateColor(model.sys_ruzhu, ""+item.userMeetingSignUp.status, binding.itemRzxx.ddBtn)
                        for (list in model.sys_ruzhu) {
                            if (list.dictValue.equals(item.userMeetingSignUp.status)) {
                                state_ruzhu.status = list.dictValue
                                if (list.dictValue.equals("1")) {
                                    binding.itemRzxx.location.text =
                                        "签到后分配"
                                }
                            }
                        }
                    }
                    binding.itemRzxx.kong.visibility = View.GONE
                    binding.itemRzxx.ll.visibility = View.VISIBLE
                    state_ruzhu.meetingName = item.meetingSignUpLocation.name
                    state_ruzhu.autoStatus = item.meetingSignUpLocation.autoStatus
                    state_ruzhu.timeLong = item.meetingSignUpLocation.timeLong
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
                    item.meetingSignUpLocation.location?.let {
                        binding.itemRzxx.location.text =
                            "房间号：" + item.meetingSignUpLocation.location
                    }

                    binding.itemRzxx.day.text = "" + daydiff(
                        item.meetingSignUpLocation.startTime,
                        item.meetingSignUpLocation.endTime
                    ) + "天"

                    state_ruzhu.meetingId = item.meetingSignUpLocation.meetingId
                    state_ruzhu.signUpId = item.meetingSignUpLocation.signUpId
                    state_ruzhu.signUpLocationId = item.meetingSignUpLocation.id
                    state_ruzhu.userMeetingId = data.id


                }
                4 -> {
                    //会场签到
                    setStateColor(
                        model.sys_huichang,
                        "1",
                        binding.itemHcqd.zcBtn
                    )
                    item.userMeetingSignUp?.let {

                        setStateColor(
                            model.sys_huichang,
                            ""+item.userMeetingSignUp.status,
                            binding.itemHcqd.zcBtn
                        )
                        for (list in model.sys_huichang) {
                            if (list.dictValue.equals(item.userMeetingSignUp.status)) {
                                state_huichang.status = list.dictValue
                                if (list.dictValue.equals("1")) {
                                    binding.itemHcqd.location.text =
                                        "签到后分配"
                                }
                            }
                        }
                    }
                    binding.itemHcqd.kong.visibility = View.GONE
                    binding.itemHcqd.ll.visibility = View.VISIBLE
                    state_huichang.meetingName = item.meetingSignUpLocation.name
                    state_huichang.autoStatus = item.meetingSignUpLocation.autoStatus
                    state_huichang.timeLong = item.meetingSignUpLocation.timeLong
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
                    item.meetingSignUpLocation.location?.let {
                        binding.itemHcqd.location.text =
                            "座位号：" + item.meetingSignUpLocation.location
                    }

                    //                                binding.itemHcqd.location.text = "请到"+item.meetingSignUpLocation.location+"号桌"

                    state_huichang.meetingId = item.meetingSignUpLocation.meetingId
                    state_huichang.signUpId = item.meetingSignUpLocation.signUpId
                    state_huichang.signUpLocationId = item.meetingSignUpLocation.id
                    state_huichang.userMeetingId = data.id


                }
                5 -> {
                    //餐饮签到
                    setStateColor(model.sys_canyin, "1", binding.itemCyxi.zcBtn)
                    item.userMeetingSignUp?.let {
                        setStateColor(model.sys_canyin, ""+item.userMeetingSignUp.status, binding.itemCyxi.zcBtn)
                        for (list in model.sys_canyin) {
                            if (list.dictValue.equals(item.userMeetingSignUp.status)) {
                                state_chanyin.status = list.dictValue
                                if (list.dictValue.equals("1")) {
                                    binding.itemCyxi.location.text =
                                        "签到后分配"
                                }
                            }
                        }
                    }
                    binding.itemCyxi.kong.visibility = View.GONE
                    binding.itemCyxi.ll.visibility = View.VISIBLE
                    state_chanyin.meetingName = item.meetingSignUpLocation.name
                    state_chanyin.autoStatus = item.meetingSignUpLocation.autoStatus
                    state_chanyin.timeLong = item.meetingSignUpLocation.timeLong
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
                    item.meetingSignUpLocation.location?.let {
                        binding.itemCyxi.location.text =
                            "请到" + it + "号桌"
                    }
                    state_chanyin.meetingId = item.meetingSignUpLocation.meetingId
                    state_chanyin.signUpId = item.meetingSignUpLocation.signUpId
                    state_chanyin.signUpLocationId = item.meetingSignUpLocation.id
                    state_chanyin.userMeetingId = data.id


                }
                7 -> {
                    //返程签到
                    setStateColor(
                        model.sys_fancheng,
                        "1",
                        binding.itemFcxx.lcBtn
                    )
                    item.userMeetingSignUp?.let {
                        state_fancheng.status = ""+item.userMeetingSignUp.status
                            setStateColor(
                            model.sys_fancheng,
                            ""+item.userMeetingSignUp.status,
                            binding.itemFcxx.lcBtn
                        )
                    }
                    item.backUserMeetingTrip?.let {
                        binding.itemFcxx.kong.visibility = View.GONE
                        binding.itemFcxx.ll.visibility = View.VISIBLE
                        state_fancheng.meetingName = item.meetingSignUpLocation.name
                        state_fancheng.autoStatus = item.meetingSignUpLocation.autoStatus
                        state_fancheng.timeLong = item.meetingSignUpLocation.timeLong
                        binding.itemFcxx.name.text = it.remark
                        binding.itemFcxx.lcData1.text =
                            getDateStr("MM月dd", it.startDate).toString()
                        binding.itemFcxx.lcDidian1.text = it.startCity
                        binding.itemFcxx.lcJichang1.text = it.startAddress
                        binding.itemFcxx.lcData2.text =
                            getDateStr("MM月dd", it.endDate).toString()
                        binding.itemFcxx.lcDidian2.text = it.endCity
                        binding.itemFcxx.lcJichang1.text = it.endAddress
                        binding.itemFcxx.jiedai.text = "接待员：：" + item.personChargeName
                        mobile3 = item.personChargeMobile
                        binding.itemFcxx.time.text = getDateStr(
                            "HH:mm",
                            it.startTime
                        ).toString() + "-" + getDateStr("HH:mm", it.endTime).toString()
                        //personChargeMobile status

                        for (item in model.transport_type) {
                            if (it.transport.equals(item.dictValue)) {
                                if (item.dictValue.equals("03")) {
                                    binding.itemFcxx.icon.setImageResource(R.mipmap.laichengxingxi)
                                } else {
                                    binding.itemFcxx.icon.setImageResource(R.mipmap.hc)
                                }

                            }
                        }


                        state_fancheng.meetingId = item.meetingSignUpLocation.meetingId
                        state_fancheng.signUpId = item.meetingSignUpLocation.signUpId
                        state_fancheng.signUpLocationId = item.meetingSignUpLocation.id
                        state_fancheng.userMeetingId = data.id

                    }
                }
                2 -> {
                    setStateColor(
                        model.sys_laicheng,
                        "1",
                        binding.itemLcxx.lcBtn
                    )
                    //来程签到
                    item.userMeetingSignUp?.let {
                        state_laicheng.status = ""+item.userMeetingSignUp.status
                        setStateColor(
                            model.sys_laicheng,
                            ""+item.userMeetingSignUp.status,
                            binding.itemLcxx.lcBtn
                        )
                    }


                    item.userMeetingTrip?.let {
                        binding.itemLcxx.kong.visibility = View.GONE
                        binding.itemLcxx.ll.visibility = View.VISIBLE
                        binding.itemLcxx.name.text = it.remark
                        state_laicheng.meetingName = item.meetingSignUpLocation.name
                        state_laicheng.autoStatus = item.meetingSignUpLocation.autoStatus
                        state_laicheng.timeLong = item.meetingSignUpLocation.timeLong
                        binding.itemLcxx.lcData1.text =
                            getDateStr("MM月dd", it.startDate).toString()
                        binding.itemLcxx.lcDidian1.text = it.startCity
                        binding.itemLcxx.lcJichang1.text = it.startAddress
                        binding.itemLcxx.lcData2.text =
                            getDateStr("MM月dd", it.endDate).toString()
                        binding.itemLcxx.lcDidian2.text = it.endCity
                        binding.itemLcxx.lcJichang1.text = it.endAddress
                        binding.itemLcxx.jiedai.text = "接待员：" + item.personChargeName
                        mobile2 = item.personChargeMobile
                        binding.itemLcxx.time.text = getDateStr(
                            "HH:mm",
                            it.startTime
                        ).toString() + "-" + getDateStr("HH:mm", it.endTime).toString()

                        //personChargeMobile status

                        for (item in model.transport_type) {
                            if (it.transport.equals(item.dictValue)) {
                                if (item.dictValue.equals("03")) {
                                    binding.itemLcxx.icon.setImageResource(R.mipmap.laichengxingxi)
                                } else {
                                    binding.itemLcxx.icon.setImageResource(R.mipmap.hc)
                                }

                            }
                        }
                        state_laicheng.meetingId = item.meetingSignUpLocation.meetingId
                        state_laicheng.signUpId = item.meetingSignUpLocation.signUpId
                        state_laicheng.signUpLocationId = item.meetingSignUpLocation.id
                        state_laicheng.userMeetingId = data.id
                    }
                }
            }
        }
    }

    override fun onResume() {
        super.onResume()
        getData()
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

    fun daydiff(fDateString: String, oDateString: String): Int {

        val fDate = parseServerTime(fDateString)
        val oDate = parseServerTime(oDateString)
        val aCalendar = Calendar.getInstance()
        aCalendar.time = fDate
        val day1 = aCalendar[Calendar.DAY_OF_YEAR]
        aCalendar.time = oDate
        val day2 = aCalendar[Calendar.DAY_OF_YEAR]
        return day2 - day1
    }
}