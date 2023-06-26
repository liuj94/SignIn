package com.example.signin.fragment


import android.os.Build
import android.os.Bundle
import android.view.KeyEvent
import android.view.View
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.LinearLayoutManager
import com.alibaba.fastjson.JSON
import com.example.signin.AppManager
import com.example.signin.LiveDataBus
import com.example.signin.MeetingUserDectivity
import com.example.signin.PageRoutes
import com.example.signin.SubstepDelayedLoad
import com.example.signin.adapter.FMeetingDeList3Adapter
import com.example.signin.adapter.SelectMeetingAdapter
import com.example.signin.adapter.SelectMeetingAdapter2
import com.example.signin.adapter.SelectTypeDataAdapter
import com.example.signin.base.BaseBindingFragment
import com.example.signin.base.BaseViewModel
import com.example.signin.bean.*
import com.example.signin.databinding.FragMeetingde3Binding
import com.example.signin.net.JsonCallback
import com.example.signin.net.RequestCallback
import com.lzy.okgo.OkGo
import com.lzy.okgo.model.Response

/**
 *   author : LiuJie
 *   date   : 2021/2/2513:36
 */
class MettingDe3Fragment : BaseBindingFragment<FragMeetingde3Binding, BaseViewModel>() {
    companion object {
        fun newInstance(meetingid: String): MettingDe3Fragment {
            val args = Bundle()
            args.putString("meetingid", meetingid)
            val fragment = MettingDe3Fragment()
            fragment.arguments = args
            return fragment
        }
    }

    override fun getViewModel(): Class<BaseViewModel> = BaseViewModel::class.java


    private var adapter: FMeetingDeList3Adapter? = null
    private var adapterSelect: SelectMeetingAdapter? = null
    private var adapterSelect2: SelectTypeDataAdapter? = null
    private var list: MutableList<MeetingUserData> = ArrayList()
    private var siginUpList: MutableList<SiginUpListData> = ArrayList()
    private var siginUp2List: MutableList<TypeData> = ArrayList()
    private var adapterSelect3: SelectMeetingAdapter2? = null
    private var selectList3: MutableList<SiginData> = ArrayList()
    var meetingid: String? = ""
    var signUpId: String? = ""
    var signUpLocationId: String? = ""
    var status: String? = ""
    var nameMobile: String? = ""

