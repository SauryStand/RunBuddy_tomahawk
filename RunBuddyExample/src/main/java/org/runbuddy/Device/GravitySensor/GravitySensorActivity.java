package org.runbuddy.Device.GravitySensor;

import android.app.Activity;
import android.hardware.SensorEvent;
import android.os.Bundle;

import org.runbuddy.Device.CounterSensor.SensorHub;

/**
 * Created by Johnny Chow on 2016/8/15.
 */
public class GravitySensorActivity extends Activity implements SensorHub.DataClient {


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //// TODO: 2016/8/15

    }


    @Override
    public void onData(SensorEvent event, String text) {

    }
}
