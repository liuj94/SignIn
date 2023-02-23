package com.example.signin.adapter


import android.view.View

import com.example.signin.base.BaseBindingAdapter
import com.example.signin.bean.TypeData
import com.example.signin.databinding.ListTcMeetingdeBinding


class SelectTypeDataAdapter: BaseBindingAdapter<TypeData, ListTcMeetingdeBinding>() {

    override fun onBindViewHolder(holder: ListTcMeetingdeBinding, item: TypeData, position: Int) {
        holder.name.text =  item.dictLabel
        if(item.isMyselect){
            holder.img.visibility = View.VISIBLE
        }else{
            holder.img.visibility = View.GONE
        }

    }
    }
