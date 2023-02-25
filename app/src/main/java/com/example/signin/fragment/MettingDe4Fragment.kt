package com.example.signin.fragment


import android.content.Intent
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.KeyEvent
import android.view.View
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.LinearLayoutManager
import com.alibaba.fastjson.JSON
import com.dylanc.longan.startActivity
import com.dylanc.longan.toast
import com.example.signin.*
import com.example.signin.adapter.FMeetingDeList3Adapter
import com.example.signin.adapter.SelectDataAdapter
import com.example.signin.adapter.SelectMeetingAdapter
import com.example.signin.base.BaseBindingFragment
import com.example.signin.base.BaseViewModel
import com.example.signin.bean.*
import com.example.signin.databinding.FragMeetingde4Binding
import com.example.signin.net.JsonCallback
import com.example.signin.net.RequestCallback
import com.hjq.permissions.OnPermissionCallback
import com.hjq.permissions.Permission
import com.hjq.permissions.XXPermissions
import com.lzy.okgo.OkGo
import com.lzy.okgo.model.Response
import com.tencent.mmkv.MMKV
import java.io.UnsupportedEncodingException

/**
 *   author : LiuJie
 *   date   : 2021/2/2513:36
 */
class MettingDe4Fragment : BaseBindingFragment<FragMeetingde4Binding, BaseViewModel>() {
    companion object {
        fun newInstance(meetingid: String): MettingDe4Fragment {
            val args = Bundle()
            args.putString("meetingid", meetingid)
            val fragment = MettingDe4Fragment()
            fragment.arguments = args
            return fragment
        }
    }
    override fun getViewModel(): Class<BaseViewModel> = BaseViewModel::class.java

    private var list: MutableList<MeetingUserData> = ArrayList()
    private var adapter: FMeetingDeList3Adapter? = null

