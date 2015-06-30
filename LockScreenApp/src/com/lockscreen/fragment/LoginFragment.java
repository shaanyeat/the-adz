package com.lockscreen.fragment;

/*Developer: TAI ZHEN KAI
 Project 2015*/

import java.util.Arrays;

import org.json.JSONObject;

import android.app.ActionBar;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.Request;
import com.facebook.Response;
import com.facebook.Session;
import com.facebook.SessionState;
import com.facebook.UiLifecycleHelper;
import com.facebook.model.GraphUser;
import com.facebook.widget.LoginButton;
import com.facebook.widget.ProfilePictureView;
import com.lockscreen.R;
import com.lockscreen.adapter.UserItem;
import com.lockscreen.application.ForgotPassword;
import com.lockscreen.application.HomeActivity;
import com.lockscreen.application.MainActivity;
import com.lockscreen.application.RegisterActivity;
import com.lockscreen.utility.Constant;
import com.lockscreen.utility.RestClient;
import com.lockscreen.utility.SharedPreference;

public class LoginFragment extends FragmentActivity {

	TextView forgotPassword, tvRegister;
	Button btnLogin;
	EditText username, password;
	LinearLayout normalLoginLayout;
	LoginButton authButton;
	String referralCode = null;

	public static final String PREFS_NAME = "LOGIN";
	private SharedPreference pref;

	private static final String TAG = "LoginFragment";

	boolean check = true;
	ActionBar actionBar;

	private UiLifecycleHelper uiHelper;
	private Session.StatusCallback callback = new Session.StatusCallback() {
		@Override
		public void call(final Session session, final SessionState state,
				final Exception exception) {
			onSessionStateChange(session, state, exception);
		}
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.fragment_login);

		pref = new SharedPreference(this);

		uiHelper = new UiLifecycleHelper(this, callback);
		uiHelper.onCreate(savedInstanceState);

		/*
		 * actionBar = getActionBar();
		 * actionBar.setDisplayHomeAsUpEnabled(true); if (!pref.GetLogin()) {
		 * actionBar.setDisplayHomeAsUpEnabled(false); } else {
		 * actionBar.setDisplayHomeAsUpEnabled(true); }
		 */
		authButton = (LoginButton) findViewById(R.id.authButton);
		// authButton.setFragment(this);
		authButton.setReadPermissions(Arrays.asList("email", "user_location"));

		/*
		 * userName = (TextView) findViewById(R.id.userName); userPic =
		 * (ProfilePictureView) findViewById(R.id.userPic);
		 * userPic.setCropped(true); userEmail = (TextView)
		 * findViewById(R.id.userEmail); userGender = (TextView)
		 * findViewById(R.id.userGender); userLocation = (TextView)
		 * findViewById(R.id.userLocation); profileLayout = (RelativeLayout)
		 * findViewById(R.id.profileLayout);
		 */
		tvRegister = (TextView) findViewById(R.id.tvRegister);
		username = (EditText) findViewById(R.id.username);
		password = (EditText) findViewById(R.id.password);
		btnLogin = (Button) findViewById(R.id.btnLogin);
		forgotPassword = (TextView) findViewById(R.id.forgotPassword);
		normalLoginLayout = (LinearLayout) findViewById(R.id.normalLoginLayout);

