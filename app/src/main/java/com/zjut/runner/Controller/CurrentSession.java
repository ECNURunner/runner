package com.zjut.runner.Controller;

import android.content.Context;
import android.os.AsyncTask;
import android.widget.Toast;

import com.zjut.runner.Model.CampusModel;
import com.zjut.runner.Model.Database.DBController.AllOrderDBController;
import com.zjut.runner.Model.Database.DBController.CampusDBController;
import com.zjut.runner.Model.Database.DBController.MyOrderDBController;
import com.zjut.runner.Model.Database.DBController.RunDBController;
import com.zjut.runner.Model.HelperModel;
import com.zjut.runner.Model.OrderModel;
import com.zjut.runner.Model.OrderStatus;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by Administrator on 2016/10/17.
 */

public class CurrentSession {

    private static Toast toast = null;
    private static CurrentSession instance = null;
    private final static Map<String,CampusModel> campusModelMap = new ConcurrentHashMap<>();

    public static Toast getToast() {
        return toast;
    }

    private static CurrentSession getInstance() {
        if (instance == null) {
            instance = new CurrentSession();
        }
        return instance;
    }

    public static void putCampusModelwithCache(Context context,CampusModel campusModel){
        putCampusModel(context,campusModel,true);
    }
    private static void putCampusModel(Context context, CampusModel campusModel, boolean savdDB) {
        if(campusModel == null){
            return;
        }
        campusModelMap.put(campusModel.getMobile(),campusModel);
        if(savdDB){
            CampusDBController.saveCampusModelToDB(context,campusModel);
        }
    }
    public static CampusModel getCurrentCampusInfo(Context context, CampusModel campusModel){
        CampusModel sessionModel = campusModelMap.get(campusModel.getMobile());
        if(sessionModel != null){
            return sessionModel;
        }
        Collection<CampusModel> campusModels = CampusDBController.
                getCampusModelFromDB(context, campusModel.getMobile());
        if(campusModels != null && campusModels.size() > 0){
            List<CampusModel> campusModelList = new ArrayList<>(campusModels);
            return campusModelList.get(0);
        }
        return null;
    }
    public static boolean updateProfileWithCache(Context context,CampusModel campusModel){
        if(campusModel == null){
            return false;
        }
        campusModelMap.put(campusModel.getMobile(),campusModel);
        return CampusDBController.updateProfilePicToDB(context, campusModel.getUrl(),
                campusModel.getMobile());
    }
    public static boolean updateGenderWithCache(Context context,CampusModel campusModel){
        if(campusModel == null){
            return false;
        }
        campusModelMap.put(campusModel.getMobile(), campusModel);
        return CampusDBController.updateGenderToDB(context, campusModel.getGenderType().toString(),
                campusModel.getMobile());
    }
    public static boolean removeWithCache(Context context,CampusModel campusModel){
        if(campusModel == null){
            return false;
        }
        campusModelMap.remove(campusModel.getMobile());
        return CampusDBController.removeCampusModelToDB(context, campusModel.getMobile());
    }
    public static boolean updateNameWithCache(Context context,CampusModel campusModel){
        if(campusModel == null){
            return false;
        }
        campusModelMap.put(campusModel.getMobile(), campusModel);
        return CampusDBController.updateNameToDB(context,campusModel.getUsername(),campusModel.getMobile());
    }
    public static boolean updatePhoneWithCache(Context context,String oldPhone,CampusModel campusModel){
        if(campusModel == null){
            return false;
        }
        campusModelMap.put(campusModel.getMobile(),campusModel);
        return CampusDBController.updatePhoneToDB(context, campusModel.getMobile(),
                oldPhone);
    }
    public static boolean updateEmailWithCache(Context context,CampusModel campusModel){
        if(campusModel == null){
            return false;
        }
        campusModelMap.put(campusModel.getMobile(), campusModel);
        return CampusDBController.updateEmailToDB(context, campusModel.getEmail(),
                campusModel.getMobile());
    }

    public static List<OrderModel> getOrderModels(Context context, String ownerID,String status){

        Collection<OrderModel> orderModels = MyOrderDBController.getOrderFromDB(context,ownerID,status);
        if(orderModels != null && orderModels.size() > 0){
            return new ArrayList<>(orderModels);
        }
        return null;
    }

    public static List<OrderModel> getRunModels(Context context, String ownerID,String status){

        Collection<OrderModel> orderModels = RunDBController.getRunFromDB(context,ownerID,status);
        if(orderModels != null && orderModels.size() > 0){
            return new ArrayList<>(orderModels);
        }
        return null;
    }

    public static void putOrderModel(Context context,OrderModel orderModel,String ownerID){
        if(orderModel == null){
            return;
        }
        MyOrderDBController.saveOrderToDB(context,orderModel,ownerID);
    }

    public static void putRunModel(Context context,OrderModel orderModel,String ownerID){
        if(orderModel == null){
            return;
        }
        RunDBController.saveRunToDB(context,orderModel,ownerID);
    }

    public static void updateRunStatus(Context context, String requestID, OrderStatus status){
        if(requestID == null || status == null){
            return;
        }
        RunDBController.updateRunStatusToDB(context,requestID,status);
    }

    public static void putAllOrders(Context context,OrderModel orderModel,String studentID){
        if(orderModel == null){
            return;
        }
        AllOrderDBController.saveOrderToDB(context,orderModel,studentID);
    }

    public static void removeRunOrder(Context context,String requestID){
        if(requestID == null){
            return;
        }
        AllOrderDBController.removeOrderFromDB(context,requestID);
    }

    public static List<OrderModel> getAllOrderModels(Context context, String ownerID){

        Collection<OrderModel> orderModels = AllOrderDBController.getOrderFromDB(context,ownerID);
        if(orderModels != null && orderModels.size() > 0){
            return new ArrayList<>(orderModels);
        }
        return null;
    }

    public static List<HelperModel> getHelperModels(Context context, String objectID){

        Collection<HelperModel> helperModels = MyOrderDBController.getHelperFromDB(context,objectID);
        if(helperModels != null && helperModels.size() > 0){
            return new ArrayList<>(helperModels);
        }
        return null;
    }

    public static void putHelperModel(Context context,HelperModel helperModel){
        if(helperModel == null){
            return;
        }
        MyOrderDBController.saveHelpersToDB(context,helperModel);
    }


}
