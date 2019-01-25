package mobi.dzs.android.BluetoothSPP;

import android.app.AlertDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Editable;
import android.view.*;
import android.widget.*;
import mobi.dzs.util.CHexConver;

public class actMain extends BaseActivity
{
	private static final byte MENU_CONNECTION = 0;
	private static final byte MENU_CLEAR = 1;
	private static final byte MENU_IO_MODE_SET = 2;
	
	private BluetoothAdapter mBT;
	private ScrollView mScrollView;
	private boolean mbKeyboardMode;
	private EditText metSendText;
	private TextView mtvBackDataShow;
	
	private class SPPClient extends AsyncTask
	{
		private final int CONNECT_FAIL;
		private final int CONNECT_LOST;
		private final int JUMP_KEYBOARD_MODE;

		protected Integer doInBackground(String as[])
		{
			byte bytebuf[] = new byte[1024];
			Integer integer;
			String str;
			
			if (createBluetoothConnect())
			{
				str = getString(R.string.msg_connect_ok) + "\r\n";
				publishProgress(str);
				
				if (mbKeyboardMode)
					return -3;
				else
				{
					int i;
					do
					{
						do
						{
							i = ReceiveData(bytebuf);
							if (i <= 0)
								break;
							if (mInputMode == 0)
							{
								str = new String(bytebuf, 0, i);
								publishProgress(str);
							} else
							if (1 == mInputMode)
							{
								str = (new StringBuilder(String.valueOf(CHexConver.byte2HexStr(bytebuf, i)))).append(" ").toString();
								publishProgress(str);
							}
						} while (true);
					}while (i >= 0);
					return -2;
				}
			}
			else
				return -1;
		}

		protected Object doInBackground(Object aobj[])
		{
			return doInBackground((String[])aobj);
		}

		public void onPostExecute(Integer integer)
		{
			switch(integer.intValue())
			{
			case -3:
				Intent intent = new Intent(actMain.this, actKeyBoard.class);
				startActivityForResult(intent, 3);
				break;
			case -2:
				mtvBackDataShow.append("\r\n");
				mtvBackDataShow.append(getString(R.string.msg_Bluetooth_conn_lost) + "\r\n");
				break;
			case -1:
				mtvBackDataShow.append("\r\n");
				mtvBackDataShow.append(getString(R.string.msg_actDiscovery_Bluetooth_SPP_Conn_Fail) + "\r\n");
				break;
			default:
				break;
			}
		}

		public void onPostExecute(Object obj)
		{
			onPostExecute((Integer)obj);
		}

		public void onPreExecute()
		{
			mtvBackDataShow.setText("");
			mtvBackDataShow.append(getString(R.string.msg_connecting) + "\r\n");
		}

		public void onProgressUpdate(String as)
		{
			if (as.length() > 0)
				mtvBackDataShow.append(as);
			int i = mtvBackDataShow.getMeasuredHeight() - mScrollView.getHeight();
			if (i > 0)
				mScrollView.scrollTo(0, i);
		}

		private SPPClient()
		{
			CONNECT_FAIL = -1;
			CONNECT_LOST = -2;
			JUMP_KEYBOARD_MODE = -3;
		}

		SPPClient(SPPClient sppclient)
		{
			this();
		}
	}

	public actMain()
	{
		mbKeyboardMode = false;
		mBT = BluetoothAdapter.getDefaultAdapter();
	}

	protected void onActivityResult(int i, int j, Intent intent)
	{
		if (3 == j)
		{
			finish();
			return;
		}
		if (i == 1 && j == -1)
		{
			BluetoothDevice bluetoothdevice = (BluetoothDevice)intent.getParcelableExtra("android.bluetooth.device.extra.DEVICE");
			if (bluetoothdevice.getBondState() == BluetoothDevice.BOND_NONE)
			{
				try
				{
					BluetoothCtrl.createBond(bluetoothdevice);
					Toast.makeText(this, getString(R.string.msg_actDiscovery_Bluetooth_Bond_msg), 0).show();
				}
				catch (Exception exception)
				{
					Toast.makeText(this, getString(R.string.msg_actDiscovery_Bluetooth_Bond_fail), 0).show();
				}
			} 
			else
			{
				msBluetoothMAC = bluetoothdevice.getAddress();
				(new SPPClient(null)).execute(new String[0]);
			}
		}
	}

	public void onBtnSend(View view)
	{
		String s = metSendText.getText().toString();
		if (isConnect())
		{
			int i;
			if (s.length() <= 0)
				return; /* Loop/switch isn't completed */
			if (mOutputMode != 0)
			{
				byte byte0 = mOutputMode;
				i = 0;
				if (1 == byte0)
				{
					if (CHexConver.checkHexStr(s))
					{
						i = SendData(CHexConver.hexStr2Bytes(s.toUpperCase()));
					} 
					else
					{
						Toast.makeText(this, getString(R.string.msg_Hex_String_Illegal), 0).show();
						i = 0;
					}
				}
			}
			else
				i = SendData(s.getBytes());
			if (i < 0)
				Toast.makeText(this, getString(R.string.msg_actDiscovery_SendBytesErr), 0).show();
		}
		else
			Toast.makeText(this, getString(R.string.msg_please_connect), 0).show();
	}

