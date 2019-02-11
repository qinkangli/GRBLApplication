package com.example.grblapplication;

import android.app.Activity;

import android.app.ProgressDialog;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothAdapter;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import android.widget.AdapterView;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;



import android.widget.ListView;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.List;
import java.util.ArrayList;
import java.util.Set;

public class DeviceListActivity extends Activity {

    private BluetoothAdapter mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
    private static final String TAG = "DeviceListActivity";
    public static String EXTRA_DEVICE_ADDRESS = "设备地址";

    private List<BluetoothDevice> mBlueList = new ArrayList<>();

    private ListView lisetView;
    private TextView textView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_device_list);


        lisetView = (ListView) findViewById(R.id.list_view);
        textView = (TextView) findViewById(R.id.textView);
        Button searchButton = (Button) findViewById(R.id.btn_search_devices);
        Button cancelButton = (Button) findViewById(R.id.btn_close_devices);

        lisetView.setOnItemClickListener(mDeviceClickListener);

        startDiscovery();//搜索一次
        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(DeviceListActivity.this, "搜索中……", Toast.LENGTH_SHORT).show();
                startDiscovery();

            }
        });

        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mBluetoothAdapter.isDiscovering()) {
                    Toast.makeText(DeviceListActivity.this, "取消搜索中……", Toast.LENGTH_SHORT).show();
                    mBluetoothAdapter.cancelDiscovery();

                }
                DeviceListActivity.this.finish();
            }
        });


        Set<BluetoothDevice> pairedDevices = mBluetoothAdapter.getBondedDevices();
        if (pairedDevices.size() > 0) {
            for (BluetoothDevice device : pairedDevices) {// pairedDevices里面的内容
                mBlueList.add(device);
                MyAdapter madapter = new MyAdapter(DeviceListActivity.this, mBlueList);
                lisetView.setAdapter(madapter);
                madapter.notifyDataSetChanged();// 设置自动更新，当有设备添加的时候显示在屏幕上面
                textView.setText("可选设备：" + mBlueList.size() + "个\u3000\u3000本机蓝牙地址：" + getBluetoothAddress());
                Log.e(TAG, "onReceive: " + (device.getName() + ":" + device.getAddress() + " ：" + "m" + "\n"));
            }
        }


    }


    /*
    * 携带蓝牙地址放回主界面
    * paramAdapterView这个参数为适配器的指针
    * paramView在Item位置中的视图空间，就是得到Item里面的控件，这里是为了获取MAC地址，并放回给MAINActivity
    * */
    private AdapterView.OnItemClickListener mDeviceClickListener = new AdapterView.OnItemClickListener() {
        public void onItemClick(AdapterView<?> paramAdapterView, View paramView, int paramInt, long paramLong) {

            String str1 = ((TextView) paramView.findViewById(R.id.device_address)).getText().toString();
            Toast.makeText(DeviceListActivity.this,str1,Toast.LENGTH_SHORT).show();
            String str2 = str1.substring(-17 + str1.length());//截取字符串，MAC地址17位，从第零开始
            DeviceListActivity.this.ReturnData(str2);
            Log.e(TAG, "返回的MAC地址:"+str2);
        }
    };

    /**
     * 注册异步搜索蓝牙设备的广播
     */
    private void startDiscovery() {
        // 找到设备的广播
        IntentFilter filter = new IntentFilter(BluetoothDevice.ACTION_FOUND);
        // 注册广播
        registerReceiver(receiver, filter);
        // 搜索完成的广播
        IntentFilter filter1 = new IntentFilter(BluetoothAdapter.ACTION_DISCOVERY_FINISHED);
        // 注册广播
        registerReceiver(receiver, filter1);
        Log.e(TAG, "startDiscovery: 注册广播");
        startScanBluth();
    }

    /**
     * 广播接收器
     */
    private final BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            // 收到的广播类型
            String action = intent.getAction();

            // 发现设备的广播
            if (BluetoothDevice.ACTION_FOUND.equals(action)) {
                // 从intent中获取设备
                BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                // 没否配对
                if (device.getBondState() != BluetoothDevice.BOND_BONDED) {
                    if (!mBlueList.contains(device)) {
                        mBlueList.add(device);
                    }

                    MyAdapter adapter = new MyAdapter(DeviceListActivity.this, mBlueList);
                    lisetView.setAdapter(adapter);
                    textView.setText("可选设备：" + mBlueList.size() + "个\u3000\u3000本机蓝牙地址：" + getBluetoothAddress());
                    Log.e(TAG, "onReceive: " + mBlueList.size());
                    Log.e(TAG, "onReceive: " + (device.getName() + ":" + device.getAddress() + " ：" + "m" + "\n"));
                }

                // 搜索完成
            } else if (BluetoothAdapter.ACTION_DISCOVERY_FINISHED.equals(action)) {
                // 关闭进度条
                progressDialog.dismiss();
                Log.e(TAG, "onReceive: 搜索完成");
            }
        }
    };


    private ProgressDialog progressDialog;

    /**
     * 搜索蓝牙的方法
     */
    private void startScanBluth() {
        // 判断是否在搜索,如果在搜索，就取消搜索
        if (mBluetoothAdapter.isDiscovering()) {
            mBluetoothAdapter.cancelDiscovery();
        }
        // 开始搜索
        mBluetoothAdapter.startDiscovery();
        if (progressDialog == null) {
            progressDialog = new ProgressDialog(this);
        }
        progressDialog.setMessage("正在搜索，请稍后！");
        progressDialog.show();
    }


    /**
     * 获取本机蓝牙地址
     */
    private String getBluetoothAddress() {
        try {
            BluetoothAdapter bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
            Field field = bluetoothAdapter.getClass().getDeclaredField("mService");
            // 参数值为true，禁用访问控制检查
            field.setAccessible(true);
            Object bluetoothManagerService = field.get(bluetoothAdapter);
            if (bluetoothManagerService == null) {
                return null;
            }
            Method method = bluetoothManagerService.getClass().getMethod("getAddress");
            Object address = method.invoke(bluetoothManagerService);
            if (address != null && address instanceof String) {
                return (String) address;
            } else {
                return null;
            }
            //抛一个总异常省的一堆代码...
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }




    private void ReturnData(String paramString) {
        Intent localIntent = new Intent();
        localIntent.putExtra(EXTRA_DEVICE_ADDRESS, paramString);//返回的MAC地址
        setResult(-1, localIntent);
        finish();//结束当前活动
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (receiver != null) {
            this.mBluetoothAdapter.cancelDiscovery();
            unregisterReceiver(this.receiver);
        }
    }


}



