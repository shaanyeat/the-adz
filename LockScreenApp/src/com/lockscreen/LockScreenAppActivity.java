package com.lockscreen;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import android.app.Activity;
import android.app.KeyguardManager;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup.MarginLayoutParams;
import android.view.WindowManager;
import android.widget.DigitalClock;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.RelativeLayout.LayoutParams;
import android.widget.TextView;

import com.lockscreen.adapter.DealGalleryAdapter;
import com.lockscreen.loader.ImageLoader;

public class LockScreenAppActivity extends Activity {

	/** Called when the activity is first created. */
	KeyguardManager.KeyguardLock k1;
	boolean inDragMode;
	int selectedImageViewX;
	int selectedImageViewY;
	int windowwidth;
	int windowheight;
	ImageView droid, phone, home;
	int home_x, home_y;
	int[] droidpos;

	private LayoutParams layoutParams;

	ViewPager viewPager;
	public static ArrayList<String> subImg;
	public static ImageLoader imageLoader;
	TextView date;
	DigitalClock dc;

	
	//use to disable the home button
	/*@Override
	public void onAttachedToWindow() {
		// TODO Auto-generated method stub
		this.getWindow().setType(
				WindowManager.LayoutParams.TYPE_KEYGUARD_DIALOG
						| WindowManager.LayoutParams.FLAG_FULLSCREEN);

		super.onAttachedToWindow();
	}*/

	public void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		getWindow().addFlags(
		// WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON | // keep the screen on
				WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED);
//						| WindowManager.LayoutParams.FLAG_FULLSCREEN);

		setContentView(R.layout.main);

		dc = (DigitalClock) findViewById(R.id.digitalClock1);
		date = (TextView) findViewById(R.id.date);

		DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
		// get current date time with Date()
		Date currentDate = new Date();
		date.setText(dateFormat.format(currentDate));

		imageLoader = new ImageLoader(this);

		subImg = new ArrayList<String>();

		subImg.add("http://imageswiki.com/wp-content/uploads/2014/11/images-Photoshop-Image-of-the-horse-053857-.jpg");
		subImg.add("http://www.hdwallpapersimages.com/wp-content/uploads/2014/01/Winter-Tiger-Wild-Cat-Images.jpg");
		subImg.add("http://www.gettyimages.com/CMS/Pages/ImageCollection/StaticContent/image5_170127819.jpg");

		try {
			// Locate the ViewPager in viewpager_main.xml
			viewPager = (ViewPager) findViewById(R.id.pager);
			// Pass results to ViewPagerAdapter Class
			DealGalleryAdapter adapter = new DealGalleryAdapter(this, subImg);
			// Binds the Adapter to the ViewPager
			viewPager.setAdapter(adapter);

			// CirclePageIndicator Indicator =
			// (CirclePageIndicator)findViewById(R.id.indicator);
			// Indicator.bringToFront();
			// Indicator.setViewPager(viewPager);

			// if(subImg.size()<=1)
			// Indicator.setVisibility(View.GONE);
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}

		droid = (ImageView) findViewById(R.id.droid);

		System.out.println("measured width" + droid.getMeasuredWidth());
		System.out.println(" width" + droid.getWidth());

		if (getIntent() != null && getIntent().hasExtra("kill")
				&& getIntent().getExtras().getInt("kill") == 1) {
			// Toast.makeText(this, "" + "kill activityy",
			// Toast.LENGTH_SHORT).show();
			finish();
		}