    @RequiresApi(Build.VERSION_CODES.M)
    override fun initData() {

        meetingid = arguments?.getString("meetingid", "")
        binding.srecyclerview3.layoutManager = LinearLayoutManager(activity)
        adapterSelect3 = SelectMeetingAdapter2().apply {
            submitList(selectList3)
            setOnItemClickListener { _, _, position ->
                for (item in selectList3) {
                    item.isMyselect = false
                }
                selectList3[position].isMyselect = true
                signUpLocationId = "" + selectList3[position].id
                binding.qddTv.text = selectList3[position].name
                adapterSelect3?.notifyDataSetChanged()
                binding.selectLl3.visibility = View.GONE
                LiveDataBus.get().with("selectLlGONE").postValue("1")
                list.clear()
                getList()
            }
        }
        binding.srecyclerview3.adapter = adapterSelect3

        binding.srecyclerview.layoutManager = LinearLayoutManager(activity)
        adapterSelect = SelectMeetingAdapter().apply {
            submitList(siginUpList)
            setOnItemClickListener { _, _, position ->
                for (item in siginUpList) {
                    item.isMyselect = false
                }
                siginUpList[position].isMyselect = true
                signUpId = "" + siginUpList[position].id
                binding.nameTv.text = siginUpList[position].name
                adapterSelect?.notifyDataSetChanged()
                binding.selectLl.visibility = View.GONE
                LiveDataBus.get().with("selectLlGONE").postValue("1")
                setSelect2Data(siginUpList[position].type)
                adapter?.setSiginUp2List(siginUp2List)
                status = ""
                binding.name2Tv.text = "筛选"
                pageNum = 1
                list.clear()
                getqddList()
            }
        }
        binding.srecyclerview.adapter = adapterSelect

        binding.srecyclerview2.layoutManager = LinearLayoutManager(activity)
        adapterSelect2 = SelectTypeDataAdapter().apply {
            submitList(siginUp2List)
            setOnItemClickListener { _, _, position ->
                for (item in siginUp2List) {
                    item.isMyselect = false
                }
                siginUp2List[position].isMyselect = true
                if (siginUp2List[position].dictValue.equals("-999")) {

                    status = ""
//                    binding.name2Tv.text = "筛选"
                } else {
                    status = "" + siginUp2List[position].dictValue

                }
                binding.name2Tv.text = siginUp2List[position].dictLabel
                adapterSelect2?.notifyDataSetChanged()
                binding.select2Ll.visibility = View.GONE
                LiveDataBus.get().with("selectLlGONE").postValue("1")
                pageNum = 1
                list.clear()
                getList()
            }
        }
        binding.srecyclerview2.adapter = adapterSelect2

        binding.recyclerview.layoutManager = LinearLayoutManager(activity)
        adapter = FMeetingDeList3Adapter().apply {
            submitList(list)
            setOnItemClickListener { _, _, position ->
                com.dylanc.longan.startActivity<MeetingUserDectivity>(
                    "id" to list[position].id.toString(),
                    "showType" to 0
                )
            }
        }
        binding.recyclerview.adapter = adapter

        delayed()

        binding.nameLl.setOnClickListener {
            binding.select2Ll.visibility = View.GONE
            binding.selectLl3.visibility = View.GONE
            if (binding.selectLl.visibility == View.VISIBLE) {
                binding.selectLl.visibility = View.GONE
                LiveDataBus.get().with("selectLlGONE").postValue("1")
            } else {
                binding.selectLl.visibility = View.VISIBLE
                LiveDataBus.get().with("selectLlVISIBLE").postValue("1")
            }
        }
        binding.qddLl.setOnClickListener {
            binding.select2Ll.visibility = View.GONE
            binding.selectLl.visibility = View.GONE

            if (binding.selectLl3.visibility == View.VISIBLE) {
                binding.selectLl3.visibility = View.GONE
                LiveDataBus.get().with("selectLlGONE").postValue("1")
            } else {
                binding.selectLl3.visibility = View.VISIBLE
                LiveDataBus.get().with("selectLlVISIBLE").postValue("1")
            }
        }
        binding.name2Ll.setOnClickListener {
            binding.selectLl.visibility = View.GONE
            binding.selectLl3.visibility = View.GONE
            if (binding.select2Ll.visibility == View.VISIBLE) {
                binding.select2Ll.visibility = View.GONE
                LiveDataBus.get().with("selectLlGONE").postValue("1")
            } else {
                binding.select2Ll.visibility = View.VISIBLE
                LiveDataBus.get().with("selectLlVISIBLE").postValue("1")
            }

        }
        binding.select2Ll.setOnClickListener {
            if (binding.select2Ll.visibility == View.VISIBLE) {
                binding.select2Ll.visibility = View.GONE
                LiveDataBus.get().with("selectLlGONE").postValue("1")
            }
        }
        binding.selectLl.setOnClickListener {
            if (binding.selectLl.visibility == View.VISIBLE) {
                binding.selectLl.visibility = View.GONE
                LiveDataBus.get().with("selectLlGONE").postValue("1")
            }

        }
        binding.selectLl3.setOnClickListener {
            if (binding.selectLl3.visibility == View.VISIBLE) {
                binding.selectLl3.visibility = View.GONE
                LiveDataBus.get().with("selectLlGONE").postValue("1")
            }

        }
        binding.sous.setOnClickListener {
            pageNum = 1
            nameMobile = binding.et.text.toString().trim()
            list.clear()
            getList()
            activity?.hideSoftInput()
        }
        binding.et.setOnKeyListener(View.OnKeyListener { v, keyCode, event ->
            if (keyCode == KeyEvent.KEYCODE_ENTER) {
                // 监听到回车键，会执行2次该方法。按下与松开
                if (event.action == KeyEvent.ACTION_UP) {
                    nameMobile = binding.et.text.toString().trim()
                    binding.et.setText(nameMobile)
                    nameMobile?.let {
                        binding.et.setSelection(it.length)
                    }
                    pageNum = 1
                    list.clear()
                    getList()
                    activity?.hideSoftInput()
                }
            }
            false
        })
        binding.refresh.setOnRefreshListener {
            pageNum = 1
            list.clear()
            getList()
        }
        binding.refresh.setOnLoadMoreListener {
            pageNum++
            getList()
        }

        LiveDataBus.get().with("JWebSocketClientlocation", String::class.java)
            .observeForever {
                try {
                    if (AppManager.getAppManager().activityInstanceIsLive(activity)) {
                        getDatasign_up_app_list()
                    }
                } catch (e: java.lang.Exception) {
                }

            }

    }

    private val delayedLoad = SubstepDelayedLoad()
    private fun delayed() {
        delayedLoad
            .delayed(1000)
            .run {
                //延时加载布局
                getData()
                delayedLoad.clearAllRunable()

            }
            .start()
    }

