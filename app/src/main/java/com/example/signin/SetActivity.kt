package com.example.signin

import android.os.Build
import android.provider.Settings
import androidx.annotation.RequiresApi
import com.dylanc.longan.activity
import com.dylanc.longan.toast
import com.example.signin.base.BaseBindingActivity
import com.example.signin.base.BaseViewModel
import com.example.signin.databinding.ActivitySetBinding
import com.hjq.permissions.OnPermissionCallback
import com.hjq.permissions.Permission
import com.hjq.permissions.XXPermissions

class SetActivity : BaseBindingActivity<ActivitySetBinding, BaseViewModel>() {
    override fun getViewModel(): Class<BaseViewModel> = BaseViewModel::class.java
    var startApp = "1"
    @RequiresApi(Build.VERSION_CODES.M)
    override fun initData() {
        if(Build.VERSION.SDK_INT < Build.VERSION_CODES.GINGERBREAD){
            if(Settings.canDrawOverlays(this)){
                if (kv.getString("startApp", "2").equals("1")) {
                    startApp = "1"
                    binding.kg.setImageResource(R.mipmap.kaiguank)
                } else {
                    startApp = "2"
                    binding.kg.setImageResource(R.mipmap.kaiguanguan)
                }
            }else{
                startApp = "2"
                binding.kg.setImageResource(R.mipmap.kaiguanguan)
            }


            binding.kg.setOnClickListener {
                if(startApp.equals("1")){
                    startApp = "2"
                    binding.kg.setImageResource(R.mipmap.kaiguanguan)
                }else {
                    if (!Settings.canDrawOverlays(this)) {
                        XXPermissions.with(activity)
                            .permission(Permission.SYSTEM_ALERT_WINDOW)
                            .request(object : OnPermissionCallback {

                                override fun onGranted(permissions: MutableList<String>, all: Boolean) {
                                    if (!all) {
                                        toast("获取权限失败")
                                        startApp = "2"
                                        binding.kg.setImageResource(R.mipmap.kaiguanguan)
                                    } else {
                                        startApp = "1"
                                        binding.kg.setImageResource(R.mipmap.kaiguank)
                                    }
                                    kv.putString("startApp", startApp)
                                }

                                override fun onDenied(permissions: MutableList<String>, never: Boolean) {
                                    toast("获取权限失败")
                                    startApp = "2"
                                    binding.kg.setImageResource(R.mipmap.kaiguanguan)
                                    kv.putString("startApp", startApp)
                                }
                            })
                    }else{
                        startApp = "1"
                        binding.kg.setImageResource(R.mipmap.kaiguank)
                    }
//                if (!Settings.canDrawOverlays(this)) {
//                    //若未授权则请求权限
//                    var intent =  Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION);
//                    intent.setData(Uri.parse("package:" + getPackageName()));
//                    startActivityForResult(intent, 0);
//                    startApp = "1"
//                    binding.kg.setImageResource(R.mipmap.kaiguank)
//                }else{
//                    startApp = "1"
//                    binding.kg.setImageResource(R.mipmap.kaiguank)
//                    PermissionPageManager(this@SetActivity).jumpPermissionPage()
//                }
//            }
//            kv.putString("startApp",startApp)
                }
            }
        }else{
            if (kv.getString("startApp", "2").equals("1")) {
                startApp = "1"
                binding.kg.setImageResource(R.mipmap.kaiguank)
            } else {
                startApp = "2"
                binding.kg.setImageResource(R.mipmap.kaiguanguan)
            }

            binding.kg.setOnClickListener {
                if(startApp.equals("1")){
                    startApp = "2"
                    binding.kg.setImageResource(R.mipmap.kaiguanguan)
                }else {
                    startApp = "1"
                    binding.kg.setImageResource(R.mipmap.kaiguank)
                }
            }
        }

    }




}