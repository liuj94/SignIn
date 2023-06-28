package com.example.signin.fragment


import android.Manifest
import android.content.pm.PackageManager
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.annotation.RequiresApi
import com.afollestad.materialdialogs.MaterialDialog
import com.alibaba.fastjson.JSON
import com.dylanc.longan.mainThread
import com.example.signin.*
import com.example.signin.adapter.FMeetingDeList3Adapter
import com.example.signin.adapter.SelectDataAdapter
import com.example.signin.adapter.SelectMeetingAdapter
import com.example.signin.agora.TokenUtils
import com.example.signin.base.BaseBindingFragment
import com.example.signin.base.BaseViewModel
import com.example.signin.bean.*
import com.example.signin.databinding.FragMeetingde4Binding
import com.example.signin.net.RequestCallback
import com.lzy.okgo.OkGo
import com.lzy.okgo.model.Response
import io.agora.rtc2.ChannelMediaOptions
import io.agora.rtc2.Constants
import io.agora.rtc2.IRtcEngineEventHandler
import io.agora.rtc2.RtcEngine

/**
 *   author : LiuJie
 *   date   : 2021/2/2513:36
 */
class MettingDe4Fragment : BaseBindingFragment<FragMeetingde4Binding, BaseViewModel>() {
//    companion object {
//        fun newInstance(meetingid: String, businessId: String): MettingDe4Fragment {
//            val args = Bundle()
//            args.putString("meetingid", meetingid)
//            args.putString("businessId", businessId)
//            val fragment = MettingDe4Fragment()
//            fragment.arguments = args
//            return fragment
//        }
//    }

    override fun getViewModel(): Class<BaseViewModel> = BaseViewModel::class.java

    private var list: MutableList<MeetingUserData> = ArrayList()
    private var adapter: FMeetingDeList3Adapter? = null

