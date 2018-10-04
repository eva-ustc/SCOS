package es.source.code.scos.activity;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;

import es.source.code.scos.R;

/**
 * @author LRK
 * @project_name SCOS
 * @package_name es.source.code.scos.activity
 * @date 2018/10/3 20:06
 * @description
 */

public class SCOSEntryActivity extends Activity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scosentry);
    }
}
