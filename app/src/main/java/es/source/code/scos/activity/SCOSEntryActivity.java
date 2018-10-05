package es.source.code.scos.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.widget.Toast;

import es.source.code.scos.Constants;
import es.source.code.scos.R;


/**
 * @author LRK
 * @project_name SCOS
 * @package_name es.source.code.scos.activity
 * @date 2018/10/3 20:06
 * @description
 */

public class SCOSEntryActivity extends Activity {
    private GestureDetector mDetector;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scosentry);
        mDetector = new GestureDetector(this, new MyGestureDetector());
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        mDetector.onTouchEvent(event);
        return super.onTouchEvent(event);
    }

    class MyGestureDetector extends GestureDetector.SimpleOnGestureListener {

        @Override
        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
            float startX = e1.getX();
            float startY = e1.getY();
            float endX = e2.getX();
            float endY = e2.getY();
            if (startX - endX > 50) {
                Intent intent = new Intent();
                intent.setAction(Constants.ACTION_INTENT_SCOSMAIN);
                intent.addCategory(Constants.CATEGORY_INTENT_SCOSLAUNCHER);
                intent.setPackage(SCOSEntryActivity.this.getPackageName());
                // 传递参数
                intent.putExtra(Constants.FROM_ENTRY,"FromEntry");
//                intent.putExtra(Constants.FROM_ENTRY,"FromOthers");
                startActivity(intent);

//                Intent intent = new Intent(SCOSEntryActivity.this, MainScreenActivity.class); 显示意图
//                Toast.makeText(SCOSEntryActivity.this, "向左滑动了...", Toast.LENGTH_SHORT).show();
            }
            return true;
        }

    }
}


/*    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN){
            x1 = event.getX();
            y1 = event.getY();
        }
        if (event.getAction() == MotionEvent.ACTION_UP){
            x2 = event.getX();
            y2 = event.getY();
            if (x1 - x2 > 50){ // 向左滑动
                Intent intent = new Intent();
                intent.setAction(Constants.ACTION_INTENT_SCOSMAIN);
                intent.addCategory(Constants.CATEGORY_INTENT_SCOSLAUNCHER);
                intent.setPackage(this.getPackageName());
//                startActivity(intent);
                Toast.makeText(this,"向左滑动了...",Toast.LENGTH_SHORT).show();
            }
        }
        return super.onTouchEvent(event);

    }*/

