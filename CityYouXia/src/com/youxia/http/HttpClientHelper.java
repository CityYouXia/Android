package com.youxia.http;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.List;

import com.youxia.utils.YouXiaApp;

import net.tsz.afinal.FinalHttp;
import net.tsz.afinal.http.AjaxCallBack;
import net.tsz.afinal.http.AjaxParams;

public class HttpClientHelper {
	
	//public static String Basic_YouXiaUrl 	= "http://www.youxia.com";		//游侠网
	
	public static String Basic_YouXiaUrl 	= "http://1597e1873r.iask.in:20773/CityYouXia";		//游侠网
	
	private static FinalHttp finalHttp = new FinalHttp();
	
	public static void initHttp(){
		finalHttp.configTimeout(30000);
	}
	
	/**----------------------系统请求-------------------------------*/
	//登录 
	public static void loginSubmitInfo(String userName, AjaxCallBack<? extends Object> callBack)
	{
		AjaxParams ap = new AjaxParams();
		ap.put("name", "lixiaonan");
		finalHttp.get("www.baidu.com", ap, callBack);
	}
	
	//加载道路救援列表
	public static void loadRoadRescues(int pageSize, int pageNo, AjaxCallBack<? extends Object> callBack)
	{
		AjaxParams param = new AjaxParams();
		param.put("pageSize", Integer.toString(pageSize));
		param.put("nowPage", Integer.toString(pageNo));
		
		finalHttp.get(Basic_YouXiaUrl + "/helpOper/queryRoadRescue.do", param, callBack);
	}
	
	//刷新道路救援列表
	public static void loadPullRefreshRoadRescues(long pullRefreshId, AjaxCallBack<? extends Object> callBack)
	{
		AjaxParams param = new AjaxParams();
		param.put("pullRefreshId", Long.toString(pullRefreshId));
		finalHttp.get(Basic_YouXiaUrl + "/helpOper/queryRoadRescue.do", param, callBack);
	}
	
	//加载道路救援详细信息
	public static void loadRoadRescueDetailById(int helpId, AjaxCallBack<? extends Object> callBack)
	{
		AjaxParams param = new AjaxParams();
		param.put("helpId", Integer.toString(helpId));
		finalHttp.get(Basic_YouXiaUrl + "/helpOper/queryHelpDetail.do", param, callBack);	
	}
	
	//我要求助
	public static void help(List<File> list, String title, String content, String location, String reward_points, AjaxCallBack<? extends Object> callBack)
	{
		try {
			AjaxParams param = new AjaxParams();
			//系统值
			param.put("userId", "2");
			param.put("area", "2");
			param.put("longitude", Double.toString(YouXiaApp.getmLongitude()));
			param.put("latitude", Double.toString(YouXiaApp.getmLatitude()));
			//参数值
			param.put("name", title);
			param.put("content", content);
			param.put("site", location);
			param.put("rewardPoints", reward_points);
			for (int i = 1; i < list.size() + 1; i++) {
				param.put("image" + i, list.get(i - 1));
			}
			finalHttp.post(Basic_YouXiaUrl + "/helpOper/addRoadRescueHelp.do", param, callBack);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}
	
	//添加评论
	public static void 	addHelpComment(int helpId, int userId, String content, AjaxCallBack<? extends Object> callBack)
	{
		AjaxParams param = new AjaxParams();
		
		param.put("helpId", Integer.toString(helpId));
		param.put("userId", Integer.toString(userId));
		param.put("content", content);
		finalHttp.get(Basic_YouXiaUrl + "/helpOper/addHelpComment.do", param, callBack);	
	}	
	
	//获取帮助信息图片
	public static void 	queryHelpImageList(int helpId, int nowPage, int pageSize, AjaxCallBack<? extends Object> callBack)
	{
		AjaxParams param = new AjaxParams();
		
		param.put("helpId", Integer.toString(helpId));
		param.put("nowPage", Integer.toString(nowPage));
		param.put("pageSize", Integer.toString(pageSize));
		finalHttp.get(Basic_YouXiaUrl + "/helpOper/queryHelpImageList.do", param, callBack);	
	}
	
	//加载评论列表
	public static void 	queryHelpCommentList(int helpId, int nowPage, int pageSize, AjaxCallBack<? extends Object> callBack)
	{
		AjaxParams param = new AjaxParams();
		
		param.put("helpId", Integer.toString(helpId));
		param.put("nowPage", Integer.toString(nowPage));
		param.put("pageSize", Integer.toString(pageSize));
		finalHttp.get(Basic_YouXiaUrl + "/helpOper/queryHelpCommentList.do", param, callBack);	
	}	
}