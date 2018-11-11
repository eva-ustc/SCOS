package es.source.code.scos.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import es.source.code.scos.R;

/**
 * @author LRK
 * @project_name SCOS
 * @package_name es.source.code.scos.model
 * @date 2018/10/6 22:12
 * @description
 */

public class Database_food {
    public static final Integer[] coldFoods = new Integer[]{R.mipmap.ic_food_liangbanhuanggua,R.mipmap.ic_food_lushuiniurou,
            R.mipmap.ic_food_jiangluobo,R.mipmap.ic_food_xiannaimuguadong};
    public static final  String[] coldFoodNames = new String[]{"凉拌黄瓜","卤水牛肉","酱萝卜","鲜奶木瓜冻"};
    public static final  Double[] coldFoodPrices = new Double[]{25.0,39.0,18.0,22.0};

    public static final  Integer[] hotFoods = new Integer[]{R.mipmap.ic_hotfood_xihongshichaodan,R.mipmap.ic_hotfood_baochaozhugan,
            R.mipmap.ic_hotfood_hexiangfenzhengrou,R.mipmap.ic_hotfood_tiebanyangrou};
    public static final  String[] hotFoodNames = new String[]{"西红柿炒蛋","爆炒猪肝","荷香粉蒸肉","铁板羊肉"};
//    public static final  String[] hotFoodPrices = new String[]{"15元","23元","21元","35元"};
    public static final  Double[] hotFoodPrices = new Double[]{15.0,23.0,21.0,35.0};

    public static final  Integer[] seaFoods = new Integer[]{R.mipmap.ic_seafood_lachaohuaha,R.mipmap.ic_seafood_qingzhenghuaxie,
            R.mipmap.ic_seafood_qingzhengluyu,R.mipmap.ic_seafood_youmendaxia};
    public static final  String[] seaFoodNames = new String[]{"辣炒花蛤","清蒸花蟹","清蒸鲈鱼","油焖大虾"};
    public static final  Double[] seaFoodPrices = new Double[]{36.0,37.5,31.5,35.5};

    public static final  Integer[] beverage = new Integer[]{R.mipmap.ic_beverage_7up,R.mipmap.ic_beverage_beer,
            R.mipmap.ic_beverage_cola,R.mipmap.ic_beverage_sprite};
    public static final  String[] beverageNames = new String[]{"7喜","纯生啤酒","可口可乐","雪碧"};
//    public static final  String[] beveragePrices = new String[]{"3.5元","4元","3.5元","3.5元"};
    public static final  Double[] beveragePrices = new Double[]{3.5,4.0,3.5,3.5};

    public static List<Map<String,Object>> foodDatabase = new ArrayList<>();
    // 初始化food数据库
    static {
        // foodType 0--coldfood 1--hotfood 2--seafood 3--beverage
        for(int i =0;i< Database_food.coldFoods.length;i++){
            Map<String,Object> map = new HashMap<>();
            map.put("image",Database_food.coldFoods[i]);
            map.put("foodName",Database_food.coldFoodNames[i]);
            map.put("foodPrice", Database_food.coldFoodPrices[i]);
            map.put("foodType",0);
            map.put("foodStock",10); // 添加库存信息
            foodDatabase.add(map);
        }
        for(int i =0;i<Database_food.hotFoods.length;i++){
            Map<String,Object> map = new HashMap<>();
            map.put("image",Database_food.hotFoods[i]);
            map.put("foodName",Database_food.hotFoodNames[i]);
            map.put("foodPrice", Database_food.hotFoodPrices[i]);
            map.put("foodType",1);
            map.put("foodStock",10); // 添加库存信息
            foodDatabase.add(map);
        }
        for(int i =0;i<Database_food.seaFoods.length;i++){
            Map<String,Object> map = new HashMap<>();
            map.put("image",Database_food.seaFoods[i]);
            map.put("foodName",Database_food.seaFoodNames[i]);
            map.put("foodPrice", Database_food.seaFoodPrices[i]);
            map.put("foodType",2);
            map.put("foodStock",10); // 添加库存信息
            foodDatabase.add(map);
        }
        for(int i =0;i<Database_food.beverage.length;i++){
            Map<String,Object> map = new HashMap<>();
            map.put("image",Database_food.beverage[i]);
            map.put("foodName",Database_food.beverageNames[i]);
            map.put("foodPrice", Database_food.beveragePrices[i]);
            map.put("foodType",3);
            map.put("foodStock",10); // 添加库存信息
            foodDatabase.add(map);
        }
    }
}
