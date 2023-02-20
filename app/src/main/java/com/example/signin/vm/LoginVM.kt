package com.example.signin.mvvm.vm


import android.app.ProgressDialog
import android.view.View
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.lzy.okgo.model.Progress
import com.zcitc.baselibrary.BaseViewModel
import com.example.signin.constants.KeySet
import com.example.signin.helper.AppManager
import com.example.signin.helper.database.LocalDataUtils
import com.example.signin.helper.net.RequestException
import com.example.signin.helper.tool.Utils
import com.example.signin.helper.update.AppVersionBean
import com.example.signin.helper.update.CheckUpdateUtils
import com.example.signin.helper.update.UpdateAppContract
import com.example.signin.helper.web.CacheUtils
import com.example.signin.mvvm.`interface`.LoginInterface
import com.example.signin.mvvm.bean.LoginBean
import com.example.signin.mvvm.ui.activity.MainHomeActivity
import com.example.signin.mvvm.ui.widget.AlertDialog
import com.example.signin.mvvm.ui.widget.SelectOwnersDialog

import com.example.signin.utils.getLoginCode
import com.example.signin.utils.getLoginCodeToken
import com.example.signin.utils.getOwnerLists
import com.example.signin.utils.getSelfData
import com.example.signin.utils.initHttp
import com.example.signin.utils.loginForCode
import com.example.signin.utils.loginForPassword
import com.example.signin.utils.saveLoginInfo




class LoginVM : BaseViewModel() {
    var loginInterface: LoginInterface? = null
    private var isSendCode = false
    fun getLoginToken(next: (() -> Unit)? = null) {
        getLoginCodeToken(next) { checkUpdate() }
    }

    fun sendCode() {
        if (loginInterface == null) {
            return
        }
        if (Utils.isInputNull(mContext,loginInterface!!.getPhone(), "请先输入手机号码")) return
        if (loginInterface!!.getPhone().length != 11) {
            showMessage("请输入正确的手机号码")
            return
        }
        isShowLoading.value = true
        getLoginCode(loginInterface!!.getPhone(), {
            showMessage("发送成功")
            LocalDataUtils.setValue(KeySet.VARI_CODE_TAG, System.currentTimeMillis().toString())
            loginInterface!!.sendCoolDown()
            isSendCode = true
            isShowLoading.value = false
        }, {
            isSendCode = false
            try {
                if (it.exception is RequestException) {
                    if ((it.exception as RequestException).statusCode == 401) {
                        showMessage("服务器异常：401")
                        return@getLoginCode
                    }
                }
            } catch (e: Exception) {

            }
        }, {
            isShowLoading.value = false
        })

    }


    fun login() {
        if (loginInterface == null) {
            return
        }
        if (Utils.isInputNull(mContext,loginInterface!!.getPhone(), "请先输入手机号码")) return
        if (loginInterface!!.getPhone().length != 11) {
            showMessage("请输入正确的手机号码")
            return
        }

        if (!loginInterface!!.isCheckAgreement()) {
            showMessage("请同意用户协议及隐私政策")
            return
        }
        if (Utils.isInputNull(mContext,loginInterface!!.getCode(), "请先输入验证码")) return
        isShowLoading.value = true
        loginForCode(loginInterface!!.getPhone(), loginInterface!!.getCode(),
            {
                loginBean = it
                saveLoginInfo(mContext,loginInterface!!.getPhone(),it)
                getOwnersList()
            }, {
                if (it.exception is RequestException) {
                    if ((it.exception as RequestException).statusCode == 401) {
                        showMessage("服务器异常：401")
                        return@loginForCode
                    }
                    if ((it.exception as RequestException).statusCode == 403) {
                        showMessage((it.exception as RequestException).msg)
                        return@loginForCode
                    }

                }
            }, {
                isShowLoading.value = false
            })


    }

    fun loginForPsw() {
        if (loginInterface == null) {
            return
        }
        if (Utils.isInputNull(mContext,loginInterface!!.getPhone(), "请先输入用户名")) return

        if (Utils.isInputNull(mContext,loginInterface!!.getPassword(), "请先输入密码")) return
        if (loginInterface!!.getPassword().length < 0) {
            showMessage("密码长度不能小于6")
            return
        }
        if (!loginInterface!!.isCheckAgreement()) {
            showMessage("请同意用户协议及隐私政策")
            return
        }
        isShowLoading.value = true
        loginForPassword(loginInterface!!.getPhone(), loginInterface!!.getPassword(),
            {
                loginBean = it
                saveLoginInfo(mContext,loginInterface!!.getPhone(),it)
                getOwnersList()
            },
            {
                try {
                    if (it is RequestException) {
                        if (it.statusCode == 401) {
                            showMessage("服务器异常：401")
                            return@loginForPassword
                        }
                    }
                } catch (e: Exception) {

                }
            },
            { isShowLoading.value = false })

    }

    var loginBean: LoginBean? = null

    var selectOwnersDialog: SelectOwnersDialog? = null
    fun getOwnersList() {

        getOwnerLists({data->
            if (data != null && data.datas != null && data.datas.size > 0) {
                LocalDataUtils.delValue(KeySet.GUESTMODE)
                if (data.datas.size == 1 && data.datas[0].status.equals("3")) {

                    initHttp(data.datas[0].id)
                    LocalDataUtils.setValue(KeySet.OWNERS_ID, data.datas[0].id)
                    requestSelf()
                    AppManager.getAppManager().startActivity(MainHomeActivity::class.java)
                    killMyself()
                } else {
                    selectOwnersDialog = SelectOwnersDialog(
                        mContext, data.datas
                    ) { _, _, position ->
                        if (data.datas[position].status.equals("3")) {

                            initHttp(data.datas[position].id)
                            LocalDataUtils.setValue(KeySet.OWNERS_ID, data.datas[position].id)
                            selectOwnersDialog?.dismiss()
                            requestSelf()
                            AppManager.getAppManager().startActivity(MainHomeActivity::class.java)
                            killMyself()
                        }


                    }

                    selectOwnersDialog?.show()
                }


            } else {
                LocalDataUtils.delValue(KeySet.GUESTMODE)
                showPermissionDialog()

            }
        },{
            showMessage("获取企业出错")
        },{
            isShowLoading.value = false
        })



    }

