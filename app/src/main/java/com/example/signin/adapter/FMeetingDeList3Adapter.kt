package com.example.signin.adapter


import android.graphics.Color
import android.view.View
import com.alibaba.fastjson.JSON
import com.bumptech.glide.Glide
import com.example.signin.PageRoutes.Companion.BaseUrl
import com.example.signin.base.BaseBindingAdapter
import com.example.signin.bean.MeetingUserData
import com.example.signin.bean.TypeData
import com.example.signin.bean.TypeModel
import com.example.signin.databinding.ListMeetingde3Binding


class FMeetingDeList3Adapter : BaseBindingAdapter<MeetingUserData, ListMeetingde3Binding>() {
    val PHONE_BLUR_REGEX = "(\\d{3})\\d{4}(\\d{4})"
    val PHONE_BLUR_REPLACE_REGEX = "$1****$2"
    private var siginUp2List: MutableList<TypeData> = ArrayList()
    fun setSiginUp2List(siginUp2List: MutableList<TypeData>) {
        this.siginUp2List = siginUp2List
    }

    override fun onBindViewHolder(
        holder: ListMeetingde3Binding,
        item: MeetingUserData,
        position: Int
    ) {
        holder.name.text = item.name
        holder.mobile.text = ""
        item.mobile?.let {
            holder.mobile.text =
                item.mobile.replace(PHONE_BLUR_REGEX.toRegex(), PHONE_BLUR_REPLACE_REGEX)
        }
        if (item.avatar.isNullOrEmpty()) {
            holder.img.visibility = View.GONE
        } else {
            holder.img.visibility = View.VISIBLE
            Glide.with(holder.img.context).load(BaseUrl + item.avatar).into(holder.img)
        }
        var model = JSON.parseObject(kv.getString("TypeModel", ""), TypeModel::class.java)

        if (!item.userMeetingType.isNullOrEmpty()) {
            if (!model.user_meeting_type.isNullOrEmpty()) {
                for (data in model.user_meeting_type) {

                    if (item.userMeetingType.equals(data.dictValue)) {
                        holder.typeTv.text = data.dictLabel

                    }
                }
            }
            holder.typeTv.visibility = View.VISIBLE
        } else {
            holder.typeTv.visibility = View.GONE
        }
        if (!item.userMeetingSignUpStatus.isNullOrEmpty()) {

            if(!model.user_meeting_sign_up_status.isNullOrEmpty()){
                for (data in model.user_meeting_sign_up_status){
                    if(item.userMeetingSignUpStatus.equals(data.dictValue))  {
                        holder.state.text =  data.dictLabel
                        if(data.dictValue.equals("2")){
                            holder.state.setTextColor(Color.parseColor("#43CF7C"))
                        }else if(item.userMeetingSignUpStatus.equals("1")){
                            holder.state.setTextColor(Color.parseColor("#FFC300"))
                        }else{
                            holder.state.setTextColor(Color.parseColor("#666666"))
                        }
                        return
                    }
                }
            }
//            if (!siginUp2List.isNullOrEmpty()) {
//                for (data in siginUp2List) {
//                    if (item.userMeetingSignUpStatus.equals(data.dictValue)) {
//                        holder.state.text = data.dictLabel
//                        if(data.dictValue.equals("2")){
//                            holder.state.setTextColor(Color.parseColor("#43CF7C"))
//                        }else if(item.userMeetingSignUpStatus.equals("1")){
//                            holder.state.setTextColor(Color.parseColor("#FFC300"))
//                        }else{
//                            holder.state.setTextColor(Color.parseColor("#666666"))
//                        }
//                        return
//                    }
//                }
//            }

            holder.state.visibility = View.VISIBLE
        } else {
            holder.state.visibility = View.GONE
        }



    }


}