    private var adapterSelect2: SelectDataAdapter? = null
    private var adapterSelect: SelectMeetingAdapter? = null
    private var selectList2: MutableList<SiginData> = ArrayList()
    private var selectList: MutableList<SiginUpListData> = ArrayList()
    var meetingid :String? = ""
    var siginlocationId :String? = ""
    var signUpStatus :String? = ""
    var signUpId: String? = ""
    var nameMobile: String? = ""
    var autoStatus: String? = ""
    var timeLong: Int = 3
    var type: Int = 0
    @RequiresApi(Build.VERSION_CODES.M)
    override fun initData() {
        setStartData()
        meetingid = arguments?.getString("meetingid", "")
        binding.srecyclerview.layoutManager = LinearLayoutManager(activity)
        adapterSelect = SelectMeetingAdapter().apply {
            submitList(selectList)
            setOnItemClickListener { _, _, position ->
                for (item in selectList) {
                    item.isMyselect = false
                }
                selectList[position].isMyselect = true
                signUpId = "" + selectList[position].id
                type= selectList[position].type
                binding.nameTv.text = selectList[position].name
                adapterSelect?.notifyDataSetChanged()
                binding.selectLl.visibility = View.GONE
                setStartData()
                getList()
            }
        }
        binding.srecyclerview.adapter = adapterSelect

        binding.srecyclerview2.layoutManager = LinearLayoutManager(activity)
        adapterSelect2 = SelectDataAdapter().apply {
            submitList(selectList2)
            setOnItemClickListener { _, _, position ->
                if (selectList2[position].isMyselect) {
//                    for (item in selectList2) {
//                        item.isMyselect = false
//                    }
//                    siginlocationId = ""
//                    binding.name2Tv.text = "选择签到点"
//                    setStartData()
                } else {
                    for (item in selectList2) {
                        item.isMyselect = false
                    }
                    selectList2[position].isMyselect = true
                    autoStatus = "" +selectList2[position].autoStatus
                    timeLong = selectList2[position].timeLong
                    siginlocationId = "" + selectList2[position].id
                    binding.name2Tv.text = selectList2[position].name
                }

                adapterSelect2?.notifyDataSetChanged()
                binding.select2Ll.visibility = View.GONE


//                binding.time.text = ""+selectList2[position].timeLong+"分钟"
//                binding.time.text = ""+selectList2[position].timeLong+"分钟"
                if(selectList2[position].voiceStatus==2){
                    binding.ztname.text = "当前对讲处于关闭状态"
                    binding.ztname.setTextColor(Color.parseColor("#999999"))
                    binding.thstate.setImageResource(R.mipmap.tonghua1)
                    binding.ztiv.setImageResource(R.drawable.ov_ccc)
                    binding.roundProgress.progress = 0
                }else if(selectList2[position].voiceStatus==1){
                    binding.ztname.text = "当前对讲处于开启状态"
                    binding.ztname.setTextColor(Color.parseColor("#3974f6"))
                    binding.thstate.setImageResource(R.mipmap.tonghua3)
                    binding.ztiv.setImageResource(R.drawable.ov_3974f6)
                    binding.roundProgress.progress = 5000
                }

                binding.roundProgress.maxProgress = 5000




                getSiginData()
            }
        }
        binding.srecyclerview2.adapter = adapterSelect2

        binding.recyclerview.layoutManager = LinearLayoutManager(activity)
        adapter = FMeetingDeList3Adapter().apply {
            submitList(list)
            setOnItemClickListener { _, _, position ->
                com.dylanc.longan.startActivity<MeetingUserDectivity>("id" to list[position].id.toString())
            }
        }

        binding.recyclerview.adapter = adapter
        getData()


        binding.nameLl.setOnClickListener {
            binding.select2Ll.visibility = View.GONE
            if (binding.selectLl.visibility == View.VISIBLE) {
                binding.selectLl.visibility = View.GONE
            } else {
                binding.selectLl.visibility = View.VISIBLE
            }
        }
        binding.name2Ll.setOnClickListener {
            binding.selectLl.visibility = View.GONE
            if (binding.select2Ll.visibility == View.VISIBLE) {
                binding.select2Ll.visibility = View.GONE
            } else {
                binding.select2Ll.visibility = View.VISIBLE
            }

        }
        binding.select2Ll.setOnClickListener {
            if (binding.select2Ll.visibility == View.VISIBLE) {
                binding.select2Ll.visibility = View.GONE
            }
        }
        binding.selectLl.setOnClickListener {
            if (binding.selectLl.visibility == View.VISIBLE) {
                binding.selectLl.visibility = View.GONE
            }

        }
        binding.sous.setOnClickListener {
            if(siginlocationId.isNullOrEmpty()){
                toast("请选择签到点")

            }else{
                nameMobile = binding.et.text.toString().trim()
                getUserList()
            }
            activity?.hideSoftInput()
        }
        binding.et.setOnKeyListener(View.OnKeyListener { v, keyCode, event ->
            if (keyCode == KeyEvent.KEYCODE_ENTER) {
                // 监听到回车键，会执行2次该方法。按下与松开
                if(siginlocationId.isNullOrEmpty()){
                    toast("请选择签到点")

                }else{
                    nameMobile = binding.et.text.toString().trim()
                    binding.et.setText(nameMobile)
                    nameMobile?.let {
                        binding.et.setSelection(it.length)
                    }
                    getUserList()
                }

                activity?.hideSoftInput()
            }
            false
        })
        binding.moshiiv.setOnClickListener {
            if(siginlocationId.isNullOrEmpty()){
                toast("请选择签到点")
            }else{
                signUpStatus?.let { it1 -> setState(siginlocationId!!, it1) }
            }
        }
        binding.shaoma.setOnClickListener {
            if(siginlocationId.isNullOrEmpty()){
                toast("请选择签到点")

            }else{
                activity?.let {
                    XXPermissions.with(context)
                        .permission(Permission.CAMERA)
                        .request(object : OnPermissionCallback {

                            override fun onGranted(permissions: MutableList<String>, all: Boolean) {
                                if (!all) {
                                    toast("获取权限失败")
                                } else {
                                    var intent = Intent(it,ScanActivity::class.java)
                                    startActivityForResult(intent,1000)
                                }

                            }

                            override fun onDenied(permissions: MutableList<String>, never: Boolean) {
                                toast("获取权限失败")

                            }
                        })

                }
            }


        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(requestCode==1000){
            data?.let {
                var param = it.getStringExtra("QrCodeScanned")
                Log.d("tagggg","param=="+param)

                var signUpUser = JSON.parseObject(param, SignUpUser::class.java)

                signUpUser.signUpLocationId = siginlocationId
                signUpUser.signUpId = signUpId
                signUpUser.userMeetingId = signUpUser.id
                signUpUser.meetingId = signUpUser.meetingId
                signUpUser.userMeetingTypeName = signUpUser.supplement
                signUpUser.autoStatus =  autoStatus
                signUpUser.timeLong =  timeLong
                startActivity<SiginReActivity>("type" to 2, "data" to signUpUser)

            }

        }
    }
    fun setState(id: String, signUpStatu: String){
//        {id: 66, status: 1, voiceStatus: 1}
        val params = HashMap<String, String>()
        params["id"] = id
        if(signUpStatu=="1"){

            params["signUpStatus"] = "2"
        }else{

            params["signUpStatus"] = "1"
        }



        OkGo.put<String>(PageRoutes.Api_ed_meetingSignUpLocation)
            .tag(PageRoutes.Api_ed_meetingSignUpLocation)
            .upJson(JSON.toJSONString(params))
            .headers("Authorization", MMKV.mmkvWithID("MyDataMMKV").getString("token",""))
            .execute(object : RequestCallback<String>() {

                override fun onSuccess(response: Response<String>?) {
                    super.onSuccess(response)
                    if(signUpStatu=="1"){
                        signUpStatus = "2"
                    }else{
                        signUpStatus = "1"
                    }

                    if(signUpStatus == "1"){
                        binding.moshitv.text = "签入模式"
                        binding.moshiiv.setImageResource(R.mipmap.kaiguanguan)
                    }else{
                        binding.moshitv.text = "签出模式"
                        binding.moshiiv.setImageResource(R.mipmap.kaiguank)
                    }
                }

                override fun onError(response: Response<String>) {
                    super.onError(response)

                }

                override fun onFinish() {
                    super.onFinish()

                }


            })
    }
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
    }

