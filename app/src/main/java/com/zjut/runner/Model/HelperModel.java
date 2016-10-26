package com.zjut.runner.Model;


import com.avos.avoscloud.AVObject;
import com.zjut.runner.util.Constants;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Phuylai on 2016/10/26.
 */

public class HelperModel extends CampusModel {
    String requestObjectID;

    public HelperModel(String objectId, String userObjectId, String username, String mobile,
                       String email, GenderType genderType, String url,
                       String campusID, String campusName, String requestObjectID) {
        super(objectId, userObjectId, username, mobile, email, genderType, url, campusID, campusName);
        this.requestObjectID = requestObjectID;
    }


    public String getRequestObjectID() {
        return requestObjectID;
    }

    public void setRequestObjectID(String requestObjectID) {
        this.requestObjectID = requestObjectID;
    }

    public static List<HelperModel> setCampusModel(String objectId, List<AVObject> avObjects){
        if(avObjects == null)
            return null;
        List<HelperModel> helperModels = new ArrayList<>();
        for(AVObject avObject:avObjects){
            CampusModel user = CampusModel.setCampusModel(avObject.
                    getAVUser(Constants.PARAM_USER_INFO));
            CampusModel helper = CampusModel.refreshCampus(user,avObject);
            helperModels.add(new HelperModel(helper.getObjectId(),helper.getUserObjectId(),helper.getUsername(),
                    helper.getMobile(),helper.getEmail(),helper.getGenderType(),helper.getUrl(),helper.getCampusID(),
                    helper.getCampusName(),objectId));
        }
        return helperModels;
    }


}
