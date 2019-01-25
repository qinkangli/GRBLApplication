package mobi.dzs.android.BluetoothSPP;

import android.bluetooth.BluetoothDevice;
import android.util.Log;
import java.lang.reflect.Method;

public class BluetoothCtrl
{
	public static boolean createBond(BluetoothDevice bluetoothdevice) throws Exception
	{
		return ((Boolean)bluetoothdevice.getClass().getMethod("createBond", new Class[0]).invoke(bluetoothdevice, new Object[0])).booleanValue();
	}

	public static boolean removeBond(BluetoothDevice bluetoothdevice) throws Exception
	{
		return ((Boolean)bluetoothdevice.getClass().getMethod("removeBond", new Class[0]).invoke(bluetoothdevice, new Object[0])).booleanValue();
	}

	public static boolean setPin(BluetoothDevice bluetoothdevice, String s)	throws Exception
	{
		try
		{
			Method method = bluetoothdevice.getClass().getDeclaredMethod("setPin", new Class[]{byte[].class	});
			Boolean boolean1 = (Boolean)method.invoke(bluetoothdevice, s.getBytes());
			Log.e("returnValue", (new StringBuilder("setPin:")).append(boolean1).toString());
		}
		catch (SecurityException securityexception)
		{
			securityexception.printStackTrace();
		}
		catch (IllegalArgumentException illegalargumentexception)
		{
			illegalargumentexception.printStackTrace();
		}
		catch (Exception exception)
		{
			exception.printStackTrace();
		}
		return true;
	}
}
