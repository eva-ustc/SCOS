package es.source.code.scos.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.annotation.Nullable;
import android.widget.ImageView;
import android.widget.TextView;

import es.source.code.scos.R;

import static android.R.attr.min;
import static android.R.attr.thickness;

/**
 * @author LRK
 * @project_name SCOS
 * @package_name es.source.code.scos.activity
 * @date 2018/10/6 16:42
 * @description
 */

public class FoodInformationActivity extends Activity {

    private Intent intent;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_food_information);
        intent = getIntent();
        initView();
    }

    private void initView() {
        ImageView imageView = this.findViewById(R.id.iv_foodInfo_image);
        imageView.setImageResource(intent.getIntExtra("image",0));
        TextView tv_name = this.findViewById(R.id.tv_foodInfo_name);
        tv_name.setText(intent.getStringExtra("foodName"));
        TextView tv_price = this.findViewById(R.id.tv_foodInfo_price);
        tv_price.setText(""+intent.getFloatExtra("foodPrice",0));
    }
}
