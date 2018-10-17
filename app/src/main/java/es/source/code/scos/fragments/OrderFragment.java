package es.source.code.scos.fragments;

import android.annotation.SuppressLint;
import android.content.Context;
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
                    mListView.setAdapter(new OrderListViewAdapter(getActivity(),getData(),true));
                    break;
            }
        }
    };
    private List<Map<String, Object>> mOrderList = new ArrayList<>();
    private List<Map<String, Object>> mOrderedList = new ArrayList<>();
    private Button mBtn_submit;
    private TextView mTv_orderSumCount;
    private TextView mTv_orderSumPrice;
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
                if ("提交订单".equals(((Button)v).getText())) {
                    orderedFoodLists.addAll(orderFoodLists);
                    orderFoodLists.clear();
                    initBottom(getData());
                    mListView.setAdapter(new OrderListViewAdapter(getActivity(),getData(),true));
                    Toast.makeText(mContext,"订单提交成功",Toast.LENGTH_SHORT).show();
                }else {
                    orderedFoodLists.clear();
                    initBottom(getData());
                    mListView.setAdapter(new OrderListViewAdapter(getActivity(),getData(),false));
                    Toast.makeText(mContext,"交易完成!",Toast.LENGTH_SHORT).show();
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
            float sum = 0;
            for(Map<String,Object> map : mList){
                sum += (Float) map.get("foodPrice");
            }
            mTv_orderSumPrice.setText("订单总价:" + sum+ "元");
        }
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
