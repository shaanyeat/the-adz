package com.lockscreen.application;

import org.json.JSONObject;

import android.app.ActionBar;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.lockscreen.R;
import com.lockscreen.adapter.UserDetails;
import com.lockscreen.utility.Constant;
import com.lockscreen.utility.RestClient;
import com.lockscreen.utility.SharedPreference;

public class RegisterActivity extends FragmentActivity {

//	EditText firstName, lastName;
	EditText email, password;
	Button btnRegister;
	private SharedPreference pref;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.fragment_register);
		
		pref = new SharedPreference(this);

		ActionBar actionBar = getActionBar();
		actionBar.setDisplayHomeAsUpEnabled(true);

//		firstName = (EditText) findViewById(R.id.firstName);
//		lastName = (EditText) findViewById(R.id.lastName);
		email = (EditText) findViewById(R.id.email);
		password = (EditText) findViewById(R.id.password);
		btnRegister = (Button) findViewById(R.id.btnRegister);

		btnRegister.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if (!email.getText().toString().equals("")
						&& !password.getText().toString().equals("")) {
					new SignUpTask(RegisterActivity.this)
							.execute((Void[]) null);
				} else {
					Toast.makeText(RegisterActivity.this,
							R.string.fillrequired, Toast.LENGTH_LONG).show();
				}
			}
		});
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			RegisterActivity.this.finish();
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}

	// Register
	private class SignUpTask extends AsyncTask<Void, Void, Integer> {
		private ProgressDialog dialog;
		private final Context context;
		// String fieldName;
		String successcode;

		public SignUpTask(Context context) {
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
				JSONObject newUser = new JSONObject();

				newUser.put("FirstName", "");
				newUser.put("LastName", "");
				newUser.put("Email", email.getText().toString());
				newUser.put("Password", password.getText().toString());

				JSONObject devInfo = new JSONObject();
				/*
				 * Field[] fields = Build.VERSION_CODES.class.getFields(); for
				 * (Field field : fields) { fieldName = field.getName(); }
				 */
				devInfo.put("OS", "Android");
				devInfo.put("OS_Version", android.os.Build.VERSION.RELEASE);
				devInfo.put("Model", Build.MODEL);
				TelephonyManager telephonyManager = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
				devInfo.put("UniqueId", telephonyManager.getDeviceId());
				
//				devInfo.put("UniqueId", Secure.getString(context.getContentResolver(), Secure.ANDROID_ID));
				

				newUser.put("DeviceInfo", devInfo);
				
				

				RestClient client = new RestClient(Constant.BASEWEBSERVICEURL
						+ "user/signup");
				client.AddHeader("Content-type", "application/json");
				client.AddHeader("Accept", "application/json");

				client.ExecuteHybridJsonPost(newUser.toString());

				Log.v("LOCKSCREEN_CREATE_USER", newUser.toString());
				String response = client.getResponse();
				Log.v("LOCKSCREEN_CREATE_USER_RESULT", response);
				JSONObject json = new JSONObject(response);

				if (json.isNull("Errors")) {

					JSONObject result = json.getJSONObject("Result");

					Integer userid = result.getInt("UserId");
					String fname = result.getString("FirstName");
					String lname = result.getString("LastName");
					String email = result.getString("Email");
					Integer gender = result.getInt("Gender");
					String birthday = result.getString("DateOfBirth");
					String status = result.getString("UserStatus");
//					String notification = result.getString("Notif");
					String img = result.getString("ImageUrl");
					

					
					
					JSONObject reststatus = json.getJSONObject("ResponseStatus");
					successcode = reststatus.getString("Success");
					
					String apikey = json.getString("Key");
					
					Constant.currentLoginUser = new UserDetails(userid, fname, lname, email, gender,
							birthday, status, img, apikey);

				} else {
					
				}

			} catch (Exception e) {
				e.printStackTrace();
			}

			return returnCode;
		}

		protected void onPostExecute(Integer returnCode) {
			// UI work allowed here

			if (successcode.equals("1")) {
				pref.isLogin(true);
				Toast.makeText(RegisterActivity.this, R.string.regsuccess,
						Toast.LENGTH_LONG).show();
			}

			RegisterActivity.this.finish();

			dialog.dismiss();
		}
	}

}
