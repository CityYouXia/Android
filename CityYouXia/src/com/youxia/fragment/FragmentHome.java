package com.youxia.fragment;

import net.tsz.afinal.FinalActivity;
import net.tsz.afinal.annotation.view.ViewInject;

import com.youxia.BaseFragment;
import com.youxia.R;
import com.youxia.activity.RoadRescueActivity;
import com.youxia.widget.NumImageView;
import com.youxia.widget.ViewPagerAdapter;
import com.youxia.widget.ViewPagerAdvert;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

@SuppressLint("InflateParams")
public class FragmentHome extends BaseFragment {
	
	public static final String TAG = FragmentHome.class.getSimpleName();

	@ViewInject(id=R.id.road_rescure,click="btnClick") 		NumImageView 	roadRescure;		//道路救援
	@ViewInject(id=R.id.move_car,click="btnClick") 			NumImageView 	moveCar;			//挪车
	@ViewInject(id=R.id.road_conditions,click="btnClick") 	NumImageView 	roadConditions;		//报路况
	@ViewInject(id=R.id.people_search,click="btnClick") 	NumImageView 	peopleSearch;		//寻人
	@ViewInject(id=R.id.things_search,click="btnClick") 	NumImageView 	thingsSearch;		//寻物
	@ViewInject(id=R.id.express,click="btnClick")			NumImageView 	express;			//顺风递
	@ViewInject(id=R.id.asking,click="btnClick") 			NumImageView 	asking;				//问询求助
	@ViewInject(id=R.id.need_help_btn,click="btnClick") 	Button 			needHelp;
	
	@ViewInject(id=R.id.fragment_homepage_advert) 			ViewPagerAdvert	viewPagerAdvert;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View root = inflater.inflate(R.layout.fragment_home, container, false);
		FinalActivity.initInjectedView(this,root);
		return root;
	}
	
	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);
		initView();
	}
	
	private void initView() {
		viewPagerAdvert.addALocalImageToViewPager("test.jpg", R.drawable.test, "美女1");
		viewPagerAdvert.addALocalImageToViewPager("test.jpg", R.drawable.test, "美女2");
		viewPagerAdvert.addALocalImageToViewPager("test.jpg", R.drawable.test, "美女3");
		viewPagerAdvert.addALocalImageToViewPager("test.jpg", R.drawable.test, "美女4");
		viewPagerAdvert.notifyViewPager();
		
		roadRescure.setNum(15);
		moveCar.setNum(5);
		roadConditions.setNum(8);
		peopleSearch.setNum(20);
		thingsSearch.setNum(13);
		express.setNum(1);
		asking.setNum(5);
	}
	
	public void btnClick(View v){
		Intent intent = new Intent();
		switch (v.getId()) {
		case R.id.road_rescure:
			intent.setClass(getActivity(), RoadRescueActivity.class);
			break;
		case R.id.move_car:
			break;
		case R.id.road_conditions:
			break;
		case R.id.people_search:
			break;
		case R.id.things_search:
			break;
		case R.id.express:
			break;
		case R.id.asking:
			break;
		case R.id.need_help_btn:
			break;
		}
		startActivity(intent);
	}
}
