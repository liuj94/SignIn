package com.example.signin.mvvm.ui.adapter


import android.graphics.Color
import android.view.View

import com.example.signin.base.BaseBindingAdapter
import com.example.signin.databinding.ItemOwnersListBinding
import com.example.signin.mvvm.bean.OwnersData


class OwnerAdapter: BaseBindingAdapter<OwnersData, ItemOwnersListBinding>() {

    override fun onBindViewHolder(holder: ItemOwnersListBinding, item: OwnersData, position: Int) {
        holder.titleTv.text =  item.name
        holder.typeTv.text =  item.statusName
        holder.typeTv.visibility = if(!item.status.equals("3"))  View.GONE else View.VISIBLE
        if(item.status.equals("3")){
            holder.titleTv.setTextColor(Color.parseColor("#333333"))
        }else{
            holder.titleTv.setTextColor(Color.parseColor("#999999"))
        }

        }
    }
