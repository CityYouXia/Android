package com.youxia.entity;

import java.io.Serializable;

public class PhotoEntity implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 4821733748029222071L;

	public String photoid;
	public String thumbnailPath;
	public String photoPath;
	public boolean isSelected = false;
}
