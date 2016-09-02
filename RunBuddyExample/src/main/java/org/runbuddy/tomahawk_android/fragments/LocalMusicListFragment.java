package org.runbuddy.tomahawk_android.fragments;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattDescriptor;
import android.bluetooth.BluetoothGattService;
import android.bluetooth.BluetoothManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Toast;

import com.github.glomadrian.dashedcircularprogress.DashedCircularProgress;

import org.runbuddy.Device.BlueTooth.AmoComActivity;
import org.runbuddy.Device.BlueTooth.BluetoothLeClass;
import org.runbuddy.Device.BlueTooth.DeviceScanActivity;
import org.runbuddy.Device.BlueTooth.LeDeviceListAdapter;
import org.runbuddy.Device.BlueTooth.Utils;
import org.runbuddy.Device.BlueTooth.iBeaconClass;
import org.runbuddy.Device.BlueTooth.iBeaconClass.iBeacon;
import org.runbuddy.Device.CounterSensor.SensorActivity;
import org.runbuddy.Device.CounterSensor.SensorListAdapter;
import org.runbuddy.Intricate_Charts.ListViewMultiChartActivity;
import org.runbuddy.tomahawk_android.demo_zhihu.basic.BaseFragment;
import org.tomahawk.tomahawk_android.R;

import java.text.NumberFormat;
import java.util.List;

/**
 * Created by Jonney Chou on 2016/7/27.
 */
public class LocalMusicListFragment extends BaseFragment implements View.OnClickListener {

    private final static String TAG = "DeviceScanActivity";// DeviceScanActivity.class.getSimpleName();
    public static final int REFRESH = 0x000001;
    private final static int REQUEST_CODE = 1;

    public static String UUID_KEY_DATA = "0000ffe1-0000-1000-8000-00805f9b34fb";
    public static String UUID_CHAR1 = "0000fff1-0000-1000-8000-00805f9b34fb";
    public static String UUID_CHAR2 = "0000fff2-0000-1000-8000-00805f9b34fb";
    public static String UUID_CHAR3 = "0000fff3-0000-1000-8000-00805f9b34fb";
    public static String UUID_CHAR4 = "0000fff4-0000-1000-8000-00805f9b34fb";
    public static String UUID_CHAR5 = "0000fff5-0000-1000-8000-00805f9b34fb";
    public static String UUID_CHAR6 = "0000fff6-0000-1000-8000-00805f9b34fb";
    public static String UUID_CHAR7 = "0000fff7-0000-1000-8000-00805f9b34fb";
    public static String UUID_HERATRATE = "00002a37-0000-1000-8000-00805f9b34fb";
    public static String UUID_TEMPERATURE = "00002a1c-0000-1000-8000-00805f9b34fb";
    public static String UUID_0XFFA6 = "0000ffa6-0000-1000-8000-00805f9b34fb";

    static BluetoothGattCharacteristic gattCharacteristic_char1 = null;
    static BluetoothGattCharacteristic gattCharacteristic_char5 = null;
    static BluetoothGattCharacteristic gattCharacteristic_char6 = null;
    static BluetoothGattCharacteristic gattCharacteristic_heartrate = null;
    static BluetoothGattCharacteristic gattCharacteristic_keydata = null;
    static BluetoothGattCharacteristic gattCharacteristic_temperature = null;
    static BluetoothGattCharacteristic gattCharacteristic_0xffa6 = null;

    private LeDeviceListAdapter mLeDeviceListAdapter = null;//list 的adapter
    // 搜索BLE终端
    private BluetoothAdapter mBluetoothAdapter;
    // 读写BLE终端
    static private BluetoothLeClass mBLE;
    public String bluetoothAddress;
    static private byte writeValue_char1 = 0;
    private boolean mScanning;
    private Handler mHandler = null;

    private ImageButton step_count_btn;
    private Button Charts, ble_btn;
    private DashedCircularProgress dashedCircularProgress;
    private static final long SCAN_PERIOD = 100000;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getActivity().setTitle(R.string.drawer_title_running_page);
        View view = inflater.inflate(R.layout.zhihu_fragment_first_detail, container, false);
        initialView(view);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        dashedCircularProgress = (DashedCircularProgress) view.findViewById(R.id.simple_circle);
        dashedCircularProgress.setValue(50);//这个操作需要从库里面取数据，懂吗
        //Toast.makeText(getContext(), getStepCountDatePresent()+"", Toast.LENGTH_SHORT).show();

