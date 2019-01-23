package com.example.grblapplication;


import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;


import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;
import android.support.v7.widget.SwitchCompat;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Intent;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Set;


public class MainActivity extends AppCompatActivity {

    //本地蓝牙适配器
    private BluetoothAdapter mBluetoothAdapter;
    //用来存放搜到的蓝牙
    private Set<BluetoothDevice> mDevices;
    private ListView mListView;
    private ArrayList mList;
    private ArrayAdapter mAdapter;
    private TextView mConnectedView;

    private Switch aSwitch;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        aSwitch = (Switch) findViewById(R.id.switch1);

        //设置Switch事件监听
        aSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked){
                    //自定义方法初始化UI控件
                    initView();
                    initData();
                    if (!mBluetoothAdapter.isEnabled()) {
                        //调用下列蓝牙ACTION_REQUEST_ENABLE的意图
                        Intent turnOn =new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                        startActivityForResult(turnOn,0);
                    }else{
                        Toast.makeText(MainActivity.this, "Already on", Toast.LENGTH_LONG).show();
                    }
                }else {
                   /* if (mBluetoothAdapter.isEnabled()){
                        //未关闭
                        Toast.makeText(MainActivity.this,"蓝牙关闭中……",Toast.LENGTH_SHORT).show();
                        mBluetoothAdapter.disable();
                        mList.clear();
                        mConnectedView.setVisibility(View.INVISIBLE);
                        mAdapter.notifyDataSetChanged();
                    }*/
                }
            }
        });
    }
    private void initView() {
        // mListView = (ListView) findViewById(R.id.lv_bluetooth_name);
        // mConnectedView 指的是已被连接的（可用的蓝牙）
        mConnectedView = (TextView) findViewById(R.id.tv_bluetooth_connected);
    }
    private void initData() {
        //实例化蓝牙适配器
        mBluetoothAdapter =BluetoothAdapter.getDefaultAdapter();
    }

}


