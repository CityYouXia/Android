package com.youxia.popup;

import com.youxia.R;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.PopupWindow;
import android.widget.TextView;
import net.tsz.afinal.FinalActivity;
import net.tsz.afinal.annotation.view.ViewInject;

@SuppressLint({ "NewApi", "InflateParams" })
public class PopupLocation extends PopupWindow{
	
	private Activity		mContext;
	
	@ViewInject(id=R.id.popup_location_rl,click="btnClick") 			View		mView;
	@ViewInject(id=R.id.popup_location_et) 								EditText	mLocationET;
	@ViewInject(id=R.id.popup_location_cancel_tv,click="btnClick") 		TextView	mCancelTV;
	@ViewInject(id=R.id.popup_location_ok_tv,click="commit") 			TextView	mCommitTV;
	
	public String location = "";
	
	@SuppressWarnings("deprecation")
	public PopupLocation(Activity activity){
		super(activity);
		mContext = activity;
		LayoutInflater inflater = (LayoutInflater)mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		mView = inflater.inflate(R.layout.popup_location, null);
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
		case R.id.popup_location_cancel_tv:
		case R.id.popup_location_rl:
			dismiss();
			break;
		default:
			break;
		}
	}
	
	public void commit(View v){
		String loc = mLocationET.getText().toString();
		if(TextUtils.isEmpty(loc) || TextUtils.equals(loc, "")){
			dismiss();
			return;
		}
		location = mLocationET.getText().toString();
		dismiss();
	}
}