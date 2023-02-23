package com.example.signin.adapter


import com.example.signin.base.BaseBindingAdapter
import com.example.signin.bean.SiginUpListData
import com.example.signin.databinding.ListMeetingde1Binding


class FMeetingDeListAdapter: BaseBindingAdapter<SiginUpListData, ListMeetingde1Binding>() {

    override fun onBindViewHolder(holder: ListMeetingde1Binding, item: SiginUpListData, position: Int) {
        holder.name.text =  item.name
        holder.num1.text = ""+ item.locationCount
        holder.num2.text =  ""+item.beSignInCount
        holder.num3.text =  ""+item.signInCount
        holder.num4.text =  ""+item.unSignInCount

    }
    }
