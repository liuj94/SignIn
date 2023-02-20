package com.zcitc.baselibrary

import android.annotation.SuppressLint
import android.app.Activity
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.dylanc.longan.toast
import java.text.SimpleDateFormat
import java.util.*

open class BaseViewModel : ViewModel() {
    lateinit var mContext : Activity

    var isShowLoading: MutableLiveData<Boolean> = MutableLiveData()
    fun isShowLoadingLiveData(): LiveData<Boolean> {
        return isShowLoading
    }

    fun killMyself(){
        if(mContext!=null){
            mContext.finish()
        }
    }
        fun showMessage(message : String){
        if(mContext!=null){
            mContext.toast( message)
        }

    }
    @SuppressLint("SimpleDateFormat")
    fun getDataString() : String {
        val simpleDateFormat = SimpleDateFormat("yyyy-MM-dd")
        val date = Date(System.currentTimeMillis())
        return simpleDateFormat.format(date)
    }
}