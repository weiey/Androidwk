package com.weiey.app;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import com.weiey.app.activities.LoginActivity;
import com.weiey.app.dialog.AlertUtil;
import com.weiey.app.utils.LogUtil;
import com.weiey.app.utils.ToastUtil;

import java.util.Stack;



public class AppManager implements Application.ActivityLifecycleCallbacks{

	private static Stack<Activity> activityStack;
	private static AppManager instance;
	private AppManager(Application app) {
		app.registerActivityLifecycleCallbacks(this);
	}
	public static void init(Application app){
		instance = new AppManager(app);
	}


	public static AppManager getInstance() {
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

	public void logout() {
		try {
			Context mContext = currentActivity();
			if(mContext != null){
				AlertUtil.showAlert(mContext, "提示", "登录信息过期,请重新登录", new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						finishAllActivity();
						Intent intent = new Intent(mContext, LoginActivity.class);
						intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
						mContext.startActivity(intent);
						System.exit(0);
					}
				});
			}else{
				ToastUtil.show("登录信息过期,请重新登录");
			}



//			Intent intent = new Intent(mContext, LoginActivity.class);
//			intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK| Intent.FLAG_ACTIVITY_CLEAR_TOP);
//			mContext.startActivity(intent);
//			System.exit(0);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}



	@Override
	public void onActivityCreated(Activity activity, Bundle savedInstanceState) {
		addActivity(activity);
	}

	@Override
	public void onActivityStarted(Activity activity) {

	}

	@Override
	public void onActivityResumed(Activity activity) {

	}

	@Override
	public void onActivityPaused(Activity activity) {

	}

	@Override
	public void onActivityStopped(Activity activity) {

	}

	@Override
	public void onActivitySaveInstanceState(Activity activity, Bundle outState) {

	}

	@Override
	public void onActivityDestroyed(Activity activity) {
		removeActivity(activity);
	}
}
