package com.example.signin.fragment


import android.app.Activity
import android.os.Build
import android.view.View
import android.view.ViewGroup
import android.view.ViewTreeObserver
import android.widget.ImageView
import androidx.annotation.RequiresApi
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.GridLayoutManager
import com.dylanc.longan.toast
import com.lzy.okgo.OkGo
import com.lzy.okgo.model.Response
import com.example.signin.R
import com.example.signin.PageRoutes

/**
 *   author : LiuJie
 *   date   : 2021/2/2513:36
 */
class HomeMainFragment : BaseBindingFragment<FragNewhomemainListBinding,MainHomeFVM>() {

    companion object {
        fun newInstance(): HomeMainFragment {
            return HomeMainFragment()
        }
    }

    override fun getViewModel(): Class<MainHomeFVM> = MainHomeFVM::class.java


    private var isVisibleFirst: Boolean = true

    var menusUrlStr: String = PageRoutes.MENUS
    @RequiresApi(Build.VERSION_CODES.M)
    override fun initData() {
        if ( isVisibleFirst) {
//            setStatusBarHeight(toolbarView)
            isVisibleFirst = false
            //更新界面数据，如果数据还在下载中，就显示加载框
            initBanner()
            initAdapter()
            if ("1".equals(LocalDataUtils.getValue(KeySet.GUESTMODE))) {
                menusUrlStr = PageRoutes.MENUSTOURISTS
            }else{
                getSelfData()
            }
            mViewModel.requestIconList(menusUrlStr)

            getBannerData()

        }
        initFragData()
    }

    private fun getBannerData() {
        var type = LocalDataUtils.getValue(KeySet.TOKEN_TYPE)
        var token = LocalDataUtils.getValue(KeySet.API_TOKEN)


        OkGo.get<BannerModel>(PageRoutes.SINGLEADV )
            .tag(this)
            .headers(KeySet.AUTHORIZATION, "$type $token")
            .execute(object : RequestCallback<BannerModel>() {
                override fun onMySuccess(data: BannerModel) {

                    activity?.let { getBannerImageAdapter(it,data.datas) }

                }

                override fun onSuccessNullData() {
                    activity?.let { getBannerImageAdapter(it,null) }
                }

                override fun onError(response: Response<BannerModel>) {
                    activity?.let { getBannerImageAdapter(it,null) }

                }


            })
    }


    /**
     * 初始化菜单适配器
     */
    private fun initAdapter() {

       binding. myPageIconsGroup3Rv.layoutManager = GridLayoutManager(activity, 3)
        mineTool3Adapter = HomeFunctionToolAdapter().apply {
            submitList(myPageIconsGroup3)
            setOnItemClickListener { adapter, view, position ->

                try {
                    if (myPageIconsGroup3[position].redirectUrl.isNullOrEmpty()) {

                        goto(myPageIconsGroup3[position].value,position)
                    } else {
                        if (myPageIconsGroup3[position].redirectUrl.startsWith("http://") || myPageIconsGroup3[position].redirectUrl.startsWith("https://")) {
                            AppManager.getAppManager().startWeb(myPageIconsGroup3[position].redirectUrl, "")
                        } else {
                            goto(myPageIconsGroup3[position].value,position)
                        }

                    }

                } catch (e: Exception) {

                }
            }
        }
        binding.myPageIconsGroup3Rv.adapter = mineTool3Adapter

    }

    /**
     * 初始化菜单适配器
     */
    private fun initFristTootAdapter() {
        binding.myPageIconsGroup2Rv.layoutManager = GridLayoutManager(activity, 3)
        mineTool2Adapter = HomeFunctionToolAdapter().apply {
            submitList(myPageIconsGroup2)
            setOnItemClickListener { _, _, position ->
                try {
                    if (myPageIconsGroup2[position].redirectUrl.isNullOrEmpty()) {
                        goto(myPageIconsGroup2[position].value,position)
                    } else {
                        if (myPageIconsGroup2[position].redirectUrl.startsWith("http://") || myPageIconsGroup2[position].redirectUrl.startsWith("https://")) {
                            AppManager.getAppManager().startWeb(myPageIconsGroup2[position].redirectUrl, "")
                        } else {
                            goto(myPageIconsGroup2[position].value,position)
                        }

                    }

                } catch (e: Exception) {

                }
            }
        }
        binding.myPageIconsGroup2Rv.adapter = mineTool2Adapter
//        mineTool2Adapter?.
    }


