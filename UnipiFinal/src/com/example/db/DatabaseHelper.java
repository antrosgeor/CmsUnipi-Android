package com.example.db;

import com.example.data.models.BackupModel.BackupModelEntry;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper 
	extends SQLiteOpenHelper{
	
	// If you change the database schema, you must increment the database version.
    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "unipitest.db";

    public static final String TEXT_TYPE = " TEXT";
    public static final String COMMA_SEP = ",";
    public static final String EQUAL = "=";
    
    public static final String SELECT_ALL = "*";
    
    public static final String DESC = " DESC";
    public static final String ASC = " ASC";
    
    
    
    private static final String SQL_CREATE_TABLES =
        "CREATE TABLE " + BackupModelEntry.TABLE_NAME + " (" +
        BackupModelEntry.COLUMN_TYPE + TEXT_TYPE + COMMA_SEP +
        BackupModelEntry.COLUMN_DATA + TEXT_TYPE + COMMA_SEP +
        BackupModelEntry.COLUMN_TIMESTAMP + TEXT_TYPE +
        " )";

    private static final String SQL_DELETE_TABLES =
        "DROP TABLE IF EXISTS " + BackupModelEntry.TABLE_NAME;

	/**
	 * @param context
	 */
	public DatabaseHelper(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL(SQL_CREATE_TABLES);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		db.execSQL(SQL_DELETE_TABLES);
        onCreate(db);
	}

}
