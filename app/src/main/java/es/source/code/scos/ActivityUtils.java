package es.source.code.scos;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.text.TextUtils;

import java.util.List;

import es.source.code.scos.Constants;

/**
 * @author LRK
 * @project_name SCOS
 * @package_name es.source.code.scos.activity
 * @date 2018/10/7 15:07
 * @description
 */

public class ActivityUtils {
    /**
     * 跳转到指定的activity
     * @param context
     * @param ClassTo
     * @param user
     */
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

    /**
     * 判断Activity是否在运行
     * @param pkg 包名
     * @param cls 类名
     * @param context
     * @return
     */
    public static boolean isClsRnning(String pkg,String cls,Context context){
        ActivityManager manager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningTaskInfo> tasks = manager.getRunningTasks(1);
        ActivityManager.RunningTaskInfo task = tasks.get(0);
        if (task != null){
            return TextUtils.equals(task.topActivity.getPackageName(),pkg) &&
                    TextUtils.equals(task.topActivity.getClassName(),cls);
        }
        return false;
    }

}