    private val mImageItem: ArrayList<Photo>? = ArrayList<Photo>()

    /**
     * 点击事件
     */
    private fun setListener() {
        binding.refreshView?.setEnableLoadMore(false)
        binding.refreshView?.setOnRefreshListener {
            if ("1".equals(LocalDataUtils.getValue(KeySet.GUESTMODE))) {
                menusUrlStr = PageRoutes.MENUSTOURISTS
            }
            mViewModel.requestIconList(menusUrlStr)
            getBannerData()
            binding.refreshView.finishRefresh()
        }
        binding.setIv.setOnClickListener {
            AppManager.getAppManager().startActivity(SettingActivity::class.java)
        }
        binding.scanIv.setOnClickListener {
            activity?.let { it1 -> gotoScanActivity(it1,0) }


        }
        binding.userScanIv.setOnClickListener {
            AppManager.getAppManager().startOpenNewWeb(PageRoutes.QRCODECARD, "")
        }


    }
    fun startCompress(path: String, back: (file: File) -> Unit) {
        CompressTool.with(activity)
            .load(path)
            .onSuccess { back.invoke(it) }
            .launch()
    }



    var otherHeigh: Int = 0

    var mineTool2Adapter: HomeFunctionToolAdapter? = null;
    var mineTool3Adapter: HomeFunctionToolAdapter? = null;

    var myPageIconsGroup2: MutableList<MenusData> = ArrayList()
    var myPageIconsGroup3: MutableList<MenusData> = ArrayList()

     fun initFragData() {

        mViewModel.getAdListDataLiveData().observe(this, Observer {

            myPageIconsGroup3.clear()
            myPageIconsGroup2.clear()
            if (!it.menus.isNullOrEmpty()) {


//                menusAllList = getMenuList(it)
                //AppCalculator 房贷计算器 AppNews 楼市新闻  AppAnsweringQuestion政策答疑
                for (menu in it.menus) {
                    if (menu.value.equals("AppPublic")) {
                        if(!menu.children.isNullOrEmpty()){
                            for (menuData in menu.children) {
                                if(menuData.type==2){
                                    myPageIconsGroup3.add(menuData)}
                            }
                        }


                    } else if (menu.value.equals("AppBusiness")) {
                        if(!menu.children.isNullOrEmpty()) {
                            for (menuData in menu.children) {
                                if (menuData.type == 2) {
                                    myPageIconsGroup2.add(menuData)
                                }
                            }
                        }
                    }
                }

                if(myPageIconsGroup2.isNullOrEmpty()){
                    binding. myPageIconsGroup2CardView.visibility = View.GONE
                    binding.visitorTv.visibility = View.VISIBLE
                }else{
                    binding.myPageIconsGroup2CardView.visibility = View.VISIBLE
                    binding.visitorTv.visibility = View.GONE
                }

                initFristTootAdapter()

            }
            mineTool3Adapter?.notifyDataSetChanged()


        })
        setListener()


    }




