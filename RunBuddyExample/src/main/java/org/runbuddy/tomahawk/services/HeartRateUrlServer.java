package org.runbuddy.tomahawk.services;

import android.os.Handler;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.runbuddy.tomahawk.entity.LittleHeartRateUnit;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * Created by Jonney Chou on 2017/1/29.
 * <p>
 * todo
 * 目前手机与服务器的连接有问题，虽然功能实现了，但是鲁棒性很差
 * 2017.02.02
 */

public class HeartRateUrlServer {
    //remote
    //public static final String SERVER_ADDRESS = "http://www.voyager2511.top:8073/RunBuddy_ops";
    //local
    public static final String SERVER_ADDRESS = "http://192.168.0.106:8080/RunBuddy_ops/";
    public static final String EXECUTED_SUCCESS = "8888";
    private Handler mHandler;
    private final static String TAG = "HeartRateUrlServer";
    public HeartRateUrlServer() {
    }

    public HeartRateUrlServer(Handler handler) {
        mHandler = handler;
    }

    public void fastUpLoad(String highestRate, String lowestRate, String averageRate,
                           int motionState, int recommendState, int execiseTime, int execiseLoad, String recordDate) {
        JSONObject paramJson = new JSONObject();
        try {
            URL url = new URL(SERVER_ADDRESS + "/hearts");
            paramJson.put("highestRate", highestRate);
            paramJson.put("lowestRate", lowestRate);
            paramJson.put("averageRate", averageRate);
            paramJson.put("motionState", motionState);
            paramJson.put("recommendState", recommendState);
            paramJson.put("execiseTime", execiseTime);
            paramJson.put("execiseLoad", execiseLoad);
            paramJson.put("recordDate", recordDate);

            //// TODO: 2017/2/12  
            sendRequest(url, null, paramJson.toString().getBytes());
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void fastUploadRealRate(List<LittleHeartRateUnit> realRate, String uploadTime)  {
        JSONObject paramJson = new JSONObject();
        Log.i(TAG,"-->>显示List："+realRate.toString());
        try{
            if(realRate != null && realRate.size() > 0){
                JSONArray jsonArray = new JSONArray();//要传过去的jsonArr
                for(LittleHeartRateUnit unit : realRate){
                    JSONObject json = new JSONObject();
                    Log.i(TAG,"-->>id:"+unit.getId());
                    Log.i(TAG,"-->>value:"+unit.getValue());
                    Log.i(TAG,"-->>status:"+unit.getStatus());
                    json.put("id",unit.getId());
                    json.put("value",unit.getValue());
                    json.put("status",unit.getStatus());
                    jsonArray.put(json);
                }
                paramJson.put("realTimeRate",jsonArray);//加入json数组
                paramJson.put("uploadTime", uploadTime);
            }

            URL url = new URL(SERVER_ADDRESS + "/heartrate");
            sendRequest(url, null, paramJson.toString().getBytes());//上传

        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }


    private void closeStream(OutputStream stream) {
        if (stream != null) {
            try {
                stream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void closeConnect(HttpURLConnection connect) {
        if (connect != null) {
            connect.disconnect();
        }
    }


    /**
     * 向服务端发送字符请求消息
     *
     * @param url         服务端地址
     * @param headerParam HTTP头参数
     * @param requestData 请求参数
     */
    private void sendRequest(URL url, Map<String, Object> headerParam, byte[] requestData) {
        HttpURLConnection httpURLConnection = null;
        OutputStream outputStream = null;
        try {
            httpURLConnection = (HttpURLConnection) url.openConnection();
            httpURLConnection.setConnectTimeout(3000);
            httpURLConnection.setDoOutput(true);
            httpURLConnection.setDoInput(true);
            httpURLConnection.setRequestMethod("POST");
            httpURLConnection.setUseCaches(false);
            httpURLConnection.setRequestProperty("Content-Type", "application/json");
//	        httpURLConnection.connect();
            // 设置HTTP头信息
            if (headerParam != null && headerParam.size() > 0) {
                Iterator<Map.Entry<String, Object>> it = headerParam.entrySet().iterator();
                while (it.hasNext()) {
                    Map.Entry<String, Object> entry = it.next();
                    httpURLConnection.setRequestProperty(
                            entry.getKey(), entry.getValue().toString());
                }
            }
            outputStream = httpURLConnection.getOutputStream();
            outputStream.write(requestData);

            int response = httpURLConnection.getResponseCode();
            if (response == HttpURLConnection.HTTP_OK) {
                InputStream inptStream = httpURLConnection.getInputStream();
                BufferedReader reader = new BufferedReader(new InputStreamReader(inptStream));
                StringBuffer builder = new StringBuffer();
                for (String s = reader.readLine(); s != null; s = reader.readLine()) {
                    builder.append(s);
                }
                String respStr = builder.toString();
                mHandler.obtainMessage(0, respStr).sendToTarget();//错误应该出在这里,这里有什么错误？？
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            closeStream(outputStream);
            closeConnect(httpURLConnection);
        }
    }


}
