package com.example.support;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class SupportFunctions {

	
	public Date getParsedDate(String timestamp) {
    	Date date = null;
    	SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
    	try {
    		if(timestamp!=null)
    			date = formatter.parse(timestamp);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	return date;
    }
	
}
