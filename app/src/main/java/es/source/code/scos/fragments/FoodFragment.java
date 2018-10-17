package es.source.code.scos.fragments;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import es.source.code.scos.R;
import es.source.code.scos.activity.FoodDetailedActivity;
import es.source.code.scos.activity.FoodInformationActivity;
import es.source.code.scos.adapters.FoodListViewAdapter;
import es.source.code.scos.model.Database_food;

/**
 * @author LRK
 * @project_name SCOS
 * @package_name es.source.code.scos.fragments
 * @date 2018/10/6 13:11
 * @description 初始菜品页面
 */
@SuppressLint("ValidFragment")
public class FoodFragment extends Fragment {

    private Context mContext;
//    private TextView mTextView;
    private ListView mListView;
    private String mTitle;

//    private List<Map<String, Object>> mList = new ArrayList<>();
    private List<Map<String, Object>> mInitList = new ArrayList<>();
    //    private String mContent;

    /**
     * 得到标题
     * @return
     */
    public String getTitle() {
        return mTitle;
    }

    public FoodFragment(){}
    public FoodFragment(String title,ListView listView){
        super();
        mTitle = title;
        mListView = listView;
    }
    public FoodFragment(String title,List<Map<String,Object>> list){
        super();
        mTitle = title;
        mInitList = list;
    }
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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
        View view = inflater.inflate(R.layout.food_fragement,container,false);
        mListView = (ListView) view.findViewById(R.id.list);
        if (mInitList.size() == 0) {
            mInitList = getData();
        }

        mListView.setAdapter(new FoodListViewAdapter(getActivity(), mInitList));
        return view;
       /* // 创建视图
        mTextView = new TextView(mContext);
        mTextView.setTextColor(Color.RED);
//        String name = this.getArguments().getString("title");
        mTextView.setBackgroundColor(Color.TRANSPARENT);
//        mTextView.setText(name);
        return mTextView;*/
    }

    /**
     * 给listView传入数据
     * @return
     */
    private List<Map<String, Object>> getData() {
//        mList = new ArrayList<>();
        switch (mTitle){
            case "冷菜":
                for(int i =0;i< Database_food.coldFoods.length;i++){
                    Map<String,Object> map = new HashMap<>();
                    map.put("image",Database_food.coldFoods[i]);
                    map.put("foodName",Database_food.coldFoodNames[i]);
                    map.put("foodPrice", Database_food.coldFoodPrices[i]);
                    mInitList.add(map);
                }
                break;
            case "热菜":
                for(int i =0;i<Database_food.hotFoods.length;i++){
                    Map<String,Object> map = new HashMap<>();
                    map.put("image",Database_food.hotFoods[i]);
                    map.put("foodName",Database_food.hotFoodNames[i]);
                    map.put("foodPrice", Database_food.hotFoodPrices[i]);
                    mInitList.add(map);
                }
                break;
            case "海鲜":
                for(int i =0;i<Database_food.seaFoods.length;i++){
                    Map<String,Object> map = new HashMap<>();
                    map.put("image",Database_food.seaFoods[i]);
                    map.put("foodName",Database_food.seaFoodNames[i]);
                    map.put("foodPrice", Database_food.seaFoodPrices[i]);
                    mInitList.add(map);
                }
                break;
            case "酒水":
                for(int i =0;i<Database_food.beverage.length;i++){
                    Map<String,Object> map = new HashMap<>();
                    map.put("image",Database_food.beverage[i]);
                    map.put("foodName",Database_food.beverageNames[i]);
                    map.put("foodPrice", Database_food.beveragePrices[i]);
                    mInitList.add(map);
                }
                break;
            default:
                break;

        }
 /*       for(int i=0;i<10;i++){
            Map<String,Object> map = new HashMap<>();
            map.put("image",R.drawable.ic_food1);
            map.put("foodName","红烧肉");
            map.put("foodPrice","25元");
            mList.add(map);
        }*/
        return mInitList;
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
    mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            Toast.makeText(mContext,"菜品详细信息",Toast.LENGTH_SHORT).show();
            /*Map<String,Object> foodinfo = mInitList.get(position);
            Intent intent = new Intent(getActivity(), FoodInformationActivity.class);
            intent.putExtra("image",(Integer)foodinfo.get("image"));
            intent.putExtra("foodName",(String) foodinfo.get("foodName"));
            intent.putExtra("foodPrice",(Float) foodinfo.get("foodPrice"));
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);*/
            Intent intent = new Intent(getActivity(), FoodDetailedActivity.class);
            getActivity().startActivity(intent);
        }
    });
    }
}