	public void onCreate(Bundle bundle)
	{
		super.onCreate(bundle);
		setContentView(R.layout.main);
		if (!mBT.isEnabled())
			mBT.enable();
		mScrollView = (ScrollView)findViewById(R.id.sv_Back_Data_Scroll);
		mtvBackDataShow = (TextView)findViewById(R.id.tv_Back_Data_Show);
		metSendText = (EditText)findViewById(R.id.et_main_Send_Text);
		InitLoad();
	}

	public boolean onCreateOptionsMenu(Menu menu)
	{
		menu.add(0, MENU_CONNECTION, MENU_CONNECTION, R.string.menu_main_Connection);
		menu.add(0, MENU_CLEAR, MENU_CLEAR, R.string.menu_Clear);
		menu.add(0, MENU_IO_MODE_SET, MENU_IO_MODE_SET, R.string.menu_main_IO_Mode);
		return true;
	}

	public void onDestroy()
	{
		super.onDestroy();
		terminateConnect();
		if (mBT.isEnabled())
			mBT.disable();
	}

	public boolean onKeyDown(int KeyCode, KeyEvent keyevent)
	{
		if (KeyCode == KeyEvent.KEYCODE_BACK)
			finish();
		return super.onKeyDown(KeyCode, keyevent);
	}

	public void onMenuConnection()
	{
		if (mBT.isEnabled())
		{
			if (isConnect())
				Toast.makeText(this, getString(R.string.msg_re_connect), 0).show();
			else
			{
				android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(this);
				if (msBluetoothMAC != null)
				{
					builder.setTitle(getString(R.string.menu_main_Connection));
					builder.setMessage(getString(R.string.msg_connect_history));
					
					builder.setPositiveButton(R.string.btn_connect, new android.content.DialogInterface.OnClickListener() {

						public void onClick(DialogInterface dialoginterface, int i)
						{
							(new SPPClient(null)).execute(new String[0]);
						}
					});
					
					builder.setNegativeButton(R.string.btn_reSearch, new android.content.DialogInterface.OnClickListener() {

						public void onClick(DialogInterface dialoginterface, int i)
						{
							showBluetootchDiscovery();
						}
					});
					builder.create().show();
				} 
				else
					showBluetootchDiscovery();
			}
		}
		else
			openButetooth();
	}

	public void onMenuIO_ModeSet()
	{
		android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(this);
		builder.setTitle(getString(R.string.dialog_title_IO_Mode_Set));
		View view = LayoutInflater.from(this).inflate(R.layout.dialog_io_mode, null);
		final RadioButton rbInChar = (RadioButton)view.findViewById(R.id.rb_IO_Mode_Set_In_Char);
		RadioButton radiobutton = (RadioButton)view.findViewById(R.id.rb_IO_Mode_Set_In_Hex);
		final RadioButton rbOutChar = (RadioButton)view.findViewById(R.id.rb_IO_Mode_Set_Out_Char);
		RadioButton radiobutton1 = (RadioButton)view.findViewById(R.id.rb_IO_Mode_Set_Out_Hex);
		if (mInputMode == 0)
			rbInChar.setChecked(true);
		else
			radiobutton.setChecked(true);
		if (mOutputMode == 0)
			rbOutChar.setChecked(true);
		else
			radiobutton1.setChecked(true);
		
		builder.setView(view);
		builder.setPositiveButton(R.string.btn_yes, new android.content.DialogInterface.OnClickListener() {

			public void onClick(DialogInterface dialoginterface, int i)
			{				
				if (rbInChar.isChecked())
					mInputMode = 0;
				else
					mInputMode = 1;
				if (rbOutChar.isChecked())
					mOutputMode = 0;
				else
					mOutputMode = 1;
				saveData("InputMode", mInputMode);
				saveData("OutputMode", mOutputMode);
			}
		});
		builder.create().show();
	}

	public boolean onMenuItemSelected(int i, MenuItem menuitem)
	{		
		switch(menuitem.getItemId())
		{
		case MENU_CONNECTION:
			android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(this);
			builder.setTitle(getString(R.string.dialog_title_Operat_Mode));
			builder.setMessage(getString(R.string.msg_main_Operation_mode));
			builder.setPositiveButton(R.string.btn_Normal_mode, new android.content.DialogInterface.OnClickListener() {

				public void onClick(DialogInterface dialoginterface, int j)
				{
					mbKeyboardMode = false;
					onMenuConnection();
				}
			});
			builder.setNegativeButton(R.string.btn_Keyboard_mode, new android.content.DialogInterface.OnClickListener() {

				public void onClick(DialogInterface dialoginterface, int j)
				{
					mbKeyboardMode = true;
					onMenuConnection();
				}
			});
			builder.create().show();
			break;
		case MENU_CLEAR:
			mtvBackDataShow.setText("");
			break;
		case MENU_IO_MODE_SET:
			onMenuIO_ModeSet();
			break;
		default:
			break;
		}
		return true;
	}

	public void onStart()
	{
		super.onStart();
		if (!mBT.isEnabled() && !mBT.isEnabled())
			Toast.makeText(this, getString(R.string.msg_actDiscovery_Bluetooth_Open_Modle), 0).show();
	}
}
