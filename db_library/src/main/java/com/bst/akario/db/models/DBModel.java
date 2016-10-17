package com.bst.akario.db.models;

import net.sqlcipher.database.SQLiteDatabase;
import net.sqlcipher.database.SQLiteDatabase.CursorFactory;
import net.sqlcipher.database.SQLiteOpenHelper;
import android.content.Context;

import com.bst.akario.db.TableOperator;

public class DBModel extends SQLiteOpenHelper {

	public static final int TRUE_INT = 1;
	public static final int FALSE_INT = 2;
	public static final String DATABASE_KEY = "youshouldknowthis";
	public static final byte[] SQLITE_LOCK = new byte[0];

	private SQLiteDatabase db;

	public DBModel(Context context, String name, CursorFactory factory,
			int version) {
		super(context, name, factory, version);
	}

	protected String getReadLogKey() {
		return "DBModel Read Operator:";
	}

	protected String getWriteLogKey() {
		return "DBModel Write Operator:";
	}

	private SQLiteDatabase getSQLiteDatabase() {
		if (db == null || !db.isOpen()) {
			db = getWritableDatabase(DATABASE_KEY);
		}
		return db;
	}

	public boolean readOperator(TableOperator operator) {
		boolean ret = false;
		synchronized (SQLITE_LOCK) {
			SQLiteDatabase db = getSQLiteDatabase();

			try {
				db.beginTransaction();
				operator.doWork(db);
				db.setTransactionSuccessful();
				ret = true;
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				if (db != null) {
					db.endTransaction();
				}
			}

			return ret;
		}
	}

	public boolean writeOperator(TableOperator operator) {
		boolean ret = false;

		synchronized (SQLITE_LOCK) {
			SQLiteDatabase db = getSQLiteDatabase();

			try {
				db.beginTransaction();
				operator.doWork(db);
				db.setTransactionSuccessful();
				ret = true;
			} catch (Exception e) {
			} finally {
				if (db != null) {
					db.endTransaction();
				}
			}

			return ret;
		}
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
	}
}
