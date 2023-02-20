package com.example.signin.mvvm.ui.activity

import android.annotation.SuppressLint
import android.content.Intent
import android.view.View
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import com.dylanc.longan.toast
import com.example.signin.R

import com.example.signin.base.BaseBindingActivity

import com.example.signin.constants.KeySet
import com.example.signin.databinding.ActivityLoginBinding
import com.example.signin.helper.AppManager
import com.example.signin.helper.database.LocalDataUtils
import com.example.signin.helper.update.AppVersionBean
import com.example.signin.helper.web.CacheUtils

import com.example.signin.mvvm.`interface`.LoginInterface
import com.example.signin.mvvm.ui.widget.UpdateDialog

import com.example.signin.mvvm.vm.LoginVM
import com.zj.singclick.SingleClick


class LoginActivity : BaseBindingActivity<ActivityLoginBinding, LoginVM>(), Runnable, LoginInterface, View.OnClickListener {

    override fun getViewModel(): Class<LoginVM> = LoginVM::class.java

    private var lastSendCodeTime: Long = 0

    private var isCodeMode = true

//    override fun initView(savedInstanceState: Bundle?) = R.layout.activity_login
    override fun initData() {
        CacheUtils.clearCache(this, null)
        mViewModel.loginInterface = this
        binding.modeSwitch.text = "验证码登录"
    binding.modeHint.text = "密码登录"
    binding.codeGroup.visibility = View.GONE
    binding.loginPassword.visibility = View.VISIBLE
    binding.loginForget.visibility = View.VISIBLE
    binding.loginPhone.setHint("请输入用户名")
    binding.loginPassword.setHint("请输入密码")
        isCodeMode = false
    binding.modeSwitch.setOnClickListener(this)
    binding.register.setOnClickListener(this)
    binding.loginSendCode.setOnClickListener(this)
    binding.login.setOnClickListener(this)
    binding.loginUserAgreementText.setOnClickListener(this)
    binding.loginPrivacyText.setOnClickListener(this)
        lastSendCodeTime = try {
            java.lang.Long.parseLong(LocalDataUtils.getValue(KeySet.VARI_CODE_TAG))
        } catch (e: Exception) {
            0
        }

    binding.loginForget.setOnClickListener(this)
        mViewModel.getLoginToken()

        mViewModel.getAppVersionBeanLiveData().observe(this, Observer {
            showUpdateDialog(it)
        })
    }
    /**
     * 显示版本更新弹窗
     */
    var isUpdatShow = false
    fun showUpdateDialog(info: AppVersionBean?) {
        if (info == null) {
            return
        }
        if (isUpdatShow) {
            return
        }
        var ignoreVersion = LocalDataUtils.getValue(KeySet.IGNORE_VERSION)
        if(!ignoreVersion.isNullOrEmpty()){
            var ignoreVersionInt :Int= ignoreVersion.replace(".","").trim().toInt()
            var versionInt :Int= info.version.replace(".","").trim().toInt()
            if(ignoreVersionInt<versionInt){
                setUpdateDialog(info)
            }
        }else{
            setUpdateDialog(info)
        }


    }

    private fun setUpdateDialog(info: AppVersionBean) {
        val dialog = UpdateDialog(this)
        dialog.setCancelableFlag(!info.isForceUpdate)
        dialog.setVersionCode("V" + info.version)
        dialog.setVersionContent(info.releaseLog)
        dialog.setOnUpdateClickListener(object : UpdateDialog.OnUpdateClickListener {
            override fun onConfirm() {
                mViewModel.downloadAPK(info)
//                getPermissions(this@LoginActivity, Permission.READ_MEDIA_IMAGES
//                    ,{mViewModel.downloadAPK(info)}
//                    ,{ mViewModel.showMessage("缺少存储权限，无法下载")})

            }

            override fun onCancel() {
                isUpdatShow = false
                LocalDataUtils.setValue(KeySet.IGNORE_VERSION, info.version.toString())
            }
        })
        dialog.show()
        isUpdatShow = true
    }

