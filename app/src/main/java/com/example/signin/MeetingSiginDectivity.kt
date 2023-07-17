package com.example.signin

import android.content.BroadcastReceiver
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.content.IntentFilter
import android.media.MediaPlayer
import android.util.Log
import android.view.KeyEvent
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.alibaba.fastjson.JSON
import com.common.face.api.FaceUtil
import com.dylanc.longan.activity
import com.dylanc.longan.startActivity
import com.dylanc.longan.toast
import com.example.signin.adapter.FMeetingDeList3Adapter
import com.example.signin.adapter.SelectMeetingAdapter2
import com.example.signin.base.BaseBindingActivity
import com.example.signin.base.BaseViewModel
import com.example.signin.bean.*
import com.example.signin.databinding.ActMeetingSigindeBinding
import com.example.signin.net.RequestCallback
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.hello.scan.ScanCallBack
import com.hello.scan.ScanTool
import com.hjq.permissions.OnPermissionCallback
import com.hjq.permissions.Permission
import com.hjq.permissions.XXPermissions
import com.jeremyliao.liveeventbus.LiveEventBus
import com.lzy.okgo.OkGo
import com.lzy.okgo.model.Response
import sigin
import java.io.File
import java.io.UnsupportedEncodingException


class MeetingSiginDectivity : BaseBindingActivity<ActMeetingSigindeBinding, BaseViewModel>(),
    ScanCallBack {
    override fun getViewModel(): Class<BaseViewModel> = BaseViewModel::class.java

    var id = ""
    var signUpId = ""
    var meetingid = ""
    var autoStatus = ""
    var timeLong = 3
    var showType = 0
    var name = ""
    var signUpStatus = ""
    var nameMobile: String? = ""
    var params: String? = ""
    var failedMsg: String = if (signUpStatus == "2") "签出失败" else "签到失败"
    var okMsg: String = if (signUpStatus == "2") "签出成功" else "签到成功"
    var repeatMsg: String = if (signUpStatus == "2") "重复签出" else "重复签到 "

    //    var okMsg: String = "签到成功"
//    var repeatMsg: String = "重复签到"
    var voiceStatus: String = "2"
    private var list: MutableList<MeetingUserData> = ArrayList()
    private var adapter: FMeetingDeList3Adapter? = null
    private var adapterSelect3: SelectMeetingAdapter2? = null
    private var selectList3: MutableList<SiginData> = ArrayList()
//    private var mDecodeReader: DecodeReader? = null
    var moshi: String? = ""
    override fun initData() {
        mViewModel.isShowLoading.value = true
        intent.getStringExtra("id")?.let { id = it }
        intent.getStringExtra("name")?.let {
            name = it
            binding.name.text = it
        }
        intent.getStringExtra("params")?.let { params = it }
        intent.getStringExtra("meetingid")?.let { meetingid = it }
        intent.getStringExtra("signUpId")?.let { signUpId = it }
        intent.getStringExtra("autoStatus")?.let { autoStatus = it }
        intent.getStringExtra("okMsg")?.let { okMsg = it }
        intent.getStringExtra("failedMsg")?.let { failedMsg = it }
        intent.getStringExtra("repeatMsg")?.let { repeatMsg = it }
        intent.getStringExtra("voiceStatus")?.let { voiceStatus = it }
        intent.getIntExtra("timeLong", 3)?.let { timeLong = it }
        intent.getIntExtra("showType", 0)?.let { showType = it }
//        LiveDataBus.get().with("JWebSocketClientRefresh", String::class.java)
//        LiveDataBus.get().with("JWebSocketClientlocation", String::class.java)
//            .observeForever {
                LiveEventBus
                    .get<String>("JWebSocketClientlocation", String::class.java)
                    .observe(this) {
                try {
                    if (AppManager.getAppManager()
                            .activityInstanceIsLive(MeetingSiginDectivity@ this)
                    ) {
                        getSiginData()
                    }
                } catch (e: java.lang.Exception) {
                }

            }
        var num = ""
        if (showType == 1) {
            num = "注册签到"
        } else if (showType == 2) {
            num = "来程签到"
        } else if (showType == 3) {
            num = "入住签到"
        } else if (showType == 4) {
            num = "会场签到"
        } else if (showType == 5) {
            num = "餐饮签到"
        } else if (showType == 6) {
            num = "礼品签到"
        } else if (showType == 7) {
            num = "返程签到"
        } else if (showType == 8) {
            num = "发票签到"
        }
        binding.title.text = num
        registerReceiver()
        moshi = kv.getString("shaomamoshi", "激光头识别")
        if (moshi.equals("激光头识别")) {
            var a = SiginData()
            a.name = "二维码识别"
            a.isKuan = true
            selectList3.add(a)
            var a1 = SiginData()
            a1.name = "摄像头识别"
            a1.isKuan = true
            selectList3.add(a1)
            var a2 = SiginData()
            a2.name = "激光头识别"
            a2.isMyselect = true
            a2.isKuan = true
            selectList3.add(a2)
//            openHardreader()

        } else if (moshi.equals("二维码识别")) {
            var a = SiginData()
            a.name = "二维码识别"
            a.isMyselect = true
            a.isKuan = true
            selectList3.add(a)

            var a1 = SiginData()
            a1.name = "摄像头识别"
            a1.isKuan = true
            selectList3.add(a1)
            var a2 = SiginData()
            a2.name = "激光头识别"
            a2.isKuan = true
            selectList3.add(a2)
            ScanTool.GET.initSerial(
                this@MeetingSiginDectivity,
                "/dev/ttyACM0",
                115200,
                this@MeetingSiginDectivity
            )
            ScanTool.GET.playSound(false)
        } else if (moshi.equals("摄像头识别")) {
            var a = SiginData()
            a.name = "二维码识别"
            a.isKuan = true
            selectList3.add(a)

            var a1 = SiginData()
            a1.name = "摄像头识别"
            a1.isMyselect = true
            a1.isKuan = true
            selectList3.add(a1)
            var a2 = SiginData()
            a2.isKuan = true
            a2.name = "激光头识别"
            selectList3.add(a2)

        } else {
            var a = SiginData()
            a.name = "二维码识别"
            a.isKuan = true
            selectList3.add(a)
            var a1 = SiginData()
            a1.name = "摄像头识别"
            a1.isKuan = true
            selectList3.add(a1)
            var a2 = SiginData()
            a2.name = "激光头识别"
            a2.isKuan = true
            selectList3.add(a2)
        }




        binding.srecyclerview.layoutManager = LinearLayoutManager(activity)
        adapterSelect3 = SelectMeetingAdapter2().apply {
            submitList(selectList3)
            setOnItemClickListener { _, _, position ->
                binding.selectLl.visibility = View.GONE
                for (item in selectList3) {
                    item.isMyselect = false
                }
                selectList3[position].isMyselect = true
                moshi = selectList3[position].name
                kv.putString("shaomamoshi", moshi)
                adapterSelect3?.notifyDataSetChanged()
                if (moshi.equals("激光头识别")) {
                    ScanTool.GET.release()
//                    openHardreader()
                } else if (moshi.equals("二维码识别")) {
//                    mDecodeReader?.close()
//                    mDecodeReader = null
                    ScanTool.GET.initSerial(
                        this@MeetingSiginDectivity,
                        "/dev/ttyACM0",
                        115200,
                        this@MeetingSiginDectivity
                    )
                    ScanTool.GET.playSound(false)
                } else {
//                    mDecodeReader?.close()
//                    mDecodeReader = null
                    ScanTool.GET.release()
                }
            }
        }
        binding.srecyclerview.adapter = adapterSelect3
        binding.ll.setOnClickListener {
            binding.selectLl.visibility = View.VISIBLE
        }
        binding.selectLl.setOnClickListener {
            binding.selectLl.visibility = View.GONE
        }

        binding.recyclerview.layoutManager = LinearLayoutManager(activity)
        adapter = FMeetingDeList3Adapter().apply {
            submitList(list)
            setOnItemClickListener { _, _, position ->
                com.dylanc.longan.startActivity<MeetingUserDectivity>(
                    "id" to list[position].id.toString(),
                    "showType" to showType
                )
            }
        }
        setSelect2Data(showType)
        binding.recyclerview.adapter = adapter

        binding.sous.setOnClickListener {
            nameMobile = binding.et.text.toString().trim()
//            binding.et.setText(nameMobile)
//            list.clear()
//            getList()
            activity?.hideSoftInput()
            var url = PageRoutes.Api_meeting_sign_up_data_list + params
            if (!nameMobile.isNullOrEmpty()) {
                url = "$url&nameMobile=$nameMobile"
            }
            com.dylanc.longan.startActivity<SiginUserActivity>(
                "url" to url,
                "showType" to showType,
                "name" to name
            )
        }
        binding.et.setOnKeyListener(View.OnKeyListener { v, keyCode, event ->
            if (keyCode == KeyEvent.KEYCODE_ENTER) {
                // 监听到回车键，会执行2次该方法。按下与松开
                if (event.action == KeyEvent.ACTION_UP) {
                    nameMobile = binding.et.text.toString().trim()

                    activity?.hideSoftInput()
                    var url = PageRoutes.Api_meeting_sign_up_data_list + params
                    if (!nameMobile.isNullOrEmpty()) {
                        url = "$url&nameMobile=$nameMobile"
                    }
                    com.dylanc.longan.startActivity<SiginUserActivity>(
                        "url" to url,
                        "showType" to showType,
                        "name" to name
                    )

                }
            }
            false
        })
        signUpStatus = ""+ kv.getString("zd_signUpStatus_"+meetingid + id, "1")
        if (signUpStatus == "2") {
            binding.moshitv.text = "签出模式"
            binding.moshiiv.setImageResource(R.mipmap.kaiguanguan)
        } else {
            binding.moshitv.text = "签入模式"
            binding.moshiiv.setImageResource(R.mipmap.kaiguank)
        }
        failedMsg = if (signUpStatus == "2") "签出失败" else "签到失败"
        okMsg = if (signUpStatus == "2") "签出成功" else "签到成功"
        repeatMsg = if (signUpStatus == "2") "重复签出" else "重复签到 "
        getSiginData()

        binding.sm.setOnClickListener {
            if (moshi.equals("识别模式")) {
                toast("请选择识别模式")
                return@setOnClickListener
            }
            if (!moshi.equals("摄像头识别")) {
                return@setOnClickListener
            }
            activity?.let {

                XXPermissions.with(activity)
                    .permission(Permission.CAMERA)
                    .request(object : OnPermissionCallback {

                        override fun onGranted(permissions: MutableList<String>, all: Boolean) {
                            if (!all) {
                                toast("获取权限失败")
                            } else {
                                com.dylanc.longan.startActivity<ScanActivity>(
                                    "id" to id,
                                    "name" to "" + name,
                                    "params" to "" + params,
                                    "meetingid" to "" + meetingid,
                                    "autoStatus" to "" + autoStatus,
                                    "voiceStatus" to "" + voiceStatus,
                                    "timeLong" to timeLong,
                                    "showType" to showType,
                                    "okMsg" to "" + okMsg,
                                    "failedMsg" to "" + failedMsg,
                                    "repeatMsg" to "" + repeatMsg,
                                    "signUpId" to "" + signUpId,
                                    "meetingSignUpLocationName" to "" + binding.name.text.toString()
                                )
//                                    var intent = Intent(it,ScanActivity::class.java)
//                                    startActivityForResult(intent,1000)
                            }

                        }

                        override fun onDenied(permissions: MutableList<String>, never: Boolean) {
                            toast("获取权限失败")

                        }
                    })

            }
        }
        binding.moshill.setOnClickListener {
            MaterialAlertDialogBuilder(this)
                .setTitle("提示")
                .setMessage("是否切换签入/签出模式")
                .setNegativeButton("否") { dialog, which ->
                    dialog.dismiss()
                }
                .setPositiveButton("是") { dialog: DialogInterface?, which: Int ->
                    setState()
                }
                .show()


        }
        binding.shaoma.setOnClickListener {
            if (moshi.equals("识别模式") || moshi.isNullOrEmpty()) {
                toast("请选择识别模式")
                return@setOnClickListener
            }
            if (!moshi.equals("摄像头识别")) {
                return@setOnClickListener
            }
            activity?.let {

                XXPermissions.with(activity)
                    .permission(Permission.CAMERA)
                    .request(object : OnPermissionCallback {

                        override fun onGranted(permissions: MutableList<String>, all: Boolean) {
                            if (!all) {
                                toast("获取权限失败")
                            } else {
                                com.dylanc.longan.startActivity<ScanActivity>(
                                    "id" to id,
                                    "name" to "" + name,
                                    "params" to "" + params,
                                    "meetingid" to "" + meetingid,
                                    "autoStatus" to "" + autoStatus,
                                    "voiceStatus" to "" + voiceStatus,
                                    "timeLong" to timeLong,
                                    "showType" to showType,
                                    "okMsg" to "" + okMsg,
                                    "failedMsg" to "" + failedMsg,
                                    "repeatMsg" to "" + repeatMsg,
                                    "signUpId" to "" + signUpId,
                                    "meetingSignUpLocationName" to "" + binding.name.text.toString()
                                )
//                                    var intent = Intent(it,ScanActivity::class.java)
//                                    startActivityForResult(intent,1000)
                            }

                        }

                        override fun onDenied(permissions: MutableList<String>, never: Boolean) {
                            toast("获取权限失败")

                        }
                    })

            }


        }

    }

    private var siginUp2List: MutableList<TypeData> = ArrayList()
    private fun setSelect2Data(type: Int) {
        //1 注册签到2 来程签到3 入住签到4 会场签到5 餐饮签到6 礼品签到7 返程签到
        var model: TypeModel
        if (!kv.getString("TypeModel", "").isNullOrEmpty()) {
            model = JSON.parseObject(kv.getString("TypeModel", ""), TypeModel::class.java)
            siginUp2List.clear()
            when (type) {
                1 -> {
                    siginUp2List.addAll(model.sys_zhuce)
                }
                2 -> {
                    siginUp2List.addAll(model.sys_laicheng)
                }
                3 -> {
                    siginUp2List.addAll(model.sys_ruzhu)
                }
                4 -> {
                    siginUp2List.addAll(model.sys_huichang)
                }
                5 -> {
                    siginUp2List.addAll(model.sys_canyin)
                }
                6 -> {
                    siginUp2List.addAll(model.sys_liping)
                }
                7 -> {
                    siginUp2List.addAll(model.sys_fancheng)
                }
                8 -> {
                    siginUp2List.addAll(model.sys_fapiao)
                }
            }
        }

        adapter?.setSiginUp2List(siginUp2List)
    }



    //    var addressStatus = "2"
    fun setState() {
        signUpStatus = ""+ kv.getString("zd_signUpStatus_"+meetingid + id, "1")
        if (signUpStatus.equals("1")) {
            kv.putString("zd_signUpStatus_"+meetingid + id, "2")
            signUpStatus = "2"
        } else {
            kv.putString("zd_signUpStatus_"+meetingid + id, "1")
            signUpStatus = "1"
        }

        if (signUpStatus == "2") {
            binding.moshitv.text = "签出模式"
            binding.moshiiv.setImageResource(R.mipmap.kaiguanguan)
        } else {
            binding.moshitv.text = "签入模式"
            binding.moshiiv.setImageResource(R.mipmap.kaiguank)
        }
        failedMsg = if (signUpStatus == "2") "签出失败" else "签到失败"
        okMsg = if (signUpStatus == "2") "签出成功" else "签到成功"
        repeatMsg = if (signUpStatus == "2") "重复签出" else "重复签到"

    }

    var meetingFormData: MeetingFormData? = null
    private fun getSiginData() {
        Log.d("getSiginData==","getSiginData====开始")
        Log.d("getSiginData==",PageRoutes.Api_meetingSignUpLocationDe + id)
        kv.getString("token", "")?.let { Log.d("getSiginData==", it) }
        mViewModel.isShowLoading.value = true
        OkGo.get<SiginData>(PageRoutes.Api_meetingSignUpLocationDe + id)
            .tag(PageRoutes.Api_meetingSignUpLocationDe)
            .headers("Authorization", kv.getString("token", ""))
            .execute(object : RequestCallback<SiginData>() {
                override fun onSuccessNullData() {
                    super.onSuccessNullData()

                }

                override fun onMySuccess(data: SiginData) {
                    super.onMySuccess(data)
                    Log.d("getSiginData==","getSiginData====onMySuccess")
                    try {
                        meetingFormData = MeetingFormData()
                        meetingFormData?.meetingFormList = data.meetingFormList
                        kv.putString("MeetingFormData", JSON.toJSONString(meetingFormData))
                        data.meetingSignUpLocationConfig?.let {
                            if(it.printModel==1){
                                kv.putBoolean("printZd",true)
                            }else{
                                kv.putBoolean("printZd",false)
                            }
                            if(it.printStatus==1){
                                kv.putBoolean("printStatus",true)
                            }else{
                                kv.putBoolean("printStatus",false)
                            }

                        }

                        binding.num1.text = "" + data.beUserCount
                        binding.num2.text = "" + data.signUpCount
                        binding.num3.text = "" + data.localSignUpCount
                        //01 签入模式 02 签入签出模式
//                    signUpStatus	签到模式 1签入模式 2签出模式

//                        signUpStatus = "" + data.signUpStatus
//                        signUpStatus = "" + kv.getString("zd_signUpStatus_"+meetingid+id, "1")
//                        if (signUpStatus == "2") {
//                            binding.moshitv.text = "签出模式"
//                            binding.moshiiv.setImageResource(R.mipmap.kaiguanguan)
//                        } else {
//                            binding.moshitv.text = "签入模式"
//                            binding.moshiiv.setImageResource(R.mipmap.kaiguank)
//                        }
                        failedMsg = if (signUpStatus == "2") "签出失败" else "签到失败"
                        okMsg = if (signUpStatus == "2") "签出成功" else "签到成功"
                        repeatMsg = if (signUpStatus == "2") "重复签出" else "重复签到 "
                    } catch (e: java.lang.Exception) {
                    }


                }

                override fun onError(response: Response<SiginData>) {
                    super.onError(response)

                }

                override fun onFinish() {
                    super.onFinish()
                    try {
                        mViewModel.isShowLoading.value = false
                    } catch (e: java.lang.Exception) {
                    }
                }


            })
    }


    var isShiBieZ = false


    var isPause = true
    override fun onResume() {
        super.onResume()
        mViewModel.isShowLoading.value = false
        isShiBieZ = false
        isPause = false
        if (moshi.equals("二维码识别")) {
//            ScanTool.GET.playSound(true)
            closeBlue()
            closeRed()
        }
//        if (moshi.equals("激光头识别")) {
//            openHardreader()
//        }
        binding.et.setText("")
        nameMobile = ""

    }

    override fun onPause() {
        super.onPause()
        isPause = true
//        if (moshi.equals("二维码识别")) {
//
//        }
//        if (moshi.equals("激光头识别")) {
//            mDecodeReader?.close()
//            mDecodeReader = null
//        }
    }

    override fun onDestroy() {
        super.onDestroy()
        unregisterReceiver(receiver)
        ScanTool.GET.release()
        closeBlue()
        closeRed()
    }

    override fun onScanCallBack(data: String?) {
        if (isShiBieZ) {
            return
        }
//        ScanTool.GET.playSound(false)
        isShiBieZ = true
        var mRingPlayer =
            MediaPlayer.create(this@MeetingSiginDectivity, R.raw.ddd)
        mRingPlayer?.start()
        try {
            goRe(data)
        } catch (e: Exception) {
            goshibai(SignUpUser())
        }
    }

    private fun goRe(data: String?) {
        if (isPause) {
//            isShiBieZ = false
            return
        }
        if (data.isNullOrEmpty()) {
            goshibai(SignUpUser())
        } else {
            getmeetingCode(data)


        }

    }

    private fun goshibai(signUpUser: SignUpUser) {
        if (moshi.equals("二维码识别")) {
            openRed()
        }
        //                isShiBieZ = false
        signUpUser.signUpLocationId = id
        signUpUser.meetingName = name
        signUpUser.signUpId = signUpId
        signUpUser.userMeetingId = ""
        signUpUser.meetingId = ""
        signUpUser.userMeetingTypeName = ""
        signUpUser.autoStatus = autoStatus
        signUpUser.timeLong = timeLong
        signUpUser.okMsg = okMsg
        signUpUser.failedMsg = failedMsg
        signUpUser.repeatMsg = repeatMsg
        signUpUser.voiceStatus = voiceStatus
        signUpUser.signUpStatus = signUpStatus
        signUpUser.success = "500"
        startActivity<SiginReAutoActivity>(
            "type" to showType,
            "data" to signUpUser
        )
    }

    private fun getmeetingCode(meetingCode: String) {
        mViewModel.isShowLoading.value = true
        OkGo.get<SignUpUser>(PageRoutes.Api_meetingCode + meetingCode)
            .tag(PageRoutes.Api_meetingCode + meetingCode)
            .headers("Authorization", kv.getString("token", ""))
            .execute(object : RequestCallback<SignUpUser>() {
                override fun onSuccessNullData() {
                    super.onSuccessNullData()

                }

                override fun onMySuccess(data: SignUpUser) {
                    super.onMySuccess(data)
                    try {
                        var signUpUser = data

                        if (!signUpUser.meetingId.equals(meetingid)) {
                            goshibai(signUpUser)
                            return
                        }
                        if (showType == 8) {
                            if (signUpUser.orderId.isNullOrEmpty()) {
                                goshibai(signUpUser)
                                return
                            }
                        }

                        if (signUpUser.id.isNullOrEmpty()) {
                            goshibai(signUpUser)
                            return
                        }
                        getData(signUpUser.id)


                    } catch (e: Exception) {
                        goshibai(SignUpUser())

                    }


                }

                override fun onError(response: Response<SignUpUser>) {
                    super.onError(response)
                    mViewModel.isShowLoading.value = false
                    goshibai(SignUpUser())
                }

                override fun onFinish() {
                    super.onFinish()

                }


            })
    }

    private fun getData(userid: String) {

        OkGo.get<MeetingUserDeData>(PageRoutes.Api_meetinguser_data + userid + "?id=" + userid)
            .tag(PageRoutes.Api_meetinguser_data + userid + "?id=" + userid)
            .headers("Authorization", kv.getString("token", ""))
            .execute(object : RequestCallback<MeetingUserDeData>() {
                override fun onSuccessNullData() {
                    super.onSuccessNullData()

                }

                override fun onMySuccess(data: MeetingUserDeData) {
                    super.onMySuccess(data)
                    meetingFormData?.let {
                        for (list in it.meetingFormList) {
                            for (listFrom in data.userMeetingForms) {
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
                    var signUpUser = SignUpUser()
                    signUpUser.signUpLocationId = id
                    signUpUser.meetingName = name
                    signUpUser.signUpId = signUpId
                    signUpUser.userMeetingId = userid
                    signUpUser.meetingId = "" + data.meetingId
                    signUpUser.userMeetingTypeName = "" + data.userMeetingTypeName
                    signUpUser.autoStatus = autoStatus
                    signUpUser.timeLong = timeLong
                    signUpUser.okMsg = okMsg
                    signUpUser.failedMsg = failedMsg
                    signUpUser.repeatMsg = repeatMsg
                    signUpUser.voiceStatus = voiceStatus
                    signUpUser.signUpStatus = signUpStatus
                    if (showType == 8) {
                        var order: MeetingUserDeData.UserOrderBean =
                            MeetingUserDeData.UserOrderBean()
                        if (data.userOrder != null) {
                            if (moshi.equals("二维码识别")) {
                                openRed()
                            }
                            order = data.userOrder
                            order.supplement = data.userMeetingTypeName
                            order.userName = data.name
                            order.corporateName = data.corporateName
                            order.meetingSignUpLocationName = binding.name.text.toString()
                            startActivity<ExamineKPActivity>(
                                "order" to order,
                                "timeLong" to timeLong,
                                "autoStatus" to "" + autoStatus
                            )
                            return
                        }

                    }
                    var avatar = ""
                    data.avatar?.let { avatar = it }
                    if (showType == 3) {
                        var ruzhustatus = "1"
                        for (item in data.meetingSignUps) {
                            when (item.type) {
                                3 -> {
                                    ruzhustatus =  "" + item.select
                                }
                            }}
                        data.meetingSignUps?.let {
                            for (item in data.meetingSignUps) {
                                if (item.type == 3) {
                                    item.userMeetingAccommodation?.let { userMeetingAccommodation ->
                                        userMeetingAccommodation.roomNo?.let { location ->
                                            signUpUser.location = location
                                        }
                                    }
                                }
                            }
                        }
                        com.dylanc.longan.startActivity<SiginReActivity>(
                            "ruzhustatus" to "2",
                            "type" to showType,
                            "data" to signUpUser,
                            "avatar" to avatar
                        )
                    } else {
                        if (showType != 8) {
                            var params = java.util.HashMap<String, String>()
                            params["meetingId"] = signUpUser.meetingId//会议id
                            params["signUpLocationId"] = signUpUser.signUpLocationId//签到点id
                            params["signUpId"] = signUpUser.signUpId//签到站id
                            params["userMeetingId"] = signUpUser.userMeetingId//用户参与会议id
                            params["status"] = "2"//用户参与会议id
                            params["signUpStatus"] = signUpStatus
                            sigin(JSON.toJSONString(params), { success ->
                                signUpUser.success = success
                                com.dylanc.longan.startActivity<SiginReAutoActivity>(
                                    "type" to showType,
                                    "data" to signUpUser,
                                    "avatar" to avatar
                                )
                                if (moshi.equals("二维码识别")) {
                                    openBlue()
                                }
                            }, {
                                if (moshi.equals("二维码识别")) {
                                    openRed()
                                }
                                signUpUser.success = "500"
                                com.dylanc.longan.startActivity<SiginReAutoActivity>(
                                    "type" to showType,
                                    "data" to signUpUser,
                                    "avatar" to avatar
                                )
                            }, {})
                        } else {
                            if (moshi.equals("二维码识别")) {
                                openRed()
                            }
                            signUpUser.success = "500"
                            com.dylanc.longan.startActivity<SiginReAutoActivity>(
                                "type" to showType,
                                "data" to signUpUser,
                                "avatar" to avatar
                            )
                        }
                    }


                }

                override fun onError(response: Response<MeetingUserDeData>) {
                    super.onError(response)
                    goshibai(SignUpUser())
                }

                override fun onFinish() {
                    super.onFinish()
                    mViewModel.isShowLoading.value = false
                }


            })
    }

    fun openRed() {
        val deviceon = File("/sys/class/leds/led-red")
        if (deviceon.canRead()) {
            FaceUtil.LedSet("led-red", 1);
        }
    }

    fun closeRed() {
        val deviceon = File("/sys/class/leds/led-red")
        if (deviceon.canRead()) {
            FaceUtil.LedSet("led-red", 0);
        }
    }

    fun openBlue() {
        val deviceon = File("/sys/class/leds/led-blue")
        if (deviceon.canRead()) {
            FaceUtil.LedSet("led-blue", 1);
        }
    }

    fun closeBlue() {
        val deviceon = File("/sys/class/leds/led-blue")
        if (deviceon.canRead()) {
            FaceUtil.LedSet("led-blue", 0);
        }
    }
    private val ACTION_DATA_CODE_RECEIVED = "com.sunmi.scanner.ACTION_DATA_CODE_RECEIVED"
    private val DATA = "data"
    private val SOURCE = "source_byte"
    private val receiver: BroadcastReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent) {
            if (moshi.equals("激光头识别")) {
                if (isPause) {
                    return
                }
                if (isShiBieZ) {
                    return
                }
                isShiBieZ = true
                var mRingPlayer =
                    MediaPlayer.create(this@MeetingSiginDectivity, R.raw.ddd)
                mRingPlayer?.start()
                try {
                    val code = intent.getStringExtra(DATA)
                    /* val data: ByteArray? = intent.getByteArrayExtra(SOURCE)*/
                    if (code != null && !code.isEmpty()) {
                        goRe(code)
                    }else{
                        goshibai(SignUpUser())
                    }
                } catch (e: UnsupportedEncodingException) {
                    e.printStackTrace()
                    goshibai(SignUpUser())
                }

            }


        }
    }

    private fun registerReceiver() {
        val filter = IntentFilter()
        filter.addAction(ACTION_DATA_CODE_RECEIVED)
        registerReceiver(receiver, filter)
    }

}