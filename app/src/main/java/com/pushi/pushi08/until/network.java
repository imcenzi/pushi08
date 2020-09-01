package com.pushi.pushi08.until;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Paint;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.util.Log;
import android.widget.TextView;

import java.io.*;
import java.net.*;
import java.util.*;

public class network {
    /**
     * 检查网络是否可用
     *
     * @param paramContext
     * @return
     */
    public static boolean checkEnable(Context paramContext) {
        boolean i = false;
        @SuppressLint("WrongConstant") NetworkInfo localNetworkInfo = ((ConnectivityManager) paramContext
                .getSystemService("connectivity")).getActiveNetworkInfo();
        if ((localNetworkInfo != null) && (localNetworkInfo.isAvailable()))
            return true;
        return false;
    }

    /**
     * 获取HOSTIP地址
     */
    public static String getHostIP() {
        String hostIp = null;
        try {
            Enumeration nis = NetworkInterface.getNetworkInterfaces();
            InetAddress ia = null;
            while (nis.hasMoreElements()) {
                NetworkInterface ni = (NetworkInterface) nis.nextElement();
                Enumeration<InetAddress> ias = ni.getInetAddresses();
                while (ias.hasMoreElements()) {
                    ia = ias.nextElement();
                    if (ia instanceof Inet6Address) {
                        continue;// skip ipv6
                    }
                    String ip = ia.getHostAddress();
                    if (!"127.0.0.1".equals(ip)) {
                        hostIp = ia.getHostAddress();
                        break;
                    }
                }
            }
        } catch (SocketException e) {
            //Log.i("yao", "SocketException");
            //e.printStackTrace();
            return null;
        }
        return hostIp;

    }

    /**
     * 将ip的整数形式转换成ip形式
     *
     * @param ipInt
     * @return
     */
    public static String int2ip(int ipInt) {
        StringBuilder sb = new StringBuilder();
        sb.append(ipInt & 0xFF).append(".");
        sb.append((ipInt >> 8) & 0xFF).append(".");
        sb.append((ipInt >> 16) & 0xFF).append(".");
        sb.append((ipInt >> 24) & 0xFF);
        return sb.toString();
    }

    /**
     * 获取当前WIFI ip地址
     *
     * @param context
     * @return
     */
    public static String getLocalIpAddress(Context context) {
        try {

            WifiManager wifiManager = (WifiManager) context
                    .getSystemService(Context.WIFI_SERVICE);
            WifiInfo wifiInfo = wifiManager.getConnectionInfo();
            int i = wifiInfo.getIpAddress();
            String ip = int2ip(i);
            if (ip.equals("0.0.0.0")) {
                return getHostIP();
            }
            return ip;
        } catch (Exception ex) {
            //return " 获取IP出错!!!!请保证是WIFI,或者请重新打开网络!\n" + ex.getMessage();
            return getHostIP();
        }
        // return null;
    }


    /**
     * 保存位图到本地
     *
     * @param bitmap
     * @param path   本地路径
     * @return void
     */
    public void SavaImage(Bitmap bitmap, String path) {
        File file = new File(path);
        FileOutputStream fileOutputStream = null;
        //文件夹不存在，则创建它
        if (!file.exists()) {
            file.mkdir();
        }
        try {
            fileOutputStream = new FileOutputStream(path + "/" + System.currentTimeMillis() + ".png");
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fileOutputStream);
            fileOutputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 判断文件是否存在
     *
     * @param filePath
     * @return
     */
    public static boolean fileIsExists(String filePath) {
        try {
            File f = new File(filePath);
            if (!f.exists()) {
                return false;
            }
        } catch (Exception e) {
            return false;
        }
        return true;
    }

    /**
     * 获取网络图片资源
     *
     * @param urls 服务器地址
     * @return
     */
    public static Bitmap getHttpBitmap(String urls) {
        //创建获取网络图片的变量
        Bitmap bitmap = null;
        InputStream inputStream = null;
        try {
            URL url = new URL(urls);                    //服务器地址
            if (url != null) {
                //打开连接
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.setConnectTimeout(8000);//设置网络连接超时的时间为3秒
                httpURLConnection.setRequestMethod("GET");        //设置请求方法为GET
                httpURLConnection.setDoInput(true);
                httpURLConnection.setUseCaches(false); //设置不使用缓存
                int responseCode = httpURLConnection.getResponseCode();    // 获取服务器响应值
                if (responseCode == HttpURLConnection.HTTP_OK) {        //正常连接
                    inputStream = httpURLConnection.getInputStream();        //获取输入流
                    bitmap = BitmapFactory.decodeStream(inputStream);
                    inputStream.close();
                    return bitmap;
                }
                return null;
            }
        } catch (Exception e) {
            Log.e("缓存网络图片出错：", "getHttpBitmap: ", e);
            return null;
        }
        return null;
    }

    /**
     * 获取网络图片资源
     *
     * @param urls 服务器地址
     * @return
     */
    public static boolean getHttpPho(String urls, String path, String code) {
        //创建获取网络图片的变量
        Bitmap bitmap = null;
        InputStream inputStream = null;
        //创建保存的文件对象
        File file = new File(path);
        FileOutputStream fileOutputStream = null;
        //文件夹不存在，则创fonts建它
        if (!file.exists()) {
            file.mkdir();
        }
        try {
            URL url = new URL(urls);                    //服务器地址
            if (url != null) {
                //打开连接
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.setConnectTimeout(8000);//设置网络连接超时的时间为3秒
                httpURLConnection.setRequestMethod("GET");        //设置请求方法为GET
                httpURLConnection.setDoInput(true);
                httpURLConnection.setUseCaches(false); //设置不使用缓存
                int responseCode = httpURLConnection.getResponseCode();    // 获取服务器响应值
                if (responseCode == HttpURLConnection.HTTP_OK) {        //正常连接
                    inputStream = httpURLConnection.getInputStream();        //获取输入流
                    bitmap = BitmapFactory.decodeStream(inputStream);

                    fileOutputStream = new FileOutputStream(path + "/" + code + ".jpg");
                    bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fileOutputStream);
                    fileOutputStream.close();

                    return true;
                }
                return false;
            }
        } catch (Exception e) {
            return false;
        }
        return false;
    }

    /**
     * 半角转全角
     *
     * @param inputStr
     * @return
     */
    public static String transport(String inputStr) {
        char arr[] = inputStr.toCharArray();
        for (int i = 0; i < arr.length; i++) {
            if (arr[i] == ' ') {
                arr[i] = '\u3000';
            } else if (arr[i] < '\177') {
                arr[i] = (char) (arr[i] + 65248);
            }

        }
        return new String(arr);
    }

    /**
     * 自动分割文本
     *
     * @param tv
     * @return
     */
    @SuppressLint("LongLogTag")
    public static String autoSplitText(final TextView tv) {
        final String rawText = tv.getText().toString(); //原始文本
        final Paint tvPaint = tv.getPaint(); //paint，包含字体等信息
        final float tvWidth = tv.getWidth() - tv.getPaddingLeft() - tv.getPaddingRight(); //控件可用宽度

        //将原始文本按行拆分
        String[] rawTextLines = rawText.replaceAll("\r", "").split("\n");
        StringBuilder sbNewText = new StringBuilder();
        for (String rawTextLine : rawTextLines) {
            if (tvPaint.measureText(rawTextLine) <= tvWidth) {
                //如果整行宽度在控件可用宽度之内，就不处理了
                sbNewText.append(rawTextLine);
            } else {
                //如果整行宽度超过控件可用宽度，则按字符测量，在超过可用宽度的前一个字符处手动换行
                float lineWidth = 0;
                for (int cnt = 0; cnt != rawTextLine.length(); ++cnt) {
                    char ch = rawTextLine.charAt(cnt);
                    lineWidth += tvPaint.measureText(String.valueOf(ch));
                    if (lineWidth <= tvWidth) {
                        sbNewText.append(ch);
                    } else {
                        sbNewText.append("\n");
                        lineWidth = 0;
                        --cnt;
                    }
                }
            }
            sbNewText.append("\n");
        }

        //把结尾多余的\n去掉
        if (!rawText.endsWith("\n")) {
            sbNewText.deleteCharAt(sbNewText.length() - 1);
        }
        return sbNewText.toString();
    }

