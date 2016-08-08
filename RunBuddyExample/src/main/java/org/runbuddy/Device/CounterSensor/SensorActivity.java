package org.runbuddy.Device.CounterSensor;

import android.app.Activity;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.os.Bundle;
import android.os.Handler;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;

import org.tomahawk.tomahawk_android.R;

import java.util.ArrayList;

/**
 * Created by Jonney Chou on 2016/8/7.
 */
public class SensorActivity extends Activity implements SensorHub.DataClient {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.item_trainning_detail_listview_home);

        Bundle data = getIntent().getExtras();
        Long sid = data.getLong("sensorID");
        mSensorHub = SensorHub.getInstance(getApplicationContext());
        mSensor = mSensorHub.getSensor((int) (sid >> 8), (int) (sid & 0xff));
    }

    private void resetConsole() {
        String text = "Name: " + mSensor.getName() +
                "\nType: " + SensorListAdapter.sensorName(Sensor.TYPE_STEP_COUNTER) +
                "\nVendor: " + mSensor.getVendor() +
                "\nVersion: " + mSensor.getVersion() +
                "\nRange: " + mSensor.getMaximumRange() +
                "\nMin-Delay: " + mSensor.getMinDelay() +
                "\nPower: " + mSensor.getPower() + "mA" +
                "\nResolution: " + mSensor.getResolution() + "\n-------------------------------------\n";

        if (null == mTextView) {
            mTextView = (TextView) findViewById(R.id.sensor);
        }

        mTextView.setText(text);
    }

    private void registerHandlers() {

        mConsoleButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resetConsole();
            }
        });

        final SensorActivity inst = this;
        mConsoleSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                // TODO Auto-generated method stub
                if (isChecked) {
                    mTID = Thread.currentThread().getId();
                    mHandler = new Handler();

                    mSensorHub.startSensor(mSensor, inst);
                } else {
                    mSensorHub.stopSensor(mSensor, inst);
                }
            }
        });

    }

    public void onResume() {
        super.onResume();

        mTextView = (TextView) findViewById(R.id.sensor);
        mConsoleButton = (Button) findViewById(R.id.ClearText);
        mConsoleSwitch = (Switch) findViewById(R.id.SwitchTextPrint);

        resetConsole();
        mTextView.setMovementMethod(new ScrollingMovementMethod());
        mConsoleSwitch.setChecked(false);
        ArrayList<SensorHub.DataClient> list = mSensorHub.peekSensorClients(mSensor);
        registerHandlers();

    }

    public void onPause() {
        super.onPause();
        mSensorHub.stopSensor(mSensor, this);
    }


    private SensorHub mSensorHub;
    private Sensor mSensor;
    private TextView mTextView;
    private Button mConsoleButton;
    private Switch mConsoleSwitch;
    private Handler mHandler;
    private long mTID;

    @Override
    public void onData(SensorEvent event, final String data) {
        // TODO Auto-generated method stub
        if (Thread.currentThread().getId() == mTID) {
            //In the main thread
            mTextView.append("\n");
            mTextView.append(data + "www");
        } else {
            mHandler.post(new Runnable() {
                @Override
                public void run() {
                    // TODO Auto-generated method stub
                    mTextView.append("\n");
                    mTextView.append(data + "asd");
                }

            });
        }
    }
}
