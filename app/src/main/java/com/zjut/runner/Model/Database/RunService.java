package com.zjut.runner.Model.Database;

import android.content.ContentValues;
import android.content.Context;

import com.bst.akario.db.TableOperator;
import com.zjut.runner.Model.CampusModel;
import com.zjut.runner.Model.Database.Table.RunDB;
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
 * Created by Phuylai on 2016/10/31.
 */

public class RunService {
    private RunDB runDB;
    private static final ContentValues contentValues = new ContentValues();

    public RunService(Context context){
        runDB = RunDB.getInstance(context);
    }

    final static String getRun =
            "SELECT * FROM "
                    + RunDB.TABLE_RUN
                    + " WHERE "
                    + RunDB.KEY_STUDENT_ID
                    + " =? AND "
                    + RunDB.KEY_STATUS + " =?";

    public Collection<OrderModel> loadRunModel(final String ownerID, final String status){
        final List<OrderModel> orderModelList = new ArrayList<>();
        TableOperator tableOperator = new TableOperator() {
            @Override
            public void doWork(SQLiteDatabase db) {
                net.sqlcipher.Cursor cursor = db.rawQuery(getRun,new String[]{ownerID,status});;
                if(cursor.moveToFirst()){
                    do{
                        OrderModel orderModel = getRunInfo(cursor);
                        if(orderModel != null)
                            orderModelList.add(orderModel);
                    }while (cursor.moveToNext());
                }
                cursor.close();
            }
        };
        runDB.readOperator(tableOperator);
        return orderModelList;
    }

    private OrderModel getRunInfo(net.sqlcipher.Cursor cursor) {
        if(cursor == null){
            return null;
        }
        String requestID = cursor.getString(cursor.getColumnIndex(RunDB.KEY_REQUEST_ID));

        String requestObjectID = cursor.getString(
                cursor.getColumnIndex(RunDB.KEY_OBJECT_ID));

        String remark = cursor.getString(
                cursor.getColumnIndex(RunDB.KEY_REMARK));

        String orderDate = cursor.getString(cursor.getColumnIndex(RunDB.KEY_ORDER_DATE));

        String deadline = cursor.getString(cursor.getColumnIndex(RunDB.KEY_DEADLINE));

        String title = cursor.getString(cursor.getColumnIndex(RunDB.KEY_TITLE));

        String dest = cursor.getString(cursor.getColumnIndex(RunDB.KEY_DEST));

        int charge = cursor.getInt(cursor.getColumnIndex(RunDB.KEY_CHARGE));

        int finalCharge = cursor.getInt(cursor.getColumnIndex(RunDB.KEY_FINAL_CHARGE));

        String ownerName = cursor.getString(cursor.getColumnIndex(RunDB.KEY_NAME));

        String mobile = cursor.getString(cursor.getColumnIndex(RunDB.KEY_MOBILE));

        String email = cursor.getString(cursor.getColumnIndex(RunDB.KEY_EMAIL));

        String gender = cursor.getString(cursor.getColumnIndex(RunDB.KEY_GENDER));
        GenderType genderType = null;
        if(!StringUtil.isNull(gender)){
            genderType = GenderType.getType(gender);
        }

        String url = cursor.getString(cursor.getColumnIndex(RunDB.KEY_URL));

        OrderStatus orderStatus = null;
        String status = cursor.getString(cursor.getColumnIndex(RunDB.KEY_STATUS));
        if(!StringUtil.isNull(status)){
            orderStatus = OrderStatus.getType(status);
        }
        String install = cursor.getString(cursor.getColumnIndex(RunDB.KEY_INSTALL));
        CampusModel owner = null;
        owner = new CampusModel(null,ownerName,mobile,email,genderType,url,install);
        MLog.i("RUN", "FROM DB");
        return new OrderModel(remark,orderDate,deadline,title,dest,charge,finalCharge,
                owner,requestObjectID,requestID,orderStatus);
    }

    public boolean SaveRunToDB(final OrderModel orderModel, final String ownerID){
        synchronized (runDB){
            TableOperator insertOperator = new TableOperator() {
                @Override
                public void doWork(SQLiteDatabase db) {
                    saveRunToDB(db, orderModel,ownerID);
                }
            };
            return runDB.writeOperator(insertOperator);
        }
    }

    private boolean saveRunToDB(SQLiteDatabase db, OrderModel orderModel, String ownerID) {
        contentValues.clear();
        contentValues.put(RunDB.KEY_REQUEST_ID,orderModel.getObjectID());
        contentValues.put(RunDB.KEY_OBJECT_ID, orderModel.getReplyRequestObjectID());
        contentValues.put(RunDB.KEY_REMARK,orderModel.getRemark());
        contentValues.put(RunDB.KEY_STUDENT_ID,ownerID);
        contentValues.put(RunDB.KEY_CHARGE,orderModel.getCharge());
        contentValues.put(RunDB.KEY_FINAL_CHARGE,orderModel.getFinalCharge());
        contentValues.put(RunDB.KEY_ORDER_DATE,orderModel.getOrderDate());
        contentValues.put(RunDB.KEY_DEADLINE,orderModel.getDeadline());
        contentValues.put(RunDB.KEY_TITLE,orderModel.getTitle());
        contentValues.put(RunDB.KEY_STATUS,orderModel.getStatus().toString());
        contentValues.put(RunDB.KEY_DEST,orderModel.getDest());
        CampusModel owner = orderModel.getOwnerModel();
        if(owner != null){
            contentValues.put(RunDB.KEY_URL,owner.getUrl());
            contentValues.put(RunDB.KEY_NAME,owner.getCampusName());
            contentValues.put(RunDB.KEY_INSTALL,owner.getInstallationID());
            if(owner.getGenderType() != null) {
                contentValues.put(RunDB.KEY_GENDER, owner.getGenderType().toString());
            }
            contentValues.put(RunDB.KEY_MOBILE,owner.getMobile());
            contentValues.put(RunDB.KEY_EMAIL,owner.getEmail());
        }

        db.insertWithOnConflict(RunDB.TABLE_RUN, null, contentValues, SQLiteDatabase.CONFLICT_REPLACE);
        MLog.i("my run", " insert");
        return true;
    }

    public boolean updateRunService(final String requestID, final OrderStatus status){
        synchronized (runDB){
            TableOperator tableOperator = new TableOperator() {
                @Override
                public void doWork(SQLiteDatabase db) {
                    contentValues.clear();
                    contentValues.put(RunDB.KEY_STATUS,status.toString());
                    db.update(RunDB.TABLE_RUN, contentValues,
                           RunDB.KEY_REQUEST_ID + " =?", new String[]{requestID});
                }
            };
            MLog.i("run status ","Updated");
            return runDB.writeOperator(tableOperator);
        }
    }
}
