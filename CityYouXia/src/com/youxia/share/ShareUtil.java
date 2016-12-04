package com.youxia.share;

import java.io.File;
import java.util.HashMap;

import com.tencent.connect.share.QQShare;
import com.tencent.tauth.IUiListener;
import com.tencent.tauth.Tencent;
import com.tencent.tauth.UiError;
import com.youxia.entity.ShareEntity;
import com.youxia.utils.YouXiaUtils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import cn.sharesdk.framework.Platform;
import cn.sharesdk.framework.PlatformActionListener;
import cn.sharesdk.framework.ShareSDK;
import cn.sharesdk.framework.Platform.ShareParams;

public class ShareUtil implements PlatformActionListener,IUiListener {

	private Context				mContext;
	
	public ShareUtil(Context context) {
		super();
		mContext = context;
		ShareSDK.initSDK(mContext);
	}
	
	public void creatShareWeb(ShareEntity shareEntity){
		ShareParams sp = new ShareParams();
		//短信分享
		if(TextUtils.equals(shareEntity.platform, YouXiaUtils.PLATFORMSHORTMESSAGE)) {
			Uri smsToUri = Uri.parse( "smsto:" );
	    	Intent sendIntent =  new  Intent(Intent.ACTION_VIEW, smsToUri);
	    	sendIntent.putExtra( "sms_body" ,shareEntity.content);
	    	sendIntent.setType( "vnd.android-dir/mms-sms" );
	    	mContext.startActivity(sendIntent);
		}
		//邮件分享
		if(TextUtils.equals(shareEntity.platform, YouXiaUtils.PLATFORMEMAIL)) {
			Intent email =  new  Intent(android.content.Intent.ACTION_SEND);  
	    	email.setType( "plain/text" );
	    	//设置邮件默认标题
	    	email.putExtra(android.content.Intent.EXTRA_SUBJECT, shareEntity.title);
	    	//设置要默认发送的内容
	    	email.putExtra(android.content.Intent.EXTRA_TEXT, shareEntity.content);
	    	//调用系统的邮件系统
	    	mContext.startActivity(Intent.createChooser(email,  "请选择邮件发送软件" ));
		}
		//QQ好友
		if(TextUtils.equals(shareEntity.platform, YouXiaUtils.PLATFORMQQ)) {
			sp.setTitle(shareEntity.title);
			sp.setTitleUrl(shareEntity.url);
	      	sp.setText(shareEntity.content);
	      	sp.setImageUrl(shareEntity.imageurl);
	      	Platform qq = ShareSDK.getPlatform(shareEntity.platform);
	      	qq.setPlatformActionListener(this);
	      	qq.share(sp);
		}
		//QQ空间
		if(TextUtils.equals(shareEntity.platform, YouXiaUtils.PLATFORMQZONE)) {
			sp.setTitle(shareEntity.title);
			sp.setTitleUrl(shareEntity.url);
	      	sp.setText(shareEntity.content);
	      	sp.setSite(shareEntity.title);
	      	sp.setSiteUrl(shareEntity.url);
	      	sp.setImageUrl(shareEntity.imageurl);
	      	Platform qzone = ShareSDK.getPlatform(shareEntity.platform);
	      	qzone.setPlatformActionListener(this);
	      	qzone.share(sp);
		}
		//微信好友
		if(TextUtils.equals(shareEntity.platform, YouXiaUtils.PLATFORMWECHAT)) {
			sp.setShareType(Platform.SHARE_WEBPAGE);
			sp.setTitle(shareEntity.title);
			sp.setText(shareEntity.content);
			sp.setImageUrl(shareEntity.imageurl);
			sp.setUrl(shareEntity.url);
	    	Platform wechat = ShareSDK.getPlatform(shareEntity.platform);
	    	wechat.setPlatformActionListener(this);
	    	wechat.share(sp);
		}
		//微信朋友圈
		if(TextUtils.equals(shareEntity.platform, YouXiaUtils.PLATFORMWECHATMOMENTS)) {
			sp.setShareType(Platform.SHARE_WEBPAGE);
			sp.setTitle(shareEntity.title);
			sp.setText(shareEntity.content);
			sp.setImageUrl(shareEntity.imageurl);
			sp.setUrl(shareEntity.url);
	    	Platform wechatmoments = ShareSDK.getPlatform(shareEntity.platform);
	    	wechatmoments.setPlatformActionListener(this);
	    	wechatmoments.share(sp);
		}
	}
	