		tvRegister.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent i = new Intent(LoginFragment.this,
						RegisterActivity.class);
				startActivity(i);
			}
		});

		btnLogin.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if (!username.getText().toString().equals("")
						&& !password.getText().toString().equals("")) {
//					new SignInTask(LoginFragment.this).execute((Void[]) null);
					showNormalLoginInputDialog() ;
				} else {
					Toast.makeText(LoginFragment.this, R.string.fillrequired,
							Toast.LENGTH_LONG).show();
				}
			}
		});

		forgotPassword.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent i = new Intent(LoginFragment.this, ForgotPassword.class);
				startActivity(i);
			}
		});

		// Check for login status

		/*
		 * if (pref.GetLogin()) {
		 * 
		 * try { normalLoginLayout.setVisibility(View.GONE);
		 * authButton.setVisibility(View.GONE);
		 * profileLayout.setVisibility(View.VISIBLE);
		 * 
		 * userName.setText(Constant.currentLoginUser.getFirstName() .toString()
		 * + " " + Constant.currentLoginUser.getLastName().toString());
		 * userEmail.setText(Constant.currentLoginUser.getEmail() .toString());
		 * } catch (Exception e) {
		 * 
		 * } }
		 */

	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			if (pref.GetLogin()) {
				Intent i = new Intent(LoginFragment.this, HomeActivity.class);
				startActivity(i);
				LoginFragment.this.finish();
			}
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}

	@Override
	public void onResume() {
		super.onResume();

		// For scenarios where the main activity is launched and user
		// session is not null, the session state change notification
		// may not be triggered. Trigger it if it's open/closed.
		Session session = Session.getActiveSession();
		if (session != null && (session.isOpened() || session.isClosed())) {
			onSessionStateChange(session, session.getState(), null);
		}

		uiHelper.onResume();
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		uiHelper.onActivityResult(requestCode, resultCode, data);
	}

	@Override
	public void onPause() {
		super.onPause();
		uiHelper.onPause();
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		uiHelper.onDestroy();
	}

	@Override
	public void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		uiHelper.onSaveInstanceState(outState);
	}

	private void onSessionStateChange(Session session, SessionState state,
			Exception exception) {
		if (state.isOpened()) {
			Log.i(TAG, "Logged in...");
			// Get the user's data.
			makeMeRequest(session);
			// get Access token and pass to server
			Log.v("Access Token", session.getAccessToken());
			Constant.currentLoginUser = new UserItem();
			Constant.currentLoginUser.setToken(session.getAccessToken());

			if (!pref.GetLogin()) {
				showInputDialog();
				check = false;
			}
			pref.isFacebookLogin(true);

		} else if (state.isClosed()) {
			Log.i(TAG, "Logged out...");
			pref.isFacebookLogin(false);
		}
	}

	private void makeMeRequest(final Session session) {
		// Make an API call to get user data and define a
		// new callback to handle the response.
		Request request = Request.newMeRequest(session,
				new Request.GraphUserCallback() {
					@Override
					public void onCompleted(GraphUser user, Response response) {
						// TODO Auto-generated method stub
						// If the response is successful
						if (session == Session.getActiveSession()) {
							if (user != null) {
								// Set the id for the ProfilePictureView
								// view that in turn displays the profile
								// picture.
								/*
								 * userPic.setProfileId(user.getId());
								 * 
								 * // Set the Textview's text to the user's
								 * name. userName.setText(user.getName());
								 * 
								 * // set the username
								 * userEmail.setText(user.getProperty("email")
								 * .toString());
								 * 
								 * // set the gender
								 * userGender.setText(user.getProperty("gender")
								 * .toString());
								 * 
								 * // set the location
								 * userLocation.setText(user.getLocation()
								 * .getProperty("name").toString()); //
								 * userLocation
								 * .setText(user.getLocation().toString());
								 */
							}
						}
						if (response.getError() != null) {
							// Handle errors, will do so later.
						}
					}
				});
		request.executeAsync();
	}

	private void showNormalLoginInputDialog() {

		// get prompts.xml view
		LayoutInflater layoutInflater = LayoutInflater.from(LoginFragment.this);
		View promptView = layoutInflater.inflate(R.layout.input_dialog, null);
		AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
				LoginFragment.this);
		alertDialogBuilder.setView(promptView);

		final EditText editText = (EditText) promptView
				.findViewById(R.id.edittext);
		// setup a dialog window
		alertDialogBuilder
				.setCancelable(false)
				.setPositiveButton("OK", new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int id) {
						referralCode = editText.getText().toString();

						new SignInTask(LoginFragment.this)
								.execute((Void[]) null);
					}
				})
				.setNegativeButton("Cancel",
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog, int id) {
								dialog.cancel();

								new SignInTask(LoginFragment.this)
										.execute((Void[]) null);
							}
						});

		// create an alert dialog
		AlertDialog alert = alertDialogBuilder.create();
		alert.show();
	}

	private void showInputDialog() {

		// get prompts.xml view
		LayoutInflater layoutInflater = LayoutInflater.from(LoginFragment.this);
		View promptView = layoutInflater.inflate(R.layout.input_dialog, null);
		AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
				LoginFragment.this);
		alertDialogBuilder.setView(promptView);

		final EditText editText = (EditText) promptView
				.findViewById(R.id.edittext);
		// setup a dialog window
		alertDialogBuilder
				.setCancelable(false)
				.setPositiveButton("OK", new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int id) {
						referralCode = editText.getText().toString();

						new SignInFacebook(LoginFragment.this)
								.execute((Void[]) null);
					}
				})
				.setNegativeButton("Cancel",
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog, int id) {
								dialog.cancel();

								new SignInFacebook(LoginFragment.this)
										.execute((Void[]) null);
							}
						});

		// create an alert dialog
		AlertDialog alert = alertDialogBuilder.create();
		alert.show();
	}

	// Login
	private class SignInTask extends AsyncTask<Void, Void, Integer> {
		private ProgressDialog dialog;
		private final Context context;
		// String fieldName;
		String successcode = "0";
		String errormsg;

		public SignInTask(Context context) {
			this.context = context;
		}

		@Override
		protected void onPreExecute() {
			dialog = ProgressDialog.show(context, "",
					getResources().getString(R.string.progesschecking), true);
			dialog.show();
		}

		@Override
		protected Integer doInBackground(Void... ignored) {
			Integer returnCode = 0;
			try {
				JSONObject loginUser = new JSONObject();

				loginUser.put("Username", username.getText().toString());
				loginUser.put("Password", password.getText().toString());

				JSONObject devInfo = new JSONObject();
				/*
				 * Field[] fields = Build.VERSION_CODES.class.getFields(); for
				 * (Field field : fields) { fieldName = field.getName(); }
				 */
				devInfo.put("OS", "Android");
				devInfo.put("OS_Version", android.os.Build.VERSION.RELEASE); //
				devInfo.put("Model", Build.MODEL); //
				TelephonyManager telephonyManager = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
				devInfo.put("UniqueId", telephonyManager.getDeviceId());

				loginUser.put("DeviceInfo", devInfo);

				RestClient client = new RestClient(Constant.BASEWEBSERVICEURL
						+ "user/login");
				client.AddHeader("Content-type", "application/json");
				client.AddHeader("Accept", "application/json");

				client.ExecuteHybridJsonPost(loginUser.toString());

				Log.v("LOCKSCREEN_LOGIN_USER", loginUser.toString());
				String response = client.getResponse();
				Log.v("LOCKSCREEN_LOGIN_USER", response);
				// Create JSON Object
				JSONObject json = new JSONObject(response);
				if (!json.isNull("Result")) {

					JSONObject result = json.getJSONObject("Result");

					/*
					 * Integer userid = result.getInt("UserId"); String fname =
					 * result.getString("FirstName"); String lname =
					 * result.getString("LastName"); String email =
					 * result.getString("Email"); Integer gender =
					 * result.getInt("Gender"); String birthday =
					 * result.getString("DateOfBirth"); String status =
					 * result.getString("UserStatus"); String img =
					 * result.getString("ImageUrl"); Integer point =
					 * result.getInt("PointBalance");
					 */

					Integer userid = result.getInt("UserId");
					String fname = result.getString("FirstName");
					String lname = result.getString("LastName");
					String email = result.getString("Email");
					Integer gender = result.getInt("Gender");
					String birthday = result.getString("DateOfBirth");
					String status = result.getString("UserStatus");
					String img = result.getString("ImageUrl");
					Integer point = result.getInt("PointBalance");

					JSONObject reststatus = json
							.getJSONObject("ResponseStatus");
					successcode = reststatus.getString("Success");

					String apikey = json.getString("Key");

					Constant.currentLoginUser = new UserItem(userid, fname,
							lname, email, gender, birthday, status, img, point);

					pref.setapikey(apikey);

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
			// LoginFragment.this.finish();

			if (successcode.equals("1")) {
				pref.isLogin(true);
				/*
				 * normalLoginLayout.setVisibility(View.GONE);
				 * authButton.setVisibility(View.GONE);
				 * profileLayout.setVisibility(View.VISIBLE);
				 * 
				 * userName.setText(Constant.currentLoginUser.getFirstName()
				 * .toString() + " " +
				 * Constant.currentLoginUser.getLastName().toString());
				 * userEmail.setText(Constant.currentLoginUser.getEmail()
				 * .toString());
				 */

				SharedPreferences.Editor editor = getSharedPreferences(
						PREFS_NAME, MODE_PRIVATE).edit();
				editor.putBoolean("loginStatus", true);
				editor.commit();

				Intent intent = new Intent(LoginFragment.this,
						HomeActivity.class);
				startActivity(intent);
				LoginFragment.this.finish();

			} else {
				Toast.makeText(LoginFragment.this, errormsg, Toast.LENGTH_LONG)
						.show();
			}

			dialog.dismiss();
		}
	}

	// Login through facebook
	private class SignInFacebook extends AsyncTask<Void, Void, Integer> {
		private ProgressDialog dialog;
		private final Context context;
		// String fieldName;
		String successcode = "0";
		String errormsg;

		public SignInFacebook(Context context) {
			this.context = context;
		}

		@Override
		protected void onPreExecute() {
			 dialog = ProgressDialog.show(context, "",
			 getResources().getString(R.string.progesschecking), true);
			 dialog.show();
		}

		@Override
		protected Integer doInBackground(Void... ignored) {
			Integer returnCode = 0;
			try {
				JSONObject loginUser = new JSONObject();

				loginUser.put("AccessToken",
						Constant.currentLoginUser.getToken());
				JSONObject devInfo = new JSONObject();
				/*
				 * Field[] fields = Build.VERSION_CODES.class.getFields(); for
				 * (Field field : fields) { fieldName = field.getName(); }
				 */
				devInfo.put("OS", "Android");
				devInfo.put("OS_Version", android.os.Build.VERSION.RELEASE); //
				devInfo.put("Model", Build.MODEL); //
				TelephonyManager telephonyManager = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
				devInfo.put("UniqueId", telephonyManager.getDeviceId());

				loginUser.put("DeviceInfo", devInfo);

				loginUser.put("ReferralCode", referralCode);

				RestClient client = new RestClient(Constant.BASEWEBSERVICEURL
						+ "user/loginfb");
				client.AddHeader("Content-type", "application/json");
				client.AddHeader("Accept", "application/json");

				client.ExecuteHybridJsonPost(loginUser.toString());

				Log.v("LOCKSCREEN_FBLOGIN_USER", loginUser.toString());
				String response = client.getResponse();
				Log.v("LOCKSCREEN_FBLOGIN_USER", response);
				// Create JSON Object
				JSONObject json = new JSONObject(response);
				if (!json.isNull("Result")) {

					JSONObject result = json.getJSONObject("Result");

					Integer userid = result.getInt("UserId");
					String fname = result.getString("FirstName");
					String lname = result.getString("LastName");
					String email = result.getString("Email");
					Integer gender = result.getInt("Gender");
					String birthday = result.getString("DateOfBirth");
					String status = result.getString("UserStatus");
					String img = result.getString("ImageUrl");
					Integer point = result.getInt("PointBalance");

					JSONObject reststatus = json
							.getJSONObject("ResponseStatus");
					successcode = reststatus.getString("Success");

					String apikey = json.getString("Key");

					Constant.currentLoginUser = new UserItem(userid, fname,
							lname, email, gender, birthday, status, img, point);

					pref.setapikey(apikey);

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
			// LoginFragment.this.finish();

			if (successcode.equals("1")) {
				pref.isLogin(true);
				/*
				 * normalLoginLayout.setVisibility(View.GONE);
				 * authButton.setVisibility(View.GONE);
				 * profileLayout.setVisibility(View.VISIBLE);
				 * 
				 * userName.setText(Constant.currentLoginUser.getFirstName()
				 * .toString() + " " +
				 * Constant.currentLoginUser.getLastName().toString());
				 * userEmail.setText(Constant.currentLoginUser.getEmail()
				 * .toString());
				 */

				SharedPreferences.Editor editor = getSharedPreferences(
						PREFS_NAME, MODE_PRIVATE).edit();
				editor.putBoolean("loginStatus", true);
				editor.commit();

				Intent intent = new Intent(LoginFragment.this,
						HomeActivity.class);
				startActivity(intent);
				LoginFragment.this.finish();
			} else {
				Toast.makeText(LoginFragment.this, errormsg, Toast.LENGTH_LONG)
						.show();
			}

			 dialog.dismiss();
		}
	}

	@Override
	public void onBackPressed() {
		if (pref.GetLogin()) {
			Intent i = new Intent(LoginFragment.this, HomeActivity.class);
			startActivity(i);
			LoginFragment.this.finish();
		} else {
			super.onBackPressed();
		}

	}

}