    fun showPermissionDialog() {
//        val builder = MaterialDialog.Builder(mContext)
//        builder.title("温馨提示")
//            .content("暂无关联企业,是否选择进入游客模式")
//            .positiveText("确定")
//            .onPositive { _, _ ->
//
//                LocalDataUtils.setValue(KeySet.GUESTMODE, "1")
//                AppManager.getAppManager().startActivity(MainHomeActivity::class.java)
//                killMyself()
//            }
//            .negativeText("取消")
//            .show()
        AlertDialog(mContext).builder().setTitle("温馨提示")
            .setMsg("暂无关联企业,是否选择进入游客模式")
            .setPositiveButton("确定", View.OnClickListener {
                LocalDataUtils.setValue(KeySet.GUESTMODE, "1")
                AppManager.getAppManager().startActivity(MainHomeActivity::class.java)
                killMyself()
            })
            .setNegativeButton("取消",{

            })

    }



    /**版本更新*/
    var mAppVersionBean: MutableLiveData<AppVersionBean> = MutableLiveData()
    fun getAppVersionBeanLiveData(): LiveData<AppVersionBean> {
        return mAppVersionBean
    }

    fun checkUpdate() {
        CheckUpdateUtils.getInstance().checkUpdate(mContext, object : UpdateAppContract.OnCheckCallback {
            override fun isLatest() {
                mAppVersionBean.value = null
            }

            override fun hasUpdate(appVersionBean: AppVersionBean) {
                mAppVersionBean.value = appVersionBean
            }

            override fun onFail() {
                mAppVersionBean.value = null
            }

            override fun onFinish() {
            }
        })
    }

    var downloadingDialog:ProgressDialog?=null
    fun downloadAPK(info: AppVersionBean) {
//        downloadingDialog = MaterialDialog.Builder(mContext)
//            .title("正在下载")
//            .cancelable(false)
//            .progress(false, 100)
//            .build()
        downloadingDialog = ProgressDialog(mContext).apply {
            setProgressStyle(ProgressDialog.STYLE_HORIZONTAL)
            setTitle("正在下载")
            setMessage("请稍后...")
            progress = 0
            max = 100
            setCancelable(false)
        }
        downloadingDialog?.show()

        CheckUpdateUtils.getInstance()
            .downloadUpdateFile(mContext, info.downloadUri,
                info.version + ".apk", object : UpdateAppContract.OnDownloadCallback {
                    override fun onDownLoadStart() {
                        showDownloadingDialog(null)
                    }

                    override fun onProgress(progress: Progress) {
                        showDownloadingDialog(progress)
                    }

                    override fun onFail(msg: String) {
                        showMessage(msg)
                        dismissDownloadingDialog()
                    }

                    override fun onSuccess(path: String) {
                        dismissDownloadingDialog()
                        showDownloadFinishDialog(!info.isForceUpdate, path)
                    }
                })
    }

    /**
     * 下载进度框
     */
//    var downloadingDialog: MaterialDialog? = null
    fun showDownloadingDialog(progress: Progress?) {
        progress?.let {
            if (downloadingDialog?.isShowing == true)
                downloadingDialog?.setProgress((it.currentSize * 100 / it.totalSize).toInt())
            return
        }

        downloadingDialog?.setProgress(0)
        if (downloadingDialog?.isShowing == false)
            downloadingDialog?.show()
    }

    fun showDownloadFinishDialog(cancelable: Boolean, path: String) {
        var alertDialog = AlertDialog(mContext).builder()
        alertDialog.setCancelable(false)
        alertDialog.setTitle("更新提示")
            .setMsg("下载完成，是否前往安装?")
            .setPositiveButton("是", View.OnClickListener {
                CacheUtils.clearCache(mContext, null)
                LocalDataUtils.setValue(KeySet.AGREE, "flase")
                LocalDataUtils.setValue(KeySet.CONFIGS_VERSION, "1")
                CheckUpdateUtils.getInstance().installApk(mContext, path)
            })
        if (cancelable) {
            alertDialog.setNegativeButton("否",
                View.OnClickListener {
                })
        }
        alertDialog.show()
//        MaterialDialog.Builder(mContext)
//            .title("更新提示")
//            .content("下载完成，是否前往安装?")
//            .positiveText("是")
//            .negativeText(if (cancelable) "否" else "")
//            .onNegative { dialog, _ -> dialog.dismiss() }
//            .autoDismiss(false)
//            .cancelable(cancelable)
//            .onPositive { _, _ ->
//                CacheUtils.clearCache(mContext, null)
//                LocalDataUtils.setValue(KeySet.AGREE, "flase")
//                LocalDataUtils.setValue(KeySet.CONFIGS_VERSION, "1")
//                CheckUpdateUtils.getInstance().installApk(mContext, path)
//            }
//            .show()
    }

    fun dismissDownloadingDialog() {
        if (downloadingDialog != null && downloadingDialog!!.isShowing)
            downloadingDialog!!.dismiss()
    }

    fun requestSelf() {
        getSelfData {
            if (it != null) {
                loginBean?.userId = it.id
                loginBean?.let {data -> saveLoginInfo(mContext,loginInterface!!.getPhone(),data) }
            }
        }

    }


}