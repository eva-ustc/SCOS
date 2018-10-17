package es.source.code.scos.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;

import es.source.code.scos.Constants;

/**
 * @author LRK
 * @project_name SCOS
 * @package_name es.source.code.scos.activity
 * @date 2018/10/7 15:07
 * @description
 */

public class ActivityUtils {
    public static void redirectTo(Context context, Class<? extends Activity> ClassTo, Parcelable user) {
        Intent intent = new Intent(context,ClassTo);
        if (user != null) {
            Bundle bundle = new Bundle();
            bundle.putParcelable(Constants.USER_INFO,user);
            intent.putExtras(bundle);
        }
        context.startActivity(intent);
    }
    public static void redirectTo(Context context, Class<? extends Activity> ClassTo, Parcelable user,Integer defaultPage) {
        Intent intent = new Intent(context,ClassTo);
        intent.putExtra(Constants.INDEX_DEFAULT_FRAGMENT, defaultPage);
        if (user != null) {
            Bundle bundle = new Bundle();
            bundle.putParcelable(Constants.USER_INFO,user);
            intent.putExtras(bundle);
        }
        context.startActivity(intent);
    }
    public static void redirectTo(Context context, Class<? extends Activity> ClassTo) {
        Intent intent = new Intent(context,ClassTo);
        context.startActivity(intent);
    }
}
