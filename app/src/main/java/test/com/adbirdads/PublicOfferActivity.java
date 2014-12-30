package test.com.adbirdads;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.app.ActionBar;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.StrictMode;
import android.preference.PreferenceManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;

//import com.example.adbird.Classes.AdBirdContentProvider;
//import com.example.adbird.ParseApplication.TrackerName;
import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;
import com.parse.PushService;

public class PublicOfferActivity extends Activity{

	public String language = "";
	private UserRegisterTask mAuthTask = null;
	private String jsonError;
	public String name = "";
	public String surname = "";
	public String email = "";
	public String password  = "";
	public String sex = "";
	public String birthday = "";
	public String cityId = "";
 	public ContentResolver resolver;




 	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.public_offer_main);

		ActionBar action_bar = getActionBar();
		action_bar.setDisplayShowTitleEnabled(true);
		action_bar.setDisplayHomeAsUpEnabled(true);
		action_bar.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);

		SharedPreferences myPr = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
		String myPref_text = myPr.getString("adbirdlanguage", null);
		if(myPref_text == null){
			language = "ru";
		}else{
			if(myPref_text != null){
				language = myPref_text;
			}
		}
		
		Intent intent = getIntent(); 
		name = intent.getStringExtra("name");
		surname = intent.getStringExtra("surname");
		email = intent.getStringExtra("email");
		password = intent.getStringExtra("password");
		sex = intent.getStringExtra("sex");
		birthday = intent.getStringExtra("birthday");
		cityId = intent.getStringExtra("city");
		
		jsonError = "none";

		StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
		StrictMode.setThreadPolicy(policy);

		if(language.equals("ru")){
			action_bar.setTitle(getResources().getString(R.string.public_offer));
		}else{
			action_bar.setTitle(getResources().getString(R.string.public_offer));			
		}


	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.public_offer, menu);
		MenuItem itemYes = menu.findItem(R.id.action_yes);
	
		if(language.equals("ru")){
			itemYes.setTitle(getResources().getString(R.string.accepted));
		}else{
			itemYes.setTitle(getResources().getString(R.string.accepted));
		}

		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.action_yes:
			
			if (isNetworkAvailable()) {
				if (isNetworkAvailable()) {
					mAuthTask = new UserRegisterTask();
					mAuthTask.execute((Void) null);
				}
			} else {
				String noInternetConn = getString(R.string.no_internet);
				Toast.makeText(getBaseContext(),noInternetConn, Toast.LENGTH_SHORT).show();
			}
			return true;
		case android.R.id.home:
			super.onBackPressed();
			return true;	
		}
		return super.onOptionsItemSelected(item);
	}
	
	public class UserRegisterTask extends AsyncTask<Void, Void, Boolean> {

		private ProgressDialog dialog = new ProgressDialog(PublicOfferActivity.this);

		@Override
		protected void onPreExecute() {
			String msg = "";
			if(language.equals("ru")){
				msg = getString(R.string.please_wait_ru);
			}else{
				msg = getString(R.string.please_wait);
			}
			this.dialog.setMessage(msg);
			this.dialog.show();
		}

		@Override
		protected Boolean doInBackground(Void... params) {
			Boolean success = false;
			try {
				try {
					success = sendRegister();
				} catch (UnsupportedEncodingException e) {
					e.printStackTrace();
				} catch (JSONException e) {
					e.printStackTrace();
				}

				Thread.sleep(2000);
			} catch (InterruptedException e) {
				return false;
			}
			return success;
		}

		@Override
		protected void onPostExecute(final Boolean success) {

			if (dialog.isShowing()) {
				dialog.dismiss();
			}
			mAuthTask = null;

			if (success) {
//				Toast.makeText(getBaseContext(), "successful register",Toast.LENGTH_SHORT).show();
				setSharedDetails();
				//Intent intentRegister = new Intent(getBaseContext(),SocialNetworksActivity.class);
				//startActivity(intentRegister);
				String replaceAt = email.replace("@", "_");
				String parseEmail = replaceAt.replace(".", "_");
				PushService.subscribe(getApplicationContext(), parseEmail,Rating.class);
				finish();
			} else if (jsonError.equals("email")) {
//				email.setError(getString(R.string.error_invalid_email));
//				email.requestFocus();
//				Toast.makeText(getApplicationContext(), "Email Error", Toast.LENGTH_LONG).show();
			} else if (jsonError.equals("saving")) {
				if(language.equals("ru")){
					Toast.makeText(getApplicationContext(), getResources().getString(R.string.saving_err), Toast.LENGTH_LONG).show();
				}else{
					Toast.makeText(getBaseContext(),"Sorry we could not register you, please try later again",Toast.LENGTH_SHORT).show();
				}
			} else {
				Toast.makeText(getApplicationContext(), "err", Toast.LENGTH_LONG).show();
			}
		}

		public Boolean sendRegister() throws UnsupportedEncodingException,
		JSONException {
			HttpClient httpclient = new DefaultHttpClient();
			HttpPost httppost = new HttpPost("http://api.adbird.net/index.php/surfer/register");
			List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(2);
			nameValuePairs.add(new BasicNameValuePair("email", email));
			nameValuePairs.add(new BasicNameValuePair("password", password));
			nameValuePairs.add(new BasicNameValuePair("name", name));
			nameValuePairs.add(new BasicNameValuePair("surname", surname));
			nameValuePairs.add(new BasicNameValuePair("instaLogin","jbagda"));
			nameValuePairs.add(new BasicNameValuePair("sex",sex));
			nameValuePairs.add(new BasicNameValuePair("city", cityId));
			nameValuePairs.add(new BasicNameValuePair("birthday", birthday));
			httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs,"UTF-8"));
			try {
				HttpResponse response = httpclient.execute(httppost);
				BufferedReader reader = new BufferedReader(new InputStreamReader(response.getEntity().getContent(), "UTF-8"));
				String json = reader.readLine();
				JSONObject jsonObject = new JSONObject(json);
				if (json.equals(null)) {
					return false;
				} else if (jsonObject.has("success") && jsonObject.get("success").equals("registration")) {
					resolver = getApplicationContext().getContentResolver();
					resolver.delete(AdBirdContentProvider.CONTENT_URI_USER,null, null);
					resolver.delete(AdBirdContentProvider.CONTENT_URI_HISTORY,null, null);
					resolver.delete(AdBirdContentProvider.CONTENT_URI_ADS,null, null);
					return true;
				} else if (jsonObject.has("error") && jsonObject.getString("error").equals("email registered")) {
					jsonError = "email";
					Log.d("login", "email error");
					return false;
				} else if (jsonObject.has("error") && jsonObject.getString("error").equals("instaLogin registered")) {
					jsonError = "insta";
					Log.d("login", "insta login already registered");
					return false;
				} else if (jsonObject.has("error") && jsonObject.getString("error").equals("saving")) {
					jsonError = "saving";
					return false;
				} else if (jsonObject.has("error") && jsonObject.getString("error").equals("usualCategories have to be in JSON format")) {
					Log.d("login", "PUT THEMES IN JSON");
					return false;
				} else {
					Log.d("login", "fail " + json);
					return false;
				}
			} catch (IOException e) {
				Log.d("login", "error execute");
				Log.d("login", e.toString());
			}
			Log.d("login", "response execute");
			return false;
		}
	}
	
	protected void onStart() {
		super.onStart();
	};

	protected void onRestart() {
		super.onRestart();
	};

	public void onResume() {
		Tracker t = ((ParseApplication) getApplication()).getTracker(ParseApplication.TrackerName.APP_TRACKER);
		t.setScreenName("com.example.adbird.Register");
		t.send(new HitBuilders.AppViewBuilder().build());
		super.onResume();
	};

	public void onStop() {
		super.onStop();
	};

	protected void onDestroy() {
		super.onDestroy();
	};

	public boolean isNetworkAvailable() {
		ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
		return activeNetworkInfo != null && activeNetworkInfo.isConnectedOrConnecting();
	}
	
	private void setSharedDetails() {
		SharedPreferences prefs = this.getSharedPreferences("prefs",MODE_PRIVATE);
		SharedPreferences.Editor details = prefs.edit();
		details.putString("email", email);
		details.putString("password", password);
        details.putString("name", name);
        details.putString("surname", surname);
        details.putString("city", cityId);
		details.commit();
	}

}
