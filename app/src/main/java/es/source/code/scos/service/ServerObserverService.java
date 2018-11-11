package es.source.code.scos.service;

import android.app.ActivityManager;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Log;

import java.lang.ref.WeakReference;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import es.source.code.scos.Constants;
import es.source.code.scos.model.Database_food;

import static es.source.code.scos.activity.MainScreenGridViewActivity.Tag;

/**
 * @author LRK
 * @project_name SCOS
 * @package_name es.source.code.scos.service
 * @date 2018/10/25 19:14
 * @description
 *  God Bless,No Bug!
 */
public class ServerObserverService extends Service {
    public static final String Tag = "ServerObserverService";
//    private MyHandler mHandler;

    // 来自客户端的Messenger
    private Messenger mClientMessenger = null;
    //指向MyHandler的Messenger
    private Messenger mServiceMessenger = new Messenger(new MyHandler());
    private Timer mTimer;

    @Override
    public void onCreate() {
        super.onCreate();
//        mHandler = new MyHandler(this);
    }

    /**
     * 处理客户端发送来的请求
     */
    private class MyHandler extends Handler{
        // 弱引用
//        WeakReference<ServerObserverService> mWeakReference;
        /*public MyHandler(ServerObserverService service){
            mWeakReference = new WeakReference<>(service);
        }*/
        Thread serviceThread = new Thread(new MyThread());

        @Override
        public void handleMessage(Message msg) {
//            super.handleMessage(msg);
            // 通过Messenger的replyTo获取客户端自身的Messenger
            // Service可以通过它向客户端发送message
            mClientMessenger = msg.replyTo;

            switch (msg.what){
                case 1:
                    if (mClientMessenger!=null){
                        // TODO 启动多线程模拟器接收服务器传回菜品库存信息
//                        startThread();
                        serviceThread.start();
                    }

                    break;
                case 0:
                    // 关闭模拟接收服务器传回菜品库存信息的多进程
//                    serviceThread.stop();
                    Log.d(Tag ,"serviceThread isAlive? :"+serviceThread.isAlive());
                    mTimer.cancel();
                    Message message = Message.obtain();
                    message.what = 2;
                    try {
                        mClientMessenger.send(message);
                    } catch (RemoteException e) {
                        e.printStackTrace();
                    }
                    break;
            }
        }

        /**
         * 启动多线程,休眠频率300ms
         */
        private void startThread() {
            TimerTask task = new TimerTask() {
                @Override
                public void run() {
                    if (!serviceThread.isAlive()){
                        serviceThread.start();
                    }
                }
            };
            Timer timer = new Timer();
            timer.scheduleAtFixedRate(task,0,3000);

        }
    }

    private class MyThread implements Runnable{


        @Override
        public void run() {
            // 模拟服务器传回菜品库存信息
            TimerTask task = new TimerTask() {
                @Override
                public void run() {
                    if (isAppRun(ServerObserverService.this)){
                        // 如果scos进程在运行,发送Message给scos进程
                        Message msgToClient = Message.obtain();
                        // 向客户端发送10,并携带库存信息
                        msgToClient.what = 10;
                        Bundle bundle = new Bundle();
                        // TODO 结账后扣除Dababase_food的库存
                        for (Map<String,Object> foodStock : Database_food.foodDatabase){
                            bundle.putInt((String) foodStock.get("foodName"),(Integer) foodStock.get("foodStock"));
                        }
                        msgToClient.setData(bundle);
                        try {
                            mClientMessenger.send(msgToClient);
                            Log.d(Tag ,"thread: "+Thread.currentThread().getName() +"alive? :"+ Thread.currentThread().isAlive());
                        } catch (RemoteException e) {
                            e.printStackTrace();
                        }
                    }
                }
            };
            mTimer = new Timer();
            mTimer.scheduleAtFixedRate(task,0,3000);
        }
    }
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        // 返回Service自身Messenger所对应的IBinder,并将其发送共享给客户端
        return mServiceMessenger.getBinder();
    }

    @Override
    public void onDestroy() {
        mClientMessenger = null;
        super.onDestroy();
//        mHandler.removeCallbacksAndMessages(null);
    }

    /**
     * 判断app是否在运行
     * @param context
     * @return
     */
    private boolean isAppRun(Context context){

        boolean isAppRunnig = false;
        ActivityManager manager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningTaskInfo> list = manager.getRunningTasks(100);
        for (ActivityManager.RunningTaskInfo info: list) {
            if (TextUtils.equals(info.topActivity.getPackageName(),Constants.MY_PKG_NAME)||
                    TextUtils.equals(info.baseActivity.getPackageName(),Constants.MY_PKG_NAME)){
                isAppRunnig = true;
                break;
            }
        }
        return isAppRunnig;
    }
}
