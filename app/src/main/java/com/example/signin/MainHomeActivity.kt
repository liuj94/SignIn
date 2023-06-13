package com.example.signin


import android.media.MediaPlayer
import android.util.Log
import androidx.fragment.app.Fragment
import com.alibaba.fastjson.JSON
import com.dylanc.longan.toast
import com.example.signin.PageRoutes.Companion.Api_appVersion
import com.example.signin.adapter.MainViewPagerAdapter
import com.example.signin.base.BaseBindingActivity
import com.example.signin.base.BaseViewModel
import com.example.signin.bean.CustomUpdateParser
import com.example.signin.bean.SocketData
import com.example.signin.databinding.ActivityMainBinding
import com.example.signin.fragment.HomeMainFragment
import com.example.signin.fragment.MyFragment
import com.example.signin.net.RequestCallback
import com.lzy.okgo.OkGo
import com.lzy.okgo.model.Response
import com.tencent.mmkv.MMKV
import com.xuexiang.xupdate.XUpdate
import getDataType
import java.net.URI


class MainHomeActivity : BaseBindingActivity<ActivityMainBinding, BaseViewModel>()
//    ,
//    KeyEventResolver.OnScanSuccessListener
{

    override fun getViewModel(): Class<BaseViewModel> = BaseViewModel::class.java
    var isMainHome = true

    override fun initData() {
        SpeechUtils.getInstance(this@MainHomeActivity)
        val uri: URI = URI.create("wss://meeting.nbqichen.com/websocket/user?source=sys&Authorization="+kv.getString("token", ""))
        val client: JWebSocketClient = object : JWebSocketClient(uri) {
            override fun onMessage(message: String) {

                try {
                    var data =  JSON.parseObject(message, SocketData::class.java)
                    if(data.code.equals("200")){
                        if(data.type.equals("refresh")||data.type.equals("delete_location")||data.type.equals("add_location")){
                            LiveDataBus.get().with("JWebSocketClientlocation").postValue("1")
                        }

                    }
                }catch (e:Exception){

                }



            }
        }
        try {
            client.connectBlocking()
        } catch (e: InterruptedException) {
            e.printStackTrace()
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
                                                                getDataType("user_type") {
//                                                                    getDataType("signSiteType") {}
                                                                    getDataType("sys_fapiao") {}
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
        }

//        LiveDataBus.get().with("voiceStatus", String::class.java)
//            .observeForever {
//
//               if(!isMainHome){
//
//                   if(SpeechUtils.getInstance(this@MainHomeActivity).isSpeech){
//                       SpeechUtils.getInstance(this@MainHomeActivity).speakText(it);
//                   }else{
//                       var mRingPlayer: MediaPlayer? = null
//                       if (it.contains("成功")) {
//                           mRingPlayer = MediaPlayer.create(this@MainHomeActivity, R.raw.cg);
//                           mRingPlayer?.start();
//                       } else  if (it.contains("重复")){
//                           mRingPlayer = MediaPlayer.create(this@MainHomeActivity, R.raw.cf);
//                           mRingPlayer?.start();
//                       }else {
//                           mRingPlayer = MediaPlayer.create(this@MainHomeActivity, R.raw.qdsb);
//                           mRingPlayer?.start();
//                       }
//                   }
//
//
//               }
//
//
//            }
        LiveDataBus.get().with("voiceTime", String::class.java)
            .observeForever {
                setState(it)

            }

    }


//    override fun onInitScan(isSuccess: Boolean) {
//        val str = if (isSuccess) "初始化成功" else "初始化失败"
////        Toast.makeText(this, str, Toast.LENGTH_SHORT).show()
//    }

    override fun onResume() {
        super.onResume()
        isMainHome = true
        XUpdate.newBuild(this)
            .updateParser( CustomUpdateParser(this))
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










}