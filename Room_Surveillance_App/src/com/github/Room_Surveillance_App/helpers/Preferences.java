package com.github.Room_Surveillance_App.helpers;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager.NameNotFoundException;
import android.util.Log;

import com.github.Room_Surveillance_App.MainActivity;

public class Preferences {

	private static final String NUMBER = "number";
	private static final String REG_ID = "regId";
	private static final String APP_VERSION = "appVersion";

	private final Context context;
	private final SharedPreferences prefs;
	private final SharedPreferences.Editor editor;

	public Preferences(Context context) {
		this.context = context;
		prefs = context.getSharedPreferences(
				MainActivity.class.getSimpleName(), Context.MODE_PRIVATE);
		editor = prefs.edit();
	}

	public String getNumber() {
		return prefs.getString(NUMBER, "");
	}

	public void setNumber(String number) {
		editor.putString(NUMBER, number);
		editor.commit();
	}

	public String getRegId() {
		return prefs.getString(REG_ID, "");
	}

	public void setRegId(String number) {
		editor.putString(REG_ID, number);
		editor.commit();
	}

	public int getAppVersion() {
		return prefs.getInt(APP_VERSION, Integer.MIN_VALUE);
	}

	public void setAppVersion(int appVersion) {
		editor.putInt(APP_VERSION, appVersion);
		editor.commit();
	}

	public int getCurrentAppVersion() {
		try {
			PackageInfo packageInfo = context.getPackageManager()
					.getPackageInfo(context.getPackageName(), 0);
			return packageInfo.versionCode;
		} catch (NameNotFoundException e) {
			Log.d("RegisterActivity",
					"I never expected this! Going down, going down!" + e);
			throw new RuntimeException(e);
		}
	}

	public boolean isRegistered() {
		// Check if number and regId is set and if the current app version is
		// the same like the app version when it was registered
		if (getRegId().isEmpty() || getNumber().isEmpty()
				|| getAppVersion() != getCurrentAppVersion())
			return false;
		else
			return true;

	}
}
