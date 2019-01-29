package com.example.grblapplication;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;

import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.content.Intent;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2019/1/29 0029.
 */

public class BlueToothController {
    private BluetoothAdapter mAdapter;
    public BlueToothController(){
        mAdapter = BluetoothAdapter.getDefaultAdapter();
    }

    /**
     * 打开蓝牙
     * */
    public void turnOnBlueTooth(Activity activity,int requestCode){
        Intent intent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
        activity.startActivityForResult(intent,requestCode);
    }

    /**
     * 查找设备
     */
    public void findDevice() {
        assert (mAdapter != null);
        mAdapter.startDiscovery();
    }

    /**
     * 获取已绑定设备
     */
    public List<BluetoothDevice> getBondedDeviceList(){
        return new ArrayList<>(mAdapter.getBondedDevices());
    }





}
