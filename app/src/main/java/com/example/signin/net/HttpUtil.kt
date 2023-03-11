import com.alibaba.fastjson.JSON
import com.example.signin.PageRoutes
import com.example.signin.bean.*
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

fun upFile(
    file: File,
    success: ((data: UploadData) -> Unit)? = null,
    error: (() -> Unit)? = null,
    finish: (() -> Unit)? = null
) {

    OkGo.post<UploadData>(PageRoutes.Api_upload)
        .tag(PageRoutes.Api_upload)
        .params("file", file)
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

fun add(
    avatar: String,
    success: (() -> Unit)? = null,
    error: (() -> Unit)? = null,
    finish: (() -> Unit)? = null
) {
    val params = HashMap<String, String>()
    params["avatar"] = avatar
    OkGo.put<String>(PageRoutes.Api_editUser)
        .tag(PageRoutes.Api_editUser)
        .upJson(JSON.toJSONString(params))
        .headers("Authorization", MMKV.mmkvWithID("MyDataMMKV").getString("token", ""))
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
    params: String,
    success: ((String) -> Unit)? = null,
    error: (() -> Unit)? = null,
    finish: (() -> Unit)? = null
) {

    OkGo.post<String>(PageRoutes.Api_sigin)
        .tag(PageRoutes.Api_sigin)
        .upJson(params)
        .headers("Authorization", MMKV.mmkvWithID("MyDataMMKV").getString("token", ""))
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
fun sigin2(
    params: String,
    success: ((String) -> Unit)? = null,
    error: ((String) -> Unit)? = null,
    finish: (() -> Unit)? = null
) {

    OkGo.post<String>(PageRoutes.Api_sigin)
        .tag(PageRoutes.Api_sigin)
        .upJson(params)
        .headers("Authorization", MMKV.mmkvWithID("MyDataMMKV").getString("token", ""))
        .execute(object : RequestCallback<String>() {

            override fun onMySuccess(data: String) {
                super.onMySuccess(data)
                //1成功 2重复
                success?.invoke(data)

            }

            override fun onError(response: Response<String>) {
                super.onError(response)
                error?.invoke(""+response.exception.message)
            }

            override fun onFinish() {
                super.onFinish()
                finish?.invoke()
            }


        })
}
fun getDataType(type: String, finish: (() -> Unit)? = null) {

    OkGo.get<List<TypeData>>(PageRoutes.Api_datatype + type)
        .tag(PageRoutes.Api_datatype)
        .execute(object : RequestCallback<List<TypeData>>() {
            override fun onMySuccess(data: List<TypeData>?) {
                super.onMySuccess(data)
                var model: TypeModel
                var kv = MMKV.mmkvWithID("MyDataMMKV")
                if (kv.getString("TypeModel", "").isNullOrEmpty()) {
                    model = TypeModel()
                } else {
                    model = JSON.parseObject(kv.getString("TypeModel", ""), TypeModel::class.java)
                }
                when (type) {
                    "sys_zhuce" -> {
                        model.sys_zhuce = data
                    }
                    "sys_ruzhu" -> {
                        model.sys_ruzhu = data
                    }
                    "sys_huichang" -> {
                        model.sys_huichang = data
                    }
                    "sys_laicheng" -> {
                        model.sys_laicheng = data
                    }
                    "sys_liping" -> {
                        model.sys_liping = data
                    }
                    "sys_fancheng" -> {
                        model.sys_fancheng = data
                    }
                    "sys_canyin" -> {
                        model.sys_canyin = data
                    }
                    "user_meeting_sign_up_status" -> {
                        model.user_meeting_sign_up_status = data
                    }
                    "user_meeting_type" -> {
                        model.user_meeting_type = data
                    }

                    "sys_invoice_status" -> {
                        model.sys_invoice_status = data
                    }
                    "sys_invoice_type" -> {
                        model.sys_invoice_type = data
                    }
                    "transport_type" -> {
                        model.transport_type = data
                    }
                    "sys_examine_reason" -> {
                        model.sys_examine_reason = data
                    }

                    "pay_status" -> {
                        model.pay_status = data
                    }
                    "user_type" -> {
                        model.user_type = data
                    }

                }
                kv.putString("TypeModel", JSON.toJSONString(model))

            }


            override fun onError(response: Response<List<TypeData>>) {
                super.onError(response)


            }

            override fun onFinish() {
                super.onFinish()
                finish?.invoke()
            }


        })



}
fun getUserInfo(success: (() -> Unit)? = null) {
    OkGo.get<User>(PageRoutes.Api_getUserInfo)
        .tag(PageRoutes.Api_getUserInfo)
        .headers("Authorization", MMKV.mmkvWithID("MyDataMMKV").getString("token", ""))
        .execute(object : RequestCallback<User>() {
            override fun onSuccessNullData() {
                super.onSuccessNullData()

            }

            override fun onMySuccess(data: User) {
                super.onMySuccess(data)
                MMKV.mmkvWithID("MyDataMMKV")
                    .putString("userData", JSON.toJSONString(data))
                success?.invoke()
            }


            override fun onFinish() {
                super.onFinish()

            }


        })
}

fun detect(img:String,success: (() -> Unit)? = null,error: (() -> Unit)? = null) {
    OkGo.get<String>(PageRoutes.Api_detect+img)
        .tag(PageRoutes.Api_detect)
//        .headers("Authorization", MMKV.mmkvWithID("MyDataMMKV").getString("token", ""))
        .execute(object : RequestCallback<String>() {
            override fun onSuccessNullData() {
                super.onSuccessNullData()
                success?.invoke()
            }

            override fun onMySuccess(data: String) {
                super.onMySuccess(data)

                success?.invoke()

            }

            override fun onError(response: Response<String>?) {
//                super.onError(response)
                error?.invoke()
            }

            override fun onFinish() {
                super.onFinish()

            }


        })
}

fun search(img:String,success: ((data: FaceData) -> Unit)? = null,error: (() -> Unit)? = null,finish: (() -> Unit)? = null) {
    OkGo.get<FaceData>(PageRoutes.Api_search+img)
        .tag(PageRoutes.Api_search)

        .execute(object : RequestCallback<FaceData>() {
            override fun onSuccessNullData() {
                super.onSuccessNullData()
                error?.invoke()
            }

            override fun onMySuccess(data: FaceData) {
                super.onMySuccess(data)

                success?.invoke(data)
            }

            override fun onError(response: Response<FaceData>?) {
                super.onError(response)
                error?.invoke()

            }

            override fun onFinish() {
                super.onFinish()
                finish?.invoke()
            }


        })
}