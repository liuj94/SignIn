//package com.example.signin
//
//import android.Manifest
//import android.bluetooth.BluetoothAdapter
//import android.bluetooth.BluetoothClass
//import android.bluetooth.BluetoothDevice
//import android.bluetooth.BluetoothSocket
//import android.content.BroadcastReceiver
//import android.content.Context
//import android.content.Intent
//import android.content.IntentFilter
//import android.content.pm.PackageManager
//import android.graphics.Bitmap
//import android.graphics.BitmapFactory
//import android.graphics.Canvas
//import android.graphics.Color
//import android.graphics.ColorMatrix
//import android.graphics.ColorMatrixColorFilter
//import android.graphics.Paint
//import android.graphics.Rect
//import android.location.LocationManager
//import android.os.Bundle
//import android.os.Handler
//import android.os.Looper
//import android.os.Message
//import android.os.Parcelable
//import android.os.Process
//import android.text.TextUtils
//import android.util.Log
//import android.view.View
//import android.widget.AdapterView
//import android.widget.ArrayAdapter
//import android.widget.ListView
//import android.widget.TextView
//import android.widget.Toast
//import androidx.annotation.IntRange
//import androidx.appcompat.app.AppCompatActivity
//import java.io.ByteArrayOutputStream
//import java.io.IOException
//import java.nio.charset.StandardCharsets
//import java.util.UUID
//
////import androidx.appcompat.app.AppCompatActivity
////import android.os.Bundle
////
////class MainActivity : AppCompatActivity() {
////    override fun onCreate(savedInstanceState: Bundle?) {
////        super.onCreate(savedInstanceState)
////        setContentView(R.layout.activity_main2)
////    }
////}
//class MainActivity : AppCompatActivity() {
//    private val SPP_UUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB")
//
//    @Volatile
//    private var sppSocket: BluetoothSocket? = null
//    private var connStatus = -1
//    private var receiver: BroadcastReceiver? = null
//    private val mainHandler: Handler = object : Handler(Looper.getMainLooper()) {
//        override fun handleMessage(msg: Message) {
//            super.handleMessage(msg)
//            val toastStr = if (msg.what == CONN_SUCC) "连接成功" else "连接失败"
//            Toast.makeText(this@MainActivity, toastStr, Toast.LENGTH_SHORT).show()
//        }
//    }
//    private var adapter: ArrayAdapter<*>? = null
//    private var selectedDevice: String? = null
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        setContentView(R.layout.activity_main2)
//        val lv = findViewById<ListView>(R.id.device)
//        lv.adapter = ArrayAdapter(
//            this, android.R.layout.simple_list_item_1, ArrayList<String>()
//        ).also {
//            adapter = it
//        }
//        lv.onItemClickListener =
//            AdapterView.OnItemClickListener { parent: AdapterView<*>?, view: View, position: Int, id: Long ->
//                val device =
//                    (view.findViewById<View>(android.R.id.text1) as TextView).text
//                        .toString()
//                selectedDevice =
//                    device.split("\n\n".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()[1]
//                val tv = findViewById<TextView>(R.id.selected)
//                tv.text = "选中设备:" + selectedDevice
//            }
//        findViewById<View>(android.R.id.content).postDelayed({
//            val intentFilter = IntentFilter()
//            intentFilter.addAction(BluetoothAdapter.ACTION_STATE_CHANGED) //蓝牙开关
//            intentFilter.addAction(BluetoothDevice.ACTION_BOND_STATE_CHANGED) //蓝牙连接状态
//            intentFilter.addAction(BluetoothDevice.ACTION_ACL_DISCONNECTED) //蓝牙连接状态
//            intentFilter.addAction(BluetoothAdapter.ACTION_DISCOVERY_STARTED) //搜索设备
//            intentFilter.addAction(BluetoothDevice.ACTION_FOUND)
//            intentFilter.addAction(BluetoothAdapter.ACTION_DISCOVERY_FINISHED)
//            registerReceiver(object : BroadcastReceiver() {
//                override fun onReceive(context: Context, intent: Intent) { //此处处理逻辑
//                    val action = intent.action
//                    when (action) {
//                        BluetoothAdapter.ACTION_DISCOVERY_STARTED -> {
//                            Log.d("123456", "ACTION_DISCOVERY_STARTED")
//                            adapter!!.clear()
//                            selectedDevice = null
//                            val tv = findViewById<TextView>(R.id.selected)
//                            tv.text = "选中设备:"
//                        }
//
//                        BluetoothDevice.ACTION_FOUND -> {
//                            val parcelable =
//                                intent.getParcelableExtra<Parcelable>(BluetoothDevice.EXTRA_DEVICE) //得到该设备
//                            if (parcelable !is BluetoothDevice) return
//                            val device = parcelable as BluetoothDevice
//                            Log.d(
//                                "123456",
//                                "ACTION_FOUND device addr =" + device.address
//                            )
//                            val isPrinter =
//                                BluetoothClass.Device.Major.IMAGING == device.bluetoothClass
//                                    .majorDeviceClass
//                            if (isPrinter && !TextUtils.isEmpty(device.name) && device.name
//                                    .startsWith("CT")
//                            ) {
//                                //系统搜索会有重复的对象,需要自行过滤
////                                device.getName() + "\n\n" + device.getAddress()
//                                adapter!!.add(""+device.getName() + "\n\n" + device.getAddress())
//                            }
//                        }
//                    }
//                }
//            }.also { receiver = it }, intentFilter)
//        }, 1000L)
//    }
//
//    @Throws(Exception::class)
//    fun onViewClick(view: View) {
//        if (view.id == R.id.conn) {
//            var needDelay = false
//            if (sppSocket != null && sppSocket!!.isConnected) {
//                sppSocket!!.close() //会触发Receiver断开连接回调
//                sppSocket = null
//                needDelay = true
//            }
//            if (!hasPermission(this)) {
//                requestPermissions(
//                    arrayOf(
//                        Manifest.permission.BLUETOOTH,
//                        Manifest.permission.BLUETOOTH_ADMIN,
//                        Manifest.permission.ACCESS_COARSE_LOCATION,
//                        Manifest.permission.ACCESS_FINE_LOCATION
//                    ), 0
//                )
//                return
//            }
//            if (!BluetoothAdapter.getDefaultAdapter().isEnabled) {
//                Log.d("123456", "BluetoothAdapter is disabled")
//                return
//            }
//            if (selectedDevice == null || adapter!!.count == 0) {
//                searchDeviceSPP()
//                return
//            }
//            if (!needDelay) {
//                connectSPP(selectedDevice!!)
//                return
//            }
//            findViewById<View>(android.R.id.content).postDelayed({
//                connectSPP(
//                    selectedDevice!!
//                )
//            }, 1000L)
//            return
//        }
//        if (view.id == R.id.print) {
//
//        if (!hasPermission(this)) {
//            return
//        }
//        if (sppSocket == null || !sppSocket!!.isConnected || sppSocket!!.outputStream == null) {
//            return
//        }
//        val bitmap = BitmapFactory.decodeResource(resources, R.drawable.test)
//        val widthPX = Math.max(
//            0, if (bitmap.width % 8 != 0) Math.max(
//                0,
//                bitmap.width + 8 - bitmap.width % 8
//            ) else bitmap.width
//        ) //只希望多,不希望裁剪
//
//
//       // CT220B在开机状态下,同时长按电源键和走纸键,可以切换标签或者票据模式
//        if (false) {
//            val bytes = printLabel(
//                widthPX / printerDot,
//                bitmap.height / printerDot,
//                0, 0,
//                15, 5,
//                1, 2,
//                0, 0, bitmap, 1
//            )
//            if (bytes != null) {
//                sppSocket!!.outputStream.write(bytes)
//            }
//        } else {
//            val bytes1 = printReceipt(widthPX / printerDot, bitmap.height / printerDot, bitmap)
//            if (bytes1 != null) {
//                sppSocket!!.outputStream.write(bytes1)
//            }
//        }}
//    }
//
//    private fun searchDeviceSPP() {
//        val defaultAdapter = BluetoothAdapter.getDefaultAdapter()
//        if (defaultAdapter.isDiscovering) {
//            return
//        }
//        defaultAdapter.startDiscovery()
//    }
//
//    private fun connectSPP(macAddr: String) {
//        if (!hasPermission(this)) {
//            requestPermissions(
//                arrayOf(
//                    Manifest.permission.BLUETOOTH,
//                    Manifest.permission.BLUETOOTH_ADMIN,
//                    Manifest.permission.ACCESS_COARSE_LOCATION,
//                    Manifest.permission.ACCESS_FINE_LOCATION
//                ), 0
//            )
//            return
//        }
//        if (TextUtils.isEmpty(macAddr) || !BluetoothAdapter.checkBluetoothAddress(macAddr)) {
//            return
//        }
//        if (!BluetoothAdapter.getDefaultAdapter().isEnabled) {
//            return
//        }
//        val locationManager = getSystemService(LOCATION_SERVICE) as LocationManager
//        if (!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
//            return
//        }
//        val device = BluetoothAdapter.getDefaultAdapter().getRemoteDevice(macAddr)
//        if (device.bondState != BluetoothDevice.BOND_BONDED) {
//            device.createBond()
//            return
//        }
//        Thread(Runnable {
//            connStatus = -1
//            try {
//                sppSocket = device.createInsecureRfcommSocketToServiceRecord(SPP_UUID)
//                sppSocket.connect()
//            } catch (e: IOException) {
//                sppSocket = null
//                e.printStackTrace()
//                mainHandler.sendEmptyMessage(CONN_FAIL)
//                return@Runnable
//            }
//            connStatus = 1
//            mainHandler.sendEmptyMessage(CONN_SUCC)
//        }).start()
//    }
//
//    @Throws(Exception::class)
//    private fun printLabel(
//        @IntRange(from = 1, to = 48) widthMM: Int,
//        @IntRange(from = 1, to = 110) heightMM: Int,
//        @IntRange(from = 0, to = 1) direction: Int,
//        @IntRange(from = 0, to = 1) revert: Int,
//        @IntRange(from = 1, to = 15) density: Int,
//        @IntRange(from = 0, to = 5) speed: Int,
//        @IntRange(from = 0, to = 2) paperType: Int,
//        @IntRange(from = 0, to = 10) gapMM: Int,
//        @IntRange(from = 0, to = 10 * 8) xOffsetDot: Int,
//        @IntRange(from = 0, to = 10 * 8) yOffsetDot: Int,
//        bitmap: Bitmap,
//        @IntRange(from = 1, to = 999) printCount: Int
//    ): ByteArray? {
//        var bitmap = bitmap
//        val baos = ByteArrayOutputStream()
//        //清除缓存
//        baos.write("CLS\r\n".toByteArray(StandardCharsets.UTF_8))
//        //设置热敏
//        baos.write("SET RIBBON OFF\r\n".toByteArray(StandardCharsets.UTF_8))
//        //设置打印原点
//        baos.write("REFERENCE 0,0\r\n".toByteArray(StandardCharsets.UTF_8))
//        //旋转0°~180°,镜像翻转
//        baos.write("DIRECTION $direction,$revert\r\n".toByteArray(StandardCharsets.UTF_8))
//        //设置打印浓度
//        baos.write("DENSITY $density\r\n".toByteArray(StandardCharsets.UTF_8))
//        //设置打印速度
//        baos.write("SPEED $speed\r\n".toByteArray(StandardCharsets.UTF_8))
//        //设置纸张类型和纸张间隔
//        baos.write(getPaperType(paperType, gapMM).toByteArray(StandardCharsets.UTF_8))
//        //设置标签尺寸
//        baos.write("SIZE $widthMM mm,$heightMM mm\r\n".toByteArray(StandardCharsets.UTF_8))
//        if (bitmap.width % 8 != 0) {
//            var w = bitmap.width
//            w = Math.max(0, if (w % 8 != 0) Math.max(0, w + 8 - w % 8) else w) //只希望多,不希望裁剪
//            val b = Bitmap.createBitmap(w, bitmap.height, Bitmap.Config.ARGB_8888)
//            val c = Canvas(b)
//            c.drawColor(Color.WHITE)
//            c.drawBitmap(
//                bitmap,
//                Rect(0, 0, bitmap.width, bitmap.height),
//                Rect(0, 0, w, bitmap.height),
//                null
//            )
//            bitmap = b
//        }
//        baos.write(addImageHeader(xOffsetDot, yOffsetDot, bitmap.width, bitmap.height))
//        baos.write(addImageBody(bitmap))
//        baos.write(byteArrayOf(0x0D, 0x0A))
//        //设置份数,执行打印
//        baos.write("PRINT $printCount,1\r\n".toByteArray(StandardCharsets.UTF_8))
//        return baos.toByteArray()
//    }
//
//    @Throws(Exception::class)
//    private fun printReceipt(
//        @IntRange(from = 1, to = 72) widthMM: Int,
//        heightMM: Int,
//        bitmap: Bitmap
//    ): ByteArray? {
//        var bitmap = bitmap
//        val baos = ByteArrayOutputStream()
//        if (bitmap.width % 8 != 0) {
//            var w = bitmap.width
//            w = Math.max(0, if (w % 8 != 0) Math.max(0, w + 8 - w % 8) else w) //只希望多,不希望裁剪
//            val b = Bitmap.createBitmap(w, bitmap.height, Bitmap.Config.ARGB_8888)
//            val c = Canvas(b)
//            c.drawColor(Color.WHITE)
//            c.drawBitmap(
//                bitmap,
//                Rect(0, 0, bitmap.width, bitmap.height),
//                Rect(0, 0, w, bitmap.height),
//                null
//            )
//            bitmap = b
//        }
//        val black = 0
//        val white = 1
//        val quality = 150 //值越大,越能打印越浅的颜色
//        val width = bitmap.width
//        var height = bitmap.height
//        val pixels = IntArray(bitmap.width * bitmap.height)
//        val src = ByteArray(bitmap.width * bitmap.height)
//        val grayBitmap = toGrayscale(bitmap)
//        grayBitmap.getPixels(pixels, 0, bitmap.width, 0, 0, bitmap.width, bitmap.height)
//        format_K_dither16x16(pixels, grayBitmap.width, grayBitmap.height, src)
//
//        //清空缓存
//        baos.write(byteArrayOf(27, 64))
//        //定位走纸
//        baos.write(byteArrayOf(0x1D, 0X0E))
//        val command = ByteArray(8)
//        height = src.size / width
//        command[0] = 29
//        command[1] = 118
//        command[2] = 48
//        command[3] = (0 and 1).toByte()
//        command[4] = (width / 8 % 256).toByte()
//        command[5] = (width / 8 / 256).toByte()
//        command[6] = (height % 256).toByte()
//        command[7] = (height / 256).toByte()
//        baos.write(command)
//        val cmd = ByteArray(width / 8 * height)
//        var index = 0
//        var temp = 0
//        val part = IntArray(8)
//        for (j in 0 until grayBitmap.height) {
//            var i = 0
//            while (i < grayBitmap.width) {
//                for (k in 0..7) { //横向每8个像素点组成一个字节。
//                    val pixel = grayBitmap.getPixel(i + k, j)
//                    val red = pixel and 0x00ff0000 shr 16 //获取r分量
//                    val green = pixel and 0x0000ff00 shr 8 //获取g分量
//                    val blue = pixel and 0x000000ff //获取b分量
//                    val gray = (0.29900 * red + 0.58700 * green + 0.11400 * blue).toInt() // 灰度转化公式
//                    part[k] = if (gray > quality) black else white //灰度值大于128位   白色 为第k位0不打印
//                }
//                temp =
//                    part[0] * 128 + part[1] * 64 + part[2] * 32 + part[3] * 16 + part[4] * 8 + part[5] * 4 + part[6] * 2 + part[7] * 1
//                cmd[index++] = temp.toByte()
//                i += 8
//            }
//        }
//        baos.write(cmd)
//        return baos.toByteArray()
//    }
//
//    private fun toGrayscale(bmpOriginal: Bitmap): Bitmap {
//        val height = bmpOriginal.height
//        val width = bmpOriginal.width
//        val bmpGrayscale = Bitmap.createBitmap(width, height, Bitmap.Config.RGB_565)
//        val c = Canvas(bmpGrayscale)
//        val paint = Paint()
//        val cm = ColorMatrix()
//        cm.setSaturation(0.0f)
//        val f = ColorMatrixColorFilter(cm)
//        paint.colorFilter = f
//        c.drawBitmap(bmpOriginal, 0.0f, 0.0f, paint)
//        return bmpGrayscale
//    }
//
//    /****************************label */
//    fun addImageHeader(xDot: Int, yDot: Int, bitmapWidth: Int, bitmapHeight: Int): ByteArray {
//        var xDot = xDot
//        var yDot = yDot
//        xDot = Math.max(0, if (xDot % 8 != 0) Math.max(0, xDot - xDot % 8) else xDot)
//        yDot = Math.max(0, if (yDot % 8 != 0) Math.max(0, yDot - yDot % 8) else yDot)
//        val str = "BITMAP $xDot,$yDot,$bitmapWidth" /8 + "," + bitmapHeight + ",1,"
//        return str.toByteArray()
//    }
//
//    /**
//     * @param papertype 0是间隙纸,1是连续纸,2是黑标纸
//     * @param gapMM
//     * @return
//     */
//    fun getPaperType(papertype: Int, gapMM: Int): String {
//        val gapStr: String
//        gapStr = if (papertype == 2) {
//            "BLINE $gapMM mm,0 mm\r\n"
//        } else if (papertype == 1) {
//            """
//     GAP 0 mm,0 mm
//
//     """.trimIndent()
//        } else { // 设置标签间隙，按照实际尺寸设置，如果为无间隙纸则设置为0
//            "GAP $gapMM mm,0 mm\r\n"
//        }
//        return gapStr
//    }
//
//    private fun getSpeedValue(speedNum: Int): Int {
//        var speed = 2
//        when (speedNum) {
//            0 -> speed = 2
//            1 -> speed = 3
//            2 -> speed = 4
//            3 -> speed = 5
//            4 -> speed = 6
//            5 -> speed = 8
//        }
//        return speed
//    }
//
//    companion object {
//        private const val CONN_SUCC = 1000
//        private const val CONN_FAIL = 1001
//
//        /**
//         * 203DPI的dot = 8
//         * 300DPI的dot = 12
//         * 600DPI的dot = 24
//         * 打印机220B是203DPI
//         */
//        private const val printerDot = 8
//
//        /****************************receipt */
//        private val Floyd16x16 = arrayOf(
//            intArrayOf(0, 128, 32, 160, 8, 136, 40, 168, 2, 130, 34, 162, 10, 138, 42, 170),
//            intArrayOf(192, 64, 224, 96, 200, 72, 232, 104, 194, 66, 226, 98, 202, 74, 234, 106),
//            intArrayOf(48, 176, 16, 144, 56, 184, 24, 152, 50, 178, 18, 146, 58, 186, 26, 154),
//            intArrayOf(240, 112, 208, 80, 248, 120, 216, 88, 242, 114, 210, 82, 250, 122, 218, 90),
//            intArrayOf(12, 140, 44, 172, 4, 132, 36, 164, 14, 142, 46, 174, 6, 134, 38, 166),
//            intArrayOf(204, 76, 236, 108, 196, 68, 228, 100, 206, 78, 238, 110, 198, 70, 230, 102),
//            intArrayOf(60, 188, 28, 156, 52, 180, 20, 148, 62, 190, 30, 158, 54, 182, 22, 150),
//            intArrayOf(252, 124, 220, 92, 244, 116, 212, 84, 254, 126, 222, 94, 246, 118, 214, 86),
//            intArrayOf(3, 131, 35, 163, 11, 139, 43, 171, 1, 129, 33, 161, 9, 137, 41, 169),
//            intArrayOf(195, 67, 227, 99, 203, 75, 235, 107, 193, 65, 225, 97, 201, 73, 233, 105),
//            intArrayOf(51, 179, 19, 147, 59, 187, 27, 155, 49, 177, 17, 145, 57, 185, 25, 153),
//            intArrayOf(243, 115, 211, 83, 251, 123, 219, 91, 241, 113, 209, 81, 249, 121, 217, 89),
//            intArrayOf(15, 143, 47, 175, 7, 135, 39, 167, 13, 141, 45, 173, 5, 133, 37, 165),
//            intArrayOf(207, 79, 239, 111, 199, 71, 231, 103, 205, 77, 237, 109, 197, 69, 229, 101),
//            intArrayOf(63, 191, 31, 159, 55, 183, 23, 151, 61, 189, 29, 157, 53, 181, 21, 149),
//            intArrayOf(254, 127, 223, 95, 247, 119, 215, 87, 253, 125, 221, 93, 245, 117, 213, 85)
//        )
//
//        private fun format_K_dither16x16(
//            orgpixels: IntArray,
//            xsize: Int,
//            ysize: Int,
//            despixels: ByteArray
//        ) {
//            var k = 0
//            for (y in 0 until ysize) {
//                for (x in 0 until xsize) {
//                    if (orgpixels[k] and 255 > Floyd16x16[x and 15][y and 15]) {
//                        despixels[k] = 0
//                    } else {
//                        despixels[k] = 1
//                    }
//                    ++k
//                }
//            }
//        }
//
//        fun addImageBody(b: Bitmap): ByteArray {
//            val width = b.width // 获取位图的宽
//            val height = b.height // 获取位图的高
//            val bytes = ByteArray(width / 8 * height)
//            val p = IntArray(8)
//            val black = 0
//            val white = 1
//            val quality = 150 //值越大,越能打印越浅的颜色
//            for (i in 0 until height) {
//                for (j in 0 until width / 8) {
//                    for (z in 0..7) {
//                        val grey = b.getPixel(j * 8 + z, i)
//                        val red = grey and 0x00FF0000 shr 16
//                        val green = grey and 0x0000FF00 shr 8
//                        val blue = grey and 0x000000FF
//                        var gray =
//                            (0.29900 * red + 0.58700 * green + 0.11400 * blue).toInt() // 灰度转化公式
//                        gray = if (gray <= quality) black else white
//                        p[z] = gray
//                    }
//                    val value =
//                        (p[0] * 128 + p[1] * 64 + p[2] * 32 + p[3] * 16 + p[4] * 8 + p[5] * 4 + p[6] * 2 + p[7]).toByte()
//                    bytes[width / 8 * i + j] = value
//                }
//            }
//            return bytes
//        }
//
//        fun hasPermission(c: Context): Boolean {
//            val bt = PackageManager.PERMISSION_GRANTED == c.checkPermission(
//                Manifest.permission.BLUETOOTH, Process.myPid(), Process.myUid()
//            )
//            val toggle = PackageManager.PERMISSION_GRANTED == c.checkPermission(
//                Manifest.permission.BLUETOOTH_ADMIN, Process.myPid(), Process.myUid()
//            )
//            val location = PackageManager.PERMISSION_GRANTED == c.checkPermission(
//                Manifest.permission.ACCESS_COARSE_LOCATION, Process.myPid(), Process.myUid()
//            )
//            val location2 = PackageManager.PERMISSION_GRANTED == c.checkPermission(
//                Manifest.permission.ACCESS_FINE_LOCATION, Process.myPid(), Process.myUid()
//            )
//            return bt && toggle && location && location2
//        }
//    }
//}