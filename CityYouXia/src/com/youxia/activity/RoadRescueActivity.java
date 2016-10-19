package com.youxia.activity;

import java.util.ArrayList;
import java.util.List;

import com.alibaba.fastjson.JSON;
import com.youxia.BaseActivity;
import com.youxia.BaseLinkedListAdapter;
import com.youxia.R;
import com.youxia.entity.HelpEntity;
import com.youxia.utils.YouXiaUtils;
import com.youxia.widget.pulltorefreshlistview.OnRefreshListener;
import com.youxia.widget.pulltorefreshlistview.RefreshListView;

import android.content.Context;
import android.content.Intent;
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
		loadingProblems();
	}
	
	//下拉刷新
	@Override
	public void onDownPullRefresh() {
		if(!YouXiaUtils.netWorkStatusCheck(this)) {
			mListView.hideHeaderView();
			return;
		}
		freshProblems();
	}
		
	//加载问题和更多
	@SuppressWarnings("unused")
	public void loadingProblems() {
		if(!YouXiaUtils.netWorkStatusCheck(this)) return;
		AjaxCallBack<String> callBack = new AjaxCallBack<String>() {
			@Override
			public void onSuccess(String result) {
				super.onSuccess(result);
				//网络请求成功
				if (result != null && !TextUtils.isEmpty(result) && !result.equals("null")){
					try 
					{
						List<HelpEntity> list = JSON.parseArray(result, HelpEntity.class);		//新闻列表
						if (list == null || list.size() <= 0) {
							loadMoreFlag = false;
						}
						else {
							if(list.size() < pageSize) loadMoreFlag = false;
							else loadMoreFlag = true;
							
							if(pageNo == 1){
								RoadRescueActivity.this.freshListView((ArrayList<HelpEntity>)list);
								pullRefreshId = list.get(0).helpid;//为加载更多服务
							}else{
								RoadRescueActivity.this.addLastListView((ArrayList<HelpEntity>)list);
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
//			HttpClientHelper.loadingProblems(Integer.toString(pageSize), Integer.toString(pageNo), callBack);
	}
	
	
	@SuppressWarnings("unused")
	private void freshProblems() {
		AjaxCallBack<String> callBack = new AjaxCallBack<String>() {
			@Override
			public void onSuccess(String result) {
				super.onSuccess(result);
				//网络请求成功
				if (result != null && !TextUtils.isEmpty(result) && !result.equals("null")){
					try 
					{
						List<HelpEntity> list = JSON.parseArray(result, HelpEntity.class);		//新闻列表
						if (list == null || list.size() <= 0) {
						}
						else {
							RoadRescueActivity.this.addFirstListView((ArrayList<HelpEntity>)list);
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
//			HttpClientHelper.loadingPullRefreshProblems(Long.toString(pullRefreshId), callBack);
	}

	public void freshListView(ArrayList<HelpEntity> paramArrayList)
	{			
		if (paramArrayList == null || this.mListAdapter == null) return;
		
		this.mListAdapter.removeAll();
		for (int i = 0; i < paramArrayList.size(); i++)
			this.mListAdapter.addObject(paramArrayList.get(i));
		this.mListAdapter.notifyDataSetChanged();
	}
	
	public void addFirstListView(ArrayList<HelpEntity> paramArrayList)
	{			
		if (paramArrayList == null || this.mListAdapter == null) return;
		
		for (int i = 0; i < paramArrayList.size(); i++)  
			this.mListAdapter.addFirst(paramArrayList.get(i));
		this.mListAdapter.notifyDataSetChanged();		  
	}
	
	public void addLastListView(ArrayList<HelpEntity> paramArrayList)
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
		
		HelpEntity RoadRescueEntity = (HelpEntity)parent.getAdapter().getItem(position);
		if (RoadRescueEntity == null) return;
	
		//activity跳转		
		Intent intent = new Intent();
		
		Bundle bundle = new Bundle();
		bundle.putInt("id", RoadRescueEntity.helpid);		
		
		intent.putExtras(bundle);
		
		//intent.setClass(this, ActivityDeviceData.class);
		//startActivity(intent);
	}
	
	public class MyListAdapter extends BaseLinkedListAdapter
	{
		@SuppressWarnings("unused")
		private Context context;
		
		public MyListAdapter(Context context)
		{
			this.context = context;
		}
		
		class ViewHold {
			TextView  tvReportDatetime;
			ImageView ivReportStatus;
			TextView  tvProblem;			
	    }
		
		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			
			HelpEntity	localData = (HelpEntity)getItem(position);
			
			ViewHold hold = null;
			
			if (convertView == null){
				
				hold = new ViewHold();
				convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.listview_item_roadrescue, parent, false);
				
//				hold.ivReportStatus  	= ((ImageView)convertView.findViewById(R.id.problem_status_image));
//				hold.tvReportDatetime 	= ((TextView)convertView.findViewById(R.id.problem_report_datetime));
//				hold.tvProblem 			= ((TextView)convertView.findViewById(R.id.problem_describe));
				
				convertView.setTag(hold);
			}
			else 
			{
				hold = (ViewHold) convertView.getTag();
			}
			
//			hold.tvReportDatetime.setText(localData.create_datetime);
//			hold.tvProblem.setText(localData.problem);
//
//			if (localData.status == 1) hold.ivReportStatus.setImageResource(R.drawable.problem_report_pass);
//			else hold.ivReportStatus.setImageResource(R.drawable.problem_report_unpass);
//			

			return convertView;
		}	
	}
}