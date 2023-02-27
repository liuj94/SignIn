package com.example.signin.adapter


import android.graphics.Color
import com.example.signin.R

import com.example.signin.base.BaseBindingAdapter
import com.example.signin.bean.TypeData
import com.example.signin.databinding.ListTcSelectBinding


class SelectAdapter: BaseBindingAdapter<TypeData, ListTcSelectBinding>() {

    override fun onBindViewHolder(holder: ListTcSelectBinding, item: TypeData, position: Int) {
        holder.name.text =  item.dictLabel
        if(item.isMyselect){
            holder.name.setTextColor(Color.parseColor("#3974F6"))
            holder.img.setImageResource(R.mipmap.xuanz1)
        }else{
            holder.name.setTextColor(Color.parseColor("#333333"))
            holder.img.setImageResource(R.mipmap.xuanzhe2)
        }

    }
    }
