package com.example.signin

import android.content.Intent
import android.util.Log
import android.view.KeyEvent
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.alibaba.fastjson.JSON
import com.dylanc.longan.activity
import com.dylanc.longan.startActivity
import com.dylanc.longan.toast
import com.example.signin.adapter.FMeetingDeList3Adapter
import com.example.signin.base.BaseBindingActivity
import com.example.signin.base.BaseViewModel
import com.example.signin.bean.MeetingUserData

import com.example.signin.bean.MeetingUserModel
import com.example.signin.bean.SiginData
import com.example.signin.bean.SignUpUser
import com.example.signin.databinding.ActMeetingSigindeBinding
import com.example.signin.net.JsonCallback

import com.example.signin.net.RequestCallback
import com.hjq.permissions.OnPermissionCallback
import com.hjq.permissions.Permission
import com.hjq.permissions.XXPermissions
import com.lzy.okgo.OkGo
import com.lzy.okgo.model.Response
import com.tencent.mmkv.MMKV

class MeetingSiginDectivity : BaseBindingActivity<ActMeetingSigindeBinding, BaseViewModel>() {
    override fun getViewModel(): Class<BaseViewModel> = BaseViewModel::class.java

    var id = ""
    var signUpId = ""
    var meetingid = ""
    var autoStatus = ""
    var timeLong = 3
    var name = ""
    var signUpStatus = ""
    var nameMobile: String? = ""
    var params: String? = ""
    private var list: MutableList<MeetingUserData> = ArrayList()
    private var adapter: FMeetingDeList3Adapter? = null
    override fun initData() {
        intent.getStringExtra("id")?.let { id = it }
        intent.getStringExtra("name")?.let { name = it }
        intent.getStringExtra("params")?.let { params = it }
        intent.getStringExtra("meetingid")?.let { meetingid = it }
        intent.getStringExtra("signUpId")?.let { signUpId = it }
        intent.getStringExtra("autoStatus")?.let { autoStatus = it }
        intent.getIntExtra("timeLong",3)?.let { timeLong = it }
        binding.recyclerview.layoutManager = LinearLayoutManager(activity)
        adapter = FMeetingDeList3Adapter().apply {
            submitList(list)
            setOnItemClickListener { _, _, position ->
                com.dylanc.longan.startActivity<MeetingUserDectivity>("id" to list[position].id.toString())
            }
        }

        binding.recyclerview.adapter = adapter
        binding.sous.setOnClickListener {
            nameMobile = binding.et.text.toString().trim()
            binding.et.setText(nameMobile)
            getList()
            activity?.hideSoftInput()
        }
        binding.et.setOnKeyListener(View.OnKeyListener { v, keyCode, event ->
            if (keyCode == KeyEvent.KEYCODE_ENTER) {
                // 监听到回车键，会执行2次该方法。按下与松开
                nameMobile = binding.et.text.toString().trim()
                binding.et.setText(nameMobile)
                nameMobile?.let {
                    binding.et.setSelection(it.length)
                }


                getList()
                activity?.hideSoftInput()
            }
            false
        })

        getSiginData()
        binding.sm.setOnClickListener {
            startActivity<ScanActivity>()
        }
        binding.moshill.setOnClickListener {
            setState()
        }
        binding.shaoma.setOnClickListener {
           activity?.let {
                    XXPermissions.with(activity)
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
    private fun getList() {
        if (nameMobile.isNullOrEmpty()) {
            list.clear()
            adapter?.notifyDataSetChanged()
            binding.recyclerview.visibility = View.GONE
            binding.kong.visibility = View.GONE
            return
        }
        var url =PageRoutes.Api_meetinguser +params
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
    var addressStatus = "1"
    fun setState(){

        val params = HashMap<String, String>()
        params["id"] = id
        if(addressStatus.equals("1")){
            addressStatus = "2"
            params["addressStatus"] = "2"
        }else{
            addressStatus = "1"
            params["addressStatus"] = "1"
        }



        OkGo.put<String>(PageRoutes.Api_ed_meetingSignUpLocation)
            .tag(PageRoutes.Api_ed_meetingSignUpLocation)
            .upJson(JSON.toJSONString(params))
            .headers("Authorization", MMKV.mmkvWithID("MyDataMMKV")
                .getString("token",""))
            .execute(object : RequestCallback<String>() {

                override fun onSuccess(response: Response<String>?) {
                    super.onSuccess(response)
                    if(addressStatus == "1"){
                        binding.moshitv.text = "签入模式"
                        binding.moshiiv.setImageResource(R.mipmap.kaiguanguan)
                    }else{
                        binding.moshitv.text = "签出模式"
                        binding.moshiiv.setImageResource(R.mipmap.kaiguank)
                    }
                }

                override fun onError(response: Response<String>) {
                    super.onError(response)
                    toast("")
                }

                override fun onFinish() {
                    super.onFinish()

                }


            })
    }
    private fun getSiginData() {
        OkGo.get<SiginData>(PageRoutes.Api_meetingSignUpLocationDe +id)
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
                    //01 签入模式 02 签入签出模式
                    signUpStatus = data.addressStatus
                    if(data.modelType == "1"){
                        addressStatus = "1"
                        binding.moshitv.text = "签入模式"
                        binding.moshiiv.setImageResource(R.mipmap.kaiguanguan)
                    }else{
                        addressStatus = "2"
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

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(requestCode==1000){
            data?.let {
                var param = it.getStringExtra("QrCodeScanned")
                Log.d("tagggg","param=="+param)

                var signUpUser = JSON.parseObject(param, SignUpUser::class.java)

                signUpUser.signUpLocationId = id
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
}