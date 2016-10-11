package com.youxia.share;

import com.youxia.R;
import com.youxia.entity.ShareEntity;
import com.youxia.utils.YouXiaUtils;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class PopupWindowShare extends PopupWindow implements OnClickListener{

	private Activity		mContext;
	private View			mView;
	private ShareEntity		mShareEntity;
	private int				mShareType	= -1;
	@SuppressWarnings("deprecation")
	public PopupWindowShare(Activity activity, ShareEntity shareEntity, int shareType){
		super(activity);
		mContext = activity;
		mShareEntity = shareEntity;
		mShareType = shareType;
		LayoutInflater inflater = (LayoutInflater)mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		mView = inflater.inflate(R.layout.popup_share, null);
		
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
		initView();
	}
	
	private void initView() {
		RelativeLayout mRelativeLayout = (RelativeLayout)mView.findViewById(R.id.popup_share);
		mRelativeLayout.setOnClickListener(this);
		LinearLayout mShareLL = (LinearLayout)mView.findViewById(R.id.popup_share_wechat_share);
		mShareLL.setOnClickListener(this);
		mShareLL = (LinearLayout)mView.findViewById(R.id.popup_share_wechatmoments_share);
		mShareLL.setOnClickListener(this);
		mShareLL = (LinearLayout)mView.findViewById(R.id.popup_share_qq_share);
		mShareLL.setOnClickListener(this);
		mShareLL = (LinearLayout)mView.findViewById(R.id.popup_share_shortmessage);
		mShareLL.setOnClickListener(this);
		mShareLL = (LinearLayout)mView.findViewById(R.id.popup_share_qzone);
		mShareLL.setOnClickListener(this);
		mShareLL = (LinearLayout)mView.findViewById(R.id.popup_share_email);
		mShareLL.setOnClickListener(this);
		TextView mCancel = (TextView)mView.findViewById(R.id.popup_share_cancel);
		mCancel.setOnClickListener(this);
	}

	public void onClick(View v) {
		String platform = "";
		switch (v.getId()) {
		case R.id.popup_share:
			dismiss();
			break;
		case R.id.popup_share_cancel:
			dismiss();
			break;
		case R.id.popup_share_wechat_share:
			platform = YouXiaUtils.PLATFORMWECHAT;
			break;
		case R.id.popup_share_wechatmoments_share:
			platform = YouXiaUtils.PLATFORMWECHATMOMENTS;
			break;
		case R.id.popup_share_qq_share:
			platform = YouXiaUtils.PLATFORMQQ;
			break;
		case R.id.popup_share_shortmessage:
			platform = YouXiaUtils.PLATFORMSHORTMESSAGE;
			break;
		case R.id.popup_share_qzone:
			platform = YouXiaUtils.PLATFORMQZONE;
			break;
		case R.id.popup_share_email:
			platform = YouXiaUtils.PLATFORMEMAIL;
			break;
		default:
			platform = "";
			break;
		}
		if(TextUtils.equals("", platform) || TextUtils.isEmpty(platform)) return;
		else showShare(platform);
	}
	
	private void showShare(String platform) {
		try {
			mShareEntity.platform = platform;
			ShareUtil su = new ShareUtil(mContext);
			switch (mShareType) {
			case -1:
				return;
			case YouXiaUtils.SHARETEXT:
				if(TextUtils.equals(platform, YouXiaUtils.PLATFORMEMAIL)){
					mShareEntity.content = mShareEntity.content;
				}
				if(TextUtils.equals(platform, YouXiaUtils.PLATFORMSHORTMESSAGE)){
					mShareEntity.content = mShareEntity.content;
				}
				su.creatShareText(mShareEntity);
				break;
			case YouXiaUtils.SHAREWEBPAGE:
				if(TextUtils.equals(platform, YouXiaUtils.PLATFORMWECHATMOMENTS)){
					mShareEntity.title = mShareEntity.title;
				}
				if(TextUtils.equals(platform, YouXiaUtils.PLATFORMEMAIL)){
					mShareEntity.content = mShareEntity.content + "\n" + mShareEntity.url;
				}
				if(TextUtils.equals(platform, YouXiaUtils.PLATFORMSHORTMESSAGE)){
					mShareEntity.content = mShareEntity.title + "\n" + mShareEntity.url;
				}
				su.creatShareWeb(mShareEntity);
				break;
			default:
				return;
			}
			dismiss();
		} catch (Exception e) {
			e.getMessage();
		}
	}
}
