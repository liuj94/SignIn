package com.example.signin.mvvm.vm

import androidx.fragment.app.Fragment
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData

import com.example.signin.base.BaseViewModel

import com.example.signin.fragment.HomeMainFragment
import com.example.signin.mvvm.ui.fragment.MyFragment

import kotlin.collections.ArrayList


class MainHomeAVM : BaseViewModel() {
    /**fragment列表*/
    var mFragmentLists: MutableLiveData<MutableList<Fragment>> = MutableLiveData()

    fun getFragmentListsLiveData(): LiveData<MutableList<Fragment>> {
        return mFragmentLists
    }

    var homeFragment: HomeMainFragment? = null
    var myFragment: MyFragment? = null
    fun getFragmentLists() {
        val fragmentLists: MutableList<Fragment> = ArrayList<Fragment>()
        homeFragment = HomeMainFragment()
        fragmentLists.add(homeFragment!!)
        myFragment = MyFragment()
        fragmentLists.add(myFragment!!)


        mFragmentLists.value = fragmentLists
    }


//    /**版本更新*/
//    var mAppVersionBean: MutableLiveData<AppVersionBean> = MutableLiveData()
//    fun getAppVersionBeanLiveData(): LiveData<AppVersionBean> {
//        return mAppVersionBean
//    }
//
//    fun checkUpdate() {
//        CheckUpdateUtils.getInstance().checkUpdate(mContext, object : UpdateAppContract.OnCheckCallback {
//            override fun isLatest() {
//                mAppVersionBean.value = null
//            }
//
//            override fun hasUpdate(appVersionBean: AppVersionBean) {
//                mAppVersionBean.value = appVersionBean
//            }
//
//            override fun onFail() {
//                mAppVersionBean.value = null
//            }
//
//            override fun onFinish() {
//            }
//        })
//    }
//
//
//    fun downloadAPK(info: AppVersionBean) {
//
//        progressDialog = ProgressDialog(mContext)
//        progressDialog?.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL)
//        progressDialog?.setTitle("正在下载")
//        progressDialog?.setMessage("请稍后...")
//        progressDialog?.progress = 0
//        progressDialog?.max = 100
//        progressDialog?.show()
//        progressDialog?.setCancelable(false)
//        CheckUpdateUtils.getInstance()
//            .downloadUpdateFile(mContext, info.downloadUri,
//                info.version + ".apk", object : UpdateAppContract.OnDownloadCallback {
//                    override fun onDownLoadStart() {
//                        showDownloadingDialog(null)
//                    }
//
//                    override fun onProgress(progress: Progress) {
//                        showDownloadingDialog(progress)
//                    }
//
//                    override fun onFail(msg: String) {
//                        showMessage(msg)
//                        dismissDownloadingDialog()
//                    }
//
//                    override fun onSuccess(path: String) {
//                        dismissDownloadingDialog()
//                        showDownloadFinishDialog(!info.isForceUpdate, path)
//                    }
//                })
//    }
//
//    /**
//     * 下载进度框
//     */
////    var downloadingDialog: MaterialDialog? = null
//    var progressDialog:ProgressDialog? = null
//    fun showDownloadingDialog(progress: Progress?) {
//        progress?.let {
//            if (progressDialog?.isShowing == true)
//                progressDialog?.setProgress((it.currentSize * 100 / it.totalSize).toInt())
//            return
//        }
//
//        progressDialog?.setProgress(0)
//        if (progressDialog?.isShowing == false)
//            progressDialog?.show()
//    }
//
//    fun showDownloadFinishDialog(cancelable: Boolean, path: String) {
//        var alertDialog = AlertDialog(mContext).builder()
//        alertDialog.setCancelable(false)
//        alertDialog.setTitle("更新提示")
//            .setMsg("下载完成，是否前往安装?")
//            .setPositiveButton("是", View.OnClickListener {
//                CacheUtils.clearCache(mContext, null)
//                LocalDataUtils.setValue(KeySet.AGREE, "flase")
//                LocalDataUtils.setValue(KeySet.CONFIGS_VERSION, "1")
//                CheckUpdateUtils.getInstance().installApk(mContext, path)
//            })
//        if (cancelable) {
//            alertDialog.setNegativeButton("否",
//                View.OnClickListener {
//                })
//        }
//        alertDialog.show()
//
//    }
//
//    fun dismissDownloadingDialog() {
//        if (progressDialog != null && progressDialog!!.isShowing)
//            progressDialog!!.dismiss()
//    }
//
//
//
//
//
//
//    var mAdListData: MutableLiveData<MenuListData> = MutableLiveData()
//    fun getAdListDataLiveData(): LiveData<MenuListData> {
//        return mAdListData
//    }
//
//    fun requestIconList(url: String) {
//        var type = LocalDataUtils.getValue(KeySet.TOKEN_TYPE)
//        var token = LocalDataUtils.getValue(KeySet.API_TOKEN)
//
//        OkGo.get<MenuListData>(url)
//            .tag(this)
//            .headers(KeySet.AUTHORIZATION, "$type $token")
//            .execute(object : RequestCallback<MenuListData>() {
//                override fun onMySuccess(data: MenuListData) {
//                    mAdListData.value = data
//
//                }
//
//                override fun onError(response: Response<MenuListData>?) {
//                    mAdListData.value = MenuListData()
//                }
//            })
//    }
//    var isMessage: MutableLiveData<Boolean> = MutableLiveData()
//    fun getIsMessageLiveData(): LiveData<Boolean> {
//        return isMessage
//    }
//
//    fun getUnreadMessage() {
//        var type = LocalDataUtils.getValue(KeySet.TOKEN_TYPE)
//        var token = LocalDataUtils.getValue(KeySet.API_TOKEN)
//
//        OkGo.get<Boolean>(PageRoutes.UNREADMESSAGE)
//            .tag(this)
//            .headers(KeySet.AUTHORIZATION, "$type $token")
//            .execute(object : RequestCallback<Boolean>() {
//                override fun onMySuccess(data: Boolean) {
//                    isMessage.value = data
////                    mMainMsgNum.visibility = if (data) View.GONE  else View.VISIBLE
//
//                }
//
//                override fun onError(response: Response<Boolean>?) {
////                    mMainMsgNum.visibility = View.GONE
//                    isMessage.value = true
//                    if (response!!.exception is RequestException) {
//                        if ((response!!.exception as RequestException).statusCode == 401) {
//                            clearLoginInfo()
//                            AppManager.getAppManager().startActivity(LoginActivity::class.java)
//                            return
//                        }
//
//                    }
//
//                }
//            })
//    }
}