package com.zjut.runner.Model.Database.DBController;

import android.content.Context;

import com.zjut.runner.Model.Database.MyOrderService;
import com.zjut.runner.Model.Database.RunService;
import com.zjut.runner.Model.OrderModel;

import java.util.Collection;

/**
 * Created by Phuylai on 2016/10/31.
 */

public class RunDBController {

    public static Collection<OrderModel> getRunFromDB(Context context, String ownerID, String status){
        RunService runService = DBController.runService(context);
        if(runService == null){
            return null;
        }
        return runService.loadRunModel(ownerID,status);
    }

    public static void saveRunToDB(Context context, OrderModel orderModel,String ownerID){
        RunService runService = DBController.runService(context);
        if(runService == null){
            return;
        }
        runService.SaveRunToDB(orderModel,ownerID);
    }
}
