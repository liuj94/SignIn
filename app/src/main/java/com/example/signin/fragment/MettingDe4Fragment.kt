package com.example.signin.fragment


import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.view.KeyEvent
import android.view.View
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.LinearLayoutManager
import com.alibaba.fastjson.JSON
import com.dylanc.longan.toast
import com.example.signin.PageRoutes
import com.example.signin.R
import com.example.signin.base.BaseBindingFragment
import com.example.signin.base.BaseViewModel
import com.example.signin.databinding.FragMeetingde4Binding
import com.example.signin.adapter.SelectDataAdapter
import com.example.signin.adapter.SelectMeetingAdapter
import com.example.signin.bean.SiginData
import com.example.signin.bean.SiginUpListData
import com.example.signin.bean.SiginUpListModel
import com.example.signin.net.RequestCallback
import com.lzy.okgo.OkGo
import com.lzy.okgo.model.Response
import com.tencent.mmkv.MMKV

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


    private var adapterSelect2: SelectDataAdapter? = null
    private var adapterSelect: SelectMeetingAdapter? = null
    private var selectList2: MutableList<SiginData> = ArrayList()
    private var selectList: MutableList<SiginUpListData> = ArrayList()
    var meetingid :String? = ""
    var siginlocationId :String? = ""
    var signUpStatus :String? = ""
    var signUpId: String? = ""
    var nameMobile: String? = ""
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
                    siginlocationId = "" + selectList2[position].id
                    binding.name2Tv.text = selectList2[position].name
                }

                adapterSelect2?.notifyDataSetChanged()
                binding.select2Ll.visibility = View.GONE


                binding.time.text = ""+selectList2[position].timeLong+"分钟"
                if(selectList2[position].voiceStatus==2){
                    binding.ztname.text = "当前对讲处于关闭状态"
                    binding.ztname.setTextColor(Color.parseColor("#999999"))
                    binding.thstate.setImageResource(R.mipmap.tonghua1)
                    binding.ztiv.setImageResource(R.drawable.ov_ccc)
                }else if(selectList2[position].voiceStatus==1){
                    binding.ztname.text = "当前对讲处于开启状态"
                    binding.ztname.setTextColor(Color.parseColor("#3974f6"))
                    binding.thstate.setImageResource(R.mipmap.tonghua3)
                    binding.ztiv.setImageResource(R.drawable.ov_3974f6)
                }
                binding.roundProgress.progress = selectList2[position].timeLong
                binding.roundProgress.maxProgress = 5000




                getSiginData()
            }
        }
        binding.srecyclerview2.adapter = adapterSelect2
        getData()


        binding.nameLl.setOnClickListener {
            if (binding.selectLl.visibility == View.VISIBLE) {
                binding.selectLl.visibility = View.GONE
            } else {
                binding.selectLl.visibility = View.VISIBLE
            }
        }
        binding.name2Ll.setOnClickListener {
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
            nameMobile = binding.et.text.toString().trim()

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
}
