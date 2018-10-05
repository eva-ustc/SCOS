package es.source.code.scos.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import es.source.code.scos.Constants;
import es.source.code.scos.R;

/**
 * @author LRK
 * @project_name SCOS
 * @package_name es.source.code.scos.activity
 * @date 2018/10/4 13:01
 * @description
 */

public class MainScreenActivity extends Activity implements View.OnClickListener {

    private TextView mTv_meal;
    private TextView mTv_order;
    private TextView mTv_login;
    private TextView mTv_help;
    private String mMsg_fromentry;
    private String mMsg_fromlogin;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mainscreen);
        Intent intent = getIntent();
        mMsg_fromentry = intent.getStringExtra(Constants.FROM_ENTRY);
        mMsg_fromlogin = intent.getStringExtra(Constants.FROM_LOGIN);
        // 初始化界面
        initView();
        // 绑定点击事件
        initListener();
        if (!TextUtils.isEmpty(mMsg_fromentry)) {
            Toast.makeText(this, mMsg_fromentry,Toast.LENGTH_SHORT).show();
        }
        if (!TextUtils.isEmpty(mMsg_fromlogin)) {
            Toast.makeText(this, mMsg_fromlogin,Toast.LENGTH_SHORT).show();
        }
    }
    private void initListener() {

        mTv_login.setOnClickListener(this);
        mTv_help.setOnClickListener(this);
        mTv_login.setOnClickListener(this);
    }
    private void initView() {
        mTv_meal = this.findViewById(R.id.tv_meal);
        mTv_order = this.findViewById(R.id.tv_order);
        if (mMsg_fromentry!=null && !Constants.FROM_ENTRY.equals(mMsg_fromentry)) {
            mTv_meal.setVisibility(View.INVISIBLE);
            mTv_order.setVisibility(View.INVISIBLE);
        }else {
            mTv_meal.setOnClickListener(this);
            mTv_order.setOnClickListener(this);
        }
        mTv_login = this.findViewById(R.id.tv_login);
        mTv_help = this.findViewById(R.id.tv_help);

        if (mMsg_fromlogin!=null && "LoginSuccess".equals(mMsg_fromlogin)){
            mTv_meal.setVisibility(View.VISIBLE);
            mTv_order.setVisibility(View.VISIBLE);
        }else {
            mTv_meal.setVisibility(View.INVISIBLE);
            mTv_order.setVisibility(View.INVISIBLE);
        }
    }
    /**
     * 处理点击事件
     * @param view
     */
    @Override
    public void onClick(View view) {
        if (view==mTv_meal){
            Toast.makeText(this,"先点菜吧...",Toast.LENGTH_SHORT).show();
        }else if (view == mTv_order){
            Toast.makeText(this,"看下订单...",Toast.LENGTH_SHORT).show();
        }else if (view == mTv_login){
            Intent intent = new Intent(this,LoginOrRegisterActivity.class);
            startActivity(intent);

        }else if (view == mTv_help){
            Toast.makeText(this,"你~需要我的帮助...",Toast.LENGTH_SHORT).show();
        }
    }
}
