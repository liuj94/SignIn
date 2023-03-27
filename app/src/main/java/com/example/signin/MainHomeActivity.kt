package com.example.signin


import android.os.Build
import android.text.TextUtils
import android.util.Log
import androidx.fragment.app.Fragment
import com.alibaba.fastjson.JSON
import com.dylanc.longan.toast
import com.example.signin.adapter.MainViewPagerAdapter
import com.example.signin.base.BaseBindingActivity
import com.example.signin.base.BaseViewModel
import com.example.signin.databinding.ActivityMainBinding
import com.example.signin.fragment.HomeMainFragment
import com.example.signin.fragment.MyFragment
import com.example.signin.net.RequestCallback
import com.hello.scan.ScanCallBack
import com.hello.scan.ScanTool
import com.lzy.okgo.OkGo
import com.lzy.okgo.model.Response
import com.tencent.mmkv.MMKV
import getDataType
import kotlin.collections.ArrayList
import kotlin.collections.HashMap


class MainHomeActivity : BaseBindingActivity<ActivityMainBinding, BaseViewModel>(), ScanCallBack {
    private var sDevices: MutableMap<String, Pair<String, Int>>? = null


    private var scanCount = 0

    override fun getViewModel(): Class<BaseViewModel> = BaseViewModel::class.java
    var isMainHome = true
    override fun initData() {
        SpeechUtils.getInstance(this@MainHomeActivity)
        sDevices = HashMap<String, Pair<String, Int>>()
        sDevices!!.put("TPS508", Pair<String, Int>("/dev/ttyACM0", 115200))
        sDevices!!.put("TPS360", Pair<String, Int>("/dev/ttyACM0", 115200))
        sDevices!!.put("TPS537", Pair<String, Int>("/dev/ttyACM0", 115200))
        sDevices!!.put("D2", Pair<String, Int>("/dev/ttyHSL0", 9600)) // D2串口模式
        sDevices!!.put("D2",  Pair<String, Int>("/dev/ttyACM0", 115200)); // D2U转串模式
        sDevices!!.put("TPS980", Pair<String, Int>("/dev/ttyS0", 115200))
        sDevices!!.put("TPS980P", Pair<String, Int>("/dev/ttyS0", 115200))
        sDevices!!.put("TPS980P",  Pair<String, Int>("/dev/ttyS0", 9600));
        sDevices!!.put("TPS530", Pair<String, Int>("/dev/ttyUSB0", 115200))
        if (!initScanTool()) {
//            Toast.makeText(this, "该机型还没有适配", Toast.LENGTH_SHORT).show();
        } else {
            ScanTool.GET.playSound(true);
        }

        getFragmentLists()

        getDataType("sys_zhuce") {
            getDataType("sys_ruzhu") {
                getDataType("sys_huichang") {
                    getDataType("sys_laicheng") {
                        getDataType("sys_liping") {
                            getDataType("sys_fancheng") {
                                getDataType("sys_canyin") {
                                    getDataType("user_meeting_sign_up_status") {
                                        getDataType("user_meeting_type") {
                                            getDataType("sys_invoice_status") {
                                                getDataType("sys_invoice_type") {
                                                    getDataType("transport_type") {
                                                        getDataType("sys_examine_reason") {
                                                            getDataType("pay_status") {
                                                                getDataType("user_type") {}
                                                            }

                                                        }


                                                    }

                                                }

                                            }

                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }

        LiveDataBus.get().with("voiceStatus", String::class.java)
            .observeForever {
                SpeechUtils.getInstance(this@MainHomeActivity).speakText(it);

            }
        LiveDataBus.get().with("voiceTime", String::class.java)
            .observeForever {
                setState(it)

            }

    }

    /**
     * 判断使用模式
     *
     * @return 返回true表示表示该机型已经适配，false表示该机型还没有适配
     */
    private fun initScanTool(): Boolean {
        for (s in sDevices!!.keys) {
            if (s == Build.MODEL) {
                val (first, second) = sDevices!![s] ?: continue
                Log.e("Hello", "judgeModel == > $s")
                Log.e("Hello", "path == > $first")
                Log.e("Hello", "baud rate == > $second")
                ScanTool.GET.initSerial(this, first, second, this@MainHomeActivity)
                return true
            }
        }
        return false
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
        binding.mViewPager.offscreenPageLimit = 4
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

    override fun onScanCallBack(data: String?) {
        try {
            if (AppManager.getAppManager().activityClassIsLive(ScanActivity::class.java)) {
                if (null != AppManager.getAppManager().findActivity(ScanActivity::class.java)) {
                    if (AppManager.getAppManager().getTopActivity()==ScanActivity::class.java) {
                        if (TextUtils.isEmpty(data)) return
                        Log.e("Hello", "回调数据 == > $data")
                        LiveDataBus.get().with("onScanCallBack").postValue(JSON.toJSONString(data))
                    }

                }
            }
        }catch (e:Exception){}


    }

    override fun onInitScan(isSuccess: Boolean) {
//        val str = if (isSuccess) "初始化成功" else "初始化失败"
//        Toast.makeText(this, str, Toast.LENGTH_SHORT).show()
    }

    override fun onDestroy() {
        try {
        ScanTool.GET.release(); }catch (e:Exception){}
        super.onDestroy()
    }

}