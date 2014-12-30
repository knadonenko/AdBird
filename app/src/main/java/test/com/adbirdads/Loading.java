package test.com.adbirdads;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteConstraintException;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.Menu;
import android.view.animation.AnimationSet;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


public class Loading extends Utilities implements OnTaskCompleted {
	private AdsTask mAuthTask = null;
	private RatingTask ratingTask = null;
	private ContentResolver resolver;
	private String email;
	private String password;
	private String scan;
    String stId = "";
    String instagramLogin = "";
	public String language = null;

    JSONParser jsonParser = new JSONParser();

    SharedPreferences prefs;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_loading);
        startLoadingAnimation();

		SharedPreferences myPref = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
		String myPref_text = myPref.getString("adbirdlanguage", null);
		if(myPref_text == null){
			language = "ru";
		}else{
			if(myPref_text != null){
				language = myPref_text;
			}
		}
		
		if (isNetworkAvailable()) {
			ratingTask = new RatingTask(this);
			ratingTask.execute((Void) null);
		} else {
			String noInternetConn = getString(R.string.no_internet);
			Toast.makeText(getBaseContext(), noInternetConn, Toast.LENGTH_SHORT)
					.show();
		}
		/*if (!Ads.loaded) {
			if (isNetworkAvailable()) {
				mAuthTask = new AdsTask();
				mAuthTask.execute((Void) null);
				Ads.loaded = true;
			} else {
				String noInternetConn = getString(R.string.no_internet);
				Toast.makeText(getBaseContext(), noInternetConn,
						Toast.LENGTH_SHORT).show();
			}
		}*/
		prefs = PreferenceManager.getDefaultSharedPreferences(Loading.this);
		email = prefs.getString("email", null);
		password = prefs.getString("password", null);
        stId = prefs.getString("socialID", null);
        instagramLogin = prefs.getString("instagramLogin", null);
        Log.d("EMAIL", email);
        Log.d("PASSWORD", password);
		
		TextView waiting = (TextView)this.findViewById(R.id.wait);
		TextView text = (TextView)this.findViewById(R.id.rating_calc);
		

	}

	public boolean isNetworkAvailable() {
		ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo activeNetworkInfo = connectivityManager
				.getActiveNetworkInfo();
		return activeNetworkInfo != null
				&& activeNetworkInfo.isConnectedOrConnecting();
	}

	@Override
	public void onTaskCompleted() {
		if (scan != null) {
			if (scan.equals("1")) {
				if (isNetworkAvailable()) {
					if (isNetworkAvailable()) {
						UserDetailsTask1 userDetailsTask = new UserDetailsTask1();
						userDetailsTask.execute((Void) null);
                        //new UserDetailTask().execute();
					}
				} else {
					String noInternetConn = getString(R.string.no_internet);
					Toast.makeText(getBaseContext(), noInternetConn,
							Toast.LENGTH_SHORT).show();
				}
			} else if (scan.equals("null")) {
				callRatingAgain();
			}
		} else {
			callRatingAgain();
		}
	}

	public void goToRatingPage() {
		Intent intentLoading = new Intent(getBaseContext(), Rating.class);
		startActivity(intentLoading);
	}

	@Override
	public void onBackPressed() {
	}

	protected void onStart() {
		super.onStart();
	};

	protected void onRestart() {
		super.onRestart();
	};

	public void onResume() {
		super.onResume();
	};

	public void onPause() {
		super.onPause();
	};

	public void onStop() {
		super.onStop();
	};

	protected void onDestroy() {
		super.onDestroy();
	};

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		// getMenuInflater().inflate(R.menu.loading, menu);
		return true;
	}

	private void startLoadingAnimation() {
		ImageView myImageView = (ImageView) findViewById(R.id.animation);

		AnimationSet animSet = new AnimationSet(true);
		animSet.setInterpolator(new LinearInterpolator());
		animSet.setFillAfter(true);
		animSet.setFillEnabled(true);

		final RotateAnimation animRotate = new RotateAnimation(0.0f, 360.0f,
				RotateAnimation.RELATIVE_TO_SELF, 0.5f,
				RotateAnimation.RELATIVE_TO_SELF, 0.5f);

		animRotate.setDuration(6000);
		animRotate.setFillAfter(true);
		animRotate.setRepeatCount(RotateAnimation.INFINITE);
		animSet.addAnimation(animRotate);

		myImageView.startAnimation(animSet);
	}

	public class AdsTask extends AsyncTask<Void, Void, Boolean> {
		@Override
		protected Boolean doInBackground(Void... params) {
			Boolean success = false;
			try {
				try {
					success = getAds();
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

		public Boolean getAds() throws UnsupportedEncodingException,
				JSONException {
			// Create a new HttpClient and Post Header
			HttpClient httpclient = new DefaultHttpClient();
			HttpPost httppost = new HttpPost(
					"http://api.adbird.net/index.php/offer/showToSurfer");
			List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(2);
			nameValuePairs.add(new BasicNameValuePair("email", email));
			nameValuePairs.add(new BasicNameValuePair("password", password));
			httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));

			// Execute HTTP Post Request
			try {
				HttpResponse response = httpclient.execute(httppost);
				BufferedReader reader = new BufferedReader(
						new InputStreamReader(
								response.getEntity().getContent(), "UTF-8"));

				String json = reader.readLine();
				JSONArray jsonArray = null;
				JSONObject jsonObject = null;
				try {
					jsonArray = new JSONArray(json);

				} catch (Exception je) {
					jsonObject = new JSONObject(json);

				}
				if (jsonArray != null) {
					saveAds(jsonArray);
					return true;
				} else if (jsonObject != null) {
					if (jsonObject.has("error")
							&& jsonObject.get("error").equals("email invalid")) {
					} else if (jsonObject.has("error")
							&& jsonObject.get("error").equals(
									"password invalid")) {
					} else {
						return false;
					}
				} else {
					return false;
				}

			} catch (IOException e) {
				Log.d("loading", "error execute");
				Log.d("loading", e.toString());
			}
			return false;
		}

		protected void saveAds(JSONArray ads) {
			for (int i = 0; i < ads.length(); i++) {
				try {
					this.saveAd(ads.getJSONObject(i));
				} catch (JSONException e) {
					Log.d("loading", "Not a JSON Object");
					e.printStackTrace();
				}
			}
		}

		protected void saveAd(JSONObject json) {
			resolver = getContentResolver();
			try {
				ContentValues values = new ContentValues();
				String adID = json.getString("id");
				String adPicture = json.getString("photo");
				String adText = json.getString("text");
				String adTextHash = json.getString("hashtags");
				String adTextRef = json.getString("references");

				String companyName = json.getString("companyName");
				String companyLogo = json.getString("logoImage");
				String companyUsername = "unknown";
				String adTimestamp = "dd:mm:yy";

				values.put(DatabaseHandler.KEY_AD_ID, adID);
				values.put(DatabaseHandler.KEY_PICTURE, adPicture);
				values.put(DatabaseHandler.KEY_AD_TEXT, adText);
				values.put(DatabaseHandler.KEY_AD_TEXT_HASHTAGS, adTextHash);
				values.put(DatabaseHandler.KEY_AD_TEXT_REFERENCES, adTextRef);
				values.put(DatabaseHandler.KEY_COMPANY_NAME, companyName);
				values.put(DatabaseHandler.KEY_COMPANY_LOGO, companyLogo);
				values.put(DatabaseHandler.KEY_COMPANY_USERNAME,
						companyUsername);
				values.put(DatabaseHandler.KEY_AD_TIMESTAMP, adTimestamp);
				try {
					resolver.insert(AdBirdContentProvider.CONTENT_URI_ADS,
							values);
				} catch (SQLiteConstraintException e) {
					Log.d("loading", "already exists " + e);
				}

			} catch (JSONException e) {
				e.printStackTrace();
			}
		}

		@Override
		protected void onPostExecute(final Boolean success) {
		}

		@Override
		protected void onCancelled() {
		}

	}

	public void callRatingAgain() {
		Log.d("loading", "AA inside callRatingAgain");
		if (isNetworkAvailable()) {
			ratingTask = new RatingTask(this);
			ratingTask.execute((Void) null);
		} else {
			String noInternetConn = getString(R.string.no_internet);
			Toast.makeText(getBaseContext(), noInternetConn, Toast.LENGTH_SHORT)
					.show();
		}
	}

	public class RatingTask extends AsyncTask<Void, Void, Boolean> {
		private OnTaskCompleted listener;
		private ContentResolver resolver;

		public RatingTask(OnTaskCompleted listener) {
			this.listener = listener;
		}

		@Override
		protected Boolean doInBackground(Void... params) {
			Boolean success = false;
			try {
				getRating();
				Thread.sleep(2000);
			} catch (InterruptedException e) {
				return false;
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			} catch (JSONException e) {
				e.printStackTrace();
			}
			return success;
		}

		public Boolean getRating() throws UnsupportedEncodingException,
				JSONException {

			// Create a new HttpClient and Post Header
			HttpClient httpclient = new DefaultHttpClient();
			HttpPost httppost = new HttpPost(
					"http://api.adbird.net/index.php/surfer/getRating");
			List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(2);
			nameValuePairs.add(new BasicNameValuePair("email", email));
			nameValuePairs.add(new BasicNameValuePair("password", password));
            nameValuePairs.add(new BasicNameValuePair("stId", stId));
            nameValuePairs.add(new BasicNameValuePair("login", instagramLogin));
			httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));

			// Execute HTTP Post Request

			resolver = getContentResolver();

			try {
				HttpResponse response = httpclient.execute(httppost);
				BufferedReader reader = new BufferedReader(
						new InputStreamReader(
								response.getEntity().getContent(), "UTF-8"));

				String json = reader.readLine();

				JSONObject jsonObject = new JSONObject(json);
				if (jsonObject != null) {
					ContentValues values = new ContentValues();
					if (jsonObject.has("rating")) {
						// put it into db
						scan = jsonObject.getString("scanSuccess");
						if (scan == null) {
							Log.d("loading", "ERROR SCAN SUCCESS IF NULL");
						} else if (scan.equals("0")) {
							Log.d("loading", "ERROR 0");
						} else if (scan.equals("1")) {
							Log.d("loading", "ALL GOOD SCAN SUCCESS IS 1");
							values.put(DatabaseHandler.KEY_RATING,
									jsonObject.getString("rating"));
							@SuppressWarnings("unused")
							Uri ratingInsert = resolver.insert(
									AdBirdContentProvider.CONTENT_URI_USER,
									values);
							// put it into shared prefs
							SharedPreferences prefs = getSharedPreferences(
									"rating", Context.MODE_PRIVATE);
							SharedPreferences.Editor details = prefs.edit();
							details.putString("rating",
									jsonObject.getString("rating"));
							details.putString("level",
									jsonObject.getString("level"));

							details.commit();
						}
					} else if (jsonObject.has("error")) {
						Log.d("loading",
								"error getting rating " + jsonObject.toString());
                        Toast.makeText(Loading.this, getResources().getString(R.string.errorInstagram), Toast.LENGTH_LONG).show();
					}
				}

			} catch (IOException e) {
				Log.d("loading", "error execute");
				Log.d("loading", e.toString());
			}
			Log.d("loading", "response execute");
			return false;
		}

		@Override
		protected void onPostExecute(final Boolean success) {
			listener.onTaskCompleted();
		}

		@Override
		protected void onCancelled() {
		}

	}

    public class UserDetailTask extends AsyncTask<String, String, String> {
        JSONObject jsonObject;

        @Override
        protected String doInBackground(String... strings) {

            List<NameValuePair> params = new ArrayList<NameValuePair>();
            params.add(new BasicNameValuePair("email", email));
            params.add(new BasicNameValuePair("password", password));

            jsonObject =
                    jsonParser.makeHttpRequest("http://api.adbird.net/index.php/surfer/show", "POST", params);

            Log.d("JSON", String.valueOf(jsonObject));

            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            if (jsonObject != null) {
                try {
                    if (jsonObject.has("success")
                            && jsonObject.get("success").equals("yes")) {
                        Log.d("login", "SUCCESS");
                        ContentValues values = new ContentValues();

                        //saveUserDetails(jsonString);
                        String name = jsonObject.getString("name");
                        String surname = jsonObject.getString("surname");
                        String username = jsonObject.getString("instaLogin");
                        String amount = jsonObject.getString("money");
                        String rating = jsonObject.getString("rating");
                        String ratingChange = jsonObject.getString("ratingChange");
                        String cityUpdateAvailable = jsonObject
                                .getString("cityUpdateAvailable");
                        String themesUpdateAvailable = jsonObject
                                .getString("themesUpdateAvailable");

                        String location = jsonObject.getString("city");
                        String themes = jsonObject.getString("usualCategories");
                        //JSONArray themesArray = new JSONArray(themes);
                        String full_name = name + " " + surname;
                        values.put(DatabaseHandler.KEY_NAME, full_name);
                        values.put(DatabaseHandler.KEY_USERNAME, username);
                        values.put(DatabaseHandler.KEY_AMOUNT, amount);
                        values.put(DatabaseHandler.KEY_RATING, rating);
                        values.put(DatabaseHandler.KEY_RATING_CHANGE, ratingChange);
                        values.put(DatabaseHandler.KEY_LOCATION, location);
                        values.put(DatabaseHandler.KEY_CITY_UPDATE_AVAILABLE,
                                cityUpdateAvailable);
                        values.put(DatabaseHandler.KEY_THEME_UPDATE_AVAILABLE,
                                themesUpdateAvailable);
                        // INSERTING INTO USER TABLE
                        resolver.delete(AdBirdContentProvider.CONTENT_URI_USER, null,
                                null);
                        resolver.insert(AdBirdContentProvider.CONTENT_URI_USER, values);
                    } else if (jsonObject.has("error")
                            && jsonObject.get("error").equals("email invalid")) {
                        Log.d("login", "email wrong");
                    } else if (jsonObject.has("error")
                            && jsonObject.get("error").equals(
                            "password invalid")) {
                        Log.d("login", "password wrong");
                    } else if (jsonObject.has("error")
                            && jsonObject.get("error").equals(
                            "surfer update crashes")) {
                        Log.d("login",
                                "ERROR IN GET USER DETAILS surfer update crashes");
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
    }


	public class UserDetailsTask1 extends AsyncTask<Void, Void, Boolean> {

		@Override
		protected Boolean doInBackground(Void... params) {
			Boolean success = false;
			try {
				try {
					success = getUserDetails();
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

		// GET USER DETAILS
		private Boolean getUserDetails() throws UnsupportedEncodingException,
				JSONException {

			HttpClient httpclient = new DefaultHttpClient();
			HttpPost httppost = new HttpPost(
					"http://api.adbird.net/index.php/surfer/show");
			// put values into name value pair
			List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(2);
			nameValuePairs.add(new BasicNameValuePair("email", email));
			nameValuePairs.add(new BasicNameValuePair("password", password));
			httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
			// send the request
			try {
				HttpResponse response = httpclient.execute(httppost);
				BufferedReader reader = new BufferedReader(
						new InputStreamReader(
								response.getEntity().getContent(), "UTF-8"));

				String jsonString = reader.readLine();
				JSONObject jsonObject = null;
				jsonObject = new JSONObject(jsonString);
				if (jsonObject != null) {
					if (jsonObject.has("success")
							&& jsonObject.get("success").equals("yes")) {
						Log.d("login", "SUCCESS");
						saveUserDetails(jsonString);
					} else if (jsonObject.has("error")
							&& jsonObject.get("error").equals("email invalid")) {
						Log.d("login", "email wrong " + jsonString);
						return false;
					} else if (jsonObject.has("error")
							&& jsonObject.get("error").equals(
									"password invalid")) {
						Log.d("login", "password wrong " + jsonString);
						return false;
					} else if (jsonObject.has("error")
							&& jsonObject.get("error").equals(
									"surfer update crashes")) {
						Log.d("login",
								"ERROR IN GET USER DETAILS surfer update crashes"
										+ jsonString);
						return false;
					} else {
						return false;
					}
				}

			} catch (IOException e) {
				Log.d("login", "getting data back");
				Log.d("login", e.toString());
			}
			return false;
		}

		private void saveUserDetails(String result) {
			@SuppressWarnings("unused")
			JSONObject json;
			resolver = getApplicationContext().getContentResolver();
			try {
				json = new JSONObject(result);
				ContentValues values = new ContentValues();
				ContentValues themeValues = new ContentValues();

				JSONObject user = new JSONObject(result);

				String name = user.getString("name");
				String surname = user.getString("surname");
				String username = user.getString("instaLogin");
				String amount = user.getString("money");
				String rating = user.getString("rating");
				String ratingChange = user.getString("ratingChange");
				String cityUpdateAvailable = user
						.getString("cityUpdateAvailable");
				String themesUpdateAvailable = user
						.getString("themesUpdateAvailable");

				String location = user.getString("city");
				String themes = user.getString("usualCategories");
				//JSONArray themesArray = new JSONArray(themes);
				String full_name = name + " " + surname;
				values.put(DatabaseHandler.KEY_NAME, full_name);
				values.put(DatabaseHandler.KEY_USERNAME, username);
				values.put(DatabaseHandler.KEY_AMOUNT, amount);
				values.put(DatabaseHandler.KEY_RATING, rating);
				values.put(DatabaseHandler.KEY_RATING_CHANGE, ratingChange);
				values.put(DatabaseHandler.KEY_LOCATION, location);
				values.put(DatabaseHandler.KEY_CITY_UPDATE_AVAILABLE,
						cityUpdateAvailable);
				values.put(DatabaseHandler.KEY_THEME_UPDATE_AVAILABLE,
						themesUpdateAvailable);
				// INSERTING INTO USER TABLE
				resolver.delete(AdBirdContentProvider.CONTENT_URI_USER, null,
						null);
				resolver.insert(AdBirdContentProvider.CONTENT_URI_USER, values);

                SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(Loading.this);
                SharedPreferences.Editor edit = sp.edit();
                edit.putString("rating", rating);
                edit.commit();

				/*for (int n = 0; n < themesArray.length(); n++) {
					String themeId = themesArray.getString(n);
					themeValues.put(DatabaseHandler.KEY_SELECTED, "1");
					try {
						resolver.update(
								AdBirdContentProvider.CONTENT_URI_THEMES,
								themeValues, DatabaseHandler.KEY_ID + "=?",
								new String[] { themeId });
					} catch (SQLiteException s) {
						Log.d("login", "exception " + s);
					}

				}*/
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}

		@Override
		protected void onPostExecute(final Boolean success) {
			goToRatingPage();
		}

	}
}
