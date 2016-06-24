package com.example.unipitest;

import com.example.async.tasks.FetchPageTask;
import com.example.preferences.SettingsActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

public class HomeActivity extends MainActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		getLayoutInflater().inflate(R.layout.activity_home, frameLayout);

	}

	@Override
	public void onStart() {
		super.onStart();

		Intent intent = this.getIntent();
		if (intent != null && intent.hasExtra(Intent.EXTRA_TEXT)) {
			String page = intent.getStringExtra(Intent.EXTRA_TEXT);
			updatePageview(page);
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.home, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			Intent Settings = new Intent(HomeActivity.this,
					SettingsActivity.class);
			startActivity(Settings);
			return true;
			
			
		}
		
		
		if (id == R.id.action_share) {	
			Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND); 
			sharingIntent.setType("text/plain");
			//Calling Application class (see application tag in AndroidManifest.xml)
			//final GlobalClass globalVariable = (GlobalClass) getApplicationContext();
			//String name  = globalVariable.getnamesharepages();
			
			TextView thetitle = (TextView) findViewById(R.id.Header);
			String sharetitle = thetitle.getText().toString();
			
			TextView thebody = (TextView) findViewById(R.id.Body);
			String sharebody = thebody.getText().toString();
			
			String sharepage = "\t " + sharetitle + "\n\n\n\t " + sharebody;
			
			sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "Subject Here");
			sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, sharepage);
			startActivity(Intent.createChooser(sharingIntent, "Share to"));                  
	     }
		
		return super.onOptionsItemSelected(item);
	}

	// Update Listview
	private void updatePageview(String page) {
		FetchPageTask datatask = new FetchPageTask(this, this);
		datatask.execute(page);
	}
}
