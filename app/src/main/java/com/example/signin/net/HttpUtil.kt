package com.example.signin.utils

import android.app.Activity
import android.content.Context
import android.text.TextUtils
import com.alibaba.fastjson.JSON
import com.dylanc.longan.toast
import com.lzy.okgo.OkGo
import com.lzy.okgo.model.Response
import com.example.signin.constants.Constants
import com.example.signin.constants.KeySet
import com.example.signin.constants.PageRoutes
import com.example.signin.helper.GetDeviceId
import com.example.signin.helper.database.LocalDataUtils
import com.example.signin.helper.net.JsonCallback
import com.example.signin.helper.net.RequestCallback


import com.example.signin.mvvm.bean.BannedCommentData
import com.example.signin.mvvm.bean.LiveBean
import com.example.signin.mvvm.bean.LiveDetailsData
import com.example.signin.mvvm.bean.LoginBean
import com.example.signin.mvvm.bean.OwnersModel
import com.example.signin.mvvm.bean.SelfUserData
import com.example.signin.mvvm.bean.Token
import java.util.HashMap


inline fun <reified T> String.toBeanList(): List<T> = JSON.parseArray(this, T::class.java)
inline fun <reified T> String.toBean(): T = JSON.parseObject(this, T::class.java)

fun GetRequest(
    url: String,
    success: ((data: String) -> Unit)? = null,
    error: ((throwable: Throwable) -> Unit)? = null,
    finish: (() -> Unit)? = null,
    successNullData: (() -> Unit)? = null,
) {
    val type = LocalDataUtils.getValue(KeySet.TOKEN_TYPE)
    val token = LocalDataUtils.getValue(KeySet.API_TOKEN)

    OkGo.get<String>(url)
        .tag(url)
        .headers(KeySet.AUTHORIZATION, "$type $token").execute(object : RequestCallback<String>() {


            override fun onMySuccess(data: String) {
                super.onMySuccess(data)
                success?.invoke(data)
            }

            override fun onSuccessNullData() {
                super.onSuccessNullData()
                successNullData?.invoke()
            }

            override fun onError(response: Response<String>) {
                super.onError(response)
                error?.invoke(response.exception)
            }

            override fun onFinish() {
                super.onFinish()
                finish?.invoke()
            }


        })
}

fun PutRequest(
    url: String,
    paramsStr: String,
    success: ((data: String) -> Unit)? = null,
    error: ((throwable: Throwable) -> Unit)? = null,
    finish: (() -> Unit)? = null,
    successNullData: (() -> Unit)? = null
) {
    val type = LocalDataUtils.getValue(KeySet.TOKEN_TYPE)
    val token = LocalDataUtils.getValue(KeySet.API_TOKEN)

    OkGo.put<String>(url)
        .tag(url)
        .upJson(paramsStr)
        .headers(KeySet.AUTHORIZATION, "$type $token").execute(object : RequestCallback<String>() {
            override fun onSuccessNullData() {
                super.onSuccessNullData()
                successNullData?.invoke()
            }

            override fun onMySuccess(data: String) {
                super.onMySuccess(data)
                success?.invoke(data)
            }

            override fun onError(response: Response<String>) {
                super.onError(response)
                error?.invoke(response.exception)
            }

            override fun onFinish() {
                super.onFinish()
                finish?.invoke()
            }


        })
}

fun PostRequest(
    url: String,
    paramsStr: String,
    success: ((data: String) -> Unit)? = null,
    error: ((throwable: Throwable) -> Unit)? = null,
    finish: (() -> Unit)? = null,
    successNullData: (() -> Unit)? = null
) {
    val type = LocalDataUtils.getValue(KeySet.TOKEN_TYPE)
    val token = LocalDataUtils.getValue(KeySet.API_TOKEN)

    OkGo.post<String>(url)
        .tag(url)
        .upJson(paramsStr)
        .headers(KeySet.AUTHORIZATION, "$type $token").execute(object : RequestCallback<String>() {
            override fun onSuccessNullData() {
                super.onSuccessNullData()
                successNullData?.invoke()
            }

            override fun onMySuccess(data: String) {
                super.onMySuccess(data)
                success?.invoke(data)
            }

            override fun onError(response: Response<String>) {
                super.onError(response)
                error?.invoke(response.exception)
            }

            override fun onFinish() {
                super.onFinish()
                finish?.invoke()
            }


        })
}

