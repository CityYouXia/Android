package com.youxia.activity;

import java.util.ArrayList;
import java.util.List;

import com.alibaba.fastjson.JSON;
import com.youxia.BaseActivity;
import com.youxia.R;
import com.youxia.adapter.CommentListAdapter;
import com.youxia.entity.HelpCommentListEntity;
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
	
	private CommentListAdapter mCommentListAdapter;
	
	private int						pageNo 	= 1;
	private int						pageSize	= 8;
	private int						helpId	= -1;
	private boolean					loadMoreFlag	= true;
	private boolean					refreshFlag = false;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_commentlist);
		initView();
	}

	private void initView() {
		mTitleBarTitle.setText(getString(R.string.activity_commentlist));
		mCommentListAdapter = new CommentListAdapter(this);
		mListView.setAdapter(mCommentListAdapter);
		mListView.setOnRefreshListener(this);
		if(!YouXiaUtils.netWorkStatusCheck(this)) {			
			mLoadingView.notifyViewChanged(CustomLoadingView.State.network);
			mListView.setVisibility(View.GONE);
			
			return;
		}
		Bundle bundle = this.getIntent().getBundleExtra("bundle");
		helpId = bundle.getInt("helpId");
//		String result = "[{\"commentId\":28,\"helpId\":5,\"userId\":1,\"content\":\"我勒个去的啊\",\"createDate\":\"2分钟前\",\"commentUserName\":\"会飞的猪\",\"commentUserPhoto\":\"/userImages/doudou.jpg\",\"sex\":1},{\"commentId\":27,\"helpId\":5,\"userId\":1,\"content\":\"嗯嗯\",\"createDate\":\"41分钟前\",\"commentUserName\":\"会飞的猪\",\"commentUserPhoto\":\"/userImages/doudou.jpg\",\"sex\":1},{\"commentId\":26,\"helpId\":5,\"userId\":1,\"content\":\"我的楼主又丢了啊\",\"createDate\":\"41分钟前\",\"commentUserName\":\"会飞的猪\",\"commentUserPhoto\":\"/userImages/doudou.jpg\",\"sex\":1},{\"commentId\":25,\"helpId\":5,\"userId\":1,\"content\":\"谁抢我沙发\",\"createDate\":\"42分钟前\",\"commentUserName\":\"会飞的猪\",\"commentUserPhoto\":\"/userImages/doudou.jpg\",\"sex\":1},{\"commentId\":24,\"helpId\":5,\"userId\":1,\"content\":\"嗯嗯\",\"createDate\":\"1小时前\",\"commentUserName\":\"会飞的猪\",\"commentUserPhoto\":\"/userImages/doudou.jpg\",\"sex\":1}]";
//		List<RoadRescueDetailCommentListEntity> list = JSON.parseArray(result,
//				RoadRescueDetailCommentListEntity.class); 
//		freshListView((ArrayList<RoadRescueDetailCommentListEntity>)list);
		loadCommentList();
	}
	
	public void btnClick(View v){
		switch (v.getId()) {
		case R.id.title_bar_back:
			finish();
			break;
		}
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
						List<HelpCommentListEntity> list = JSON.parseArray(result,
								HelpCommentListEntity.class); // 评论列表
						if (list == null || list.size() <= 0) {
							loadMoreFlag = false;
						}
						else {
							if(list.size() < pageSize) loadMoreFlag = false;
							else loadMoreFlag = true;
							
							if(pageNo == 1){
								CommentListActivity.this.freshListView((ArrayList<HelpCommentListEntity>)list);
							}else{
								CommentListActivity.this.addLastListView((ArrayList<HelpCommentListEntity>)list);
							}
						}
					}
					catch (Exception e) {
						System.out.println(e.getMessage());
						if(pageNo == 1)	CommentListActivity.this.loadingProc(false);
						else mListView.hideFooterView();
					}
				}
				else {
					loadMoreFlag = false;
				}
				
				if(pageNo == 1)	CommentListActivity.this.loadingProc(false);
				else mListView.hideFooterView();
				
				if (refreshFlag) {
					mListView.hideHeaderView();
					refreshFlag = false;
				}
				
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
				
				if (refreshFlag) {
					mListView.hideHeaderView();
					refreshFlag = false;
				}
			}
		};
		HttpClientHelper.queryHelpCommentList(helpId, pageNo, pageSize, callBack);
	}
	
	public void freshListView(ArrayList<HelpCommentListEntity> paramArrayList) {
		if (paramArrayList == null || this.mCommentListAdapter == null)
			return;

		this.mCommentListAdapter.removeAll();
		for (int i = 0; i < paramArrayList.size(); i++)
			this.mCommentListAdapter.addObject(paramArrayList.get(i));
		this.mCommentListAdapter.notifyDataSetChanged();
	}
	
	public void addFirstListView(ArrayList<HelpCommentListEntity> paramArrayList)
	{			
		if (paramArrayList == null || this.mCommentListAdapter == null) return;
		
		for (int i = 0; i < paramArrayList.size(); i++)  
			this.mCommentListAdapter.addFirst(paramArrayList.get(i));
		this.mCommentListAdapter.notifyDataSetChanged();		  
	}
	
	public void addLastListView(ArrayList<HelpCommentListEntity> paramArrayList)
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
		refreshFlag = true;
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
