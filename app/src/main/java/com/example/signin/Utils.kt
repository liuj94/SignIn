package com.example.signin

import android.app.Activity
import android.content.Context
import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelStoreOwner
import com.dylanc.longan.toast
import com.example.signin.compress.CompressTool
import com.guoqi.actionsheet.ActionSheet
import com.hjq.permissions.OnPermissionCallback
import com.hjq.permissions.Permission
import com.hjq.permissions.XXPermissions
import com.huantansheng.easyphotos.EasyPhotos
import com.huantansheng.easyphotos.callback.SelectCallback
import com.huantansheng.easyphotos.models.album.entity.Photo
import java.io.File

fun <T : ViewModel> obtainViewModel(owner: ViewModelStoreOwner, viewModelClass: Class<T>) =
    ViewModelProvider(owner, ViewModelProvider.NewInstanceFactory()).get(viewModelClass)

//压缩单张图片
fun startCompres(context:Context,path: String, back: (file: File) -> Unit) {
    CompressTool.with(context)
        .load(path)
        .onSuccess { back.invoke(it) }
        .launch()
}

 fun startCompress(context: Context, pathList: List<Photo>, back: (file: MutableList<File>) -> Unit) {
    var listFile: MutableList<File> = ArrayList()
    for (data in pathList) {
        CompressTool.with(context)
            .load(Uri.parse(data.path).toString())
            .onSuccess {
                listFile.add(it)
                if (listFile.size == pathList.size) {
                    back.invoke(listFile)
                }
            }
            .launch()
    }

}
/**
 * 解码Unicode字符串，得到原始字符串
 *
 * @param unicode Unicode字符串
 * @return 解码后的原始字符串
 */

fun encode(unicode: String? = ""): String {
    var s:String = ""
    if(!unicode.isNullOrEmpty()){
        if(unicode.contains("\\u")){
            val builder = StringBuilder()
            val hex = unicode.split("\\\\u".toRegex()).toTypedArray()
            for (i in 1 until hex.size) {
                val data = hex[i].toInt(16)
                builder.append(data.toChar())
            }
            return builder.toString()
        }
        if(unicode.contains("%u")){
            val builder = StringBuilder()
            val hex = unicode.split("%u".toRegex()).toTypedArray()
            for (i in 1 until hex.size) {
                val data = hex[i].toInt(16)
                builder.append(data.toChar())
            }
            return builder.toString()
        }
    }

    return s
}
fun takePhotoDialog(context:Activity,
                    finish: ((file:File) -> Unit)? = null) {

    XXPermissions.with(context)
        .permission(Permission.CAMERA)
        .permission(Permission.READ_MEDIA_IMAGES)
        .request(object : OnPermissionCallback {

            override fun onGranted(permissions: MutableList<String>, all: Boolean) {
                if (all) {
                    ActionSheet.showSheet(context, { whichButton: Int ->
                        when (whichButton) {
                            ActionSheet.CHOOSE_PICTURE -> {
                                EasyPhotos.createAlbum(
                                    context,
                                    false,
                                    true,
                                    GlideEngine.getInstance()
                                )
                                    .setPuzzleMenu(false)
                                    .start(object : SelectCallback() {
                                        override fun onResult(
                                            photos: java.util.ArrayList<Photo>,
                                            isOriginal: Boolean
                                        ) {
                                            if (!photos.isNullOrEmpty()) {
                                                startCompres(
                                                    context,
                                                    photos[0].path
                                                ) { file ->
                                                    finish?.invoke(file)

                                                }

                                            }

                                        }

                                        override fun onCancel() {
                                        }
                                    })
                            }
                            ActionSheet.TAKE_PICTURE -> {
                                EasyPhotos.createCamera(context, true)
                                    .setFileProviderAuthority("com.example.signin.fileProvider")

                                    .start(object : SelectCallback() {
                                        override fun onResult(
                                            photos: java.util.ArrayList<Photo>,
                                            isOriginal: Boolean
                                        ) {
                                            if (!photos.isNullOrEmpty()) {
                                                startCompres(
                                                    context,
                                                    photos[0].path
                                                ) { file ->
                                                    finish?.invoke(file)
                                                }

                                            }

                                        }

                                        override fun onCancel() {
                                        }
                                    })

                            }

                        }
                    }, null)
                } else {
                    context.toast("获取手机权限失败")
                }

            }

            override fun onDenied(permissions: MutableList<String>, never: Boolean) {


            }
        })

}


