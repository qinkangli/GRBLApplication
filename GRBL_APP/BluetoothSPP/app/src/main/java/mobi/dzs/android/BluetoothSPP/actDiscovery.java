package mobi.dzs.android.BluetoothSPP;

import android.app.ListActivity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.*;
import android.os.*;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import java.util.ArrayList;
import java.util.List;

public class actDiscovery extends ListActivity
{
	private List _CLASS;
	private List _RSSI;
	private List _devices;
	private volatile boolean _discoveryFinished;
	private BroadcastReceiver _discoveryReceiver;
	private Runnable _discoveryWorkder;
	private BroadcastReceiver _foundReceiver;
	private Handler _handler;
	private BluetoothAdapter mBT;

	public actDiscovery()
	{
		_handler = new Handler();
		mBT = BluetoothAdapter.getDefaultAdapter();
		_devices = new ArrayList();
		_RSSI = new ArrayList();
		_CLASS = new ArrayList();
		_discoveryWorkder = new Runnable() {

			public void run()
			{
				mBT.startDiscovery();
				Log.d("EF-BTBee", ">>Starting Discovery");
				do
				{
					if (_discoveryFinished)
					{
						Log.d("EF-BTBee", ">>Finished");
						return;
					}
					try
					{
						Thread.sleep(100L);
					}
					catch (InterruptedException interruptedexception) { }
				} while (true);
			}
		};
		_foundReceiver = new BroadcastReceiver() {

			public void onReceive(Context context, Intent intent)
			{
				BluetoothDevice bluetoothdevice = (BluetoothDevice)intent.getParcelableExtra("android.bluetooth.device.extra.DEVICE");
				_devices.add(bluetoothdevice);
				Bundle bundle = intent.getExtras();
				_RSSI.add(String.valueOf(bundle.get("android.bluetooth.device.extra.RSSI")));
				_CLASS.add(String.valueOf(bundle.get("android.bluetooth.device.extra.CLASS")));
				showDevices();
			}
		};
		_discoveryReceiver = new BroadcastReceiver() {

			public void onReceive(Context context, Intent intent)
			{
				Log.d("EF-BTBee", ">>unregisterReceiver");
				unregisterReceiver(_foundReceiver);
				unregisterReceiver(this);
				_discoveryFinished = true;
			}
		};
	}

	protected void onCreate(Bundle bundle)
	{
		super.onCreate(bundle);
		getWindow().setFlags(4, 4);
		setContentView(R.layout.discovery);
		if (!mBT.isEnabled())
		{
			Log.w("EF-BTBee", ">>BTBee is disable!");
			finish();
		} else
		{
			IntentFilter intentfilter = new IntentFilter("android.bluetooth.adapter.action.DISCOVERY_FINISHED");
			registerReceiver(_discoveryReceiver, intentfilter);
			IntentFilter intentfilter1 = new IntentFilter("android.bluetooth.device.action.FOUND");
			registerReceiver(_foundReceiver, intentfilter1);
			SamplesUtils.indeterminate(this, _handler, getString(R.string.msg_actDiscovery_Search), _discoveryWorkder, new android.content.DialogInterface.OnDismissListener() {

				public void onDismiss(DialogInterface dialoginterface)
				{
					do
					{
						if (!mBT.isDiscovering())
						{
							_discoveryFinished = true;
							return;
						}
						mBT.cancelDiscovery();
					} while (true);
				}
			}, true);
		}
	}

	protected void onListItemClick(ListView listview, View view, int i, long l)
	{
		Log.d("EF-BTBee", ">>Click device");
		Intent intent = new Intent();
		intent.putExtra("android.bluetooth.device.extra.DEVICE", (Parcelable)_devices.get(i));
		setResult(-1, intent);
		finish();
	}

	protected void showDevices()
	{
		ArrayList arraylist = new ArrayList();
		int i = 0;
		int j = _devices.size();
		do
		{
			if (i >= j)
			{
				Log.d("EF-BTBee", ">>showDevices");
				final ArrayAdapter adapter = new ArrayAdapter(this, 0x1090003, arraylist);
				_handler.post(new Runnable() {

					public void run()
					{
						setListAdapter(adapter);
					}
				});
				return;
			}
			StringBuilder stringbuilder = new StringBuilder();
			BluetoothDevice bluetoothdevice = (BluetoothDevice)_devices.get(i);
			stringbuilder.append(bluetoothdevice.getAddress());
			stringbuilder.append((new StringBuilder(" (rssi:")).append((String)_RSSI.get(i)).append(")").toString());
			stringbuilder.append('\n');
			stringbuilder.append(bluetoothdevice.getName());
			stringbuilder.append((new StringBuilder(" (class:")).append((String)_CLASS.get(i)).append(")").toString());
			arraylist.add(stringbuilder.toString());
			i++;
		} while (true);
	}
}
