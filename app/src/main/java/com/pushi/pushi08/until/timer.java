package com.pushi.pushi08.until;

import android.annotation.SuppressLint;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;

public class timer implements Runnable{
    //获取数据
    private String [] datas;
    /**
     * 重写run()方法
     */
    @Override
    public void run() {
        for (;;){
            try {
                Thread.sleep(1000);
                mHandler.sendMessage(mHandler.obtainMessage());
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

        }
    }

    /**
     * 实现handleMessage()方法，用于接收Message，刷新UI
     */
    @SuppressLint("HandlerLeak")
    private Handler mHandler = new Handler() {
        public void handleMessage(Message msg) {

            super.handleMessage(msg);
            refreshUI();
        }

        /**
         * 更新视图
         */
        private void refreshUI() {

        }
    };

    private String[] getdata(){

        try {
            //请求数据
            String data = soap.getCityWeather("192.168.0.0","xp");
            //解析数据
            data = data.split("￥")[1].split("X")[0].toString().trim();
            String datas [] = data.split("\\$");
            this.datas = datas;
            Log.e("测试数据", "数据: "+data+"----数组长度："+datas.length);
        } catch (IOException e) {
            Log.e("IOException", "onCreate: ",e);
        } catch (XmlPullParserException e) {
            Log.e("XmlPullParserException", "onCreate: ",e);
            //e.printStackTrace();
        }
        return datas;
    }
}
