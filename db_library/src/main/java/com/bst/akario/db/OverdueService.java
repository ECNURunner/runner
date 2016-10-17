package com.bst.akario.db;

import java.util.ArrayList;
import java.util.Collection;

import net.sqlcipher.Cursor;
import net.sqlcipher.database.SQLiteDatabase;
import android.content.ContentValues;
import android.content.Context;

import com.bst.akario.db.models.OverdueDB;
import com.bst.akario.model.OverdueModel;

/**
 * 
 * @author will.Wu
 * 
 */
public class OverdueService {
	private OverdueDB mOverdueDB;

	public OverdueService(Context context) {
		mOverdueDB = OverdueDB.getInstance(context);
	}

	public boolean addOverdueUrl(String url, String maxAge) {
		return insertOverdueUrl(url, maxAge);
	}

	/**
	 * @author will.Wu
	 * @param userName
	 *            (email or mobile phone)
	 * @return
	 */
	public OverdueModel getOverdueMaxAgeByUrl(String url) {
		return getSavedOverdueByUrl(url);
	}

	private boolean insertOverdueUrl(final String url, final String maxAge) {
		synchronized (mOverdueDB) {
			TableOperator readOperator = new TableOperator() {

				@Override
				public void doWork(SQLiteDatabase db) {
					ContentValues contentValues = new ContentValues();
					contentValues.put(OverdueDB.KEY_URL, url);
					contentValues.put(OverdueDB.KEY_GET_TIME,
							System.currentTimeMillis());
					contentValues.put(OverdueDB.KEY_MAX_AGE, maxAge);
					db.insertWithOnConflict(OverdueDB.TABLE_OVERDUE_MODEL,
							null, contentValues,
							SQLiteDatabase.CONFLICT_REPLACE);
				}
			};
			return mOverdueDB.writeOperator(readOperator);
		}
	}

	final static String getSavedMaxAgeByUrlQuery = "SELECT * FROM "
			+ OverdueDB.TABLE_OVERDUE_MODEL + " WHERE " + OverdueDB.KEY_URL
			+ "=?";

	private OverdueModel getSavedOverdueByUrl(final String url) {
		synchronized (mOverdueDB) {
			final Collection<OverdueModel> overdueModels = new ArrayList<OverdueModel>();

			TableOperator readOperator = new TableOperator() {

				@Override
				public void doWork(SQLiteDatabase db) {
					Cursor cursor = db.rawQuery(getSavedMaxAgeByUrlQuery,
							new String[] { url });

					if (cursor.moveToFirst()) {
						do {
							try {
								long maxAge = cursor.getLong(cursor
										.getColumnIndex(OverdueDB.KEY_MAX_AGE));
								long getTime = cursor
										.getLong(cursor
												.getColumnIndex(OverdueDB.KEY_GET_TIME));
								overdueModels.add(new OverdueModel(getTime,
										maxAge, url));
							} catch (Exception e) {
							}
						} while (cursor.moveToNext());
					}
					cursor.close();
				}
			};
			mOverdueDB.readOperator(readOperator);

			if (overdueModels.size() <= 0) {
				return null;
			}
			return overdueModels.iterator().next();
		}
	}
}
