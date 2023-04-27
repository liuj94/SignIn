package com.example.signin

import android.view.KeyEvent
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.alibaba.fastjson.JSON
import com.common.apiutil.decode.DecodeReader
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
import com.example.signin.net.JsonCallback

import com.example.signin.net.RequestCallback
import com.hello.scan.ScanCallBack
import com.hello.scan.ScanTool
import com.hjq.permissions.OnPermissionCallback
import com.hjq.permissions.Permission
import com.hjq.permissions.XXPermissions
import com.lzy.okgo.OkGo
import com.lzy.okgo.model.Response
import com.tencent.mmkv.MMKV
import sigin
import java.io.File
import java.io.UnsupportedEncodingException
import java.nio.charset.StandardCharsets

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
    var failedMsg: String = "签到失败"
    var okMsg: String = "签到成功"
    var repeatMsg: String = "重复签到"
    var voiceStatus: String = "2"
    private var list: MutableList<MeetingUserData> = ArrayList()
    private var adapter: FMeetingDeList3Adapter? = null
    private var adapterSelect3: SelectMeetingAdapter2? = null
    private var selectList3: MutableList<SiginData> = ArrayList()
    private var mDecodeReader: DecodeReader? = null
    var moshi :String?= ""
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
        var num=""
        if(showType == 1){
            num = "注册签到"
        }else if(showType == 2){
            num = "来程签到"
        }else if(showType == 3){
            num = "入住签到"
        }else if(showType == 4){
            num = "会场签到"
        }else if(showType == 5){
            num = "餐饮签到"
        }else if(showType == 6){
            num = "礼品签到"
        }else if(showType == 7){
            num = "返程签到"
        }else if(showType == 8){
            num = "发票签到"
        }
        binding.title.text = num
         moshi = kv.getString("shaomamoshi","")
        if(moshi.equals("激光头识别")){
            var a = SiginData()
            a.name = "二维码识别"
            a.isKuan =true
            selectList3.add(a)
            var a1 = SiginData()
            a1.name = "摄像头识别"
            a1.isKuan =true
            selectList3.add(a1)
            var a2 = SiginData()
            a2.name = "激光头识别"
            a2.isMyselect = true
            a2.isKuan =true
            selectList3.add(a2)

            val deviceon = File("/sys/class/leds/led-white")
            if (deviceon.canRead()) {
                FaceUtil.LedSet("led-white", 0);
            }
            ScanTool.GET.release()
            openHardreader()

        }else if(moshi.equals("二维码识别")){
            var a = SiginData()
            a.name = "二维码识别"
            a.isMyselect = true
            a.isKuan =true
            selectList3.add(a)

            var a1 = SiginData()
            a1.name = "摄像头识别"
            a1.isKuan =true
            selectList3.add(a1)
            var a2 = SiginData()
            a2.name = "激光头识别"
            a2.isKuan =true
            selectList3.add(a2)
            mDecodeReader?.close()

            val deviceon = File("/sys/class/leds/led-white")
            if (deviceon.canRead()) {
                FaceUtil.LedSet("led-white", 1);
            }else{
                toast("灯光打开失败")
            }
            ScanTool.GET.initSerial(this@MeetingSiginDectivity, "/dev/ttyACM0", 115200, this@MeetingSiginDectivity)

        }else if(moshi.equals("摄像头识别")){
            var a = SiginData()
            a.name = "二维码识别"
            a.isKuan =true
            selectList3.add(a)

            var a1 = SiginData()
            a1.name = "摄像头识别"
            a1.isMyselect = true
            a1.isKuan =true
            selectList3.add(a1)
            var a2 = SiginData()
            a2.isKuan =true
            a2.name = "激光头识别"
            selectList3.add(a2)

        }else{
            var a = SiginData()
            a.name = "二维码识别"
            a.isKuan =true
            selectList3.add(a)
            var a1 = SiginData()
            a1.name = "摄像头识别"
            a1.isKuan =true
            selectList3.add(a1)
            var a2 = SiginData()
            a2.name = "激光头识别"
            a2.isKuan =true
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
                kv.putString("shaomamoshi",moshi)
                adapterSelect3?.notifyDataSetChanged()
                if(moshi.equals("激光头识别")){
                    val deviceon = File("/sys/class/leds/led-white")
                    if (deviceon.canRead()) {
                        FaceUtil.LedSet("led-white", 0);
                    }
                    ScanTool.GET.release()
                    openHardreader()
                }else if(moshi.equals("二维码识别")){
                    val deviceon = File("/sys/class/leds/led-white")
                    if (deviceon.canRead()) {
                        FaceUtil.LedSet("led-white", 1);
                    }else{
                        toast("灯光打开失败")
                    }
                    mDecodeReader?.close()
                    ScanTool.GET.initSerial(this@MeetingSiginDectivity, "/dev/ttyACM0", 115200, this@MeetingSiginDectivity)
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
//                binding.et.setText(nameMobile)
//                nameMobile?.let {
//                    binding.et.setSelection(it.length)
//                }
//                    list.clear()
//                getList()
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
        setState()

        binding.sm.setOnClickListener {
            if(moshi.equals("识别模式")){
                toast("请选择识别模式")
                return@setOnClickListener
            }
            if(!moshi.equals("摄像头识别")){
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
                                    "signUpId" to "" + signUpId
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
            setState()
        }
        binding.shaoma.setOnClickListener {
            if(moshi.equals("识别模式")){
                toast("请选择识别模式")
                return@setOnClickListener
            }
            if(!moshi.equals("摄像头识别")){
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
                                    "signUpId" to "" + signUpId
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
                    siginUp2List.addAll(model.sys_liping)
                }
            }
        }

        adapter?.setSiginUp2List(siginUp2List)
    }

    private fun getList() {
        if (nameMobile.isNullOrEmpty()) {
            list.clear()
            adapter?.notifyDataSetChanged()
            binding.recyclerview.visibility = View.GONE
            binding.kong.visibility = View.GONE
            return
        }
        var url = PageRoutes.Api_meeting_sign_up_data_list + params
        if (!nameMobile.isNullOrEmpty()) {
            url = "$url&nameMobile=$nameMobile"
        }
        OkGo.get<MeetingUserModel>(url)
            .tag(url)
            .headers("Authorization", kv.getString("token", ""))
            .execute(object : JsonCallback<MeetingUserModel>(MeetingUserModel::class.java) {

                override fun onSuccess(response: Response<MeetingUserModel>) {
                    list.clear()
                    response?.let {
                        list.addAll(response.body().data)
                        adapter?.notifyDataSetChanged()
                        if (!nameMobile.isNullOrEmpty()) {
                            if (list.size > 0) {
                                binding.kong.visibility = View.GONE
                                binding.recyclerview.visibility = View.VISIBLE
                            } else {
                                binding.kong.visibility = View.VISIBLE
                                binding.recyclerview.visibility = View.GONE
                            }

                        } else {
                            binding.kong.visibility = View.GONE
                            binding.recyclerview.visibility = View.GONE
                        }
                    }
                }

                override fun onError(response: Response<MeetingUserModel>?) {
                    super.onError(response)

                }

                override fun onFinish() {

                }
            })

    }

    var addressStatus = "1"
    fun setState() {

        val params = HashMap<String, String>()
        params["id"] = id
        if (addressStatus.equals("1")) {
            addressStatus = "2"
//            params["addressStatus"] = "2"
            params["signUpStatus"] = "2"
        } else {
            addressStatus = "1"
//            params["addressStatus"] = "1"
            params["signUpStatus"] = "1"
        }



        OkGo.put<String>(PageRoutes.Api_ed_meetingSignUpLocation)
            .tag(PageRoutes.Api_ed_meetingSignUpLocation)
            .upJson(JSON.toJSONString(params))
            .headers(
                "Authorization", MMKV.mmkvWithID("MyDataMMKV")
                    .getString("token", "")
            )
            .execute(object : RequestCallback<String>() {

                override fun onSuccess(response: Response<String>?) {
                    super.onSuccess(response)
//                    if(addressStatus == "1"){
//                        binding.moshitv.text = "签入模式"
//                        binding.moshiiv.setImageResource(R.mipmap.kaiguanguan)
//                    }else{
//                        binding.moshitv.text = "签出模式"
//                        binding.moshiiv.setImageResource(R.mipmap.kaiguank)
//                    }

                }

                override fun onError(response: Response<String>) {
                    super.onError(response)

                }

                override fun onFinish() {
                    super.onFinish()
                    getSiginData()
                }


            })
    }

    private fun getSiginData() {
        OkGo.get<SiginData>(PageRoutes.Api_meetingSignUpLocationDe + id)
            .tag(PageRoutes.Api_meetingSignUpLocationDe)
            .headers("Authorization", kv.getString("token", ""))
            .execute(object : RequestCallback<SiginData>() {
                override fun onSuccessNullData() {
                    super.onSuccessNullData()

                }

                override fun onMySuccess(data: SiginData) {
                    super.onMySuccess(data)
                    binding.num1.text = "" + data.beUserCount
                    binding.num2.text = "" + data.signUpCount
                    binding.num3.text = "" + data.localSignUpCount
                    //01 签入模式 02 签入签出模式
//                    signUpStatus	签到模式 1签出模式 2签入模式
                    signUpStatus = "" + data.signUpStatus
                    if (signUpStatus == "2") {
                        binding.moshitv.text = "签入模式"
                        binding.moshiiv.setImageResource(R.mipmap.kaiguank)

                    } else {
                        binding.moshitv.text = "签出模式"
                        binding.moshiiv.setImageResource(R.mipmap.kaiguanguan)

                    }

                }

                override fun onError(response: Response<SiginData>) {
                    super.onError(response)

                }

                override fun onFinish() {
                    super.onFinish()
                    mViewModel.isShowLoading.value = false
                }


            })
    }
    override fun dispatchKeyEvent(event: KeyEvent): Boolean {
        //要是重虚拟键盘输入怎不拦截
        if ("Virtual" == event.device.name) {
            return super.dispatchKeyEvent(event)
        }
        if(moshi.equals("二维码识别")){
            mDecodeReader?.open(115200)
        }
        return true
    }
    var isShiBieZ = false
    private fun openHardreader() {
        if (mDecodeReader == null) {
            mDecodeReader = DecodeReader(this) //初始化
        }

        mDecodeReader?.setDecodeReaderListener { data ->
            if (!isShiBieZ){
                isShiBieZ = true
                try {
                    val str = String(data, StandardCharsets.UTF_8)
                    runOnUiThread {
                        goRe(str)
//                        mDecodeReader?.close()
                    }
                } catch (e: UnsupportedEncodingException) {
                    e.printStackTrace()
                }
            }

        }
//        mDecodeReader?.open(115200)

    }
    var isPause =true
    override fun onResume() {
        super.onResume()
        isPause = false
        if(moshi.equals("激光头识别")){
            val deviceon = File("/sys/class/leds/led-white")
            if (deviceon.canRead()) {
                FaceUtil.LedSet("led-white", 1);
            }
        }
        binding.et.setText("")
        nameMobile = ""
    }

    override fun onPause() {
        super.onPause()
        isPause = true
        if(moshi.equals("激光头识别")){
            val deviceon = File("/sys/class/leds/led-white")
            if (deviceon.canRead()) {
                FaceUtil.LedSet("led-white", 0);
            }
        }
    }
    override fun onDestroy() {
        super.onDestroy()
        mDecodeReader?.close()
        ScanTool.GET.release()
        val deviceon = File("/sys/class/leds/led-white")
        if (deviceon.canRead()) {
            FaceUtil.LedSet("led-white", 0);
        }
    }

    override fun onScanCallBack(data: String?) {
        try {
            if(!isShiBieZ ){
                isShiBieZ = true
                goRe(data)
            }

        } catch (e: Exception) {
            isShiBieZ = false
        }
    }
    private fun goRe(data: String?) {
        if (isPause) {
            isShiBieZ = false
            return
        }


        try {
            var signUpUser = JSON.parseObject(data, SignUpUser::class.java)


            signUpUser.signUpLocationId = id
            signUpUser.meetingName = name
            signUpUser.signUpId = signUpId
            signUpUser.userMeetingId = signUpUser.id
            signUpUser.meetingId = signUpUser.meetingId
            signUpUser.userMeetingTypeName = signUpUser.supplement
            signUpUser.autoStatus = autoStatus
            signUpUser.timeLong = timeLong
            signUpUser.okMsg = okMsg
            signUpUser.failedMsg = failedMsg
            signUpUser.repeatMsg = repeatMsg
            signUpUser.voiceStatus = voiceStatus
            if(signUpUser.id.isNullOrEmpty()){
                isShiBieZ = false
                signUpUser.success = "500"
                startActivity<SiginReAutoActivity>(
                    "type" to showType,
                    "data" to signUpUser
                )
                return
            }
            if (showType == 3) {
                com.dylanc.longan.startActivity<SiginReActivity>(
                    "type" to showType,
                    "data" to signUpUser
                )
            } else {
                var params = HashMap<String, String>()
                params["meetingId"] = signUpUser.meetingId//会议id
                params["signUpLocationId"] = signUpUser.signUpLocationId//签到点id
                params["signUpId"] = signUpUser.signUpId//签到站id
                params["userMeetingId"] = signUpUser.userMeetingId//用户参与会议id
                params["status"] = "2"//用户参与会议id
                mViewModel.isShowLoading.value = true
                sigin(JSON.toJSONString(params), { success ->
                    signUpUser.success = success
                    com.dylanc.longan.startActivity<SiginReAutoActivity>(
                        "type" to showType,
                        "data" to signUpUser
                    )
                }, {
                    signUpUser.success = "500"
                    startActivity<SiginReAutoActivity>(
                        "type" to showType,
                        "data" to signUpUser
                    )
                }, {
                    isShiBieZ = false
                    mViewModel.isShowLoading.value = false
                })
            }
//            if (autoStatus.equals("1")) {
//                if (showType == 3) {
//                    com.dylanc.longan.startActivity<SiginReActivity>(
//                        "type" to showType,
//                        "data" to signUpUser
//                    )
//                } else {
//                    var params = HashMap<String, String>()
//                    params["meetingId"] = signUpUser.meetingId//会议id
//                    params["signUpLocationId"] = signUpUser.signUpLocationId//签到点id
//                    params["signUpId"] = signUpUser.signUpId//签到站id
//                    params["userMeetingId"] = signUpUser.userMeetingId//用户参与会议id
//                    params["status"] = "2"//用户参与会议id
//                    mViewModel.isShowLoading.value = true
//                    sigin(JSON.toJSONString(params), { success ->
//                        signUpUser.success = success
//                        com.dylanc.longan.startActivity<SiginReAutoActivity>(
//                            "type" to showType,
//                            "data" to signUpUser
//                        )
//                    }, {
//                        signUpUser.success = "500"
//                        startActivity<SiginReAutoActivity>(
//                            "type" to showType,
//                            "data" to signUpUser
//                        )
//                    }, {
//                        isShiBieZ = false
//                        mViewModel.isShowLoading.value = false
//                    })
//                }
//            } else {
//                isShiBieZ = false
//                com.dylanc.longan.startActivity<SiginReActivity>(
//                    "type" to showType,
//                    "data" to signUpUser
//                )
//            }
        } catch (e: Exception) {
            isShiBieZ = false
//            toast("二维码解析失败")
            var signUpUser = SignUpUser()
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
            signUpUser.success = "500"
            startActivity<SiginReAutoActivity>(
                "type" to showType,
                "data" to signUpUser
            )
        }
    }


}