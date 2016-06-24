package com.example.data.models;

import java.io.Serializable;
import java.util.Date;

import android.content.ContentValues;
import android.provider.BaseColumns;

public class BackupModel 
	implements Serializable
{
	private static final long serialVersionUID = 1L;
	
	private String type;
	private Date timestamp;
	private String data;

	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public Date getTimestamp() {
		return timestamp;
	}
	public void setTimestamp(Date timestamp) {
		this.timestamp = timestamp;
	}
	public String getData() {
		return data;
	}
	public void setData(String data) {
		this.data = data;
	}
	public BackupModel() {}
	
	public BackupModel(Date timestamp, String type, String data){
		this.timestamp = timestamp;
		this.type = type;
		this.data = data;
	}
	
	public static abstract class BackupModelEntry 
		implements BaseColumns
	{
	    public static final String TABLE_NAME = "bup";
	    public static final String COLUMN_TIMESTAMP = "timestamp";
	    public static final String COLUMN_TYPE = "type";
	    public static final String COLUMN_DATA = "data";

	    private static String[] projection = {
	    	COLUMN_TIMESTAMP,
	    	COLUMN_TYPE,
	    	COLUMN_DATA,
	    };
	    
	    public static String[] getDefaultProjection(){
	    	return projection;
	    }
	}
	
	private transient ContentValues values = new ContentValues();
    
    public ContentValues getContentValues()
    {	
    	values.put(BackupModelEntry.COLUMN_TIMESTAMP, this.timestamp.getTime());
    	values.put(BackupModelEntry.COLUMN_TYPE, this.type);
    	values.put(BackupModelEntry.COLUMN_DATA, this.data);

    	
    	return this.values;
    }
	
}
