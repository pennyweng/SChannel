package com.jookershop.schannel.util;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.util.Log;
import android.widget.RelativeLayout.LayoutParams;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.jookershop.schannel.Consts;


public class AdUtil {
	
	public static void showAD(Context context, final AdView adView) {
		final SharedPreferences sp = context.getSharedPreferences(Consts.STORE, 0 );
		if(sp.contains(Consts.KEY_CLICK_AD) && (System.currentTimeMillis() - sp.getLong(Consts.KEY_CLICK_AD, 0)) < 2592000000l) {
			adView.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, 0));
			adView.destroy();
//			adView.setLayoutParams(new android.widget.RelativeLayout.LayoutParams(android.widget.RelativeLayout.LayoutParams.MATCH_PARENT, 1));
		} else {
			final AlertDialog.Builder builder = new Builder(context);
			builder.setMessage("點選下方的廣告, 一個月內就不會再看到任何廣告了!!!");
			builder.setPositiveButton("下次再提醒我",
					new DialogInterface.OnClickListener() {
						@Override
						public void onClick(
								DialogInterface dialog,
								int which) {
							sp.edit().putBoolean(Consts.KEY_SHOW_ALERT, true).commit();
							dialog.dismiss();
						}
					});
			builder.setNegativeButton("我知道了",
					new DialogInterface.OnClickListener() {
						@Override
						public void onClick(
								DialogInterface dialog,
								int which) {
							sp.edit().putBoolean(Consts.KEY_SHOW_ALERT, false).commit();
							dialog.dismiss();
						}
					});			
			
			if(!sp.contains(Consts.KEY_SHOW_ALERT) || (sp.contains(Consts.KEY_SHOW_ALERT) && sp.getBoolean(Consts.KEY_SHOW_ALERT, true)))
			builder.create().show();

			
			AdRequest adRequest = new AdRequest.Builder().build();
		    adView.loadAd(adRequest);		
			adView.setAdListener(new AdListener() {
				@Override
				public void onAdClosed() {
					super.onAdClosed();
					Log.d(Consts.TAG, "google ad onAdClosed");
				}
	
				@Override
				public void onAdFailedToLoad(int errorCode) {
					// TODO Auto-generated method stub
					super.onAdFailedToLoad(errorCode);
					Log.d(Consts.TAG, "google ad error" + errorCode);
				}
	
				@Override
				public void onAdLeftApplication() {
					// TODO Auto-generated method stub
					super.onAdLeftApplication();
					Log.d(Consts.TAG, "google ad onAdLeftApplication");
					sp.edit().putLong(Consts.KEY_CLICK_AD, System.currentTimeMillis()).commit();
					adView.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, 0));
				}
	
				@Override
				public void onAdLoaded() {
					super.onAdLoaded();
					Log.d(Consts.TAG, "google ad load");
				}
	
				@Override
				public void onAdOpened() {
					super.onAdOpened();
					Log.d(Consts.TAG, "google ad onAdOpened");
					sp.edit().putLong(Consts.KEY_CLICK_AD, System.currentTimeMillis()).commit();
					adView.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, 0));
				}
				
			});
		}		
		
	}
}
