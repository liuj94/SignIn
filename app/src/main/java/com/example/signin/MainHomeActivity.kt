package com.example.signin


import android.graphics.Bitmap
import android.util.Log
import androidx.fragment.app.Fragment
import com.alibaba.fastjson.JSON
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.RequestOptions
import com.bumptech.glide.request.target.Target
import com.dylanc.longan.toast
import com.example.signin.PageRoutes.Companion.Api_appVersion
import com.example.signin.PageRoutes.Companion.BaseUrl
import com.example.signin.adapter.MainViewPagerAdapter
import com.example.signin.base.BaseBindingActivity
import com.example.signin.base.BaseViewModel
import com.example.signin.bean.CustomUpdateParser
import com.example.signin.bean.SiginData
import com.example.signin.bean.SocketData
import com.example.signin.databinding.ActivityMainBinding
import com.example.signin.fragment.HomeMainFragment
import com.example.signin.fragment.MyFragment
import com.example.signin.net.RequestCallback
import com.hjq.permissions.OnPermissionCallback
import com.hjq.permissions.Permission
import com.hjq.permissions.XXPermissions
import com.lzy.okgo.OkGo
import com.lzy.okgo.model.Response
import com.tencent.mmkv.MMKV
import com.xuexiang.xupdate.XUpdate
import getDataType
import java.net.URI


class MainHomeActivity : BaseBindingActivity<ActivityMainBinding, BaseViewModel>() {

    override fun getViewModel(): Class<BaseViewModel> = BaseViewModel::class.java
    var isMainHome = true
    var client: JWebSocketClient? = null
    var printUnit: PrintUnit? = null
    override fun initData() {
        SpeechUtils.getInstance(this@MainHomeActivity)
        val uri: URI = URI.create(
            "wss://meeting.nbqichen.com/websocket/user?source=sys&Authorization=" + kv.getString(
                "token",
                ""
            )
        )


        client = object : JWebSocketClient(uri) {
            override fun onMessage(message: String) {

                try {
                    var data = JSON.parseObject(message, SocketData::class.java)
                    if (data.code.equals("200")) {
                        if (data.type.equals("refresh") || data.type.equals("delete_location") || data.type.equals(
                                "add_location"
                            )
                        ) {
                            LiveDataBus.get().with("JWebSocketClientlocation").postValue("1")
                        } else if (data.type.equals("print")) {
                            kv.putString(message, "printData")
                            var printZd = kv.getBoolean("printZd", false)
                            if (printZd) {
                                printImg(data)
                            }
                        }

                    }
                } catch (e: Exception) {

                }


            }
        }
        try {
            client?.connectBlocking()
        } catch (e: InterruptedException) {
            e.printStackTrace()
        }
        getFragmentLists()

        getDataType("sys_zhuce") {
            getDataType("sys_ruzhu") {
                getDataType("sys_huichang") {

                }
            }
        }
        getDataType("sys_laicheng") {
            getDataType("sys_liping") {
                getDataType("sys_fancheng") {

                }
            }
        }
        getDataType("sys_canyin") {
            getDataType("user_meeting_sign_up_status") {
                getDataType("user_meeting_type") {


                }
            }
        }
        getDataType("sys_invoice_status") {
            getDataType("sys_invoice_type") {
                getDataType("transport_type") {


                }

            }

        }
        getDataType("sys_examine_reason") {
            getDataType("pay_status") {
                getDataType("user_type") {
//                                                                    getDataType("signSiteType") {}
                    getDataType("sys_fapiao") {}
                }

            }

        }

        LiveDataBus.get().with("voiceTime", String::class.java)
            .observeForever {
                setState(it)

            }
        LiveDataBus.get().with("JWebSocketClientlocationPrint", String::class.java)
            .observeForever {
                var message = kv.getString("printData", "")
                if (message.isNullOrEmpty()) {
                    try {
                        var data = JSON.parseObject(message, SocketData::class.java)
                        printImg(data)
                    } catch (e: Exception) {
                    }

                }


            }
        LiveDataBus.get().with("Printqiehuan", String::class.java)
            .observeForever {
                printUnit?.connectSPP(it)

            }
        XXPermissions.with(this@MainHomeActivity)
            .permission(Permission.BLUETOOTH_SCAN)
            .permission(Permission.BLUETOOTH_CONNECT)
            .permission(Permission.BLUETOOTH_ADVERTISE)
            .permission(Permission.ACCESS_COARSE_LOCATION)
            .request(object : OnPermissionCallback {

                override fun onGranted(permissions: MutableList<String>, all: Boolean) {
                    if (all) {

                        printUnit = PrintUnit(this@MainHomeActivity, object : PrintUnit.ListPrinter {
                                override fun printer(p: SiginData) {
                                    var selectedDevice = p.mac
//                                    p.split("\n\n".toRegex()).dropLastWhile { it.isEmpty() }
//                                        .toTypedArray().get(1)

                                    printUnit?.connectSPP(p.mac)
                                }

                                override fun conPrint(p: Boolean) {
                                    Log.d(
                                        "printUnitXXPermissions",
                                        "搜索 conPrint=" + p
                                    )
                                }

                            })
                        printUnit?.OnePrintRegisterReceiver()
                    } else {
                        toast("获取蓝牙权限失败")
                    }

                }

                override fun onDenied(permissions: MutableList<String>, never: Boolean) {


                }
            })
    }

