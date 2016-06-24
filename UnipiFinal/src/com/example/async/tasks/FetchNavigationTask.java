package com.example.async.tasks;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Date;
import com.example.async.tasks.WebServiceSettings;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.example.data.models.BackupModel;
import com.example.db.CmsTypesConstrains;
import com.example.db.DatabaseController;

import com.example.support.SupportFunctions;
import android.content.Context;
import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.ArrayAdapter;


public class FetchNavigationTask extends AsyncTask<String, Void, String[]> {

    private final String LOG_TAG = FetchNavigationTask.class.getSimpleName();
    private SupportFunctions support = new SupportFunctions();

	
	private ArrayAdapter<String>  mDataAdapter;
    private DatabaseController dbController;
    
    public FetchNavigationTask(Context context, ArrayAdapter<String> mDataAdapter) {
		super();
		this.mDataAdapter = mDataAdapter;
		this.dbController = new DatabaseController(context);
	}


	// Take the String representing the JSON Response and pull out the data we need to construct 
    // the Strings Array we need. constructor takes the JSON string and converts it into an Object hierarchy.
    private String[] GetData(String jsonstr)throws JSONException {
    	
        JSONObject  mainObject = new JSONObject(jsonstr);
        JSONArray   dataArray = mainObject.getJSONArray("data");
    	String[] navigation = new String[dataArray.length()];;

        for (int i = 0; i < dataArray.length(); i++) {
            JSONObject pageObject = dataArray.getJSONObject(i);
            String pageTitle = pageObject.getString("label");
            navigation[i] = pageTitle;
        }
        
        return navigation;
    }
    
    
    @Override
    protected String[] doInBackground(String... params) {

        // If there are no params there's nothing to look up.  Verify size of params.
        if (params.length == 0) {
            return null;
        }
        
        String[] JsonData = null;
        String status = getUpdatedStatus();
        
        // Log last updade date 
        Log.v(LOG_TAG, "Last Update: " + status);
        
        BackupModel bup = dbController.getEntry(CmsTypesConstrains.CMS_NAVIGATION);
        
        try {
	        if (bup == null) {
	            Log.v(LOG_TAG, "Bup = null " + bup);
	        	String result = getNavigation();
	        	if(result == null) return null;
	        	Date jsonDate = support.getParsedDate(status);
	        	JsonData = GetData(result);				
	        	dbController.insertEntry(
	        			new BackupModel(
	        					jsonDate, 
	        					CmsTypesConstrains.CMS_NAVIGATION, 
	        					result));
			}
	        else {
	            Log.v(LOG_TAG, "Bup != null : " + bup);
	        	//Parse Date from json to Date object
	        	Date jsonDate =  support.getParsedDate(status);

	        	if(jsonDate!=null) {
		        	if(bup.getTimestamp().before(jsonDate)) {
			            Log.v(LOG_TAG, "Db Data outdated " + bup);
		        		String result = getNavigation();
		        		JsonData = GetData(getNavigation());
		        		bup.setData(result);
		        		bup.setTimestamp(jsonDate);
		        		dbController.updateEntry(bup);
		        	}
		        	else {
			            Log.v(LOG_TAG, "Db Data up to date " + bup);
		        		JsonData = GetData(bup.getData());
		        	}
	        	}
	        	else {
	        		JsonData = GetData(bup.getData());
	        		Log.d(LOG_TAG, bup.getData());
	        	}
	        }
        
        } catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
         
        return JsonData;
    }

    
    @Override
    protected void onPostExecute(String[] result) {
        mDataAdapter.clear();
        if (result != null) {
	        for(String dataStr : result) {
	        	Log.v(LOG_TAG, "onPostExecute  " + dataStr );  
	        	mDataAdapter.add(dataStr);
	        }
        	mDataAdapter.add("Find Us");
        }
    }
    
    
    
