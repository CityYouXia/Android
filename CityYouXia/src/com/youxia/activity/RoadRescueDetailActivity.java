package com.youxia.activity;

import java.util.ArrayList;

import org.json.JSONObject;

import com.youxia.BaseActivity;
import com.youxia.BaseLinkedListAdapter;
import com.youxia.R;
import com.youxia.activity.RoadRescueActivity.MyListAdapter;
import com.youxia.entity.HelpListEntity;
import com.youxia.entity.RoadRescueDetailCommentListEntity;
import com.youxia.http.HttpClientHelper;
import com.youxia.utils.YouXiaApp;
import com.youxia.utils.YouXiaUtils;
import com.youxia.widget.ListViewForScrollView;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import net.tsz.afinal.annotation.view.ViewInject;
import net.tsz.afinal.http.AjaxCallBack;

public class RoadRescueDetailActivity extends BaseActivity {

	@ViewInject(id = R.id.title_bar_title)
	TextView mTitleBarTitle;// 标题
	@ViewInject(id = R.id.activity_road_rescue_detail_address)
	TextView mSiteTextView;// 地址
	@ViewInject(id = R.id.activity_road_rescue_detail_score)
	TextView mRewardPointsTextView;// 积分
	@ViewInject(id = R.id.activity_road_rescue_detail_nickname)
	TextView mNickNameTextView;// 求助者昵称
	@ViewInject(id = R.id.road_rescue_detail_listitem_comment_time)
	TextView mCreateDateTextView;// 发布多长时间
	@ViewInject(id = R.id.activity_road_rescue_detail_title)
	TextView mDetailTitleTextView;// 求助标题
	@ViewInject(id = R.id.activity_road_rescue_detail_information)
	TextView mContentTextView;// 求助详细信息
	@ViewInject(id = R.id.comment_send)
	TextView mSendTextView;// 发送
	@ViewInject(id = R.id.activity_road_rescue_detail_rescuers_nickname)
	TextView mHelpUserNameTextView;// 帮助者姓名
	@ViewInject(id = R.id.comment_edittext)
	EditText mCommentEditText;// 消息编辑框
	@ViewInject(id = R.id.title_bar_back, click = "btnClick")
	RelativeLayout mTitleBarBack;// 返回
	@ViewInject(id = R.id.activity_road_rescue_detail_comment_list)
	ListViewForScrollView mCommentList;// 评论列表
	@ViewInject(id = R.id.activity_road_rescue_detail_rescue_button)
	Button mButtonRescue;// 立即救援按钮
	@ViewInject(id = R.id.activity_road_rescue_detail_resoved_information)
	LinearLayout mResolveInformationLinearLayout;// 信息被解决之后的信息块
	@ViewInject(id = R.id.activity_road_rescue_detail_user_portrait)
	ImageView mUserPortraitImageView;// 求助者头像
	@ViewInject(id = R.id.activity_road_rescue_detail_status)
	ImageView mStatusImageView;// 道路救援详情解决状态

