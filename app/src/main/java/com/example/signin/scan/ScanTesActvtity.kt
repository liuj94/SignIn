package com.example.signin.scan

import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.signin.R
import com.hello.scan.ScanCallBack
import com.hello.scan.ScanTool
import java.util.*

class ScanTestActvtity : AppCompatActivity(), ScanCallBack {
    private var scanCount = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_scan_tes_actvtity)
        if (!initScanTool()) {
            Toast.makeText(this, "该机型还没适配", Toast.LENGTH_SHORT).show()
        } else {
            ScanTool.GET.playSound(true)
        }

    }

    /**
     * 判断使用模式
     *
     * @return 返回true表示表示该机型已经适配，false表示该机型还没有适配
     */
    private fun initScanTool(): Boolean {
        ScanTool.GET.initSerial(this, "/dev/ttyACM0", 115200, this@ScanTestActvtity)
        return true
    }

    override fun onScanCallBack(data: String) {
        if (TextUtils.isEmpty(data)) return
        Log.e("Hello", "回调数据 == > $data")

        findViewById<TextView>(R.id.scan_result).setText(String.format(Locale.CHINA, "扫码结果：\n\n%s", data))

    }

    override fun onInitScan(isSuccess: Boolean) {
        val str = if (isSuccess) "初始化成功" else "初始化失败"
//        Toast.makeText(this, str, Toast.LENGTH_SHORT).show()
    }

    override fun onDestroy() {
        super.onDestroy()
        ScanTool.GET.release()
    }

//    companion object {
//        private var sDevices: MutableMap<String, Pair<String, Int>>? = null
//
//        init {
//            /*
//         * 支持设备列表，若是没在支持列表数据机型，在已知串口号和波特率的情况下，可以直接传数据
//         */
//            sDevices = HashMap()
//            sDevices["TPS508"] =
//                Pair("/dev/ttyACM0", 115200)
//            sDevices["TPS360"] =
//                Pair("/dev/ttyACM0", 115200)
//            sDevices["TPS537"] =
//                Pair("/dev/ttyACM0", 115200)
//            sDevices["F2"] =
//                Pair("/dev/ttyACM0", 115200)
//            sDevices["D2"] =
//                Pair("/dev/ttyHSL0", 115200) // D2串口模式
//            //sDevices.put("D2", new Pair<>("/dev/ttyACM0", 115200)); // D2U转串模式
//            sDevices["D2M"] =
//                Pair("/dev/ttyHSL0", 115200) //串口模式
//            sDevices["TPS980"] =
//                Pair("/dev/ttyS0", 115200)
//            sDevices["TPS980P"] =
//                Pair("/dev/ttyS0", 115200)
//            //sDevices.put("TPS980P", new Pair<>("/dev/ttyS0", 9600));
//            sDevices["TPS530"] =
//                Pair("/dev/ttyUSB0", 115200)
//            sDevices["TPS580P"] =
//                Pair("/dev/ttyHSL0", 115200)
//        }
//    }
}