package com.lockscreen.utility;

/*Developer: TAI ZHEN KAI
 Project 2015*/

import java.io.ByteArrayOutputStream;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Base64;
import android.util.DisplayMetrics;

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
	    DisplayMetrics displayMetrics = Context.getResources().getDisplayMetrics();
	    int dp = Math.round(px / (displayMetrics.heightPixels / DisplayMetrics.DENSITY_DEFAULT));
	    return dp * 4;
	}

}
