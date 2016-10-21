package com.zjut.runner.Model.Database.DBController;

import android.content.Context;

import com.zjut.runner.Model.CampusModel;
import com.zjut.runner.Model.Database.CampusService;

import java.util.Collection;

/**
 * Created by Administrator on 2016/10/21.
 */

public class CampusDBController {

    public static void saveCampusModelToDB(Context context, CampusModel campusModel){
        CampusService campusService = DBController.getCampusService(context);
        if(campusService == null){
            return;
        }
        campusService.SaveCampusModelToDB(campusModel);
    }

    public static boolean removeCampusModelToDB(Context context,String phone){
        CampusService campusService = DBController.getCampusService(context);
        if(campusService == null){
            return false;
        }
        return campusService.removeCampusModelFromDB(phone);
    }

    public static Collection<CampusModel> getCampusModelFromDB(Context context, String phone){
        CampusService campusService = DBController.getCampusService(context);
        if(campusService == null){
            return null;
        }
        return campusService.loadCampusModel(phone);
    }

    public static boolean updateNameToDB(Context context,String name,String phone){
        CampusService campusService = DBController.getCampusService(context);
        if(campusService == null){
            return false;
        }
        return campusService.updateUserName(name, phone);
    }

    public static boolean updateGenderToDB(Context context,String gender,String phone){
        CampusService campusService = DBController.getCampusService(context);
        if(campusService == null){
            return false;
        }
        return campusService.updateGender(gender, phone);
    }

    public static boolean updateProfilePicToDB(Context context,String urlThumbnail,String phone){
        CampusService campusService = DBController.getCampusService(context);
        if(campusService == null){
            return false;
        }
        return campusService.updateProfilePic(urlThumbnail,phone);
    }

    public static boolean updateEmailToDB(Context context,String email,String phone){
        CampusService campusService = DBController.getCampusService(context);
        if(campusService == null){
            return false;
        }
        return campusService.updateEmail(email, phone);
    }

    public static boolean updatePhoneToDB(Context context,String mobile,String phone){
        CampusService campusService = DBController.getCampusService(context);
        if(campusService == null){
            return false;
        }
        return campusService.updatePhone(mobile, phone);
    }

}
