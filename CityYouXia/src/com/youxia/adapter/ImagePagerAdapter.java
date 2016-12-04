package com.youxia.adapter;

import java.util.ArrayList;

import com.youxia.R;
import com.youxia.entity.HelpImageListEntity;
import com.youxia.http.HttpClientHelper;
import com.youxia.utils.YouXiaApp;
import com.youxia.widget.imageshow.ImageShowViewPager;
import com.youxia.widget.imageshow.TouchImageView;

import android.annotation.SuppressLint;
import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
/**
 * 图片浏览的PagerAdapter
 */
public class ImagePagerAdapter extends PagerAdapter {
	Context context;
	ArrayList<HelpImageListEntity> imgsUrl;
	LayoutInflater inflater = null;
	//view内控件
	TouchImageView full_image;
	
	public ImagePagerAdapter(Context context, ArrayList<HelpImageListEntity> imgsUrl) {
		this.context = context;
		this.imgsUrl = imgsUrl;
		inflater = LayoutInflater.from(context);
	}
	
	/** 动态加载数据 */
	@Override
	public void setPrimaryItem(ViewGroup container, int position, Object object) {
		super.setPrimaryItem(container, position, object);
		((ImageShowViewPager) container).mCurrentView = ((TouchImageView) ((View)object).findViewById(R.id.full_image));
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
		full_image = (TouchImageView)view.findViewById(R.id.full_image);
		YouXiaApp.mFinalBitmap.display(full_image, HttpClientHelper.Basic_YouXiaUrl + imgsUrl.get(position).imageUrl);
		((ViewPager) container).addView(view);
		return view;
	}

	@Override
	public void destroyItem(ViewGroup container, int position, Object object) {
			((ViewPager) container).removeView((View) object);  
	}
}
