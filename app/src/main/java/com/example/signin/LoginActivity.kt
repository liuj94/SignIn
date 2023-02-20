package com.example.signin




class LoginActivity : BaseActivity<ActivityLoginBinding, BaseViewModel>(){

    override fun getViewModel(): Class<BaseViewModel> = BaseViewModel::class.java

    private var lastSendCodeTime: Long = 0

    private var isCodeMode = true

//    override fun initView(savedInstanceState: Bundle?) = R.layout.activity_login
    override fun initData() {

    }
    private var exitTime: Long = 0
    override fun onBackPressed() {
        if (System.currentTimeMillis() - exitTime > 2000) {
            toast( "再按一次返回退出程序")
            exitTime = System.currentTimeMillis()
        } else {
            LocalDataUtils.delValue(KeySet.IS_VERIFICATION_FINGERPRINT)
            AppManager.getAppManager().appExit()
        }
    }

}


