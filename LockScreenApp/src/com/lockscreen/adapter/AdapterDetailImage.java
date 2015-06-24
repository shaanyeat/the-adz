package com.lockscreen.adapter;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.lockscreen.R;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

public class AdapterDetailImage extends PagerAdapter {
	 
    private Activity activity;
    private ArrayList<String> _imagePaths;
    private LayoutInflater inflater;
    int[] image;
    DisplayImageOptions options;
    
    public AdapterDetailImage(Activity activity, ArrayList<String> imagePaths){
        this.activity = activity;
        this._imagePaths = imagePaths;
        
        options = new DisplayImageOptions.Builder()
					.cacheInMemory(true)
					.cacheOnDisk(true)
					.considerExifParams(true)
					.bitmapConfig(Bitmap.Config.RGB_565)
					.build();
    }
    
    public AdapterDetailImage(Activity activity, int[] imagePaths) {
        this.activity = activity;
        this.image = imagePaths;
    }
	@Override
    public int getCount() {
        return this._imagePaths.size();
    }
 
    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == ((LinearLayout) object);
    }
     
    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        ImageView imgDisplay;
  
        inflater = (LayoutInflater) activity
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View itemView = inflater.inflate(R.layout.adapter_details_image, container,
                false);
  
        imgDisplay = (ImageView) itemView.findViewById(R.id.imgDisplay);
        
        ImageLoader imageLoader = ImageLoader.getInstance();
		imageLoader.init(ImageLoaderConfiguration.createDefault(activity));
		
		Log.v("IMAGE URL", _imagePaths.get(position));
        
		imageLoader.displayImage(_imagePaths.get(position), imgDisplay, options);
//		imgDisplay.setImageDrawable(getResources().getDrawable(image.get(position));
//        imgDisplay.setImageResource(_imagePaths.get(position));
        ((ViewPager) container).addView(itemView);
  
        return itemView;
    }
     
    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        ((ViewPager) container).removeView((LinearLayout) object);
  
    }
}