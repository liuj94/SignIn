package com.example.signin

import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.afollestad.materialdialogs.LayoutMode
import com.afollestad.materialdialogs.MaterialDialog
import com.afollestad.materialdialogs.bottomsheets.BottomSheet
import com.afollestad.materialdialogs.customview.customView
import com.alibaba.fastjson.JSON
import com.bigkoo.pickerview.builder.TimePickerBuilder
import com.dylanc.longan.activity
import com.example.signin.adapter.SelectAdapter
import com.example.signin.base.BaseBindingActivity
import com.example.signin.base.BaseViewModel
import com.example.signin.bean.TypeModel
import com.example.signin.bean.XCData
import com.example.signin.databinding.ActEditXcBinding
import com.example.signin.net.RequestCallback
import com.lzy.okgo.OkGo
import com.lzy.okgo.model.Response
import java.text.SimpleDateFormat
import java.util.*


class XCActivity  : BaseBindingActivity<ActEditXcBinding, BaseViewModel>() {
    override fun getViewModel(): Class<BaseViewModel> = BaseViewModel::class.java
    var id = ""
    var type = 0
    var model:TypeModel? = null
    override fun initData() {
        intent.getStringExtra("userMeetingId")?.let { id = it }
        intent.getIntExtra("type", 0)?.let { type = it }
        //1来程2返程
        if(type==1){
            binding.title.text ="修改来程信息"
            binding.endCity.isFocusable = false
            binding.endCity.isFocusableInTouchMode = false
        }else{
            binding.title.text ="修改返程信息"
            binding.startCity.isFocusable = false
            binding.startCity.isFocusableInTouchMode = false
        }
         model = JSON.parseObject(kv.getString("TypeModel", ""), TypeModel::class.java)

//        val pvTime = TimePickerBuilder(
//            this@XCActivity
//        ) { date, v ->
//
//        }.build()
        getData()
        binding.transportLL.setOnClickListener {
            select()
        }
        binding.startDatell.setOnClickListener {
            showData{
                binding.startDate.setText(toData(it))
            }
        }
        binding.startTimell.setOnClickListener {
            showTime{
                binding.startTime.setText(toTime(it))
            }
        }
        binding.endDatell.setOnClickListener {
            showData{
                binding.endDate.setText(toData(it))
            }
        }
        binding.endTimell.setOnClickListener {
            showTime {
                binding.endTime.setText(toTime(it))
            }
        }
        binding.btn.setOnClickListener {
            putTrip()
        }


    }
    fun toData(date:Date) :String{
        var sdf =  SimpleDateFormat("yyyy-MM-dd");
        return sdf.format(date)
    }
    fun toTime(date:Date) :String{
        var sdf =  SimpleDateFormat("HH:mm");
        return sdf.format(date)
    }
    fun select() {

        MaterialDialog(this, BottomSheet(LayoutMode.WRAP_CONTENT)).show {
//            title(null,"请选择审核不通过原因")

            customView(	//自定义弹窗
                viewRes = R.layout.tc_xz,//自定义文件
                dialogWrapContent = true,	//让自定义宽度生效
                scrollable = true,			//让自定义宽高生效
                noVerticalPadding = true    //让自定义高度生效
            ).apply{
                findViewById<TextView>(R.id.btn1).setOnClickListener { dismiss() }
                findViewById<TextView>(R.id.btn2).setOnClickListener { dismiss() }
                findViewById<ImageView>(R.id.gb).setOnClickListener { dismiss() }
                findViewById<RecyclerView>(R.id.recyclerView).layoutManager = LinearLayoutManager(activity)

                var adapter = SelectAdapter().apply {
                    submitList(model?.transport_type)
                    setOnItemClickListener { _, _, position ->
                        for(item in items){
                            items[position].isMyselect = false
                        }
                        items[position].isMyselect = true
                        notifyDataSetChanged()
                        binding.transport.text =  items[position].dictLabel
                        xcData?.transport =  items[position].dictValue
                        dismiss()

                    }
                }

                findViewById<RecyclerView>(R.id.recyclerView).adapter = adapter
            }



        }
    }
    private fun showTime(back: (data: Date) -> Unit) {
        val selectedDate: Calendar = Calendar.getInstance()
      var  pvTime = TimePickerBuilder(this) { date, v -> //选中事件回调
          back.invoke(date)
        }.apply {
            setType(booleanArrayOf(false, false, false, true, true, false)) // 默认全部显示
            setCancelText("取消") //取消按钮文字
            setSubmitText("确认") //确认按钮文字
            setContentTextSize(18) //滚轮文字大小
            setTitleSize(20) //标题文字大小
            setTitleText("时间选择") //标题文字
            setOutSideCancelable(false) //点击屏幕，点在控件外部范围时，是否取消显示
            isCyclic(true) //是否循环滚动
//            setTitleColor(Color.BLACK) //标题文字颜色
//            setSubmitColor(Color.BLUE) //确定按钮文字颜色
//            setCancelColor(Color.BLUE) //取消按钮文字颜色
//            setTitleBgColor(-0x99999a) //标题背景颜色 Night mode
//            setBgColor(-0xcccccd) //滚轮背景颜色 Night mode
            setDate(selectedDate) // 如果不设置的话，默认是系统时间*/
//            setRangDate(startDate, endDate) //起始终止年月日设定
            setLabel("年", "月", "日", "时", "分", "秒") //默认设置为年月日时分秒
            isCenterLabel(false) //是否只显示中间选中项的label文字，false则每项item全部都带有label。
            isDialog(false) //是否显示为对话框样式
        }
        pvTime.build() .show()
    }
    private fun showData(back: (data: Date) -> Unit) {
        val selectedDate: Calendar = Calendar.getInstance()
      var  pvTime  = TimePickerBuilder(this) { date, v -> //选中事件回调
//            tvTime.setText(jdk.nashorn.internal.objects.NativeDate.getTime(date))
          back.invoke(date)
        }.apply {
            setType(booleanArrayOf(true, true, true, false, false, false)) // 默认全部显示
            setCancelText("取消") //取消按钮文字
            setSubmitText("确认") //确认按钮文字
            setContentTextSize(18) //滚轮文字大小
            setTitleSize(20) //标题文字大小
            setTitleText("时间选择") //标题文字
            setOutSideCancelable(false) //点击屏幕，点在控件外部范围时，是否取消显示
            isCyclic(true) //是否循环滚动
//            setTitleColor(Color.BLACK) //标题文字颜色
//            setSubmitColor(Color.BLUE) //确定按钮文字颜色
//            setCancelColor(Color.BLUE) //取消按钮文字颜色
//            setTitleBgColor(-0x99999a) //标题背景颜色 Night mode
//            setBgColor(-0xcccccd) //滚轮背景颜色 Night mode
            setDate(selectedDate) // 如果不设置的话，默认是系统时间*/
//            setRangDate(startDate, endDate) //起始终止年月日设定
            setLabel("年", "月", "日", "时", "分", "秒") //默认设置为年月日时分秒
            isCenterLabel(false) //是否只显示中间选中项的label文字，false则每项item全部都带有label。
            isDialog(false) //是否显示为对话框样式
        }
        pvTime.build() .show()
    }

