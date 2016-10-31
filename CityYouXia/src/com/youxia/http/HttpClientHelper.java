package com.youxia.http;

import net.tsz.afinal.FinalHttp;
import net.tsz.afinal.http.AjaxCallBack;
import net.tsz.afinal.http.AjaxParams;

public class HttpClientHelper {
	
	//public static String Basic_YouXiaUrl 	= "http://www.youxia.com";		//游侠网
	
	public static String Basic_YouXiaUrl 	= "http://1597e1873r.iask.in:20773";		//游侠网
	
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
}