package com.lockscreen.application;

/*Developer: TAI ZHEN KAI
Project 2015*/

import org.json.JSONObject;

import android.app.ActionBar;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.lockscreen.R;
import com.lockscreen.utility.Constant;
import com.lockscreen.utility.RestClient;

public class ForgotPassword extends FragmentActivity {

	EditText resetEmail;
	Button resetBtn;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.fragment_forgotpassword);

//		ActionBar actionBar = getActionBar();
//		actionBar.setDisplayHomeAsUpEnabled(true);

		resetEmail = (EditText) findViewById(R.id.resetEmail);
		resetBtn = (Button) findViewById(R.id.resetBtn);

		resetBtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

				if (resetEmail.getText().toString().equals("")) {
					Toast.makeText(ForgotPassword.this, R.string.fillrequired,
							Toast.LENGTH_LONG).show();
				} else {
					new resetPassword(ForgotPassword.this)
							.execute((Void[]) null);
				}
			}
		});

	}

/*	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			ForgotPassword.this.finish();
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}*/

	// Login
	private class resetPassword extends AsyncTask<Void, Void, Integer> {
		private ProgressDialog dialog;
		private final Context context;
		String successcode;

		public resetPassword(Context context) {
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

				loginUser.put("Email", resetEmail.getText().toString());

				RestClient client = new RestClient(Constant.BASEWEBSERVICEURL
						+ "user/resetpassword");
				client.AddHeader("Content-type", "application/json");
				client.AddHeader("Accept", "application/json");

				client.ExecuteHybridJsonPost(loginUser.toString());

				Log.v("LOCKSCREEN_RESET_PASSWORD", loginUser.toString());
				String response = client.getResponse();
				// Log.v("LOCKSCREEN_CREATE_USER", response);
				// // Create JSON Object
				JSONObject json = new JSONObject(response.toString());
				if (json.isNull("Error")) {

					JSONObject result = json.getJSONObject("ResponseStatus");
					successcode = result.getString("Success");

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
				Toast.makeText(ForgotPassword.this,
						R.string.resetpasswordmailsend, Toast.LENGTH_LONG)
						.show();

				ForgotPassword.this.finish();
			}

			dialog.dismiss();
		}
	}

}