    fun goto(value: String,position :Int) {
        //AppCalculator 房贷计算器 AppNews 楼市新闻  AppAnsweringQuestion政策答疑
//    var url: String = "$BASE_WB_CNNBFDC2/deeplink://local/$value"
        var url: String = ""
        when (value) {
            "AppCalculator" -> url = "$BASE_WB_CNNBFDC2_GZ/house/calculator"
            "AppNews" -> url = "$BASE_WB_CNNBFDC2_GZ/house/articles?code=10"
            "AppAnsweringQuestion" -> url = "$BASE_WB_CNNBFDC2_GZ/house/articles?code=11"
            "AppAlbum" -> {
                //楼盘图
                url = "$BASE_WB_CNNBFDC2/deeplink://local/album"
            }
            "AppInformation" -> {
                //楼盘资讯
                url = "$BASE_WB_CNNBFDC2/deeplink://local/information"
            }
            "AppAppointment" -> {
                //预约带看
                url = "$BASE_WB_CNNBFDC2/deeplink://local/appointment"
            }
            "AppDepartment" -> {
                //组织结构管理
                url = "$BASE_WB_CNNBFDC2/deeplink://local/company/department"
            }
            "AppCompanyHouse" -> {
                //房源管理
                url = "$BASE_WB_CNNBFDC2/deeplink://local/company/house-list"
            }
            "AppUserHouse" -> {
                //我的房源
                url = "$BASE_WB_CNNBFDC2/deeplink://local/user/house-list"
            }
            "AppFavorites" -> {
                //我的收藏
                url = "${BASE_WB_CNNBFDC2}/deeplink://local/user/favorites?tab=0"

            }
            "APPPresalesHouse" ->{
                //预展楼盘
                url = "${BASE_WB_CNNBFDC2_GZ}/house/yzlp/sh"
            }
            "AppPayment" ->{
                //资金支付
                url = "${BASE_WB_CNNBFDC2}/fund-supervision/payment"

            }
           "AppHouseAudit" ->{
               //房源审核
               url = "${BASE_WB_CNNBFDC2}/house/audit"
           }
            "APPLiveStreaming"->{
                LocalDataUtils.setValue("LivesPublish","")
                LocalDataUtils.setValue("LivesStart","")
                LocalDataUtils.setValue("LivesStop","")
                LocalDataUtils.setValue("LivesBanComment","")
                LocalDataUtils.setValue("LivesCancelBanComment","")
                //直播
                if ("1".equals(LocalDataUtils.getValue(KeySet.GUESTMODE))) {
                    toast("当前是游客模式不能开播")
                    return
                }
//                LiveManagement.Lives.Publish
                //LiveManagement.Lives.Start
                //LiveManagement.Lives.Stop
                //权限 添加
               var permissionLives : List<MenusData> = myPageIconsGroup2[position].children
                for (permissionData in permissionLives){
                    if (permissionData.value.contains("SelfLiveManagement.Lives.Publish")){
                        LocalDataUtils.setValue("LivesPublish","LiveManagement.Lives.Publish")
                    }
                    if (permissionData.value.contains("SelfLiveManagement.Lives.Start")){
                        LocalDataUtils.setValue("LivesStart","LiveManagement.Lives.Start")
                    }
                    if (permissionData.value.contains("SelfLiveManagement.Lives.Stop")){
                        LocalDataUtils.setValue("LivesStop","LiveManagement.Lives.Stop")
                    }
                    if (permissionData.value.contains("SelfLiveManagement.Lives.CancelBanComment")){
                        LocalDataUtils.setValue("LivesCancelBanComment","LiveManagement.Lives.CancelBanComment")
                    }
                    if (permissionData.value.contains("SelfLiveManagement.Lives.BanComment")){
                        LocalDataUtils.setValue("LivesBanComment","LiveManagement.Lives.BanComment")
                    }

//


                }
                AppManager.getAppManager().startActivity(LiveListActivity::class.java)
                return
            }

        }
        AppManager.getAppManager().startWeb(url, "")
    }
    var bannerList: MutableList<BannerData>? = null
    fun initBanner() {

        binding.bannerRl.getViewTreeObserver().addOnGlobalLayoutListener(object : ViewTreeObserver.OnGlobalLayoutListener {

            override fun onGlobalLayout() {
                val lp: ViewGroup.LayoutParams = binding.bannerRl.layoutParams
                lp.width = binding.bannerRl.width
                lp.height = Math.round(binding.bannerRl.width * 0.5626822157434402).toInt()
                binding.bannerRl.layoutParams = lp
                binding.bannerRl.viewTreeObserver
                    .removeGlobalOnLayoutListener(this)
            }
        });


        binding.banner.setBannerRound(15f)
        binding.banner.setAdapter(getBannerImageAdapter(requireActivity(), bannerList))
        binding.banner.setOnBannerListener { _, position ->

        }
    }

