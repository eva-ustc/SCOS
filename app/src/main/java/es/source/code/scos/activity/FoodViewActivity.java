package es.source.code.scos.activity;

import android.content.Intent;
import android.os.Bundle;
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

import es.source.code.scos.Constants;
import es.source.code.scos.R;
import es.source.code.scos.adapters.FoodPageAdapter;
import es.source.code.scos.fragments.FoodFragment;
import es.source.code.scos.model.User;

import static es.source.code.scos.activity.MainScreenGridViewActivity.Tag;
import static es.source.code.scos.activity.MainScreenGridViewActivity.orderFoodLists;

/**
 * @author LRK
 * @project_name SCOS
 * @package_name es.source.code.scos.activity
 * @date 2018/10/5 20:23
 * @description
 */

public class FoodViewActivity extends AppCompatActivity {

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
            case R.id.action_view_order:
                Toast.makeText(this,"查看订单",Toast.LENGTH_SHORT).show();
                // TODO 跳转FoodOrderView 传入user 默认显示已下菜单
              ActivityUtils.redirectTo(this,FoodOrderViewActivity.class,mUser,1);
                return true;
            case R.id.action_call:
                Toast.makeText(this,"呼叫服务",Toast.LENGTH_SHORT).show();
                // TODO
                return true;
            default:
                return true;
        }
    }
}

