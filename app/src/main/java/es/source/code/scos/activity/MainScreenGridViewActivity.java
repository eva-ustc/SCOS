package es.source.code.scos.activity;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import es.source.code.scos.Constants;
import es.source.code.scos.R;
import es.source.code.scos.model.User;

import static es.source.code.scos.Constants.USER_INFO;

/**
 * @author LRK
 * @project_name SCOS
 * @package_name es.source.code.scos.activity
 * @date 2018/10/5 17:24
 * @description
 */

public class MainScreenGridViewActivity extends Activity{

    public static final String Tag = "MainScreenGridView";

    private GridView mAppGridView;
    private int[] mAppIcons = {
            R.drawable.tab_menu_meal,R.drawable.tab_menu_order,
            R.drawable.tab_menu_login,R.drawable.tab_menu_help
    };
    private String[] mAppNames = {
      "点菜","查看订单","登陆/注册","系统帮助"
    };
    private String mMsg_fromentry;
    private String mMsg_fromlogin;
    private View mItem_help;
    private View mItem_login;
    private View mItem_order;
    private View mItem_meal;
    private User mUser;
    public static List<Map<String,Object>> orderFoodLists = new ArrayList<>();
    public static List<Map<String,Object>> orderedFoodLists = new ArrayList<>();
    private SharedPreferences mLoginSetting;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mainscreen_gridview);
        Intent intent = getIntent();
        mMsg_fromentry = intent.getStringExtra(Constants.FROM_ENTRY);
        mMsg_fromlogin = intent.getStringExtra(Constants.FROM_LOGIN);

        initView();
        if (!TextUtils.isEmpty(mMsg_fromentry)) {
            Toast.makeText(this, mMsg_fromentry,Toast.LENGTH_SHORT).show();
        }
        if (!TextUtils.isEmpty(mMsg_fromlogin) ) {
            if ("LoginSuccess".equals(mMsg_fromlogin) || "RegisterSuccess".equals(mMsg_fromlogin)){
                mUser = intent.getParcelableExtra(USER_INFO);
                updateState(true,mUser);
            }else if ("Return".equals(mMsg_fromlogin)){

            }else {
                mUser = null;
            }
//            Toast.makeText(this, mMsg_fromlogin,Toast.LENGTH_SHORT).show();
        }
        if (mUser != null && !mUser.getOldUser()) {
            Log.d(Tag , mUser.toString());
            Toast.makeText(this,"欢迎您成为SCOS新用户: "+mUser.getUserName(),Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onDestroy() {
        updateState(false,null);
        super.onDestroy();
    }

    private void updateState(boolean loginState,User user){
        mLoginSetting = this.getSharedPreferences(Constants.SHAREDPREFERENCE_USER_INFO, MODE_PRIVATE);
        // 获取编辑器 开启编辑模式
        SharedPreferences.Editor edit = mLoginSetting.edit();
        if (user!=null){
            // 保存变量
            edit.putBoolean(Constants.SP_LOGINSTATE,loginState);
            edit.putString(Constants.SP_USERNAME,user.getUserName());
//            edit.putString("password",user.getPassword());
//            edit.putBoolean("isOldUser",user.getOldUser());
        }else {
            edit.clear();
        }
        // 提交修改 保存到SharePreference
        edit.commit();
    }

    /**
     * 获得焦点时触发,判断是否显示点菜和查看订单功能
     * @param hasFocus
     */
    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        if (hasFocus){
            mItem_meal = mAppGridView.getChildAt(0);
            mItem_order = mAppGridView.getChildAt(1);
//            if (mMsg_fromentry!=null && !Constants.FROM_ENTRY.equals(mMsg_fromentry)) {
            if (!isRegister()) {
                mItem_meal.setVisibility(View.INVISIBLE);
                mItem_order.setVisibility(View.INVISIBLE);
            }else {
                mItem_meal.setVisibility(View.VISIBLE);
                mItem_order.setVisibility(View.VISIBLE);
            }
            mItem_login = mAppGridView.getChildAt(2);
            mItem_help= mAppGridView.getChildAt(3);
            /*if (isRegister()){ *//*|| mUser!=null*//*
                mItem_meal.setVisibility(View.VISIBLE);
                mItem_order.setVisibility(View.VISIBLE);
            }else {
                mItem_meal.setVisibility(View.INVISIBLE);
                mItem_order.setVisibility(View.INVISIBLE);
            }*/
        }
        super.onWindowFocusChanged(hasFocus);
    }

    /**
     * 判断是否已登录
     * @return
     */
    private boolean isRegister(){
        mLoginSetting = this.getSharedPreferences(Constants.SHAREDPREFERENCE_USER_INFO, MODE_PRIVATE);
        return mLoginSetting.getBoolean(Constants.SP_LOGINSTATE,false);
    }

    /**
     * 初始化GridView界面
     */
    private void initView() {

        if (isRegister()){  // 已登录
            mUser = new User();
            mUser.setOldUser(this.getSharedPreferences("settings_info",MODE_PRIVATE).getBoolean("isOldUser",false));
        }
        // 获取界面GridView
        mAppGridView = this.findViewById(R.id.gridview_menu);
        // 初始化数据.创建一个List对象,存储所有的导航项
        List<Map<String,Object>> listItems = new ArrayList<>();
        for (int i = 0; i< mAppNames.length;i++){
            Map<String,Object> listItem = new HashMap<>(); // 导航项
            listItem.put("icon",mAppIcons[i]);
            listItem.put("name",mAppNames[i]);
            listItems.add(listItem);
        }

        SimpleAdapter simpleAdapter = new SimpleAdapter(this,listItems,R.layout.gridview_item,
                new String[]{"icon","name"},new int[]{R.id.icon_img,R.id.tv_name});

        mAppGridView.setAdapter(simpleAdapter);
//        mAppGridView.getAdapter().getView(0,null,null).setVisibility(View.INVISIBLE);
        mAppGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent;
               switch (position){
                   case 0: // 点菜
                       Toast.makeText(MainScreenGridViewActivity.this,"先点菜吧...",Toast.LENGTH_SHORT).show();
                       ActivityUtils.redirectTo(MainScreenGridViewActivity.this,FoodViewActivity.class);
                       break;
                   case 1: // 查看订单
                       Toast.makeText(MainScreenGridViewActivity.this,"看下订单...",Toast.LENGTH_SHORT).show();
                       ActivityUtils.redirectTo(MainScreenGridViewActivity.this,FoodOrderViewActivity.class,mUser,0);
                       break;
                   case 2: // 登陆
                       ActivityUtils.redirectTo(MainScreenGridViewActivity.this,LoginOrRegisterActivity.class);
                       break;
                   case 3: // 系统帮助
                       Toast.makeText(MainScreenGridViewActivity.this,"你~需要我的帮助...",Toast.LENGTH_SHORT).show();
                       intent = new Intent(MainScreenGridViewActivity.this,SCOSHelperActivity.class);
                       startActivity(intent);
                       break;
               }
            }
        });

    }


}
