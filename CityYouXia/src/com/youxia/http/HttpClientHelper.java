package com.youxia.http;

import net.tsz.afinal.FinalHttp;
import net.tsz.afinal.http.AjaxCallBack;
import net.tsz.afinal.http.AjaxParams;

public class HttpClientHelper {
	
	public static String Basic_YouXiaUrl 	= "http://www.youxia.com";		//游侠网
	
	private static FinalHttp finalHttp = new FinalHttp();
	
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
		param.put("pageNo", Integer.toString(pageNo));
		
		finalHttp.get(Basic_YouXiaUrl + "/helpOper/queryRoadHelpUnsolve.do", param, callBack);
	}

	//刷新道路救援列表
	public static void loadPullRefreshRoadRescues(long pullRefreshId, AjaxCallBack<? extends Object> callBack)
	{
		AjaxParams param = new AjaxParams();
		
		param.put("pullRefreshId", Long.toString(pullRefreshId));
		
		finalHttp.get(Basic_YouXiaUrl + "/helpOper/queryRoadHelpUnsolve.do", param, callBack);
	}
}