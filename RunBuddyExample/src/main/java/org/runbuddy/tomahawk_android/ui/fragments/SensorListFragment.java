package org.runbuddy.tomahawk_android.ui.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;

import com.github.glomadrian.dashedcircularprogress.DashedCircularProgress;

import org.runbuddy.Device.BlueTooth.DeviceScanActivity;
import org.runbuddy.Device.CounterSensor.SensorActivity;
import org.runbuddy.Device.CounterSensor.SensorListAdapter;
import org.runbuddy.tomahawk_android.ui.Intricate_Charts.ListViewMultiChartActivity;
import org.runbuddy.tomahawk_android.ui.basic.BaseFragment;
import org.tomahawk.tomahawk_android.R;

import java.text.NumberFormat;

/**
 * Created by Jonney Chou on 2016/7/27.
 */
public class SensorListFragment extends BaseFragment implements View.OnClickListener {

    private ImageButton step_count_btn;
    private Button Charts, ble_btn;
    private DashedCircularProgress dashedCircularProgress;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getActivity().setTitle(R.string.drawer_title_running_mv_page);
        View view = inflater.inflate(R.layout.zhihu_fragment_first_detail, container, false);
        initialView(view);
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
                    mIntent = new Intent(getContext(), SensorActivity.class);
                    data.putLong("sensorID", 4864);
                    mIntent.putExtras(data);
                    getActivity().startActivity(mIntent);
                    //Toast.makeText(getContext(), "need to add sensor counter", Toast.LENGTH_SHORT).show();
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

    }


    private Intent mIntent, mIntent2, mIntent3;

    @Override
    public void onClick(View v) {

        if (v.getId() == R.id.charts_info) {
            mIntent2 = new Intent(getActivity(), ListViewMultiChartActivity.class);
            startActivity(mIntent2);
        } else if (v.getId() == R.id.ble_conn) {
            mIntent3 = new Intent(getActivity(), DeviceScanActivity.class);
            startActivity(mIntent3);
        }
    }


}
