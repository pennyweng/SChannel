package com.jookershop.schannel.util;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.widget.ArrayAdapter;


public class PictureUtil {
	public static final int PICK_FROM_CAMERA = 1;
	public static final int CROP_FROM_CAMERA = 2;
	public static final int PICK_FROM_FILE = 3;
	
	
	public static AlertDialog showMenu(final Context context, final File mFileTemp) {
		final String[] items = new String[] { "從相機取得照片", "從相簿取得照片" };

		ArrayAdapter<String> adapter = new ArrayAdapter<String>(context,
				android.R.layout.select_dialog_item, items);
		AlertDialog.Builder builder = new AlertDialog.Builder(context);

		builder.setTitle("選擇照片");
		builder.setAdapter(adapter, new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int item) { // pick from
																	// camera
				if (item == 0) {
					Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

					try {
						Uri mImageCaptureUri = null;
						String state = Environment.getExternalStorageState();
						if (Environment.MEDIA_MOUNTED.equals(state)) {
							mImageCaptureUri = Uri.fromFile(mFileTemp);
						}
						intent.putExtra(
								android.provider.MediaStore.EXTRA_OUTPUT,
								mImageCaptureUri);
						intent.putExtra("return-data", true);
						((Activity)context).startActivityForResult(intent, PICK_FROM_CAMERA);
					} catch (ActivityNotFoundException e) {

						e.printStackTrace();
					}
				} else { // pick from file
					Intent intent = new Intent();

					intent.setType("image/*");
					intent.setAction(Intent.ACTION_GET_CONTENT);

					((Activity)context).startActivityForResult(Intent.createChooser(intent,
							"Complete action using"), PICK_FROM_FILE);
				}
			}
		});

		final AlertDialog dialog = builder.create();
		dialog.show();
		return dialog;
	}

	public static void copyStream(InputStream input, OutputStream output)
			throws IOException {

		byte[] buffer = new byte[1024];
		int bytesRead;
		while ((bytesRead = input.read(buffer)) != -1) {
			output.write(buffer, 0, bytesRead);
		}
	}	
}

