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
	
	@Override
	public void onCreate() 
	{
		super.onCreate();
		mContext = this;
		mFinalBitmap = FinalBitmap.create(this);
		//初始化调用一次即可
		PreferencesUtils.init("SolarKE_YunWei.Golbal", MODE_MULTI_PROCESS);
		//短信SMS
//		SMSSDK.initSDK(this, "1754c2e667a16", "ec8508af82d1b3e8b5e1137d72041439");
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
}