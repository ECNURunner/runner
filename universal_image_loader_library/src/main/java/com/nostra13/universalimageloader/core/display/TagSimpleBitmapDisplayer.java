package com.nostra13.universalimageloader.core.display;

import android.graphics.Bitmap;
import android.view.View;

import com.nostra13.universalimageloader.core.assist.LoadedFrom;
import com.nostra13.universalimageloader.core.imageaware.ImageAware;

public class TagSimpleBitmapDisplayer extends SimpleBitmapDisplayer {

	private String url;

	public TagSimpleBitmapDisplayer(String url) {
		this.url = url;
	}

	@Override
	public void display(Bitmap bitmap, ImageAware imageAware,
			LoadedFrom loadedFrom) {
		View view = imageAware.getWrappedView();
		if (view == null) {
			return;
		}
		String tagUrl = (String) view.getTag();
		if (tagUrl == null) {
			return;
		}
		if (tagUrl.equals(url)) {
			super.display(bitmap, imageAware, loadedFrom);
		}
	}
}
