package com.example.signin.mvvm.ui.fragment


import android.os.Build
import android.util.Log
import android.view.View
import androidx.annotation.RequiresApi
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.zcitc.cloudhome.base.BaseBindingFragment


import com.example.signin.R
import com.example.signin.PageRoutes
import com.example.signin.helper.AppManager
import com.example.signin.mvvm.bean.SelfUserData
import com.example.signin.mvvm.ui.activity.SettingActivity

import java.text.SimpleDateFormat
import java.util.*
import com.example.signin.constants.KeySet
import com.example.signin.databinding.FragMyListBinding
import com.example.signin.helper.RefreshTokenUtils
import com.example.signin.helper.database.LocalDataUtils
import com.example.signin.mvvm.bean.MenusData
import com.example.signin.mvvm.ui.activity.PrivacyWebViewctivity
import com.example.signin.mvvm.ui.adapter.MineTool2Adapter
import com.example.signin.mvvm.vm.MainMyFVM
import com.example.signin.startCompres
import com.example.signin.utils.takePhotoDialog


/**
 *   author : LiuJie
 *   date   : 2021/2/2513:36
 */
class   MyFragment : BaseBindingFragment<FragMyListBinding, MainMyFVM>() {

    companion object {
        fun newInstance(): MyFragment {
            return MyFragment()
        }
    }

    override fun getViewModel(): Class<MainMyFVM> = MainMyFVM::class.java

//    override fun initView(
//        inflater: LayoutInflater,
//        container: ViewGroup?,
//        savedInstanceState: Bundle?
//    ): View =
//        inflater.inflate(R.layout.frag_my_list, container, false)

    private var isVisibleFirst: Boolean = true
//    var menusAllList: MutableList<MenusData> = java.util.ArrayList()
    var userDataUrlStr: String = PageRoutes.SELF
    var menusUrlStr: String = PageRoutes.MENUS

    @RequiresApi(Build.VERSION_CODES.M)
    override fun initData() {
        if ( isVisibleFirst) {
//            setStatusBarHeight(toolbarView)
            isVisibleFirst = false
            //更新界面数据，如果数据还在下载中，就显示加载框
            initAdList()
            if ("1".equals(LocalDataUtils.getValue(KeySet.GUESTMODE))) {
                binding.userScanIv.visibility = View.GONE
                userDataUrlStr = PageRoutes.SELFTOURISTS
                menusUrlStr = PageRoutes.MENUSTOURISTS
            }
            mViewModel.requestSelf(userDataUrlStr)
            mViewModel.requestIconList(menusUrlStr)
            mViewModel.getAdListDataLiveData().observe(this, Observer {
                myPageIconsGroup.clear()
                if (!it.menus.isNullOrEmpty()) {
//                    menusAllList = getMenuList(it)

                    for (menu in it.menus) {
                        if (menu.value.equals("AppUser")) {
                            if(!menu.children.isNullOrEmpty()){
                                for (menuData in menu.children) {
                                    if(menuData.type==2){
                                        myPageIconsGroup.add(menuData)
                                    }

                                }
                            }

                        }

                    }


                }
                mineToolAdapter?.notifyDataSetChanged()


            })

        }
        mViewModel.getSelfDataLiveData().observe(this, Observer {
            try {
                setHeadData(it)
            } catch (e: Exception) {
                Log.d("HomeException", "Exception==getHotHouseListLiveData")
            }

        })

        setListener()
    }

    var myPageIconsGroup: MutableList<MenusData> = java.util.ArrayList()
    var mineToolAdapter: MineTool2Adapter? = null
    private fun initAdList() {
        binding.myPageIconsGroupRv.layoutManager = LinearLayoutManager(activity)
        mineToolAdapter = MineTool2Adapter().apply {
            submitList(myPageIconsGroup)
            setOnItemClickListener { _, _, position ->
                try {
                    if (myPageIconsGroup[position].redirectUrl.isNullOrEmpty()) {
                        goto(myPageIconsGroup[position].value)
                    } else {
                        if (myPageIconsGroup[position].redirectUrl.startsWith("http://") || myPageIconsGroup[position].redirectUrl.startsWith("https://")) {
                            AppManager.getAppManager().startWeb(myPageIconsGroup[position].redirectUrl, "")
                        } else {
                            goto(myPageIconsGroup[position].value)
                        }

                    }

                } catch (e: Exception) {

                }

            }
        }
        binding.myPageIconsGroupRv.adapter = mineToolAdapter
//        mineToolAdapter?.
    }


    /**
     * 点击事件
     */
    private fun setListener() {
        binding.refreshView?.setEnableLoadMore(false)
        binding.refreshView?.setOnRefreshListener {
            if ("1".equals(LocalDataUtils.getValue(KeySet.GUESTMODE))) {
                userDataUrlStr = PageRoutes.SELFTOURISTS
                menusUrlStr = PageRoutes.MENUSTOURISTS
            }
            mViewModel.requestSelf(userDataUrlStr)
            mViewModel.requestIconList(menusUrlStr)
            binding.refreshView.finishRefresh()
        }
        binding.setIv.setOnClickListener {
            AppManager.getAppManager().startActivity(SettingActivity::class.java)
        }
//        binding.houseHTv.setOnClickListener {
//            AppManager.getAppManager().startActivity(HouseListActivity::class.java)
//        }
        binding.privacyHTv.setOnClickListener {
            AppManager.getAppManager().startActivity(PrivacyWebViewctivity::class.java)
        }
        binding.userScanIv.setOnClickListener {
            AppManager.getAppManager().startOpenNewWeb(PageRoutes.QRCODECARD, "")
        }
        binding.mineAvatar.setOnClickListener {
            activity?.let { it1 ->
                takePhotoDialog(it1,1){photos->
                    Glide.with(it1)
                        .load(photos[0].path)
                        .placeholder(R.mipmap.home_image_face)
                        .error(R.mipmap.home_image_face)
                        .into(binding.mineAvatar)
                    startCompres(it1, photos[0].path) { file -> mViewModel.uploadFile(file) }
                }
            }



        }

    }





