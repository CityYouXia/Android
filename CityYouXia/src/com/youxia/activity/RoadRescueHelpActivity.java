package com.youxia.activity;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.youxia.BaseActivity;
import com.youxia.R;
import com.youxia.http.HttpClientHelper;
import com.youxia.photopicker.utils.Bimp;
import com.youxia.photopicker.utils.FileUtils;
import com.youxia.popup.PopupLocation;
import com.youxia.popup.PopupPhotoPicker;
import com.youxia.popup.PopupProblemAbandoned;
import com.youxia.popup.PopupReward;
import com.youxia.utils.YouXiaActivityManager;
import com.youxia.utils.YouXiaApp;
import com.youxia.utils.YouXiaUtils;
import com.youxia.widget.NoScrollGridView;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.PopupWindow.OnDismissListener;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import net.tsz.afinal.annotation.view.ViewInject;
import net.tsz.afinal.http.AjaxCallBack;

public class RoadRescueHelpActivity extends BaseActivity {
	
	//title
	@ViewInject(id=R.id.title_bar_title) 								TextView			mTitleBarTitle;
	@ViewInject(id=R.id.title_bar_textbtn_tv) 							TextView			mTitleBarDoneText;
	@ViewInject(id=R.id.title_bar_textbtn,click="commit") 				RelativeLayout		mTitleBarDone;
	@ViewInject(id=R.id.title_bar_back,click="abord") 					RelativeLayout		mTitleBarBack;
	
	@ViewInject(id=R.id.road_rescue_help_title_et) 						EditText			mHelpTitle;
	@ViewInject(id=R.id.road_rescue_help_content_et) 					EditText			mHelpContent;
	@ViewInject(id=R.id.road_rescue_location_tv,click="setLocation") 	TextView			mHelpLocation;
	@ViewInject(id=R.id.road_rescue_reward_points_tv,click="setReward")	TextView			mHelpReward;
	@ViewInject(id=R.id.road_rescue_help_gv) 							NoScrollGridView	mPhotoGV;
	
