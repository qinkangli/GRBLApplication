package com.example.grblapplication;


import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;


import android.view.WindowManager;
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
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);//隐藏状态栏
        getSupportActionBar().hide();//隐藏标题栏
        setContentView(R.layout.activity_main);
    }

}


