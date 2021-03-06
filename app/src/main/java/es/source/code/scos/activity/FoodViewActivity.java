package es.source.code.scos.activity;

import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import es.source.code.scos.ActivityUtils;
import es.source.code.scos.Constants;
import es.source.code.scos.R;
import es.source.code.scos.adapters.FoodPageAdapter;
import es.source.code.scos.fragments.FoodFragment;
import es.source.code.scos.model.Database_food;
import es.source.code.scos.model.User;
import es.source.code.scos.service.ServerObserverService;

import static es.source.code.scos.activity.MainScreenGridViewActivity.orderFoodLists;

/**
 * @author LRK
 * @project_name SCOS
 * @package_name es.source.code.scos.activity
 * @date 2018/10/5 20:23
 * @description
 */

public class FoodViewActivity extends AppCompatActivity {
    public static final String Tag = "FoodViewActivity";
    private ViewPager mViewPager;
    private TabLayout mTabLayout;
    private String[] titles = {"冷菜", "热菜", "海鲜", "酒水"};
    List<FoodFragment> mFragments;
    private ListView mListView;
    private User mUser;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_foodview);
        initView();
        mListView = new ListView(this);
        mUser = getIntent().getParcelableExtra(Constants.USER_INFO);
    }

    private void initView() {
        mTabLayout = (TabLayout) this.findViewById(R.id.tabLayour);
        mViewPager = (ViewPager) this.findViewById(R.id.viewPage);
        // 初始化数据
        mFragments = new ArrayList<>();
        for (int i = 0; i < titles.length; i++) {
            //TODO
            FoodFragment foodFragment = new FoodFragment(titles[i], mListView);
            mFragments.add(foodFragment);
        }

        // 创建适配器
        FoodPageAdapter adapter = new FoodPageAdapter(getSupportFragmentManager(),mFragments);
        mViewPager.setAdapter(adapter);
        mTabLayout.setupWithViewPager(mViewPager);
        mTabLayout.setTabMode(TabLayout.MODE_FIXED);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.actionbar_menu,menu);
        return super.onCreateOptionsMenu(menu);
    }

    /**
     * ActionBar 点击事件
     * @param item
     * @return
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
//        Intent intent;
        switch (item.getItemId()){
            case R.id.action_orders:
                Toast.makeText(this,"已点菜品"+ orderFoodLists.size(),Toast.LENGTH_SHORT).show();
                for(Map<String,Object> map : orderFoodLists){
                    Log.d(Tag ,"image: " + map.get("image"));
                    Log.d(Tag ,"foodName: " + map.get("foodName"));
                    Log.d(Tag ,"foodPrice: " + map.get("foodPrice"));
                }
                // TODO 跳转FoodOrderView 传入user 默认显示未下菜单
               ActivityUtils.redirectTo(this,FoodOrderViewActivity.class,mUser,0);
                return true;
            case R.id.action_view_order: // 已点菜品
                Toast.makeText(this,"查看订单",Toast.LENGTH_SHORT).show();
                // TODO 跳转FoodOrderView 传入user 默认显示已下菜单
              ActivityUtils.redirectTo(this,FoodOrderViewActivity.class,mUser,1);
                return true;
            case R.id.action_call:
                Toast.makeText(this,"呼叫服务",Toast.LENGTH_SHORT).show();
                // TODO
                return true;
            case R.id.action_begin_or_stop_update: // 开始或停止实时更新
                Log.d(Tag ,item.getTitle().toString());
                // 开启服务,

                if ("启动实时更新".equals(item.getTitle().toString())){
                    bindServerService(conn_begin);
                    Toast.makeText(this,"启动实时更新",Toast.LENGTH_SHORT).show();
                    // 发送Message.what=1
                    item.setTitle("停止实时更新");
                }else {
                    sendWhat2Service(0);
                    item.setTitle("启动实时更新");
                }
                return true;
            default:
                return true;
        }
    }

    private boolean isBind = false;
    private Messenger mServerMessenger = null;
    private Messenger mClientMessenger = new Messenger(new ClientHandler());
    private class ClientHandler extends Handler{

        @Override
        public void handleMessage(Message msg) {
//            super.handleMessage(msg);
            switch (msg.what){
                case 10:
                    // TODO 传入的what值为10,解析库存信息,更新菜品信息
                    Bundle bundle = msg.getData();
                    for (Map<String,Object> foodStock : Database_food.foodDatabase){
                        String foodName = (String) foodStock.get("foodName");
                        int stock = bundle.getInt(foodName);
                        Log.d(Tag ,"菜品: "+ foodName + "库存:" +stock);
                    }
                    break;
                case 2:
                    // 传入的what值为2,解绑服务
                    unbindService(conn_begin);
                    isBind = false;
                    break;
            }
        }
    }

    /**
     * 发送1启动多线程实时更新
     */
    private ServiceConnection conn_begin = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            // 客户端与Service建立连接
            Log.d(Tag ,"客户端 onServiceConnected");

            // 从Service的onBind()方法返回的IBinder初始化一个指向Service端的Messenger
            mServerMessenger = new Messenger(service);
//            isBind = true;
            sendWhat2Service(1);
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            mServerMessenger = null;
            isBind = false;
            Log.d(Tag ,"客户端 onServiceDisConnected");
        }
    };
/*    private ServiceConnection conn_stop = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            // 客户端与Service建立连接
            Log.d(Tag ,"客户端 onServiceConnected");

            // 从Service的onBind()方法返回的IBinder初始化一个指向Service端的Messenger
            mServerMessenger = new Messenger(service);
//            isBind = true;
            sendWhat2Service(0);
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            mServerMessenger = null;
            isBind = false;
            Log.d(Tag ,"客户端 onServiceDisConnected");
        }
    };*/

    private void sendWhat2Service(int what) {
        Message msg = Message.obtain();
        msg.what = what;
        // 将Message 的replyTo设置为客户端的ClientMessenger,以便Service可以向客户端发送消息
        msg.replyTo = mClientMessenger;
        try {
            Log.d(Tag ,"向Service发送Message.what=1/0");
            mServerMessenger.send(msg);
        } catch (RemoteException e) {
            Log.d(Tag ,"向Service发送Message失败!");
            e.printStackTrace();
        }
    }

    /**
     * 点击了启动实时更新,开启服务
     */
    private boolean bindServerService(ServiceConnection conn) {

        if (!isBind){ // 如果没有绑定服务
            Intent intent = new Intent();
            intent.setClass(this,ServerObserverService.class);
//            startService(intent);
            isBind = bindService(intent,conn,BIND_AUTO_CREATE);
            return isBind;
        }
        return false;
    }
}

