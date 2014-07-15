package com.worthed.googleplus;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import android.os.Bundle;
import android.app.Activity;
import android.app.PendingIntent;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.GoogleApiClient.ConnectionCallbacks;
import com.google.android.gms.common.api.GoogleApiClient.OnConnectionFailedListener;
import com.google.android.gms.plus.Plus;
import com.google.android.gms.plus.model.people.Person;
import com.google.gson.Gson;
import com.worthed.googleplus.R;

public class OauthGooglePlusActivity extends Activity implements
		ConnectionCallbacks, OnConnectionFailedListener {
	private final String TAG = OauthGooglePlusActivity.class.getSimpleName();

	public static final String GOOGLE_CLIENT_ID = "660350728293-197gudu5u4t66agg4qcpvg0im14icgtn.apps.googleusercontent.com";
	public static final String GOOGLE_URL_CODE = "https://accounts.google.com/o/oauth2/device/code";
	public static final String GOOGLE_URL_TOKEN = "https://accounts.google.com/o/oauth2/token";
	
	/* Request code used to invoke sign in user interactions. */
	private static final int RC_SIGN_IN = 0;

	/* Client used to interact with Google APIs. */
	private GoogleApiClient mGoogleApiClient;

	/*
	 * A flag indicating that a PendingIntent is in progress and prevents us
	 * from starting further intents.
	 */
	private boolean mIntentInProgress;

	// Used to store the PendingIntent most recently returned by Google Play
	// services until the user clicks 'sign in'.
	private PendingIntent mSignInIntent;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Log.d(TAG, "onCreate()");
		setContentView(R.layout.activity_oauth_google_plus);
		mGoogleApiClient = buildGoogleApiClient();
	}

	private GoogleApiClient buildGoogleApiClient() {
		// When we build the GoogleApiClient we specify where connected and
		// connection failed callbacks should be returned, which Google APIs our
		// app uses and which OAuth 2.0 scopes our app requests.
		return new GoogleApiClient.Builder(this).addConnectionCallbacks(this)
				.addOnConnectionFailedListener(this)
				.addApi(Plus.API, Plus.PlusOptions.builder().build())
				.addScope(Plus.SCOPE_PLUS_LOGIN).build();
	}

	@Override
	protected void onStart() {
		// TODO Auto-generated method stub
		Log.d(TAG, "onStart()");
		super.onStart();
		mGoogleApiClient.connect();
	}

	@Override
	protected void onStop() {
		// TODO Auto-generated method stub
		Log.d(TAG, "onStop()");
		super.onStop();
		if (mGoogleApiClient.isConnected()) {
			mGoogleApiClient.disconnect();
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.oauth_google_plus, menu);
		return true;
	}

	@Override
	public void onConnectionFailed(ConnectionResult result) {
		// TODO Auto-generated method stub
		Log.d(TAG, "onConnectionFailed()");
		mSignInIntent = result.getResolution();
	}

	@Override
	public void onConnected(Bundle connectionHint) {
		// TODO Auto-generated method stub
		// We've resolved any connection errors. mGoogleApiClient can be used to
		// access Google APIs on behalf of the user.
		Log.d(TAG, "onConnected()");
		// Retrieve some profile information to personalize our app for the
		// user.
		Person currentUser = Plus.PeopleApi.getCurrentPerson(mGoogleApiClient);
		Log.d(TAG, "user id    :" + currentUser.getId());
		Log.d(TAG, "user name  :" + currentUser.getDisplayName());
		new Thread(new Runnable() {
			
			@Override
			public void run() {
				// TODO Auto-generated method stub
				List<NameValuePair> codeParams = new ArrayList<NameValuePair>();
				codeParams.add(new BasicNameValuePair("client_id",
						GOOGLE_CLIENT_ID));
				codeParams.add(new BasicNameValuePair("scope",
						"email profile"));
				String codeResult = new HttpUtils().doPost(GOOGLE_URL_CODE, codeParams);
				if (!TextUtils.isEmpty(codeResult) && !codeResult.startsWith(HttpUtils.ERROR_PREFIX)) {
					Gson gson = new Gson();
					GoogleCode googleCode = gson.fromJson(codeResult, GoogleCode.class);
					if (googleCode != null) {
						Log.d(TAG, "Google Device Code : " + googleCode.getDevice_code());
						List<NameValuePair> tokenParams = new ArrayList<NameValuePair>();
						tokenParams.add(new BasicNameValuePair("client_id",
								GOOGLE_CLIENT_ID));
						tokenParams.add(new BasicNameValuePair("scope",
								"email profile"));
						String tokenResult = new HttpUtils().doPost(GOOGLE_URL_CODE, tokenParams);
					}
				} else {
					Log.d(TAG, "ERROR HAPPENED!");
				}
			}
		}).start();
	}

	@Override
	public void onConnectionSuspended(int cause) {
		// TODO Auto-generated method stub
		Log.d(TAG, "onConnectionSuspended()");
		mGoogleApiClient.connect();
	}

}
