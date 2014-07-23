package com.github.Area_Surveillance_App.gcm;

import android.app.IntentService;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.github.Area_Surveillance_App.MainActivity;
import com.github.Room_Surveillance_App.R;
import com.google.android.gms.gcm.GoogleCloudMessaging;

public class GcmIntentService extends IntentService {
	
	public static String message = null;
	

	public static final int id = 1;
	private NotificationManager notificationManager;
	NotificationCompat.Builder notificationBuilder;

	public GcmIntentService() {
		super("GcmIntentService");
	}

	//receive message from server
	@Override
	protected void onHandleIntent(Intent intent) {
		Bundle bundleExtras = intent.getExtras();
		GoogleCloudMessaging gcm = GoogleCloudMessaging.getInstance(this);

		String messageType = gcm.getMessageType(intent);

		if (!bundleExtras.isEmpty()) {
			if (GoogleCloudMessaging.MESSAGE_TYPE_MESSAGE.equals(messageType)) {
				notify("Motion detected!");
				
				
				message = bundleExtras.get("message").toString();
				Log.i("Async-Example", "notification received: " + message);
			}
		}
		GcmBroadcastReceiver.completeWakefulIntent(intent);
	}

	//notification in notificationbar
	private void notify(String msg) {		
		notificationManager = (NotificationManager) this
				.getSystemService(Context.NOTIFICATION_SERVICE);

		PendingIntent contentIntent = PendingIntent.getActivity(this, 0,
				new Intent(this, MainActivity.class), 0);

		NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(
				this).setSmallIcon(R.drawable.ic_launcher)
				.setContentTitle("Area Surveillance App")
				.setStyle(new NotificationCompat.BigTextStyle().bigText(msg))
				.setContentText(msg)
				.setAutoCancel(true);

		mBuilder.setContentIntent(contentIntent);
		notificationManager.notify(id, mBuilder.build());
	}

}
