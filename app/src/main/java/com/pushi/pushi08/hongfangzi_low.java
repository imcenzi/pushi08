package com.pushi.pushi08;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.database.sqlite.SQLiteOpenHelper;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import com.pushi.pushi08.until.AutoOpenWifi;
import com.pushi.pushi08.until.AutoScrollView;
import com.pushi.pushi08.until.network;
import com.squareup.picasso.Picasso;
import android.view.View.OnClickListener;

import java.util.LinkedList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import static com.pushi.pushi08.until.Rule.HIDE.imageView;

/**
 * @author Admin
 * @version 1.0
 * @date 2020/8/19 15:12
 */
public class hongfangzi_low extends Activity implements OnClickListener {

    //退出控件
    private Button mExitBtn;
    private static boolean isExit = false;

    private TextView mYSXM;
    private TextView mYSZC;
    private TextView mYSJJ;
    private TextView mHZXM1;
    private TextView mHZXM2;
    private TextView mHZXM3;
    private TextView mHZXM4;
    private TextView mZZJZ;

    private ImageView YSTP;
    private ImageView ZSTP;
    private ImageView EWMTP;
    private ImageView DHTP;
    private ImageView background;

    private AutoScrollView mAUTO;
    //定义一个wifi服务
    private WifiManager wifiManager = null;

    // 获取当前设备IP地址
    private String ip;
    //设置医生照片资源库地址
    //private String zp_url = "http://192.168.44.1:8080/cesi/app_yy/";
    private String zp_url = "http://172.168.7.30:8082/App_yy/";
    //设置服务器地址
    private String fw_url = "http://172.168.7.30:8082/Service1.asmx";
    private String fw_name = "getdata";
    @SuppressLint("HandlerLeak")
    private Handler handler = new Handler() {
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {


        /** 获取WIFI服务
         */
        wifiManager = (WifiManager) this.getApplicationContext().getSystemService(Context.WIFI_SERVICE);

        View decorView = this.getWindow().getDecorView();
        //透明状态栏
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        //透明导航栏
        decorView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);
        //常亮
        this.getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.hongfangzi_low);
        //this.ip = network.getLocalIpAddress(getApplicationContext());

