package com.lockscreen.application;

/*Developer: TAI ZHEN KAI
Project 2015*/

import android.app.ActionBar;
import android.app.ActionBar.Tab;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.Typeface;
import android.location.Location;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.GoogleApiClient.ConnectionCallbacks;
import com.google.android.gms.common.api.GoogleApiClient.OnConnectionFailedListener;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.lockscreen.LockScreenAppActivity;
import com.lockscreen.R;
import com.lockscreen.fragment.LoginFragment;
import com.lockscreen.tabsswipe.adapter.TabsPagerAdapter;
import com.lockscreen.utility.Constant;
import com.lockscreen.utility.SharedPreference;

public class HomeActivity extends FragmentActivity implements
		ActionBar.TabListener, ConnectionCallbacks, OnConnectionFailedListener,
		LocationListener {

	private ViewPager viewPager;
	private TabsPagerAdapter mAdapter;
	private ActionBar actionBar;
	private SharedPreference pref;
	// Tab titles
	private String[] tabs = { "Home", "Campaign", "Rewards", "History" };
	private int[] tabsIcon = { R.drawable.home, R.drawable.campaign,
			R.drawable.records, R.drawable.ranking };

	private final static int PLAY_SERVICES_RESOLUTION_REQUEST = 1000;
	// Location updates intervals in sec
	private static int UPDATE_INTERVAL = 10000; // 10 sec
	private static int FATEST_INTERVAL = 5000; // 5 sec
	private static int DISPLACEMENT = 10; // 10 meters
	// Google client to interact with Google API
	private GoogleApiClient mGoogleApiClient;
	private LocationRequest mLocationRequest;

	// boolean flag to toggle periodic location updates
	private boolean mRequestingLocationUpdates = false;

	private Location mLastLocation;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		pref = new SharedPreference(this);
		// get login user details
		if (pref.GetLogin()) {
			pref.getUserDetails();
		} else {
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

			// set firsttime pref to false
			Editor editor = prefs.edit();
			editor.putBoolean("firsttime", false);
			editor.commit();
		}

		// First we need to check availability of play services
		if (checkPlayServices()) {

			// Building the GoogleApi client
			buildGoogleApiClient();

			createLocationRequest();
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

		actionBar.setIcon(R.drawable.logo_white);

		// ActionBar.Tab tab = this.actionBar.newTab ();
		// tab.setCustomView(R.layout.tab_layout);

		// Adding Tabs
		for (Integer tab_name : tabsIcon) {
			actionBar.addTab(actionBar.newTab().setIcon(tab_name)
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

		// getMenuInflater().inflate(R.menu.global, menu);// Menu Resource, Menu

		TextView tv = new TextView(this);
		if(pref.GetLogin()){
			tv.setText(Constant.currentLoginUser.getFirstName());
		}
		tv.setTextColor(getResources().getColor(R.color.white));
		tv.setPadding(40, 10, 40, 10);
		tv.setBackgroundDrawable(getResources().getDrawable(R.drawable.actionbar_button_style));
		tv.setTypeface(null, Typeface.BOLD);
		tv.setTextSize(18);
		menu.add(0, 0, 1, R.string.navigation_drawer_open).setActionView(tv)
				.setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
		tv.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

				Intent intent = new Intent(HomeActivity.this, ProfileActivity.class);
				startActivity(intent);
				HomeActivity.this.finish();
			}
		});

		/*
		 * MenuItem logOut = (MenuItem) menu.findItem(R.id.action_logout); if
		 * (!pref.GetLogin()) { logOut.setVisible(false); } else {
		 * logOut.setVisible(true); }
		 */
		return true;
	}

	/*
	 * @Override public boolean onPrepareOptionsMenu(Menu menu) {
	 * super.onPrepareOptionsMenu(menu); MenuItem logOut = (MenuItem)
	 * menu.findItem(R.id.action_logout); if (!pref.GetLogin()) {
	 * logOut.setVisible(false); } else { logOut.setVisible(true); } return
	 * true; }
	 */
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
	public void onBackPressed() {
		pref.isLogin(true);

		super.onBackPressed();

	}

	/**
	 * Method to verify google play services on the device
	 * */
	private boolean checkPlayServices() {
		int resultCode = GooglePlayServicesUtil
				.isGooglePlayServicesAvailable(this);
		if (resultCode != ConnectionResult.SUCCESS) {
			if (GooglePlayServicesUtil.isUserRecoverableError(resultCode)) {
				GooglePlayServicesUtil.getErrorDialog(resultCode, this,
						PLAY_SERVICES_RESOLUTION_REQUEST).show();
			} else {
				Toast.makeText(getApplicationContext(),
						"This device is not supported.", Toast.LENGTH_LONG)
						.show();
				finish();
			}
			return false;
		}
		return true;
	}

	/**
	 * Creating google api client object
	 * */
	protected synchronized void buildGoogleApiClient() {
		mGoogleApiClient = new GoogleApiClient.Builder(this)
				.addConnectionCallbacks(this)
				.addOnConnectionFailedListener(this)
				.addApi(LocationServices.API).build();
	}

	/**
	 * Creating location request object
	 * */
	protected void createLocationRequest() {
		mLocationRequest = new LocationRequest();
		mLocationRequest.setInterval(UPDATE_INTERVAL);
		mLocationRequest.setFastestInterval(FATEST_INTERVAL);
		mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
		mLocationRequest.setSmallestDisplacement(DISPLACEMENT);
	}

	/**
	 * Method to display the location on UI
	 * */
	private void displayLocation() {

		mLastLocation = LocationServices.FusedLocationApi
				.getLastLocation(mGoogleApiClient);

		if (mLastLocation != null) {
			double latitude = mLastLocation.getLatitude();
			double longitude = mLastLocation.getLongitude();

			// lblLocation.setText(latitude + ", " + longitude);
			// Toast.makeText(this, latitude + ", " + longitude,
			// Toast.LENGTH_LONG)
			// .show();

			pref.setCoordinates(Double.toString(latitude),
					Double.toString(longitude));

		} else {
			Toast.makeText(
					this,
					"Couldn't get the location. Make sure location is enabled on the device",
					Toast.LENGTH_LONG).show();
			// lblLocation.setText("(Couldn't get the location. Make sure location is enabled on the device)");
		}
	}

	/**
	 * Starting the location updates
	 * */
	protected void startLocationUpdates() {

		LocationServices.FusedLocationApi.requestLocationUpdates(
				mGoogleApiClient, mLocationRequest, this);

	}

	/**
	 * Stopping location updates
	 */
	protected void stopLocationUpdates() {
		LocationServices.FusedLocationApi.removeLocationUpdates(
				mGoogleApiClient, this);
	}

	@Override
	protected void onStart() {
		super.onStart();
		if (mGoogleApiClient != null) {
			mGoogleApiClient.connect();
		}
	}

	@Override
	protected void onResume() {
		super.onResume();

		checkPlayServices();

		// Resuming the periodic location updates
		if (mGoogleApiClient.isConnected() && mRequestingLocationUpdates) {
			startLocationUpdates();
		}
	}

	@Override
	protected void onStop() {
		super.onStop();
		if (mGoogleApiClient.isConnected()) {
			mGoogleApiClient.disconnect();
		}
	}

	@Override
	protected void onPause() {
		super.onPause();
		stopLocationUpdates();
	}

	/**
	 * Google api callback methods
	 */
	@Override
	public void onConnectionFailed(ConnectionResult result) {
		Log.i(HomeActivity.class.getSimpleName(),
				"Connection failed: ConnectionResult.getErrorCode() = "
						+ result.getErrorCode());
	}

	@Override
	public void onConnected(Bundle arg0) {

		// Once connected with google api, get the location
		displayLocation();

		if (mRequestingLocationUpdates) {
			startLocationUpdates();
		}
	}

	@Override
	public void onConnectionSuspended(int arg0) {
		mGoogleApiClient.connect();
	}

	@Override
	public void onLocationChanged(Location location) {
		// Assign the new location
		mLastLocation = location;

		// Displaying the new location on UI
		displayLocation();
	}
}
