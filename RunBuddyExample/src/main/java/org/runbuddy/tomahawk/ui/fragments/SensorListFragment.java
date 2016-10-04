package org.runbuddy.tomahawk.ui.fragments;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.github.glomadrian.dashedcircularprogress.DashedCircularProgress;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.github.mikephil.charting.utils.ColorTemplate;

import org.runbuddy.Device.BlueTooth.DeviceScanActivity;
import org.runbuddy.Device.CounterSensor.SensorHub;
import org.runbuddy.R;
import org.runbuddy.tomahawk.activities.AmapActivity;
import org.runbuddy.tomahawk.ui.IntricateCharts.listviewItems.BarChartItem;
import org.runbuddy.tomahawk.ui.IntricateCharts.listviewItems.ChartItem;
import org.runbuddy.tomahawk.ui.IntricateCharts.listviewItems.LineChartItem;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Johnny Chow on 2016/7/27.
 */
public class SensorListFragment extends Fragment implements View.OnClickListener, SensorHub.DataClient {

    private Button ble_btn, map_btn;
    private DashedCircularProgress dashedCircularProgress;
    private ViewPager viewPager;
    private SensorHub mSensorHub;
    private Sensor mSensor;
    private TextView mTextView;
    private Handler mHandler;
    private long mTID;
    private static int step_pre = 0;
    private ListView mlistView;
    ArrayList<ChartItem> list = new ArrayList<ChartItem>();

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getActivity().setTitle(R.string.running_page);
        View view = inflater.inflate(R.layout.running_detail, container, false);
        Long sid = (long) 4864;
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


        for (int i = 0; i < 10; i++) {
            if (i % 3 == 0) {
                list.add(new LineChartItem(generateDataLine(i + 1), getContext()));
            } else if (i % 3 == 1) {
                list.add(new BarChartItem(generateDataBar(i + 1), getContext()));
            }
//            else if (i % 3 == 2) {
//                list.add(new PieChartItem(generateDataPie(i + 1), getContext()));
//            }
        }

        ChartDataAdapter cda = new ChartDataAdapter(getContext(), list);
        mlistView.setAdapter(cda);

    }


    private void onSpeed() {
        dashedCircularProgress.setIcon(R.drawable.speed);
        dashedCircularProgress.setValue(getStepCountDatePresent(step_pre));
    }

    private void onStrong() {
        dashedCircularProgress.setIcon(R.drawable.strong);
        dashedCircularProgress.setValue(100);
    }

    public static SensorListFragment getInstance() {
        return new SensorListFragment();
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


    public static class SpeedFragment extends Fragment {

        TextView textView;

        @Override
        public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                                 @Nullable Bundle savedInstanceState) {
            return inflater.inflate(R.layout.speed, container, false);
        }

        @Override
        public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
            textView = (TextView) view.findViewById(R.id.speed_id);
            textView.setText(SensorListFragment.step_pre + "步");
        }

    }

    public static class StrongFragment extends Fragment {
        @Override
        public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                                 @Nullable Bundle savedInstanceState) {
            return inflater.inflate(R.layout.strong, container, false);
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
        map_btn = (Button) view.findViewById(R.id.Map_btn);
        map_btn.setOnClickListener(this);
        mTextView = (TextView) view.findViewById(R.id.textView_steps);
        mTextView.setOnClickListener(this);


    }


    private Intent mIntent3, mIntent4;

    @Override
    public void onClick(View v) {

        if (v.getId() == R.id.ble_conn) {
            mIntent3 = new Intent(getActivity(), DeviceScanActivity.class);
            startActivity(mIntent3);
        } else if (v.getId() == R.id.Map_btn) {
            mIntent4 = new Intent(getActivity(), AmapActivity.class);
            startActivity(mIntent4);
        }
    }

    //可能存在bug
    final SensorListFragment inst = this;

    private void GetStepCount() {
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
            mTextView.setText(str_num);//显示在屏幕的数值
            step_pre = Integer.valueOf(str_num);//进度条的百分比
            //setStep_temp_count(Integer.valueOf(str_num));
            dashedCircularProgress.setValue(getStepCountDatePresent(step_pre));//这个操作需要从库里面取数

        } else {
            mHandler.post(new Runnable() {
                @Override
                public void run() {
                    mTextView.append("\n");
                    mTextView.append(data + "steps");
                }
            });
        }
    }


    public void onPause() {
        super.onPause();
        mSensorHub.stopSensor(mSensor, this);
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
     *
     * @return
     */
    private LineData generateDataLine(int cnt) {
        ArrayList<Entry> e1 = new ArrayList<Entry>();

        for (int i = 0; i < 12; i++) {
            e1.add(new Entry(i, (int) (Math.random() * 65) + 40));
        }

        LineDataSet d1 = new LineDataSet(e1, "New DataSet " + cnt + ", (1)");
        d1.setLineWidth(2.5f);
        d1.setCircleRadius(4.5f);
        d1.setHighLightColor(Color.rgb(244, 117, 117));
        d1.setDrawValues(false);

        ArrayList<Entry> e2 = new ArrayList<Entry>();

        for (int i = 0; i < 12; i++) {
            e2.add(new Entry(i, e1.get(i).getY() - 30));
        }

        LineDataSet d2 = new LineDataSet(e2, "New DataSet " + cnt + ", (2)");
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


}