        onWifiOpenDoing();//打开wifi
        initUI();//加载试图
        handler5();//加载背景图
        updataerr();//判断网络状况
        //初始化滚动
        initData();
        initEvent();
        //退出模块
        initView();


    }


    /**
     * 初始化判断网络环境
     */
    @SuppressLint("SetTextI18n")
    private void updataerr() {
        if (!network.checkEnable(getApplicationContext())) {
            mZZJZ.setText("正在连接网络...");
            //ip = network.getLocalIpAddress(getApplicationContext());
            //Log.e("ip++++++++++++++++++++++++++++++++++++++++++", "run: "+ip );
            new Thread() {
                @Override
                public void run() {
                    try {
                        for (; ; ) {
                            if (network.checkEnable(getApplicationContext())) {
                                //handler2();

                                handler5();
                                updateUI();//更新基础数据
                                return;
                            }
                            Thread.sleep(2000);

                        }
                    } catch (Exception e) {
                        Log.e("----------", "err----" + e);
                    }
                }
            }.start();
        } else {
            //ip = network.getLocalIpAddress(getApplicationContext());
            updateUI();//更新基础数据
            //handler5();
        }
    }


    /**
     * wifi打开后执行某个操作
     * <p>
     * wifiManager.setWifiEnabled(true);//打开wifi
     * wifiManager.setWifiEnabled(false);//关闭wifi
     */
    private void onWifiOpenDoing() {
        new Thread() {
            @Override
            public void run() {
                try {
                    for (; ; ) {
                        boolean wifi = isWifiOpened();
                        if (wifi == true) {
                            Thread.sleep(2000);
                            updataerr();
                            return;

                        } else {
                            wifiManager.setWifiEnabled(true);//打开wifi
                            //Thread.sleep(2000);
                            //restart(this);//打开wifi后重启程序

                        }
                    }

                } catch (Exception e) {
                    Log.e("wifi------err", "onWifiOpenDoing: " + e);
                }
            }
        }.start();

    }

    /***重启整个APP*/
    public static void restart(Context context) {
        // 获取启动的intent
        Intent intent = context.getPackageManager().getLaunchIntentForPackage(context.getPackageName());
        PendingIntent restartIntent = PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_ONE_SHOT);
        // 设置杀死应用后2秒重启
        AlarmManager mgr = (AlarmManager) context.getSystemService(hongfangzi_low.ALARM_SERVICE);
        mgr.set(AlarmManager.RTC, System.currentTimeMillis() + 1000, restartIntent);
        // 重启应用
        android.os.Process.killProcess(android.os.Process.myPid());
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
     * 实时更新UI视图
     */
    private void updateUI() {
        new Thread() {
            @Override
            public void run() {
                try {
                    ip = network.getLocalIpAddress(getApplicationContext());
                    String data1 = null;
                    String data2 = null;
                    int i = 0;
                    for (; ; ) {
                        //ip = network.getLocalIpAddress(getApplicationContext());
                        //Log.e("ip---------------------------------------", "run: "+ip );
                        String data = network.getdata(fw_url, fw_name, ip, "");

                        if (data == null) {
                            updata(null);
                            updataerr();
                            Thread.sleep(2000);
                            return;
                        }
                        if (i == 0) {
                            updata(data);
                        } else {
                            if (i % 2 != 0) { //单数刷新
                                data1 = data;
                                if (!(data1.equals(data2))) {
                                    updata(data1);
                                }

                            } else {//双数刷新
                                data2 = data;
                                if (!(data1.equals(data2))) {
                                    updata(data2);
                                }
                            }


                        }
                        i++;
                        if (i == 200) {
                            updata(data);
                            i = 1;
                        }
                        Thread.sleep(2000);

                    }


                } catch (Exception e) {
                    Log.e("TAG", "updateUI: " + e);
                    updataerr();
                }
            }
        }.start();

    }


    //初始化滚动
    private void initData() {
        mAUTO.setAutoToScroll(true);//设置可以自动滑动
        mAUTO.setFistTimeScroll(2000);//设置第一次自动滑动的时间
        mAUTO.setScrollRate(50);//设置滑动的速率
        mAUTO.setScrollLoop(true);//设置是否循环滑动
    }

    // 监听是否达到头部或底部
    private void initEvent() {
        mAUTO.setScanScrollChangedListener(new AutoScrollView.ISmartScrollChangedListener() {
            @Override
            public void onScrolledToBottom() {
                //Toast.makeText(jingan_low_1.this, "底部", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onScrolledToTop() {
                //Toast.makeText(jingan_low_1.this, "顶部", Toast.LENGTH_SHORT).show();
            }
        });
    }

    /**
     * 加载试图
     */
    private void initUI() {


        mYSXM = findViewById(R.id.HFZ_YSXM);
        mYSZC = findViewById(R.id.HFZ_YSZC);
        mZZJZ = findViewById(R.id.HFZ_ZZJZ);
        mHZXM1 = findViewById(R.id.HFZ_HZXM1);
        mHZXM2 = findViewById(R.id.HFZ_HZXM2);
        mHZXM3 = findViewById(R.id.HFZ_HZXM3);
        mHZXM4 = findViewById(R.id.HFZ_HZXM4);
        mYSJJ = findViewById(R.id.HFZ_YSJJ);

        YSTP = findViewById(R.id.HFZ_YSZP);//医生图片
        ZSTP = findViewById(R.id.HFZ_ZSZP);//诊室图片
        EWMTP = findViewById(R.id.HFZ_EWNTP);//二维码图片
        DHTP = findViewById(R.id.HFZ_DH);
        background = findViewById(R.id.HFZ_low);//背景图

        mAUTO = findViewById(R.id.HFZ_AUTO);


    }

    /**
     * 更新医生图片
     */
    public void updataYSTP(final String ysgh) {
        handler.post(new Runnable() {
            @Override
            public void run() {
                Picasso.get().load(zp_url + ysgh + ".jpg")
                        .into(YSTP);
            }
        });

    }


    /**
     * 更新数据
     */
    public void updata(final String data) {
        handler.post(new Runnable() {
            @Override
            public void run() {
                try {
                    if (data != null) {

                        if (data != "" && data.indexOf("$") > 0) {

                            String[] lsdata = data.split("\\￥")[1].split("\\$");

                            //System.out.println(lsdata[2].split("诊室")[0]);

                            updataYSTP(lsdata[3]);//更新医生图片
                            updataZSTP(lsdata[1] + "ZS");//更新诊室图片
                            updataEWMTP(lsdata[3] + "X");//更新二维码图片
                            updataDHTP(lsdata[13] + "dh");//更新党徽图片

                            mYSJJ.setText(ToDBC(lsdata[6]));


                            mYSXM.setText(lsdata[4]);//医生姓名
                            mYSZC.setText(lsdata[5]);//医生职称
                            mZZJZ.setText(lsdata[7]);//正在就诊
                            mHZXM1.setText(lsdata[8]);//候诊1
                            mHZXM2.setText(lsdata[9]);//候诊2
                            mHZXM3.setText(lsdata[10]);//回诊1
                            mHZXM4.setText(lsdata[11]);//回诊2


                        } else {
                            updataerr();
                            Log.e("错误信息", "data: " + "未获取到数据");

                        }
                    } else {

//                        System.out.println(data);
//                        Log.e("datadatadatadata", "run:" + data);
                        updataerr();
                        Thread.sleep(2000);
                        mZZJZ.setText("");
                        return;

                    }

                } catch (Exception e) {
                    Log.e("错误信息", "run: " + e);
                }
            }

        });
    }


    /**
     * 更新诊室图片
     */
    public void updataZSTP(final String zsdm) {
        handler.post(new Runnable() {
            @Override
            public void run() {
                Picasso.get().load(zp_url + zsdm + ".jpg")
                        .into(ZSTP);
            }
        });

    }

    /**
     * 更新医生二维码图片
     */
    public void updataEWMTP(final String ysgh) {
        handler.post(new Runnable() {
            @Override
            public void run() {
                Picasso.get().load(zp_url + ysgh + ".jpg")
                        .into(EWMTP);
            }
        });

    }


    /**
     * 更新背景照片
     *
     * @param
     */
    private void handler5() {
        handler.post(new Runnable() {
            @Override
            public void run() {
                Picasso.get().load(zp_url + "background_low.jpg")
                        .placeholder(R.mipmap.hongfangzi_low)
                        .error(R.mipmap.hongfangzi_low)
                        .into(background);

            }
        });
    }


    /**
     * 更新医生二维码图片
     */
    public void updataDHTP(final String dhdm) {
        handler.post(new Runnable() {
            @Override
            public void run() {
                Picasso.get().load(zp_url + dhdm + ".jpg")
                        .into(DHTP);
            }
        });

    }

    /***
     * 全角转半角
     * */
    public static String ToDBC(String input) {
        char[] c = input.toCharArray();
        for (int i = 0; i < c.length; i++) {
            if (c[i] == 12288) {
                c[i] = (char) 32;
                continue;
            }
            if (c[i] > 65280 && c[i] < 65375) {
                c[i] = (char) (c[i] - 65248);
            }
        }
        return new String(c);
    }


    /**
     * 退出程序
     */
    private void initView() {
        mExitBtn = (Button) findViewById(R.id.HFZ_over);
        mExitBtn.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.HFZ_over:
                exit();
                break;

            default:
                break;
        }
    }

    private void exit() {
        if (!isExit) {
            isExit = true;
            //Toast.makeText(this, "在按一次退出程序", Toast.LENGTH_SHORT).show();
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
