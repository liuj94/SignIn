package com.example.signin.adapter


import android.view.View

import com.example.signin.base.BaseBindingAdapter
import com.example.signin.bean.SiginData
import com.example.signin.bean.TypeData
import com.example.signin.databinding.ListTcMeetingdeBinding


class SelectDataAdapter: BaseBindingAdapter<SiginData, ListTcMeetingdeBinding>() {

    override fun onBindViewHolder(holder: ListTcMeetingdeBinding, item: SiginData, position: Int) {
        holder.name.text =  item.name
        if(item.isMyselect){
            holder.img.visibility = View.VISIBLE
        }else{
            holder.img.visibility = View.GONE
        }

    }
    }
