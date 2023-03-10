package com.example.signin


import android.media.MediaPlayer
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
import com.lzy.okgo.OkGo
import com.lzy.okgo.model.Response
import com.tencent.mmkv.MMKV


import getDataType


class MainHomeActivity : BaseBindingActivity<ActivityMainBinding, BaseViewModel>() {


    override fun getViewModel(): Class<BaseViewModel> = BaseViewModel::class.java
var  mRingPlayer :MediaPlayer? = null

    override fun initData() {

        getFragmentLists()

        getDataType("sys_zhuce"){
            getDataType("sys_ruzhu"){
                getDataType("sys_huichang"){
                    getDataType("sys_laicheng"){
                        getDataType("sys_liping"){
                            getDataType("sys_fancheng"){
                                getDataType("sys_canyin"){
                                    getDataType("user_meeting_sign_up_status"){
                                        getDataType("user_meeting_type"){
                                            getDataType("sys_invoice_status"){
                                                getDataType("sys_invoice_type"){
                                                    getDataType("transport_type"){
                                                        getDataType("sys_examine_reason"){
                                                            getDataType("pay_status"){
                                                                getDataType("user_type"){}
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
                if (mRingPlayer != null){
                    mRingPlayer?.stop();
                    mRingPlayer?.release();
                    mRingPlayer = null;
                }
             //????????????
                if(it.equals("1")){

                    mRingPlayer = MediaPlayer.create(this, R.raw.cg);
                    mRingPlayer?.start();

                }else{
                    mRingPlayer = MediaPlayer.create(this, R.raw.cf);
                    mRingPlayer?.start();
                }

            }
        LiveDataBus.get().with("voiceTime", String::class.java)
            .observeForever {
                setState(it)

            }

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
            toast("??????????????????????????????")
            exitTime = System.currentTimeMillis()
        } else {
            System.exit(0);
        }
    }


    fun setState(params:String){


        JSON.toJSONString(params)
        OkGo.post<String>(PageRoutes.Api_voice)
            .tag(PageRoutes.Api_voice)
            .upJson(params)
            .headers("Authorization", MMKV.mmkvWithID("MyDataMMKV")
                .getString("token",""))
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

    }
}