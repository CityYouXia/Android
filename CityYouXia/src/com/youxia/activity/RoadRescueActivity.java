package com.youxia.activity;

import java.util.ArrayList;
import java.util.List;

import com.alibaba.fastjson.JSON;
import com.youxia.BaseActivity;
import com.youxia.BaseLinkedListAdapter;
import com.youxia.R;
import com.youxia.entity.HelpListEntity;
import com.youxia.http.HttpClientHelper;
import com.youxia.utils.YouXiaApp;
import com.youxia.utils.YouXiaUtils;
import com.youxia.widget.pulltorefreshlistview.OnRefreshListener;
import com.youxia.widget.pulltorefreshlistview.RefreshListView;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import net.tsz.afinal.annotation.view.ViewInject;
import net.tsz.afinal.http.AjaxCallBack;

public class RoadRescueActivity extends BaseActivity implements ListView.OnItemClickListener, OnRefreshListener{
	
	//title
	@ViewInject(id=R.id.title_bar_title) 								TextView			mTitleBarTitle;
	@ViewInject(id=R.id.title_bar_back,click="btnClick") 				RelativeLayout		mTitleBarBack;
	
	@ViewInject(id=R.id.activity_roadrescue_listview) 					RefreshListView		mListView;
	
	
	private MyListAdapter			mListAdapter;
	
