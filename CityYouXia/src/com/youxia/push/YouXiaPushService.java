package com.youxia.push;

import java.util.LinkedHashSet;
import java.util.Set;

import com.youxia.R;

import android.annotation.SuppressLint;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Handler;
import android.os.IBinder;
import android.util.Log;
import cn.jpush.android.api.JPushInterface;
import cn.jpush.android.api.CustomPushNotificationBuilder;
import cn.jpush.android.api.TagAliasCallback;

public class YouXiaPushService extends Service{
	
	private static final String TAG = YouXiaPushService.class.getSimpleName();
	
	private AlarmManager 			mAlarmManager 	= null;
	private PendingIntent 			mPendingIntent 	= null;
	private boolean					mAliasFlag		= false;
	private boolean					mTagFlag		= false;
	private boolean					mPushFlag		= false;
	
	//receiver
	private YouXiaPushReceiver		mMessageReceiver;
	
	//Service常量定义
	private static final long		FreshTime		= 60 * 1000;  //心跳周期
	
	@Override
	public IBinder onBind(Intent intent) 
	{
		return null;
	}
	
	@Override
	public void onCreate() 
	{
		//启动Service，并开启心跳循环
	    Intent intent = new Intent(getApplicationContext(), YouXiaPushService.class);        
		mAlarmManager = (AlarmManager)getSystemService(ALARM_SERVICE);
		mPendingIntent = PendingIntent.getService(this, 0, intent, Intent.FLAG_ACTIVITY_NEW_TASK);
		long now = System.currentTimeMillis();
		mAlarmManager.setInexactRepeating(AlarmManager.RTC, now, FreshTime, mPendingIntent);
		JPushInterface.init(getApplicationContext());
		CustomPushNotificationBuilder customBuilder = new CustomPushNotificationBuilder(getApplicationContext(), 
														R.layout.customer_notitfication_layout, 
														R.id.customnotify_icon, 
														R.id.customnotify_title,
														R.id.customnotify_text);
		customBuilder.layoutIconDrawable = R.drawable.ic_launcher;
		JPushInterface.setPushNotificationBuilder(2, customBuilder);
		
		//设置标签和别名
		setTag();
		setAlias();
		//动态注册监听器
		registerMessageReceiver();
		super.onCreate();
	}
	
	@Override
	public void onDestroy()
	{
		mAliasFlag	= false;
		mTagFlag	= false;
		mPushFlag	= false;
		JPushInterface.stopPush(getApplicationContext());
		unRegisterMessageReceiver();
		super.onDestroy();
	}
	
	//Service启动后回调，每次心跳都会回调
	@Override
	public int onStartCommand(Intent intent, int flags, int startId) 
	{
		//考虑SolarKEPushService被杀掉，服务重启需要重启PushService
		if(mPushFlag == false) 
		{
			JPushInterface.init(getApplicationContext());
			
			JPushInterface.resumePush(getApplicationContext());
			mPushFlag = true;
		}
		//别名和标签是否设置成功，否则重新设置
		if(!mAliasFlag) setAlias();
		if(!mTagFlag)	setTag();
		
		return START_STICKY;
	}
	
	private final TagAliasCallback mAliasCallback = new TagAliasCallback() {

        @Override
        public void gotResult(int code, String alias, Set<String> tags) {
            String logs ;
            switch (code) {
            case 0:
                logs = "Set tag and alias success";
                mAliasFlag = true;
                Log.i(TAG, logs);
                break;
                
            case 6002:
                logs = "Failed to set alias and tags due to timeout. Try again after 60s.";
                Log.i(TAG, logs);
                if (YouXiaPushUtil.isConnected(getApplicationContext())) {
                	mHandler.sendMessageDelayed(mHandler.obtainMessage(MSG_SET_ALIAS, alias), 1000 * 60);
                } else {
                	Log.i(TAG, "No network");
                }
                break;
                
            default:
                logs = "Failed with errorCode = " + code;
                mAliasFlag = false;
                Log.e(TAG, logs);
            }
        }
	};
	
    private final TagAliasCallback mTagsCallback = new TagAliasCallback() {
        @Override
        public void gotResult(int code, String alias, Set<String> tags) {
            String logs ;
            switch (code) {
            case 0:
                logs = "Set tag and alias success";
                mTagFlag = true;
                Log.i(TAG, logs);
                break;
                
            case 6002:
                logs = "Failed to set alias and tags due to timeout. Try again after 60s.";
                Log.i(TAG, logs);
                if (YouXiaPushUtil.isConnected(getApplicationContext())) {
                	mHandler.sendMessageDelayed(mHandler.obtainMessage(MSG_SET_TAGS, tags), 1000 * 60);
                } else {
                	Log.i(TAG, "No network");
                }
                break;
            
            default:
                logs = "Failed with errorCode = " + code;
                mTagFlag = false;
                Log.e(TAG, logs);
            }
        }
    };
    
	private static final int MSG_SET_ALIAS = 1001;
	private static final int MSG_SET_TAGS = 1002;

    @SuppressLint("HandlerLeak")
	private final Handler mHandler = new Handler() {
        @SuppressWarnings("unchecked")
		@Override
        public void handleMessage(android.os.Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
            case MSG_SET_ALIAS:
                Log.d(TAG, "Set alias in handler.");
                JPushInterface.setAliasAndTags(getApplicationContext(), (String) msg.obj, null, mAliasCallback);
                break;
                
            case MSG_SET_TAGS:
                Log.d(TAG, "Set tags in handler.");
                JPushInterface.setAliasAndTags(getApplicationContext(), null, (Set<String>) msg.obj, mTagsCallback);
                break;
                
            default:
                Log.i(TAG, "Unhandled msg - " + msg.what);
            }
        }
    };
    	
	private void setTag(){
		
//		String tag = PreferencesUtils.getString(YunWeiApp.getAppContext(), YunWeiUtils.KEY_ELECTRIC_PUSHTAG, "");
		String tag = "";
		if (null == tag || tag == "") return;
		
		//  ","隔开的多个 转换成 Set
		String[] sArray = tag.split(",");
		Set<String> tagSet = new LinkedHashSet<String>();
		for (String sTagItme : sArray) {
			if(YouXiaPushUtil.isValidTagAndAlias(sTagItme)) {
				tagSet.add(sTagItme);
			}
		}
		
		//调用JPush API设置Tag
		mHandler.sendMessage(mHandler.obtainMessage(MSG_SET_TAGS, tagSet));

	}
	
	private void setAlias(){
		
//		String alias = PreferencesUtils.getString(ElectricApplication.getmContext(), ElectricCommon.KEY_ELECTRIC_PUSHALIAS, "");
		String alias = "test";
		if (null == alias || alias == "") return;
		if(!YouXiaPushUtil.isValidTagAndAlias(alias)) return;
		
		//调用JPush API设置Alias
		mHandler.sendMessage(mHandler.obtainMessage(MSG_SET_ALIAS, alias));
	}
	
	public void registerMessageReceiver() {
		mMessageReceiver = new YouXiaPushReceiver();
		IntentFilter filter = new IntentFilter();
		filter.setPriority(IntentFilter.SYSTEM_HIGH_PRIORITY);
		registerReceiver(mMessageReceiver, filter);
	}
	
	public void unRegisterMessageReceiver() {
		unregisterReceiver(mMessageReceiver);
	}
}
