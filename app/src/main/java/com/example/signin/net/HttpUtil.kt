import com.alibaba.fastjson.JSON
import com.example.signin.PageRoutes
import com.example.signin.bean.UploadData
import com.example.signin.net.RequestCallback
import com.lzy.okgo.OkGo
import com.lzy.okgo.model.Response
import com.lzy.okgo.request.base.Request
import com.tencent.mmkv.MMKV
import java.io.File
import java.util.HashMap

//package com.example.signin.utils

inline fun <reified T> String.toBeanList(): List<T> = JSON.parseArray(this, T::class.java)
inline fun <reified T> String.toBean(): T = JSON.parseObject(this, T::class.java)

fun upFile(file: File,
    success: ((data: UploadData) -> Unit)? = null,
    error: (() -> Unit)? = null,
    finish: (() -> Unit)? = null) {

    OkGo.post<UploadData>(PageRoutes.Api_upload)
        .tag(PageRoutes.Api_upload)
        .params("file",file)
//        .isMultipart(true)
//            .headers("Content-Type", "application/x-www-form-urlencoded")
        .execute(object : RequestCallback<UploadData>() {
            override fun onStart(request: Request<UploadData, out Request<Any, Request<*, *>>>) {
                super.onStart(request)

            }

            override fun onFinish() {
                finish?.invoke()
            }

            override fun onMySuccess(data: UploadData) {
                success?.invoke(data)

            }

            override fun onError(response: Response<UploadData>) {
                super.onError(response)
                error?.invoke()
            }
        })
}
fun add( avatar:String,
         userName:String,
         success: (() -> Unit)? = null,
         error: (() -> Unit)? = null,
         finish: (() -> Unit)? = null){
    val params = HashMap<String, String>()
    params["avatar"] = avatar
    params["userName"] = userName
    OkGo.put<String>(PageRoutes.Api_editUser)
        .tag(PageRoutes.Api_editUser)
        .upJson(JSON.toJSONString(params))
        .headers("Authorization", MMKV.mmkvWithID("MyDataMMKV").getString("token",""))
        .execute(object : RequestCallback<String>() {


            override fun onMySuccess(data: String?) {
                super.onMySuccess(data)
                success?.invoke()
            }

            override fun onSuccessNullData() {
                super.onSuccessNullData()
                success?.invoke()
            }

            override fun onError(response: Response<String>) {
                super.onError(response)
                error?.invoke()

            }

            override fun onFinish() {
                super.onFinish()
                finish?.invoke()
            }


        })
}

 fun sigin(
    params:String,
    success: ((String) -> Unit)? = null,
                  error: (() -> Unit)? = null,
                  finish: (() -> Unit)? = null) {

    OkGo.post<String>(PageRoutes.Api_sigin)
        .tag(PageRoutes.Api_sigin)
        .upJson(params)
        .headers("Authorization",MMKV.mmkvWithID("MyDataMMKV").getString("token",""))
        .execute(object : RequestCallback<String>() {

            override fun onMySuccess(data: String) {
                super.onMySuccess(data)
                //1成功 2重复
                success?.invoke(data)

            }

            override fun onError(response: Response<String>) {
                super.onError(response)
                error?.invoke()
            }

            override fun onFinish() {
                super.onFinish()
               finish?.invoke()
            }


        })
}