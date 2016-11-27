package com.youxia.utils;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.lang.reflect.Field;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.json.JSONObject;

import com.youxia.R;
import com.youxia.http.NetWorkHelper;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Point;
import android.graphics.Rect;
import android.os.Build;
import android.os.Build.VERSION;
import android.os.Environment;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.view.WindowManager;
import android.widget.Toast;
import cn.sharesdk.tencent.qq.QQ;
import cn.sharesdk.tencent.qzone.QZone;
import cn.sharesdk.wechat.friends.Wechat;
import cn.sharesdk.wechat.moments.WechatMoments;

public class YouXiaUtils {	
	//分享参数
	public static final int				SHARETEXT						=	0;						//文本
	public static final int				SHAREIMAGE						=	1;						//图片
	public static final int				SHAREWEBPAGE					=	2;						//网址
	public static final String			PLATFORMSHORTMESSAGE			=	"ShortMessage";			//短信
	public static final String			PLATFORMEMAIL					=	"E-Mail";				//邮件
	public static final String			PLATFORMQQ						=	QQ.NAME;				//QQ
	public static final String			PLATFORMQZONE					=	QZone.NAME;				//QQ空间
	public static final String			PLATFORMWECHAT					=	Wechat.NAME;			//微信好友
	public static final String			PLATFORMWECHATMOMENTS			=	WechatMoments.NAME;		//微信朋友圈
	public static final String			PLATFORMSAVE					=	"save";					//保存本地
	
	public static final int				PROBLEM_REPORT_PHOTO_MAXNUMBER	=	6;			//问题上报图片选择数量限制
	public static final int				PROBLEM_REPORT_CAMERA_RESULTCODE=	9999;		//问题上报照相result
	public static final String			PROBLEM_REPORT_PHOTO_NAME		=	"help_temp.png";
	
	//************************************************************************
	//以下是公用函数
	//************************************************************************
	public static int getDaysofMonth(int year,int month)
	{
		int judge = 0 ;
		int days  = 30;
		/*判断当年是否为闰年*/
		if ((year%4==0 && year%100!=0) || (year%100==0 && year%400==0))
			judge = 1;
		else 
			judge = 0;

		if (month==1 || month==3 || month==5 || month==7 || month==8 || month==10 || month==12)/*判断每月的天数*/
			days = 31;

		if (month==4||month==6||month==9||month==11)
			days = 30;

		if (month==2 && judge==0)    days = 28;
		if (month==2 && judge==1)    days = 29;

		return days;
	}
	
	@SuppressWarnings("deprecation")
	@SuppressLint("NewApi")
	public static int getScreenWidth(Context context) {
		WindowManager manager = (WindowManager)context.getSystemService(Context.WINDOW_SERVICE);
		Display display = manager.getDefaultDisplay();
		
		int width = 0;
		try {
			if (GetAndroidSDKVersion() < Build.VERSION_CODES.HONEYCOMB_MR2) {
				width = display.getWidth();
			}
			else {
				Point size = new Point();
				display.getSize(size);
				width = size.x;
			}
		}
		catch (Exception e) {
			Log.e(null, e.toString());
		}
		
		return width;
	}
	
	@SuppressWarnings("deprecation")
	@SuppressLint("NewApi")
	public static int getScreenHeight(Context context) {
		WindowManager manager = (WindowManager)context.getSystemService(Context.WINDOW_SERVICE);
		Display display = manager.getDefaultDisplay();
		
		int height = 0;
		try {
			if (GetAndroidSDKVersion() < Build.VERSION_CODES.HONEYCOMB_MR2) {
				height = display.getHeight();
			}
			else {
				Point size = new Point();
				display.getSize(size);
				height = size.y;
			}
		}
		catch (Exception e) {
			Log.e(null, e.toString());
		}
		
		return height;
	}
	
	public static float getScreenDensity(Context context) {
    	try {
    		DisplayMetrics dm = new DisplayMetrics();
	    	WindowManager manager = (WindowManager)context.getSystemService(Context.WINDOW_SERVICE);
	    	manager.getDefaultDisplay().getMetrics(dm);
	    	return dm.density;
    	} catch(Exception ex) {
    	
    	}
    	return 1.0f;
    }
	
