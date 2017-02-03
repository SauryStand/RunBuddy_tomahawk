package org.runbuddy.tomahawk.services;

import android.os.Handler;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;

import java.net.URL;
import java.util.Iterator;
import java.util.Map;

/**
 * Created by Jonney Chou on 2017/1/29.
 *
 *  todo
 *  目前手机与服务器的连接有问题
 *  其次是论文压根没开始写，不知道怎么写，特别是开头
 *  明天返鹏城，时间只剩下2个月，时间安排，专注力，都大大降低
 *  2017.02.02
 *
 *
 */

public class URLServer {
    //remote
    public static final String SERVER_ADDRESS = "http://www.voyager2511.top:8073/RunBuddy_ops";
    //local
    //public static final String SERVER_ADDRESS = "http://localhost:8080/RunBuddy_ops/";

    private Handler mHandler;

    public URLServer() {

    }

    public URLServer(Handler handler) {
        mHandler = handler;
    }

    public void fastUpLoad(String ratebyte) {

        String test = "testing_heartRate";//测试数据

        JSONObject paramJson = new JSONObject();
        try {
            //URL url = new URL(SERVER_ADDRESS + "/heartrate/upload");
            URL url = new URL(SERVER_ADDRESS );//这样不行
            paramJson.put("ratebyte", test);
            sendRequest(url, null, paramJson.toString().getBytes());
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (IOException e) {
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
            httpURLConnection.setRequestProperty("Content-Type", "application/json ");
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
                mHandler.obtainMessage(0, respStr).sendToTarget();
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            closeStream(outputStream);
            closeConnect(httpURLConnection);
        }
    }


}