    /**
     * 请求webservice获取数据
     *
     * @return
     */
    public static String getdata(String server_url, String service_name, String local_ip, String tip) {
        String data_map = null;
        //设置请求地址
        soap.SERVER_URL = server_url;
        //设置请求的方法名称
        soap.W_NAME = service_name;
        //请求数据
        try {
            String data = soap.getCityWeather(local_ip, tip);
            //String data = "[{\"zqmc\":\"中医诊区\",\"zsxh\":\"\",\"name\":\"\",\"zsmc\":\"1\",\"ks\":\"针灸\",\"tip\":\"-请 到-就 诊\",\"hjxx\":\"\",\"wxts\":\"请排队就诊，祝您早日康复，谢谢配合！\"}]";
            //String data = "[{\"YSGH\":\"000804X\",\"ZSBH\":\"1\",\"YSXM\":\"张医生\",\"YSZC\":\"主治医师\",\"YSJJ\":\"医生简介\",\"JZXM\":\"\",\"HZXM1\":\"\",\"HZXM2\":\"\",\"HZXM3\":\"\"}]";
            //String data = "[{\"ZSDM\":\"nfm01\",\"ZSMC\":\"一诊室\",\"YSGH\":\"1044\",\"YSXM\":\"陆帅\",\"YSZC\":\"副主任\",\"YSJJ\":\"擅长：擅长糖尿病3C治疗及糖尿病肾病神经病变和甲状腺疾病的诊治。\",\"JZXH\":\"37\",\"JZXM\":\"郁汉昌  \",\"DHJZ1\":\"38号胡*\",\"DHJZ2\":\"39号张*云\"}]";
            //String data = "[{\"YSXM\":\"罗张三\",\"YSGH\":\"1001\",\"YSZC\":\"副主任医师\",\"JZXH\":\"1001\",\"JZXM\":\"张三三\",\"DHJZ1\":\"10号 李四\",\"DHJZ2\":\"11号 张三\",\"YSJJ\":\"找零请当面点清，谢谢！\",\"ZSMC\":\"88号缴费处\"}]";
            //String data = "[{\"YSXM\":\"罗张三\",\"YSGH\":\"1001\",\"YSZC\":\"副主任医师\",\"DQXM\":\"李四\",\"YSJG\":\"500.23\",\"SSJG\":\"600.68\",\"ZLJG\":\"100.156\",\"TSXX\":\"找零请当面点清，谢谢！\",\"NAME\":\"88号缴费处\",\"WXTS\":\"请缴费患者有序排队，不拥挤，不插队，弘扬社会主义核心价值观！\"}]";
            //String data = "[{\"zqmc\":\"门诊医生坐诊一览表\",\"qzgx\":\"false\",\"wxts\":\"当前显示医生为今日坐诊医生，候诊人数因实时变化，具体人数以候诊大屏显示为准，请排队等候，谢谢配合！\",\"ysdm\":\"000804\",\"ysxm\":\"李宏大\",\"ysks\":\"李宏大工作室\",\"hzrs\":2},{\"zqmc\":\"门诊医生坐诊一览表\",\"qzgx\":\"false\",\"wxts\":\"当前显示医生为今日坐诊医生，候诊人数因实时变化，具体人数以候诊大屏显示为准，请排队等候，谢谢配合！\",\"ysdm\":\"000802\",\"ysxm\":\"周明飞\",\"ysks\":\"1号诊室\",\"hzrs\":2},{\"zqmc\":\"门诊医生坐诊一览表\",\"qzgx\":\"false\",\"wxts\":\"当前显示医生为今日坐诊医生，候诊人数因实时变化，具体人数以候诊大屏显示为准，请排队等候，谢谢配合！\",\"ysdm\":\"000807\",\"ysxm\":\"唐金华\",\"ysks\":\"1号诊室\",\"hzrs\":2},{\"zqmc\":\"门诊医生坐诊一览表\",\"qzgx\":\"false\",\"wxts\":\"当前显示医生为今日坐诊医生，候诊人数因实时变化，具体人数以候诊大屏显示为准，请排队等候，谢谢配合！\",\"ysdm\":\"000809\",\"ysxm\":\"易庆颖\",\"ysks\":\"3号诊室\",\"hzrs\":2},{\"zqmc\":\"门诊医生坐诊一览表\",\"qzgx\":\"false\",\"wxts\":\"当前显示医生为今日坐诊医生，候诊人数因实时变化，具体人数以候诊大屏显示为准，请排队等候，谢谢配合！\",\"ysdm\":\"000820\",\"ysxm\":\"王冬玲\",\"ysks\":\"4号诊室\",\"hzrs\":2},{\"zqmc\":\"门诊医生坐诊一览表\",\"qzgx\":\"false\",\"wxts\":\"当前显示医生为今日坐诊医生，候诊人数因实时变化，具体人数以候诊大屏显示为准，请排队等候，谢谢配合！\",\"ysdm\":\"000828\",\"ysxm\":\"蒋彩云\",\"ysks\":\"5号诊室\",\"hzrs\":2},{\"zqmc\":\"门诊医生坐诊一览表\",\"qzgx\":\"false\",\"wxts\":\"当前显示医生为今日坐诊医生，候诊人数因实时变化，具体人数以候诊大屏显示为准，请排队等候，谢谢配合！\",\"ysdm\":\"000834\",\"ysxm\":\"朱志娟\",\"ysks\":\"6号诊室\",\"hzrs\":2},{\"zqmc\":\"门诊医生坐诊一览表\",\"qzgx\":\"false\",\"wxts\":\"当前显示医生为今日坐诊医生，候诊人数因实时变化，具体人数以候诊大屏显示为准，请排队等候，谢谢配合！\",\"ysdm\":\"000851\",\"ysxm\":\"丁敏莉\",\"ysks\":\"7号诊室\",\"hzrs\":2},{\"zqmc\":\"门诊医生坐诊一览表\",\"qzgx\":\"false\",\"wxts\":\"当前显示医生为今日坐诊医生，候诊人数因实时变化，具体人数以候诊大屏显示为准，请排队等候，谢谢配合！\",\"ysdm\":\"000856\",\"ysxm\":\"陈敏\",\"ysks\":\"8号诊室\",\"hzrs\":2},{\"zqmc\":\"门诊医生坐诊一览表\",\"qzgx\":\"false\",\"wxts\":\"当前显示医生为今日坐诊医生，候诊人数因实时变化，具体人数以候诊大屏显示为准，请排队等候，谢谢配合！\",\"ysdm\":\"000859\",\"ysxm\":\"马玉琴\",\"ysks\":\"9号科室\",\"hzrs\":2},{\"zqmc\":\"门诊医生坐诊一览表\",\"qzgx\":\"false\",\"wxts\":\"当前显示医生为今日坐诊医生，候诊人数因实时变化，具体人数以候诊大屏显示为准，请排队等候，谢谢配合！\",\"ysdm\":\"001381\",\"ysxm\":\"王敏\",\"ysks\":\"10号诊室\",\"hzrs\":2},{\"zqmc\":\"门诊医生坐诊一览表\",\"qzgx\":\"false\",\"wxts\":\"当前显示医生为今日坐诊医生，候诊人数因实时变化，具体人数以候诊大屏显示为准，请排队等候，谢谢配合！\",\"ysdm\":\"001384\",\"ysxm\":\"韩丽丽\",\"ysks\":\"11号诊室\",\"hzrs\":2},{\"zqmc\":\"门诊医生坐诊一览表\",\"qzgx\":\"false\",\"wxts\":\"当前显示医生为今日坐诊医生，候诊人数因实时变化，具体人数以候诊大屏显示为准，请排队等候，谢谢配合！\",\"ysdm\":\"001382\",\"ysxm\":\"黄静\",\"ysks\":\"12号诊室\",\"hzrs\":2},{\"zqmc\":\"门诊医生坐诊一览表\",\"qzgx\":\"false\",\"wxts\":\"当前显示医生为今日坐诊医生，候诊人数因实时变化，具体人数以候诊大屏显示为准，请排队等候，谢谢配合！\",\"ysdm\":\"001543\",\"ysxm\":\"王春龙\",\"ysks\":\"13号诊室\",\"hzrs\":2},{\"zqmc\":\"门诊医生坐诊一览表\",\"qzgx\":\"false\",\"wxts\":\"当前显示医生为今日坐诊医生，候诊人数因实时变化，具体人数以候诊大屏显示为准，请排队等候，谢谢配合！\",\"ysdm\":\"001545\",\"ysxm\":\"孙彪\",\"ysks\":14号诊室\"\",\"hzrs\":2},{\"zqmc\":\"门诊医生坐诊一览表\",\"qzgx\":\"false\",\"wxts\":\"当前显示医生为今日坐诊医生，候诊人数因实时变化，具体人数以候诊大屏显示为准，请排队等候，谢谢配合！\",\"ysdm\":\"001788\",\"ysxm\":\"王冬玲\",\"ysks\":\"15号科室\",\"hzrs\":2},{\"zqmc\":\"门诊医生坐诊一览表\",\"qzgx\":\"false\",\"wxts\":\"当前显示医生为今日坐诊医生，候诊人数因实时变化，具体人数以候诊大屏显示为准，请排队等候，谢谢配合！\",\"ysdm\":\"003465\",\"ysxm\":\"陶倩\",\"ysks\":\"16号诊室\",\"hzrs\":2},{\"zqmc\":\"门诊医生坐诊一览表\",\"qzgx\":\"false\",\"wxts\":\"当前显示医生为今日坐诊医生，候诊人数因实时变化，具体人数以候诊大屏显示为准，请排队等候，谢谢配合！\",\"ysdm\":\"003863\",\"ysxm\":\"杨红梅\",\"ysks\":\"17号诊室\",\"hzrs\":2},{\"zqmc\":\"门诊医生坐诊一览表\",\"qzgx\":\"false\",\"wxts\":\"当前显示医生为今日坐诊医生，候诊人数因实时变化，具体人数以候诊大屏显示为准，请排队等候，谢谢配合！\",\"ysdm\":\"004189\",\"ysxm\":\"马莉莉\",\"ysks\":\"18号科室\",\"hzrs\":2},{\"zqmc\":\"门诊医生坐诊一览表\",\"qzgx\":\"false\",\"wxts\":\"当前显示医生为今日坐诊医生，候诊人数因实时变化，具体人数以候诊大屏显示为准，请排队等候，谢谢配合！\",\"ysdm\":\"004289\",\"ysxm\":\"周翠花\",\"ysks\":\"19号科室\",\"hzrs\":2},{\"zqmc\":\"门诊医生坐诊一览表\",\"qzgx\":\"false\",\"wxts\":\"当前显示医生为今日坐诊医生，候诊人数因实时变化，具体人数以候诊大屏显示为准，请排队等候，谢谢配合！\",\"ysdm\":\"004291\",\"ysxm\":\"尹相进\",\"ysks\":\"20号科室\",\"hzrs\":2},{\"zqmc\":\"门诊医生坐诊一览表\",\"qzgx\":\"false\",\"wxts\":\"当前显示医生为今日坐诊医生，候诊人数因实时变化，具体人数以候诊大屏显示为准，请排队等候，谢谢配合！\",\"ysdm\":\"004320\",\"ysxm\":\"樊爱民\",\"ysks\":\"21号科室\",\"hzrs\":2},{\"zqmc\":\"门诊医生坐诊一览表\",\"qzgx\":\"false\",\"wxts\":\"当前显示医生为今日坐诊医生，候诊人数因实时变化，具体人数以候诊大屏显示为准，请排队等候，谢谢配合！\",\"ysdm\":\"004551\",\"ysxm\":\"李平\",\"ysks\":\"22号科室\",\"hzrs\":2},{\"zqmc\":\"门诊医生坐诊一览表\",\"qzgx\":\"false\",\"wxts\":\"当前显示医生为今日坐诊医生，候诊人数因实时变化，具体人数以候诊大屏显示为准，请排队等候，谢谢配合！\",\"ysdm\":\"004728\",\"ysxm\":\"杭跃跃\",\"ysks\":\"23号科室\",\"hzrs\":2},{\"zqmc\":\"门诊医生坐诊一览表\",\"qzgx\":\"false\",\"wxts\":\"当前显示医生为今日坐诊医生，候诊人数因实时变化，具体人数以候诊大屏显示为准，请排队等候，谢谢配合！\",\"ysdm\":\"004770\",\"ysxm\":\"邢宪乐\",\"ysks\":\"24号科室\",\"hzrs\":2},{\"zqmc\":\"门诊医生坐诊一览表\",\"qzgx\":\"false\",\"wxts\":\"当前显示医生为今日坐诊医生，候诊人数因实时变化，具体人数以候诊大屏显示为准，请排队等候，谢谢配合！\",\"ysdm\":\"004956\",\"ysxm\":\"陶珍\",\"ysks\":\"25号科室\",\"hzrs\":2},{\"zqmc\":\"门诊医生坐诊一览表\",\"qzgx\":\"false\",\"wxts\":\"当前显示医生为今日坐诊医生，候诊人数因实时变化，具体人数以候诊大屏显示为准，请排队等候，谢谢配合！\",\"ysdm\":\"004957\",\"ysxm\":\"张进秋\",\"ysks\":\"26号科室\",\"hzrs\":2},{\"zqmc\":\"门诊医生坐诊一览表\",\"qzgx\":\"false\",\"wxts\":\"当前显示医生为今日坐诊医生，候诊人数因实时变化，具体人数以候诊大屏显示为准，请排队等候，谢谢配合！\",\"ysdm\":\"004988\",\"ysxm\":\"张伟\",\"ysks\":\"27号科室\",\"hzrs\":2},{\"zqmc\":\"门诊医生坐诊一览表\",\"qzgx\":\"false\",\"wxts\":\"当前显示医生为今日坐诊医生，候诊人数因实时变化，具体人数以候诊大屏显示为准，请排队等候，谢谢配合！\",\"ysdm\":\"005013\",\"ysxm\":\"周无忌\",\"ysks\":\"28号科室\",\"hzrs\":2},{\"zqmc\":\"门诊医生坐诊一览表\",\"qzgx\":\"false\",\"wxts\":\"当前显示医生为今日坐诊医生，候诊人数因实时变化，具体人数以候诊大屏显示为准，请排队等候，谢谢配合！\",\"ysdm\":\"005188\",\"ysxm\":\"刘良好\",\"ysks\":\"29号科室\",\"hzrs\":2},{\"zqmc\":\"门诊医生坐诊一览表\",\"qzgx\":\"false\",\"wxts\":\"当前显示医生为今日坐诊医生，候诊人数因实时变化，具体人数以候诊大屏显示为准，请排队等候，谢谢配合！\",\"ysdm\":\"005314\",\"ysxm\":\"张文瑶\",\"ysks\":\"30号科室\",\"hzrs\":2},{\"zqmc\":\"门诊医生坐诊一览表\",\"qzgx\":\"false\",\"wxts\":\"当前显示医生为今日坐诊医生，候诊人数因实时变化，具体人数以候诊大屏显示为准，请排队等候，谢谢配合！\",\"ysdm\":\"005722\",\"ysxm\":\"严夕霞\",\"ysks\":\"34号科室\",\"hzrs\":2},{\"zqmc\":\"门诊医生坐诊一览表\",\"qzgx\":\"false\",\"wxts\":\"当前显示医生为今日坐诊医生，候诊人数因实时变化，具体人数以候诊大屏显示为准，请排队等候，谢谢配合！\",\"ysdm\":\"005750\",\"ysxm\":\"许迎照\",\"ysks\":\"35号科室\",\"hzrs\":2},{\"zqmc\":\"门诊医生坐诊一览表\",\"qzgx\":\"false\",\"wxts\":\"当前显示医生为今日坐诊医生，候诊人数因实时变化，具体人数以候诊大屏显示为准，请排队等候，谢谢配合！\",\"ysdm\":\"005716\",\"ysxm\":\"朱咪咪\",\"ysks\":\"33号科室\",\"hzrs\":2},{\"zqmc\":\"门诊医生坐诊一览表\",\"qzgx\":\"false\",\"wxts\":\"当前显示医生为今日坐诊医生，候诊人数因实时变化，具体人数以候诊大屏显示为准，请排队等候，谢谢配合！\",\"ysdm\":\"005540\",\"ysxm\":\"昌娟\",\"ysks\":\"32号科室\",\"hzrs\":2},{\"zqmc\":\"门诊医生坐诊一览表\",\"qzgx\":\"false\",\"wxts\":\"当前显示医生为今日坐诊医生，候诊人数因实时变化，具体人数以候诊大屏显示为准，请排队等候，谢谢配合！\",\"ysdm\":\"005422\",\"ysxm\":\"张俊\",\"ysks\":\"31号科室\",\"hzrs\":2}]";
            //String data = "[{\"ZSMC\":\"门诊医生坐诊一览表\",\"KSMC\":\"false\",\"DQJZ\":\"\",\"DHJZ\":\"000804\",\"PDRS\":\"李宏大\",\"WXTS\":\"李宏大工作室\",\"hzrs\":2},{\"ZSMC\":\"门诊医生坐诊一览表\",\"KSMC\":\"false\",\"DQJZ\":\"13号 李思思似\",\"DHJZ\":\"000807\",\"PDRS\":\"唐金华\",\"WXTS\":\"儿科诊室二\",\"hzrs\":6},{\"ZSMC\":\"门诊医生坐诊一览表\",\"KSMC\":\"false\",\"DQJZ\":\"13号 李思思似\",\"DHJZ\":\"000809\",\"PDRS\":\"易庆颖\",\"WXTS\":\"中医内科一室\",\"hzrs\":4},{\"ZSMC\":\"门诊医生坐诊一览表\",\"KSMC\":\"false\",\"DQJZ\":\"13号 李思思似\",\"DHJZ\":\"000820\",\"PDRS\":\"王冬玲\",\"WXTS\":\"三号诊室\",\"hzrs\":0},{\"ZSMC\":\"门诊医生坐诊一览表\",\"KSMC\":\"false\",\"DQJZ\":\"13号 李思思似\",\"DHJZ\":\"000828\",\"PDRS\":\"蒋彩云\",\"WXTS\":\"四号诊室\",\"hzrs\":0},{\"ZSMC\":\"门诊医生坐诊一览表\",\"KSMC\":\"false\",\"DQJZ\":\"13号 李思思似\",\"DHJZ\":\"000851\",\"PDRS\":\"丁敏莉\",\"WXTS\":\"妇科一室\",\"hzrs\":2},{\"ZSMC\":\"门诊医生坐诊一览表\",\"KSMC\":\"false\",\"DQJZ\":\"13号 李思思似\",\"DHJZ\":\"000859\",\"PDRS\":\"马玉琴\",\"WXTS\":\"六号诊室\",\"hzrs\":0},{\"ZSMC\":\"门诊医生坐诊一览表\",\"KSMC\":\"false\",\"DQJZ\":\"13号 李思思似\",\"DHJZ\":\"001384\",\"PDRS\":\"韩丽丽\",\"WXTS\":\"韩丽丽工作室\",\"hzrs\":2},{\"ZSMC\":\"门诊医生坐诊一览表\",\"KSMC\":\"false\",\"DQJZ\":\"13号 李思思似\",\"DHJZ\":\"003863\",\"PDRS\":\"杨红梅\",\"WXTS\":\"1号诊室\",\"hzrs\":0},{\"ZSMC\":\"门诊医生坐诊一览表\",\"KSMC\":\"false\",\"DQJZ\":\"13号 李思思似\",\"DHJZ\":\"004189\",\"PDRS\":\"马莉莉\",\"WXTS\":\"妇科二室\",\"hzrs\":8}]";
            //String data = "[{\"ZSBH\":\"北站诊室\",\"YSXM\":\"阿萨斯\",\"YSZC\":\" zhuren\",\"YSJJ\":\"简介:     色鹅鹅鹅饿鹅鹅，鹅鹅鹅，鹅鹅鹅鹅鹅鹅鹅，饿鹅鹅鹅，饿鹅鹅鹅饿鹅鹅鹅饿水电费，付付付付付付付付付付付，付付付付付付付付付付付付付付付付付付付付\",\"HZXM1\":\"zhangjiujiu\",\"JZXM\":\"牛爱花\",\"YSGH\":\"001\",\"HZXM2\":\"张牛牛\"}]";
            //String data = "[{\"ZSMC\":\"北站诊室\",\"DQJY_BH\":\"65号\",\"DQJY_MZ\":\"牛爱花\"},{\"JYBH\":\"67号\",\"JYMZ\":\"张牛牛\"},{\"JYBH\":\"68号\",\"JYMZ\":\"张牛牛\"},{\"JYBH\":\"69号\",\"JYMZ\":\"张牛牛\"},{\"JYBH\":\"70号\",\"JYMZ\":\"张牛牛\"},{\"JYBH\":\"71号\",\"JYMZ\":\"张牛牛\"},{\"JYBH\":\"72号\",\"JYMZ\":\"张牛牛\"}]";
            //jiangqiao_high_3
            //String data = "[{\"ZQMC\":\"测试诊区\",\"ZSMC\":\"诊室1\",\"KSMC\":\"科室1\",\"JZXH\":\"12号\",\"JZXM\":\"不想上班\",\"WXTS\":\"排队！！！！！！！！！\"},{\"ZQMC\":\"测试诊区\",\"ZSMC\":\"诊室2\",\"KSMC\":\"科室2\",\"JZXH\":\"12号\",\"JZXM\":\"不想上班\",\"WXTS\":\"排队！！！！！！！！！\"},{\"ZQMC\":\"测试诊区\",\"ZSMC\":\"诊室3\",\"KSMC\":\"科室3\",\"JZXH\":\"12号\",\"JZXM\":\"不想上班\",\"WXTS\":\"排队！！！！！！！！！\"},{\"ZQMC\":\"测试诊区\",\"ZSMC\":\"诊室4\",\"KSMC\":\"科室4\",\"JZXH\":\"12号\",\"JZXM\":\"不想上班\",\"WXTS\":\"排队！！！！！！！！！\"},{\"ZQMC\":\"测试诊区\",\"ZSMC\":\"诊室5\",\"KSMC\":\"科室5\",\"JZXH\":\"12号\",\"JZXM\":\"不想上班\",\"WXTS\":\"排队！！！！！！！！！\"},{\"ZQMC\":\"测试诊区\",\"ZSMC\":\"诊室6\",\"KSMC\":\"科室6\",\"JZXH\":\"12号\",\"JZXM\":\"不想上班\",\"WXTS\":\"排队！！！！！！！！！\"},{\"ZQMC\":\"测试诊区\",\"ZSMC\":\"诊室7\",\"KSMC\":\"科室7\",\"JZXH\":\"12号\",\"JZXM\":\"不想上班\",\"WXTS\":\"排队！！！！！！！！！\"},{\"ZQMC\":\"测试诊区\",\"ZSMC\":\"诊室7\",\"KSMC\":\"科室7\",\"JZXH\":\"12号\",\"JZXM\":\"不想上班\",\"WXTS\":\"排队！！！！！！！！！\"},{\"ZQMC\":\"测试诊区\",\"ZSMC\":\"诊室7\",\"KSMC\":\"科室7\",\"JZXH\":\"12号\",\"JZXM\":\"不想上班\",\"WXTS\":\"排队！！！！！！！！！\"},{\"ZQMC\":\"测试诊区\",\"ZSMC\":\"诊室7\",\"KSMC\":\"科室7\",\"JZXH\":\"12号\",\"JZXM\":\"不想上班\",\"WXTS\":\"排队！！！！！！！！！\"}]";
            //kunshan_high_1
            //String data ="[{\"ZQMC\":\"诊区名称\",\"ZSMC\":\"123\",\"ZZJC\":\"是是是\",\"MZHZ\":\"54孙忠海-60张荣侯 -1归查珍-62余课宁-40凌培生-91徐文重-117未期龙-121加联伟-122曹革平-126范水路\",\"ZYHZ\":\"54孙忠海-60张荣侯 -1归查珍-62余课宁-40凌培生-91徐文重-117未期龙-121加联伟-122曹革平-126范水路-120王红萍-131起章华-152江王芳-158钱惠芬-161数巧5-186 王路-180杜白林-190燕宝龙-192高秋幕-193手面\"},{\"ZQMC\":\"诊区名称\",\"ZSMC\":\"123\",\"ZZJC\":\"是是是\",\"MZHZ\":\"54孙忠海-60张荣侯 -1归查珍-62余课宁-40凌培生-91徐文重-117未期龙-121加联伟-122曹革平-126范水路-120王红萍-131起章华-152江王芳-158钱惠芬-161数巧5-186 王路-180杜白林-190燕宝龙-192高秋幕-193手面\",\"ZYHZ\":\"54孙忠海-60张荣侯 -1归查珍-62余课宁-40凌培生-91徐文重-117未期龙-121加联伟-122曹革平-126范水路-120王红萍-131起章华-152江王芳-158钱惠芬-161数巧5-186 王路-180杜白林-190燕宝龙-192高秋幕-193手面\"},{\"ZQMC\":\"诊区名称\",\"ZSMC\":\"123\",\"ZZJC\":\"是是是\",\"MZHZ\":\"54孙忠海-60张荣侯 -1归查珍-62余课宁-40凌培生-91徐文重-117未期龙-121加联伟-122曹革平-126范水路-120王红萍-131起章华-152江王芳-158钱惠芬-161数巧5-186 王路-180杜白林-190燕宝龙-192高秋幕-193手面\",\"ZYHZ\":\"54孙忠海-60张荣侯 -1归查珍-62余课宁-40凌培生-91徐文重-117未期龙-121加联伟-122曹革平-126范水路-120王红萍-131起章华-152江王芳-158钱惠芬-161数巧5-186 王路-180杜白林-190燕宝龙-192高秋幕-193手面\"},{\"ZQMC\":\"诊区名称\",\"ZSMC\":\"123\",\"ZZJC\":\"是是是\",\"MZHZ\":\"54孙忠海-60张荣侯 -1归查珍-62余课宁-40凌培生-91徐文重-117未期龙-121加联伟-122曹革平-126范水路-120王红萍-131起章华-152江王芳-158钱惠芬-161数巧5-186 王路-180杜白林-190燕宝龙-192高秋幕-193手面\",\"ZYHZ\":\"54孙忠海-60张荣侯 -1归查珍-62余课宁-40凌培生-91徐文重-117未期龙-121加联伟-122曹革平-126范水路-120王红萍-131起章华-152江王芳-158钱惠芬-161数巧5-186 王路-180杜白林-190燕宝龙-192高秋幕-193手面\"},{\"ZQMC\":\"诊区名称\",\"ZSMC\":\"123\",\"ZZJC\":\"是是是\",\"MZHZ\":\"54孙忠海-60张荣侯 -1归查珍-62余课宁-40凌培生-91徐文重-117未期龙-121加联伟-122曹革平-126范水路-120王红萍-131起章华-152江王芳-158钱惠芬-161数巧5-186 王路-180杜白林-190燕宝龙-192高秋幕-193手面\",\"ZYHZ\":\"54孙忠海-60张荣侯 -1归查珍-62余课宁-40凌培生-91徐文重-117未期龙-121加联伟-122曹革平-126范水路-120王红萍-131起章华-152江王芳-158钱惠芬-161数巧5-186 王路-180杜白林-190燕宝龙-192高秋幕-193手面\"}]";
            //String data = "[{\"ZQMC\":\"新诊区\",\"ZSMC\":\"新诊室\",\"DQJZ\":\"12号李丽丽\",\"DHJZ\":\"000804\",\"MZHZ\":\"李宏大\",\"ZYHZ\":\"李宏大工作室\",\"hzrs\":2},{\"ZQMC\":\"新诊区\",\"ZSMC\":\"新诊室\",\"DQJZ\":\"13号 李思思似\",\"DHJZ\":\"000807\",\"MZHZ\":\"唐金华\",\"ZYHZ\":\"儿科诊室二\",\"hzrs\":6},{\"ZQMC\":\"新诊区\",\"ZSMC\":\"新诊室\",\"DQJZ\":\"13号 李思思似\",\"DHJZ\":\"000809\",\"MZHZ\":\"易庆颖\",\"ZYHZ\":\"中医内科一室\",\"hzrs\":4},{\"ZQMC\":\"新诊区\",\"ZSMC\":\"新诊室\",\"DQJZ\":\"13号 李思思似\",\"DHJZ\":\"000820\",\"MZHZ\":\"王冬玲\",\"ZYHZ\":\"三号诊室\",\"hzrs\":0},{\"ZQMC\":\"新诊区\",\"ZSMC\":\"新诊室\",\"KSMC\":\"新科室\",\"DQJZ\":\"13号 李思思似\",\"DHJZ\":\"000828\",\"MZHZ\":\"蒋彩云\",\"ZYHZ\":\"四号诊室\",\"hzrs\":0},{\"ZQMC\":\"新诊区\",\"ZSMC\":\"新诊室\",\"DQJZ\":\"13号 李思思似\",\"DHJZ\":\"000851\",\"MZHZ\":\"丁敏莉\",\"ZYHZ\":\"妇科一室\",\"hzrs\":2},{\"ZQMC\":\"新诊区\",\"ZSMC\":\"新诊室\",\"DQJZ\":\"13号 李思思似\",\"DHJZ\":\"000859\",\"MZHZ\":\"马玉琴\",\"ZYHZ\":\"六号诊室\",\"hzrs\":0},{\"ZQMC\":\"新诊区\",\"ZSMC\":\"新诊室\",\"DQJZ\":\"13号 李思思似\",\"DHJZ\":\"001384\",\"MZHZ\":\"韩丽丽\",\"ZYHZ\":\"韩丽丽工作室\",\"hzrs\":2},{\"ZQMC\":\"新诊区\",\"ZSMC\":\"新诊室\",\"DQJZ\":\"13号 李思思似\",\"DHJZ\":\"003863\",\"MZHZ\":\"杨红梅\",\"ZYHZ\":\"1号诊室\",\"hzrs\":0},{\"ZQMC\":\"新诊区\",\"ZSMC\":\"新诊室\",\"DQJZ\":\"13号 李思思似\",\"DHJZ\":\"004189\",\"MZHZ\":\"马莉莉\",\"ZYHZ\":\"妇科二室\",\"hzrs\":8}]";
            //beizhan_yao
            //String data = "[{\"DB\":\"000804X\",\"ZL\":\"九九九\",\"DM\":\"张医生\",\"MC\":\"主治医师\",\"DW\":\"医生简介\",\"DJ\":\"200\"},{\"DB\":\"04X\",\"ZL\":\"九九九\",\"DM\":\"张医生\",\"MC\":\"主治医师\",\"DW\":\"医生简介\",\"DJ\":\"200\"},{\"DB\":\"00080\",\"ZL\":\"九九九\",\"DM\":\"张医生\",\"MC\":\"主治医师\",\"DW\":\"医生简介\",\"DJ\":\"200\"},{\"DB\":\"000804X\",\"ZL\":\"九九\",\"DM\":\"张医生\",\"MC\":\"主治医师\",\"DW\":\"医生简介\",\"DJ\":\"200\"},{\"DB\":\"000804X\",\"ZL\":\"九九九\",\"DM\":\"张医生\",\"MC\":\"主师\",\"DW\":\"医生简介\",\"DJ\":\"200\"},{\"DB\":\"000804X\",\"ZL\":\"九九九\",\"DM\":\"张医生\",\"MC\":\"主治医师\",\"DW\":\"简介\",\"DJ\":\"20\"},{\"DB\":\"000804X\",\"ZL\":\"九九九\",\"DM\":\"张医生\",\"MC\":\"主师\",\"DW\":\"医生简介\",\"DJ\":\"200\"},{\"DB\":\"000804X\",\"ZL\":\"九九九\",\"DM\":\"张医生\",\"MC\":\"主师\",\"DW\":\"医生简介\",\"DJ\":\"200\"},{\"DB\":\"000804X\",\"ZL\":\"九九九\",\"DM\":\"张医生\",\"MC\":\"主师\",\"DW\":\"医生简介\",\"DJ\":\"200\"},{\"DB\":\"000804X\",\"ZL\":\"九九九\",\"DM\":\"张医生\",\"MC\":\"主师\",\"DW\":\"医生简介\",\"DJ\":\"200\"},{\"DB\":\"000804X\",\"ZL\":\"九九九\",\"DM\":\"张医生\",\"MC\":\"主师\",\"DW\":\"医生简介\",\"DJ\":\"200\"},{\"DB\":\"000804X\",\"ZL\":\"九九九\",\"DM\":\"张医生\",\"MC\":\"主师\",\"DW\":\"医生简介\",\"DJ\":\"200\"},{\"DB\":\"000804X\",\"ZL\":\"九九九\",\"DM\":\"张医生\",\"MC\":\"主师\",\"DW\":\"医生简介\",\"DJ\":\"200\"},{\"DB\":\"000804X\",\"ZL\":\"九九九\",\"DM\":\"张医生\",\"MC\":\"主师\",\"DW\":\"医生简介\",\"DJ\":\"200\"},{\"DB\":\"000804X\",\"ZL\":\"九九九\",\"DM\":\"张医生\",\"MC\":\"主师\",\"DW\":\"医生简介\",\"DJ\":\"200\"},{\"DB\":\"000804X\",\"ZL\":\"九九九\",\"DM\":\"张医生\",\"MC\":\"主师\",\"DW\":\"医生简介\",\"DJ\":\"200\"}]";
            //beizhan_high_1
            //String data = "[{\"DB\":\"000804X\",\"MZKS\":\"九九九\",\"YSXM\":\"张医生\",\"KZSJ\":\"主治医师\",\"GHRS\":\"医生简介\"},{\"DB\":\"w\",\"MZKS\":\"九九九\",\"YSXM\":\"张医生\",\"KZSJ\":\"主治医师\",\"GHRS\":\"医生简介\"},{\"DB\":\"000804X\",\"MZKS\":\"九s\",\"YSXM\":\"张医生\",\"KZSJ\":\"主治医师\",\"GHRS\":\"医生简介\"},{\"DB\":\"000804X\",\"MZKS\":\"九九九\",\"YSXM\":\"张医生\",\"KZSJ\":\"主治医师\",\"GHRS\":\"医生简介\"},{\"DB\":\"000804X\",\"MZKS\":\"九九九\",\"YSXM\":\"张医生\",\"KZSJ\":\"fsd师\",\"GHRS\":\"医生简介\"},{\"DB\":\"000804X\",\"MZKS\":\"九九九\",\"YSXM\":\"张医生\",\"KZSJ\":\"主治医师\",\"GHRS\":\"医生简介\"},{\"DB\":\"000804X\",\"MZKS\":\"九九九\",\"YSXM\":\"张医生\",\"KZSJ\":\"主治医师\",\"GHRS\":\"医生简介\"},{\"DB\":\"000804X\",\"MZKS\":\"九九九\",\"YSXM\":\"张医生\",\"KZSJ\":\"主治医师\",\"GHRS\":\"医生简介\"},{\"DB\":\"000804X\",\"MZKS\":\"九九九\",\"YSXM\":\"张医生\",\"KZSJ\":\"\",\"GHRS\":\"医生简介\"},{\"DB\":\"000804X\",\"MZKS\":\"九九九\",\"YSXM\":\"张医生\",\"KZSJ\":\"主治医师\",\"GHRS\":\"医生简介\"},{\"DB\":\"000804X\",\"MZKS\":\"九九九\",\"YSXM\":\"张医生\",\"KZSJ\":\"主治医师\",\"GHRS\":\"简介\"}]";
            //xinhua_high_bchao
            //String data = "[{\"DLMC\":\"duilie1\",\"ZSMC\":\"诊室01\",\"ZZJZ\":\"12号李丽丽\",\"DHJZ\":\"张小明\",\"PDRS\":\"李宏大\",\"WXTS\":\"有序就诊\"},{\"DLMC\":\"duilie2\",\"ZSMC\":\"诊室02\",\"ZZJZ\":\"12号李丽丽\",\"DHJZ\":\"张小明\",\"PDRS\":\"李宏大\",\"WXTS\":\"有序就诊\"},{\"DLMC\":\"duilie3\",\"ZSMC\":\"诊室03\",\"ZZJZ\":\"12号李丽丽\",\"DHJZ\":\"张小明\",\"PDRS\":\"李宏大\",\"WXTS\":\"有序就诊\"},{\"DLMC\":\"duilie4\",\"ZSMC\":\"诊室04\",\"ZZJZ\":\"12号李丽丽\",\"DHJZ\":\"张小明\",\"PDRS\":\"李宏大\",\"WXTS\":\"有序就诊\"},{\"DLMC\":\"duilie5\",\"ZSMC\":\"诊室05\",\"ZZJZ\":\"12号李丽丽\",\"DHJZ\":\"张小明\",\"PDRS\":\"李宏大\",\"WXTS\":\"有序就诊\"},{\"DLMC\":\"duilie6\",\"ZSMC\":\"诊室06\",\"ZZJZ\":\"12号李丽丽\",\"DHJZ\":\"张小明\",\"PDRS\":\"李宏大\",\"WXTS\":\"有序就诊\"},{\"DLMC\":\"duilie7\",\"ZSMC\":\"诊室07\",\"ZZJZ\":\"12号李丽丽\",\"DHJZ\":\"张小明\",\"PDRS\":\"李宏大\",\"WXTS\":\"有序就诊\"},{\"DLMC\":\"duilie8\",\"ZSMC\":\"诊室08\",\"ZZJZ\":\"12号李丽丽\",\"DHJZ\":\"张小明\",\"PDRS\":\"李宏大\",\"WXTS\":\"有序就诊\"},{\"DLMC\":\"duilie9\",\"ZSMC\":\"诊室09\",\"ZZJZ\":\"12号李丽丽\",\"DHJZ\":\"张小明\",\"PDRS\":\"李宏大\",\"WXTS\":\"有序就诊\"},{\"DLMC\":\"duilie10\",\"ZSMC\":\"诊室10\",\"ZZJZ\":\"12号李丽丽\",\"DHJZ\":\"张小明\",\"PDRS\":\"李宏大\",\"WXTS\":\"有序就诊\"},{\"DLMC\":\"duilie11\",\"ZSMC\":\"诊室11\",\"ZZJZ\":\"12号李丽丽\",\"DHJZ\":\"张小明\",\"PDRS\":\"李宏大\",\"WXTS\":\"有序就诊\"},{\"DLMC\":\"duilie12\",\"ZSMC\":\"诊室12\",\"ZZJZ\":\"12号李丽丽\",\"DHJZ\":\"张小明\",\"PDRS\":\"李宏大\",\"WXTS\":\"有序就诊\"},{\"DLMC\":\"duilie1\",\"ZSMC\":\"诊室01\",\"ZZJZ\":\"12号李丽丽\",\"DHJZ\":\"张小明\",\"PDRS\":\"李宏大\",\"WXTS\":\"有序就诊\"},{\"DLMC\":\"duilie2\",\"ZSMC\":\"诊室02\",\"ZZJZ\":\"12号李丽丽\",\"DHJZ\":\"张小明\",\"PDRS\":\"李宏大\",\"WXTS\":\"有序就诊\"},{\"DLMC\":\"duilie3\",\"ZSMC\":\"诊室03\",\"ZZJZ\":\"12号李丽丽\",\"DHJZ\":\"张小明\",\"PDRS\":\"李宏大\",\"WXTS\":\"有序就诊\"},{\"DLMC\":\"duilie4\",\"ZSMC\":\"诊室04\",\"ZZJZ\":\"12号李丽丽\",\"DHJZ\":\"张小明\",\"PDRS\":\"李宏大\",\"WXTS\":\"有序就诊\"},{\"DLMC\":\"duilie5\",\"ZSMC\":\"诊室05\",\"ZZJZ\":\"12号李丽丽\",\"DHJZ\":\"张小明\",\"PDRS\":\"李宏大\",\"WXTS\":\"有序就诊\"},{\"DLMC\":\"duilie6\",\"ZSMC\":\"诊室06\",\"ZZJZ\":\"12号李丽丽\",\"DHJZ\":\"张小明\",\"PDRS\":\"李宏大\",\"WXTS\":\"有序就诊\"},{\"DLMC\":\"duilie7\",\"ZSMC\":\"诊室07\",\"ZZJZ\":\"12号李丽丽\",\"DHJZ\":\"张小明\",\"PDRS\":\"李宏大\",\"WXTS\":\"有序就诊\"},{\"DLMC\":\"duilie8\",\"ZSMC\":\"诊室08\",\"ZZJZ\":\"12号李丽丽\",\"DHJZ\":\"张小明\",\"PDRS\":\"李宏大\",\"WXTS\":\"有序就诊\"},{\"DLMC\":\"duilie9\",\"ZSMC\":\"诊室09\",\"ZZJZ\":\"12号李丽丽\",\"DHJZ\":\"张小明\",\"PDRS\":\"李宏大\",\"WXTS\":\"有序就诊\"},{\"DLMC\":\"duilie10\",\"ZSMC\":\"诊室10\",\"ZZJZ\":\"12号李丽丽\",\"DHJZ\":\"张小明\",\"PDRS\":\"李宏大\",\"WXTS\":\"有序就诊\"},{\"DLMC\":\"duilie11\",\"ZSMC\":\"诊室11\",\"ZZJZ\":\"12号李丽丽\",\"DHJZ\":\"张小明\",\"PDRS\":\"李宏大\",\"WXTS\":\"有序就诊\"},{\"DLMC\":\"duilie12\",\"ZSMC\":\"诊室12\",\"ZZJZ\":\"12号李丽丽\",\"DHJZ\":\"张小明\",\"PDRS\":\"李宏大\",\"WXTS\":\"有序就诊\"}]";
            //新华3
            //String data  = "[{\"ZQMC\":\"测试区\",\"DLMC\":\"队列1\",\"ZSMC\":\"诊室01\",\"JZXH\":\"01号\",\"BRXM\":\"张小明\",\"PDRS\":\"21\",\"WXTS\":\"有序就诊\"},{\"ZQMC\":\"测试区\",\"DLMC\":\"队列2\",\"ZSMC\":\"诊室02\",\"JZXH\":\"02号\",\"BRXM\":\"张小明\",\"PDRS\":\"22\",\"WXTS\":\"有序就诊\"},{\"ZQMC\":\"测试区\",\"DLMC\":\"队列3\",\"ZSMC\":\"诊室03\",\"JZXH\":\"03号\",\"BRXM\":\"张小明\",\"PDRS\":\"33\",\"WXTS\":\"有序就诊\"},{\"ZQMC\":\"测试区\",\"DLMC\":\"队列4\",\"ZSMC\":\"诊室04\",\"JZXH\":\"04号\",\"BRXM\":\"张小明\",\"PDRS\":\"44\",\"WXTS\":\"有序就诊\"},{\"ZQMC\":\"测试区\",\"DLMC\":\"队列5\",\"ZSMC\":\"诊室05\",\"JZXH\":\"05号\",\"BRXM\":\"张小明\",\"PDRS\":\"55\",\"WXTS\":\"有序就诊\"},{\"ZQMC\":\"测试区\",\"DLMC\":\"队列6\",\"ZSMC\":\"诊室06\",\"JZXH\":\"06号\",\"BRXM\":\"张小明\",\"PDRS\":\"66\",\"WXTS\":\"有序就诊\"},{\"ZQMC\":\"测试区\",\"DLMC\":\"队列7\",\"ZSMC\":\"诊室07\",\"JZXH\":\"07号\",\"BRXM\":\"张小明\",\"PDRS\":\"77\",\"WXTS\":\"有序就诊\"}]";
            //xinhua_low_1
            //String data = "[{\"ZSMC\":\"诊室01\",\"ZZJZXH\":\"12号\",\"ZZJZXM\":\"张小明\",\"DHXH1\":\"DHXH1\",\"DHXM1\":\"DHXM1\",\"DHXH2\":\"DHXH2\",\"DHXM2\":\"DHXM2\",\"DHXH3\":\"DHXH3\",\"DHXM3\":\"DHXM3\",\"DHXH4\":\"DHXH4\",\"DHXM4\":\"DHXM4\",\"WXTS\":\"把最好的留给孩子\"}]";
            //xinhua_high_2
            //String data = "[{\"DHXH\":\"1号\",\"DHXM\":\"test1\",\"ZSMC\":\"测试诊室1\",\"ZSDM\":\"001\",\"ZZJZXH\":\"3\",\"ZZJZXM\":\"041测试\"},{\"DHXH\":\"2号\",\"DHXM\":\"test2\",\"ZSMC\":\"测试诊室2\",\"ZSDM\":\"001\",\"ZZJZXH\":\"3\",\"ZZJZXM\":\"041测试\"},{\"DHXH\":\"3号\",\"DHXM\":\"test3\",\"ZSMC\":\"测试诊室3\",\"ZSDM\":\"002\",\"ZZJZXH\":\"3\",\"ZZJZXM\":\"041测试\"},{\"DHXH\":\"4号\",\"DHXM\":\"test4\",\"ZSMC\":\"测试诊室4\",\"ZSDM\":\"002\",\"ZZJZXH\":\"3\",\"ZZJZXM\":\"041测试\"},{\"DHXH\":\"55号\",\"DHXM\":\"test4\",\"ZSMC\":\"测试诊室5\",\"ZSDM\":\"002\",\"ZZJZXH\":\"3\",\"ZZJZXM\":\"041测试\"},{\"DHXH\":\"55号\",\"DHXM\":\"test4\",\"ZSMC\":\"测试诊室5\",\"ZSDM\":\"002\",\"ZZJZXH\":\"3\",\"ZZJZXM\":\"041测试\"},{\"DHXH\":\"55号\",\"DHXM\":\"test4\",\"ZSMC\":\"测试诊室5\",\"ZSDM\":\"002\",\"ZZJZXH\":\"3\",\"ZZJZXM\":\"041测试\"},{\"DHXH\":\"55号\",\"DHXM\":\"test4\",\"ZSMC\":\"测试诊室5\",\"ZSDM\":\"002\",\"ZZJZXH\":\"3\",\"ZZJZXM\":\"041测试\"},{\"DHXH\":\"55号\",\"DHXM\":\"test4\",\"ZSMC\":\"测试诊室5\",\"ZSDM\":\"002\",\"ZZJZXH\":\"3\",\"ZZJZXM\":\"041测试\"},{\"DHXH\":\"55号\",\"DHXM\":\"test4\",\"ZSMC\":\"测试诊室5\",\"ZSDM\":\"002\",\"ZZJZXH\":\"3\",\"ZZJZXM\":\"041测试\"},{\"DHXH\":\"55号\",\"DHXM\":\"test4\",\"ZSMC\":\"测试诊室5\",\"ZSDM\":\"002\",\"ZZJZXH\":\"3\",\"ZZJZXM\":\"041测试\"},{\"DHXH\":\"55号\",\"DHXM\":\"test4\",\"ZSMC\":\"测试诊室5\",\"ZSDM\":\"002\",\"ZZJZXH\":\"3\",\"ZZJZXM\":\"041测试\"},{\"DHXH\":\"55号\",\"DHXM\":\"test4\",\"ZSMC\":\"测试诊室5\",\"ZSDM\":\"002\",\"ZZJZXH\":\"3\",\"ZZJZXM\":\"041测试\"},{\"DHXH\":\"55号\",\"DHXM\":\"test4\",\"ZSMC\":\"测试诊室5\",\"ZSDM\":\"002\",\"ZZJZXH\":\"3\",\"ZZJZXM\":\"041测试\"},{\"DHXH\":\"55号\",\"DHXM\":\"test4\",\"ZSMC\":\"测试诊室5\",\"ZSDM\":\"002\",\"ZZJZXH\":\"3\",\"ZZJZXM\":\"041测试\"},{\"DHXH\":\"55号\",\"DHXM\":\"test4\",\"ZSMC\":\"测试诊室5\",\"ZSDM\":\"002\",\"ZZJZXH\":\"3\",\"ZZJZXM\":\"041测试\"},{\"DHXH\":\"55号\",\"DHXM\":\"test4\",\"ZSMC\":\"测试诊室5\",\"ZSDM\":\"002\",\"ZZJZXH\":\"3\",\"ZZJZXM\":\"041测试\"},{\"DHXH\":\"55号\",\"DHXM\":\"test4\",\"ZSMC\":\"测试诊室5\",\"ZSDM\":\"002\",\"ZZJZXH\":\"3\",\"ZZJZXM\":\"041测试\"},{\"DHXH\":\"55号\",\"DHXM\":\"test4\",\"ZSMC\":\"测试诊室5\",\"ZSDM\":\"002\",\"ZZJZXH\":\"3\",\"ZZJZXM\":\"041测试\"},{\"DHXH\":\"55号\",\"DHXM\":\"test4\",\"ZSMC\":\"测试诊室5\",\"ZSDM\":\"002\",\"ZZJZXH\":\"3\",\"ZZJZXM\":\"041测试\"},{\"DHXH\":\"55号\",\"DHXM\":\"test4\",\"ZSMC\":\"测试诊室5\",\"ZSDM\":\"002\",\"ZZJZXH\":\"3\",\"ZZJZXM\":\"041测试\"},{\"DHXH\":\"55号\",\"DHXM\":\"test4\",\"ZSMC\":\"测试诊室5\",\"ZSDM\":\"002\",\"ZZJZXH\":\"3\",\"ZZJZXM\":\"041测试\"},{\"DHXH\":\"55号\",\"DHXM\":\"test4\",\"ZSMC\":\"测试诊室5\",\"ZSDM\":\"002\",\"ZZJZXH\":\"3\",\"ZZJZXM\":\"041测试\"},{\"DHXH\":\"55号\",\"DHXM\":\"test4\",\"ZSMC\":\"测试诊室5\",\"ZSDM\":\"003\",\"ZZJZXH\":\"3\",\"ZZJZXM\":\"041测试\"},{\"DHXH\":\"55号\",\"DHXM\":\"test4\",\"ZSMC\":\"测试诊室5\",\"ZSDM\":\"003\",\"ZZJZXH\":\"3\",\"ZZJZXM\":\"041测试\"},{\"DHXH\":\"55号\",\"DHXM\":\"test4\",\"ZSMC\":\"测试诊室5\",\"ZSDM\":\"003\",\"ZZJZXH\":\"3\",\"ZZJZXM\":\"041测试\"},{\"DHXH\":\"55号\",\"DHXM\":\"test4\",\"ZSMC\":\"测试诊室5\",\"ZSDM\":\"003\",\"ZZJZXH\":\"3\",\"ZZJZXM\":\"041测试\"},{\"DHXH\":\"55号\",\"DHXM\":\"test4\",\"ZSMC\":\"测试诊室5\",\"ZSDM\":\"003\",\"ZZJZXH\":\"3\",\"ZZJZXM\":\"041测试\"},{\"DHXH\":\"55号\",\"DHXM\":\"test4\",\"ZSMC\":\"测试诊室5\",\"ZSDM\":\"003\",\"ZZJZXH\":\"3\",\"ZZJZXM\":\"041测试\"},{\"DHXH\":\"55号\",\"DHXM\":\"test4\",\"ZSMC\":\"测试诊室5\",\"ZSDM\":\"003\",\"ZZJZXH\":\"3\",\"ZZJZXM\":\"041测试\"},{\"DHXH\":\"55号\",\"DHXM\":\"test4\",\"ZSMC\":\"测试诊室5\",\"ZSDM\":\"003\",\"ZZJZXH\":\"3\",\"ZZJZXM\":\"041测试\"},{\"DHXH\":\"55号\",\"DHXM\":\"test4\",\"ZSMC\":\"测试诊室5\",\"ZSDM\":\"003\",\"ZZJZXH\":\"3\",\"ZZJZXM\":\"041测试\"},{\"DHXH\":\"55号\",\"DHXM\":\"test4\",\"ZSMC\":\"测试诊室5\",\"ZSDM\":\"003\",\"ZZJZXH\":\"3\",\"ZZJZXM\":\"041测试\"},{\"DHXH\":\"55号\",\"DHXM\":\"test4\",\"ZSMC\":\"测试诊室5\",\"ZSDM\":\"003\",\"ZZJZXH\":\"3\",\"ZZJZXM\":\"041测试\"},{\"DHXH\":\"55号\",\"DHXM\":\"test4\",\"ZSMC\":\"测试诊室5\",\"ZSDM\":\"003\",\"ZZJZXH\":\"3\",\"ZZJZXM\":\"041测试\"},{\"DHXH\":\"55号\",\"DHXM\":\"test4\",\"ZSMC\":\"测试诊室5\",\"ZSDM\":\"004\",\"ZZJZXH\":\"3\",\"ZZJZXM\":\"041测试\"},{\"DHXH\":\"55号\",\"DHXM\":\"test4\",\"ZSMC\":\"测试诊室5\",\"ZSDM\":\"004\",\"ZZJZXH\":\"3\",\"ZZJZXM\":\"041测试\"},{\"DHXH\":\"55号\",\"DHXM\":\"test4\",\"ZSMC\":\"测试诊室5\",\"ZSDM\":\"004\",\"ZZJZXH\":\"3\",\"ZZJZXM\":\"041测试\"},{\"DHXH\":\"55号\",\"DHXM\":\"test4\",\"ZSMC\":\"测试诊室5\",\"ZSDM\":\"004\",\"ZZJZXH\":\"3\",\"ZZJZXM\":\"041测试\"},{\"DHXH\":\"55号\",\"DHXM\":\"test4\",\"ZSMC\":\"测试诊室5\",\"ZSDM\":\"004\",\"ZZJZXH\":\"3\",\"ZZJZXM\":\"041测试\"},{\"DHXH\":\"55号\",\"DHXM\":\"test4\",\"ZSMC\":\"测试诊室5\",\"ZSDM\":\"004\",\"ZZJZXH\":\"3\",\"ZZJZXM\":\"041测试\"},{\"DHXH\":\"55号\",\"DHXM\":\"test4\",\"ZSMC\":\"测试诊室5\",\"ZSDM\":\"004\",\"ZZJZXH\":\"3\",\"ZZJZXM\":\"041测试\"},{\"DHXH\":\"55号\",\"DHXM\":\"test4\",\"ZSMC\":\"测试诊室5\",\"ZSDM\":\"004\",\"ZZJZXH\":\"3\",\"ZZJZXM\":\"041测试\"},{\"DHXH\":\"55号\",\"DHXM\":\"test4\",\"ZSMC\":\"测试诊室5\",\"ZSDM\":\"004\",\"ZZJZXH\":\"3\",\"ZZJZXM\":\"041测试\"},{\"DHXH\":\"55号\",\"DHXM\":\"test4\",\"ZSMC\":\"测试诊室5\",\"ZSDM\":\"004\",\"ZZJZXH\":\"3\",\"ZZJZXM\":\"041测试\"},{\"DHXH\":\"55号\",\"DHXM\":\"test4\",\"ZSMC\":\"测试诊室5\",\"ZSDM\":\"004\",\"ZZJZXH\":\"3\",\"ZZJZXM\":\"041测试\"},{\"DHXH\":\"55号\",\"DHXM\":\"test4\",\"ZSMC\":\"测试诊室5\",\"ZSDM\":\"004\",\"ZZJZXH\":\"3\",\"ZZJZXM\":\"041测试\"},{\"DHXH\":\"55号\",\"DHXM\":\"test4\",\"ZSMC\":\"测试诊室5\",\"ZSDM\":\"004\",\"ZZJZXH\":\"3\",\"ZZJZXM\":\"041测试\"},{\"DHXH\":\"55号\",\"DHXM\":\"test4\",\"ZSMC\":\"测试诊室5\",\"ZSDM\":\"004\",\"ZZJZXH\":\"3\",\"ZZJZXM\":\"041测试\"}]";
            //String data = "[{\"ZQMC\":\"ZQMC测试\",\"DHXH\":\"4\",\"DHXM\":\"刘德华\",\"ZSMC\":\"测试诊室1\",\"ZSDM\":\"001\",\"ZZJZXH\":\"3\",\"ZZJZXM\":\"041测试\"},{\"ZQMC\":\"ZQMC测试\",\"DHXH\":\"2\",\"DHXM\":\"郭德纲 \",\"ZSMC\":\"测试诊室1\",\"ZSDM\":\"001\",\"ZZJZXH\":\"3\",\"ZZJZXM\":\"041测试\"},{\"ZQMC\":\"ZQMC测试\",\"DHXH\":\"3\",\"DHXM\":\"郭富城\",\"ZSMC\":\"测试诊室1\",\"ZSDM\":\"001\",\"ZZJZXH\":\"3\",\"ZZJZXM\":\"041测试\"},{\"ZQMC\":\"ZQMC测试\",\"DHXH\":\"5\",\"DHXM\":\"王小明\",\"ZSMC\":\"测试诊室1\",\"ZSDM\":\"001\",\"ZZJZXH\":\"3\",\"ZZJZXM\":\"041测试\"},{\"ZQMC\":\"ZQMC测试\",\"DHXH\":\"1\",\"DHXM\":\"牛二\",\"ZSMC\":\"测试诊室1\",\"ZSDM\":\"001\",\"ZZJZXH\":\"3\",\"ZZJZXM\":\"041测试\"},{\"ZQMC\":\"ZQMC测试\",\"DHXH\":\"7\",\"DHXM\":\"牛二\",\"ZSMC\":\"测试诊室1\",\"ZSDM\":\"001\",\"ZZJZXH\":\"3\",\"ZZJZXM\":\"041测试\"},{\"ZQMC\":\"ZQMC测试\",\"DHXH\":\"6\",\"DHXM\":\"牛二\",\"ZSMC\":\"测试诊室1\",\"ZSDM\":\"001\",\"ZZJZXH\":\"3\",\"ZZJZXM\":\"041测试\"},{\"ZQMC\":\"ZQMC测试\",\"DHXH\":\"20\",\"DHXM\":\"牛二\",\"ZSMC\":\"测试诊室1\",\"ZSDM\":\"001\",\"ZZJZXH\":\"3\",\"ZZJZXM\":\"041测试\"},{\"ZQMC\":\"ZQMC测试\",\"DHXH\":\"11\",\"DHXM\":\"牛二\",\"ZSMC\":\"测试诊室1\",\"ZSDM\":\"001\",\"ZZJZXH\":\"3\",\"ZZJZXM\":\"041测试\"},{\"ZQMC\":\"ZQMC测试\",\"DHXH\":\"25\",\"DHXM\":\"牛二\",\"ZSMC\":\"测试诊室1\",\"ZSDM\":\"001\",\"ZZJZXH\":\"3\",\"ZZJZXM\":\"041测试\"},{\"ZQMC\":\"ZQMC测试\",\"DHXH\":\"4\",\"DHXM\":\"刘德华\",\"ZSMC\":\"测试诊室1\",\"ZSDM\":\"003\",\"ZZJZXH\":\"3\",\"ZZJZXM\":\"041测试\"},{\"ZQMC\":\"ZQMC测试\",\"DHXH\":\"2\",\"DHXM\":\"郭德纲 \",\"ZSMC\":\"测试诊室1\",\"ZSDM\":\"003\",\"ZZJZXH\":\"3\",\"ZZJZXM\":\"041测试\"},{\"ZQMC\":\"ZQMC测试\",\"DHXH\":\"3\",\"DHXM\":\"郭富城\",\"ZSMC\":\"测试诊室1\",\"ZSDM\":\"003\",\"ZZJZXH\":\"3\",\"ZZJZXM\":\"041测试\"},{\"ZQMC\":\"ZQMC测试\",\"DHXH\":\"5\",\"DHXM\":\"王小明\",\"ZSMC\":\"测试诊室1\",\"ZSDM\":\"003\",\"ZZJZXH\":\"3\",\"ZZJZXM\":\"041测试\"},{\"ZQMC\":\"ZQMC测试\",\"DHXH\":\"1\",\"DHXM\":\"牛二\",\"ZSMC\":\"测试诊室1\",\"ZSDM\":\"003\",\"ZZJZXH\":\"3\",\"ZZJZXM\":\"041测试\"},{\"ZQMC\":\"ZQMC测试\",\"DHXH\":\"7\",\"DHXM\":\"牛二\",\"ZSMC\":\"测试诊室1\",\"ZSDM\":\"003\",\"ZZJZXH\":\"3\",\"ZZJZXM\":\"041测试\"},{\"ZQMC\":\"ZQMC测试\",\"DHXH\":\"6\",\"DHXM\":\"牛二\",\"ZSMC\":\"测试诊室1\",\"ZSDM\":\"003\",\"ZZJZXH\":\"3\",\"ZZJZXM\":\"041测试\"},{\"ZQMC\":\"ZQMC测试\",\"DHXH\":\"20\",\"DHXM\":\"牛二\",\"ZSMC\":\"测试诊室1\",\"ZSDM\":\"003\",\"ZZJZXH\":\"3\",\"ZZJZXM\":\"041测试\"},{\"ZQMC\":\"ZQMC测试\",\"DHXH\":\"11\",\"DHXM\":\"牛二\",\"ZSMC\":\"测试诊室1\",\"ZSDM\":\"003\",\"ZZJZXH\":\"3\",\"ZZJZXM\":\"041测试\"},{\"ZQMC\":\"ZQMC测试\",\"DHXH\":\"25\",\"DHXM\":\"牛二\",\"ZSMC\":\"测试诊室1\",\"ZSDM\":\"003\",\"ZZJZXH\":\"3\",\"ZZJZXM\":\"041测试\"},{\"ZQMC\":\"ZQMC测试\",\"DHXH\":\"4\",\"DHXM\":\"刘德华\",\"ZSMC\":\"测试诊室1\",\"ZSDM\":\"004\",\"ZZJZXH\":\"3\",\"ZZJZXM\":\"041测试\"},{\"ZQMC\":\"ZQMC测试\",\"DHXH\":\"2\",\"DHXM\":\"郭德纲 \",\"ZSMC\":\"测试诊室1\",\"ZSDM\":\"004\",\"ZZJZXH\":\"3\",\"ZZJZXM\":\"041测试\"},{\"ZQMC\":\"ZQMC测试\",\"DHXH\":\"3\",\"DHXM\":\"郭富城\",\"ZSMC\":\"测试诊室1\",\"ZSDM\":\"004\",\"ZZJZXH\":\"3\",\"ZZJZXM\":\"041测试\"},{\"ZQMC\":\"ZQMC测试\",\"DHXH\":\"5\",\"DHXM\":\"王小明\",\"ZSMC\":\"测试诊室1\",\"ZSDM\":\"004\",\"ZZJZXH\":\"3\",\"ZZJZXM\":\"041测试\"},{\"ZQMC\":\"ZQMC测试\",\"DHXH\":\"1\",\"DHXM\":\"牛二\",\"ZSMC\":\"测试诊室1\",\"ZSDM\":\"004\",\"ZZJZXH\":\"3\",\"ZZJZXM\":\"041测试\"},{\"ZQMC\":\"ZQMC测试\",\"DHXH\":\"7\",\"DHXM\":\"牛二\",\"ZSMC\":\"测试诊室1\",\"ZSDM\":\"004\",\"ZZJZXH\":\"3\",\"ZZJZXM\":\"041测试\"},{\"ZQMC\":\"ZQMC测试\",\"DHXH\":\"6\",\"DHXM\":\"牛二\",\"ZSMC\":\"测试诊室1\",\"ZSDM\":\"004\",\"ZZJZXH\":\"3\",\"ZZJZXM\":\"041测试\"},{\"ZQMC\":\"ZQMC测试\",\"DHXH\":\"20\",\"DHXM\":\"牛二\",\"ZSMC\":\"测试诊室1\",\"ZSDM\":\"004\",\"ZZJZXH\":\"3\",\"ZZJZXM\":\"041测试\"},{\"ZQMC\":\"ZQMC测试\",\"DHXH\":\"11\",\"DHXM\":\"牛二\",\"ZSMC\":\"测试诊室1\",\"ZSDM\":\"004\",\"ZZJZXH\":\"3\",\"ZZJZXM\":\"041测试\"},{\"ZQMC\":\"ZQMC测试\",\"DHXH\":\"25\",\"DHXM\":\"牛二\",\"ZSMC\":\"测试诊室1\",\"ZSDM\":\"004\",\"ZZJZXH\":\"3\",\"ZZJZXM\":\"041测试\"},{\"ZQMC\":\"ZQMC测试\",\"DHXH\":\"4\",\"DHXM\":\"刘德华\",\"ZSMC\":\"测试诊室1\",\"ZSDM\":\"005\",\"ZZJZXH\":\"3\",\"ZZJZXM\":\"041测试\"},{\"ZQMC\":\"ZQMC测试\",\"DHXH\":\"2\",\"DHXM\":\"郭德纲 \",\"ZSMC\":\"测试诊室1\",\"ZSDM\":\"005\",\"ZZJZXH\":\"3\",\"ZZJZXM\":\"041测试\"},{\"ZQMC\":\"ZQMC测试\",\"DHXH\":\"3\",\"DHXM\":\"郭富城\",\"ZSMC\":\"测试诊室1\",\"ZSDM\":\"005\",\"ZZJZXH\":\"3\",\"ZZJZXM\":\"041测试\"},{\"ZQMC\":\"ZQMC测试\",\"DHXH\":\"5\",\"DHXM\":\"王小明\",\"ZSMC\":\"测试诊室1\",\"ZSDM\":\"005\",\"ZZJZXH\":\"3\",\"ZZJZXM\":\"041测试\"},{\"ZQMC\":\"ZQMC测试\",\"DHXH\":\"1\",\"DHXM\":\"牛二\",\"ZSMC\":\"测试诊室1\",\"ZSDM\":\"005\",\"ZZJZXH\":\"3\",\"ZZJZXM\":\"041测试\"},{\"ZQMC\":\"ZQMC测试\",\"DHXH\":\"7\",\"DHXM\":\"牛二\",\"ZSMC\":\"测试诊室1\",\"ZSDM\":\"005\",\"ZZJZXH\":\"3\",\"ZZJZXM\":\"041测试\"},{\"ZQMC\":\"ZQMC测试\",\"DHXH\":\"6\",\"DHXM\":\"牛二\",\"ZSMC\":\"测试诊室1\",\"ZSDM\":\"005\",\"ZZJZXH\":\"3\",\"ZZJZXM\":\"041测试\"},{\"ZQMC\":\"ZQMC测试\",\"DHXH\":\"20\",\"DHXM\":\"牛二\",\"ZSMC\":\"测试诊室1\",\"ZSDM\":\"005\",\"ZZJZXH\":\"3\",\"ZZJZXM\":\"041测试\"},{\"ZQMC\":\"ZQMC测试\",\"DHXH\":\"11\",\"DHXM\":\"牛二\",\"ZSMC\":\"测试诊室1\",\"ZSDM\":\"005\",\"ZZJZXH\":\"3\",\"ZZJZXM\":\"041测试\"},{\"ZQMC\":\"ZQMC测试\",\"DHXH\":\"25\",\"DHXM\":\"牛二\",\"ZSMC\":\"测试诊室1\",\"ZSDM\":\"005\",\"ZZJZXH\":\"3\",\"ZZJZXM\":\"041测试\"}]";
            //String data = "[{\"ZQMC\":\"ZQMC测试\",\"DHXH\":\"3号\",\"DHXM\":\"刘德华\",\"ZSMC\":\"测试诊室1\",\"ZSDM\":\"001\",\"ZZJZXH\":\"3\",\"ZZJZXM\":\"041测试\"},{\"ZQMC\":\"ZQMC测试\",\"DHXH\":\"2号\",\"DHXM\":\"郭德纲 \",\"ZSMC\":\"测试诊室1\",\"ZSDM\":\"001\",\"ZZJZXH\":\"3\",\"ZZJZXM\":\"041测试\"},{\"ZQMC\":\"ZQMC测试\",\"DHXH\":\"11号\",\"DHXM\":\"郭富城\",\"ZSMC\":\"测试诊室1\",\"ZSDM\":\"001\",\"ZZJZXH\":\"3\",\"ZZJZXM\":\"041测试\"},{\"ZQMC\":\"ZQMC测试\",\"DHXH\":\"1号\",\"DHXM\":\"王小明\",\"ZSMC\":\"测试诊室1\",\"ZSDM\":\"001\",\"ZZJZXH\":\"3\",\"ZZJZXM\":\"041测试\"}]";
            //yafangsuo
            //String data ="[{\"YSXM\":\"1号\",\"YSZC\":\"test1\",\"YSJJ\":\"测试测试诊室测试诊室测试诊室测试诊室测试诊室测试诊室诊室\",\"JZXM\":\"001\",\"ZBXM1\":\"3\",\"ZBXM2\":\"041测试\",\"ZBXM3\":\"041测试\",\"YSGH\":\"0123\"}]";
            //String data ="[{\"ZSDM\":\"1号\",\"ZSMC\":\"1号\",\"JZXH\":\"1号\",\"YSXM\":\"1号\",\"YSZC\":\"test1\",\"YSJJ\":\"测试诊室\",\"JZXM\":\"001\",\"HZ1XH\":\"3\",\"HZ2XH\":\"3\",\"HZ3XH\":\"3\",\"ZBXM1\":\"3\",\"ZBXM2\":\"041测试\",\"ZBXM3\":\"041测试\",\"YSGH\":\"0123\"}]";
            //String data ="[{\"ZSMC\":\"1号\",\"DQJZ\":\"test1\",\"YSXM\":\"测试诊室\",\"YSZC\":\"001\",\"WXTS\":\"3\"}]";
            //jinan_low_3
            //String data = "[{\"ZSBH\":\"1号\",\"YSXM\":\"1号\",\"YSZC\":\"test1\",\"YSJJ\":\"测试诊室\",\"JZXM\":\"001\",\"ZBXM1\":\"3\",\"ZBXM2\":\"041测试\",\"ZBXM3\":\"041测试\"}]";
            //xinhua_low_2
            //String data = "[{\"ZSMC\":\"诊室01\",\"ZZJZXH\":\"12号\",\"ZZJZXM\":\"张小明\",\"DHXH1\":\"DHXH1\",\"DHXM1\":\"DHXM1\",\"DHXH2\":\"DHXH2\",\"DHXM2\":\"DHXM2\",\"DHXH3\":\"DHXH3\",\"DHXM3\":\"DHXM3\",\"DHXH4\":\"DHXH4\",\"DHXM4\":\"DHXM4\"},{\"ZSMC\":\"诊室01\",\"ZZJZXH\":\"12号\",\"ZZJZXM\":\"张小明\",\"DHXH1\":\"DHXH1\",\"DHXM1\":\"DHXM1\",\"DHXH2\":\"DHXH2\",\"DHXM2\":\"DHXM2\",\"DHXH3\":\"DHXH3\",\"DHXM3\":\"DHXM3\",\"DHXH4\":\"DHXH4\",\"DHXM4\":\"DHXM4\"},{\"ZSMC\":\"诊室01\",\"ZZJZXH\":\"12号\",\"ZZJZXM\":\"张小明\",\"DHXH1\":\"DHXH1\",\"DHXM1\":\"DHXM1\",\"DHXH2\":\"DHXH2\",\"DHXM2\":\"DHXM2\",\"DHXH3\":\"DHXH3\",\"DHXM3\":\"DHXM3\",\"DHXH4\":\"DHXH4\",\"DHXM4\":\"DHXM4\"},{\"ZSMC\":\"诊室01\",\"ZZJZXH\":\"12号\",\"ZZJZXM\":\"张小明\",\"DHXH1\":\"DHXH1\",\"DHXM1\":\"DHXM1\",\"DHXH2\":\"DHXH2\",\"DHXM2\":\"DHXM2\",\"DHXH3\":\"DHXH3\",\"DHXM3\":\"DHXM3\",\"DHXH4\":\"DHXH4\",\"DHXM4\":\"DHXM4\"}]";
            //jiangqiao_low_1
            //String data = "[{\"KSMC\":\"关节外科\",\"DQJZ\":\"1号 赵*彤\",\"YSXM\":\"刘小勇\",\"YSZC\":\"副主任医师\",\"YSGH\":\"110\",\"WXTS\":\"温馨提示：请等候排队，祝您早日康复！\"}]";
            //jiangqiao_yao
            //String data = "[{\"DHXM\":\"刘1德华\",\"YFMC\":\"①药房\",\"YFDM\":\"1\"},{\"DHXM\":\"刘2德华\",\"YFMC\":\"①药房\",\"YFDM\":\"1\"},{\"DHXM\":\"刘3德华\",\"YFMC\":\"①药房\",\"YFDM\":\"1\"},{\"DHXM\":\"刘4德华\",\"YFMC\":\"①药房\",\"YFDM\":\"1\"},{\"DHXM\":\"刘5德华\",\"YFMC\":\"①药房\",\"YFDM\":\"1\"},{\"DHXM\":\"刘6德华\",\"YFMC\":\"①药房\",\"YFDM\":\"1\"},{\"DHXM\":\"刘7德华\",\"YFMC\":\"①药房\",\"YFDM\":\"1\"},{\"DHXM\":\"刘1德华\",\"YFMC\":\"②药房\",\"YFDM\":\"02\"},{\"DHXM\":\"刘2德华\",\"YFMC\":\"②药房\",\"YFDM\":\"02\"},{\"DHXM\":\"刘3德华\",\"YFMC\":\"②药房\",\"YFDM\":\"02\"},{\"DHXM\":\"刘4德华\",\"YFMC\":\"②药房\",\"YFDM\":\"02\"},{\"DHXM\":\"刘5德华\",\"YFMC\":\"②药房\",\"YFDM\":\"02\"},{\"DHXM\":\"刘6德华\",\"YFMC\":\"②药房\",\"YFDM\":\"02\"},{\"DHXM\":\"刘7德华\",\"YFMC\":\"②药房\",\"YFDM\":\"02\"},{\"DHXM\":\"刘8德华\",\"YFMC\":\"②药房\",\"YFDM\":\"02\"},{\"DHXM\":\"刘9德华\",\"YFMC\":\"②药房\",\"YFDM\":\"02\"},{\"DHXM\":\"刘1德华\",\"YFMC\":\"③药房\",\"YFDM\":\"03\"},{\"DHXM\":\"刘2德华\",\"YFMC\":\"③药房\",\"YFDM\":\"03\"},{\"DHXM\":\"刘3德华\",\"YFMC\":\"③药房\",\"YFDM\":\"03\"},{\"DHXM\":\"刘4德华\",\"YFMC\":\"③药房\",\"YFDM\":\"03\"},{\"DHXM\":\"刘5德华\",\"YFMC\":\"③药房\",\"YFDM\":\"03\"},{\"DHXM\":\"刘6德华\",\"YFMC\":\"③药房\",\"YFDM\":\"03\"},{\"DHXM\":\"刘7德华\",\"YFMC\":\"③药房\",\"YFDM\":\"03\"},{\"DHXM\":\"刘8德华\",\"YFMC\":\"③药房\",\"YFDM\":\"03\"},{\"DHXM\":\"刘9德华\",\"YFMC\":\"③药房\",\"YFDM\":\"03\"},{\"DHXM\":\"刘1德华\",\"YFMC\":\"④药房\",\"YFDM\":\"04\"},{\"DHXM\":\"刘2德华\",\"YFMC\":\"④药房\",\"YFDM\":\"04\"},{\"DHXM\":\"刘3德华\",\"YFMC\":\"④药房\",\"YFDM\":\"04\"},{\"DHXM\":\"刘4德华\",\"YFMC\":\"④药房\",\"YFDM\":\"04\"},{\"DHXM\":\"刘5德华\",\"YFMC\":\"④药房\",\"YFDM\":\"04\"},{\"DHXM\":\"刘6德华\",\"YFMC\":\"④药房\",\"YFDM\":\"04\"},{\"DHXM\":\"刘7德华\",\"YFMC\":\"④药房\",\"YFDM\":\"04\"},{\"DHXM\":\"刘8德华\",\"YFMC\":\"④药房\",\"YFDM\":\"04\"},{\"DHXM\":\"刘9德华\",\"YFMC\":\"④药房\",\"YFDM\":\"04\"},{\"DHXM\":\"刘1德华\",\"YFMC\":\"⑤药房\",\"YFDM\":\"05\"},{\"DHXM\":\"刘2德华\",\"YFMC\":\"⑤药房\",\"YFDM\":\"05\"},{\"DHXM\":\"刘3德华\",\"YFMC\":\"⑤药房\",\"YFDM\":\"05\"},{\"DHXM\":\"刘4德华\",\"YFMC\":\"⑤药房\",\"YFDM\":\"05\"},{\"DHXM\":\"刘5德华\",\"YFMC\":\"⑤药房\",\"YFDM\":\"05\"},{\"DHXM\":\"刘6德华\",\"YFMC\":\"⑤药房\",\"YFDM\":\"05\"},{\"DHXM\":\"刘7德华\",\"YFMC\":\"⑤药房\",\"YFDM\":\"05\"},{\"DHXM\":\"刘8德华\",\"YFMC\":\"⑤药房\",\"YFDM\":\"05\"},{\"DHXM\":\"刘9德华\",\"YFMC\":\"⑤药房\",\"YFDM\":\"05\"},{\"DHXM\":\"刘1德华\",\"YFMC\":\"⑥药房\",\"YFDM\":\"06\"},{\"DHXM\":\"刘2德华\",\"YFMC\":\"⑥药房\",\"YFDM\":\"06\"},{\"DHXM\":\"刘3德华\",\"YFMC\":\"⑥药房\",\"YFDM\":\"06\"},{\"DHXM\":\"刘4德华\",\"YFMC\":\"⑥药房\",\"YFDM\":\"06\"},{\"DHXM\":\"刘5德华\",\"YFMC\":\"⑥药房\",\"YFDM\":\"06\"},{\"DHXM\":\"刘6德华\",\"YFMC\":\"⑥药房\",\"YFDM\":\"06\"},{\"DHXM\":\"刘7德华\",\"YFMC\":\"⑥药房\",\"YFDM\":\"06\"},{\"DHXM\":\"刘8德华\",\"YFMC\":\"⑥药房\",\"YFDM\":\"06\"},{\"DHXM\":\"刘9德华\",\"YFMC\":\"⑥药房\",\"YFDM\":\"06\"},{\"DHXM\":\"刘1德华\",\"YFMC\":\"⑦药房\",\"YFDM\":\"07\"},{\"DHXM\":\"刘2德华\",\"YFMC\":\"⑦药房\",\"YFDM\":\"07\"},{\"DHXM\":\"刘3德华\",\"YFMC\":\"⑦药房\",\"YFDM\":\"07\"},{\"DHXM\":\"刘4德华\",\"YFMC\":\"⑦药房\",\"YFDM\":\"07\"},{\"DHXM\":\"刘5德华\",\"YFMC\":\"⑦药房\",\"YFDM\":\"07\"},{\"DHXM\":\"刘6德华\",\"YFMC\":\"⑦药房\",\"YFDM\":\"07\"},{\"DHXM\":\"刘7德华\",\"YFMC\":\"⑦药房\",\"YFDM\":\"07\"},{\"DHXM\":\"刘8德华\",\"YFMC\":\"⑦药房\",\"YFDM\":\"07\"},{\"DHXM\":\"刘9德华\",\"YFMC\":\"⑦药房\",\"YFDM\":\"07\"},{\"DHXM\":\"刘1德华\",\"YFMC\":\"⑧药房\",\"YFDM\":\"08\"},{\"DHXM\":\"刘2德华\",\"YFMC\":\"⑧药房\",\"YFDM\":\"08\"},{\"DHXM\":\"刘3德华\",\"YFMC\":\"⑧药房\",\"YFDM\":\"08\"},{\"DHXM\":\"刘4德华\",\"YFMC\":\"⑧药房\",\"YFDM\":\"08\"},{\"DHXM\":\"刘5德华\",\"YFMC\":\"⑧药房\",\"YFDM\":\"08\"},{\"DHXM\":\"刘6德华\",\"YFMC\":\"⑧药房\",\"YFDM\":\"08\"},{\"DHXM\":\"刘7德华\",\"YFMC\":\"⑧药房\",\"YFDM\":\"08\"},{\"DHXM\":\"刘8德华\",\"YFMC\":\"⑧药房\",\"YFDM\":\"08\"},{\"DHXM\":\"刘9德华\",\"YFMC\":\"⑧药房\",\"YFDM\":\"08\"}]";
            //String data = "[{\"ZQMC\":\"ZQMC测试\",\"DHXM\":\"刘德华\",\"YFMC\":\"测试诊室1\",\"YFDM\":\"002\"},{\"ZQMC\":\"ZQMC测试\",\"DHXM\":\"刘德华\",\"YFMC\":\"测试诊室1\",\"YFDM\":\"001\"},{\"ZQMC\":\"ZQMC测试\",\"DHXM\":\"刘德华\",\"YFMC\":\"测试诊室1\",\"YFDM\":\"001\"},{\"ZQMC\":\"ZQMC测试\",\"DHXM\":\"刘德华\",\"YFMC\":\"测试诊室1\",\"YFDM\":\"001\"},{\"ZQMC\":\"ZQMC测试\",\"DHXM\":\"刘德华\",\"YFMC\":\"测试诊室1\",\"YFDM\":\"001\"}]";
            //String data ="[{\"DHXM\":\"刘1德华\",\"YFMC\":\"①药房\",\"YFDM\":\"1\"},{\"DHXM\":\"刘1德华\",\"YFMC\":\"②药房\",\"YFDM\":\"2\"},{\"DHXM\":\"刘1德华\",\"YFMC\":\"③药房\",\"YFDM\":\"3\"},{\"DHXM\":\"刘1德华\",\"YFMC\":\"④药房\",\"YFDM\":\"4\"},{\"DHXM\":\"刘1德华\",\"YFMC\":\"⑤药房\",\"YFDM\":\"5\"},{\"DHXM\":\"刘1德华\",\"YFMC\":\"⑥药房\",\"YFDM\":\"6\"},{\"DHXM\":\"刘1德华\",\"YFMC\":\"⑦药房\",\"YFDM\":\"7\"},{\"DHXM\":\"刘1德华\",\"YFMC\":\"⑧药房\",\"YFDM\":\"8\"}]";
            //String data = "[{\"DHXM\":\"刘1德华\",\"YFMC\":\"①药房\",\"YFDM\":\"1\"},{\"DHXM\":\"刘1德华\",\"YFMC\":\"⑩药房\",\"YFDM\":\"10\"},{\"DHXM\":\"刘1德华\",\"YFMC\":\"③药房\",\"YFDM\":\"3\"},{\"DHXM\":\"刘1德华\",\"YFMC\":\"②药房\",\"YFDM\":\"2\"}]";
            //jiangqiao_gnk
            //String data = "[{\"ZQMC\":\"功能科\",\"DHJZXH\":\"3号\",\"DHJZXM\":\"刘德华\",\"ZSMC\":\"诊室1\",\"ZSDM\":\"001\",\"JZXH\":\"3\",\"JZXM\":\"我我我\"},{\"ZQMC\":\"功能科\",\"DHJZXH\":\"3号\",\"DHJZXM\":\"刘德华\",\"ZSMC\":\"诊室2\",\"ZSDM\":\"002\",\"JZXH\":\"3\",\"JZXM\":\"我我我\"},{\"ZQMC\":\"功能科\",\"DHJZXH\":\"3号\",\"DHJZXM\":\"刘德华\",\"ZSMC\":\"诊室3\",\"ZSDM\":\"003\",\"JZXH\":\"3\",\"JZXM\":\"我我我\"},{\"ZQMC\":\"功能科\",\"DHJZXH\":\"3号\",\"DHJZXM\":\"刘德华\",\"ZSMC\":\"诊室4\",\"ZSDM\":\"004\",\"JZXH\":\"3\",\"JZXM\":\"我我我\"},{\"ZQMC\":\"功能科\",\"DHJZXH\":\"3号\",\"DHJZXM\":\"刘德华\",\"ZSMC\":\"诊室5\",\"ZSDM\":\"005\",\"JZXH\":\"3\",\"JZXM\":\"我我我\"},{\"ZQMC\":\"功能科\",\"DHJZXH\":\"3号\",\"DHJZXM\":\"刘德华\",\"ZSMC\":\"诊室8\",\"ZSDM\":\"008\",\"JZXH\":\"3\",\"JZXM\":\"我我我\"},{\"ZQMC\":\"功能科\",\"DHJZXH\":\"3号\",\"DHJZXM\":\"刘德华\",\"ZSMC\":\"诊室9\",\"ZSDM\":\"009\",\"JZXH\":\"3\",\"JZXM\":\"我我我\"}]";
            //jiangqiao——cry
            //String data = "[{\"ZQMC\":\"出入院办理\",\"WXTS\":\"提示\",\"HJXX1\":null,\"HJXX2\":null,\"HJXX3\":null,\"HJXX4\":null,\"HJXX5\":null,\"HJXX6\":null,\"HJXX7\":null}]";
            //String data = "[{\"ZQMC\":\"出入院办理\",\"WXTS\":\"温馨提示：请按顺序排队办理，祝您早日康复！\",\"HJXX1\":\"请6号报*测到2诊室办理\",\"HJXX2\":\"null\",\"HJXX3\":\"null\",\"HJXX4\":\"null\",\"HJXX5\":\"null\",\"HJXX6\":\"null\",\"HJXX7\":\"null\"}]";
            //String data = "[{\"ZQMC\":\"眼科\",\"CONTENT\":\"请6号报*测到2诊室办理\",\"WXTS\":\"温馨提示：请在候诊区有序候诊，谢绝医药代表进入诊区，谢谢！\"},{\"ZQMC\":\"眼科\",\"CONTENT\":\"请62号报*测到2诊室办理\",\"WXTS\":\"温馨提示：请在候诊区有序候诊，谢绝医药代表进入诊区，谢谢！\"},{\"ZQMC\":\"眼科\",\"CONTENT\":\"请63号报*测到2诊室办理\",\"WXTS\":\"温馨提示：请在候诊区有序候诊，谢绝医药代表进入诊区，谢谢！\"},{\"ZQMC\":\"眼科\",\"CONTENT\":\"请64号报*测到2诊室办理\",\"WXTS\":\"温馨提示：请在候诊区有序候诊，谢绝医药代表进入诊区，谢谢！\"},{\"ZQMC\":\"眼科\",\"CONTENT\":\"请65号报*测到2诊室办理\",\"WXTS\":\"温馨提示：请在候诊区有序候诊，谢绝医药代表进入诊区，谢谢！\"},{\"ZQMC\":\"眼科\",\"CONTENT\":\"请66号报*测到2诊室办理\",\"WXTS\":\"温馨提示：请在候诊区有序候诊，谢绝医药代表进入诊区，谢谢！\"},{\"ZQMC\":\"眼科\",\"CONTENT\":\"请67号报*测到2诊室办理\",\"WXTS\":\"温馨提示：请在候诊区有序候诊，谢绝医药代表进入诊区，谢谢！\"},{\"ZQMC\":\"眼科\",\"CONTENT\":\"请66号报*测到2诊室办理\",\"WXTS\":\"温馨提示：请在候诊区有序候诊，谢绝医药代表进入诊区，谢谢！\"},{\"ZQMC\":\"眼科\",\"CONTENT\":\"请67号报*测到2诊室办理\",\"WXTS\":\"温馨提示：请在候诊区有序候诊，谢绝医药代表进入诊区，谢谢！\"}]";
            //宝中心_high
            //String data = "[{\"ZQMC\":\"宝中心\",\"DHXM\":\"刘德华\",\"ZSMC\":\"诊室1\",\"JZXH\":\"1号\",\"JZXM\":\"测试是\"},{\"ZQMC\":\"宝中心\",\"DHXM\":\"刘德华\",\"ZSMC\":\"诊室2\",\"JZXH\":\"2号\",\"JZXM\":\"测试是\"},{\"ZQMC\":\"宝中心\",\"DHXM\":\"刘德华\",\"ZSMC\":\"诊室3\",\"JZXH\":\"3号\",\"JZXM\":\"测试是\"},{\"ZQMC\":\"宝中心\",\"DHXM\":\"刘德华\",\"ZSMC\":\"诊室4\",\"JZXH\":\"4号\",\"JZXM\":\"测试是\"},{\"ZQMC\":\"宝中心\",\"DHXM\":\"刘德华\",\"ZSMC\":\"诊室5\",\"JZXH\":\"5号\",\"JZXM\":\"测试是\"},{\"ZQMC\":\"宝中心\",\"DHXM\":\"刘德华\",\"ZSMC\":\"诊室6\",\"JZXH\":\"6号\",\"JZXM\":\"测试是\"},{\"ZQMC\":\"宝中心\",\"DHXM\":\"刘德华\",\"ZSMC\":\"诊室7\",\"JZXH\":\"7号\",\"JZXM\":\"测试是\"},{\"ZQMC\":\"宝中心\",\"DHXM\":\"刘德华\",\"ZSMC\":\"诊室8\",\"JZXH\":\"8号\",\"JZXM\":\"测试是\"},{\"ZQMC\":\"宝中心\",\"DHXM\":\"刘德华\",\"ZSMC\":\"诊室9\",\"JZXH\":\"9号\",\"JZXM\":\"测试是\"}]";
            //String data = "[{\"ZQMC\":\"出入院\",\"HJXX1\":\"两句话包括GV酷狗1\",\"HJXX2\":\"两句话包括GV酷狗2\",\"HJXX3\":\"两句话包括GV酷狗3\",\"HJXX4\":\"两句话包括GV酷狗4\",\"HJXX5\":\"两句话包括GV酷狗5\",\"HJXX6\":\"两句话包括GV酷狗6\",\"HJXX7\":\"两句话包括GV酷狗7\",\"WXTS\":\"请排队！！！！！！！！！！！！！！\"}]";
            //Log.e("请求到的数据", "getdata: " + data);
            //Log.e("------------------", "getdata: "+data );
            //新华放射
            //String data  = "[{\"ZQMC\":\"放射科\",\"ZSMC\":\"MR\",\"ZZJZXH\":\"1\",\"ZZJZXM\":\"陈*悦\",\"DHXH\":\"\",\"DHXM\":\"\",\"WXTS\":\"请您在等候区等候叫号\"},{\"ZQMC\":\"放射科\",\"ZSMC\":\"CT\",\"ZZJZXH\":\"1\",\"ZZJZXM\":\"陈*悦\",\"DHXH\":\"5,3,\",\"DHXM\":\"王*,测*5,\",\"WXTS\":\"请您在等候区等候叫号\"},{\"ZQMC\":\"放射科\",\"ZSMC\":\"DR1\",\"ZZJZXH\":\"1\",\"ZZJZXM\":\"程*涵\",\"DHXH\":\"\",\"DHXM\":\"\",\"WXTS\":\"请您在等候区等候叫号\"}]";
            //郑州儿中心
            //String data = "[{\"ZQMC\":\"测试诊区\",\"ZSMC\":\"测试01\",\"YSXM\":\"测试医生\",\"GHHZ\":\"测试患者1\",\"WXTS\":\"请排队！！！！！！！！！！！！！！\"},{\"ZQMC\":\"测试诊区\",\"ZSMC\":\"测试诊室2\",\"YSXM\":\"测试医生2\",\"GHHZ\":\"测试患者2\",\"WXTS\":\"请排队！！！！！！！！！！！！！！\"},{\"ZQMC\":\"测试诊区\",\"ZSMC\":\"测试诊室3\",\"YSXM\":\"测试医生3\",\"GHHZ\":\"测试患者3\",\"WXTS\":\"请排队！！！！！！！！！！！！！！\"},{\"ZQMC\":\"测试诊区\",\"ZSMC\":\"测试诊室4\",\"YSXM\":\"测试医生4\",\"GHHZ\":\"测试患者4\",\"WXTS\":\"请排队！！！！！！！！！！！！！！\"},{\"ZQMC\":\"测试诊区\",\"ZSMC\":\"测试诊室5\",\"YSXM\":\"测试医生5\",\"GHHZ\":\"测试患者5\",\"WXTS\":\"请排队！！！！！！！！！！！！！！\"},{\"ZQMC\":\"测试诊区\",\"ZSMC\":\"测试诊室6\",\"YSXM\":\"测试医生61\",\"GHHZ\":\"测试患者61\",\"WXTS\":\"请排队！！！！！！！！！！！！！！\"},{\"ZQMC\":\"测试诊区\",\"ZSMC\":\"测试诊室71\",\"YSXM\":\"测试医生71\",\"GHHZ\":\"测试患者71\",\"WXTS\":\"请排队！！！！！！！！！！！！！！\"},{\"ZQMC\":\"测试诊区\",\"ZSMC\":\"测试诊室81\",\"YSXM\":\"测试医生81\",\"GHHZ\":\"测试患者81\",\"WXTS\":\"请排队！！！！！！！！！！！！！！\"}]";
            //静安北站小屏
            //String data = "[{\"ZSBH\":\"出入院办理\",\"WXTS\":\"温馨提示：请按顺序排队办理，祝您早日康复！\",\"YSXM\":\"请理\",\"YSZC\":\"请6号办理\",\"YSJJ\":\"上海璞石医疗科技有限公司专注于：智慧门诊，智慧病房，智慧医技，移动医院，5G远程医疗医疗系统等智慧医院必备系统。它基于医院内部计算机网络，与医院HIS、PACS、LIS系统建立数据接口，采用全嵌入式一体化解决方案，也可通过本公司软件单独使用，通过消除患者拥挤混乱的就诊现象，建立良好的医院、医生及患者之间的良好沟通、就诊及办公环境，大幅提高智慧医院形象，全面促进医院智能化、信息化、人性化的服务提高整体形象。公司通过ISO9000认证，3A认证及20多项行业专业软件注册证，双软认证，诊室显示屏设计专利，配套硬件3c认证，全国多个省份设立办事处和售后服务团队，与全国近百家医院建立很好的合作关系。\",\"JZXM\":\"撒酒黄精\",\"HZXM1\":\"asd\",\"HZXM2\":\"kujn\",\"YSGH\":\"001\"}]";
            //莘县大屏
            //String data ="[{\"ZSMC\":\"测试诊室\",\"YSXM\":\"测试医生\",\"ZZJZ\":\"测试就诊\",\"DHJZ\":\"测试等候\",\"PDRS\":\"测试排队\",\"WXTS\":\"测试提示\"},{\"ZSMC\":\"测试诊室\",\"YSXM\":\"测试医生\",\"ZZJZ\":\"测试就诊\",\"DHJZ\":\"测试等候\",\"PDRS\":\"测试排队\",\"WXTS\":\"测试提示\"},{\"ZSMC\":\"测试诊室\",\"YSXM\":\"测试医生\",\"ZZJZ\":\"测试就诊\",\"DHJZ\":\"测试等候\",\"PDRS\":\"测试排队\",\"WXTS\":\"测试提示\"},{\"ZSMC\":\"测试诊室\",\"YSXM\":\"测试医生\",\"ZZJZ\":\"测试就诊\",\"DHJZ\":\"测试等候\",\"PDRS\":\"测试排队\",\"WXTS\":\"测试提示\"},{\"ZSMC\":\"测试诊室\",\"YSXM\":\"测试医生\",\"ZZJZ\":\"测试就诊\",\"DHJZ\":\"测试等候\",\"PDRS\":\"测试排队\",\"WXTS\":\"测试提示\"},{\"ZSMC\":\"测试诊室\",\"YSXM\":\"测试医生\",\"ZZJZ\":\"测试就诊\",\"DHJZ\":\"测试等候\",\"PDRS\":\"测试排队\",\"WXTS\":\"测试提示\"},{\"ZSMC\":\"测试诊室\",\"YSXM\":\"测试医生\",\"ZZJZ\":\"测试就诊\",\"DHJZ\":\"测试等候\",\"PDRS\":\"测试排队\",\"WXTS\":\"测试提示\"},{\"ZSMC\":\"测试诊室\",\"YSXM\":\"测试医生\",\"ZZJZ\":\"测试就诊\",\"DHJZ\":\"测试等候\",\"PDRS\":\"测试排队\",\"WXTS\":\"测试提示\"},{\"ZSMC\":\"测试诊室\",\"YSXM\":\"测试医生\",\"ZZJZ\":\"测试就诊\",\"DHJZ\":\"测试等候\",\"PDRS\":\"测试排队\",\"WXTS\":\"测试提示\"},{\"ZSMC\":\"测试诊室\",\"YSXM\":\"测试医生\",\"ZZJZ\":\"测试就诊\",\"DHJZ\":\"测试等候\",\"PDRS\":\"测试排队\",\"WXTS\":\"测试提示\"}]";
            //莘县小屏
            //String data = "[{\"zsmc\":\"测试诊室\",\"ysxm\":\"刘大拿\",\"yszc\":\"副主任医师\",\"ysgh\":\"001\",\"dqjj\":\"01号 测试就诊\",\"dhjj\":\"02号 测试等候\",\"ysjj\":\"上海璞石医疗科技有限公司专注于：智慧门诊，智慧病房，智慧医技，移动医院，5G远程医疗医疗系统等智慧医院必备系统。\"}]";
            //江桥门诊信息发布
            //String data = "[{\"ZQMC\":\"内科\",\"CONTENT\":\"请 消化内科门诊 11号 消*2到 诊室09 就诊\",\"WXTS\":\"温馨提示：请在候诊区有序候诊，谢绝医药代表进入诊区，谢谢！\"},{\"ZQMC\":\"内科\",\"CONTENT\":\"请 消化内科门诊 13号 消*4到 诊室03 就诊\",\"WXTS\":\"温馨提示：请在候诊区有序候诊，谢绝医药代表进入诊区，谢谢！\"},{\"ZQMC\":\"内科\",\"CONTENT\":\"请 消化内科门诊 17号 1*0到 诊室03 就诊\",\"WXTS\":\"温馨提示：请在候诊区有序候诊，谢绝医药代表进入诊区，谢谢！\"}]";
            //红房子
            //String data = "￥0$1-2$1-2诊室$7241$刘凯玲$主治医师$宫腔疾病、STD、生殖道癌前病变、隐匿性宫颈癌等诊治；具有宫腔镜四级手术资质，擅长宫腔镜下治疗粘膜下肌宫腔疾病、STD、生殖道癌前病变、隐匿性宫颈癌等诊治；具有宫腔镜四级手术资质，擅长宫腔镜下治疗粘膜下肌宫腔疾病、STD、生殖道癌前病变、隐匿性宫颈癌等诊治；具有宫腔镜四级手术资质，擅长宫腔镜下治疗粘膜下肌宫腔疾病、STD、生殖道癌前病变、隐匿性宫颈癌等诊治；具有宫腔镜四级手术资质，擅长宫腔镜下治疗粘膜下肌宫腔疾病、STD、生殖道癌前病变、隐匿性宫颈癌等诊治；具有宫腔镜四级手术资质，擅长宫腔镜下治疗粘膜下肌宫腔疾病、STD、生殖道癌前病变、隐匿性宫颈癌等诊治；具有宫腔镜四级手术资质，擅长宫腔镜下治疗粘膜下肌宫腔疾病、STD、生殖道癌前病变、隐匿性宫颈癌等诊治；具有宫腔镜四级手术资质，擅长宫腔镜下治疗粘膜下肌宫腔疾病、STD、生殖道癌前病变、隐匿性宫颈癌等诊治；具有宫腔镜四级手术资质，擅长宫腔镜下治疗粘膜下肌宫腔疾病、STD、生殖道癌前病变、隐匿性宫颈癌等诊治；具有宫腔镜四级手术资质，擅长宫腔镜下治疗粘膜下肌宫腔疾病、STD、生殖道癌前病变、隐匿性宫颈癌等诊治；具有宫腔镜四级手术资质，擅长宫腔镜下治疗粘膜下肌瘤$正在就诊:病人$郭维(jzxm1)$测试(jzxm2)$李晴晴(回的诊1)$李大晴(回诊2)$$1$X";
//￥0$11$11-1诊室$7618$左蔚雯$主治医师$$正在就诊: 谢香英$王娇娇$$$$$0$X
//￥0$11$11诊室$7249$武贝贝$主治医师$$正在就诊: 邓亭$关雪柯$$$$$1$X
//String data = "￥0$6$6诊室$1369$王彩燕$副主任医师$熟悉妇产科常见病的诊治，特别是计划生育疾病处理和避孕方式选择，擅长计划生育手术。$正在就诊: 吉黎$顾玉英$吴小明$王慧慧(回诊)$$$0$X";

            //根据DHXH对josn进行排序
          /*  List<Map<String, Object>> user_keywords_list;
            try {
                user_keywords_list = JackSonUtil.json2listMap(data);
                Collections.sort(user_keywords_list, new Comparator<Map<String, Object>>() {
                    @Override
                    public int compare(Map<String, Object> o1, Map<String, Object> o2) {
                        //用正则表达式取出字符串中的数字
                        String regEx = "[^0-9]";
                        Pattern p = Pattern.compile(regEx);
                        //Matcher m = p.matcher(o1.get("DHXH").toString()).replaceAll("").trim());
                        Integer count1 = Integer.valueOf(p.matcher(o1.get("DHJZXH").toString()).replaceAll("").trim());//count1是从你list里面拿出来的第一个 age
                        Integer count2 = Integer.valueOf(p.matcher(o2.get("DHJZXH").toString()).replaceAll("").trim()); //count2是从你list里面拿出来的第二个age
                        return count1.compareTo(count2);//降序，反之升序
                    }
                });
                data = JackSonUtil.obj2json(user_keywords_list);
            } catch (Exception e) {
                e.printStackTrace();
            }*/



/*

            //解析数据
            if (!data.equals("anyType{}")) {
                if (tip.isEmpty()) {
                    data_map = jsonTo.jsonToMap(data.split("\\[")[1].split("]")[0]);
                } else {
                    data_map = jsonTo.jsonToMap(data);
                }
            } else {
                data_map = null;
            }

*/





            /**
             * 特殊情况
             * 小屏tip（init）不为空时
             * */
           /* if (!data.equals("anyType{}")) {
                data_map = jsonTo.jsonToMap(data.split("\\[")[1].split("]")[0]);
            } else {
                data_map = null;
            }*/

            //Log.e("测试数据", "数据: " + data_map + "----数组长度：" + data_map.size());

            return data;
        } catch (Exception e) {
            return null;
        }

    }

