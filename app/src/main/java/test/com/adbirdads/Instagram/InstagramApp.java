package test.com.adbirdads.Instagram;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONObject;
import org.json.JSONTokener;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Message;
import android.preference.PreferenceManager;
import android.util.Log;

import test.com.adbirdads.JSONParser;

public class InstagramApp {

    private InstagramSession mSession;
    private InstagramDialog mDialog;
    private OAuthAuthenticationListener mListener;
    private ProgressDialog mProgress;
    private String mAuthUrl;
    private String mTokenUrl;
    private String mAccessToken;
    private String email, password, instaLogin;
    private Context mCtx;

    private String mClientId;
    private String mClientSecret;
    boolean alreadyRegistered;


    private static int WHAT_FINALIZE = 0;
    private static int WHAT_ERROR = 1;
    private static int WHAT_FETCH_INFO = 2;
    public Context context = null;
    public static String mCallbackUrl = "";
    private static final String AUTH_URL = "https://api.instagram.com/oauth/authorize/";
    private static final String TOKEN_URL = "https://api.instagram.com/oauth/access_token";
    private static final String API_URL = "https://api.instagram.com/v1";

    private static final String TAG = "InstagramAPI";
    public String language = "";

    public InstagramApp(Context context, String clientId, String clientSecret,
                        String callbackUrl) {
        this.context = context;
        mClientId = clientId;
        mClientSecret = clientSecret;
        mCtx = context;
        mSession = new InstagramSession(context);
        mAccessToken = mSession.getAccessToken();
        mCallbackUrl = callbackUrl;
        mTokenUrl = TOKEN_URL + "?client_id=" + clientId + "&client_secret="
                + clientSecret + "&redirect_uri=" + mCallbackUrl + "&grant_type=authorization_code";
        mAuthUrl = AUTH_URL + "?client_id=" + clientId + "&redirect_uri="
                + mCallbackUrl + "&response_type=code&display=touch&scope=likes+comments+relationships";

        InstagramDialog.OAuthDialogListener listener = new InstagramDialog.OAuthDialogListener() {
            @Override
            public void onComplete(String code) {
                getAccessToken(code);
            }

            @Override
            public void onError(String error) {
                mListener.onFail("Authorization failed");
            }
        };

        SharedPreferences myPref = PreferenceManager.getDefaultSharedPreferences(context);
        String myPref_text = myPref.getString("adbirdlanguage", null);
        if (myPref_text == null) {
            language = "ru";
        } else {
            if (myPref_text != null) {
                language = myPref_text;
            }
        }

        mDialog = new InstagramDialog(context, mAuthUrl, listener);
        mProgress = new ProgressDialog(context);
        mProgress.setCancelable(false);
    }

    private void getAccessToken(final String code) {
        /*if(language.equals("ru")){
			mProgress.setMessage(context.getResources().getString(R.string.loading_ru));
		}else{
			mProgress.setMessage(context.getResources().getString(R.string.loading));
		}*/
        mProgress.show();

        new Thread() {
            @Override
            public void run() {
                Log.i(TAG, "Getting access token");
                int what = WHAT_FETCH_INFO;
                try {
                    URL url = new URL(TOKEN_URL);
                    //URL url = new URL(mTokenUrl + "&code=" + code);
                    Log.i(TAG, "Opening Token URL " + url.toString());
                    HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
                    urlConnection.setRequestMethod("POST");
                    urlConnection.setDoInput(true);
                    urlConnection.setDoOutput(true);
                    //urlConnection.connect();
                    OutputStreamWriter writer = new OutputStreamWriter(urlConnection.getOutputStream());
                    writer.write("client_id=" + mClientId +
                            "&client_secret=" + mClientSecret +
                            "&grant_type=authorization_code" +
                            "&redirect_uri=" + mCallbackUrl +
                            "&code=" + code);
                    writer.flush();
                    String response = streamToString(urlConnection.getInputStream());
                    Log.i(TAG, "response " + response);
                    JSONObject jsonObj = (JSONObject) new JSONTokener(response).nextValue();

                    mAccessToken = jsonObj.getString("access_token");
                    Log.i(TAG, "Got access token: " + mAccessToken);

                    String id = jsonObj.getJSONObject("user").getString("id");
                    String user = jsonObj.getJSONObject("user").getString("username");
                    String name = jsonObj.getJSONObject("user").getString("full_name");

                    Log.d("USER", user);
                    SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
                    SharedPreferences.Editor edit = sp.edit();
                    edit.putString("instagramLogin", user);
                    edit.commit();

                    sp = PreferenceManager.getDefaultSharedPreferences(context);
                    email = sp.getString("email", null);
                    password = sp.getString("password", null);
                    instaLogin = user;
                    Log.d("email", email);
                    Log.d("password", password);

                    new UpdateInstagram().execute();

                    mSession.storeAccessToken(mAccessToken, id, user, name);

                } catch (Exception ex) {
                    what = WHAT_ERROR;
                    ex.printStackTrace();
                }

                mHandler.sendMessage(mHandler.obtainMessage(what, 1, 0));
            }
        }.start();
    }

