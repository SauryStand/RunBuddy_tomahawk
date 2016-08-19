package org.runbuddy.tomahawk_android.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;

import org.runbuddy.Device.CounterSensor.SensorActivity;
import org.runbuddy.Device.CounterSensor.SensorListAdapter;
import org.runbuddy.Intricate_Charts.ListViewMultiChartActivity;
import org.tomahawk.tomahawk_android.R;

/**
 * Created by Jonney Chou on 2016/7/27.
 */
public class LocalMusicListFragment extends Fragment implements View.OnClickListener {

    private ImageButton step_count_btn;
    private Button Charts;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getActivity().setTitle(R.string.drawer_title_localList);
        View view = inflater.inflate(R.layout.zhihu_fragment_first_detail, container, false);
        initialView(view);
        return view;
    }

    private void initialView(View view) {
        step_count_btn = (ImageButton) view.findViewById(R.id.step_counting_btn);
        step_count_btn.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if(motionEvent.getAction() == MotionEvent.ACTION_DOWN){
                    view.setBackgroundResource(R.drawable.running_48px_gray);
                    SensorListAdapter.sensorType(16);//16是step counter
                    Bundle data = new Bundle();
                    mIntent = new Intent(getContext(), SensorActivity.class);
                    data.putLong("sensorID", 4864);
                    mIntent.putExtras(data);
                    getActivity().startActivity(mIntent);
                    //Toast.makeText(getContext(), "need to add sensor counter", Toast.LENGTH_SHORT).show();
                }else if(motionEvent.getAction() == MotionEvent.ACTION_UP){
                    view.setBackgroundResource(R.drawable.running_48px);
                }
                return false;
            }
        });
        Charts = (Button)view.findViewById(R.id.charts_info);
        Charts.setOnClickListener(this);
    }


    private Intent mIntent,mIntent2;

    @Override
    public void onClick(View v) {

        if (v.getId() == R.id.step_counting_btn) {

        }
        if(v.getId() == R.id.charts_info){
            mIntent2 = new Intent(getActivity(), ListViewMultiChartActivity.class);
            startActivity(mIntent2);
        }


    }
}
