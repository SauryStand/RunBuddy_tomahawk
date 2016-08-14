package org.runbuddy.tomahawk_android.fragments;

import android.content.Intent;
import android.hardware.Sensor;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.fragmentation.SupportFragment;

import org.runbuddy.Device.CounterSensor.SensorActivity;
import org.runbuddy.Device.CounterSensor.SensorHub;
import org.runbuddy.Device.CounterSensor.SensorListAdapter;
import org.runbuddy.tomahawk_android.demo_zhihu.ui.view.BottomBar;
import org.runbuddy.tomahawk_android.utils.FragmentInfo;
import org.tomahawk.tomahawk_android.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Jonney Chou on 2016/7/27.
 */
public class LocalMusicListFragment extends Fragment implements View.OnClickListener {

    public static final int MODE_HEADER_STATIC_SMALL = 8;
    private MenuItem mCountryCodePicker;
    private List<FragmentInfo> mFragmentInfos = new ArrayList<>();

    private FragmentInfo mSelectedFragmentInfo;
    private SensorHub mSensorHub;
    private Sensor mSensor;
    private TextView mTextView;
    private Button mConsoleButton;
    private Switch mConsoleSwitch;
    private Handler mHandler;
    private Button clickme;
    private long mTID;
    public BottomBar mBottomBar;
    private SupportFragment[] mFragments = new SupportFragment[3];

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getActivity().setTitle(R.string.drawer_title_localList);
        View view = inflater.inflate(R.layout.zhihu_fragment_first_detail, container, false);
        initialView(view);
        //clickme = (Button) view.findViewById(R.id.click_me);
        //clickme.setOnClickListener(this);


        //to do list



        return view;
    }

    private void initialView(View view) {
        clickme = (Button) view.findViewById(R.id.click_me);
        clickme.setOnClickListener(this);
    }


    private Intent mIntent;

    @Override
    public void onClick(View v) {

        if (v.getId() == R.id.click_me) {
            SensorListAdapter.sensorType(16);//16是step counter
            Bundle data = new Bundle();
            mIntent = new Intent(getContext(), SensorActivity.class);
            data.putLong("sensorID", 4864);
            mIntent.putExtras(data);
            getActivity().startActivity(mIntent);
            Toast.makeText(getContext(), "need to add sensor counter", Toast.LENGTH_SHORT).show();
        }


    }
}