    private var adapterSelect2: SelectDataAdapter? = null
    private var adapterSelect: SelectMeetingAdapter? = null
    private var selectList2: MutableList<SiginData> = ArrayList()
    private var selectList: MutableList<SiginUpListData> = ArrayList()
    var meetingid: String? = ""
    var businessId: String? = ""
    var siginlocationId: String? = ""
    var signUpStatus: String? = ""
    var signUpId: String? = ""
    var nameMobile: String? = ""
    var autoStatus: String? = ""
    var timeLong: Int = 3
    var type: Int = 0
    var userData: User? = null
    var isShow: Boolean = false
    override fun onDestroyView() {
        super.onDestroyView()
        isShow = false
        Log.d("MettingDe2Fragment","MettingDe4Fragmenton==onDestroyView()")
    }
    @RequiresApi(Build.VERSION_CODES.M)
    override fun initData() {
        isShow = true
        userData = JSON.parseObject(kv.getString("userData", ""), User::class.java)
        setStartData()
        meetingid =kv.getString("meetingid", "")
        businessId =kv.getString("businessId", "")
//        meetingid = arguments?.getString("meetingid", "1")
//        businessId = arguments?.getString("businessId", "1")
        CHANNEL = "" + meetingid
//        binding.srecyclerview.layoutManager = LinearLayoutManager(activity)
//        adapterSelect = SelectMeetingAdapter().apply {
//            submitList(selectList)
//            setOnItemClickListener { _, _, position ->
//                for (item in selectList) {
//                    item.isMyselect = false
//                }
//                selectList[position].isMyselect = true
//                signUpId = "" + selectList[position].id
//                type = selectList[position].type
//                binding.nameTv.text = selectList[position].name
//                adapterSelect?.notifyDataSetChanged()
//                binding.selectLl.visibility = View.GONE
//                LiveDataBus.get().with("selectLlGONE").postValue("1")
//                setStartData()
//                getList()
//            }
//        }
//        binding.srecyclerview.adapter = adapterSelect
//
//        binding.srecyclerview2.layoutManager = LinearLayoutManager(activity)
//        adapterSelect2 = SelectDataAdapter().apply {
//            submitList(selectList2)
//            setOnItemClickListener { _, _, position ->
//                if (selectList2[position].isMyselect) {
////                    for (item in selectList2) {
////                        item.isMyselect = false
////                    }
////                    siginlocationId = ""
////                    binding.name2Tv.text = "选择签到点"
////                    setStartData()
//                } else {
//                    for (item in selectList2) {
//                        item.isMyselect = false
//                    }
//                    selectList2[position].isMyselect = true
//                    autoStatus = "" + selectList2[position].autoStatus
//                    okMsg = selectList2[position].okMsg
//                    failedMsg = selectList2[position].failedMsg
//                    repeatMsg = selectList2[position].repeatMsg
//                    voiceStatus = selectList2[position].speechStatus
//                    timeLong = selectList2[position].timeLong
//                    siginlocationId = "" + selectList2[position].id
//                    binding.name2Tv.text = selectList2[position].name
//                }
//
//                adapterSelect2?.notifyDataSetChanged()
//                binding.select2Ll.visibility = View.GONE
//                LiveDataBus.get().with("selectLlGONE").postValue("1")
//
////                binding.time.text = ""+selectList2[position].timeLong+"分钟"
////                binding.time.text = ""+selectList2[position].timeLong+"分钟"
//
//                if (selectList2[position].voiceStatus == 2) {
//                    binding.ztname.text = "当前对讲处于关闭状态"
//                    binding.ztname.setTextColor(Color.parseColor("#999999"))
//                    binding.thstate.setImageResource(R.mipmap.tonghua1)
//                    binding.ztiv.setImageResource(R.drawable.ov_ccc)
//                    binding.roundProgress.progress = 0
//                } else if (selectList2[position].voiceStatus == 1) {
//                    binding.ztname.text = "当前对讲处于开启状态"
//                    binding.ztname.setTextColor(Color.parseColor("#3974f6"))
//                    binding.thstate.setImageResource(R.mipmap.tonghua3)
//                    binding.ztiv.setImageResource(R.drawable.ov_3974f6)
//                    binding.roundProgress.progress = 5000
//                }
//
//                binding.roundProgress.maxProgress = 5000
//
//
////                getSiginData()
//            }
//        }
//        binding.srecyclerview2.adapter = adapterSelect2
//
//        binding.recyclerview.layoutManager = LinearLayoutManager(activity)
//        adapter = FMeetingDeList3Adapter().apply {
//            submitList(list)
//            setOnItemClickListener { _, _, position ->
//                com.dylanc.longan.startActivity<MeetingUserDectivity>(
//                    "id" to list[position].id.toString(),
//                    "showType" to type
//                )
//            }
//        }
//
//        binding.recyclerview.adapter = adapter
//        getData()
//        delayed()
        getData()
//        binding.nameLl.setOnClickListener {
//            binding.select2Ll.visibility = View.GONE
//            if (binding.selectLl.visibility == View.VISIBLE) {
//                binding.selectLl.visibility = View.GONE
//                LiveDataBus.get().with("selectLlGONE").postValue("1")
//            } else {
//                binding.selectLl.visibility = View.VISIBLE
//                LiveDataBus.get().with("selectLlVISIBLE").postValue("1")
//            }
//        }
//        binding.name2Ll.setOnClickListener {
//            binding.selectLl.visibility = View.GONE
//            if (binding.select2Ll.visibility == View.VISIBLE) {
//                binding.select2Ll.visibility = View.GONE
//                LiveDataBus.get().with("selectLlGONE").postValue("1")
//            } else {
//                binding.select2Ll.visibility = View.VISIBLE
//                LiveDataBus.get().with("selectLlVISIBLE").postValue("1")
//            }
//
//        }
//        binding.select2Ll.setOnClickListener {
//            if (binding.select2Ll.visibility == View.VISIBLE) {
//                binding.select2Ll.visibility = View.GONE
//                LiveDataBus.get().with("selectLlGONE").postValue("1")
//            }
//        }
//        binding.selectLl.setOnClickListener {
//            if (binding.selectLl.visibility == View.VISIBLE) {
//                binding.selectLl.visibility = View.GONE
//                LiveDataBus.get().with("selectLlGONE").postValue("1")
//            }
//
//        }
//        binding.sous.setOnClickListener {
//            if (siginlocationId.isNullOrEmpty()) {
//                toast("请选择签到点")
//
//            } else {
//                nameMobile = binding.et.text.toString().trim()
//                getUserList()
//            }
//            activity?.hideSoftInput()
//        }
//        binding.et.setOnKeyListener(View.OnKeyListener { v, keyCode, event ->
//            if (keyCode == KeyEvent.KEYCODE_ENTER) {
//                // 监听到回车键，会执行2次该方法。按下与松开
//                if (event.action == KeyEvent.ACTION_UP) {
//                    if (siginlocationId.isNullOrEmpty()) {
//                        toast("请选择签到点")
//
//                    } else {
//                        nameMobile = binding.et.text.toString().trim()
//                        binding.et.setText(nameMobile)
//                        nameMobile?.let {
//                            binding.et.setSelection(it.length)
//                        }
//                        getUserList()
//                    }
//
//                    activity?.hideSoftInput()
//                }
//            }
//            false
//        })
//        binding.moshiiv.setOnClickListener {
//            if (siginlocationId.isNullOrEmpty()) {
//                toast("请选择签到点")
//            } else {
//                signUpStatus?.let { it1 -> setState(siginlocationId!!, it1) }
//            }
//        }
//        binding.shaoma.setOnClickListener {
//            if (siginlocationId.isNullOrEmpty()) {
//                toast("请选择签到点")
//
//            } else {
//                activity?.let {
//                    XXPermissions.with(context)
//                        .permission(Permission.CAMERA)
//                        .request(object : OnPermissionCallback {
//
//                            override fun onGranted(permissions: MutableList<String>, all: Boolean) {
//                                if (!all) {
//                                    toast("获取权限失败")
//                                } else {
//                                    var intent = Intent(it, ScanActivity::class.java)
//                                    startActivityForResult(intent, 1000)
//                                }
//
//                            }
//
//                            override fun onDenied(
//                                permissions: MutableList<String>,
//                                never: Boolean
//                            ) {
//                                toast("获取权限失败")
//
//                            }
//                        })
//
//                }
//            }
//
//
//        }


        binding.thstate.setOnClickListener {
            OnResultManager.requestPermissions(
                this,
                arrayOf(Manifest.permission.RECORD_AUDIO),
                200
            ) { requestCode: Int, _: Array<String>, grantResults: IntArray ->
                if (requestCode == 200 && grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    userData?.let {
//                        if(talkTime>0){
                        if (it.userType.equals("01") || it.userType.equals("04")) {
                            voice()
                        } else {
                            if (selectList2.size > 0) {
                                if (selectList2[0].voiceStatus == 1) {
                                    //对讲开启
                                    voice()

                                } else {

                                }
                            } else {

                            }

                        }
//                        }else{
//                            toast("当前暂无通话时长")
//                        }

                    }
                } else {
                    activity?.let { it1 ->
                        MaterialDialog(it1).show {
                            title(text = "提示")
                            message(text = "无语音权限，无法使用通话功能")
                            positiveButton(text = "确定") {

                            }

                        }
                    }
                }
            }

        }
        activity?.let {
            mRtcEngine = TokenUtils.initializeAndJoinChannel(it, mRtcEventHandler)
        }
//        getSiginData()

        LiveDataBus.get().with("JWebSocketClientlocation", String::class.java)
            .observeForever {
                try {
                    if (isShow) {
                            getDatasign_up_app_list()
                    }
                } catch (e: java.lang.Exception) {
                }

            }
    }