	public void creatShareText(ShareEntity shareEntity){
		ShareParams sp = new ShareParams();
		//短信分享
		if(TextUtils.equals(shareEntity.platform, YouXiaUtils.PLATFORMSHORTMESSAGE)) {
			Uri smsToUri = Uri.parse( "smsto:" );
	    	Intent sendIntent =  new  Intent(Intent.ACTION_VIEW, smsToUri);
	    	sendIntent.putExtra( "sms_body" ,shareEntity.content);
	    	sendIntent.setType( "vnd.android-dir/mms-sms" );
	    	mContext.startActivity(sendIntent);
		}
		//邮件分享
		if(TextUtils.equals(shareEntity.platform, YouXiaUtils.PLATFORMEMAIL)) {
			Intent email =  new  Intent(android.content.Intent.ACTION_SEND);  
	    	email.setType( "plain/text" );
	    	//设置邮件默认标题
	    	email.putExtra(android.content.Intent.EXTRA_SUBJECT, shareEntity.title);
	    	//设置要默认发送的内容
	    	email.putExtra(android.content.Intent.EXTRA_TEXT, shareEntity.content);
	    	//调用系统的邮件系统
	    	mContext.startActivity(Intent.createChooser(email,  "请选择邮件发送软件" ));
		}
		//QQ好友
		if(TextUtils.equals(shareEntity.platform, YouXiaUtils.PLATFORMQQ)) {
			sp.setShareType(Platform.SHARE_TEXT);
			sp.setTitle(shareEntity.title);
	      	sp.setText(shareEntity.content);
	      	Platform qq = ShareSDK.getPlatform(shareEntity.platform);
	      	qq.setPlatformActionListener(this);
	      	qq.share(sp);
		}
		//QQ空间
		if(TextUtils.equals(shareEntity.platform, YouXiaUtils.PLATFORMQZONE)) {
			sp.setShareType(Platform.SHARE_TEXT);
			sp.setTitle(shareEntity.title);
			sp.setTitleUrl(shareEntity.url);
	      	sp.setText(shareEntity.content);
	      	sp.setSite(shareEntity.title);
	      	sp.setSiteUrl(shareEntity.url);
	      	sp.setImageUrl(shareEntity.imageurl);
	      	Platform qzone = ShareSDK.getPlatform(shareEntity.platform);
	      	qzone.setPlatformActionListener(this);
	      	qzone.share(sp);
		}
		//微信好友
		if(TextUtils.equals(shareEntity.platform, YouXiaUtils.PLATFORMWECHAT)) {
			sp.setShareType(Platform.SHARE_TEXT);
			sp.setTitle(shareEntity.title);
			sp.setText(shareEntity.content);
	    	Platform wechat = ShareSDK.getPlatform(shareEntity.platform);
	    	wechat.setPlatformActionListener(this);
	    	wechat.share(sp);
		}
		//微信朋友圈
		if(TextUtils.equals(shareEntity.platform, YouXiaUtils.PLATFORMWECHATMOMENTS)) {
			sp.setShareType(Platform.SHARE_TEXT);
			sp.setTitle(shareEntity.title);
			sp.setText(shareEntity.content);
	    	Platform wechatmoments = ShareSDK.getPlatform(shareEntity.platform);
	    	wechatmoments.setPlatformActionListener(this);
	    	wechatmoments.share(sp);
		}
	}
	
	public void creatShareImage(Activity activity, ShareEntity shareEntity){
		if(activity == null || shareEntity == null ) return;
		ShareParams sp = new ShareParams();
		Tencent mTencent = Tencent.createInstance("1104905849", mContext);
		IUiListener iUiListener = new IUiListener() {
			
			@Override
			public void onError(UiError arg0) {
				
			}
			
			@Override
			public void onComplete(Object arg0) {
				
			}
			
			@Override
			public void onCancel() {
				
			}
		};
		//QQ好友
		if(TextUtils.equals(shareEntity.platform, YouXiaUtils.PLATFORMQQ)) {
			Bundle params = new Bundle();
		    params.putString(QQShare.SHARE_TO_QQ_IMAGE_LOCAL_URL,shareEntity.imagepath);
		    params.putString(QQShare.SHARE_TO_QQ_APP_NAME, shareEntity.title);
		    params.putInt(QQShare.SHARE_TO_QQ_KEY_TYPE, QQShare.SHARE_TO_QQ_TYPE_IMAGE);
		    mTencent.shareToQQ(activity, params, iUiListener);
		}
		//QQ空间
		if(TextUtils.equals(shareEntity.platform, YouXiaUtils.PLATFORMQZONE)) {
			sp.setTitle(shareEntity.title);
			sp.setTitleUrl(shareEntity.url);
	      	sp.setText(shareEntity.content);
	      	sp.setSite(shareEntity.title);
	      	sp.setSiteUrl(shareEntity.url);
	      	sp.setImagePath(shareEntity.imagepath);
	      	Platform qzone = ShareSDK.getPlatform(shareEntity.platform);
	      	qzone.setPlatformActionListener(this);
	      	qzone.share(sp);
		}
		//微信好友
		if(TextUtils.equals(shareEntity.platform, YouXiaUtils.PLATFORMWECHAT)) {
			sp.setShareType(Platform.SHARE_IMAGE);
			sp.setTitle(shareEntity.title);
			sp.setImagePath(shareEntity.imagepath);
	    	Platform wechat = ShareSDK.getPlatform(shareEntity.platform);
	    	wechat.setPlatformActionListener(this);
	    	wechat.share(sp);
		}
		//微信朋友圈
		if(TextUtils.equals(shareEntity.platform, YouXiaUtils.PLATFORMWECHATMOMENTS)) {
			sp.setShareType(Platform.SHARE_IMAGE);
			sp.setTitle(shareEntity.title);
			sp.setImagePath(shareEntity.imagepath);
	    	Platform wechatmoments = ShareSDK.getPlatform(shareEntity.platform);
	    	wechatmoments.setPlatformActionListener(this);
	    	wechatmoments.share(sp);
		}
		//保存本地文件
		if(TextUtils.equals(shareEntity.platform, YouXiaUtils.PLATFORMSAVE)) {
			Bitmap bitmap = BitmapFactory.decodeFile(getImagePath());
			String foldername = "Share/";
			String imagename = System.currentTimeMillis() + ".png";
			if(YouXiaUtils.savePic(bitmap, imagename, foldername)){
//				YouXiaUtils.showAlert(activity, "保存文件到" + getFolderPath() + imagename);
//				SolarKECommon.showToast(activity, "保存文件到" + getFolderPath() + imagename, Toast.LENGTH_SHORT);
			}
			else {
//				YouXiaUtils.showAlert(activity, "保存文件失败");
//				SolarKECommon.showToast(activity, "保存文件失败", Toast.LENGTH_SHORT);
			}
		}
		
	}
	