    private fun printImg(data: SocketData) {
        val options = RequestOptions()
            .override(data.cardW.intValueExact(), data.cardH.intValueExact())
        for (url in data.urls) {
            Glide.with(this@MainHomeActivity).asBitmap()
                .load(BaseUrl + url)
                .apply(options)
                .listener(object : RequestListener<Bitmap> {
                    override fun onLoadFailed(
                        e: GlideException?,
                        model: Any?,
                        target: Target<Bitmap>?,
                        isFirstResource: Boolean
                    ): Boolean {

                        return false
                    }

                    override fun onResourceReady(
                        resource: Bitmap?,
                        model: Any?,
                        target: Target<Bitmap>?,
                        dataSource: DataSource?,
                        isFirstResource: Boolean
                    ): Boolean {
                        resource?.let { b ->
                            printUnit?.let {
                                if (it.isConPrint) {
                                    try {
                                        it.print(b)
                                    } catch (e: Exception) {
                                    }

                                } else {
                                    toast("打印机未连接")
                                }
                            }

                        }
                        return false
                    }
                }
                ).submit()

        }
    }


    override fun onResume() {
        super.onResume()
        isMainHome = true
        XUpdate.newBuild(this)
            .updateParser(CustomUpdateParser(this))
            .updateUrl(Api_appVersion)
            .update();

    }

    override fun onPause() {
        super.onPause()
        isMainHome = false
    }

    var homeFragment: HomeMainFragment? = null
    var myFragment: MyFragment? = null
    fun getFragmentLists() {
        val fragmentLists: MutableList<Fragment> = ArrayList<Fragment>()
        homeFragment = HomeMainFragment()
        fragmentLists.add(homeFragment!!)
        myFragment = MyFragment()
//        fragmentLists.add(JoinChannelAudio())
        fragmentLists.add(myFragment!!)
        initAdapter(fragmentLists)

    }


    private fun initAdapter(fragments: MutableList<Fragment>) {
        val mAdapter = MainViewPagerAdapter(supportFragmentManager, fragments)
        binding.mViewPager.adapter = mAdapter
        binding.mViewPager.offscreenPageLimit = 1
        initListener()

    }

    override fun initListener() {
        binding.mMainBottomRg.setOnCheckedChangeListener { _, checkedId ->
            when (checkedId) {
                R.id.mMainHomeRb -> {
                    binding.mViewPager?.currentItem = 0
                }

                R.id.mMainMineRb -> {
                    binding.mViewPager?.currentItem = 1
                }


            }
        }


    }


    private var exitTime: Long = 0
    override fun onBackPressed() {
        if (System.currentTimeMillis() - exitTime > 2000) {
            toast("再按一次返回退出程序")
            exitTime = System.currentTimeMillis()
        } else {
            System.exit(0);
        }
    }


    fun setState(params: String) {


        JSON.toJSONString(params)
        OkGo.post<String>(PageRoutes.Api_voice)
            .tag(PageRoutes.Api_voice)
            .upJson(params)
            .headers(
                "Authorization", MMKV.mmkvWithID("MyDataMMKV")
                    .getString("token", "")
            )
            .execute(object : RequestCallback<String>() {

                override fun onSuccess(response: Response<String>?) {
                    super.onSuccess(response)

                }

                override fun onError(response: Response<String>) {
                    super.onError(response)

                }

                override fun onFinish() {
                    super.onFinish()

                }


            })
    }


    override fun onDestroy() {
        super.onDestroy()
        try {
            client?.close()
        } catch (e: java.lang.Exception) {
            e.printStackTrace()
        }
        printUnit?.PrintUnregisterReceiver()
    }


}