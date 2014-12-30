package test.com.adbirdads;

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
import android.os.Bundle;
import android.os.StrictMode;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;
import com.parse.PushService;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

public class PublicOffer extends Activity {
    private WebView browser;

    public String name = "";
    public String surname = "";
    public String email = "";
    public String password  = "";
    public String sex = "";
    public String birthday = "";
    public String cityId = "";
    public String country = "";

    Button button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_public_offer);

        ActionBar action_bar = getActionBar();
        action_bar.setDisplayShowTitleEnabled(true);
        action_bar.setDisplayHomeAsUpEnabled(true);
        action_bar.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);

        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(PublicOffer.this);
        SharedPreferences.Editor editor = sp.edit();
        editor.putString("registered", "true");
        editor.commit();

        Intent intent = getIntent();
        name = intent.getStringExtra("name");
        surname = intent.getStringExtra("surname");
        email = intent.getStringExtra("email");
        password = intent.getStringExtra("password");
        sex = intent.getStringExtra("sex");
        birthday = intent.getStringExtra("birthday");
        cityId = intent.getStringExtra("city");
        country = intent.getStringExtra("country");

        if(country == null) {
            country = "Kazakhstan";
        }
        if (cityId == null) {
            cityId = "Almaty";
        }

//        Log.d("BIRTHDAY", birthday);
//        Log.d("Country", country);
//        Log.d("City", cityId);

        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(PublicOffer.this);
        email = prefs.getString("email", null);
        password = prefs.getString("password", null);
        Log.d("EMAIL", email);
        Log.d("PASSWORD", password);

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        button = (Button) findViewById(R.id.buttonAgree);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isNetworkAvailable()) {
                    if (isNetworkAvailable()) {
                        new RegisterUserTask().execute();
                        //new UserData().execute();
                    }
                } else {
                    String noInternetConn = getString(R.string.no_internet);
                    Toast.makeText(getBaseContext(),noInternetConn, Toast.LENGTH_SHORT).show();
                }
            }
        });
        browser = (WebView) findViewById(R.id.webView);
        browser.setWebViewClient(new MyBrowser());
        openURL();
    }

    public void openURL() {
        String url = "http://customer.adbird.kz/index.php/customer/leaderAgreement";
        browser.getSettings().setLoadsImagesAutomatically(true);
        browser.getSettings().setJavaScriptEnabled(true);
        browser.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);
        browser.loadUrl(url);
    }

    private class MyBrowser extends WebViewClient {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            view.loadUrl(url);
            return true;
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        switch (item.getItemId()) {
            case R.id.action_yes:

                if (isNetworkAvailable()) {
                    if (isNetworkAvailable()) {
                        //mAuthTask = new UserRegisterTask();
                        //mAuthTask.execute((Void) null);

                    }
                } else {
                    String noInternetConn = getString(R.string.no_internet);
                    Toast.makeText(getBaseContext(), noInternetConn, Toast.LENGTH_SHORT).show();
                }
                return true;
            case android.R.id.home:
                super.onBackPressed();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    //проверка интернета
    public boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnectedOrConnecting();
    }

    class RegisterUserTask extends AsyncTask {
        ProgressDialog pDialog;
        JSONParser jsonParser = new JSONParser();
        JSONObject jsonObject;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(PublicOffer.this);
            pDialog.setMessage(getResources().getString(R.string.pleaseWait));
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(true);
            pDialog.show();
        }

        @Override
        protected Object doInBackground(Object[] objects) {

            HttpClient httpclient = new DefaultHttpClient();
            HttpPost httppost = new HttpPost("http://api.adbird.net/index.php/surfer/register");
            List<NameValuePair> params = new ArrayList<NameValuePair>();
            params.add(new BasicNameValuePair("email", email));
            //params.add(new BasicNameValuePair("instaLogin","labuda"));
            params.add(new BasicNameValuePair("name", name));
            params.add(new BasicNameValuePair("surname", surname));
            params.add(new BasicNameValuePair("sex",sex));
            params.add(new BasicNameValuePair("city", cityId));
            params.add(new BasicNameValuePair("country", country));
            params.add(new BasicNameValuePair("birthday", birthday));
            params.add(new BasicNameValuePair("password", password));
            Log.d("PARAMS", String.valueOf(params));

            try {
                httppost.setEntity(new UrlEncodedFormEntity(params,"UTF-8"));
                HttpResponse response = httpclient.execute(httppost);
                BufferedReader reader = new BufferedReader(new InputStreamReader(response.getEntity().getContent(), "UTF-8"));
                String json = reader.readLine();
                jsonObject = new JSONObject(json);
                Log.d("JSON", jsonObject.toString());
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            } catch (ClientProtocolException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(Object o) {
            super.onPostExecute(o);
            pDialog.dismiss();
            if (jsonObject.has("success")) {

                Intent intentRegister = new Intent(getBaseContext(),SocialNetworksActivity.class);
                startActivity(intentRegister);
                String replaceAt = email.replace("@", "_");
                String parseEmail = replaceAt.replace(".", "_");
                PushService.subscribe(getApplicationContext(), parseEmail,Rating.class);

            } else {

            }
        }
    }
    /*public class UserRegisterTask extends AsyncTask<Void, Void, Boolean> {

        private ProgressDialog dialog = new ProgressDialog(PublicOffer.this);

        @Override
        protected void onPreExecute() {
            String msg = "";
            dialog = new ProgressDialog(PublicOffer.this);
            dialog.setMessage(PublicOffer.this.getString(R.string.pleaseWait));
            dialog.setIndeterminate(false);
            dialog.setCancelable(true);
            dialog.show();
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
                //PushService.subscribe(getApplicationContext(), parseEmail,Rating.class);
                finish();
            } else if (jsonError.equals("email")) {
//				email.setError(getString(R.string.error_invalid_email));
//				email.requestFocus();
//				Toast.makeText(getApplicationContext(), "Email Error", Toast.LENGTH_LONG).show();
            } else if (jsonError.equals("saving")) {
                //if(language.equals("ru")){
                  //  Toast.makeText(getApplicationContext(), getResources().getString(R.string.saving_err_ru), Toast.LENGTH_LONG).show();
                //}else{
                    Toast.makeText(getBaseContext(),"Sorry we could not register you, please try again later",Toast.LENGTH_SHORT).show();
                    Log.d("JSON", jsonError.toString());
                //}
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
    }*/

    protected void onStart() {
        super.onStart();
    };

    protected void onRestart() {
        super.onRestart();
    };

    public void onResume() {
        Tracker t = ((ParseApplication) getApplication()).getTracker(ParseApplication.TrackerName.APP_TRACKER);
        t.setScreenName("test.com.adbirdads.Register");
        t.send(new HitBuilders.AppViewBuilder().build());
        super.onResume();
    };

    public void onStop() {
        super.onStop();
    };

    protected void onDestroy() {
        super.onDestroy();
    };

    private void setSharedDetails() {
        SharedPreferences prefs = this.getSharedPreferences("prefs",MODE_PRIVATE);
        SharedPreferences.Editor details = prefs.edit();
        details.putString("email", email);
        details.putString("password", password);
        details.commit();
    }

}
