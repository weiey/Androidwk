package com.weiey.app;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import com.weiey.app.utils.LogUtil;

import java.util.Stack;


public class AppManager {

	private static Stack<Activity> activityStack;
	private static AppManager instance;

	private AppManager() {
	}


	public static AppManager getInstance() {
		if (null == instance) {
			synchronized (AppManager.class) {
				if (null == instance) {
					instance = new AppManager();
				}
			}
		}
		return instance;
	}

	public void addActivity(Activity activity) {
		if (activityStack == null) {
			activityStack = new Stack<Activity>();
		}
		activityStack.push(activity);
		print();
	}

	public Activity currentActivity() {
		Activity activity = activityStack.lastElement();
		return activity;
	}

	public void removeActivity(Activity activity) {
		try {
			if (activityStack.contains(activity)) {
				activityStack.remove(activity);
			}
		} catch (Exception e) {
			LogUtil.e("AppManager", Log.getStackTraceString(e));
		}
		print();
	}

	private void print(){
		try {
			if(!AppConfig.DEBUG){
				return;
			}
			for (int i = 0, size = activityStack.size(); i < size; i++) {
				if (null != activityStack.get(i)) {
					LogUtil.i("activityStack="+i+"=", activityStack.get(i).getLocalClassName());
				}
			}
		} catch (Exception e) {
			LogUtil.e("AppManager", Log.getStackTraceString(e));
		}
	}

	public void finishAllActivity() {

		try {
			if(activityStack != null){
				for (int i = 0, size = activityStack.size(); i < size; i++) {
					Activity activity = activityStack.pop();
					if (null != activity) {
						activity.finish();
						activity.overridePendingTransition(0,0);
					}

				}
				activityStack.clear();
			}
		} catch (Exception e) {
			LogUtil.e("AppManager", Log.getStackTraceString(e));
		}

	}

	public Stack<Activity> getActivityStack() {
		return activityStack;
	}

	public void exitApp(Context context) {
		finishAllActivity();
		android.os.Process.killProcess(android.os.Process.myPid());
	}
	public void logout(Context mContext) {
		try {
			finishAllActivity();
//			Intent intent = new Intent(mContext, LoginActivity.class);
//			intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK| Intent.FLAG_ACTIVITY_CLEAR_TOP);
//			mContext.startActivity(intent);
//			System.exit(0);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
