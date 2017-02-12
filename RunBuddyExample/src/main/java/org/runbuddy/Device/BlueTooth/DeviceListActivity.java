package org.runbuddy.device.BlueTooth;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.widget.ArrayAdapter;

/**
 * Created by Johnny Chow on 2016/9/7.
 */
public class DeviceListActivity extends Activity {

    /**
     * Tag for Log
     */
    private static final String TAG = "DeviceListActivity";

    /**
     * Return Intent extra
     */
    public static String EXTRA_DEVICE_ADDRESS = "device_address";

    /**
     * Member fields
     */
    private BluetoothAdapter mBtAdapter;

    /**
     * Newly discovered devices
     */
    private ArrayAdapter<String> mNewDevicesArrayAdapter;


}
