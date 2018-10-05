package es.source.code.scos.activity;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;

import com.ashokvarma.bottomnavigation.BottomNavigationBar;
import com.ashokvarma.bottomnavigation.BottomNavigationItem;

import es.source.code.scos.R;

/**
 * @author LRK
 * @project_name SCOS
 * @package_name es.source.code.scos.activity
 * @date 2018/10/4 13:01
 * @description
 */

public class MainScreen2Activity extends Activity implements BottomNavigationBar.OnTabSelectedListener {

    private BottomNavigationBar mBnb_container;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mainscreen2);
        initView();
    }

    private void initView() {
        mBnb_container = this.findViewById(R.id.bnb_container);
        mBnb_container.setAutoHideEnabled(false); // 设置不需要自动隐藏
        mBnb_container.setMode(BottomNavigationBar.MODE_DEFAULT);
        mBnb_container.setBackgroundStyle(BottomNavigationBar.BACKGROUND_STYLE_STATIC);
        mBnb_container.setBarBackgroundColor("#ffffff"); // 白色
        mBnb_container.setInActiveColor("#DCDCDC"); // 灰色
        mBnb_container.setActiveColor("#696969"); // 暗灰色

        // 创建选项
        BottomNavigationItem mealItem = new BottomNavigationItem(R.drawable.meal,"点菜");
        BottomNavigationItem orderItem = new BottomNavigationItem(R.drawable.order,"查看订单");
        BottomNavigationItem loginItem = new BottomNavigationItem(R.drawable.login,"登陆/注册");
        BottomNavigationItem helpItem = new BottomNavigationItem(R.drawable.help,"系统帮助");

        mBnb_container.addItem(mealItem).addItem(orderItem).addItem(loginItem).addItem(helpItem);
        mBnb_container.initialise();
        mBnb_container.setTabSelectedListener(this);
    }

    /**
     * 底部监听
     * @param position
     */
    @Override
    public void onTabSelected(int position) {

    }

    @Override
    public void onTabUnselected(int position) {

    }

    @Override
    public void onTabReselected(int position) {

    }
}
