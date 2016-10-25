package com.zjut.runner.Model.Database.Table;

import android.content.Context;

import com.bst.akario.db.models.DBModel;

import net.sqlcipher.database.SQLiteDatabase;

/**
 * Created by Administrator on 2016/10/21.
 */

public class CampusModelDB extends DBModel{

    private static CampusModelDB mInstance;
    private static final int VERSION = 1;
    private static final String DBNAME = "campusmodel.db";

    public static final String TABLE_CAMPUS = "campus_data";

    public static final String KEY_ID = "_id";
    public static final String OBJECT_ID = "objectId";
    public static final String USER_OBJECT_ID = "userObjectId";
    public static final String KEY_CARD_ID = "card_id";
    public static final String KEY_CARD_PASS = "card_pass";
    public static final String KEY_NAME = "card_name";
    public static final String KEY_MOBILE = "acc_mobile";
    public static final String KEY_EMAIL = "acc_email";
    public static final String KEY_DIS_NAME = "acc_name";
    public static final String KEY_GENDER = "acc_gender";
    public static final String KEY_URL = "profile_url";
    public static final String KEY_BALANCE = "card_balance";

    private CampusModelDB(Context context){
        super(context,DBNAME,null,VERSION);
    }

    public static CampusModelDB getInstance(Context context) {
        if (mInstance == null) {
            mInstance = new CampusModelDB(context);
        }
        return mInstance;
    }

    @Override
    protected String getReadLogKey() {
        return "CampusDB Read Operator:";
    }

    @Override
    protected String getWriteLogKey() {
        return "CampusDB Write Operator:";
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(
                "CREATE TABLE IF NOT EXISTS " + TABLE_CAMPUS
                        + " ("
                        + KEY_ID + " integer primary key autoincrement, "
                        + KEY_CARD_ID + " varchar(20), "
                        + OBJECT_ID + " varchar(25) unique, "
                        + USER_OBJECT_ID + " varchar(25) unique, "
                        + KEY_CARD_PASS + " varchar(20), "
                        + KEY_NAME + " varchar(60), "
                        + KEY_MOBILE + " varchar(20), "
                        + KEY_EMAIL + " varchar(60), "
                        + KEY_DIS_NAME + " varchar(30), "
                        + KEY_BALANCE + " float, "
                        + KEY_GENDER + " varchar(10), "
                        + KEY_URL + " varchar(100), "
                        + "CONSTRAINT"
                        + " unique_id"
                        + " UNIQUE"
                        + " ("
                        + KEY_CARD_ID + ","
                        + KEY_MOBILE
                        + ")"
                        + ")"
        );
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_CAMPUS);
        onCreate(db);
    }
}
