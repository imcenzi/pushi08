package com.pushi.pushi08.until;

import android.content.Context;
import android.net.wifi.WifiManager;
import android.util.Log;
import android.widget.Toast;

/**
 * @author Admin
 * @version 1.0
 * @date 2020/8/27 14:09
 */
public class AutoOpenWifi {
    private  WifiManager wifiManager = null;
    /**
     * wifi打开后执行某个操作
     *
     * wifiManager.setWifiEnabled(true);//打开wifi
     * wifiManager.setWifiEnabled(false);//关闭wifi
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
        //Toast.makeText(this, "wifi已经打开", Toast.LENGTH_SHORT).show();
    }

    /**
     * 判断wifi是否已经打开
     *
     * @return true：已打开、false:未打开
     */
    public  boolean isWifiOpened() {
        int status = wifiManager.getWifiState();
        if (status == WifiManager.WIFI_STATE_ENABLED) {
            //wifi已经打开
            return true;
        } else {
            return false;
        }
    }


    }
