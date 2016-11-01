package com.zjut.runner.Model.Database.DBController;

import android.content.Context;

import com.zjut.runner.Model.Database.AllOrderService;
import com.zjut.runner.Model.Database.MyOrderService;
import com.zjut.runner.Model.OrderModel;

import java.util.Collection;

/**
 * Created by Administrator on 2016/10/28.
 */

public class AllOrderDBController {

    public static void saveOrderToDB(Context context, OrderModel orderModel, String studentID){
        AllOrderService myOrderService = DBController.getAllOrderService(context);
        if(myOrderService == null){
            return;
        }
        myOrderService.SaveOrderToDB(orderModel,studentID);
    }

    public static Collection<OrderModel> getOrderFromDB(Context context, String studentID){
        AllOrderService myOrderService = DBController.getAllOrderService(context);
        if(myOrderService == null){
            return null;
        }
        return myOrderService.loadMyOrderModel(studentID);
    }

    public static void removeOrderFromDB(Context context,String requestID){
        AllOrderService myOrderService = DBController.getAllOrderService(context);
        if(myOrderService == null){
            return;
        }
        myOrderService.removeOrderFromDB(requestID);
    }

}