	private int						pageNo 	= 1;
	private int						pageSize	= 5;
	public	long 					pullRefreshId 	= 0;		//下拉刷新，最新ID
	private boolean					loadMoreFlag	= true;

	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_roadrescue);
		initView();
	}
	
	private void initView() {
		mTitleBarTitle.setText(getString(R.string.activity_roadrescue));
		
		loadingRoadRescues();
	}

	public void btnClick(View v){
		switch (v.getId()) {
		case R.id.title_bar_back:
			finish();
			break;
		}
	}
	
	//上拉加载更多
	@Override
	public void onLoadingMore() {
		if(!YouXiaUtils.netWorkStatusCheck(this) || !loadMoreFlag) {
			mListView.hideFooterView();
			return;
		}
		loadingRoadRescues();
	}
	
	//下拉刷新
	@Override
	public void onDownPullRefresh() {
		if(!YouXiaUtils.netWorkStatusCheck(this)) {
			mListView.hideHeaderView();
			return;
		}
		freshRoadRescues();
	}
		
	//加载问题和更多
	public void loadingRoadRescues() {
		if(!YouXiaUtils.netWorkStatusCheck(this)) return;
		AjaxCallBack<String> callBack = new AjaxCallBack<String>() {
			@Override
			public void onSuccess(String result) {
				super.onSuccess(result);
				//网络请求成功
				if (result != null && !TextUtils.isEmpty(result) && !result.equals("null")){
					try 
					{
						List<HelpListEntity> list = JSON.parseArray(result, HelpListEntity.class);		//新闻列表
						if (list == null || list.size() <= 0) {
							loadMoreFlag = false;
						}
						else {
							if(list.size() < pageSize) loadMoreFlag = false;
							else loadMoreFlag = true;
							
							if(pageNo == 1){
								RoadRescueActivity.this.freshListView((ArrayList<HelpListEntity>)list);
								pullRefreshId = list.get(0).helpid;//为加载更多服务
							}else{
								RoadRescueActivity.this.addLastListView((ArrayList<HelpListEntity>)list);
							}
						}
					}
					catch (Exception e) {
						System.out.println(e.getMessage());
						if(pageNo == 1)	RoadRescueActivity.this.loadingProc(false);
						else mListView.hideFooterView();
					}
				}
				
				if(pageNo == 1)	RoadRescueActivity.this.loadingProc(false);
				else mListView.hideFooterView();
				
				if(!loadMoreFlag) { 
					mListView.showNoMoreView();
				}
				else {
					mListView.hideNoMoreView();
					pageNo++;
				}
			}
			
			@Override
			public void onStart() {
				super.onStart();
				//启动设置
				if(pageNo == 1)	RoadRescueActivity.this.loadingProc(true);
			}
			
			@Override
			public void onFailure(Throwable t, int errorNo, String strMsg) {
				super.onFailure(t, errorNo, strMsg);
				if(pageNo == 1)	RoadRescueActivity.this.loadingProc(false);
				else mListView.hideFooterView();
			}
		};
		HttpClientHelper.loadRoadRescues(pageSize, pageNo, callBack);
	}
	
	
	private void freshRoadRescues() {
		AjaxCallBack<String> callBack = new AjaxCallBack<String>() {
			@Override
			public void onSuccess(String result) {
				super.onSuccess(result);
				//网络请求成功
				if (result != null && !TextUtils.isEmpty(result) && !result.equals("null")){
					try 
					{
						List<HelpListEntity> list = JSON.parseArray(result, HelpListEntity.class);		//新闻列表
						if (list == null || list.size() <= 0) {
						}
						else {
							RoadRescueActivity.this.addFirstListView((ArrayList<HelpListEntity>)list);
							pullRefreshId = list.get(0).helpid;
						}
					}
					catch (Exception e) {
						System.out.println(e.getMessage());
					}
				}
				mListView.hideHeaderView();
			}
			
			@Override
			public void onFailure(Throwable t, int errorNo, String strMsg) {
				super.onFailure(t, errorNo, strMsg);
				mListView.hideHeaderView();
			}
		};
		HttpClientHelper.loadPullRefreshRoadRescues(pullRefreshId, callBack);
	}

	public void freshListView(ArrayList<HelpListEntity> paramArrayList)
	{			
		if (paramArrayList == null || this.mListAdapter == null) return;
		
		this.mListAdapter.removeAll();
		for (int i = 0; i < paramArrayList.size(); i++)
			this.mListAdapter.addObject(paramArrayList.get(i));
		this.mListAdapter.notifyDataSetChanged();
	}
	
	public void addFirstListView(ArrayList<HelpListEntity> paramArrayList)
	{			
		if (paramArrayList == null || this.mListAdapter == null) return;
		
		for (int i = 0; i < paramArrayList.size(); i++)  
			this.mListAdapter.addFirst(paramArrayList.get(i));
		this.mListAdapter.notifyDataSetChanged();		  
	}
	
	public void addLastListView(ArrayList<HelpListEntity> paramArrayList)
	{
		if (paramArrayList == null || this.mListAdapter == null) return;
		
		for (int i = 0; i < paramArrayList.size(); i++)  
			this.mListAdapter.addLast(paramArrayList.get(i));
		this.mListAdapter.notifyDataSetChanged();		  
	}
	
	public void loadingProc(boolean showloading)
	{
//		if (showloading) {
//			mLoadingLayout.setVisibility(View.VISIBLE);
//			mListView.setVisibility(View.GONE);
//		}
//		else {
//			mLoadingLayout.setVisibility(View.GONE);
//			mListView.setVisibility(View.VISIBLE);
//		}
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long id) 
	{	
		if(position > this.mListAdapter.getList().size()) return;
		
		HelpListEntity roadRescueEntity = (HelpListEntity)parent.getAdapter().getItem(position);
		if (roadRescueEntity == null) return;
	
		//activity跳转		
		Intent intent = new Intent();
		
		Bundle bundle = new Bundle();
		bundle.putInt("id", roadRescueEntity.helpid);		
		
		intent.putExtras(bundle);
		
		//intent.setClass(this, ActivityDeviceData.class);
		//startActivity(intent);
	}
	
	public class MyListAdapter extends BaseLinkedListAdapter
	{
		private Context context;
		
		public MyListAdapter(Context context)
		{
			this.context = context;
		}
		
		class ViewHold {			
			ImageView ivHeadPhoto;
			TextView  tvUserName;
			TextView  tvDatetime;
			TextView  tvCommentCount;
			ImageView ivResultPic;
			TextView  tvHelpContent;
			TextView  tvAddress;
			TextView  tvRewards;
			TextView  tvViewCount;
			ImageView ivScenePhoto;
			TextView  tvScenePhotoCount;
			View     layoutScene;
	    }
		
		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			
			HelpListEntity	localData = (HelpListEntity)getItem(position);
			
			ViewHold hold = null;
							
			if (convertView == null){
				
				hold = new ViewHold();
				convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.listview_item_roadrescue, parent, false);
				
				hold.ivHeadPhoto 			 =  (ImageView)convertView.findViewById(R.id.roadrescue_listview_head_img);
				hold.tvUserName 			 =  (TextView)convertView.findViewById(R.id.roadrescue_listview_name);
				hold.tvDatetime				 =  (TextView)convertView.findViewById(R.id.roadrescue_listview_time);
				hold.tvCommentCount			 =  (TextView)convertView.findViewById(R.id.roadrescue_listview_comment_count);
				hold.ivResultPic			 =  (ImageView)convertView.findViewById(R.id.roadrescue_listview_result_img);
				hold.tvHelpContent			 =  (TextView)convertView.findViewById(R.id.roadrescue_listview_help);
				hold.tvAddress				 =  (TextView)convertView.findViewById(R.id.roadrescue_listview_address);
				hold.tvRewards				 =  (TextView)convertView.findViewById(R.id.roadrescue_listview_reward);
				hold.tvViewCount			 =  (TextView)convertView.findViewById(R.id.roadrescue_listview_viewcount);
				hold.ivScenePhoto			 =  (ImageView)convertView.findViewById(R.id.roadrescue_listview_scenephoto);
				hold.tvScenePhotoCount		 =  (TextView)convertView.findViewById(R.id.roadrescue_listview_scenephoto_count);
				
				hold.layoutScene			 =  (View)convertView.findViewById(R.id.roadrescue_listview_scene_layout);
				
				convertView.setTag(hold);
			}
			else 
			{
				hold = (ViewHold) convertView.getTag();
			}
			
			if (hold == null)  return convertView;
			
			hold.tvUserName.setText(localData.username);
			hold.tvDatetime.setText(localData.from_time);
			hold.tvCommentCount.setText(Integer.toString(localData.commentcount));
			hold.tvHelpContent.setText(localData.help_content);
			hold.tvAddress.setText(localData.site);
			hold.tvRewards.setText(Integer.toString(localData.reward_points));
			hold.tvViewCount.setText(Integer.toString(localData.viewcount));
			hold.tvScenePhotoCount.setText(Integer.toString(localData.scenePhotoCount));
			
			if (localData.userphoto.isEmpty()) {				
				hold.ivHeadPhoto.setImageResource((localData.sex == true) ? R.drawable.male_little_default : R.drawable.female_little_default);
			}
			else {
				Bitmap bitmap = BitmapFactory.decodeResource(this.context.getResources(), (localData.sex == true) ? R.drawable.male_little_default : R.drawable.female_little_default);
				YouXiaApp.mFinalBitmap.display(hold.ivHeadPhoto, HttpClientHelper.Basic_YouXiaUrl + "/images/userphotos/" + localData.userphoto, bitmap);
			}
			
			if (localData.is_solve) {
				hold.ivResultPic.setImageResource(R.drawable.help_result_ok);
			}
			else {
				hold.ivResultPic.setImageResource(R.drawable.help_result_unsolved);
			}
			
			if (localData.scenePhotoCount <= 0) {
				hold.layoutScene.setVisibility(View.GONE);
			}

			return convertView;
		}	
	}
}