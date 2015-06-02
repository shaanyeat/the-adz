package com.lockscreen;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.webkit.JsResult;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;

@SuppressLint("JavascriptInterface") public class WebViewActivity extends Activity {

	private WebView webView;

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.webview);

		String value = getIntent().getExtras().getString("imageurl");

		webView = (WebView) findViewById(R.id.webView1);
		// webView.getSettings().setJavaScriptEnabled(true);

		webView.getSettings().setJavaScriptEnabled(true);

		// loads the WebView completely zoomed out
		webView.getSettings().setLoadWithOverviewMode(true);

		// true makes the Webview have a normal viewport such as a normal
		// desktop browser
		// when false the webview will have a viewport constrained to it's own
		// dimensions
		webView.getSettings().setUseWideViewPort(true);

		// override the web client to open all links in the same webview
		webView.setWebViewClient(new MyWebViewClient());
		webView.setWebChromeClient(new MyWebChromeClient());

		// Injects the supplied Java object into this WebView. The object is
		// injected into the
		// JavaScript context of the main frame, using the supplied name. This
		// allows the
		// Java object's public methods to be accessed from JavaScript.
		webView.addJavascriptInterface(new JavaScriptInterface(this), "Android");
		webView.loadUrl(value);
	}

	// customize your web view client to open links from your own site in the
	// same web view otherwise just open the default browser activity with the
	// URL
	private class MyWebViewClient extends WebViewClient {
		@Override
		public boolean shouldOverrideUrlLoading(WebView view, String url) {
			/*
			 * if (Uri.parse(url).getHost().equals("demo.mysamplecode.com")) {
			 * return false; } Intent intent = new Intent(Intent.ACTION_VIEW,
			 * Uri.parse(url)); startActivity(intent); return true;
			 */

			view.loadUrl(url);
			return false;
		}
	}

	private class MyWebChromeClient extends WebChromeClient {

		// display alert message in Web View
		@Override
		public boolean onJsAlert(WebView view, String url, String message,
				JsResult result) {
			Log.d("WebViewActivity", message);
			new AlertDialog.Builder(view.getContext()).setMessage(message)
					.setCancelable(true).show();
			result.confirm();
			return true;
		}

	}

	public class JavaScriptInterface {
		Context mContext;

		// Instantiate the interface and set the context
		JavaScriptInterface(Context c) {
			mContext = c;
		}

		// using Javascript to call the finish activity
		public void closeMyActivity() {
			finish();
		}

	}

	// Web view has record of all pages visited so you can go back and forth
	// just override the back button to go back in history if there is page
	// available for display
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if ((keyCode == KeyEvent.KEYCODE_BACK) && webView.canGoBack()) {
			webView.goBack();
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}

}
