package com.solbjerg.justplayer;

import java.io.File;

import android.app.Application;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Environment;
import android.preference.PreferenceManager;
import android.util.Log;

public class JustPlayerApplication extends Application {
	
	private static final String TAG = JustPlayerApplication.class.getSimpleName();
	private SharedPreferences prefs;
	private static final String DFT_ROOT = Environment.getExternalStorageDirectory().getAbsolutePath();
	private static final int DFT_POS_UNIT = 5000;
	public static final String CURRENT_SONG = "CURRENT_SONG";
	
	@Override
	public void onCreate() {
		super.onCreate();
		this.prefs = PreferenceManager.getDefaultSharedPreferences(this);
		Log.i(TAG, "onCreated");
	}
	
	@Override
	public void onTerminate() {
		super.onTerminate();
		Log.i(TAG, "onTerminate");
	}

	public String getCurrentSong() {
		return this.prefs.getString("currentSong", "");
	}
	
	public void setCurrentSong(String song) {
		Editor editor = this.prefs.edit();
		editor.putString("currentSong", song);
		editor.commit();
	}
	
	public int getPosUnit() {
		return this.prefs.getInt("posUnit", DFT_POS_UNIT);
	}

}
