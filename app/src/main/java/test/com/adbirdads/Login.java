package test.com.adbirdads;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

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
import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.database.sqlite.SQLiteConstraintException;
import android.database.sqlite.SQLiteException;
import android.location.Geocoder;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;
import com.google.android.gms.location.LocationClient;
import com.parse.PushService;

import test.com.adbirdads.yandexlocator.LbsInfo;
import test.com.adbirdads.yandexlocator.LbsLocationListener;
import test.com.adbirdads.yandexlocator.WifiAndCellCollector;

//import com.google.android.gms.analytics.HitBuilders;
//import com.google.android.gms.analytics.Tracker;
//import com.parse.PushService;

//import adbird.com.Classes.AdBirdContentProvider;
//import adbird.com.Classes.DatabaseHandler;

public class Login extends Utilities implements OnTaskCompleted, LbsLocationListener {

	public static final String EXTRA_EMAIL = "com.example.android.authenticatordemo.extra.EMAIL";
	private UserLoginTask mAuthTask = null;
	private String mEmail;
	private String mPassword;
	private ThemesTask themeTask = null;
    private ProgressDialog pDialog;
    private AlertDialog alert;



    private static final String TAG_RESULTS = "results";

    JSONArray results = null;

	private EditText mEmailView;
	private EditText mPasswordView;
	private View mLoginFormView;
	private View mLoginStatusView;
    private ProgressDialog progressDialog;

    private Login instance;

	@SuppressWarnings("unused")
	private TextView mLoginStatusMessageView;
	public static String jsonError = "jojo";
	private String language;
	ContentResolver resolver;
	public TextView forgetPasswordText = null;
	public TextView registerText = null;
	public TextView titleText = null;
	public TextView tutorialText = null;

    // Configurators
    String email, password, myCity, myCountry;
    JSONObject json;

    LocationClient mLocationClient;

    private TextView addressLabel;
    private TextView locationLabel;
    private WifiAndCellCollector wifiAndCellCollector;

    public String counName = "";
    public String getCityName = "";
    public String intentCityName = "";
    String lat, lng;
    private ArrayList<String> countryName = new ArrayList<String>();
    private Map<String, String> countryCCFips = new HashMap<String, String>();
    private ArrayList<String> cityName = new ArrayList<String>();
    private Map<String, String> cityAndId = new HashMap<String, String>();

    Geocoder gcd;
    TextView city, country;

    String instLogin;
    String name;
    String surname;
    String rating = null;

	@SuppressLint("CutPasteId")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		SharedPreferences prefsParse = getSharedPreferences("prefs",MODE_PRIVATE);
		String emailParse = prefsParse.getString("email", null);
		String lang = prefsParse.getString("lang", null);

        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(this);
        String localization = sp.getString("locale", "ru");

        gcd = new Geocoder(Login.this, Locale.ENGLISH);
        //city = (TextView) findViewById(R.id.getLocation);
        //country = (TextView) findViewById(R.id.addressLabel);

        instance = this;
        SharedPreferences settings = getPreferences(Activity.MODE_PRIVATE);
        String uuid = settings.getString("UUID", null);
        if (uuid == null) {
            uuid = generateUUID();
            SharedPreferences.Editor edit = settings.edit();
            edit.putString("UUID", uuid);
            edit.commit();
        }
        wifiAndCellCollector = new WifiAndCellCollector(this, this, uuid);

		SharedPreferences myPref = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
		String myPref_text = myPref.getString("locale", null);
		if(myPref_text == null){
			language = "ru";
		}else{
			if(myPref_text != null){
				language = myPref_text;
			}
		}

//        if (progressDialog != null && progressDialog.isShowing()) {
//            progressDialog.hide();
//        }
//        progressDialog = ProgressDialog.show(instance, null, "Please wait");
//        progressDialog.setCanceledOnTouchOutside(false);
//        progressDialog.setCancelable(false);
//        progressDialog.show();
        (new Thread() {
            @Override
            public void run() {
                wifiAndCellCollector.requestMyLocation();
            }
        }).start();

