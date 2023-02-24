package com.example.signin

import com.bumptech.glide.Glide
import com.dylanc.longan.toast
import com.example.signin.base.BaseBindingActivity
import com.example.signin.base.BaseViewModel

import com.example.signin.bean.MeetingUserDeData
import com.example.signin.databinding.ActMeetingUserInfoBinding

import com.example.signin.net.RequestCallback
import com.lzy.okgo.OkGo
import com.lzy.okgo.model.Response

class MeetingUserDectivity : BaseBindingActivity<ActMeetingUserInfoBinding, BaseViewModel>() {
    override fun getViewModel(): Class<BaseViewModel> = BaseViewModel::class.java

    //var data : MeetingUserDeData
    var id = ""
    override fun initData() {
        intent.getStringExtra("id")?.let { id = it }

        getData()
    }

    var examineStatus: Int = 0
    private fun getData() {

        OkGo.get<MeetingUserDeData>(PageRoutes.Api_meetinguser_data + id + "?id=" + id)
            .tag(PageRoutes.Api_meetinguser_data + id + "?id=" + id)
            .headers("Authorization", kv.getString("token", ""))
            .execute(object : RequestCallback<MeetingUserDeData>() {
                override fun onSuccessNullData() {
                    super.onSuccessNullData()

                }

                override fun onMySuccess(data: MeetingUserDeData) {
                    super.onMySuccess(data)

                    binding.itemYhxx.name.text = data.name
                    binding.itemYhxx.gongshiName.text = data.corporateName
                    binding.itemYhxx.zhengjiangType.text = data.userMeetingTypeName
                    Glide.with(this@MeetingUserDectivity).load(PageRoutes.BaseUrl + data.avatar)
                        .error(R.mipmap.touxiang).into(binding.itemYhxx.tx)
                    //订单
                    binding.itemDdxx.ddName.text = data.userOrder.ticketName
                    binding.itemDdxx.ddPrice.text = "¥" + data.userOrder.amount
                    //invoiceType	1 普票 2专票
                    binding.itemDdxx.ddType.text =
                        "开票类型:" + if (data.userOrder.invoiceType.equals("1")) "普票" else "专票"
                    binding.itemDdxx.ddTime.text = data.userOrder.createTime
                    binding.itemDdxx.ddNum.text = data.userOrder.invoiceNo
                    //0初始状态 1待审核 2审核成功 3审核失败
                    examineStatus = data.userOrder.examineStatus
                    when (data.userOrder.examineStatus) {
                        0 -> {
                            binding.itemDdxx.ddBtn.text = "初始状态"
                        }
                        1 -> {
                            binding.itemDdxx.ddBtn.text = "待审核"
                        }
                        2 -> {
                            binding.itemDdxx.ddBtn.text = "审核成功"
                        }
                        3 -> {
                            binding.itemDdxx.ddBtn.text = "审核失败"
                        }

                    }


                    //来程信息
                    binding.itemLcxx.lcName.text = data.userMeetingTrip.transport
                    binding.itemLcxx.lcData1.text = data.userMeetingTrip.startDate
                    binding.itemLcxx.lcDidian1.text = data.userMeetingTrip.startCity
                    binding.itemLcxx.lcJichang1.text = data.userMeetingTrip.startAddress
                    binding.itemLcxx.lcData2.text = data.userMeetingTrip.endDate
                    binding.itemLcxx.lcDidian2.text = data.userMeetingTrip.endCity
                    binding.itemLcxx.lcJichang1.text = data.userMeetingTrip.endAddress
                    binding.itemLcxx.time.text = data.userMeetingTrip.startTime+"-"+data.userMeetingTrip.endTime



                }

                override fun onError(response: Response<MeetingUserDeData>) {
                    super.onError(response)
                    toast(response.message())
                }

                override fun onFinish() {
                    super.onFinish()

                }


            })
    }

}