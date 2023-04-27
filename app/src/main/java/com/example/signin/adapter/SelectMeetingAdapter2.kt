package com.example.signin.adapter


import android.view.View

import com.example.signin.base.BaseBindingAdapter
import com.example.signin.bean.SiginData
import com.example.signin.bean.SiginUpListData
import com.example.signin.databinding.ListTcMeetingdeBinding


class SelectMeetingAdapter2: BaseBindingAdapter<SiginData, ListTcMeetingdeBinding>() {

    override fun onBindViewHolder(holder: ListTcMeetingdeBinding, item: SiginData, position: Int) {
        holder.name.text =  item.name
        holder.kuan.text =  item.name
        if(item.isMyselect){
            holder.img.visibility = View.VISIBLE
        }else{
            holder.img.visibility = View.GONE
        }
        if(item.isKuan){
            holder.name.visibility = View.GONE
            holder.kuan.visibility = View.VISIBLE
        }else{
            holder.kuan.visibility = View.GONE
            holder.name.visibility = View.VISIBLE
        }

    }
    }
