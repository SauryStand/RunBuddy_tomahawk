package org.runbuddy.tomahawk.ui.fragments.starpage;

import android.os.Handler;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Jonney Chou on 2016/10/21.
 */

public class URLServer {

    private Handler mHandler;

    public URLServer(Handler handler) {
        mHandler = handler;
    }


    public void  getImage() {
        JSONObject paramJson = new JSONObject();
        try {
            //paramJson.put("publishedAt", publishedAt);
            URL url = new URL("http://gank.io/api/data/%E7%A6%8F%E5%88%A9/15/2");
            sendRequest(url, paramJson.toString().getBytes());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 构造并获取令牌数据集
     *
     * @param token 服务端地址
     * @return 令牌数据集
     */
    private Map<String, Object> createTokenMap(String token) {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("token", token);
        return map;
    }


    /**
     * 向服务端发送字符请求消息
     *
     * @param url         服务端地址
     * @param requestData 请求参数
     */
    private void sendRequest(URL url, byte[] requestData) {
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
//            if(headerParam != null && headerParam.size() > 0) {
//                Iterator<Map.Entry<String, Object>> it = headerParam.entrySet().iterator();
//                while (it.hasNext()) {
//                    Map.Entry<String, Object> entry = it.next();
//                    httpURLConnection.setRequestProperty(
//                            entry.getKey(), entry.getValue().toString());
//                }
//            }
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


}
