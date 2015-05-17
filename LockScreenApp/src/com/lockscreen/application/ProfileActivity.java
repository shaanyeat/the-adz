package com.lockscreen.application;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.widget.TextView;

import com.lockscreen.R;

public class ProfileActivity extends FragmentActivity{
	
	TextView userFirstName, userLastName, userEmail;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.fragment_profile);
		
		userFirstName = (TextView) findViewById(R.id.userFirstName);
		userLastName = (TextView) findViewById(R.id.userLastName);
		userEmail = (TextView) findViewById(R.id.userEmail);
		
	}

}
