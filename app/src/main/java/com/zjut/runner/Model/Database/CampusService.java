package com.zjut.runner.Model.Database;

import android.content.ContentValues;
import android.content.Context;

import com.bst.akario.db.TableOperator;
import com.zjut.runner.Model.CampusModel;
import com.zjut.runner.Model.Database.Table.CampusModelDB;
import com.zjut.runner.Model.GenderType;
import com.zjut.runner.util.MLog;

import net.sqlcipher.database.SQLiteDatabase;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Created by Administrator on 2016/10/21.
 */

public class CampusService {

    private CampusModelDB campusModelDB;

    private static final ContentValues contentValues = new ContentValues();

    public CampusService(Context context) {
        campusModelDB = CampusModelDB.getInstance(context);
    }

    final static String getSavedCampusModelCardID =
            "SELECT "
                    + "*"
                    + " FROM "
                    + CampusModelDB.TABLE_CAMPUS
                    + " WHERE "
                    + CampusModelDB.KEY_CARD_ID + " =?";

    final static String getSavedCampusModelPhone =
            "SELECT "
                    + "*"
                    + " FROM "
                    + CampusModelDB.TABLE_CAMPUS
                    + " WHERE "
                    + CampusModelDB.KEY_MOBILE + " =?";

    public Collection<CampusModel> loadCampusModel(final String phoneNumber){
        final List<CampusModel> campusModels = new ArrayList<>();
        TableOperator readOperator = new TableOperator() {
            @Override
            public void doWork(SQLiteDatabase db) {

                net.sqlcipher.Cursor cursor = db.rawQuery(getSavedCampusModelPhone,
                        new String[]{phoneNumber});
                if(cursor.moveToFirst()){
                    do{
                        try{
                            CampusModel campusModel = getSavedCampusModel(cursor);
                            if(campusModel != null){
                                campusModels.add(campusModel);
                            }
                        }catch (Exception e){
                            e.printStackTrace();
                        }
                    }while(cursor.moveToNext());
                }
                cursor.close();
            }
        };
        campusModelDB.readOperator(readOperator);
        return campusModels;
    }

    private CampusModel getSavedCampusModel(net.sqlcipher.Cursor cursor) {
        if(cursor == null){
            return null;
        }

        String cardID = cursor.getString(
                cursor.getColumnIndex(CampusModelDB.KEY_CARD_ID));

        String cardPass = cursor.getString(
                cursor.getColumnIndex(CampusModelDB.KEY_CARD_PASS));

        String cardName = cursor.getString(
                cursor.getColumnIndex(CampusModelDB.KEY_NAME));

        String mobile = cursor.getString(
                cursor.getColumnIndex(CampusModelDB.KEY_MOBILE));

        String email = cursor.getString(
                cursor.getColumnIndex(CampusModelDB.KEY_EMAIL));

        String displayName = cursor.getString(
                cursor.getColumnIndex(CampusModelDB.KEY_DIS_NAME));

        String url = cursor.getString(
                cursor.getColumnIndex(CampusModelDB.KEY_URL));

        GenderType genderType = GenderType.getType(cursor.getString(
                cursor.getColumnIndex(CampusModelDB.KEY_GENDER)));

        float balance = cursor.getFloat(
                cursor.getColumnIndex(CampusModelDB.KEY_BALANCE));

        String objectId = cursor.getString(
                cursor.getColumnIndex(CampusModelDB.OBJECT_ID)
        );

        String userObjectId = cursor.getString(
                cursor.getColumnIndex(CampusModelDB.USER_OBJECT_ID)
        );

        MLog.i("CAMPUS DATA","FROM DB");
        return new CampusModel(objectId,userObjectId,displayName,mobile,email,genderType,url,cardPass,
                cardID,cardName,balance);
    }

    public boolean SaveCampusModelToDB(final CampusModel campusModel){
        synchronized (campusModelDB){
            TableOperator insertOperator = new TableOperator() {
                @Override
                public void doWork(SQLiteDatabase db) {
                    saveCampusModelToDB(db,campusModel);
                }
            };
            return campusModelDB.writeOperator(insertOperator);
        }
    }

