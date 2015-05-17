package com.lockscreen.utility;


import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

import com.facebook.Session;
import com.lockscreen.adapter.UserDetails;

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
    public static final String KEY_NOTIF = "notif";
    public static final String KEY_IMAGEURL = "imageUrl";
    public static final String KEY_APIKEY = "apiKey";

    
    
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
    		editor.putString(KEY_NOTIF, Constant.currentLoginUser.getnotification());
    		editor.putString(KEY_IMAGEURL, Constant.currentLoginUser.getimgUrl());
    		editor.putString(KEY_APIKEY, Constant.currentLoginUser.getApiKey());
    	}
    	
    	
    	editor.commit();
    }
    
    public void setapikey(){
    	editor.putString(KEY_APIKEY, Constant.currentLoginUser.getApiKey());
    	editor.commit();
    }
    
    
    public void isFacebookLogin(boolean isFacebookLogin){
        
    	editor.putBoolean(isLoggedIn,isFacebookLogin);
    	editor.commit();
    }

    public void getUserDetails(){
    	
    	Constant.currentLoginUser = new UserDetails();
    	Constant.currentLoginUser.setUserId(pref.getInt(KEY_USERID, 0));
    	Constant.currentLoginUser.setFirstName(pref.getString(KEY_FIRSTNAME, null));
    	Constant.currentLoginUser.setLastName(pref.getString(KEY_LASTNAME, null));
		Constant.currentLoginUser.setEmail(pref.getString(KEY_EMAIL, null));
		Constant.currentLoginUser.setGender(pref.getInt(KEY_GENDER, 0));
		Constant.currentLoginUser.setdateofBirth(pref.getString(KEY_DATEOFBIRTH, null));
		Constant.currentLoginUser.setpassword(pref.getString(KEY_PASS, null));
		Constant.currentLoginUser.setnotification(pref.getString(KEY_NOTIF, null));
		Constant.currentLoginUser.setimgUrl(pref.getString(KEY_IMAGEURL, null));
		Constant.currentLoginUser.setApiKey(pref.getString(KEY_APIKEY, null));
    }
    
    
    public boolean GetLogin(){
    	
    	boolean user_login = false;
    	
    	user_login = pref.getBoolean(isLoggedIn,user_login);
    	    	
        return user_login;
    }
  
    public void logoutUser(){
    	if (Session.getActiveSession() != null) {
			Session.getActiveSession().closeAndClearTokenInformation();
		}
		Session.setActiveSession(null);
        editor.clear();
        editor.commit();
    }
}