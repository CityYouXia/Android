package com.youxia.utils;

import cn.jpush.android.api.JPushInterface;

import com.baidu.mapapi.SDKInitializer;
import com.tencent.bugly.crashreport.CrashReport;
import com.youxia.http.HttpClientHelper;

import android.app.Application;
import android.content.Context;
import net.tsz.afinal.FinalBitmap;

public class YouXiaApp extends Application 
{
	private static	 	YouXiaApp 		mContext;
	public static		FinalBitmap		mFinalBitmap;
	
	private static		String			mLocation 		= "";
	private static		double			mLongitude 		= 0;
	private static		double			mLatitude 		= 0;
	
	@Override
	public void onCreate() 
	{
		super.onCreate();
		mContext = this;
		
		mFinalBitmap = FinalBitmap.create(this);
		HttpClientHelper.initHttp();
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

	public static double getmLongitude() {
		return mLongitude;
	}

	public static void setmLongitude(double mLongitude) {
		YouXiaApp.mLongitude = mLongitude;
	}

	public static double getmLatitude() {
		return mLatitude;
	}

	public static void setmLatitude(double mLatitude) {
		YouXiaApp.mLatitude = mLatitude;
	}
}