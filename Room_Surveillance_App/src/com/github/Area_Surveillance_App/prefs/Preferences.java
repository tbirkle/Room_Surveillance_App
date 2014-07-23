package com.github.Area_Surveillance_App.prefs;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager.NameNotFoundException;
import android.util.Log;

import com.github.Area_Surveillance_App.MainActivity;

public class Preferences {

	private static final String NUMBER = "number";
	private static final String ID = "ID";
	private static final String VERSION = "Version";

	private final Context context;
	private final SharedPreferences sharedPrefs;
	private final SharedPreferences.Editor editor;

	public Preferences(Context context) {
		this.context = context;
		sharedPrefs = context.getSharedPreferences(
				MainActivity.class.getSimpleName(), Context.MODE_PRIVATE);
		editor = sharedPrefs.edit();
	}

	public String number() {
		return sharedPrefs.getString(NUMBER, "");
	}

	public void number(String number) {
		editor.putString(NUMBER, number);
		editor.commit();
	}

	public String regId() {
		return sharedPrefs.getString(ID, "");
	}

	public void regId(String number) {
		editor.putString(ID, number);
		editor.commit();
	}

	public int appVersion() {
		return sharedPrefs.getInt(VERSION, Integer.MIN_VALUE);
	}

	public void appVersion(int appVersion) {
		editor.putInt(VERSION, appVersion);
		editor.commit();
	}

	public int getCurrentVersion() {
		try {
			PackageInfo packageInfo = context.getPackageManager()
					.getPackageInfo(context.getPackageName(), 0);
			return packageInfo.versionCode;
		} catch (NameNotFoundException e) {
			throw new RuntimeException(e);
		}
	}

	public boolean isRegistered() {
		if (regId().isEmpty() || number().isEmpty()
				|| appVersion() != getCurrentVersion())
			return false;
		else
			return true;

	}
}
