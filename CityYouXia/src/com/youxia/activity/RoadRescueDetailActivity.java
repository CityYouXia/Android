package com.youxia.activity;

import org.json.JSONObject;

import com.youxia.BaseActivity;
import com.youxia.BaseLinkedListAdapter;
import com.youxia.R;
import com.youxia.entity.HelpListEntity;
import com.youxia.http.HttpClientHelper;
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
	TextView mAddressTextView;// 地址
	@ViewInject(id = R.id.activity_road_rescue_detail_score)
	TextView mScoreTextView;// 积分
	@ViewInject(id = R.id.activity_road_rescue_detail_nickname)
	TextView mNickNameTextView;// 求助者昵称
	@ViewInject(id = R.id.activity_road_rescue_detail_time_detail)
	TextView mTimeTextView;// 发布多长时间
	@ViewInject(id = R.id.activity_road_rescue_detail_title)
	TextView mDetailTitleTextView;// 求助标题
	@ViewInject(id = R.id.activity_road_rescue_detail_information)
	TextView mDetailInformationTextView;// 求助详细信息
	@ViewInject(id = R.id.comment_send)
	TextView mSendTextView;// 发送
	@ViewInject(id = R.id.activity_road_rescue_detail_rescuers_nickname)
	TextView mRescueNameTextView;// 帮助者姓名
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
		
		
		//isResolved(true);???
	}

	private void loadRoadRescueDetailById(Integer id) {
		if(!YouXiaUtils.netWorkStatusCheck(this)) return;
		AjaxCallBack<String> callBack = new AjaxCallBack<String>() {
			@Override
			public void onSuccess(String result) {
				super.onSuccess(result);
				//网络请求成功
				if (result != null && !TextUtils.isEmpty(result) && !result.equals("null")){
					try 
					{
						JSONObject json = new JSONObject(result);
						mNickNameTextView.setText(json.getString("createUserNickName"));
						mDetailTitleTextView.setText(json.getString("name"));
						mDetailInformationTextView.setText(json.getString("content"));
						mAddressTextView.setText(json.getString("site"));
						mRescueNameTextView.setText(json.getString("helpUserName"));
						mScoreTextView.setText(json.getString("rewardPoints"));
						mRescueNameTextView.setText(json.getString(""));
						
						
						
						int isSolve = json.getInt("isSolve");
						if(isSolve == 0)  isResolved(false);
							isResolved(true);
						
						
					}
					catch (Exception e) {
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

	public class MyListAdapter extends BaseLinkedListAdapter {
		private Context context;

		public MyListAdapter(Context context) {
			this.context = context;
		}

		class ViewHold {
			ImageView ivHeadPhoto;
			TextView tvNickName;
			TextView tvDatetime;
			TextView tvCommentCount;
			ImageView ivIsSolved;
			TextView tvContent;
			TextView tvAddress;
			TextView tvRewardPoints;
			TextView tvViewCount;
			ImageView ivScenePhoto;
			TextView tvScenePhotoCount;
			View layoutScene;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {

			HelpListEntity localData = (HelpListEntity) getItem(position);

			ViewHold hold = null;

			if (convertView == null) {

				hold = new ViewHold();
				convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.listview_item_roadrescue,
						parent, false);

				hold.ivHeadPhoto = (ImageView) convertView.findViewById(R.id.roadrescue_listview_userphoto);
				hold.tvNickName = (TextView) convertView.findViewById(R.id.roadrescue_listview_nickname);
				hold.tvDatetime = (TextView) convertView.findViewById(R.id.roadrescue_listview_time);
				hold.tvCommentCount = (TextView) convertView.findViewById(R.id.roadrescue_listview_comment_count);
				hold.ivIsSolved = (ImageView) convertView.findViewById(R.id.roadrescue_listview_solved_img);
				hold.tvContent = (TextView) convertView.findViewById(R.id.roadrescue_listview_content);
				hold.tvAddress = (TextView) convertView.findViewById(R.id.roadrescue_listview_address);
				hold.tvRewardPoints = (TextView) convertView.findViewById(R.id.roadrescue_listview_rewardpoints);
				hold.tvViewCount = (TextView) convertView.findViewById(R.id.roadrescue_listview_viewcount);
				hold.ivScenePhoto = (ImageView) convertView.findViewById(R.id.roadrescue_listview_scenephoto);
				hold.tvScenePhotoCount = (TextView) convertView.findViewById(R.id.roadrescue_listview_scenephoto_count);

				hold.layoutScene = (View) convertView.findViewById(R.id.roadrescue_listview_scene_layout);

				convertView.setTag(hold);
			} else {
				hold = (ViewHold) convertView.getTag();
			}

			if (hold == null)
				return convertView;

			hold.tvNickName.setText(localData.createUserNickName);
			hold.tvDatetime.setText(localData.createDate);
			hold.tvCommentCount.setText(Integer.toString(localData.commentCount));
			hold.tvContent.setText(localData.content);
			hold.tvAddress.setText(localData.site);
			hold.tvRewardPoints.setText(Integer.toString(localData.rewardPoints) + "奖励");
			hold.tvViewCount.setText("围观" + Integer.toString(localData.viewCount) + "次");
			hold.tvScenePhotoCount.setText(Integer.toString(localData.helpPhotoCount) + "张");

			if (localData.userPhoto.isEmpty()) {
				hold.ivHeadPhoto.setImageResource(
						(localData.sex == true) ? R.drawable.male_little_default : R.drawable.female_little_default);
			} else {
				Bitmap bitmap = BitmapFactory.decodeResource(this.context.getResources(),
						(localData.sex == true) ? R.drawable.male_little_default : R.drawable.female_little_default);
				// YouXiaApp.mFinalBitmap.display(hold.ivHeadPhoto,
				// HttpClientHelper.Basic_YouXiaUrl + localData.userPhoto,
				// bitmap);
			}

			if (localData.isSolve == 2) {
				hold.ivIsSolved.setImageResource(R.drawable.help_result_ok);
			} else {
				hold.ivIsSolved.setImageResource(R.drawable.help_result_unsolved);
			}

			if (localData.helpPhotoCount <= 0) {
				hold.layoutScene.setVisibility(View.GONE);
			} else {
				// YouXiaApp.mFinalBitmap.display(hold.ivScenePhoto,
				// HttpClientHelper.Basic_YouXiaUrl + localData.helpPhotoUrl);

			}

			return convertView;
		}
	}

}
