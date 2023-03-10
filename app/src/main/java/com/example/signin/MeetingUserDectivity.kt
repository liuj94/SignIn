package com.example.signin

import android.annotation.SuppressLint
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
import sigin
import java.text.SimpleDateFormat
import java.util.*

class MeetingUserDectivity : BaseBindingActivity<ActMeetingUserInfoBinding, BaseViewModel>() {
    override fun getViewModel(): Class<BaseViewModel> = BaseViewModel::class.java

    //var data : MeetingUserDeData
    var id = ""
    var showType = 0
    var state_dingdan = "1"
    var order: MeetingUserDeData.UserOrderBean? = null
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
        intent.getIntExtra("showType", 0)?.let { showType = it }

        if (showType != 0) {
            binding.itemZcbd.root.visibility = View.GONE
            binding.itemLcxx.root.visibility = View.GONE
            binding.itemRzxx.root.visibility = View.GONE
            binding.itemHcqd.root.visibility = View.GONE
            binding.itemCyxi.root.visibility = View.GONE
            binding.itemLpff.root.visibility = View.GONE
            binding.itemFcxx.root.visibility = View.GONE
            when (showType) {
                1 -> {
                    binding.itemZcbd.root.visibility = View.VISIBLE
                }
                2 -> {
                    binding.itemLcxx.root.visibility = View.VISIBLE
                }
                3 -> {
                    binding.itemRzxx.root.visibility = View.VISIBLE
                }
                4 -> {
                    binding.itemHcqd.root.visibility = View.VISIBLE
                }
                5 -> {
                    binding.itemCyxi.root.visibility = View.VISIBLE
                }
                6 -> {
                    binding.itemLpff.root.visibility = View.VISIBLE
                }
                7 -> {
                    binding.itemFcxx.root.visibility = View.VISIBLE
                }
            }
        }
        binding.itemDdxx.ll.setOnClickListener {
            startActivity<ExamineActivity>("order" to order)
        }
        binding.itemDdxx.ddBtn.setOnClickListener {
            if (examineStatus.equals("2")) {
                if (invoiceStatus.equals("1")) {
                    startActivity<ExamineKPActivity>("order" to order)
                }
            } else if (examineStatus.equals("1")) {
                startActivity<ExamineActivity>("order" to order)
            }


        }
        binding.itemLcxx.lcBtn.setOnClickListener {
            if (state_laicheng.status.equals("1")) {
                gotoSigin(state_laicheng, 2)

            }

        }
        binding.itemZcbd.zcBtn.setOnClickListener {
            if (state_zhuche.status.equals("1")) {
                gotoSigin(state_zhuche, 1)
//                startActivity<SiginReActivity>("type" to 1, "data" to state_zhuche)
            }
        }
        binding.itemRzxx.ddBtn.setOnClickListener {
            if (state_ruzhu.status.equals("1")) {
//                gotoSigin(state_ruzhu,3)
                startActivity<SiginReActivity>("type" to 3, "data" to state_ruzhu)
            }
        }
        binding.itemHcqd.zcBtn.setOnClickListener {
            if (state_huichang.status.equals("1")) {
                gotoSigin(state_huichang, 4)
//                startActivity<SiginReActivity>("type" to 4, "data" to state_huichang)
            }
//            else{
//                if(state_huichang.location.equals("")){
//                    gotoSigin(state_huichang,4)
//                }
//            }
        }
        binding.itemCyxi.zcBtn.setOnClickListener {
            if (state_chanyin.status.equals("1")) {
                gotoSigin(state_chanyin, 5)
//                startActivity<SiginReActivity>("type" to 5, "data" to state_chanyin)
            }
//            else{
//                if(state_chanyin.location.equals("")){
//                    gotoSigin(state_chanyin,5)
//                }
//            }
        }
        binding.itemFcxx.lcBtn.setOnClickListener {
            if (state_fancheng.status.equals("1")) {
                gotoSigin(state_fancheng, 7)
//                startActivity<SiginReActivity>("type" to 7, "data" to state_fancheng)
            }
        }

