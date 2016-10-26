package com.zjut.runner.Model.Database.Table;

import android.content.Context;

import com.bst.akario.db.models.DBModel;

import net.sqlcipher.database.SQLiteDatabase;

/**
 * Created by Phuylai on 2016/10/26.
 */

public class MyOrderDB extends DBModel {

    private static MyOrderDB instance;
    private static final int VERSION = 2;
    private static final String DBNAME = "myorder.db";

    public MyOrderDB(Context context){
        super(context,DBNAME,null,VERSION);
    }

    public static MyOrderDB getInstance(Context context){
        if(instance == null)
            instance = new MyOrderDB(context);
        return instance;
    }

    public static final String TABLE_MYORDER = "table_myorder";
    public static final String TABLE_HELPERS = "table_helper";

    public static final String KEY_ID = "_id";
    public static final String KEY_OBJECT_ID = "_objectID";
    public static final String KEY_STUDENT_ID = "_studentID";
    public static final String KEY_REMARK = "_remark";
    public static final String KEY_CHOSEN = "_chosen";
    public static final String KEY_ORDER_DATE = "_orderDate";
    public static final String KEY_DEADLINE = "_deadline";
    public static final String KEY_TITLE = "_title";
    public static final String KEY_DEST = "_dest";
    public static final String KEY_CHARGE = "_charge";
    public static final String KEY_FINAL_CHARGE = "_finalCharge";
    public static final String USER_OBJECT_ID = "userObjectId";
    public static final String KEY_CARD_ID = "card_id";
    public static final String KEY_NAME = "card_name";
    public static final String KEY_MOBILE = "acc_mobile";
    public static final String KEY_EMAIL = "acc_email";
    public static final String KEY_GENDER = "acc_gender";
    public static final String KEY_URL = "profile_url";
    public static final String KEY_STATUS ="_status";
    public static final String KEY_NUM_HELPER = "_numHelper";

    public static final String HELPER_CAMPUS_ID = "objectId";
    public static final String HELPER_USER_ID = "userObjectId";
    public static final String HELPER_CARD_ID = "card_id";
    public static final String HELPER_NAME = "card_name";
    public static final String HELPER_MOBILE = "acc_mobile";
    public static final String HELPER_EMAIL = "acc_email";

    @Override
    protected String getReadLogKey() {
        return "MyOrderDB Read Operator:";
    }

    @Override
    protected String getWriteLogKey() {
        return "MyOrderDB Write Operator:";
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL( "CREATE TABLE IF NOT EXISTS " + TABLE_MYORDER
                + " ("
                + KEY_ID + " integer primary key autoincrement, "
                + KEY_OBJECT_ID + " varchar(25) unique, "
                + KEY_REMARK + " varchar(100), "
                + KEY_CARD_ID + " varchar(15), "
                + KEY_CHARGE + " int, "
                + KEY_CHOSEN + " boolean, "
                + KEY_FINAL_CHARGE + " int, "
                + KEY_URL + " varchar(100), "
                + USER_OBJECT_ID + " varchar(25), "
                + KEY_NAME + " varchar(25), "
                + KEY_GENDER + " varchar(7), "
                + KEY_MOBILE + " varchar(12), "
                + KEY_EMAIL + " varchar(60), "
                + KEY_STUDENT_ID + " varchar(15), "
                + KEY_ORDER_DATE + " varchar(20), "
                + KEY_DEADLINE + " varchar(15), "
                + KEY_TITLE + " varchar(25), "
                + KEY_STATUS + " varchar(10), "
                + KEY_NUM_HELPER + " int, "
                + KEY_DEST + " varchar(20))"
        );

        db.execSQL(
                "CREATE TABLE IF NOT EXISTS " + TABLE_HELPERS
                        + " ("
                        + KEY_ID + " integer primary key autoincrement, "
                        + KEY_OBJECT_ID + " varchar(20), "
                        + HELPER_CARD_ID + " varchar(20), "
                        + HELPER_CAMPUS_ID + " varchar(25) unique, "
                        + HELPER_USER_ID + " varchar(25) unique, "
                        + HELPER_NAME + " varchar(60), "
                        + HELPER_MOBILE + " varchar(20), "
                        + HELPER_EMAIL + " varchar(60), "
                        + KEY_GENDER + " varchar(10), "
                        + KEY_URL + " varchar(100), FOREIGN KEY ("
                        + KEY_OBJECT_ID + ")"
                        + " REFERENCES "
                        + TABLE_MYORDER
                        + "(" + KEY_OBJECT_ID + "))"
        );
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_MYORDER);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_HELPERS);
        onCreate(db);
    }


}