        if (!getContext().getPackageManager().hasSystemFeature(
                PackageManager.FEATURE_BLUETOOTH_LE)) {
            Toast.makeText(getActivity(), R.string.ble_not_supported, Toast.LENGTH_SHORT)
                    .show();
            return;
            //finish();
        } else {
            Log.i(TAG, "initialize Bluetooth, has BLE system");
        }
        // Initializes a Bluetooth adapter. For API level 18 and above, get a
        // reference to
        // BluetoothAdapter through BluetoothManager.
        final BluetoothManager bluetoothManager = (BluetoothManager) getContext().getSystemService(Context.BLUETOOTH_SERVICE);
        mBluetoothAdapter = bluetoothManager.getAdapter();
        // Checks if Bluetooth is supported on the device.
        if (mBluetoothAdapter == null) {
            Toast.makeText(getActivity(), R.string.error_bluetooth_not_supported,
                    Toast.LENGTH_SHORT).show();
            return;
        } else {
            Log.i(TAG, "mBluetoothAdapter = " + mBluetoothAdapter);
        }
        //
        mBluetoothAdapter.enable();
        Log.i(TAG, "mBluetoothAdapter.enable");

        mBLE = new BluetoothLeClass(getContext());
        if (!mBLE.initialize()) {
            Log.e(TAG, "Unable to initialize Bluetooth");
            //finish();
            return;
        }
        Log.i(TAG, "mBLE = e" + mBLE);
        // 发现BLE终端的Service时回�?
        mBLE.setOnServiceDiscoverListener(mOnServiceDiscover);
        // 收到BLE终端数据交互的事�?
        mBLE.setOnDataAvailableListener(mOnDataAvailable);
        mHandler = new Handler() {
            int count = 0;

            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
            }
        };
        new MyThread().start();

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

    @Override
    public void onResume() {
        Log.i(TAG, "---> onResume");
        super.onResume();
        mBLE.close();
        // Initializes list view adapter.
        mLeDeviceListAdapter = new LeDeviceListAdapter(getActivity());
        //setListAdapter(mLeDeviceListAdapter);
        scanLeDevice(true);
    }

    private void setListAdapter(LeDeviceListAdapter mLeDeviceListAdapter) {
        throw new RuntimeException("Stub!");
    }

    @Override
    public void onPause() {
        Log.i(TAG, "---> onPause");
        super.onPause();
    }

    @Override
    public void onStop() {
        Log.i(TAG, "---> onStop");
        super.onStop();
    }


    public void onListItemClick(ListView l, View v, int position, long id) {
        final iBeacon device = mLeDeviceListAdapter.getDevice(position);
        if (device == null)
            return;
        if (mScanning) {
            mBluetoothAdapter.stopLeScan(mLeScanCallback);
            mScanning = false;
        }

        Log.i(TAG, "mBluetoothAdapter.enable");
        bluetoothAddress = device.bluetoothAddress;
        boolean bRet = mBLE.connect(device.bluetoothAddress);

        Log.i(TAG, "connect bRet = " + bRet);

        Toast toast = Toast.makeText(getContext(), "正在连接设备并获取服务中",
                Toast.LENGTH_SHORT);
        toast.setGravity(Gravity.CENTER, 0, 0);
        toast.show();
    }


    private void scanLeDevice(final boolean enable) {
        if (enable) {
            // Stops scanning after a pre-defined scan period.
            mHandler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    mScanning = false;
                    mBluetoothAdapter.stopLeScan(mLeScanCallback);
                    //invalidateOptionsMenu();
                }
            }, SCAN_PERIOD);

            mScanning = true;
            mBluetoothAdapter.startLeScan(mLeScanCallback);
        } else {
            mScanning = false;
            mBluetoothAdapter.stopLeScan(mLeScanCallback);
        }
        //invalidateOptionsMenu();
    }


    /**
     * 搜索到BLE终端服务的事�?
     */
    private BluetoothLeClass.OnServiceDiscoverListener mOnServiceDiscover = new BluetoothLeClass.OnServiceDiscoverListener() {
        @Override
        public void onServiceDiscover(BluetoothGatt gatt) {
            displayGattServices(mBLE.getSupportedGattServices());
        }
    };

    /**
     * 收到BLE终端数据交互的事�?
     */
    private BluetoothLeClass.OnDataAvailableListener mOnDataAvailable = new BluetoothLeClass.OnDataAvailableListener() {
        /**
         * BLE终端数据被读的事�?
         */
        @Override
        public void onCharacteristicRead(BluetoothGatt gatt,
                                         BluetoothGattCharacteristic characteristic, int status) {
            // 执行 mBLE.readCharacteristic(gattCharacteristic); 后就会收到数�? if
            // (status == BluetoothGatt.GATT_SUCCESS)
            Log.e(TAG,
                    "onCharRead " + gatt.getDevice().getName() + " read "
                            + characteristic.getUuid().toString() + " -> "
                            + Utils.bytesToHexString(characteristic.getValue()));
            AmoComActivity.char6_display(Utils.bytesToString(characteristic
                    .getValue()), characteristic.getValue(), characteristic
                    .getUuid().toString());
        }

        /**
         * 收到BLE终端写入数据回调
         */
        public void onCharacteristicWrite(BluetoothGatt gatt,
                                          BluetoothGattCharacteristic characteristic) {
            Log.e(TAG, "onCharWrite " + gatt.getDevice().getName() + " write "
                    + characteristic.getUuid().toString() + " -> "
                    + new String(characteristic.getValue()));
            // OtherActivity.char6_display(Utils.bytesToHexString(characteristic.getValue()));
            AmoComActivity.char6_display(Utils.bytesToString(characteristic
                    .getValue()), characteristic.getValue(), characteristic
                    .getUuid().toString());
        }
    };


    // Device scan callback.
    private BluetoothAdapter.LeScanCallback mLeScanCallback = new BluetoothAdapter.LeScanCallback() {

        @Override
        public void onLeScan(final BluetoothDevice device, int rssi,
                             byte[] scanRecord) {

            final iBeacon ibeacon = iBeaconClass.fromScanData(device, rssi,
                    scanRecord);

            new Runnable() {
                @Override
                public void run() {
                    mLeDeviceListAdapter.addDevice(ibeacon);
                    mLeDeviceListAdapter.notifyDataSetChanged();

                    // 发现小米3必须加以下的这3个语句，否则不更新数据，而三星的机子s3则没有这个问题
                    if (mScanning == true) {
                        mBluetoothAdapter.stopLeScan(mLeScanCallback);
                        mBluetoothAdapter.startLeScan(mLeScanCallback);
                    }
                }
            };

            // rssi
            Log.i(TAG, "rssi = " + rssi);
            Log.i(TAG, "mac = " + device.getAddress());
            Log.i(TAG, "scanRecord.length = " + scanRecord.length);
        }
    };

    private void displayGattServices(List<BluetoothGattService> gattServices) {
        if (gattServices == null)
            return;
        BluetoothGattCharacteristic Characteristic_cur = null;

        for (BluetoothGattService gattService : gattServices) {
            // -----Service的字段信道----//
            int type = gattService.getType();
            Log.e(TAG, "-->service type:" + Utils.getServiceType(type));
            Log.e(TAG, "-->includedServices size:"
                    + gattService.getIncludedServices().size());
            Log.e(TAG, "-->service uuid:" + gattService.getUuid());
            // -----Characteristics的字段信�?----//
            List<BluetoothGattCharacteristic> gattCharacteristics = gattService
                    .getCharacteristics();
            for (final BluetoothGattCharacteristic gattCharacteristic : gattCharacteristics) {
                Log.e(TAG, "---->char uuid:" + gattCharacteristic.getUuid());
                int permission = gattCharacteristic.getPermissions();
                Log.e(TAG,
                        "---->char permission:"
                                + Utils.getCharPermission(permission));
                int property = gattCharacteristic.getProperties();
                Log.e(TAG,
                        "---->char property:"
                                + Utils.getCharPropertie(property));
                byte[] data = gattCharacteristic.getValue();
                if (data != null && data.length > 0) {
                    Log.e(TAG, "---->char value:" + new String(data));
                }
                if (gattCharacteristic.getUuid().toString().equals(UUID_CHAR5)) {
                    gattCharacteristic_char5 = gattCharacteristic;
                }
                if (gattCharacteristic.getUuid().toString().equals(UUID_CHAR6)) {
                    // 把char1 保存起来�?以方便后面读写数据时使用
                    gattCharacteristic_char6 = gattCharacteristic;
                    Characteristic_cur = gattCharacteristic;
                    mBLE.setCharacteristicNotification(gattCharacteristic, true);
                    Log.i(TAG, "+++++++++UUID_CHAR6");
                }
                if (gattCharacteristic.getUuid().toString()
                        .equals(UUID_HERATRATE)) {
                    // 把heartrate 保存起来�?以方便后面读写数据时使用
                    gattCharacteristic_heartrate = gattCharacteristic;
                    Characteristic_cur = gattCharacteristic;
                    // 接受Characteristic被写的�?�?收到蓝牙模块的数据后会触发mOnDataAvailable.onCharacteristicWrite()
                    mBLE.setCharacteristicNotification(gattCharacteristic, true);
                    Log.i(TAG, "+++++++++UUID_HERATRATE");
                }
                if (gattCharacteristic.getUuid().toString()
                        .equals(UUID_KEY_DATA)) {
                    // 把heartrate 保存起来�?以方便后面读写数据时使用
                    gattCharacteristic_keydata = gattCharacteristic;
                    Characteristic_cur = gattCharacteristic;
                    // 接受Characteristic被写的�?�?收到蓝牙模块的数据后会触发mOnDataAvailable.onCharacteristicWrite()
                    mBLE.setCharacteristicNotification(gattCharacteristic, true);
                    Log.i(TAG, "+++++++++UUID_KEY_DATA");
                }
                if (gattCharacteristic.getUuid().toString()
                        .equals(UUID_TEMPERATURE)) {
                    // 把heartrate 保存起来�?以方便后面读写数据时使用
                    gattCharacteristic_temperature = gattCharacteristic;
                    Characteristic_cur = gattCharacteristic;
                    // 接受Characteristic被写的�?�?收到蓝牙模块的数据后会触发mOnDataAvailable.onCharacteristicWrite()
                    mBLE.setCharacteristicNotification(gattCharacteristic, true);
                    Log.i(TAG, "+++++++++UUID_TEMPERATURE");
                }
                if (gattCharacteristic.getUuid().toString()
                        .equals(UUID_0XFFA6)) {
                    // 把heartrate 保存起来�?以方便后面读写数据时使用
                    gattCharacteristic_0xffa6 = gattCharacteristic;
                    Characteristic_cur = gattCharacteristic;
                    Log.i(TAG, "+++++++++UUID_0XFFA6");
                }
                // -----Descriptors的字段信�?----//
                List<BluetoothGattDescriptor> gattDescriptors = gattCharacteristic
                        .getDescriptors();
                for (BluetoothGattDescriptor gattDescriptor : gattDescriptors) {
                    Log.e(TAG, "-------->desc uuid:" + gattDescriptor.getUuid());
                    int descPermission = gattDescriptor.getPermissions();
                    Log.e(TAG,
                            "-------->desc permission:"
                                    + Utils.getDescPermission(descPermission));
                    byte[] desData = gattDescriptor.getValue();
                    if (desData != null && desData.length > 0) {
                        Log.e(TAG, "-------->desc value:" + new String(desData));
                    }
                }
            }
        }//

        Intent intent = new Intent();
        intent.setClass(getActivity(), AmoComActivity.class);
        intent.putExtra("mac_addr", bluetoothAddress);
        intent.putExtra("char_uuid", Characteristic_cur.getUuid().toString());
        startActivityForResult(intent, REQUEST_CODE);

    }

    static public void writeChar1() {
        byte[] writeValue = new byte[1];
        Log.i(TAG, "gattCharacteristic_char1 = " + gattCharacteristic_char1);
        if (gattCharacteristic_char1 != null) {
            writeValue[0] = writeValue_char1++;
            Log.i(TAG, "gattCharacteristic_char1.setValue writeValue[0] ="
                    + writeValue[0]);
            boolean bRet = gattCharacteristic_char1.setValue(writeValue);
            mBLE.writeCharacteristic(gattCharacteristic_char1);
        }
    }

    static public void writeChar6(String string) {
        // byte[] writeValue = new byte[1];
        Log.i(TAG, "gattCharacteristic_char6 = " + gattCharacteristic_char6);
        if (gattCharacteristic_char6 != null) {
            // writeValue[0] = writeValue_char1++;
            // Log.i(TAG, "gattCharacteristic_char6.setValue writeValue[0] =" +
            // writeValue[0]);
            // byte[] writebyte = new byte[4];

            boolean bRet = gattCharacteristic_char6.setValue(string.getBytes());
            mBLE.writeCharacteristic(gattCharacteristic_char6);
        }
    }

    static public void read_char1() {
        byte[] writeValue = new byte[1];
        Log.i(TAG, "readCharacteristic = ");
        if (gattCharacteristic_char1 != null) {
            mBLE.readCharacteristic(gattCharacteristic_char1);
        }
    }

    static public void read_uuid_0xffa6() {
        Log.i(TAG, "readCharacteristic = ");
        if (gattCharacteristic_0xffa6 != null) {
            mBLE.readCharacteristic(gattCharacteristic_0xffa6);
        }
    }

    public class MyThread extends Thread {
        public void run() {
            while (!Thread.currentThread().isInterrupted()) {

                Message msg = new Message();
                msg.what = REFRESH;
                mHandler.sendMessage(msg);
                try {
                    Thread.sleep(200);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }


}
