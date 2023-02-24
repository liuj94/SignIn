package com.example.signin.adapter;

import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import com.example.signin.bean.MeetingData;
import com.example.signin.fragment.BannerFragment;

import java.util.List;


public class IconFristPagerAdapter extends FragmentStatePagerAdapter {
    private List<MeetingData> mTitles;
    public IconFristPagerAdapter(FragmentManager fm, List<MeetingData> titles) {
        super(fm);
        mTitles = titles;
    }
    @Override
    public Fragment getItem(int position) {
        BannerFragment fragment = BannerFragment.Companion.newInstance(mTitles.get(position));

        return fragment;
    }

    @Override
    public int getCount() {
        return mTitles == null ? 0 : mTitles.size();
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {

        return super.instantiateItem(container, position);
    }



    @Override
    public int getItemPosition(Object object) {
        return POSITION_NONE;
    }




}
