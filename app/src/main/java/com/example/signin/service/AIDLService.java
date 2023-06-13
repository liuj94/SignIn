package com.example.signin.service;

import android.app.Service;
import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.util.Log;

import androidx.annotation.Nullable;

public class AIDLService extends Service {
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
    private static ServiceConnection conn = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
//            scanInterface = IScanInterface.Stub.asInterface(service);
            Log.i("setting", "Scanner Service Connected!");
        }
        @Override
        public void onServiceDisconnected(ComponentName name) {
            Log.e("setting", "Scanner Service Disconnected!");
//            scanInterface = null;
        }
    };
    public void bindScannerService() {
        Intent intent = new Intent();
        intent.setPackage("com.sunmi.scanner");
                intent.setAction("com.sunmi.scanner.IScanInterface");
        bindService(intent, conn, Service.BIND_AUTO_CREATE);
    }
}
