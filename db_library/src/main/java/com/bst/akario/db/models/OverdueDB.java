package com.bst.akario.db.models;

import net.sqlcipher.database.SQLiteDatabase;
import android.content.Context;

/**
 * 
 * @author will.Wu
 * 
 */	
public class OverdueDB extends DBModel {
	private static OverdueDB mInstance;
	private static final int VERSION = 1;
	private static final String DBNAME = "overdue.db";

	public static final String TABLE_OVERDUE_MODEL = "overdue_data";

	public static final String KEY_ID = "_id";
	public static final String KEY_URL = "_url";
	public static final String KEY_GET_TIME = "get_time";
	public static final String KEY_MAX_AGE = "max_age";

	private OverdueDB(Context context) {
		super(context, DBNAME, null, VERSION);
	}

	public static OverdueDB getInstance(Context context) {
		if (mInstance == null) {
			mInstance = new OverdueDB(context);
		}
		return mInstance;
	}

	@Override
	protected String getReadLogKey() {
		return "Favorite Read Operator:";
	}

	@Override
	protected String getWriteLogKey() {
		return "Favorite Write Operator:";
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL("CREATE TABLE IF NOT EXISTS " + TABLE_OVERDUE_MODEL + " ("
				+ KEY_ID + " integer primary key autoincrement, " + KEY_URL
				+ " varchar(100) UNIQUE, " + KEY_GET_TIME + " varchar(100),"
				+ KEY_MAX_AGE + " varchar(100))");
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_OVERDUE_MODEL);
		onCreate(db);
	}

}
