//package com.example.signin.webs;
//
//
//import android.util.Log;
//
//import org.java_websocket.client.WebSocketClient;
//import org.java_websocket.drafts.Draft;
//import org.java_websocket.drafts.Draft_6455;
//import org.java_websocket.handshake.ServerHandshake;
//
//import java.net.URI;
//import java.util.ArrayList;
//import java.util.Date;
//import java.util.List;
//import java.util.concurrent.TimeUnit;
//
//public class WebSocketHelper {
//
//    private static final String TAG = "websocket";
//
//    private MyWebSocket myWebSocket;
//
//    private static class SingletonHolder {
//        private static final WebSocketHelper instance = new WebSocketHelper();
//    }
//
//    private WebSocketHelper() {
//
//    }
//
//    public static WebSocketHelper getInstance() {
//        return WebSocketHelper.SingletonHolder.instance;
//    }
//
//
//    private boolean isRunning = false;
//
//    private boolean isInterrupt = false;
//
//    public void conn() {
//
//        if (isRunning) {
//            return;
//        }
//
//        isInterrupt = false;
//
//
//        Runnable runnable = new Runnable() {
//            @Override
//            public void run() {
//
//                while (!isInterrupt) {
//
//                    isRunning = true;
//
//                    if (isOpenOk) {
//
//                        sendMsg("111");
//
//                        try {
//                            Thread.sleep(20 * 1000);
//                        } catch (Exception e) {
//                            e.printStackTrace();
//                        }
//
//                    } else {
//
//                        try {
//                            String url = "ws://" + Constants.IP + ":" + Constants.PORT + "/websocket/t_" + Constants.terminal_id;
//                            Log.e(TAG, url);
//                            myWebSocket = new MyWebSocket(new URI(url), new Draft_6455());
//                            myWebSocket.connectBlocking(20, TimeUnit.SECONDS);
//                        } catch (Exception e) {
//                            Log.e(TAG, e.getMessage());
//                        }
//
//                        try {
//                            Thread.sleep(5 * 1000);
//                        } catch (Exception e) {
//                            e.printStackTrace();
//                        }
//
//                    }
//
//                }
//
//                Log.e(TAG, "run over");
//
//            }
//        };
//
//        Utils.getCachedThreadPool().execute(runnable);
//    }
//
//
//    public static boolean isOpenOk = false;
//
//    private class MyWebSocket extends WebSocketClient {
//
//        MyWebSocket(URI serverUri, Draft protocolDraft) {
//            super(serverUri, protocolDraft);
//        }
//
//        @Override
//        public void onOpen(ServerHandshake handshakedata) {
//            Log.d(TAG, "onOpen");
//
//            isOpenOk = true;
//
//        }
//
//        @Override
//        public void onMessage(String message) {
//
//            Log.d(TAG, "receive: " + message);
//
//        }
//
//        @Override
//        public void onClose(int code, String reason, boolean remote) {
//            Log.d(TAG, "Connection close by " + (remote ? "remote peer" : "us") + " at " + new Date(System.currentTimeMillis()));
//            isOpenOk = false;
//        }
//
//        @Override
//        public void onError(Exception ex) {
//            Log.d(TAG, "onError" + ex.getMessage());
//            isOpenOk = false;
//        }
//    }
//
//
//    public void sendMsg(String message) {
//
//        Log.e(TAG, "message==" + message);
//
//        try {
//
//            if (myWebSocket != null) {
//                myWebSocket.send(message);
//            }
//
//        } catch (Exception e) {
//            e.printStackTrace();
//
//            isOpenOk = false;
//
//        }
//
//    }
//
//    public void destroy() {
//
//        try {
//
//            isInterrupt = true;
//
//            if (myWebSocket != null) {
//                myWebSocket.closeBlocking();
//            }
//
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//
//
//    }
//
//}
//
//