    var pageNum = 1
    private fun getList() {
        if (signUpId.equals("")) {
            return
        }
        var url =
//            PageRoutes.Api_meeting_sign_up_data_list + meetingid + "&orderByColumn=createTime&isAsc=desc&signUpId=" + signUpId + "&pageSize=10&pageNum=" + pageNum
            PageRoutes.Api_meeting_sign_up_data_list + meetingid + "&signUpId=" + signUpId + "&signUpLocationId=" + signUpLocationId + "&pageSize=10&pageNum=" + pageNum
        if (signUpLocationId.isNullOrEmpty() || signUpLocationId.equals("0")) {
            url =
//            PageRoutes.Api_meeting_sign_up_data_list + meetingid + "&orderByColumn=createTime&isAsc=desc&signUpId=" + signUpId + "&pageSize=10&pageNum=" + pageNum
                PageRoutes.Api_meeting_sign_up_data_list + meetingid + "&signUpId=" + signUpId + "&pageSize=10&pageNum=" + pageNum

        }

        if (!status.isNullOrEmpty()) {
            url = "$url&signUpStatus=$status"
        }
        if (!nameMobile.isNullOrEmpty()) {
            url = "$url&nameMobile=$nameMobile"
        }
        OkGo.get<MeetingUserModel>(url)
            .tag(url)
            .headers("Authorization", kv.getString("token", ""))
            .execute(object : JsonCallback<MeetingUserModel>(MeetingUserModel::class.java) {

                override fun onSuccess(response: Response<MeetingUserModel>) {
                    try {
                        response.body()?.let {
                            response.body().data?.let {
                                list.addAll(response.body().data)
                                adapter?.notifyDataSetChanged()
                                binding.num.text = "名单列表（" + response.body().total + "）"
                                if (pageNum == 1 && list.size <= 0) {
                                    binding.recyclerview.visibility = View.GONE
                                    binding.kong.visibility = View.VISIBLE
                                } else {
                                    binding.recyclerview.visibility = View.VISIBLE
                                    binding.kong.visibility = View.GONE
                                }
                            }

                        }
                    } catch (e: Exception) {
                    }

                }

                override fun onError(response: Response<MeetingUserModel>?) {
                    super.onError(response)

                }

                override fun onFinish() {
                    try {
                        binding.refresh.finishLoadMore()
                        binding.refresh.finishRefresh()
                    }catch (e:Exception){}

                }
            })

    }

    private fun getData() {

        if (!kv.getString("SiginUpListModelmeetingId"+meetingid, "").isNullOrEmpty()) {
            var data =
                JSON.parseObject(kv.getString("SiginUpListModelmeetingId"+meetingid, ""), SiginUpListModel::class.java)
            siginUpList.clear()
            try {

                if (data.list.size > 0) {
                    data.list[0].isMyselect = true
                    signUpId = data.list[0].id
                    binding.nameTv.text = data.list[0].name
                    siginUpList.addAll(data.list)
                    adapterSelect?.notifyDataSetChanged()
                    setSelect2Data(data.list[0].type)
                    getqddList()


                }
            }catch (e:Exception){}

        }else{
            getDatasign_up_app_list()
        }

    }

    private fun getDatasign_up_app_list() {

        OkGo.get<List<SiginUpListData>>(PageRoutes.Api_meeting_sign_up_app_list + meetingid)
            .tag(PageRoutes.Api_meeting_sign_up_app_list + meetingid + "f4")
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
                    try {
                        siginUpList.clear()
                        if (data.size > 0) {
                            data[0].isMyselect = true
                            signUpId = data[0].id
                            binding.nameTv.text = data[0].name
                            siginUpList.addAll(data)
                            adapterSelect?.notifyDataSetChanged()
                            setSelect2Data(data[0].type)
                            getqddList()


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

    private fun setSelect2Data(type: Int) {
        //1 注册签到2 来程签到3 入住签到4 会场签到5 餐饮签到6 礼品签到7 返程签到
        var model: TypeModel
        if (!kv.getString("TypeModel", "").isNullOrEmpty()) {
            model = JSON.parseObject(kv.getString("TypeModel", ""), TypeModel::class.java)
            siginUp2List.clear()
            var all: TypeData = TypeData()
            all.dictLabel = "全部"
            all.dictValue = "-999"
            siginUp2List.add(all)
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
        adapterSelect2?.notifyDataSetChanged()
        adapter?.setSiginUp2List(siginUp2List)
    }

    private fun getqddList() {
        OkGo.get<List<SiginData>>(PageRoutes.Api_meetingSignUpLocation + meetingid + "&signUpId=" + signUpId)
            .tag(PageRoutes.Api_meetingSignUpLocation + meetingid + 2)
            .headers("Authorization", kv.getString("token", ""))
            .execute(object : RequestCallback<List<SiginData>>() {
                override fun onSuccessNullData() {
                    super.onSuccessNullData()

                }

                override fun onMySuccess(data: List<SiginData>) {
                    super.onMySuccess(data)
                    try {
                        selectList3.clear()
                        var a = SiginData()
                        a.name = "全部"
                        a.id = 0
                        selectList3.add(a)
                        selectList3.addAll(data)
                        selectList3[0].isMyselect = true
                        signUpLocationId = "" + selectList3[0].id
                        binding.qddTv.text = selectList3[0].name
                        adapterSelect3?.notifyDataSetChanged()
                        pageNum = 1
                        list.clear()
                        getList()
                    } catch (e: Exception) {
                    }

                }

                override fun onError(response: Response<List<SiginData>>) {
                    super.onError(response)

                }

                override fun onFinish() {
                    super.onFinish()
                    try {
                        binding.refresh.finishRefresh()
                    }catch (e:Exception){}

                }


            })
    }


}
