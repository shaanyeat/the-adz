package com.lockscreen;

import android.app.Activity;
import android.os.Bundle;
import android.webkit.WebView;
import android.widget.Toast;

public class WebViewActivity extends Activity {

	private WebView webView;
	
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.webview);
		
		String value = getIntent().getExtras().getString("imageurl");
		
		webView = (WebView) findViewById(R.id.webView1);
//		webView.getSettings().setJavaScriptEnabled(true);
		webView.loadUrl(value);
	}
 
	
}