		try {
			// initialize receiver

			startService(new Intent(this, MyService.class));

			StateListener phoneStateListener = new StateListener();
			TelephonyManager telephonyManager = (TelephonyManager) getSystemService(TELEPHONY_SERVICE);
			telephonyManager.listen(phoneStateListener,
					PhoneStateListener.LISTEN_CALL_STATE);

			windowwidth = getWindowManager().getDefaultDisplay().getWidth();
			System.out.println("windowwidth" + windowwidth);
			windowheight = getWindowManager().getDefaultDisplay().getHeight();
			System.out.println("windowheight" + windowheight);

			MarginLayoutParams marginParams2 = new MarginLayoutParams(
					droid.getLayoutParams());

			if(isTablet(this))
				marginParams2.setMargins(0, windowheight - 60, 0, 0); 
			else
				marginParams2.setMargins(0, (windowheight/32) * 28, 0, 0);

			// marginParams2.setMargins((windowwidth / 24) * 10,
			// ((windowheight / 32) * 15), 0, 0);

			// marginParams2.setMargins(((windowwidth-droid.getWidth())/2),((windowheight/32)*8),0,0);
			RelativeLayout.LayoutParams layoutdroid = new RelativeLayout.LayoutParams(
					marginParams2);

			droid.setLayoutParams(layoutdroid);

			LinearLayout homelinear = (LinearLayout) findViewById(R.id.homelinearlayout);
			// homelinear.setPadding(0, 0, 0, 0);

			home = (ImageView) findViewById(R.id.home);

			MarginLayoutParams marginParams1 = new MarginLayoutParams(
					home.getLayoutParams());

			marginParams1.setMargins(0, 0, 0, 0);
			// marginParams1.setMargins((windowwidth / 24) * 10, 0,
			// (windowheight / 32) * 1, 0);
			// marginParams1.setMargins(((windowwidth-home.getWidth())/2),0,(windowheight/32)*10,0);
			LinearLayout.LayoutParams layout = new LinearLayout.LayoutParams(
					marginParams1);

			home.setLayoutParams(layout);

			droid.setOnTouchListener(new View.OnTouchListener() {

				@Override
				public boolean onTouch(View v, MotionEvent event) {
					// TODO Auto-generated method stub
					layoutParams = (LayoutParams) v.getLayoutParams();

					switch (event.getAction()) {

					case MotionEvent.ACTION_DOWN:
						int[] hompos = new int[2];
						// int[] phonepos=new int[2];
						droidpos = new int[2];
						// phone.getLocationOnScreen(phonepos);
						home.getLocationOnScreen(hompos);
						home_x = hompos[0];
						home_y = hompos[1];
						// phone_x=phonepos[0];
						// phone_y=phonepos[1];

						break;
					case MotionEvent.ACTION_MOVE:
						int x_cord = (int) event.getRawX();
						int y_cord = (int) event.getRawY();

						if (x_cord > windowwidth - (windowwidth / 24)) {
							x_cord = windowwidth - (windowwidth / 24) * 2;
						}
						if (y_cord > windowheight - (windowheight / 32)) {
							y_cord = windowheight - (windowheight / 32) * 2;
						}

						layoutParams.leftMargin = x_cord;
						layoutParams.topMargin = y_cord;

						droid.getLocationOnScreen(droidpos);
						v.setLayoutParams(layoutParams);

						if (((x_cord - home_x) <= (windowwidth / 24) * 4 && (home_x - x_cord) <= (windowwidth / 24) * 3)
								&& ((home_y - y_cord) <= (windowheight / 32) * 2)) {
							System.out.println("home overlapps");
							System.out.println("homeee" + home_x + "  "
									+ (int) event.getRawX() + "  " + x_cord
									+ " " + droidpos[0]);

							System.out.println("homeee" + home_y + "  "
									+ (int) event.getRawY() + "  " + y_cord
									+ " " + droidpos[1]);

							v.setVisibility(View.GONE);

							// startActivity(new Intent(Intent.ACTION_VIEW,
							// Uri.parse("content://contacts/people/")));
							finish();
						} else {
							System.out.println("homeee" + home_x + "  "
									+ (int) event.getRawX() + "  " + x_cord
									+ " " + droidpos[0]);

							System.out.println("homeee" + home_y + "  "
									+ (int) event.getRawY() + "  " + y_cord
									+ " " + droidpos[1]);

							System.out.println("home notttt overlapps");
						}

						break;
					case MotionEvent.ACTION_UP:

						int x_cord1 = (int) event.getRawX();
						int y_cord2 = (int) event.getRawY();

						if (((x_cord1 - home_x) <= (windowwidth / 24) * 4 && (home_x - x_cord1) <= (windowwidth / 24) * 3)
								&& ((home_y - y_cord2) <= (windowheight / 32) * 2)) {
							System.out.println("home overlapps");
							System.out.println("homeee" + home_x + "  "
									+ (int) event.getRawX() + "  " + x_cord1
									+ " " + droidpos[0]);

							System.out.println("homeee" + home_y + "  "
									+ (int) event.getRawY() + "  " + y_cord2
									+ " " + droidpos[1]);

							// startActivity(new Intent(Intent.ACTION_VIEW,
							// Uri.parse("content://contacts/people/")));
							// finish();
						} else {

							layoutParams.leftMargin = 0;
							// layoutParams.bottomMargin = 5;
							
							if(isTablet(LockScreenAppActivity.this))
								layoutParams.topMargin = windowheight - 60;
							else
								layoutParams.topMargin = (windowheight/32) * 28;
							
							// layoutParams.leftMargin = (windowwidth / 24) *
							// 10;
							// layoutParams.topMargin = (windowheight / 32) *
							// 15;
							v.setLayoutParams(layoutParams);

						}

					}

					return true;
				}
			});
		} catch (Exception e) {
			// TODO: handle exception
		}

	}

	class StateListener extends PhoneStateListener {
		@Override
		public void onCallStateChanged(int state, String incomingNumber) {

			super.onCallStateChanged(state, incomingNumber);
			switch (state) {
			case TelephonyManager.CALL_STATE_RINGING:
				break;
			case TelephonyManager.CALL_STATE_OFFHOOK:
				System.out.println("call Activity off hook");
				finish();

				break;
			case TelephonyManager.CALL_STATE_IDLE:
				break;
			}
		}
	};

	public void onSlideTouch(View view, MotionEvent event) {
		switch (event.getAction()) {
		case MotionEvent.ACTION_DOWN:
			break;
		case MotionEvent.ACTION_MOVE:
			int x_cord = (int) event.getRawX();
			int y_cord = (int) event.getRawY();

			if (x_cord > windowwidth) {
				x_cord = windowwidth;
			}
			if (y_cord > windowheight) {
				y_cord = windowheight;
			}

			layoutParams.leftMargin = x_cord - 25;
			layoutParams.topMargin = y_cord - 75;

			view.setLayoutParams(layoutParams);
			break;
		default:
			break;

		}
	}

	@Override
	public void onBackPressed() {
		// Don't allow back to dismiss.
		return;
	}

	// only used in lockdown mode
	@Override
	protected void onPause() {
		super.onPause();

		// Don't hang around.
		// finish();
	}

	@Override
	protected void onStop() {
		super.onStop();

		// Don't hang around.
		// finish();
	}

	@Override
	public boolean onKeyDown(int keyCode, android.view.KeyEvent event) {

		if ((keyCode == KeyEvent.KEYCODE_VOLUME_DOWN)
				|| (keyCode == KeyEvent.KEYCODE_POWER)
				|| (keyCode == KeyEvent.KEYCODE_VOLUME_UP)
				|| (keyCode == KeyEvent.KEYCODE_CAMERA)) {
			// this is where I can do my stuff
			return true; // because I handled the event
		}
		if ((keyCode == KeyEvent.KEYCODE_HOME)) {

			return true;
		}

		return false;

	}

	public boolean dispatchKeyEvent(KeyEvent event) {
		if (event.getKeyCode() == KeyEvent.KEYCODE_POWER
				|| (event.getKeyCode() == KeyEvent.KEYCODE_VOLUME_DOWN)
				|| (event.getKeyCode() == KeyEvent.KEYCODE_POWER)) {
			// Intent i = new Intent(this, NewActivity.class);
			// startActivity(i);
			return false;
		}
		if ((event.getKeyCode() == KeyEvent.KEYCODE_HOME)) {

			System.out.println("alokkkkkkkkkkkkkkkkk");
			return true;
		}
		return false;
	}

	public void onDestroy() {
		// k1.reenableKeyguard();

		super.onDestroy();
	}

	// check is tablet or not
	public static boolean isTablet(Context context) {
		return (context.getResources().getConfiguration().screenLayout & Configuration.SCREENLAYOUT_SIZE_MASK) >= Configuration.SCREENLAYOUT_SIZE_LARGE;
	}

}