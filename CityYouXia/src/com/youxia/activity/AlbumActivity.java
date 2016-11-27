package com.youxia.activity;

import java.io.Serializable;
import java.util.List;

import com.youxia.BaseActivity;
import com.youxia.R;
import com.youxia.entity.AlbumEntity;
import com.youxia.photopicker.adapter.ImageBucketAdapter;
import com.youxia.photopicker.utils.AlbumHelper;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;
import net.tsz.afinal.annotation.view.ViewInject;

public class AlbumActivity extends BaseActivity {
	
	//title
	@ViewInject(id=R.id.title_bar_title) 							TextView			mTitleBarTitle;
	@ViewInject(id=R.id.title_bar_textbtn_tv) 						TextView			mTitleBarCancelText;
	@ViewInject(id=R.id.title_bar_back) 							RelativeLayout		mTitleBarBack;
	@ViewInject(id=R.id.title_bar_textbtn,click="btnClick") 		RelativeLayout		mTitleBarCancel;
	
	@ViewInject(id=R.id.activity_album_gv) 							GridView			mAlbumGV;
	
	private List<AlbumEntity> dataList;
	public static Bitmap bimap;
	ImageBucketAdapter adapter;// 自定义的适配器
	AlbumHelper helper;
	
	public static final String EXTRA_IMAGE_LIST = "imagelist";
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_album);
		
		helper = AlbumHelper.getHelper();
		helper.init(getApplicationContext());
		
		initData();
		initView();
	}
	
	/**
	 * 初始化数据
	 */
	private void initData() {
		dataList = helper.getImagesBucketList(false);	
		bimap=BitmapFactory.decodeResource(
				getResources(),
				R.drawable.icon_addpic_unfocused);
	}
	
	private void initView() {
		mTitleBarBack.setVisibility(View.GONE);
		mTitleBarCancel.setVisibility(View.VISIBLE);
		mTitleBarCancelText.setText(getString(R.string.cancel));
		mTitleBarTitle.setText(getString(R.string.album));
		
		adapter = new ImageBucketAdapter(AlbumActivity.this, dataList);
		mAlbumGV.setAdapter(adapter);
		mAlbumGV.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				Intent intent = new Intent(AlbumActivity.this, PhotoActivity.class);
				intent.putExtra(AlbumActivity.EXTRA_IMAGE_LIST,(Serializable) dataList.get(position).photoList);
				startActivity(intent);
			}
		});
	}
	
	public void btnClick(View v){
		switch (v.getId()) {
		case R.id.title_bar_textbtn:
			finish();
			break;
		}
	}
}