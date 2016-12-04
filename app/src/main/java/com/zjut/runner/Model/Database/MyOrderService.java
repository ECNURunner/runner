package com.zjut.runner.Model.Database;

import android.content.ContentValues;
import android.content.Context;

import com.bst.akario.db.TableOperator;
import com.zjut.runner.Model.CampusModel;
import com.zjut.runner.Model.Database.Table.MyOrderDB;
import com.zjut.runner.Model.GenderType;
import com.zjut.runner.Model.HelperModel;
import com.zjut.runner.Model.OrderModel;
import com.zjut.runner.Model.OrderStatus;
import com.zjut.runner.util.MLog;
import com.zjut.runner.util.StringUtil;

import net.sqlcipher.database.SQLiteDatabase;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Created by Phuylai on 2016/10/26.
 */

public class MyOrderService {

    private MyOrderDB myOrderDB;
    private final static ContentValues contentValues = new ContentValues();

    public MyOrderService(Context context){
        myOrderDB = MyOrderDB.getInstance(context);
    }

    final static String getMyOrder =
            "SELECT * FROM "
            + MyOrderDB.TABLE_MYORDER
            + " WHERE "
            + MyOrderDB.KEY_CARD_ID
            + " =? AND "
            + MyOrderDB.KEY_STATUS + " =?";

    final static String getRequestHelpers =
            "SELECT * FROM "
                    + MyOrderDB.TABLE_HELPERS
                    + " WHERE "
                    + MyOrderDB.KEY_OBJECT_ID
                    + " =?";

    public Collection<OrderModel> loadMyOrderModel(final String ownerID,final String status){
        final List<OrderModel> orderModelList = new ArrayList<>();
        TableOperator tableOperator = new TableOperator() {
            @Override
            public void doWork(SQLiteDatabase db) {
                net.sqlcipher.Cursor cursor = db.rawQuery(getMyOrder,new String[]{ownerID,status});
                if(cursor.moveToFirst()){
                    do{
                        OrderModel orderModel = getOrderInfo(cursor);
                        if(orderModel != null)
                            orderModelList.add(orderModel);
                    }while (cursor.moveToNext());
                }
                cursor.close();
            }
        };
       myOrderDB.readOperator(tableOperator);
        return orderModelList;
    }


    private OrderModel getOrderInfo(net.sqlcipher.Cursor cursor) {
        if(cursor == null){
            return null;
        }
        String requestObjectID = cursor.getString(
                cursor.getColumnIndex(MyOrderDB.KEY_OBJECT_ID));

        String remark = cursor.getString(
                cursor.getColumnIndex(MyOrderDB.KEY_REMARK));

        boolean isChosen = Boolean.parseBoolean(cursor.getString(cursor.getColumnIndex(MyOrderDB.KEY_CHOSEN)));

        String orderDate = cursor.getString(cursor.getColumnIndex(MyOrderDB.KEY_ORDER_DATE));

        String deadline = cursor.getString(cursor.getColumnIndex(MyOrderDB.KEY_DEADLINE));

        String title = cursor.getString(cursor.getColumnIndex(MyOrderDB.KEY_TITLE));

        String dest = cursor.getString(cursor.getColumnIndex(MyOrderDB.KEY_DEST));

        int charge = cursor.getInt(cursor.getColumnIndex(MyOrderDB.KEY_CHARGE));

        int finalCharge = cursor.getInt(cursor.getColumnIndex(MyOrderDB.KEY_FINAL_CHARGE));

        String helperUserID = cursor.getString(cursor.getColumnIndex(MyOrderDB.USER_OBJECT_ID));

        String cardID = cursor.getString(cursor.getColumnIndex(MyOrderDB.KEY_CARD_ID));

        String helperName = cursor.getString(cursor.getColumnIndex(MyOrderDB.KEY_NAME));

        String mobile = cursor.getString(cursor.getColumnIndex(MyOrderDB.KEY_MOBILE));

        String email = cursor.getString(cursor.getColumnIndex(MyOrderDB.KEY_EMAIL));

        String install = cursor.getString(cursor.getColumnIndex(MyOrderDB.KEY_INSTALL));

        String gender = cursor.getString(cursor.getColumnIndex(MyOrderDB.KEY_GENDER));
        GenderType genderType = null;
        if(!StringUtil.isNull(gender)){
            genderType = GenderType.getType(gender);
        }

        String url = cursor.getString(cursor.getColumnIndex(MyOrderDB.KEY_URL));

        OrderStatus orderStatus = null;
        String status = cursor.getString(cursor.getColumnIndex(MyOrderDB.KEY_STATUS));
        if(!StringUtil.isNull(status)){
            orderStatus = OrderStatus.getType(status);
        }

        int helperNum = cursor.getInt(cursor.getColumnIndex(MyOrderDB.KEY_NUM_HELPER));

        CampusModel helper = null;
        if(helperNum > 0){
            helper = new CampusModel(helperUserID,helperName,mobile,email,genderType,url,install);
        }

        MLog.i("My Order", "FROM DB");
        return new OrderModel(remark,isChosen,orderDate,deadline,title,dest,charge,finalCharge,
                helper,orderStatus,requestObjectID,helperNum);
    }

