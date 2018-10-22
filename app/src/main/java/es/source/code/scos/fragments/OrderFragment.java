package es.source.code.scos.fragments;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import es.source.code.scos.Constants;
import es.source.code.scos.R;
import es.source.code.scos.adapters.OrderListViewAdapter;
import es.source.code.scos.model.User;

import static es.source.code.scos.activity.MainScreenGridViewActivity.Tag;
import static es.source.code.scos.activity.MainScreenGridViewActivity.orderFoodLists;
import static es.source.code.scos.activity.MainScreenGridViewActivity.orderedFoodLists;

/**
 * @author LRK
 * @project_name SCOS
 * @package_name es.source.code.scos.fragments
 * @date 2018/10/6 13:11
 * @description 订单展示页面
 */
@SuppressLint("ValidFragment")
public class OrderFragment extends Fragment {

    private Context mContext;
//    private TextView mTextView;
    private ListView mListView;
    private String mTitle;
    private Button mBtn_submit;
    public Handler mHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){  // 根据传递的数据判断是到哪个界面,刷新UI以及数据
                case 0:
                    mBtn_submit.setText("结账");
                    initBottom(mOrderedList);
                    mListView.setAdapter(new OrderListViewAdapter(getActivity(),getData(),false));
                    break;
                case 1:
                    mBtn_submit.setText("提交订单");
                    initBottom(mOrderList);
                    // TODO 模拟结账功能
//                    mMyAsyncTask.execute();
                    mListView.setAdapter(new OrderListViewAdapter(getActivity(),getData(),true));
                    break;
                // TODO 取消付款按钮
            }
        }
    };
    private MyAsyncTask mMyAsyncTask;
    private TextView mTv_progressInfo;

    // AsyncTask内部实现类
    private class MyAsyncTask extends AsyncTask<String,Integer,String>{
        /**
         * 方法1：onPreExecute（）
         *  作用：执行 线程任务前的操作
         */
        @Override
        protected void onPreExecute() {
           mTv_progressInfo.setText("加载中...");
        }

        /**
         * 作用：接收输入参数、执行任务中的耗时操作、返回 线程任务执行的结果
         *   此处通过计算从而模拟“加载进度”的情况
         * @param params
         * @return
         */
        @Override
        protected String doInBackground(String... params) {
            try{
                int count =0;
                while (count < 100){
                    count++;
                    publishProgress(count);
                    Log.d(Tag ,"count == " +count);
                    Thread.sleep(60);
                }
            }catch (InterruptedException e) {
                e.printStackTrace();
            }
            return null;
        }

        /**
         * 作用：在主线程 显示线程任务执行的进度
         * @param values
         */
        @Override
        protected void onProgressUpdate(Integer... values) {

            mProgressBar_submit.setProgress(values[0]);
            switch (values[0]/10%3){
                case 0:
                    mTv_progressInfo.setText("loading.");
                    break;
                case 1:
                    mTv_progressInfo.setText("loading..");
                    break;
                case 2:
                    mTv_progressInfo.setText("loading...");
                    break;

            }

        }

        /**
         * 作用：接收线程任务执行结果、将执行结果显示到UI组件
         * @param s
         */
        @Override
        protected void onPostExecute(String s) {
            mTv_progressInfo.setText("处理完毕...");
            mBtn_submit.setEnabled(false);
            Toast.makeText(mContext,"交易完成!结账金额:" +getSum(getData()) +"元",Toast.LENGTH_SHORT).show();
            orderedFoodLists.clear();
            initBottom(getData());
            mListView.setAdapter(new OrderListViewAdapter(getActivity(),getData(),false));
        }

        /**
         * 作用：将异步任务设置为：取消状态
         */
        @Override
        protected void onCancelled() {
            mTv_progressInfo.setText("已取消...");
            mProgressBar_submit.setProgress(0);

        }
    }

    private List<Map<String, Object>> mOrderList = new ArrayList<>();
    private List<Map<String, Object>> mOrderedList = new ArrayList<>();

    private TextView mTv_orderSumCount;
    private TextView mTv_orderSumPrice;
    private ProgressBar mProgressBar_submit;
    //    private String mContent;


    /**
     * 得到标题
     * @return
     */
    public String getTitle() {
        return mTitle;
    }

    public OrderFragment(){}

    public OrderFragment(String title, List<Map<String,Object>> orderList,List<Map<String,Object>> orderedList){
        super();
        mTitle = title;
        mOrderList = orderList;
        mOrderedList = orderedList;
    }
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(Tag ,"onCreate...");
        mContext = getActivity();
    }

    /**
     * 创建页面
     * @param inflater
     * @param container
     * @param savedInstanceState
     * @return
     */
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(Tag ,"onCrreateView...");
        mBtn_submit = getActivity().findViewById(R.id.btn_submit);
        mTv_orderSumCount = getActivity().findViewById(R.id.tv_orderSumCount);
        mTv_orderSumPrice = getActivity().findViewById(R.id.tv_orderSumPrice);
        mProgressBar_submit = getActivity().findViewById(R.id.progressbar_submit);
        mTv_progressInfo = getActivity().findViewById(R.id.tv_progressInfo);
        mMyAsyncTask = new MyAsyncTask();

        View view = inflater.inflate(R.layout.food_fragement,container,false);
        mListView = (ListView) view.findViewById(R.id.list);
        List<Map<String,Object>> list = getData();

        if ("未下单菜".equals(mTitle)){
            mListView.setAdapter(new OrderListViewAdapter(getActivity(),list,true));
            mBtn_submit.setText("提交订单");
        }
        else {
            mListView.setAdapter(new OrderListViewAdapter(getActivity(),list,false));
            mBtn_submit.setText("结账");
        }
        mBtn_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO 订单状态改变后刷新fragment
                if ("提交订单".equals(((Button)v).getText())) { //提交订单
                    orderedFoodLists.addAll(orderFoodLists);
                    orderFoodLists.clear();
                    initBottom(getData());
                    mListView.setAdapter(new OrderListViewAdapter(getActivity(),getData(),true));
                    Toast.makeText(mContext,"订单提交成功",Toast.LENGTH_SHORT).show();
                }else {  // 结账
                    mMyAsyncTask.execute(); // 启用AsyncTask
                    User user = getActivity().getIntent().getParcelableExtra(Constants.USER_INFO);
                    if (user !=null && user.getOldUser()){
                        Toast.makeText(mContext,"您好,老顾客,本次您可享受7折优惠",Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });

        return view;
       /* // 创建视图
        mTextView = new TextView(mContext);
        mTextView.setTextColor(Color.RED);
//        String name = this.getArguments().getString("title");
        mTextView.setBackgroundColor(Color.TRANSPARENT);
//        mTextView.setText(name);
        return mTextView;*/
    }

    private void initBottom(List<Map<String,Object>> mList) {
        if (mList != null) {
            mTv_orderSumCount.setText("菜品总数:"+mList.size());
            float sum = getSum(mList);
            mTv_orderSumPrice.setText("订单总价:" + sum+ "元");
        }
    }

    private float getSum(List<Map<String, Object>> mList) {
        float sum = 0;
        for(Map<String,Object> map : mList){
            sum += (Float) map.get("foodPrice");
        }
        return sum;
    }
    /**
     * 给listView传入数据
     * @return
     */
    private List<Map<String, Object>> getData() {
        switch (mTitle){
            case "未下单菜":
                return orderFoodLists;
            case "已下单菜":
                return orderedFoodLists;
        }
            return  null;
    }

    /**
     * 绑定数据
     * @param savedInstanceState
     */
    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
//        mTextView.setText(mContent);
        // 设置监听事件,跳转到详细信息页面
    }

    /**
     * fragment切换时刷新数据
     * @param isVisibleToUser
     */
    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        Log.d(Tag ,"setUserVisibleHint...");
        if (isVisibleToUser  ){
            new Thread(new Runnable() {
                @Override
                public void run() {  // fragment切换时更新界面
                    Message message = new Message();
                    if ("未下单菜".equals(mTitle)){
                        message.what = 1;
                        mHandler.sendMessage(message);
                    }
                    else if ("已下单菜".equals(mTitle)){
                        message.what = 0;
                        mHandler.sendMessage(message);
                    }
                }
            }).start();
        }
    }
}
