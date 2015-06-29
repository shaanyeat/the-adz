package com.lockscreen.application;

/*Developer: TAI ZHEN KAI
Project 2015*/

import android.app.ActionBar;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.Session;
import com.lockscreen.R;
import com.lockscreen.fragment.LoginFragment;
import com.lockscreen.utility.Constant;
import com.lockscreen.utility.SharedPreference;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

public class ProfileActivity extends FragmentActivity { 
	
	TextView logout, userPoints, userFullName;
	EditText firstName, lastName, email;
	ImageView profilePic;
	Button btnInviteFriends, btnInterest;
	
	ActionBar actionBar;
	private SharedPreference pref;
	DisplayImageOptions options;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.fragment_user_details);
		
		pref = new SharedPreference(this);
		
		actionBar = getActionBar();
		actionBar.setDisplayHomeAsUpEnabled(true);
		
		options = new DisplayImageOptions.Builder().cacheInMemory(true)
				.cacheOnDisk(true).considerExifParams(true)
				.bitmapConfig(Bitmap.Config.RGB_565).build();
		
		logout = (TextView) findViewById(R.id.logout);
		userPoints = (TextView) findViewById(R.id.userPoints);
		userFullName = (TextView) findViewById(R.id.userFullName);
		firstName = (EditText) findViewById(R.id.firstName);
		lastName = (EditText) findViewById(R.id.lastName);
		email = (EditText) findViewById(R.id.email);	
		profilePic  = (ImageView) findViewById(R.id.profilePic);
		btnInviteFriends = (Button) findViewById(R.id.btnInviteFriends);
		btnInterest = (Button) findViewById(R.id.btnInterest);
		
		//Set Value
		userPoints.setText(Constant.currentLoginUser.getPoints() + " Points");
		userFullName.setText(Constant.currentLoginUser.getFirstName() + " " + Constant.currentLoginUser.getLastName());
		firstName.setText(Constant.currentLoginUser.getFirstName());
		lastName.setText(Constant.currentLoginUser.getLastName());
		email.setText(Constant.currentLoginUser.getEmail());
		
		ImageLoader imageLoader = ImageLoader.getInstance();
		imageLoader.init(ImageLoaderConfiguration.createDefault(ProfileActivity.this));
		
		Log.v("Url",Constant.currentLoginUser.getimgUrl());
		
		imageLoader.displayImage(Constant.currentLoginUser.getimgUrl(),
				profilePic, options);
		
		logout.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				pref.logoutUser();
				LogoutFB(ProfileActivity.this);
				Intent x = new Intent(ProfileActivity.this, LoginFragment.class);
				startActivity(x);
				ProfileActivity.this.finish();
			}
		});
		
		btnInviteFriends.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Toast.makeText(ProfileActivity.this, "Coming Soon, Stay Tuned!", Toast.LENGTH_LONG).show();
			}
		});
		
		btnInterest.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Toast.makeText(ProfileActivity.this, "Coming Soon, Stay Tuned!", Toast.LENGTH_LONG).show();
			}
		});
		
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			Intent x = new Intent(ProfileActivity.this, HomeActivity.class);
			startActivity(x);
			ProfileActivity.this.finish();
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}
	
	public static void LogoutFB(Context context) {
	    Session session = Session.getActiveSession();
	    if (session != null) {

	        if (!session.isClosed()) {
	            session.closeAndClearTokenInformation();
	            //clear your preferences if saved
	        }
	    } else {

	        session = new Session(context);
	        Session.setActiveSession(session);

	        session.closeAndClearTokenInformation();
	            //clear your preferences if saved

	    }

	}
	
	@Override
	public void onBackPressed() {
		Intent x = new Intent(ProfileActivity.this, HomeActivity.class);
		startActivity(x);
		ProfileActivity.this.finish();
	}

}
