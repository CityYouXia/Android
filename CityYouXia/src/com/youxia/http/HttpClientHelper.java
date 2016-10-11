package com.youxia.http;

import net.tsz.afinal.FinalHttp;
import net.tsz.afinal.http.AjaxCallBack;
import net.tsz.afinal.http.AjaxParams;

public class HttpClientHelper {
	
	private static FinalHttp finalHttp = new FinalHttp();
	
	/**----------------------系统请求-------------------------------*/
	//登录 
	public static void loginSubmitInfo(String userName, AjaxCallBack<? extends Object> callBack)
	{
		AjaxParams ap = new AjaxParams();
		ap.put("name", "lixiaonan");
		finalHttp.get("www.baidu.com", ap, callBack);
	}
}