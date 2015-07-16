package com.lockscreen.utility;

/*Developer: TAI ZHEN KAI
Project 2015*/

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

import com.facebook.Session;
import com.lockscreen.adapter.UserItem;

@SuppressLint("CommitPrefEdits") public class SharedPreference {

	SharedPreferences pref;
    Editor editor;
    Context _context;
    int PRIVATE_MODE = 0;
    private static final String PREF_NAME = "UserData";
    private static final String isLoggedIn = "isLogin";
     

    public static final String KEY_USERID = "userId";
    public static final String KEY_FIRSTNAME = "firstName";
    public static final String KEY_LASTNAME = "lastName";
    public static final String KEY_EMAIL = "email";
    public static final String KEY_GENDER = "gender";
    public static final String KEY_DATEOFBIRTH = "dateOfBirth";
    public static final String KEY_PASS = "password";
    public static final String KEY_STATUS = "status";
    public static final String KEY_IMAGEURL = "imageUrl";
    public static final String KEY_APIKEY = "apiKey";
    public static final String KEY_LAT = "latitude";
    public static final String KEY_LONG = "longtitude";
    
    public static final String KEY_VERSION = "applicationVersion";

    
    
    public SharedPreference(Context context){
        this._context = context;
        pref = _context.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
        editor = pref.edit();
    }
    
    public void isLogin(boolean isLogin){
        
    	editor.putBoolean(isLoggedIn,isLogin);
        
    	if(isLogin){

    		editor.putInt(KEY_USERID, Constant.currentLoginUser.getUserId());
    		editor.putString(KEY_FIRSTNAME, Constant.currentLoginUser.getFirstName());
    		editor.putString(KEY_LASTNAME, Constant.currentLoginUser.getLastName());
    		editor.putString(KEY_EMAIL, Constant.currentLoginUser.getEmail());
    		editor.putInt(KEY_GENDER, Constant.currentLoginUser.getGender());
    		editor.putString(KEY_DATEOFBIRTH, Constant.currentLoginUser.getdateofBirth());
    		editor.putString(KEY_PASS, Constant.currentLoginUser.getpassword());
    		editor.putString(KEY_STATUS, Constant.currentLoginUser.getStatus());
    		editor.putString(KEY_IMAGEURL, Constant.currentLoginUser.getimgUrl());
//    		editor.putString(KEY_APIKEY, Constant.currentLoginUser.getApiKey());
    	}
    	
    	
    	editor.commit();
    }
    
    public void setapikey(String apikey){
    	editor.putString(KEY_APIKEY, apikey);
    	editor.commit();
    }
    
    public String getapikey(){
    	return pref.getString(KEY_APIKEY, null);
    }
    
    public void setCoordinates(String latitude, String longtitude){
    	editor.putString(KEY_LAT, latitude);
    	editor.putString(KEY_LONG, longtitude);
    	editor.commit();
    }
    
    public String getCoordinatesLat(){
    	return pref.getString(KEY_LAT, null);
    }
    
    public String getCoordinatesLong(){
    	return pref.getString(KEY_LONG, null);
    }
    
    
    public void isFacebookLogin(boolean isFacebookLogin){
        
    	editor.putBoolean(isLoggedIn,isFacebookLogin);
    	editor.commit();
    }

    public void getUserDetails(){
    	
    	Constant.currentLoginUser = new UserItem();
    	Constant.currentLoginUser.setUserId(pref.getInt(KEY_USERID, 0));
    	Constant.currentLoginUser.setFirstName(pref.getString(KEY_FIRSTNAME, null));
    	Constant.currentLoginUser.setLastName(pref.getString(KEY_LASTNAME, null));
		Constant.currentLoginUser.setEmail(pref.getString(KEY_EMAIL, null));
		Constant.currentLoginUser.setGender(pref.getInt(KEY_GENDER, 0));
		Constant.currentLoginUser.setdateofBirth(pref.getString(KEY_DATEOFBIRTH, null));
		Constant.currentLoginUser.setpassword(pref.getString(KEY_PASS, null));
		Constant.currentLoginUser.setStatus(pref.getString(KEY_STATUS, null));
		Constant.currentLoginUser.setimgUrl(pref.getString(KEY_IMAGEURL, null));
//		Constant.currentLoginUser.setApiKey(pref.getString(KEY_APIKEY, null));
    }
    
    public void updateProfile(){
    	editor.putString(KEY_FIRSTNAME, Constant.currentLoginUser.getFirstName());
		editor.putString(KEY_LASTNAME, Constant.currentLoginUser.getLastName());
		editor.commit();
    }
    
    public void setAppVersion(String version){
    	editor.putString(KEY_VERSION, version);
    	editor.commit();
    }
    
    public String getAppVersion(){
    	return pref.getString(KEY_VERSION, null);
    }
    
    
    public boolean GetLogin(){
    	
    	boolean user_login = false;
    	
    	user_login = pref.getBoolean(isLoggedIn,user_login);
    	    	
        return user_login;
    }
  
    public void logoutUser(){
    	Session session = Session.getActiveSession();
		if (session != null) {

			if (!session.isClosed()) {
				session.closeAndClearTokenInformation();
				// clear your preferences if saved
			}
		} else {

			session = new Session(_context);
			Session.setActiveSession(session);

			session.closeAndClearTokenInformation();
			// clear your preferences if saved
		}
        editor.clear();
        editor.commit();
    }
    
    
    public void storeAdzImage(ArrayList<String> imgurl){
    	 Set<String> set = new HashSet<String>();
		 set.addAll(imgurl);
		 editor.putStringSet("imgOffline", set);
		 editor.commit();
    }
    
    
   public Set<String> getAdzImage(){
	   Set<String> set = pref.getStringSet("imgOffline", null);
	   
	   return set;
   }
}