	private CommentListAdapter mCommentListAdapter;
	private int						pageNo 	= 1;
	private int						pageSize	= 5;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_road_rescue_detail);
		initView();
	}

	private void initView() {
		mTitleBarTitle.setText(getString(R.string.activity_road_rescue_detail_title));
		Integer helpId = this.getIntent().getExtras().getInt("id");
		loadRoadRescueDetailById(helpId);
		this.mCommentListAdapter = new CommentListAdapter(this);
		this.mCommentList.setAdapter(mCommentListAdapter);
		//loadCommentList(helpId);
	}

	private void loadRoadRescueDetailById(Integer id) {
		if (!YouXiaUtils.netWorkStatusCheck(this))
			return;
		AjaxCallBack<String> callBack = new AjaxCallBack<String>() {
			@Override
			public void onSuccess(String result) {
				super.onSuccess(result);
				// 网络请求成功
				if (result != null && !TextUtils.isEmpty(result) && !result.equals("null")) {
					try {
						JSONObject json = new JSONObject(result);
						mNickNameTextView.setText(json.getString("createUserNickName"));
						mDetailTitleTextView.setText(json.getString("name"));
						mContentTextView.setText(json.getString("content"));
						mSiteTextView.setText(json.getString("site"));
						mHelpUserNameTextView.setText(json.getString("helpUserName"));
						mRewardPointsTextView.setText(json.getString("rewardPoints") + "积分");
						mCreateDateTextView.setText(json.getString("createDate"));
						int iSex = json.getInt("sex");
						Bitmap bitmap = BitmapFactory.decodeResource(getResources(), (iSex == 1) ? R.drawable.male_little_default : R.drawable.female_little_default);
						YouXiaApp.mFinalBitmap.display(mUserPortraitImageView, HttpClientHelper.Basic_YouXiaUrl +  json.getString("userPhoto"), bitmap);
						
						int isSolve = json.getInt("isSolve");
						if (isSolve == 0) {
							isResolved(false);
						} else {
							isResolved(true);
						}

					} catch (Exception e) {
						System.out.println(e.getMessage());

					}
				}

			}

			@Override
			public void onFailure(Throwable t, int errorNo, String strMsg) {
				super.onFailure(t, errorNo, strMsg);
				YouXiaUtils.showToast(getApplication(), getString(R.string.load_fail), 0);
			}
		};
		HttpClientHelper.loadRoadRescueDetailById(id, callBack);

	}

	private void loadCommentList(Integer id) {
		if (!YouXiaUtils.netWorkStatusCheck(this))
			return;
		AjaxCallBack<String> callBack = new AjaxCallBack<String>() {
			@Override
			public void onSuccess(String result) {
				super.onSuccess(result);
				// 网络请求成功
				if (result != null && !TextUtils.isEmpty(result) && !result.equals("null")) {
					try {
						JSONObject json = new JSONObject(result);
						mNickNameTextView.setText(json.getString("createUserNickName"));
						mDetailTitleTextView.setText(json.getString("name"));
						mContentTextView.setText(json.getString("content"));
						mSiteTextView.setText(json.getString("site"));
						mHelpUserNameTextView.setText(json.getString("helpUserName"));
						mRewardPointsTextView.setText(json.getString("rewardPoints") + "积分");
						mCreateDateTextView.setText(json.getString("createDate"));
						
						//Bitmap bitmap = BitmapFactory.decodeResource(this.context.getResources(), (localData.sex == true) ? R.drawable.male_little_default : R.drawable.female_little_default);
						//YouXiaApp.mFinalBitmap.display(mUserPortraitImageView, HttpClientHelper.Basic_YouXiaUrl +  json.getString("userPhoto"), bitmap);
						
						int isSolve = json.getInt("isSolve");
						if (isSolve == 0) {
							isResolved(false);
						} else {
							isResolved(true);
						}

					} catch (Exception e) {
						System.out.println(e.getMessage());

					}
				}

			}

			@Override
			public void onFailure(Throwable t, int errorNo, String strMsg) {
				super.onFailure(t, errorNo, strMsg);
				YouXiaUtils.showToast(getApplication(), getString(R.string.load_fail), 0);
			}
		};
		HttpClientHelper.loadRoadRescueDetailById(id, callBack);

	}	
	
	// true 已解决 false未解决
	private void isResolved(Boolean flag) {
		if (flag) {
			mStatusImageView.setImageResource(R.drawable.resolved);
			mButtonRescue.setVisibility(View.GONE);
			mResolveInformationLinearLayout.setVisibility(View.VISIBLE);
		} else {
			mStatusImageView.setImageResource(R.drawable.unresolved);
			mButtonRescue.setVisibility(View.VISIBLE);
			mResolveInformationLinearLayout.setVisibility(View.GONE);
		}
	}

	public void btnClick(View v) {
		switch (v.getId()) {
		case R.id.title_bar_back:
			finish();
			break;
		}
	}

	public class CommentListAdapter extends BaseLinkedListAdapter {
		private Context context;

		public CommentListAdapter(Context context) {
			this.context = context;
		}

		class ViewHold {
			ImageView ivHeadPhoto;
			TextView tvNickName;
			TextView tvDatetime;
			TextView tvContent;
			TextView tvFloor;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {

			RoadRescueDetailCommentListEntity localData = (RoadRescueDetailCommentListEntity) getItem(position);

			ViewHold hold = null;

			if (convertView == null) {

				hold = new ViewHold();
				convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.listview_item_comment,
						parent, false);

				hold.ivHeadPhoto = (ImageView) convertView.findViewById(R.id.road_rescue_detail_listitem_rescuer_portrait);
				hold.tvNickName = (TextView) convertView.findViewById(R.id.road_rescue_detail_listitem_rescuer_nickname);
				hold.tvDatetime = (TextView) convertView.findViewById(R.id.road_rescue_detail_listitem_comment_time);
				hold.tvContent = (TextView) convertView.findViewById(R.id.road_rescue_detail_listitem_comment_content);
				hold.tvFloor = (TextView) convertView.findViewById(R.id.road_rescue_detail_listitem_floor);
				
				convertView.setTag(hold);
			} else {
				hold = (ViewHold) convertView.getTag();
			}

			if (hold == null)
				return convertView;

			hold.tvNickName.setText(localData.commentNickName);
			hold.tvDatetime.setText(localData.createDate);
			hold.tvContent.setText(localData.commentContent);
			
			
			if (localData.userPhoto.isEmpty()) {
				hold.ivHeadPhoto.setImageResource(
						(localData.sex == true) ? R.drawable.male_little_default : R.drawable.female_little_default);
			} else {
				Bitmap bitmap = BitmapFactory.decodeResource(this.context.getResources(),
						(localData.sex == true) ? R.drawable.male_little_default : R.drawable.female_little_default);
			}
			hold.tvFloor.setText(position+1 + "楼");
			return convertView;
		}
	}

	
	public void freshListView(ArrayList<HelpListEntity> paramArrayList)
	{			
		if (paramArrayList == null || this.mCommentListAdapter == null) return;
		
		this.mCommentListAdapter.removeAll();
		for (int i = 0; i < paramArrayList.size(); i++)
			this.mCommentListAdapter.addObject(paramArrayList.get(i));
		this.mCommentListAdapter.notifyDataSetChanged();
	}
	
}
