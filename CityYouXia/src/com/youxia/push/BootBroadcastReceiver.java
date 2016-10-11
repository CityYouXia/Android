package com.youxia.push;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

/**
 * BootBroadcastReceiver是监听开机service是否启动的静态监听器
 * AppSettingFragment下推送设置的开关可以动态设置监听器是否使用
 * */
public class BootBroadcastReceiver extends BroadcastReceiver{

	@Override
	public void onReceive(Context context, Intent intent) 
	{
		if (Intent.ACTION_BOOT_COMPLETED.equals(intent.getAction()))
		{
			Intent startServiceIntent = new Intent(context, YouXiaPushService.class);
			startServiceIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			context.startService(startServiceIntent);
		}
	}
}
