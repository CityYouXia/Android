package com.youxia;

import com.youxia.utils.YouXiaActivityManager;

import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;
import net.tsz.afinal.FinalActivity;

public abstract class BaseActivity extends FinalActivity{
	
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		//取消标题栏（ActionBar）
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		//禁止横屏
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
		//启动activity时不自动弹出软键盘
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        // 添加Activity到堆栈  
        YouXiaActivityManager.getAppManager().addActivity(this);
	}
	
	public void recycle() {
		
	}
}
