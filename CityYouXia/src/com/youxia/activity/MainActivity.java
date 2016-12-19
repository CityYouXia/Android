package com.youxia.activity;

import java.util.ArrayList;

import com.youxia.BaseFragment;
import com.youxia.BaseFragmentActivity;
import com.youxia.R;
import com.youxia.fragment.FragmentMy;
import com.youxia.fragment.FragmentTask;
import com.youxia.fragment.FragmentHome;
import com.youxia.map.BaiduMapLocation;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.PixelFormat;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.View;
import android.widget.RelativeLayout;
import net.tsz.afinal.FinalActivity;
import net.tsz.afinal.annotation.view.ViewInject;

public class MainActivity extends BaseFragmentActivity {
	
	@ViewInject(id=R.id.activity_main_bottom_bar_home,click="btnClick") 		RelativeLayout	fragmentHomePage;
	@ViewInject(id=R.id.activity_main_bottom_bar_task,click="btnClick") 		RelativeLayout 	fragmentTaskPage;
	@ViewInject(id=R.id.activity_main_bottom_bar_my,click="btnClick") 			RelativeLayout 	fragmentMyPage;
	
	//底部Tablayout
	private static final  String 				HOME_TABLINDEX		="MAINACTIVIY_TABINDEX";
	private int 								tabIndex			= 0;
	private ArrayList<View>						mTabLayouts			= null;
	
	private static final  String[] 				FRAGMENT_TAGS = {FragmentHome.TAG,FragmentTask.TAG,FragmentMy.TAG};
	private Fragment 							mFragmentContent 	= null;		//当前页
	private ArrayList<Fragment>					mFragmentList		= null;		//Fragment列表
	
	private BaiduMapLocation					mLocation			= null;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		FinalActivity.initInjectedView(this);
		
		getWindow().setFormat(PixelFormat.TRANSLUCENT);
		
		mLocation = new BaiduMapLocation();
		mLocation.initLocation(this,30000);
		
