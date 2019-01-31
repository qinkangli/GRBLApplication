package com.example.grblapplication;

import android.bluetooth.BluetoothSocket;
import android.support.v7.app.AppCompatActivity;
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
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;


import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.List;
import java.util.ArrayList;
import java.util.Set;
import java.util.UUID;


public class DeviceListActivity extends AppCompatActivity implements View.OnClickListener {

    private Button btn_search_devices;
    private Button btn_close_devices;
    private ListView list_bonded_devices;
    private List<BluetoothDevice> bondedDevicesList;
    private MyListAdapter mBondedAdapter;
    private ListView list_search_devices;
    private List<BluetoothDevice> searchDevicesList;
    private MyListAdapter mSearchAdapter;
    private BluetoothAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setTitle("选取连接设备");
        setContentView(R.layout.activity_device_list);

        btn_search_devices = (Button) findViewById(R.id.btn_search_devices);
        btn_search_devices.setOnClickListener(this);
        btn_close_devices = (Button) findViewById(R.id.btn_close_devices);
        btn_close_devices.setOnClickListener(this);

        //已配对设备列表
        list_bonded_devices = (ListView) findViewById(R.id.list_bonded_devices);
        bondedDevicesList = new ArrayList<BluetoothDevice>();
        //设置适配器
        mBondedAdapter = new MyListAdapter(this, bondedDevicesList);
        list_bonded_devices.setAdapter(mBondedAdapter);

        list_bonded_devices.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(DeviceListActivity.this,"已经配对",Toast.LENGTH_SHORT).show();
                try{
                    //连接
                }
                catch (Exception e){
                    e.printStackTrace();
                }
            }
        });

        //搜索到的设备列表
        list_search_devices = (ListView) findViewById(R.id.list_search_devices);
        searchDevicesList = new ArrayList<BluetoothDevice>();
        mSearchAdapter = new MyListAdapter(this, searchDevicesList);
        list_search_devices.setAdapter(mSearchAdapter);
        list_search_devices.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                BluetoothDevice device = searchDevicesList.get(position);
                try {
                    // 配对
                    Method createBondMethod = BluetoothDevice.class.getMethod("createBond");
                    createBondMethod.invoke(device);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });


        //MainActivity已经开启蓝牙，所以获取所有已经绑定的蓝牙设备
        getBondedDevices();

        // 注册用以接收到已搜索到的蓝牙设备的receiver
        IntentFilter mFilter = new IntentFilter(BluetoothDevice.ACTION_FOUND);
        mFilter.addAction(BluetoothDevice.ACTION_FOUND);
        mFilter.addAction(BluetoothDevice.ACTION_BOND_STATE_CHANGED);
        mFilter.addAction(BluetoothAdapter.ACTION_SCAN_MODE_CHANGED);
        mFilter.addAction(BluetoothAdapter.ACTION_STATE_CHANGED);
        mFilter.addAction(BluetoothAdapter.ACTION_DISCOVERY_FINISHED);
        // 注册广播接收器，接收并处理搜索结果
        registerReceiver(receiver, mFilter);
    }


    /**
     *  获取所有已经绑定的蓝牙设备
     */
    private void getBondedDevices() {
        bondedDevicesList.clear();
        adapter = BluetoothAdapter.getDefaultAdapter();
        Set<BluetoothDevice> devices = adapter.getBondedDevices();
        bondedDevicesList.addAll(devices);
        //为listview动态设置高度（有多少条目就显示多少条目）
        setListViewHeight(bondedDevicesList.size());
        mBondedAdapter.notifyDataSetChanged();
    }

    private BroadcastReceiver receiver = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            // 获得已经搜索到的蓝牙设备
            if (action.equals(BluetoothDevice.ACTION_FOUND)) {
                BluetoothDevice device = intent
                        .getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                // 搜索到的不是已经绑定的蓝牙设备
                if (device.getBondState() != BluetoothDevice.BOND_BONDED) {
                    // 防止重复添加
                    if (searchDevicesList.indexOf(device) == -1)
                        searchDevicesList.add(device);
                    //devicesList.add("未配对 | "+device.getName() + "（"  + device.getAddress()+"）");
                    mSearchAdapter.notifyDataSetChanged();
                }
                // 搜索完成
            } else if (action
                    .equals(BluetoothAdapter.ACTION_DISCOVERY_FINISHED)) {
                setProgressBarIndeterminateVisibility(false);
                setTitle("搜索完成");
            } else if (action.equals(BluetoothDevice.ACTION_BOND_STATE_CHANGED)) {
                // 状态改变的广播
                BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                String name = device.getName();
                if (device.getName().equalsIgnoreCase(name)) {
                    int connectState = device.getBondState();
                    switch (connectState) {
                        case BluetoothDevice.BOND_NONE:  //10
                            Toast.makeText(DeviceListActivity.this, "取消配对："+device.getName(), Toast.LENGTH_SHORT).show();
                            break;
                        case BluetoothDevice.BOND_BONDING:  //11
                            Toast.makeText(DeviceListActivity.this, "正在配对："+device.getName(), Toast.LENGTH_SHORT).show();
                            break;
                        case BluetoothDevice.BOND_BONDED:   //12
                            Toast.makeText(DeviceListActivity.this, "完成配对："+device.getName(), Toast.LENGTH_SHORT).show();
                            getBondedDevices();
                            try {
                                // 连接
                                connect(device);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                            break;
                    }
                }
            }
        }
    };

    //蓝牙设备的连接（客户端）
    private void connect(BluetoothDevice device) {
        // 固定的UUID
        final String SPP_UUID = "00001101-0000-1000-8000-00805F9B34FB";
        UUID uuid = UUID.fromString(SPP_UUID);
        try {
            BluetoothSocket socket = device.createRfcommSocketToServiceRecord(uuid);
            socket.connect();
//			OutputStream outputStream = socket.getOutputStream();
//	        InputStream inputStream = socket.getInputStream();
//	        outputStream.write("StartOnNet\n".getBytes());
//	        outputStream.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_search_devices:
                setProgressBarIndeterminateVisibility(true);
                setTitle("正在扫描....");
                searchDevicesList.clear();
                mSearchAdapter.notifyDataSetChanged();
                // 如果正在搜索，就先取消搜索
                if (!adapter.isDiscovering()) {
                    adapter.cancelDiscovery();
                }
                // 开始搜索蓝牙设备,搜索到的蓝牙设备通过广播返回
                adapter.startDiscovery();
                break;
            case R.id.btn_close_devices:
                // 如果正在搜索，就先取消搜索
                if (adapter.isDiscovering()) {
                    adapter.cancelDiscovery();
                }
                break;
        }

    }

    //为listview动态设置高度（有多少条目就显示多少条目）
    private void setListViewHeight(int count) {
        if (mBondedAdapter==null) {
            return ;
        }
        int totalHeight = 0;
        for (int i = 0; i < count; i++) {
            View listItem = mBondedAdapter.getView(i, null, list_bonded_devices);
            listItem.measure(0, 0);
            totalHeight += listItem.getMeasuredHeight();
        }
        ViewGroup.LayoutParams params = list_bonded_devices.getLayoutParams();
        params.height = totalHeight;
        list_bonded_devices.setLayoutParams(params);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //解除注册
        unregisterReceiver(receiver);
    }

    @Override
    protected void onResume() {
        super.onResume();
        getBondedDevices();
    }

}