        binding.itemLpff.lpBtn.setOnClickListener {
            if (state_liwu.status.equals("1")) {
                gotoSigin(state_liwu, 6)
//                startActivity<SiginReActivity>("type" to 6, "data" to state_liwu)
            }
        }
        binding.itemFcxx.call1.setOnClickListener {
            takePhone(mobile3)
        }
        binding.itemLcxx.call1.setOnClickListener {
            takePhone(mobile2)
        }
        binding.itemYhxx.call1.setOnClickListener {
            takePhone(mobile)
        }
        binding.itemYhxx.call.setOnClickListener {
            takePhone(mobile1)
        }
    }

    private fun gotoSigin(data: SignUpUser, type: Int) {
        if (data.equals("2")) {

            var params = HashMap<String, String>()
            params["meetingId"] = data.meetingId//??????id
            params["signUpLocationId"] = data.signUpLocationId//?????????id
            params["signUpId"] = data.signUpId//?????????id
            params["userMeetingId"] = data.userMeetingId//??????????????????id
            params["status"] = "2"//??????????????????id
            sigin(JSON.toJSONString(params), { success ->
                data.success = success
                startActivity<SiginReAutoActivity>(
                    "type" to type,
                    "data" to data
                )
            }, {}, {})
        } else {
            startActivity<SiginReActivity>("type" to type, "data" to data)
        }
    }

    fun takePhone(phone: String) {
        val uri = Uri.parse("tel:$phone") //????????????????????????
        val it = Intent()
        it.action = Intent.ACTION_DIAL
        it.data = uri
        startActivity(it)


    }

    private fun getData() {
        mViewModel.isShowLoading.value = true
        OkGo.get<MeetingUserDeData>(PageRoutes.Api_meetinguser_data + id + "?id=" + id)
            .tag(PageRoutes.Api_meetinguser_data + id + "?id=" + id)
            .headers("Authorization", kv.getString("token", ""))
            .execute(object : RequestCallback<MeetingUserDeData>() {
                override fun onSuccessNullData() {
                    super.onSuccessNullData()

                }

                override fun onMySuccess(data: MeetingUserDeData) {
                    super.onMySuccess(data)
                    try {
                        setDate(data)
                    } catch (e: java.lang.Exception) {

                    }


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

    @SuppressLint("SetTextI18n")
    private fun setDate(data: MeetingUserDeData) {
        binding.itemYhxx.name.text = data.name
        binding.itemYhxx.gongshiName.text = data.corporateName
        binding.itemYhxx.zhengjiangType.text = data.userMeetingTypeName
        binding.itemYhxx.jiedaidName.text = "????????????" + data.personChargeName
        mobile = data.personChargeMobile
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


        //??????
        binding.itemDdxx.kong.visibility = View.VISIBLE
        binding.itemDdxx.ll.visibility = View.GONE
        data.userOrder?.let {
            order = it
            order?.userName = data.name
            order?.corporateName = data.corporateName
            binding.itemDdxx.kong.visibility = View.GONE
            binding.itemDdxx.ll.visibility = View.VISIBLE
            binding.itemDdxx.ddName.text = it.ticketName
            binding.itemDdxx.ddPrice.text = "??" + it.amount
            binding.itemDdxx.ddType.text =
                "????????????:"
            //invoiceType	1 ?????? 2??????
            for (item in model.sys_invoice_type) {
                if (it.invoiceType.equals(item.dictValue)) {
                    binding.itemDdxx.ddType.text =
                        "????????????:" + item.dictLabel
                }
            }

            binding.itemDdxx.ddTime.text = parseTime(it.createTime)
            binding.itemDdxx.ddNum.text = it.invoiceNo
            //0???????????? 1????????? 2???????????? 3????????????
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
                            binding.itemDdxx.ddBtn.text = "????????????"
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

        if (data.userOrder == null) {
            binding.itemDdxx.root.visibility = View.GONE
            binding.infoLl.visibility = View.VISIBLE
        }
        for (item in data.meetingSignUps) {
            when (item.type) {
                1 -> {
                    try {
                        //????????????
                        binding.itemZcbd.kong.visibility = View.GONE
                        binding.itemZcbd.ll.visibility = View.VISIBLE
                        setStateColor(model.sys_zhuce, "1", binding.itemZcbd.zcBtn)

                        data.id?.let { state_zhuche.userMeetingId = data.id }

                        item.userMeetingSignUp?.let {
                            state_zhuche.status = "" + it.status
                            setStateColor(
                                model.sys_zhuce,
                                "" + it.status,
                                binding.itemZcbd.zcBtn
                            )
                        }
                        item.meetingSignUpLocation?.let {
                            state_zhuche.userMeetingId = data.id
                            eData(state_zhuche, it)
                            it.name?.let { name -> binding.itemZcbd.name.text = name }
                            it.startTime?.let { startTime ->
                                binding.itemZcbd.data.text =
                                    getDateStr("MM???dd", startTime)
                                it.endTime?.let { endTime ->
                                    binding.itemZcbd.time.text = getDateStr(
                                        "HH:mm",
                                        startTime
                                    ) + "-" + getDateStr("HH:mm", endTime)
                                }
                            }
                            it.address?.let { address -> binding.itemZcbd.address.text = address }


                        }
                    } catch (e: java.lang.Exception) {

                    }

                }
                6 -> {
                    try {
                        //????????????
                        setStateColor(model.sys_liping, "1", binding.itemLpff.lpBtn)

                        item.userMeetingSignUp?.let {
                            state_liwu.status = "" + it.status
                            setStateColor(
                                model.sys_liping,
                                "" + it.status,
                                binding.itemLpff.lpBtn
                            )

                        }
                        state_liwu.userMeetingId = data.id
                        item.meetingSignUpLocation?.let {

                            eData(state_liwu, it)
                            binding.itemLpff.kong.visibility = View.GONE
                            binding.itemLpff.ll.visibility = View.VISIBLE
                            it.name?.let { name -> binding.itemLpff.name.text = name }
                            it.startTime?.let { startTime ->
                                binding.itemLpff.date.text = getDateStr("MM???dd", startTime)
                                it.endTime?.let { endTime ->
                                    binding.itemLpff.time.text = getDateStr(
                                        "HH:mm",
                                        startTime
                                    ) + "-" + getDateStr("HH:mm", endTime)
                                }

                            }
                            it.address?.let { address -> binding.itemLpff.address.text = address }


                        }
                    } catch (e: java.lang.Exception) {

                    }

                }
                3 -> {
                    try {
                        //????????????
                        setStateColor(model.sys_ruzhu, "1", binding.itemRzxx.ddBtn)

                        item.userMeetingSignUp?.let {

                            setStateColor(
                                model.sys_ruzhu,
                                "" + it.status,
                                binding.itemRzxx.ddBtn
                            )
                            for (list in model.sys_ruzhu) {
                                if (list.dictValue.equals("" + it.status)) {
                                    state_ruzhu.status = list.dictValue
                                    if (list.dictValue.equals("1")) {
                                        binding.itemRzxx.location.text =
                                            "???????????????"
                                    }
                                }
                            }
                        }
                        binding.itemRzxx.kong.visibility = View.GONE
                        binding.itemRzxx.ll.visibility = View.VISIBLE
                        item.meetingSignUpLocation?.let {
                            state_ruzhu.userMeetingId = data.id
                            eData(state_ruzhu, it)
                            it.name?.let { name ->
                                binding.itemRzxx.name.text = name
                            }

                            it.startTime?.let { startTime ->
                                binding.itemRzxx.date1.text = getDateStr(
                                    "MM???dd",
                                    startTime
                                ).toString()
                                it.endTime?.let { endTime ->
                                    binding.itemRzxx.day.text = "" + daydiff(
                                        startTime,
                                        endTime
                                    ) + "???"
                                }
                            }
                            it.endTime?.let { endTime ->
                                binding.itemRzxx.date2.text = getDateStr(
                                    "MM???dd",
                                    endTime
                                ).toString()
                            }
                            it.address?.let { address -> binding.itemRzxx.address.text = address }

                            it.location?.let { location ->
                                binding.itemRzxx.location.text =
                                    "????????????" + location
                            }


                        }
                    } catch (e: java.lang.Exception) {

                    }

                }
                4 -> {
                    try {
                        setStateColor(
                            model.sys_huichang,
                            "1",
                            binding.itemHcqd.zcBtn
                        )
                        state_huichang.userMeetingId = data.id

                        item.userMeetingSignUp?.let {

                            setStateColor(
                                model.sys_huichang,
                                "" + it.status,
                                binding.itemHcqd.zcBtn
                            )
                            for (list in model.sys_huichang) {
                                if (list.dictValue.equals("" + it.status)) {
                                    state_huichang.status = list.dictValue
                                    if (list.dictValue.equals("1")) {
                                        binding.itemHcqd.location.text =
                                            "???????????????"
                                    }
                                }
                            }
                        }
                        item.meetingSignUpLocation?.let {
                            eData(state_huichang, it)
                            state_huichang.userMeetingId = data.id
                            binding.itemHcqd.kong.visibility = View.GONE
                            binding.itemHcqd.ll.visibility = View.VISIBLE

                            binding.itemHcqd.name.text = "" + it.name
                            binding.itemHcqd.data.text = getDateStr(
                                "MM???dd",
                                it.startTime
                            )
                            binding.itemHcqd.time.text = getDateStr(
                                "HH:mm",
                                it.startTime
                            ).toString() + "-" + getDateStr(
                                "HH:mm",
                                it.endTime
                            ).toString()

                            binding.itemHcqd.address.text = it.address
                            it.location?.let { location ->
                                binding.itemHcqd.location.text =
                                    "????????????" + location

                            }
                        }


                    } catch (e: java.lang.Exception) {

                    }
                    //????????????

                }
                5 -> {
                    try {
                        //????????????
                        setStateColor(model.sys_canyin, "1", binding.itemCyxi.zcBtn)
                        item.userMeetingSignUp?.let {
                            setStateColor(
                                model.sys_canyin,
                                "" + it.status,
                                binding.itemCyxi.zcBtn
                            )
                            for (list in model.sys_canyin) {
                                if (list.dictValue.equals("" + it.status)) {
                                    state_chanyin.status = list.dictValue
                                    if (list.dictValue.equals("1")) {
                                        binding.itemCyxi.location.text =
                                            "???????????????"
                                    }
                                }
                            }
                        }
                        binding.itemCyxi.kong.visibility = View.GONE
                        binding.itemCyxi.ll.visibility = View.VISIBLE
                        item.meetingSignUpLocation?.let {
                            state_chanyin.userMeetingId = data.id

                            eData(state_chanyin, it)
                            it.name?.let { name -> binding.itemCyxi.name.text = name }
                            it.startTime?.let { startTime ->
                                binding.itemCyxi.date.text = getDateStr(
                                    "MM???dd",
                                    startTime
                                ).toString()
                                it.endTime?.let { endTime ->
                                    binding.itemCyxi.time.text = getDateStr(
                                        "HH:mm",
                                        startTime
                                    ).toString() + "-" + getDateStr(
                                        "HH:mm",
                                        endTime
                                    ).toString()
                                }

                            }

                            it.address?.let { address ->
                                binding.itemCyxi.address.text = address
                            }



                            it.location?.let { location ->
                                binding.itemCyxi.location.text =
                                    "??????" + it + "??????"
                                state_chanyin.location = location
                            }


                        }
                    } catch (e: java.lang.Exception) {

                    }

                }
                7 -> {
                    try {
                        //????????????
                        setStateColor(
                            model.sys_fancheng,
                            "1",
                            binding.itemFcxx.lcBtn
                        )
                        item.userMeetingSignUp?.let {
                            state_fancheng.status = "" + it.status
                            setStateColor(
                                model.sys_fancheng,
                                "" + it.status,
                                binding.itemFcxx.lcBtn
                            )
                        }
                        item.meetingSignUpLocation?.let {
                            state_fancheng.userMeetingId = data.id
                            eData(state_fancheng, it)
                        }

                        item.backUserMeetingTrip?.let {
                            binding.itemFcxx.kong.visibility = View.GONE
                            binding.itemFcxx.ll.visibility = View.VISIBLE

                            binding.itemFcxx.name.text = it.remark
                            binding.itemFcxx.lcData1.text =
                                getDateStr("MM???dd", it.startDate).toString()
                            binding.itemFcxx.lcDidian1.text = it.startCity
                            binding.itemFcxx.lcJichang1.text = it.startAddress
                            binding.itemFcxx.lcData2.text =
                                getDateStr("MM???dd", it.endDate).toString()
                            binding.itemFcxx.lcDidian2.text = it.endCity
                            binding.itemFcxx.lcJichang1.text = it.endAddress
                            binding.itemFcxx.jiedai.text = "???????????????" + item.personChargeName
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


                        }
                    } catch (e: java.lang.Exception) {

                    }
                }
                2 -> {
                    try {
                        setStateColor(
                            model.sys_laicheng,
                            "1",
                            binding.itemLcxx.lcBtn
                        )
                        //????????????
                        item.userMeetingSignUp?.let {
                            state_laicheng.status = "" + item.userMeetingSignUp.status
                            setStateColor(
                                model.sys_laicheng,
                                "" + item.userMeetingSignUp.status,
                                binding.itemLcxx.lcBtn
                            )
                        }
                        item.meetingSignUpLocation?.let {
                            state_laicheng.userMeetingId = data.id
                            eData(state_laicheng, it)
                        }
                        item.userMeetingTrip?.let {
                            binding.itemLcxx.kong.visibility = View.GONE
                            binding.itemLcxx.ll.visibility = View.VISIBLE
                            binding.itemLcxx.name.text = it.remark

                            binding.itemLcxx.lcData1.text =
                                getDateStr("MM???dd", it.startDate).toString()
                            binding.itemLcxx.lcDidian1.text = it.startCity
                            binding.itemLcxx.lcJichang1.text = it.startAddress
                            binding.itemLcxx.lcData2.text =
                                getDateStr("MM???dd", it.endDate).toString()
                            binding.itemLcxx.lcDidian2.text = it.endCity
                            binding.itemLcxx.lcJichang1.text = it.endAddress
                            binding.itemLcxx.jiedai.text = "????????????" + item.personChargeName
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


                        }
                    } catch (e: java.lang.Exception) {

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
        if (format.isNullOrEmpty() || dateStr.isNullOrEmpty()) {
            return ""
        }
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
        if (serverTime.isNullOrEmpty()) {
            return ""
        }
        var format = "yyyy-MM-dd HH:mm:ss"
        val sdf = SimpleDateFormat(format, Locale.CHINESE)
        sdf.timeZone = TimeZone.getTimeZone("GMT+8:00")
        var date: Date = Date()
        try {
            date = sdf.parse(serverTime)
        } catch (e: Exception) {

        }
        val formatter = SimpleDateFormat("MM???dd??? HH:mm")
        return formatter.format(date)

    }

    fun daydiff(fDateString: String, oDateString: String): Int {
        if (fDateString.isNullOrEmpty() || oDateString.isNullOrEmpty()) {
            return 0
        }
        val fDate = parseServerTime(fDateString)
        val oDate = parseServerTime(oDateString)
        val aCalendar = Calendar.getInstance()
        aCalendar.time = fDate
        val day1 = aCalendar[Calendar.DAY_OF_YEAR]
        aCalendar.time = oDate
        val day2 = aCalendar[Calendar.DAY_OF_YEAR]
        return day2 - day1
    }

    fun eData(d1: SignUpUser, d2: MeetingUserDeData.MeetingSignUpsBean.MeetingSignUpLocationBean) {
//        d1.meetingId = d2.meetingId
//        d1.signUpId = d2.signUpId
//        d1.signUpLocationId = d2.id
//        d1.meetingName = d2.name
//        d1.autoStatus = d2.autoStatus
//        d1.timeLong = d2.timeLong
//        d1.failedMsg = d2.failedMsg
//        d1.okMsg = d2.okMsg
//        d1.repeatMsg = d2.repeatMsg
        d2.meetingId?.let { d1.meetingId = it }
        d2.signUpId?.let { d1.signUpId = it }
        d2.id?.let { d1.signUpLocationId = it }
        d2.name?.let { d1.meetingName = it }
        d2.autoStatus?.let { d1.autoStatus = it }
        d2.timeLong?.let { d1.timeLong = it }
        d2.failedMsg?.let { d1.failedMsg = it }
        d2.okMsg?.let { d1.okMsg = it }
        d2.repeatMsg?.let { d1.repeatMsg = it }
        d2.speechStatus?.let { d1.voiceStatus = it }


    }
}