    public Collection<HelperModel> loadRequestHelpers(final String objectID){
        final List<HelperModel> helperModels = new ArrayList<>();
        TableOperator tableOperator = new TableOperator() {
            @Override
            public void doWork(SQLiteDatabase db) {
                net.sqlcipher.Cursor cursor = db.rawQuery(getRequestHelpers,new String[]{objectID});
                if(cursor.moveToFirst()){
                    do{
                        HelperModel helperModel = getHelpersInfo(cursor);
                        if(helperModel != null)
                            helperModels.add(helperModel);
                    }while (cursor.moveToNext());
                }
                cursor.close();
            }
        };
        myOrderDB.readOperator(tableOperator);
        return helperModels;
    }

    private HelperModel getHelpersInfo(net.sqlcipher.Cursor cursor) {
        if(cursor == null){
            return null;
        }
        String helperCampusID = cursor.getString(
                cursor.getColumnIndex(MyOrderDB.HELPER_CAMPUS_ID));

        String helperUserID = cursor.getString(cursor.getColumnIndex(MyOrderDB.HELPER_USER_ID));

        String cardID = cursor.getString(cursor.getColumnIndex(MyOrderDB.HELPER_CARD_ID));

        String name = cursor.getString(cursor.getColumnIndex(MyOrderDB.HELPER_NAME));

        String mobile = cursor.getString(cursor.getColumnIndex(MyOrderDB.HELPER_MOBILE));

        String email = cursor.getString(cursor.getColumnIndex(MyOrderDB.HELPER_EMAIL));

        String requestID = cursor.getString(cursor.getColumnIndex(MyOrderDB.KEY_OBJECT_ID));

        String gender = cursor.getString(cursor.getColumnIndex(MyOrderDB.KEY_GENDER));

        String objectId = cursor.getString(cursor.getColumnIndex(MyOrderDB.REQUEST_REPLY));

        String install = cursor.getString(cursor.getColumnIndex(MyOrderDB.KEY_INSTALL));

        int charge = cursor.getInt(cursor.getColumnIndex(MyOrderDB.KEY_CHARGE));

        GenderType genderType = null;
        if(!StringUtil.isNull(gender)){
            genderType = GenderType.getType(gender);
        }

        String url = cursor.getString(cursor.getColumnIndex(MyOrderDB.KEY_URL));

        MLog.i("Helpers", "FROM DB");
        return new HelperModel(helperCampusID,helperUserID,null,mobile,email,genderType,url,cardID,name,requestID,objectId,charge,install);
    }

    public boolean SaveOrderToDB(final OrderModel orderModel, final String ownerID){
        synchronized (myOrderDB){
            TableOperator insertOperator = new TableOperator() {
                @Override
                public void doWork(SQLiteDatabase db) {
                    saveOrderToDB(db, orderModel,ownerID);
                }
            };
            return myOrderDB.writeOperator(insertOperator);
        }
    }

