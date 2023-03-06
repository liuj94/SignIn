package com.example.signin.adapter


import com.example.signin.base.BaseBindingAdapter
import com.example.signin.bean.MeetingData
import com.example.signin.databinding.ListHomeBinding


class HomeListAdapter: BaseBindingAdapter<MeetingData, ListHomeBinding>() {

    override fun onBindViewHolder(holder: ListHomeBinding, item: MeetingData, position: Int) {
        holder.name.text =  item.name
        holder.time1.text =  item.startTime
        holder.time2.text =  item.endTime
        if(item.status.equals("2")){
            holder.state.text = "报名中"
        }else if(item.status.equals("1")){
            holder.state.text = "未开始"
        }else{
            holder.state.text = "已结束"
        }

    }
    }
