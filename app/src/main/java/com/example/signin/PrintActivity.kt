package com.example.signin

import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.TextView
import com.dylanc.longan.toast
import com.example.signin.base.BaseBindingActivity
import com.example.signin.base.BaseViewModel
import com.example.signin.databinding.ActPrintBinding
import com.hjq.permissions.OnPermissionCallback
import com.hjq.permissions.Permission
import com.hjq.permissions.XXPermissions

class PrintActivity : BaseBindingActivity<ActPrintBinding, BaseViewModel>() {
    override fun getViewModel(): Class<BaseViewModel> = BaseViewModel::class.java
    var printkaiguan = false
    var printZd = false


    /**
     * 203DPI的dot = 8
     * 300DPI的dot = 12
     * 600DPI的dot = 24
     * 打印机220B是203DPI
     */
    private val printerDot = 8

    private var adapter: ArrayAdapter<String>? = null
    private var selectedDevice: String? = null
    var printUnit: PrintUnit? = null
    override fun initData() {
        printUnit = PrintUnit(this)
        printkaiguan = kv.getBoolean("printkaiguan", false)
        printZd = kv.getBoolean("printZd", false)
        if (printZd) {
            binding.kg.setImageResource(R.mipmap.kaiguank)
        } else {
            binding.kg.setImageResource(R.mipmap.kaiguanguan)
        }
        if (printkaiguan) {
            binding.gnkg.setImageResource(R.mipmap.kaiguank)
            binding.llkg.visibility = View.VISIBLE
        } else {
            binding.gnkg.setImageResource(R.mipmap.kaiguanguan)
            binding.llkg.visibility = View.GONE
        }
        binding.kg.setOnClickListener {
            var print = !printZd
            kv.putBoolean("printZd", print)
        }
        binding.gnkg.setOnClickListener {
            var print = !printkaiguan
            kv.putBoolean("printkaiguan", print)
        }
        binding.device.setAdapter(ArrayAdapter<String>(
            this, android.R.layout.simple_list_item_1, ArrayList<String>()
        ).also {
            adapter = it
        })
        binding.device.setOnItemClickListener(AdapterView.OnItemClickListener { parent: AdapterView<*>?, view: View, position: Int, id: Long ->
            val device =
                (view.findViewById<View>(android.R.id.text1) as TextView).text
                    .toString()
            selectedDevice =
                device.split("\n\n".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()[1]
            binding.selected.text = "当前连接设备:" + selectedDevice
        })
        XXPermissions.with(this@PrintActivity)
            .permission(Permission.BLUETOOTH_SCAN)
            .permission(Permission.BLUETOOTH_CONNECT)
            .permission(Permission.BLUETOOTH_ADVERTISE)
            .request(object : OnPermissionCallback {

                override fun onGranted(permissions: MutableList<String>, all: Boolean) {
                    if (all) {
                        printUnit?.PrintRegisterReceiver()
                    } else {
                        toast("获取蓝牙权限失败")
                    }

                }

                override fun onDenied(permissions: MutableList<String>, never: Boolean) {


                }
            })
        var isFrist = true
        printUnit?.setListPrinter(object : PrintUnit.ListPrinter {
            override fun printer(p: String) {
                adapter?.add(p)
               if(isFrist){
                   isFrist = false
                    selectedDevice =
                       p.split("\n\n".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray().get(1)
                   printUnit?.connectSPP(selectedDevice)
               }

            }

            override fun conPrint(p: Boolean) {
                binding.selected.text = "当前连接设备:" + selectedDevice
            }

        })
    }

    override fun onDestroy() {
        super.onDestroy()
        printUnit?.PrintUnregisterReceiver()
    }

}