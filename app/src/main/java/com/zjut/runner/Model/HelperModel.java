package com.zjut.runner.Model;


import com.avos.avoscloud.AVObject;
import com.zjut.runner.util.Constants;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Phuylai on 2016/10/26.
 */

public class HelperModel extends CampusModel{
    String requestObjectID;
    int helperCharge;
    String requstReplyID;

    public HelperModel(String objectId, String userObjectId, String username, String mobile,
                       String email, GenderType genderType, String url,
                       String campusID, String campusName, String requestObjectID,String requstReplyID, int helperCharge) {
        super(objectId, userObjectId, username, mobile, email, genderType, url, campusID, campusName);
        this.requestObjectID = requestObjectID;
        this.helperCharge = helperCharge;
        this.requstReplyID = requstReplyID;
    }

    public int getHelperCharge() {
        return helperCharge;
    }

    public String getRequstReplyID() {
        return requstReplyID;
    }

    public void setRequstReplyID(String requstReplyID) {
        this.requstReplyID = requstReplyID;
    }

    public void setHelperCharge(int helperCharge) {
        this.helperCharge = helperCharge;
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
            AVObject campus = avObject.getAVObject(Constants.PARAM_CAMPUS_INFO);
            CampusModel helper = CampusModel.refreshCampus(user,campus);
            int helperCharge = (int) avObject.getNumber(Constants.PARAM_CHARGE);
            String requestReply = avObject.getObjectId();
            helperModels.add(new HelperModel(helper.getObjectId(),helper.getUserObjectId(),helper.getUsername(),
                    helper.getMobile(),helper.getEmail(),helper.getGenderType(),helper.getUrl(),helper.getCampusID(),
                    helper.getCampusName(),objectId,requestReply,helperCharge));
        }
        return helperModels;
    }


}
