package com.example.async.tasks;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import com.example.async.tasks.WebServiceSettings;

import com.example.data.models.BackupModel;
import com.example.data.models.News;
import com.example.db.CmsTypesConstrains;
import com.example.db.DatabaseController;
import com.example.unipitest.R;

import android.app.Activity;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.TextView;

public class FetchNewsDetailTask extends AsyncTask<String, Void, News> {

	private final String LOG_TAG = FetchNewsDetailTask.class.getSimpleName();
	private DatabaseController dbController;
	private Activity activity;

	public FetchNewsDetailTask(Context context, Activity activity) {
		super();
		this.dbController = new DatabaseController(context);
		this.activity = activity;

	}

	@Override
	protected News doInBackground(String... params) {

		// If there are no params there's nothing to look up. Verify size of
		// params.
		if (params.length == 0) {
			return null;
		}

		String pageNo = params[0];

		News JsonData = null;

		BackupModel bup = dbController.getEntry(CmsTypesConstrains.CMS_NEWS);

		try {
			JsonData = GetData(bup.getData(), pageNo);
		} catch (JSONException e) {
			e.printStackTrace();
		}

		return JsonData;
	}

	private News GetData(String jsonstr, String pageNo) throws JSONException {

		JSONObject mainObject = new JSONObject(jsonstr);
		JSONArray dataArray = mainObject.getJSONArray("data");

		final News News = new News();

		JSONObject newsObject = dataArray.getJSONObject(Integer.parseInt(pageNo));

		String newsTitle = newsObject.getString("title");
		String newsDate = newsObject.getString("date");
		String newsAuthor = newsObject.getString("author");
		String newsBody = newsObject.getString("body");

		News.setNewsTitle(newsTitle);
		News.setNewsDate(newsDate);
		News.setNewsAuthor(newsAuthor);
		News.setNewsBody(newsBody);

		return News;

	}

	@Override
	protected void onPostExecute(News result) {
		
		Log.v(LOG_TAG, "onPostExecute" + result.toString() );
    	if (result != null) {
    		
			TextView title =(TextView) activity.findViewById(R.id.news_detail_title);
			TextView author=(TextView) activity.findViewById(R.id.news_detail_author);
			TextView date  =(TextView) activity.findViewById(R.id.news_detail_date);
			TextView body  =(TextView) activity.findViewById(R.id.news_detail_body);
			TextView by  =(TextView) activity.findViewById(R.id.by);
			
			title.setText(result.getNewsTitle());
			author.setText(result.getNewsAuthor());
			date.setText(result.getNewsDate());
			body.setText(result.getNewsBody());
			by.setText("by");
        }

	}

}