    var xcData :XCData? = null
    private fun getData() {
        mViewModel.isShowLoading.value = true
        OkGo.get<XCData>(PageRoutes.Api_get_trip + id + "?type=" + type+"&userMeetingId="+id)
            .tag(PageRoutes.Api_get_trip)
            .headers("Authorization", kv.getString("token", ""))
            .execute(object : RequestCallback<XCData>() {
                override fun onSuccessNullData() {
                    super.onSuccessNullData()

                }

                override fun onMySuccess(data: XCData) {
                    super.onMySuccess(data)
                    xcData = data
                    binding.startCity.setText(data.startCity)
                    binding.endCity.setText(data.endCity)
                    model?.let {
                        for (item in it.transport_type) {
                            if (xcData!!.transport.equals(item.dictValue.trim())) {
                                binding.transport.text = item.dictLabel
                            }
                        }
                    }
                    binding.remark.setText(data.remark)

                    binding.startDate.text = data.startDate
                    binding.startTime.text = data.startTime
                    binding.endTime.text = data.endTime
                    binding.endDate.text = data.endDate
                    binding.startAddress.setText(data.startAddress)
                    binding.endAddress.setText(data.endAddress)
                }

                override fun onError(response: Response<XCData>) {
                    super.onError(response)

                }

                override fun onFinish() {
                    super.onFinish()
                    mViewModel.isShowLoading.value = false
                }


            })
    }

    fun putTrip(){
        xcData?.remark = binding.remark.text.toString()
        xcData?.endAddress = binding.endAddress.text.toString()
        xcData?.startAddress = binding.startAddress.text.toString()
        xcData?.endDate = binding.endDate.text.toString()
        xcData?.endTime = binding.endTime.text.toString()
        xcData?.startTime = binding.startTime.text.toString()
        xcData?.startDate = binding.startDate.text.toString()
        xcData?.endCity = binding.endCity.text.toString()
        xcData?.startCity = binding.startCity.text.toString()
        OkGo.put<String>(PageRoutes.Api_trip)
            .tag(PageRoutes.Api_trip)
            .upJson(JSON.toJSONString(xcData))
            .headers("Authorization",kv.getString("token",""))
            .execute(object : RequestCallback<String>() {

                override fun onSuccess(response: Response<String>?) {
                    super.onSuccess(response)
                    finish()
                }

                override fun onError(response: Response<String>) {
                    super.onError(response)

                    mViewModel.isShowLoading.value = false
                }

                override fun onFinish() {
                    super.onFinish()

                }


            })
    }
}