package com.github.Area_Surveillance_App;

import android.app.Activity;
import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.github.Area_Surveillance_App.prefs.Preferences;
import com.github.Room_Surveillance_App.R;



public class MainActivity extends Activity {
	
	private Preferences prefs;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		prefs = new Preferences(getApplicationContext());
		if (!prefs.isRegistered()) {
			startActivity(new Intent(getApplicationContext(),
					RegisterActivity.class));
			finish();
		}
		
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		if (savedInstanceState == null) {
			getFragmentManager().beginTransaction()
					.add(R.id.container, new PlaceholderFragment()).commit();
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
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
			View rootView = inflater.inflate(R.layout.fragment_main, container,
					false);
			return rootView;
		}
	}

	public void startImagesActivity(View view) {
		Intent intent = new Intent(this, ImagesActivity.class);
    	startActivity(intent);
	}
	
	public void goToGallery(View view) {
		Intent intent = new Intent(this, GalleryActivity.class);
    	startActivity(intent);
	}
	
}