    fun getBannerImageAdapter(activity: Activity, bannerList: List<BannerData>?): BannerImageAdapter<Any> {

        var path: List<Any>? = null
        if (bannerList.isNullOrEmpty()) {
            path = java.util.ArrayList<Int>()
            path.add(R.mipmap.banner)
        } else {
            path = java.util.ArrayList<String>()
            for (bean in bannerList) {
                bean.imgUrl?.let { path.add(it) }

            }
        }
        var adapter = object : BannerImageAdapter<Any>(path) {
            override fun onCreateHolder(parent: ViewGroup?, viewType: Int): BannerImageHolder {
                val imageView = ImageView(parent!!.context)
                val params = ViewGroup.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.MATCH_PARENT
                )
                imageView.layoutParams = params
                imageView.scaleType = ImageView.ScaleType.FIT_XY
                return BannerImageHolder(imageView)

            }

            override fun onBindView(holder: BannerImageHolder, data: Any, position: Int, size: Int) {
                if (data is String) {
                    GlideUtils.showImage(
                        activity,
                        data as String?,
                        holder.imageView,
                        R.mipmap.home_image_placeholder3
                    )


                } else if (data is Int) {
                    GlideUtils.showImage(
                        activity,
                        data,
                        holder.imageView,
                        R.mipmap.home_image_placeholder3
                    )


                }
            }

        }
        return adapter
    }
//    fun initBanner(){
//
//        val display: Display =requireActivity().windowManager.defaultDisplay
//        val lp = LinearLayout.LayoutParams(
//            display.width, Math.round(display.width * 0.3455284552845528).toInt()
//        )
//        lp.topMargin = dp2px(requireActivity(), 0)
//        binding.banner.layoutParams = lp
//        binding.banner.setBackgroundColor(requireActivity().resources.getColor(R.color.gray_f5f5f5))
//        binding.banner.setPadding(dp2px(requireActivity(), 16), 0, dp2px(requireActivity(), 16), 0)
//        binding.banner.setBannerStyle(BannerConfig.CIRCLE_INDICATOR)
//
//    }

//    fun setBannerData(
//        bannerList: List<BannerData>?,
//    ) {
//
//        var path: List<*>? = null
//        if (bannerList.isNullOrEmpty()) {
//            path = java.util.ArrayList<Int>()
//            path.add(R.mipmap.banner)
//        } else {
//            path = java.util.ArrayList<String>()
//            for (bean in bannerList) {
//                path.add(bean.multimediaUrl)
//            }
//        }
//
//        banner.setImageLoader(GlideImageLoader())
//        banner.setImages(path)
//        banner.setDelayTime(3000)
//        banner.setViewPagerIsScroll(true)
//        banner.start()
//        if(!bannerList.isNullOrEmpty()){
//            if (bannerList.size > 1) {
//                banner.setOnBannerListener(OnBannerListener { position ->
//                    if (!bannerList.isNullOrEmpty()) {
//                        if (position < bannerList.size) {
//                            if (!bannerList[position].eventLink.isNullOrEmpty()
//                            ) {
//                                AppManager.getAppManager()
//                                    .startWeb(bannerList[position].eventLink, "")
//                            }
//                        }
//                    }
//                })
//            } else if (bannerList.size == 1) {
//                banner.setOnClickListener(View.OnClickListener { v: View? ->
//                    if (bannerList.size > 0) {
//                        if (bannerList.get(0).eventLink != null && "" != bannerList.get(0)
//                                .eventLink
//                        ) {
//                            AppManager.getAppManager()
//                                .startWeb(bannerList[0].eventLink, "")
//                        }
//                    }
//
//
//                })
//            }
//        }
//
//    }
//    class GlideImageLoader : ImageLoader() {
//        override fun displayImage(context: Context?, path: Any?, imageView: ImageView) {
//            try {
//                if (path is String) {
//                    GlideUtils.showImage(
//                        context,
//                        path as String?,
//                        imageView,
//                        R.mipmap.home_image_placeholder3
//                    )
//                    imageView.scaleType = ImageView.ScaleType.FIT_XY
//                }
//                if (path is Int) {
//                    GlideUtils.showImage(
//                        context,
//                        R.mipmap.banner,
//                        imageView,
//                        R.mipmap.home_image_placeholder3
//                    )
//                    imageView.scaleType = ImageView.ScaleType.FIT_XY
//
//                }
//            } catch (e: java.lang.Exception) {
//                e.printStackTrace()
//            }
//        }
//    }


}
