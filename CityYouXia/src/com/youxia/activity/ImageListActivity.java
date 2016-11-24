package com.youxia.activity;

import java.util.ArrayList;

import com.youxia.BaseActivity;
import com.youxia.R;
import com.youxia.adapter.ImagePagerAdapter;
import com.youxia.entity.RoadRescueDetailHelpImageListEntity;
import com.youxia.widget.imageshow.ImageShowViewPager;

import android.os.Bundle;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.widget.TextView;
import net.tsz.afinal.annotation.view.ViewInject;

public class ImageListActivity extends BaseActivity{
	@ViewInject(id = R.id.image_pager)
	ImageShowViewPager mImageShowViewPager;
	@ViewInject(id = R.id.page_number)
	TextView mPageNumber;
	
	private ArrayList<RoadRescueDetailHelpImageListEntity> mImageList;
	private ImagePagerAdapter mImagePagerAdapter;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_imagelist);
		initView();
	}
	
	@SuppressWarnings("unchecked")
	private void initView() {
		mImageList = new ArrayList<RoadRescueDetailHelpImageListEntity>();
		Bundle bundle = this.getIntent().getBundleExtra("bundle"); 
		mImageList = (ArrayList<RoadRescueDetailHelpImageListEntity>) bundle.getSerializable("imageList");
		int pageNo = bundle.getInt("position");
		mImageShowViewPager.setOnPageChangeListener(new PageChangeListener());
		
		if (mImageList != null && mImageList.size() > 0) {
			mImagePagerAdapter = new ImagePagerAdapter(this, mImageList);
			mImageShowViewPager.setAdapter(mImagePagerAdapter);
		}
		mImageShowViewPager.setCurrentItem(pageNo);
	}
	
	private class PageChangeListener implements OnPageChangeListener{

		@Override
		public void onPageScrollStateChanged(int arg0) {
			
		}

		@Override
		public void onPageScrolled(int arg0, float arg1, int arg2) {
			
		}

		@Override
		public void onPageSelected(int position) {
			mPageNumber.setText((position+1) + "/" + mImageList.size());
		}
	}
}
