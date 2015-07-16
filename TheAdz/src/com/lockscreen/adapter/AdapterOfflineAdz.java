package com.lockscreen.adapter;

/*Developer: TAI ZHEN KAI
Project 2015*/

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.lockscreen.LockScreenAppActivity;
import com.lockscreen.R;
import com.nostra13.universalimageloader.core.DisplayImageOptions;

public class AdapterOfflineAdz extends PagerAdapter {
	 
    private Activity activity;
    private ArrayList<String> _imagePaths;
    private LayoutInflater inflater;
    int[] image;
    DisplayImageOptions options;
    
    public AdapterOfflineAdz(Activity activity, ArrayList<String> items){
        this.activity = activity;
        this._imagePaths = items;
        
        options = new DisplayImageOptions.Builder()
					.cacheInMemory(false)
					.cacheOnDisk(false)
					.considerExifParams(false)
					.bitmapConfig(Bitmap.Config.RGB_565)
					.build();
    }
    
    public AdapterOfflineAdz(Activity activity, int[] imagePaths) {
        this.activity = activity;
        this.image = imagePaths;
    }
	@Override
    public int getCount() {
        return this._imagePaths.size();
    }
 
	@Override
	public boolean isViewFromObject(View view, Object object) {
		return view == ((RelativeLayout) object);
	}
     
    @Override
	public Object instantiateItem(ViewGroup container, final int position) {
		ImageView imgDisplay;

		inflater = (LayoutInflater) activity
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View itemView = inflater.inflate(R.layout.adapter_deal_gallery,
				container, false);

		imgDisplay = (ImageView) itemView.findViewById(R.id.imgDisplay);
	
		try {

			LockScreenAppActivity.imageLoader.DisplayImage(_imagePaths.get(position), 0, imgDisplay);
			// sleep(3000);
		} catch (Exception e) {
			e.printStackTrace();
		}

		((ViewPager) container).addView(itemView);

		return itemView;
	}
     
    @Override
	public void destroyItem(ViewGroup container, int position, Object object) {
		((ViewPager) container).removeView((RelativeLayout) object);

	}
}