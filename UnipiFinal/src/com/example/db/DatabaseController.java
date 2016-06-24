package com.example.db;

import java.util.Date;

import com.example.data.models.BackupModel;
import com.example.data.models.BackupModel.BackupModelEntry;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class DatabaseController {

	private SQLiteDatabase db;
	private DatabaseHelper dbHelper;
	/**
	 * 
	 */
	public DatabaseController(Context context) {
		 this.dbHelper = new DatabaseHelper(context);
	}
	
	public void insertEntry(BackupModel entry) {

		if(entry==null)
			return;
		
		SQLiteDatabase db = dbHelper.getWritableDatabase();	
		ContentValues values = entry.getContentValues();
			
		db.insert(BackupModelEntry.TABLE_NAME, null, values);
		
		db.close();
	}
	
	public void updateEntry(BackupModel entry) {

		if(entry==null)
			return;
		
		SQLiteDatabase db = dbHelper.getWritableDatabase();	
		ContentValues values = entry.getContentValues();

		//db.update(BackupModelEntry.TABLE_NAME,  values, BackupModelEntry.COLUMN_TYPE+DatabaseHelper.EQUAL+entry.getType(), null);
		db.update(BackupModelEntry.TABLE_NAME,  values,null, null);
		db.close();
	}
	
	public void deleteEntry(String type) {
		
		if(type==null || type.equals(""))
			return;
		
		SQLiteDatabase db = dbHelper.getWritableDatabase();	
			
		db.delete(BackupModelEntry.TABLE_NAME, BackupModelEntry.COLUMN_TYPE+DatabaseHelper.EQUAL+type, null);
		
		db.close();
	}
	
	public BackupModel getEntry(String type) {
		
		if(type==null || type.equals(""))
			return null;
		
		BackupModel result = null;
		
		SQLiteDatabase db = dbHelper.getReadableDatabase();

		Cursor c = db.query(
				BackupModelEntry.TABLE_NAME, 
				BackupModelEntry.getDefaultProjection(), 
				BackupModelEntry.COLUMN_TYPE + DatabaseHelper.EQUAL +"'"+ type +"'",
				null,null,
				null,null,null);

		if(c.moveToFirst()) 
		{		

			do{
            	
				result = new BackupModel(
	            			new Date(c.getLong(c.getColumnIndex(BackupModelEntry.COLUMN_TIMESTAMP))),
	            			c.getString(c.getColumnIndex(BackupModelEntry.COLUMN_TYPE)),
	            			c.getString(c.getColumnIndex(BackupModelEntry.COLUMN_DATA))
						);
                 
            }while(c.moveToNext());
        }
		
        c.close();
        db.close();
		
		return result;
	}
}
