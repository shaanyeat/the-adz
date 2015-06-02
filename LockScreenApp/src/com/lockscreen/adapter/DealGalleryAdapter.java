package com.lockscreen.adapter;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.lockscreen.LockScreenAppActivity;
import com.lockscreen.R;
import com.lockscreen.WebViewActivity;
import com.lockscreen.application.RegisterActivity;
import com.lockscreen.fragment.LoginFragment;

public class DealGalleryAdapter extends PagerAdapter {

	private Activity activity;
	private ArrayList<CampaignItem> _imagePaths;
	private ArrayList<CampaignItem> _imagelink;
	private LayoutInflater inflater;
	private ArrayList<CampaignItem> mItems;
	Boolean dummyflag = false;
	int[] flag;

	// constructor
	public DealGalleryAdapter(Activity activity, ArrayList<CampaignItem> imagePaths, Boolean dummyflag) {
		this.activity = activity;
		this.mItems = imagePaths;
		this.dummyflag = dummyflag;
	}
	
	//campaignItem Constructor
	public DealGalleryAdapter(Activity activity, ArrayList<CampaignItem> items) {
		this.activity = activity;
		this.mItems = items;
	}

	public DealGalleryAdapter(Activity activity, int[] imagePaths) {
		this.activity = activity;
		this.flag = imagePaths;
	}

	@Override
	public int getCount() {
		return mItems.size();
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
		
		final CampaignItem item = mItems.get(position);
		

		imgDisplay.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				// Toast.makeText(activity,
				// LockScreenAppActivity.subImg.get(position),
				// Toast.LENGTH_LONG).show();
				if (dummyflag == false) {
					Intent intent = new Intent(activity, WebViewActivity.class);
					intent.putExtra("imageurl",
							item.linkUrl);
					activity.startActivity(intent);
				}else{
					Intent intent = new Intent(activity, LoginFragment.class);
					activity.startActivity(intent);
				}

			}
		});

		try {

			LockScreenAppActivity.imageLoader.DisplayImage(item.subImgUrl, 0, imgDisplay);
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