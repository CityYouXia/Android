package com.youxia.activity;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import com.youxia.BaseActivity;
import com.youxia.R;
import com.youxia.entity.PhotoEntity;
import com.youxia.photopicker.adapter.ImageGridAdapter;
import com.youxia.photopicker.adapter.ImageGridAdapter.TextCallback;
import com.youxia.photopicker.utils.AlbumHelper;
import com.youxia.photopicker.utils.Bimp;
import com.youxia.utils.YouXiaActivityManager;
import com.youxia.utils.YouXiaUtils;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;
import net.tsz.afinal.annotation.view.ViewInject;

public class PhotoActivity extends BaseActivity {
	
	public static final String EXTRA_IMAGE_LIST = "imagelist";
	//title
	@ViewInject(id=R.id.title_bar_title) 							TextView			mTitleBarTitle;
	@ViewInject(id=R.id.title_bar_textbtn_tv) 						TextView			mTitleBarCancelText;
	@ViewInject(id=R.id.title_bar_back) 							RelativeLayout		mTitleBarBack;
	@ViewInject(id=R.id.title_bar_textbtn,click="btnClick") 		RelativeLayout		mTitleBarCancel;
	
	@ViewInject(id=R.id.activity_photo_gv) 							GridView			mPhotoGV;
	@ViewInject(id=R.id.activity_photo_done_tv,click="photoDone") 	TextView			mPhotoPickerDone;
	
	private List<PhotoEntity> dataList;
	private ImageGridAdapter adapter;
	AlbumHelper helper;
	
	@SuppressLint("HandlerLeak")
	Handler mHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case 0:
				String sb = "最多选择" + YouXiaUtils.PROBLEM_REPORT_PHOTO_MAXNUMBER + "张图片";
				YouXiaUtils.showToast(PhotoActivity.this, sb, Toast.LENGTH_SHORT);
				break;
			}
		}
	};
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_photo);
		
		helper = AlbumHelper.getHelper();
		helper.init(getApplicationContext());
		
		initView();
	}
	
	@SuppressWarnings("unchecked")
	private void initView() {
		mTitleBarBack.setVisibility(View.GONE);
		mTitleBarCancel.setVisibility(View.VISIBLE);
		mTitleBarCancelText.setText(getString(R.string.cancel));
		mTitleBarTitle.setText(getString(R.string.album));
		
		dataList = (List<PhotoEntity>) getIntent().getSerializableExtra(EXTRA_IMAGE_LIST);
		
		mPhotoGV.setSelector(new ColorDrawable(Color.TRANSPARENT));
		adapter = new ImageGridAdapter(PhotoActivity.this, dataList,
				mHandler);
		mPhotoGV.setAdapter(adapter);
		adapter.setTextCallback(new TextCallback() {
			public void onListen(int count) {
				mPhotoPickerDone.setText("完成" + "(" + count + ")");
			}
		});

		mPhotoGV.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				adapter.notifyDataSetChanged();
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
	
	public void photoDone(View v){
		ArrayList<String> list = new ArrayList<String>();
		Collection<String> c = adapter.map.values();
		Iterator<String> it = c.iterator();
		for (; it.hasNext();) {
			list.add(it.next());
		}

		if (Bimp.act_bool) {
			Bimp.act_bool = false;
		}
		for (int i = 0; i < list.size(); i++) {
			if (Bimp.drr.size() < YouXiaUtils.PROBLEM_REPORT_PHOTO_MAXNUMBER) {
				Bimp.drr.add(list.get(i));
			}
		}
		
		YouXiaActivityManager.getAppManager().finishActivity(AlbumActivity.class);
		YouXiaActivityManager.getAppManager().finishActivity(this);
	}
}