package com.github.Room_Surveillance_App;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import android.app.Activity;
import android.app.Fragment;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.text.format.Time;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

 













import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;


public class ImagesActivity extends Activity {

	private static final String LOG_TAG = "ImagesActivity";
	private static final String TAG = "ImagesActivity";
	
	private String downloadUrl = "http://citiesofmigration.ca/wp-content/uploads/2011/02/test.jpg";
	

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_images);
		

		if (savedInstanceState == null) {
			getFragmentManager().beginTransaction()
					.add(R.id.container, new PlaceholderFragment()).commit();
		}
	}
	
	private class DownloadImageTask extends AsyncTask<String, Void, Bitmap> {

		ImageView bmImage;
		
		public DownloadImageTask(ImageView bmImage) {
			this.bmImage = bmImage;
		}
		
		@Override
		protected Bitmap doInBackground(String... urls) {
			// TODO Auto-generated method stub
			Log.i("Async-Example", "doInBackground called");
			
			String urldisplay = urls[0];
			Bitmap mIcon11 = null;
			InputStream in = null;
			
			try {
				in = new java.net.URL(urldisplay).openStream();
				Log.i("Async-Example", "open Stream");
				mIcon11 = BitmapFactory.decodeStream(in);
				if (mIcon11 == null)
				{
					Log.i("Async-Example", "scheisse wars");
				}
				Log.i("Async-Example", "decode Stream");
			} catch (MalformedURLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			finally {
				try {
					in.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			
			Log.i("Async-Example", "doInBackground returning");
			return mIcon11;
		}

		@Override
		protected void onPostExecute(Bitmap result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
			Log.i("Async-Example", "onPostExcecute");
			bmImage.setImageBitmap(result);
			
//			MediaStore.Images.Media.insertImage(getContentResolver(), result, "test", "test");
//			Log.i("Async-Example", "saving complete");
			
			String path = Environment.getExternalStorageDirectory() + "/Room";
			File folder = new File(path);
			
//			File folder = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), "Room");
			
			if (!folder.exists())
			{
				boolean success = false;
				success = folder.mkdir();
				
				if (!success)
				{
					return;
				}
			}
			
			
			
			OutputStream fOut = null;
			
			SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd_HHmmss");
			String currentDateandTime = sdf.format(new Date());
			
			File file = new File(folder.getAbsolutePath(), "Room_" + currentDateandTime + ".jpg");
			Log.i("Async-Example", "Path: " + file.getAbsolutePath());
			try {
				fOut = new FileOutputStream(file);
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			result.compress(Bitmap.CompressFormat.JPEG, 85, fOut);
			
			Uri contentUri = Uri.fromFile(folder);
			Intent mediascanner = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, contentUri);
			mediascanner.setData(contentUri);
			getApplicationContext().sendBroadcast(mediascanner);
			
			try {
				fOut.flush();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			try {
				fOut.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			try {
				MediaStore.Images.Media.insertImage(getContentResolver(), file.getAbsolutePath(), file.getName(), file.getName());
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		
	}
		
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.images, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	/**
	 * A placeholder fragment containing a simple view.
	 */
	public static class PlaceholderFragment extends Fragment {

		public PlaceholderFragment() {
		}

		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) {
			View rootView = inflater.inflate(R.layout.fragment_images,
					container, false);
			return rootView;
		}
	}
	
	public void startDownload(View view)
	{
		new DownloadImageTask((ImageView) findViewById(R.id.imageView))
			.execute(downloadUrl);
		Log.i("Async-Example", "button pressed");
	}
	
	
}