		//gps = new GPSTracker(Login.this);
		
		SharedPreferences.Editor details = prefsParse.edit();
		details.putString("choseLang", "true");
		details.commit();

		if(emailParse != null){
			String replaceAt = emailParse.replace("@", "_");
			String parseEmail = replaceAt.replace(".", "_");
			PushService.unsubscribe(getApplicationContext(), parseEmail);
		}

		setContentView(R.layout.activity_login);
		//setupUI(findViewById(R.id.login_form));

		//titleText = (TextView)this.findViewById(R.id.share_text);
		forgetPasswordText = (TextView)this.findViewById(R.id.forgetPass);

		registerText = (TextView)this.findViewById(R.id.registerTV);
		//tutorialText = (TextView)this.findViewById(R.id.take_tour);

		//Ads.toastCount = 0;
		if (isNetworkAvailable()) {
			themeTask = new ThemesTask(this);
			themeTask.execute((Void) null);
		}else {
			String noInternetConn = getString(R.string.no_internet);
			Toast.makeText(getBaseContext(),noInternetConn,Toast.LENGTH_SHORT).show();
		}

		ImageView button = (ImageView) findViewById(R.id.sign_in_button);

		mEmail = getIntent().getStringExtra(EXTRA_EMAIL);
		mEmailView = (EditText) findViewById(R.id.email);

		mPasswordView = (EditText) findViewById(R.id.password);
		//mLoginFormView = findViewById(R.id.login_form);
		mLoginStatusView = findViewById(R.id.login_status);
		mLoginStatusMessageView = (TextView) findViewById(R.id.login_status_message);

		forgetPasswordText.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intent = new Intent(getApplicationContext(), Forgot_Password.class);
				startActivity(intent);
			}
		});

		findViewById(R.id.loginButton).setOnClickListener(
				new OnClickListener() {
					@Override
					public void onClick(View view) {
						if (attemptLogin()) {
							if (isNetworkAvailable()) {
								//mAuthTask = new UserLoginTask();
								//mAuthTask.execute((Void) null);
                                new UserLoginTask().execute();

                            } else {
								String noInternetConn = getString(R.string.no_internet);
								Toast.makeText(getBaseContext(),
										noInternetConn,
										Toast.LENGTH_SHORT).show();
							}
						} else {
							Toast.makeText(getBaseContext(), getResources().getString(R.string.cantLogin),
									Toast.LENGTH_SHORT).show();
						}
					}
				});

		registerText.setOnClickListener(
				new OnClickListener() {
					@Override
					public void onClick(View v) {
						Intent intentRegister = new Intent(getBaseContext(),Register.class);
						startActivity(intentRegister);
					}
				});


		EditText pass = (EditText) findViewById(R.id.password);
		pass.setOnEditorActionListener(new EditText.OnEditorActionListener() {
			public boolean onEditorAction(TextView v, int actionId,
					KeyEvent event) {
				if (event != null && event.getAction() != KeyEvent.ACTION_DOWN) {
					return false;
				}
				if (actionId == EditorInfo.IME_ACTION_UNSPECIFIED) {
					findViewById(R.id.sign_in_button).performClick();					
					return true;
				}
				return false;
			}

		});



	//	Ads.loaded = false;

