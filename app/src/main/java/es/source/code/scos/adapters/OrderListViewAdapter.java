package es.source.code.scos.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;
import java.util.Map;

import es.source.code.scos.R;
import es.source.code.scos.activity.FoodDetailedActivity;
import es.source.code.scos.activity.FoodInformationActivity;
import es.source.code.scos.activity.FoodViewActivity;

import static es.source.code.scos.activity.MainScreenGridViewActivity.orderFoodLists;

/**
 * @author LRK
 * @project_name SCOS
 * @package_name es.source.code.scos.adapters
 * @date 2018/10/6 14:54
 * @description FoodListView适配器
 */

public class OrderListViewAdapter extends BaseAdapter {

    private List<Map<String,Object>> mData;
    private LayoutInflater mLayoutInflater;
    private Context mContext;
    private OrderInfo mOrderInfo;
    private boolean hasButton;


    public OrderListViewAdapter(Context context, List<Map<String,Object>> data,boolean hasButton){
        this.mContext = context;
        this.mData = data;
        this.mLayoutInflater = LayoutInflater.from(context);
        this.hasButton = hasButton;
    }

    public final class OrderInfo{
        public ImageView iv_orderImg;
        public TextView tv_orderName;
        public TextView tv_orderPrice;
        public TextView tv_orderCount;
        public TextView tv_orderComment;
        public Button btn_Delete;
    }

    @Override
    public int getCount() {
        if (mData != null) {

            return mData.size();
        }
        return 0;
    }

    @Override
    public Object getItem(int position) {
        return mData.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    /**
     * 创建视图
     * @param position
     * @param convertView
     * @param parent
     * @return
     */
    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        mOrderInfo = null;
        if (convertView == null){
            // 获得食物组件,实例化食物组件
            mOrderInfo = new OrderInfo();
            convertView = mLayoutInflater.inflate(R.layout.order_item_listview,null);
            mOrderInfo.iv_orderImg = (ImageView) convertView.findViewById(R.id.iv_orderImg);
            mOrderInfo.tv_orderName = (TextView) convertView.findViewById(R.id.tv_orderName);
            mOrderInfo.tv_orderPrice = (TextView) convertView.findViewById(R.id.tv_orderPrice);
            mOrderInfo.tv_orderCount = (TextView) convertView.findViewById(R.id.tv_orderCount);
            mOrderInfo.tv_orderComment = (TextView) convertView.findViewById(R.id.tv_orderComment);

            mOrderInfo.btn_Delete = (Button) convertView.findViewById(R.id.btn_Delete);
            if (!hasButton){
                mOrderInfo.btn_Delete.setVisibility(View.INVISIBLE);
            }
            convertView.setTag(mOrderInfo);
        }else {
            mOrderInfo = (OrderInfo) convertView.getTag();
        }

        // 绑定数据
        mOrderInfo.iv_orderImg.setBackgroundResource((Integer) mData.get(position).get("image"));
        mOrderInfo.tv_orderName.setText((String)mData.get(position).get("foodName"));
        mOrderInfo.tv_orderPrice.setText(""+mData.get(position).get("foodPrice"));
        mOrderInfo.tv_orderCount.setText("数量:1");
        mOrderInfo.tv_orderComment.setText("美食");

        //添加点击事件
        if (hasButton){
            mOrderInfo.btn_Delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Button btn_submit = (Button) v;
                    Toast.makeText(mContext,"退点成功",Toast.LENGTH_SHORT).show();
                    orderFoodLists.remove(mData.get(position));

                }
            });
        }
        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(mContext,"菜品详细信息",Toast.LENGTH_SHORT).show();
                /*Map<String,Object> foodinfo = mData.get(position);
                Intent intent = new Intent(mContext, FoodInformationActivity.class);
                intent.putExtra("image",(Integer)foodinfo.get("image"));
                intent.putExtra("foodName",(String) foodinfo.get("foodName"));
                intent.putExtra("foodPrice", (Float) foodinfo.get("foodPrice"));
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);*/
                Intent intent = new Intent(mContext, FoodDetailedActivity.class);
                mContext.startActivity(intent);
            }
        });

       /* mFoodInfo.iv_foodImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(mContext,"菜品详细信息",Toast.LENGTH_SHORT).show();
                Map<String,Object> foodinfo = mData.get(position);
                Intent intent = new Intent(mContext, FoodInformationActivity.class);
                intent.putExtra("image",(Integer)foodinfo.get("image"));
                intent.putExtra("foodName",(String) foodinfo.get("foodName"));
                intent.putExtra("foodPrice",(String) foodinfo.get("foodPrice"));
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                mContext.startActivity(intent);
            }
        });*/
        return convertView;
    }
}
