package com.example.signin.adapter;

import android.content.Context;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.DecelerateInterpolator;

import androidx.viewpager.widget.ViewPager;


import com.example.signin.R;

import net.lucode.hackware.magicindicator.buildins.UIUtil;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.CommonNavigatorAdapter;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.IPagerIndicator;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.IPagerTitleView;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.indicators.LinePagerIndicator;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * date   : 2021/3/212:49
 * @author liujie
 */
public class MyCommonNavigatorAdapter2 extends CommonNavigatorAdapter {
    List<String> recommends = new ArrayList<>();
    Context mContext;
    ViewPager viewPager;
    int w = 36;
    public MyCommonNavigatorAdapter2(Context mContext, List<String> recommends, ViewPager viewPager){
        this.mContext = mContext;
        this.recommends = recommends;
        this.viewPager = viewPager;
    }
    public MyCommonNavigatorAdapter2(Context mContext, List<String> recommends, ViewPager viewPager, int w){
        this.mContext = mContext;
        this.recommends = recommends;
        this.viewPager = viewPager;
        this.w = w;
    }
    @Override
    public int getCount() {
        return recommends.size();
    }

    @Override
    public IPagerTitleView getTitleView(Context context, int index) {
        ScaleTransitionPagerTitleView simplePagerTitleView = new ScaleTransitionPagerTitleView(context);
        simplePagerTitleView.setText(recommends.get(index));
        simplePagerTitleView.setNormalColor(mContext.getResources().getColor(R.color.gray_4));
        simplePagerTitleView.setSelectedColor(mContext.getResources().getColor(R.color.text4ca6fc));
        simplePagerTitleView.setOnClickListener(v -> viewPager.setCurrentItem(index));

        return simplePagerTitleView;
    }

    @Override
    public IPagerIndicator getIndicator(Context context) {
        HXLinePagerIndicator indicator = new HXLinePagerIndicator(context);
        indicator.setMode(LinePagerIndicator.MODE_EXACTLY);
        indicator.setLineHeight(UIUtil.dip2px(context, 5));
        indicator.setLineWidth(UIUtil.dip2px(context, w));
        indicator.setRoundRadius(UIUtil.dip2px(context, 2));

        indicator.setStartInterpolator(new AccelerateInterpolator());
        indicator.setEndInterpolator(new DecelerateInterpolator(2.0f));

        return indicator;
    }
}
