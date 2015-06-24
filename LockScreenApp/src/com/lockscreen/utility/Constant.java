package com.lockscreen.utility;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import com.lockscreen.adapter.UserDetails;

public class Constant {

	public static String BASEWEBSERVICEURL = "http://theadzdemo.cloudapp.net/TheAdzAPI/";

	public static UserDetails currentLoginUser;

	public static boolean isNetworkAvailable(Context cont) {
		ConnectivityManager connectivityManager = (ConnectivityManager) cont.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo activeNetworkInfo = connectivityManager
				.getActiveNetworkInfo();
		return activeNetworkInfo != null && activeNetworkInfo.isConnected();
	}
}
