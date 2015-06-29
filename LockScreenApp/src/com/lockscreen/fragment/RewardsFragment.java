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
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.lockscreen.R;
import com.lockscreen.adapter.RewardItem;
import com.lockscreen.application.CampaignDetails;
import com.lockscreen.application.RedeemActivity;
import com.lockscreen.utility.Constant;
import com.lockscreen.utility.RestClient;
import com.lockscreen.utility.SharedPreference;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

public class RewardsFragment extends Fragment {

	GridView reward_grid;
	SharedPreference pref;

	private ArrayList<RewardItem> cItems;
	DisplayImageOptions options;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		View rootView = inflater.inflate(R.layout.fragment_reward, container,
				false);

		reward_grid = (GridView) rootView.findViewById(R.id.reward_grid);

		pref = new SharedPreference(getActivity());

		cItems = new ArrayList<RewardItem>();

		options = new DisplayImageOptions.Builder().cacheInMemory(true)
				.cacheOnDisk(true).considerExifParams(true)
				.bitmapConfig(Bitmap.Config.RGB_565).build();

		// getRewardsList
		new rewardsList(getActivity()).execute((Void[]) null);

		return rootView;
	}

	// getRewardsList
	private class rewardsList extends AsyncTask<Void, Void, Integer> {
		private ProgressDialog dialog;
		private final Context context;
		// String fieldName;
		String successcode = "0";
		String errormsg;

		public rewardsList(Context context) {
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
				RestClient client = new RestClient(Constant.BASEWEBSERVICEURL
						+ "reward/list");

				client.AddHeader("Content-type", "application/json");
				client.AddHeader("Accept", "application/json");
				Log.v("KEY", pref.getapikey());
				client.AddHeader("Key", pref.getapikey());

				client.Execute(RestClient.GET);

				String response = client.getResponse();
				Log.v("Rewards_List", response);
				// Create JSON Object
				JSONObject json = new JSONObject(response);
				if (!json.isNull("Result")) {

					JSONArray data = json.getJSONArray(("Result"));
					for (int i = 0; i < data.length(); i++) {
						JSONObject res = data.getJSONObject(i);

						Integer id = res.getInt("RewardId");
						String name = res.getString("Name");
						String description = null;
						if (! res.isNull("Description")){
						 description = res.getString("Description");
						}
						String subImageId = res.getString("SubImageId");
						String subImageName = res.getString("SubImageName");
						String subImageUrl = res.getString("SubImageUrl").replace("[", "")
								.replace("]", "").replace("\"", "")
								.replace("\\", "");
						String subImageLink = res.getString("SubImageUrlLink");
						Integer typeId = res.getInt("RewardTypeId");
						String typeName = res.getString("RewardTypeName");
						Boolean typeDeliver = res
								.getBoolean("RewardTypeDelivery");
						Boolean typeMoney = res
								.getBoolean("RewardTypeMoneyTransfer");
						Boolean typeMobile = res.getBoolean("RewardTypeMobile");

						cItems.add(new RewardItem(id, name, description,
								subImageId, subImageName, subImageUrl,
								subImageLink, typeId, typeName, typeDeliver,
								typeMoney, typeMobile));
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
			reward_grid.setAdapter(adapter);

			dialog.dismiss();
		}

	}

	public class ItemAdapter extends BaseAdapter {
		ArrayList<RewardItem> mItems;
		private Activity activity;

		// private Context context;

		public ItemAdapter(Activity activity, ArrayList<RewardItem> items) {
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
			public ImageView productImage;
			public TextView productName;
			public ImageButton productOverflow;
			public TextView productDesc;
		}

		@Override
		public View getView(final int position, View convertView,
				ViewGroup parent) {

			ViewHolder holder;

			if (convertView == null) {
				LayoutInflater inflater = LayoutInflater.from(getActivity()
						.getBaseContext());
				convertView = inflater.inflate(R.layout.item_reward, parent,
						false);
				holder = new ViewHolder();

				holder.productImage = (ImageView) convertView
						.findViewById(R.id.productImage);
				holder.productName = (TextView) convertView
						.findViewById(R.id.productName);
				holder.productDesc = (TextView) convertView
						.findViewById(R.id.productDesc);

				convertView.setTag(holder);
			} else {
				holder = (ViewHolder) convertView.getTag();
			}

			final RewardItem itemreward = mItems.get(position);

			holder.productName.setText(itemreward.name);
			holder.productDesc.setText(itemreward.description);

			ImageLoader.getInstance().displayImage(itemreward.imageUrl,
					holder.productImage, options);

			convertView.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub

					Log.v(RewardsFragment.class.getSimpleName(),itemreward.rewardId.toString());
					Intent intent = new Intent(activity, RedeemActivity.class);
					intent.putExtra("RewardId",String.valueOf(itemreward.rewardId));
					intent.putExtra("Delivery",String.valueOf(itemreward.typeDelivery));
					intent.putExtra("Money",String.valueOf(itemreward.typeMoney));
					intent.putExtra("Mobile",String.valueOf(itemreward.Mobile));
					activity.startActivity(intent);
				}
			});
			
			return convertView;
		}

	}

}
