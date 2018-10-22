package es.source.code.scos.activity;

import android.Manifest;
import android.app.Activity;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.telephony.PhoneNumberUtils;
import android.telephony.SmsManager;
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

import static android.R.attr.data;
import static es.source.code.scos.activity.MainScreenGridViewActivity.Tag;

/**
 * @author LRK
 * @project_name SCOS
 * @package_name es.source.code.scos.activity
 * @date 2018/10/17 13:06
 * @description
 */

public class SCOSHelperActivity extends AppCompatActivity {

    private GridView mGridView;
    private List<Map<String, Object>> mDataList;
    private SimpleAdapter mAdapter;
    public Handler mHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case 1:
                    Toast.makeText(SCOSHelperActivity.this,"求助邮件已发送成功",Toast.LENGTH_SHORT).show();
                    break;
            }
        }
    };
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_helper);
        initData();
        initView();
    }

    /**
     * 初始化数据mDataList
     */
    private void initData() {
        int[] icons = {R.mipmap.ic_protocol,R.mipmap.ic_about,R.mipmap.ic_callphone,
                R.mipmap.ic_message,R.mipmap.ic_email};
        String[] names = {"用户使用协议","关于系统","电话人工帮助","短信帮助","邮件帮助"};
        mDataList = new ArrayList<>();
        for (int i = 0; i<icons.length; i++){
            Map<String,Object> map = new HashMap<>();
            map.put("img",icons[i]);
            map.put("text",names[i]);
            mDataList.add(map);
        }
    }
    /**
     * 初始化视图
     */
    private void initView() {
        mGridView = (GridView) this.findViewById(R.id.gridview_help);
        String[] from = {"img","text"};
        int[] to = {R.id.iv_help_img,R.id.tv_help_text};
        mAdapter = new SimpleAdapter(this,mDataList, R.layout.gridview_helper_item,from,to);
        mGridView.setAdapter(mAdapter);
        mGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                switch (position){
                    case 0:  // 用户协议
                        Toast.makeText(SCOSHelperActivity.this,"查看用户协议",Toast.LENGTH_SHORT).show();
                        break;
                    case 1:  // 关于系统
                        Toast.makeText(SCOSHelperActivity.this,"关于系统",Toast.LENGTH_SHORT).show();
                        break;
                    case 2:  // 电话人工帮助
                        doCall("5554");
                        break;
                    case 3:  // 短信帮助
//                        sendMessage("5554","test scos helper");
                        doSendMessage();
                        break;
                    case 4:  // 邮件帮助
                        sendEmail();
                        break;
                }

            }

        });
    }
    /**
     * 发短信2
     */
    private void doSendMessage() {
        if (Build.VERSION.SDK_INT >= 23) {
            int checkCallPhonePermission = ContextCompat.checkSelfPermission(this,
                    Manifest.permission.SEND_SMS);
            if (checkCallPhonePermission != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, new String[] {
                        Manifest.permission.SEND_SMS
                }, Constants.REQUEST_CODE_ASK_SEND_MESSAGE);
                return;
            } else {
                // 上面已经写好的发短信方法 callDirectly(mobile);
                sendMessage("5554","test scos helper");
            }
        } else {
            sendMessage("5554","test scos helper");
        }
        if (ContextCompat.checkSelfPermission(SCOSHelperActivity.this, Manifest.permission.SEND_SMS) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(SCOSHelperActivity.this, new String[]{Manifest.permission.SEND_SMS},1);
        }

    }
    /**
     * 发邮件
     */
    private void sendEmail() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                // 发邮件
                Intent intent = new Intent(Intent.ACTION_SENDTO);
                intent.setData(Uri.parse("mailto:1326510824@qq.com"));
                intent.putExtra(Intent.EXTRA_SUBJECT,"求助邮件的主题");
                intent.putExtra(Intent.EXTRA_TEXT,"这里是求助内容");
                //  SCOSHelperActivity.this.startActivity(intent);
                SCOSHelperActivity.this.startActivityForResult(intent,Constants.REQUEST_CODE_ASK_FOR_RESULT);
            }
        }).start();
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == Constants.REQUEST_CODE_ASK_FOR_RESULT){
            Log.d(Tag ,"resultCode: " + resultCode);
            Message message = new Message();
            message.what = 1;
            Log.d(Tag ,"data: " + data);
            mHandler.sendMessage(message);
        }
    }

    /**
     * 发短信2
     */
    private void sendMessage2(String destPhone,String content) {
        if (PhoneNumberUtils.isGlobalPhoneNumber(destPhone)){
            Intent intent = new Intent(Intent.ACTION_SENDTO, Uri.parse("smsto:"+destPhone));
            intent.putExtra("sms_body",content);
            startActivity(intent);
        }
    }

    /**
     * 打电话
     */
    public void doCall(String mobile) {
        if (Build.VERSION.SDK_INT >= 23) {
            int checkCallPhonePermission = ContextCompat.checkSelfPermission(this,
                    Manifest.permission.CALL_PHONE);
            if (checkCallPhonePermission != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, new String[] {
                        Manifest.permission.CALL_PHONE
                }, Constants.REQUEST_CODE_ASK_CALL_PHONE);
                return;
            } else {
                // 上面已经写好的拨号方法 callDirectly(mobile);
                call(mobile);
            }
        } else {
            call(mobile);
        }
    }
    /**
     * 打电话给10086
     */
    private void call(String mobile) {
        Intent intent = new Intent();

        Uri uri = Uri.parse("tel:"+mobile);
        intent.setData(uri);

        intent.setAction(Intent.ACTION_CALL);
        intent.addCategory(Intent.CATEGORY_DEFAULT);
        intent.setPackage("com.android.server.telecom");
        startActivity(intent);
    }

    /**
     * 根据获取权限的结果作不同的处理
     * @param requestCode
     * @param permissions
     * @param grantResults
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode){
            case Constants.REQUEST_CODE_ASK_SEND_MESSAGE: // 发短信
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    sendMessage("5554","test scos helper");
                } else {
                    Toast.makeText(this, "你没启动短信权限", Toast.LENGTH_SHORT).show();
                }
                break;
            case Constants.REQUEST_CODE_ASK_CALL_PHONE: // 打电话
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                   call("5554");
                } else {
                    Toast.makeText(this, "你没启动拨号权限", Toast.LENGTH_SHORT).show();
                }
                break;
            case Constants.REQUEST_CODE_ASK_SEND_EMAIL: // 发邮件
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    sendMessage("5554","test scos helper");
                } else {
                    Toast.makeText(this, "你没启动邮件权限", Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }

    private void sendMessage(String destPhone, String content) {
        PendingIntent pi = PendingIntent.getActivity(SCOSHelperActivity.this,0,new Intent(),0);
        SmsManager.getDefault().sendTextMessage(destPhone,null,content,pi,null);
        Toast.makeText(SCOSHelperActivity.this,"求助短信发送成功",Toast.LENGTH_SHORT).show();
    }
}
