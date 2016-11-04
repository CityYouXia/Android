package com.youxia.fragment;

import net.tsz.afinal.FinalActivity;
import net.tsz.afinal.annotation.view.ViewInject;

import com.youxia.BaseFragment;
import com.youxia.R;
import com.youxia.activity.RoadRescueActivity;
import com.youxia.widget.TouchFingerImageView;
import com.youxia.widget.ViewPagerAdvert;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.ImageView;
import android.widget.LinearLayout;

@SuppressLint("InflateParams")
public class FragmentHome extends BaseFragment implements OnClickListener {
	
	public static final String TAG = FragmentHome.class.getSimpleName();
	
	@ViewInject(id=R.id.fragment_homepage_advert) 			ViewPagerAdvert	viewPagerAdvert;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View root = inflater.inflate(R.layout.fragment_home, container, false);
		FinalActivity.initInjectedView(this,root);
		return root;
	}
	
	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);
		initView();
	}
	
	@SuppressWarnings("deprecation")
	private void initView() {
		viewPagerAdvert.addALocalImageToViewPager("ad1.jpg", R.drawable.ad1, "广告1");
		viewPagerAdvert.addALocalImageToViewPager("ad2.jpg", R.drawable.ad2, "广告2");
		viewPagerAdvert.addALocalImageToViewPager("ad3.jpg", R.drawable.ad3, "广告3");
		viewPagerAdvert.notifyViewPager();
		

		//计算单位图片宽度
		int imageWidth = (getActivity().getWindowManager().getDefaultDisplay().getWidth() - 8) / 4;
		int imageHeight= (int)(imageWidth * 0.78);
		
		//获取列布局
		LinearLayout firstLinearLayout  = (LinearLayout)getActivity().findViewById(R.id.home_first_layout);	
		LinearLayout secondLinearLayout = (LinearLayout)getActivity().findViewById(R.id.home_second_layout);
		
		//添加第一列元素
		firstLinearLayout.addView(getImageView(imageWidth * 2,  imageHeight* 2, R.drawable.home_road_rescue));
		firstLinearLayout.addView(getImageView(imageWidth * 2, imageHeight,R.drawable.home_movecar));
		firstLinearLayout.addView(getImageView(imageWidth * 2, imageHeight,R.drawable.home_traffic));
		firstLinearLayout.addView(getImageView(imageWidth * 2,  imageHeight* 2, R.drawable.home_road_rescue));
		
		//添加第二列元素
		LinearLayout linearLayout = getHorizontalLayout();
		
		linearLayout.addView(getImageView(imageWidth, imageHeight, R.drawable.home_missing_person));
		linearLayout.addView(getImageView(imageWidth, imageHeight,R.drawable.home_missing_thing));

		secondLinearLayout.addView(linearLayout);

		secondLinearLayout.addView(getImageView(imageWidth * 2, imageHeight * 2,R.drawable.home_express));
		secondLinearLayout.addView(getImageView(imageWidth * 2, imageHeight,R.drawable.home_asking));
	}
	
	public LinearLayout getHorizontalLayout()
	{
		LinearLayout layout = new LinearLayout(getActivity());  
        LinearLayout.LayoutParams itemParam = new LinearLayout.LayoutParams(  
        		LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);  
        layout.setOrientation(LinearLayout.HORIZONTAL);  
        layout.setLayoutParams(itemParam);  

        return layout;
	}
	
	public ImageView getImageView(int layout_width, int layout_height, int resId) 
	{
		TouchFingerImageView imageView = new TouchFingerImageView(getActivity());
		
		imageView.setImageResource(resId);
		
		LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(layout_width, layout_height); 
		layoutParams.setMargins(8, 8, 8, 8);
		imageView.setLayoutParams(layoutParams);
		
		imageView.setTag(resId);
		
		imageView.setOnClickListener(this);
		
		return imageView;
	}
	
	@Override
	public void onClick(View v) {
		
		switch (Integer.parseInt(v.getTag().toString())) {
		case R.drawable.home_road_rescue:
			Intent intent = new Intent();
			
			intent.setClass(getActivity(), RoadRescueActivity.class);
			
			startActivity(intent);
			break;
		case R.drawable.home_missing_person:
			break;
		case R.drawable.home_missing_thing:
			break;
		case R.drawable.home_traffic:
			break;
		case R.drawable.home_express:
			break;
		case R.drawable.home_movecar:
			break;
		case R.drawable.home_asking:
			break;
		}
	}
	
	public void btnClick(View v){
	
	}


}
