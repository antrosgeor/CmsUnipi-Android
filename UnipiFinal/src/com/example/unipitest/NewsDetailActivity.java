package com.example.unipitest;

import com.example.async.tasks.FetchNewsDetailTask;
import com.example.preferences.SettingsActivity;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

public class NewsDetailActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_news_detail);

		Intent intent = this.getIntent();
		if (intent != null && intent.hasExtra(Intent.EXTRA_TEXT)) {
			String newsStr = intent.getStringExtra(Intent.EXTRA_TEXT);
			updateNews(newsStr);
		}

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.news_detail, menu);

		// Intent intent = this.getIntent();
		// if (intent != null && intent.hasExtra(Intent.EXTRA_TEXT)) {
		// String newsStr = intent.getStringExtra(Intent.EXTRA_TEXT);
		// TextView title=(TextView)findViewById(R.id.news_detail_textview);
		// title.setText(newsStr);
		// }

		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			Intent Settings = new Intent(NewsDetailActivity.this,
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

			TextView thetitle = (TextView) findViewById(R.id.news_detail_title);
			String sharetitle = thetitle.getText().toString();

			TextView thebody = (TextView) findViewById(R.id.news_detail_body);
			String sharebody = thebody.getText().toString();

			String sharepage = "\t " + sharetitle + "\n\n\n\t " + sharebody;

			sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT,
					"Subject Here");
			sharingIntent
					.putExtra(android.content.Intent.EXTRA_TEXT, sharepage);
			startActivity(Intent.createChooser(sharingIntent, "Share via"));
		}
		return super.onOptionsItemSelected(item);
	}

	// Update Listview
	private void updateNews(String news_id) {
		FetchNewsDetailTask datatask = new FetchNewsDetailTask(this, this);
		datatask.execute(news_id);
	}
}
