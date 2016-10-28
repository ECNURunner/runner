package com.zjut.runner.Model.Database.DBController;

import android.content.Context;

import com.zjut.runner.Model.Database.AllOrderService;
import com.zjut.runner.Model.Database.CampusService;
import com.zjut.runner.Model.Database.MyOrderService;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Administrator on 2016/10/21.
 */

public class DBController {

    private static Map<Context, CampusService> campusModels = new HashMap<>();
    private static Map<Context,MyOrderService> orderServiceMap = new HashMap<>();
    private static Map<Context,AllOrderService> AllorderServiceMap = new HashMap<>();
    /**
     * @author Phuylai
     * @param context
     *
     */
    public static CampusService getCampusService(Context context){
        if(context == null){
            return null;
        }
        Context applicationContext = context.getApplicationContext();
        CampusService campusService = campusModels.get(applicationContext);
        if(campusService == null){
            campusService = new CampusService(applicationContext);
            campusModels.put(applicationContext,campusService);
        }
        return campusService;
    }

    /**
     * @author Phuylai
     * @param context
     *
     */
    public static MyOrderService getMyOrderService(Context context){
        if(context == null){
            return null;
        }
        Context applicationContext = context.getApplicationContext();
        MyOrderService myOrderService = orderServiceMap.get(applicationContext);
        if(myOrderService == null){
            myOrderService = new MyOrderService(applicationContext);
            orderServiceMap.put(applicationContext,myOrderService);
        }
        return myOrderService;
    }

    /**
     * @author Phuylai
     * @param context
     *
     */
    public static AllOrderService getAllOrderService(Context context){
        if(context == null){
            return null;
        }
        Context applicationContext = context.getApplicationContext();
        AllOrderService allOrderService = AllorderServiceMap.get(applicationContext);
        if(allOrderService == null){
            allOrderService = new AllOrderService(applicationContext);
            AllorderServiceMap.put(applicationContext,allOrderService);
        }
        return allOrderService;
    }


}
