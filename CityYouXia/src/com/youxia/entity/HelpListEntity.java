package com.youxia.entity;

import java.io.Serializable;

public class HelpListEntity implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = -1;
	
	public int 		helpId;
	public int 		categoryId;
	public String   name;
	public String   content;
	
	public int 		userId;
	public String   createUserName;
	public String   createUserNickName;
	public String   userPhoto;
	public boolean  sex;
			
	public int      area;
	public double	longitude;
	public double 	latitude;		/*维度*/
	public String	site;			/*位置*/	

	public int 		rewardPoints;	/*悬赏分*/
	public int  	isSolve;		/*是否解决*/
		
	public int 		viewCount;		/*浏览次数*/
	public int		commentCount;	/*评论次数*/ 
	
	public String   helpPhotoUrl;
	public int      helpPhotoCount;
		
	public String  	createDate;		/*距当前时间*/	
}
