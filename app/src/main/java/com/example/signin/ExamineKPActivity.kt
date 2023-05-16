package com.example.signin

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Color
import android.media.MediaPlayer
import android.os.AsyncTask
import android.text.Html
import android.view.KeyEvent
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import cn.bingoogolapple.qrcode.core.BGAQRCodeUtil
import cn.bingoogolapple.qrcode.zxing.QRCodeEncoder
import com.afollestad.materialdialogs.LayoutMode
import com.afollestad.materialdialogs.MaterialDialog
import com.afollestad.materialdialogs.bottomsheets.BottomSheet
import com.afollestad.materialdialogs.customview.customView
import com.alibaba.fastjson.JSON
import com.bumptech.glide.Glide
import com.common.apiutil.decode.DecodeReader
import com.common.face.api.FaceUtil
import com.dylanc.longan.activity
import com.dylanc.longan.startActivity
import com.dylanc.longan.toast
import com.example.signin.adapter.SelectAdapter
import com.example.signin.base.BaseBindingActivity
import com.example.signin.base.BaseViewModel
import com.example.signin.base.StatusBarUtil
import com.example.signin.bean.MeetingUserDeData
import com.example.signin.bean.TypeData
import com.example.signin.databinding.ActKpBinding
import com.example.signin.net.RequestCallback
import com.hello.scan.ScanCallBack
import com.hello.scan.ScanTool
import com.hjq.permissions.OnPermissionCallback
import com.hjq.permissions.Permission
import com.hjq.permissions.XXPermissions
import com.lzy.okgo.OkGo
import com.lzy.okgo.model.Response
import java.io.File
import java.io.UnsupportedEncodingException
import java.nio.charset.StandardCharsets


class ExamineKPActivity : BaseBindingActivity<ActKpBinding, BaseViewModel>(), ScanCallBack {
    override fun initTranslucentStatus() {
        StatusBarUtil.setTranslucentStatus(this, Color.TRANSPARENT)
        //设置状态栏字体颜色
        StatusBarUtil.setAndroidNativeLightStatusBar(this, true)
    }

    override fun getViewModel(): Class<BaseViewModel> = BaseViewModel::class.java
    var order: MeetingUserDeData.UserOrderBean = MeetingUserDeData.UserOrderBean()