	/**
	 * 根据手机分辨率从dp转成px
	 * @param context
	 * @param dpValue
	 * @return
	 */
	public static  int dip2px(Context context, float dpValue) {  
        final float scale = context.getResources().getDisplayMetrics().density;  
        return (int) (dpValue * scale + 0.5f);  
    }  
	  
    /** 
     * 根据手机的分辨率从 px(像素) 的单位 转成为 dp 
     */  
	public static  int px2dip(Context context, float pxValue) {  
        final float scale = context.getResources().getDisplayMetrics().density;  
        return (int) (pxValue / scale + 0.5f)-15;  
    }  
	
	public static int GetAndroidSDKVersion()
	{
		int sysVersion = VERSION.SDK_INT;
		return sysVersion;
	}
	
	/**
	 * 检测sdcard是否可用
	 * 
	 * @return true为可用，否则为不可用
	 */
	public static boolean sdCardIsAvailable() {
		String status = Environment.getExternalStorageState();
		if (!status.equals(Environment.MEDIA_MOUNTED))
			return false;
		return true;
	}
	
	/*
	 * 获取SD路径
	 */
	public static String getSDPath() {
		String sdDir = "";
		
		boolean sdCardExist = Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED);
		
		if (sdCardExist) {
			sdDir = Environment.getExternalStorageDirectory().getAbsolutePath();//获取根目录
		}
		
		return sdDir;
	}
	
	/**
	 * 判断文件存放文件的目录是否存在，同时对外提供接口
	 * @param path 存放文件目录
	 * @return
	 */
	public static boolean dirsExits(String path){
		File file = new File(path);
		return file.exists();
	}
	
	/**
	 * 创建指定的目录
	 * @param path 目录的路径
	 */
	public static void createDir(String path){
		File file = new File(path);
		file.mkdirs();
	}
	
	/*
	 * 写入调试日志
	 * 
	 */
	@SuppressLint("SimpleDateFormat")
	@SuppressWarnings("unused")
	public static void writeDebugLog(String log)
	{	
		if (log == null || log.equals("")) return;
		
		FileOutputStream fop = null;
		OutputStreamWriter writer = null;
		try {
			if (YouXiaUtils.sdCardIsAvailable()) {
            	File  parentpath 	= new File(YouXiaUtils.getSDPath() + "/YouXia");
            	File  logpath 		= new File(YouXiaUtils.getSDPath() + "/YouXia/" + "debug.log");
            	if (!parentpath.exists()) {
            		boolean ret = parentpath.mkdirs();
            	}
            	
            	if (!logpath.exists()) {
            		try {
            			logpath.createNewFile();
            		}
            		catch (IOException e){
            			e.printStackTrace();
            			return;
            		}
            	}
           
            	fop = new FileOutputStream(logpath, true);
			
				writer = new	OutputStreamWriter(fop, "UTF-8");
				String nowTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS").format(new Date());//当前时间
				String logcontext = "[" + nowTime + "] " + log + "\r\n";
				writer.write(logcontext);
				writer.flush();
				writer.close();
				
				fop.close();
			}
			else YouXiaUtils.writeDebugLog("日志写入--SD卡不可用");
		}
		catch (Exception e){
			e.printStackTrace();
		}
		finally {
			try {
				if (fop != null) fop.close();
				if (writer != null) writer.close(); 
			}
			catch (IOException e){
				e.printStackTrace();
			}
		}
	}
	
	@SuppressWarnings("unused")
	public static boolean savePic(Bitmap bitmap, String picName, String folderName) {
		FileOutputStream fos = null;
		try {			
			File  parentpath 	= new File(getSDPath() + "/YouXia/" + folderName);
        	File  picpath 		= new File(getSDPath() + "/YouXia/" + folderName + picName);
        	if (!parentpath.exists()) {
        		boolean ret = parentpath.mkdirs();
        	}
        	
        	if (!picpath.exists()) {
        		try {
        			picpath.createNewFile();
        		}
        		catch (IOException e){
        			e.printStackTrace();
        			return false;
        		}
        	}
        	
        	fos = new FileOutputStream(picpath);
        	
        	if(null != fos){
        		bitmap.compress(Bitmap.CompressFormat.PNG, 100, fos);
            	fos.flush();
            	fos.close();
        	}
        	
		} catch (Exception e) {			
			e.printStackTrace();
			return false;
		}
		return true;
	}
	
	//获取Android设备唯一标示
	public static String getAndroidUniqueCode(Context context)
	{
		String uniquecode = "";
		
		uniquecode = getImieStatus(context);
		
		if (uniquecode.equals("")) 
			uniquecode = getAndroidId(context);
		
		return uniquecode;
	}
	
	public static String getImieStatus(Context context) 
	{
		TelephonyManager tm = (TelephonyManager)context.getSystemService(Context.TELEPHONY_SERVICE);
		String deviceId 	= tm.getDeviceId();
	
		return deviceId;
	}
		
	public static String getAndroidId(Context context)
	{
		String androidId = android.provider.Settings.Secure.getString(context.getContentResolver(), android.provider.Settings.Secure.ANDROID_ID);
		return androidId;
	}
	
	/** 公共使用Toast函数*/
	private static Toast mToast	= null;
    public static void showToast(final Context context, final String msg, final int duration)
    {
    	if(mToast == null){
    		mToast = Toast.makeText(context, msg, Toast.LENGTH_SHORT);
    	}
    	else 
    	{
    		mToast.setText(msg);
    	}
    	mToast.show();
    }
    
    /**
     * 公共使用提示框函数(Alert)
     * */
