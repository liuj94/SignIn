package com.example.signin

import android.graphics.Color
import com.alibaba.fastjson.JSON
import com.dylanc.longan.startActivity
import com.dylanc.longan.toast
import com.example.signin.base.BaseBindingActivity
import com.example.signin.base.BaseViewModel
import com.example.signin.base.StatusBarUtil
import com.example.signin.bean.CustomUpdateParser
import com.example.signin.bean.Token
import com.example.signin.bean.User
import com.example.signin.databinding.ActLoginBinding
import com.example.signin.net.RequestCallback
import com.lzy.okgo.OkGo
import com.lzy.okgo.model.Response
import com.xuexiang.xupdate.XUpdate


class LoginActivity : BaseBindingActivity<ActLoginBinding, BaseViewModel>() {
    override fun initTranslucentStatus() {
        StatusBarUtil.setTranslucentStatus(this, Color.TRANSPARENT)
        //设置状态栏字体颜色
        StatusBarUtil.setAndroidNativeLightStatusBar(this,true)
    }
    override fun getViewModel(): Class<BaseViewModel> = BaseViewModel::class.java

    override fun initData() {
        kv.remove("token")
        kv.remove("userData")
       var msg = intent.getStringExtra("msg")
        if (!msg.isNullOrEmpty()){
            toast(msg)
        }
        //15310333405
        binding.userName.setText(kv.getString("loginusername","15310333405"))
        binding.login.setOnClickListener {
            longin()
        }

//        longin()
    }
    fun longin(){
        mViewModel.isShowLoading.value = true
        val params = HashMap<String, String>()
        params["username"] = binding.userName.text.toString().trim()
        params["password"] = binding.password.text.toString().trim()
        //source=app
        params["source"] = "app"

        OkGo.post<Token>(PageRoutes.Api_login)
            .tag(PageRoutes.Api_login)
            .upJson(JSON.toJSONString(params))
            .execute(object : RequestCallback<Token>() {
                override fun onSuccessNullData() {
                    super.onSuccessNullData()

                }

                override fun onMySuccess(data: Token) {
                    super.onMySuccess(data)
                    kv.putString("token", data.token)
                    kv.putString("loginusername", binding.userName.text.toString().trim())
                    OkGo.get<User>(PageRoutes.Api_getUserInfo)
                        .tag(PageRoutes.Api_getUserInfo)
                        .headers("Authorization", data.token)
                        .execute(object : RequestCallback<User>() {
                            override fun onSuccessNullData() {
                                super.onSuccessNullData()

                            }

                            override fun onMySuccess(data: User) {
                                super.onMySuccess(data)
                                if (data.userType.equals("01") || data.userType.equals("04")) {
                                    data.name = binding.userName.text.toString().trim()
                                    data.password = binding.password.text.toString().trim()
                                    kv.putString("userData", JSON.toJSONString(data))
                                    kv.putString("userDataId", data.userId)

                                    startActivity<MainHomeActivity>()
                                    finish()
                                } else {
                                    if (data.userName.isNullOrEmpty()) {
                                        data.name = binding.userName.text.toString().trim()
                                        data.password = binding.password.text.toString().trim()
                                        kv.putString("userData", JSON.toJSONString(data))
                                        kv.putString("userDataId", data.userId)

                                        startActivity<MainHomeActivity>()
                                        finish()
//                                        MaterialDialog(this@LoginActivity).show {
//                                          customView(	//自定义弹窗
//                                                viewRes = R.layout.tc_user_add,//自定义文件
//                                                dialogWrapContent = true,	//让自定义宽度生效
//                                                scrollable = true,			//让自定义宽高生效
//                                                noVerticalPadding = true    //让自定义高度生效
//                                            ).apply {
//                                              findViewById<TextView>(R.id.add).setOnClickListener {
//                                                  add(findViewById<EditText>(R.id.userName).text.toString().trim(),JSON.toJSONString(data))
//                                              }
//                                          }
//
//                                            cancelOnTouchOutside(false)	//点击外部不消失
//                                        }

                                    } else {
//                                        com.dylanc.longan.startActivity<MainHomeActivity>("id" to liveDataList[position].id)

                                        data.name = binding.userName.text.toString().trim()
                                        data.password = binding.password.text.toString().trim()
                                        kv.putString("userData", JSON.toJSONString(data))
                                        kv.putString("userDataId", data.userId)
//                                        startActivity<MainHomeActivity>()
                                        startActivity<MainHomeActivity>()
                                        finish()

                                    }//                                        MaterialDialog(this@LoginActivity).show {
//                                          customView(	//自定义弹窗
//                                                viewRes = R.layout.tc_user_add,//自定义文件
//                                                dialogWrapContent = true,	//让自定义宽度生效
//                                                scrollable = true,			//让自定义宽高生效
//                                                noVerticalPadding = true    //让自定义高度生效
//                                            ).apply {
//                                              findViewById<TextView>(R.id.add).setOnClickListener {
//                                                  add(findViewById<EditText>(R.id.userName).text.toString().trim(),JSON.toJSONString(data))
//                                              }
//                                          }
//
//                                            cancelOnTouchOutside(false)	//点击外部不消失
//                                        }
                                }
                            }


                            override fun onFinish() {
                                super.onFinish()

                            }


                        })
                }

                override fun onError(response: Response<Token>) {
                    super.onError(response)

                    mViewModel.isShowLoading.value = false
                }

                override fun onFinish() {
                    super.onFinish()

                }


            })
    }

    fun add( userName:String,userData :String){
        val params = HashMap<String, String>()
//        params["phonenumber"] = phonenumber
        params["nickName"] = userName

        OkGo.put<String>(PageRoutes.Api_editUser)
            .tag(PageRoutes.Api_editUser)
            .upJson(JSON.toJSONString(params))
            .headers("Authorization",kv.getString("token",""))
            .execute(object : RequestCallback<String>() {

                override fun onSuccess(response: Response<String>?) {
                    super.onSuccess(response)
                    kv.putString("userData",userData)
                    startActivity<MainHomeActivity>()
                    finish()
                }

                override fun onError(response: Response<String>) {
                    super.onError(response)

                    mViewModel.isShowLoading.value = false
                }

                override fun onFinish() {
                    super.onFinish()

                }


            })
    }

    private var exitTime: Long = 0
    override fun onBackPressed() {
        if (System.currentTimeMillis() - exitTime > 2000) {
            toast("再按一次返回退出程序")
            exitTime = System.currentTimeMillis()
        } else {
            AppManager.getAppManager().killAll()
            System.exit(0);
        }
    }

    override fun onResume() {
        super.onResume()
        XUpdate.newBuild(this)
            .updateUrl(PageRoutes.Api_appVersion)
//            .promptThemeColor(ResUtils.getColor(R.color.text4c93fd))
//            .promptButtonTextColor(Color.WHITE)
//            .promptTopResId(R.mipmap.bg_update_top)
             .updateParser( CustomUpdateParser(this))
            .update();

    }
}