fun PostRequestLoginToken(
    url: String,
    paramsStr: String,
    success: ((data: String) -> Unit)? = null,
    error: ((throwable: Throwable) -> Unit)? = null,
    finish: (() -> Unit)? = null,
    successNullData: (() -> Unit)? = null
) {
    val type = LocalDataUtils.getValue(KeySet.TOKEN_TYPE)
    var token = LocalDataUtils.getValue(KeySet.LOGIN_TOKEN)
    OkGo.post<String>(url)
        .tag(url)
        .upJson(paramsStr)
        .headers(KeySet.AUTHORIZATION, "$type $token").execute(object : RequestCallback<String>() {
            override fun onSuccessNullData() {
                super.onSuccessNullData()
                successNullData?.invoke()
            }

            override fun onMySuccess(data: String) {
                super.onMySuccess(data)
                success?.invoke(data)
            }

            override fun onError(response: Response<String>) {
                super.onError(response)
                error?.invoke(response.exception)
            }

            override fun onFinish() {
                super.onFinish()
                finish?.invoke()
            }


        })
}

/**
 * 获取直播详情
 */
fun getLiveDetails(
    id: String,
    success: ((data: LiveDetailsData) -> Unit)? = null,
    finish: (() -> Unit)? = null
) {

    GetRequest(
        PageRoutes.LIVEDETAILS + id,
        {
            success?.invoke(it.toBean())
        },
        null, finish
    )


}

/**
 * 获取禁言列表
 */
fun getBannedCommentList(
    id: String,
    success: ((data: List<BannedCommentData>) -> Unit)? = null,
    finish: (() -> Unit)? = null
) {

    GetRequest(
        PageRoutes.BANNEDCOMMENTLIST + id,
        {
            success?.invoke(it.toBeanList())
        },
        null, finish
    )


}

/**
 * 禁言
 */
fun banComment(context: Context,
    liveId: String,
    userId: String,
    level: Int,
    success: (() -> Unit)? = null,
    error: ((throwable: Throwable) -> Unit)? = null,
    finish: (() -> Unit)? = null
) {
    if (LocalDataUtils.getValue("LivesBanComment").isNullOrEmpty()){
        context.toast("没有禁言权限，请联系管理员")
        return
    }
    var params = HashMap<String, Any>()
    params["liveId"] = liveId
    params["userId"] = userId
    params["level"] = level
    PutRequest(PageRoutes.BANCOMMENT, JSON.toJSONString(params),
        { success?.invoke() }, error, finish, { success?.invoke() })
}

/**
 * 取消禁言
 */
fun cancelBanComment(context: Context,
    liveId: String,
    id: String,
    success: (() -> Unit)? = null,
    error: ((throwable: Throwable) -> Unit)? = null,
    finish: (() -> Unit)? = null
) {
    if (LocalDataUtils.getValue("LivesCancelBanComment").isNullOrEmpty()){
        context.toast("没有取消禁言权限，请联系管理员")
        return
    }
    val params = HashMap<String, Any>()
    params["liveId"] = liveId
    params["id"] = id
    PutRequest(PageRoutes.CANCELBANCOMMENT, JSON.toJSONString(params),
        { success?.invoke() }, error, finish, { success?.invoke() })

}

/**
 * 批量取消禁言
 */
fun batchCancelBanComment(context: Context,
    liveId: String,
    ids: List<String>,
    success: (() -> Unit)? = null,
    error: ((throwable: Throwable) -> Unit)? = null,
    finish: (() -> Unit)? = null
) {
    if (LocalDataUtils.getValue("LivesCancelBanComment").isNullOrEmpty()){
        context.toast("没有取消禁言权限，请联系管理员")
        return
    }
    val params = HashMap<String, Any>()
    params["liveId"] = liveId
    params["ids"] = ids
    PutRequest(PageRoutes.BATCHCANCELBANCOMMENT, JSON.toJSONString(params),
        { success?.invoke() }, error, finish, { success?.invoke() })

}

/**
 * 获取用户信息
 */
fun getSelfData(success: ((data: SelfUserData?) -> Unit)? = null) {
    getSelfData(PageRoutes.SELF, success)
}

/**
 * 获取用户信息或者游客信息
 */
