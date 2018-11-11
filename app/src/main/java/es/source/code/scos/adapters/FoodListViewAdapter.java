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

public class FoodListViewAdapter extends BaseAdapter {

    private List<Map<String,Object>> mData;
    private LayoutInflater mLayoutInflater;
    private Context mContext;
    private FoodInfo mFoodInfo;


    public FoodListViewAdapter(Context context,List<Map<String,Object>> data){
        this.mContext = context;
        this.mData = data;
        this.mLayoutInflater = LayoutInflater.from(context);

    }

    public final class FoodInfo{
        public ImageView iv_foodImg;
        public TextView tv_foodName;
        public TextView tv_foodPrice;
        public Button btn_addOrDel;
    }

    @Override
    public int getCount() {
        return mData.size();
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
        mFoodInfo = null;
        if (convertView == null){
            // 获得食物组件,实例化食物组件
            mFoodInfo = new FoodInfo();
            convertView = mLayoutInflater.inflate(R.layout.food_item_listview,null);
            mFoodInfo.iv_foodImg = (ImageView) convertView.findViewById(R.id.iv_foodImg);
            mFoodInfo.tv_foodName = (TextView) convertView.findViewById(R.id.tv_foodName);
            mFoodInfo.tv_foodPrice = (TextView) convertView.findViewById(R.id.tv_foodPrice);
            mFoodInfo.btn_addOrDel = (Button) convertView.findViewById(R.id.btn_addOrDel);
            convertView.setTag(mFoodInfo);
        }else {
            mFoodInfo = (FoodInfo) convertView.getTag();
        }

        // 绑定数据
        mFoodInfo.iv_foodImg.setBackgroundResource((Integer) mData.get(position).get("image"));
        mFoodInfo.tv_foodName.setText((String)mData.get(position).get("foodName"));
        mFoodInfo.tv_foodPrice.setText(""+mData.get(position).get("foodPrice"));

        //添加点击事件
        mFoodInfo.btn_addOrDel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Button btn_submit = (Button) v;
                if ("加入订单".equals(btn_submit.getText().toString())){
                    Toast.makeText(mContext,"点菜成功",Toast.LENGTH_SHORT).show();
                    orderFoodLists.add(mData.get(position));
                    ((Button)v).setText("退点");
                }else if ("退点".equals(btn_submit.getText().toString())){
                    Toast.makeText(mContext,"退点成功",Toast.LENGTH_SHORT).show();
                    orderFoodLists.remove(mData.get(position));
                    ((Button)v).setText("加入订单");
                }
            }
        });
        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(mContext,"菜品详细信息",Toast.LENGTH_SHORT).show();
                Map<String,Object> foodinfo = mData.get(position);
//                Intent intent = new Intent(mContext, FoodInformationActivity.class);
                Intent intent = new Intent(mContext, FoodDetailedActivity.class);
                intent.putExtra("image",(Integer)foodinfo.get("image"));
                intent.putExtra("foodName",(String) foodinfo.get("foodName"));
                intent.putExtra("foodPrice", (Float) foodinfo.get("foodPrice"));

                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
              //intent = new Intent(mContext, FoodDetailedActivity.class);
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
