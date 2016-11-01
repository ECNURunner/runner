package com.zjut.runner.Model.Database.Table;

import android.content.Context;

import com.bst.akario.db.models.DBModel;

import net.sqlcipher.database.SQLiteDatabase;

/**
 * Created by Phuylai on 2016/10/31.
 */

public class RunDB extends DBModel{
    private static RunDB instance;
    private static final int VERSION = 1;
    private static final String DBNAME = "run.db";

    public RunDB(Context context){
       super(context,DBNAME,null,VERSION);
    }

    public static RunDB getInstance(Context context){
        if(instance == null)
            instance = new RunDB(context);
        return instance;
    }

    public static final String TABLE_RUN = "table_run";

    public static final String KEY_ID = "_id";
    public static final String KEY_OBJECT_ID = "_objectID";
    public static final String KEY_REQUEST_ID = "_requestID";
    public static final String KEY_STUDENT_ID = "_studentID";
    public static final String KEY_REMARK = "_remark";
    public static final String KEY_ORDER_DATE = "_orderDate";
    public static final String KEY_DEADLINE = "_deadline";
    public static final String KEY_TITLE = "_title";
    public static final String KEY_DEST = "_dest";
    public static final String KEY_CHARGE = "_charge";
    public static final String KEY_FINAL_CHARGE = "_finalCharge";
    public static final String KEY_NAME = "card_name";
    public static final String KEY_MOBILE = "acc_mobile";
    public static final String KEY_EMAIL = "acc_email";
    public static final String KEY_GENDER = "acc_gender";
    public static final String KEY_URL = "profile_url";
    public static final String KEY_STATUS ="_status";
    public static final String KEY_INSTALL = "_install";

    @Override
    protected String getReadLogKey() {
        return "Run Read Operator:";
    }

    @Override
    protected String getWriteLogKey() {
        return "Run Write Operator:";
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL( "CREATE TABLE IF NOT EXISTS " + TABLE_RUN
                + " ("
                + KEY_ID + " integer primary key autoincrement, "
                + KEY_OBJECT_ID + " varchar(25), "
                + KEY_REQUEST_ID + " varchar(25) unique, "
                + KEY_REMARK + " varchar(100), "
                + KEY_CHARGE + " int, "
                + KEY_STUDENT_ID + " varchar(15), "
                + KEY_INSTALL + " varchar(50), "
                + KEY_NAME + " varchar(25), "
                + KEY_GENDER + " varchar(7), "
                + KEY_MOBILE + " varchar(12), "
                + KEY_EMAIL + " varchar(60), "
                + KEY_URL + " varchar(100), "
                + KEY_ORDER_DATE + " varchar(20), "
                + KEY_DEADLINE + " varchar(15), "
                + KEY_STATUS + " varchar(10), "
                + KEY_FINAL_CHARGE + " int, "
                + KEY_TITLE + " varchar(25), "
                + KEY_DEST + " varchar(20))"
        );
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_RUN);
        onCreate(db);
    }

}
