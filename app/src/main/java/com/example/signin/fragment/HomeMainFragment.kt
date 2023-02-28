package com.example.signin.fragment


import android.graphics.Color
import android.os.Build
import android.view.KeyEvent
import android.view.View
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.LinearLayoutManager
import com.dylanc.longan.startActivity
import com.example.signin.MeetingDeActivity
import com.example.signin.PageRoutes
import com.example.signin.R

import com.example.signin.base.BaseBindingFragment
import com.example.signin.base.BaseViewModel
import com.example.signin.bean.MeetingData

import com.example.signin.databinding.FragHomeBinding
import com.example.signin.adapter.HomeListAdapter
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


    private var adapter: HomeListAdapter? = null
    private var list: MutableList<MeetingData> = ArrayList()

    //-1,全部;2,进行中;3,过期
    var state = -1

    //pageNum:
    //1
    //pageSize:
    var pageNum = 1

    var meetingName = ""

    @RequiresApi(Build.VERSION_CODES.M)
    override fun initData() {

        binding.soubtn.setOnClickListener {
            if (binding.sousll.visibility == View.VISIBLE) {
                binding.sousll.visibility = View.GONE
                binding.et.setText("")
                meetingName = ""
                pageNum = 1
                getData()
            } else {
                binding.sousll.visibility = View.VISIBLE
            }

        }
        binding.sous.setOnClickListener {
            meetingName = binding.et.text.toString().trim()
            pageNum = 1
            list.clear()
            getData()
        }
        binding.et.setOnKeyListener(View.OnKeyListener { v, keyCode, event ->
            if (keyCode == KeyEvent.KEYCODE_ENTER) {
                // 监听到回车键，会执行2次该方法。按下与松开
                if (event.action == KeyEvent.ACTION_UP) {
                    meetingName = binding.et.text.toString().trim()
                    binding.et.setText(meetingName)
                    meetingName?.let {
                        binding.et.setSelection(it.length)
                    }
                    pageNum = 1
                    list.clear()
                    getData()
                    activity?.hideSoftInput()
                }

            }
            false
        })
        binding.btnLl1.setOnClickListener {
            list.clear()
            state = -1
            pageNum = 1
            binding.btnTv1.setTextColor(Color.parseColor("#ff333333"))
            binding.btnTv2.setTextColor(Color.parseColor("#5B5B5B"))
            binding.btnTv3.setTextColor(Color.parseColor("#5B5B5B"))
            binding.btnV1.visibility = View.VISIBLE
            binding.btnV2.visibility = View.INVISIBLE
            binding.btnV3.visibility = View.INVISIBLE
            getData()
        }
        binding.btnLl2.setOnClickListener {
            list.clear()
            pageNum = 1
            state = 2
            binding.btnTv2.setTextColor(Color.parseColor("#ff333333"))
            binding.btnTv1.setTextColor(Color.parseColor("#5B5B5B"))
            binding.btnTv3.setTextColor(Color.parseColor("#5B5B5B"))
            binding.btnV2.visibility = View.VISIBLE
            binding.btnV1.visibility = View.INVISIBLE
            binding.btnV3.visibility = View.INVISIBLE
            getData()
        }
        binding.btnLl3.setOnClickListener {
            list.clear()
            state = 3
            pageNum = 1
            binding.btnTv3.setTextColor(Color.parseColor("#ff333333"))
            binding.btnTv2.setTextColor(Color.parseColor("#5B5B5B"))
            binding.btnTv1.setTextColor(Color.parseColor("#5B5B5B"))
            binding.btnV3.visibility = View.VISIBLE
            binding.btnV2.visibility = View.INVISIBLE
            binding.btnV1.visibility = View.INVISIBLE
            getData()
        }
        binding.recyclerview.layoutManager = LinearLayoutManager(activity)
        adapter = HomeListAdapter().apply {
            submitList(list)
            setOnItemClickListener { _, _, position ->
                setEmptyViewLayout(requireActivity(), R.layout.layout_emptyview)
                startActivity<MeetingDeActivity>(
                    "meetingId" to "" + list[position].id,
                    "meetingName" to "" + list[position].name
                )
            }
        }

        binding.recyclerview.adapter = adapter
        getData()
        binding.refresh.setOnRefreshListener {
            pageNum = 1
            list.clear()
            getData()
        }
        binding.refresh.setOnLoadMoreListener {
            pageNum++
            getData()
        }

    }


    private fun getData() {
        mViewModel.isShowLoading.value = true
        var url =
            PageRoutes.Api_meetingList + "pageNum=" + pageNum + "&pageSize=10&name=" + meetingName
        if (state != -1) {
            url =
                PageRoutes.Api_meetingList + "status=" + state + "&pageNum=" + pageNum + "&pageSize=10&name=" + meetingName
        }

        OkGo.get<List<MeetingData>>(url)
            .tag(url)
            .headers("Authorization", kv.getString("token", ""))
            .execute(object : RequestCallback<List<MeetingData>>() {
                override fun onSuccessNullData() {
                    super.onSuccessNullData()

                }

                override fun onMySuccess(data: List<MeetingData>) {
                    super.onMySuccess(data)
                    if (pageNum == 1) {
                        list.clear()
                    }
                    list.addAll(data)
                    adapter?.notifyDataSetChanged()
                    if (list.size <= 0) {
                        binding.recyclerview.visibility = View.GONE
                        binding.kong.visibility = View.VISIBLE
                    } else {
                        binding.recyclerview.visibility = View.VISIBLE
                        binding.kong.visibility = View.GONE
                    }


                }

                override fun onError(response: Response<List<MeetingData>>) {
                    super.onError(response)

                }

                override fun onFinish() {
                    super.onFinish()
                    binding.refresh.finishRefresh()
                    binding.refresh.finishLoadMore()
                    mViewModel.isShowLoading.value = false
                }


            })
    }


}