    private fun voice() {

        if (voiceState == 0) {
            mViewModel.isShowLoading.value = true
            initializeAndJoinChannel()
        } else {
            mRtcEngine?.leaveChannel()

        }
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.d("taggg", "onDestroy")
        mRtcEngine?.leaveChannel()
        RtcEngine.destroy()
        mRtcEngine = null
    }


    // 填写频道名称。
    private var CHANNEL = ""

    private var mRtcEngine: RtcEngine? = null
    private val mRtcEventHandler = object : IRtcEngineEventHandler() {
        //        onJoinChannelSuccess
        override fun onJoinChannelSuccess(channel: String?, uid: Int, elapsed: Int) {
            super.onJoinChannelSuccess(channel, uid, elapsed)
            Log.i(
                "IRtcEngineEventHandler",
                String.format("onJoinChannelSuccess channel %s uid %d", channel, uid)
            )
            mainThread {
                mViewModel.isShowLoading.value = false
                voiceState = 1
                binding.ztname.text = "当前对讲处于通话状态"
                binding.ztname.setTextColor(Color.parseColor("#43cf7c"))
                binding.ztiv.setImageResource(R.drawable.ov_ff43cf7c)
                binding.thstate.setImageResource(R.mipmap.tonghua2)
            }

        }

        override fun onLeaveChannel(stats: RtcStats?) {
            super.onLeaveChannel(stats)
            Log.i(
                "IRtcEngineEventHandler",
                String.format("onLeaveChannel ", stats)
            )
            Log.i(
                "IRtcEngineEventHandler",
                String.format("onLeaveChannel totalDuration=", stats?.totalDuration)
            )
            stats?.let {
                val params = HashMap<String, Any>()
                params["meetingId"] = "" + meetingid
                params["time"] = it.totalDuration * 1000
                LiveDataBus.get().with("voiceTime").postValue(JSON.toJSONString(params))
            }


            if (mRtcEngine != null) {
                try {
                    mainThread {
                        voiceState = 0
                        try {
                            binding?.let {
                                it.ztname.text = "当前对讲处于开启状态"
                                it.ztname.setTextColor(Color.parseColor("#3974f6"))
                                it.ztiv.setImageResource(R.drawable.ov_3974f6)
                                it.thstate.setImageResource(R.mipmap.tonghua3)
                            }
                        } catch (e: Exception) {
                            Log.i(
                                "IRtcEngineEventHandler",
                                String.format("onLeaveChannel ", stats)
                            )
                        }
                    }

                } catch (e: Exception) {
                    Log.i(
                        "IRtcEngineEventHandler",
                        String.format("onLeaveChannel ", stats)
                    )
                }
            }

        }


        override fun onError(err: Int) {
            super.onError(err)
            Log.i(
                "IRtcEngineEventHandler",
                String.format("onError", err)
            )
        }
    }


