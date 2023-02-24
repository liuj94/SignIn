package com.example.signin

import android.content.Intent
import android.os.Bundle
import android.util.SparseArray
import androidx.fragment.app.Fragment


class ResultFragment : Fragment() {

    private var callbacks = SparseArray<(requestCode: Int, resultCode: Int, data: Intent?) -> Unit>()
    private var permissionCallbacks =
            SparseArray<(requestCode: Int, permissions: Array<String>, grantResults: IntArray) -> Unit>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        retainInstance = true
    }

    /******************************************************************************************************************/

    fun startActivityForResult(
            intent: Intent,
            requestCode: Int,
            callback: (requestCode: Int, resultCode: Int, data: Intent?) -> Unit
    ) {
        callbacks.put(requestCode, callback)
        startActivityForResult(intent, requestCode)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        callbacks.get(requestCode)?.invoke(requestCode, resultCode, data)
        callbacks.remove(requestCode)
    }

    /******************************************************************************************************************/

    fun requestPermissions(
            permissions: Array<out String>,
            requestCode: Int,
            callback: (requestCode: Int, permissions: Array<String>, grantResults: IntArray) -> Unit
    ) {
        permissionCallbacks.put(requestCode, callback)
        requestPermissions(permissions, requestCode)
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        permissionCallbacks.get(requestCode)?.invoke(requestCode, permissions, grantResults)
        permissionCallbacks.remove(requestCode)
    }


}