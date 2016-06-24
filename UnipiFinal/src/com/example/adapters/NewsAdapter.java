package com.example.adapters;

import java.util.ArrayList;

import com.example.data.models.News;
import com.example.unipitest.R;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;


public class NewsAdapter extends ArrayAdapter<News> {


	public NewsAdapter(Context context, ArrayList<News> news) {
	       super(context, 0, news);
	    }
	
	
	@Override
    public View getView(int position, View convertView, ViewGroup parent) {
       // Get the data item for this position
		News news = getItem(position);  
       
       // Check if an existing view is being reused, otherwise inflate the view
       if (convertView == null) {
          convertView = LayoutInflater.from(getContext()).inflate(R.layout.news_item, parent, false);
       }
       
       // Lookup view for data population
       TextView title = (TextView) convertView.findViewById(R.id.list_item_news);
       TextView body = (TextView) convertView.findViewById(R.id.list_item_news_body);
       TextView date = (TextView) convertView.findViewById(R.id.list_item_news_date);

       // Populate the data into the template view using the data object
       title.setText(news.getNewsTitle());
       body.setText(news.getNewsBody());
       date.setText(news.getNewsDate());

       // Return the completed view to render on screen
       return convertView;
   }

}
