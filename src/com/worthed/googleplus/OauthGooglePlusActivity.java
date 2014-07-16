package com.worthed.googleplus;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import android.os.Bundle;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.graphics.Bitmap;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.webkit.JsResult;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

import com.google.gson.Gson;
import com.worthed.googleplus.R;

@SuppressLint("SetJavaScriptEnabled")
public class OauthGooglePlusActivity extends Activity {
	private final String TAG = OauthGooglePlusActivity.class.getSimpleName();

	public static final String GOOGLE_CLIENT_ID = "660350728293-mrfnpd6rt7bbp8iledmti9lr24hiqas3.apps.googleusercontent.com";
	public static final String GOOGLE_CLIENT_SECRET = "rJgBB2w69vKmcTB2Ml8mzH3F";
	public static final String GOOGLE_BASE_URL = "https://accounts.google.com/o/oauth2/auth";
	public static final String GOOGLE_REDIRECT_URI = "https://www.worthed.com/oauth2callback";
	public static final String GOOGLE_CODE_RESPONSE = "https://oauth2-login-demo.appspot.com/";

	public static final String GOOGLE_LOGIN_URL = GOOGLE_BASE_URL
			+ "?client_id="
			+ GOOGLE_CLIENT_ID
			+ "&redirect_uri="
			+ GOOGLE_REDIRECT_URI
			+ "&response_type=code"
			+ "&state="
			+ "DGP"
			+ new Random().nextInt()
			+ "FRC"
			+ new Random().nextDouble()
			+ "&scope=https://www.googleapis.com/auth/plus.login https://www.googleapis.com/auth/userinfo.email https://www.googleapis.com/auth/userinfo.profile https://www.google.com/reader/api/0/subscription https://www.googleapis.com/auth/plus.me"
			+ "&approval_prompt=force&access_type=offline&data-requestvisibleactions=http://schemas.google.com/AddActivity";

	public static final String GOOGLE_URL_CODE = "https://accounts.google.com/o/oauth2/device/code";
	public static final String GOOGLE_URL_TOKEN = "https://accounts.google.com/o/oauth2/token";

	// Maybe useful in the future.
	public static final String GOOGLE_URL_USERID = "https://www.googleapis.com/plus/v1/people/me?access_token=token&fields=id";
	// https://www.googleapis.com/oauth2/v1/tokeninfo?access_token=1/fFBGRNJru1FQd44AzqT3Zg
	// https://developers.google.com/+/api/latest/people/get
	
	private WebView oauthWebView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Log.v(TAG, "onCreate()");
		setContentView(R.layout.activity_oauth_google_plus);
		initWebView();
	}

	private void initWebView() {
		oauthWebView = (WebView) findViewById(R.id.oauth_googleplus_webview);
		oauthWebView.clearCache(true);
		oauthWebView.getSettings().setCacheMode(WebSettings.LOAD_NO_CACHE);
		oauthWebView.setWebChromeClient(new WebChromeClient() {
			@Override
			public boolean onJsConfirm(WebView view, String url,
					String message, JsResult result) {
				// TODO Auto-generated method stub
				Log.d(TAG, "ChromeClient url : " + url);
				handlerUrl(url);
				return super.onJsConfirm(view, url, message, result);
			}

			@Override
			public void onProgressChanged(WebView view, int newProgress) {
				// TODO Auto-generated method stub
				super.onProgressChanged(view, newProgress);
			}
		});
		oauthWebView.setWebViewClient(new WebViewClient() {
			@Override
			public void onPageStarted(WebView view, String url, Bitmap favicon) {
				// TODO Auto-generated method stub
				Log.d(TAG, "WebViewClient start url : " + url);
				handlerUrl(url);
				super.onPageStarted(view, url, favicon);
			}

			@Override
			public void onPageFinished(WebView view, String url) {
				// TODO Auto-generated method stub
				// handler.sendEmptyMessage(PROGRESS_HIDE);
				Log.d(TAG, "WebViewClient finish url : " + url);
				super.onPageFinished(view, url);
			}
		});
		loading(GOOGLE_LOGIN_URL);
	}

	private void loading(String url) {
		Log.d(TAG, "loading HTTP " + url);
		oauthWebView.getSettings().setJavaScriptEnabled(true);
		oauthWebView.getSettings().setSupportZoom(true);
		oauthWebView.getSettings().setBuiltInZoomControls(true);
		oauthWebView.getSettings().setCacheMode(WebSettings.LOAD_NO_CACHE);
		oauthWebView.loadUrl(url);
	}

	private void handlerUrl(String url) {
		Log.v(TAG, "handlerUrl()");
		String code = TokenUtils.getCode(url);
		String error = TokenUtils.getError(url);
		if (!TextUtils.isEmpty(code)) {
			if (OauthGooglePlusActivity.this != null) {
				Toast.makeText(OauthGooglePlusActivity.this, "code : " + code,
						Toast.LENGTH_SHORT).show();
			}
			Log.e(TAG, "code : " + code);
			requestToken(code);
		} else if (!TextUtils.isEmpty(error)) {
			if (OauthGooglePlusActivity.this != null) {
				Toast.makeText(OauthGooglePlusActivity.this,
						"error : " + error, Toast.LENGTH_SHORT).show();
			}
			Log.e(TAG, "error : " + error);
		} else {
			Log.w(TAG, "Get no code or token!");
		}
	}

	private void requestToken(final String code) {
		new Thread(new Runnable() {
			
			@Override
			public void run() {
				// TODO Auto-generated method stub
				List<NameValuePair> params = new ArrayList<NameValuePair>();
				params.add(new BasicNameValuePair("code", code));
				params.add(new BasicNameValuePair("client_id", GOOGLE_CLIENT_ID));
				params.add(new BasicNameValuePair("client_secret", GOOGLE_CLIENT_SECRET));
				params.add(new BasicNameValuePair("redirect_uri", GOOGLE_REDIRECT_URI));
				params.add(new BasicNameValuePair("grant_type", "authorization_code"));
				
				String responseStr = new HttpUtils().doPost(GOOGLE_URL_TOKEN, params);
				if (!TextUtils.isEmpty(responseStr) && !responseStr.contains(HttpUtils.ERROR_PREFIX)) {
					Gson gson = new Gson();
					GoogleToken googleToken = gson.fromJson(responseStr, GoogleToken.class);
					if (googleToken != null) {
						Log.e(TAG, "Google Access Token  : " + googleToken.getAccess_token());
						Log.d(TAG, "Google Expires       : " + googleToken.getExpires_in());
						Log.d(TAG, "Google Refresh Token : " + googleToken.getRefresh_token());
						Log.d(TAG, "Google Token Type    : " + googleToken.getToken_type());
					}
				} else {
					Log.w(TAG, "token response is null");
				}
			}
		}).start();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.oauth_google_plus, menu);
		return true;
	}

}