	public void creatShareInfo(Activity activity, ShareEntity shareEntity){
		ShareParams sp = new ShareParams();
		//短信分享
		if(TextUtils.equals(shareEntity.platform, YouXiaUtils.PLATFORMSHORTMESSAGE)) {
			Uri smsToUri = Uri.parse( "smsto:" );
	    	Intent sendIntent =  new  Intent(Intent.ACTION_VIEW, smsToUri);
	    	sendIntent.putExtra( "sms_body" ,shareEntity.content);
	    	sendIntent.setType( "vnd.android-dir/mms-sms" );
	    	mContext.startActivity(sendIntent);
		}
		//邮件分享
		if(TextUtils.equals(shareEntity.platform, YouXiaUtils.PLATFORMEMAIL)) {
			Intent email =  new  Intent(android.content.Intent.ACTION_SEND);  
	    	email.setType( "plain/text" );
	    	//设置邮件默认标题
	    	email.putExtra(android.content.Intent.EXTRA_SUBJECT, shareEntity.title);
	    	//设置要默认发送的内容
	    	email.putExtra(android.content.Intent.EXTRA_TEXT, shareEntity.content);
	    	//调用系统的邮件系统
	    	mContext.startActivity(Intent.createChooser(email,  "请选择邮件发送软件" ));
		}
		//QQ好友
		if(TextUtils.equals(shareEntity.platform, YouXiaUtils.PLATFORMQQ)) {
			Intent sendIntent = new Intent();
	        sendIntent.setAction(Intent.ACTION_SEND);
	        sendIntent.putExtra(Intent.EXTRA_TEXT, shareEntity.content);
	        sendIntent.setType("text/plain");
	        try {
	            sendIntent.setClassName("com.tencent.mobileqq", "com.tencent.mobileqq.activity.JumpActivity");
	            mContext.startActivity(sendIntent);
	        } catch (Exception e) {
	           
	        }
		}
		//微信好友
		if(TextUtils.equals(shareEntity.platform, YouXiaUtils.PLATFORMWECHAT)) {
			sp.setShareType(Platform.SHARE_TEXT);
			sp.setTitle(shareEntity.title);
			sp.setText(shareEntity.content);
	    	Platform wechat = ShareSDK.getPlatform(shareEntity.platform);
	    	wechat.setPlatformActionListener(this);
	    	wechat.share(sp);
		}
	}
	
	@SuppressWarnings("unused")
	private String getFolderPath() {
		File picpath = new File(YouXiaUtils.getSDPath() + "/SolarKE_YunWei/Share/");
		return picpath.toString();
	}
	
	private String getImagePath() {
		File picpath = new File(YouXiaUtils.getSDPath() + "/SolarKE_YunWei/Share/solarke.png");
		return picpath.toString();
	}
	
	@Override
	public void onCancel(Platform arg0, int arg1) {
		
	}

	@Override
	public void onComplete(Platform arg0, int arg1, HashMap<String, Object> arg2) {
		
	}

	@Override
	public void onError(Platform arg0, int arg1, Throwable arg2) {
		
	}

	@Override
	public void onCancel() {
		
	}

	@Override
	public void onComplete(Object arg0) {
		
	}

	@Override
	public void onError(UiError arg0) {
		
	}
}
