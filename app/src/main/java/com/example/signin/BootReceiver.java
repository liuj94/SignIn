package com.example.signin;



import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.util.Log;

public class BootReceiver extends BroadcastReceiver {
    private static final String ACTION = "android.intent.action.BOOT_COMPLETED";


    @Override
    public void onReceive(Context context, Intent intent) {
//        if (intent.getAction().equals(Intent.ACTION_BOOT_COMPLETED)) {
//            Intent newIntent = new Intent(context, SplashActivity.class);  // 要启动的Activity
//            //这句话必须加上才能开机自动运行app的界面
//            newIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//            //2.如果自启动Activity
//            context.startActivity(newIntent);
//        }
        Log.e("接收广播", "onReceive: ");
        Log.e("接收广播", "onReceive: " + intent.getAction());
        //开机启动
        if (ACTION.equals(intent.getAction())) {
            Log.e("接收广播", "onReceive: 启动了。。。");

//第一种方式：根据包名
            PackageManager packageManager = context.getPackageManager();
            Intent mainIntent = packageManager.getLaunchIntentForPackage("com.example.signin");
            mainIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK|
        Intent.FLAG_ACTIVITY_CLEAR_TOP |Intent.FLAG_ACTIVITY_CLEAR_TASK);

            context.startActivity(mainIntent);
            context.startService(mainIntent);


//第二种方式：指定class类，跳转到相应的Acitivity
//            Intent mainIntent = new Intent(context, SplashActivity.class);
//            /**
//             * Intent.FLAG_ACTIVITY_NEW_TASK
//             * Intent.FLAG_ACTIVITY_CLEAR_TOP
//             */
//            mainIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//            mainIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//            context.startActivity(mainIntent);
//            context.startService(mainIntent);
        }
    }

//    public BootReceiver() {
//    }
//
//    @Override
//    public void onReceive(Context context, Intent intent) {
//        Log.v("Jason", "BootReceiver start");
//        //启动MainActivity
//       if( MMKV.mmkvWithID("MyDataMMKV").getString("startApp", "1").equals("1")){
//           Intent intent1 = new Intent(context, SplashActivity.class);
//           intent1.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);//添加标记
//           context.startActivity(intent1);
//           Log.v("Jason", "BootReceiver end");
//       }
//
//    }
}

