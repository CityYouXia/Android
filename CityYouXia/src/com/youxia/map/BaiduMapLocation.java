package com.youxia.map;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.location.LocationClientOption.LocationMode;
import com.youxia.utils.YouXiaApp;

import android.content.Context;

public class BaiduMapLocation {
	private LocationClient		mLocationClient;		//定位客户端
	private MyLocationListener	mMyLocationListener;	//定位的监听器
	private Context				mContext;				
	
	public void initLocation(Context context, int scanPeriod){
		if(null == context) return;
		mContext = context;
		mLocationClient = new LocationClient(context);
		mMyLocationListener = new MyLocationListener();
		mLocationClient.registerLocationListener(mMyLocationListener);
		// 设置定位的相关配置
		LocationClientOption option = new LocationClientOption();
		option.setLocationMode(LocationMode.Hight_Accuracy);
		option.setCoorType("bd09ll"); // 设置坐标类型
		if(scanPeriod > 0 ){
			option.setScanSpan(scanPeriod);
		}
		else {
			option.setScanSpan(600000);
		}
		option.setIsNeedAddress(true); //可选，设置是否需要地址信息，默认不需要
		option.setOpenGps(true); //可选，默认false,设置是否使用gps
		option.setLocationNotify(true); //可选，默认false，设置是否当gps有效时按照1S1次频率输出GPS结果
		option.setIgnoreKillProcess(false); //可选，默认true，定位SDK内部是一个SERVICE，并放到了独立进程，设置是否在stop的时候杀死这个进程，默认不杀死  
		option.setPriority(LocationClientOption.GpsFirst);
		option.setAddrType("all");
		mLocationClient.setLocOption(option);
	}
	
	//实现实位回调监听
	public class MyLocationListener implements BDLocationListener
	{
		@Override
		public void onReceiveLocation(BDLocation location)
		{
			if (location == null)
				return;
			double latitude = location.getLatitude();
			double longitude = location.getLongitude();
			String city = location.getCity();
			
			YouXiaApp.setmLocation(location.getAddrStr());
		}
	}
	
	// 退出时销毁定位
	public void onDestroy() {
		mLocationClient.unRegisterLocationListener(mMyLocationListener);
		mLocationClient.stop();
	}

	//启动时启动定位
	public void onStart()
	{
		if (!mLocationClient.isStarted())
		{
			mLocationClient.start();
		}
	}

	// 暂停定位
	public void onStop() {
		mLocationClient.unRegisterLocationListener(mMyLocationListener);
		mLocationClient.stop();
	}
}
