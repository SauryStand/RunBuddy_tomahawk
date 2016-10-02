package org.runbuddy.tomahawk.ui.fragments;

import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.text.method.ScrollingMovementMethod;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Switch;
import android.widget.TextView;

import com.github.glomadrian.dashedcircularprogress.DashedCircularProgress;

import org.runbuddy.Device.BlueTooth.DeviceScanActivity;
import org.runbuddy.Device.CounterSensor.SensorActivity;
import org.runbuddy.Device.CounterSensor.SensorHub;
import org.runbuddy.Device.CounterSensor.SensorListAdapter;
import org.runbuddy.R;
import org.runbuddy.tomahawk.activities.AmapActivity;
import org.runbuddy.tomahawk.ui.Intricate_Charts.ListViewMultiChartActivity;
import org.runbuddy.tomahawk.ui.basic.BaseFragment;

import java.text.NumberFormat;

/**
 * Created by Jonney Chou on 2016/7/27.
 */
public class SensorListFragment extends BaseFragment implements View.OnClickListener,SensorHub.DataClient{

    private ImageButton step_count_btn;
    private Button Charts, ble_btn, map_btn;
    private DashedCircularProgress dashedCircularProgress;
    private SensorHub mSensorHub;
    private Sensor mSensor;
    private TextView mTextView;
    private Button mConsoleButton;
    private Switch mConsoleSwitch;
    private Handler mHandler;
    private long mTID;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getActivity().setTitle(R.string.running_page);
        View view = inflater.inflate(R.layout.zhihu_fragment_first_detail, container, false);

        Long sid = (long)4864;
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
        dashedCircularProgress.setValue(55);//这个操作需要从库里面取数据，懂吗
        //Toast.makeText(getContext(), getStepCountDatePresent()+"", Toast.LENGTH_SHORT).show();

    }

    private int getStepCountDatePresent() {
        NumberFormat numberFormat = NumberFormat.getInstance();
        numberFormat.setMaximumFractionDigits(1);
        int steps = SensorActivity.getStep_temp_count();
        float tem = (float) steps / ((float) steps * 10) * 10;
        return (int) tem;
    }

    private void initialView(View view) {
        step_count_btn = (ImageButton) view.findViewById(R.id.step_counting_btn);
        step_count_btn.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
                    view.setBackgroundResource(R.drawable.running_48px_gray);
                    SensorListAdapter.sensorType(16);//16是step counter
                    Bundle data = new Bundle();
                    //mIntent = new Intent(getContext(), SensorActivity.class);
                    //data.putLong("sensorID", 4864);
                    //mIntent.putExtras(data);
                    //getActivity().startActivity(mIntent);
                    GetStepCount();
                } else if (motionEvent.getAction() == MotionEvent.ACTION_UP) {
                    view.setBackgroundResource(R.drawable.running_48px);
                }
                return false;
            }
        });
        Charts = (Button) view.findViewById(R.id.charts_info);
        Charts.setOnClickListener(this);
        ble_btn = (Button) view.findViewById(R.id.ble_conn);
        ble_btn.setOnClickListener(this);
        map_btn = (Button) view.findViewById(R.id.Map_btn);
        map_btn.setOnClickListener(this);
        mTextView = (TextView) view.findViewById(R.id.textView_steps);
        mTextView.setOnClickListener(this);

    }



    private Intent mIntent, mIntent2, mIntent3, mIntent4;

    @Override
    public void onClick(View v) {

        if (v.getId() == R.id.charts_info) {
            mIntent2 = new Intent(getActivity(), ListViewMultiChartActivity.class);
            startActivity(mIntent2);
        } else if (v.getId() == R.id.ble_conn) {
            mIntent3 = new Intent(getActivity(), DeviceScanActivity.class);
            startActivity(mIntent3);
        } else if (v.getId() == R.id.Map_btn) {
            mIntent4 = new Intent(getActivity(), AmapActivity.class);
            startActivity(mIntent4);
        }
    }



    private void resetConsole() {
        String text = "Name: " + mSensor.getName() +
                "\nType: " + SensorListAdapter.sensorName(mSensor.getType()) +
                "\nVendor: " + mSensor.getVendor() +
                "\nVersion: " + mSensor.getVersion() +
                "\nRange: " + mSensor.getMaximumRange() +
                "\nMin-Delay: " + mSensor.getMinDelay() +
                "\nPower: " + mSensor.getPower() + "mA" +
                "\nResolution: " + mSensor.getResolution() + "\n-------------------------------------\n";
        if (null == mTextView) {
            //mTextView = (TextView) findViewById(R.id.sensor);
        }

        mTextView.setText(text);
    }

    //可能存在bug
    final SensorListFragment inst = this;
    private void GetStepCount(){
        mTID = Thread.currentThread().getId();
        mHandler = new Handler();
        mSensorHub.startSensor(mSensor, inst);
    }

    @Override
    public void onData(SensorEvent event, final String data) {
        if (Thread.currentThread().getId() == mTID) {
            //In the main thread
            //mTextView.append("\n");
            String str_num = data.replace(".0", "");
            //mTextView.append(str_num + "step");
            mTextView.setText(str_num);
            //setStep_temp_count(Integer.valueOf(str_num));

        } else {
            mHandler.post(new Runnable() {
                @Override
                public void run() {
                    mTextView.append("\n");
                    mTextView.append(data + "asd");
                }
            });
        }
    }

    public void onResume() {
        super.onResume();
        resetConsole();
        mTextView.setMovementMethod(new ScrollingMovementMethod());
    }

    public void onPause() {
        super.onPause();
        mSensorHub.stopSensor(mSensor, this);
    }




}
