package com.example.signin

import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import android.util.Log
import android.view.View
import android.widget.TextView
import com.alibaba.fastjson.JSON
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CircleCrop
import com.bumptech.glide.request.RequestOptions
import com.dylanc.longan.startActivity
import com.example.signin.base.BaseBindingActivity
import com.example.signin.base.BaseViewModel
import com.example.signin.bean.*
import com.example.signin.databinding.ActMeetingUserInfoBinding
import com.example.signin.net.RequestCallback
import com.lzy.okgo.OkGo
import com.lzy.okgo.model.Response
import sigin
import java.math.BigDecimal
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class MeetingUserDectivity : BaseBindingActivity<ActMeetingUserInfoBinding, BaseViewModel>() {
    override fun getViewModel(): Class<BaseViewModel> = BaseViewModel::class.java

    //var data : MeetingUserDeData
    var id = ""
    var location = ""
    var showType = 0
    var state_dingdan = "1"
    var order: MeetingUserDeData.UserOrderBean = MeetingUserDeData.UserOrderBean()
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
        Log.d("getDataaaaaa", "getData()==" + id)
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
//1来程2返程
        binding.itemFcxx.edit.setOnClickListener {
            startActivity<XCActivity>(
                "type" to 2,
                "userMeetingId" to state_fancheng.userMeetingId
            )
        }
        binding.itemLcxx.edit.setOnClickListener {
            startActivity<XCActivity>(
                "type" to 1,
                "userMeetingId" to state_laicheng.userMeetingId
            )
        }
        binding.itemDdxx.ll.setOnClickListener {
            if (!examineStatus.equals("-1")) {
                startActivity<ExamineActivity>("order" to order)
            }

        }
        binding.itemDdxx.ddBtn.setOnClickListener {
            if (examineStatus.equals("2")) {
                if (cAmount > 0) {
                    if (invoiceStatus.equals("2")) {
                        startActivity<ExamineKPActivity>("order" to order)
                    }

                } else {
                    startActivity<ExamineActivity>("order" to order)
                }

            } else if (examineStatus.equals("1")) {
                startActivity<ExamineActivity>("order" to order)
            } else if (examineStatus.equals("-1")) {

            } else {
                startActivity<ExamineActivity>("order" to order)
            }
//            if (invoiceStatus.equals("5")) {
//                startActivity<ExamineKPActivity>("order" to order)
//            }

        }
        binding.itemLcxx.lcBtn.setOnClickListener {
            if (state_laicheng.status.equals("1")) {
                gotoSigin(state_laicheng, 2)

            }

        }
        binding.itemZcbd.zcBtn.setOnClickListener {
//            gotoSigin(state_zhuche, 1)
            if (state_zhuche.status.equals("2")) {
                gotoSigin(state_zhuche, 1)
//                startActivity<SiginReActivity>("type" to 1, "data" to state_zhuche)
            }
        }
        binding.itemRzxx.ddBtn.setOnClickListener {
            if (state_ruzhu.status.equals("1")) {
//                gotoSigin(state_ruzhu,3)
                if(location.isNullOrEmpty()){
                    state_ruzhu.success = "500"
                    startActivity<SiginReAutoActivity>(
                        "type" to 3,
                        "data" to state_ruzhu, "avatar" to avatar
                    )
                }else{
                    startActivity<SiginReActivity>(
                        "ruzhustatus" to "2",
                        "type" to 3,
                        "data" to state_ruzhu,
                        "avatar" to avatar
                    )
                }

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
            if (!mobile.isNullOrEmpty()) {
                takePhone(mobile)
            }

        }
        binding.itemYhxx.call.setOnClickListener {
            takePhone(mobile1)
        }
    }

    private fun gotoSigin(data: SignUpUser, type: Int) {
        getSiginData(data.signUpLocationId) {
            var params = HashMap<String, String>()
            params["meetingId"] = data.meetingId//会议id
            params["signUpLocationId"] = data.signUpLocationId//签到点id
            params["signUpId"] = data.signUpId//签到站id
            params["userMeetingId"] = data.userMeetingId//用户参与会议id
            params["status"] = "2"//用户参与会议id
            sigin(JSON.toJSONString(params), { success ->
                data.success = success
                startActivity<SiginReAutoActivity>(
                    "type" to type,
                    "data" to data, "avatar" to avatar
                )
            }, {
                data.success = "500"
                startActivity<SiginReAutoActivity>(
                    "type" to type,
                    "data" to data, "avatar" to avatar
                )
            }, {
                mViewModel.isShowLoading.value = false
            })

        }


    }

    fun takePhone(phone: String) {
        val uri = Uri.parse("tel:$phone") //设置要操作的路径
        val it = Intent()
        it.action = Intent.ACTION_DIAL
        it.data = uri
        startActivity(it)


    }

    var meetingUserDeData: MeetingUserDeData? = null
    private fun getData() {
        mViewModel.isShowLoading.value = true
        Log.d("getDataaaaaa", "getData()==" + id)
        OkGo.get<MeetingUserDeData>(PageRoutes.Api_meetinguser_data + id + "?id=" + id)
            .tag(PageRoutes.Api_meetinguser_data + id + "?id=" + id)
            .headers("Authorization", kv.getString("token", ""))
            .execute(object : RequestCallback<MeetingUserDeData>() {
                override fun onSuccessNullData() {
                    super.onSuccessNullData()

                }

                override fun onMySuccess(data: MeetingUserDeData) {
                    super.onMySuccess(data)
                    meetingUserDeData = data

                    try {
                        setDate(data)
                    } catch (e: java.lang.Exception) {
                        Log.d("Exception", "" + e?.message)
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
    var avatar = ""
    var cAmount: Double = 0.00

    @SuppressLint("SetTextI18n")
    private fun setDate(data: MeetingUserDeData) {
        data.corporateName?.let { corporateName ->
            binding.itemYhxx.gongshiName.text = corporateName
        }
        data.name?.let { name -> binding.itemYhxx.name.text = name }
        data.userMeetingTypeName?.let { userMeetingTypeName ->
            binding.itemYhxx.zhengjiangType.text = userMeetingTypeName
        }
        binding.itemYhxx.jiedaidName.visibility = View.GONE
        binding.itemYhxx.call1.visibility = View.GONE
        data.jiedai?.let {
            it.selectCheckboxParam?.let { param ->
                binding.itemYhxx.jiedaidName.text = "接待员：" + param.boxValue
                param.mobile?.let { m -> mobile = m }
                binding.itemYhxx.jiedaidName.visibility = View.VISIBLE
                binding.itemYhxx.call1.visibility = View.VISIBLE
            }

        }
        data.mobile?.let { m -> mobile1 = m }
        data.avatar?.let { avatar = it }
        Glide.with(this@MeetingUserDectivity).load(PageRoutes.BaseUrl + data.avatar).apply(
            RequestOptions.bitmapTransform(
                CircleCrop()
            )
        )
            .error(R.mipmap.touxiang).into(binding.itemYhxx.tx)
//        Glide.with(this@MeetingUserDectivity).load(PageRoutes.BaseUrl + data.avatar)
//            .error(R.mipmap.touxiang).into(binding.itemYhxx.tx)
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
            order = it
            order.supplement = data.userMeetingTypeName
            order.userName = data.name
            order.corporateName = data.corporateName
            binding.itemDdxx.kong.visibility = View.GONE
            binding.itemDdxx.ll.visibility = View.VISIBLE
            it.ticketName?.let { ticketName -> binding.itemDdxx.ddName.text = ticketName }
            it.amount?.let { amount ->
                binding.itemDdxx.ddPrice.text = "¥" + amount
                try {
                    cAmount = BigDecimal(amount).toDouble()
                } catch (e: Exception) {
                    Log.d("Exception", "" + e?.message)
                }

            }

            if (cAmount > 0) {
                binding.itemDdxx.ddType.text =
                    ""
                if (order.invoiceType.equals("1")) {
                    binding.itemDdxx.ddType.text = "" + "增值税普通发票"
                } else {
                    binding.itemDdxx.ddType.text = "" + "增值税专用发票"
                }
                //invoiceType	1 普票 2专票
//                for (item in model.sys_invoice_type) {
//                    if (it.invoiceType.equals(item.dictValue.trim())) {
//                        binding.itemDdxx.ddType.text =
//                            "开票类型:" + item.dictLabel
//                    }
//                }


            }

            it.createTime?.let { createTime ->
                binding.itemDdxx.ddTime.text = parseTime2(createTime)
            }

            //0初始状态 1待审核 2审核成功 3审核失败
            examineStatus = "" + it.examineStatus
            invoiceStatus = "" + it.invoiceStatus
            binding.itemDdxx.ddBtn.text = ""
            binding.itemDdxx.ddBtn.setBackgroundResource(R.drawable.shape_bg_ff3974f6_15)
            binding.infoLl.visibility = View.GONE
            if (examineStatus.equals("2")) {
                binding.infoLl.visibility = View.VISIBLE
                binding.itemDdxx.ddBtn.text = ""
                if (cAmount > 0) {
                    if (invoiceStatus.equals("2")) {
                        binding.itemDdxx.ddBtn.text = "开具发票"
                        binding.itemDdxx.ddBtn.setBackgroundResource(R.drawable.shape_bg_ff3974f6_15)

                    } else if (invoiceStatus.equals("5")) {
                        binding.itemDdxx.ddBtn.text = "已开票"
                        binding.itemDdxx.ddBtn.setBackgroundResource(R.drawable.shape_bg_999999_15)

                    } else {
                        binding.itemDdxx.ddBtn.text = "待申请"
                        binding.itemDdxx.ddBtn.setBackgroundResource(R.drawable.shape_bg_999999_15)
                    }
                } else {
                    binding.itemDdxx.ddBtn.text = "订单详情"
                    binding.itemDdxx.ddBtn.setBackgroundResource(R.drawable.shape_bg_ff3974f6_15)
                }
            } else if (examineStatus.equals("3")) {
                binding.itemDdxx.ddBtn.setBackgroundResource(R.drawable.shape_bg_999999_15)
                binding.itemDdxx.ddBtn.text = "审核失败"
            } else {
                binding.itemDdxx.ddBtn.setBackgroundResource(R.drawable.shape_bg_ff3974f6_15)
                binding.itemDdxx.ddBtn.text = "待审核"
            }
            if (data.userMeeting.status.equals("-1")) {
                binding.itemDdxx.ddBtn.setBackgroundResource(R.drawable.shape_bg_999999_15)
                binding.itemDdxx.ddBtn.text = "待付款"
                examineStatus = "-1"
            }
//            for (item in model.sys_invoice_status) {
//                if (examineStatus.equals(item.dictValue.trim())) {
//                    binding.itemDdxx.ddBtn.text = item.dictLabel
//                    if (item.dictValue.trim().equals("2")) {
//                        binding.infoLl.visibility = View.VISIBLE
//                        //'1未开发票 2申请中 3申请成功 4申请失败 5已开票
//                        if (invoiceStatus.equals("1")) {
//                            if (cAmount > 0) {
////                                binding.itemDdxx.ddBtn.text = "开具发票"
//                                binding.itemDdxx.ddBtn.text = "待申请"
//                                binding.itemDdxx.ddBtn.setBackgroundResource(R.drawable.shape_bg_999999_15)
//                            } else {
//                                binding.itemDdxx.ddBtn.text = "订单详情"
//                                binding.itemDdxx.ddBtn.setBackgroundResource(R.drawable.shape_bg_ff3974f6_15)
//
//                            }
//
//
//                        } else if (invoiceStatus.equals("2")) {
//                            binding.itemDdxx.ddBtn.text = "开具发票"
//                            binding.itemDdxx.ddBtn.setBackgroundResource(R.drawable.shape_bg_ff3974f6_15)
//                        }  else if (invoiceStatus.equals("5")) {
//                            binding.itemDdxx.ddBtn.text = "已开票"
//                            binding.itemDdxx.ddBtn.setBackgroundResource(R.drawable.shape_bg_999999_15)
//                        } else {
//                            binding.itemDdxx.ddBtn.text = "待申请"
//                            binding.itemDdxx.ddBtn.setBackgroundResource(R.drawable.shape_bg_999999_15)
//                        }
//
//                    }
//                    else if (item.dictValue.trim().equals("3")) {
//                        binding.itemDdxx.ddBtn.setBackgroundResource(R.drawable.shape_bg_999999_15)
//                    } else {
//                        binding.itemDdxx.ddBtn.setBackgroundResource(R.drawable.shape_bg_ff3974f6_15)
//
//                    }
//                }
//            }

        }
        data.userInvoice?.let {
            it.no?.let { invoiceNo -> binding.itemDdxx.ddNum.text = invoiceNo }
        }


        if (data.userOrder == null) {
            binding.itemDdxx.root.visibility = View.GONE
            binding.infoLl.visibility = View.VISIBLE
        }
        binding.itemZcbd.root.visibility = View.GONE
        binding.itemLpff.root.visibility = View.GONE
        binding.itemRzxx.root.visibility = View.GONE
        binding.itemHcqd.root.visibility = View.GONE
        binding.itemCyxi.root.visibility = View.GONE
        binding.itemFcxx.root.visibility = View.GONE
        binding.itemLcxx.root.visibility = View.GONE
//        var signUpType = kv.getString("signUpType","0")
        for (item in data.meetingSignUps) {
            when (item.type) {
                8 -> {
                    item.meetingSignUpLocation?.let {
                        it.name?.let { name -> order.meetingSignUpLocationName = name }
                    }
                }

                1 -> {

                    try {
                        //注册报到
                        binding.itemZcbd.kong.visibility = View.GONE
                        binding.itemZcbd.ll.visibility = View.VISIBLE
                        setStatezcColor(model.sys_zhuce, "1", binding.itemZcbd.zcBtn)

                        data.id?.let { state_zhuche.userMeetingId = data.id }
                        state_zhuche.status = "" + item.select
                        setStatezcColor(
                            model.sys_zhuce,
                            "" + item.select,
                            binding.itemZcbd.zcBtn
                        )

                        item.meetingSignUpLocation?.let {
                            state_zhuche.userMeetingId = data.id
                            eData(state_zhuche, it)
                            it.name?.let { name -> binding.itemZcbd.name.text = name }
                            it.startTime?.let { startTime ->
                                binding.itemZcbd.data.text =
                                    getDateStr("MM月dd", it.startTime)
                                it.endTime?.let { endTime ->
//                                    binding.itemZcbd.time.text = startTime + "-" +endTime
                                    binding.itemZcbd.time.text = getDateStr2(
                                        "HH:mm",
                                        startTime
                                    ) + "-" + getDateStr2("HH:mm", endTime)
                                }
                            }
                            it.address?.let { address -> binding.itemZcbd.address.text = address }


                        }
                    } catch (e: java.lang.Exception) {
                        Log.d("Exception", "" + e?.message)
                    }

                }

                6 -> {
                    try {

                        binding.itemLpff.kong.visibility = View.GONE
                        binding.itemLpff.ll.visibility = View.VISIBLE
                        //礼品发放
                        setStateColor(model.sys_liping, "1", binding.itemLpff.lpBtn)
                        state_liwu.status = "" + item.select
                        setStateColor(
                            model.sys_liping,
                            "" + item.select,
                            binding.itemLpff.lpBtn
                        )

                        state_liwu.userMeetingId = data.id
                        item.meetingSignUpLocation?.let {

                            eData(state_liwu, it)
                            binding.itemLpff.kong.visibility = View.GONE
                            binding.itemLpff.ll.visibility = View.VISIBLE
                            it.name?.let { name -> binding.itemLpff.name.text = name }
                            it.startTime?.let { startTime ->
                                binding.itemLpff.date.text = getDateStr("MM月dd", startTime)
                                it.endTime?.let { endTime ->
//                                    binding.itemLpff.time.text = startTime + "-" +endTime
                                    binding.itemLpff.time.text = getDateStr2(
                                        "HH:mm",
                                        startTime
                                    ) + "-" + getDateStr2("HH:mm", endTime)
                                }

                            }
                            it.address?.let { address -> binding.itemLpff.address.text = address }
                            it.location?.let { location ->
                                binding.itemLpff.location.text = location
                            }


                        }
                    } catch (e: java.lang.Exception) {
                        Log.d("Exception", "" + e?.message)
                    }

                }

                3 -> {
                    try {

                        binding.itemRzxx.kong.visibility = View.VISIBLE
                        binding.itemRzxx.ll.visibility = View.GONE
                        item.userMeetingAccommodation?.let { userMeetingAccommodation ->
                            binding.itemRzxx.kong.visibility = View.GONE
                            binding.itemRzxx.ll.visibility = View.VISIBLE
                            //入住签到

                            state_ruzhu.status = "" + item.select
                            setStateColor(
                                model.sys_ruzhu,
                                state_ruzhu.status,
                                binding.itemRzxx.ddBtn
                            )
                            item.userMeetingSignUp?.let {


                                for (list in model.sys_ruzhu) {
                                    if (list.dictValue.equals("" + item.select)) {

                                        if (list.dictValue.equals("1")) {
                                            binding.itemRzxx.location.text =
                                                "房间号：暂无"
                                        } else {
                                            binding.itemRzxx.location.text =
                                                "房间号：暂无"
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

                            }


                            userMeetingAccommodation.startTime?.let { startTime ->
                                binding.itemRzxx.date1.text = getDateStr(
                                    "MM月dd",
                                    startTime
                                ).toString()
                                userMeetingAccommodation.endTime?.let { endTime ->
                                    binding.itemRzxx.day.text = "" + daydiff(
                                        startTime,
                                        endTime
                                    ) + "天"
                                }
                            }
                            userMeetingAccommodation.endTime?.let { endTime ->
                                binding.itemRzxx.date2.text = getDateStr(
                                    "MM月dd",
                                    endTime
                                ).toString()
                            }
                            userMeetingAccommodation.accommodation?.let { address ->
                                binding.itemRzxx.address.text = address
                            }

                            userMeetingAccommodation.roomNo?.let { l ->
                                location = l
                                binding.itemRzxx.location.text =
                                    "房间号：" + l
                            }

                        }

                    } catch (e: java.lang.Exception) {
                        Log.d("Exception", "" + e?.message)
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
                        setStateColor(
                            model.sys_huichang,
                            "" + item.select,
                            binding.itemHcqd.zcBtn
                        )
                        state_huichang.status = "" + item.select
//                        item.userMeetingSignUp?.let {
//
//
//                            for (list in model.sys_huichang) {
//                                if (list.dictValue.equals("" + item.select)) {
//
//                                    binding.itemHcqd.location.text =
//                                        "待分配"
//                                }
//                            }
//                        }
                        item.meetingSignUpLocation?.let {
                            eData(state_huichang, it)
                            state_huichang.userMeetingId = data.id
                            binding.itemHcqd.kong.visibility = View.GONE
                            binding.itemHcqd.ll.visibility = View.VISIBLE

                            binding.itemHcqd.name.text = "" + it.name
                            it.startTime?.let { startTime ->
                                binding.itemHcqd.data.text = getDateStr(
                                    "MM月dd",
                                    startTime
                                )
//                            binding.itemHcqd.time.text = it.startTime + "-" +it.endTime
                                binding.itemHcqd.time.text = getDateStr2(
                                    "HH:mm",
                                    startTime
                                ).toString() + "-" + getDateStr2(
                                    "HH:mm",
                                    it.endTime
                                ).toString()

                            }
                            it.address?.let { address -> binding.itemHcqd.address.text = address }

                            it.location?.let { location ->
                                binding.itemHcqd.location.text =
                                    "座位号：" + location

                            }
                        }


                    } catch (e: java.lang.Exception) {
                        Log.d("Exception", "" + e?.message)
                    }
                    //会场签到

                }

                5 -> {
                    try {

                        //餐饮签到
                        setStateColor(model.sys_canyin, "1", binding.itemCyxi.zcBtn)
                        setStateColor(
                            model.sys_canyin,
                            "" + item.select,
                            binding.itemCyxi.zcBtn
                        )
                        state_chanyin.status = "" + item.select
//                        item.userMeetingSignUp?.let {
//
//                            for (list in model.sys_canyin) {
//                                if (list.dictValue.equals("" + item.select)) {
//
//                                    binding.itemCyxi.location.text =
//                                        "待分配"
//                                }
//                            }
//                        }
                        binding.itemCyxi.kong.visibility = View.GONE
                        binding.itemCyxi.ll.visibility = View.VISIBLE
                        item.meetingSignUpLocation?.let {
                            state_chanyin.userMeetingId = data.id

                            eData(state_chanyin, it)
                            it.name?.let { name -> binding.itemCyxi.name.text = name }
                            it.startTime?.let { startTime ->
                                binding.itemCyxi.date.text = getDateStr(
                                    "MM月dd",
                                    startTime
                                ).toString()
                                it.endTime?.let { endTime ->
//                                    binding.itemCyxi.time.text = startTime + "-" +endTime
                                    binding.itemCyxi.time.text = getDateStr2(
                                        "HH:mm",
                                        startTime
                                    ).toString() + "-" + getDateStr2(
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
                                    "请到" + location + "号桌"
                                state_chanyin.location = location
                            }


                        }
                    } catch (e: java.lang.Exception) {
                        Log.d("Exception", "" + e?.message)
                    }

                }

                7 -> {
                    try {

                        //返程签到
                        setStateColor(
                            model.sys_fancheng,
                            "1",
                            binding.itemFcxx.lcBtn
                        )
                        state_fancheng.status = "" + item.select
                        setStateColor(
                            model.sys_fancheng,
                            "" + item.select,
                            binding.itemFcxx.lcBtn
                        )
                        item.meetingSignUpLocation?.let {
                            state_fancheng.userMeetingId = data.id
                            eData(state_fancheng, it)
                        }

                        item.backUserMeetingTrip?.let {
                            binding.itemFcxx.kong.visibility = View.GONE
                            binding.itemFcxx.ll.visibility = View.VISIBLE
                            it.remark?.let { remark -> binding.itemFcxx.name.text = remark }

                            it.startDate?.let { startDate ->
                                binding.itemFcxx.lcData1.text =
                                    getDateStr("MM月dd", startDate).toString()
                            }
                            it.startCity?.let { startCity ->
                                binding.itemFcxx.lcDidian1.text = startCity
                            }
                            it.startAddress?.let { startAddress ->
                                binding.itemFcxx.lcJichang1.text = startAddress
                            }

//                            binding.itemFcxx.lcJichang1.text = it.startAddress
                            it.endDate?.let { endDate ->
                                binding.itemFcxx.lcData2.text =
                                    getDateStr("MM月dd", endDate).toString()
                            }
                            it.endCity?.let { endCity -> binding.itemFcxx.lcDidian2.text = endCity }
                            it.endAddress?.let { endAddress ->
                                binding.itemFcxx.lcJichang2.text = endAddress
                            }

                            it.startTime?.let { startTime ->
                                binding.itemFcxx.time.text = startTime + "-" + it.endTime
                            }

//                            binding.itemFcxx.time.text = getDateStr2(
//                                "HH:mm",
//                                it.startTime
//                            ).toString() + "-" + getDateStr2("HH:mm", it.endTime).toString()
                            //personChargeMobile status

                            for (item in model.transport_type) {
                                if (it.transport.equals(item.dictValue.trim())) {
                                    if (item.dictValue.trim().equals("03")) {
                                        binding.itemFcxx.icon.setImageResource(R.mipmap.laichengxingxi)
                                    } else {
                                        binding.itemFcxx.icon.setImageResource(R.mipmap.hc)
                                    }

                                }
                            }


                        }
                        binding.itemFcxx.jiedai.visibility = View.GONE
                        binding.itemFcxx.call1.visibility = View.GONE
                        data.backSiji?.let {
                            it.selectCheckboxParam?.let { param ->
                                binding.itemFcxx.jiedai.text = "司机：" + param.boxValue
                                param.mobile?.let { m -> mobile3 = m }
                                binding.itemFcxx.jiedai.visibility = View.VISIBLE
                                binding.itemFcxx.call1.visibility = View.VISIBLE
                                binding.itemFcxx.jiedaill.visibility = View.VISIBLE
                            }

                        }

                    } catch (e: java.lang.Exception) {
                        Log.d("Exception", "" + e?.message)
                    }
                }

                2 -> {
                    try {

                        setStateColor(
                            model.sys_laicheng,
                            "1",
                            binding.itemLcxx.lcBtn
                        )
                        //来程签到
                        state_laicheng.status = "" + item.select
                        setStateColor(
                            model.sys_laicheng,
                            "" + item.select,
                            binding.itemLcxx.lcBtn
                        )

                        item.meetingSignUpLocation?.let {
                            state_laicheng.userMeetingId = data.id
                            eData(state_laicheng, it)
                        }
                        item.userMeetingTrip?.let {
                            binding.itemLcxx.kong.visibility = View.GONE
                            binding.itemLcxx.ll.visibility = View.VISIBLE
                            binding.itemLcxx.name.text = it.remark
                            it.startDate?.let { startDate ->
                                binding.itemLcxx.lcData1.text =
                                    getDateStr("MM月dd", startDate).toString()
                            }

//                            binding.itemLcxx.lcDidian1.text = it.startCity
//                            binding.itemLcxx.lcJichang1.text = it.startAddress
                            it.startCity?.let { startCity ->
                                binding.itemLcxx.lcDidian1.text = startCity
                            }
                            it.startAddress?.let { startAddress ->
                                binding.itemLcxx.lcJichang1.text = startAddress
                            }

                            it.endDate?.let { endDate ->
                                binding.itemLcxx.lcData2.text =
                                    getDateStr("MM月dd", endDate).toString()
                            }
                            it.endCity?.let { endCity -> binding.itemLcxx.lcDidian2.text = endCity }
                            it.endAddress?.let { endAddress ->
                                binding.itemLcxx.lcJichang2.text = endAddress
                            }

                            it.startTime?.let { startTime ->
                                binding.itemLcxx.time.text = startTime + "-" + it.endTime
                            }

//                            binding.itemLcxx.time.text = getDateStr2(
//                                "HH:mm",
//                                it.startTime
//                            ).toString() + "-" + getDateStr2("HH:mm", it.endTime).toString()

                            //personChargeMobile status

                            for (item in model.transport_type) {
                                if (it.transport.equals(item.dictValue.trim())) {
                                    if (item.dictValue.trim().equals("03")) {
                                        binding.itemLcxx.icon.setImageResource(R.mipmap.laichengxingxi)
                                    } else {
                                        binding.itemLcxx.icon.setImageResource(R.mipmap.hc)
                                    }

                                }
                            }


                        }

                        binding.itemFcxx.jiedai.visibility = View.GONE
                        binding.itemFcxx.call1.visibility = View.GONE
                        data.tripSiji?.let {
                            it.selectCheckboxParam?.let { param ->
                                binding.itemLcxx.jiedai.text = "司机：" + param.boxValue
//                                mobile2 = param.mobile
                                param.mobile?.let { m -> mobile2 = m }
                                binding.itemLcxx.jiedai.visibility = View.VISIBLE
                                binding.itemLcxx.call1.visibility = View.VISIBLE
                                binding.itemLcxx.jiedaill.visibility = View.VISIBLE
                            }

                        }
                    } catch (e: java.lang.Exception) {
                        Log.d("Exception", "" + e?.message)
                    }
                }
            }
        }
        var userData = JSON.parseObject(kv.getString("userData", ""), User::class.java)
        userData?.let { uDara ->
            if (uDara.userType == "06") {
                for (type in uDara.types) {
                    when (type) {
                        "1" -> {
                            binding.itemZcbd.root.visibility = View.VISIBLE
                        }

                        "2" -> {
                            binding.itemLcxx.root.visibility = View.VISIBLE
                        }

                        "3" -> {
                            binding.itemRzxx.root.visibility = View.VISIBLE
                        }

                        "4" -> {
                            binding.itemHcqd.root.visibility = View.VISIBLE
                        }

                        "5" -> {
                            binding.itemCyxi.root.visibility = View.VISIBLE
                        }

                        "6" -> {
                            binding.itemLpff.root.visibility = View.VISIBLE
                        }

                        "7" -> {
                            binding.itemFcxx.root.visibility = View.VISIBLE
                        }
                    }
                }

            } else {
                for (item in data.meetingSignUps) {
                    when (item.type) {
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
            }
        }


    }

    override fun onResume() {
        super.onResume()
        Log.d("getDataaaaaa", "onResume()==" + id)
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

    private fun setStatezcColor(
        lists: List<TypeData>,
        status: String,
        tv: TextView
    ) {
        for (list in lists) {
            if (list.dictValue.equals(status)) {
                tv.text = list.dictLabel
                if (!list.dictValue.equals("2")) {
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

        var format = "yyyy-MM-dd"
        val sdf = SimpleDateFormat(format, Locale.CHINESE)
        sdf.timeZone = TimeZone.getTimeZone("GMT+8:00")
        var date: Date = Date()
        try {
            date = sdf.parse(serverTime)
        } catch (e: Exception) {

        }
        return date
    }

    fun getDateStr2(format: String, dateStr: String): String {
        if (format.isNullOrEmpty() || dateStr.isNullOrEmpty()) {
            return ""
        }
        var date = parseServerTime2(dateStr)
        var format = format
        if (format == null || format.isEmpty()) {
            format = "MM-dd"
        }
        val formatter = SimpleDateFormat(format)
        return formatter.format(date)
    }

    fun parseServerTime2(serverTime: String): Date {

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
        val formatter = SimpleDateFormat("MM月dd日 HH:mm")
        return formatter.format(date)

    }

    fun parseTime2(serverTime: String): String {
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
        val formatter = SimpleDateFormat("MM月dd日")
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
        var numdata = day2 - day1
        if (numdata < 1) {
            numdata = 1
        }
        return numdata
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

    var meetingFormData: MeetingFormData? = null
    private fun getSiginData(signUpLocationId: String, back: () -> Unit) {
        mViewModel.isShowLoading.value = true
        OkGo.get<SiginData>(PageRoutes.Api_meetingSignUpLocationDe + signUpLocationId)
            .tag(PageRoutes.Api_meetingSignUpLocationDe)
            .headers("Authorization", kv.getString("token", ""))
            .execute(object : RequestCallback<SiginData>() {
                override fun onSuccessNullData() {
                    super.onSuccessNullData()

                }

                override fun onMySuccess(data: SiginData) {
                    super.onMySuccess(data)
                    data.meetingSignUpLocationConfig?.let {
                        if (it.printModel == 1) {
                            kv.putBoolean("printZd", true)
                        } else {
                            kv.putBoolean("printZd", false)
                        }
                        if (it.printStatus == 1) {
                            kv.putBoolean("printStatus", true)
                        } else {
                            kv.putBoolean("printStatus", false)
                        }

                    }
                    meetingFormData = MeetingFormData()
                    meetingFormData?.meetingFormList = data.meetingFormList

                    meetingFormData?.let {
                        meetingUserDeData?.let { meetingUserDeData ->
                            for (list in it.meetingFormList) {
                                for (listFrom in meetingUserDeData.userMeetingForms) {
                                    if (list.name.equals(listFrom.name)) {
                                        if (listFrom.selectCheckboxParam != null) {
                                            list.value = listFrom.selectCheckboxParam.boxValue
                                        } else {
                                            list.value = listFrom.value
                                        }
                                    }

                                }
                            }
                            kv.putString("MeetingFormData", JSON.toJSONString(meetingFormData))
                        }


                    }
                    back.invoke()
                }

                override fun onError(response: Response<SiginData>) {
                    super.onError(response)
                    meetingFormData = MeetingFormData()
                    meetingFormData?.meetingFormList = ArrayList<MeetingFormList>()
                    meetingFormData?.let {
                        kv.putString(
                            "MeetingFormData",
                            JSON.toJSONString(meetingFormData)
                        )
                    }
                    back.invoke()
                }


            })
    }
}