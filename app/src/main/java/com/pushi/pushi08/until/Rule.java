package com.pushi.pushi08.until;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import com.squareup.picasso.Picasso;

import java.util.HashMap;
import java.util.Map;

public class Rule {

    /**
     * 1.基础配置信息类
     */
    public static class Config{
        public static Activity activity = null;//页面对象
        public static String resources_src = "";//资源文件地址
        public static String request_src = "";//接口请求地址
        public static String request_name = "";//接口名称
        public static String tip = "";//对接接口的标识
    }

    /**
     * 2.时间显示模块
     */
    public static int TIME_SWITCH = 0;
    public static class TIME{
        public static TextView mTIME = null;//年月日 时间
        public static TextView mWEEK = null;//周
        public static String time_rule = "yyyy年MM月dd日  HH:mm:ss";//时间格式
        public static int interval = 1000;//时间更新频率
    }
    /**
     * 3.语音转换
     */
    public static int VOICE_SWITCH = 0;
    public static class VOICE{

    }
    /**
     * 4.数据更新
     */
    public static int DATA_SWITCH = 0;
    public static class DATA{
        public static Map<String,Object> data_str = new HashMap<>();//数据界面元素集合
        public static int interval = 2000;//更新频率
    }
    /**
     * 5.是否开启全屏
     */
    public static int FULL_SCREEN = 0;
    /**
     * 6.是否开启图片定时隐藏功能
     */
    public static int HIDE_PHOTO = 0;
    public static class HIDE{
        public static ImageView imageView = null;//需要隐藏的图片
        public static int interval = 10000;//更新频率 0：不更新 1 更新
        public static int state = View.INVISIBLE;//图片状态 INVISIBLE 隐藏 VISIBLE 展示
    }
    /**
     * 7.图片更新
     */
    public static int IMAGE_SWITCH = 0;
    public static class IMAGE{
        public static Map<String,Object> data_img = new HashMap<>();//图片界面元素集合
        public static Map<String,String> data_img_key = new HashMap<>();//图片界面元素key集合
        public static int interval = 2000;//更新频率
    }
//    /**
//     * 8.图片更新
//     */
//    public static int IMAGE_SWITCH = 0;
//    public static class IMAGE{
//        public static Map<String,Object> data_img = new HashMap<>();//图片界面元素集合
//        public static Map<String,String> data_img_key = new HashMap<>();//图片界面元素key集合
//        public static int interval = 2000;//更新频率
//    }
    /**
     * 9.图片更新
     */
    public static Picasso Pic = null;

}
