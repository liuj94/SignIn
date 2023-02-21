package com.example.signin.fragment


import android.os.Build
import android.view.View
import androidx.annotation.RequiresApi
import com.example.signin.R
import com.example.signin.base.BaseBindingFragment
import com.example.signin.base.BaseViewModel
import com.example.signin.databinding.FragHomeBinding

/**
 *   author : LiuJie
 *   date   : 2021/2/2513:36
 */
class HomeMainFragment : BaseBindingFragment<FragHomeBinding, BaseViewModel>() {

    companion object {
        fun newInstance(): HomeMainFragment {
            return HomeMainFragment()
        }
    }

    override fun getViewModel(): Class<BaseViewModel> = BaseViewModel::class.java


    private var isVisibleFirst: Boolean = true

    @RequiresApi(Build.VERSION_CODES.M)
    override fun initData() {
        if ( isVisibleFirst) {
//            setStatusBarHeight(toolbarView)
            isVisibleFirst = false


        }

    }

    private fun initTabList(dates: AdvertisementRecommendationData) {

        var titles: MutableList<String> = ArrayList()
        var redirectUrl: MutableList<String> = ArrayList()
        var orderNo: MutableList<String> = ArrayList()
        for (it in dates.datas) {
            titles.add(it.tags)
            redirectUrl.add(it.redirectUrl)
            orderNo.add(it.menuCode.toString())
        }
        if (titles.size > 0) {
            mBinding.magicIndicator.visibility = View.VISIBLE
        }
        mBinding.homeVp.adapter = TabPagerAdapter(childFragmentManager, redirectUrl, orderNo)
        mBinding.homeVp.offscreenPageLimit = titles.size
        context?.let {
            mBinding.magicIndicator.setBackgroundColor(
                ContextCompat.getColor(context!!, R.color.gray_f5f5f5)
            )
        }
        val commonNavigator = CommonNavigator(activity)
        commonNavigator.isAdjustMode = false
        commonNavigator.adapter = MyCommonNavigatorAdapter(activity, titles, mBinding.homeVp)
        mBinding.magicIndicator.navigator = commonNavigator

        ViewPagerHelper.bind(mBinding.magicIndicator, mBinding.homeVp)


    }

}