    private fun initializeAndJoinChannel() {
        /**In the demo, the default is to enter as the anchor. */
        mRtcEngine?.setClientRole(Constants.CLIENT_ROLE_BROADCASTER)
        mRtcEngine?.setAudioProfile(0)
        mRtcEngine?.setAudioScenario(0)

        mRtcEngine?.setDefaultAudioRoutetoSpeakerphone(true)
        var option = ChannelMediaOptions()
        option.autoSubscribeAudio = true
        option.autoSubscribeVideo = true

        TokenUtils.gen(requireContext(), CHANNEL, 0, object :
            TokenUtils.OnTokenGenCallback<String> {
            override fun onTokenGen(ret: String?) {

                if (ret != null) {
                    mRtcEngine?.joinChannel(ret, CHANNEL, 0, option)
                }


            }

        })


    }

    //0未通话 1加入房间
    var voiceState = 0
    var failedMsg: String = "签到失败"
    var okMsg: String = "签到成功"
    var repeatMsg: String = "重复签到"
    var voiceStatus: String = "2"
//    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
//        super.onActivityResult(requestCode, resultCode, data)
////        if (requestCode == 1000) {
////            data?.let {
////                var param = it.getStringExtra("QrCodeScanned")
////                Log.d("tagggg", "param==" + param)
////
////                var signUpUser = JSON.parseObject(param, SignUpUser::class.java)
////
////                signUpUser.signUpLocationId = siginlocationId
////                signUpUser.signUpId = signUpId
////                signUpUser.userMeetingId = signUpUser.id
////                signUpUser.meetingId = signUpUser.meetingId
////                signUpUser.userMeetingTypeName = signUpUser.supplement
////                signUpUser.autoStatus = autoStatus
////                signUpUser.timeLong = timeLong
////                signUpUser.okMsg = okMsg
////                signUpUser.failedMsg = failedMsg
////                signUpUser.repeatMsg = repeatMsg
////                signUpUser.voiceStatus = voiceStatus
////
////                if (autoStatus.equals("1")) {
////                    if (type == 3) {
////                        startActivity<SiginReActivity>("type" to type, "data" to signUpUser)
////                    } else {
////                        var params = java.util.HashMap<String, String>()
////                        params["meetingId"] = signUpUser.meetingId//会议id
////                        params["signUpLocationId"] = signUpUser.signUpLocationId//签到点id
////                        params["signUpId"] = signUpUser.signUpId//签到站id
////                        params["userMeetingId"] = signUpUser.userMeetingId//用户参与会议id
////                        params["status"] = "2"//用户参与会议id
////                        sigin(JSON.toJSONString(params), { success ->
////                            signUpUser.success = success
////                            startActivity<SiginReAutoActivity>("type" to type, "data" to signUpUser)
////                        }, {}, {})
////                    }
////
////                } else {
////                    startActivity<SiginReActivity>("type" to type, "data" to signUpUser)
////                }
////
////
////            }
////
////        }
//    }

//    fun setState(id: String, signUpStatu: String) {
////        {id: 66, status: 1, voiceStatus: 1}
//        val params = HashMap<String, String>()
//        params["id"] = id
//        if (signUpStatu == "1") {
//
//            params["signUpStatus"] = "2"
//        } else {
//
//            params["signUpStatus"] = "1"
//        }
//
//
//
//        OkGo.put<String>(PageRoutes.Api_ed_meetingSignUpLocation)
//            .tag(PageRoutes.Api_ed_meetingSignUpLocation)
//            .upJson(JSON.toJSONString(params))
//            .headers("Authorization", MMKV.mmkvWithID("MyDataMMKV").getString("token", ""))
//            .execute(object : RequestCallback<String>() {
//
//                override fun onSuccess(response: Response<String>?) {
//                    super.onSuccess(response)
//                    if (signUpStatu == "1") {
//                        signUpStatus = "2"
//                    } else {
//                        signUpStatus = "1"
//                    }
//
//                    if (signUpStatus == "1") {
//                        binding.moshitv.text = "签入模式"
//                        binding.moshiiv.setImageResource(R.mipmap.kaiguanguan)
//                    } else {
//                        binding.moshitv.text = "签出模式"
//                        binding.moshiiv.setImageResource(R.mipmap.kaiguank)
//                    }
//                }
//
//                override fun onError(response: Response<String>) {
//                    super.onError(response)
//
//                }
//
//                override fun onFinish() {
//                    super.onFinish()
//
//                }
//
//
//            })
//    }

