package com.zjut.runner.util;

import android.app.Activity;

import java.util.ArrayList;
import java.util.Collection;

/**
 * Created by Phuylai on 2016/10/4.
 */
public class ActivitiesManager {
    public static ActivitiesManager instance = null;
    public static ActivitiesManager getInstance(){
        if(instance == null)
            instance = new ActivitiesManager();
        return instance;
    }

    private Collection<Activity> activities = new ArrayList<>();

    public void addActivity(Activity activity){
        if(activity == null)
            return;
        activities.add(activity);
    }

    public void removeActivity(Activity activity){
        if(activity == null)
            return;
        activities.remove(activity);
    }

    public boolean isBackground(){
        if(activities == null)
            return true;
        return activities.size() <= 0;
    }
}
