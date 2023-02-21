package com.example.signin.adapter

import android.util.Log
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter

class MainViewPagerAdapter(fm: FragmentManager, private val fragmentList: List<Fragment>) :
    FragmentPagerAdapter(fm) {
    override fun getItem(position: Int): Fragment {
        return fragmentList[position]
    }

    override fun getCount(): Int {
        return fragmentList.size
    }

    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        Log.e("zrg", "instantiateItem: 当前位置position=$position")
        return super.instantiateItem(container, position)
    }

    override fun getItemPosition(`object`: Any): Int {
        return POSITION_NONE
    }
}