    private fun getData() {
        if(!kv.getString("SiginUpListModel","").isNullOrEmpty()){
            var data = JSON.parseObject(kv.getString("SiginUpListModel",""), SiginUpListModel::class.java)
            selectList.clear()

            selectList.addAll(data.list)
            selectList[0].isMyselect = true
            signUpId = selectList[0].id
            type = selectList[0].type

            binding.nameTv.text = selectList[0].name
            adapterSelect?.notifyDataSetChanged()
            getList()
        }
    }
    private fun getList() {
        OkGo.get<List<SiginData>>(PageRoutes.Api_meetingSignUpLocation + meetingid + "&signUpId=" + signUpId)
            .tag(PageRoutes.Api_meetingSignUpLocation + meetingid+2)
            .headers("Authorization", kv.getString("token", ""))
            .execute(object : RequestCallback<List<SiginData>>() {
                override fun onSuccessNullData() {
                    super.onSuccessNullData()

                }

                override fun onMySuccess(data: List<SiginData>) {
                    super.onMySuccess(data)
                    selectList2.clear()
                    selectList2.addAll(data)
                    adapterSelect2?.notifyDataSetChanged()


                }

                override fun onError(response: Response<List<SiginData>>) {
                    super.onError(response)

                }

                override fun onFinish() {
                    super.onFinish()

                }


            })
    }
    private fun getSiginData() {
        OkGo.get<SiginData>(PageRoutes.Api_meetingSignUpLocationDe +  siginlocationId)
            .tag(PageRoutes.Api_meetingSignUpLocationDe )
            .headers("Authorization", kv.getString("token", ""))
            .execute(object : RequestCallback<SiginData>() {
                override fun onSuccessNullData() {
                    super.onSuccessNullData()

                }

                override fun onMySuccess(data: SiginData) {
                    super.onMySuccess(data)
                    binding.num1.text  =""+ data.beUserCount
                    binding.num2.text  = ""+data.signUpCount
                    binding.num3.text  = ""+data.localSignUpCount
//                    binding.num1.text  = data.signUpNeedCount
//                    binding.num2.text  = data.signUpCount
//                    binding.num3.text  = data.meetingSignUpCount
                    //01 签入模式 02 签入签出模式
                    signUpStatus = data.addressStatus
                   if(data.addressStatus == "1"){
                       binding.moshitv.text = "签入模式"
                       binding.moshiiv.setImageResource(R.mipmap.kaiguanguan)
                   }else{
                       binding.moshitv.text = "签出模式"
                       binding.moshiiv.setImageResource(R.mipmap.kaiguank)
                   }

                }

                override fun onError(response: Response<SiginData>) {
                    super.onError(response)

                }

                override fun onFinish() {
                    super.onFinish()

                }


            })
    }

    private fun getUserList() {
        if (nameMobile.isNullOrEmpty()) {
            list.clear()
            adapter?.notifyDataSetChanged()
            binding.recyclerview.visibility = View.GONE
            binding.kong.visibility = View.GONE
            return
        }
        var url =PageRoutes.Api_meetinguser +meetingid + "&signUpId=" + signUpId+"&signUpLocationId="+siginlocationId
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
                            if(list.size>0){
                                binding.kong.visibility = View.GONE
                                binding.recyclerview.visibility = View.VISIBLE
                            }else{
                                binding.kong.visibility = View.VISIBLE
                                binding.recyclerview.visibility = View.GONE
                            }

                        }else{
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


}
