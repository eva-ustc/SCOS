package es.source.code.scos.activity;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;
import java.util.zip.Inflater;

import es.source.code.scos.R;
import es.source.code.scos.adapters.ViewAdapter;
import es.source.code.scos.model.Database_food;

import static es.source.code.scos.activity.MainScreenGridViewActivity.Tag;
import static es.source.code.scos.activity.MainScreenGridViewActivity.orderFoodLists;
import static es.source.code.scos.model.Database_food.foodDatabase;

/**
 * @author LRK
 * @project_name SCOS
 * @package_name es.source.code.scos.activity
 * @date 2018/10/7 17:12
 * @description
 */

public class FoodDetailedActivity extends Activity {

    private ViewPager mViewPager;
    private LayoutInflater mInflater;
    List<View> viewList =new ArrayList<>();
    private ViewAdapter mAdapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_food_detailed);

        mViewPager = this.findViewById(R.id.viewPage_foodDetailed);
        mInflater = LayoutInflater.from(this);

        for (int i=0;i< foodDatabase.size();i++){
            View view = mInflater.inflate(R.layout.food_detail_info,null);
            ImageView iv_foodDetailInfo_image = view.findViewById(R.id.iv_foodDetailInfo_image);
            TextView tv_foodDetailInfo_name = view.findViewById(R.id.tv_foodDetailInfo_name);
            TextView tv_foodDetailInfo_price = view.findViewById(R.id.tv_foodDetailInfo_price);
            EditText et_foodDetailInfo_comm = view.findViewById(R.id.et_foodDetailInfo_comm);
            Button btn_itemAddOrDel = view.findViewById(R.id.btn_itemAddOrDel);
            // 初始化数据
            iv_foodDetailInfo_image.setImageResource((Integer) foodDatabase.get(i).get("image"));
            tv_foodDetailInfo_name.setText((String)foodDatabase.get(i).get("foodName"));
            tv_foodDetailInfo_price.setText((Float)foodDatabase.get(i).get("foodPrice")+"元");
            et_foodDetailInfo_comm.setHint("备注信息:美食");
            if (orderFoodLists.contains(foodDatabase.get(i))){
                btn_itemAddOrDel.setText("退点");
            }else {
                btn_itemAddOrDel.setText("点菜");
            }
            btn_itemAddOrDel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if ("点菜".equals(((Button)v).getText().toString())){
//                        Log.d(Tag ,"currentItem: "+mViewPager.getCurrentItem());
                        orderFoodLists.add(foodDatabase.get(mViewPager.getCurrentItem()));
                        ((Button) v).setText("退点");
                    }else {
                        orderFoodLists.remove(foodDatabase.get(mViewPager.getCurrentItem()));
                        ((Button) v).setText("点菜");
                    }

                }
            });
            viewList.add(view);
        }

        // 创建适配器
        mAdapter = new ViewAdapter(viewList);
        mViewPager.setAdapter(mAdapter);
    }
}