    @SingleClick(1000)
    override fun onClick(v: View) {
      when (v.id) {
          R.id.modeSwitch  ->{
              if (isCodeMode) {
                  binding.modeSwitch.text = "验证码登录"
                  binding.modeHint.text = "密码登录"
                  binding.codeGroup.visibility = View.GONE
                  binding.loginPassword.visibility = View.VISIBLE
                  binding.loginForget.visibility = View.VISIBLE
                  binding.loginPhone.setHint("请输入用户名")
                  isCodeMode = false
                  binding.loginUserAgreement.isChecked = false
              } else {
                  binding.modeSwitch.text = "密码登录"
                  binding.modeHint.text = "验证码登录"
                  binding.codeGroup.visibility = View.VISIBLE
                  binding.loginPassword.visibility = View.GONE
                  binding.loginForget.visibility = View.GONE
                  binding.loginPhone.setHint("请输入手机号")
                  binding.loginPassword.setHint("请输入密码")
                  isCodeMode = true
                  binding.loginUserAgreement.isChecked = false
              }
          }

          R.id.register  ->{
              AppManager.getAppManager().startActivity(RegisterActivity::class.java)
          }
          R.id.loginSendCode  ->{
              mViewModel.getLoginToken { mViewModel.sendCode() }
          }
          R.id.login  ->{
//              LocalDataUtils.setValue(KeySet.USERID,"2f72f3b3338d435de33308d9fdb4ede2")
//              LocalDataUtils.setValue(KeySet.AVATARURL,"https://gimg2.baidu.com/image_search/src=http%3A%2F%2Fc-ssl.duitang.com%2Fuploads%2Fblog%2F202107%2F09%2F20210709142454_dc8dc.thumb.1000_0.jpeg&refer=http%3A%2F%2Fc-ssl.duitang.com&app=2002&size=f9999,10000&q=a80&n=0&g=0n&fmt=auto?sec=1663203130&t=849906f1f8aa9167027b226efc4aa06e")
//              LocalDataUtils.setValue(KeySet.REAL_NAME,"liuj")
//              LiveStreamingUtil().initLiveStreaming(this,"2f72f3b3338d435de33308d9fdb4ede2")
//              LiveStreamingUtil().createLive(this, "f13a45b3-13a9-406d-b050-d8af3b1865fb", false)
              if (isCodeMode) mViewModel.login() else mViewModel.loginForPsw()
          }
          R.id.loginUserAgreementText  ->{
              val userAgreementIntent = Intent(this, PrivacyWebViewctivity::class.java)
              userAgreementIntent.putExtra("url", "file:///android_asset/user_agreement.html")
              userAgreementIntent.putExtra("title", "用户协议")
              startActivity(userAgreementIntent)
          }
          R.id.loginPrivacyText  ->{
              com.dylanc.longan.startActivity<PrivacyWebViewctivity>()
//              AppManager.getAppManager().startActivity(PrivacyWebViewctivity::class.java)
          }
          R.id.loginForget  ->{
//              launchActivity(Intent(this, ResetPswActivity::class.java))
              com.dylanc.longan.startActivity<ResetPswActivity>()
          }

      }
    }


    override fun isCheckAgreement(): Boolean {
        return binding.loginUserAgreement.isChecked
    }


    /*********************************************************************************************/

    override fun getPhone() = binding.loginPhone.text.toString().trim()

    override fun getCode() = binding.loginCode.text.toString().trim()

    override fun getLoginMode() = isCodeMode

    override fun getPassword() = binding.loginPassword.password().trim()

    override fun sendCoolDown() {

        binding.loginSendCode.setTextColor(ContextCompat.getColor(this, R.color.gray_4))
        lastSendCodeTime = System.currentTimeMillis()
        binding.loginSendCode.post(this@LoginActivity)
    }

    override fun setLoginEnable(enable: Boolean) {
        binding.login.isEnabled = enable
    }

    /*********************************************************************************************/
    @SuppressLint("SetTextI18n")
    override fun run() {
        val diff = (60 - (System.currentTimeMillis() - lastSendCodeTime) / 1000).toInt()
        binding.loginSendCode.let {
            if (diff > 0) {
                it.text = diff.toString() + "秒"
                it.isEnabled = false
                it.postDelayed(this, 1000)
            } else {
                it.isEnabled = true
                it.text = "发送验证码"
                it.setTextColor(ContextCompat.getColor(this, R.color.gray_2))

            }
        }
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


