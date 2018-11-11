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
import android.widget.TextView;
import android.widget.Toast;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import es.source.code.scos.ActivityUtils;
import es.source.code.scos.Constants;
import es.source.code.scos.EventMessage;
import es.source.code.scos.R;
import es.source.code.scos.ServiceEventMessage;
import es.source.code.scos.adapters.FoodPageAdapter;
import es.source.code.scos.fragments.FoodFragment;
import es.source.code.scos.model.Database_food;
import es.source.code.scos.model.User;
import es.source.code.scos.service.ServerObserverForEventBusService;
import es.source.code.scos.service.ServerObserverService;

import static es.source.code.scos.activity.MainScreenGridViewActivity.orderFoodLists;

/**
 * @author LRK
 * @project_name SCOS
 * @package_name es.source.code.scos.activity
 * @date 2018/10/5 20:23
 * @description
 */

public class FoodViewForEventBusActivity extends AppCompatActivity {
    public static final String Tag = "FoodViewForEventBus";
    private ViewPager mViewPager;
    private TabLayout mTabLayout;
    private String[] titles = {"冷菜", "热菜", "海鲜", "酒水"};
    List<FoodFragment> mFragments;
    private ListView mListView;
    private User mUser;
    private boolean isBind = false;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_foodview);
        initView();
        mListView = new ListView(this);
        mUser = getIntent().getParcelableExtra(Constants.USER_INFO);
        EventBus.getDefault().register(this);
    }


    @Override
    protected void onDestroy() {
        EventBus.getDefault().unregister(this);
        super.onDestroy();
    }

    /**
     * Handler 处理接收到的消息
     * @param message
     */
    @Subscribe(threadMode=ThreadMode.MAIN,sticky = true)
    public void onReceiveEvent(EventMessage message){
        switch (message.getContent()){
            case 10:
                // TODO 传入的what值为10,解析库存信息,更新菜品信息

                Map<String,Object> foodinfo = message.getFoodInfo();
                for(String foodName : foodinfo.keySet()){
                    int stock = (int) foodinfo.get(foodName);
                    Log.d(Tag ,"菜品: "+ foodName + "库存:" +stock);
                }
                updateFoodStock(foodinfo);
                /*Bundle bundle = msg.getData();
                for (Map<String,Object> foodStock : Database_food.foodDatabase){
                    String foodName = (String) foodStock.get("foodName");
                    int stock = bundle.getInt(foodName);
                    Log.d(Tag ,"菜品: "+ foodName + "库存:" +stock);
                }*/
                break;
            case 2:
                // 传入的what值为2,解绑服务
//                unbindService(conn_begin);
                Intent intent = new Intent();
                intent.setClass(this,ServerObserverForEventBusService.class);
                stopService(intent);
                isBind = false;
                break;
        }
    }

    private void updateFoodStock(Map<String, Object> foodinfo) {
        TextView tv_stock = this.findViewById(R.id.tv_foodStockNum);
        TextView tv_foodName = this.findViewById(R.id.tv_foodName);
        Log.d(Tag ,tv_foodName.getText().toString());
        Log.d(Tag ,tv_stock.getText().toString());
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
                    sendMessage(1);
                    Toast.makeText(this,"启动实时更新",Toast.LENGTH_SHORT).show();
                    // 发送Message.what=1
                    item.setTitle("停止实时更新");
                }else if ("停止实时更新".equals(item.getTitle().toString())){
                    // 解绑服务,发送0
                    sendMessage(0);
                    item.setTitle("启动实时更新");
                }
                return true;
            default:
                return true;
        }
    }

    /**
     * 向服务端发送消息
     * @param info 1/0
     */
    private void sendMessage(int info) {
        EventMessage message = new EventMessage();
        message.setContent(info);
        EventBus.getDefault().postSticky(message);
    }


    /**
     * 点击了启动实时更新,开启服务
     */
    private boolean bindServerService(ServiceConnection conn) {

        if (!isBind){ // 如果没有绑定服务
            Intent intent = new Intent();
            intent.setClass(this,ServerObserverForEventBusService.class);
//            startService(intent);
            startService(intent);
            isBind = true;
//            isBind = bindService(intent,conn,BIND_AUTO_CREATE);
        }
        return isBind;
    }
    /**
     * 发送1启动多线程实时更新
     */
    private ServiceConnection conn_begin = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            // 客户端与Service建立连接
            Log.d(Tag ,"客户端 onServiceConnected");
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            isBind = false;
            Log.d(Tag ,"客户端 onServiceDisConnected");
        }
    };
}

