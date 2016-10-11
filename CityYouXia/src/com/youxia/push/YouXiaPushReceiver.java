package com.youxia.push;

import java.util.List;

import com.alibaba.fastjson.JSONObject;
import android.app.ActivityManager;
import android.app.ActivityManager.RunningTaskInfo;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import cn.jpush.android.api.JPushInterface;

/**
 * 自定义接收器
 * 
 * 如果不定义这个 Receiver，则：
 * 1) 默认用户会打开主界面
 * 2) 接收不到自定义消息
 */

public class YouXiaPushReceiver extends BroadcastReceiver {
	private static final String TAG = YouXiaPushReceiver.class.getSimpleName();
	
	@Override
	public void onReceive(Context context, Intent intent) {
        Bundle bundle = intent.getExtras();
        if (JPushInterface.ACTION_REGISTRATION_ID.equals(intent.getAction())) {
        	Log.d(TAG, "JPush用户注册成功");
        } else if (JPushInterface.ACTION_MESSAGE_RECEIVED.equals(intent.getAction())) {
//        	bigNotificationPush(context, bundle);
        } else if (JPushInterface.ACTION_NOTIFICATION_RECEIVED.equals(intent.getAction())) {
            Log.d(TAG, "[SolarKEPushReceiver] 接收到推送下来的通知");
            int notifactionId = bundle.getInt(JPushInterface.EXTRA_NOTIFICATION_ID);
            Log.d(TAG, "[SolarKEPushReceiver] 接收到推送下来的通知的ID: " + notifactionId);
        } else if (JPushInterface.ACTION_NOTIFICATION_OPENED.equals(intent.getAction())) {	        	
        	//判断APP是否处于运行状态,点击后进入不同页面
        	Intent pushIntent = new Intent();
        	//String title = bundle.getString(JPushInterface.EXTRA_NOTIFICATION_TITLE);
        	//String content = bundle.getString(JPushInterface.EXTRA_ALERT);
        	String content = bundle.getString(JPushInterface.EXTRA_EXTRA);
    	    JSONObject json = JSONObject.parseObject(content);
    	    if(!json.containsKey("url") && TextUtils.isEmpty(json.getString("url"))) return;
            
//            pushIntent.putExtra(YunWeiUtils.YUNWEI_PUSHMESSAGE,json.getString("url"));
            context.startActivity(pushIntent);
        } else if (JPushInterface.ACTION_RICHPUSH_CALLBACK.equals(intent.getAction())) {
        } else if(JPushInterface.ACTION_CONNECTION_CHANGE.equals(intent.getAction())) {
        } else {
        }
	}
	
	/*
	 * 自定义BigNotification消息推送，调用传入icon、标题、内容等展示消息即可
	 * */
//	@SuppressLint("InlinedApi")
//	private void bigNotificationPush(Context context, Bundle bundle)
//	{
//		//构造NotificationBuilder
//		NotificationCompat.Builder builder = new NotificationCompat.Builder(context);
//		builder.setSmallIcon(R.drawable.ic_launcher);
//		builder.setContentTitle(context.getString(R.string.app_name));
//		//builder.setContentText(bundle.getString(JPushInterface.EXTRA_MESSAGE));
//		builder.setDefaults(Notification.DEFAULT_ALL);
//		builder.setDefaults(Notification.FLAG_AUTO_CANCEL);
//		
//		builder.setStyle(new NotificationCompat.BigTextStyle().bigText(bundle.getString(JPushInterface.EXTRA_MESSAGE)));
//		builder.setPriority(Notification.PRIORITY_DEFAULT);
//		
//		Intent notificationIntent = new Intent();
//        
//        if(isRunningApp(context,"com.solarke.android")){
//        	notificationIntent = new Intent(Intent.ACTION_MAIN);
//        	notificationIntent.addCategory(Intent.CATEGORY_LAUNCHER);
//        }
//        else{
////        	notificationIntent = new Intent(context, ActivitySplash.class);
//        }
//        
//        PendingIntent contentIntent =
//        		PendingIntent.getActivity(context, 0, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);
//        
//        builder.setContentIntent(contentIntent);
//        
//        NotificationManager		mNotificationManager;
//		mNotificationManager = (NotificationManager)context.getSystemService(Context.NOTIFICATION_SERVICE);
//		mNotificationManager.notify(bundle.getInt(JPushInterface.EXTRA_NOTIFICATION_ID), builder.build());
//	}
	
	/*
	 * 判断当前APP是否处于运行状态
	 * **/
	private static boolean isRunningApp(Context context, String packageName)
	{	
		boolean isRunningApp = false;
		
		ActivityManager activityManager = (ActivityManager)context.getSystemService(Context.ACTIVITY_SERVICE);
		
		List<RunningTaskInfo> list = activityManager.getRunningTasks(200);
		
		for(RunningTaskInfo info : list){
			if((info.topActivity.getPackageName().equals(packageName)) && (info.baseActivity.getPackageName().equals(packageName)))
			{
				isRunningApp = true;
				break;
			}
		}
		return isRunningApp;
	}
}