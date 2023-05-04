package com.example.signin

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.afollestad.materialdialogs.LayoutMode
import com.afollestad.materialdialogs.MaterialDialog
import com.afollestad.materialdialogs.bottomsheets.BottomSheet
import com.afollestad.materialdialogs.customview.customView
import com.alibaba.fastjson.JSON
import com.dylanc.longan.activity
import com.example.signin.adapter.SelectAdapter
import com.example.signin.base.BaseBindingActivity
import com.example.signin.base.BaseViewModel
import com.example.signin.bean.MeetingUserDeData
import com.example.signin.bean.TypeModel
import com.example.signin.databinding.ActInformationReviewBinding
import com.example.signin.net.RequestCallback
import com.lzy.okgo.OkGo
import com.lzy.okgo.model.Response
import java.util.HashMap


class ExamineActivity : BaseBindingActivity<ActInformationReviewBinding, BaseViewModel>() {
    override fun getViewModel(): Class<BaseViewModel> = BaseViewModel::class.java

    var order: MeetingUserDeData.UserOrderBean = MeetingUserDeData.UserOrderBean()
    var failRemark: String = ""
    var params = HashMap<String, Any>()
    override fun initData() {

        intent.getSerializableExtra("order")?.let { order = it as MeetingUserDeData.UserOrderBean }
        binding.btn1.setOnClickListener {

            select()
        }
        binding.btn2.setOnClickListener {
            params["orderId"] = order.id
            params["examine"] = true
            examine()
        }
        binding.stateIv.visibility = View.GONE
        binding.order.text = "订单编号：" + order.orderNo
        binding.name.text = "参会姓名：" + order.userName
        binding.sjPrice.text = "¥ " + order.amount
        binding.yhPrice.text = "¥ 0"
        binding.playPrice.text = "¥ " + order.amount
        binding.mpname.text = "" + order.ticketName

        binding.playType.visibility = View.GONE

        //门票类型 1免费 2付费
        if (order.ticketType == 1) {
            binding.state.text = "已付款"
            binding.playTime.text = "付款时间：" + order.createTime
            order.payFinishTime?.let {
                binding.playTime.text = "付款时间：" + order.payFinishTime
            }
            binding.stateIv.visibility = View.VISIBLE

        } else {
            var model =
                JSON.parseObject(kv.getString("TypeModel", ""), TypeModel::class.java)
            for (item in model.pay_status) {
                if (order.payStatus.equals(item.dictValue)) {
                    binding.state.text = item.dictLabel
                    if (item.dictValue.equals("03")){
                        binding.stateIv.visibility = View.VISIBLE
                    }
                }
            }
            //            if(order.payStatus.equals("2")){
//                binding.state.text = "已付款"
//            }else{
//
//            }
            binding.playTime.text = "付款时间："
            binding.playType.text = "支付方式："
            order.payFinishTime?.let {
                binding.playTime.text = "付款时间：" + order.payFinishTime
                binding.playType.visibility = View.VISIBLE
//                if(order.payType.equals("1")){
//                    binding.playType.text ="支付方式：微信"
//                }
                binding.playType.text = "支付方式：微信"

            }


        }
//        0初始状态 1待审核 2审核成功 3审核失败
        if (order.examineStatus == 0 || order.examineStatus == 1) {
            binding.btnll.visibility = View.VISIBLE
            binding.reviewTime.visibility = View.GONE
        } else {
            binding.reviewTime.visibility = View.VISIBLE
            binding.reviewTime.text = "审核时间：" + order.updateTime
            binding.btnll.visibility = View.GONE
        }



    }

    private fun examine() {

        mViewModel.isShowLoading.value = true
        OkGo.post<String>(PageRoutes.Api_examine)
            .tag(PageRoutes.Api_examine)
            .upJson(JSON.toJSONString(params))
            .headers("Authorization", kv.getString("token", ""))
            .execute(object : RequestCallback<String>() {

                override fun onSuccess(response: Response<String>?) {
                    super.onSuccess(response)
                    finish()
                }

                override fun onFinish() {
                    super.onFinish()

                }

                override fun onError(response: Response<String>?) {
                    super.onError(response)
                    mViewModel.isShowLoading.value = false
                }


            })
    }
//    private fun getData() {
//
//        mViewModel.isShowLoading.value = true
//        OkGo.get<OlderData>(PageRoutes.Api_order)
//            .tag(PageRoutes.Api_order)
//            .headers("Authorization", kv.getString("token", ""))
//            .execute(object : RequestCallback<OlderData>() {
//
//                override fun onMySuccess(data: OlderData?) {
//                    super.onMySuccess(data)
//                }
//
//                override fun onFinish() {
//                    super.onFinish()
//
//                }
//
//                override fun onError(response: Response<OlderData>?) {
//                    super.onError(response)
//                    mViewModel.isShowLoading.value = false
//                }
//
//
//            })
//    }
fun select() {



    MaterialDialog(this, BottomSheet(LayoutMode.WRAP_CONTENT)).show {
//            title(null,"请选择审核不通过原因")

        customView(	//自定义弹窗
            viewRes = R.layout.tc_ly,//自定义文件
            dialogWrapContent = true,	//让自定义宽度生效
            scrollable = true,			//让自定义宽高生效
            noVerticalPadding = true    //让自定义高度生效
        ).apply{
            findViewById<TextView>(R.id.btn1).setOnClickListener { dismiss() }
            findViewById<TextView>(R.id.btn2).setOnClickListener { examine() }
            findViewById<ImageView>(R.id.gb).setOnClickListener { dismiss() }
            findViewById<RecyclerView>(R.id.recyclerView).layoutManager = LinearLayoutManager(activity)
            var model = JSON.parseObject(kv.getString("TypeModel", ""), TypeModel::class.java)

            var adapter = SelectAdapter().apply {
                submitList(model.sys_examine_reason)
                setOnItemClickListener { _, _, position ->
                    for(item in items){
                        items[position].isMyselect = false
                    }
                    items[position].isMyselect = true
                    notifyDataSetChanged()
                    failRemark = items[position].dictLabel
                    params["failRemark"] = failRemark
                    params["examine"] = false

                }
            }

            findViewById<RecyclerView>(R.id.recyclerView).adapter = adapter
        }



    }
}
//    fun select() {
//        var model = JSON.parseObject(kv.getString("TypeModel", ""), TypeModel::class.java)
//        var list: MutableList<String> = ArrayList<String>()
//        for (item in model.sys_examine_reason) {
//            list.add(item.dictLabel)
//        }
//
//
//        MaterialDialog(this, BottomSheet(LayoutMode.WRAP_CONTENT)).show {
//            title(null,"请选择审核不通过原因")
//            listItems(items = list, selection = object : ItemListener {
//                override fun invoke(dialog: MaterialDialog, index: Int, text: CharSequence) {
//                    failRemark = list[index]
//                    params["failRemark"] = failRemark
//                    params["examine"] = false
//                    examine()
//                }
//            })
//
//        }
//    }

}