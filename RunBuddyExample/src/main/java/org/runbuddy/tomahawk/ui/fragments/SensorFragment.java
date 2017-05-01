package org.runbuddy.tomahawk.ui.fragments;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.github.glomadrian.dashedcircularprogress.DashedCircularProgress;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.github.mikephil.charting.utils.ColorTemplate;

import org.json.JSONException;
import org.json.JSONObject;
import org.runbuddy.Device.BlueTooth.DeviceScanActivity;
import org.runbuddy.Device.CounterSensor.SensorHub;
import org.runbuddy.libtomahawk.collection.HeartRate;
import org.runbuddy.tomahawk.R;
import org.runbuddy.tomahawk.entity.LittleHeartRateUnit;
import org.runbuddy.tomahawk.services.HeartRateUrlServer;
import org.runbuddy.tomahawk.ui.IntricateCharts.listviewItems.BarChartItem;
import org.runbuddy.tomahawk.ui.IntricateCharts.listviewItems.ChartItem;
import org.runbuddy.tomahawk.ui.IntricateCharts.listviewItems.LineChartItem;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;

/**
 * 传感器关键fragment
 * Created by Johnny Chow on 2016/7/27.
 */
public class SensorFragment extends Fragment implements View.OnClickListener, SensorHub.DataClient {
    private Button ble_btn, map_btn;
    private DashedCircularProgress dashedCircularProgress;
    private ViewPager viewPager;
    private SensorHub mSensorHub;
    private Sensor mSensor;
    private static TextView step_TextView;
    /**
     * 线程与handler
     */
    private Handler mHandler;
    protected Thread mThread,mThread1;

    private static Handler step_Handler;
    private long mTID;
    private static int step_pre = 0;
    private ListView mlistView;
    ArrayList<ChartItem> list = new ArrayList<ChartItem>();
    static List<String> rate = new ArrayList<>();
    private String tagUpload = "TAGUPLOAD";
    private Intent mIntent3;
    /************
     * ble config
     ********************/
    private final static String TAG = "SensorFragment";
    static TextView Text_Recv;
    static String Str_Recv;
    static String ReciveStr;
    static Handler Heart_Handler = new Handler();
    static Handler Upload_Heart_Handler = new Handler();
    static boolean ifDisplayInHexStringOnOff = true;
    static boolean ifDisplayTimeOnOff = true;
    static int Totol_recv_bytes = 0;
    static int Totol_recv_bytes_temp = 0;

    /*********************************/

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getActivity().setTitle(R.string.running_page);
        View view = inflater.inflate(R.layout.running_detail, container, false);
        Long sid = (long) 4864;//根据sensor算出的long值
        mSensorHub = SensorHub.getInstance(getContext());
        mSensor = mSensorHub.getSensor((int) (sid >> 8), (int) (sid & 0xff));

