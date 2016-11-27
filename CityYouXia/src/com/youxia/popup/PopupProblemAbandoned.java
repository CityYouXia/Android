package com.youxia.popup;

import com.youxia.R;
import com.youxia.utils.YouXiaActivityManager;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.PopupWindow;
import android.widget.TextView;
import net.tsz.afinal.FinalActivity;
import net.tsz.afinal.annotation.view.ViewInject;

@SuppressLint("NewApi")
public class PopupProblemAbandoned extends PopupWindow{
	
	private Activity		mContext;
	private View			mView;
	
	@ViewInject(id=R.id.popup_help_abandoned_cancel_tv,click="btnClick") 	TextView	mCancel;
	@ViewInject(id=R.id.popup_help_abandoned_confirm_tv,click="abandoned") 	TextView	mOK;
	
	@SuppressWarnings("deprecation")
	public PopupProblemAbandoned(Activity activity){
		super(activity);
		mContext = activity;
		LayoutInflater inflater = (LayoutInflater)mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		mView = inflater.inflate(R.layout.popup_help_abandoned, null);
		FinalActivity.initInjectedView(this,mView);
		
		int screenHeight = mContext.getWindowManager().getDefaultDisplay().getHeight();
		int screenWidth = mContext.getWindowManager().getDefaultDisplay().getWidth();
		
		//设置PopupWindow的View
		this.setContentView(mView);
		//设置PopupWindow弹出窗体的宽
		this.setWidth(screenWidth);
		//设置PopupWindow弹出窗体的高
		this.setHeight(screenHeight);
		
		//设置PopupWindow弹出窗体可点击
		this.setFocusable(true);
		//设置PopupWindow弹出窗体动画效果
		//this.setAnimationStyle(R.style.MySharePopAnimStyle);
		//实例化一个ColorDrawable颜色为半透明
		ColorDrawable dw = new ColorDrawable(0000000000);
		//设置SelectPicPopupWindow弹出窗体的背景
		this.setBackgroundDrawable(dw);	
	}
	
	public void btnClick(View v) {
		switch (v.getId()) {
		case R.id.popup_help_abandoned_cancel_tv:
			dismiss();
			break;
		default:
			break;
		}
	}
	
	public void abandoned(View v){
		YouXiaActivityManager.getAppManager().finishActivity(mContext);
		dismiss();
	}
}