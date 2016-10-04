package com.zjut.runner.util;

import android.os.Handler;

import java.util.HashMap;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by Phuylai on 2016/10/4.
 */
public class RunnableManager {
    public static final String TAG = RunnableManager.class.getSimpleName();
    public static RunnableManager instance = null;

    private Map<String, Runnable> runnableMaps = new HashMap<String, Runnable>();
    private Handler handler = new Handler();

    public synchronized static RunnableManager getInstance() {
        if (instance == null) {
            instance = new RunnableManager();
        }
        return instance;
    }

    public synchronized void postDelayed(final Runnable runnable, long waitTime) {
        if (runnable == null) {
            return;
        }
        if (runnableMaps.containsKey(runnable.toString())) {
            return;
        }
        runnableMaps.put(runnable.toString(), runnable);
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {

            @Override
            public void run() {
                handler.post(runnable);
                runnableMaps.remove(runnable.toString());
            }
        }, waitTime);
    }
}
