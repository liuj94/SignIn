package com.example.signin.fragment


import android.os.Build
import android.os.Bundle
import android.view.View
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.LinearLayoutManager
import com.alibaba.fastjson.JSON
import com.example.signin.LiveDataBus
import com.example.signin.MeetingSiginDectivity
import com.example.signin.PageRoutes
import com.example.signin.base.BaseBindingFragment
import com.example.signin.base.BaseViewModel
import com.example.signin.bean.SiginData
import com.example.signin.bean.SiginUpListData
import com.example.signin.databinding.FragMeetingde2Binding
import com.example.signin.adapter.FMeetingDeList2Adapter
import com.example.signin.adapter.SelectMeetingAdapter
import com.example.signin.bean.SiginUpListModel
import com.example.signin.net.RequestCallback
import com.lzy.okgo.OkGo
import com.lzy.okgo.model.Response

/**
 *   author : LiuJie
 *   date   : 2021/2/2513:36
 */
class MettingDe2Fragment : BaseBindingFragment<FragMeetingde2Binding, BaseViewModel>() {
    companion object {
        fun newInstance(meetingid: String): MettingDe2Fragment {
            val args = Bundle()
            args.putString("meetingid", meetingid)
            val fragment = MettingDe2Fragment()
            fragment.arguments = args
            return fragment
        }
    }

    override fun getViewModel(): Class<BaseViewModel> = BaseViewModel::class.java


    private var isVisibleFirst: Boolean = true
    private var adapter: FMeetingDeList2Adapter? = null
    private var adapterSelect: SelectMeetingAdapter? = null
    private var list: MutableList<SiginData> = ArrayList()
    private var selectList: MutableList<SiginUpListData> = ArrayList()
    var meetingid: String? = ""
    var signUpId: String? = ""

    @RequiresApi(Build.VERSION_CODES.M)
    override fun initData() {

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
                    LiveDataBus.get().with("selectLlGONE").postValue("1")
                    getList()
                }
            }
            binding.srecyclerview.adapter = adapterSelect

            binding.recyclerview.layoutManager = LinearLayoutManager(activity)
            adapter = FMeetingDeList2Adapter().apply {
                submitList(list)
                setOnItemClickListener { _, _, position ->
                    var params =meetingid + "&signUpId=" + list[position].signUpId+"&signUpLocationId="+list[position].id
                    com.dylanc.longan.startActivity<MeetingSiginDectivity>("id" to ""+list[position].id,
                        "name" to ""+list[position].name,"params" to ""+params,
                        "meetingid" to ""+meetingid,
                        "autoStatus" to ""+list[position].autoStatus,
                        "voiceStatus" to ""+list[position].speechStatus,
                        "timeLong" to list[position].timeLong,
                        "showType" to list[position].type,
                        "okMsg" to ""+list[position].okMsg,
                        "failedMsg" to ""+list[position].failedMsg,
                        "repeatMsg" to ""+list[position].repeatMsg,
                        "signUpId" to ""+list[position].signUpId)

                }
            }
            binding.recyclerview.adapter = adapter


            binding.nameLl.setOnClickListener {
                if(binding.selectLl.visibility == View.VISIBLE){
                    LiveDataBus.get().with("selectLlGONE").postValue("1")
                    binding.selectLl.visibility = View.GONE
                }else{
                    LiveDataBus.get().with("selectLlVISIBLE").postValue("1")
                    binding.selectLl.visibility = View.VISIBLE
                }
            }
            binding.selectLl.setOnClickListener {

                if(binding.selectLl.visibility == View.VISIBLE){
                    LiveDataBus.get().with("selectLlGONE").postValue("1")
                    binding.selectLl.visibility = View.GONE
                }

            }

        getData()
        getList()
        binding.refresh.setEnableLoadMore(false)
        binding.refresh.setOnRefreshListener {
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
                    try {
                        binding.num.text = "当前签到点（" + data.size + "）"
                        list.clear()
                        list.addAll(data)
                        adapter?.notifyDataSetChanged()

                    }catch(e:Exception){}

                }

                override fun onError(response: Response<List<SiginData>>) {
                    super.onError(response)

                }

                override fun onFinish() {
                    super.onFinish()
                    binding.refresh.finishRefresh()
                }


            })
    }

    private fun getData() {
        if(!kv.getString("SiginUpListModel","").isNullOrEmpty()){
            var data = JSON.parseObject(kv.getString("SiginUpListModel",""), SiginUpListModel::class.java)
            selectList.clear()

            var all = SiginUpListData()
            all.name = "全部签到站点"
            all.id = ""
            all.isMyselect = true
            selectList.add(all)
            selectList.addAll(data.list)
            adapterSelect?.notifyDataSetChanged()
        }
//        OkGo.get<List<SiginUpListData>>(PageRoutes.Api_meeting_sign_up_app_list + meetingid)
//            .tag(PageRoutes.Api_meeting_sign_up_app_list + meetingid)
//            .headers("Authorization", kv.getString("token", ""))
//            .execute(object : RequestCallback<List<SiginUpListData>>() {
//                override fun onSuccessNullData() {
//                    super.onSuccessNullData()
//
//                }
//
//                override fun onMySuccess(data: List<SiginUpListData>) {
//                    super.onMySuccess(data)
//                    selectList.clear()
////                    全部签到站点
//                    var all = SiginUpListData()
//                    all.name = "全部签到站点"
//                    all.id = ""
//                    all.isMyselect = true
//                    selectList.add(all)
//                    selectList.addAll(data)
//                    adapterSelect?.notifyDataSetChanged()
//
//                }
//
//                override fun onError(response: Response<List<SiginUpListData>>) {
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
    }


}
