package com.jookershop.schannel;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URLEncoder;
import java.util.ArrayList;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ProgressBar;

import com.google.android.gms.ads.AdView;
import com.jookershop.schannel.util.AdUtil;
import com.jookershop.schannel.util.Message;
import com.jookershop.schannel.util.PictureUtil;
import com.koushikdutta.async.http.AsyncHttpPost;
import com.koushikdutta.async.http.AsyncHttpResponse;
import com.koushikdutta.async.http.body.AsyncHttpRequestBody;
import com.koushikdutta.async.http.body.FileBody;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;


public class MainActivity extends Activity {
	private RaceIItemAdpaterList adapter;
	private ListView lv;
	private ProgressBar pb;
	private EditText et;
	private Button bt;
	private static File mFileTemp;
	public static final String TEMP_PHOTO_FILE_NAME = "temp_photo.jpg";
	private static boolean upload = false;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		this.getActionBar().hide();
		final AdView adView = (AdView) findViewById(R.id.adView);
		AdUtil.showAD(this, adView);
		
		String state = Environment.getExternalStorageState();
		if (Environment.MEDIA_MOUNTED.equals(state)) {
			mFileTemp = new File(Environment.getExternalStorageDirectory(),
					TEMP_PHOTO_FILE_NAME);
		} else {
			mFileTemp = new File(getFilesDir(), TEMP_PHOTO_FILE_NAME);
		}	
		
		pb = (ProgressBar) this.findViewById(R.id.progressBar1);
		adapter = new RaceIItemAdpaterList(this, R.layout.item,
				new ArrayList<RaceItem>());
		lv = (ListView) this.findViewById(R.id.listView1);
		lv.setAdapter(adapter);

