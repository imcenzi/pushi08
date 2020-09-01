package com.pushi.pushi08;

/**
 * @author Admin
 * @version 1.0
 * @date 2020/8/26 11:01
 */

import java.util.Timer;
import java.util.TimerTask;

import android.app.Activity;
import android.content.Context;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Toast;

public class exit extends Activity implements OnClickListener {

    private WifiManager wifiManager = null;


    private Button mExitBtn;
    private static boolean isExit = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {


        /** 获取WIFI服务
         */
        wifiManager = (WifiManager) getApplicationContext().getSystemService(Context.WIFI_SERVICE);


        super.onCreate(savedInstanceState);
        setContentView(R.layout.exit);
        initView();
        //打开WiFi
        onWifiOpenDoing();

    }


/**
 *
 *wifiManager.setWifiEnabled(true);//打开wifi
 *
 *wifiManager.setWifiEnabled(false);//关闭wifi
 */

    /**
     * wifi打开后执行某个操作
     */
    private void onWifiOpenDoing() {
        while (!isWifiOpened()) {
            try {
                //为了避免程序一直while循环，让它睡个100毫秒在检测……
                Thread.currentThread();
                Thread.sleep(100);
            } catch (InterruptedException ie) {
            }
        }
        //在wifi打开后执行的操作都写在这下面
        Log.i("log", "wifi已经打开");
        Toast.makeText(this, "wifi已经打开", Toast.LENGTH_SHORT).show();
    }

    /**
     * 判断wifi是否已经打开
     *
     * @return true：已打开、false:未打开
     */
    public boolean isWifiOpened() {
        int status = wifiManager.getWifiState();
        if (status == WifiManager.WIFI_STATE_ENABLED) {
            //wifi已经打开
            return true;
        } else {
            return false;
        }
    }


    /**
     * 退出程序
     */
    private void initView() {
        mExitBtn = (Button) findViewById(R.id.exit11);
        mExitBtn.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.exit11:
                exit();
                break;

            default:
                break;
        }
    }

    private void exit() {
        if (!isExit) {
            isExit = true;
            Toast.makeText(this, "在按一次退出程序", Toast.LENGTH_SHORT).show();
            new Timer().schedule(new TimerTask() {

                @Override
                public void run() {
                    isExit = false;
                }
            }, 2000);
        } else {
            finish();
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            exit();
        }
        return false;
    }
}

