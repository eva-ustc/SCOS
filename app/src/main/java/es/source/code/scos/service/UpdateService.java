package es.source.code.scos.service;

import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.nfc.Tag;
import android.support.annotation.Nullable;
import android.util.Log;
import android.widget.Button;
import android.widget.RemoteViews;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import es.source.code.scos.R;
import es.source.code.scos.activity.FoodDetailedActivity;
import es.source.code.scos.model.Database_food;

/**
 * @author LRK
 * @project_name SCOS
 * @package_name es.source.code.scos.service
 * @date 2018/10/27 19:16
 * @description
 * God Bless,No Bug!
 */
public class UpdateService extends IntentService {

    private static final int Notification_ID = 0;
    private static final String BTN_CLICK_ACTION = "scos.intent.action.CLEAR";
    private NotificationManager mManager; // NotificationManager
    private static final String Tag = "UpdateService";

    /**
     * Creates an IntentService.  Invoked by your subclass's constructor.
     *
     */
    public UpdateService() {

        super("UpdateService");
    }
    @Override
    public void onCreate() {
        super.onCreate();
        mManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        /**
         * 模拟检查服务器是否有菜品更新信息,如果有则使用NotificationManager发送状态栏通知
         * 通知内容为"新品上架:菜品,价格,类型"
         * 点击该通知,屏幕跳转到FoodDetailed界面,显示该菜品详细信息
         */
        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
//        Map<String,Object> add_food = new HashMap<>(); // 新加菜品

        /*add_food.put("image", R.mipmap.ic_xilianghecuyu);
        add_food.put("foodName","西凉河醋鱼");
        add_food.put("foodPrice", 69.5f);
        add_food.put("foodStock",10);
        Database_food.foodDatabase.add(add_food);*/
        // 处理服务器端的菜品更新信息
        handleServerAddFood();

        // 发送通知
//        sendNotification(add_food);
    }

    /**
     * 获取服务器传回的菜品更新信息
     * @return
     */
    private void handleServerAddFood() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    URL url = new URL("http://10.0.2.2:8080/scos/updateJSONFood");
                    HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
                    if (200 == urlConnection.getResponseCode()){
                        InputStream is = urlConnection.getInputStream();
                        ByteArrayOutputStream baos = new ByteArrayOutputStream();
                        byte[] buffer = new byte[1024];
                        int len = 0;
                        while (-1 != (len = is.read(buffer))){
                            baos.write(buffer,0,len);
                            baos.flush();
                        }
                        String msg_addFood = baos.toString();
                        Log.d(Tag ,msg_addFood);
                        handleAddFood(msg_addFood);
                    }

                } catch (MalformedURLException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            private void handleAddFood(String msg_addFood) {
                try {
                    JSONObject foodInfo = new JSONObject(msg_addFood);
                    Map<String,Object> foodInfo_map = new HashMap<>();
                    String foodName = foodInfo.getString("foodName");
                        foodInfo_map.put("foodName", foodName!=null?foodName:null);
                    Double foodPrice = foodInfo.getDouble("foodPrice");
                    foodInfo_map.put("foodPrice", foodPrice!=null?foodPrice:null);
                    Integer foodStock = foodInfo.getInt("foodStock");
                    foodInfo_map.put("foodStock", foodStock!=null?foodStock:null);
                    Integer foodType = foodInfo.getInt("foodType");
                    foodInfo_map.put("foodType", foodType!=null?foodType:null);
                    foodInfo_map.put("image", R.mipmap.ic_xilianghecuyu);
                    Database_food.foodDatabase.add(foodInfo_map);
                    sendNotification(foodInfo_map);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    /**
     * 发送通知
     * @param add_food
     */
    private void sendNotification(Map<String, Object> add_food) {

        if (add_food!=null){

            // 1 调用getSystemService()获取系统的NotificationManager服务
            Notification notification;

            // 2 创建一个Notification对象并为其设置各种属性
            Notification.Builder builder = new Notification.Builder(this);

            builder.setSmallIcon(R.mipmap.ic_launcher_logo); // 设置图标
            // 添加button
            RemoteViews remoteViews = new RemoteViews(getPackageName(),R.layout.notification);
//            remoteViews.setImageViewResource(R.id.img_logoNotif,R.mipmap.ic_launcher_logo);
            remoteViews.setTextViewText(R.id.tv_foodTitleNotif,"新增菜品");
            remoteViews.setTextViewText(R.id.tv_foodInfoNotif,(String)add_food.get("foodName")+","+add_food.get("foodPrice")+","+
                    "热菜");

            Intent btnIntent = new Intent(BTN_CLICK_ACTION);
            PendingIntent clearIntent = PendingIntent.getBroadcast(this,0,btnIntent,0);
            remoteViews.setOnClickPendingIntent(R.id.btn_clearNotification,clearIntent);

            builder.setContent(remoteViews);

            builder.setDefaults(Notification.DEFAULT_SOUND); // 设置更新提示音

            /*builder.setTicker("新增了菜品,赶快来看看吧~"); // 设置状态栏提示
            builder.setContentTitle("新增菜品"); // 设置标题
            builder.setContentText((String)add_food.get("foodName")+","+add_food.get("foodPrice")+","+
                    "热菜");  // 设置内容*/
            builder.setWhen(System.currentTimeMillis()); // 设置时间

            // 3 为Notification对象设置事件信息
            Intent intent = new Intent(this,FoodDetailedActivity.class);
            PendingIntent pendingIntent = PendingIntent.getActivity(this,0,intent,0);
            builder.setContentIntent(pendingIntent); // 设置点击后的意图
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.JELLY_BEAN) {
                notification = builder.build();

            }else {
                notification = builder.getNotification();
            }
            notification.flags = Notification.FLAG_AUTO_CANCEL;
            // 4 通过NotificationManager的notify()方法发送状态栏通知
            mManager.notify(Notification_ID,notification);

            BroadcastReceiver onClickReceiver = new BroadcastReceiver() {
                @Override
                public void onReceive(Context context, Intent intent) {
                    if (intent.getAction().equals(BTN_CLICK_ACTION)){
                        mManager.cancel(Notification_ID);
                    }
                }
            };
            IntentFilter filter = new IntentFilter();
            filter.addAction(BTN_CLICK_ACTION);
            registerReceiver(onClickReceiver,filter);
        }
    }
}
