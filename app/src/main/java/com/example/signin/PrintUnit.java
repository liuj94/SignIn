package com.example.signin;

import android.Manifest;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothClass;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.Paint;
import android.graphics.Rect;
import android.location.LocationManager;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.os.Parcelable;
import android.text.TextUtils;
import android.util.Log;

import androidx.annotation.IntRange;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;

import com.example.signin.bean.SiginData;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import static android.content.Context.LOCATION_SERVICE;

public class PrintUnit {
    /**
     * 203DPI的dot = 8
     * 300DPI的dot = 12
     * 600DPI的dot = 24
     * 打印机220B是203DPI
     */
    private static final int printerDot = 8;
    private final UUID SPP_UUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");
    private static final int CONN_SUCC = 1000;
    private static final int CONN_FAIL = 1001;
    private volatile BluetoothSocket sppSocket = null;
    private int connStatus = -1;
    private BroadcastReceiver receiver = null;
    public boolean isConPrint = false;
    private Handler mainHandler = new Handler(Looper.getMainLooper()) {
        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);
            String toastStr = msg.what == CONN_SUCC ? "连接成功" : "连接失败";
            Log.d("aaaaprintUnitXXPermissions", " conPrint=" + toastStr);
            if (msg.what == CONN_SUCC) {
                isConPrint = true;
            } else {
                isConPrint = false;
            }
            if (listPrinter != null) {
                listPrinter.conPrint(isConPrint);
            }
            ;
//            Toast.makeText(context, toastStr, Toast.LENGTH_SHORT).show();
        }
    };
    Context context;
    List<SiginData> list = new ArrayList<>();

    public PrintUnit(Context context) {
        this.context = context;
        searchDeviceSPP(context);
    }

    public PrintUnit(Context context, ListPrinter listPrinter) {
        this.context = context;
        this.listPrinter = listPrinter;
        searchDeviceSPP(context);
    }

    public void PrintUnregisterReceiver() {
        if (receiver != null) {
            context.unregisterReceiver(receiver);
        }

    }

    public void PrintRegisterReceiver() {
//        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.BLUETOOTH_CONNECT) != PackageManager.PERMISSION_GRANTED) {
//            return;
//        }
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(BluetoothAdapter.ACTION_STATE_CHANGED);//蓝牙开关
        intentFilter.addAction(BluetoothDevice.ACTION_BOND_STATE_CHANGED);//蓝牙连接状态
        intentFilter.addAction(BluetoothDevice.ACTION_ACL_DISCONNECTED);//蓝牙连接状态

        intentFilter.addAction(BluetoothAdapter.ACTION_DISCOVERY_STARTED);//搜索设备
        intentFilter.addAction(BluetoothDevice.ACTION_FOUND);
        intentFilter.addAction(BluetoothAdapter.ACTION_DISCOVERY_FINISHED);
        receiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {//此处处理逻辑
                String action = intent.getAction();
                switch (action) {
                    case BluetoothAdapter.ACTION_DISCOVERY_STARTED:
                        Log.d("123456", "ACTION_DISCOVERY_STARTED");
//                        adapter.clear();
//                        selectedDevice = null;
//                        TextView tv = findViewById(R.id.selected);
//                        tv.setText("选中设备:");

                        break;
                    case BluetoothDevice.ACTION_FOUND:
//                        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.BLUETOOTH_CONNECT) != PackageManager.PERMISSION_GRANTED) {
//                            return;
//                        }
                        Parcelable parcelable = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);//得到该设备
                        if (!(parcelable instanceof BluetoothDevice))
                            return;
                        BluetoothDevice device = (BluetoothDevice) parcelable;

                        boolean isPrinter = BluetoothClass.Device.Major.IMAGING == device.getBluetoothClass().getMajorDeviceClass();
                        Log.e("aaprintUnitXXPermissions", "------------搜索--- $device.getName()==" + device.getName());
                        if (isPrinter && !TextUtils.isEmpty(device.getName()) && device.getName().startsWith("CT")) {
                            //系统搜索会有重复的对象,需要自行过滤
                            Log.e("aaprintUnitXXPermissions", "------------搜索--- $device.getName()==" + device.getName());
                            SiginData a = new SiginData();
                            a.setName(device.getName());
                            a.setMac(device.getAddress());
                            a.setKuan(true);
                            list.add(a);
//                            list.add(device.getName() + "\n\n" + device.getAddress());
                            if (listPrinter != null) {
                                Log.e("printUnitXXPermissions", "------------istPrinter!=null--- $device.getName()==" + device.getName());
//                                listPrinter.printer(device.getName() + "\n\n" + device.getAddress());
                                listPrinter.printer(a);
                            }
                        }
                        break;
                }
            }
        };
        context.registerReceiver(receiver, intentFilter);
    }

    public void OnePrintRegisterReceiver() {
//        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.BLUETOOTH_CONNECT) != PackageManager.PERMISSION_GRANTED) {
//            Log.d("XXPermissions", "checkSelfPermission");
//            return;
//        }
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(BluetoothAdapter.ACTION_STATE_CHANGED);//蓝牙开关
        intentFilter.addAction(BluetoothDevice.ACTION_BOND_STATE_CHANGED);//蓝牙连接状态
        intentFilter.addAction(BluetoothDevice.ACTION_ACL_DISCONNECTED);//蓝牙连接状态

        intentFilter.addAction(BluetoothAdapter.ACTION_DISCOVERY_STARTED);//搜索设备
        intentFilter.addAction(BluetoothDevice.ACTION_FOUND);
        intentFilter.addAction(BluetoothAdapter.ACTION_DISCOVERY_FINISHED);
        receiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {//此处处理逻辑
                String action = intent.getAction();
                switch (action) {

                    case BluetoothAdapter.ACTION_DISCOVERY_STARTED:
                        Log.d("XXPermissions", "ACTION_DISCOVERY_STARTED");
//                        adapter.clear();
//                        selectedDevice = null;
//                        TextView tv = findViewById(R.id.selected);
//                        tv.setText("选中设备:");

                        break;
                    case BluetoothDevice.ACTION_FOUND:

                        Parcelable parcelable = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);//得到该设备
                        if (!(parcelable instanceof BluetoothDevice))
                            return;
                        BluetoothDevice device = (BluetoothDevice) parcelable;
                        boolean isPrinter = BluetoothClass.Device.Major.IMAGING == device.getBluetoothClass().getMajorDeviceClass();

                        if (isPrinter && !TextUtils.isEmpty(device.getName()) && device.getName().startsWith("CT")) {
                            Log.d("printUnitXXPermissions", "ACTION_FOUND device addr name=" + device.getName() + "==Address===" + device.getAddress());
                            //系统搜索会有重复的对象,需要自行过滤
                            if (list.size() < 1) {
                                SiginData a = new SiginData();
                                a.setName(device.getName());
                                a.setMac(device.getAddress());
                                a.setKuan(true);
//                                listPrinter.printer(device.getName() + "\n\n" + device.getAddress());
                                list.add(a);
                                if (listPrinter != null) {
                                    listPrinter.printer(a);
//                                    listPrinter.printer(device.getName() + "\n\n" + device.getAddress());
                                }
                            }

                        }
                        break;
                }
            }
        };
        context.registerReceiver(receiver, intentFilter);
    }

    ListPrinter listPrinter;

    public ListPrinter getListPrinter() {
        return listPrinter;
    }

    public void setListPrinter(ListPrinter listPrinter) {
        this.listPrinter = listPrinter;
    }

    public interface ListPrinter {
        void printer(SiginData p);

        void conPrint(boolean p);
    }

    public void searchDeviceSPP(Context context) {
        BluetoothAdapter defaultAdapter = BluetoothAdapter.getDefaultAdapter();
        if (defaultAdapter == null) {
            Log.e("XXPermissions", "--------------- 不支持蓝牙");
            return;
        }
        if (!defaultAdapter.isEnabled()) {
            boolean res = defaultAdapter.enable();
            if (res == true) {
                Log.e("XXPermissions", "--------------- 蓝牙打开成功");
            } else {
                Log.e("XXPermissions", "--------------- 蓝牙打开失败");
            }
        } else if (defaultAdapter != null && defaultAdapter.isEnabled()) {
//            myBtResultCallback.showToastMsg("蓝牙已打开");
            Log.e("XXPermissions", "--------------- 蓝牙已打开");
        } else {
            Log.e("XXPermissions", "--------------- 蓝牙打开失败");
//            myBtResultCallback.showToastMsg("蓝牙打开失败");
        }
        Set<BluetoothDevice> devices = defaultAdapter.getBondedDevices();

        for (BluetoothDevice bluetoothDevice : devices) {
//            Log.d("printUnitXXPermissions", "ACTION_FOUND device addr name=" + bluetoothDevice.getName() + "==Address===" + bluetoothDevice.getAddress());
            boolean isPrinter = BluetoothClass.Device.Major.IMAGING == bluetoothDevice.getBluetoothClass().getMajorDeviceClass();
//            Log.e("printUnitXXPermissions", "------------isPrinter--- " + isPrinter);
            if (isPrinter && !TextUtils.isEmpty(bluetoothDevice.getName()) && bluetoothDevice.getName().startsWith("CT")) {
                //系统搜索会有重复的对象,需要自行过滤
//                if(list.size()<1){
                SiginData a = new SiginData();
                a.setName(bluetoothDevice.getName());
                a.setMac(bluetoothDevice.getAddress());
                a.setKuan(true);
                list.add(a);
                if (listPrinter != null) {
                    Log.e("printUnitXXPermissions", "------------kkklbluetoothDevice--- bluetoothDevice.getName()==" + bluetoothDevice.getName());

//                                listPrinter.printer(device.getName() + "\n\n" + device.getAddress());
                    listPrinter.printer(a);
//                        listPrinter.printer(bluetoothDevice.getName() + "\n\n" + bluetoothDevice.getAddress());
                }
//                }

            }

        }
        if (defaultAdapter.isDiscovering()) {
            return;
        }
        defaultAdapter.startDiscovery();

    }

    public void print(Bitmap bitmap)  {
//        int widthPX = Math.max(0, bitmap.getWidth() % 8 != 0 ?
//                Math.max(0, bitmap.getWidth() + 8 - (bitmap.getWidth() % 8)) : bitmap.getWidth());//只希望多,不希望裁剪

//        byte[] bytes1 = printReceipt(widthPX / printerDot, bitmap.getHeight() / printerDot, bitmap);
        try {
            byte[] bytes1 = printReceipt(bitmap);
            if (bytes1 != null && sppSocket != null) {
                Log.d(
                        "aaaaprintUnitXXPermissions",
                        "print(Bitmap bitmap)="
                );
                sppSocket.getOutputStream().write(bytes1);
            }
        }catch (Exception e){}

    }
    public void print(Bitmap bitmap,int widthPX)  {
//        int widthPX = Math.max(0, bitmap.getWidth() % 8 != 0 ?
//                Math.max(0, bitmap.getWidth() + 8 - (bitmap.getWidth() % 8)) : bitmap.getWidth());//只希望多,不希望裁剪

//        byte[] bytes1 = printReceipt(widthPX / printerDot, bitmap.getHeight() / printerDot, bitmap);
        try {
            byte[] bytes = printLabel(widthPX / printerDot,
                    bitmap.getHeight() / printerDot,
                    0, 0,
                    15, 5,
                    1, 2,
                    0, 0, bitmap, 1);
            if (bytes != null) {
                sppSocket.getOutputStream().write(bytes);
            }
            if (bytes != null && sppSocket != null) {
                Log.d(
                        "aaaaprintUnitXXPermissions",
                        "print(Bitmap bitmap)="
                );
                sppSocket.getOutputStream().write(bytes);
            }
        }catch (Exception e){}

    }
    private @Nullable byte[] printLabel(@IntRange(from = 1, to = 48)int widthMM,
                                        @IntRange(from = 1, to = 110)int heightMM,
                                        @IntRange(from = 0, to = 1) int direction,
                                        @IntRange(from = 0, to = 1) int revert,
                                        @IntRange(from = 1, to = 15) int density,
                                        @IntRange(from = 0, to = 5) int speed,
                                        @IntRange(from = 0, to = 2) int paperType,
                                        @IntRange(from = 0, to = 10) int gapMM,
                                        @IntRange(from = 0, to = 10 * 8/*dot*/) int xOffsetDot,
                                        @IntRange(from = 0, to = 10 * 8/*dot*/) int yOffsetDot,
                                        @NonNull Bitmap bitmap,
                                        @IntRange(from = 1, to = 999) int printCount
    ) throws Exception {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        //清除缓存
        baos.write("CLS\r\n".getBytes(StandardCharsets.UTF_8));
        //设置热敏
        baos.write( "SET RIBBON OFF\r\n".getBytes(StandardCharsets.UTF_8));
        //设置打印原点
        baos.write(("REFERENCE 0,0\r\n").getBytes(StandardCharsets.UTF_8));
        //旋转0°~180°,镜像翻转
        baos.write(("DIRECTION " + direction + ',' + revert + "\r\n").getBytes(StandardCharsets.UTF_8));
        //设置打印浓度
        baos.write(("DENSITY " + density + "\r\n").getBytes(StandardCharsets.UTF_8));
        //设置打印速度
        baos.write(("SPEED " + speed + "\r\n").getBytes(StandardCharsets.UTF_8));
        //设置纸张类型和纸张间隔
        baos.write(getPaperType(paperType, gapMM).getBytes(StandardCharsets.UTF_8));
        //设置标签尺寸
        baos.write(("SIZE " + widthMM + " mm," + heightMM + " mm" + "\r\n").getBytes(StandardCharsets.UTF_8));

        if (bitmap.getWidth() % 8 != 0) {
            int w = bitmap.getWidth();
            w = Math.max(0, w % 8 != 0 ? Math.max(0, w + 8 - (w % 8)) : w);//只希望多,不希望裁剪
            Bitmap b = Bitmap.createBitmap(w, bitmap.getHeight(), Bitmap.Config.ARGB_8888);
            Canvas c = new Canvas(b);
            c.drawColor(Color.WHITE);
            c.drawBitmap(bitmap,
                    new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight()),
                    new Rect(0, 0, w, bitmap.getHeight()),
                    null);
            bitmap = b;
        }

        baos.write(addImageHeader(xOffsetDot, yOffsetDot, bitmap.getWidth(), bitmap.getHeight()));
        baos.write(addImageBody(bitmap));
        baos.write(new byte[]{0x0D, 0x0A});
        //设置份数,执行打印
        baos.write(("PRINT " + printCount + ",1\r\n").getBytes(StandardCharsets.UTF_8));
        return baos.toByteArray();
    }
    public byte[] addImageHeader(int xDot, int yDot, int bitmapWidth, int bitmapHeight) {
        xDot = Math.max(0, xDot % 8 != 0 ? Math.max(0, xDot - (xDot % 8)) : xDot);

        yDot = Math.max(0, yDot % 8 != 0 ? Math.max(0, yDot - (yDot % 8)) : yDot);

        String str = "BITMAP " + xDot + "," + yDot + "," + (bitmapWidth / 8) + "," + bitmapHeight + ",1,";
        return str.getBytes();
    }

    public static byte[] addImageBody(Bitmap b) {
        int width = b.getWidth(); // 获取位图的宽
        int height = b.getHeight(); // 获取位图的高
        byte[] bytes = new byte[(width) / 8 * height];
        int[] p = new int[8];

        int black = 0, white = 1;
        int quality = 150;//值越大,越能打印越浅的颜色

        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width / 8; j++) {
                for (int z = 0; z < 8; z++) {
                    int grey = b.getPixel(j * 8 + z, i);
                    int red = ((grey & 0x00FF0000) >> 16);
                    int green = ((grey & 0x0000FF00) >> 8);
                    int blue = (grey & 0x000000FF);
                    int gray = (int) (0.29900 * red + 0.58700 * green + 0.11400 * blue); // 灰度转化公式
                    gray = gray <= quality ? black : white;
                    p[z] = gray;
                }
                byte value = (byte) (p[0] * 128 + p[1] * 64 + p[2] * 32 + p[3] * 16 + p[4] * 8 + p[5] * 4 + p[6] * 2 + p[7]);
                bytes[width / 8 * i + j] = value;
            }
        }
        return bytes;
    }

    /**
     * @param papertype 0是间隙纸,1是连续纸,2是黑标纸
     * @param gapMM
     * @return
     */
    public String getPaperType(int papertype, int gapMM) {
        String gapStr;
        if (papertype == 2) {
            gapStr = "BLINE " + gapMM + " mm," + 0 + " mm\r\n";
        } else if (papertype == 1) {
            gapStr = "GAP " + 0 + " mm," + 0 + " mm\r\n";
        } else {// 设置标签间隙，按照实际尺寸设置，如果为无间隙纸则设置为0
            gapStr = "GAP " + gapMM + " mm," + 0 + " mm\r\n";
        }
        return gapStr;
    }
    private @Nullable byte[] printReceipt(
            @NonNull Bitmap bitmap) throws Exception {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();

        if (bitmap.getWidth() % 8 != 0) {
//            int w = bitmap.getWidth();
            int w = bitmap.getWidth();
            w = Math.max(0, w % 8 != 0 ? Math.max(0, w + 8 - (w % 8)) : w);//只希望多,不希望裁剪
            Bitmap b = Bitmap.createBitmap(w, bitmap.getHeight(), Bitmap.Config.ARGB_8888);
            Canvas c = new Canvas(b);
            c.drawColor(Color.WHITE);
            c.drawBitmap(bitmap,
                    new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight()),
                    new Rect(0, 0, w, bitmap.getHeight()),
                    null);
            bitmap = b;
        }
        int black = 0, white = 1;
        int quality = 150;//值越大,越能打印越浅的颜色

        int width = bitmap.getWidth();
        int height = bitmap.getHeight();

        int[] pixels = new int[bitmap.getWidth() * bitmap.getHeight()];
        byte[] src = new byte[bitmap.getWidth() * bitmap.getHeight()];
        Bitmap grayBitmap = toGrayscale(bitmap);
        grayBitmap.getPixels(pixels, 0, bitmap.getWidth(), 0, 0, bitmap.getWidth(), bitmap.getHeight());
        format_K_dither16x16(pixels, grayBitmap.getWidth(), grayBitmap.getHeight(), src);

        //清空缓存
        baos.write(new byte[]{27, 64});
        //定位走纸
        baos.write(new byte[]{0x1D, 0X0E});

        byte[] command = new byte[8];
        height = src.length / width;
        command[0] = 29;
        command[1] = 118;
        command[2] = 48;
        command[3] = (byte) (0 & 1);
        command[4] = (byte) (width / 8 % 256);
        command[5] = (byte) (width / 8 / 256);
        command[6] = (byte) (height % 256);
        command[7] = (byte) (height / 256);
        baos.write(command);

        byte[] cmd = new byte[width / 8 * height];
        int index = 0, temp = 0;
        int part[] = new int[8];
        for (int j = 0; j < grayBitmap.getHeight(); j++) {
            for (int i = 0; i < grayBitmap.getWidth(); i += 8) {
                for (int k = 0; k < 8; k++) {//横向每8个像素点组成一个字节。
                    int pixel = grayBitmap.getPixel(i + k, j);
                    int red = (pixel & 0x00ff0000) >> 16;//获取r分量
                    int green = (pixel & 0x0000ff00) >> 8;//获取g分量
                    int blue = pixel & 0x000000ff;//获取b分量
                    int gray = (int) (0.29900 * red + 0.58700 * green + 0.11400 * blue); // 灰度转化公式

                    part[k] = gray > quality ? black : white;//灰度值大于128位   白色 为第k位0不打印
                }
                temp = part[0] * 128 +
                        part[1] * 64 +
                        part[2] * 32 +
                        part[3] * 16 +
                        part[4] * 8 +
                        part[5] * 4 +
                        part[6] * 2 +
                        part[7] * 1;
                cmd[index++] = (byte) temp;
            }
        }
        baos.write(cmd);
        return baos.toByteArray();
    }

    private Bitmap toGrayscale(Bitmap bmpOriginal) {
        int height = bmpOriginal.getHeight();
        int width = bmpOriginal.getWidth();
        Bitmap bmpGrayscale = Bitmap.createBitmap(width, height, Bitmap.Config.RGB_565);
        Canvas c = new Canvas(bmpGrayscale);
        Paint paint = new Paint();
        ColorMatrix cm = new ColorMatrix();
        cm.setSaturation(0.0F);
        ColorMatrixColorFilter f = new ColorMatrixColorFilter(cm);
        paint.setColorFilter(f);
        c.drawBitmap(bmpOriginal, 0.0F, 0.0F, paint);
        return bmpGrayscale;
    }

    private static void format_K_dither16x16(int[] orgpixels, int xsize, int ysize, byte[] despixels) {
        int k = 0;
        for (int y = 0; y < ysize; ++y) {
            for (int x = 0; x < xsize; ++x) {
                if ((orgpixels[k] & 255) > Floyd16x16[x & 15][y & 15]) {
                    despixels[k] = 0;
                } else {
                    despixels[k] = 1;
                }
                ++k;
            }
        }
    }

    private static int[][] Floyd16x16 = new int[][]{
            {0, 128, 32, 160, 8, 136, 40, 168, 2, 130, 34, 162, 10, 138, 42, 170},
            {192, 64, 224, 96, 200, 72, 232, 104, 194, 66, 226, 98, 202, 74, 234, 106},
            {48, 176, 16, 144, 56, 184, 24, 152, 50, 178, 18, 146, 58, 186, 26, 154},
            {240, 112, 208, 80, 248, 120, 216, 88, 242, 114, 210, 82, 250, 122, 218, 90},
            {12, 140, 44, 172, 4, 132, 36, 164, 14, 142, 46, 174, 6, 134, 38, 166},
            {204, 76, 236, 108, 196, 68, 228, 100, 206, 78, 238, 110, 198, 70, 230, 102},
            {60, 188, 28, 156, 52, 180, 20, 148, 62, 190, 30, 158, 54, 182, 22, 150},
            {252, 124, 220, 92, 244, 116, 212, 84, 254, 126, 222, 94, 246, 118, 214, 86},
            {3, 131, 35, 163, 11, 139, 43, 171, 1, 129, 33, 161, 9, 137, 41, 169},
            {195, 67, 227, 99, 203, 75, 235, 107, 193, 65, 225, 97, 201, 73, 233, 105},
            {51, 179, 19, 147, 59, 187, 27, 155, 49, 177, 17, 145, 57, 185, 25, 153},
            {243, 115, 211, 83, 251, 123, 219, 91, 241, 113, 209, 81, 249, 121, 217, 89},
            {15, 143, 47, 175, 7, 135, 39, 167, 13, 141, 45, 173, 5, 133, 37, 165},
            {207, 79, 239, 111, 199, 71, 231, 103, 205, 77, 237, 109, 197, 69, 229, 101},
            {63, 191, 31, 159, 55, 183, 23, 151, 61, 189, 29, 157, 53, 181, 21, 149},
            {254, 127, 223, 95, 247, 119, 215, 87, 253, 125, 221, 93, 245, 117, 213, 85}
    };

    public void connectSPP(String macAddr) {
        Log.d("aaaaprintUnitXXPermissions", "connectSPP=" + macAddr);
        Log.d("aaaaprintUnitXXPermissions", "BluetoothAdapter.checkBluetoothAddress(macAddr)=" + BluetoothAdapter.checkBluetoothAddress(macAddr));
        if (TextUtils.isEmpty(macAddr) || !BluetoothAdapter.checkBluetoothAddress(macAddr)) {
            return;
        }
        if (!BluetoothAdapter.getDefaultAdapter().isEnabled()) {
            return;
        }
        LocationManager locationManager = (LocationManager) context.getSystemService(LOCATION_SERVICE);
        if (!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            return;
        }
        BluetoothDevice device = BluetoothAdapter.getDefaultAdapter().getRemoteDevice(macAddr);

        if (device.getBondState() != BluetoothDevice.BOND_BONDED) {
            device.createBond();
            return;
        }
        new Thread(() -> {
            connStatus = -1;
            try {
                sppSocket = device.createInsecureRfcommSocketToServiceRecord(SPP_UUID);
                sppSocket.connect();
            } catch (IOException e) {
                sppSocket = null;
                e.printStackTrace();
                mainHandler.sendEmptyMessage(CONN_FAIL);
                return;
            }
            connStatus = 1;
            mainHandler.sendEmptyMessage(CONN_SUCC);
        }).start();
    }

    public static boolean hasPermission(Context c) {
        boolean bt = PackageManager.PERMISSION_GRANTED == c.checkPermission(
                Manifest.permission.BLUETOOTH, android.os.Process.myPid(), android.os.Process.myUid());
        boolean toggle = PackageManager.PERMISSION_GRANTED == c.checkPermission(
                Manifest.permission.BLUETOOTH_ADMIN, android.os.Process.myPid(), android.os.Process.myUid());
        boolean location = PackageManager.PERMISSION_GRANTED == c.checkPermission(
                Manifest.permission.ACCESS_COARSE_LOCATION, android.os.Process.myPid(), android.os.Process.myUid());
        boolean location2 = PackageManager.PERMISSION_GRANTED == c.checkPermission(
                Manifest.permission.ACCESS_FINE_LOCATION, android.os.Process.myPid(), android.os.Process.myUid());
        return bt && toggle && location && location2;
    }

}
