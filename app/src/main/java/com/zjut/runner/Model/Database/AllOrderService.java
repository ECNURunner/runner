package com.zjut.runner.Model.Database;

import android.content.ContentValues;
import android.content.Context;

import com.bst.akario.db.TableOperator;
import com.zjut.runner.Model.CampusModel;
import com.zjut.runner.Model.Database.Table.MyOrderDB;
import com.zjut.runner.Model.Database.Table.OtherOrderDB;
import com.zjut.runner.Model.GenderType;
import com.zjut.runner.Model.OrderModel;
import com.zjut.runner.Model.OrderStatus;
import com.zjut.runner.util.MLog;
import com.zjut.runner.util.StringUtil;

import net.sqlcipher.database.SQLiteDatabase;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Created by Administrator on 2016/10/28.
 */

public class AllOrderService {

    private OtherOrderDB otherOrderDB;
    private static ContentValues contentValues = new ContentValues();

    public AllOrderService(Context context){
         otherOrderDB = OtherOrderDB.getInstance(context);
    }

    static String getOrders =
            "SELECT * FROM "
                    + OtherOrderDB.TABLE_ORDER
                    + " WHERE "
                    + OtherOrderDB.KEY_STUDENT_ID
                    + " =?";

    public Collection<OrderModel> loadMyOrderModel(final String campusID){
        final List<OrderModel> orderModelList = new ArrayList<>();
        TableOperator tableOperator = new TableOperator() {
            @Override
            public void doWork(SQLiteDatabase db) {
                net.sqlcipher.Cursor cursor = db.rawQuery(getOrders,new String[]{campusID});
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
        otherOrderDB.readOperator(tableOperator);
        return orderModelList;
    }

    private OrderModel getOrderInfo(net.sqlcipher.Cursor cursor) {
        if(cursor == null){
            return null;
        }
        String requestObjectID = cursor.getString(
                cursor.getColumnIndex(OtherOrderDB.KEY_OBJECT_ID));

        String remark = cursor.getString(
                cursor.getColumnIndex(MyOrderDB.KEY_REMARK));

        String orderDate = cursor.getString(cursor.getColumnIndex(OtherOrderDB.KEY_ORDER_DATE));

        String deadline = cursor.getString(cursor.getColumnIndex(OtherOrderDB.KEY_DEADLINE));

        String title = cursor.getString(cursor.getColumnIndex(OtherOrderDB.KEY_TITLE));

        String dest = cursor.getString(cursor.getColumnIndex(OtherOrderDB.KEY_DEST));

        String ownerObj = cursor.getString(cursor.getColumnIndex(OtherOrderDB.KEY_OWNER_USERID));

        String helperName = cursor.getString(cursor.getColumnIndex(OtherOrderDB.KEY_NAME));

        String mobile = cursor.getString(cursor.getColumnIndex(OtherOrderDB.KEY_MOBILE));

        String email = cursor.getString(cursor.getColumnIndex(OtherOrderDB.KEY_EMAIL));

        String gender = cursor.getString(cursor.getColumnIndex(OtherOrderDB.KEY_GENDER));
        GenderType genderType = null;
        if(!StringUtil.isNull(gender)){
            genderType = GenderType.getType(gender);
        }

        String url = cursor.getString(cursor.getColumnIndex(MyOrderDB.KEY_URL));

        CampusModel owner = new CampusModel(ownerObj,helperName,mobile,email,genderType,url);

        int charge = cursor.getInt(cursor.getColumnIndex(OtherOrderDB.KEY_CHARGE));

        int helperNum = cursor.getInt(cursor.getColumnIndex(OtherOrderDB.KEY_NUM_HELPER));

        MLog.i("All Order", "FROM DB");
        return new OrderModel(remark,orderDate,deadline,title,dest,charge,owner,requestObjectID,helperNum);
    }

    public boolean SaveOrderToDB(final OrderModel orderModel, final String ownerID){
        synchronized (otherOrderDB){
            TableOperator insertOperator = new TableOperator() {
                @Override
                public void doWork(SQLiteDatabase db) {
                    saveOrderToDB(db, orderModel,ownerID);
                }
            };
            return otherOrderDB.writeOperator(insertOperator);
        }
    }

    private boolean saveOrderToDB(SQLiteDatabase db, OrderModel orderModel, String ownerID) {
        contentValues.clear();
        contentValues.put(OtherOrderDB.KEY_OBJECT_ID, orderModel.getObjectID());
        contentValues.put(OtherOrderDB.KEY_REMARK,orderModel.getRemark());
        contentValues.put(OtherOrderDB.KEY_STUDENT_ID,ownerID);
        contentValues.put(OtherOrderDB.KEY_CHARGE,orderModel.getCharge());
        contentValues.put(OtherOrderDB.KEY_ORDER_DATE,orderModel.getOrderDate());
        contentValues.put(OtherOrderDB.KEY_DEADLINE,orderModel.getDeadline());
        contentValues.put(OtherOrderDB.KEY_TITLE,orderModel.getTitle());
        contentValues.put(OtherOrderDB.KEY_DEST,orderModel.getDest());
        contentValues.put(OtherOrderDB.KEY_NUM_HELPER,orderModel.getHelpers());
        CampusModel owner = orderModel.getOwnerModel();
        contentValues.put(OtherOrderDB.KEY_OWNER_USERID,owner.getUserObjectId());
        contentValues.put(OtherOrderDB.KEY_URL,owner.getUrl());
        if(owner.getGenderType() != null) {
            contentValues.put(MyOrderDB.KEY_GENDER, owner.getGenderType().toString());
        }
        contentValues.put(OtherOrderDB.KEY_CARD_ID,owner.getCampusID());
        contentValues.put(OtherOrderDB.KEY_MOBILE,owner.getMobile());
        contentValues.put(OtherOrderDB.KEY_EMAIL,owner.getEmail());
        contentValues.put(OtherOrderDB.KEY_NAME,owner.getCampusName());
        db.insertWithOnConflict(OtherOrderDB.TABLE_ORDER, null, contentValues, SQLiteDatabase.CONFLICT_REPLACE);
        MLog.i("all order", " insert");
        return true;
    }

}
