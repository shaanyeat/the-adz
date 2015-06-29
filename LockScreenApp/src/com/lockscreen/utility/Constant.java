package com.lockscreen.utility;

/*Developer: TAI ZHEN KAI
Project 2015*/

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import com.lockscreen.adapter.UserItem;

public class Constant {

	public static String BASEWEBSERVICEURL = "http://theadzdemo.cloudapp.net/TheAdzAPI/";

	public static UserItem currentLoginUser;

	public static boolean isNetworkAvailable(Context cont) {
		ConnectivityManager connectivityManager = (ConnectivityManager) cont.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo activeNetworkInfo = connectivityManager
				.getActiveNetworkInfo();
		return activeNetworkInfo != null && activeNetworkInfo.isConnected();
	}
}
