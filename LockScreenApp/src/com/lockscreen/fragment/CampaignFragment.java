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
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.etsy.android.grid.StaggeredGridView;
import com.lockscreen.R;
import com.lockscreen.adapter.CampaignItem;
import com.lockscreen.application.CampaignDetails;
import com.lockscreen.utility.Constant;
import com.lockscreen.utility.RestClient;
import com.lockscreen.utility.SharedPreference;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

public class CampaignFragment extends Fragment {

	StaggeredGridView campaignGrid;
	private ArrayList<CampaignItem> cItems;
	DisplayImageOptions options;
	
	private SharedPreference pref;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		View rootView = inflater.inflate(R.layout.fragment_campaign, container,
				false);
		
		pref = new SharedPreference(getActivity());

		options = new DisplayImageOptions.Builder()
				.cacheInMemory(false)
				.cacheOnDisk(true).considerExifParams(true)
				.bitmapConfig(Bitmap.Config.RGB_565).build();

		cItems = new ArrayList<CampaignItem>();

		campaignGrid = (StaggeredGridView) rootView.findViewById(R.id.campaignGrid);

		// get campaign List
		new campaingList(getActivity()).execute((Void[]) null);

		return rootView;
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
			dialog = ProgressDialog.show(context, "",
					getResources().getString(R.string.progesschecking), true);
			dialog.show();
		}

		@Override
		protected Integer doInBackground(Void... ignored) {
			Integer returnCode = 0;
			try {
				RestClient client = new RestClient(Constant.BASEWEBSERVICEURL
						+ "campaign/list");

				client.AddHeader("Content-type", "application/json");
				client.AddHeader("Accept", "application/json");
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
						Integer imgWidth = res.getInt("ImageWidth");
						Integer imgHeight = res.getInt("ImageHeight");
						
						cItems.add(new CampaignItem(id, name, mId, mName, imgName,imgUrl, linkurl, imgWidth, imgHeight));
						

					}

					JSONObject reststatus = json
							.getJSONObject("ResponseStatus");
					successcode = reststatus.getString("Success");

					String apiKey = json.getString("Key");

					// Constant.currentLoginUser = new UserDetails();
//					Constant.currentLoginUser.setApiKey(apiKey);
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

			if (successcode.equals("1")) {
				
				ItemAdapter adapter = new ItemAdapter(getActivity(), cItems);
				campaignGrid.setAdapter(adapter);

			}

			dialog.dismiss();
		}
	}

	public class ItemAdapter extends BaseAdapter {
		ArrayList<CampaignItem> mItems;
		private Activity activity;

		public ItemAdapter(Activity activity, ArrayList<CampaignItem> items) {
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

			public TextView cName;
			public TextView campaignMerchantName;
			public ImageView campaignImg;
			public RelativeLayout itemlayout;

		}

		@Override
		public View getView(final int position, View convertView,
				ViewGroup parent) {

			ViewHolder holder;

			if (convertView == null) {
				LayoutInflater inflater = LayoutInflater.from(activity);
				convertView = inflater.inflate(R.layout.campaign_list_item,
						parent, false);
				holder = new ViewHolder();

				holder.itemlayout = (RelativeLayout) convertView.findViewById(R.id.itemlayout);
				
				holder.cName = (TextView) convertView
						.findViewById(R.id.campaignName);
				holder.campaignMerchantName = (TextView) convertView
						.findViewById(R.id.campaignMerchantName);
				holder.campaignImg = (ImageView) convertView
						.findViewById(R.id.campaignImg);

				convertView.setTag(holder);
			} else {
				holder = (ViewHolder) convertView.getTag();
			}
			final CampaignItem item = mItems.get(position);

			holder.cName.setText(item.campaignName);
			holder.campaignMerchantName.setText(item.merchanName);
			
			Log.v("Height", Integer.toString(Constant.pxToDp(item.imgHeight, activity)));
			
			holder.campaignImg.getLayoutParams().height = Constant.pxToDp(item.imgHeight, activity);
//			holder.campaignImg.getLayoutParams().width = Constant.pxToDp(item.imgWidth, activity);
			
			holder.campaignImg.requestLayout();
			
			ImageLoader imageLoader = ImageLoader.getInstance();
			imageLoader.init(ImageLoaderConfiguration.createDefault(getActivity()));
			
			Log.v("IMAGE URL", item.subImgUrl);
			
			imageLoader.displayImage(item.subImgUrl,
					holder.campaignImg, options);

			holder.itemlayout.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					Intent intent = new Intent(activity, CampaignDetails.class);
					intent.putExtra("campaignId",String.valueOf(item.campaignId));
					activity.startActivity(intent);
				}
			});

			return convertView;
		}
	}

}