    /**
     * 判断手机是否拥有Root权限。
     *
     * @return 有root权限返回true，否则返回false。
     */
    public static boolean isRoot() {
        boolean bool = false;
        try {
            bool = new File("/system/bin/su").exists() || new File("/system/xbin/su").exists();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return bool;
    }

    /**
     * 应用程序运行命令获取 Root权限，设备必须已破解(获得ROOT权限)
     *
     * @param command 命令：String apkRoot="chmod 777 "+getPackageCodePath(); RootCommand(apkRoot);
     * @return 应用程序是/否获取Root权限
     */
    public boolean RootCommand(String command) {
        Process process = null;
        DataOutputStream os = null;
        DataInputStream is = null;
        try {
            process = Runtime.getRuntime().exec("su");
            os = new DataOutputStream(process.getOutputStream());
            os.writeBytes(command + "\n");
            os.writeBytes("exit\n");
            os.flush();
            int aa = process.waitFor();
            Log.w("a", "waitFor():" + aa);
            is = new DataInputStream(process.getInputStream());
            byte[] buffer = new byte[is.available()];
            Log.d("w", "大小" + buffer.length);
            is.read(buffer);
            String out = new String(buffer);
            Log.e("eee", "返回:" + out);
        } catch (Exception e) {
            e.printStackTrace();
            Log.e("tag", "205:\n" + e);
            return false;
        } finally {
            try {
                if (os != null) {
                    os.close();
                }
                if (is != null) {
                    is.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
                Log.e("tag", "217:\n" + e);
            }
            process.destroy();
        }
        Log.d("tag", "222 SUCCESS");
        return true;
    }

}