/*		if(language.equals("ru")){
			titleText.setText(getResources().getString(R.string.share_text));
			mEmailView.setHint(getResources().getString(R.string.prompt_email));
			mPasswordView.setHint(getResources().getString(R.string.promt_password));
			//button.setBackgroundResource(R.drawable.sign_in_button_ru);
			registerText.setText(getResources().getString(R.string.title_activity_register));
			forgetPasswordText.setText(getResources().getString(R.string.forget_password));
			tutorialText.setText(getResources().getString(R.string.take_tour));
		}else{
			if(language.equals("en")){
				titleText.setText(getResources().getString(R.string.share_text));
				mEmailView.setHint(getResources().getString(R.string.prompt_email));
				mPasswordView.setHint(getResources().getString(R.string.prompt_password));
				//button.setBackgroundResource(R.drawable.sign_in_button);
				registerText.setText(getResources().getString(R.string.title_activity_register));
				forgetPasswordText.setText(getResources().getString(R.string.forget_password));
				tutorialText.setText(getResources().getString(R.string.take_tour));
			}
		}*/

	}

    @Override
    public void onLocationChange(final LbsInfo lbsInfo) {
        if(isNetworkAvailable()) {
            if (lbsInfo != null) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (progressDialog != null && progressDialog.isShowing()) {
                            progressDialog.hide();
                        }
                        if (lbsInfo.isError) {
                            if (alert != null && alert.isShowing()) {
                                alert.hide();
                            }

                            AlertDialog.Builder builder = new AlertDialog.Builder(instance);
                            builder.setMessage(lbsInfo.errorMessage)
                                    .setCancelable(false)
                                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int id) {
                                            dialog.cancel();
                                        }
                                    });
                            alert = builder.create();
                            alert.show();
                            //lbsLatitude.setText("");
                            //lbsLongtitude.setText("");
                            //lbsAltitude.setText("");
                            //lbsPrecision.setText("");
                            //lbsType.setText("");
                        } else {
                            lat = lbsInfo.lbsLatitude;
                            lng = lbsInfo.lbsLongtitude;
//                            Log.d("COORDINATES", lbsInfo.lbsLatitude);
//                            Log.d("COORDINATES", lbsInfo.lbsLongtitude);

                            try {
                                new GetGeo().execute().get(10000, TimeUnit.MILLISECONDS);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            } catch (ExecutionException e) {
                                e.printStackTrace();
                            } catch (TimeoutException e) {
                                e.printStackTrace();
                            }

                            //lbsLatitude.setText("Latitude="+lbsInfo.lbsLatitude);
                            //lbsLongtitude.setText("Longtitude="+lbsInfo.lbsLongtitude);
                            //lbsAltitude.setText("Altitude="+lbsInfo.lbsAltitude);
                            //lbsPrecision.setText("Precision="+lbsInfo.lbsPrecision);
                            //lbsType.setText("Type="+lbsInfo.lbsType);
                        }
                    }
                });
            }
        }
    }

    private class GetGeo extends AsyncTask<String, String, String> {

        String City, Country;
        String Status;
        JSONObject zero2;
        JSONObject zero;
        JSONArray address_components;

        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(Login.this);
            pDialog.setMessage(Login.this.getString(R.string.pleaseWait));
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(true);
            pDialog.show();
        }

        @Override
        protected String doInBackground(String... strings) {

            JSONParser jsonParser = new JSONParser();

            String url = "http://maps.googleapis.com/maps/api/geocode/json?latlng="
                    + lat + "," + lng + "&sensor=true&language=en";

            JSONObject json = jsonParser.getJSONFromUrl(url);
//            Log.d("URL", url);
            Log.d("ADDRESS", json.toString());

            try {
                Status = json.getString("status");
                if(Status.equals("OK")) {
                    results = json.getJSONArray(TAG_RESULTS);
                    zero = results.getJSONObject(0);
                    address_components = zero.getJSONArray("address_components");


                    for(int i = 0; i< address_components.length(); i++) {
                        zero2 = address_components.getJSONObject(i);
                        String long_name = zero2.getString("long_name");
                        JSONArray mtypes = zero2.getJSONArray("types");
                        String Type = mtypes.getString(0);

                        if (TextUtils.isEmpty(long_name) == false || !long_name.equals(null) || long_name.length() > 0 || long_name != "") {
                            if (Type.equalsIgnoreCase("street_number")) {
                                String Address1 = long_name + " ";
                            } else if (Type.equalsIgnoreCase("route")) {
                                String Address1 = "";
                                Address1 = Address1 + long_name;
                            } else if (Type.equalsIgnoreCase("sublocality")) {
                                String Address2 = long_name;
                            } else if (Type.equalsIgnoreCase("locality")) {
                                // Address2 = Address2 + long_name + ", ";
                                City = long_name;
                                myCity = City;
                                Log.d("City", City);
                            } else if (Type.equalsIgnoreCase("administrative_area_level_2")) {
                                String County = long_name;
                            } else if (Type.equalsIgnoreCase("administrative_area_level_1")) {
                                String State = long_name;
                            } else if (Type.equalsIgnoreCase("country")) {
                                Country = long_name;
                                myCountry = Country;
                                Log.d("Country", Country);
                            } else if (Type.equalsIgnoreCase("postal_code")) {
                                String PIN = long_name;
                            }
                        }
                    }

                }

            }
            catch (JSONException e) {
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            pDialog.dismiss();
            if(Status.equals("OK")) {
                //city.setText(City);
                //country.setText(Country);
                SharedPreferences cityCountry = PreferenceManager.getDefaultSharedPreferences(Login.this);
                SharedPreferences.Editor editor = cityCountry.edit();
                editor.putString("city", City);
                editor.putString("country", Country);
            } else {
                Toast.makeText(getApplicationContext(), "Check your internet connection",
                        Toast.LENGTH_LONG).show();
            }
        }
    }

	@Override
	public void onTaskCompleted() {
		Log.d("login", "ON TASK COMPLETED");
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
		//Tracker t = ((ParseApplication) getApplication()).getTracker(ParseApplication.TrackerName.APP_TRACKER);
		//t.setScreenName("test.com.adbirdads.Login");
		//t.send(new HitBuilders.AppViewBuilder().build());
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

	public boolean isNetworkAvailable() {
		ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
		return activeNetworkInfo != null && activeNetworkInfo.isConnectedOrConnecting();
	}


	public boolean attemptLogin() {

		mEmailView.setError(null);
		mPasswordView.setError(null);

		mEmail = mEmailView.getText().toString();
		mPassword = mPasswordView.getText().toString();

		boolean cancel = true;
		@SuppressWarnings("unused")
		View focusView = null;

		if (TextUtils.isEmpty(mPassword)) {
			mPasswordView.setError(null);
			focusView = mPasswordView;
			cancel = false;
		} else if (mPassword.length() < 6) {
			mPasswordView.setError(null);
			focusView = mPasswordView;
			cancel = false;
		}

		if (TextUtils.isEmpty(mEmail)) {
			mEmailView.setError(null);
			focusView = mEmailView;
			cancel = false;
		} else if (!mEmail.contains("@") || !android.util.Patterns.EMAIL_ADDRESS.matcher(mEmail).matches()) {
			mEmailView.setError(null);
			focusView = mEmailView;
			cancel = false;
		}
		return cancel;
	}

	private void setSharedDetails() {
		SharedPreferences prefs = this.getSharedPreferences("prefs",MODE_PRIVATE);
		SharedPreferences.Editor details = prefs.edit();
		details.putString("email", email);
		details.putString("password", password);
		details.putString("choseLang", "false");
		details.commit();
	}

	@TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
	private void showProgress(final boolean show) {
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
			int shortAnimTime = getResources().getInteger(android.R.integer.config_shortAnimTime);

			mLoginStatusView.setVisibility(View.VISIBLE);
			mLoginStatusView.animate().setDuration(shortAnimTime)
			.alpha(show ? 1 : 0)
			.setListener(new AnimatorListenerAdapter() {
				@Override
				public void onAnimationEnd(Animator animation) {
					mLoginStatusView.setVisibility(show ? View.VISIBLE : View.GONE);
				}
			});

			mLoginFormView.setVisibility(View.VISIBLE);
			mLoginFormView.animate().setDuration(shortAnimTime)
			.alpha(show ? 0 : 1)
			.setListener(new AnimatorListenerAdapter() {
				@Override
				public void onAnimationEnd(Animator animation) {
					mLoginFormView.setVisibility(show ? View.GONE
							: View.VISIBLE);
				}
			});
		} else {
			mLoginStatusView.setVisibility(show ? View.VISIBLE : View.GONE);
			mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
		}
	}

    public class UserLoginTask extends AsyncTask {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog=new ProgressDialog(Login.this);
            progressDialog.show();
            Log.d("CLICK", "CLICK");
        }

        @Override
        protected Object doInBackground(Object[] objects) {

            email = mEmailView.getText().toString();
            password = mPasswordView.getText().toString();
            JSONParser jsonParser = new JSONParser();

            List<NameValuePair> params = new ArrayList<NameValuePair>();
            params.add(new BasicNameValuePair("email", email));
            params.add(new BasicNameValuePair("password", password));
            params.add(new BasicNameValuePair("city", "Almaty"));
            params.add(new BasicNameValuePair("country", "Kazakhstan"));
            Log.d(myCity, myCountry);

            json = jsonParser.makeHttpRequest("http://api.adbird.net/index.php/surfer/login", "POST", params);

            Log.d("JSON", String.valueOf(json));

            return null;
        }

        @Override
        protected void onPostExecute(Object o) {
            super.onPostExecute(o);

            String error = null;

            try {
                error = json.getString("error");
            } catch (JSONException e) {
                e.printStackTrace();
            }

            if (json.has("success")) {
                new UserData().execute();
                SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(Login.this);
                SharedPreferences.Editor details = prefs.edit();
                details.putString("email", email);
                details.putString("password", password);
                //details.putString("name", );
                details.commit();
            } else if (error.equals("email invalid")) {
                Toast.makeText(Login.this, getResources().getString(R.string.wrongEmail), Toast.LENGTH_LONG).show();
            } else if (error.equals("password invalid")) {
                Toast.makeText(Login.this, getResources().getString(R.string.wrongPass), Toast.LENGTH_LONG).show();
            }
        }
    }

    public class UserData extends AsyncTask {
        JSONParser jsonParser = new JSONParser();
        JSONObject jsonObject;
        String categories = null;
        String avatar = null;

        @Override
        protected Object doInBackground(Object[] objects) {

            List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
            nameValuePairs.add(new BasicNameValuePair("email", email));
            nameValuePairs.add(new BasicNameValuePair("password", password));

            jsonObject = jsonParser.makeHttpRequest("http://api.adbird.net/index.php/surfer/show", "POST", nameValuePairs);

            Log.d("USER", jsonObject.toString());

            return null;
        }

        @Override
        protected void onPostExecute(Object o) {
            super.onPostExecute(o);
            if (jsonObject.has("success")) {

                try {
                    String name = jsonObject.getString("name");
                    String surname = jsonObject.getString("surname");
                    String money = jsonObject.getString("money");
                    String city = jsonObject.getString("city");

                    JSONArray jsonArray = jsonObject.getJSONArray("social");
                    if (jsonArray.length() != 0) {
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject socialObject = jsonArray.getJSONObject(i);
                            instLogin = socialObject.getString("login");
                            rating = socialObject.getString("rating");
                            categories = socialObject.getString("categories");
                            avatar = socialObject.getString("avatar");
                            avatar = avatar.replace("\\/", "/");

                            SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(Login.this);
                            SharedPreferences.Editor editor = sharedPreferences.edit();
                            editor.putString("name", name);
                            editor.putString("surname", surname);
                            editor.putString("money", money);
                            editor.putString("city", city);
                            editor.putString("instagramLogin", instLogin);
                            editor.putString("rating", rating);
                            editor.putString("categories", categories);
                            editor.putString("avatar", avatar);
                            editor.commit();
                            Log.d("committed", "true");
                            progressDialog.dismiss();
                            Intent intent = new Intent(Login.this, MainActivity.class);
                            startActivity(intent);
                        }
                    } else {
                        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(Login.this);
                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        editor.putString("name", name);
                        editor.putString("surname", surname);
                        editor.commit();
                        Intent intent = new Intent(Login.this, SocialNetworksActivity.class);
                        startActivity(intent);

                    }



                } catch (JSONException e) {
                    e.printStackTrace();
                }



            }
        }
    }



	public class ThemesTask extends AsyncTask<Void, Void, Boolean> {
		@SuppressWarnings("unused")
		private OnTaskCompleted listener;
		private ContentResolver resolver;

		public ThemesTask(OnTaskCompleted listener) {
			this.listener = listener;
		}

		@Override
		protected Boolean doInBackground(Void... params) {
			Boolean success = false;
			try {
				getThemes();
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

		public Boolean getThemes() throws UnsupportedEncodingException,
		JSONException {

			// Create a new HttpClient and Post Header
			HttpClient httpclient = new DefaultHttpClient();
			HttpPost httppost = new HttpPost(
					"http://api.adbird.net/index.php/server/getCategories");
			// Execute HTTP Post Request

			resolver = getApplicationContext().getContentResolver();

			try {
				HttpResponse response = httpclient.execute(httppost);
				BufferedReader reader = new BufferedReader(
						new InputStreamReader(
								response.getEntity().getContent(), "UTF-8"));

				String json = reader.readLine();
				JSONArray jsonArray = new JSONArray(json);
				ContentValues values = new ContentValues();
				// Delete the old themes
				resolver.delete(AdBirdContentProvider.CONTENT_URI_THEMES, null,
						null);
				for (int i = 0; i < jsonArray.length(); i++) {
					if ((jsonArray.getJSONObject(i)).has("id")) {
						String id = (jsonArray.getJSONObject(i)).get("id")
								.toString();
						String name = (jsonArray.getJSONObject(i)).get("name")
								.toString();
						String name_rus = (jsonArray.getJSONObject(i)).get(
								"name_rus").toString();
						values.put(DatabaseHandler.KEY_ID, id);
						values.put(DatabaseHandler.KEY_THEMES_NAME, name);
						values.put(DatabaseHandler.KEY_THEMES_NAME_RUS,
								name_rus);
						values.put(DatabaseHandler.KEY_SELECTED, "0");

						try {
							resolver.insert(
									AdBirdContentProvider.CONTENT_URI_THEMES,
									values);
						} catch (SQLiteConstraintException e) {
							Log.d("login", "already exists " + e);
						}
					}
				}

			} catch (IOException e) {
				Log.d("login", "error execute");
				Log.d("login", e.toString());
			}
			return false;
		}

		@Override
		protected void onPostExecute(final Boolean success) {
			Log.d("login", "ON POST EXECUTE");
		}

		@Override
		protected void onCancelled() {
		}
	}
    public String generateUUID() {
        UUID uuid = UUID.randomUUID();
        StringBuilder str = new StringBuilder(uuid.toString());
        int index = str.indexOf("-");
        while (index > 0) {
            str.deleteCharAt(index);
            index = str.indexOf("-");
        }
        return str.toString();
    }

    @Override
    public boolean onKeyUp(int keyCode, KeyEvent event) {


        switch (keyCode) {
            case KeyEvent.KEYCODE_ENTER :
                if (isNetworkAvailable()) {
                    //mAuthTask = new UserLoginTask();
                    //mAuthTask.execute((Void) null);
                    new UserLoginTask().execute();
                }
            default:
                return super.onKeyUp(keyCode, event);

        }

    }

    public void showNoNetwork(){
        Toast.makeText(getBaseContext(), getString(R.string.no_internet), Toast.LENGTH_SHORT).show();
    }
}