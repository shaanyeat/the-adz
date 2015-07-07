package com.lockscreen.adapter;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.lockscreen.R;
import com.lockscreen.WebViewActivity;
import com.lockscreen.fragment.LoginFragment;

public class DefaultLandingAdapter extends PagerAdapter {

	private Activity activity;
	private ArrayList<Integer> _imagePaths;
	private LayoutInflater inflater;
	int[] image;

	public DefaultLandingAdapter(Activity activity,
			ArrayList<Integer> imagePaths) {
		this.activity = activity;
		this._imagePaths = imagePaths;
	}

	public DefaultLandingAdapter(Activity activity, int[] imagePaths) {
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
	public Object instantiateItem(ViewGroup container, int position) {
		ImageView imgDisplay;

		inflater = (LayoutInflater) activity
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View itemView = inflater.inflate(R.layout.adapter_deal_gallery,
				container, false);

		imgDisplay = (ImageView) itemView.findViewById(R.id.imgDisplay);

		imgDisplay.setImageResource(_imagePaths.get(position));
		((ViewPager) container).addView(itemView);

		imgDisplay.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

				Intent intent = new Intent(activity, LoginFragment.class);
				activity.startActivity(intent);
				activity.finish();

			}
		});

		return itemView;
	}

	@Override
	public void destroyItem(ViewGroup container, int position, Object object) {
		((ViewPager) container).removeView((LinearLayout) object);

	}
}