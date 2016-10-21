package com.zjut.runner.Model.Database.DBController;

import android.content.Context;

import com.zjut.runner.Model.Database.CampusService;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Administrator on 2016/10/21.
 */

public class DBController {

    private static Map<Context, CampusService> campusModels = new HashMap<>();
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
}
