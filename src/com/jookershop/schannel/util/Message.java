package com.jookershop.schannel.util;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;

public class Message {
	public static void showMsgDialog(final Context context, final String Msg) {
		if(context != null)
		((Activity)context).runOnUiThread( new Runnable() {
			public void run() {
				Builder MyAlertDialog = new AlertDialog.Builder(context);
				MyAlertDialog.setMessage(Msg);
				DialogInterface.OnClickListener OkClick = new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {
						
					}
				};
				MyAlertDialog.setNeutralButton("½T©w", OkClick);
				MyAlertDialog.show();
				
			}
		});
	}
}
