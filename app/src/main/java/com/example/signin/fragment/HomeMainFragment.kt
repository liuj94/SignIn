package com.example.signin.fragment


import android.os.Build
import android.view.View
import android.widget.EditText
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.afollestad.materialdialogs.MaterialDialog
import com.afollestad.materialdialogs.customview.customView
import com.dylanc.longan.toast
import com.example.signin.PageRoutes
import com.example.signin.R
import com.example.signin.base.BaseBindingFragment
import com.example.signin.base.BaseViewModel
import com.example.signin.bean.MeetingData
import com.example.signin.bean.UserInfoData
import com.example.signin.databinding.FragHomeBinding
import com.example.signin.mvvm.ui.adapter.HomeListAdapter
import com.example.signin.net.RequestCallback
import com.lzy.okgo.OkGo
import com.lzy.okgo.model.Response

/**
 *   author : LiuJie
 *   date   : 2021/2/2513:36
 */
class HomeMainFragment : BaseBindingFragment<FragHomeBinding, BaseViewModel>() {

    companion object {
        fun newInstance(): HomeMainFragment {
            return HomeMainFragment()
        }
    }

    override fun getViewModel(): Class<BaseViewModel> = BaseViewModel::class.java


    private var isVisibleFirst: Boolean = true
    private var adapter: HomeListAdapter ? = null
    private var list: MutableList<MeetingData>  = ArrayList()

    @RequiresApi(Build.VERSION_CODES.M)
    override fun initData() {
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
        }

    }

    private fun getData() {
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
