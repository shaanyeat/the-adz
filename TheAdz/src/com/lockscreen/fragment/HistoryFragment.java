package com.lockscreen.fragment;

/*Developer: TAI ZHEN KAI
Project 2015*/

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONObject;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.lockscreen.R;
import com.lockscreen.adapter.HistoryItem;
import com.lockscreen.adapter.RewardItem;
import com.lockscreen.application.RedeemActivity;
import com.lockscreen.fragment.RewardsFragment.ItemAdapter;
import com.lockscreen.fragment.RewardsFragment.ItemAdapter.ViewHolder;
import com.lockscreen.utility.Constant;
import com.lockscreen.utility.RestClient;
import com.lockscreen.utility.SharedPreference;
import com.nostra13.universalimageloader.core.ImageLoader;

public class HistoryFragment extends Fragment {
	
	ListView historyList;
	SharedPreference pref;
	
	private ArrayList<HistoryItem> cItems = new ArrayList<HistoryItem>();

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		View rootView = inflater.inflate(R.layout.fragment_history, container, false);
		
		historyList = (ListView) rootView.findViewById(R.id.historyList);
		
		pref = new SharedPreference(getActivity());
		
		// getHistoryList
		new historyList(getActivity()).execute((Void[]) null);
		
		return rootView;
	}
	
	
	
	
	// getRewardsList
		private class historyList extends AsyncTask<Void, Void, Integer> {
			private ProgressDialog dialog;
			private final Context context;
			// String fieldName;
			String successcode = "0";
			String errormsg;

			public historyList(Context context) {
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
				cItems.clear();
				try {
					RestClient client = new RestClient(Constant.BASEWEBSERVICEURL
							+ "redemption/list");

					client.AddHeader("Content-type", "application/json");
					client.AddHeader("Accept", "application/json");
					Log.v("KEY", pref.getapikey());
					client.AddHeader("Key", pref.getapikey());

					client.Execute(RestClient.GET);

					String response = client.getResponse();
					Log.v("History_List", response);
					// Create JSON Object
					JSONObject json = new JSONObject(response);
					if (!json.isNull("Result")) {

						JSONArray data = json.getJSONArray(("Result"));
						for (int i = 0; i < data.length(); i++) {
							JSONObject res = data.getJSONObject(i);

							Integer redemptionId = res.getInt("RedemptionId");
							Integer rewardId = res.getInt("RewardId");
							String name = res.getString("Name");
							String delivery = res.getString("AddressLine1");
							String delivery2 = res.getString("AddressLine2");
							String city = res.getString("City");
							String state = res.getString("State");
							String country = res.getString("Country");
							String postcode = res.getString("PostCode");
							
							Integer operator = res.getInt("MobileOperatorId");
							Integer redempStatus = res.getInt("RedemptionStatusId");
							String redempName = res.getString("RedemptionStatusName");
							String redempDate = res.getString("RedemptionDate");
							Boolean reviewedStatus = res.getBoolean("Reviewed");

							cItems.add(new HistoryItem(redemptionId,rewardId,name,delivery + delivery2 + city + state + postcode + country ,operator,
									redempStatus,redempName,redempDate,reviewedStatus));
						}

						JSONObject reststatus = json
								.getJSONObject("ResponseStatus");
						successcode = reststatus.getString("Success");

						String apiKey = json.getString("Key");
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

				ItemAdapter adapter = new ItemAdapter(getActivity(), cItems);
				historyList.setAdapter(adapter);

				dialog.dismiss();
			}

		}

		public class ItemAdapter extends BaseAdapter {
			ArrayList<HistoryItem> mItems;
			private Activity activity;

			// private Context context;

			public ItemAdapter(Activity activity, ArrayList<HistoryItem> items) {
				this.activity = activity;
				this.mItems = items;
			}

			@Override
			public int getCount() {
				return mItems.size();
			}

			@Override
			public Object getItem(int location) {
				return mItems.get(location);
			}

			@Override
			public long getItemId(int position) {
				return position;
			}

			class ViewHolder {
				public TextView deliveryName;
				public TextView deliveryAddress;
				public TextView deliveryStatus;
				public TextView redemptionDate;
			}

			@Override
			public View getView(final int position, View convertView,
					ViewGroup parent) {

				ViewHolder holder;

				if (convertView == null) {
					LayoutInflater inflater = LayoutInflater.from(getActivity()
							.getBaseContext());
					convertView = inflater.inflate(R.layout.item_history, parent,
							false);
					holder = new ViewHolder();

					holder.deliveryName = (TextView) convertView
							.findViewById(R.id.deliveryName);
					holder.deliveryAddress = (TextView) convertView
							.findViewById(R.id.deliveryAddress);
					holder.deliveryStatus = (TextView) convertView
							.findViewById(R.id.deliveryStatus);
					holder.redemptionDate = (TextView) convertView
							.findViewById(R.id.redemptionDate);

					convertView.setTag(holder);
				} else {
					holder = (ViewHolder) convertView.getTag();
				}

				final HistoryItem itemreward = mItems.get(position);

				holder.deliveryName.setText("Name : " + itemreward.name);
				holder.deliveryAddress.setText("Address : " + itemreward.develiryAddress);
				holder.deliveryStatus.setText("Status : " + itemreward.redemptionStatusName);
				holder.redemptionDate.setText("Date : " + itemreward.redemptionDate);

				convertView.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
/*
						Log.v(RewardsFragment.class.getSimpleName(),itemreward.rewardId.toString());
						Intent intent = new Intent(activity, RedeemActivity.class);
						intent.putExtra("RewardId",String.valueOf(itemreward.rewardId));
						intent.putExtra("Delivery",String.valueOf(itemreward.typeDelivery));
						intent.putExtra("Money",String.valueOf(itemreward.typeMoney));
						intent.putExtra("Mobile",String.valueOf(itemreward.Mobile));
						activity.startActivity(intent);*/
					}
				});
				
				return convertView;
			}

		}
}
