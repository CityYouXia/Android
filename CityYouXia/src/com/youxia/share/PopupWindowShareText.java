package com.youxia.share;

import com.youxia.R;
import com.youxia.entity.ShareEntity;
import com.youxia.utils.YouXiaUtils;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.ClipboardManager;
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
import android.widget.Toast;

@SuppressLint("NewApi")
public class PopupWindowShareText extends PopupWindow implements OnClickListener{

	private Activity		mContext;
	private View			mView;
	private ShareEntity		mShareEntity;
	@SuppressWarnings("deprecation")
	public PopupWindowShareText(Activity activity, ShareEntity shareEntity){
		super(activity);
		mContext = activity;
		mShareEntity = shareEntity;
		LayoutInflater inflater = (LayoutInflater)mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		mView = inflater.inflate(R.layout.popup_share_text, null);
		
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
		mShareLL = (LinearLayout)mView.findViewById(R.id.popup_share_qq_share);
		mShareLL.setOnClickListener(this);
		mShareLL = (LinearLayout)mView.findViewById(R.id.popup_share_shortmessage);
		mShareLL.setOnClickListener(this);
		mShareLL = (LinearLayout)mView.findViewById(R.id.popup_share_email);
		mShareLL.setOnClickListener(this);
		mShareLL = (LinearLayout)mView.findViewById(R.id.popup_share_copy);
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
		case R.id.popup_share_qq_share:
			platform = YouXiaUtils.PLATFORMQQ;
			break;
		case R.id.popup_share_shortmessage:
			platform = YouXiaUtils.PLATFORMSHORTMESSAGE;
			break;
		case R.id.popup_share_email:
			platform = YouXiaUtils.PLATFORMEMAIL;
			break;
		case R.id.popup_share_copy:
			copy();
			break;
		default:
			platform = "";
			break;
		}
		if(TextUtils.equals("", platform) || TextUtils.isEmpty(platform)) return;
		else showShare(platform);
	}
	
	@SuppressWarnings("deprecation")
	private void copy() {
		if(mShareEntity == null) return;
		
		ClipboardManager clipboardManager= (ClipboardManager)mContext.getSystemService(Context.CLIPBOARD_SERVICE);
		clipboardManager.setText(mShareEntity.content);//设置Clipboard 的内容
//		YouXiaUtils.showToast(mContext, "位置信息已复制到剪贴板");
		YouXiaUtils.showToast(mContext, "信息已复制到剪贴板", 0);
		dismiss();
	}

	private void showShare(String platform) {
		try {
			mShareEntity.platform = platform;
			ShareUtil su = new ShareUtil(mContext);
			su.creatShareInfo(mContext, mShareEntity);
			dismiss();
		} catch (Exception e) {
			e.getMessage();
		}
	}
}
