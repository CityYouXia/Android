package com.youxia.adapter;

import java.util.ArrayList;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;
import com.youxia.R;
import com.youxia.entity.HelpImageListEntity;
import com.youxia.http.HttpClientHelper;
import com.youxia.widget.imageshow.ImageShowViewPager;
import com.youxia.widget.imageshow.Options;
import com.youxia.widget.imageshow.TouchImageView;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;
/**
 * 图片浏览的PagerAdapter
 */
public class ImagePagerAdapter extends PagerAdapter {
	Context context;
	ArrayList<HelpImageListEntity> imgsUrl;
	LayoutInflater inflater = null;
	//view内控件
	protected ImageLoader imageLoader = ImageLoader.getInstance();
	TouchImageView full_image;
	ProgressBar progress;
	TextView retry;
	private enum loadState{started,failed,complete,cancelled};
	DisplayImageOptions options;
	
	public ImagePagerAdapter(Context context, ArrayList<HelpImageListEntity> imgsUrl) {
		this.context = context;
		this.imgsUrl = imgsUrl;
		imageLoader.init(ImageLoaderConfiguration.createDefault(context));
		inflater = LayoutInflater.from(context);
		options = Options.getListOptions();
	}
	
	/** 动态加载数据 */
	@Override
	public void setPrimaryItem(ViewGroup container, int position, Object object) {
		super.setPrimaryItem(container, position, object);
		full_image = (TouchImageView) ((View)object).findViewById(R.id.full_image);
		final int positionFinal = position;
		if(((ImageShowViewPager) container).mCurrentView != full_image) {
			progress= (ProgressBar)((View)object).findViewById(R.id.progress);
			retry= (TextView)((View)object).findViewById(R.id.retry);
			showLoadState(loadState.complete);
			retry.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					loadImageByURL(HttpClientHelper.Basic_YouXiaUrl + imgsUrl.get(positionFinal).imageUrl);
				}
			});
			loadImageByURL(HttpClientHelper.Basic_YouXiaUrl + imgsUrl.get(position).imageUrl);
			((ImageShowViewPager) container).mCurrentView = full_image;
		}
	}
	
	@Override
	public int getCount() {
		return imgsUrl == null ? 0 : imgsUrl.size();
	}

	@Override
	public boolean isViewFromObject(View arg0, Object arg1) {
		return arg0 == arg1;
	}

	@Override
	public int getItemPosition(Object object) {
		return super.getItemPosition(object);
	}

	@SuppressLint("InflateParams")
	@SuppressWarnings("static-access")
	@Override
	public Object instantiateItem(ViewGroup container, int position) {
		View view = inflater.from(context).inflate(R.layout.viewpager_item_imageshow, null);
		((ViewPager) container).addView(view);
		return view;
	}

	@Override
	public void destroyItem(ViewGroup container, int position, Object object) {
			((ViewPager) container).removeView((View) object);  
	}
	
	private void loadImageByURL(String url){
		imageLoader.displayImage(url, full_image, options, new ImageLoadingListener() {
			
			@Override
			public void onLoadingStarted(String imageUri, View view) {
				showLoadState(loadState.started);
			}
			
			@Override
			public void onLoadingFailed(String imageUri, View view, FailReason failReason) {
				showLoadState(loadState.failed);
			}
			
			@Override
			public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
				showLoadState(loadState.complete);
			}
			
			@Override
			public void onLoadingCancelled(String imageUri, View view) {
				showLoadState(loadState.cancelled);
			}
		});
		
		/*			Bitmap bitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.defaultimage);
		YouXiaApp.mFinalBitmap.configLoadfailImage(R.drawable.defaultimage);
		YouXiaApp.mFinalBitmap.display(full_image, imgsUrl.get(position).imageUrl, bitmap);*/
		//full_image = (TouchImageView)view.findViewById(R.id.full_image);
		//YouXiaApp.mFinalBitmap.configLoadingImage(R.drawable.default_image);
		//YouXiaApp.mFinalBitmap.configLoadfailImage(R.drawable.loading_failed);
		//Bitmap bitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.defaultimage);
		//YouXiaApp.mFinalBitmap.display(full_image, imgsUrl.get(position).imageUrl, bitmap);
		//YouXiaApp.mFinalBitmap.display(full_image, HttpClientHelper.Basic_YouXiaUrl + imgsUrl.get(position).imageUrl);
//		Bitmap loadfailBitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.loading_failed);
//		Bitmap loadingBitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.defaultimage);
//		YouXiaApp.mFinalBitmap.display(full_image, HttpClientHelper.Basic_YouXiaUrl + imgsUrl.get(position).imageUrl, loadingBitmap, loadfailBitmap);
		//imageLoader.displayImage(imgsUrl.get(position).imageUrl, full_image);
	}
	
	private void showLoadState(loadState state){
		switch (state) {
		case started:
			progress.setVisibility(View.VISIBLE);
			full_image.setVisibility(View.GONE);
			retry.setVisibility(View.GONE);
			break;
		case failed:
			progress.setVisibility(View.GONE);
			full_image.setVisibility(View.GONE);
			retry.setVisibility(View.VISIBLE);
			break;
		case complete:
			progress.setVisibility(View.GONE);
			full_image.setVisibility(View.VISIBLE);
			retry.setVisibility(View.GONE);
			break;
		case cancelled:
			progress.setVisibility(View.GONE);
			full_image.setVisibility(View.GONE);
			retry.setVisibility(View.VISIBLE);
			break;
		default:
			break;
		}
	}
}
