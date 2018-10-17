package es.source.code.scos.activity;

import android.os.Bundle;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Handler;
import java.util.logging.LogRecord;

import es.source.code.scos.Constants;
import es.source.code.scos.R;
import es.source.code.scos.adapters.FoodPageAdapter;
import es.source.code.scos.adapters.OrderPageAdapter;
import es.source.code.scos.fragments.OrderFragment;
import es.source.code.scos.model.Database_food;
import es.source.code.scos.model.User;

import static es.source.code.scos.activity.MainScreenGridViewActivity.orderFoodLists;
import static es.source.code.scos.activity.MainScreenGridViewActivity.orderedFoodLists;

/**
 * @author LRK
 * @project_name SCOS
 * @package_name es.source.code.scos.activity
 * @date 2018/10/6 22:03
 * @description
 */

public class FoodOrderViewActivity extends AppCompatActivity{

    private ViewPager mOrder_viewPager;
    private TabLayout mOrder_tabLayout;
    private String[] titles = {"未下单菜","已下单菜"};
    List<OrderFragment> mFragments;
    private User mUser;
    private Integer defaultPage;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_food_order_view);
        mUser = getIntent().getParcelableExtra(Constants.USER_INFO);
        defaultPage = getIntent().getIntExtra(Constants.INDEX_DEFAULT_FRAGMENT,0);
        initView();

    }

    private void initView() {
        mOrder_viewPager = (ViewPager) this.findViewById(R.id.order_viewPage);
        mOrder_tabLayout = (TabLayout) this.findViewById(R.id.order_tabLayour);

        //初始化测试数据
     /*   List<Map<String,Object>> list = new ArrayList<>();
        for(int i =0;i<Database_food.hotFoods.length;i++){
            Map<String,Object> map = new HashMap<>();
            map.put("image",Database_food.hotFoods[i]);
            map.put("foodName",Database_food.hotFoodNames[i]);
            map.put("foodPrice", Database_food.hotFoodPrices[i]);
            list.add(map);
        }*/

        mFragments = new ArrayList<>();
        for(int i=0;i<titles.length;i++){
            OrderFragment foodFragment = new OrderFragment(titles[i],orderFoodLists,orderedFoodLists);
            mFragments.add(foodFragment);
        }

        // 创建适配器
        OrderPageAdapter adapter = new OrderPageAdapter(getSupportFragmentManager(),mFragments);
        mOrder_viewPager.setAdapter(adapter);
        mOrder_viewPager.setCurrentItem(defaultPage);
        mOrder_tabLayout.setupWithViewPager(mOrder_viewPager);
        mOrder_tabLayout.setTabMode(TabLayout.MODE_FIXED);
    }
}
