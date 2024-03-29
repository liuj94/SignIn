package com.example.signin

import android.annotation.SuppressLint
import android.app.Application
import android.content.Context
import android.graphics.Typeface
import android.os.Build
import android.util.Log
import com.common.apiutil.util.SDKUtil
import com.common.apiutil.util.SystemUtil
import com.dylanc.longan.toast
import com.example.signin.agora.GlobalSettings
import com.tencent.bugly.crashreport.CrashReport
import com.xuexiang.xupdate.XUpdate
import com.xuexiang.xupdate.entity.UpdateError.ERROR.CHECK_NO_NEW_VERSION
import com.xuexiang.xupdate.utils.UpdateUtils


class App :  Application() {
    companion object {
        var mApplication: Application? = null

        open fun getInstance(): Context {
            return mApplication!!
        }
        private var globalSettings: GlobalSettings? = null
        open  fun getGlobalSettings(): GlobalSettings? {
            if (globalSettings == null) {
                globalSettings = GlobalSettings()
            }
            return globalSettings
        }
    }

    var fontPathc = "fonts/zitic.ttf"
    override fun onCreate() {
        super.onCreate()
        mApplication = this
        AppManager.getAppManager().init(mApplication)
        SDKUtil.getInstance(this).initSDK()
//        CTPL.getInstance().init(getInstance(), RespCallback())
        initUtils()
        if (!SystemUtil.checkPackage("com.common.service")) {
            Log.d("tagg", "API 调用 >> 系统反射")
        } else {
            Log.d("tagg", "API 调用 >> 服务APK")
        }

        CrashReport.initCrashReport(this, "361d06bc84", false)

        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.O_MR1) {
            closeAndroidPDialog()
        }

        replaceSystemDefaultFont(this, fontPathc)


        XUpdate.get()
            .debug(true)
            .isWifiOnly(true) //默认设置只在wifi下检查版本更新
            .isGet(true) //默认设置使用get请求检查版本
            .isAutoMode(false) //默认设置非自动模式，可根据具体使用配置
            .param("versionCode", UpdateUtils.getVersionCode(this)) //设置默认公共请求参数
            .param("appKey", packageName)
            .setOnUpdateFailureListener { error ->

                //设置版本更新出错的监听
                if (error.code != CHECK_NO_NEW_VERSION) {          //对不同错误进行处理
                    toast(error.toString())
                }
            }
            .supportSilentInstall(true) //设置是否支持静默安装，默认是true
            .setIUpdateHttpService(OKHttpUpdateHttpService()) //这个必须设置！实现网络请求功能。
            .init(this)


    }
    fun replaceSystemDefaultFont(context: Context, fontPath: String) {

//這里我们修改的是MoNOSPACE,是因为我们在主题里给app设置的默认字体就是monospace，设置其他的也可以
//        replaceTypefaceField("MONOSPACE", createTypeface(context, fontPath))
        replaceTypefaceField("MONOSPACE", createTypeface(context, fontPathc))
    }

    //通过字体地址创建自定义字体
    private fun createTypeface(context: Context, fontPath: String): Typeface {
        return Typeface.createFromAsset(context.assets, fontPath)
    }

    //关键--》通过修改MONOSPACE字体为自定义的字体达到修改app默认字体的目的
    private fun replaceTypefaceField(fieldName: String, value: Any) {
        try {
            val defaultField = Typeface::class.java.getDeclaredField(fieldName)
            defaultField.isAccessible = true
            defaultField[null] = value
        } catch (e: NoSuchFieldException) {
            e.printStackTrace()
        } catch (e: IllegalAccessException) {
            e.printStackTrace()
        }
    }







    private fun initUtils() {




    }


    @SuppressLint("SoonBlockedPrivateApi")
    private fun closeAndroidPDialog() {
        try {
            val aClass = Class.forName("android.content.pm.PackageParser\$Package")
            val declaredConstructor = aClass.getDeclaredConstructor(String::class.java)
            declaredConstructor.isAccessible = true
        } catch (e: Exception) {
            e.printStackTrace()
        }
        try {
            val cls = Class.forName("android.app.ActivityThread")
            val declaredMethod = cls.getDeclaredMethod("currentActivityThread")
            declaredMethod.isAccessible = true
            val activityThread = declaredMethod.invoke(null)
            val mHiddenApiWarningShown = cls.getDeclaredField("mHiddenApiWarningShown")
            mHiddenApiWarningShown.isAccessible = true
            mHiddenApiWarningShown.setBoolean(activityThread, true)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }



}