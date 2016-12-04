package com.youxia.adapter;

import com.youxia.BaseLinkedListAdapter;
import com.youxia.R;
import com.youxia.entity.HelpCommentListEntity;
import com.youxia.http.HttpClientHelper;
import com.youxia.utils.YouXiaApp;
import com.youxia.utils.YouXiaUtils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

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

		HelpCommentListEntity localData = (HelpCommentListEntity) getItem(position);

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
		if (YouXiaUtils.isEmpty(localData.commentUserPhoto)) {
			hold.ivHeadPhoto.setImageResource((localData.sex == true) ? R.drawable.male_little_default : R.drawable.female_little_default);
		}
		else {
			Bitmap bitmap = BitmapFactory.decodeResource(this.context.getResources(), (localData.sex == true) ? R.drawable.male_little_default : R.drawable.female_little_default);
			YouXiaApp.mFinalBitmap.display(hold.ivHeadPhoto,
					HttpClientHelper.Basic_YouXiaUrl + localData.commentUserPhoto, bitmap);
		}
		hold.tvFloor.setText(position + 1 + "楼");
		return convertView;
	}
}
