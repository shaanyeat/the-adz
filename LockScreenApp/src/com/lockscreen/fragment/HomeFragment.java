package com.lockscreen.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;

import com.lockscreen.LockScreenAppActivity;
import com.lockscreen.R;

@SuppressWarnings("deprecation")
public class HomeFragment extends Fragment{
	
	Button btnPreview;
	
	public HomeFragment() {
		// Empty constructor required for fragment subclasses
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.fragment_home, container, false);


		btnPreview = (Button) rootView.findViewById(R.id.btnPreview);
		btnPreview.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(getActivity(),LockScreenAppActivity.class);
				startActivity(intent);
			}
		});
		
		return rootView;

	}

}