    private fun setStartData() {
        siginlocationId = ""
        binding.name2Tv.text = "选择签到点"
        binding.num1.text = "0"
        binding.num2.text = "0"
        binding.num3.text = "0"
        binding.moshitv.text = "签入模式"
        binding.time.text = "5000分钟"
        binding.ztname.text = "当前对讲处于关闭状态"
        binding.et.setText("")
        binding.ztname.setTextColor(Color.parseColor("#999999"))
        binding.moshiiv.setImageResource(R.mipmap.kaiguanguan)
        binding.ztiv.setImageResource(R.drawable.ov_ccc)
        binding.thstate.setImageResource(R.mipmap.tonghua1)
        binding.roundProgress.progress = 0
        binding.roundProgress.maxProgress = 5000
        userData?.let {
            if (it.userType.equals("01") || it.userType.equals("04")) {
                binding.ztname.text = "当前对讲处于开启状态"
                binding.ztiv.setImageResource(R.drawable.ov_3974f6)
                binding.thstate.setImageResource(R.mipmap.tonghua3)
            }
        }

    }

//    private val delayedLoad = SubstepDelayedLoad()
//    private fun delayed() {
//        delayedLoad
//            .delayed(1000)
//            .run {
//                //延时加载布局
//                getData()
//                delayedLoad.clearAllRunable()
//
//            }
//            .start()
//    }

    private fun getData() {
        if (!kv.getString("SiginUpListModelmeetingId"+meetingid, "").isNullOrEmpty()) {
            var data =
                JSON.parseObject(kv.getString("SiginUpListModelmeetingId"+meetingid, ""), SiginUpListModel::class.java)
            selectList.clear()

            selectList.addAll(data.list)
            selectList[0].isMyselect = true
            signUpId = selectList[0].id
            type = selectList[0].type

            binding.nameTv.text = selectList[0].name
            adapterSelect?.notifyDataSetChanged()
            getList()
        }else{
            getDatasign_up_app_list()
        }

    }

