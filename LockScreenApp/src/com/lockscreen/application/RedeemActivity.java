package com.lockscreen.application;

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
import android.widget.LinearLayout;
import android.widget.Toast;

import com.lockscreen.R;
import com.lockscreen.utility.Constant;
import com.lockscreen.utility.RestClient;
import com.lockscreen.utility.SharedPreference;

public class RedeemActivity extends FragmentActivity {

	ActionBar actionBar;
	LinearLayout typeDeliveryLayout, typeMoneyLayout, typeMobileLayout;
	Button btnRedeem;
	EditText deliveryAddress1,deliveryAddress2,postcode, city, state, country, etBankName, etBankAcc, etBankAccName, etMobileOperator, etMobileNumber;

	SharedPreference pref;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.redeem);

		actionBar = getActionBar();
		actionBar.setDisplayHomeAsUpEnabled(true);

		pref = new SharedPreference(RedeemActivity.this);

		final String rewardId = getIntent().getExtras().getString("RewardId");
		String typeDelivery = getIntent().getExtras().getString("Delivery");
		String typeMoney = getIntent().getExtras().getString("Money");
		String typeMobile = getIntent().getExtras().getString("Mobile");

		typeDeliveryLayout = (LinearLayout) findViewById(R.id.typeDeliveryLayout);
		typeMoneyLayout = (LinearLayout) findViewById(R.id.typeMoneyLayout);
		typeMobileLayout = (LinearLayout) findViewById(R.id.typeMobileLayout);
		deliveryAddress1 = (EditText) findViewById(R.id.deliveryAddress1);
		deliveryAddress2 = (EditText) findViewById(R.id.deliveryAddress2);
		postcode = (EditText) findViewById(R.id.postcode);
		city = (EditText) findViewById(R.id.city);
		state = (EditText) findViewById(R.id.state);
		country = (EditText) findViewById(R.id.country);
		etBankName = (EditText) findViewById(R.id.etBankName);
		etBankAcc = (EditText) findViewById(R.id.etBankAcc);
		etBankAccName = (EditText) findViewById(R.id.etBankAccName);
		etMobileOperator = (EditText) findViewById(R.id.etMobileOperator);
		etMobileNumber = (EditText) findViewById(R.id.etMobileNumber);

		btnRedeem = (Button) findViewById(R.id.btnRedeem);

		if (typeDelivery.equals("true")) {
			typeDeliveryLayout.setVisibility(View.VISIBLE);
		}
		if (typeMoney.equals("true")) {
			typeMoneyLayout.setVisibility(View.VISIBLE);
		}
		if (typeMobile.equals("true")) {
			typeMobileLayout.setVisibility(View.VISIBLE);
		}

		btnRedeem.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

				new redeemTask(RedeemActivity.this, rewardId)
						.execute((Void[]) null);
			}
		});

	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			RedeemActivity.this.finish();
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}

	// Redeem
	private class redeemTask extends AsyncTask<Void, Void, Integer> {
		private ProgressDialog dialog;
		private final Context context;
		// String fieldName;
		String successcode = "0";
		String rewardId;
		String message;

		public redeemTask(Context context, String rewardId) {
			this.context = context;
			this.rewardId = rewardId;
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
				JSONObject redeem = new JSONObject();

				redeem.put("RewardId", rewardId);
				redeem.put("AddressLine1", deliveryAddress1.getText().toString());
				redeem.put("AddressLine2", deliveryAddress2.getText().toString());
				redeem.put("City", city.getText().toString());
				redeem.put("State", state.getText().toString());
				redeem.put("Country", country.getText().toString());
				redeem.put("PostCode", postcode.getText().toString());
				redeem.put("BankName", etBankName.getText().toString());
				redeem.put("BankAccountNum", etBankAcc.getText().toString());
				redeem.put("BankAccountName", etBankAccName.getText().toString());
				redeem.put("MobileOperatorId", etMobileOperator.getText().toString());
				redeem.put("MobileAccNum", etMobileNumber.getText().toString());

				RestClient client = new RestClient(Constant.BASEWEBSERVICEURL
						+ "redemption/request");
				client.AddHeader("Content-type", "application/json");
				client.AddHeader("Accept", "application/json");
				Log.v("KEY", pref.getapikey());
				client.AddHeader("Key", pref.getapikey());

				client.ExecuteHybridJsonPost(redeem.toString());

				Log.v("REDEEM_REQUEST", redeem.toString());
				String response = client.getResponse();
				Log.v("REDEEM_REQUEST", response);
				JSONObject json = new JSONObject(response);

				if (!json.isNull("Result")) {
					JSONObject result = json.getJSONObject("Result");
//					message = result.getString("Message");
					JSONObject reststatus = json.getJSONObject("ResponseStatus");
					successcode = reststatus.getString("Success");

					String apiKey = json.getString("Key");
					pref.setapikey(apiKey);

				} else {
					JSONObject reststatus = json.getJSONObject("ResponseStatus");
					successcode = reststatus.getString("Success");
					if(!successcode.equals("1")){
						message = reststatus.getString("Message");
					}
				}
				
				String apiKey = json.getString("Key");
				pref.setapikey(apiKey);

			} catch (Exception e) {
				e.printStackTrace();
			}

			return returnCode;
		}

		protected void onPostExecute(Integer returnCode) {
			// UI work allowed here
			if (successcode.equals("1")) {
				Toast.makeText(context, R.string.redeemSuccess, Toast.LENGTH_LONG).show();
			}else{
				Toast.makeText(context, message, Toast.LENGTH_LONG).show();
			}
			
			RedeemActivity.this.finish();
			
			dialog.dismiss();
		}
	}

}
