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
import android.widget.Toast;

import org.tomahawk.tomahawk_android.R;

/**
 * Created by Jonney Chou on 2016/8/7.
 */
public class SensorActivity extends Activity implements SensorHub.DataClient {

    private static int step_temp_count;

    public static int getStep_temp_count() {
        return step_temp_count;
    }

    public void setStep_temp_count(int step_temp_count) {
        this.step_temp_count = step_temp_count;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.item_trainning_detail_listview_home);
        //getSupportActionBar().setTitle("");
        //这里应该加个title,flag2016.08.14

        Bundle data = getIntent().getExtras();
        Long sid = data.getLong("sensorID");
        mSensorHub = SensorHub.getInstance(getApplicationContext());
        mSensor = mSensorHub.getSensor((int) (sid >> 8), (int) (sid & 0xff));
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
                //打开了开关
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

    //入库操作
    private void insertIntoDB(){

    }




    public void onResume() {
        super.onResume();

        mTextView = (TextView) findViewById(R.id.sensor);
        mConsoleButton = (Button) findViewById(R.id.ClearText);
        mConsoleSwitch = (Switch) findViewById(R.id.SwitchTextPrint);
        resetConsole();
        mTextView.setMovementMethod(new ScrollingMovementMethod());
        mConsoleSwitch.setChecked(false);
        registerHandlers();

    }

    public void onPause() {
        super.onPause();
        mSensorHub.stopSensor(mSensor, this);
    }

    /******************
     * flag is on top
     ****************************/

    private SensorHub mSensorHub;
    private Sensor mSensor;
    private TextView mTextView;
    private Button mConsoleButton;
    private Switch mConsoleSwitch;
    private Handler mHandler;
    private long mTID;

    @Override
    public void onData(SensorEvent event, final String data) {
        if (Thread.currentThread().getId() == mTID) {
            //In the main thread
            mTextView.append("\n");
            String str_num = data.replace(".0", "");
            mTextView.append(str_num + "step");
            //setStep_temp_count(Integer.valueOf(str_num));
            Toast.makeText(getApplicationContext(), getStep_temp_count() + "", Toast.LENGTH_LONG).show();
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
}