    private boolean saveOrderToDB(SQLiteDatabase db, OrderModel orderModel, String ownerID) {
        contentValues.clear();
        contentValues.put(MyOrderDB.KEY_OBJECT_ID, orderModel.getObjectID());
        contentValues.put(MyOrderDB.KEY_REMARK,orderModel.getRemark());
        contentValues.put(MyOrderDB.KEY_CARD_ID,ownerID);
        contentValues.put(MyOrderDB.KEY_CHARGE,orderModel.getCharge());
        contentValues.put(MyOrderDB.KEY_CHOSEN,orderModel.isChosen());
        contentValues.put(MyOrderDB.KEY_FINAL_CHARGE,orderModel.getFinalCharge());
        contentValues.put(MyOrderDB.KEY_ORDER_DATE,orderModel.getOrderDate());
        contentValues.put(MyOrderDB.KEY_DEADLINE,orderModel.getDeadline());
        contentValues.put(MyOrderDB.KEY_TITLE,orderModel.getTitle());
        contentValues.put(MyOrderDB.KEY_STATUS,orderModel.getStatus().toString());
        contentValues.put(MyOrderDB.KEY_DEST,orderModel.getDest());
        contentValues.put(MyOrderDB.KEY_NUM_HELPER,orderModel.getHelpers());
        CampusModel helper = orderModel.getHelper();
        if(helper != null){
            contentValues.put(MyOrderDB.KEY_URL,helper.getUrl());
            contentValues.put(MyOrderDB.USER_OBJECT_ID,helper.getUserObjectId());
            contentValues.put(MyOrderDB.KEY_NAME,helper.getCampusName());
            if(helper.getGenderType() != null) {
                contentValues.put(MyOrderDB.KEY_GENDER, helper.getGenderType().toString());
            }
            contentValues.put(MyOrderDB.KEY_MOBILE,helper.getMobile());
            contentValues.put(MyOrderDB.KEY_EMAIL,helper.getEmail());
            contentValues.put(MyOrderDB.KEY_STUDENT_ID,helper.getCampusID());
            contentValues.put(MyOrderDB.KEY_INSTALL,helper.getInstallationID());
        }

        db.insertWithOnConflict(MyOrderDB.TABLE_MYORDER, null, contentValues, SQLiteDatabase.CONFLICT_REPLACE);
        MLog.i("my order", " insert");
        return true;
    }

    public boolean SaveHelpersToDB(final HelperModel helperModel){
        synchronized (myOrderDB){
            TableOperator insertOperator = new TableOperator() {
                @Override
                public void doWork(SQLiteDatabase db) {
                    saveHelpersToDB(db, helperModel);
                }
            };
            return myOrderDB.writeOperator(insertOperator);
        }
    }

    private boolean saveHelpersToDB(SQLiteDatabase db, HelperModel helperModel) {
        contentValues.clear();
        contentValues.put(MyOrderDB.KEY_OBJECT_ID, helperModel.getRequestObjectID());
        contentValues.put(MyOrderDB.HELPER_CARD_ID,helperModel.getCampusID());
        contentValues.put(MyOrderDB.HELPER_CAMPUS_ID,helperModel.getObjectId());
        contentValues.put(MyOrderDB.HELPER_USER_ID,helperModel.getUserObjectId());
        contentValues.put(MyOrderDB.HELPER_NAME,helperModel.getCampusName());
        contentValues.put(MyOrderDB.HELPER_MOBILE,helperModel.getMobile());
        contentValues.put(MyOrderDB.HELPER_EMAIL,helperModel.getEmail());
        if(helperModel.getGenderType() != null) {
            contentValues.put(MyOrderDB.KEY_GENDER, helperModel.getGenderType().toString());
        }
        contentValues.put(MyOrderDB.KEY_URL,helperModel.getUrl());
        contentValues.put(MyOrderDB.REQUEST_REPLY, helperModel.getObjectId());
        contentValues.put(MyOrderDB.KEY_CHARGE,helperModel.getHelperCharge());
        contentValues.put(MyOrderDB.KEY_INSTALL,helperModel.getInstallationID());

        db.insertWithOnConflict(MyOrderDB.TABLE_HELPERS, null, contentValues, SQLiteDatabase.CONFLICT_REPLACE);
        MLog.i("Helper", " insert");
        return true;
    }



}
