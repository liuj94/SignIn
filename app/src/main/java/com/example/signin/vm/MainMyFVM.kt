package com.example.signin.mvvm.vm


import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.alibaba.fastjson.JSON
import com.lzy.okgo.OkGo
import com.lzy.okgo.model.Response
import com.lzy.okgo.request.base.Request
import com.zcitc.baselibrary.BaseViewModel
import com.example.signin.constants.KeySet
import com.example.signin.constants.PageRoutes
import com.example.signin.helper.database.LocalDataUtils
import com.example.signin.helper.net.RequestCallback
import com.example.signin.mvvm.bean.*
import com.example.signin.utils.getSelfData
import java.io.File
import java.util.HashMap

class MainMyFVM : BaseViewModel() {


    var mSelfData: MutableLiveData<SelfUserData> = MutableLiveData()
    fun getSelfDataLiveData(): LiveData<SelfUserData> {
        return mSelfData
    }

    fun requestSelf(url: String) {
        getSelfData(url) {
            if (it != null) {
                mSelfData.value = it
            } else {
                mSelfData.value = null
            }
        }
    }

    var mAdListData: MutableLiveData<MenuListData> = MutableLiveData()
    fun getAdListDataLiveData(): LiveData<MenuListData> {
        return mAdListData
    }

    fun requestIconList(url: String) {
        var type = LocalDataUtils.getValue(KeySet.TOKEN_TYPE)
        var token = LocalDataUtils.getValue(KeySet.API_TOKEN)

        OkGo.get<MenuListData>(url)
            .tag(this)
            .headers(KeySet.AUTHORIZATION, "$type $token")
            .execute(object : RequestCallback<MenuListData>() {
                override fun onMySuccess(data: MenuListData) {
                    mAdListData.value = data

                }

                override fun onError(response: Response<MenuListData>?) {
                    mAdListData.value = MenuListData()
                }
            })
    }

    fun uploadFile(file: File) {
        var type = LocalDataUtils.getValue(KeySet.TOKEN_TYPE)
//        var apiToken = LocalDataUtils.getValue(KeySet.LOGIN_TOKEN)
        var apiToken = LocalDataUtils.getValue(KeySet.API_TOKEN)

        OkGo.post<List<String>>(PageRoutes.UPLOADFILE)
            .tag(this)
            .params("file", file)
            .isMultipart(true)
            .headers(KeySet.AUTHORIZATION, "$type $apiToken")
            .execute(object : RequestCallback<List<String>>() {
                override fun onStart(request: Request<List<String>, out Request<Any, Request<*, *>>>) {
                    super.onStart(request)
                    isShowLoading.value = true

                }

                override fun onFinish() {
                    isShowLoading.value = false

                }

                override fun onMySuccess(data: List<String>) {

                    if (!data.isNullOrEmpty()) {
                        submitAvatar(data[0])
                    }

                }

                override fun onError(response: Response<List<String>>) {
                    super.onError(response)
                }
            })


    }

    fun submitAvatar(avatarUrl: String) {
        var type = LocalDataUtils.getValue(KeySet.TOKEN_TYPE)
        var apiToken = LocalDataUtils.getValue(KeySet.API_TOKEN)

        val params: HashMap<String, Any> = HashMap()
        params["avatarUrl"] = avatarUrl
        OkGo.post<String>(PageRoutes.SELFAVATAR)
            .tag(this)
            .upJson(JSON.toJSONString(params))
            .headers(KeySet.AUTHORIZATION, "$type $apiToken")
            .execute(object : RequestCallback<String>() {
                override fun onStart(request: Request<String, out Request<Any, Request<*, *>>>?) {
                    super.onStart(request)
                    isShowLoading.value = true
                }

                override fun onSuccess(response: Response<String>?) {
                    super.onSuccess(response)
                    LocalDataUtils.setValue(KeySet.AVATARURL, avatarUrl)
                }

                override fun onFinish() {
                    isShowLoading.value = false

                }


            })
    }
}