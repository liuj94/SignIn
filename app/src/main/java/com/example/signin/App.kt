package com.example.signin

import android.annotation.SuppressLint

import android.app.Application
import android.content.Context
import android.os.Build

class App :  Application() {
    companion object {
        var mApplication: Application? = null
        var isBack: Boolean = false
        open fun getInstance(): Context {
            return mApplication!!
        }

    }

    override fun onCreate() {
        super.onCreate()
        mApplication = this

        initUtils()


        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.O_MR1) {
            closeAndroidPDialog()
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