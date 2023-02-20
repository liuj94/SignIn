package com.example.signin

import android.app.Activity
import android.content.Context
import android.graphics.*
import android.net.ConnectivityManager
import android.net.Uri
import android.os.Build
import android.text.SpannableStringBuilder
import android.text.Spanned
import android.text.style.AbsoluteSizeSpan
import android.util.Log
import android.view.Display
import android.view.View
import android.widget.TextView
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelStoreOwner
import com.alibaba.fastjson.JSON
import com.example.signin.utils.ACache
import com.hjq.permissions.OnPermissionCallback
import com.hjq.permissions.XXPermissions
import com.lzy.okgo.OkGo
import com.lzy.okgo.https.HttpsUtils
import com.lzy.okgo.model.HttpHeaders
import java.text.SimpleDateFormat
import java.util.Date

fun <T : ViewModel> obtainViewModel(owner: ViewModelStoreOwner, viewModelClass: Class<T>) =
    ViewModelProvider(owner, ViewModelProvider.NewInstanceFactory()).get(viewModelClass)

fun <T> parseObjectJSON(context: Context, name: String, dataClass: Class<T>) =
    JSON.parseObject(ACache.get(context).getAsString(name), dataClass::class.java)

fun saveJSON(context: Context, name: String, data: String) {
    val simpleDateFormat = SimpleDateFormat("yyyy-MM-dd")
    val date = Date(System.currentTimeMillis())
    ACache.get(context).put(name + simpleDateFormat.format(date), data, 1 * ACache.TIME_DAY)
}

fun removeJSON(context: Context, name: String) {
    val simpleDateFormat = SimpleDateFormat("yyyy-MM-dd")
    val date = Date(System.currentTimeMillis())
    ACache.get(context).remove(name + simpleDateFormat.format(date))
}

fun isExistJsonStr(context: Context, name: String): Boolean {
    val simpleDateFormat = SimpleDateFormat("yyyy-MM-dd")
    val date = Date(System.currentTimeMillis())
    if (ACache.get(context).getAsString(name + simpleDateFormat.format(date)).isNullOrEmpty()) {
        return false
    }
    return true
}