    public class UpdateInstagram extends AsyncTask<Void, Void, Void> {
        JSONParser jsonParser = new JSONParser();
        JSONObject jsonObject;
        boolean reg;

        @Override
        protected Void doInBackground(Void... voids) {

            List<NameValuePair> params = new ArrayList<NameValuePair>();
            params.add(new BasicNameValuePair("email", email));
            params.add(new BasicNameValuePair("password", password));
            params.add(new BasicNameValuePair("login", instaLogin));
            params.add(new BasicNameValuePair("stId", "1"));
            jsonObject = jsonParser.makeHttpRequest("http://api.adbird.com/index.php/surfer/activateSocial", "POST", params);

            Log.d("JOBJECT", jsonObject.toString());

            SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(mCtx);
            SharedPreferences.Editor ed = preferences.edit();
            ed.putBoolean("alreadyREG", jsonObject.has("lastRatingUpdate"));
            ed.putString("socialized", "yes");
            ed.commit();

            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);

        }
    }

    private void fetchUserName() {
		/*if(language.equals("ru")){
			mProgress.setMessage(context.getResources().getString(R.string.finilizing_ru));
		}else{
			mProgress.setMessage(context.getResources().getString(R.string.finilizing));
		}*/

        new Thread() {
            @Override
            public void run() {
                Log.i(TAG, "Fetching user info");
                int what = WHAT_FINALIZE;
                try {
                    URL url = new URL(API_URL + "/users/" + mSession.getId() + "/?access_token=" + mAccessToken);

                    Log.d(TAG, "Opening URL " + url.toString());
                    HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
                    urlConnection.setRequestMethod("GET");
                    urlConnection.setDoInput(true);
                    urlConnection.setDoOutput(true);
                    urlConnection.connect();
                    String response = streamToString(urlConnection.getInputStream());
                    System.out.println(response);
                    JSONObject jsonObj = (JSONObject) new JSONTokener(response).nextValue();
                    String name = jsonObj.getJSONObject("data").getString("full_name");
                    String bio = jsonObj.getJSONObject("data").getString("bio");
                    Log.i(TAG, "Got name: " + name + ", bio [" + bio + "]");
                } catch (Exception ex) {
                    what = WHAT_ERROR;
                    ex.printStackTrace();
                }

                mHandler.sendMessage(mHandler.obtainMessage(what, 2, 0));
                showResponseDialog(getUserName(), "1232435435");
            }
        }.start();

    }


    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == WHAT_ERROR) {
                mProgress.dismiss();
                if (msg.arg1 == 1) {
                    mListener.onFail("Failed to get access token");
                } else if (msg.arg1 == 2) {
                    mListener.onFail("Failed to get user information");
                }
            } else if (msg.what == WHAT_FETCH_INFO) {
                fetchUserName();
            } else {
                mProgress.dismiss();
                mListener.onSuccess();
            }
        }
    };

    public boolean hasAccessToken() {
        return (mAccessToken == null) ? false : true;
    }

    public void setListener(OAuthAuthenticationListener listener) {
        mListener = listener;
    }

    public String getUserName() {
        return mSession.getUsername();
    }

    public String getId() {
        return mSession.getId();
    }

    public String getName() {
        return mSession.getName();
    }

    public void authorize() {
        //Intent webAuthIntent = new Intent(Intent.ACTION_VIEW);
        //webAuthIntent.setData(Uri.parse(AUTH_URL));
        //mCtx.startActivity(webAuthIntent);
        mDialog.show();
    }

    private String streamToString(InputStream is) throws IOException {
        String str = "";

        if (is != null) {
            StringBuilder sb = new StringBuilder();
            String line;

            try {
                BufferedReader reader = new BufferedReader(
                        new InputStreamReader(is));

                while ((line = reader.readLine()) != null) {
                    sb.append(line);
                }

                reader.close();
            } finally {
                is.close();
            }

            str = sb.toString();
        }

        return str;
    }

    public void resetAccessToken() {
        if (mAccessToken != null) {
            mSession.resetAccessToken();
            mAccessToken = null;
        }
    }

    public void showResponseDialog(String name, String accessToken) {
        Intent broadcastIntent = new Intent();
        broadcastIntent.setAction(MainActivity.ResponseListener.ACTION_RESPONSE);
        broadcastIntent.putExtra(MainActivity.ResponseListener.EXTRA_NAME, name);
        broadcastIntent.putExtra(MainActivity.ResponseListener.EXTRA_ACCESS_TOKEN, accessToken);
        context.sendBroadcast(broadcastIntent);
    }

    public interface OAuthAuthenticationListener {
        public abstract void onSuccess();

        public abstract void onFail(String error);
    }
}