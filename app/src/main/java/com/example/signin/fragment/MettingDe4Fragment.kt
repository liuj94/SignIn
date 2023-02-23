package com.example.signin.fragment


import android.os.Build
import android.os.Bundle
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.LinearLayoutManager
import com.dylanc.longan.toast
import com.example.signin.PageRoutes
import com.example.signin.base.BaseBindingFragment
import com.example.signin.base.BaseViewModel
import com.example.signin.bean.MeetingData
import com.example.signin.databinding.FragMeetingde4Binding
import com.example.signin.adapter.HomeListAdapter
import com.example.signin.net.RequestCallback
import com.lzy.okgo.OkGo
import com.lzy.okgo.model.Response

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


    private var isVisibleFirst: Boolean = true
    private var adapter: HomeListAdapter? = null
    private var list: MutableList<MeetingData> = ArrayList()
    var meetingid :String? = ""

    @RequiresApi(Build.VERSION_CODES.M)
    override fun initData() {
        meetingid = arguments?.getString("meetingid", "")
        if ( isVisibleFirst) {
//            setStatusBarHeight(toolbarView)
            isVisibleFirst = false

            binding.recyclerview.layoutManager = LinearLayoutManager(activity)
            adapter = HomeListAdapter().apply {
                submitList(list)
                setOnItemClickListener { _, _, position ->

                }
            }
            binding.recyclerview.adapter = adapter
            getData()
            binding.refresh.setOnRefreshListener {
                getData()
            }
        }

    }

    private fun getData() {
       var token =  kv.getString("token","")
        OkGo.get<List<MeetingData>>(PageRoutes.Api_meetingList)
            .tag(PageRoutes.Api_meetingList)
            .headers("Authorization",kv.getString("token",""))
            .execute(object : RequestCallback<List<MeetingData>>() {
                override fun onSuccessNullData() {
                    super.onSuccessNullData()

                }

                override fun onMySuccess(data: List<MeetingData>) {
                    super.onMySuccess(data)

                    adapter?.submitList(data)
                    adapter?.notifyDataSetChanged()

                }

                override fun onError(response: Response<List<MeetingData>>) {
                    super.onError(response)
                    toast(response.message())
                }

                override fun onFinish() {
                    super.onFinish()

                }


            })
    }


}
