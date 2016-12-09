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
import com.youxia.widget.CustomLoadingView;
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
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import net.tsz.afinal.annotation.view.ViewInject;
import net.tsz.afinal.http.AjaxCallBack;

public class FindPersonActivity extends BaseActivity implements ListView.OnItemClickListener, OnRefreshListener{
	
	//title
	@ViewInject(id=R.id.title_bar_title) 								TextView			mTitleBarTitle;
	@ViewInject(id=R.id.title_bar_back,click="btnClick") 				RelativeLayout		mTitleBarBack;
	@ViewInject(id=R.id.activity_findperson_help_button,click="btnClick") Button			mHelpBtn;
	@ViewInject(id=R.id.activity_findperson_listview) 					RefreshListView		mListView;
	@ViewInject(id=R.id.customer_loading_view) 							CustomLoadingView	mLoadingView;
	
	private MyListAdapter			mListAdapter;
	private int						pageNo 	= 1;
	private int						pageSize	= 5;
	public	long 					pullRefreshId 	= 0;		//下拉刷新，最新ID
	private boolean					loadMoreFlag	= true;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_findperson);
		initView();
	}
	
	private void initView() {
		mTitleBarTitle.setText(getString(R.string.activity_findperson));
		
		this.mListAdapter = new MyListAdapter(this);
		this.mListView.setAdapter(mListAdapter);
		this.mListView.setOnRefreshListener(this);
		this.mListView.setOnItemClickListener(this);
		
		if(!YouXiaUtils.netWorkStatusCheck(this)) {			
			this.loadingProc(CustomLoadingView.State.network);	
			return;
		}
		
		loadingFindPersons();
	}

	public void btnClick(View v){
		switch (v.getId()) {
		case R.id.title_bar_back:
			finish();
			break;
		case R.id.activity_findperson_help_button:
			Intent intent = new Intent();
			intent.setClass(this, RoadRescueHelpActivity.class);
			startActivity(intent);
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
		loadingFindPersons();
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
	public void loadingFindPersons() {
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
								FindPersonActivity.this.freshListView((ArrayList<HelpListEntity>)list);
								pullRefreshId = list.get(0).helpId;//为加载更多服务
							}else{
								FindPersonActivity.this.addLastListView((ArrayList<HelpListEntity>)list);
							}
						}
					}
					catch (Exception e) {
						System.out.println(e.getMessage());
						if(pageNo == 1)	FindPersonActivity.this.loadingProc(CustomLoadingView.State.failed);
						else mListView.hideFooterView();
					}
				}
				
				if(pageNo == 1)	FindPersonActivity.this.loadingProc(CustomLoadingView.State.done);
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
				if(pageNo == 1)	FindPersonActivity.this.loadingProc(CustomLoadingView.State.loading);
			}
			
			@Override
			public void onFailure(Throwable t, int errorNo, String strMsg) {
				super.onFailure(t, errorNo, strMsg);
				if(pageNo == 1)	FindPersonActivity.this.loadingProc(CustomLoadingView.State.failed);
				else mListView.hideFooterView();
			}
		};
		HttpClientHelper.loadFindPersons(pageSize, pageNo, callBack);
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
							FindPersonActivity.this.addFirstListView((ArrayList<HelpListEntity>)list);
							pullRefreshId = list.get(0).helpId;
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
		HttpClientHelper.loadPullRefreshFindPersons(pullRefreshId, callBack);
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
	
	public void loadingProc(CustomLoadingView.State state)
	{
		
		if (state == CustomLoadingView.State.done) {
			mLoadingView.notifyViewChanged(CustomLoadingView.State.done);
			mListView.setVisibility(View.VISIBLE);
		}
		else {
			mLoadingView.notifyViewChanged(state);
			mListView.setVisibility(View.GONE);		
		}
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
		bundle.putInt("id", roadRescueEntity.helpId);		
		
		intent.putExtras(bundle);
		
		intent.setClass(this, RoadRescueDetailActivity.class);
		startActivity(intent);
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
			TextView  tvNickName;
			TextView  tvDatetime;
			TextView  tvCommentCount;
			ImageView ivIsSolved;
			TextView  tvContent;
			TextView  tvAddress;
			TextView  tvRewardPoints;
			TextView  tvViewCount;
			ImageView ivScenePhoto1;
			ImageView ivScenePhoto2;			
			View     layoutScene;
	    }
		
		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			
			HelpListEntity	localData = (HelpListEntity)getItem(position);
			
			ViewHold hold = null;
							
			if (convertView == null){
				
				hold = new ViewHold();
				convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.listview_item_findperson, parent, false);
				
				hold.ivHeadPhoto 			 =  (ImageView)convertView.findViewById(R.id.findperson_listview_userphoto);
				hold.tvNickName 			 =  (TextView)convertView.findViewById(R.id.findperson_listview_nickname);
				hold.tvDatetime				 =  (TextView)convertView.findViewById(R.id.findperson_listview_time);
				hold.tvCommentCount			 =  (TextView)convertView.findViewById(R.id.findperson_listview_comment_count);
				hold.ivIsSolved			 	 =  (ImageView)convertView.findViewById(R.id.findperson_listview_solved_img);
				hold.tvContent			 	 =  (TextView)convertView.findViewById(R.id.findperson_listview_content);
				hold.tvAddress				 =  (TextView)convertView.findViewById(R.id.findperson_listview_address);
				hold.tvRewardPoints			 =  (TextView)convertView.findViewById(R.id.findperson_listview_rewardpoints);
				hold.tvViewCount			 =  (TextView)convertView.findViewById(R.id.findperson_listview_viewcount);
				hold.ivScenePhoto1			 =  (ImageView)convertView.findViewById(R.id.findperson_listview_scenephoto1);
				hold.ivScenePhoto2			 =  (ImageView)convertView.findViewById(R.id.findperson_listview_scenephoto2);
				
				hold.layoutScene			 =  (View)convertView.findViewById(R.id.findperson_listview_scene_layout);
				
				convertView.setTag(hold);
			}
			else 
			{
				hold = (ViewHold) convertView.getTag();
			}
			
			if (hold == null)  return convertView;
			
			hold.tvNickName.setText(localData.createUserNickName);
			hold.tvDatetime.setText(localData.createDate);
			hold.tvCommentCount.setText(Integer.toString(localData.commentCount));
			hold.tvContent.setText(localData.content);
			hold.tvAddress.setText(localData.site);
			hold.tvRewardPoints.setText(Integer.toString(localData.rewardPoints) + "奖励");
			hold.tvViewCount.setText("围观" + Integer.toString(localData.viewCount) + "次");
			
			if (localData.userPhoto.isEmpty()) {				
				hold.ivHeadPhoto.setImageResource((localData.sex == true) ? R.drawable.male_little_default : R.drawable.female_little_default);
			}
			else {
				Bitmap bitmap = BitmapFactory.decodeResource(this.context.getResources(), (localData.sex == true) ? R.drawable.male_little_default : R.drawable.female_little_default);
				YouXiaApp.mFinalBitmap.display(hold.ivHeadPhoto, HttpClientHelper.Basic_YouXiaUrl + localData.userPhoto, bitmap);
			}
			
			if (localData.isSolve == 2) {
				hold.ivIsSolved.setImageResource(R.drawable.help_result_ok);
			}
			else {
				hold.ivIsSolved.setImageResource(R.drawable.help_result_unsolved);
			}
			
			if (localData.helpPhotoCount <= 0) {
				hold.layoutScene.setVisibility(View.GONE);
			}
//			else {
//				YouXiaApp.mFinalBitmap.display(hold.ivScenePhoto, HttpClientHelper.Basic_YouXiaUrl + localData.helpPhotoUrl);
//			}

			return convertView;
		}	
	}
}