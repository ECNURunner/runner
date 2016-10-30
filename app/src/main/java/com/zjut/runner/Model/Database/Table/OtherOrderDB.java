package com.zjut.runner.Model.Database.Table;

import android.content.Context;

import com.bst.akario.db.models.DBModel;

import net.sqlcipher.database.SQLiteDatabase;

/**
 * Created by Administrator on 2016/10/28.
 */

public class OtherOrderDB extends DBModel {

    private static OtherOrderDB instance;
    private static final int VERSION = 3;
    private static final String DBNAME = "otherorder.db";

    public OtherOrderDB(Context context){
        super(context,DBNAME,null,VERSION);
    }

    public static OtherOrderDB getInstance(Context context){
        if(instance == null)
            instance = new OtherOrderDB(context);
        return instance;
    }

    public static final String TABLE_ORDER = "table_allorder";

    public static final String KEY_ID = "_id";
    public static final String KEY_OBJECT_ID = "_objectID";
    public static final String KEY_REPLY_ID = "_replyID";
    public static final String KEY_STUDENT_ID = "_studentID";
    public static final String KEY_REMARK = "_remark";
    public static final String KEY_ORDER_DATE = "_orderDate";
    public static final String KEY_DEADLINE = "_deadline";
    public static final String KEY_TITLE = "_title";
    public static final String KEY_DEST = "_dest";
    public static final String KEY_CHARGE = "_charge";
    public static final String KEY_CARD_ID = "card_id";
    public static final String KEY_NAME = "card_name";
    public static final String KEY_MOBILE = "acc_mobile";
    public static final String KEY_EMAIL = "acc_email";
    public static final String KEY_GENDER = "acc_gender";
    public static final String KEY_URL = "profile_url";
    public static final String KEY_OWNER_USERID = "owner_userID";
    public static final String KEY_NUM_HELPER = "_numHelper";

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL( "CREATE TABLE IF NOT EXISTS " + TABLE_ORDER
                + " ("
                + KEY_ID + " integer primary key autoincrement, "
                + KEY_OBJECT_ID + " varchar(25), "
                + KEY_REPLY_ID + " varchar(25) unique,"
                + KEY_OWNER_USERID + " varchar(25), "
                + KEY_REMARK + " varchar(100), "
                + KEY_CHARGE + " int, "
                + KEY_STUDENT_ID + " varchar(15), "
                + KEY_CARD_ID + " varchar(15), "
                + KEY_NAME + " varchar(25), "
                + KEY_GENDER + " varchar(7), "
                + KEY_MOBILE + " varchar(12), "
                + KEY_EMAIL + " varchar(60), "
                + KEY_URL + " varchar(100), "
                + KEY_ORDER_DATE + " varchar(20), "
                + KEY_DEADLINE + " varchar(15), "
                + KEY_TITLE + " varchar(25), "
                + KEY_NUM_HELPER + " int, "
                + KEY_DEST + " varchar(20))"
        );
    }
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_ORDER);
        onCreate(db);
    }

    @Override
    protected String getReadLogKey() {
        return "AllOrderDB Read Operator:";
    }

    @Override
    protected String getWriteLogKey() {
        return "AllOrderDB Write Operator:";
    }

}