        initialView(view);
        GetStepCount();
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        dashedCircularProgress = (DashedCircularProgress) view.findViewById(R.id.simple_circle);
        dashedCircularProgress.setValue(getStepCountDatePresent(step_pre));//这个操作需要从库里面取数
        viewPager = (ViewPager) view.findViewById(R.id.circular_view_pager);
        viewPager.setAdapter(new PagerAdapter(getChildFragmentManager()));
        viewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset,
                                       int positionOffsetPixels) {
                //Empty
            }

            @Override
            public void onPageSelected(int position) {
                if (position == 0) {
                    onSpeed();
                } else if (position == 1) {
                    onStrong();
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {
                //Empty
            }
        });
        //Charts just 7 panal
        for (int i = 0; i < 10; i++) {
            if (i % 3 == 0) {
                list.add(new LineChartItem(generateDataLine(i + 1), getContext()));
            } else if (i % 3 == 1) {
                list.add(new BarChartItem(generateDataBar(i + 1), getContext()));
            }
        }

        ChartDataAdapter cda = new ChartDataAdapter(getContext(), list);
        mlistView.setAdapter(cda);

        step_Handler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                switch (msg.what) {
                    case 1:
                        step_TextView.setText(SensorFragment.step_pre + "步");//这个有bug
                        break;
                    default:
                        break;
                }
            }
        };

    }

    /**
     * todo 2017.02.06
     * 与服务器通信已经完成，剩下的就是完成数据传输，跟android端的问题了
     * 效率超级慢
     * modify in 2017.02.08
     */
    private Runnable heartRateRunnable = new Runnable() {
        @Override
        public void run() {
            String highestRate = "116";
            String lowestRate = "61";
            String averageRate = "73";
            int motionState = 1;
            int recommendState = 0;
            int execiseTime = 18;
            int execiseLoad = 23;
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            String recordDate = dateFormat.format(new Date()).toString();
            HeartRateUrlServer uploadServer = new HeartRateUrlServer(commitHandler);//调用反馈线程
            uploadServer.fastUpLoad(highestRate, lowestRate, averageRate, motionState, recommendState, execiseTime, execiseLoad, recordDate);
        }
    };
    /**
     * 上传实时心率，param:list<LittleHeartRateUnit>,uploadTime
     */
    //add in 2017.02.19
    private Runnable realHeartRateRunnable = new Runnable() {
        @Override
        public void run() {
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            String recordDate = dateFormat.format(new Date()).toString();
            List<LittleHeartRateUnit> realArr = new ArrayList<>();
            int ran = 0, max = 140, min = 45;
            for (int i = 1; i <= 20; i++) {
                LittleHeartRateUnit real = new LittleHeartRateUnit();
                Random random = new Random();
                ran = random.nextInt(max) % (max - min + 1) + min;//生成50-140以内的随机数
                real.setId(i + "");
                real.setValue(ran + "");
                if (ran < 50)
                    real.setStatus(0 + "");//小雨50无效，置0
                else
                    real.setStatus(1 + "");
                realArr.add(real);//模拟出心率数据
            }
            HeartRateUrlServer uploadServer = new HeartRateUrlServer(commitHandler);
            uploadServer.fastUploadRealRate(realArr, recordDate);//传的是json数组哦,entity是RealTimeRate

        }
    };

    /**
     * 上传数据相关
     * add in 2017.02.08
     * 修改于2017.04.08
     */
    @SuppressLint("HandlerLeak")
    private Handler commitHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 0:
                    execute(msg);
                    break;
                case 1:
                    break;
            }
        }

        private void execute(Message msg) {
            try {
                JSONObject resultJson = new JSONObject(msg.obj.toString());
                if (resultJson.get("code").toString().equals(HeartRateUrlServer.EXECUTED_SUCCESS)) {
                    Toast.makeText(getActivity(), "System callback:" + resultJson.toString() + "and code is " + resultJson.get("code").toString(), Toast.LENGTH_SHORT).show();
                }else{
                    Toast.makeText(getActivity(), "System callback:" + resultJson.toString() + "and code is " + resultJson.get("code").toString(), Toast.LENGTH_SHORT).show();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    };

    /**
     * 实时上传数据相关，测试上传用，勿删
     * add in 2017.04.22
     */
    @SuppressLint("HandlerLeak")
    private static Handler commitHandler2 = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 0:
                    execute(msg);
                    break;
                case 1:
                    break;
            }
        }
        private void execute(Message msg) {
            try {
                JSONObject resultJson = new JSONObject(msg.obj.toString());
                if (resultJson.get("code").toString().equals(HeartRateUrlServer.EXECUTED_SUCCESS)) {
                    //Toast.makeText(get, "System callback:" + resultJson.toString() + "and code is " + resultJson.get("code").toString(), Toast.LENGTH_SHORT).show();
                }else{
                    //Toast.makeText(getActivity(), "System callback:" + resultJson.toString() + "and code is " + resultJson.get("code").toString(), Toast.LENGTH_SHORT).show();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    };


    private void updateStepUI() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                Message msg = new Message();
                msg.what = 1;
                SensorFragment.step_Handler.sendMessage(msg);
            }
        }).start();
    }

    private void onSpeed() {
        //dashedCircularProgress.setIcon(R.drawable.speed);
        dashedCircularProgress.setValue(getStepCountDatePresent(step_pre));
    }

    private void onStrong() {
        //dashedCircularProgress.setIcon(R.drawable.heartRate);
        dashedCircularProgress.setValue(100);
    }

    public static SensorFragment getInstance() {
        return new SensorFragment();
    }

    private class PagerAdapter extends FragmentPagerAdapter {
        public PagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            switch (position) {
                case 0:
                    return new SpeedFragment();
                case 1:
                    return new StrongFragment();
            }

            return new SpeedFragment();
        }

        @Override
        public int getCount() {
            return 2;
        }
    }

    //将步数转化成百分比
    private int getStepCountDatePresent(int data) {
        int i = 1, temp = data;
        while (temp / 10 > 0) {
            i++;
            temp /= 10;
        }
        double tem = Math.round((data / (Math.pow(10, i))) * 100);
        return (int) tem;
    }

    private void initialView(View view) {

        mlistView = (ListView) view.findViewById(R.id.listView1);
        ble_btn = (Button) view.findViewById(R.id.ble_conn);
        ble_btn.setOnClickListener(this);
        map_btn = (Button) view.findViewById(R.id.map_btn);
        map_btn.setOnClickListener(this);

        Text_Recv = (TextView) view.findViewById(R.id.device_address);//接收到的字符串
        Text_Recv.setGravity(Gravity.CLIP_VERTICAL | Gravity.CLIP_HORIZONTAL);
        ReciveStr = "";
        Text_Recv.setMovementMethod(ScrollingMovementMethod.getInstance());

        ifDisplayInHexStringOnOff = true;
        ifDisplayTimeOnOff = true;
    }

    @Override
    public void onClick(View v) {
        //Toast.makeText(getActivity(),"look here",Toast.LENGTH_LONG).show();
        if (v.getId() == R.id.ble_conn) {
            mIntent3 =new Intent(getActivity(), DeviceScanActivity.class);
            startActivity(mIntent3);
        } else if (v.getId() == R.id.map_btn) {
            mThread = new Thread(heartRateRunnable);//开启上传数据到服务器的线程
            //mThread = new Thread(realHeartRateRunnable);//这里切换线程
            mThread.start();
        }else if(v.getId() == R.id.map_btn2){
            Toast.makeText(getActivity(),"flag",Toast.LENGTH_SHORT).show();
            mThread1 = new Thread(realHeartRateRunnable);//这个是实时心率的
            mThread1.start();
        }
    }

    //可能存在bug
    final SensorFragment inst = this;

    private void GetStepCount() {
        mTID = Thread.currentThread().getId();
        mHandler = new Handler();
        mSensorHub.startSensor(mSensor, inst);
    }

    @Override
    public void onData(SensorEvent event, final String data) {
        if (Thread.currentThread().getId() == mTID) {
            //mTextView.append("\n");
            String str_num = data.replace(".0", "");
            //mTextView.setText(str_num);//显示在屏幕的数值
            step_pre = Integer.valueOf(str_num);//进度条的百分比
            dashedCircularProgress.setValue(getStepCountDatePresent(step_pre));//这个操作需要从库里面取数
            updateStepUI();

        } else {
            mHandler.post(new Runnable() {
                @Override
                public void run() {
//                    mTextView.append("\n");
//                    mTextView.append(data + "steps");
                    String str_num = data.replace(".0", "");
                    //mTextView.setText(str_num);//显示在屏幕的数值
                    step_pre = Integer.valueOf(str_num);//进度条的百分比
                    dashedCircularProgress.setValue(getStepCountDatePresent(step_pre));//这个操作需要从库里面取数
                    updateStepUI();
                }
            });
        }
    }


    public void onPause() {
        super.onPause();
        mSensorHub.stopSensor(mSensor, this);
        onSpeed();//保存计步百分比状态？
    }

    /**
     * adapter that supports 3 different item types
     */
    private class ChartDataAdapter extends ArrayAdapter<ChartItem> {

        public ChartDataAdapter(Context context, List<ChartItem> objects) {
            super(context, 0, objects);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            return getItem(position).getView(position, convertView, getContext());
        }

        @Override
        public int getItemViewType(int position) {
            // return the views type
            return getItem(position).getItemType();
        }

        @Override
        public int getViewTypeCount() {
            return 3; // we have 3 different item-types
        }
    }

    /**
     * generates a random ChartData object with just one DataSet
     * just for sun aadadadada
     * 根据日期划分数据才行啊，一共展示12天的数据
     * 判断0点就把数据保存至本地数据库
     *
     * @return
     */
    private LineData generateDataLine(int cnt) {
        ArrayList<Entry> e1 = new ArrayList<Entry>();
        for (int i = 0; i < 12; i++) {
            e1.add(new Entry(i, (int) (Math.random() * 65) + 40));//这里生成随机数
        }

        LineDataSet d1 = new LineDataSet(e1, "今日状态" + cnt + ", (1)");
        d1.setLineWidth(2.5f);
        d1.setCircleRadius(4.5f);
        d1.setHighLightColor(Color.rgb(244, 117, 117));
        d1.setDrawValues(false);

        ArrayList<Entry> e2 = new ArrayList<Entry>();

        for (int i = 0; i < 12; i++) {
            e2.add(new Entry(i, e1.get(i).getY() - 30));
        }

        LineDataSet d2 = new LineDataSet(e2, "历史平均水平" + cnt + ", (2)");
        d2.setLineWidth(2.5f);
        d2.setCircleRadius(4.5f);
        d2.setHighLightColor(Color.rgb(244, 117, 117));
        d2.setColor(ColorTemplate.VORDIPLOM_COLORS[0]);
        d2.setCircleColor(ColorTemplate.VORDIPLOM_COLORS[0]);
        d2.setDrawValues(false);

        ArrayList<ILineDataSet> sets = new ArrayList<ILineDataSet>();
        sets.add(d1);
        sets.add(d2);

        LineData cd = new LineData(sets);
        return cd;
    }


    //// TODO: 2017/4/4  
    private LineData generateDataLine() {

        LineData re = new LineData();
        return re;
    }


    /**
     * generates a random ChartData object with just one DataSet
     *
     * @return
     */
    private BarData generateDataBar(int cnt) {

        ArrayList<BarEntry> entries = new ArrayList<BarEntry>();

        for (int i = 0; i < 12; i++) {
            entries.add(new BarEntry(i, (int) (Math.random() * 70) + 30));
        }

        BarDataSet d = new BarDataSet(entries, "New DataSet " + cnt);
        d.setColors(ColorTemplate.VORDIPLOM_COLORS);
        d.setHighLightAlpha(255);

        BarData cd = new BarData(d);
        cd.setBarWidth(0.9f);
        return cd;
    }


    public static class SpeedFragment extends Fragment {
        @Override
        public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                                 @Nullable Bundle savedInstanceState) {
            return inflater.inflate(R.layout.speed, container, false);
        }

        @Override
        public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {

            step_TextView = (TextView) view.findViewById(R.id.speed_id);
            step_TextView.setText(SensorFragment.step_pre + "步");

        }
    }


    /**
     * 计算心率，但是实际上这个方法没有一点用处
     * @param heartRate_byte
     * @param TimeRecord
     */
    public static void CountHeartRate(String heartRate_byte, String TimeRecord) {

        int temp_value = 0;
        temp_value = Integer.valueOf(heartRate_byte);//先把心率换算成整形
        if (temp_value > 0 && temp_value < 220) {
            ArrayList<HeartRate> heartRates = new ArrayList<>();
            HeartRate heartRate_single = new HeartRate();
            heartRate_single.setHeart_rate(heartRate_byte);
            heartRate_single.setRecord_time(TimeRecord);
            heartRates.add(heartRate_single);
            SaveToDB(heartRates);//未对数据做处理
        }

    }

    //入库处理
    public static void SaveToDB(ArrayList array) {
        //// TODO: 2016/12/2
    }


    //内部类
    //2016/10/6
    public static class StrongFragment extends Fragment {

        @Override
        public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                                 @Nullable Bundle savedInstanceState) {
            return inflater.inflate(R.layout.heart_rate, container, false);
        }
    }

     /**
     * 负责读出心率数据，这个是线程同步的，也就是原子性的，你需要注意代码逻辑才能写出来
     * 这个方法比较重要
     * @param str
     * @param data
     * @param uuid
     */
    public static synchronized void char6_display(String str, byte[] data,
                                                  String uuid) {
        int temp_Rate[] = new int[30];//每次调用都会new这些变量，这是需要优化的地方
        if (uuid.equals(DeviceScanActivity.UUID_HERATRATE)) {
            SimpleDateFormat formatter = new SimpleDateFormat("HH:mm:ss ");
            Date curDate = new Date(System.currentTimeMillis());// 获取当前时间
            String TimeStr = formatter.format(curDate);
            String DisplayStr = "[" + TimeStr + "] " + "HR:" + "=" + data[1];
            // Text_Recv.append(DisplayStr + "\r\n");
            Str_Recv = DisplayStr + "\r\n";
            String heartRate_byte = data[1] + "";

            rate.add(heartRate_byte);//定义在上面的String数组,加工心率数据
            CountHeartRate(heartRate_byte, TimeStr);//计算平均心率，没来一次记录一次心率和时间
             for (int i = 0; i < 30; i++) {
                temp_Rate[i] = Integer.valueOf(heartRate_byte);
            }

        }
        if (rate.size() != 0) {
            processHeartRateString(rate);
        }

        String highestRate = "100";
        String lowestRate = "60";
        String averageRate = "75";
        int motionState = 1;
        int recommendState = 0;
        int execiseTime = 18;
        int execiseLoad = 25;
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String recordDate = dateFormat.format(new Date()).toString();
        HeartRateUrlServer uploadServer = new HeartRateUrlServer(commitHandler2);//调用反馈线程
        uploadServer.fastUpLoad(highestRate, lowestRate, averageRate, motionState, recommendState, execiseTime, execiseLoad, recordDate);

        Log.i(TAG, "char6_display str = " + str);
        Totol_recv_bytes += str.length();
        Totol_recv_bytes_temp += str.length();

        Heart_Handler.post(new Runnable() {
            @Override
            public synchronized void run() {
                //scrollView.fullScroll(ScrollView.FOCUS_DOWN);// 滚动到底
                //Text_Recv.append(Str_Recv);
                Text_Recv.setText(Str_Recv);
                // 数据太多时清空数据
                if (Totol_recv_bytes_temp > 10000) {
                    Totol_recv_bytes_temp = 0;
                    Text_Recv.setText("");
                }
            }
        });
    }

    /**
     * 测试，同样要保证线程安全才行
     * @param str
     */
    public static synchronized void processHeartRateString(List<String> str) {
        for (int i = 0; i < str.size(); i++) {
            Log.d(TAG, "%%%%%%%%%%%看这里打印的数据%%%%%%%%%%%%%generateDataLine: " + str.get(i).toString());
        }
    }


}