//    public static void showAlert(Activity context, String msg){
//    	final PopupAlert mPopupAlert = new PopupAlert(context, msg);
//    	mPopupAlert.showAtLocation(context.getWindow().getDecorView(), Gravity.CENTER, 0, 0);
//    	mPopupAlert.setOnDismissListener(new OnDismissListener() {
//			@Override
//			public void onDismiss() {
//				
//			}
//		});
//    }
    
    /**
     * 公共使用提示框函数(Waiting)
     * */
//    private static PopupWaiting mPopupWaiting = null;
//    public static void showWaiting(Activity context, String msg){
//    	if(mPopupWaiting == null){
//    		mPopupWaiting = new PopupWaiting(context, msg);
//    	}
//    	mPopupWaiting.showAtLocation(context.getWindow().getDecorView(), Gravity.CENTER, 0, 0);
//    }
//    
//    public static void setWaiting(String msg){
//    	if(mPopupWaiting == null){
//    		return;
//    	}
//    	mPopupWaiting.initView(msg);
//    }
//    
//    public static void dissmissWaiting(){
//    	if(mPopupWaiting == null) return;
//    		mPopupWaiting.dismiss();
//    		mPopupWaiting = null;
//    }
    
    /** 获取系统状态栏高度
     *@return 返回状态栏高度的像素值 
     */
    public static int getStatusBarHeight(Activity context)
    {
    	 int statusBarHeight = 0;
         try {
             Class<?> c = Class.forName("com.android.internal.R$dimen");
             Object o = c.newInstance();
             Field field = c.getField("status_bar_height");
             int x = (Integer) field.get(o);
             statusBarHeight = context.getResources().getDimensionPixelSize(x);
         } catch (Exception e) {
             e.printStackTrace();
             Rect frame = new Rect();
             context.getWindow().getDecorView().getWindowVisibleDisplayFrame(frame);
             statusBarHeight = frame.top;
         }
         return statusBarHeight;
    }
    
    /** 公共函数-检验手机网络 */
    public static boolean netWorkStatusCheck(Context context){
    	//判断网络状态
  		int status = NetWorkHelper.getNetworkStatus(context);
  		if (status != NetWorkHelper.NETWORK_WIFI && status != NetWorkHelper.NETWORK_MOBIL) {
  			showToast(context, context.getResources().getString(R.string.common_network_none), Toast.LENGTH_LONG);
  			return false;
  		}
  		return true;
    }   
    
    /** 公共函数-处理带[]的JSON */
    public static String serialJSONString(String src)
    {
    	String destSrc = src.replace("\\", "");
    	destSrc = destSrc.substring(1, destSrc.length() - 1); 
    	
    	return destSrc;
    }
    
    /** 公共函数-处理获取城市名称JSON */
    public static void getCityNameFormJson(String src, String[] provinces, String[] citys, String[] districts)
    {
    	if (src == null || "".equals(src)) return ;
    	
    	try
    	{
    		String jsonString  = src.replace("renderReverse&&renderReverse", "");
    		jsonString = jsonString.substring(1, jsonString.length() - 1); 
    		
    		JSONObject jsonObject = new JSONObject(jsonString);
    		 
    		JSONObject addressComponent = jsonObject.getJSONObject("result").getJSONObject("addressComponent");
    		
    		provinces[0] = addressComponent.getString("province");
    		citys[0] = addressComponent.getString("city");
    		districts[0] = addressComponent.getString("district");
    	}
    	catch (Exception e)
    	{
    		e.printStackTrace();
    	}
    }
    
    /**公共函数-检验手机号码正确性 */
    public static boolean telphoneCheck(String phonenumber){
		Pattern pattern = Pattern.compile("((^((13[0-9])|(15[^4,\\D])|(18[0-9])|(17[0,6-8])|(14[5,7]))\\d{8}$)|(^0[1,2]\\d{1}-\\d{8}$)|(^0[3-9]\\d{2}-\\d{7,8}$))");
		Matcher matcher = pattern.matcher(phonenumber);
		return matcher.matches();
	}
    
    /**公共函数-检验用户名 */
    public static String usernameCheck(String username){
		if(YouXiaUtils.telphoneCheck(username)){
			username = username.substring(0, 3) + "****" + username.substring(7, 11);
		}
		return username;
	}
    
    public static DecimalFormat df0 = new DecimalFormat("#0");
   	public static DecimalFormat df1 = new DecimalFormat("#0.0");
   	public static DecimalFormat df2 = new DecimalFormat("#0.00");
   	public static DecimalFormat df3 = new DecimalFormat("#0.000");
   	
   	//double格式化函数
   	public static double getDF0Dot(double data)
   	{
   		return Double.parseDouble(df0.format(data));
   	}
   	
   	public static double getDF1Dot(double data)
   	{
   		return Double.parseDouble(df1.format(data));
   	}
   	
   	public static double getDF2Dot(double data)
   	{
   		return Double.parseDouble(df2.format(data));
   	}
   	
   	public static double getDF3Dot(double data)
   	{
   		return Double.parseDouble(df3.format(data));
   	}
   	
   	public static String getDF1DotString(double data)
	{
		return df1.format(data);
	}
	
	public static String getDF2DotString(double data)
	{
		return df2.format(data);
	}
	
	public static String getDF3DotString(double data)
	{
		return df3.format(data);
	}
	
	/**
	 * @author qjl
	 * 获得新闻列表小图片的名称
	 * */
	public static String obtainSmallImage(String imageUrl){
		String rtnStr = "";
		int position = imageUrl.lastIndexOf(".");
		if(!TextUtils.isEmpty(imageUrl)){
			StringBuffer stringBuffer = new StringBuffer(imageUrl);
			rtnStr = stringBuffer.insert(position, "_small").toString();
		}
		else rtnStr = imageUrl;
		
		return rtnStr;
	}
	
	/**
	 * @author qjl
	 * PopupWindow
	 * */
  	public static int getScreenHeight(DisplayMetrics dm, View mView){
  		int screenHeight = dm.heightPixels/2;
		screenHeight = dm.heightPixels-mView.getHeight();//弹出窗体的高度即为更改头像以下部分的屏幕的高度=屏幕高度-更改头像的高度，如果想往上移应该减小屏幕高度
  		return screenHeight; 
  	}	
	
    /**
     * 判断给定字符串是否空白串。 空白串是指由空格、制表符、回车符、换行符组成的字符串 若输入字符串为null或空字符串，返回true
     * 
     * @param input
     * @return boolean
     */
    public static boolean isEmpty(String input) {
        if (input == null || "".equals(input) || TextUtils.isEmpty(input)) {
            return true;
        }
        else {
        	return false;
        }
    }
  	
    public static boolean inputCheckEmpty(Context context, String input){
    	if (isEmpty(input)) {
    		showToast(context, context.getString(R.string.input_empty), 0);
			return true;
		}
    	return false;
    }
    
  	/**
  	 * JPush推送存储别名和标签
  	 * */
  	public static void setAlias(Context context, String aliasString) {
  		if(null == aliasString || TextUtils.isEmpty(aliasString)) return;
  	}
   	
	/**
	 * 图形比例压缩
	 * */
	public static Bitmap compressImageByProportion(String srcPath) {
        BitmapFactory.Options newOpts = new BitmapFactory.Options();
        //开始读入图片，此时把options.inJustDecodeBounds 设回true了
        newOpts.inJustDecodeBounds = true;
        Bitmap bitmap = BitmapFactory.decodeFile(srcPath,newOpts);//此时返回bm为空

        newOpts.inJustDecodeBounds = false;
        int w = newOpts.outWidth;
        int h = newOpts.outHeight;
        //现在主流手机比较多是800*480分辨率，所以高和宽我们设置为
        float hh = 800f;//这里设置高度为800f
        float ww = 480f;//这里设置宽度为480f
        //缩放比。由于是固定比例缩放，只用高或者宽其中一个数据进行计算即可
        int be = 1;//be=1表示不缩放
        if (w > h && w > ww) {//如果宽度大的话根据宽度固定大小缩放
            be = (int) (newOpts.outWidth / ww);
        } else if (w < h && h > hh) {//如果高度高的话根据宽度固定大小缩放
            be = (int) (newOpts.outHeight / hh);
        }
        if (be <= 0)
            be = 1;
        newOpts.inSampleSize = be;//设置缩放比例
        //重新读入图片，注意此时已经把options.inJustDecodeBounds 设回false了
        bitmap = BitmapFactory.decodeFile(srcPath, newOpts);
        return YouXiaUtils.compressImageByQuality(bitmap);//压缩好比例大小后再进行质量压缩
    }
	
	/**
	 * 图形质量压缩
	 * */
	public static Bitmap compressImageByQuality(Bitmap image) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        image.compress(Bitmap.CompressFormat.JPEG, 100, baos);//质量压缩方法，这里100表示不压缩，把压缩后的数据存放到baos中
        int options = 100;
        while ( baos.toByteArray().length / 1024>100) {    //循环判断如果压缩后图片是否大于100kb,大于继续压缩        
            baos.reset();//重置baos即清空baos
            options -= 10;//每次都减少10
            image.compress(Bitmap.CompressFormat.JPEG, options, baos);//这里压缩options%，把压缩后的数据存放到baos中

        }
        ByteArrayInputStream isBm = new ByteArrayInputStream(baos.toByteArray());//把压缩后的数据baos存放到ByteArrayInputStream中
        Bitmap bitmap = BitmapFactory.decodeStream(isBm, null, null);//把ByteArrayInputStream数据生成图片
        return bitmap;
    }
	
	/**
	 * 把显示的数值改为#,###,###,###格式的数量
	 * */
	public static String formatNumber(Double value){
		DecimalFormat df = new DecimalFormat("#,###,###,###");
		return df.format(value).toString();
	}
	/**
	 * 把显示的数值改为#,###,###,###.##格式的数量
	 * */
	public static String formatFloatNumber(Double value){
		DecimalFormat df = new DecimalFormat("#,###,###,##0.00");
		return df.format(value).toString();
	}
	
	/**
     * 字符串转Byte
     * 
     * @param str
     * @param defValue
     * @return
     */
    public static Byte toByte(String str, Byte defValue) {
        try {
            return Byte.parseByte(str);
        } catch (Exception e) {
        }
        return defValue;
    }	
	
	/**
     * 字符串转整数
     * 
     * @param str
     * @param defValue
     * @return
     */
    public static int toInt(String str, int defValue) {
        try {
            return Integer.parseInt(str);
        } catch (Exception e) {
        }
        return defValue;
    }
	
    /**
     * 对象转整数
     * 
     * @param obj
     * @return 转换异常返回 0
     */
    public static int toInt(Object obj) {
        if (obj == null)
            return 0;
        return toInt(obj.toString(), 0);
    }
    
    /**
     * 字符串转长整数
     * 
     * @param str
     * @return 转换异常返回 0
     */
    public static long toLong(String str, long defValue) {
        try {
            return Long.parseLong(str);
        } catch (Exception e) {
        }
        return defValue;
    }

    /**
     * 字符串转Double
     * 
     * @param str
     * @return 转换异常返回 defValue
     */
    public static double toDouble(String str, double defValue) {
        try {
            return Double.parseDouble(str);
        } catch (Exception e) {
        }
        return defValue;
    }    
    
    /**
     * 字符串转布尔值
     * 
     * @param b
     * @return 转换异常返回 false
     */
    public static boolean toBool(String b, boolean defValue) {
        try {
            return Boolean.parseBoolean(b);
        } catch (Exception e) {
        }
        return defValue;
    }
}
