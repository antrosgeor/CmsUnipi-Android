package com.example.unipitest;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.example.preferences.SettingsActivity;
import com.example.sensors.ShakeEventListener;
import com.example.adapters.NewsAdapter;
import com.example.async.tasks.FetchNewsTask;
import com.example.data.models.News;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

public class NewsActivity extends MainActivity {

	private SensorManager mSensorManager;

	private ShakeEventListener mSensorListener;

	protected ListView mNewsListview;

	public NewsAdapter mNewsAdapter;

	protected String[] newsListArray = {};

	List<String> dummyNewsData = new ArrayList<String>(
			Arrays.asList(newsListArray));
	SwipeRefreshLayout mSwipeRefreshLayout;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		getLayoutInflater().inflate(R.layout.test, frameLayout);

		final SwipeRefreshLayout mSwipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipe_container);
		News newNews = new News();
		newNews.setNewsTitle("test title");
		newNews.setNewsBody("test body");
		newNews.setNewsDate("newsDate");
		
		ArrayList<News> arrayOfNews = new ArrayList<News>();

		final NewsAdapter mNewsAdapter = new NewsAdapter(this, arrayOfNews);
		mNewsAdapter.add(newNews);

		// Configure the refreshing colors
		mSwipeRefreshLayout.setColorSchemeResources(
				android.R.color.holo_blue_bright,
				android.R.color.holo_green_light,
				android.R.color.holo_orange_light,
				android.R.color.holo_red_light);

		mSwipeRefreshLayout
				.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
					@Override
					public void onRefresh() {
						updateNewsListview(mNewsAdapter);
						test();
					}
				});

		mNewsListview = (ListView) findViewById(R.id.listview_news);
		
		//mNewsAdapter = new ArrayAdapter<String>(this, R.layout.news_item,R.id.list_item_news,dummyNewsData);
		
		//mNewsListview.setAdapter(mNewsAdapter);
		
		

		mNewsListview.setAdapter(mNewsAdapter);
		
		mNewsListview.setOnItemClickListener(new NewsItemClickListener());

		// Update NewsListview
		updateNewsListview(mNewsAdapter);

		mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
		mSensorListener = new ShakeEventListener();

		mSensorListener
				.setOnShakeListener(new ShakeEventListener.OnShakeListener() {

					public void onShake() {

						mSwipeRefreshLayout.post(new Runnable() {
							@Override
							public void run() {
								mSwipeRefreshLayout.setRefreshing(true);
							}
						});

						updateNewsListview(mNewsAdapter);
						// test();
					}
				});
	}

	@Override
	protected void onResume() {
		super.onResume();
		mSensorManager.registerListener(mSensorListener,
				mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER),
				SensorManager.SENSOR_DELAY_UI);
	}

	@Override
	protected void onPause() {
		mSensorManager.unregisterListener(mSensorListener);
		super.onPause();
	}

	private void test() {
		//Toast.makeText(this, R.string.refresh , Toast.LENGTH_SHORT).show();
		// mSwipeRefreshLayout.setRefreshing(false);

	}

	// // On Start
	// @Override
	// public void onStart() {
	// super.onStart();
	// updateNewsListview();
	// }

	// Update Listview
	private void updateNewsListview(NewsAdapter mNewsAdapter) {
		mNewsAdapter.clear();
		FetchNewsTask datatask = new FetchNewsTask(this, mNewsAdapter, this);
		datatask.execute("gh");

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.news, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			Intent Settings = new Intent(NewsActivity.this,
					SettingsActivity.class);
			startActivity(Settings);
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	/* The click listner for ListView in the navigation drawer */
	private class NewsItemClickListener implements ListView.OnItemClickListener {
		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position,
				long id) {

			//String newsItem = mNewsAdapter.getItem(position);
			selectItem(position);
			mDrawerList.setItemChecked(position, true);

//			Intent News = new Intent(NewsActivity.this,
//					NewsDetailActivity.class).putExtra(Intent.EXTRA_TEXT,
//					newsItem);
			
			Intent News = new Intent(NewsActivity.this,
					NewsDetailActivity.class).putExtra(Intent.EXTRA_TEXT,
							Integer.toString(position));
			startActivity(News);

			overridePendingTransition(R.anim.animation,R.anim.animation2 );  


		}

	}

	//Test function
	private void selectItem(int position) {
		
		//Get Language Preferences 
		String Language = getDeafaultPreferences();
		
		//Test Toast Language Preference
		Toast.makeText(getApplicationContext(), Language, Toast.LENGTH_SHORT).show();
		
		//Test toast ListItem
		Toast.makeText(getApplicationContext(), Integer.toString(position),Toast.LENGTH_SHORT).show();
	}
	
	
	//Get Default Shared Language Preferences
	private String getDeafaultPreferences(){
		
		SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(NewsActivity.this);
		String Language = prefs.getString(
				getString(R.string.pref_language_key),
				getString(R.string.pref_language_greek));
		
		return Language;

	}
	
	
}
