package org.runbuddy.device.CounterSensor;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.util.Log;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Johnny on 2016/8/6.
 */

public class SensorHub implements SensorEventListener {

    public interface DataClient {
        void onData(SensorEvent event, String text);
    }

    public static SensorHub getInstance(Context ctx) {
        if (null == mInstance) {
            mInstance = new SensorHub(ctx);
        }
        return mInstance;
    }

    private SensorHub(Context ctx) {
        mSensorManager = (SensorManager) ctx.getSystemService(Context.SENSOR_SERVICE);
        mSensors = mSensorManager.getSensorList(Sensor.TYPE_ALL);
        mClients = new HashMap<Sensor, ArrayList<DataClient>>();
        Log.i("tangzm", "total sensor:" + mSensors.size());
    }

    public int SensorNum(int type) {
        int num = 0;
        for (int i = 0; i < mSensors.size(); i++) {
            if (type == mSensors.get(i).getType()) {
                num++;
            }
        }
        return num;
    }

    public Sensor getSensor(int type, int index) {
        for (int i = 0; i < mSensors.size(); i++) {
            if (type == mSensors.get(i).getType()) {
                if (index == 0) {
                    return mSensors.get(i);
                } else {
                    index--;
                }
            }
        }

        return null;
    }

    public void startSensor(Sensor sensor, DataClient client) {
        ArrayList<DataClient> clientList = mClients.get(sensor);

        if (null == clientList) {
            mSensorManager.registerListener(this, sensor, 0);
            clientList = new ArrayList<DataClient>();
            mClients.put(sensor, clientList);
        }

        clientList.add(client);
    }

    public void stopSensor(Sensor sensor, DataClient client) {
        ArrayList<DataClient> clientList = mClients.get(sensor);

        if (clientList != null) {
            for (int i = 0; i < clientList.size(); i++) {
                if (clientList.get(i) == client) {
                    clientList.remove(i);
                }
            }

            if (0 == clientList.size()) {
                mSensorManager.unregisterListener(this, sensor);
                mClients.remove(sensor);
            }
        }
    }

    public void stopSensor(Sensor sensor) {
        mSensorManager.unregisterListener(this, sensor);
        mClients.remove(sensor);
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        // TODO Auto-generated method stub
        //String text = "-->>" + event.timestamp;
        String text = "";
        for (int i = 0; i < event.values.length; i++) {
            text += event.values[i];//这里原来是把芯片计步的数据都算出来再显示，应该是算一个时间戳之类的
        }

        ArrayList<DataClient> clientList = mClients.get(event.sensor);

        if (clientList != null) {
            for (int i = 0; i < clientList.size(); i++) {
                clientList.get(i).onData(event, text);
            }
        }
    }

    public ArrayList<DataClient> peekSensorClients(Sensor sensor) {
        return mClients.get(sensor);
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
        // TODO Auto-generated method stub

    }

    private static SensorHub mInstance = null;
    private SensorManager mSensorManager;
    private List<Sensor> mSensors;
    private HashMap<Sensor, ArrayList<DataClient>> mClients;
}
