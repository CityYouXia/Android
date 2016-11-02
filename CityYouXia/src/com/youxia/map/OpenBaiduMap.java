package com.youxia.map;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;

import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.utils.OpenClientUtil;
import com.baidu.mapapi.utils.route.BaiduMapRoutePlan;
import com.baidu.mapapi.utils.route.RouteParaOption;

public class OpenBaiduMap {
	/**
	 * 启动百度地图驾车路线规划,手机上有百度地图，则打开地图。没有则使用浏览器访问手机web版百度地图 。
	 * start_lat:起点维度坐标,start_lng:起点经度坐标,startName:起点名称
	 * end_lat:终点维度坐标,end_lng:终点经度坐标,endName:终点名称
	 * 
	 * */
	public void startRoutePlanDriving(double start_lat, double start_lng, 
			double end_lat, double end_lng, String startName, String endName, Context context){
		LatLng start_point = new LatLng(start_lat, start_lng);
		LatLng end_point = new LatLng(end_lat, end_lng);
		
		RouteParaOption para = new RouteParaOption()
			.startPoint(start_point)
			.endPoint(end_point);
		
		//默认起点名称
		if(startName == null || startName.length() == 0){
			startName = "当前位置";
		}
		
		//默认重点名称
		if(endName == null || endName.length() == 0){
			endName = "目的地";
		}
		
		para.startName(startName).endName(endName);
		
		try {
			BaiduMapRoutePlan.openBaiduMapDrivingRoute(para, context);
		} catch(Exception e){
			e.printStackTrace();
			showDialog(context);
		}
	}

	/**
	 * 提示未安装百度地图app或app版本过低
	 * 
	 */
	public void showDialog(Context context) {
		final Context e_context = context;
		AlertDialog.Builder builder = new AlertDialog.Builder(context);
		builder.setMessage("您尚未安装百度地图app或app版本过低，点击确认安装？");
		builder.setTitle("提示");
		builder.setPositiveButton("确认", new OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
				OpenClientUtil.getLatestBaiduMapApp(e_context);
			}
		});

		builder.setNegativeButton("取消", new OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
			}
		});

		builder.create().show();
	}
	
	
}
