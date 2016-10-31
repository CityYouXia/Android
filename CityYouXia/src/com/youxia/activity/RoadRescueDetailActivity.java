package com.youxia.activity;

import com.youxia.BaseActivity;
import com.youxia.R;
import com.youxia.widget.ListViewForScrollView;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import net.tsz.afinal.annotation.view.ViewInject;

public class RoadRescueDetailActivity extends BaseActivity {

	@ViewInject(id = R.id.title_bar_title)
	TextView mTitleBarTitle;
	@ViewInject(id = R.id.activity_road_rescue_detail_address)
	TextView mAddressTextView;
	@ViewInject(id = R.id.activity_road_rescue_detail_score)
	TextView mScoreTextView;
	@ViewInject(id = R.id.title_bar_back, click = "btnClick")
	RelativeLayout mTitleBarBack;
	@ViewInject(id = R.id.activity_road_rescue_detail_comment_list)
	ListViewForScrollView mCommentList;
	@ViewInject(id = R.id.activity_road_rescue_detail_rescue_button)
	Button mButtonRescue;
	@ViewInject(id = R.id.activity_road_rescue_detail_resoved_information)
	LinearLayout mResolveInformationLinearLayout;
	@ViewInject(id = R.id.activity_road_rescue_detail_user_portrait)
	ImageView mUserPortraitImageView;
	@ViewInject(id = R.id.activity_road_rescue_detail_status)
	ImageView mStatusImageView;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_road_rescue_detail);
		initView();
	}

	private void initView() {
		mTitleBarTitle.setText(getString(R.string.activity_road_rescue_detail_title));

		//？？？通过列表传递过来的id查询相关信息？？？
		isResolved(true);
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

}
