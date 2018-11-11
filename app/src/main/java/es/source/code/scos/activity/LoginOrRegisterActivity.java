package es.source.code.scos.activity;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;


import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import es.source.code.scos.Constants;
import es.source.code.scos.R;
import es.source.code.scos.model.User;
/**
 * @author LRK
 * @project_name SCOS
 * @package_name es.source.code.scos.activity
 * @date 2018/10/4 19:22
 * @description
 */

public class LoginOrRegisterActivity extends Activity implements View.OnClickListener {

    public static final String Tag = "LoginOrRegisterActivity";
    private Button mBtn_login;
    private Button mBtn_return;
    private TextView mEt_account;
    private TextView mEt_password;
    private Pattern mPattern;
    private Matcher mMatcher;
    private ProgressBar mProgressBar_login;
    private TextView mTv_register;
    private SharedPreferences mLoginInfo;
    private String mSp_username;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        initView();
        initListener();

    }

    private void initListener() {

        mBtn_login.setOnClickListener(this);
        mBtn_return.setOnClickListener(this);
        mTv_register.setOnClickListener(this);
    }

    private void initView() {
        mBtn_login = this.findViewById(R.id.btn_login);
        mBtn_return = this.findViewById(R.id.btn_return);
        mTv_register = this.findViewById(R.id.tv_register);

        mEt_account = this.findViewById(R.id.et_account);
        mEt_password = this.findViewById(R.id.et_password);
        mProgressBar_login = this.findViewById(R.id.progressbar_login);

        //读取用户登录状态

        mSp_username = getUsername();
        if (mSp_username !=null) {
            // 已登录过,隐藏注册按钮,用户名显示保存的用户名
            Toast.makeText(this,"已登录~",Toast.LENGTH_SHORT).show();
            mEt_account.setText(mSp_username);
        }else {
            // 未登录过,隐藏登录按钮
            Toast.makeText(this,"未登录~",Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * 查看sharedPreference是否有用户登录信息
     * @return
     */
    private String getUsername() {
        mLoginInfo = this.getSharedPreferences(Constants.SHAREDPREFERENCE_USER_INFO,MODE_PRIVATE);
        String username = mLoginInfo.getString(Constants.SP_USERNAME, null);
       /* if (username != null) {
            return username;
        }*/
        return username;
    }

    @Override
    public void onClick(View v) {
        /**
         * 点击返回
         */
        if (v == mBtn_return){
            // 判断是否有用户名记录
            if (getUsername()!=null){
                updateUserInfo(null,false);
            }

            // 跳转到MainScreen页面,并传递"Return"参数
            redirect2MainScreen(Constants.PACKAGE_NAME, "Return",null);
        }
        // 用户名和密码校验
        boolean checkResult = checkAccount() && checkPassword();
        String str_account = mEt_account.getText().toString().trim();
        String str_password = mEt_password.getText().toString().trim();
        /**
         *  点击登陆/注册
         */
        if (v == mBtn_login){
            if (checkResult){
                updateUserInfo(mSp_username,true);
                serverValidator();

            }
        }else if (v == mTv_register){ // 注册
            if (checkResult){
                // 保存用户信息到SharedPreference
                updateUserInfo(mEt_account.getText().toString().trim(),true);

                // 构造User对象传递给MainScreen
                User loginUser = new User();
                loginUser.setUserName(str_account);
                loginUser.setPassword(str_password);
                loginUser.setOldUser(false);
//                ActivityUtils.redirectTo(this,MainScreenGridViewActivity.class,loginUser);
                redirect2MainScreen(Constants.PACKAGE_NAME,"RegisterSuccess",loginUser);
            }
        }
        Log.d(Tag,"username: " + getUsername());
    }

    /**
     * 设置get请求参数
     */
    private void serverValidator() {
        doGet("http://10.0.2.2:8080/scos/login?username="+mEt_account.getText().toString().trim()
                +"&password="+mEt_password.getText().toString().trim());
    }

    private void doGet(final String path){
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    URL url = new URL(path.trim());
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
                        String msg = baos.toString();
                        getJSONData(msg);
                        Log.d(Tag, msg);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();

    }

    private void getJSONData(String msg) {
        try {
            JSONObject root = new JSONObject(msg);
            int resultcode = root.getInt("RESULTCODE");
            if (resultcode == 1){
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        doProgress(); // 处理进度条函数
                        Toast.makeText(LoginOrRegisterActivity.this,"resultCode==1,登陆验证成功!",Toast.LENGTH_SHORT).show();
                    }
                });
            }else if (resultcode == 0){
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(LoginOrRegisterActivity.this,"resultCode==0,登陆验证失败!",Toast.LENGTH_SHORT).show();
                    }
                });
            }
            Log.d(Tag,"获取resultCode: "+ resultcode);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    /**
     *  更新用户登录状态信息
     * @param username 用户名
     * @param loginState 是否登陆
     */
    private void updateUserInfo(String username,Boolean loginState) {
        mLoginInfo = this.getSharedPreferences(Constants.SHAREDPREFERENCE_USER_INFO, MODE_PRIVATE);
        // 获取编辑器 开启编辑模式
        SharedPreferences.Editor edit = mLoginInfo.edit();
        // 保存变量
            //用户名
        if (username != null) {
            edit.putString(Constants.SP_USERNAME,username);
        }
            //登陆状态
        if (loginState != null) {
            edit.putBoolean(Constants.SP_LOGINSTATE,loginState);
        }
        // 提交修改 保存到SharePreference
        edit.commit();
    }

    /**
     * 验证密码
     */
    private boolean checkPassword() {
        String str_password = mEt_password.getText().toString().trim();
        mPattern = Pattern.compile("[a-zA-Z0-9]+");
        mMatcher = mPattern.matcher(str_password);
        if (mMatcher.matches()){
//            Toast.makeText(this,"匹配成功",Toast.LENGTH_SHORT).show();
            return true;
        }else {
            mEt_password.setError("输入密码不符合规则!");
//            Toast.makeText(this,"匹配不成功",Toast.LENGTH_SHORT).show();
            return false;
        }
    }

    /**
     * 验证账号
     */
    private boolean checkAccount() {
        String str_account = mEt_account.getText().toString().trim();
        mPattern = Pattern.compile("[a-zA-Z0-9]+");
        mMatcher = mPattern.matcher(str_account);
        if (mMatcher.matches()){
            Toast.makeText(this,"匹配成功",Toast.LENGTH_SHORT).show();
            return true;
        }else {
            mEt_account.setError("输入用户名不符合规则!");
            Toast.makeText(this,"匹配不成功",Toast.LENGTH_SHORT).show();
            return false;
        }
    }

    /**
     * 2s走完进度条
     */
    private void doProgress() {

        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                int progress = mProgressBar_login.getProgress();
                try {
                    while (progress < mProgressBar_login.getMax()) {
                        mProgressBar_login.incrementProgressBy(5);
                        progress = mProgressBar_login.getProgress();
                        Thread.sleep(100);
                    }
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        mProgressBar_login.setVisibility(View.INVISIBLE);
                        User user = new User();
                        user.setUserName(mEt_account.getText().toString().trim());
                        user.setPassword(mEt_password.getText().toString().trim());
                        user.setOldUser(true);
                        redirect2MainScreen(Constants.PACKAGE_NAME, "LoginSuccess",user);

                    }
                });
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });
        thread.start();
    }

    /**
     * 带参数跳转到MainScreen
     * @param packageName 应用包名
     * @param msg 传递的String类型参数
     * @param user  传递的User引用数据类型
     */
    private void redirect2MainScreen(String packageName, String msg, Parcelable user) {
        Intent intent = new Intent();
//        intent.setAction(ACTION_INTENT_SCOSMAIN);
        intent.setAction(Constants.ACTION_INTENT_GRIDVIEWMAIN);
        intent.addCategory(Constants.CATEGORY_INTENT_SCOSLAUNCHER);
        intent.setPackage(packageName);
        intent.putExtra(Constants.FROM_LOGIN, msg);
        if (user != null) {
            Bundle bundle = new Bundle();
            bundle.putParcelable(Constants.USER_INFO,user);
            intent.putExtras(bundle);
        }
        startActivity(intent);
    }
}
