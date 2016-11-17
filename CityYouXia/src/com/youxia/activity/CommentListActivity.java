package com.youxia.activity;

import java.util.ArrayList;
import java.util.List;

import com.alibaba.fastjson.JSON;
import com.youxia.BaseActivity;
import com.youxia.R;
import com.youxia.adapter.AdapterCommentList;
import com.youxia.entity.RoadRescueDetailCommentListEntity;
import com.youxia.http.HttpClientHelper;
import com.youxia.utils.YouXiaUtils;
import com.youxia.widget.CustomLoadingView;
import com.youxia.widget.pulltorefreshlistview.OnRefreshListener;
import com.youxia.widget.pulltorefreshlistview.RefreshListView;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;
import net.tsz.afinal.annotation.view.ViewInject;
import net.tsz.afinal.http.AjaxCallBack;

public class CommentListActivity extends BaseActivity implements OnRefreshListener{

	//title
	@ViewInject(id=R.id.title_bar_title) 								TextView			mTitleBarTitle;
	@ViewInject(id=R.id.title_bar_back,click="btnClick") 				RelativeLayout		mTitleBarBack;
	@ViewInject(id=R.id.activity_commentlist_listview) 					RefreshListView		mListView;
	@ViewInject(id=R.id.activity_commentlist_customer_loading_view)		CustomLoadingView	mLoadingView;
	
	private AdapterCommentList mCommentListAdapter;
	
	private int						pageNo 	= 1;
	private int						pageSize	= 5;
	private int						helpId	= -1;
	public	long 					pullRefreshId 	= 0;		//下拉刷新，最新ID
	private boolean					loadMoreFlag	= true;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_commentlist);
		initView();
	}

	private void initView() {
		mTitleBarTitle.setText(getString(R.string.activity_commentlist));
		mCommentListAdapter = new AdapterCommentList(this);
		mListView.setOnRefreshListener(this);
		if(!YouXiaUtils.netWorkStatusCheck(this)) {			
			mLoadingView.notifyViewChanged(CustomLoadingView.State.network);
			mListView.setVisibility(View.GONE);
			
			return;
		}
		helpId = this.getIntent().getExtras().getInt("helpId");
		loadCommentList();
	}
	
	//加载问题和更多
	public void loadCommentList() {
		if(!YouXiaUtils.netWorkStatusCheck(this)) return;
		
		AjaxCallBack<String> callBack = new AjaxCallBack<String>() {
			@Override
			public void onSuccess(String result) {
				super.onSuccess(result);
				//网络请求成功
				if (result != null && !TextUtils.isEmpty(result) && !result.equals("null")){
					try 
					{
						List<RoadRescueDetailCommentListEntity> list = JSON.parseArray(result,
								RoadRescueDetailCommentListEntity.class); // 评论列表
						if (list == null || list.size() <= 0) {
							loadMoreFlag = false;
						}
						else {
							if(list.size() < pageSize) loadMoreFlag = false;
							else loadMoreFlag = true;
							
							if(pageNo == 1){
								CommentListActivity.this.freshListView((ArrayList<RoadRescueDetailCommentListEntity>)list);
								pullRefreshId = list.get(0).commentId;//为加载更多服务
							}else{
								CommentListActivity.this.addLastListView((ArrayList<RoadRescueDetailCommentListEntity>)list);
							}
						}
					}
					catch (Exception e) {
						System.out.println(e.getMessage());
						if(pageNo == 1)	CommentListActivity.this.loadingProc(false);
						else mListView.hideFooterView();
					}
				}
				
				if(pageNo == 1)	CommentListActivity.this.loadingProc(false);
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
				if(pageNo == 1)	CommentListActivity.this.loadingProc(true);
			}
			
			@Override
			public void onFailure(Throwable t, int errorNo, String strMsg) {
				super.onFailure(t, errorNo, strMsg);
				if(pageNo == 1)	CommentListActivity.this.loadingProc(false);
				else mListView.hideFooterView();
			}
		};
		HttpClientHelper.queryHelpCommentList(helpId, pageNo, pageSize, callBack);
	}
	
	public void freshListView(ArrayList<RoadRescueDetailCommentListEntity> paramArrayList) {
		if (paramArrayList == null || this.mCommentListAdapter == null)
			return;

		this.mCommentListAdapter.removeAll();
		for (int i = 0; i < paramArrayList.size(); i++)
			this.mCommentListAdapter.addObject(paramArrayList.get(i));
		this.mCommentListAdapter.notifyDataSetChanged();
	}
	
	public void addFirstListView(ArrayList<RoadRescueDetailCommentListEntity> paramArrayList)
	{			
		if (paramArrayList == null || this.mCommentListAdapter == null) return;
		
		for (int i = 0; i < paramArrayList.size(); i++)  
			this.mCommentListAdapter.addFirst(paramArrayList.get(i));
		this.mCommentListAdapter.notifyDataSetChanged();		  
	}
	
	public void addLastListView(ArrayList<RoadRescueDetailCommentListEntity> paramArrayList)
	{
		if (paramArrayList == null || this.mCommentListAdapter == null) return;
		
		for (int i = 0; i < paramArrayList.size(); i++)  
			this.mCommentListAdapter.addLast(paramArrayList.get(i));
		this.mCommentListAdapter.notifyDataSetChanged();		  
	}
	
	public void loadingProc(boolean showloading)
	{
		if (showloading) {
			mLoadingView.notifyViewChanged(CustomLoadingView.State.loading);
			mListView.setVisibility(View.GONE);
		}
		else {
			mLoadingView.notifyViewChanged(CustomLoadingView.State.done);
			mListView.setVisibility(View.VISIBLE);
		}
	}
	
	@Override
	public void onDownPullRefresh() {
		if(!YouXiaUtils.netWorkStatusCheck(this)) {
			mListView.hideHeaderView();
			return;
		}
		pageNo = 1;
		loadCommentList();
	}

	@Override
	public void onLoadingMore() {
		if(!YouXiaUtils.netWorkStatusCheck(this) || !loadMoreFlag) {
			mListView.hideFooterView();
			return;
		}
		loadCommentList();
	}
	

}
