package com.youxia.entity;

import java.io.Serializable;

public class HelpEntity implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = -1;
	
	public int 		helpid;
	public int 		categoryid;
	public String 	name;		
	public int 		userid;		
	public int 		area;	
	public double	longitude;
	public double 	latitude;		/*维度*/
	public String	site;			/*位置*/	
		
	public Byte 	help_flag;		/*救助标志*/
	public int  	help_userid;	/*救助人id*/
	public String	help_username;	/*救助人名称*/
	public String 	help_date;  	/*救助日期*/
	
	public int 		reward_points;	/*悬赏分*/
	public Byte		is_solve;		/*是否解决*/
		
	public int 		viewcount;		/*浏览次数*/
	public int		commentcount;	/*评论次数*/ 
		
	public String  	create_date;	/*创建日期*/	  
}
