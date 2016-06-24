package com.example.async.tasks;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import org.json.JSONException;
import org.json.JSONObject;
import com.example.unipitest.ContactActivity;
import com.example.unipitest.R;
import android.app.Activity;
import android.app.ProgressDialog;
import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.EditText;
import com.example.async.tasks.WebServiceSettings;

public class PostContactForm extends AsyncTask<String, Void, String> {

	private final String LOG_TAG = PostContactForm.class.getSimpleName();

	private Activity activity;
	private ProgressDialog progress;

	public PostContactForm(Activity activity, ProgressDialog progress) {
		super();
		this.activity = activity;
		this.progress = progress;
	}

	// Take the String representing the JSON Response and pull out the data we
	// need
	private String GetData(String jsonstr) throws JSONException {

		JSONObject mainObject = new JSONObject(jsonstr);
		String Message = mainObject.getString("message");

		Log.v(LOG_TAG, "Message" + Message);

		return Message;
	}

	@Override
	protected String doInBackground(String... params) {

		// If there are no params there's nothing to Post. Verify size of
		// params.
		if (params.length == 0) {
			return null;
		} else {
			Log.v(LOG_TAG, "user_name Param " + params[0]);
			Log.v(LOG_TAG, "user_email Param " + params[1]);
			Log.v(LOG_TAG, "phone_number Param " + params[2]);
			Log.v(LOG_TAG, "message Param " + params[3]);
		}

		// These two need to be declared outside the try/catch
		// so that they can be closed in the finally block.
		HttpURLConnection urlConnection = null;
		BufferedReader reader = null;

		// Will contain the raw JSON response as a string.
		String dataJsonStr = null;

		try {

			// Make url
			final String BASE_URL = WebServiceSettings.BASE_URL_POST;

			Uri builtUri = Uri.parse(BASE_URL).buildUpon().build();

			URL url = new URL(builtUri.toString());

			Log.v(LOG_TAG, "Built URI " + builtUri.toString());

			// Create request to RestApi, and open the connection
			urlConnection = (HttpURLConnection) url.openConnection();
			urlConnection.setRequestMethod("POST");
			urlConnection.setConnectTimeout(5000); //set timeout to 5 seconds

			
			OutputStream os = urlConnection.getOutputStream();
			BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(
					os, "UTF-8"));
			writer.write("user_name=" + params[0] + "&user_email=" + params[1]
					+ "&phone_number=" + params[2] + "&msg=" + params[3]);
			writer.flush();
			writer.close();
			os.close();

			urlConnection.connect();

			// get the response code, returns 200 if it's OK
			int responseCode = urlConnection.getResponseCode();

			// log connection
			Log.v(LOG_TAG, "connection" + responseCode);

			// Read the input stream into a String
			InputStream inputStream = urlConnection.getInputStream();
			StringBuffer buffer = new StringBuffer();
			if (inputStream == null) {
				// Nothing to do.
				return null;
			}

			reader = new BufferedReader(new InputStreamReader(inputStream));

			String line;
			while ((line = reader.readLine()) != null) {
				buffer.append(line + "\n");
			}

			if (buffer.length() == 0) {
				// Stream was empty. No point in parsing.
				return null;
			}
			dataJsonStr = buffer.toString();

			// log Json String
			Log.v(LOG_TAG, "DATA JSON" + dataJsonStr);

		} catch (IOException e) {
			Log.e(LOG_TAG, "Error Fetching Data", e);
			// If the code didn't successfully get data, there's no point in
			// attemping to parse it.
			return null;
		} finally {
			if (urlConnection != null) {
				urlConnection.disconnect();
			}
			if (reader != null) {
				try {
					reader.close();
				} catch (final IOException e) {
					Log.e(LOG_TAG, "Error closing stream", e);
				}
			}
		}

		try {
			return GetData(dataJsonStr);

		} catch (JSONException e) {
			Log.e(LOG_TAG, e.getMessage(), e);
			e.printStackTrace();
		}

		// This will only happen if there was an error getting or parsing the
		// data.
		return null;
	}

	@Override
	protected void onPostExecute(String result) {
		Log.v(LOG_TAG, "result" + result);

		//Update Ui Thread
		rsetForm(result);
	}
	
	public void rsetForm(String response) {
		
		EditText name = (EditText) activity.findViewById(R.id.edittextName);
		EditText email = (EditText) activity.findViewById(R.id.edittextemail);
		EditText phone = (EditText) activity.findViewById(R.id.edittextphone);
		EditText body = (EditText) activity.findViewById(R.id.edittextbody);
		//Check if Message was sent and clear the form
		if (response != null && !response.isEmpty() ) {
			name.setText("");
			email.setText("");
			phone.setText("");
			body.setText("");
		}else {
			response = "Connection problem. Please try later";
		}
		
		//Remove progrees dialog and display Toast Response
		progress.dismiss();
		((ContactActivity) activity).showToast(response);
	}

}