		initView(savedInstanceState);
	}
	
	@Override
	public View onCreateView(String name, Context context, AttributeSet attrs) {
		return super.onCreateView(name, context, attrs);
	}
	
	@Override
	protected void onSaveInstanceState(Bundle outState) {
		//方法一：保存tab选中的状态
		outState.putInt(HOME_TABLINDEX, tabIndex);
		super.onSaveInstanceState(outState);
	}
	
	protected void initView(Bundle savedInstanceState) {
		mTabLayouts = new ArrayList<View>();
		mTabLayouts.add(fragmentHomePage);
		mTabLayouts.add(fragmentTaskPage);
		mTabLayouts.add(fragmentMyPage);
		
		//初始化Fragment
		mFragmentList = new ArrayList<Fragment>();
		if (savedInstanceState != null) 
		{
			//读取上一次界面Save的时候tab选中的状态
			tabIndex = savedInstanceState.getInt(HOME_TABLINDEX, tabIndex);
			
			FragmentManager 	fm = getSupportFragmentManager();  
			FragmentTransaction ft = fm.beginTransaction();
			
			FragmentHome homeFragment = (FragmentHome)fm.findFragmentByTag(FragmentHome.TAG);
			if (homeFragment != null) mFragmentList.add(homeFragment);
			
			FragmentTask monitorFragment = (FragmentTask)fm.findFragmentByTag(FragmentTask.TAG);
			if (monitorFragment != null) mFragmentList.add(monitorFragment);
			
			FragmentMy informationFragment = (FragmentMy)fm.findFragmentByTag(FragmentMy.TAG);
			if (informationFragment != null) mFragmentList.add(informationFragment);
			
			for (int i = 0; i < mTabLayouts.size(); i++) {
				if (i == tabIndex) {
					mTabLayouts.get(i).setSelected(true);
				}
				else {
					mTabLayouts.get(i).setSelected(false);					
				}
			}
			
			for (int i = 0; i < mFragmentList.size(); i++)
			{
				Fragment frag = mFragmentList.get(i);
				if (frag == null) continue;
				if (frag.getTag().equals(FRAGMENT_TAGS[tabIndex])) 
				{
		    		ft.show(frag);
		    		mFragmentContent = frag;
				}
				else {
					ft.hide(frag);
				}
			}
			
			ft.commit();
		}
		else 
		{
			FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
			
			this.mFragmentContent = new FragmentHome();
			ft.replace(R.id.activity_main_fragment_container, this.mFragmentContent, FragmentHome.TAG);
			ft.commit();
			
			mTabLayouts.get(0).setSelected(true);
			
			mFragmentList.add(this.mFragmentContent);
		}
	}

	@Override
	public void onAttachFragment(Fragment fragment) {
		super.onAttachFragment(fragment);
		
		//防止在内存不足或其他情况下fragment被清理，fragment重叠
		if (mFragmentList != null) {
			boolean existflag = false;
			int i = 0;
			int size = mFragmentList.size();
			if  (fragment instanceof FragmentHome)
			{
				for (i = 0; i < size; i++)
				{
					if (mFragmentList.get(i) != null && mFragmentList.get(i) instanceof FragmentHome) {
						existflag = true;
						break;
					}
				}
				if (!existflag) mFragmentList.add(fragment);
			}
			else if (fragment instanceof FragmentTask) 
			{
				for (i = 0; i < size; i++)
				{
					if (mFragmentList.get(i) != null && mFragmentList.get(i) instanceof FragmentTask) {
						existflag = true;
						break;
					}
				}
				if (!existflag) mFragmentList.add(fragment);
			}
			else if (fragment instanceof FragmentMy) 
			{
				for (i = 0; i < size; i++)
				{
					if (mFragmentList.get(i) != null && mFragmentList.get(i) instanceof FragmentMy) {
						existflag = true;
						break;
					}
				}
				if (!existflag) mFragmentList.add(fragment);
			}
		}
	}
	
	public Fragment getFragmentByTag(ArrayList<Fragment> fragmentList, String tag)
	{
		for (int i = 0; i < fragmentList.size(); i++) {					
			if (tag.equals(fragmentList.get(i).getTag())){
				return fragmentList.get(i);
			}
		}
		
		return null;
	}

	public void btnClick(View v) 
	{
		Fragment	newFragment 	= null;
		
		String      tagName = "";

		switch (v.getId()) {
		case R.id.activity_main_bottom_bar_home:					//首页
			if (!(mFragmentContent instanceof FragmentHome))		
			{
				newFragment  = getFragmentByTag(mFragmentList, "FragmentTaskInWaiting");
				if (newFragment == null) newFragment = new FragmentHome();			
				
				tagName = FragmentHome.TAG;
				
				tabIndex = 0;
			}
			break;
		case R.id.activity_main_bottom_bar_task:
			if (!(mFragmentContent instanceof FragmentTask))		
			{
				newFragment  = getFragmentByTag(mFragmentList, "FragmentTask");
				if (newFragment == null) newFragment = new FragmentTask();					
				
				tagName = FragmentTask.TAG;
				
				tabIndex = 1;
			}
			break;
		case R.id.activity_main_bottom_bar_my:
			if (!(mFragmentContent instanceof FragmentMy))
			{
				newFragment  = getFragmentByTag(mFragmentList, "FragmentMyPage");
				if (newFragment == null) newFragment = new FragmentMy();					
				
				tagName = FragmentMy.TAG;
				
				tabIndex = 2;
			}
			break;
		default:
			break;
		}
		
		if (newFragment != null) switchFragment(mFragmentContent, newFragment, tagName);
		
		for (int i = 0; i < mTabLayouts.size(); i++) {
			RelativeLayout tmplayout = (RelativeLayout)findViewById(v.getId());
			if (tmplayout.equals(mTabLayouts.get(i))) 
			{
				mTabLayouts.get(i).setSelected(true);
			}
			else mTabLayouts.get(i).setSelected(false);
		}
	}
	
	private void switchFragment(Fragment oldFragment, Fragment newFragment, String tagName) {
		if (oldFragment == null || newFragment == null)	return;
		
		FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
    	
    	if (!newFragment.isAdded()) {
    		fragmentTransaction.hide(oldFragment);
    		fragmentTransaction.add(R.id.activity_main_fragment_container, newFragment, tagName);
    		fragmentTransaction.commit();
    	}
    	else {
    		fragmentTransaction.hide(oldFragment);
    		fragmentTransaction.show(newFragment);
    		fragmentTransaction.commit();
    	}
    	    	
    	//系统资源回收
    	((BaseFragment)oldFragment).recycle();    	
    	
    	mFragmentContent = newFragment;
	}
	
	public boolean onKeyDown(int keyCode, KeyEvent event){
		if(keyCode == KeyEvent.KEYCODE_BACK)
		{
			exitApp();
			return false;
		}
		
		return super.onKeyDown(keyCode, event);
	}
	

	//退出程序提示
	private void exitApp()
	{		
		AlertDialog.Builder alertbBuilder = new AlertDialog.Builder(this);
		alertbBuilder.setTitle("提示").setMessage("确定要退出程序吗？");
		alertbBuilder.setPositiveButton("确定", new DialogInterface.OnClickListener() {    
		    @Override   
		    public void onClick(DialogInterface dialog, int which) {
//					solarLocation.onDestroy();
		            // 结束这个Activity    
                    int nPid = android.os.Process.myPid();    
                    android.os.Process.killProcess(nPid);
                    //System.exit(0);
                    //获取当前位置经纬度
//    				PreferencesUtils.putString(ActivityHome.this, SolarKECommon.KEY_CURRENTLANTITUDE, "");
//    				PreferencesUtils.putString(ActivityHome.this, SolarKECommon.KEY_CURRENTLONTITUDE, "");
		        }    
		    });
	        
        alertbBuilder.setNegativeButton("取消", new DialogInterface.OnClickListener() {    
            @Override   
            public void onClick(DialogInterface dialog, int which) {    
                dialog.cancel();    
            }    
        }).create();
        
        alertbBuilder.show();
	}
	
	@Override
	protected void onDestroy() {
		super.onDestroy();
	}
	
	@Override
	protected void onPause() {
		mLocation.onStop();
		super.onPause();
	}
	
	@Override
	protected void onResume() {
		super.onResume();
	}
	
	@Override
	protected void onStart() {
		mLocation.onStart();
		super.onStart();
	}
	
	@Override
	protected void onStop() {
		mLocation.onStop();
		super.onStop();
	}
}
