package com.example.signin.fragment


import android.os.Build
import android.os.Bundle
import android.view.KeyEvent
import android.view.View
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.LinearLayoutManager
import com.alibaba.fastjson.JSON
import com.example.signin.MeetingUserDectivity
import com.example.signin.PageRoutes
import com.example.signin.adapter.FMeetingDeList3Adapter
import com.example.signin.adapter.SelectMeetingAdapter
import com.example.signin.adapter.SelectTypeDataAdapter
import com.example.signin.base.BaseBindingFragment
import com.example.signin.base.BaseViewModel
import com.example.signin.bean.*
import com.example.signin.databinding.FragMeetingde3Binding
import com.example.signin.net.JsonCallback
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
    var meetingid: String? = ""
    var signUpId: String? = ""
    var status: String? = ""
    var nameMobile: String? = ""

    @RequiresApi(Build.VERSION_CODES.M)
    override fun initData() {

        meetingid = arguments?.getString("meetingid", "")
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
                setSelect2Data(siginUpList[position].type)
                adapter?.setSiginUp2List(siginUp2List)
                status = ""
                binding.name2Tv.text = "筛选"
                getList()
            }
        }
        binding.srecyclerview.adapter = adapterSelect

        binding.srecyclerview2.layoutManager = LinearLayoutManager(activity)
        adapterSelect2 = SelectTypeDataAdapter().apply {
            submitList(siginUp2List)
            setOnItemClickListener { _, _, position ->
                if (siginUp2List[position].isMyselect) {
                    for (item in siginUp2List) {
                        item.isMyselect = false
                    }
                    status = ""
                    binding.name2Tv.text = "筛选"
                } else {
                    for (item in siginUp2List) {
                        item.isMyselect = false
                    }
                    siginUp2List[position].isMyselect = true
                    status = "" + siginUp2List[position].dictValue
                    binding.name2Tv.text = siginUp2List[position].dictLabel
                }

                adapterSelect2?.notifyDataSetChanged()
                binding.select2Ll.visibility = View.GONE
                getList()
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
        binding.refresh.setOnRefreshListener {
            pageNum = 1
            list.clear()
            getList()
        }
        binding.refresh.setOnLoadMoreListener {
            pageNum++
            getList()
        }

    }

    var pageNum = 1
    private fun getList() {
        if (signUpId.equals("")) {
            return
        }
        var url =
            PageRoutes.Api_meetinguser + meetingid + "&orderByColumn=createTime&isAsc=desc&&signUpId=" + signUpId + "&pageSize=10&pageNum=" + pageNum
        if (!status.isNullOrEmpty()) {
            url = "$url&status=$status"
        }
        if (!nameMobile.isNullOrEmpty()) {
            url = "$url&nameMobile=$nameMobile"
        }
        OkGo.get<MeetingUserModel>(url)
            .tag(url)
            .headers("Authorization", kv.getString("token", ""))
            .execute(object : JsonCallback<MeetingUserModel>(MeetingUserModel::class.java) {

                override fun onSuccess(response: Response<MeetingUserModel>) {

                    response?.let {
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

                override fun onError(response: Response<MeetingUserModel>?) {
                    super.onError(response)

                }

                override fun onFinish() {
                    binding.refresh.finishLoadMore()
                    binding.refresh.finishRefresh()
                }
            })

    }

    private fun getData() {
        if (!kv.getString("SiginUpListModel", "").isNullOrEmpty()) {
            var data =
                JSON.parseObject(kv.getString("SiginUpListModel", ""), SiginUpListModel::class.java)
            siginUpList.clear()
            if (data.list.size > 0) {
                data.list[0].isMyselect = true
                signUpId = data.list[0].id
                binding.nameTv.text = data.list[0].name
                siginUpList.addAll(data.list)
                adapterSelect?.notifyDataSetChanged()
                setSelect2Data(data.list[0].type)
                getList()
            }

        }

    }

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
            }
        }
        adapterSelect2?.notifyDataSetChanged();
        adapter?.setSiginUp2List(siginUp2List)
    }


}
