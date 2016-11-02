package com.youxia.entity;

import java.io.Serializable;

public class RoadRescueDetailCommentListEntity implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 6375767515696936257L;
	
	public String   userPhoto; //头像
	public String   commentNickName; //评论者昵称
	public String  	createDate;		/*距当前时间*/	
	public String  	commentContent;		/*评论内容*/
	public boolean  sex;/*性别*/	
}
