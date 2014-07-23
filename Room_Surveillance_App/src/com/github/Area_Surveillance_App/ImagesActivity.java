package com.github.Area_Surveillance_App;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.SocketException;
import java.net.URLConnection;

import android.app.Activity;
import android.app.Fragment;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;





import android.widget.Toast;

import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;

import android.os.AsyncTask;

import java.text.SimpleDateFormat;
import java.util.Date;

import com.github.Area_Surveillance_App.gcm.GcmIntentService;
import com.github.Room_Surveillance_App.R;


public class ImagesActivity extends Activity {
	
	public static String incomingMessage;
	
	private String downloadUrl = "http://citiesofmigration.ca/wp-content/uploads/2011/02/test.jpg";
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_images);

		if (savedInstanceState == null) {
			getFragmentManager().beginTransaction()
					.add(R.id.container, new PlaceholderFragment()).commit();
		}
		
//		DownloadImageTask downloadImage = new DownloadImageTask((ImageView) findViewById(R.id.imageView));
//		Log.i("Async-Example", "create button called");
//		
//		downloadImage.execute(downloadUrl);
		
		
	}
	
	public void setMessage(String msg) {
		incomingMessage = msg;
	}
	
	public class DownloadImageTask extends AsyncTask<String, Void, Bitmap> {

		ImageView bmImage;
		
		public DownloadImageTask(ImageView bmImage) {
			this.bmImage = bmImage;
		}
		
		
		@Override
		protected Bitmap doInBackground(String... urls) {
			// TODO Auto-generated method stub
			Log.i("Async-Example", "doInBackground called");
			
//####################### ordner und file erstellen###############
			String path = Environment.getExternalStorageDirectory() + "/Room";
			File folder = new File(path);
			Log.i("Async-Example", "get path");
			
			
//			File folder = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), "Room");
			
			if (!folder.exists())
			{
				boolean success = false;
				success = folder.mkdir();
				Log.i("Async-Example", "create folder");
				
				if (!success)
				{
					return null;
				}
			}
			
			SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd_HHmmss");
			String currentDateandTime = sdf.format(new Date());
			String exactPath = "Room_" + currentDateandTime + ".jpg";
			File file = new File(folder.getAbsolutePath(), exactPath);
			
//########################################################
			
			Bitmap mIcon11 = null;
			
			String ftpServer = "ftp.g8j.de";
			String ftpUser = "187687-rs";
			String ftpPass = "Dp3Jp23SS9";
			
			URLConnection ucon = null;
			
			FTPClient ftp = null;
			OutputStream out = null;
			boolean success = false;

			
//			try {	
				
				try {
				ftp = new FTPClient();
				ftp.setConnectTimeout(5 * 1000);
				ftp.connect(ftpServer);
				Log.i("Async-Example", "ftp connected");
				
				ftp.login(ftpUser, ftpPass);
				Log.i("Async-Example", "ftp logged in");
				ftp.setFileType(FTP.BINARY_FILE_TYPE);
				Log.i("Async-Example", "start downloading");
				ftp.enterLocalPassiveMode();
				
				try {
					
					String tmp = GcmIntentService.message;
					Log.i("Async-Example", "image message: " + tmp);				
					
					
				out = new BufferedOutputStream(new FileOutputStream(file));
				success = ftp.retrieveFile(tmp, out);
				Log.i("Async-Example", "downloading");
				} finally {
					if (out != null) {
						out.close();
					}
				}
				} catch (SocketException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} finally {
					if (ftp != null) {
						try {
							ftp.logout();
						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						try {
							ftp.disconnect();
						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
				}
				
				
				Log.i("Async-Example", "going to decode file");
				mIcon11 = BitmapFactory.decodeFile(file.getAbsolutePath());//"/sdcard/Room/"+ exactPath);
				if (mIcon11 == null)
				{
					Log.i("Async-Example", "scheisse wars");
				}

			Log.i("Async-Example", "doInBackground returning");
			return mIcon11;
		}
		
		
		

		@Override
		protected void onPostExecute(Bitmap result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
			Log.i("Async-Example", "onPostExcecute");
			if (result != null) {
				bmImage.setImageBitmap(result);
			} else
			{
				Toast.makeText(getApplicationContext(), R.string.no_image, Toast.LENGTH_LONG).show();
				
			}
			Log.i("Async-Example", "result");
//			
////			MediaStore.Images.Media.insertImage(getContentResolver(), result, "test", "test");
////			Log.i("Async-Example", "saving complete");
//			
//			String path = Environment.getExternalStorageDirectory() + "/Room";
//			File folder = new File(path);
//			Log.i("Async-Example", "get path");
//			
//			
////			File folder = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), "Room");
//			
//			if (!folder.exists())
//			{
//				boolean success = false;
//				success = folder.mkdir();
//				Log.i("Async-Example", "create folder");
//				
//				if (!success)
//				{
//					return;
//				}
//			}
//			
//			
//			
//			OutputStream fOut = null;
//			
//			SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd_HHmmss");
//			String currentDateandTime = sdf.format(new Date());
//			
//			File file = new File(folder.getAbsolutePath(), "Room_" + currentDateandTime + ".jpg");
//			Log.i("Async-Example", "Path: " + file.getAbsolutePath());
//			try {
//				fOut = new FileOutputStream(file);
//			} catch (FileNotFoundException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
//			
//			result.compress(Bitmap.CompressFormat.JPEG, 85, fOut);
//			
//			Uri contentUri = Uri.fromFile(folder);
//			Intent mediascanner = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, contentUri);
//			mediascanner.setData(contentUri);
//			getApplicationContext().sendBroadcast(mediascanner);
//			
//			try {
//				fOut.flush();
//			} catch (IOException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
//			try {
//				fOut.close();
//			} catch (IOException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
//			
//			try {
//				MediaStore.Images.Media.insertImage(getContentResolver(), file.getAbsolutePath(), file.getName(), file.getName());
//			} catch (FileNotFoundException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
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
