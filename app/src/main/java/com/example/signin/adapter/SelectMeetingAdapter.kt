package com.example.signin.adapter


import android.view.View

import com.example.signin.base.BaseBindingAdapter
import com.example.signin.bean.SiginUpListData
import com.example.signin.databinding.ListTcMeetingdeBinding


class SelectMeetingAdapter: BaseBindingAdapter<SiginUpListData, ListTcMeetingdeBinding>() {

    override fun onBindViewHolder(holder: ListTcMeetingdeBinding, item: SiginUpListData, position: Int) {
        holder.name.text =  item.name
        if(item.isMyselect){
            holder.img.visibility = View.VISIBLE
        }else{
            holder.img.visibility = View.GONE
        }

    }
    }
