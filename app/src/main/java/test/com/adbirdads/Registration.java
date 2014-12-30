package test.com.adbirdads;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.parse.PushService;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;


public class Registration extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);

        new RegisterUserTask().execute();
    }

    class RegisterUserTask extends AsyncTask {
        ProgressDialog pDialog;
        String password;
        String email;
        JSONParser jsonParser = new JSONParser();
        JSONObject jsonObject;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(Registration.this);
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
            params.add(new BasicNameValuePair("email", "testingt11@test.com"));
            //params.add(new BasicNameValuePair("instaLogin","labuda"));
            params.add(new BasicNameValuePair("name", "Тест"));
            params.add(new BasicNameValuePair("surname", "Тест"));
            params.add(new BasicNameValuePair("sex","0"));
            params.add(new BasicNameValuePair("city", "Almaty"));
            params.add(new BasicNameValuePair("country", "Kazakhstan"));
            params.add(new BasicNameValuePair("birthday", "1990-12-12"));
            params.add(new BasicNameValuePair("password", "qwerty"));
            try {
                httppost.setEntity(new UrlEncodedFormEntity(params,"UTF-8"));
                HttpResponse response = httpclient.execute(httppost);
                BufferedReader reader = new BufferedReader(new InputStreamReader(response.getEntity().getContent(), "UTF-8"));
                String json = reader.readLine();
                JSONObject jsonObject = new JSONObject(json);
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            } catch (ClientProtocolException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

            //jsonObject = jsonParser.makeHttpRequest("http://api.adbird.com/index.php/surfer/register", "POST", params);

//            Log.d("JSON", jsonObject.toString());

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
                PushService.subscribe(getApplicationContext(), parseEmail, Rating.class);
                SharedPreferences subscribedPrefs = PreferenceManager.getDefaultSharedPreferences(Registration.this);
                SharedPreferences.Editor editor = subscribedPrefs.edit();
                editor.putString("subscribed", "true");
                editor.commit();

            } else {

            }
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.registration, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
