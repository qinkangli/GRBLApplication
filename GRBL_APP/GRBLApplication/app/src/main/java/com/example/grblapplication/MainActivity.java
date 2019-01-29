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

import android.widget.Button;



import java.util.ArrayList;
import java.util.Set;


public class MainActivity extends AppCompatActivity {




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);//隐藏状态栏
        getSupportActionBar().hide();//隐藏标题栏
        setContentView(R.layout.activity_main);

        Button FindEquipmentButton = (Button) findViewById(R.id.AddEquipment);
        //添加蓝牙设备
        FindEquipmentButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Toast.makeText(MainActivity.this, "添加设备", Toast.LENGTH_SHORT).show();
                Intent intent =new Intent(MainActivity.this,DeviceListActivity.class);
                startActivity(intent);
            }
        });

    }

}


