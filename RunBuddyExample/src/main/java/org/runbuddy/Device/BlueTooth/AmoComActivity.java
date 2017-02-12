package org.runbuddy.device.BlueTooth;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.ScrollView;
import android.widget.TextView;

import org.runbuddy.tomahawk.R;

import java.text.SimpleDateFormat;
import java.util.Date;


public class AmoComActivity extends Activity implements View.OnClickListener {
    private final static String TAG = "DeviceScanActivity";// DeviceScanActivity.class.getSimpleName();
    static TextView Text_Recv;
    static String Str_Recv;
    static String ReciveStr;
    static ScrollView scrollView;
    static Handler Heart_Handler = new Handler();
    static boolean ifDisplayInHexStringOnOff = true;
    static boolean ifDisplayTimeOnOff = true;
    static int Totol_recv_bytes = 0;
    static int Totol_recv_bytes_temp = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ble_layout);

        Text_Recv = (TextView) findViewById(R.id.device_address);//接收到的字符串
        Text_Recv.setGravity(Gravity.CLIP_VERTICAL | Gravity.CLIP_HORIZONTAL);
        ReciveStr = "";
        Text_Recv.setMovementMethod(ScrollingMovementMethod.getInstance());
        scrollView = (ScrollView) findViewById(R.id.scroll);

        ifDisplayInHexStringOnOff = true;
        ifDisplayTimeOnOff = true;

    }

    //负责读出心率数据
    public static synchronized void char6_display(String str, byte[] data,
                                                  String uuid) {
        Log.i(TAG, "char6_display str = " + str);

        if (uuid.equals(DeviceScanActivity.UUID_HERATRATE)) {
            SimpleDateFormat formatter = new SimpleDateFormat("HH:mm:ss ");
            Date curDate = new Date(System.currentTimeMillis());// 获取当前时间
            String TimeStr = formatter.format(curDate);
            String DisplayStr = "[" + TimeStr + "] " + "HeartRate : " + data[0]
                    + "=" + data[1];
            Str_Recv = DisplayStr + "\r\n";
        } else {
            // 默认显示 hex
            byte[] midbytes = str.getBytes();
            String HexStr = Utils.bytesToHexString(midbytes);
            Str_Recv = HexStr;
        }

        Totol_recv_bytes += str.length();
        Totol_recv_bytes_temp += str.length();

        Heart_Handler.post(new Runnable() {
            @Override
            public synchronized void run() {
                scrollView.fullScroll(ScrollView.FOCUS_DOWN);// 滚动到底
                Text_Recv.append(Str_Recv);
                // 数据太多时清空数据
                if (Totol_recv_bytes_temp > 10000) {
                    Totol_recv_bytes_temp = 0;
                    Text_Recv.setText("");
                }

            }
        });
    }

    public synchronized static String GetLastData() {
        String string = Str_Recv;
        return string;
    }

    @Override
    public void onClick(View view) {

    }


}