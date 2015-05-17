package com.lockscreen.application;

import android.app.ActionBar;
import android.app.ActionBar.Tab;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.view.Menu;
import android.view.MenuItem;

import com.lockscreen.LockScreenAppActivity;
import com.lockscreen.R;
import com.lockscreen.fragment.LoginFragment;
import com.lockscreen.tabsswipe.adapter.TabsPagerAdapter;
import com.lockscreen.utility.SharedPreference;

public class HomeActivity extends FragmentActivity implements ActionBar.TabListener {

	private ViewPager viewPager;
	private TabsPagerAdapter mAdapter;
	private ActionBar actionBar;
	private SharedPreference pref;
	// Tab titles
	private String[] tabs = { "Home", "Campaign", "Records", "Ranking" };

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		pref = new SharedPreference(this);
		//get login user details
		if (pref.GetLogin()) {
			pref.getUserDetails();
		}else{
			Intent i = new Intent(HomeActivity.this, LoginFragment.class);
			startActivity(i);
			HomeActivity.this.finish();
		}
		

		SharedPreferences prefs = PreferenceManager
				.getDefaultSharedPreferences(this);

		// RUN THE LOCK SCREEN ONE TIMES ONLY ONCE THE APP INSTALLED
		if (prefs.getBoolean("firsttime", true)) {
			Intent intent = new Intent(HomeActivity.this,
					LockScreenAppActivity.class);
			startActivity(intent);

			// set fristtime pref to false
			Editor editor = prefs.edit();
			editor.putBoolean("firsttime", false);
			editor.commit();
		}

		// Initilization
		viewPager = (ViewPager) findViewById(R.id.pager);
		actionBar = getActionBar();
		mAdapter = new TabsPagerAdapter(getSupportFragmentManager());

		viewPager.setAdapter(mAdapter);
		actionBar.setHomeButtonEnabled(false);
		actionBar.setDisplayShowTitleEnabled(false);
		actionBar.setDisplayHomeAsUpEnabled(false);
		actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);

		// Adding Tabs
		for (String tab_name : tabs) {
			actionBar.addTab(actionBar.newTab().setText(tab_name)
					.setTabListener(this));
		}

		/**
		 * on swiping the viewpager make respective tab selected
		 * */
		viewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {

			@Override
			public void onPageSelected(int position) {
				// on changing the page
				// make respected tab selected
				actionBar.setSelectedNavigationItem(position);
			}

			@Override
			public void onPageScrolled(int arg0, float arg1, int arg2) {
			}

			@Override
			public void onPageScrollStateChanged(int arg0) {
			}
		});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		getMenuInflater().inflate(R.menu.global, menu);// Menu Resource, Menu
		
		MenuItem logOut = (MenuItem) menu.findItem(R.id.action_logout);
	    if (!pref.GetLogin()) {
			logOut.setVisible(false);
		}else{
			logOut.setVisible(true);
		}
		return true;
	}
	
	@Override
	public boolean onPrepareOptionsMenu(Menu menu) {
	    super.onPrepareOptionsMenu(menu);
	    MenuItem logOut = (MenuItem) menu.findItem(R.id.action_logout);
	    if (!pref.GetLogin()) {
			logOut.setVisible(false);
		}else{
			logOut.setVisible(true);
		}
	    return true;
	}
	

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.action_account:
			Intent i = new Intent(HomeActivity.this, LoginFragment.class);
			startActivity(i);
			HomeActivity.this.finish();
			return true;
		case R.id.action_settings:
			return true;
		case R.id.action_logout:
			pref.logoutUser();			
			Intent x = new Intent(HomeActivity.this, LoginFragment.class);
			startActivity(x);
			HomeActivity.this.finish();
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}
	

	@Override
	public void onTabReselected(Tab tab, FragmentTransaction ft) {
	}

	@Override
	public void onTabSelected(Tab tab, FragmentTransaction ft) {
		// on tab selected
		// show respected fragment view
		viewPager.setCurrentItem(tab.getPosition());
	}

	@Override
	public void onTabUnselected(Tab tab, FragmentTransaction ft) {
	}

	
	@Override
	public void onBackPressed(){
		pref.isLogin(true);
		
		super.onBackPressed();

	}
}