    private fun getDatasign_up_app_list() {

        OkGo.get<List<SiginUpListData>>(PageRoutes.Api_meeting_sign_up_app_list + meetingid)
            .tag(PageRoutes.Api_meeting_sign_up_app_list + meetingid + "f5")
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
                        selectList.clear()
                        selectList.addAll(data)
                        if(selectList.size>0){
                            selectList[0].isMyselect = true
                            signUpId = selectList[0].id
                            type = selectList[0].type

                            binding.nameTv.text = selectList[0].name
                            adapterSelect?.notifyDataSetChanged()
                            getList()

                        }
                    }catch (e:Exception){}

                }

                override fun onError(response: Response<List<SiginUpListData>>) {
                    super.onError(response)


                }

                override fun onFinish() {
                    super.onFinish()

                }


            })
    }

    private fun getList() {
        OkGo.get<List<SiginData>>(PageRoutes.Api_meetingSignUpLocation + meetingid + "&signUpId=" + signUpId)
            .tag(PageRoutes.Api_meetingSignUpLocation + meetingid + 2)
            .headers("Authorization", kv.getString("token", ""))
            .execute(object : RequestCallback<List<SiginData>>() {
                override fun onSuccessNullData() {
                    super.onSuccessNullData()

                }

                override fun onMySuccess(data: List<SiginData>) {
                    super.onMySuccess(data)
                    if(!isShow){
                        return
                    }
                    try {
                    selectList2.clear()
                    selectList2.addAll(data)
                    adapterSelect2?.notifyDataSetChanged()
                    if (selectList2.size > 0) {
                        selectList2[0].isMyselect = true
                        autoStatus = "" + selectList2[0].autoStatus
                        okMsg = selectList2[0].okMsg
                        failedMsg = selectList2[0].failedMsg
                        repeatMsg = selectList2[0].repeatMsg
                        voiceStatus = selectList2[0].speechStatus
                        timeLong = selectList2[0].timeLong
                        siginlocationId = "" + selectList2[0].id
                        binding.name2Tv.text = selectList2[0].name

                        userData = JSON.parseObject(kv.getString("userData", ""), User::class.java)
                        if (selectList2[0].voiceStatus == 2) {
                            binding.ztname.text = "当前对讲处于关闭状态"
                            binding.ztname.setTextColor(Color.parseColor("#999999"))
                            binding.thstate.setImageResource(R.mipmap.tonghua1)
                            binding.ztiv.setImageResource(R.drawable.ov_ccc)
                            binding.roundProgress.progress = 0
                        } else if (selectList2[0].voiceStatus == 1) {
                            binding.ztname.text = "当前对讲处于开启状态"
                            binding.ztname.setTextColor(Color.parseColor("#3974f6"))
                            binding.thstate.setImageResource(R.mipmap.tonghua3)
                            binding.ztiv.setImageResource(R.drawable.ov_3974f6)
                            binding.roundProgress.progress = 5000
                        }
                        binding.roundProgress.maxProgress = 5000
                        userData?.let {
                            if (it.userType.equals("01") || it.userType.equals("04")) {
                                binding.ztname.text = "当前对讲处于开启状态"
                                binding.ztname.setTextColor(Color.parseColor("#3974f6"))
                                binding.ztiv.setImageResource(R.drawable.ov_3974f6)
                                binding.thstate.setImageResource(R.mipmap.tonghua3)
                                binding.roundProgress.progress = 5000
                            }
                        }
                    }
                    }catch (e:Exception){}

                }

                override fun onError(response: Response<List<SiginData>>) {
                    super.onError(response)

                }

                override fun onFinish() {
                    super.onFinish()
                    getSiginData()
                }


            })
    }

    var talkTime = 0
    private fun getSiginData() {
        OkGo.get<Balance>(PageRoutes.Api_balance + businessId)
            .tag(PageRoutes.Api_balance)
            .headers("Authorization", kv.getString("token", ""))
            .execute(object : RequestCallback<Balance>() {
                override fun onSuccessNullData() {
                    super.onSuccessNullData()

                }

                override fun onMySuccess(data: Balance) {
                    super.onMySuccess(data)
                    if(!isShow){
                        return
                    }
                    try {
                        talkTime = data.talkTime
//                    binding.roundProgress.progress = (data.talkTime/1000)/60
                        binding.roundProgress.progress = 100
                        binding.roundProgress.maxProgress = 100
//                    binding.roundProgress.maxProgress = (data.talkTime/1000)/60
                        if (data.talkTime > 60000) {
                            binding.time.text = "" + (data.talkTime / 1000) / 60 + "分钟"
                        } else {
                            binding.time.text = "" + (data.talkTime / 1000) + "秒"
                        }
                    }catch (e:Exception){}



                }

                override fun onError(response: Response<Balance>) {
                    super.onError(response)

                }

                override fun onFinish() {
                    super.onFinish()

                }


            })
    }



}
