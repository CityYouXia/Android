package com.youxia.entity;

import java.io.Serializable;

public class HelpListEntity implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = -1;
	
	public int 		helpid;
	public int 		categoryid;
	public String   help_content;
	
	public int 		userid;
	public String   username;
	public String   userphoto;
	public boolean  sex;
			
	public double	longitude;
	public double 	latitude;		/*维度*/
	public String	site;			/*位置*/	

	public int 		reward_points;	/*悬赏分*/
	public boolean	is_solve;		/*是否解决*/
		
	public int 		viewcount;		/*浏览次数*/
	public int		commentcount;	/*评论次数*/ 
	
	public String   scenePhoto;
	public int      scenePhotoCount;
		
	public String  	from_time;		/*距当前时间*/	  
}
