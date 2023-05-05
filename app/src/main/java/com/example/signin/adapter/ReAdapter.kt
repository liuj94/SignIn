package com.example.signin.adapter


import android.view.View
import com.example.signin.base.BaseBindingAdapter
import com.example.signin.bean.MeetingFormList
import com.example.signin.databinding.ItemReInfoBinding


class ReAdapter: BaseBindingAdapter<MeetingFormList, ItemReInfoBinding>() {

    override fun onBindViewHolder(holder: ItemReInfoBinding, item: MeetingFormList, position: Int) {
        holder.name.visibility = View.GONE
        holder.name2.visibility = View.GONE
        if(position==0){
            holder.name.visibility = View.VISIBLE
            holder.name.text =  item.value
        }else{
            holder.name2.visibility = View.VISIBLE
            holder.name2.text =  item.value
        }

    }
    }
