/****************************************************************************
 * 广告图片轮播类
 * ViewPagerAdvert.java
 * @author nightrain
 * @data 2015-10-07
****************************************************************************/
package com.youxia.widget;

import java.util.ArrayList;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import com.youxia.R;
import com.youxia.utils.PixelUtil;
import com.youxia.utils.YouXiaApp;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class ViewPagerAdvert extends FrameLayout {
	public 	ArrayList<String> 			advertImageUrls   	= null;	//图片资源URL列表
	public 	ArrayList<String> 			advettImagetitles 	= null;	//图片标题
	public 	ArrayList<ImageView> 		advertImageSource 	= null;	//图片控件资源
	public 	ArrayList<View>				advertDots 		  	= null;	//指示点
	
	public 	CustomViewPager 			advertViewPager		= null;	//viewpager显示图片
	public 	AdvertPagerAdapter  		advertAdapter		= null;	//viewPager的适配器
	
	public 	int 						curPage 		  	= 0;	//当前显示的页
	public 	int 						oldPage 		  	= 0;	//上一次显示的页
	
	public 	LinearLayout				dotLayout	  		= null;	//图片指示点布局
	public 	TextView 					advertTitleTV 		= null;	//图片文字
	
	public  ScheduledExecutorService   	turnScheduled 		= null;	//轮播定时器
	
	private Context						mContext			= null;
	
	//以下是对外接口属性
	private boolean						showTitle			= true;	//默认显示标题标志
	private int 						turnDelayTime		= 5;	//开始轮播延迟时间，单位秒
	private int 						turnIntevalTime		= 10;	//轮播间隔时间，单位秒
	
	public ViewPagerAdvert(Context context) 
	{
		super(context);
	}
	
	public ViewPagerAdvert(Context context, AttributeSet attrs) 
	{
		super(context, attrs);
		
		mContext = context;
		
		// 导入布局
        LayoutInflater.from(context).inflate(R.layout.viewpager_advert, this, true);
        
        initAdvertViewPager(context, attrs);
        
        resetAdvertViewPagerImages();
	}

	public ViewPagerAdvert(Context context, AttributeSet attrs, int defStyle) 
	{
		super(context, attrs, defStyle);
	}


	private void initAdvertViewPager(Context context, AttributeSet attrs) 
	{
		if (context != null) {
			TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.ViewPagerAdvert); 
			showTitle = typedArray.getBoolean(R.styleable.ViewPagerAdvert_showTitle, true); 
			turnDelayTime = typedArray.getResourceId(R.styleable.ViewPagerAdvert_turnDelayTime, 5);
			turnDelayTime = typedArray.getResourceId(R.styleable.ViewPagerAdvert_turnDelayTime, 5);
			
			typedArray.recycle();
		}
		
		//图片的标题
		advertTitleTV = (TextView)findViewById(R.id.viewpager_advert_title_tv);
		advertTitleTV.setText("");
		if (!showTitle) advertTitleTV.setVisibility(View.GONE);
		else advertTitleTV.setVisibility(View.VISIBLE);
		
		dotLayout = (LinearLayout)findViewById(R.id.viewpager_advert_dot_ll);
		
		//显示图片的VIew
		advertViewPager = (CustomViewPager)findViewById(R.id.viewpager_advert_vp);
		
		//为viewPager设置适配器
		advertAdapter = new AdvertPagerAdapter();
		advertViewPager.setAdapter(advertAdapter);
		
		//为viewPager添加监听器，该监听器用于当图片变换时，标题和点也跟着变化
		MyPageChangeListener listener = new MyPageChangeListener();
		advertViewPager.setOnPageChangeListener(listener);
				
		//开启定时器，每隔6秒自动播放下一张（通过调用线程实现）（与Timer类似，可使用Timer代替）
		turnScheduled =  Executors.newSingleThreadScheduledExecutor();
		
		//设置一个线程，该线程用于通知UI线程变换图片
		ViewPagerTask pagerTask = new ViewPagerTask();
		turnScheduled.scheduleAtFixedRate(pagerTask, turnDelayTime, turnIntevalTime, TimeUnit.SECONDS);
	}
	
	//ViewPager每次仅最多加载三张图片（有利于防止发送内存溢出）
	private class AdvertPagerAdapter extends PagerAdapter
	{
			  
		@Override
		public int getCount() {
			System.out.println("getCount");
			
			int count = 0;
			if (advertImageSource != null) count = advertImageSource.size();  
			return count;
		}
		
		@Override
		public boolean isViewFromObject(View arg0, Object arg1) {
			//判断将要显示的图片是否和现在显示的图片是同一个
			//arg0为当前显示的图片，arg1是将要显示的图片
			System.out.println("isViewFromObject");
			return arg0 == arg1;
		}
		
		@Override
		public void destroyItem(ViewGroup container, int position, Object object) {
			System.out.println("destroyItem==" + position);
			
			//销毁该图片
			if (advertImageSource != null && advertImageSource.size() > 0) {
				container.removeView(advertImageSource.get(position));
			}
		}
		
		@Override
		public Object instantiateItem(ViewGroup container, int position) {
			//初始化将要显示的图片，将该图片加入到container中，即viewPager中
			if (advertImageSource != null && advertImageSource.size() > 0) { 
				container.addView(advertImageSource.get(position));
			}
			System.out.println("instantiateItem===" + position +"===="+container.getChildCount());
			
			if (advertImageSource == null) return null;
			return advertImageSource.get(position);
		}
	}
			  
	//监听ViewPager的变化
	private class MyPageChangeListener implements OnPageChangeListener {
		@Override
		public void onPageScrollStateChanged(int arg0) {
			
		}

		@Override
		public void onPageScrolled(int arg0, float arg1, int arg2) {
			
		}

		@Override
		public void onPageSelected(int position) {
			//当显示的图片发生变化之后			
			//设置标题
			if (advettImagetitles != null && advettImagetitles.size() > 0) 
				advertTitleTV.setText(advettImagetitles.get(position));
	
			
			//改变点的状态
			if (advertDots != null && advertDots.size() > 0) {
				oldPage = curPage;
				advertDots.get(position).setBackgroundResource(R.drawable.dot_focused);
				advertDots.get(oldPage).setBackgroundResource(R.drawable.dot_normal);
				
				//记录的页面
				curPage = position;
			}
		}
	}
	
	private class ViewPagerTask implements Runnable{
		@Override
		public void run() {
			if (advertImageSource != null && advertImageSource.size() > 1) {
				//发送消息给UI线程
				adverthandler.sendEmptyMessage(1);
			}
		}
	}
	
	@SuppressLint("HandlerLeak")
	private Handler adverthandler= new Handler(){
		public void handleMessage(Message msg) {
			
			switch (msg.what) {
			case 1:
				
				if (advertImageSource != null && advertImageSource.size() > 1) {
					//接收到消息后，更新页面
					int page = (curPage+ 1) %  advertImageSource.size(); 
					advertViewPager.setCurrentItem(page);					
				}
				break;
			default:
				break;
			}
		};
	};
	
	/**
	* 重置viewpager所有图片
	* @return
	*/
	public void resetAdvertViewPagerImages()
	{
		curPage = 0;
		oldPage = 0;
		if (advertImageUrls != null) advertImageUrls.clear();
		advertImageUrls = null;
		advertImageUrls = new ArrayList<String>();
		
		if (advettImagetitles != null) advettImagetitles.clear();
		advettImagetitles = null;
		advettImagetitles = new ArrayList<String>();
		
		if (advertImageSource != null) advertImageSource.clear();
		advertImageSource = null;
		advertImageSource = new ArrayList<ImageView>();

		if (advertDots != null) advertDots.clear();
		advertDots = null;
		advertDots = new ArrayList<View>();
		
		if (advertViewPager != null) advertViewPager.removeAllViews();
		
		advertAdapter.notifyDataSetChanged();
	}
	
	/**
	* 给viewpager动态添加一个本地图片,常用于本地测试
	* @param imgurl，	如: "a.png"
	* @param imgid，		如: R.drawable.a
	* @param imgtitle,	如: "美女"
	* @return
	*/
	public void addALocalImageToViewPager(String imgurl, int imgid, String imgtitle)
	{
		if (advertImageUrls 	== null ||
			advettImagetitles 	== null ||
			advertImageSource	== null ||
			dotLayout			== null ||
			advertDots			== null ||
			advertAdapter		== null) return;
		
		advertImageUrls.add(imgurl);
		advettImagetitles.add(imgtitle);
		ImageView image = new ImageView(mContext);
		image.setTag(imgurl);
		image.setImageDrawable(mContext.getResources().getDrawable(imgid));
		image.setScaleType(ImageView.ScaleType.FIT_XY);
		advertImageSource.add(image);
		
		
		View dotview = new View(mContext);
		if (advertDots.size() <= 0) dotview.setBackgroundResource(R.drawable.dot_focused);
		else dotview.setBackgroundResource(R.drawable.dot_normal);
		
		LinearLayout.LayoutParams layoutparam = new LinearLayout.LayoutParams(PixelUtil.dpToPx(mContext, 5), PixelUtil.dpToPx(mContext, 5));
			
		layoutparam.leftMargin  = PixelUtil.dpToPx(mContext, 3);
		layoutparam.rightMargin = PixelUtil.dpToPx(mContext, 3);
		layoutparam.topMargin	= PixelUtil.dpToPx(mContext, 8);
			
		dotview.setLayoutParams(layoutparam);
		dotLayout.addView(dotview);
		advertDots.add(dotview);
	}
	
	public void notifyViewPager(){
		advertTitleTV.setText(advettImagetitles.get(0));
		advertAdapter.notifyDataSetChanged();
	}
	
	/**
	* 给viewpager动态添加一个网络图片,常用于取后台广告
	* @param imgurl，	如: "http://yw.solarke.com/WebPages/advert/a.png"
	* @param imgtitle,	如: "光伏大促销"
	* @return
	*/
	public void addAURLImageToViewPager(String imgurl, String imgtitle)
	{
		if (advertImageUrls 	== null ||
			advettImagetitles 	== null ||
			advertImageSource	== null ||
			dotLayout			== null ||
			advertDots			== null ||
			advertAdapter		== null) return;
		
		advertImageUrls.add(imgurl);
		advettImagetitles.add(imgtitle);
		ImageView image = new ImageView(mContext);
		image.setTag(imgurl);
		image.setImageDrawable(mContext.getResources().getDrawable(R.drawable.nopicture));
		image.setScaleType(ImageView.ScaleType.FIT_XY);
		advertImageSource.add(image);
		
		YouXiaApp.mFinalBitmap.display(image, imgurl, BitmapFactory.decodeResource(mContext.getResources(), R.drawable.nopicture));
		
		View dotview = new View(mContext);
		if (advertDots.size() <= 0) dotview.setBackgroundResource(R.drawable.dot_focused);
		else dotview.setBackgroundResource(R.drawable.dot_normal);
		
		LinearLayout.LayoutParams layoutparam = new LinearLayout.LayoutParams(PixelUtil.dpToPx(mContext, 5), PixelUtil.dpToPx(mContext, 5));
			
		layoutparam.leftMargin  = PixelUtil.dpToPx(mContext, 3);
		layoutparam.rightMargin = PixelUtil.dpToPx(mContext, 3);
		layoutparam.topMargin	= PixelUtil.dpToPx(mContext, 8);
			
		dotview.setLayoutParams(layoutparam);
		dotLayout.addView(dotview);
		advertDots.add(dotview);

		advertAdapter.notifyDataSetChanged();
	}
}
