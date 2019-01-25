package mobi.dzs.android.BluetoothSPP;

import android.app.Activity;
import android.bluetooth.*;
import android.content.Intent;
import android.content.SharedPreferences;
import android.widget.Toast;
import java.io.*;
import java.util.UUID;
import mobi.dzs.android.util.sysinfo;

public class BaseActivity extends Activity
{

	protected static final byte IO_MODE_CHAR = 0;
	protected static final byte IO_MODE_HEX = 1;
	protected static final byte REQUEST_DISCOVERY = 1;
	protected static final byte REQUEST_ENABLE = 2;
	protected static final byte REQUEST_KEYBOARD = 3;
	private static boolean mbConectOk = false;
	private static BluetoothSocket mbsSocket = null;
	private static InputStream misIn = null;
	private static OutputStream mosOut = null;
	protected byte mInputMode;
	protected byte mOutputMode;
	protected String msBluetoothMAC;

	public BaseActivity()
	{
		msBluetoothMAC = null;
		mInputMode = 0;
		mOutputMode = 0;
	}

	protected final void InitLoad()
	{
		mInputMode = (byte)getIntData("InputMode");
		mOutputMode = (byte)getIntData("OutputMode");
		msBluetoothMAC = getStrData("BluetoothMAC");
	}

	protected int ReceiveData(byte bytebuf[])
	{	
		if (!mbConectOk)
			return -2;
		try
		{
			return misIn.read(bytebuf);
		}
		catch (IOException ioexception)
		{
			terminateConnect();
			return -3;
		}
	}

	protected int SendData(byte bytebuf[])
	{
		int bytelength;
		if (mbConectOk)
		{
			try
			{
				mosOut.write(bytebuf);
				bytelength = bytebuf.length;
			}
			catch (IOException ioexception)
			{
				terminateConnect();
				bytelength = -3;
			}
		}
		else
			bytelength = -2;
		return bytelength;
	}

	protected boolean createBluetoothConnect()
	{
		BluetoothDevice bluetoothdevice;
		if (mbConectOk)
		{
			try
			{
				misIn.close();
				mosOut.close();
				mbsSocket.close();
			}
			catch (IOException ioexception1)
			{
				misIn = null;
				mosOut = null;
				mbsSocket = null;
				mbConectOk = false;
			}
		}
		try
		{
			bluetoothdevice = BluetoothAdapter.getDefaultAdapter().getRemoteDevice(msBluetoothMAC);
			mbsSocket = bluetoothdevice.createRfcommSocketToServiceRecord(UUID.fromString("00001101-0000-1000-8000-00805F9B34FB"));
			mbsSocket.connect();
			saveData("BluetoothMAC", msBluetoothMAC);
			mosOut = mbsSocket.getOutputStream();
			misIn = mbsSocket.getInputStream();
			mbConectOk = true;
			return true;
		}
		catch (IOException ioexception)
		{
			msBluetoothMAC = null;
			saveData("BluetoothMAC", ((String) (null)));
			mbConectOk = false;
			return false;
		}
	}

	protected int getIntData(String s)
	{
		return getSharedPreferences((new sysinfo(this)).getPackageName(), 0).getInt(s, 0);
	}

	protected String getStrData(String s)
	{
		return getSharedPreferences((new sysinfo(this)).getPackageName(), 0).getString(s, null);
	}

	protected boolean isConnect()
	{
		return mbConectOk;
	}

	protected void openButetooth()
	{
		Intent intent = new Intent("android.bluetooth.adapter.action.REQUEST_ENABLE");
		Toast.makeText(this, getString(R.string.msg_actDiscovery_Bluetooth_Open_Fail), 0).show();
		startActivityForResult(intent, 2);
	}

	protected String readBluetoothMAC()
	{
		return getSharedPreferences((new sysinfo(this)).getPackageName(), 0).getString("BluetoothMAC", null);
	}

	protected void saveBluetoothMAC(String s)
	{
		android.content.SharedPreferences.Editor editor = getSharedPreferences((new sysinfo(this)).getPackageName(), 0).edit();
		editor.putString("BluetoothMAC", s);
		editor.commit();
	}

	protected void saveData(String s, int i)
	{
		android.content.SharedPreferences.Editor editor = getSharedPreferences((new sysinfo(this)).getPackageName(), 0).edit();
		editor.putInt(s, i);
		editor.commit();
	}

	protected void saveData(String s, String s1)
	{
		android.content.SharedPreferences.Editor editor = getSharedPreferences((new sysinfo(this)).getPackageName(), 0).edit();
		editor.putString(s, s1);
		editor.commit();
	}

	protected void showBluetootchDiscovery()
	{
		Intent intent = new Intent(this, actDiscovery.class);
		Toast.makeText(this, getString(R.string.msg_actDiscovery_select_device), 0).show();
		startActivityForResult(intent, 1);
	}

	protected void terminateConnect()
	{
		if (mbConectOk)
		{
			try
			{
				mbConectOk = false;
				mbsSocket.close();
				misIn.close();
				mosOut.close();
			}
			catch (IOException localIOException)
			{
		        misIn = null;
		        mosOut = null;
		        mbsSocket = null;
			}
		}
	}
}
