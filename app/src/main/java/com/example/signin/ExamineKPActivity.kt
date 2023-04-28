package com.example.signin

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Color
import android.os.AsyncTask
import android.text.Html
import android.widget.Toast
import cn.bingoogolapple.qrcode.core.BGAQRCodeUtil
import cn.bingoogolapple.qrcode.zxing.QRCodeEncoder
import com.alibaba.fastjson.JSON
import com.dylanc.longan.activity
import com.dylanc.longan.startActivity
import com.dylanc.longan.toast
import com.example.signin.base.BaseBindingActivity
import com.example.signin.base.BaseViewModel
import com.example.signin.base.StatusBarUtil
import com.example.signin.bean.MeetingUserDeData
import com.example.signin.bean.TypeModel
import com.example.signin.databinding.ActKpBinding
import com.example.signin.net.RequestCallback
import com.hjq.permissions.OnPermissionCallback
import com.hjq.permissions.Permission
import com.hjq.permissions.XXPermissions
import com.lzy.okgo.OkGo
import com.lzy.okgo.model.Response
import kotlin.collections.HashMap


class ExamineKPActivity : BaseBindingActivity<ActKpBinding, BaseViewModel>() {
    override fun initTranslucentStatus() {
        StatusBarUtil.setTranslucentStatus(this, Color.TRANSPARENT)
        //设置状态栏字体颜色
        StatusBarUtil.setAndroidNativeLightStatusBar(this, true)
    }

    override fun getViewModel(): Class<BaseViewModel> = BaseViewModel::class.java
    var order: MeetingUserDeData.UserOrderBean = MeetingUserDeData.UserOrderBean()

    var params = HashMap<String, Any>()
    override fun initData() {

        intent.getSerializableExtra("order")?.let { order = it as MeetingUserDeData.UserOrderBean }
        binding.name.text = ""
        binding.userName.text = ""
        binding.companyName.text = ""
        order.corporateName?.let { binding.companyName.text = it }
        order.meetingSignUpLocationName?.let { binding.name.text = it }
        order.userName?.let { binding.userName.text = it }
        var p = "开票金额<font color='#D43030'>" + order.amount + "</font>元"
        binding.amount.text = Html.fromHtml(p)
        //增值税专用发票、普通销售发票
        if (order.invoiceType.equals("1")) {
            binding.invoiceType.text = "普通销售发票"
        } else {
            binding.invoiceType.text = "增值税专用发票"
        }
        var model =
            JSON.parseObject(kv.getString("TypeModel", ""), TypeModel::class.java)
        for (item in model.sys_invoice_type) {
            if (order.invoiceType.equals(item.dictValue)) {
                binding.invoiceType.text =
                    item.dictLabel
            }
        }
        /**
         * id: data.id
        name: escape(data.userMeeting.name)
        corporateName: escape(data.sponsor"
        supplement: escape(data.supplement)
        meetingId: data.meetingId
        nowTime: new Date().getTime()
         */
//        var qRCodeParams = HashMap<String, Any>()
//        order.userId?.let { qRCodeParams["id"] = it }
//        order.meetingName?.let { qRCodeParams["name"] = it }
//        order.corporateName?.let { qRCodeParams["corporateName"] = it }
//        order.supplement?.let { qRCodeParams["supplement"] = it }
//        order.meetingId?.let { qRCodeParams["meetingId"] = it }
//        qRCodeParams["nowTime"] = Date().time
//        createChineseQRCode(JSON.toJSONString(qRCodeParams))
        binding.btn.setOnClickListener { startActivity<ExamineActivity>("order" to order) }
        binding.submit.setOnClickListener {
            //        binding.invoiceNumber
//        binding.invoiceNo
            examine()
        }
        getBill()
        //起始符+版本号+base64（名称</>纳税人识别号</>地址电话</>开户行及账号</>CRC）+结束符
//        var content =
//        "$01"+""+CRC16Util.calculateCRC16()+"$"
//        044031801104（发票代码）,
//        40021227（发票号码）,
//        String s3 = "Real-How-To";
//        var temp = s3.split(",");
        binding.sm.setOnClickListener {
            activity?.let {

                XXPermissions.with(activity)
                    .permission(Permission.CAMERA)
                    .request(object : OnPermissionCallback {

                        override fun onGranted(permissions: MutableList<String>, all: Boolean) {
                            if (!all) {
                                toast("获取权限失败")
                            } else {
                                var intent = Intent(it, ScanActivity::class.java)
                                startActivityForResult(intent, 1111)
                            }

                        }

                        override fun onDenied(permissions: MutableList<String>, never: Boolean) {
                            toast("获取权限失败")

                        }
                    })

            }
        }

    }

    fun getBill() {
        OkGo.get<MeetingUserDeData.UserOrderBean>(PageRoutes.Api_bill + order.id)
            .tag(PageRoutes.Api_bill)
            .headers("Authorization", kv.getString("token", ""))
            .execute(object : RequestCallback<MeetingUserDeData.UserOrderBean>() {

                override fun onSuccess(response: Response<MeetingUserDeData.UserOrderBean>?) {
                    super.onSuccess(response)

                }

                override fun onMySuccess(data: MeetingUserDeData.UserOrderBean?) {
                    super.onMySuccess(data)
                    data?.let { createChineseQRCode(it.qr) }

                }

                override fun onError(response: Response<MeetingUserDeData.UserOrderBean>) {
                    super.onError(response)

                    mViewModel.isShowLoading.value = false
                }

                override fun onFinish() {
                    super.onFinish()

                }


            })
    }

    private fun examine() {
        var params = HashMap<String, Any>()
        params["id"] = order.id
        mViewModel.isShowLoading.value = true
        OkGo.post<String>(PageRoutes.Api_billfinish)
            .tag(PageRoutes.Api_billfinish)
            .upJson(JSON.toJSONString(params))
            .headers("Authorization", kv.getString("token", ""))
            .execute(object : RequestCallback<String>() {

                override fun onSuccess(response: Response<String>?) {
                    super.onSuccess(response)
                    finish()
                }

                override fun onFinish() {
                    super.onFinish()
                    mViewModel.isShowLoading.value = false
                }

                override fun onError(response: Response<String>?) {
                    super.onError(response)
                }


            })
    }

    private fun createChineseQRCode(content: String) {

        object : AsyncTask<Void?, Void?, Bitmap?>() {

            override fun onPostExecute(bitmap: Bitmap?) {
                if (bitmap != null) {
                    binding.stateIv.setImageBitmap(bitmap)
                } else {
                    Toast.makeText(this@ExamineKPActivity, "生成二维码失败", Toast.LENGTH_SHORT).show()
                }
            }

            override fun doInBackground(vararg params: Void?): Bitmap? {
                return QRCodeEncoder.syncEncodeQRCode(
                    content,
                    BGAQRCodeUtil.dp2px(this@ExamineKPActivity, 150f)
                )
            }
        }.execute()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 1111) {
            data?.getStringExtra("dm").let {
                binding.invoiceNumber.text = it
            }
            data?.getStringExtra("hm")?.let {
                binding.invoiceNo.text = it
            }

        }
    }

}