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

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import es.source.code.scos.Constants;
import es.source.code.scos.EventMessage;
import es.source.code.scos.ServiceEventMessage;
import es.source.code.scos.model.Database_food;

/**
 * @author LRK
 * @project_name SCOS
 * @package_name es.source.code.scos.service
 * @date 2018/10/25 19:14
 * @description
 *  God Bless,No Bug!
 */
public class ServerObserverForEventBusService extends Service {
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        EventBus.getDefault().register(this);

    }

    @Override
    public void onDestroy() {
        EventBus.getDefault().unregister(this);
        super.onDestroy();
    }

    /**
     * 处理接收到的消息
     * @param message
     */
    @Subscribe(threadMode=ThreadMode.MAIN,sticky = true)
    public void onReceiveEvent(EventMessage message){
        int info = message.getContent();
        switch (info){
            case 1: // 接收到1,启动多线程并向客户端发送 10&库存消息
                new Thread(new MyThread()).start();
                break;
            case 0: // 接收到0,关闭多线程
                mTimer.cancel();
                send(2,null);
                break;
        }
    }

    /**
     * 向客户端发送消息 10/2
     * @param info
     * @param stockInfo
     */
    public void send(Integer info,Map<String, Object> stockInfo){
        EventMessage message = new EventMessage();
        message.setContent(info);
        if (stockInfo!=null){
            message.setFoodInfo(stockInfo);
        }
        EventBus.getDefault().post(message);
    }

    Timer mTimer = new Timer();
    private class MyThread implements Runnable{

        @Override
        public void run() {
            // 模拟服务器传回菜品库存信息
            TimerTask task = new TimerTask() {
                @Override
                public void run() {
                    if (isAppRun(ServerObserverForEventBusService.this)){
                        // 如果scos进程在运行,发送Message给scos进程
                        // 向客户端发送10,并携带库存信息
                        Map<String,Object> foodStock = new HashMap<>();
                        // TODO 结账后扣除Dababase_food的库存
                        for (Map<String,Object> stock : Database_food.foodDatabase){
                            foodStock.put((String) stock.get("foodName"),stock.get("foodStock"));
                        }
                        send(10,foodStock);
                    }
                }
            };
//            mTimer = new Timer();
            mTimer.scheduleAtFixedRate(task,0,3000);
        }
    }
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
