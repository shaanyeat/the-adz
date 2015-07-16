package com.lockscreen.application;

/*Developer: TAI ZHEN KAI
 Project 2015*/

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import org.json.JSONObject;

import android.app.ActionBar;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.FragmentActivity;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.Session;
import com.lockscreen.R;
import com.lockscreen.adapter.UserItem;
import com.lockscreen.fragment.LoginFragment;
import com.lockscreen.utility.Constant;
import com.lockscreen.utility.RestClient;
import com.lockscreen.utility.SharedPreference;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

public class ProfileActivity extends FragmentActivity {

	TextView logout, userPoints, userFullName;
	EditText firstName, lastName, email;
	ImageView profilePic;
	Button btnInviteFriends, btnInterest, btnUpdateProfile;

	ActionBar actionBar;
	private SharedPreference pref;
	DisplayImageOptions options;

	private Uri fileUri;
	private static final String IMAGE_DIRECTORY_NAME = "The Adz";
	String imageBase64Url, imageMimeType;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.fragment_user_details);

		pref = new SharedPreference(this);

		actionBar = getActionBar();
		actionBar.setDisplayHomeAsUpEnabled(true);

		options = new DisplayImageOptions.Builder().cacheInMemory(false)
				.cacheOnDisk(true).considerExifParams(true)
				.bitmapConfig(Bitmap.Config.RGB_565).build();

		logout = (TextView) findViewById(R.id.logout);
		userPoints = (TextView) findViewById(R.id.userPoints);
		userFullName = (TextView) findViewById(R.id.userFullName);
		firstName = (EditText) findViewById(R.id.firstName);
		lastName = (EditText) findViewById(R.id.lastName);
		email = (EditText) findViewById(R.id.email);
		email.setKeyListener(null);
		profilePic = (ImageView) findViewById(R.id.profilePic);
		btnInviteFriends = (Button) findViewById(R.id.btnInviteFriends);
		btnInterest = (Button) findViewById(R.id.btnInterest);
		btnUpdateProfile = (Button) findViewById(R.id.btnUpdateProfile);

		// Set Value
		userPoints.setText(Constant.currentLoginUser.getPoints() + " Points");
		userFullName.setText(Constant.currentLoginUser.getFirstName() + " "
				+ Constant.currentLoginUser.getLastName());
		firstName.setText(Constant.currentLoginUser.getFirstName());
		lastName.setText(Constant.currentLoginUser.getLastName());
		email.setText(Constant.currentLoginUser.getEmail());

		ImageLoader imageLoader = ImageLoader.getInstance();
		imageLoader.init(ImageLoaderConfiguration
				.createDefault(ProfileActivity.this));

		Log.v("Url", Constant.currentLoginUser.getimgUrl());

		profilePic.setBackground(null);
		imageLoader.displayImage(Constant.currentLoginUser.getimgUrl(),
				profilePic, options);

		profilePic.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				selectImage();
			}
		});

		logout.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

				// Facebook logout
				LogoutFB(ProfileActivity.this);

				pref.logoutUser();
				Intent x = new Intent(ProfileActivity.this, LoginFragment.class);
				startActivity(x);
				ProfileActivity.this.finish();
			}
		});

		btnInviteFriends.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Toast.makeText(ProfileActivity.this,
						"Coming Soon, Stay Tuned!", Toast.LENGTH_LONG).show();
			}
		});

		btnInterest.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Toast.makeText(ProfileActivity.this,
						"Coming Soon, Stay Tuned!", Toast.LENGTH_LONG).show();
			}
		});

		btnUpdateProfile.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				new updateProfile(ProfileActivity.this).execute((Void[]) null);	
			}
		});

	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			Intent x = new Intent(ProfileActivity.this, HomeActivity.class);
			startActivity(x);
			ProfileActivity.this.finish();
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}

	public static void LogoutFB(Context context) {
		Session session = Session.getActiveSession();
		if (session != null) {

			if (!session.isClosed()) {
				session.closeAndClearTokenInformation();
				// clear your preferences if saved
			}
		} else {

			session = new Session(context);
			Session.setActiveSession(session);

			session.closeAndClearTokenInformation();
			// clear your preferences if saved
		}

	}

	@Override
	public void onBackPressed() {
		Intent x = new Intent(ProfileActivity.this, HomeActivity.class);
		startActivity(x);
		ProfileActivity.this.finish();
	}

	private void selectImage() {

		final CharSequence[] options = { "Take a Photo", "Choose from Gallery",
				"Cancel" };

		AlertDialog.Builder builder = new AlertDialog.Builder(
				ProfileActivity.this);
		builder.setTitle("Set Photo!");
		builder.setItems(options, new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int item) {
				if (options[item].equals("Take a Photo")) {
					Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
					fileUri = getOutputMediaFileUri(1);
					intent.putExtra(MediaStore.EXTRA_OUTPUT, fileUri);
					startActivityForResult(intent, 1);

				} else if (options[item].equals("Choose from Gallery")) {
					Intent intent = new Intent(
							Intent.ACTION_PICK,
							android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
					startActivityForResult(intent, 2);

				} else if (options[item].equals("Cancel")) {
					dialog.dismiss();
				}
			}
		});
		builder.show();
	}

	public Uri getOutputMediaFileUri(int type) {
		return Uri.fromFile(getOutputMediaFile(type));
	}

	private static File getOutputMediaFile(int type) {

		// External sdcard location
		File mediaStorageDir = new File(
				Environment
						.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES),
				"The Adz");

		// Create the storage directory if it does not exist
		if (!mediaStorageDir.exists()) {
			if (!mediaStorageDir.mkdirs()) {
				Log.d(IMAGE_DIRECTORY_NAME, "Oops! Failed create "
						+ IMAGE_DIRECTORY_NAME + " directory");
				return null;
			}
		}

		// Create a media file name
		String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss",
				Locale.getDefault()).format(new Date());
		File mediaFile;
		if (type == 1) {
			mediaFile = new File(mediaStorageDir.getPath() + File.separator
					+ "IMG_" + timeStamp + ".jpg");
		} else {
			return null;
		}

		return mediaFile;
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (resultCode == RESULT_OK) {
			if (requestCode == 1) {

				try {
					Bitmap bitmap;
					BitmapFactory.Options bitmapOptions = new BitmapFactory.Options();

					bitmap = BitmapFactory.decodeFile(fileUri.getPath(),
							bitmapOptions);
					profilePic.setBackground(null);
					profilePic.setImageBitmap(bitmap);

					String outPath = new File(fileUri.getPath()).getPath();
					File file = new File(outPath);

					OutputStream outFile = null;

					try {
						outFile = new FileOutputStream(file);
						bitmap.compress(Bitmap.CompressFormat.JPEG, 85, outFile);
						outFile.flush();
						outFile.close();
						String filePath = file.getPath();
						String imageName = file.getName();
						// Toast.makeText(this, filePath,
						// Toast.LENGTH_LONG).show();
						imageMimeType = filePath.substring(filePath
								.lastIndexOf(".") + 1);
						Log.w("Image Type from gallery......******************.........",
								imageMimeType + "");
						imageBase64Url = Constant.encodeTobase64(filePath);

						// new
						// updateProfileTask(Profile.this,"Image").execute();
					} catch (FileNotFoundException e) {
						e.printStackTrace();
					} catch (IOException e) {
						e.printStackTrace();
					} catch (Exception e) {
						e.printStackTrace();
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			} else if (requestCode == 2) {

				Uri selectedImage = data.getData();
				String[] filePath = { MediaStore.Images.Media.DATA };
				Cursor c = getContentResolver().query(selectedImage, filePath,
						null, null, null);
				c.moveToFirst();
				int columnIndex = c.getColumnIndex(filePath[0]);
				String picturePath = c.getString(columnIndex);
				c.close();
				// Bitmap thumbnail = (BitmapFactory.decodeFile(picturePath));

				BitmapFactory.Options options = new BitmapFactory.Options();
				options.inSampleSize = 6;

				Bitmap thumbnail = (BitmapFactory.decodeFile(picturePath,
						options));

				Log.w("Path of image from gallery......******************.........",
						picturePath + "");
				profilePic.setBackground(null);
				profilePic.setImageBitmap(thumbnail);

				File f = new File(picturePath);
				String imageName = f.getName();

				imageMimeType = picturePath.substring(picturePath
						.lastIndexOf(".") + 1);
				Log.w("Image Type from gallery......******************.........",
						imageMimeType + "");
				imageBase64Url = Constant.encodeTobase64(picturePath);
				// Constant.currentLoginUser.setImageName(imageName);

				// new updateProfileTask(Profile.this,"Image").execute();
			}
		} else {
			System.out.println("MEDIA FAILLL");
		}
	}

	// Web Services
	// Update Profile
	private class updateProfile extends AsyncTask<Void, Void, Integer> {
		private ProgressDialog dialog;
		private final Context context;
		// String fieldName;
		String successcode;

		public updateProfile(Context context) {
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

				newUser.put("FirstName", firstName.getText().toString());
				newUser.put("LastName", lastName.getText().toString());
				newUser.put("ImageByteString", imageBase64Url);
				newUser.put("ImageMimeType", imageMimeType);

				JSONObject devInfo = new JSONObject();

				devInfo.put("OS", "Android");
				devInfo.put("OS_Version", android.os.Build.VERSION.RELEASE);
				devInfo.put("Model", Build.MODEL);
				TelephonyManager telephonyManager = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
				devInfo.put("UniqueId", telephonyManager.getDeviceId());
				newUser.put("DeviceInfo", devInfo);

				RestClient client = new RestClient(Constant.BASEWEBSERVICEURL
						+ "user/update");
				client.AddHeader("Content-type", "application/json");
				client.AddHeader("Accept", "application/json");
				Log.v("KEY", pref.getapikey());
				client.AddHeader("Key", pref.getapikey());

				client.ExecuteHybridJsonPost(newUser.toString());

				Log.v("LOCKSCREEN_UPDATE_USER", newUser.toString());
				String response = client.getResponse();
				Log.v("LOCKSCREEN_UPDATE_USER_RESULTS", response);
				JSONObject json = new JSONObject(response);

				if (json.isNull("Errors")) {
					JSONObject reststatus = json
							.getJSONObject("ResponseStatus");
					successcode = reststatus.getString("Success");

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
				Constant.currentLoginUser.setFirstName(firstName.getText().toString());
				Constant.currentLoginUser.setLastName(lastName.getText().toString());
				
				//update details in sharedpreference
				pref.updateProfile();

				Toast.makeText(context, R.string.updatesuccess, Toast.LENGTH_LONG).show();
			}
			dialog.dismiss();
		}
	}

}
