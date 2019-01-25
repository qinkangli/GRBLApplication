package mobi.dzs.android.BluetoothSPP;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Handler;

abstract class SamplesUtils
{
	private static ProgressDialog createProgressDialog(Context context, String s)
	{
		ProgressDialog progressdialog = new ProgressDialog(context);
		progressdialog.setIndeterminate(false);
		progressdialog.setMessage(s);
		return progressdialog;
	}

	public static void indeterminate(Context context, Handler handler, String s, Runnable runnable, android.content.DialogInterface.OnDismissListener ondismisslistener)
	{
		indeterminateInternal(context, handler, s, runnable, ondismisslistener, true);
	}

	public static void indeterminate(Context context, Handler handler, String s, Runnable runnable, android.content.DialogInterface.OnDismissListener ondismisslistener, boolean flag)
	{
		indeterminateInternal(context, handler, s, runnable, ondismisslistener, flag);
	}

	private static void indeterminateInternal(Context context, final Handler handler, String s, final Runnable runnable, android.content.DialogInterface.OnDismissListener ondismisslistener, boolean flag)
	{
		final ProgressDialog dialog = createProgressDialog(context, s);
		dialog.setCancelable(flag);
		if (ondismisslistener != null)
			dialog.setOnDismissListener(ondismisslistener);
		dialog.show();
		(new Thread() {
			public void run()
			{
				runnable.run();
				handler.post(new Runnable() {

					public void run()
					{
						try
						{
							dialog.dismiss();
						}
						catch(Exception exception)
						{
						}
					}
				});
			}
		}).start();
	}
}
