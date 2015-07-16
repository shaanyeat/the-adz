package com.lockscreen.utility;

/*Developer: TAI ZHEN KAI
 Project 2015*/

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Environment;
import android.util.Base64;
import android.util.DisplayMetrics;
import android.widget.Toast;

import com.lockscreen.adapter.UserItem;

public class Constant {

	public static String BASEWEBSERVICEURL = "http://theadzdemo.cloudapp.net/TheAdzAPI/";

	public static UserItem currentLoginUser;

	public static boolean isNetworkAvailable(Context cont) {
		ConnectivityManager connectivityManager = (ConnectivityManager) cont
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo activeNetworkInfo = connectivityManager
				.getActiveNetworkInfo();
		return activeNetworkInfo != null && activeNetworkInfo.isConnected();
	}

	public static String encodeTobase64(String reImage) {

		Bitmap bm = decodeFile(reImage);
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		bm.compress(Bitmap.CompressFormat.JPEG, 100, baos); // bm is the bitmap
															// object
		byte[] b = baos.toByteArray();
		String imageEncoded = Base64.encodeToString(b, Base64.DEFAULT);

		return imageEncoded;
	}

	public static Bitmap decodeFile(String filePath) {

		// Decode image size
		BitmapFactory.Options o = new BitmapFactory.Options();
		o.inJustDecodeBounds = true;
		BitmapFactory.decodeFile(filePath, o);

		// The new size we want to scale to
		final int REQUIRED_SIZE = 400;

		// Find the correct scale value. It should be the power of 2.
		int width_tmp = o.outWidth, height_tmp = o.outHeight;
		int scale = 1;
		while (true) {
			if (width_tmp / 2 < REQUIRED_SIZE || height_tmp / 2 < REQUIRED_SIZE)
				break;
			width_tmp /= 2;
			height_tmp /= 2;
			scale *= 2;
		}

		// Decode with inSampleSize
		BitmapFactory.Options o2 = new BitmapFactory.Options();
		o2.inSampleSize = scale;
		return BitmapFactory.decodeFile(filePath, o2);
	}

	public static int pxToDp(int px, Context Context) {
		DisplayMetrics displayMetrics = Context.getResources()
				.getDisplayMetrics();
		int dp = Math
				.round(px
						/ (displayMetrics.heightPixels / DisplayMetrics.DENSITY_DEFAULT));
		return dp * 4;

		// DisplayMetrics displayMetrics =
		// Context.getResources().getDisplayMetrics();
		// int dp = px / (displayMetrics.densityDpi / 160);
		// return dp;
	}

	public static void saveImagetoSD(Bitmap bitmap, Context context,
			String fileName, Integer folderId) {
		// folderId = 1 = campaign folder
		// folderId = 2 = reward folder

		if (checkFileExist(fileName, folderId) == true) {
			// do ntg
		} else {

			OutputStream output;

			// Find the SD Card path
			File filepath = Environment.getExternalStorageDirectory();

			File dir = null;

			// Create a new folder in SD Card
			if (folderId == 1) {
				dir = new File(filepath.getAbsolutePath() + "/The Adz/Campaign");

			} else if (folderId == 2) {
				dir = new File(filepath.getAbsolutePath() + "/The Adz/Reward");
			}
			dir.mkdirs();

			// Create a name for the saved image
			File file = new File(dir, fileName + ".png");

			// Show a toast message on successful save
			// Toast.makeText(context,
			// "Image Saved to SD Card",Toast.LENGTH_SHORT).show();
			try {

				output = new FileOutputStream(file);

				// Compress into png format image from 0% - 100%
				bitmap.compress(Bitmap.CompressFormat.PNG, 100, output);
				output.flush();
				output.close();
			}

			catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	public static boolean checkFileExist(String fileName, Integer folderId) {

		// Find the SD Card path
		File filepath = Environment.getExternalStorageDirectory();

		File dir = null;

		// Create a new folder in SD Card
		if (folderId == 1) {
			dir = new File(filepath.getAbsolutePath() + "/The Adz/Campaign");

		} else if (folderId == 2) {
			dir = new File(filepath.getAbsolutePath() + "/The Adz/Reward");
		}
		dir.mkdirs();

		// Create a name for the saved image
		File file = new File(dir, fileName + ".png");

		if (file.exists()) {
			return true;
		} else {
			return false;
		}
	}

	public static class getBitmap extends AsyncTask<Void, Void, Void> {
		private Exception exception;
		private Context context;
		private String imgsrc;
		Bitmap myBitmap;
		String fileName;
		Integer folderId;

		public getBitmap(Context context, String imgurl, String fileName,
				Integer folderId) {
			this.context = context;
			this.imgsrc = imgurl;
			this.fileName = fileName;
			this.folderId = folderId;
		}

		protected Void doInBackground(Void... ignored) {
			try {
				URL url = new URL(imgsrc);
				HttpURLConnection connection = (HttpURLConnection) url
						.openConnection();
				connection.setDoInput(true);
				connection.connect();
				InputStream input = connection.getInputStream();
				myBitmap = BitmapFactory.decodeStream(input);
			} catch (IOException e) {
				e.printStackTrace();
			}
			return null;
		}

		protected void onPostExecute(Void feed) {
			// TODO:
			saveImagetoSD(myBitmap, context, fileName, folderId);
		}
	}

	public static Bitmap getResizedBitmap(Bitmap image, int maxSize) {
		int width = image.getWidth();
		int height = image.getHeight();

		float bitmapRatio = (float) width / (float) height;
		if (bitmapRatio > 0) {
			width = maxSize;
			height = (int) (width / bitmapRatio);
		} else {
			height = maxSize;
			width = (int) (height * bitmapRatio);
		}
		return Bitmap.createScaledBitmap(image, width, height, true);
	}

}
