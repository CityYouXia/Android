package com.youxia.activity;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import com.alibaba.fastjson.JSON;
import com.youxia.BaseActivity;
import com.youxia.BaseLinkedListAdapter;
import com.youxia.R;
import com.youxia.adapter.CommentListAdapter;
import com.youxia.entity.HelpCommentListEntity;
import com.youxia.entity.HelpImageListEntity;
import com.youxia.http.HttpClientHelper;
import com.youxia.utils.YouXiaApp;
import com.youxia.utils.YouXiaUtils;
import com.youxia.widget.ListViewForScrollView;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
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

	@ViewInject(id = R.id.title_bar_title)	TextView mTitleBarTitle;// 标题
	@ViewInject(id = R.id.activity_road_rescue_detail_address)	TextView mSiteTextView;// 地址
	@ViewInject(id = R.id.activity_road_rescue_detail_score)	TextView mRewardPointsTextView;// 积分
	@ViewInject(id = R.id.activity_road_rescue_detail_nickname)	TextView mNickNameTextView;// 求助者昵称
	@ViewInject(id = R.id.road_rescue_detail_listitem_comment_time)	TextView mCreateDateTextView;// 发布多长时间
	@ViewInject(id = R.id.title_bar_title)	TextView mTitleTextView; // 标题
	@ViewInject(id = R.id.activity_road_rescue_detail_rescue_title)	TextView mDetailTitleTextView;// 任务详情标题
	@ViewInject(id = R.id.activity_road_rescue_detail_information)	TextView mContentTextView;// 求助详细信息
	@ViewInject(id = R.id.comment_send, click = "btnClick")	TextView mSendTextView;// 发送
	@ViewInject(id = R.id.activity_road_rescue_detail_rescuers_nickname)	TextView mHelpUserNameTextView;// 帮助者姓名
	@ViewInject(id = R.id.activity_road_rescue_detail_load_more_image, click = "btnClick")	TextView mLoadMoreImageTextView;// 加载更多图片
	@ViewInject(id = R.id.activity_road_rescue_detail_load_more_comment, click = "btnClick")	TextView mLoadMoreCommentsTextView;// 加载更多评论列表
	@ViewInject(id = R.id.activity_road_rescue_detail_no_comment)	TextView mNoCommentTextView;// 没有评论提示
	@ViewInject(id = R.id.comment_edittext)	EditText mCommentEditText;// 消息编辑框
	@ViewInject(id = R.id.title_bar_back, click = "btnClick")	RelativeLayout mTitleBarBack;// 返回
	@ViewInject(id = R.id.activity_road_rescue_detail_resoved_information)	LinearLayout mResolveInformationLinearLayout;// 信息被解决之后的信息块
	@ViewInject(id = R.id.activity_road_rescue_detail_user_portrait)	ImageView mUserPortraitImageView;// 求助者头像
	@ViewInject(id = R.id.activity_road_rescue_detail_status)	ImageView mStatusImageView;// 道路救援详情解决状态
	@ViewInject(id = R.id.activity_road_rescue_detail_rescue_button)	Button mButtonRescue;// 立即救援按钮
	@ViewInject(id = R.id.activity_road_rescue_detail_comment_list)	ListViewForScrollView mCommentList;// 评论列表
	@ViewInject(id = R.id.activity_road_rescue_detail_image_gridview)	GridView mImageGridView;

	private CommentListAdapter mCommentListAdapter;
	private ImageGridViewAdapter mImageGridAdapter;
	private ArrayList<HelpImageListEntity> mImageList;
	private int pageNo = 1;
	private int pageSize = 5;
	private int userId = 1;
	private int helpId;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_roadrescue_detail);
		mTitleTextView.setText(getString(R.string.activity_road_rescue_detail_title));
		initView();
	}

	private void initView() {
		mTitleBarTitle.setText(getString(R.string.activity_road_rescue_detail_title));
		// 加载基本信息
		helpId = this.getIntent().getIntExtra("id", -1);
		loadRoadRescueDetailById(helpId);
		// 加载评论列表
		mCommentListAdapter = new CommentListAdapter(this);
		mCommentList.setAdapter(mCommentListAdapter);
		loadCommentList(helpId);
		// 加载图片列表
		mImageGridAdapter = new ImageGridViewAdapter(this);
		mImageGridView.setAdapter(mImageGridAdapter);
		mImageGridView.setOnItemClickListener(new ImageGridViewItemClickListener());
		mImageGridView.setHorizontalSpacing(YouXiaUtils.dip2px(this, 1));
		loadImageList(helpId);

		// test
//		mImageList = new ArrayList<HelpImageListEntity>();
//		hasImages(true);
//
//		HelpImageListEntity entity0 = new HelpImageListEntity();
//		entity0.imageUrl = "http://222.222.60.178:19927/icpms_appserver/images/qrcode/APKDownload_huayi.png";
//		entity0.createDate = "2016";
//		entity0.helpId = "0";
//		entity0.imageId = "0";
//		entity0.modifyDate = "2016";
//		entity0.name = "123";
//		entity0.orders = "123";
//		HelpImageListEntity entity1 = new HelpImageListEntity();
//		entity1.imageUrl = "http://222.222.60.178:19927/icpms_appserver/images/head/carowner_1.png";
//		HelpImageListEntity entity2 = new HelpImageListEntity();
//		entity2.imageUrl = "http://222.222.60.178:19927/icpms_appserver/images/head/carowner_3.png";
//		HelpImageListEntity entity3 = new HelpImageListEntity();
//		entity3.imageUrl = "http://222.222.60.178:19927/icpms_appserver/images/head/carowner_1.png";
//		mImageList.add(entity0);
//		mImageList.add(entity1);
//		mImageList.add(entity2);
//		mImageList.add(entity3);
//		RoadRescueDetailActivity.this.freshGridView(mImageList);
//		if (mImageList.size() < 3) {
//			hasMoreImages(false);
//		} else {
//			hasMoreImages(true);
//		}
//		
//		String commentList = "[{\"commentId\":28,\"helpId\":5,\"userId\":1,\"content\":\"我勒个去的啊\",\"createDate\":\"2分钟前\",\"commentUserName\":\"会飞的猪\",\"commentUserPhoto\":\"/userImages/doudou.jpg\",\"sex\":1},{\"commentId\":27,\"helpId\":5,\"userId\":1,\"content\":\"嗯嗯\",\"createDate\":\"41分钟前\",\"commentUserName\":\"会飞的猪\",\"commentUserPhoto\":\"/userImages/doudou.jpg\",\"sex\":1},{\"commentId\":26,\"helpId\":5,\"userId\":1,\"content\":\"我的楼主又丢了啊\",\"createDate\":\"41分钟前\",\"commentUserName\":\"会飞的猪\",\"commentUserPhoto\":\"/userImages/doudou.jpg\",\"sex\":1},{\"commentId\":25,\"helpId\":5,\"userId\":1,\"content\":\"谁抢我沙发\",\"createDate\":\"42分钟前\",\"commentUserName\":\"会飞的猪\",\"commentUserPhoto\":\"/userImages/doudou.jpg\",\"sex\":1},{\"commentId\":24,\"helpId\":5,\"userId\":1,\"content\":\"嗯嗯\",\"createDate\":\"1小时前\",\"commentUserName\":\"会飞的猪\",\"commentUserPhoto\":\"/userImages/doudou.jpg\",\"sex\":1}]";
//		List<HelpCommentListEntity> list = JSON.parseArray(commentList,HelpCommentListEntity.class); // 评论列表
//		freshListView((ArrayList<HelpCommentListEntity>) list);
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
						if (isSolve == 1) {
							isResolved(false);
						} else if (isSolve == 2) {
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
			@SuppressWarnings("static-access")
			@Override
			public void onSuccess(String result) {
				super.onSuccess(result);
				// 网络请求成功
				if (result != null && !TextUtils.isEmpty(result) && !result.equals("null")) {
					try {
						if (result.equals("0")) {
							YouXiaUtils.showToast(RoadRescueDetailActivity.this,
									getString(R.string.activity_road_rescue_detail_comment_success), 0);
							mCommentEditText.setText("");
							InputMethodManager imm = (InputMethodManager) getSystemService(
									RoadRescueDetailActivity.this.INPUT_METHOD_SERVICE);
							// 隐藏键盘
							imm.hideSoftInputFromWindow(mCommentEditText.getWindowToken(), 0);
							// 评论列表头部加一条新评论
							loadCommentList(helpId);// 重新加载评论列表
						} else {
							YouXiaUtils.showToast(RoadRescueDetailActivity.this,
									getString(R.string.activity_road_rescue_detail_comment_fail), 0);
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
						List<HelpCommentListEntity> list = JSON.parseArray(result,
								HelpCommentListEntity.class); // 评论列表
						if (list == null || list.size() <= 0) {
							// 没有评论信息
							hasComments(false);
						} else {
							hasComments(true);
							RoadRescueDetailActivity.this
									.freshListView((ArrayList<HelpCommentListEntity>) list);
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
					hasComments(false);
				}
			}

			@Override
			public void onFailure(Throwable t, int errorNo, String strMsg) {
				super.onFailure(t, errorNo, strMsg);
				YouXiaUtils.showToast(getApplication(), getString(R.string.load_fail), 0);
				hasComments(false);
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
						mImageList = (ArrayList<HelpImageListEntity>) JSON.parseArray(result,
								HelpImageListEntity.class); // 图片列表
						if (mImageList == null || mImageList.size() <= 0) {
							// 没有图片
							hasImages(false);
						} else {
							hasImages(true);
							RoadRescueDetailActivity.this.freshGridView(mImageList);
							if (mImageList.size() < 3) {
								hasMoreImages(false);
							} else {
								hasMoreImages(true);
							}
						}
					} catch (Exception e) {
						System.out.println(e.getMessage());
						hasImages(false);
					}
				}
			}

			@Override
			public void onFailure(Throwable t, int errorNo, String strMsg) {
				super.onFailure(t, errorNo, strMsg);
				YouXiaUtils.showToast(getApplication(), getString(R.string.load_fail), 0);
				hasImages(false);
			}
		};
		HttpClientHelper.queryHelpImageList(helpId, -1, -1, callBack);
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
	private void hasComments(Boolean flag) {
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

	// true有图片，false无图片
	private void hasImages(Boolean flag) {
		if (flag) {
			mImageGridView.setVisibility(View.VISIBLE);
		} else {
			mImageGridView.setVisibility(View.GONE);
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

	// 跳转到某个Activity,
	private void jumpToActivity(Class<?> activityClass, Bundle bundle) {
		Intent intent = new Intent();
		if (bundle != null) {
			intent.putExtra("bundle", bundle);
		}
		intent.setClass(this, activityClass);
		startActivity(intent);
	}

	public void btnClick(View v) throws JSONException {
		Bundle bundle = new Bundle();
		bundle.putInt("helpId", helpId);
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
			bundle.putInt("position", 3);
			bundle.putSerializable("imageList", mImageList);
			jumpToActivity(ImageListActivity.class, bundle);
			break;
		case R.id.activity_road_rescue_detail_load_more_comment:
			// 加载更多评论列表
			jumpToActivity(CommentListActivity.class, bundle);
			break;
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

			HelpImageListEntity localData = (HelpImageListEntity) getItem(position);

			ViewHold hold = null;

			if (convertView == null) {

				hold = new ViewHold();
				convertView = LayoutInflater.from(parent.getContext())
						.inflate(R.layout.gridview_item_help_image, parent, false);

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

	private class ImageGridViewItemClickListener implements OnItemClickListener {

		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
			Bundle bundle = new Bundle();
			bundle.putInt("position", position);
			bundle.putSerializable("imageList", mImageList);
			jumpToActivity(ImageListActivity.class, bundle);
		}

	}

	public void freshListView(ArrayList<HelpCommentListEntity> paramArrayList) {
		if (paramArrayList == null || this.mCommentListAdapter == null)
			return;

		this.mCommentListAdapter.removeAll();
		for (int i = 0; i < paramArrayList.size(); i++)
			this.mCommentListAdapter.addObject(paramArrayList.get(i));
		this.mCommentListAdapter.notifyDataSetChanged();
	}

	public void addFirstListView(ArrayList<HelpCommentListEntity> paramArrayList) {
		if (paramArrayList == null || this.mCommentListAdapter == null)
			return;

		for (int i = 0; i < paramArrayList.size(); i++)
			this.mCommentListAdapter.addFirst(paramArrayList.get(i));
		this.mCommentListAdapter.notifyDataSetChanged();
	}

	public void addLastListView(ArrayList<HelpCommentListEntity> paramArrayList) {
		if (paramArrayList == null || this.mCommentListAdapter == null)
			return;

		for (int i = 0; i < paramArrayList.size(); i++)
			this.mCommentListAdapter.addLast(paramArrayList.get(i));
		this.mCommentListAdapter.notifyDataSetChanged();
	}

	public void freshGridView(ArrayList<HelpImageListEntity> paramArrayList) {
		if (paramArrayList == null || this.mImageGridAdapter == null)
			return;

		this.mImageGridAdapter.removeAll();
		for (int i = 0; i < paramArrayList.size(); i++)
			this.mImageGridAdapter.addObject(paramArrayList.get(i));
		this.mImageGridAdapter.notifyDataSetChanged();
	}
}