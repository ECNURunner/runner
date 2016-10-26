package com.zjut.runner.Model.Database.DBController;

import android.content.Context;

import com.zjut.runner.Model.Database.MyOrderService;
import com.zjut.runner.Model.HelperModel;
import com.zjut.runner.Model.OrderModel;

import java.util.Collection;

/**
 * Created by Phuylai on 2016/10/26.
 */

public class MyOrderDBController {

    public static void saveOrderToDB(Context context, OrderModel orderModel,String ownerID){
        MyOrderService myOrderService = DBController.getMyOrderService(context);
        if(myOrderService == null){
            return;
        }
        myOrderService.SaveOrderToDB(orderModel,ownerID);
    }

    public static void saveHelpersToDB(Context context, HelperModel helperModel){
        MyOrderService myOrderService = DBController.getMyOrderService(context);
        if(myOrderService == null){
            return;
        }
        myOrderService.SaveHelpersToDB(helperModel);
    }

    public static Collection<OrderModel> getOrderFromDB(Context context,String ownerID,String status){
        MyOrderService myOrderService = DBController.getMyOrderService(context);
        if(myOrderService == null){
            return null;
        }
        return myOrderService.loadMyOrderModel(ownerID,status);
    }

    public static Collection<HelperModel> getHelperFromDB(Context context,String requestID){
        MyOrderService myOrderService = DBController.getMyOrderService(context);
        if(myOrderService == null){
            return null;
        }
        return myOrderService.loadRequestHelpers(requestID);
    }

}
