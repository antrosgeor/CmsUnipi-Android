package com.example.unipitest;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

import com.example.preferences.*;
import com.example.async.tasks.*;
import android.support.v4.app.ActionBarDrawerToggle;
//import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ListView;
import android.widget.TextView;

public class MainActivity extends ActionBarActivity {

	protected FrameLayout frameLayout;
	protected ListView mDrawerList;
	private ArrayAdapter<String> mDataAdapter;
	private DrawerLayout mDrawerLayout;
	private ActionBarDrawerToggle actionBarDrawerToggle;
	protected String[] listArray = {};
	List<String> dummyData = new ArrayList<String>(Arrays.asList(listArray));
	int langpos;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		
		Button en = (Button) findViewById(R.id.eng);
		Button gr = (Button) findViewById(R.id.gr);

		getActionBar().setIcon(
				new ColorDrawable(getResources().getColor(
						android.R.color.transparent)));

		frameLayout = (FrameLayout) findViewById(R.id.content_frame);
		mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
		mDrawerList = (ListView) findViewById(R.id.left_drawer);

		mDataAdapter = new ArrayAdapter<String>(this, // The current context
														// (this activity)
				R.layout.menu_item, // The name of the layout ID.
				R.id.menu_list_item, // The ID of the textview to populate.
				dummyData);

		mDrawerList.setAdapter(mDataAdapter);

		mDrawerList.setOnItemClickListener(new DrawerItemClickListener());
		getActionBar().setTitle("Menu");
		actionBarDrawerToggle = new ActionBarDrawerToggle(this, /* host Activity */
		mDrawerLayout, /* DrawerLayout object */
		R.drawable.ic_drawer, /* nav drawer icon to replace 'Up' caret */
		R.string.drawer_open, /* "open drawer" description */
		R.string.drawer_close /* "close drawer" description */
		) {

			/** Called when a drawer has settled in a completely closed state. */
			public void onDrawerClosed(View view) {
				super.onDrawerClosed(view);
				getActionBar().setTitle("Menu");
			}

			/** Called when a drawer has settled in a completely open state. */
			public void onDrawerOpened(View drawerView) {
				super.onDrawerOpened(drawerView);
				getActionBar().setTitle("Menu");
			}
		};

		// me tin epeilogi tou gr. pigeni kai exteli to updateconfig me entoli
		// "gr"
		en.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				updateconfig("en");
				// changelang(en);
				// Intent act1=new Intent(v.getContext(),MainActivity.class);
				// startActivity(act1);
			}
		});
		// me tin epeilogi tou gr. pigeni kai exteli to updateconfig me entoli
		// "gr"
		gr.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				updateconfig("gr");
				// changelang(gr);
				// Intent act1=new Intent(v.getContext(),MainActivity.class);
				// startActivity(act1);
			}
		});

		// Set the drawer toggle as the DrawerListener
		mDrawerLayout.setDrawerListener(actionBarDrawerToggle);

		getActionBar().setDisplayHomeAsUpEnabled(true);
		getActionBar().setHomeButtonEnabled(true);

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
			Intent Settings = new Intent(MainActivity.this,
					SettingsActivity.class);
			startActivity(Settings);
			return true;
		}
		if (actionBarDrawerToggle.onOptionsItemSelected(item)) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	// On Start
	@Override
	public void onStart() {
		super.onStart();
		updateListview();
	}

	// Update Listview
	private void updateListview() {
		FetchNavigationTask datatask = new FetchNavigationTask(this,
				mDataAdapter);
		datatask.execute("");
	}

	/* The click listner for ListView in the navigation drawer */
	private class DrawerItemClickListener implements
			ListView.OnItemClickListener {
		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position,
				long id) {

			String menuItem = mDataAdapter.getItem(position);
			selectItem(position);
			mDrawerList.setItemChecked(position, true);
			switch (menuItem) {
			case "News":
				Intent News = new Intent(MainActivity.this, NewsActivity.class);
				startActivity(News);
				break;
			case "Contact":
				Intent Contact = new Intent(MainActivity.this,
						ContactActivity.class);
				startActivity(Contact);
				break;
			case "Find Us":
				Intent Maps = new Intent(MainActivity.this, MapsActivity.class);
				startActivity(Maps);
				break;
			default:
				// Toast.makeText(getApplicationContext(),
				// Integer.toString(position), Toast.LENGTH_SHORT).show();
				Intent Page = new Intent(MainActivity.this, HomeActivity.class)
						.putExtra(Intent.EXTRA_TEXT, Integer.toString(position));
				startActivity(Page);
				break;
			}
			overridePendingTransition(R.anim.animation, R.anim.animation2);

		}

	}

	private void selectItem(int position) {
		// update selected item and title, then close the drawer
		// Toast.makeText(getApplicationContext(), Integer.toString(position),
		// Toast.LENGTH_SHORT).show();
		mDrawerList.setItemChecked(position, true);
		mDrawerLayout.closeDrawer(GravityCompat.START);
	}

	// apo edo kai pera einai gia tin epilogi tis glosas pou ginete apo to
	// string. kai string_gr
	public void changelang(View v) {
		switch (langpos) {
		case 0:
			updateconfig("en");
			break;
		case 1:
			updateconfig("gr");
			break;
		}
	}

	public void updateconfig(String s) {
		String languageToload = s;
		Locale locale = new Locale(languageToload);
		Locale.setDefault(locale);
		Configuration config = new Configuration();
		config.locale = locale;
		getBaseContext().getResources().updateConfiguration(config,
				getBaseContext().getResources().getDisplayMetrics());
		// this.setContentView(R.layout.activity_main);
		setTitle(R.string.app_name);
		((Button) findViewById(R.id.eng))
				.setText(R.string.pref_language_english);
		((Button) findViewById(R.id.gr)).setText(R.string.pref_language_greek);

		// Bundle tempBundle =new Bundle();
		// onCreate(tempBundle);
		// setTitle(R.string.app_name);
		// invalidateOptionsMenu();
	}
}
