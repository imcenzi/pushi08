package com.pushi.pushi08.until;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;

public class soap {
    // Webservice服务器地址
    public static String SERVER_URL;
    // 调用的webservice命令空间
    private static final String PACE = "http://tempuri.org/";
    // 获取webservice的方法名
    public static String W_NAME;

    /**
     * WebService请求数据
     * @param ip 本机的IP地址
     * @param tip 请求数据的标识
     * @return
     * @throws IOException
     * @throws XmlPullParserException
     */
    public static String  getCityWeather(String ip,String tip) throws IOException, XmlPullParserException {
        SoapObject soapObject = new SoapObject(PACE, W_NAME);
        soapObject.addProperty("ip",ip);
        soapObject.addProperty("init",tip);
        final SoapSerializationEnvelope serializa = new SoapSerializationEnvelope(
                SoapEnvelope.VER11);
        serializa.bodyOut =soapObject;
        serializa.dotNet =true;
        HttpTransportSE httpSe = new HttpTransportSE(SERVER_URL);
        httpSe.debug =true;
        httpSe.call(PACE+W_NAME,serializa);
        SoapObject result = (SoapObject) serializa.bodyIn;
        return result.getProperty(0).toString();
    }

//    public static String mains() throws IOException, XmlPullParserException {
//        String paramString;
//
//        SoapObject localSoapObject = new SoapObject("http://tempuri.org/", "getdata");
//        localSoapObject.addProperty("ip", "192.168.1.1");
//        localSoapObject.addProperty("init", "paramString");
//        SoapSerializationEnvelope paramStrin = new SoapSerializationEnvelope(100);
//        paramStrin.bodyOut = localSoapObject;
//        paramStrin.dotNet = true;
//        paramStrin.setOutputSoapObject(localSoapObject);
//        new HttpTransportSE("http://192.168.2.102:8088/Service1.asmx").call("http://192.168.2.102:8088/Service1.asmxgetdata", paramStrin);
//        paramString = ((SoapObject)paramStrin.bodyIn).getProperty(0).toString();
//        Log.d(paramString, "main: paramString");
//        return paramString;
//    }


}
