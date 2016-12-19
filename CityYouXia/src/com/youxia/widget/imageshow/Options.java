package com.youxia.widget.imageshow;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;

import android.graphics.Bitmap;

public class Options {
	/** 新闻列表中用到的图片加载配置 */
	public static DisplayImageOptions getListOptions() {
		DisplayImageOptions options = new DisplayImageOptions.Builder()
				// 设置下载的图片是否缓存在内存中
				.cacheInMemory(true)
				// 设置下载的图片是否缓存在SD卡中
				.cacheOnDisk(true)
				.considerExifParams(true)
				// 设置图片以如何的编码方式显示
				.imageScaleType(ImageScaleType.EXACTLY_STRETCHED)
				// 设置图片的解码类型
				.bitmapConfig(Bitmap.Config.RGB_565)
				// 设置图片在下载前是否重置，复位
				.resetViewBeforeLoading(false)
				.build();
		return options;
	}
}
