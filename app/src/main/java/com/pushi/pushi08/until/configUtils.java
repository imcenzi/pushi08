package com.pushi.pushi08.until;

import java.io.*;
import java.util.HashMap;
import java.util.Map;

public class configUtils {
    /**
     * 保存用户名密码到本地文件中 位置放在app应用包下
     * @param service_url
     * @param resource_url
     * @return
     */
    public static boolean saveConfig(String service_url,String resource_url){
        try {
            String info =  service_url +"##"+ resource_url;
            File file = new File("/data/data/com.example.login/info.txt");
            FileOutputStream outputStream = new FileOutputStream(file);
            outputStream.write(info.getBytes());
            outputStream.close();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 从app应用包路径下读取文件中信息
     * @return
     */
    public static Map<String,String> readConfig(){
        try {
            Map map = new HashMap<String,String>();
            File file = new File("/data/data/com.example.login/info.txt");
            FileInputStream inputStream = new FileInputStream(file);
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
            String info = bufferedReader.readLine();
            String[] userinfos = info.split("##");
            map.put("service_url", userinfos[0]);
            map.put("resource_url", userinfos[1]);
            return map;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

}