///**
// * 是否显示倒计时广告
// */
//fun isShowAd(startTimeTicks: String, endTimeTicks: String): Boolean {
//
//    var isShow = false
//    val simpleDateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
//    try {
//        val startTimeLong = if (startTimeTicks.length > 10) startTimeTicks.toLong() else (startTimeTicks + "000").toLong()
//        val endTimeLong = if (endTimeTicks.length > 10) endTimeTicks.toLong() else (endTimeTicks + "000").toLong()
//        val currentTime = simpleDateFormat.parse(simpleDateFormat.format(Date()))
//        if (currentTime.time in (startTimeLong + 1) until endTimeLong) {
//            isShow = true
//        }
//
//    } catch (e: Exception) {
//        e.printStackTrace()
//        Log.d("TAGTime", "e=$e")
//    }
//    return isShow
//}
//
///**
// * 设置TextView上下渐变
// */
//fun setTvGradientColor(text: TextView, startColorStr: String, endColorStr: String) {
//    val mLinearGradient = LinearGradient(
//        0f, 0f, 0f, text.getPaint().getTextSize(),
//        Color.parseColor(startColorStr), Color.parseColor(endColorStr), Shader.TileMode.REPEAT
//    )
//    text.paint.shader = mLinearGradient
//    text.invalidate()
//
//}
//
///**
// * 设置TextView上下渐变和大小变更
// */
//fun setTSizeAndColor(context: Context?, str: String, text: TextView, ischangeColor: Boolean) {
//
//    text.text = str
//
//    val spannable = SpannableStringBuilder(text.getText().toString())
//
//    spannable.setSpan(AbsoluteSizeSpan(dip2px(context, 33f).toInt()), 0, text.length() - 1, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
//    spannable.setSpan(AbsoluteSizeSpan(dip2px(context, 10f).toInt()), text.length() - 1, text.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
//
//    text.text = spannable
//
//    if (context == null) {
//        return
//    }
//    val face: Typeface = Typeface.createFromAsset(context!!.assets, "fonts/cf_din_cnrg.ttf")
//    text.typeface = face
//    if (ischangeColor) {
//        val mLinearGradient = LinearGradient(
//            0f, 0f, 0f, text.paint.textSize,
//            Color.parseColor("#FF05AAE6"), Color.parseColor("#FF03D6B3"), Shader.TileMode.REPEAT
//        )
//        text.paint.shader = mLinearGradient
//        text.invalidate()
//    }
//
//
//}
//
//fun setTextViewStyles(text: TextView) {
//    val mLinearGradient = LinearGradient(
//        0f, 0f, 0f, text.paint.textSize,
//        Color.parseColor("#FF05AAE6"), Color.parseColor("#FF03D6B3"), Shader.TileMode.REPEAT
//    )
//    text.paint.shader = mLinearGradient
//    text.invalidate()
//}
//
//fun getPmWidth(activity: FragmentActivity?): Int {
//    val display: Display? = activity?.windowManager?.defaultDisplay
//    var size = Point()
//    var width = 1920
//    display?.let {
//        display.getSize(size)
//        width = size.x
//    }
//
//    return width
//}
//
//fun getPmWidth(activity: Activity): Int {
//    val display: Display? = activity?.windowManager?.defaultDisplay
//    var size = Point()
//    display?.getSize(size)
//    return size.x
//}
//
//fun getPmHeight(activity: FragmentActivity?): Int {
//    val display: Display? = activity?.windowManager?.defaultDisplay
//    var size = Point()
//    display?.getSize(size)
//    return size.y
//}
//
//fun getPmHeight(activity: Activity): Int {
//    val display: Display? = activity?.windowManager?.defaultDisplay
//    var size = Point()
//    display?.getSize(size)
//    return size.y
//}
//
//fun getAliasUserId(): String {
//
//    return LocalDataUtils.getValue(KeySet.USERID)
//}
//
//fun getUserId(): String {
//
//    return LocalDataUtils.getValue(KeySet.USERID)
//}
//
//fun gotoCustomAction(context: Context, uMessage: UMessage) {
//
//    CustomActionUtils.openActivity(context, uMessage)
//}
//
//fun getMenuList(data: MenuListData): MutableList<MenusData> {
//    var menusAllList: MutableList<MenusData> = ArrayList()
//    if (!data.menus.isNullOrEmpty()) {
//        for (data in data.menus) {
//            if (!data.value.equals("AppChats")) {
//                if (!data.children.isNullOrEmpty()) {
//                    for (menuData in data.children) {
//                        //系统权限类型 0 = 未知 1 = 菜单 2 = 页面 3 = 权限值 4 = 分隔符
//                        if (menuData.type == 1 || menuData.type == 2) {
//                            menuData.parentValue = data.value
//                            menusAllList.add(menuData)
//
//                            if (!menuData.children.isNullOrEmpty()) {
//                                for (childrenData in menuData.children) {
//                                    //系统权限类型 0 = 未知 1 = 菜单 2 = 页面 3 = 权限值 4 = 分隔符
//                                    if ( childrenData.type == 2) {
//
//                                        childrenData.parentValue = data.value
//                                        menusAllList.add(childrenData)
//                                    }
//                                }
//                            }
//
//                        }
//
//
//                    }
//                }
//
//
//            }
//        }
//    }
//    return menusAllList
//}
//
//
///**
// * 是否同意隐私
// */
//fun isAgreePrivacy():Boolean{
//    val agree = LocalDataUtils.getValue(KeySet.AGREE)
//    if ("true".equals(agree)) {
//        return true
//    }
//    return false
//}
///**
// * 撤回隐私弹窗
// */
//fun withdrawPrivacyAgreement(context:Context){
//    AlertDialog(context).builder().setTitle("撤回隐私协议")
//        .setMsg("如果您撤回隐私协议，将无法体验宁波房产的全部功能。")
//        .setPositiveButton("确定撤回", View.OnClickListener {
//
//            val agreementDialog = AgreementDialog(context)
//            agreementDialog?.setOnClickListener{
//                if (it?.id == R.id.tv_dialog_no) {
//                    LocalDataUtils.setValue(KeySet.OWNERS_ID, "")
//                    val userID = getUserId()
//                    PushAgent.getInstance(context).deleteAlias(userID,
//                        PushConstants.ALIASTYPE) { isSuccess, message -> }
//                    LocalDataUtils.clearLoginInfo()
//                    RefreshTokenUtils.refreshWithOpen(context, null, null, null)
//                    TaskPool.execute("clearCache") {
//                        LocalDataUtils.setValue(KeySet.CACHE_CLEAR_TAG, "true")
//                        CacheUtils.clearCache(context, null)
////                        LocalDataUtils.setValue(KeySet.IS_USE_FINGERPRINT, "")
//                    }
//                    LocalDataUtils.setValue(KeySet.AGREE, "flase")
////                    LocalDataUtils.setValue(KeySet.INITAGREE, "flase")
//                    AppManager.getAppManager().killAll()
////                    AppManager.getAppManager().startActivity(MainBrowseActivity::class.java)
//
//                }
//
//            }
//            agreementDialog?.show()
//
//        }).setNegativeButton("取消", View.OnClickListener { }).show()
//}
// fun saveLoginInfo(data: LoginBean) {
//    LogUtils.e("浏览器保存了一个登陆token" + data.toString())
//    LocalDataUtils.saveQueryToken(data)
//    LocalDataUtils.setValue(KeySet.FACEAUTH_TIME, "0")
////    LocalDataUtils.setValue(KeySet.USERID, data.userId)
//    LocalDataUtils.setValue(KeySet.API_TOKEN, data.accessToken)
//    LocalDataUtils.setValue(KeySet.API_TOKEN_LIMIT, data.accessTokenExpiredIn.toString())
//    LocalDataUtils.setValue(KeySet.REFRESH_CODE, data.refreshCode)
//    LocalDataUtils.setValue(KeySet.REFRESH_CODE_LIMIT, data.refreshCodeExpiredIn.toString())
//
//    val obj = TokenTransformObject(data.accessToken)
//    val user = UserInfo(obj.userID())
//    LocalDataUtils.setValue(KeySet.SSOUSERID, obj.userID())
//    user.prevAvatar = obj.avatar()
//    user.realName = obj.name()
//    user.isIdentityNoVerified = obj.isIdentitynoVerified()
//    user.mobile = obj.phone()
//    LocalDataUtils.setBaseInfo(user)
//    LocalDataUtils.tokenChange()
//    RefreshTokenUtils.refreshWithLoop(FLApplication.getInstance())
//}
// fun saveLoginInfo(context:Context,phone : String, data: LoginBean) {
//    LocalDataUtils.saveQueryToken(data)
////    LocalDataUtils.setValue(KeySet.USERID, data.userId)
//    LocalDataUtils.setValue(KeySet.FACEAUTH_TIME, "0")
//    LocalDataUtils.setValue(KeySet.MOBILE, phone)
//    LocalDataUtils.setValue(KeySet.API_TOKEN, data.accessToken)
//    LocalDataUtils.setValue(KeySet.API_TOKEN_LIMIT, data.accessTokenExpiredIn.toString())
//    LocalDataUtils.setValue(KeySet.REFRESH_CODE, data.refreshCode)
//    LocalDataUtils.setValue(KeySet.REFRESH_CODE_LIMIT, data.refreshCodeExpiredIn.toString())
//    LocalDataUtils.tokenChange()
//    PushHelper.setAlias(context)
//    val obj = TokenTransformObject(data.accessToken)
//     LocalDataUtils.setValue(KeySet.SSOUSERID, obj.userID())
//    val user = UserInfo(obj.userID())
//    user.prevAvatar = obj.avatar()
//    user.realName = obj.name()
//    user.isIdentityNoVerified = obj.isIdentitynoVerified()
//    user.mobile = obj.phone()
//    LocalDataUtils.setBaseInfo(user)
//    RefreshTokenUtils.refreshWithLoop(context)
//
//}
//
//val userAgent = "Mozilla/5.0 (Windows NT 6.1) AppleWebKit/537.31 (KHTML, like Gecko) Chrome/26.0.1410.43 BIDUBrowser/6.x Safari/537.31"
// fun initHttp(OwnerId: String) {
//    val builder = OkHttpClient.Builder()
//    val sslParams1 = HttpsUtils.getSslSocketFactory()
//    builder.sslSocketFactory(sslParams1.sSLSocketFactory, sslParams1.trustManager)
//    var headers: HttpHeaders = HttpHeaders()
//    headers.put("User-Agent", userAgent)
//    headers.put("platform", "1")
//    headers.put("app-version", BuildConfig.VERSION_NAME)
//    headers.put("api-version", "1")
//    headers.put("Appid", "A44ED000-3DBF-04D8-0000-001360F6FE91")
//    headers.put("OwnerId", OwnerId)
//    headers.put("os-version", Build.VERSION.SDK_INT.toString())
//    headers.put("model", DeviceUtils.getModel())
//    headers.put("manufacturer", Build.MANUFACTURER)
//    headers.put("screen-width", com.blankj.utilcode.util.Utils.getApp().resources.displayMetrics.widthPixels.toString())
//    headers.put("screen-height", com.blankj.utilcode.util.Utils.getApp().resources.displayMetrics.heightPixels.toString())
//    OkGo.getInstance().init(FLApplication.mApplication).setOkHttpClient(builder.build())
//        .addCommonHeaders(headers)
//}
//
//fun getPermissions( context:Context,permissionStr:String,successful: () -> Unit,failure: () -> Unit){
//    XXPermissions.with(context)
//        .permission(permissionStr)
//        .request(object : OnPermissionCallback {
//
//            override fun onGranted(permissions: MutableList<String>, all: Boolean) {
//                if (!all) {
//                    failure.invoke()
//                } else {
//                    successful.invoke()
//                }
//
//            }
//
//            override fun onDenied(permissions: MutableList<String>, never: Boolean) {
//                failure.invoke()
//
//            }
//        })
//}
//fun getPermissions( context:Context,permissionStr:String,permissionStr2:String,successful: () -> Unit,failure: () -> Unit){
//    XXPermissions.with(context)
//        // 申请单个权限Manifest.permission.CAMERA
//        .permission(permissionStr)
//        .permission(permissionStr2)
//        .request(object : OnPermissionCallback {
//
//            override fun onGranted(permissions: MutableList<String>, all: Boolean) {
//                if (!all) {
//                    failure.invoke()
//                } else {
//                    successful.invoke()
//                }
//
//            }
//
//            override fun onDenied(permissions: MutableList<String>, never: Boolean) {
//                failure.invoke()
//
//            }
//        })
//}
//
//    fun setStatusBarHeight(context:Activity,toolbarView: View) {
//        val height = StatusBarUtil.getStatusBarHeight(context)
//        val display = context.windowManager.defaultDisplay
//        val layoutParams = toolbarView.layoutParams
//        layoutParams.height = height
//        layoutParams.width = display.width
//        toolbarView.layoutParams = layoutParams
//    }
///**
// * 获取当前网络状态
// */
//fun getNetworkStr(mContext: Context): String {
//    var conMann: ConnectivityManager =
//        mContext?.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager;
//    mContext?.getSystemService(Context.CONNECTIVITY_SERVICE);
//    mContext?.getSystemService(Context.CONNECTIVITY_SERVICE);
//    var mobileNetworkInfo = conMann.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
//    var wifiNetworkInfo = conMann.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
//    var device_network: String = ""
//    if (mobileNetworkInfo!!.isConnected()) {
//        device_network = "移动网络"
//    } else if (wifiNetworkInfo!!.isConnected()) {
//        device_network = "WIFI网络"
//    } else {
//        device_network = "无网络"
//    }
//    return device_network
//}
//
// fun initHttp() {
//    val builder = OkHttpClient.Builder()
//    val sslParams1 = HttpsUtils.getSslSocketFactory()
//    builder.sslSocketFactory(sslParams1.sSLSocketFactory, sslParams1.trustManager)
//    var headers: HttpHeaders = HttpHeaders()
//
//    headers.put("platform", "1")
//    headers.put("app-version", BuildConfig.VERSION_NAME)
//    headers.put("api-version", "1")
//    headers.put("Appid", "A44ED000-3DBF-04D8-0000-001360F6FE91")
//    headers.put("OwnerId", LocalDataUtils.getValue(KeySet.OWNERS_ID))
//    val agree = LocalDataUtils.getValue(KeySet.AGREE)
//    if ("true".equals(agree)) {
//        headers.put("User-Agent", userAgent)
//        headers.put("os-version", Build.VERSION.SDK_INT.toString())
//        headers.put("model", DeviceUtils.getModel())
//        headers.put("manufacturer", Build.MANUFACTURER)
//        headers.put("screen-width", Utils.getApp().resources.displayMetrics.widthPixels.toString())
//        headers.put("screen-height", Utils.getApp().resources.displayMetrics.heightPixels.toString())
//    }
//
//    OkGo.getInstance().init(FLApplication.mApplication).setOkHttpClient(builder.build())
//        .addCommonHeaders(headers)
//}
//
//fun initWebViewCache(context: Context) {
//    val builder = WebViewCacheInterceptor.Builder(context)
//    builder.setCachePath(File(context.cacheDir, "cache_path_name")) //设置缓存路径，默认getCacheDir，名称CacheWebViewCache
//        .setCacheSize(1024 * 1024 * 100.toLong()) //设置缓存大小，默认100M
//        .setConnectTimeoutSecond(20) //设置http请求链接超时，默认20秒
//        .setReadTimeoutSecond(20) //设置http请求链接读取超时，默认20秒
//    val extension = CacheExtensionConfig()
//    extension.addExtension("json").removeExtension("swf")
//    builder.setCacheExtensionConfig(extension)
//    builder.setDebug(BuildConfig.DEBUG)
//    builder.setResourceInterceptor { true }
//    WebViewCacheInterceptorInst.getInstance().init(builder)
//}
//
//
////压缩单张图片
//fun startCompres(context:Context,path: String, back: (file: File) -> Unit) {
//    CompressTool.with(context)
//        .load(path)
//        .onSuccess { back.invoke(it) }
//        .launch()
//}
///**------------------------------------------------------------------------------------------**/
////压缩图片
// fun startCompress(context: Context,path: String, back: (file: File) -> Unit) {
//    CompressTool.with(context)
//        .load(path)
//        .onSuccess { back.invoke(it) }
//        .launch()
//}
//
// fun startCompress(context: Context,pathList: List<Photo>, back: (file: MutableList<File>) -> Unit) {
//    var listFile: MutableList<File> = ArrayList()
//    for (data in pathList) {
//        CompressTool.with(context)
//            .load(Uri.parse(data.path).toString())
//            .onSuccess {
//                listFile.add(it)
//                if (listFile.size == pathList.size) {
//                    back.invoke(listFile)
//                }
//            }
//            .launch()
//    }
//
//}


