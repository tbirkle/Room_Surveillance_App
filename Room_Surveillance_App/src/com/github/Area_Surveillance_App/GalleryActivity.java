package com.github.Area_Surveillance_App;

import static android.widget.ImageView.ScaleType.CENTER_CROP;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;

import com.github.Room_Surveillance_App.R;
import com.squareup.picasso.Picasso;



public class GalleryActivity extends Activity {

	Integer[] pics = {R.drawable.ic_launcher};
//	public static File bigPictureFile;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_gallery);

		
		if (savedInstanceState == null) {
			getFragmentManager().beginTransaction()
					.add(R.id.container, new PlaceholderFragment()).commit();
		}
		
		
	}
	
	
	
//############################################
	final class SampleGridViewAdapter extends BaseAdapter {
		  private final Context context;
		  private final List<String> urls = new ArrayList<String>();
		  private List<File> files = new ArrayList<File>();
		  Boolean isBig = false;

		  public SampleGridViewAdapter(Context context) {
		    this.context = context;
		    
		    
		    // Room verzeichnis durchsuchen
		    String path = Environment.getExternalStorageDirectory() + "/Room";
		    File dir = new File(path);
		    File[] filesInDir = dir.listFiles();
		    for(int i = 0; i < filesInDir.length; i++)
		    {
		    	File file = filesInDir[i];
		    	files.add(file);
		    }
		    
		    Log.i("Async-Example", "Files in dir:" + filesInDir.length);

		    urls.add("http://citiesofmigration.ca/wp-content/uploads/2011/02/test.jpg");
		    urls.add("http://www.nationalflaggen.de/media/flags/flagge-spanien.gif");
		    urls.add("http://upload.wikimedia.org/wikipedia/commons/thumb/7/78/Flag_of_Chile.svg/1500px-Flag_of_Chile.svg.png");
		    urls.add("ftp://187687-rs:Dp3Jp23SS9@ftp.g8j.de/img/Tesla_Coil.JPG");
		    urls.add("ftp://ftp.g8j.de/img/Tesla_Coil.JPG");
		    urls.add("ftp://ftp.g8j.de/img/Tesla_Coil.JPG");
		    urls.add("ftp://ftp.g8j.de/img/Tesla_Coil.JPG");
		  }

		  @Override public View getView(int position, View convertView, ViewGroup parent) {
		    SquaredImageView view = (SquaredImageView) convertView;
		    Log.i("Async-Example", "getView");
		    if (view == null) {
		      view = new SquaredImageView(context);
		      view.setScaleType(CENTER_CROP);
		      view.setClickable(true);
		    }

		    // Get the image URL for the current position.
//		    String url = getItem(position);
		    final File fileToLoad = getItem(position);
		    
//		    bigPictureFile = fileToLoad;
		    
		    Log.i("Async-Example", "get file position " + position);
		    
		    Log.i("Async-Example", "fileToLoad: " + fileToLoad.getPath());

		    // Trigger the download of the URL asynchronously into the image view.
		    Picasso.with(context) //
		        .load(fileToLoad) //
		        .placeholder(R.drawable.ic_launcher) //
		        .error(R.drawable.no_photo) //
		        .fit()//.centerCrop() // mitte des bildes wird angezeigt
		        .into(view);
		    
		    Log.i("Async-Example", "load image into grid");
		    
//		    Log.i("Async-Example", "Downloaded:" + url);
		    

		    view.setOnClickListener(new View.OnClickListener() {
				
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					Log.i("Async-Example", "onclick listener");
					
					
//					goToBigPicture(v);
					
					ImageView iv = (ImageView) findViewById(R.id.bigView);
//					iv.setImageResource(R.drawable.no_photo);
					
					if (!isBig) { 
						iv.setEnabled(true);
						Log.i("Async-Example", "Imageview enabled");
						
						String path = "/sdcard/DCIM/Camera/IMG_20140608_164722.jpg";
						File file = new File(path);
						Uri uri = Uri.fromFile(file);
						Picasso.with(context).load(fileToLoad).into(iv);//load(uri).into(iv);
						isBig = true;
						
					} else {
						iv.setEnabled(false);
						iv.setImageResource(R.drawable.white);
						Log.i("Async-Example", "Imageview disabled");
						isBig = false;
					}
				}

			});
		  

		    return view;
		  }
		  

//		  @Override public int getCount() {
//		    return urls.size();
//		  }
		  @Override public int getCount() {
			  return files.size();
		  }

//		  @Override public String getItem(int position) {
//		    return urls.get(position);
//		  }
		  @Override public File getItem(int position) {
			  return files.get(position);			  
		  }

		  @Override public long getItemId(int position) {
		    return position;
		  }
		}
//##############################################
	
	
	

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.gallery, menu);
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
	@SuppressLint("ValidFragment")
	private class PlaceholderFragment extends Fragment {

//		public PlaceholderFragment() {
//		}

		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) {
			View rootView = inflater.inflate(R.layout.fragment_gallery,
					container, false);
			
			
//			ImageView lv = (ImageView) rootView.findViewById(R.id.imageGallery);
//			lv.setImageResource(R.drawable.ic_launcher);
//			Picasso.with(getActivity()).load("http://citiesofmigration.ca/wp-content/uploads/2011/02/test.jpg").into(lv);
			
			
//			setContentView(R.layout.fragment_gallery);
			
			final ImageView iv = (ImageView) findViewById(R.id.bigView);
			
			
			GridView gv = (GridView) rootView.findViewById(R.id.grid_view);
		    gv.setAdapter(new SampleGridViewAdapter(getActivity()));
			
			return rootView;
		}
		
	}

}
