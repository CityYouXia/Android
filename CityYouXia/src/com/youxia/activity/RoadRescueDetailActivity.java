package com.youxia.activity;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONObject;

import com.alibaba.fastjson.JSON;
import com.youxia.BaseActivity;
import com.youxia.BaseLinkedListAdapter;
import com.youxia.R;
import com.youxia.entity.RoadRescueDetailCommentListEntity;
import com.youxia.entity.RoadRescueDetailHelpImageListEntity;
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
import android.widget.GridView;
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
	@ViewInject(id = R.id.title_bar_title)
	TextView mTitleTextView; // 标题
	@ViewInject(id = R.id.activity_road_rescue_detail_rescue_title)
	TextView mDetailTitleTextView;// 任务详情标题
	@ViewInject(id = R.id.activity_road_rescue_detail_information)
	TextView mContentTextView;// 求助详细信息
	@ViewInject(id = R.id.comment_send, click = "btnClick")
	TextView mSendTextView;// 发送
	@ViewInject(id = R.id.activity_road_rescue_detail_rescuers_nickname)
	TextView mHelpUserNameTextView;// 帮助者姓名
	@ViewInject(id = R.id.activity_road_rescue_detail_load_more_image, click = "btnClick")
	TextView mLoadMoreImageTextView;// 加载更多图片
	@ViewInject(id = R.id.activity_road_rescue_detail_load_more_comment, click = "btnClick")
	TextView mLoadMoreCommentsTextView;// 加载更多评论列表
	@ViewInject(id = R.id.activity_road_rescue_detail_no_comment)
	TextView mNoCommentTextView;// 没有评论提示
	@ViewInject(id = R.id.comment_edittext)
	EditText mCommentEditText;// 消息编辑框
	@ViewInject(id = R.id.title_bar_back, click = "btnClick")
	RelativeLayout mTitleBarBack;// 返回
	@ViewInject(id = R.id.activity_road_rescue_detail_resoved_information)
	LinearLayout mResolveInformationLinearLayout;// 信息被解决之后的信息块
	@ViewInject(id = R.id.activity_road_rescue_detail_user_portrait)
	ImageView mUserPortraitImageView;// 求助者头像
	@ViewInject(id = R.id.activity_road_rescue_detail_status)
	ImageView mStatusImageView;// 道路救援详情解决状态
	@ViewInject(id = R.id.activity_road_rescue_detail_rescue_button)
	Button mButtonRescue;// 立即救援按钮
	@ViewInject(id = R.id.activity_road_rescue_detail_comment_list)
	ListViewForScrollView mCommentList;// 评论列表
	@ViewInject(id = R.id.activity_road_rescue_detail_image_gridview)
	GridView mImageGridView;

	private CommentListAdapter mCommentListAdapter;
	private ImageGridViewAdapter mImageGridAdapter;
	private int pageNo = 1;
	private int pageSize = 5;
	private int userId = 1;
	private int helpId;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_road_rescue_detail);
		mTitleTextView.setText(getString(R.string.activity_road_rescue_detail_title));
		initView();
	}

	private void initView() {
		mTitleBarTitle.setText(getString(R.string.activity_road_rescue_detail_title));
		// 加载基本信息
		helpId = this.getIntent().getExtras().getInt("id");
		loadRoadRescueDetailById(helpId);
		// 加载评论列表
		mCommentListAdapter = new CommentListAdapter(this);
		mCommentList.setAdapter(mCommentListAdapter);
		loadCommentList(helpId);
		// 加载图片列表
		mImageGridAdapter = new ImageGridViewAdapter(this);
		mImageGridView.setAdapter(mImageGridAdapter);
		loadImageList(helpId);
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
						Bitmap bitmap = BitmapFactory.decodeResource(RoadRescueDetailActivity.this.getResources(),
								(iSex == 1) ? R.drawable.male_little_default : R.drawable.female_little_default);
						YouXiaApp.mFinalBitmap.display(mUserPortraitImageView,
								HttpClientHelper.Basic_YouXiaUrl + json.getString("userPhoto"), bitmap);

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

	private void addHelpComment(String content) {
		if (!YouXiaUtils.netWorkStatusCheck(this))
			return;
		AjaxCallBack<String> callBack = new AjaxCallBack<String>() {
			@Override
			public void onSuccess(String result) {
				super.onSuccess(result);
				// 网络请求成功
				if (result != null && !TextUtils.isEmpty(result) && !result.equals("null")) {
					try {
						if (result.equals("0")) {
							YouXiaUtils.showToast(RoadRescueDetailActivity.this, getString(R.string.activity_road_rescue_detail_comment_success), 0);
							mCommentEditText.setText("");
							//评论列表头部加一条新评论
						}
						else{
							YouXiaUtils.showToast(RoadRescueDetailActivity.this, getString(R.string.activity_road_rescue_detail_comment_fail), 0);
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
		HttpClientHelper.addHelpComment(helpId, userId, content, callBack);

	}

	private void loadCommentList(Integer helpId) {
		if (!YouXiaUtils.netWorkStatusCheck(this))
			return;
		AjaxCallBack<String> callBack = new AjaxCallBack<String>() {
			@Override
			public void onSuccess(String result) {
				super.onSuccess(result);
				// 网络请求成功
				if (result != null && !TextUtils.isEmpty(result) && !result.equals("null")) {
					try {
						List<RoadRescueDetailCommentListEntity> list = JSON.parseArray(result,
								RoadRescueDetailCommentListEntity.class); // 评论列表
						if (list == null || list.size() <= 0) {
							// 没有评论信息
							hasComment(false);
						} else {
							hasComment(true);
							RoadRescueDetailActivity.this
									.freshListView((ArrayList<RoadRescueDetailCommentListEntity>) list);
							if (list.size() < pageSize) {
								hasMoreComments(false);
							} else {
								hasMoreComments(true);
							}
						}
					} catch (Exception e) {
						System.out.println(e.getMessage());
					}
				} else {
					// 没有评论信息
					hasComment(false);
				}
			}

			@Override
			public void onFailure(Throwable t, int errorNo, String strMsg) {
				super.onFailure(t, errorNo, strMsg);
				YouXiaUtils.showToast(getApplication(), getString(R.string.load_fail), 0);
			}
		};
		HttpClientHelper.queryHelpCommentList(helpId, pageNo, pageSize, callBack);
	}

	private void loadImageList(int helpId) {
		if (!YouXiaUtils.netWorkStatusCheck(this))
			return;
		AjaxCallBack<String> callBack = new AjaxCallBack<String>() {
			@Override
			public void onSuccess(String result) {
				super.onSuccess(result);
				// 网络请求成功
				if (result != null && !TextUtils.isEmpty(result) && !result.equals("null")) {
					try {
						List<RoadRescueDetailHelpImageListEntity> list = JSON.parseArray(result,
								RoadRescueDetailHelpImageListEntity.class); // 图片列表
						if (list == null || list.size() <= 0) {
							// 没有图片
						} else {
							hasComment(true);
							RoadRescueDetailActivity.this
									.freshGridView((ArrayList<RoadRescueDetailHelpImageListEntity>) list);
							if (list.size() < pageSize) {
								hasMoreImages(false);
							} else {
								hasMoreImages(true);
							}
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
		HttpClientHelper.queryHelpImageList(helpId, pageNo, pageSize, callBack);
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

	// true有评论，false没有评论
	private void hasComment(Boolean flag) {
		if (flag) {
			mNoCommentTextView.setVisibility(View.GONE);
			mCommentList.setVisibility(View.VISIBLE);
		} else {
			mNoCommentTextView.setVisibility(View.VISIBLE);
			mCommentList.setVisibility(View.GONE);
		}
	}

	// true有更多评论，false没有更多评论
	private void hasMoreComments(Boolean flag) {
		if (flag) {
			mLoadMoreCommentsTextView.setVisibility(View.VISIBLE);
		} else {
			mLoadMoreCommentsTextView.setVisibility(View.GONE);
		}
	}

	// true有更多图片，false没有更多图片
	private void hasMoreImages(Boolean flag) {
		if (flag) {
			mLoadMoreImageTextView.setVisibility(View.VISIBLE);
		} else {
			mLoadMoreImageTextView.setVisibility(View.GONE);
		}
	}

	public void btnClick(View v) {
		switch (v.getId()) {
		case R.id.title_bar_back:
			finish();
			break;
		case R.id.comment_send:
			// 发送评论
			String content = mCommentEditText.getText().toString();
			if (!YouXiaUtils.inputCheckEmpty(this, content)) {
				addHelpComment(mCommentEditText.getText().toString());
			}
			break;
		case R.id.activity_road_rescue_detail_load_more_image:
			// 加载更多图片

			break;
		case R.id.activity_road_rescue_detail_load_more_comment:
			// 加载更多评论列表

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
				convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.listview_item_comment, parent,
						false);

				hold.ivHeadPhoto = (ImageView) convertView
						.findViewById(R.id.road_rescue_detail_listitem_rescuer_portrait);
				hold.tvNickName = (TextView) convertView
						.findViewById(R.id.road_rescue_detail_listitem_rescuer_nickname);
				hold.tvDatetime = (TextView) convertView.findViewById(R.id.road_rescue_detail_listitem_comment_time);
				hold.tvContent = (TextView) convertView.findViewById(R.id.road_rescue_detail_listitem_comment_content);
				hold.tvFloor = (TextView) convertView.findViewById(R.id.road_rescue_detail_listitem_floor);

				convertView.setTag(hold);
			} else {
				hold = (ViewHold) convertView.getTag();
			}

			if (hold == null)
				return convertView;

			hold.tvNickName.setText(localData.commentUserName);
			hold.tvDatetime.setText(localData.createDate);
			hold.tvContent.setText(localData.content);

			Bitmap bitmap = BitmapFactory.decodeResource(context.getResources(),
					(localData.sex == true) ? R.drawable.male_little_default : R.drawable.female_little_default);
			YouXiaApp.mFinalBitmap.display(hold.ivHeadPhoto,
					HttpClientHelper.Basic_YouXiaUrl + localData.commentUserPhoto, bitmap);

			hold.tvFloor.setText(position + 1 + "楼");
			return convertView;
		}
	}

	public class ImageGridViewAdapter extends BaseLinkedListAdapter {
		private Context context;

		public ImageGridViewAdapter(Context context) {
			this.context = context;
		}

		class ViewHold {
			ImageView ivRoadRescue;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {

			RoadRescueDetailHelpImageListEntity localData = (RoadRescueDetailHelpImageListEntity) getItem(position);

			ViewHold hold = null;

			if (convertView == null) {

				hold = new ViewHold();
				convertView = LayoutInflater.from(parent.getContext())
						.inflate(R.layout.listview_item_roadrescuedetail_image, parent, false);

				hold.ivRoadRescue = (ImageView) convertView
						.findViewById(R.id.road_rescue_detail_listitem_rescuer_portrait);

				convertView.setTag(hold);
			} else {
				hold = (ViewHold) convertView.getTag();
			}

			if (hold == null)
				return convertView;
			Bitmap bitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.defaultimage);
			YouXiaApp.mFinalBitmap.display(hold.ivRoadRescue, HttpClientHelper.Basic_YouXiaUrl + localData.imageUrl,
					bitmap);
			return convertView;
		}
	}

	public void freshListView(ArrayList<RoadRescueDetailCommentListEntity> paramArrayList) {
		if (paramArrayList == null || this.mCommentListAdapter == null)
			return;

		this.mCommentListAdapter.removeAll();
		for (int i = 0; i < paramArrayList.size(); i++)
			this.mCommentListAdapter.addObject(paramArrayList.get(i));
		this.mCommentListAdapter.notifyDataSetChanged();
	}

	public void freshGridView(ArrayList<RoadRescueDetailHelpImageListEntity> paramArrayList) {
		if (paramArrayList == null || this.mImageGridAdapter == null)
			return;

		this.mImageGridAdapter.removeAll();
		for (int i = 0; i < paramArrayList.size(); i++)
			this.mImageGridAdapter.addObject(paramArrayList.get(i));
		this.mImageGridAdapter.notifyDataSetChanged();
	}
}
