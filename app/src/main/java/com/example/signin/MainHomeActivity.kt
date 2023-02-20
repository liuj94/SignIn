package com.example.signin.mvvm.ui.activity

import android.os.CountDownTimer
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import com.dylanc.longan.toast
import com.example.signin.R

import com.example.signin.base.BaseBindingActivity


import com.example.signin.constants.KeySet
import com.example.signin.PageRoutes
import com.example.signin.databinding.ActivityMainHomeBinding
import com.example.signin.helper.AppManager
import com.example.signin.helper.CustomActionUtils
import com.example.signin.helper.RefreshTokenUtils
import com.example.signin.helper.database.LocalDataUtils
import com.example.signin.helper.update.AppVersionBean
import com.example.signin.helper.web.CacheUtils
import com.example.signin.mvvm.ui.adapter.MainViewPagerAdapter
import com.example.signin.mvvm.ui.widget.UpdateDialog
import com.example.signin.mvvm.vm.MainHomeAVM
import com.example.signin.utils.PushHelper
import com.example.signin.setTvGradientColor


class MainHomeActivity : BaseBindingActivity<ActivityMainHomeBinding, MainHomeAVM>() {


    override fun getViewModel(): Class<MainHomeAVM> = MainHomeAVM::class.java

//    override fun initView(savedInstanceState: Bundle?) = R.layout.activity_main_home

   var menusUrlStr: String = PageRoutes.MENUS
    override fun initData() {
        CacheUtils.clearCache(this, null)
        PushHelper.init(applicationContext)
        val url: String? = intent.getStringExtra("url")
        url?.let { AppManager.getAppManager().startWeb(url, "") }
        if ("1".equals(LocalDataUtils.getValue(KeySet.GUESTMODE))) {
            menusUrlStr = PageRoutes.MENUSTOURISTS
        }
        mViewModel.requestIconList(menusUrlStr)
        mViewModel.getAdListDataLiveData().observe(this, Observer {
            if (!it.menus.isNullOrEmpty()) {
                for (data in it.menus) {
                    if (data.value.equals("AppChats")) {
                        binding.mMainMsgRb.visibility = View.VISIBLE
                        mViewModel.getUnreadMessage()
                    }
                }
            }

        })
        mViewModel.getFragmentLists()
        mViewModel.getFragmentListsLiveData().observe(this, Observer {
            initAdapter(it)
            setUIView()

        })

        mViewModel.getAppVersionBeanLiveData().observe(this, Observer {
            showUpdateDialog(it)
        })
        mViewModel.getIsMessageLiveData().observe(this, Observer {
            binding.mMainMsgNum.visibility = if (it) View.GONE  else View.VISIBLE
        })
    }


    /**
     * 设置延迟数据
     */
    private fun setUIView() {
        PushHelper.setAlias(this@MainHomeActivity)

        setRefreshTokenDownTimer()
        mRefreshTokenDownTimer?.start()
        mViewModel?.checkUpdate()

    }


    /**
     *  token 刷新倒计时
     */
    var mRefreshTokenDownTimer: CountDownTimer? = null
    private fun setRefreshTokenDownTimer() {
        mRefreshTokenDownTimer = object : CountDownTimer(864000000, 60000) {
            override fun onTick(millisUntilFinished: Long) {
                RefreshTokenUtils.requestTokenWithLoop(this@MainHomeActivity)
                RefreshTokenUtils.refreshWithLoop(this@MainHomeActivity)
                if( binding.mMainMsgRb.visibility == View.VISIBLE){
                    mViewModel.getUnreadMessage()
                }


            }

            /**
             * 倒计时结束后调用的
             */
            override fun onFinish() {

            }
        }
    }


