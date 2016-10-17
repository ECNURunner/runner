package com.bst.akario.db.controller;

import java.util.HashMap;
import java.util.Map;

import android.content.Context;

import com.bst.akario.db.OverdueService;
import com.bst.akario.model.OverdueModel;

/**
 * 
 * @author will.Wu
 * 
 */
public class OverdueDBController {

	private static Map<Context, OverdueService> dbOverdueModels = new HashMap<Context, OverdueService>();

	/**
	 * @author will.Wu
	 * @param context
	 * @param url
	 * @param maxAge
	 * @return
	 */
	public static boolean addOverdue(Context context, String url, String maxAge) {
		OverdueService overdueService = getOverdueDBService(context);
		if (overdueService == null) {
			return false;
		}
		return overdueService.addOverdueUrl(url, maxAge);
	}

	/**
	 * @author will.Wu
	 * @param context
	 * @param url
	 * @return
	 */
	public static boolean checkUrlIsOverDue(Context context, String url) {
		OverdueService overdueService = getOverdueDBService(context);
		if (overdueService == null) {
			return false;
		}
		OverdueModel overdueModel = overdueService.getOverdueMaxAgeByUrl(url);
		if (overdueModel == null) {
			return false;
		}
		long currentTime = System.currentTimeMillis();
		long topGetTime = overdueModel.getGetTime();
		long maxAge = overdueModel.getMaxAge() * 1000;
		return Math.abs(currentTime - topGetTime) > maxAge;
	}

	private static OverdueService getOverdueDBService(Context context) {
		OverdueService overdueService = dbOverdueModels.get(context);
		if (overdueService == null) {
			overdueService = new OverdueService(context);
		}
		return overdueService;
	}
}
