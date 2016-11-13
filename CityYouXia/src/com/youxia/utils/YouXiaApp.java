package com.youxia.utils;

import cn.jpush.android.api.JPushInterface;

import com.baidu.mapapi.SDKInitializer;
import com.tencent.bugly.crashreport.CrashReport;

import android.app.Application;
import android.content.Context;
import net.tsz.afinal.FinalBitmap;

public class YouXiaApp extends Application 
{
	private static	 YouXiaApp 		mContext;
	public static	 FinalBitmap	mFinalBitmap;
	
	private static		String		mLocation = "";
	
	@Override
	public void onCreate() 
	{
		super.onCreate();
		mContext = this;
		
		mFinalBitmap = FinalBitmap.create(this);
		//初始化调用一次即可
		PreferencesUtils.init("YouXia.Golbal", MODE_MULTI_PROCESS);
		//腾讯Bugly初始化
		CrashReport.initCrashReport(getApplicationContext(), "474b2320a8", false);
		//百度地图使用 
		SDKInitializer.initialize(this);
		//极光推送
		JPushInterface.init(this);
	}
	
	public static Context getAppContext()
	{
	        return mContext;
	}

	public static String getmLocation() {
		return mLocation;
	}

	public static void setmLocation(String mLocation) {
		YouXiaApp.mLocation = mLocation;
	}
}