    /**
     * 显示版本更新弹窗
     */
    var isUpdatShow = false
    fun showUpdateDialog(info: AppVersionBean?) {
        if (info == null) {
            return
        }
        if (isUpdatShow) {
            return
        }
        var ignoreVersion = LocalDataUtils.getValue(KeySet.IGNORE_VERSION)
        if(!ignoreVersion.isNullOrEmpty()){
            var ignoreVersionInt :Int= ignoreVersion.replace(".","").trim().toInt()
            var versionInt :Int= info.version.replace(".","").trim().toInt()
            if(ignoreVersionInt<versionInt){
                setUpdateDialog(info)
            }
        }else{
            setUpdateDialog(info)
        }

    }

    private fun setUpdateDialog(info: AppVersionBean) {
        val dialog = UpdateDialog(this)
        dialog.setCancelableFlag(!info.isForceUpdate)
        dialog.setVersionCode("V" + info.version)
        dialog.setVersionContent(info.releaseLog)
        dialog.setOnUpdateClickListener(object : UpdateDialog.OnUpdateClickListener {
            override fun onConfirm() {
                mViewModel.downloadAPK(info)
//                getPermissions(this@MainHomeActivity, Permission.READ_MEDIA_IMAGES
//                    ,{mViewModel.downloadAPK(info)}
//                    ,{ mViewModel.showMessage("缺少存储权限，无法下载")})

            }

            override fun onCancel() {
                isUpdatShow = false
                LocalDataUtils.setValue(KeySet.IGNORE_VERSION, info.version.toString())
            }
        })
        dialog.show()
        isUpdatShow = true
    }

    private fun initAdapter(fragments: MutableList<Fragment>) {
        val mAdapter = MainViewPagerAdapter(supportFragmentManager, fragments)
        binding.mViewPager.adapter = mAdapter
        binding.mViewPager.offscreenPageLimit = 4
        initListener()
        setTvGradientColor( binding.mMainHomeRb, "#FF4c93fd", "#FF4ca6fc")
        setTvGradientColor( binding.mMainMineRb, "#FF999999", "#FF999999")


    }

    override fun initListener() {
        binding.mMainBottomRg.setOnCheckedChangeListener { _, checkedId ->
            when (checkedId) {
                R.id.mMainHomeRb -> {
                    setTvGradientColor( binding.mMainHomeRb, "#FF4c93fd", "#FF4ca6fc")
                    setTvGradientColor( binding.mMainMineRb, "#FF999999", "#FF999999")
                    binding.mViewPager?.currentItem = 0

                }
                R.id.mMainMineRb -> {
                    setTvGradientColor( binding.mMainMineRb, "#FF4c93fd", "#FF4ca6fc")
                    setTvGradientColor( binding.mMainHomeRb, "#FF999999", "#FF999999")
                    binding.mViewPager?.currentItem = 1


                }


            }
        }
        binding.mMainMsgRb.setOnClickListener {

//            AppManager.getAppManager().startOpenNewWeb("http://10.168.100.33:6600/fang-sh/app/test", "")
//            AppManager.getAppManager().startOpenNewWeb("http://10.168.100.86:6600/fang-sh/app/user/house-list", "消息")
            AppManager.getAppManager().startOpenNewWeb(PageRoutes.PRIVACYSTATEMENT, "消息")
//            AppManager.getAppManager().startOpenNewWeb("http://10.168.100.33:6600/test", "")

        }


    }


    override fun onResume() {
        super.onResume()
        if (CustomActionUtils.openMsg) {
            CustomActionUtils.openMsg = false
        }
        if( binding.mMainMsgRb.visibility == View.VISIBLE){
            mViewModel.getUnreadMessage()
        }

    }

    override fun onDestroy() {
        super.onDestroy()
        mRefreshTokenDownTimer?.cancel()


    }

    private var exitTime: Long = 0
    override fun onBackPressed() {
        if (System.currentTimeMillis() - exitTime > 2000) {
            toast("再按一次返回退出程序")
            exitTime = System.currentTimeMillis()
        } else {
            LocalDataUtils.delValue(KeySet.IS_VERIFICATION_FINGERPRINT)
            AppManager.getAppManager().appExit()
        }
    }




}