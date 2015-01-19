package com.jookershop.schannel;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import com.koushikdutta.urlimageviewhelper.UrlImageViewHelper;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class RaceIItemAdpaterList extends ArrayAdapter<RaceItem>{
    Context context; 
    int layoutResourceId;    
    ArrayList<RaceItem> data = null;
    private SimpleDateFormat formatter;
    
    public RaceIItemAdpaterList(Context context, int layoutResourceId, ArrayList<RaceItem> data) {
        super(context, layoutResourceId, data);
        this.layoutResourceId = layoutResourceId;
        this.context = context;
        this.data = data;
        formatter = new SimpleDateFormat("MM/dd HH:mm");        
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
//        View row = convertView;
        RaceItem ri = data.get(position);
        
//        if(row == null) {
        	LayoutInflater inflater = ((Activity)context).getLayoutInflater();
        	View row = inflater.inflate(R.layout.item, parent, false);
        	
        	((TextView) row.findViewById(R.id.msg)).setText(ri.getMsg());
        	((TextView) row.findViewById(R.id.room)).setText(ri.getRoom());
        	((TextView) row.findViewById(R.id.ts)).setText(formatter.format(new Date(ri.getTs())) + "");
	    	ImageView iv = (ImageView) row.findViewById(R.id.imageView1);
	    	
	    	if(ri.getImg() != null && ri.getImg().length() > 0) {
	    	Log.d(Consts.TAG, "get image:" + Consts.BASE_URL + "/schannel/img?id=" + ri.getImg());
	    	UrlImageViewHelper.setUrlDrawable(iv, Consts.BASE_URL + "/schannel/img?id=" + ri.getImg(), R.drawable.download);
	    	}
//        }
        return row;
    }
    
    static class WeatherHolder
    {
    	TextView id;
        TextView title;
        TextView opendate;
        TextView win_num;
        TextView win_phone;
    }
}