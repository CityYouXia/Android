package com.youxia.popup;

import com.youxia.R;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.PopupWindow;
import android.widget.TextView;
import net.tsz.afinal.FinalActivity;
import net.tsz.afinal.annotation.view.ViewInject;

@SuppressLint({ "NewApi", "InflateParams" })
public class PopupReward extends PopupWindow{
	
	private Activity		mContext;
	
	@ViewInject(id=R.id.popup_reward_rl,click="btnClick") 			View		mView;
	@ViewInject(id=R.id.popup_reward_et) 							EditText	mRewardET;
	@ViewInject(id=R.id.popup_reward_cancel_tv,click="btnClick") 	TextView	mCancelTV;
	@ViewInject(id=R.id.popup_reward_ok_tv,click="commit") 			TextView	mCommitTV;
	
	public int reward = 0;
	
	@SuppressWarnings("deprecation")
	public PopupReward(Activity activity){
		super(activity);
		mContext = activity;
		LayoutInflater inflater = (LayoutInflater)mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		mView = inflater.inflate(R.layout.popup_reward, null);
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
		case R.id.popup_reward_cancel_tv:
		case R.id.popup_reward_rl:
			dismiss();
			break;
		default:
			break;
		}
	}
	
	public void commit(View v){
		int re = Integer.valueOf(mRewardET.getText().toString());
		if(re <= 0) return;
		reward = re;
		dismiss();
	}
}