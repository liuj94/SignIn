package com.example.signin.mvvm.ui.adapter


import android.graphics.Color
import android.view.View

import com.example.signin.base.BaseBindingAdapter
import com.example.signin.bean.MeetingData
import com.example.signin.databinding.ListHomeBinding


class HomeListAdapter: BaseBindingAdapter<MeetingData, ListHomeBinding>() {

    override fun onBindViewHolder(holder: ListHomeBinding, item: MeetingData, position: Int) {
        holder.name.text =  item.name
        holder.time1.text =  item.createTime
        holder.time2.text =  item.updateTime}
    }
