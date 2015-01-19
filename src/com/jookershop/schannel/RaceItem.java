package com.jookershop.schannel;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;

public class RaceItem {
	private String msg;
	private long ts;
	private String uid;
	private String room;
	private String img;
	
	public RaceItem() {
	}
	
	public RaceItem(String msg, long ts, String uid, String room) {
		super();
		this.msg = msg;
		this.ts = ts;
		this.uid = uid;
		this.room = room;
	}

	

	public String getImg() {
		return img;
	}

	public void setImg(String img) {
		this.img = img;
	}

	public String getRoom() {
		return room;
	}

	public void setRoom(String room) {
		this.room = room;
	}

	public static RaceItem fromJSON(JSONObject jo1, Activity activity ) {
		RaceItem ri = new RaceItem();
		try {
			ri.setMsg(jo1.getString("msg"));
			ri.setTs(jo1.getLong("ts"));
			ri.setUid(jo1.getString("uid"));
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return ri;
	}


	public String getMsg() {
		return msg;
	}


	public void setMsg(String msg) {
		this.msg = msg;
	}


	public long getTs() {
		return ts;
	}


	public void setTs(long ts) {
		this.ts = ts;
	}


	public String getUid() {
		return uid;
	}


	public void setUid(String uid) {
		this.uid = uid;
	}
}