    private boolean saveCampusModelToDB(SQLiteDatabase db, CampusModel campusModel) {
        contentValues.clear();
        contentValues.put(CampusModelDB.OBJECT_ID,campusModel.getObjectId());
        contentValues.put(CampusModelDB.USER_OBJECT_ID,campusModel.getUserObjectId());
        contentValues.put(CampusModelDB.KEY_CARD_ID, campusModel.getCampusID());
        contentValues.put(CampusModelDB.KEY_CARD_PASS, campusModel.getCardPass());
        contentValues.put(CampusModelDB.KEY_NAME, campusModel.getCampusName());
        contentValues.put(CampusModelDB.KEY_BALANCE,campusModel.getBalance());
        contentValues.put(CampusModelDB.KEY_DIS_NAME,campusModel.getUsername());
        contentValues.put(CampusModelDB.KEY_MOBILE,campusModel.getMobile());
        contentValues.put(CampusModelDB.KEY_EMAIL,campusModel.getEmail());
        if(campusModel.getGenderType() != null) {
            contentValues.put(CampusModelDB.KEY_GENDER, campusModel.getGenderType().toString());
        }
        contentValues.put(CampusModelDB.KEY_URL,campusModel.getUrl());
        db.insertWithOnConflict(CampusModelDB.TABLE_CAMPUS, null, contentValues, SQLiteDatabase.CONFLICT_REPLACE);
        MLog.i("Campus Data"," insert");
        return true;
    }

    public boolean removeCampusModelFromDB(
            final String phone
    ){
        synchronized (campusModelDB){
            TableOperator deleteOperator = new TableOperator() {
                @Override
                public void doWork(SQLiteDatabase db) {
                    db.delete(CampusModelDB.TABLE_CAMPUS,
                            CampusModelDB.KEY_MOBILE + " =?",new String[]{phone});
                }
            };
            MLog.i("Campus Data","Remove");
            return campusModelDB.writeOperator(deleteOperator);
        }
    }

    public boolean updateUserName(final String name,final String phone){
        synchronized (campusModelDB){
            TableOperator tableOperator = new TableOperator() {
                @Override
                public void doWork(SQLiteDatabase db) {
                    contentValues.clear();
                    contentValues.put(CampusModelDB.KEY_DIS_NAME,name);
                    db.update(CampusModelDB.TABLE_CAMPUS, contentValues,
                            CampusModelDB.KEY_DIS_NAME + " =?", new String[]{phone});
                }
            };
            MLog.i("Campus Data","Update Name");
            return campusModelDB.writeOperator(tableOperator);
        }
    }

    public boolean updateGender(final String gender,final String phone){
        synchronized (campusModelDB){
            TableOperator tableOperator = new TableOperator() {
                @Override
                public void doWork(SQLiteDatabase db) {
                    contentValues.clear();
                    contentValues.put(CampusModelDB.KEY_GENDER, gender);
                    db.update(CampusModelDB.TABLE_CAMPUS,contentValues,
                            CampusModelDB.KEY_GENDER + " =?",new String[]{phone});
                }
            };
            MLog.i("Campus Data","Update Gender");
            return campusModelDB.writeOperator(tableOperator);
        }
    }

    public boolean updateProfilePic(final String url,final String phone){
        synchronized (campusModelDB){
            TableOperator tableOperator = new TableOperator() {
                @Override
                public void doWork(SQLiteDatabase db) {
                    contentValues.clear();
                    contentValues.put(CampusModelDB.KEY_URL, url);
                    db.update(CampusModelDB.TABLE_CAMPUS,contentValues,
                            CampusModelDB.KEY_URL + " =?",new String[]{phone});
                }
            };
            MLog.i("Campus Data","Update Profile Pic");
            return campusModelDB.writeOperator(tableOperator);
        }
    }

    public boolean updateEmail(final String email,final String phone){
        synchronized (campusModelDB){
            TableOperator tableOperator = new TableOperator() {
                @Override
                public void doWork(SQLiteDatabase db) {
                    contentValues.clear();
                    contentValues.put(CampusModelDB.KEY_EMAIL, email);
                    db.update(CampusModelDB.TABLE_CAMPUS,contentValues,
                            CampusModelDB.KEY_EMAIL + " =?",new String[]{phone});
                }
            };
            MLog.i("Campus Data","Update Email");
            return campusModelDB.writeOperator(tableOperator);
        }
    }

    public boolean updatePhone(final String mobile,final String phone){
        synchronized (campusModelDB){
            TableOperator tableOperator = new TableOperator() {
                @Override
                public void doWork(SQLiteDatabase db) {
                    contentValues.clear();
                    contentValues.put(CampusModelDB.KEY_MOBILE, mobile);
                    db.update(CampusModelDB.TABLE_CAMPUS,contentValues,
                            CampusModelDB.KEY_MOBILE + " =?",new String[]{phone});
                }
            };
            MLog.i("Campus Data","Update Mobile");
            return campusModelDB.writeOperator(tableOperator);
        }
    }
}
