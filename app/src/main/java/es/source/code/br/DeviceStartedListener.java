package es.source.code.br;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import es.source.code.scos.service.UpdateService;

/**
 * @author LRK
 * @project_name SCOS
 * @package_name es.source.code.br
 * @date 2018/10/27 23:46
 * @description
 * God Bless,No Bug!
 */
public class DeviceStartedListener extends BroadcastReceiver {
    private static final String Tag = "DeviceStartedListener";

    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();
        if ("android.intent.action.BOOT_COMPLETED".equals(action)){
            Log.d(Tag ,"检测到开机启动完成,启动UpdateService服务");
            Intent serverLauncher = new Intent(context,UpdateService.class);
            context.startService(serverLauncher);
        }
    }
}
