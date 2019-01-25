package mobi.dzs.android.BluetoothSPP;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.Editable;
import android.view.*;
import android.widget.*;
import java.util.Hashtable;
import mobi.dzs.android.util.sysinfo;

public class actKeyBoard extends BaseActivity
{
	private static final byte MENU_CLEAR = 1;
	private static final byte MENU_SET_KEY_BOARD = 0;
	private ScrollView mScrollView;
	private boolean mbSetMode;
	private Button mbtns[];
	private Hashtable mhtSendVal;
	private TextView mtvBackDataShow;

	public actKeyBoard()
	{
		mbSetMode = false;
		mbtns = new Button[9];
		mhtSendVal = new Hashtable();
	}

	private void Init()
	{
		int i = (new sysinfo(this)).getscreenWidth() / 3;
		int j = 0;
		do
		{
			if (j >= 9)
				return;
			String s = getStrData((new StringBuilder("BtnKey_name")).append(String.valueOf(j)).toString());
			String s1 = getStrData((new StringBuilder("BtnKey_value")).append(String.valueOf(j)).toString());
			
			if (s != null)
				mbtns[j].setText(s);
			android.view.ViewGroup.LayoutParams layoutparams;
			if (s1 != null)
				mhtSendVal.put(Integer.valueOf(j), s1);
			else
				mhtSendVal.put(Integer.valueOf(j), "");
			layoutparams = mbtns[j].getLayoutParams();
			layoutparams.height = i;
			mbtns[j].setLayoutParams(layoutparams);
			j++;
		} while (true);
	}

	private void sendMsg(String s)
	{
		int i;
		if (SendData(s.getBytes()) >= 0)
			mtvBackDataShow.append((new StringBuilder(String.valueOf(s))).append("(send ok) ").toString());
		else
			mtvBackDataShow.append((new StringBuilder(String.valueOf(s))).append("(send fail) ").toString());
		i = mtvBackDataShow.getMeasuredHeight() - mScrollView.getHeight();
		if (i > 0)
			mScrollView.scrollTo(0, i);
	}

	private void setBtnKeyboard(final int iId)
	{
		android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(this);
		builder.setTitle(getString(R.string.dialog_title_keyboard_Set));
		View view = LayoutInflater.from(this).inflate(R.layout.dialog_set_keyboard, null);
		final EditText tvBtnName = (EditText)view.findViewById(R.id.et_keyboard_set_BtnName);
		final EditText tvSendVal = (EditText)view.findViewById(R.id.et_keyboard_set_SendValue);
		tvBtnName.setText(mbtns[iId].getText());
		tvSendVal.setText((CharSequence)mhtSendVal.get(Integer.valueOf(iId)));
		builder.setView(view);
		builder.setPositiveButton(R.string.btn_yes, new android.content.DialogInterface.OnClickListener() {

			public void onClick(DialogInterface dialoginterface, int i)
			{
				mbtns[iId].setText(tvBtnName.getText().toString().trim());
				mhtSendVal.remove(Integer.valueOf(iId));
				mhtSendVal.put(Integer.valueOf(iId), tvSendVal.getText().toString().trim());
				saveData((new StringBuilder("BtnKey_name")).append(String.valueOf(iId)).toString(), tvBtnName.getText().toString().trim());
				saveData((new StringBuilder("BtnKey_value")).append(String.valueOf(iId)).toString(), tvSendVal.getText().toString().trim());
			}
		});
		builder.create().show();
	}

	public void onBtnClick_Array(View view)
	{
		int id = view.getId();
	
		for (int j = 0; j<9; j++)
		{
			if (mbtns[j].getId() != id)
				continue;
			if (mbSetMode)
				setBtnKeyboard(j);
			else if (!((String)mhtSendVal.get(Integer.valueOf(j))).equals(""))
				sendMsg((String)mhtSendVal.get(Integer.valueOf(j)));
		}
	}

	public void onCreate(Bundle bundle)
	{
		super.onCreate(bundle);
		setContentView(R.layout.keyboard_mode);
		mScrollView = (ScrollView)findViewById(R.id.sv_keyboard_Back_Data_Scroll);
		mtvBackDataShow = (TextView)findViewById(R.id.tv_keyboard_Back_Data_Show);
		
		mbtns[0] = (Button)findViewById(R.id.btn_keyboard_1);
		mbtns[1] = (Button)findViewById(R.id.btn_keyboard_2);
		mbtns[2] = (Button)findViewById(R.id.btn_keyboard_3);
		mbtns[3] = (Button)findViewById(R.id.btn_keyboard_4);
		mbtns[4] = (Button)findViewById(R.id.btn_keyboard_5);
		mbtns[5] = (Button)findViewById(R.id.btn_keyboard_6);
		mbtns[6] = (Button)findViewById(R.id.btn_keyboard_7);
		mbtns[7] = (Button)findViewById(R.id.btn_keyboard_8);
		mbtns[8] = (Button)findViewById(R.id.btn_keyboard_9);
	}

	public boolean onCreateOptionsMenu(Menu menu)
	{
		menu.add(0, MENU_SET_KEY_BOARD, MENU_SET_KEY_BOARD, R.string.menu_keyboard_set);
		menu.add(0, MENU_CLEAR, MENU_CLEAR, R.string.menu_Clear);
		return true;
	}

	public boolean onKeyDown(int KeyCode, KeyEvent keyevent)
	{
		if (KeyCode == KeyEvent.KEYCODE_BACK)
		{
			setResult(Activity.DEFAULT_KEYS_SEARCH_LOCAL, null);
			finish();
		}
		return super.onKeyDown(KeyCode, keyevent);
	}

	public boolean onMenuItemSelected(int i, MenuItem menuitem)
	{		
		switch(menuitem.getItemId())
		{
		case MENU_SET_KEY_BOARD:
			if (mbSetMode)
			{
				menuitem.setTitle(R.string.menu_keyboard_set);
				mtvBackDataShow.setText(getString(R.string.tv_actKeyBoard_Init) + "\n");
			} 
			else
			{
				menuitem.setTitle(R.string.menu_keyboard_set_over);
				mtvBackDataShow.setText(getString(R.string.msg_keyboard_Setings) + "\n");
			}
			if (mbSetMode)
				mbSetMode = false;
			else
				mbSetMode = true;
			break;
		case MENU_CLEAR:
			mtvBackDataShow.setText("");
			break;
		default:
			break;
		}
		return true;
	}

	public void onResume()
	{
		super.onResume();
		Init();
	}
}
