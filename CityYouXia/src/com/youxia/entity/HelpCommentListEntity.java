package com.youxia.entity;

import java.io.Serializable;

public class HelpCommentListEntity implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 6375767515696936257L;
	public long		commentId;
	public String   helpId;
	public String   userId;
	public String   content;
	public String   createDate;
	public String   commentUserName;
	public String   commentUserPhoto;
	public boolean  sex;
}