    protected String getNavigation(){
    	
    	// These two need to be declared outside the try/catch
        // so that they can be closed in the finally block.
        HttpURLConnection urlConnection = null;
        BufferedReader reader = null;

        // Will contain the raw JSON response as a string.
        String dataJsonStr = null;
    	
    	 try {

         	 //Make url 
             final String BASE_URL = WebServiceSettings.HOST_URL_GET_NAVIGATION;
             Uri builtUri = Uri.parse(BASE_URL).buildUpon().build();;
             URL url = new URL(builtUri.toString());
             
             //log Url String
             Log.v(LOG_TAG, "Built URI " + builtUri.toString());
             
             // Create  request to RestApi, and open the connection
             urlConnection = (HttpURLConnection) url.openConnection();
             urlConnection.setRequestMethod("GET");
             urlConnection.connect();
             
             // get the response code, returns 200 if it's OK
             int responseCode = urlConnection.getResponseCode();
             
             //log connection
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
                 // Since it's JSON, adding a newline isn't necessary (it won't affect parsing)
                 // But it does make debugging a *lot* easier if you print out the completed
                 // buffer for debugging.
                 buffer.append(line + "\n");
             }

             if (buffer.length() == 0) {
                 // Stream was empty.  No point in parsing.
                 return null;
             }
             dataJsonStr = buffer.toString();
             
             //log Json String
             Log.v(LOG_TAG, "DATA JSON" + dataJsonStr);

         } catch (IOException e) {
             Log.e(LOG_TAG, "Error Fetching Data", e);
             // If the code didn't successfully get data, there's no point in attemping to parse it.
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
    	
         return dataJsonStr;	
    }
    
    
    protected String getUpdatedStatus(){
    	
        HttpURLConnection urlConnection = null;
        BufferedReader reader = null;
        String dataJsonStr = null;
    	
    	 try {

         	 //Make url 
             final String BASE_URL = WebServiceSettings.HOST_URL_GET_NAVIGATION_UPDATE;
             Uri builtUri = Uri.parse(BASE_URL).buildUpon().build();;
             URL url = new URL(builtUri.toString());
             
             //log Url String
             Log.v(LOG_TAG, "getUpdatedStatus - Built URI " + builtUri.toString());
             
             // Create  request to RestApi, and open the connection
             urlConnection = (HttpURLConnection) url.openConnection();
             urlConnection.setRequestMethod("GET");
             urlConnection.connect();
             
             // get the response code, returns 200 if it's OK
             int responseCode = urlConnection.getResponseCode();
             
             //log connection
             Log.v(LOG_TAG, "getUpdatedStatus - connection" + responseCode);
 			

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
                 // Since it's JSON, adding a newline isn't necessary (it won't affect parsing)
                 // But it does make debugging a *lot* easier if you print out the completed
                 // buffer for debugging.
                 buffer.append(line + "\n");
             }

             if (buffer.length() == 0) {
                 // Stream was empty.  No point in parsing.
                 return null;
             }
             dataJsonStr = buffer.toString();
             
             //log Json String
             Log.v(LOG_TAG, "getUpdatedStatus - DATA JSON" + dataJsonStr);

         } catch (IOException e) {
             Log.e(LOG_TAG, "getUpdatedStatus - Error Fetching Data", e);
             // If the code didn't successfully get data, there's no point in attemping to parse it.
             return null;
         } finally {
             if (urlConnection != null) {
                 urlConnection.disconnect();
             }
             if (reader != null) {
                 try {
                     reader.close();
                 } catch (final IOException e) {
                     Log.e(LOG_TAG, "getUpdatedStatus - Error closing stream", e);
                 }
             }
         }
    	 
    	 try {
    		 
    		 JSONObject  mainObject = new JSONObject(dataJsonStr);
    	     JSONArray   dataArray = mainObject.getJSONArray("data");
    	     JSONObject date = dataArray.getJSONObject(0);
    	     String updated = date.getString("date");    	        
    		 
             return updated;
             
         } catch (JSONException e) {
             Log.e(LOG_TAG, e.getMessage(), e);
             e.printStackTrace();
         }
		return null;
    	
    	    	
    }
}