fun getSelfData(url: String, success: ((data: SelfUserData?) -> Unit)? = null) {
    GetRequest(url,
        { data ->
            val data: SelfUserData = data.toBean()
            data.saleAgencyPractitionerId?.let {  LocalDataUtils.setValue(KeySet.SAPID, it) }
            data.id?.let {  LocalDataUtils.setValue(KeySet.USERID, it) }
            data.realName?.let {  LocalDataUtils.setValue(KeySet.REAL_NAME, it) }
            data.avatarUrl?.let {  LocalDataUtils.setValue(KeySet.AVATARURL, it) }

            success?.invoke(data)

        },
        null, null,
        {
            LocalDataUtils.setValue(KeySet.SAPID, "")
            success?.invoke(null)
        })

}



/**
 * 获取登录验证码token
 */
fun getLoginCodeToken(
    next: (() -> Unit)? = null,
    success: (() -> Unit)? = null
) {
    OkGo.post<Token>(PageRoutes.TOKEN)
        .tag("getLoginToken")
        .headers(KeySet.AUTHORIZATION, Constants.BASIC)
        .headers(KeySet.CONTENT_TYPE, Constants.LOGIN_CONTENT_TYPE_VALUE)
//                .isSpliceUrl(true)
        .params(KeySet.GRANT_TYPE, Constants.GRANT_TYPE)
        .params(KeySet.SCOPE, Constants.SCOPE)
        .params(KeySet.CLIENT_ID, Constants.CLIENT_ID)
        .execute(object : JsonCallback<Token>(Token::class.java) {
            override fun onSuccess(response: Response<Token>) {
                LocalDataUtils.setValue(KeySet.LOGIN_TOKEN, response.body().access_token)
                LocalDataUtils.setValue(KeySet.TOKEN_TYPE, response.body().token_type)
                next?.invoke()
                success?.invoke()
            }

            override fun onError(response: Response<Token>?) {
                super.onError(response)
                LocalDataUtils.delValue(KeySet.LOGIN_TOKEN)
            }
        })
}


/**
 *获取验证码
 */
fun getLoginCode(
    phone: String,
    success: (() -> Unit)? = null,
    error: ((response: Response<Boolean>) -> Unit)? = null,
    finish: (() -> Unit)? = null
) {

    var type = LocalDataUtils.getValue(KeySet.TOKEN_TYPE)
    var token = LocalDataUtils.getValue(KeySet.LOGIN_TOKEN)

    OkGo.post<Boolean>(PageRoutes.GET_CODE)
        .tag("getLoginCode")
        .headers(KeySet.AUTHORIZATION, "$type $token")
        .isSpliceUrl(true)
        .params(KeySet.MOBILE, phone).execute(object : RequestCallback<Boolean>() {
            override fun onSuccess(response: Response<Boolean>) {
                LocalDataUtils.setValue(KeySet.VARI_CODE_TAG, System.currentTimeMillis().toString())
                success?.invoke()

            }

            override fun onError(response: Response<Boolean>) {
                error?.invoke(response)
                super.onError(response)
            }

            override fun onFinish() {
                finish?.invoke()
            }
        })
}

/**
 *验证码登录
 */
fun loginForCode(
    phone: String,
    code: String,
    success: ((data: LoginBean) -> Unit)? = null,
    error: ((response: Response<LoginBean>) -> Unit)? = null,
    finish: (() -> Unit)? = null
) {

    val params = HashMap<String, String>()
    params[KeySet.MOBILE] = phone
    params[KeySet.VERIFY_CODE] = code
    params[KeySet.DEVICE_NAME] = Constants.DEVICE
    var type = LocalDataUtils.getValue(KeySet.TOKEN_TYPE)
    var token = LocalDataUtils.getValue(KeySet.LOGIN_TOKEN)

    OkGo.post<LoginBean>(PageRoutes.LOGIN)
        .tag("loginForCode")
        .headers(KeySet.AUTHORIZATION, "$type $token")
        .upJson(JSON.toJSONString(params))
        .execute(object : RequestCallback<LoginBean>() {

            override fun onMySuccess(data: LoginBean) {
                success?.invoke(data)
            }

            override fun onError(response: Response<LoginBean>) {
                error?.invoke(response)
                super.onError(response)

            }

            override fun onFinish() {
                finish?.invoke()
            }
        })
}

/**
 *账号密码登录
 */
