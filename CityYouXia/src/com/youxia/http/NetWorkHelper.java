package com.youxia.http;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

public class NetWorkHelper {
	
	public static int NETWORK_NOUSED 					= 	0;
	public static int NETWORK_UNAVALID_OR_UNCONNECTED 	= 	1;
	public static int NETWORK_WIFI   					=  	2;
	public static int NETWORK_MOBIL  					=  	3;
	
	public static int NETWORK_ERROR  					=  -100;
	
	public static int getNetworkStatus(Context context)
	{
		ConnectivityManager connectivity = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
		if (connectivity == null) return NETWORK_ERROR;
		
		NetworkInfo info = connectivity.getActiveNetworkInfo();
		if (info == null) {
			return NETWORK_NOUSED;
		}
		else {			
			if (info.isAvailable() && info.getState() == NetworkInfo.State.CONNECTED) {
				if (info.getType() == ConnectivityManager.TYPE_WIFI) return NETWORK_WIFI;
				else if (info.getType() == ConnectivityManager.TYPE_MOBILE) return NETWORK_MOBIL;
			}	
			else return NETWORK_UNAVALID_OR_UNCONNECTED;
		}
		
	
		return NETWORK_NOUSED;
	}
}