	private GridAdapter mAdapter;
	private int			mReward		= 0;
	private String		mLocation	= "";
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_roadrescue_help);
		mLocation = YouXiaApp.getmLocation();
		initView();
	}
	
	@Override
	protected void onDestroy() {
		super.onDestroy();
		Bimp.clear();
	}
	
	private void initView() {
		mTitleBarTitle.setText(getString(R.string.activity_help));
		mTitleBarDone.setVisibility(View.VISIBLE);
		mTitleBarDoneText.setText(getString(R.string.commit));
		mHelpLocation.setText(mLocation);
		mHelpReward.setText(mReward + "分");
		mPhotoGV.setSelector(new ColorDrawable(Color.TRANSPARENT));
		mAdapter = new GridAdapter(this);
		mAdapter.update();
		mPhotoGV.setAdapter(mAdapter);		
	}
	
	public void abord(View v){
		if(Bimp.drr.size() == 0 && mHelpTitle.getText().toString().equals("") && mHelpContent.getText().toString().equals("")){
			YouXiaActivityManager.getAppManager().finishActivity(this);
		}else{
			PopupProblemAbandoned mPopup = new PopupProblemAbandoned(this);
			mPopup.showAtLocation(getWindow().getDecorView(), Gravity.CENTER, 0, 0);
		}
	}
	
	public void commit(View v) {
		String title = mHelpTitle.getText().toString();
		String content = mHelpContent.getText().toString();
		if(title.equals("")){
			return;
		}
		if(content.equals("")){
			return;
		}
		
		List<File> list = new ArrayList<File>();	
		for (int i = 0; i < Bimp.bmp.size(); i++) {
			String foldername = "help/";
        	if(YouXiaUtils.savePic(YouXiaUtils.compressImageByQuality(Bimp.bmp.get(i)), "help_temp_" + i + ".png", foldername)){
        		File file = new File(YouXiaUtils.getSDPath() + "/youxia/help/" + "help_temp_" + i + ".png");
        		list.add(file);
			}
		}
		
		AjaxCallBack<String> callBack = new AjaxCallBack<String>() {
			@Override
			public void onSuccess(String result) {
				super.onSuccess(result);
				//网络请求成功
				if (result != null && !TextUtils.isEmpty(result) && !result.equals("null")){
					if(TextUtils.equals(result, "0")){
						Toast.makeText(RoadRescueHelpActivity.this, "提交失败", Toast.LENGTH_SHORT).show();
					}else {
						finish();
					}
				}
			}
			
			@Override
			public void onFailure(Throwable t, int errorNo, String strMsg) {
				super.onFailure(t, errorNo, strMsg);
				YouXiaUtils.showToast(getApplication(), getString(R.string.upload_fail), 0);
			}
		};
		HttpClientHelper.help(list, mHelpTitle.getText().toString(), 
				mHelpContent.getText().toString(), mHelpLocation.getText().toString(), 
				Integer.toString(mReward), callBack);	
	}
	
	private void photoChoice() {
		final PopupPhotoPicker mPopupPhotoPicker = new PopupPhotoPicker(this);
		mPopupPhotoPicker.showAtLocation(getWindow().getDecorView(), Gravity.BOTTOM, 0, 0);
	}
	
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		switch (requestCode) {
		case YouXiaUtils.PROBLEM_REPORT_CAMERA_RESULTCODE:
			if (Bimp.drr.size() < YouXiaUtils.PROBLEM_REPORT_PHOTO_MAXNUMBER && resultCode == -1) {
				Bundle bundle = data.getExtras();
				Bitmap bitmap = (Bitmap) bundle.get("data");
				String foldername = "help/";
	        	if(YouXiaUtils.savePic(bitmap, YouXiaUtils.PROBLEM_REPORT_PHOTO_NAME, foldername)){
	        		File file = new File(YouXiaUtils.getSDPath() + "/youxia/help/" + YouXiaUtils.PROBLEM_REPORT_PHOTO_NAME);
	        		Bimp.drr.add(file.getPath());
				}
			}
			break;
		}
	}
	
	@SuppressLint("HandlerLeak")
	public class GridAdapter extends BaseAdapter {
		private LayoutInflater inflater; // 视图容器
		private int selectedPosition = -1;// 选中的位置

		public GridAdapter(Context context) {
			inflater = LayoutInflater.from(context);
		}

		public void update() {
			loading();
		}

		public int getCount() {
			return (Bimp.bmp.size() + 1);
		}

		public Object getItem(int arg0) {
			return null;
		}

		public long getItemId(int arg0) {
			return 0;
		}

		public void setSelectedPosition(int position) {
			selectedPosition = position;
		}

		public int getSelectedPosition() {
			return selectedPosition;
		}
		
		public View getView(int position, View convertView, ViewGroup parent) {
			final int coord = position;
			ViewHolder holder = null;
			if (convertView == null) {
				convertView = inflater.inflate(R.layout.gridview_item_problem_photo, parent, false);
				holder = new ViewHolder();
				holder.image = (ImageView) convertView.findViewById(R.id.item_grida_image);
				convertView.setTag(holder);
			} else {
				holder = (ViewHolder) convertView.getTag();
			}

			if (position == Bimp.bmp.size()) {
				holder.image.setImageBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.icon_addpic_unfocused));
				if (position == YouXiaUtils.PROBLEM_REPORT_PHOTO_MAXNUMBER) {
					holder.image.setVisibility(View.GONE);
				}
			} else {
				holder.image.setImageBitmap(Bimp.bmp.get(position));
			}
			
			convertView.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					if (coord == Bimp.bmp.size()) {
						photoChoice();
					} 
					else {
						Intent intent = new Intent(RoadRescueHelpActivity.this, PhotoDetailsActivity.class);
						intent.putExtra("ID", coord);
						startActivity(intent);
					}
				}
			});
			
			return convertView;
		}

		public class ViewHolder {
			public ImageView image;
		}

		Handler handler = new Handler() {
			public void handleMessage(Message msg) {
				switch (msg.what) {
				case 1:
					mAdapter.notifyDataSetChanged();
					break;
				}
				super.handleMessage(msg);
			}
		};

		public void loading() {
			new Thread(new Runnable() {
				public void run() {
					while (true) {
						if (Bimp.max == Bimp.drr.size()) {
							Message message = new Message();
							message.what = 1;
							handler.sendMessage(message);
							break;
						} else {
							try {
								String path = Bimp.drr.get(Bimp.max);
								System.out.println(path);
								Bitmap bm = Bimp.revitionImageSize(path);
								Bimp.bmp.add(bm);
								String newStr = path.substring(path.lastIndexOf("/") + 1, path.lastIndexOf("."));
								FileUtils.saveBitmap(bm, "" + newStr);
								Bimp.max += 1;
								Message message = new Message();
								message.what = 1;
								handler.sendMessage(message);
							} catch (IOException e) {
								e.printStackTrace();
							}
						}
					}
				}
			}).start();
		}
	}

	public String getString(String s) {
		String path = null;
		if (s == null)
			return "";
		for (int i = s.length() - 1; i > 0; i++) {
			s.charAt(i);
		}
		return path;
	}

	protected void onRestart() {
		mAdapter.update();
		super.onRestart();
	}
	
	public void setReward(View v){
		final PopupReward mPopupReward = new PopupReward(this);
		mPopupReward.showAtLocation(getWindow().getDecorView(), Gravity.CENTER, 0, 0);
		mPopupReward.setOnDismissListener(new OnDismissListener() {
			@Override
			public void onDismiss() {
				mReward = mPopupReward.reward;
				mHelpReward.setText(mReward + "分");
			}
		});
	}
	
	public void setLocation(View v){
		final PopupLocation mPopupLocation = new PopupLocation(this);
		mPopupLocation.showAtLocation(getWindow().getDecorView(), Gravity.CENTER, 0, 0);
		mPopupLocation.setOnDismissListener(new OnDismissListener() {
			@Override
			public void onDismiss() {
				mLocation = mPopupLocation.location;
				mHelpLocation.setText(mLocation);
			}
		});
	}
}