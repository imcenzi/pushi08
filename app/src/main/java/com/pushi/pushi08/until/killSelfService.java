package com.pushi.pushi08.until;

import android.app.Service;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;

/**
 * @author Admin
 * @version 1.0
 * @date 2020/9/1 12:36
 */
public class killSelfService extends Service {
    /**关闭应用后多久重新启动*/
    private static  long stopDelayed=1;
    private Handler handler;
    private String PackageName;

    /**
     * Instantiates a new Kill self service.
     */
    public killSelfService() {
        handler=new Handler();
    }

    @Override
    public int onStartCommand(final Intent intent, int flags, int startId) {
        stopDelayed=intent.getLongExtra("Delayed",1);
        PackageName=intent.getStringExtra("PackageName");
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent LaunchIntent = getPackageManager().getLaunchIntentForPackage(PackageName);
                startActivity(LaunchIntent);
                killSelfService.this.stopSelf();
            }
        },10);
        return super.onStartCommand(intent, flags, startId);
    }
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

}
