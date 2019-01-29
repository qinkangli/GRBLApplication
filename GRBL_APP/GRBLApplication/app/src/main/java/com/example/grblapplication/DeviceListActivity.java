package com.example.grblapplication;

import android.app.Activity;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothAdapter;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;


import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;


import java.util.Set;
import java.util.ArrayList;


public class DeviceListActivity extends Activity {



    //本地蓝牙适配器
    private BluetoothAdapter mBluetoothAdapter;
    //用来存放搜到的蓝牙
    private Set<BluetoothDevice> mDevices;
    private ListView mListView;
    private ArrayList mList;
    private ArrayAdapter mArrayAdapter;
    private TextView mConnectedView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_device_list);



        Button CancelButton = (Button) findViewById(R.id.button_cancel);

        mListView = (ListView) findViewById(R.id.paired_devices);


        //获取蓝牙适配器
        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();

        IntentFilter filter=new IntentFilter(BluetoothDevice.ACTION_FOUND);
        registerReceiver(mReceiver,filter);
        IntentFilter filter2=new IntentFilter(BluetoothAdapter.ACTION_DISCOVERY_FINISHED);
        registerReceiver(mReceiver,filter2);



        // 判断设备是否支持蓝牙功能
        if (mBluetoothAdapter == null) {
            //设备不支持蓝牙功能
            Toast.makeText(this, "当前设备不支持蓝牙功能！", Toast.LENGTH_SHORT).show();
            return;
        }

        // 打开设备的蓝牙功能
        if (!mBluetoothAdapter.isEnabled()) {
            boolean enable = mBluetoothAdapter.enable(); //返回值表示 是否成功打开了蓝牙设备
            if (enable) {
                Toast.makeText(this, "打开蓝牙功能成功！", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "打开蓝牙功能失败，请到'系统设置'中手动开启蓝牙功能！", Toast.LENGTH_SHORT).show();
                return;
            }
        }




        CancelButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent =new Intent(DeviceListActivity.this,MainActivity.class);
                startActivity(intent);
            }
        });

    }

    public void onDestroy() {

        super.onDestroy();
        //解除注册
        unregisterReceiver(mReceiver);
        Log.e("destory","解除注册");
    }

    //定义广播
    private BroadcastReceiver mReceiver=new BroadcastReceiver(){
        @Override
        public void onReceive(Context context, Intent intent) {
            String action=intent.getAction();
            Log.e("ywq", action);

            if(action.equals(BluetoothDevice.ACTION_FOUND))
            {
                BluetoothDevice device=intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                if(device.getBondState()==BluetoothDevice.BOND_BONDED)
                {

                    // Add the name and address to an array adapter to show in a ListView
                    mArrayAdapter.add(device.getName() + "\n" + device.getAddress());
                    mListView.setAdapter(mArrayAdapter);

                }
                else if(device.getBondState()!=BluetoothDevice.BOND_BONDED)
                {
                    mArrayAdapter.add("\n"+device.getName()+"==>"+device.getAddress()+"\n");
                    mListView.setAdapter(mArrayAdapter);
                }


            }
        }
    };

    }
