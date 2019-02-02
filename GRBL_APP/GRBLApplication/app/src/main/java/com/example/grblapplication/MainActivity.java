package com.example.grblapplication;

import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.WindowManager;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;
import android.support.v7.widget.SwitchCompat;
import android.bluetooth.BluetoothAdapter;
import android.content.BroadcastReceiver;
import android.bluetooth.BluetoothSocket;
import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.Intent;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Button;
import android.util.Log;
import android.view.View;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.UUID;


public class MainActivity extends AppCompatActivity implements View.OnClickListener {


    private BluetoothAdapter mBluetoothAdapter; // 本机蓝牙适配器对象
    private BluetoothDevice mBluetoothDevice;

    BluetoothSocket Bluetoothsocket = null;

    private Button btnadd;


    private static String TAG = "MainActivity";
    private ArrayList<String> mPairedDevicesArrayAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);//隐藏状态栏
        getSupportActionBar().hide();//隐藏标题栏
        setContentView(R.layout.activity_main);

        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter(); // 获得本机蓝牙适配器对象引用

        if (mBluetoothAdapter == null)
        {
            toast("该设备不支持蓝牙功能！");
            //finish();//直接退出
            return;
        }


        int initialBTState = mBluetoothAdapter.getState();
        printBTState(initialBTState); // 初始时蓝牙状态

        InitialViews();


    }

    private void InitialViews()
    {


        Button FindEquipmentButton = (Button) findViewById(R.id.AddEquipment);
        Button XUp = (Button) findViewById(R.id.XUp);
        Button XDown = (Button) findViewById(R.id.XDown);

        Button YUp = (Button) findViewById(R.id.YUp);
        Button YDown = (Button) findViewById(R.id.YDown);

        FindEquipmentButton.setOnClickListener(this);
        XUp.setOnClickListener(this);
        XDown.setOnClickListener(this);
        YUp.setOnClickListener(this);
        YDown.setOnClickListener(this);



    }




    private void printBTState(int btState)
    {
        switch (btState)
        {
            case BluetoothAdapter.STATE_OFF:
                toast("蓝牙状态:已关闭");
                Log.v(TAG, "BT State ： BluetoothAdapter.STATE_OFF ###");
                break;
            case BluetoothAdapter.STATE_TURNING_OFF:
                toast("蓝牙状态:正在关闭");
                Log.v(TAG, "BT State :  BluetoothAdapter.STATE_TURNING_OFF ###");
                break;
            case BluetoothAdapter.STATE_TURNING_ON:
                toast("蓝牙状态:正在打开");
                Log.v(TAG, "BT State ：BluetoothAdapter.STATE_TURNING_ON ###");
                break;
            case BluetoothAdapter.STATE_ON:
                toast("蓝牙状态:已打开");
                Log.v(TAG, "BT State ：BluetoothAdapter.STATE_ON ###");
                break;
            default:
                break;
        }
    }


    private void toast(String str)
    {
        Toast.makeText(MainActivity.this, str, Toast.LENGTH_SHORT).show();
    }


    @Override
    public void onClick(View v) {
        // TODO Auto-generated method stub
        //真机调试时看不到LogCat，下面为解决方法
        //在拨号界面输入 *#*#2846579#*#*
        //记得重启哈
        boolean wasBtOpened = mBluetoothAdapter.isEnabled(); // 是否已经打开
        if(!wasBtOpened){
            mBluetoothAdapter.enable();//强制打开
        }

        switch (v.getId())
        {
            case R.id.AddEquipment:
                Log.e(TAG, " ## click btOpenBySystem ##");
                if(wasBtOpened)//蓝牙已开启，进入DeviceListActivity
                {
                    // 设置蓝牙可见性，最多300秒
                    Intent intent = new Intent(MainActivity.this,DeviceListActivity.class);
                    intent.setAction(BluetoothAdapter.ACTION_REQUEST_DISCOVERABLE);
                    intent.putExtra(BluetoothAdapter.EXTRA_DISCOVERABLE_DURATION, 300);
                    MainActivity.this.startActivityForResult(intent, 1);
                }
                else{}
                break;



        }
    }

    private InputStream is;
    @Override
    protected void onActivityResult(int paramInt1, int paramInt2, Intent paramIntent)
    {
        switch (paramInt1)
        {
            case 1:
                String str1= paramIntent.getExtras().getString(DeviceListActivity.EXTRA_DEVICE_ADDRESS);
                Log.e(TAG,"DeviceListActivity返回的MAC地址:"+str1);
                this.mBluetoothDevice = this.mBluetoothAdapter.getRemoteDevice(str1);//根据蓝牙地址获取远程蓝牙设备


                break;

            case 2:
                break;

            default:
                break;

        }
/*
        String str1 = paramIntent.getExtras().getString(DeviceListActivity.EXTRA_DEVICE_ADDRESS);

        Log.e(TAG,str1);


        //根据蓝牙地址获取远程蓝牙设备
        this.mBluetoothDevice = this.mBluetoothAdapter.getRemoteDevice(str1);
        this.mPairedDevicesArrayAdapter.add(this.mBluetoothDevice.getName() + "\n" + this.mBluetoothDevice.getAddress());


        Log.e(TAG, mBluetoothDevice.getName() + ":" + mBluetoothDevice.getAddress() );*/

    }

    private final BroadcastReceiver mReceiver = new BroadcastReceiver()
    {
        public void onReceive(Context paramContext, Intent paramIntent)
        {
            if ("android.bluetooth.device.action.ACL_DISCONNECTED".equals(paramIntent.getAction()))
                MainActivity.this.disconnect();
        }
    };
    boolean bRun = true;
    public void disconnect()
    {
        unregisterReceiver(this.mReceiver);
        SharedPreferences.Editor localEditor = getSharedPreferences("Add", 0).edit();
        localEditor.clear();
        localEditor.commit();
        this.mPairedDevicesArrayAdapter.clear();
        Toast.makeText(this, "线路已断开，请重新连接！", Toast.LENGTH_SHORT).show();
        try
        {
            this.bRun = false;
           // this.is.close();
            this.Bluetoothsocket.close();
            this.Bluetoothsocket = null;
            this.bRun = false;
            this.btnadd.setText(getResources().getString(R.string.AddEquipment));
            return;
        }
        catch (IOException localIOException)
        {
        }
    }


}