    fun getDataString(long: Long?): String {
        if (long == null) {
            return ""
        }
        val simpleDateFormat = SimpleDateFormat("yyyy-MM-dd")
        val date = Date(long)
        return simpleDateFormat.format(date)
    }

    fun setHeadData(data: SelfUserData) {
        binding.houseHTv.visibility = View.GONE
        if (data == null) {

            binding.mineName.text = ""
            binding.mineCertification.text = ""
            binding.mineRecordNumber.text = ""
            binding.effectiveDate.text = ""

            try {
                Glide.with(requireActivity())
                    .load(R.mipmap.home_image_face)
                    .placeholder(R.mipmap.home_image_face)
                    .error(R.mipmap.home_image_face)
                    .into(binding.mineAvatar)
            } catch (e: Exception) {
            }
        } else {
            binding.mineName.text = data.realName
            binding.mineCertification.text = data.tel
            if (data.isPractitioner) {
//                houseHTv.visibility = View.VISIBLE
                binding.mineRecordNumber.text = "备案编号：" + data.certificateNo
                binding.effectiveDate.text = "有效期至：" + getDataString(data.certificateValidDate)
            }

            try {
                Glide.with(requireActivity())
                    .load(data.avatarUrl)
                    .placeholder(R.mipmap.home_image_face)
                    .error(R.mipmap.home_image_face)
                    .into(binding.mineAvatar)
            } catch (e: Exception) {
            }
        }

    }




    override fun onResume() {
        super.onResume()
        try {
            RefreshTokenUtils.requestTokenWithLoop(requireActivity())
            RefreshTokenUtils.refreshWithLoop(requireActivity())
        } catch (e: Exception) {
        }

    }

    fun showAvatar(avatarUrl: String) {
        try {
            Glide.with(requireActivity())
                .load(avatarUrl)
                .placeholder(R.mipmap.home_image_face)
                .error(R.mipmap.home_image_face)
                .into(binding.mineAvatar)
        } catch (e: Exception) {
        }

    }


    fun goto(value: String) {
        //AppCalculator 房贷计算器 AppNews 楼市新闻  AppAnsweringQuestion政策答疑
//        var url: String = "$BASE_WB_CNNBFDC2/deeplink://local/$value"
        var url: String = ""
        when (value) {
            "AppCalculator" -> url = "${PageRoutes.BASE_WB_CNNBFDC2_GZ}/house/calculator"
            "AppNews" -> url = "${PageRoutes.BASE_WB_CNNBFDC2_GZ}/house/articles?code=10"
            "AppAnsweringQuestion" -> url = "${PageRoutes.BASE_WB_CNNBFDC2_GZ}/house/articles?code=11"
            "AppAlbum" -> {
                //楼盘图
                url = "${PageRoutes.BASE_WB_CNNBFDC2}/deeplink://local/album"
            }
            "AppInformation" -> {
                //楼盘资讯
                url = "${PageRoutes.BASE_WB_CNNBFDC2}/deeplink://local/information"
            }
            "AppAppointment" -> {
                //预约带看
                url = "${PageRoutes.BASE_WB_CNNBFDC2}/deeplink://local/appointment"
            }
            "AppEntrust" -> {
                //委托管理
            }
            "AppTask" -> {
                //任务池
            }
            "AppUsers" -> {
                //人员管理
            }
            "AppDepartment" -> {
                //组织结构管理                         deeplink://local/company/department
                url = "${PageRoutes.BASE_WB_CNNBFDC2}/deeplink://local/company/group-structure"
            }
            "AppCompanyHouse" -> {
                //房源管理
                url = "${PageRoutes.BASE_WB_CNNBFDC2}/deeplink://local/company/house-list"
            }
            "AppUserHouse" -> {
                //我的房源
                url = "${PageRoutes.BASE_WB_CNNBFDC2}/deeplink://local/user/house-list"
            }
            "AppFavorites" -> {
                //我的收藏                             deeplink://local/user/favorites?tab=0
                url = "${PageRoutes.BASE_WB_CNNBFDC2}/deeplink://local/user/favorites?tab=0"

            }
            "AppEditRecord"->{
                //我的编辑
//                url = "${PageRoutes.BASE_WB_CNNBFDC2}/deeplink://local/user/edit-record"
                url = "${PageRoutes.BASE_WB_CNNBFDC2}/user/edit-record"
            }
//            "AppUserHouseForConsultant" -> {
//                //我的房源顾问
//                AppManager.getAppManager().startActivity(HouseListActivity::class.java)
//                return
//            }


        }
        AppManager.getAppManager().startWeb(url, "")
    }
}
