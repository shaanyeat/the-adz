package com.lockscreen;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import org.json.JSONArray;
import org.json.JSONObject;

import android.app.Activity;
import android.app.KeyguardManager;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.SimpleOnPageChangeListener;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.util.Log;
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
import android.widget.Toast;

import com.lockscreen.adapter.AdapterOfflineAdz;
import com.lockscreen.adapter.CampaignItem;
import com.lockscreen.adapter.DealGalleryAdapter;
import com.lockscreen.loader.ImageLoader;
import com.lockscreen.utility.Constant;
import com.lockscreen.utility.RestClient;
import com.lockscreen.utility.SharedPreference;

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
	public static ArrayList<String> subImgUrl;
	public static ImageLoader imageLoader;
	TextView date, dayInWeek;
	DigitalClock dc;
	SharedPreference pref;
	private ArrayList<CampaignItem> cItems;

	private static int currentPage;

	public static final String KEY_APIKEY = "apiKey";

	// use to disable the home button
	/*
	 * @Override public void onAttachedToWindow() { // TODO Auto-generated
	 * method stub this.getWindow().setType(
	 * WindowManager.LayoutParams.TYPE_KEYGUARD_DIALOG |
	 * WindowManager.LayoutParams.FLAG_FULLSCREEN);
	 * 
	 * super.onAttachedToWindow(); }
	 */

	public void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		getWindow().addFlags(
		// WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON | // keep the screen
		// on
				WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED);
		// | WindowManager.LayoutParams.FLAG_FULLSCREEN);

		setContentView(R.layout.main);
		viewPager = (ViewPager) findViewById(R.id.pager);

		pref = new SharedPreference(LockScreenAppActivity.this);

		if (Constant.isNetworkAvailable(LockScreenAppActivity.this)) {
			// get campaign List
			new campaingList(LockScreenAppActivity.this).execute((Void[]) null);
		} else {

			String imgString = pref.getAdzImage().toString().replace("[", "")
					.replace("]", "");

			List<String> items = Arrays.asList(imgString.split("\\s*,\\s*"));

			ArrayList<String> listOfStrings = new ArrayList<String>(
					items.size());
			listOfStrings.addAll(items);

			Log.v(LockScreenAppActivity.class.getSimpleName(),
					listOfStrings.toString());

			Collections.shuffle(listOfStrings);

			AdapterOfflineAdz adapter = new AdapterOfflineAdz(
					LockScreenAppActivity.this, listOfStrings);

			viewPager.setAdapter(adapter);

			Toast.makeText(LockScreenAppActivity.this,
					"No Internet Connection!", Toast.LENGTH_LONG).show();
		}

		dc = (DigitalClock) findViewById(R.id.digitalClock1);
		date = (TextView) findViewById(R.id.date);
		dayInWeek = (TextView) findViewById(R.id.dayInWeek);

		PageListener pageListener = new PageListener();
		viewPager.setOnPageChangeListener(pageListener);

		DateFormat dateFormat = new SimpleDateFormat("dd");
		// get current date time with Date()
		Date currentDate = new Date();

		Calendar now = Calendar.getInstance();
		dayInWeek.setText(now.getDisplayName(Calendar.DAY_OF_WEEK, Calendar.LONG,
				Locale.getDefault()));
		date.setText(now.getDisplayName(Calendar.MONTH, Calendar.LONG,
				Locale.getDefault()) + " " + dateFormat.format(currentDate)

		);

		imageLoader = new ImageLoader(this);

		cItems = new ArrayList<CampaignItem>();
		subImg = new ArrayList<String>();
		subImgUrl = new ArrayList<String>();

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

			if (isTablet(this))
				marginParams2.setMargins(0, windowheight - 60, 0, 0);
			else
				marginParams2.setMargins(0, (windowheight / 32) * 28, 0, 0);

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

							if (isTablet(LockScreenAppActivity.this))
								layoutParams.topMargin = windowheight - 60;
							else
								layoutParams.topMargin = (windowheight / 32) * 28;

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

	// getCampaignList
	private class campaingList extends AsyncTask<Void, Void, Integer> {
		private ProgressDialog dialog;
		private final Context context;
		// String fieldName;
		String successcode = "0";
		String errormsg;

		public campaingList(Context context) {
			this.context = context;
		}

		@Override
		protected void onPreExecute() {
			// dialog = ProgressDialog.show(context, "",
			// getResources().getString(R.string.progesschecking), true);
			// dialog.show();
		}

		@Override
		protected Integer doInBackground(Void... ignored) {
			Integer returnCode = 0;
			try {
				RestClient client = new RestClient(Constant.BASEWEBSERVICEURL
						// +
						// "campaign/list?latitude=3.31766&longitude=101.4839");
						+ "campaign/list?latitude=" + pref.getCoordinatesLat()
						+ "&longitude=" + pref.getCoordinatesLong());

				client.AddHeader("Content-type", "application/json");
				client.AddHeader("Accept", "application/json");
				// Log.v("KEY", Constant.currentLoginUser.getApiKey());
				Log.v("KEY", pref.getapikey());
				client.AddHeader("Key", pref.getapikey());

				client.Execute(RestClient.GET);

				String response = client.getResponse();
				Log.v("Campaign_List", response);
				// Create JSON Object
				JSONObject json = new JSONObject(response);
				if (!json.isNull("Result")) {

					JSONArray data = json.getJSONArray(("Result"));
					for (int i = 0; i < data.length(); i++) {
						JSONObject res = data.getJSONObject(i);

						String url = data.getString(0);

						Integer id = res.getInt("CampaignId");
						String name = res.getString("Name");
						Integer mId = res.getInt("MerchantId");
						String mName = res.getString("MerchantName");
						String imgName = res.getString("ImageName");
						String imgUrl = res.getString("ImageUrl");
						String linkurl = res.getString("LinkURL");

						cItems.add(new CampaignItem(id, name, mId, mName,
								imgName, imgUrl, linkurl));

						subImg.add(imgUrl);

						pref.storeAdzImage(subImg);

						// subImgUrl.add(linkurl);

					}

					JSONObject reststatus = json
							.getJSONObject("ResponseStatus");
					successcode = reststatus.getString("Success");

					String apiKey = json.getString("Key");

					// Constant.currentLoginUser = new UserDetails();
					// Constant.currentLoginUser.setApiKey(apiKey);
					pref.setapikey(apiKey);

				} else {
					JSONObject reststatus = json
							.getJSONObject("ResponseStatus");
					successcode = reststatus.getString("Success");

					if (successcode.equals("0")) {
						errormsg = reststatus.getString("Message");

					}
				}

			} catch (Exception e) {
				e.printStackTrace();
			}

			return returnCode;
		}

		protected void onPostExecute(Integer returnCode) {
			// UI work allowed here

			if (cItems.size() == 0) {
				// cItems.add("https://dl.dropboxusercontent.com/u/76631556/AppStream/dummylock.png");
				cItems.add(new CampaignItem(
						"https://dl.dropboxusercontent.com/u/76631556/AppStream/dummylock.png"));
				try {
					// Pass results to ViewPagerAdapter Class
					DealGalleryAdapter adapter = new DealGalleryAdapter(
							LockScreenAppActivity.this, cItems, true);
					// Binds the Adapter to the ViewPager
					viewPager.setAdapter(adapter);

				} catch (Exception e) {
					// TODO: handle exception
					e.printStackTrace();
				}
			} else if (successcode.equals("1")) {
				try {
					// Pass results to ViewPagerAdapter Class

					Collections.shuffle(cItems);

					DealGalleryAdapter adapter = new DealGalleryAdapter(
							LockScreenAppActivity.this, cItems);

					// DealGalleryAdapter adapter = new DealGalleryAdapter(
					// LockScreenAppActivity.this, subImg, subImgUrl);
					// Binds the Adapter to the ViewPager
					viewPager.setAdapter(adapter);

				} catch (Exception e) {
					// TODO: handle exception
					e.printStackTrace();
				}
			}

			// dialog.dismiss();
		}

	}

	private static class PageListener extends SimpleOnPageChangeListener {
		public void onPageSelected(int position) {
			Log.i("PAGE", "page selected " + position);
			currentPage = position;
		}
	}
}