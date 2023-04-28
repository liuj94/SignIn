package com.example.signin

import android.graphics.Color
import com.alibaba.fastjson.JSON
import com.bigkoo.pickerview.builder.TimePickerBuilder
import com.dylanc.longan.startActivity
import com.example.signin.base.BaseBindingActivity
import com.example.signin.base.BaseViewModel
import com.example.signin.bean.XCData
import com.example.signin.databinding.ActEditXcBinding
import com.example.signin.net.RequestCallback
import com.lzy.okgo.OkGo
import com.lzy.okgo.model.Response
import java.util.*


class XCActivity  : BaseBindingActivity<ActEditXcBinding, BaseViewModel>() {
    override fun getViewModel(): Class<BaseViewModel> = BaseViewModel::class.java
    var id = ""
    var type = 0
    override fun initData() {
        intent.getStringExtra("id")?.let { id = it }
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
        val pvTime = TimePickerBuilder(
            this@XCActivity
        ) { date, v ->

        }.build()
        getData()
        showTime()
    }

    private fun showTime() {
        val selectedDate: Calendar = Calendar.getInstance()
        val startDate: Calendar = Calendar.getInstance()
        //startDate.set(2013,1,1);
        //startDate.set(2013,1,1);
        val endDate: Calendar = Calendar.getInstance()
        //endDate.set(2020,1,1);

        //正确设置方式 原因：注意事项有说明
        //endDate.set(2020,1,1);

        //正确设置方式 原因：注意事项有说明
        startDate.set(2013, 0, 1)
        endDate.set(2020, 11, 31)

      var  pvTime = TimePickerBuilder(this) { date, v -> //选中事件回调
//            tvTime.setText(jdk.nashorn.internal.objects.NativeDate.getTime(date))
        }.apply {
            setType(booleanArrayOf(true, true, true, true, true, true)) // 默认全部显示
            setCancelText("Cancel") //取消按钮文字
            setSubmitText("Sure") //确认按钮文字
            setContentTextSize(18) //滚轮文字大小
            setTitleSize(20) //标题文字大小
            setTitleText("Title") //标题文字
            setOutSideCancelable(false) //点击屏幕，点在控件外部范围时，是否取消显示
            isCyclic(true) //是否循环滚动
            setTitleColor(Color.BLACK) //标题文字颜色
            setSubmitColor(Color.BLUE) //确定按钮文字颜色
            setCancelColor(Color.BLUE) //取消按钮文字颜色
            setTitleBgColor(-0x99999a) //标题背景颜色 Night mode
            setBgColor(-0xcccccd) //滚轮背景颜色 Night mode
            setDate(selectedDate) // 如果不设置的话，默认是系统时间*/
            setRangDate(startDate, endDate) //起始终止年月日设定
            setLabel("年", "月", "日", "时", "分", "秒") //默认设置为年月日时分秒
            isCenterLabel(false) //是否只显示中间选中项的label文字，false则每项item全部都带有label。
            isDialog(true) //是否显示为对话框样式
            build()
        }

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

    fun putTrip( type: Int){

        OkGo.put<String>(PageRoutes.Api_trip)
            .tag(PageRoutes.Api_trip)
            .upJson(JSON.toJSONString(xcData))
            .headers("Authorization",kv.getString("token",""))
            .execute(object : RequestCallback<String>() {

                override fun onSuccess(response: Response<String>?) {
                    super.onSuccess(response)
                    startActivity<MainHomeActivity>()
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