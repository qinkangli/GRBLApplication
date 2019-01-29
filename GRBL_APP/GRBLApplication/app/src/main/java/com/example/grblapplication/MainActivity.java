package com.example.grblapplication;

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
import java.util.ArrayList;
import java.util.Set;


public class MainActivity extends AppCompatActivity implements View.OnClickListener {


    private BluetoothAdapter mBluetoothAdapter; // 本机蓝牙适配器对象
    private static String TAG = "Bluetooth_State";


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

    private final int REQUEST_OPEN_BT_CODE = 1;
    private final int REQUEST_DISCOVERY_BT_CODE = 2;
    @Override
    public void onClick(View v) {
        // TODO Auto-generated method stub
        //真机调试时看不到LogCat，下面为解决方法
        //在拨号界面输入 *#*#2846579#*#*
        //记得重启哈
        boolean wasBtOpened = mBluetoothAdapter.isEnabled(); // 是否已经打开
        switch (v.getId())
        {
            case R.id.AddEquipment:
                Log.e(TAG, " ## click btOpenBySystem ##");
                if (!wasBtOpened) //未打开蓝牙，才需要打开蓝牙
                {
                    Intent intent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                    startActivityForResult(intent, REQUEST_OPEN_BT_CODE);
                }


                break;



        }
    }


}