fun loginForPassword(
    phone: String,
    password: String,
    success: ((data: LoginBean) -> Unit)? = null,
    error: ((throwable: Throwable) -> Unit)? = null,
    finish: (() -> Unit)? = null
) {

    getLoginCodeToken(
        {
            val params = HashMap<String, String>()
            params[KeySet.USER_NAME] = phone
            params[KeySet.PASSWORDS] = password
            params[KeySet.DEVICE_NAME] = Constants.DEVICE
            PostRequestLoginToken(
                PageRoutes.LOGIN_FOR_PSW, JSON.toJSONString(params),
                {
                    success?.invoke(it.toBean())
                }, error, finish
            )


        })
}

/**
 *获取当前用户关联企业列表
 */
fun getOwnerLists(
    success: ((data: OwnersModel?) -> Unit)? = null,
    error: ((throwable: Throwable) -> Unit)? = null,
    finish: (() -> Unit)? = null
) {
    GetRequest(PageRoutes.OWNERS,
        {
            success?.invoke(it.toBean())
        },
        {
            LocalDataUtils.delValue(KeySet.GUESTMODE)
            error?.invoke(it)
        }, finish, {
            LocalDataUtils.delValue(KeySet.GUESTMODE)
            success?.invoke(null)
        })


}

/**
 * 发布直播预告
 */
fun requestPublishLive(context: Context,id: String, success: (() -> Unit)? = null) {
    val params = HashMap<String, Any>()
    params["id"] = id

    PutRequest(PageRoutes.LIVEPUBLISH, JSON.toJSONString(params),
        {
            success?.invoke()
            context.toast("发布预告成功")
        },
        {  context.toast("发布预告失败") }, null, {
            success?.invoke()
            context.toast("发布预告成功")
        })


}

/**
 * 开启直播
 */
fun requestStartLive(context: Context, success: (() -> Unit)? = null, finish: (() -> Unit)? = null) {
    val params = HashMap<String, Any>()
    params["id"] = LocalDataUtils.getValue("LivesId")

    PutRequest(PageRoutes.LIVESTART, JSON.toJSONString(params),
        { success?.invoke() },
        {  context.toast("直播开启失败") }, finish, { success?.invoke() })

}

/**
 * 停止直播
 */
fun requestStopLive() {
    val params = HashMap<String, Any>()
    params["id"] = LocalDataUtils.getValue("LivesId")
    PutRequest(PageRoutes.LIVESTOP, JSON.toJSONString(params))
}


/**
 * 直播列表获取
 */
fun getLiveList(
    page: Int,
    success: ((data: List<LiveBean>?) -> Unit)? = null,
    error: ((throwable: Throwable) -> Unit)? = null,
    finish: (() -> Unit)? = null
) {
//var t = object:TestUtil<List<LiveBean>>(){}
//   object:TestUtil<List<LiveBean>>(){
//
//   }.getClassName(PageRoutes.LIVELIST + page)

    GetRequest(PageRoutes.LIVELIST + page, {
        success?.invoke(it.toBeanList())
    }, error, finish)

}

/**
 * 新闻转发统计
 */
 fun forwardingStatistics(context: Activity, newsId:String) {
    val type = LocalDataUtils.getValue(KeySet.TOKEN_TYPE)
    var token = LocalDataUtils.getValue(KeySet.LOGIN_TOKEN)
    if (!TextUtils.isEmpty(LocalDataUtils.getValue(KeySet.API_TOKEN))) {
        token = LocalDataUtils.getValue(KeySet.API_TOKEN)
    }

    val params = HashMap<String, String?>()
    params.put("triggerTime",System.currentTimeMillis().toString())
    params.put("deviceModel", "手机厂商：" + android.os.Build.BRAND + "," + "手机型号：" + android.os.Build.MODEL + "," + "Android系统版本号：" + android.os.Build.VERSION.RELEASE)
    params.put("deviceId", GetDeviceId.getDeviceId(context))
    params.put("deviceNetwork",getNetworkStr(context))
//        params.put("newsId","5675529d9a0e4ed4b645ae6200dd9696")
    params.put("newsId",newsId)
    OkGo.post<Boolean>(PageRoutes.FORWARDING)
        .tag(context).upJson(JSON.toJSONString(params))
        .headers(KeySet.AUTHORIZATION, "$type $token")
        .execute(object : RequestCallback<Boolean>() {
            override fun onMySuccess(data: Boolean) {
            }

            override fun onError(response: Response<Boolean>?) {
                super.onError(response)
            }
        })

}




