package com.lockscreen.application;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONObject;

import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import com.lockscreen.R;
import com.lockscreen.adapter.AdapterDetailImage;
import com.lockscreen.utility.Constant;
import com.lockscreen.utility.RestClient;
import com.lockscreen.utility.SharedPreference;
import com.nostra13.universalimageloader.core.DisplayImageOptions;

public class CampaignDetails extends FragmentActivity {

	ImageView campaignImg;
	TextView campaignName, campaignMerchant, campaignLink;
	ViewPager details_imagePager;
	PagerAdapter adapter;

	DisplayImageOptions options;
	private SharedPreference pref;

	ArrayList<String> allImages;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.fragment_campaign_details);

		pref = new SharedPreference(CampaignDetails.this);
		options = new DisplayImageOptions.Builder().cacheInMemory(true)
				.cacheOnDisk(true).considerExifParams(true)
				.bitmapConfig(Bitmap.Config.RGB_565).build();
		String campaignId = getIntent().getExtras().getString("campaignId");

		campaignImg = (ImageView) findViewById(R.id.campaignImg);
		campaignName = (TextView) findViewById(R.id.campaignName);
		campaignMerchant = (TextView) findViewById(R.id.campaignMerchant);
		campaignLink = (TextView) findViewById(R.id.campaignLink);
		details_imagePager = (ViewPager) findViewById(R.id.details_imagePager);

		allImages = new ArrayList<String>();

		// get campaign Details
		new campaingDetails(CampaignDetails.this, campaignId)
				.execute((Void[]) null);

	}

	// getCampaignDetails
	private class campaingDetails extends AsyncTask<Void, Void, Integer> {
		private ProgressDialog dialog;
		private final Context context;
		private String cId;
		// String fieldName;
		String successcode = "0";
		String errormsg;

		Integer id, mId;
		String name, refercode, startdate, enddate, mName, imgId, imgName,
				imgUrl, ImgUrlLink, linkurl;

		public campaingDetails(Context context, String id) {
			this.context = context;
			this.cId = id;
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

				JSONObject campaignCoorandId = new JSONObject();

				campaignCoorandId.put("id", cId);
				campaignCoorandId.put("latitude", "3.31766");
				campaignCoorandId.put("longitude", "101.4839");

				RestClient client = new RestClient(Constant.BASEWEBSERVICEURL
						+ "campaign/view");

				client.AddHeader("Content-type", "application/json");
				client.AddHeader("Accept", "application/json");
				Log.v("KEY", pref.getapikey());
				client.AddHeader("Key", pref.getapikey());

				// client.Execute(RestClient.GET);
				Log.v("Campaign_Details", campaignCoorandId.toString());
				client.ExecuteHybridJsonPost(campaignCoorandId.toString());

				String response = client.getResponse();
				Log.v("Campaign_Details", response);
				// Create JSON Object
				JSONObject json = new JSONObject(response);
				if (!json.isNull("Result")) {

					JSONObject res = json.getJSONObject("Result");

					id = res.getInt("CampaignId");
					name = res.getString("Name");
					refercode = res.getString("ReferenceCode");
					startdate = res.getString("Start");
					enddate = res.getString("End");
					mId = res.getInt("MerchantId");
					mName = res.getString("MerchantName");
					imgId = res.getString("SubImageId");
					imgName = res.getString("SubImageName");

					JSONArray images = res.getJSONArray("SubImageUrl");
					for (int i = 0; i < images.length(); i++) {
						allImages.add(images.getString(i).replace("[", "")
								.replace("]", "").replace("\"", "")
								.replace("\\", ""));
					}

					// imgUrl = res.getString("SubImageUrl").replace("[",
					// "").replace("]", "").replace("\"", "").replace("\\","");
					ImgUrlLink = res.getString("SubImageUrlLink");
					linkurl = res.getString("LinkURL");

					// new CampaignItem(id, name, refercode, startdate, enddate,
					// mId, mName, imgId, imgName, imgUrl, ImgUrlLink,
					// linkurl);

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

			if (successcode.equals("1")) {

				// ImageLoader imageLoader = ImageLoader.getInstance();

				// Log.v("URL",imgUrl );
				//
				// imageLoader.displayImage(imgUrl,
				// campaignImg, options);

				adapter = new AdapterDetailImage(CampaignDetails.this,
						allImages);
				details_imagePager.setAdapter(adapter);

				campaignName.setText(name);
				campaignMerchant.setText(mName);
				campaignLink.setText(linkurl);

			}

			dialog.dismiss();
		}
	}

}
