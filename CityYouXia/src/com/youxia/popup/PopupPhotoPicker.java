package com.youxia.popup;

import com.youxia.R;
import com.youxia.activity.AlbumActivity;
import com.youxia.utils.YouXiaUtils;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import net.tsz.afinal.FinalActivity;
import net.tsz.afinal.annotation.view.ViewInject;

@SuppressLint("NewApi")
public class PopupPhotoPicker extends PopupWindow{

	private Activity		mContext;
	private View			mView;
	@ViewInject(id=R.id.popup_photopicker_selector,click="btnClick") 	RelativeLayout	mPopLayout;
	@ViewInject(id=R.id.popup_photopicker_camera,click="btnClick") 		TextView		mPopCamera;
	@ViewInject(id=R.id.popup_photopicker_album,click="btnClick") 		TextView		mPopAlbum;
	@ViewInject(id=R.id.popup_photopicker_cancel,click="btnClick") 		TextView		mPopCancel;
	
	@SuppressWarnings("deprecation")
	public PopupPhotoPicker(Activity activity){
		super(activity);
		mContext = activity;
		LayoutInflater inflater = (LayoutInflater)mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		mView = inflater.inflate(R.layout.popup_photopicker, null);
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
		this.setAnimationStyle(R.style.MyPopAnimStyle);
		//实例化一个ColorDrawable颜色为半透明
		ColorDrawable dw = new ColorDrawable(0000000000);
		//设置SelectPicPopupWindow弹出窗体的背景
		this.setBackgroundDrawable(dw);		
	}
	
	public void btnClick(View v) {
		Intent intent = new Intent();
		switch (v.getId()) {
		case R.id.popup_photopicker_selector:
			break;
		case R.id.popup_photopicker_camera:
			intent = new Intent("android.media.action.IMAGE_CAPTURE");
			mContext.startActivityForResult(intent, YouXiaUtils.PROBLEM_REPORT_CAMERA_RESULTCODE);
			break;
		case R.id.popup_photopicker_album:
			intent.setClass(mContext, AlbumActivity.class);
			mContext.startActivity(intent);
			break;
		case R.id.popup_photopicker_cancel:
			break;
		}
		dismiss();
	}
}
