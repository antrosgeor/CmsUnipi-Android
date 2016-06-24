package com.example.unipitest;

import com.example.preferences.SettingsActivity;
import com.example.sensors.GPSTracker;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class MapsActivity extends MainActivity {

	Button ShowGpsLocation;
	Button ShowDirections;
	Button Call;
	GPSTracker gps;
	public double latitude;
	public double longitude;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		getLayoutInflater().inflate(R.layout.activity_maps, frameLayout);

		Button ShowDirections = (Button) findViewById(R.id.GpsDirections);
		Button Call = (Button) findViewById(R.id.call);

		gps = new GPSTracker(MapsActivity.this);
		if (gps.canGetLocation()) {
			latitude = gps.getLatitude();
			longitude = gps.getLongitude();
		}

		ShowDirections.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {

				String url = "http://maps.google.com/maps?saddr=" + latitude
						+ "," + longitude + "&daddr=37.941765,23.652767";
				Intent mapIntent = new Intent(
						android.content.Intent.ACTION_VIEW, Uri.parse(url));
				mapIntent.setClassName("com.google.android.apps.maps",
						"com.google.android.maps.MapsActivity");
				startActivity(mapIntent);

				// Uri gmmIntentUri = Uri.parse("geo:37.7749,-122.4194");
				// Uri gmmIntentUri = Uri.parse("geo:"+latitude+","+longitude);
				// Uri gmmIntentUri =
				// Uri.parse("geo:"+latitude+","+longitude+"?q=37.7749,-122.4194");
				// Log.v("Maps ", "Uri " + gmmIntentUri.toString());
				// Intent mapIntent = new Intent(Intent.ACTION_VIEW,
				// gmmIntentUri);
				// mapIntent.setPackage("com.google.android.apps.maps");

				// Resolve Activity to prevent app from crashing

				if (mapIntent.resolveActivity(getPackageManager()) != null) {
					startActivity(mapIntent);
				} else {
					Toast.makeText(getApplicationContext(), "no app found",
							Toast.LENGTH_SHORT).show();
				}
			}
		});
		
		Call.setOnClickListener(new View.OnClickListener()
	    {@Override
	    	public void onClick(View view) {
	    	
	    	Intent callIntent = new Intent(Intent.ACTION_CALL);
	        callIntent.setData(Uri.parse("tel:00302104142121"));
	        startActivity(callIntent);
	    }
	    });

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.maps, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			Intent Settings = new Intent(MapsActivity.this,
					SettingsActivity.class);
			startActivity(Settings);
			return true;
		}

		if (id == R.id.action_share) {
			Intent sharingIntent = new Intent(
					android.content.Intent.ACTION_SEND);
			sharingIntent.setType("text/plain");
			// Calling Application class (see application tag in
			// AndroidManifest.xml)
			// final GlobalClass globalVariable = (GlobalClass)
			// getApplicationContext();
			// String name = globalVariable.getnamesharepages();

			TextView textView1 = (TextView) findViewById(R.id.findus);
			String shareunipi = textView1.getText().toString();

			sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT,
					"Subject Here");
			sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT,
					shareunipi);
			startActivity(Intent.createChooser(sharingIntent, "Share to"));
		}
		return super.onOptionsItemSelected(item);
	}
}
