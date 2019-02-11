package com.example.grblapplication;

import android.content.SharedPreferences;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.WindowManager;
import android.widget.CheckedTextView;
import android.widget.CompoundButton;
import android.widget.EditText;
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
import java.io.OutputStream;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.UUID;


public class MainActivity extends AppCompatActivity implements View.OnClickListener {


    private BluetoothAdapter mBluetoothAdapter; // 本机蓝牙适配器对象
    private BluetoothDevice mBluetoothDevice;

    private BluetoothSocket Bluetoothsocket = null;//客户端


    //一些需要全局用到的控件
    private Button FindEquipmentButton;
    private TextView SendTextView;
    private EditText editText;
    private CheckedTextView Hex;
    private CheckedTextView SensorHigh;
    private CheckedTextView SensorLow;

    private TextView DeviceName;//显示连接设备的名称及MAC地址的TestView


    private InputStream is;
    private OutputStream os;


    private static String TAG = "MainActivity";



    static String[] sendstr;
    static String[] str;


    String EditStr=null;



    private int flag=0;
    boolean isConnect=false;






    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);//隐藏状态栏
        getSupportActionBar().hide();//隐藏标题栏
        setContentView(R.layout.activity_main);

        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter(); // 获得本机蓝牙适配器对象引用





        InitialViews();


    }

    private void InitialViews()
    {
        int initialBTState = mBluetoothAdapter.getState();

        if (mBluetoothAdapter == null)
        {
            toast("该设备不支持蓝牙功能！");
            finish();//直接退出
            return;
        }

        printBTState(initialBTState); // 显示初始时蓝牙状态


        FindEquipmentButton = (Button) findViewById(R.id.AddEquipment);
        Button SendFile= (Button) findViewById(R.id.Send);
        Button OpenFile= (Button) findViewById(R.id.OpenFile);
        Button Clear = (Button) findViewById(R.id.rec);

        DeviceName = (TextView) findViewById(R.id.device_name);//连接上的设备
        SendTextView = (TextView) findViewById(R.id.TextIn);
        editText = (EditText) findViewById(R.id.EditText);

        Hex= (CheckedTextView) findViewById(R.id.checkedTextView); //Hex复选框
        SensorHigh = (CheckedTextView) findViewById(R.id.SensorHigh);
        SensorLow = (CheckedTextView) findViewById(R.id.SensorLow);

        Button XUp = (Button) findViewById(R.id.XUp);
        Button XDown = (Button) findViewById(R.id.XDown);

        Button YUp = (Button) findViewById(R.id.YUp);
        Button YDown = (Button) findViewById(R.id.YDown);

        FindEquipmentButton.setOnClickListener(this);
        Hex.setOnClickListener(this);
        SensorHigh.setOnClickListener(this);
        SensorLow.setOnClickListener(this);
        Clear.setOnClickListener(this);
        SendFile.setOnClickListener(this);
        OpenFile.setOnClickListener(this);
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
            case R.id.AddEquipment://添加设备
                if(flag==0){
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
                }
                else{

                    DeviceName.setText("");
                    this.FindEquipmentButton.setText(getResources().getString(R.string.AddEquipment));
                    try {
                        this.Bluetoothsocket.close();
                        this.Bluetoothsocket = null;
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                    flag=0;

                }

                break;




            case R.id.XUp://X轴前进

                try {
                    os.write("G01 X+1\n".getBytes("utf-8"));
                } catch (IOException e) {
                    e.printStackTrace();
                }


                break;

            case R.id.XDown://X轴后退
                try {
                    os.write("G01 X-1\n".getBytes("utf-8"));
                } catch (IOException e) {
                    e.printStackTrace();
                }

                break;

            case R.id.YUp://Y轴前进
                try {
                    os.write("G01 Y+1\n".getBytes("utf-8"));
                } catch (IOException e) {
                    e.printStackTrace();
                }
                break;

            case R.id.YDown://Y轴后退
                if (isConnect == false){
                    toast("蓝牙设备未连接，请先连接设备！");

                }
                else{
                    try {
                        os.write("G01 Y-1\n".getBytes("utf-8"));
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }

                break;

            case R.id.Send://发送
                if(isConnect==false){
                    toast("蓝牙设备未连接，请先连接设备！");
                }
                else{
                    EditStr=editText.getText().toString();
                    SendTextView.setText(EditStr);

                    try{
                        os.write(EditStr.getBytes("gbk"));//发送内容串口助手实际上是将GBK转成utf-8串口助手
                    }catch (IOException e){
                        e.printStackTrace();
                    }


                }
                break;

            case R.id.rec://清空接收
                SendTextView.getText();
                SendTextView.setText("");
                break;

            case R.id.checkedTextView:
                Hex.toggle();

                break;

            case R.id.SensorHigh:
                SensorHigh.toggle();
                break;


            case R.id.SensorLow:
                SensorLow.toggle();

                break;
        }
    }


    @Override
    protected void onActivityResult(int paramInt1, int paramInt2, Intent paramIntent)
    {
        switch (paramInt1)
        {
            case 1:
                String str1= paramIntent.getExtras().getString(DeviceListActivity.EXTRA_DEVICE_ADDRESS);

                Log.e(TAG,"DeviceListActivity返回的MAC地址:"+str1);
                this.mBluetoothDevice = this.mBluetoothAdapter.getRemoteDevice(str1);//根据蓝牙地址获取远程蓝牙设备
                try{

                    Bluetoothsocket = mBluetoothDevice.createRfcommSocketToServiceRecord(UUID.fromString("00001101-0000-1000-8000-00805F9B34FB"));
                    this.Bluetoothsocket.connect();
                    DeviceName.setText(mBluetoothDevice.getName()+'\n'+mBluetoothDevice.getAddress());
                    Toast.makeText(this, "连接" + this.mBluetoothDevice.getName() + "成功！",Toast.LENGTH_SHORT).show();
                    flag=1;
                    isConnect=true;
                    os=Bluetoothsocket.getOutputStream();
                    FindEquipmentButton.setText(getResources().getString(R.string.DeleteEquipment));
                    if(os != null)
                    {

                        os.write("Hello world! ".getBytes("utf-8"));
                    }
                }
                catch (IOException e1){

                    System.out.println("IOException:"+e1);
                    isConnect=false;
                }



                break;

            case 2:
                break;

            default:
                break;

        }

    }



    private byte[] getHexBytes(String message) {
        int len = message.length() / 2;
        char[] chars = message.toCharArray();
        String[] hexStr = new String[len];
        byte[] bytes = new byte[len];
        for (int i = 0, j = 0; j < len; i += 2, j++) {
            hexStr[j] = "" + chars[i] + chars[i + 1];
            bytes[j] = (byte) Integer.parseInt(hexStr[j], 16);
        }
        return bytes;
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
        DeviceName.setText("");
        Toast.makeText(this, "线路已断开，请重新连接！", Toast.LENGTH_SHORT).show();
        try
        {
            this.bRun = false;
            this.is.close();
            this.Bluetoothsocket.close();
            this.Bluetoothsocket = null;
            this.bRun = false;
            this.FindEquipmentButton.setText(getResources().getString(R.string.AddEquipment));
            return;
        }
        catch (IOException localIOException)
        {
        }
    }


}