    var params = HashMap<String, Any>()
    var moshi: String? = ""
    override fun initData() {
        moshi = kv.getString("shaomamoshi", "")
        if (moshi.equals("激光头识别")) {
            openHardreader()
        } else if (moshi.equals("二维码识别")) {
            ScanTool.GET.initSerial(
                this@ExamineKPActivity,
                "/dev/ttyACM0",
                115200,
                this@ExamineKPActivity
            )
            ScanTool.GET.playSound(true)
        }

        intent.getSerializableExtra("order")?.let { order = it as MeetingUserDeData.UserOrderBean }
        binding.name.text = ""
        binding.userName.text = ""
        binding.companyName.text = ""
        order.corporateName?.let { binding.companyName.text = it }
        order.meetingSignUpLocationName?.let { binding.name.text = it }
        order.userName?.let { binding.userName.text = it }
        var p = "开票金额<font color='#D43030'>" + order.amount + "</font>元"
        binding.amount.text = Html.fromHtml(p)
        //增值税专用发票、普通销售发票{{pagedata.userOrder.invoiceType==1?'增值税普通发票':'增值税专用发票'}}
        if (order.invoiceType.equals("1")) {
            binding.invoiceType.text = "增值税普通发票"
        } else {
            binding.invoiceType.text = "增值税专用发票"
        }
//        var model =
//            JSON.parseObject(kv.getString("TypeModel", ""), TypeModel::class.java)
//        for (item in model.sys_invoice_type) {
//            if (order.invoiceType.equals(item.dictValue.trim())) {
//                binding.invoiceType.text =
//                    item.dictLabel
//            }
//        }
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
            if (moshi.equals("识别模式") || moshi.isNullOrEmpty()) {
                toast("请选择识别模式")
                select()
                return@setOnClickListener
            }
            if (!moshi.equals("摄像头识别")) {
                return@setOnClickListener
            }
            activity?.let {

                XXPermissions.with(activity)
                    .permission(Permission.CAMERA)
                    .request(object : OnPermissionCallback {

                        override fun onGranted(permissions: MutableList<String>, all: Boolean) {
                            if (!all) {
                                toast("获取权限失败")
                            } else {
                                var intent = Intent(it, ScanKPActivity::class.java)
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
                    data?.let {

//                        createChineseQRCode(it.qr)
                        Glide.with(this@ExamineKPActivity)
                            .load("https://meeting.nbqichen.com/prod-api/common/qr/created?content=" + it.qr + "&h=210&w=210")
                            .error(R.mipmap.qrcodeopy)
                            .into(binding.stateIv)
                    }

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
        params["code"] = binding.invoiceNumber.text.toString().trim()
        params["no"] = binding.invoiceNo.text.toString().trim()
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
                binding.invoiceNumber.setText(it)
            }
            data?.getStringExtra("hm")?.let {
                binding.invoiceNo.setText(it)
            }

        }
    }

    fun select() {
        val list: MutableList<TypeData> = ArrayList<TypeData>()
        var a = TypeData()
        a.dictLabel = "二维码识别"
        list.add(a)
        var a1 = TypeData()
        a1.dictLabel = "摄像头识别"
        list.add(a1)
        var a2 = TypeData()
        a2.dictLabel = "激光头识别"
        list.add(a2)


        MaterialDialog(this, BottomSheet(LayoutMode.WRAP_CONTENT)).show {
//            title(null,"请选择审核不通过原因")

            customView(    //自定义弹窗
                viewRes = R.layout.tc_ly,//自定义文件
                dialogWrapContent = true,    //让自定义宽度生效
                scrollable = true,            //让自定义宽高生效
                noVerticalPadding = true    //让自定义高度生效
            ).apply {
                findViewById<TextView>(R.id.title).setText("请选择模式")
                findViewById<TextView>(R.id.btn1).setOnClickListener { dismiss() }
                findViewById<TextView>(R.id.btn2).setOnClickListener {
                    dismiss()
                    kv.putString("shaomamoshi", moshi)
                    if (moshi.equals("激光头识别")) {
                        openHardreader()
                    } else if (moshi.equals("二维码识别")) {
                        ScanTool.GET.initSerial(
                            this@ExamineKPActivity,
                            "/dev/ttyACM0",
                            115200,
                            this@ExamineKPActivity
                        )
                        ScanTool.GET.playSound(true)
                    }
                }
                findViewById<ImageView>(R.id.gb).setOnClickListener { dismiss() }
                findViewById<RecyclerView>(R.id.recyclerView).layoutManager =
                    LinearLayoutManager(activity)

                var adapter = SelectAdapter().apply {
                    submitList(list)
                    setOnItemClickListener { _, _, position ->
                        for (item in list) {
                            list[position].isMyselect = false
                        }
                        list[position].isMyselect = true
                        moshi = list[position].dictLabel
                        notifyDataSetChanged()

                    }
                }

                findViewById<RecyclerView>(R.id.recyclerView).adapter = adapter
            }


        }
    }

    private var mDecodeReader: DecodeReader? = null
    override fun dispatchKeyEvent(event: KeyEvent): Boolean {
        //要是重虚拟键盘输入怎不拦截
        if ("Virtual" == event.device.name) {
            return super.dispatchKeyEvent(event)
        }
        if (moshi.equals("激光头识别")) {
            mDecodeReader?.open(115200)
        }
        return true
    }

    private fun openHardreader() {
        if (mDecodeReader == null) {
            mDecodeReader = DecodeReader(this) //初始化
        }

        mDecodeReader?.setDecodeReaderListener { data ->
            if (isPause) {
                return@setDecodeReaderListener
            }
            var mRingPlayer =
                MediaPlayer.create(this@ExamineKPActivity, R.raw.ddd)
            mRingPlayer?.start()
            mDecodeReader?.close()
            try {
                val str = String(data, StandardCharsets.UTF_8)
                runOnUiThread {
                    try {
                        var temp = str.split(",")
                        var dm = temp[2]
                        var hm = temp[3]
                        binding.invoiceNumber.setText(dm)
                        binding.invoiceNo.setText(hm)
                    } catch (e: java.lang.Exception) {
                        toast("请提供正确发票码")
                    }


                }
            } catch (e: UnsupportedEncodingException) {
                e.printStackTrace()
            }

        }


    }

    var isPause = true
    override fun onResume() {
        super.onResume()
        isPause = false
    }

    override fun onPause() {
        super.onPause()
        isPause = true
    }

    override fun onScanCallBack(p0: String?) {
        if (isPause) {
            return
        }
        try {
            p0?.let {
                var temp = p0.split(",")
                var dm = temp[2]
                var hm = temp[3]
                binding.invoiceNumber.setText(dm)
                binding.invoiceNo.setText(hm)
                openBlue()
            }

        } catch (e: java.lang.Exception) {
            toast("请提供正确发票码")
            openRed()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        mDecodeReader?.close()
        ScanTool.GET.release()
        closeBlue()
        closeRed()
    }

    fun openRed() {
        val deviceon = File("/sys/class/leds/led-red")
        if (deviceon.canRead()) {
            FaceUtil.LedSet("led-red", 1);
        }
    }

    fun closeRed() {
        val deviceon = File("/sys/class/leds/led-red")
        if (deviceon.canRead()) {
            FaceUtil.LedSet("led-red", 0);
        }
    }

    fun openBlue() {
        val deviceon = File("/sys/class/leds/led-blue")
        if (deviceon.canRead()) {
            FaceUtil.LedSet("led-blue", 1);
        }
    }

    fun closeBlue() {
        val deviceon = File("/sys/class/leds/led-blue")
        if (deviceon.canRead()) {
            FaceUtil.LedSet("led-blue", 0);
        }
    }

}