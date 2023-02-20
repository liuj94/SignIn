//package com.example.signin.mvvm.vm
//
//
//import androidx.lifecycle.LiveData
//import androidx.lifecycle.MutableLiveData
//import com.lzy.okgo.OkGo
//import com.lzy.okgo.model.Response
//import com.example.signin.base.BaseViewModel
//import com.example.signin.constants.KeySet
//import com.example.signin.helper.database.LocalDataUtils
//import com.example.signin.helper.net.RequestCallback
//import com.example.signin.mvvm.bean.*
//import com.example.signin.utils.getSelfData
//
//class MainHomeFVM : BaseViewModel() {
//
//
//
//
//
//
//    var mAdListData: MutableLiveData<MenuListData> = MutableLiveData()
//    fun getAdListDataLiveData(): LiveData<MenuListData> {
//        return mAdListData
//    }
//    fun requestIconList(url: String) {
//        var type = LocalDataUtils.getValue(KeySet.TOKEN_TYPE)
//        var token = LocalDataUtils.getValue(KeySet.API_TOKEN)
//
//        OkGo.get<MenuListData>(url)
//                .tag(this)
//                .headers(KeySet.AUTHORIZATION, "$type $token")
//                .execute(object : RequestCallback<MenuListData>() {
//                    override fun onMySuccess(data: MenuListData) {
//                        mAdListData.value = data
//
//                    }
//
//                    override fun onError(response: Response<MenuListData>?) {
//                        mAdListData.value = MenuListData()
//                    }
//                })
//    }
//
//    var mSelfData: MutableLiveData<SelfUserData> = MutableLiveData()
//    fun getSelfDataLiveData(): LiveData<SelfUserData> {
//        return mSelfData
//    }
//    fun requestSelf() {
//        getSelfData {
//            if (it != null) {
//                mSelfData.value = it
//            } else {
//                mSelfData.value = null
//            }
//        }
////
//    }
//
//
//
//
//
//
//
//}