		et = (EditText) this.findViewById(R.id.editText1);
		bt = (Button) this.findViewById(R.id.button1);
		bt.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				bt.setEnabled(false);
				pb.setVisibility(View.VISIBLE);
				String msg = et.getText().toString();
				if(msg!= null && msg.indexOf("img")!= -1) {
					upload = true;
					final AlertDialog ad = PictureUtil.showMenu(MainActivity.this, mFileTemp);
					ad.setOnKeyListener(new Dialog.OnKeyListener() {
			            @Override
			            public boolean onKey(DialogInterface arg0, int keyCode,
			                    KeyEvent event) {
			                if (keyCode == KeyEvent.KEYCODE_BACK) {
			                	upload = false;
			    				bt.setEnabled(true);
			    				pb.setVisibility(View.INVISIBLE);
			                    ad.dismiss();			    				
			                }
			                return true;
			            }
			        });
				} else sendMsg();
			}
		});
	}

	
		
	@Override
	protected void onPause() {
		super.onPause();
		if(et != null && !upload) {
			et.setText("");
		}
		if(lv != null) {
			runOnUiThread(new Runnable() {
				@Override
				public void run() {
					((RaceIItemAdpaterList)lv.getAdapter()).clear();
					lv.postInvalidate();
				}
			});
		}
	}
	
	private void sendFile() {
		String url = Consts.BASE_URL + "/schannel/img/upload?msg="
				+ URLEncoder.encode(et.getText().toString()) + "&uid="
				+ Installation.id(MainActivity.this);
		Log.d(Consts.TAG, "insert url " + url );
		AsyncHttpPost post = new AsyncHttpPost(url);
		AsyncHttpRequestBody body = new FileBody(mFileTemp);
		post.setBody(body);
		
		com.koushikdutta.async.http.AsyncHttpClient.getDefaultInstance().executeString(post, new com.koushikdutta.async.http.AsyncHttpClient.StringCallback() {

			  @Override
			    public void onCompleted(final Exception e, AsyncHttpResponse source, String result) {
			        MainActivity.this.runOnUiThread( new Runnable() {
						public void run() {
							bt.setEnabled(true);
							pb.setVisibility(View.INVISIBLE);

					        if (e != null) {
					        	Message.showMsgDialog(MainActivity.this, "Opps....發生錯誤, 請稍後再試");
					            e.printStackTrace();
					            return;
					        }							
							
							Builder MyAlertDialog = new AlertDialog.Builder(MainActivity.this);
							MyAlertDialog.setMessage("圖片上傳成功");
							DialogInterface.OnClickListener OkClick = new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog, int which) {
									keeyName();
								}
							};
							MyAlertDialog.setNeutralButton("確定", OkClick);
							MyAlertDialog.show();
						}
					});
			    }
		});				
		
	}
	
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (resultCode != RESULT_OK) {
        	upload = false;
			bt.setEnabled(true);
			pb.setVisibility(View.INVISIBLE);

			return;
		}
		switch (requestCode) {
		case PictureUtil.PICK_FROM_CAMERA:
			sendFile();
			break;
		case PictureUtil.PICK_FROM_FILE:
			InputStream inputStream;
			try {
				inputStream = getContentResolver().openInputStream(
						data.getData());
				FileOutputStream fileOutputStream = new FileOutputStream(
						mFileTemp);
				PictureUtil.copyStream(inputStream, fileOutputStream);
				fileOutputStream.close();
				inputStream.close();
				sendFile();
			} catch (IOException e) {
				e.printStackTrace();
			}

			break;
		}
	}
	
	private void sendMsg() {
		AsyncHttpClient hpClient = new AsyncHttpClient();
		String url = Consts.BASE_URL + "/leavemsg?msg="
				+ URLEncoder.encode(et.getText().toString()) + "&uid="
				+ Installation.id(MainActivity.this);
		Log.d(Consts.TAG, "url :" + url);

		hpClient.get(url, new AsyncHttpResponseHandler() {
			@Override
			public void onFailure(int arg0, Header[] arg1, byte[] arg2,
					Throwable arg3) {
				super.onFailure(arg0, arg1, arg2, arg3);
				bt.setEnabled(true);
				pb.setVisibility(View.INVISIBLE);
			}

			@Override
			public void onSuccess(String response) {
				Log.d(Consts.TAG, "/leavemsg " + response);
				try {
					JSONObject json = new JSONObject(response);
					int status = json.getInt("status");
					if (status == -1) {
						Message.showMsgDialog(MainActivity.this, "格式有問題, 請查詢使用方法!!!");
					} else if (status == 1) {
						final AlertDialog.Builder builder = new Builder(
								MainActivity.this);
						builder.setMessage("傳送成功");
						builder.setPositiveButton("確定",
								new DialogInterface.OnClickListener() {
									@Override
									public void onClick(
											DialogInterface dialog,
											int which) {
										keeyName();
										dialog.dismiss();
									}
								});
						builder.create().show();
					} else if (status == 2) {
						JSONArray jarray = json.getJSONArray("ret");
						if (jarray.length() == 0) {
							Message.showMsgDialog(MainActivity.this, "沒有任何悄悄話");
						} 
							final ArrayList<RaceItem> ri = new ArrayList<RaceItem>();

							for (int index = 0; index < jarray.length(); index++) {
								JSONObject jo = jarray.getJSONObject(index);
								RaceItem raceItem = new RaceItem(jo.getString("msg"), jo
										.getLong("ts"),
										jo.getString("uid"), jo
												.getString("room"));
								if(jo.has("img") && !jo.isNull("img")) raceItem.setImg(jo.getString("img"));
								ri.add(raceItem);
							}
							runOnUiThread(new Runnable() {
								@Override
								public void run() {

									adapter = new RaceIItemAdpaterList(
											MainActivity.this,
											R.layout.item, ri);
									lv.setAdapter(adapter);
									lv.postInvalidate();
								}
							});
					}

				} catch (JSONException e) {
					e.printStackTrace();
				} finally {
					pb.setVisibility(View.INVISIBLE);
					bt.setEnabled(true);
				}
			}
		});
	}
	
	private void keeyName() {
		String a = et.getText()
				.toString();
		if (a.indexOf(":") != -1
				&& a.indexOf("@") != -1)
			et.setText(a.substring(0,
					a.indexOf(":")));		
	}
}