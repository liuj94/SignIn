package com.example.signin

import android.annotation.SuppressLint

import android.app.Application
import android.content.Context
import android.graphics.Typeface
import android.os.Build

class App :  Application() {
    companion object {
        var mApplication: Application? = null
        var isBack: Boolean = false
        open fun getInstance(): Context {
            return mApplication!!
        }

    }
    var fontPath = "fonts/textmedium.ttf"
    override fun onCreate() {
        super.onCreate()
        mApplication = this

        initUtils()


        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.O_MR1) {
            closeAndroidPDialog()
        }

        replaceSystemDefaultFont(this, fontPath)

    }
    fun replaceSystemDefaultFont(context: Context, fontPath: String) {

//這里我们修改的是MoNOSPACE,是因为我们在主题里给app设置的默认字体就是monospace，设置其他的也可以
        replaceTypefaceField("MONOSPACE", createTypeface(context, fontPath))
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